use ark_bn254::{Fr, G1Projective as G};
use ark_ec::{CurveGroup, Group};
use ark_ff::{BigInteger, PrimeField, UniformRand};
use sha3::{Digest, Keccak256};
pub mod chaum_pedersen;
pub mod elgamal;
pub mod ballot;
#[derive(Clone)]
pub struct SchnorrProof {
    pub t: G,
    pub z: Fr,
}

pub fn prove(alpha: &Fr, g: &G, u: &G) -> SchnorrProof {
    let mut rng = ark_std::rand::thread_rng();
    let r = Fr::rand(&mut rng);
    let t = *g * r;
    let c = challenge(g, u, &t);
    let z = r + c * alpha;
    SchnorrProof { t, z }
}

pub fn verify(g: &G, u: &G, proof: &SchnorrProof) -> bool {
    let c = challenge(g, u, &proof.t);
    *g * proof.z == proof.t + *u * c
}

/// A curve point as 64 bytes: x (32, big-endian) ‖ y (32, big-endian).
/// This is exactly the byte layout Ethereum's BN254 precompiles use.
fn point_to_bytes(p: &G) -> [u8; 64] {
    let aff = p.into_affine();
    let mut out = [0u8; 64];
    out[..32].copy_from_slice(&aff.x.into_bigint().to_bytes_be());
    out[32..].copy_from_slice(&aff.y.into_bigint().to_bytes_be());
    out
}

/// c = keccak256(g ‖ u ‖ t) reduced mod the scalar order.
/// Must match the contract byte-for-byte.
fn challenge(g: &G, u: &G, t: &G) -> Fr {
    let mut h = Keccak256::new();
    h.update(point_to_bytes(g));
    h.update(point_to_bytes(u));
    h.update(point_to_bytes(t));
    let digest = h.finalize(); // 32 bytes
    Fr::from_be_bytes_mod_order(&digest) // mod scalar order, same as Solidity's % r
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn real_proof_verifies() {
        let g = G::generator();
        let alpha = Fr::from(7u64);
        let u = g * alpha;
        let proof = prove(&alpha, &g, &u);
        assert!(verify(&g, &u, &proof));
    }

    #[test]
    fn fake_proof_fails() {
        let g = G::generator();
        let u = g * Fr::from(7u64);
        let proof = prove(&Fr::from(8u64), &g, &u); // wrong secret
        assert!(!verify(&g, &u, &proof));
    }

    /// Prints a valid proof's values as decimals to paste into the Foundry test.
    #[test]
    fn print_proof_for_solidity() {
        let g = G::generator();
        let alpha = Fr::from(7u64);
        let u = g * alpha;
        let proof = prove(&alpha, &g, &u);
        assert!(verify(&g, &u, &proof));

        let ga = g.into_affine();
        let ua = u.into_affine();
        let ta = proof.t.into_affine();
        println!("g.x = {}", ga.x);
        println!("g.y = {}", ga.y);
        println!("u.x = {}", ua.x);
        println!("u.y = {}", ua.y);
        println!("t.x = {}", ta.x);
        println!("t.y = {}", ta.y);
        println!("z   = {}", proof.z);
        println!("c   = {}", challenge(&g, &u, &proof.t));
    }

    #[test]
    fn simulated_proof_verifies_without_secret() {
        use ark_bn254::{Fr, G1Projective as G};
        use ark_ec::Group;
        use ark_ff::UniformRand;

        let mut rng = ark_std::rand::thread_rng();

        let g = G::generator();
        let u = g * Fr::from(7u64); // public key. secret is 7 — but we NEVER use it below.

        // The simulation: pick c and z FIRST (arbitrary), then back-solve t.
        let c = Fr::from(4u64);
        let z = Fr::from(9u64);
        let t = g * z - u * c; // t = g*z - u*c  <-- the trick

        // Verifier's check, exactly as in a real proof:
        let lhs = g * z;
        let rhs = t + u * c;
        assert_eq!(lhs, rhs); // verifies, though no secret was used

        // sanity: prove we really didn't need the secret — c,z were free choices
        let _ = &mut rng; // (rng unused; chosen values are fixed on purpose)
    }
}
