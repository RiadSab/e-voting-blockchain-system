// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { Ownable } from "@openzeppelin/contracts/access/Ownable.sol";
import { ReentrancyGuard } from "@openzeppelin/contracts/utils/ReentrancyGuard.sol";

// this contract will be used for a trusted tally submition for now
//! LATER: verifier circuits will be implemented for a complete tally verification.

interface IElection{
    function onTallyVerified(uint256[] calldata tallies, string calldata proofCid) external;
}
interface IElectionFactory{
   function getElectionAddress(uint256 electionId) external view returns (address);
}

contract  TallyVerifier is Ownable, ReentrancyGuard{
    IElectionFactory factoryAddress;
    constructor(address _factoryAddress) Ownable(msg.sender){
        factoryAddress = IElectionFactory(_factoryAddress);
    }
    function submitFinalTally(uint256 electionId, uint256[] calldata tallies, string calldata ipfsCid) external onlyOwner nonReentrant {
        address electionAdress = factoryAddress.getElectionAddress(electionId);
        require(electionAdress != address(0), "Invalid election address");
        IElection(electionAdress).onTallyVerified(tallies, ipfsCid);
    }
}