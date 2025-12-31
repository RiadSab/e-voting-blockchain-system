import express from "express";
import { z } from "zod";
import { ethers } from "ethers";
import { Group } from "@semaphore-protocol/group";
import { Pool } from "pg"; 

// ---- Types ----
type Snapshot = {
  chainId: number;
  semaphoreAddress: string;
  groupId: string;
  depth: number;
  snapshotBlock: number;
  root: string;
  members: string[]; // Native Array
};

// ---- Config ----
const app = express();
app.use(express.json({ limit: "50mb" })); // Keeps express.json for handling incoming HTTP body

const RPC_URL = process.env.RPC_URL ?? "http://127.0.0.1:8545";
const CHAIN_ID = Number(process.env.CHAIN_ID ?? 31337);
const PORT = Number(process.env.PORT ?? 4000);

// Connect to the 'evote' database inside 'app-db' container
const DB_CONNECTION_STRING = process.env.DATABASE_URL ?? "postgresql://evote:evote123@localhost:5432/evote";

const provider = new ethers.JsonRpcProvider(RPC_URL, CHAIN_ID, { staticNetwork: true });
const iface = new ethers.Interface([
  "event MemberAdded(uint256 indexed groupId, uint256 identityCommitment, uint256 merkleTreeRoot)"
]);

// ---- Database Setup ----

const pool = new Pool({
  connectionString: DB_CONNECTION_STRING,
});

async function initDB() {
  const query = `
    CREATE TABLE IF NOT EXISTS merkle_snapshots (
      chain_id INT,
      semaphore_address TEXT,
      group_id TEXT,
      depth INT,
      snapshot_block INT,
      root TEXT,
      members TEXT[],  -- CHANGED: Using native Postgres Array
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (chain_id, semaphore_address, group_id)
    );
  `;
  try {
    await pool.query(query);
    console.log("Database table 'merkle_snapshots' verified (using TEXT[]).");
  } catch (err) {
    console.error("Database initialization failed:", err);
    process.exit(1);
  }
}

// ---- Helpers ----

async function loadSnapshot(semaphoreAddress: string, groupId: string): Promise<Snapshot | null> {
  const query = `
    SELECT * FROM merkle_snapshots 
    WHERE chain_id = $1 AND semaphore_address = $2 AND group_id = $3
  `;
  const values = [CHAIN_ID, semaphoreAddress.toLowerCase(), groupId];

  try {
    const res = await pool.query(query, values);
    if (res.rows.length === 0) return null;

    const row = res.rows[0];
    return {
      chainId: row.chain_id,
      semaphoreAddress: row.semaphore_address,
      groupId: row.group_id,
      depth: row.depth,
      snapshotBlock: row.snapshot_block,
      root: row.root,
      members: row.members // Postgres driver automatically converts TEXT[] -> JS Array
    };
  } catch (err) {
    console.error("DB Load Error:", err);
    return null;
  }
}

async function saveSnapshot(snapshot: Snapshot) {
  const query = `
    INSERT INTO merkle_snapshots (chain_id, semaphore_address, group_id, depth, snapshot_block, root, members)
    VALUES ($1, $2, $3, $4, $5, $6, $7)
    ON CONFLICT (chain_id, semaphore_address, group_id)
    DO UPDATE SET
      depth = EXCLUDED.depth,
      snapshot_block = EXCLUDED.snapshot_block,
      root = EXCLUDED.root,
      members = EXCLUDED.members,
      created_at = CURRENT_TIMESTAMP;
  `;
  
  const values = [
    snapshot.chainId,
    snapshot.semaphoreAddress.toLowerCase(),
    snapshot.groupId,
    snapshot.depth,
    snapshot.snapshotBlock,
    snapshot.root,
    snapshot.members // CHANGED: Passing raw array, no JSON.stringify
  ];

  await pool.query(query, values);
}

function getPathIndices(index: number, depth: number): number[] {
  const indices: number[] = [];
  for (let i = 0; i < depth; i++) {
    indices.push((index >> i) & 1);
  }
  return indices;
}

// ---- Group Management ----

