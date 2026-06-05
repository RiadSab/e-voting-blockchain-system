use ark_bn254::{Fr, G1Projective as G};
use ark_ec::Group;
use ark_ff::UniformRand;
use ark_std::Zero;

pub struct Keypair {
    pub secret: Fr,
    pub public: G,
}

pub fn keygen() -> Keypair {
    let mut rng = ark_std::rand::thread_rng();
    let secret = Fr::rand(&mut rng);
    let public = G::generator() * secret;     // u = g * alpha
    Keypair { secret, public }
}

#[derive(Clone)]
pub struct Ciphertext {
    pub c1: G,
    pub c2: G,
}

/// Exponential ElGamal: the vote goes in the exponent (g*vote), which is
/// what makes ciphertexts addable.
pub fn encrypt(vote: u64, pubkey: &G) -> Ciphertext {
    let mut rng = ark_std::rand::thread_rng();
    let g = G::generator();
    let beta = Fr::rand(&mut rng);
    let c1 = g * beta;                                  // was g^beta
    let c2 = g * Fr::from(vote) + *pubkey * beta;       // was g^vote * u^beta
    Ciphertext { c1, c2 }
}

/// Homomorphic add: combining two ciphertexts adds the underlying votes.
/// Point addition replaces the multiply-mod-p of the prototype.
pub fn add(a: &Ciphertext, b: &Ciphertext) -> Ciphertext {
    Ciphertext { c1: a.c1 + b.c1, c2: a.c2 + b.c2 }
}

/// Identity = encryption of 0. Starting accumulator for a tally.
pub fn zero_ciphertext() -> Ciphertext {
    Ciphertext { c1: G::zero(), c2: G::zero() }
}

/// Decrypt to g*sum, then recover sum by a linear discrete-log scan.
pub fn decrypt_count(ct: &Ciphertext, secret: &Fr, max_count: u64) -> Option<u64> {
    let s = ct.c1 * secret;        // shared secret  (was c1^alpha)
    let g_to_sum = ct.c2 - s;      // c2 / s  ->  c2 - s  in additive form  = g*sum
    let g = G::generator();
    let mut acc = G::zero();       // g*0
    for n in 0..=max_count {
        if acc == g_to_sum {
            return Some(n);
        }
        acc += g;                  // step to g*(n+1)
    }
    None
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn single_vote_round_trips() {
        let kp = keygen();
        let ct = encrypt(1, &kp.public);
        assert_eq!(decrypt_count(&ct, &kp.secret, 10), Some(1));
    }

    #[test]
    fn homomorphic_tally_per_candidate() {
        let kp = keygen();
        // 3 candidates [A,B,C]; voters pick B, B, A  ->  expect [1,2,0]
        let ballots = [[0u64, 1, 0], [0u64, 1, 0], [1u64, 0, 0]];
        let n = 3;

        let mut totals = vec![zero_ciphertext(); n];
        for ballot in ballots {
            for c in 0..n {
                let ct = encrypt(ballot[c], &kp.public);
                totals[c] = add(&totals[c], &ct);
            }
        }

        let counts: Vec<u64> = (0..n)
            .map(|c| decrypt_count(&totals[c], &kp.secret, 100).unwrap())
            .collect();

        assert_eq!(counts, vec![1, 2, 0]);
    }
}