const hre = require("hardhat");
const fs = require("fs");
const path = require("path");

async function main() {
  const networkName = hre.network.name;
  console.log(`🚀 Deploying to network: ${networkName}`);

  const CommitmentRegistry = await hre.ethers.getContractFactory("CommitmentRegistry");
  const registry = await CommitmentRegistry.deploy();
  await registry.waitForDeployment();

  const address = await registry.getAddress();
  console.log(`✅ Deployed to: ${address}`);

  // --- DIFFERENCE 1: Waiting for Block Confirmations ---
  // In Ganache, blocks are instant. In Prod, we wait to avoid "reorgs".
  if (networkName !== "ganache" && networkName !== "localhost") {
    console.log("⏳ Waiting for 5 block confirmations (Production safety)...");
    await registry.deploymentTransaction().wait(5);
  }

  // --- COMMON LOGIC: Save the file for Backend/Frontend ---
  const deploymentInfo = {
    address: address,
    network: networkName,
    timestamp: new Date().toISOString()
  };
  const artifactPath = path.join(__dirname, "../deployment-info.json");
  fs.writeFileSync(artifactPath, JSON.stringify(deploymentInfo, null, 2));
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});