const treeCache = new Map<string, { group: Group; snapshot: Snapshot; indexByCommitment: Map<string, number> }>();

function cacheKey(addr: string, gid: string) {
  return `${addr.toLowerCase()}:${gid}`;
}

function buildGroupFromMembers(members: string[]) {
  // V4: Group constructor only needs members
  const group = new Group(members);
  
  const indexByCommitment = new Map<string, number>();
  members.forEach((m, i) => {
    indexByCommitment.set(m, i);
  });

  return { group, indexByCommitment };
}

// ---- Endpoints ----

const SnapshotReq = z.object({
  semaphoreAddress: z.string().startsWith("0x"),
  groupId: z.string(),
  depth: z.number().int().positive(),
  fromBlock: z.number().int().nonnegative().optional().default(0),
  toBlock: z.number().int().nonnegative()
});

app.post("/internal/groups/snapshot", async (req, res) => {
  const parsed = SnapshotReq.safeParse(req.body);
  if (!parsed.success) return res.status(400).json(parsed.error.flatten());

  const { semaphoreAddress, groupId, depth, fromBlock, toBlock } = parsed.data;

  try {
    const topic0 = ethers.id("MemberAdded(uint256,uint256,uint256)");
    const topic1 = ethers.zeroPadValue(ethers.toBeHex(BigInt(groupId)), 32);

    const logs = await provider.getLogs({
      address: semaphoreAddress,
      topics: [topic0, topic1],
      fromBlock,
      toBlock
    });

    logs.sort((a, b) => {
      if (a.blockNumber !== b.blockNumber) return a.blockNumber - b.blockNumber;
      return a.index - b.index;
    });

    const members: string[] = [];
    let lastRoot: string | null = null;

    for (const log of logs) {
      const parsedLog = iface.parseLog(log);
      if (parsedLog) {
        members.push(parsedLog.args.identityCommitment.toString());
        lastRoot = parsedLog.args.merkleTreeRoot.toString();
      }
    }

    const { group, indexByCommitment } = buildGroupFromMembers(members);
    const rootToStore = lastRoot ?? group.root.toString();

    const snapshot: Snapshot = {
      chainId: CHAIN_ID,
      semaphoreAddress,
      groupId,
      depth,
      snapshotBlock: toBlock,
      root: rootToStore,
      members
    };

    await saveSnapshot(snapshot);

    treeCache.set(cacheKey(semaphoreAddress, groupId), { group, snapshot, indexByCommitment });

    return res.json({
      root: rootToStore,
      membersCount: members.length,
      snapshotBlock: toBlock
    });

  } catch (err: any) {
    console.error(err);
    return res.status(500).json({ error: err.message });
  }
});

const ProofReq = z.object({
  semaphoreAddress: z.string().startsWith("0x"),
  groupId: z.string(),
  commitment: z.string()
});

app.post("/internal/groups/merkle-proof", async (req, res) => {
  const parsed = ProofReq.safeParse(req.body);
  if (!parsed.success) return res.status(400).json(parsed.error.flatten());

  const { semaphoreAddress, groupId, commitment } = parsed.data;
  const key = cacheKey(semaphoreAddress, groupId);
  let cached = treeCache.get(key);

  if (!cached) {
    const snap = await loadSnapshot(semaphoreAddress, groupId);
    if (!snap) return res.status(404).json({ error: "Snapshot not found" });
    
    const built = buildGroupFromMembers(snap.members);
    cached = { ...built, snapshot: snap };
    treeCache.set(key, cached);
  }

  const idx = cached.indexByCommitment.get(commitment);
  if (idx === undefined) return res.status(404).json({ error: "Commitment not found" });

  const proof = cached.group.generateMerkleProof(idx);
  const pathIndices = getPathIndices(proof.index, cached.snapshot.depth);

  return res.json({
    depth: cached.snapshot.depth,
    root: cached.group.root.toString(),
    leafIndex: idx,
    siblings: proof.siblings.map((s) => s.toString()),
    pathIndices: pathIndices 
  });
});

app.listen(PORT, async () => {
  await initDB();
  console.log(`Merkle service listening on :${PORT}`);
});