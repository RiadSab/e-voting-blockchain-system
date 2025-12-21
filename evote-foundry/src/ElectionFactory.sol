// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/*
  - Deploys one Election contract per election.
  - Keeps canonical registry: electionId => electionAddress
  - Emits ElectionCreated events
  - Only electoralAuthority (an externally-managed multisig) can create elections.
  - Stores metadataHash and emits the ipfsCid in the event.
*/

import { Ownable } from "@openzeppelin/contracts/access/Ownable.sol";
import { Address } from "@openzeppelin/contracts/utils/Address.sol";

import { Election } from "./Election.sol";

contract ElectionFactory is Ownable {
    using Address for address;


    event ElectionCreated(
        uint256 indexed electionId,
        address indexed electionAddress,
        bytes32 indexed metadataHash,
        string ipfsCid,
        uint256 startTime,
        uint256 endTime
    );

    event FactoryPaused(bool paused);
    event GlobalConfigUpdated(address defaultTallyVerifier);


    // starts at 1 for clarity
    uint256 public electionCount;

    mapping(uint256 => address) public elections;

    address public electoralAuthority;

    bool public creationPaused;

    // These are defaults configs for new elections.
    struct GlobalConfig {
        address defaultTallyVerifier;  // address of default tally verifier contract
    }
    GlobalConfig public globalConfig;


    modifier onlyAuthority() {
        _onlyAuthority();
        _;
    }

    modifier notPaused() {
        _notPaused();
        _;
    }

    function _onlyAuthority() internal view {
        require(msg.sender == electoralAuthority, "Factory: caller not electoralAuthority");
    }

    function _notPaused() internal view {
        require(!creationPaused, "Factory: creation paused");
    }


    /// @param _defaultTallyVerifier (optional) default tally verifier address used if caller passes zero
    constructor(address _electoralAuthority,  address _defaultTallyVerifier) Ownable(_electoralAuthority) {
        electoralAuthority = _electoralAuthority;
        globalConfig = GlobalConfig({
            defaultTallyVerifier: _defaultTallyVerifier
        });

        electionCount = 1;
    }



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


    function createElection(
        string calldata ipfsCid,
        bytes32 metadataHash,
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


        // Pull electionId and increment counter
        newElectionId = electionCount;
        electionCount += 1;

        // Deploy new Election contract.
        Election e = new Election(
            newElectionId,
            metadataHash,
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

    function getElectionAddress(uint256 electionId) external view returns (address) {
        return elections[electionId];
    }
}