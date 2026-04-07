import { Group } from "@semaphore-protocol/group"
import { Snapshot } from "./merkleDto"
import { SnapshotRepository } from "./repository"
import { ethers } from "ethers"
import { provider } from "./config"
import { iface } from "./config"
import { CONFIG } from "./config"

type CachedTree = {
  group: Group
  snapshot: Snapshot
  indexByCommitment: Map<string, number>
}

export type CreateSnapshotParams = {
  semaphoreAddress: string
  groupId: string
  depth: number
  fromBlock: number
  toBlock: number
}

export type CreateSnapshotResult = {
  snapshotId: string
  rootHash: string
  membersCount: number
}

export type MerkleProofResult = {
  depth: number
  root: string
  leafIndex: number
  siblings: string[]
  pathIndices: number[]
}

export class MerkleService {
  private repo: SnapshotRepository
  private cachedTree = new Map<string, CachedTree>()

  constructor(repo: SnapshotRepository) {
    this.repo = repo
  }

  private getCacheKey(addr: string, gid: string) {
    return `${addr.toLowerCase()}:${gid}`
  }

  async createSnapshot(params: CreateSnapshotParams): Promise<CreateSnapshotResult> {
    const { semaphoreAddress, groupId, depth, fromBlock, toBlock } = params

    const topic0 = ethers.id("MemberAdded(uint256,uint256,uint256,uint256)")
    const topic1 = ethers.zeroPadValue(ethers.toBeHex(BigInt(groupId)), 32)

    const logs = await provider.getLogs({
      address: semaphoreAddress,
      topics: [topic0, topic1],
      fromBlock,
      toBlock
    })

    logs.sort((a, b) => {
      if (a.blockNumber !== b.blockNumber) return a.blockNumber - b.blockNumber
      return a.index - b.index
    })

    const members: string[] = []
    let lastRootFromEvent: string | null = null

    for (const log of logs) {
      const parsedLog = iface.parseLog(log)
      if (!parsedLog) continue

      members.push(BigInt(parsedLog.args.identityCommitment).toString())
      lastRootFromEvent = BigInt(parsedLog.args.merkleTreeRoot).toString()
    }

    const { group, indexByCommitment } = this.buildGroupFromMembers(members)
    const computedRoot = group.root.toString()

    if (lastRootFromEvent && computedRoot !== lastRootFromEvent) {
      const error = new Error(
        "Merkle root mismatch between local Group(depth) computation and on-chain emitted root."
      )
      ;(error as any).details = {
        computedRoot,
        chainRoot: lastRootFromEvent,
        depthProvided: depth,
        membersCount: members.length
      }
      throw error
    }

    const rootToStore = lastRootFromEvent ?? computedRoot

    const snapshot: Snapshot = {
      chainId: CONFIG.CHAIN_ID,
      semaphoreAddress,
      groupId,
      depth,
      snapshotBlock: toBlock,
      root: rootToStore,
      members
    }

    await this.repo.saveSnapshot(snapshot)
    this.cachedTree.set(this.getCacheKey(semaphoreAddress, groupId), {
      group,
      snapshot,
      indexByCommitment
    })

    return {
      snapshotId: `${snapshot.chainId}:${semaphoreAddress.toLowerCase()}:${groupId}:${toBlock}`,
      rootHash: rootToStore,
      membersCount: members.length
    }
  }

  async getMerkleProof(
    semaphoreAddress: string,
    groupId: string,
    identityCommitment: string
  ): Promise<MerkleProofResult> {
    const normalizedCommitment = BigInt(identityCommitment).toString()
    const key = this.getCacheKey(semaphoreAddress, groupId)
    let cached = this.cachedTree.get(key)

    if (!cached) {
      const snap = await this.repo.loadSnapshot(semaphoreAddress, groupId)
      if (!snap) throw new Error("Snapshot not found")

      const built = this.buildGroupFromMembers(snap.members)
      cached = { ...built, snapshot: snap }
      this.cachedTree.set(key, cached)
    }

    const idx = cached.indexByCommitment.get(normalizedCommitment)
    if (idx === undefined) throw new Error("Commitment not found")

    const mp = (cached.group as any).generateMerkleProof(idx)
    const siblings: bigint[] | undefined = mp?.siblings
    if (!siblings) throw new Error("Merkle proof generation failed")

    const pathIndices = this.getPathIndicesFromLeafIndex(idx, cached.snapshot.depth)

    return {
      depth: cached.snapshot.depth,
      root: cached.group.root.toString(),
      leafIndex: idx,
      siblings: siblings.map((s) => BigInt(s).toString()),
      pathIndices
    }
  }

  private buildGroupFromMembers(members: string[]) {
    const normalizedMembers = members.map((m) => BigInt(m).toString())
    const membersBigInt = normalizedMembers.map((m) => BigInt(m))

    const group = new Group(membersBigInt as any)

    const indexByCommitment = new Map<string, number>()
    normalizedMembers.forEach((m, i) => indexByCommitment.set(m, i))

    return { group, indexByCommitment }
  }

  private getPathIndicesFromLeafIndex(leafIndex: number, depth: number): number[] {
    const indices: number[] = []
    for (let i = 0; i < depth; i++) {
      indices.push((leafIndex >> i) & 1)
    }
    return indices
  }
}
