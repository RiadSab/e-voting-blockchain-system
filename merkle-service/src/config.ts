import { ethers } from "ethers"
import { Pool } from "pg"

export const CONFIG = {
  RPC_URL: process.env.RPC_URL ?? "http://127.0.0.1:8545",
  CHAIN_ID: Number(process.env.CHAIN_ID ?? 31337),
  PORT: Number(process.env.PORT ?? 4000),
  DB_URL: process.env.DATABASE_URL ?? "postgresql://evote:evote123@localhost:5432/evote"
}

export const provider = new ethers.JsonRpcProvider(CONFIG.RPC_URL, CONFIG.CHAIN_ID, {
  staticNetwork: true
})

export const dbPool = new Pool({ connectionString: CONFIG.DB_URL })

export const iface = new ethers.Interface([
  "event MemberAdded(uint256 indexed groupId, uint index,uint256 identityCommitment, uint256 merkleTreeRoot)"
])
