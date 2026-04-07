import { Pool } from "pg"
import { Snapshot } from "./merkleDto"
import { CONFIG } from "./config"

export class SnapshotRepository {
  private pool: Pool
  constructor(pool: Pool) {
    this.pool = pool
  }

  async initDB() {
    const query = `
            CREATE TABLE IF NOT EXISTS merkle_snapshots (
                chain_id INT,
                semaphore_address TEXT,
                group_id TEXT,
                depth INT,
                snapshot_block INT,
                root TEXT,
                members TEXT[],
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (chain_id, semaphore_address, group_id)
                );
            `
    try {
      await this.pool.query(query)
      console.log("Database table 'merkle_snapshots' verified.")
    } catch (err) {
      console.error("Database initialization failed:", err)
      process.exit(1)
    }
  }

  async loadSnapshot(semaphoreAddress: string, groupId: string): Promise<Snapshot | null> {
    const query = `
    SELECT * FROM merkle_snapshots
    WHERE chain_id = $1 AND semaphore_address = $2 AND group_id = $3
  `
    const values = [CONFIG.CHAIN_ID, semaphoreAddress.toLowerCase(), groupId]

    try {
      const res = await this.pool.query(query, values)
      if (res.rows.length === 0) return null

      const row = res.rows[0]
      return {
        chainId: Number(row.chain_id),
        semaphoreAddress: String(row.semaphore_address),
        groupId: String(row.group_id),
        depth: Number(row.depth),
        snapshotBlock: Number(row.snapshot_block),
        root: String(row.root),
        members: (row.members ?? []) as string[]
      }
    } catch (err) {
      console.error("DB Load Error:", err)
      return null
    }
  }

  async saveSnapshot(snapshot: Snapshot) {
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
        `

    const values = [
      snapshot.chainId,
      snapshot.semaphoreAddress.toLowerCase(),
      snapshot.groupId,
      snapshot.depth,
      snapshot.snapshotBlock,
      snapshot.root,
      snapshot.members
    ]

    await this.pool.query(query, values)
  }
}
