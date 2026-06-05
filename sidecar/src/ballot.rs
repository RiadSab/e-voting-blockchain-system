use ark_bn254::{Fr, G1Projective as G};
use ark_ec::{CurveGroup, Group};
use ark_ff::{BigInteger, PrimeField, UniformRand};
use sha3::{Digest, Keccak256};
use crate::elgamal::{self, Ciphertext};

/// One branch's transcript: two commitments (Chaum-Pedersen has two bases),
/// a challenge, and a response.
#[derive(Clone)]
pub struct Branch {
    pub a1: G, // commitment on base g
    pub a2: G, // commitment on base pubkey
    pub c: Fr, // this branch's challenge
    pub z: Fr, // response
}

/// OR-proof that ciphertext (c1,c2) encrypts 0 OR 1, without revealing which.
#[derive(Clone)]
pub struct OrProof {
    pub branch0: Branch,
    pub branch1: Branch,
}

fn point_bytes(p: &G) -> [u8; 64] {
    let aff = p.into_affine();
    let mut out = [0u8; 64];
    out[..32].copy_from_slice(&aff.x.into_bigint().to_bytes_be());
    out[32..].copy_from_slice(&aff.y.into_bigint().to_bytes_be());
    out
}

/// C = keccak256(c1, c2, all four commitments) mod order.
fn overall_challenge(c1: &G, c2: &G, b0: &Branch, b1: &Branch) -> Fr {
    let mut h = Keccak256::new();
    for p in [c1, c2, &b0.a1, &b0.a2, &b1.a1, &b1.a2] {
        h.update(point_bytes(p));
    }
    Fr::from_be_bytes_mod_order(&h.finalize())
}

/// Prove. `vote` is 0 or 1; `beta` is the encryption randomness (the secret).
/// (c1, c2) is the ciphertext: c1 = g*beta, c2 = g*vote + pubkey*beta.
pub fn prove(vote: u64, beta: &Fr, c1: &G, c2: &G, pubkey: &G) -> OrProof {
    let mut rng = ark_std::rand::thread_rng();
    let g = G::generator();

    // For each branch v, the "second statement" point is (c2 - g*v):
    //   branch 0 verifies c2        against pubkey*beta
    //   branch 1 verifies (c2 - g)  against pubkey*beta
    // Honest branch = the one matching the real vote.

    if vote == 0 {
        // ----- branch 1 is FALSE -> simulate it -----
        let c_fake = Fr::rand(&mut rng); // pick its challenge first
        let z_fake = Fr::rand(&mut rng); // pick its response first
        let m1 = *c2 - g; // branch-1's adjusted point
        // back-solve commitments so the verifier eqs hold:
        //   a1 = g*z - c1*c        a2 = pubkey*z - m1*c
        let b1 = Branch {
            a1: g * z_fake - *c1 * c_fake,
            a2: *pubkey * z_fake - m1 * c_fake,
            c: c_fake,
            z: z_fake,
        };

        // ----- branch 0 is TRUE -> commit honestly -----
        let r = Fr::rand(&mut rng);
        let mut b0 = Branch {
            a1: g * r,
            a2: *pubkey * r,
            c: Fr::from(0u64),
            z: Fr::from(0u64),
        };

        // Fiat-Shamir over both commitments
        let big_c = overall_challenge(c1, c2, &b0, &b1);
        // honest challenge is forced:
        b0.c = big_c - c_fake;
        // honest response uses the real secret beta:
        b0.z = r + b0.c * beta;

        OrProof {
            branch0: b0,
            branch1: b1,
        }
    } else {
        // vote == 1: branch 0 is FALSE -> simulate; branch 1 TRUE -> honest
        let c_fake = Fr::rand(&mut rng);
        let z_fake = Fr::rand(&mut rng);
        let m0 = *c2; // branch-0's point (v=0, no subtraction)
        let b0 = Branch {
            a1: g * z_fake - *c1 * c_fake,
            a2: *pubkey * z_fake - m0 * c_fake,
            c: c_fake,
            z: z_fake,
        };

        let r = Fr::rand(&mut rng);
        let mut b1 = Branch {
            a1: g * r,
            a2: *pubkey * r,
            c: Fr::from(0u64),
            z: Fr::from(0u64),
        };

        let big_c = overall_challenge(c1, c2, &b0, &b1);
        b1.c = big_c - c_fake;
        b1.z = r + b1.c * beta;

        OrProof {
            branch0: b0,
            branch1: b1,
        }
    }
}

