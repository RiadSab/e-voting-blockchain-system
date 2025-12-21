// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * TallyVerifier.sol
 *
 * - Verifies off-chain SNARK tally proofs via a verifier contract (IZKVerifier).
 * - Ensures the `electionId` in the publicSignals matches a registered election in ElectionFactory.
 * - Extracts the per-candidate tallies from publicSignals and calls Election.onTallyVerified(tallies, proofCid).
 */

import { Ownable } from "@openzeppelin/contracts/access/Ownable.sol";
import { ReentrancyGuard } from "@openzeppelin/contracts/utils/ReentrancyGuard.sol";

interface IZKVerifier {
    function verifyProof(bytes calldata proof, uint256[] calldata publicSignals) external view returns (bool valid);
}

interface IElectionFactory {
    function getElectionAddress(uint256 electionId) external view returns (address);
    //! this function was deleted from the factory contract
    // function isRegistered(uint256 electionId) external view returns (bool);
}

interface IElection {
    function onTallyVerified(uint256[] calldata tallies, string calldata proofCid) external;
}

contract TallyVerifier is Ownable, ReentrancyGuard {

    event TallyProofSubmitted(uint256 indexed electionId, address indexed prover, string proofCid);
    event VerifierUpdated(address oldVerifier, address newVerifier);
    event FactoryUpdated(address oldFactory, address newFactory);


    IZKVerifier public verifier;          // on-chain SNARK verifier contract
    IElectionFactory public factory;      // ElectionFactory contract address (registry)


    modifier validVerifier() {
        _validVerifier();
        _;
    }

    modifier validFactory() {
        _validFactory();
        _;
    }

    function _validVerifier() internal view {
        require(address(verifier) != address(0), "TallyVerifier: verifier not set");
    }

    function _validFactory() internal view {
        require(address(factory) != address(0), "TallyVerifier: factory not set");
    }



    constructor(address _factory, address _verifier) Ownable(msg.sender) {
        require(_factory != address(0), "TallyVerifier: factory zero");
        require(_verifier != address(0), "TallyVerifier: verifier zero");
        factory = IElectionFactory(_factory);
        verifier = IZKVerifier(_verifier);
    }


    function setVerifier(address _verifier) external onlyOwner {
        require(_verifier != address(0), "TallyVerifier: verifier zero");
        address old = address(verifier);
        verifier = IZKVerifier(_verifier);
        emit VerifierUpdated(old, _verifier);
    }

    function setFactory(address _factory) external onlyOwner {
        require(_factory != address(0), "TallyVerifier: factory zero");
        address old = address(factory);
        factory = IElectionFactory(_factory);
        emit FactoryUpdated(old, _factory);
    }


    function submitTallyProof(
        bytes calldata proof,
        uint256[] calldata publicSignals,
        string calldata proofCid
    ) external nonReentrant validVerifier validFactory {
        // Basic length checks
        require(publicSignals.length >= 2, "TallyVerifier: publicSignals too short");

        uint256 electionId = publicSignals[0];
        uint256 numCandidates = publicSignals[1];

        // sanity
        require(numCandidates > 0, "TallyVerifier: zero candidates");
        require(publicSignals.length == 2 + numCandidates, "TallyVerifier: publicSignals length mismatch");

        // require(factory.isRegistered(electionId), "TallyVerifier: election not registered");
        address electionAddr = factory.getElectionAddress(electionId);
        require(electionAddr != address(0), "TallyVerifier: election address zero");

        // Verify SNARK proof cryptographically
        bool ok = verifier.verifyProof(proof, publicSignals);
        require(ok, "TallyVerifier: invalid proof");

        // Extract tallies from publicSignals and build an array
        uint256[] memory tallies = new uint256[](numCandidates);
        for (uint256 i = 0; i < numCandidates; ++i) {
            tallies[i] = publicSignals[2 + i];
        }

        // Call election contract to finalize tallies.
        // The election contract must implement onTallyVerified(tallies, proofCid)
        IElection(electionAddr).onTallyVerified(tallies, proofCid);

        emit TallyProofSubmitted(electionId, msg.sender, proofCid);
    }
}