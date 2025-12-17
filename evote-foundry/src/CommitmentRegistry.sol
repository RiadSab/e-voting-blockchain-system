// SPDX-License-Identifier: MIT
//! NOT USED ANYMORE, WILL BE DELETED LATER
pragma solidity ^0.8.19;

contract CommitmentRegistry {
    // 1. Storage: Maps a voter's address to their secret vote hash
    mapping(address => bytes32) public commitments;

    // 2. Event: Alerts the world (and Ganache) when a vote happens
    event CommitmentSubmitted(address indexed voter, bytes32 commitment);

    // 3. Logic: Allow a user to vote, but only once
    function submitCommitment(bytes32 _commitment) external {
        require(commitments[msg.sender] == bytes32(0), "Vote already committed");

        commitments[msg.sender] = _commitment;

        emit CommitmentSubmitted(msg.sender, _commitment);
    }
}