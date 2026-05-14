# Deployment Scripts Summary

## Primary (recommended) option: DeployAll

- File: script/DeployAll.s.sol
- Behavior: deploys Semaphore + ElectionFactory in one broadcast, writes both addresses into deployments/deployments.json under the current chain id key.
- Uses env:
  - ELECTORAL_AUTHORITY (default: tx.origin)
  - DEFAULT_TALLY_VERIFIER (optional)
- Registry format: deployments.json stores a map keyed by chain id (e.g. "11155111").

## Split option: DeploySemaphore then DeployFactory

- Files: script/DeploySemaphore.s.sol and script/DeployFactory.s.sol
- DeploySemaphore writes the Semaphore address to deployments.json for the chain.
- DeployFactory reads the Semaphore address from the registry (or SEMAPHORE_ADDRESS override) and deploys ElectionFactory.
- Use this when you need to redeploy one component without touching the other.

## Why keep deployments.json

- Single source of truth for addresses per network.
- Used by off-chain services (indexer/backend) and future scripts.
- Avoids manual copy/paste errors after deployment.

## Example commands

- Deploy all: forge script script/DeployAll.s.sol --broadcast
- Deploy semaphore only: forge script script/DeploySemaphore.s.sol --broadcast
- Deploy factory only: forge script script/DeployFactory.s.sol --broadcast