pub fn verify(proof: &OrProof, c1: &G, c2: &G, pubkey: &G) -> bool {
    let g = G::generator();
    let b0 = &proof.branch0;
    let b1 = &proof.branch1;

    // 1. challenges must sum to the Fiat-Shamir challenge
    let big_c = overall_challenge(c1, c2, b0, b1);
    if b0.c + b1.c != big_c {
        return false;
    }

    // 2. branch 0 (point = c2):    g*z == a1 + c1*c   AND   pubkey*z == a2 + c2*c
    let m0 = *c2;
    let ok0 = g * b0.z == b0.a1 + *c1 * b0.c && *pubkey * b0.z == b0.a2 + m0 * b0.c;

    // 3. branch 1 (point = c2 - g)
    let m1 = *c2 - g;
    let ok1 = g * b1.z == b1.a1 + *c1 * b1.c && *pubkey * b1.z == b1.a2 + m1 * b1.c;

    ok0 && ok1
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::elgamal;

    fn setup(vote: u64) -> (Fr, G, G, G) {
        // build a real ciphertext and return (beta, c1, c2, pubkey)
        let kp = elgamal::keygen();
        let mut rng = ark_std::rand::thread_rng();
        let beta = Fr::rand(&mut rng);
        let g = G::generator();
        let c1 = g * beta;
        let c2 = g * Fr::from(vote) + kp.public * beta;
        (beta, c1, c2, kp.public)
    }

    #[test]
    fn valid_zero_verifies() {
        let (beta, c1, c2, pk) = setup(0);
        let p = prove(0, &beta, &c1, &c2, &pk);
        assert!(verify(&p, &c1, &c2, &pk));
    }

    #[test]
    fn valid_one_verifies() {
        let (beta, c1, c2, pk) = setup(1);
        let p = prove(1, &beta, &c1, &c2, &pk);
        assert!(verify(&p, &c1, &c2, &pk));
    }

    #[test]
    fn invalid_vote_cannot_prove() {
        // vote = 5 is illegal. Build its ciphertext, then try to prove it's 0-or-1.
        // Neither branch is true, so no honest branch exists -> must fail.
        let (beta, c1, c2, pk) = setup(5);
        // try to pass it off as a "1"
        let p = prove(1, &beta, &c1, &c2, &pk);
        assert!(!verify(&p, &c1, &c2, &pk));
    }
}


/// Proof that the aggregate ciphertext encrypts exactly 1.
/// This is a plain Chaum-Pedersen: (c1_sum, c2_sum - g) is a DH pair under beta_sum.
#[derive(Clone)]
pub struct SumProof {
    pub a1: G,
    pub a2: G,
    pub c: Fr,
    pub z: Fr,
}

fn sum_challenge(c1: &G, c2: &G, a1: &G, a2: &G) -> Fr {
    let mut h = Keccak256::new();
    for p in [c1, c2, a1, a2] {
        h.update(point_bytes(p));
    }
    Fr::from_be_bytes_mod_order(&h.finalize())
}

/// Prove the aggregate (c1, c2) encrypts 1, knowing beta_sum (sum of all slot betas).
pub fn prove_sum_one(beta_sum: &Fr, c1: &G, c2: &G, pubkey: &G) -> SumProof {
    let mut rng = ark_std::rand::thread_rng();
    let g = G::generator();
    let m = *c2 - g; // subtract the "1" -> should equal pubkey*beta_sum

    let r = Fr::rand(&mut rng);
    let a1 = g * r;
    let a2 = *pubkey * r;
    let c = sum_challenge(c1, &m, &a1, &a2);
    let z = r + c * beta_sum;
    SumProof { a1, a2, c, z }
}

