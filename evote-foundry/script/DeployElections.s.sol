// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "forge-std/Script.sol";
import "../src/ElectionFactory.sol";

contract DeployElections is Script {
    function run() external {
        vm.startBroadcast();



        // Set deployer as simple authority (no multisig for minimal setup)
        address authority = tx.origin;

        // Deploy factory with default tally verifier set to zero for minimal setup.
        // In production, we pass a real tally verifier address here.
        ElectionFactory factory = new ElectionFactory(
            authority,
            address(0)
        );

        // Example: create a sample election (replace with real values in app)
        string memory ipfsCid = "bafy...sample";
        bytes32 merkleRoot = bytes32(uint256(0x1234));
        uint256 pkx = 1;
        uint256 pky = 2;
        uint256 startTime = block.timestamp + 60; // starts in 1 minute
        uint256 endTime = startTime + 86400;      // lasts 1 day

        factory.createElection(
            ipfsCid,
            merkleRoot,
            pkx,
            pky,
            startTime,
            endTime,
            address(0), // use default tally verifier
            address(0), // semaphore core (none for minimal setup)
            0           // semaphore group id
        );

        console.log("Factory:", address(factory));

        // Persist addresses to deployments.json for backend consumption
        string memory obj = "deployments";
        vm.serializeAddress(obj, "factory", address(factory));
        vm.serializeAddress(obj, "tallyVerifier", address(0));
        vm.serializeString(obj, "chainId", toString(block.chainid));
        string memory json = vm.serializeString(obj, "network", "anvil-hardhat");
        vm.writeJson(json, "deployments/deployments.json");

        vm.stopBroadcast();
    }

    function toString(uint256 value) internal pure returns (string memory) {
        if (value == 0) {
            return "0";
        }
        uint256 temp = value;
        uint256 digits;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }
        bytes memory buffer = new bytes(digits);
        while (value != 0) {
            digits -= 1;
            buffer[digits] = bytes1(uint8(48 + uint256(value % 10)));
            value /= 10;
        }
        return string(buffer);
    }
}
