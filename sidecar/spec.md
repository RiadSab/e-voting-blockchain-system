# Sidecar — Architecture Spec (v1)

**Crate:** `sidecar`
**Purpose:** A stateless Rust crate, compiled to WASM and callable natively, that performs the election cryptography the TypeScript services and Solidity contracts cannot: ballot encryption, validity proofs, and homomorphic tally.

---

## 1. Responsibilities

**Owns (pure crypto, stateless):**
- Exponential ElGamal over BN254 — keygen, encrypt, decrypt
- Sigma proofs: Schnorr, Chaum-Pedersen, disjunctive OR-proof (ballot entry is 0/1), sum-to-1 proof
- Proof verification
- Homomorphic tally (per-candidate) + discrete-log recovery of counts
- *(v2)* threshold key ceremony primitives: Shamir/Feldman share math + Schnorr/Chaum-Pedersen guardian proofs

**Does NOT own:**
- State, storage, or network calls
- On-chain interaction, transaction relay, business logic, election lifecycle
- Voter eligibility / anonymity — handled by Semaphore (see §2)
- Ceremony orchestration (messaging, dropout handling) — service-layer

Statelessness is required: inputs are bytes, outputs are bytes.

---

## 2. Proof boundary

| Statement                                                   | Mechanism                   | Circuit | Built by               |
|-------------------------------------------------------------|-----------------------------|---------|------------------------|
| Eligible voter, hasn't voted before                         | SNARK                       | Yes     | Semaphore (integrated) |
| Encrypted ballot is well-formed (each entry 0/1, sums to 1) | Sigma (OR-proof, sum-proof) | No      | Sidecar                |
| Partial decryption is honest (v2)                           | Sigma (Chaum-Pedersen)      | No      | Sidecar                |

Discrete-log statements use sigma protocols (no circuit). Arbitrary computation (e.g. Semaphore's Merkle membership) uses a circuit. The sidecar contains no circuits, no R1CS, no trusted setup, no proving keys.

---

## 3. Module layout

```
sidecar/
  src/
    lib.rs            // public API + wasm-bindgen layer; Schnorr
    chaum_pedersen.rs // two-base Schnorr
    elgamal.rs        // keygen, exponential encrypt, decrypt
    tally.rs          // per-candidate homomorphic add, decrypt, count recovery
    ballot.rs         // one-hot ballot: ciphertexts + OR-proofs + sum-proof
    serialize.rs      // byte <-> struct, in the format the contract expects
```

---

## 4. Public API (WASM boundary)

Cross-boundary data is bytes / hex strings, never rich Rust types.

```
encrypt_ballot(choice_index: u32, num_candidates: u32, pubkey: bytes) -> bytes   // ballot + proofs
verify_ballot(ballot: bytes, pubkey: bytes) -> bool
tally_add(running: bytes, ballot: bytes) -> bytes                                 // homomorphic fold
decrypt_tally(running: bytes, secret_key: bytes, max_count: u32) -> Vec<u32>      // per-candidate counts
```

The native build exposes the same functions without the wasm-bindgen wrapper, so the core is built and tested natively; only the binding layer is WASM-specific.

---

## 5. Serialization (Rust <-> Solidity)

Proof bytes produced here are parsed by the Solidity verifier into BN254 points for the `ecMul` (0x07) and `ecAdd` (0x06) precompiles. The format is a contract between the sidecar and the on-chain verifier; a one-byte disagreement causes silent verification failure.

Format:
- A curve point is 64 bytes: `x || y`, each a 32-byte big-endian field element (uncompressed affine, the precompile layout).
- The Fiat-Shamir challenge is `keccak256(public points || commitments)` reduced mod the scalar order. Rust and Solidity must hash identical bytes; the contract recomputes the challenge and never accepts it as input.

---

## 6. v1 scope & build order

Vertical slices, each tested on BN254 before the next.

1. Schnorr with keccak256 Fiat-Shamir — **done**
2. Rust Schnorr proof verified on-chain in a Foundry test — **done** (~19.6k gas)
3. Chaum-Pedersen — **done**
4. Exponential ElGamal + per-candidate homomorphic tally
5. Ballot: OR-proof + sum-proof
6. `serialize` + `lib` / WASM

v1 uses a single decryption key end-to-end. The threshold ceremony replaces it in v2.

---

## 7. Decisions

- **BN254 via arkworks.** Ethereum has BN254 precompiles, so on-chain proof verification is cheap (Schnorr verifies in ~19.6k gas). This is what makes per-ballot on-chain verification viable.
- **On-chain verification of every ballot** at submission (reject invalid ballots rather than detect them after the fact).
- **Fiat-Shamir challenge** = keccak256 over the full transcript; the verifier recomputes it.
- **Hand-written Solidity verifier** — proofs are group equations, not circuits.
- **One crate**, wasm-bindgen behind a feature flag.
- **Threshold ceremony deferred to v2**; single key for v1.
