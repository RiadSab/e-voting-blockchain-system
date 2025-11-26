const hre = require("hardhat");
const fs = require("fs");
const path = require("path");

async function main() {
  console.log("🧪 Starting Alice's Test Vote...");

  // 1. Read the address of the ALREADY deployed contract
  // This ensures we are testing the exact same machine we just built
  const artifactPath = path.join(__dirname, "../deployment-info.json");
  if (!fs.existsSync(artifactPath)) {
    console.error("❌ Error: Contract not deployed. Run deploy.js first!");
    return;
  }
  
  const deployData = JSON.parse(fs.readFileSync(artifactPath, "utf8"));
  const contractAddress = deployData.address;
  console.log(`📍 Attaching to contract at: ${contractAddress}`);

  // 2. Attach to the contract
  const registry = await hre.ethers.getContractAt("CommitmentRegistry", contractAddress);

  // 3. Create the Fake Vote (Alice)
  // Real world: Keccak256(Vote + Salt)
  const voteHash = hre.ethers.id("AliceVote:CandidateA|Salt:12345");
  console.log(`📝 Prepared Commitment: ${voteHash}`);

  // 4. Submit the Vote
  console.log("🚀 Submitting transaction...");
  const tx = await registry.submitCommitment(voteHash);
  
  // 5. Wait for it to be mined
  await tx.wait();

  console.log("🎉 Alice's vote is confirmed on-chain!");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});