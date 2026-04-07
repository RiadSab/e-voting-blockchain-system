import { CONFIG, dbPool } from "./config"
import { MerkleController } from "./controller"
import { SnapshotRepository } from "./repository"
import { MerkleService } from "./service"

async function main() {
  const repo = new SnapshotRepository(dbPool)
  await repo.initDB()

  const service = new MerkleService(repo)
  const controller = new MerkleController(service)

  controller.getApp().listen(CONFIG.PORT, () => {
    console.log(`Merkle service listening on :${CONFIG.PORT}`)
    console.log(`RPC_URL=${CONFIG.RPC_URL} CHAIN_ID=${CONFIG.CHAIN_ID}`)
  })
}

main().catch((err) => {
  console.error("Fatal startup error:", err)
  process.exit(1)
})
