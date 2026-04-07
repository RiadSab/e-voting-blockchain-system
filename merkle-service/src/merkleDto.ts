import { z } from "zod"


export const ProofReq = z.object({
  semaphoreAddress: z.string().startsWith("0x"),
  groupId: z.string(),
  identityCommitment: z.string()
})

export const SnapshotReq = z.object({
  semaphoreAddress: z.string().startsWith("0x"),
  groupId: z.string(),
  depth: z.union([z.string(), z.number()]),
  fromBlock: z.union([z.string(), z.number()]).optional().default("0"),
  toBlock: z.union([z.string(), z.number()])
})

export type Snapshot = {
  chainId: number
  semaphoreAddress: string
  groupId: string
  depth: number
  snapshotBlock: number
  root: string
  members: string[]
}