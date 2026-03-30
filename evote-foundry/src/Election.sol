// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { ReentrancyGuard } from "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import { ISemaphore } from "@semaphore/contracts/interfaces/ISemaphore.sol";

contract Election is ReentrancyGuard {

    // Events
    event VoteSubmitted(uint256 c1x, uint256 c1y, uint256 c2x, uint256 c2y, uint256 timestamp);
    event ElectionClosed(uint256 indexed electionId, uint256 timestamp);
    event TallyPublished(uint256  electionId, uint256[] tallies, string proofCid);
    event VoterRegistered(uint256 indexed identityCommitment, address addedBy);

    // forge-lint: disable-next-line(screaming-snake-case-immutable)
    uint256 public immutable electionId;
    // forge-lint: disable-next-line(screaming-snake-case-immutable)
    bytes32 public immutable metadataHash; // keccak256(bytes(data))
    uint256 public publicKeyX;
    uint256 public publicKeyY;
    uint256 public startTime;
    uint256 public endTime;

    address public tallyVerifier;    // address allowed to publish final tally
    address public electionAuthority; // later:multisig / safe address controlling admin operations

    // Semaphore integration
    address public semaphore;        // Semaphore core contract
    uint256 public semaphoreGroupId; // Group used for this election



    bool public closed;
    bool public tallyPublished;

    uint256[] public finalTally; // published results (per-candidate counts)
    string public tallyProofCid; // IPFS CID of proof bundle
    uint256 public finalTallyPublished; // timestamp


    // Modifiers
    modifier onlyAuthority() {
        _onlyAuthority();
        _;
    }

    modifier onlyTallyVerifier() {
        _onlyTallyVerifier();
        _;
    }

    function _onlyAuthority() internal view {
        require(msg.sender == electionAuthority, "Election: caller not authority");
    }

    function _onlyTallyVerifier() internal view {
        require(msg.sender == tallyVerifier, "Election: caller not tallyVerifier");
    }


    constructor(
        uint256 _electionId,
        bytes32 _metadataHash,
        uint256 _pkx,
        uint256 _pky,
        uint256 _startTime,
        uint256 _endTime,
        address _tallyVerifier,
        address _electionAuthority,
        address _semaphore,
        uint256 _semaphoreGroupId
    ) {
        require(_startTime < _endTime, "Election: bad window");

        electionId = _electionId;
        metadataHash = _metadataHash;
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
        uint256 c1x,
        uint256 c1y,
        uint256 c2x,
        uint256 c2y,
        ISemaphore.SemaphoreProof calldata semaProof
    ) external nonReentrant {

        require(block.timestamp >= startTime, "Election: not started");
        require(block.timestamp <= endTime, "Election: finished");
        require(!closed, "Election: closed");

        bytes32 rawHash= keccak256(abi.encode(electionId, c1x, c1y, c2x, c2y));
        uint256 expectedMessage = uint256(rawHash) >> 8; // truncate to 248 bits, because Semaphore use 248-bit field

        require(semaProof.message == uint256(expectedMessage), "Election: bad message");
        require(semaProof.scope == electionId, "Election: bad scope");

        ISemaphore(semaphore).validateProof(semaphoreGroupId, semaProof);

        // emit the ciphertext points, query it later from a node using the contract address and blocks timestamp
        emit VoteSubmitted(c1x, c1y, c2x, c2y, block.timestamp);
    }

    function registerVoter(uint256 identityCommitment) external onlyAuthority {
        ISemaphore(semaphore).addMember(semaphoreGroupId, identityCommitment);
        emit VoterRegistered(identityCommitment, electionAuthority);
    }

    function closeElection() external onlyAuthority {
        require(!closed, "Election: already closed");
        closed = true;
        emit ElectionClosed(electionId, block.timestamp);
    }


    function setTallyVerifier(address _tallyVerifier) external onlyAuthority {
        tallyVerifier = _tallyVerifier;
    }


    /// @notice Later: Accept final tallies after the tally SNARK has been verified by the external tally verifier (for now I will use only a trusted later)
    /// @param tallies array of counts per candidate (order must match metadata)
    function onTallyVerified(uint256[] calldata tallies, string calldata proofCid) external onlyTallyVerifier {
        require(!tallyPublished, "Election: tally already published");
        require(closed || block.timestamp > endTime, "Election: cannot publish tally before close");
        
        // store final tallies
        finalTally = tallies;

        // store proof cid and its hash
        tallyProofCid = proofCid;

        tallyPublished = true;
        finalTallyPublished = block.timestamp;

        emit TallyPublished(electionId, tallies, proofCid);
    }


    function getFinalTally() external view returns (uint256[] memory) {
        require(tallyPublished, "Election: tally not published");
        return finalTally;
    }
}