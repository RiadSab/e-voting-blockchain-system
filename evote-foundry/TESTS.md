# Test Implementation Summary

## ElectionFactory tests

- Access control: only the electoral authority can create elections or update authority.
- Input validation: IPFS CID required, time windows enforced.
- Deployment: verifies election address mapping, stored fields, and semaphore group id increments.

## Election tests

- Uses real SemaphoreVerifier + Semaphore deployments.
- Constructor validation: bad time windows, zero addresses, and candidate count.
- Registration: only authority, registration window enforced, membership inserted in the Semaphore group.
- Tally flow: only tally verifier, voting must be closed, correct tally length, publish once, and readback.
- Vote submission (negative paths): not started, finished, wrong ciphertext length, bad message/scope, invalid proof.
- Vote submission (positive path): reads a real Semaphore proof fixture and verifies on-chain.

## Real Semaphore proof fixture

- Generator script: tools/semaphore-proof/generate-proof.mjs
- Output file: test/fixtures/semaphore-proof.json
- Dependencies: tools/semaphore-proof/package.json (run npm install in that folder)
- Foundry read access: foundry.toml allows reading test/fixtures.

## How to run

- ElectionFactory: forge test --match-path test/ElectionFactory.t.sol
- Election: forge test --match-path test/Election.t.sol

## Regenerating the proof fixture

1. cd tools/semaphore-proof
2. npm install
3. node generate-proof.mjs
