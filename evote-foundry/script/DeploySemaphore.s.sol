// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { Script } from "forge-std/Script.sol";
import { console } from "forge-std/console.sol";
import { stdJson } from "forge-std/StdJson.sol";

import { Semaphore } from "@semaphore/contracts/Semaphore.sol";
import { SemaphoreVerifier } from "@semaphore/contracts/base/SemaphoreVerifier.sol";
import { ISemaphoreVerifier } from "@semaphore/contracts/interfaces/ISemaphoreVerifier.sol";

using stdJson for string;

contract DeploySemaphore is Script {
	string internal constant DEPLOYMENTS_PATH = "deployments/deployments.json";

	function run() external {
		vm.startBroadcast();

		SemaphoreVerifier semaVerifier = new SemaphoreVerifier();
		Semaphore semaphore = new Semaphore(ISemaphoreVerifier(address(semaVerifier)));

		address admin = tx.origin;
		// Start group ids from 1 for this project: create and ignore group 0, then use group 1.
		semaphore.createGroup(admin);
		uint256 groupId = semaphore.createGroup(admin);

		// Do NOT add dummy members here. Identity commitments must be valid SNARK field elements;
		// the backend will add real members later.
		uint256 merkleRoot = semaphore.getMerkleTreeRoot(groupId);

		console.log("Semaphore:", address(semaphore));
		console.log("GroupId:", groupId);
		console.log("MerkleRoot:", merkleRoot);

		string memory existing = _readExistingDeployments();
		address existingFactory = _readAddress(existing, ".factory");
		address existingTallyVerifier = _readAddress(existing, ".tallyVerifier");
		string memory existingNetwork = _readString(existing, ".network");

		string memory obj = "deployments";
		// Preserve previously-deployed contracts (if present)
		if (existingFactory != address(0)) {
			vm.serializeAddress(obj, "factory", existingFactory);
		}
		if (existingTallyVerifier != address(0)) {
			vm.serializeAddress(obj, "tallyVerifier", existingTallyVerifier);
		}

		vm.serializeAddress(obj, "semaphore", address(semaphore));
		vm.serializeUint(obj, "semaphoreGroupId", groupId);
		vm.serializeUint(obj, "semaphoreMerkleRoot", merkleRoot);
		vm.serializeString(obj, "chainId", toString(block.chainid));
		string memory json = vm.serializeString(obj, "network", bytes(existingNetwork).length == 0 ? "anvil-hardhat" : existingNetwork);
		vm.writeJson(json, DEPLOYMENTS_PATH);

		vm.stopBroadcast();
	}

	function toString(uint256 value) internal pure returns (string memory) {
		if (value == 0) return "0";
		uint256 temp = value;
		uint256 digits;
		while (temp != 0) {
			digits++;
			temp /= 10;
		}
		bytes memory buffer = new bytes(digits);
		while (value != 0) {
			digits -= 1;
			// forge-lint: disable-next-line(unsafe-typecast)
			buffer[digits] = bytes1(uint8(48 + uint256(value % 10)));
			value /= 10;
		}
		return string(buffer);
	}

	function _readExistingDeployments() internal view returns (string memory) {
		if (!vm.exists(DEPLOYMENTS_PATH)) {
			return "";
		}
		return vm.readFile(DEPLOYMENTS_PATH);
	}

	function _readAddress(string memory json, string memory key) internal view returns (address) {
		if (bytes(json).length == 0) return address(0);
		return json.readAddressOr(key, address(0));
	}

	function _readString(string memory json, string memory key) internal view returns (string memory) {
		if (bytes(json).length == 0) return "";
		return json.readStringOr(key, "");
	}
}


// contract DeploySemaphore is Script {
// 	function run() external pure {
// 		// disabled
// 	}
// }
