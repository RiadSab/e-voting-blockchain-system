import { writeFileSync } from "fs";
import { resolve, dirname } from "path";
import { fileURLToPath } from "url";
import { Identity } from "@semaphore-protocol/identity";
import { Group } from "@semaphore-protocol/group";
import { generateProof } from "@semaphore-protocol/proof";
import { AbiCoder, keccak256 } from "ethers";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const ELECTION_ID = 1n;
const CANDIDATES_COUNT = 2;

const identity = new Identity("test-identity");
const members = [1n, identity.commitment];

const group = new Group(members);

const c1x = [1n, 2n];
const c1y = [11n, 12n];
const c2x = [21n, 22n];
const c2y = [31n, 32n];

const abi = AbiCoder.defaultAbiCoder();
const encoded = abi.encode(
  ["uint256", "uint256[]", "uint256[]", "uint256[]", "uint256[]"],
  [ELECTION_ID, c1x, c1y, c2x, c2y],
);
const message = BigInt(keccak256(encoded)) >> 8n;
const scope = ELECTION_ID;

const proof = await generateProof(identity, group, message, scope, 1);

const fixture = {
  electionId: ELECTION_ID.toString(),
  identityCommitment: identity.commitment.toString(),
  members: members.map((value) => value.toString()),
  c1x: c1x.map((value) => value.toString()),
  c1y: c1y.map((value) => value.toString()),
  c2x: c2x.map((value) => value.toString()),
  c2y: c2y.map((value) => value.toString()),
  message: message.toString(),
  scope: scope.toString(),
  merkleTreeDepth: proof.merkleTreeDepth.toString(),
  merkleTreeRoot: proof.merkleTreeRoot.toString(),
  nullifier: proof.nullifier.toString(),
  points: proof.points.map((value) => value.toString()),
};

const outPath = resolve(
  __dirname,
  "..",
  "..",
  "test",
  "fixtures",
  "semaphore-proof.json",
);
writeFileSync(outPath, JSON.stringify(fixture, null, 2));

console.log(`Wrote proof fixture to ${outPath}`);