pub fn verify_sum_one(proof: &SumProof, c1: &G, c2: &G, pubkey: &G) -> bool {
    let g = G::generator();
    let m = *c2 - g;
    let c = sum_challenge(c1, &m, &proof.a1, &proof.a2);
    if c != proof.c {
        return false;
    }
    g * proof.z == proof.a1 + *c1 * c && *pubkey * proof.z == proof.a2 + m * c
}

/// A full ballot: one ciphertext per candidate, an OR-proof per slot,
/// and one sum-to-1 proof over the aggregate.
pub struct Ballot {
    pub slots: Vec<Ciphertext>,
    pub or_proofs: Vec<OrProof>,
    pub sum_proof: SumProof,
}

/// Build a one-hot ballot for `choice` among `num_candidates`.
pub fn build_ballot(choice: usize, num_candidates: usize, pubkey: &G) -> Ballot {
    let mut rng = ark_std::rand::thread_rng();
    let g = G::generator();

    let mut slots = Vec::new();
    let mut or_proofs = Vec::new();
    let mut beta_sum = Fr::from(0u64);

    for i in 0..num_candidates {
        let vote = if i == choice { 1u64 } else { 0u64 };
        let beta = Fr::rand(&mut rng);
        let c1 = g * beta;
        let c2 = g * Fr::from(vote) + *pubkey * beta;
        let or = prove(vote, &beta, &c1, &c2, pubkey); // per-slot 0/1 proof

        slots.push(Ciphertext { c1, c2 });
        or_proofs.push(or);
        beta_sum += beta; // accumulate randomness
    }

    // Aggregate = homomorphic sum of all slots; should encrypt 1.
    let mut agg = elgamal::zero_ciphertext();
    for ct in &slots {
        agg = elgamal::add(&agg, ct);
    }
    let sum_proof = prove_sum_one(&beta_sum, &agg.c1, &agg.c2, pubkey);

    Ballot {
        slots,
        or_proofs,
        sum_proof,
    }
}

/// Verify a ballot completely: every slot is 0/1, and the whole thing sums to 1.
pub fn verify_ballot(ballot: &Ballot, pubkey: &G) -> bool {
    // 1. every slot is a valid 0-or-1
    for (ct, or) in ballot.slots.iter().zip(&ballot.or_proofs) {
        if !verify(or, &ct.c1, &ct.c2, pubkey) {
            return false;
        }
    }
    // 2. the aggregate encrypts exactly 1
    let mut agg = elgamal::zero_ciphertext();
    for ct in &ballot.slots {
        agg = elgamal::add(&agg, ct);
    }
    verify_sum_one(&ballot.sum_proof, &agg.c1, &agg.c2, pubkey)
}

#[cfg(test)]
mod ballot_tests {
    use super::*;

    #[test]
    fn valid_onehot_ballot_verifies() {
        let kp = elgamal::keygen();
        let ballot = build_ballot(1, 3, &kp.public); // vote for candidate 1 of 3
        assert!(verify_ballot(&ballot, &kp.public));
    }

    #[test]
    fn double_vote_fails() {
        // Hand-craft an illegal [1,1,0] ballot: each slot is a valid 1, but sum = 2.
        let kp = elgamal::keygen();
        let mut rng = ark_std::rand::thread_rng();
        let g = G::generator();

        let mut slots = Vec::new();
        let mut or_proofs = Vec::new();
        let mut beta_sum = Fr::from(0u64);
        let votes = [1u64, 1, 0]; // TWO ones -> illegal
        for v in votes {
            let beta = Fr::rand(&mut rng);
            let c1 = g * beta;
            let c2 = g * Fr::from(v) + kp.public * beta;
            or_proofs.push(prove(v, &beta, &c1, &c2, &kp.public));
            slots.push(Ciphertext { c1, c2 });
            beta_sum += beta;
        }
        let mut agg = elgamal::zero_ciphertext();
        for ct in &slots {
            agg = elgamal::add(&agg, ct);
        }
        let sum_proof = prove_sum_one(&beta_sum, &agg.c1, &agg.c2, &kp.public);
        let ballot = Ballot {
            slots,
            or_proofs,
            sum_proof,
        };

        // Every OR-proof passes (each slot IS 0 or 1), but sum-to-1 must fail (sum=2).
        assert!(!verify_ballot(&ballot, &kp.public));
    }
}
