use ark_bn254::{Fr, G1Projective as G};
use ark_ec::CurveGroup;
use ark_ff::{PrimeField, UniformRand, BigInteger};
use sha3::{Digest, Keccak256};

#[derive(Clone)]
pub struct CpProof {
    pub t1: G,  // commitment on base g:  g*r
    pub t2: G,  // commitment on base h:  h*r
    pub z: Fr,  // single response:       r + c*alpha
}

/// Prove that u = g*alpha AND v = h*alpha share the SAME secret alpha,
/// without revealing alpha. (Same secret behind both = honest decryption.)
pub fn prove(alpha: &Fr, g: &G, h: &G, u: &G, v: &G) -> CpProof {
    let mut rng = ark_std::rand::thread_rng();
    let r = Fr::rand(&mut rng);
    let t1 = *g * r;                       // one random r...
    let t2 = *h * r;                       // ...used on BOTH bases (this is the link)
    let c = challenge(g, h, u, v, &t1, &t2);
    let z = r + c * alpha;                 // single response
    CpProof { t1, t2, z }
}

pub fn verify(g: &G, h: &G, u: &G, v: &G, proof: &CpProof) -> bool {
    let c = challenge(g, h, u, v, &proof.t1, &proof.t2);
    // Two equations, same z. Both pass only if the same alpha is behind u and v.
    let eq1 = *g * proof.z == proof.t1 + *u * c;   // g*z == t1 + u*c
    let eq2 = *h * proof.z == proof.t2 + *v * c;   // h*z == t2 + v*c
    eq1 && eq2
}

fn point_to_bytes(p: &G) -> [u8; 64] {
    let aff = p.into_affine();
    let mut out = [0u8; 64];
    out[..32].copy_from_slice(&aff.x.into_bigint().to_bytes_be());
    out[32..].copy_from_slice(&aff.y.into_bigint().to_bytes_be());
    out
}

/// c = keccak256(g ‖ h ‖ u ‖ v ‖ t1 ‖ t2) mod scalar order.
/// All public values AND both commitments go in — same soundness rule as Schnorr.
fn challenge(g: &G, h: &G, u: &G, v: &G, t1: &G, t2: &G) -> Fr {
    let mut hsh = Keccak256::new();
    for p in [g, h, u, v, t1, t2] {
        hsh.update(point_to_bytes(p));
    }
    Fr::from_be_bytes_mod_order(&hsh.finalize())
}

#[cfg(test)]
mod tests {
    use super::*;
    use ark_ec::Group;

    #[test]
    fn real_proof_verifies() {
        let mut rng = ark_std::rand::thread_rng();
        let g = G::generator();
        let h = g * Fr::from(5u64);            // a second, independent base
        let alpha = Fr::from(7u64);
        let u = g * alpha;                     // u = g^alpha
        let v = h * alpha;                     // v = h^alpha (SAME alpha)
        let proof = prove(&alpha, &g, &h, &u, &v);
        assert!(verify(&g, &h, &u, &v, &proof));
    }

    #[test]
    fn different_secrets_fail() {
        let g = G::generator();
        let h = g * Fr::from(5u64);
        let u = g * Fr::from(7u64);            // u uses 7
        let v = h * Fr::from(8u64);            // v uses 8 -- DIFFERENT secret
        // Prover tries to claim a common secret of 7; must fail because v != h*7.
        let proof = prove(&Fr::from(7u64), &g, &h, &u, &v);
        assert!(!verify(&g, &h, &u, &v, &proof));
    }
}