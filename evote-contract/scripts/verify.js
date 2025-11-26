const hre = require("hardhat");
const fs = require("fs");     // Needed to read files
const path = require("path"); // Needed to find file paths

async function main() {
  // We look for the file created by deploy.js
  const artifactPath = path.join(__dirname, "../deployment-info.json");

  if (!fs.existsSync(artifactPath)) {
    console.error("❌ Error: No deployment found. Run 'npx hardhat run scripts/deploy.js' first.");
    return;
  }

  // Read the JSON file and extract the address
  const deployData = JSON.parse(fs.readFileSync(artifactPath, "utf8"));
  const contractAddress = deployData.address;

  console.log(`🔎 Connecting to contract at: ${contractAddress}`);

  // 2. Connect to the Contract
  // 'getContractAt' is a shortcut for 'getContractFactory' + 'attach'
  const registry = await hre.ethers.getContractAt("CommitmentRegistry", contractAddress);

  // 3. Query the blockchain
  const events = await registry.queryFilter("CommitmentSubmitted");

  if (events.length > 0) {
    console.log(`✅ Found ${events.length} event(s)!`);
    events.forEach((event, index) => {
        console.log(`\n--- Vote #${index + 1} ---`);
        console.log(`   Voter: ${event.args[0]}`);
        console.log(`   Hash:  ${event.args[1]}`);
    });
  } else {
    console.log("⚠️  No votes found. (Did you run 'seed-alice.js'?)");
  }
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});