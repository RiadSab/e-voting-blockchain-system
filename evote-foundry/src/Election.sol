// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import {ReentrancyGuard} from "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import {ISemaphore} from "@semaphore/contracts/interfaces/ISemaphore.sol";

contract Election is ReentrancyGuard {
    // Events
    event VoteSubmitted(
        uint256[] c1x,
        uint256[] c1y,
        uint256[] c2x,
        uint256[] c2y,
        uint256 timestamp
    );
    event ElectionClosed(uint256 indexed electionId, uint256 timestamp);
    event TallyPublished(
        uint256 electionId,
        uint256[] tallies,
        string proofCid
    );
    event VoterRegistered(uint256 indexed identityCommitment, address addedBy);
    event TallyVerifierUpdated(
        address indexed oldVerifier,
        address indexed newVerifier
    );

    // forge-lint: disable-next-line(screaming-snake-case-immutable)
    uint256 public immutable electionId;
    // forge-lint: disable-next-line(screaming-snake-case-immutable)
    bytes32 public immutable metadataHash; // sha256(canonical_metadata_json), matches IPFS CID multihash
    uint256 public immutable publicKeyX;
    uint256 public immutable publicKeyY;
    uint256 public immutable votingStartTime;
    uint256 public immutable votingEndTime;
    uint256 public immutable registrationStartTime;
    uint256 public immutable registrationEndTime;
    uint256 public immutable candidatesCount;
    address public tallyVerifier; // address allowed to publish final tally
    address public electionAuthority; // later:multisig / safe address controlling admin operations

    // Semaphore integration
    address public immutable semaphore; // Semaphore core contract
    uint256 public immutable semaphoreGroupId; // Group used for this election

    bool public closed;

    uint256[] public finalTally; // published results (per-candidate counts)
    bytes32 public tallyProofHash; // sha256 digest of tally proof bundle
    uint256 public finalTallyPublished; // timestamp


    modifier onlyAuthority() {
        _onlyAuthority();
        _;
    }

    modifier onlyTallyVerifier() {
        _onlyTallyVerifier();
        _;
    }

    function _onlyAuthority() internal view {
        require(
            msg.sender == electionAuthority,
            "Election: caller not authority"
        );
    }

    function _onlyTallyVerifier() internal view {
        require(
            msg.sender == tallyVerifier,
            "Election: caller not tallyVerifier"
        );
    }

    constructor(
        uint256 _electionId,
        bytes32 _metadataHash,
        uint256 _pkx,
        uint256 _pky,
        uint256 _registrationStartTime,
        uint256 _registrationEndTime,
        uint256 _startTime,
        uint256 _endTime,
        uint256 _candidatesCount,
        address _tallyVerifier,
        address _electionAuthority,
        address _semaphore,
        uint256 _semaphoreGroupId
    ) {
        require(_startTime < _endTime, "Election: bad window");
        require(
            _registrationStartTime < _registrationEndTime,
            "Election: bad reg window"
        );
        require(
            _registrationEndTime <= _startTime,
            "Election: reg overlaps voting"
        );
        require(_tallyVerifier != address(0), "Election: zero tallyVerifier");
        require(_electionAuthority != address(0), "Election: zero authority");
        require(_semaphore != address(0), "Election: zero semaphore");
        require(_candidatesCount > 1, "Election: must have at least 2 candidates");
        candidatesCount = _candidatesCount;
        electionId = _electionId;
        metadataHash = _metadataHash;
        publicKeyX = _pkx;
        publicKeyY = _pky;
        votingStartTime = _startTime;
        votingEndTime = _endTime;
        registrationStartTime = _registrationStartTime;
        registrationEndTime = _registrationEndTime;
        tallyVerifier = _tallyVerifier;
        electionAuthority = _electionAuthority;

        // Semaphore config
        semaphore = _semaphore;
        semaphoreGroupId = _semaphoreGroupId;

    }

    /**
     * @notice votes are submitted in an encrypted form
     * @param semaProof Semaphore proof with fields:
     *        - merkleTreeDepth, merkleTreeRoot ,
     *        - nullifier (must be unused),
     *        - message = uint256(keccak256(electionId, c1x, c1y, c2x, c2y)),
     *        - scope = electionId,
     *        - points[8] = Groth16 proof points.
     */
    function submitVote(
        uint256[] calldata c1x,
        uint256[] calldata c1y,
        uint256[] calldata c2x,
        uint256[] calldata c2y,
        ISemaphore.SemaphoreProof calldata semaProof
    ) external nonReentrant {
        require(block.timestamp >= votingStartTime, "Election: not started");
        require(block.timestamp <= votingEndTime, "Election: finished");
        require(!closed, "Election: closed");
        require(c1x.length == candidatesCount && c1y.length == candidatesCount && c2x.length == candidatesCount && c2y.length == candidatesCount
        , "Election: wrong cyphertext count");
        bytes32 rawHash = keccak256(abi.encode(electionId, c1x, c1y, c2x, c2y));
        uint256 expectedMessage = uint256(rawHash) >> 8; // truncate to 248 bits, because Semaphore use 248-bit field

        require(
            semaProof.message == expectedMessage,
            "Election: bad message"
        );
        require(semaProof.scope == electionId, "Election: bad scope");

        ISemaphore(semaphore).validateProof(semaphoreGroupId, semaProof);

        // emit the ciphertext points, query it later from a node using the contract address and blocks timestamp
        emit VoteSubmitted(c1x, c1y, c2x, c2y, block.timestamp);
    }

    function registerVoter(uint256 identityCommitment) external onlyAuthority {
        require(
            block.timestamp >= registrationStartTime &&
                block.timestamp <= registrationEndTime,
            "Election: not in registration period"
        );
        ISemaphore(semaphore).addMember(semaphoreGroupId, identityCommitment);
        emit VoterRegistered(identityCommitment, msg.sender);
    }

    function closeElection() external onlyAuthority {
        require(!closed, "Election: already closed");
        closed = true;
        emit ElectionClosed(electionId, block.timestamp);
    }

    // emitting tallyVerifier address change to protect against authority silence modifications
    function setTallyVerifier(address _tallyVerifier) external onlyAuthority {
        require(
            !closed,
            "Election: cannot change verifier in a closed election"
        );
        require(_tallyVerifier != address(0), "Election: zero address");
        emit TallyVerifierUpdated(tallyVerifier, _tallyVerifier);
        tallyVerifier = _tallyVerifier;
    }

    /// @notice Later: Accept final tallies after the tally SNARK has been verified by the external tally verifier (for now I will use only a trusted tallyVerifier)
    /// @param tallies array of counts per candidate (order must match metadata)
    function onTallyVerified(
        uint256[] calldata tallies,
        string calldata proofCid,
        bytes32 _tallyProofHash
    ) external onlyTallyVerifier {
        require(finalTallyPublished == 0, "Election: tally already published");
        require(
            closed || block.timestamp > votingEndTime,
            "Election: cannot publish tally before close"
        );
        require(tallies.length == candidatesCount, "Election: wrong tally length");
        if (!closed) {
            closed = true;
            emit ElectionClosed(electionId, block.timestamp);
        }
        // store final tallies
        finalTally = tallies;

        // store proof cid

        tallyProofHash = _tallyProofHash;
        finalTallyPublished = block.timestamp;

        emit TallyPublished(electionId, tallies, proofCid);
    }

    function getFinalTally() external view returns (uint256[] memory) {
        require(finalTallyPublished != 0, "Election: tally not published");
        return finalTally;
    }
}
