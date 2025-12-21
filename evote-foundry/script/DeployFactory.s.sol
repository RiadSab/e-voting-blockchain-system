// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { Script } from "forge-std/Script.sol";
import { console } from "forge-std/console.sol";
import { stdJson } from "forge-std/StdJson.sol";

import { ElectionFactory } from "../src/ElectionFactory.sol";

using stdJson for string;

contract DeployFactory is Script {
    string internal constant DEPLOYMENTS_PATH = "deployments/deployments.json";

    function run() external {
        vm.startBroadcast();

        string memory existing = _readExistingDeployments();

        // Preserve previously-deployed contracts (if present)
        address existingSemaphore = _readAddress(existing, ".semaphore");
        uint256 existingSemaphoreGroupId = _readUint(existing, ".semaphoreGroupId");
        uint256 existingSemaphoreMerkleRoot = _readUint(existing, ".semaphoreMerkleRoot");
        address existingTallyVerifier = _readAddress(existing, ".tallyVerifier");
        string memory existingNetwork = _readString(existing, ".network");

        // Defaults: deployer is the authority. Optionally override with env vars.
        address authority = vm.envOr("ELECTORAL_AUTHORITY", tx.origin);
        address defaultTallyVerifier = vm.envOr("DEFAULT_TALLY_VERIFIER", existingTallyVerifier);

        ElectionFactory factory = new ElectionFactory(authority, defaultTallyVerifier);

        console.log("Factory:", address(factory));
        console.log("Electoral authority:", authority);
        console.log("Default tally verifier:", defaultTallyVerifier);

        string memory obj = "deployments";
        vm.serializeAddress(obj, "factory", address(factory));

        if (existingSemaphore != address(0)) {
            vm.serializeAddress(obj, "semaphore", existingSemaphore);
        }
        if (existingSemaphoreGroupId != 0) {
            vm.serializeUint(obj, "semaphoreGroupId", existingSemaphoreGroupId);
        }
        // Note: merkle root can be zero for empty group; preserve it anyway if present in file.
        vm.serializeUint(obj, "semaphoreMerkleRoot", existingSemaphoreMerkleRoot);

        if (existingTallyVerifier != address(0)) {
            vm.serializeAddress(obj, "tallyVerifier", existingTallyVerifier);
        }

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

    function _readUint(string memory json, string memory key) internal view returns (uint256) {
        if (bytes(json).length == 0) return 0;
        return json.readUintOr(key, 0);
    }

    function _readString(string memory json, string memory key) internal view returns (string memory) {
        if (bytes(json).length == 0) return "";
        return json.readStringOr(key, "");
    }
}
