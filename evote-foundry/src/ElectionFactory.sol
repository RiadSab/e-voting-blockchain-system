// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;


import { Ownable } from "@openzeppelin/contracts/access/Ownable.sol";
import { Address } from "@openzeppelin/contracts/utils/Address.sol";
import { ISemaphore } from "@semaphore/contracts/interfaces/ISemaphore.sol";
import { Election } from "./Election.sol";

contract ElectionFactory is Ownable {
    using Address for address;


    event ElectionCreated(
        uint256 indexed electionId,
        address indexed electionAddress,
        bytes32 metadataHash,
        string ipfsCid,
        uint256 startTime,
        uint256 endTime
    );


    // starts at 1 for clarity
    uint256 public electionCount;

    mapping(uint256 => address) public elections;

    address public electoralAuthority;
    address public semaphore;


    modifier onlyAuthority() {
        _onlyAuthority();
        _;
    }

    function _onlyAuthority() internal view {
        require(msg.sender == electoralAuthority, "Factory: caller not electoralAuthority");
    }



    constructor(address _electoralAuthority, address _semaphore) Ownable(_electoralAuthority) {
        electoralAuthority = _electoralAuthority;
        semaphore = _semaphore;
        electionCount = 1;
    }



    function setElectoralAuthority(address _authority) external onlyOwner {
        require(_authority != address(0), "Factory: authority zero address");
        electoralAuthority = _authority;
    }



    function createElection(
        string calldata ipfsCid, // for retrieval
        bytes32 metadataHash, // for content verification
        uint256 pkx,
        uint256 pky,
        uint256 startTime,
        uint256 endTime,
        address tallyVerifier,
        address electionAuthority
    )
        external
        onlyAuthority
        returns (uint256 newElectionId)
    {
        require(bytes(ipfsCid).length > 0, "Factory: ipfsCid required");
        require(startTime < endTime, "Factory: invalid time window");
        require(endTime > block.timestamp, "Factory: endTime in past");

        newElectionId = electionCount;
        electionCount += 1;

        uint256 groupId = ISemaphore(semaphore).createGroup(address(this));

        Election e = new Election(
            newElectionId,
            metadataHash,
            pkx,
            pky,
            startTime,
            endTime,
            tallyVerifier,
            electionAuthority,
            semaphore,
            groupId
        );

        elections[newElectionId] = address(e);

        emit ElectionCreated(newElectionId, address(e), metadataHash, ipfsCid, startTime, endTime);
        // we can get the electionId from the receipt of the election creation transaction
        //  ex : const event = receipt.logs[0];
        return newElectionId; // can be used by another contract
    }

    function getElectionAddress(uint256 electionId) external view returns (address) {
        return elections[electionId];
    }
}