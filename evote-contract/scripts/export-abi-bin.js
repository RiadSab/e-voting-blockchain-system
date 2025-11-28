const fs = require("fs");
const path = require("path");

async function main() {

    const artifactPath = path.join(
    __dirname,
    "../artifacts/contracts/CommitmentRegistry.sol/CommitmentRegistry.json"
  );


  if (!fs.existsSync(artifactPath)) {
    console.error("Error: Artifact not found.");
    process.exit(1);
  }
  const artifact = JSON.parse(fs.readFileSync(artifactPath, "utf8"));

  // Define Output Paths (We'll put them in a 'build' folder)
  const buildDir = path.join(__dirname, "../build_web3j");
  if (!fs.existsSync(buildDir)) {
    fs.mkdirSync(buildDir);
  }

  const abiPath = path.join(buildDir, "CommitmentRegistry.abi");
  const binPath = path.join(buildDir, "CommitmentRegistry.bin");


  fs.writeFileSync(abiPath, JSON.stringify(artifact.abi)); // Save ABI
  fs.writeFileSync(binPath, artifact.bytecode);            // Save Bytecode

  console.log("Extraction Complete!");
  console.log(`   ABI: ${abiPath}`);
  console.log(`   BIN: ${binPath}`);
}

main();