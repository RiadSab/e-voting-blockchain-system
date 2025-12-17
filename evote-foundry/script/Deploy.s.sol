// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import "forge-std/Script.sol";
import "../src/CommitmentRegistry.sol";

contract DeployScript is Script {
    function run() external {
        // 1. Start Broadcasting
        vm.startBroadcast();

        // 2. Deploy
        CommitmentRegistry registry = new CommitmentRegistry();

        // 3. Stop Broadcasting
        vm.stopBroadcast();

        // 4. Log for Humans (Optional)
        console.log("Deployed CommitmentRegistry at:", address(registry));
       }
}
