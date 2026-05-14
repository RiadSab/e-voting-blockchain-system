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

		console.log("Semaphore:", address(semaphore));

		string memory existing = _readExistingDeployments();
		string memory chainKey = _chainKey();
		string memory prefix = string.concat(chainKey, ".");
		address existingFactory = _readAddress(
			existing,
			string.concat(prefix, "factory")
		);
		address existingTallyVerifier = _readAddress(
			existing,
			string.concat(prefix, "tallyVerifier")
		);
		string memory existingNetwork = _readString(
			existing,
			string.concat(prefix, "network")
		);

		string memory obj = "deployments";
		// Preserve previously-deployed contracts (if present)
		if (existingFactory != address(0)) {
			vm.serializeAddress(obj, "factory", existingFactory);
		}
		if (existingTallyVerifier != address(0)) {
			vm.serializeAddress(obj, "tallyVerifier", existingTallyVerifier);
		}

		vm.serializeAddress(obj, "semaphore", address(semaphore));
		vm.serializeString(obj, "chainId", toString(block.chainid));
		string memory json = vm.serializeString(
			obj,
			"network",
			bytes(existingNetwork).length == 0
				? _networkNameForChainId(block.chainid)
				: existingNetwork
		);
		_ensureDeploymentsFile();
		vm.writeJson(json, DEPLOYMENTS_PATH, chainKey);

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

	function _chainKey() internal view returns (string memory) {
		return string.concat(".", toString(block.chainid));
	}

	function _networkNameForChainId(uint256 chainId) internal pure returns (string memory) {
		if (chainId == 1) return "mainnet";
		if (chainId == 5) return "goerli";
		if (chainId == 11155111) return "sepolia";
		if (chainId == 17000) return "holesky";
		if (chainId == 31337) return "anvil";
		if (chainId == 10) return "optimism";
		if (chainId == 11155420) return "optimism-sepolia";
		if (chainId == 42161) return "arbitrum";
		if (chainId == 421614) return "arbitrum-sepolia";
		if (chainId == 8453) return "base";
		if (chainId == 84532) return "base-sepolia";
		if (chainId == 137) return "polygon";
		if (chainId == 80002) return "polygon-amoy";
		if (chainId == 56) return "bsc";
		if (chainId == 97) return "bsc-testnet";
		if (chainId == 43114) return "avalanche";
		if (chainId == 43113) return "avalanche-fuji";
		return string.concat("chain-", toString(chainId));
	}

	function _ensureDeploymentsFile() internal {
		if (!vm.exists(DEPLOYMENTS_PATH)) {
			vm.writeJson("{}", DEPLOYMENTS_PATH);
		}
	}
}