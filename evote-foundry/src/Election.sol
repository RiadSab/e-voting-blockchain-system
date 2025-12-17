// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/*
  Election.sol

  One-contract-per-election model.

    Constructor signature MUST match the factory deployment call:
        constructor(
            uint256 _electionId,
            string memory _ipfsCid,
            bytes32 _metadataHash,
            bytes32 _merkleRoot,
            uint256 _pkx,
            uint256 _pky,
            uint256 _startTime,
            uint256 _endTime,
            address _tallyVerifier,
            address _electionAuthority,
            address _semaphore,
            uint256 _semaphoreGroupId
        )

  See notes above regarding verifier publicSignals layout.
*/

import "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import { ISemaphore } from "@semaphore/contracts/interfaces/ISemaphore.sol";

contract Election is ReentrancyGuard {
    // -------------------------
    // Events
    // -------------------------
    event VoteSubmitted(uint256 indexed electionId, uint256 indexed voteIndex, bytes32 cipherHash, uint256 timestamp);
    event ElectionClosed(uint256 indexed electionId, uint256 timestamp);
    event TallyPublished(uint256 indexed electionId, uint256[] tallies, string proofCid);

    // -------------------------
    // Structs
    // -------------------------
    struct Ciphertext {
        uint256 c1x;
        uint256 c1y;
        uint256 c2x;
        uint256 c2y;
        uint256 timestamp;
    }

    // -------------------------
    // Immutable / constructor-set values
    // -------------------------
    uint256 public immutable electionId;
    string public ipfsCid;        // human-readable metadata pointer (emitted by factory at creation)
    bytes32 public immutable metadataHash; // keccak256(bytes(ipfsCid))
    bytes32 public merkleRoot;    // voter registry root (can be set once at construction)
    uint256 public publicKeyX;    // ElGamal PK.x
    uint256 public publicKeyY;    // ElGamal PK.y
    uint256 public startTime;     // unix ts
    uint256 public endTime;       // unix ts

    address public tallyVerifier;    // address allowed to publish final tally (or dedicated verifier that calls onTallyVerified)
    address public electionAuthority; // multisig / safe address controlling admin operations

    // Semaphore integration
    address public semaphore;        // Semaphore core contract
    uint256 public semaphoreGroupId; // Group used for this election

    // -------------------------
    // Mutable state
    // -------------------------
    Ciphertext[] private encryptedVotes; // append-only list of ciphertexts
    mapping(bytes32 => bool) private nullifierUsed; // used nullifiers (anti-double-vote)
    bool public closed; // admin closed voting early or after endTime
    bool public tallyPublished; // once final tally is published, lock

    uint256[] public finalTally; // published results (per-candidate counts)
    string public tallyProofCid; // IPFS CID of proof bundle
    bytes32 public tallyProofCidHash; // keccak256(bytes(tallyProofCid))
    uint256 public finalTallyPublished; // timestamp

    // -------------------------
    // Modifiers
    // -------------------------
    modifier onlyAuthority() {
        require(msg.sender == electionAuthority, "Election: caller not authority");
        _;
    }

    modifier onlyTallyVerifier() {
        require(msg.sender == tallyVerifier, "Election: caller not tallyVerifier");
        _;
    }

    // -------------------------
    // Constructor
    // -------------------------
    /**
     * @notice Initializes a new Election.
     * @param _electionId        Unique id assigned by the factory.
     * @param _ipfsCid           Canonical IPFS CID of the election metadata.
     * @param _metadataHash      keccak256(bytes(_ipfsCid)) anchor.
     * @param _merkleRoot        Semaphore group root expected by votes (proof must match this exact root).
     * @param _pkx               ElGamal public key X coordinate.
     * @param _pky               ElGamal public key Y coordinate.
     * @param _startTime         Voting start timestamp.
     * @param _endTime           Voting end timestamp.
     * @param _tallyVerifier     Address allowed to call onTallyVerified. If zero, no one can publish until updated.
     * @param _electionAuthority Admin (recommended multisig). Must be non-zero.
     * @param _semaphore         Semaphore core address. If zero, submitVote will revert (effectively disabled).
     * @param _semaphoreGroupId  Group id in Semaphore to validate membership/nullifier.
     */
    constructor(
        uint256 _electionId,
        string memory _ipfsCid,
        bytes32 _metadataHash,
        bytes32 _merkleRoot,
        uint256 _pkx,
        uint256 _pky,
        uint256 _startTime,
        uint256 _endTime,
        address _tallyVerifier,
        address _electionAuthority,
        address _semaphore,
        uint256 _semaphoreGroupId
    ) {
        require(_electionAuthority != address(0), "Election: authority zero");
        require(bytes(_ipfsCid).length > 0, "Election: ipfsCid required");
        require(_startTime < _endTime, "Election: bad window");

        electionId = _electionId;
        ipfsCid = _ipfsCid;
        metadataHash = _metadataHash;
        merkleRoot = _merkleRoot;
        publicKeyX = _pkx;
        publicKeyY = _pky;
        startTime = _startTime;
        endTime = _endTime;
        tallyVerifier = _tallyVerifier;
        electionAuthority = _electionAuthority;

        // Semaphore config
        semaphore = _semaphore;
        semaphoreGroupId = _semaphoreGroupId;

        closed = false;
        tallyPublished = false;
    }

    // -------------------------
    // Core: submitVote
    // -------------------------
    /**
     * @notice Submit an encrypted vote using Semaphore proof for on-chain membership/nullifier enforcement.
     * @param c1x Ciphertext C1.x.
     * @param c1y Ciphertext C1.y.
     * @param c2x Ciphertext C2.x.
     * @param c2y Ciphertext C2.y.
     * @param semaProof Semaphore proof with fields:
     *        - merkleTreeDepth, merkleTreeRoot (must equal this contract's merkleRoot),
     *        - nullifier (must be unused),
     *        - message = uint256(keccak256(electionId, c1x, c1y, c2x, c2y)),
     *        - scope = electionId,
     *        - points[8] = Groth16 proof points.
     *        Reverts on any mismatch or invalid proof.
     */
    function submitVote(
        uint256 c1x,
        uint256 c1y,
        uint256 c2x,
        uint256 c2y,
        ISemaphore.SemaphoreProof calldata semaProof
    ) external nonReentrant {
        // --- cheap structural checks first ---
        require(block.timestamp >= startTime, "Election: not started");
        require(block.timestamp <= endTime, "Election: finished"); // allow manual close below
        require(!closed, "Election: closed");
        require(!tallyPublished, "Election: tally already published");

        // Build expected bindings for Semaphore: signal binds to ciphertext and election
        bytes32 expectedSignal = keccak256(abi.encode(electionId, c1x, c1y, c2x, c2y));
        require(semaProof.message == uint256(expectedSignal), "Election: bad message");
        require(semaProof.scope == electionId, "Election: bad scope");
        require(semaProof.merkleTreeRoot == uint256(merkleRoot), "Election: merkleRoot mismatch");

        // Ensure local nullifier not re-used (Semaphore enforces it globally; this is an extra guard)
        require(!nullifierUsed[bytes32(semaProof.nullifier)], "Election: nullifier already used");

        // Verify via Semaphore
        ISemaphore(semaphore).validateProof(semaphoreGroupId, semaProof);

        // --- state updates (atomic) ---
        // mark nullifier
        nullifierUsed[bytes32(semaProof.nullifier)] = true;

        // store ciphertext
        Ciphertext memory cipher = Ciphertext({
            c1x: c1x,
            c1y: c1y,
            c2x: c2x,
            c2y: c2y,
            timestamp: block.timestamp
        });

        encryptedVotes.push(cipher);
        uint256 index = encryptedVotes.length - 1;

        // emit event with ciphertext hash for indexing (no leakage of vote)
        bytes32 cipherHash = keccak256(abi.encodePacked(c1x, c1y, c2x, c2y));
        emit VoteSubmitted(electionId, index, cipherHash, block.timestamp);
    }


    // -------------------------
    // Admin functions
    // -------------------------
    /// @notice Close the election early (or confirm closure) — only authority
    function closeElection() external onlyAuthority {
        require(!closed, "Election: already closed");
        closed = true;
        emit ElectionClosed(electionId, block.timestamp);
    }


    /// @notice Update tally verifier address (only authority)
    function setTallyVerifier(address _tallyVerifier) external onlyAuthority {
        tallyVerifier = _tallyVerifier;
    }

    // -------------------------
    // Tally finalization - called by tally system after verifying SNARK off-chain or on-chain
    // -------------------------
    /// @notice Accept final tallies after the tally SNARK has been verified by the external tally verifier
    /// @dev This function is protected by onlyTallyVerifier to ensure only the trusted verifier (or aggregator) can call it.
    /// @param tallies array of counts per candidate (order must match metadata)
    /// @param proofCid IPFS CID pointing to the proof bundle (proof, publicSignals, logs)
    function onTallyVerified(uint256[] calldata tallies, string calldata proofCid) external onlyTallyVerifier {
        require(!tallyPublished, "Election: tally already published");
        require(closed || block.timestamp > endTime, "Election: cannot publish tally before close");

        // store final tallies
        finalTally = tallies;

        // store proof cid and its hash
        tallyProofCid = proofCid;
        tallyProofCidHash = keccak256(bytes(proofCid));

        tallyPublished = true;
        finalTallyPublished = block.timestamp;

        emit TallyPublished(electionId, tallies, proofCid);
    }

    // -------------------------
    // View helpers
    // -------------------------
    function getEncryptedVotesCount() external view returns (uint256) {
        return encryptedVotes.length;
    }

    function getEncryptedVote(uint256 index) external view returns (uint256 c1x, uint256 c1y, uint256 c2x, uint256 c2y, uint256 ts) {
        require(index < encryptedVotes.length, "Election: index OOB");
        Ciphertext memory c = encryptedVotes[index];
        return (c.c1x, c.c1y, c.c2x, c.c2y, c.timestamp);
    }

    function isNullifierUsed(bytes32 nullifier) external view returns (bool) {
        return nullifierUsed[nullifier];
    }

    function getFinalTally() external view returns (uint256[] memory) {
        require(tallyPublished, "Election: tally not published");
        return finalTally;
    }
}
