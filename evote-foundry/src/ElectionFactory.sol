// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/*
  ElectionFactory.sol

  - Deploys one Election contract per election.
  - Keeps canonical registry: electionId => electionAddress
  - Stores minimal factory-level configuration and emits ElectionCreated events
  - Only electoralAuthority (an externally-managed multisig) can create elections.
  - Stores metadataHash (keccak256 of ipfsCid) on-chain and emits the ipfsCid in the event.
*/

/*
  IMPORTANT:
  This factory assumes an Election contract with the following constructor exists 

  constructor(
      uint256 electionId,
      string memory ipfsCid,
      bytes32 metadataHash,
      bytes32 merkleRoot,
      uint256 pkx,
      uint256 pky,
      uint256 startTime,
      uint256 endTime,
      address zkVoteVerifier,
      address tallyVerifier,
      address electionAuthority
  )

*/

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Address.sol";

import "./Election.sol"; 

contract ElectionFactory is Ownable {
    using Address for address;

    // -------------------------
    // Events
    // -------------------------
    /// @notice Emitted when a new election is created
    /// @param electionId Sequential id assigned by factory
    /// @param electionAddress Deployed Election contract address
    /// @param metadataHash keccak256(bytes(ipfsCid))
    /// @param ipfsCid IPFS CID (emitted for indexing; not stored in state)
    /// @param startTime election start unix time
    /// @param endTime election end unix time
    event ElectionCreated(
        uint256 indexed electionId,
        address indexed electionAddress,
        bytes32 indexed metadataHash,
        string ipfsCid,
        uint256 startTime,
        uint256 endTime
    );

    /// @notice Emitted when factory pause toggled
    event FactoryPaused(bool paused);

    /// @notice Emitted when global config updated
    event GlobalConfigUpdated(address defaultTallyVerifier);

    // -------------------------
    // Factory state
    // -------------------------
    /// @notice Next election id (starts at 1 for clarity)
    uint256 public electionCount;

    /// @notice canonical mapping: electionId -> deployed Election contract address
    mapping(uint256 => address) public elections;

    /// @notice Address of the electoral authority (the only actor who may create elections)
    /// Use a multisig (Gnosis Safe) in production; the factory owner (Ownable) can update this.
    address public electoralAuthority;

    /// @notice If true, creation of new elections is paused
    bool public creationPaused;

    /// @notice Minimal global defaults. These are optional defaults for new elections.
    struct GlobalConfig {
        address defaultTallyVerifier;  // address of default tally verifier contract
    }
    GlobalConfig public globalConfig;

    // -------------------------
    // Modifiers
    // -------------------------
    modifier onlyAuthority() {
        require(msg.sender == electoralAuthority, "Factory: caller not electoralAuthority");
        _;
    }

    modifier notPaused() {
        require(!creationPaused, "Factory: creation paused");
        _;
    }

    // -------------------------
    // Constructor
    // -------------------------
    /// @param _electoralAuthority address that will be allowed to create elections 
    /// @param _defaultTallyVerifier (optional) default tally verifier address used if caller passes zero
    constructor(address _electoralAuthority,  address _defaultTallyVerifier) Ownable(_electoralAuthority) {
        require(_electoralAuthority != address(0), "Factory: authority zero address");
        electoralAuthority = _electoralAuthority;
        globalConfig = GlobalConfig({
            defaultTallyVerifier: _defaultTallyVerifier
        });

        electionCount = 1;
    }

    // -------------------------
    // Admin / Owner functions
    // -------------------------
    /// @notice Update the electoral authority (only owner can call)
    /// @param _authority new authority address 
    function setElectoralAuthority(address _authority) external onlyOwner {
        require(_authority != address(0), "Factory: authority zero address");
        electoralAuthority = _authority;
    }

    /// @notice Pause or unpause creation of new elections (only owner)
    function setCreationPaused(bool _paused) external onlyOwner {
        creationPaused = _paused;
        emit FactoryPaused(_paused);
    }

    /// @notice Update global verifier defaults (only owner)
    function setGlobalConfig( address _defaultTallyVerifier) external onlyOwner {
        globalConfig.defaultTallyVerifier = _defaultTallyVerifier;
        emit GlobalConfigUpdated( _defaultTallyVerifier);
    }

    // -------------------------
    // Main factory function
    // -------------------------
    /**
     * @notice Create a new Election contract and register it in the factory registry.
     *
     * All parameters passed here become immutable in the new Election (constructor initializes them).
     * The factory stores the deployed address and emits an ElectionCreated event that includes the ipfsCid string
     * (so indexing tools can discover the metadata easily). The contract also stores metadataHash = keccak256(bytes(ipfsCid)).
     *
     * @dev Only callable by `electoralAuthority`. Basic time validation is enforced (start < end and end > now).
     *
     * @param ipfsCid        IPFS CID (string) of canonical election metadata (human-readable).
     * @param merkleRoot     bytes32 voter registry root (should correspond to the Semaphore group root).
     * @param pkx            ElGamal public key X coordinate (uint256).
     * @param pky            ElGamal public key Y coordinate (uint256).
     * @param startTime      unix timestamp voting starts.
     * @param endTime        unix timestamp voting ends.
     * @param tallyVerifier  address of the tally-circuit verifier. If address(0), the factory will use `globalConfig.defaultTallyVerifier`.
     *                       If both are zero, publishing the final tally will be impossible until updated.
     * @param semaphore      address of Semaphore core. If address(0), Semaphore-based vote submission is effectively disabled and
     *                       `submitVote` will revert; pass a valid core address to enable on-chain membership checks.
     * @param semaphoreGroupId uint256 group id registered in Semaphore for this election. Must match the group used by clients when
     *                         generating proofs if `semaphore` is non-zero.
     *
     * @return newElectionId the sequential id assigned to the newly created election.
     */
    function createElection(
        string calldata ipfsCid,
        bytes32 merkleRoot,
        uint256 pkx,
        uint256 pky,
        uint256 startTime,
        uint256 endTime,
        address tallyVerifier,
        address semaphore,
        uint256 semaphoreGroupId
    )
        external
        onlyAuthority
        notPaused
        returns (uint256 newElectionId)
    {
        require(bytes(ipfsCid).length > 0, "Factory: ipfsCid required");
        require(startTime < endTime, "Factory: invalid time window");
        require(endTime > block.timestamp, "Factory: endTime in past");

        // Use defaults if zero addresses passed
        address _tallyVerifier = tallyVerifier == address(0) ? globalConfig.defaultTallyVerifier : tallyVerifier;

        // Compute metadata hash (cheap anchor)
        bytes32 metadataHash = keccak256(bytes(ipfsCid));

        // Pull electionId and increment counter
        newElectionId = electionCount;
        electionCount += 1;

        // Deploy new Election contract.
        Election e = new Election(
            newElectionId,
            ipfsCid,
            metadataHash,
            merkleRoot,
            pkx,
            pky,
            startTime,
            endTime,
            _tallyVerifier,
            electoralAuthority,
            semaphore,
            semaphoreGroupId
        );

        // Store mapping
        elections[newElectionId] = address(e);

        // Emit event including the ipfsCid (for indexing)
        emit ElectionCreated(newElectionId, address(e), metadataHash, ipfsCid, startTime, endTime);

        return newElectionId;
    }

    // -------------------------
    // Views / helpers
    // -------------------------
    /// @notice Get the deployed election address for a given id
    function getElectionAddress(uint256 electionId) external view returns (address) {
        return elections[electionId];
    }

    /// @notice Convenience: check if an electionId is registered
    function isRegistered(uint256 electionId) external view returns (bool) {
        return elections[electionId] != address(0);
    }
}