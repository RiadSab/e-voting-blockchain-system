import express, { Express } from "express"
import { ProofReq, SnapshotReq } from "./merkleDto"
import { MerkleService } from "./service"

export class MerkleController {
  private app: Express
  private service: MerkleService

  constructor(service: MerkleService) {
    this.app = express()
    this.app.use(express.json({ limit: "50mb" }))
    this.service = service

    this.registerRoutes()
  }

  public getApp(): Express {
    return this.app
  }

  private registerRoutes() {
    this.app.post("/internal/groups/snapshot", async (req, res) => {
      const parsed = SnapshotReq.safeParse(req.body)
      if (!parsed.success) return res.status(400).json(parsed.error.flatten())

      const { semaphoreAddress, groupId } = parsed.data
      const depth = Number(parsed.data.depth)
      const fromBlock = Number(parsed.data.fromBlock)
      const toBlock = Number(parsed.data.toBlock)

      if (!Number.isFinite(depth) || depth <= 0)
        return res.status(400).json({ error: "Invalid depth" })
      if (!Number.isFinite(fromBlock) || fromBlock < 0)
        return res.status(400).json({ error: "Invalid fromBlock" })
      if (!Number.isFinite(toBlock) || toBlock < 0)
        return res.status(400).json({ error: "Invalid toBlock" })
      if (fromBlock > toBlock)
        return res.status(400).json({ error: "fromBlock must be <= toBlock" })

      try {
        const result = await this.service.createSnapshot({
          semaphoreAddress,
          groupId,
          depth,
          fromBlock,
          toBlock
        })
        return res.json(result)
      } catch (err: any) {
        const details = err?.details
        const msg = err?.message ?? "Internal error"
        if (details) return res.status(500).json({ error: msg, details })
        return res.status(500).json({ error: msg })
      }
    })

    this.app.post("/internal/groups/merkle-proof", async (req, res) => {
      const parsed = ProofReq.safeParse(req.body)
      if (!parsed.success) return res.status(400).json(parsed.error.flatten())

      const { semaphoreAddress, groupId } = parsed.data
      const identityCommitment = parsed.data.identityCommitment

      try {
        const proof = await this.service.getMerkleProof(
          semaphoreAddress,
          groupId,
          identityCommitment
        )
        return res.json(proof)
      } catch (err: any) {
        const msg = err?.message ?? "Internal error"
        if (msg === "Snapshot not found") return res.status(404).json({ error: msg })
        if (msg === "Commitment not found") return res.status(404).json({ error: msg })
        return res.status(500).json({ error: msg })
      }
    })
  }
}
