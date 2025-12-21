#!/usr/bin/env node
/*
  One-shot generator for Spring Boot / Web3j.

  What it does:
  1) Runs `forge build`
  2) Extracts ABI + BIN from Foundry artifacts in `out/`
  3) If `web3j` CLI is installed, generates Java wrappers

  Usage:
    node tools/generate-web3j.js \
      --package com.example.contracts \
      --outDir generated \
      --wrappersDir generated/web3j \
      --contracts Election,ElectionFactory,TallyVerifier

  Notes:
  - This script expects to be run from the Foundry repo root (where foundry.toml exists).
  - Wrapper generation requires the `web3j` CLI on PATH.
*/

const fs = require("fs");
const path = require("path");
const { spawnSync } = require("child_process");

function fail(msg, code = 1) {
  console.error(`\n[generate-web3j] ERROR: ${msg}`);
  process.exit(code);
}

function info(msg) {
  console.log(`[generate-web3j] ${msg}`);
}

function parseArgs(argv) {
  const args = {
    package: "com.evote.backend.contract",
    outDir: "generated",
    // IMPORTANT: web3j `-o/--outputDir` must be the *Java source root*.
    // It will create the package folders under it.
    // Convenience default:
    // - If the sibling Spring Boot repo exists (../evote-backend), generate directly into its Java source root.
    // - Otherwise fall back to a repo-local folder for portability.
    wrappersDir: (() => {
      const candidate = path.resolve(process.cwd(), "../evote-backend/src/main/java");
      if (fs.existsSync(candidate)) return candidate;
      return "generated/web3j";
    })(),
    contracts: ["Election", "ElectionFactory", "TallyVerifier"],
    skipWrappers: false,
  };

  for (let i = 2; i < argv.length; i++) {
    const a = argv[i];
    const next = argv[i + 1];

    if (a === "--package" && next) {
      args.package = next;
      i++;
      continue;
    }
    if (a === "--outDir" && next) {
      args.outDir = next;
      i++;
      continue;
    }
    if (a === "--wrappersDir" && next) {
      args.wrappersDir = next;
      i++;
      continue;
    }
    if (a === "--javaSrcRoot" && next) {
      args.wrappersDir = next;
      i++;
      continue;
    }
    if (a === "--contracts" && next) {
      args.contracts = next.split(",").map((s) => s.trim()).filter(Boolean);
      i++;
      continue;
    }
    if (a === "--skipWrappers") {
      args.skipWrappers = true;
      continue;
    }
    if (a === "-h" || a === "--help") {
      console.log(`\nUsage:\n  node tools/generate-web3j.js [options]\n\nOptions:\n  --package <java.package>         Java package for wrappers (default: ${args.package})\n  --contracts A,B,C                Contract names to export (default: ${args.contracts.join(",")})\n  --outDir <dir>                   Output root (default: ${args.outDir})\n  --wrappersDir <dir>              Java source root for wrappers (default: ${args.wrappersDir})\n  --javaSrcRoot <dir>              Alias for --wrappersDir\n  --skipWrappers                   Only emit ABI/BIN, do not generate Java wrappers\n`);
      process.exit(0);
    }

    fail(`Unknown arg: ${a}`);
  }

  return args;
}

function run(cmd, args, opts = {}) {
  const res = spawnSync(cmd, args, { stdio: "inherit", ...opts });
  if (res.error) {
    fail(`Failed to run '${cmd}': ${res.error.message}`);
  }
  if (res.status !== 0) {
    fail(`Command failed (${res.status}): ${cmd} ${args.join(" ")}`, res.status);
  }
}

function runCapture(cmd, args, opts = {}) {
  const res = spawnSync(cmd, args, { encoding: "utf8", ...opts });
  if (res.error) return { ok: false, stdout: "", stderr: res.error.message, status: -1 };
  return { ok: res.status === 0, stdout: res.stdout || "", stderr: res.stderr || "", status: res.status };
}

function ensureRepoRoot(cwd) {
  const foundryToml = path.join(cwd, "foundry.toml");
  if (!fs.existsSync(foundryToml)) {
    fail(`Run this from the Foundry repo root (missing foundry.toml at ${foundryToml})`);
  }
}

function ensureDir(p) {
  fs.mkdirSync(p, { recursive: true });
}

function findFoundryArtifact(outDir, contractName) {
  // Foundry layout: out/<Source>.sol/<Contract>.json
  // We search for any file named <Contract>.json one directory below out/.
  const entries = fs.readdirSync(outDir, { withFileTypes: true });
  const candidates = [];
  for (const ent of entries) {
    if (!ent.isDirectory()) continue;
    const maybe = path.join(outDir, ent.name, `${contractName}.json`);
    if (fs.existsSync(maybe)) candidates.push(maybe);
  }
  if (candidates.length === 0) {
    return null;
  }
  // If multiple match, prefer the one where folder equals `${contractName}.sol`.
  const preferred = candidates.find((p) => path.basename(path.dirname(p)) === `${contractName}.sol`);
  return preferred || candidates[0];
}

function extractAbiAndBin(artifactPath) {
  const raw = fs.readFileSync(artifactPath, "utf8");
  const artifact = JSON.parse(raw);
  const abi = artifact.abi;
  const bytecode = artifact.bytecode && artifact.bytecode.object;
  if (!abi || !Array.isArray(abi)) {
    fail(`Artifact missing ABI array: ${artifactPath}`);
  }
  if (!bytecode || typeof bytecode !== "string" || bytecode.length === 0) {
    fail(`Artifact missing bytecode.object: ${artifactPath}`);
  }
  // web3j expects BIN as hex without a 0x prefix
  const cleanBytecode = bytecode.startsWith("0x") ? bytecode.slice(2) : bytecode;
  return { abi, bytecode: cleanBytecode };
}

function detectWeb3jCli() {
  const check = runCapture("web3j", ["--version"], { stdio: "pipe" });
  if (check.ok) return "web3j";
  return null;
}

function generateWrapper(web3jCmd, abiPath, binPath, outDir, javaPackage) {
  // Common CLI form:
  //   web3j generate solidity -a <abiFile> -b <binFile> -o <outDir> -p <package>
  // Try that first.
  const res = spawnSync(web3jCmd, [
    "generate",
    "solidity",
    "-a",
    abiPath,
    "-b",
    binPath,
    "-o",
    outDir,
    "-p",
    javaPackage,
  ], { stdio: "inherit" });

  if (res.status === 0) return;

  // Fallback to long flags used by some web3j versions.
  const res2 = spawnSync(web3jCmd, [
    "generate",
    "solidity",
    "--abiFile",
    abiPath,
    "--binFile",
    binPath,
    "--outputDir",
    outDir,
    "--package",
    javaPackage,
  ], { stdio: "inherit" });

  if (res2.status !== 0) {
    fail(
      `web3j wrapper generation failed. Ensure web3j CLI is installed and supports wrapper generation.\n` +
      `Tried:\n  web3j generate solidity -a <abi> -b <bin> -o <dir> -p <pkg>\n  web3j generate solidity --abiFile <abi> --binFile <bin> --outputDir <dir> --package <pkg>`
    );
  }
}

function main() {
  const args = parseArgs(process.argv);
  const cwd = process.cwd();
  ensureRepoRoot(cwd);

  info("Running forge build...");
  run("forge", ["build"], { cwd });

  const outArtifactsDir = path.join(cwd, "out");
  if (!fs.existsSync(outArtifactsDir)) {
    fail("forge build completed, but out/ directory is missing.");
  }

  const outputRoot = path.resolve(cwd, args.outDir);
  const abiOut = path.join(outputRoot, "abi");
  const binOut = path.join(outputRoot, "bin");
  // Allow absolute paths (recommended for writing into a different repo)
  const wrappersOut = path.isAbsolute(args.wrappersDir) ? args.wrappersDir : path.resolve(cwd, args.wrappersDir);

  ensureDir(abiOut);
  ensureDir(binOut);
  ensureDir(wrappersOut);

  info(`Exporting ABI/BIN for: ${args.contracts.join(", ")}`);

  const exported = [];

  for (const name of args.contracts) {
    const artifactPath = findFoundryArtifact(outArtifactsDir, name);
    if (!artifactPath) {
      fail(`Could not find Foundry artifact for contract '${name}' under out/*/${name}.json`);
    }

    const { abi, bytecode } = extractAbiAndBin(artifactPath);

    // IMPORTANT: name the ABI file <Contract>.abi so web3j generates <Contract>.java,
    // not <Contract>.abi.java (web3j uses the ABI filename stem for the class name).
    const abiPath = path.join(abiOut, `${name}.abi`);
    const binPath = path.join(binOut, `${name}.bin`);

    fs.writeFileSync(abiPath, JSON.stringify(abi, null, 2) + "\n", "utf8");
    fs.writeFileSync(binPath, bytecode + "\n", "utf8");

    exported.push({ name, abiPath, binPath, artifactPath });
    info(`Wrote ${name}:`);
    info(`  ABI: ${path.relative(cwd, abiPath)}`);
    info(`  BIN: ${path.relative(cwd, binPath)}`);
  }

  if (args.skipWrappers) {
    info("--skipWrappers set; skipping Java wrapper generation.");
    info(`Next: generate wrappers in your Spring Boot project (Gradle/Maven) or install web3j CLI and rerun without --skipWrappers.`);
    return;
  }

  const web3jCmd = detectWeb3jCli();
  if (!web3jCmd) {
    info("web3j CLI not found on PATH.");
    console.log(`\nTo generate wrappers automatically, install web3j CLI, then rerun this script.\n\nCommon options:\n- If you already use Gradle in Spring Boot: use the web3j Gradle plugin (recommended).\n- Or install the web3j CLI binary and ensure 'web3j' is on PATH.\n\nFor now, ABI/BIN have been generated under:\n  ${path.relative(cwd, outputRoot)}\n`);
    process.exit(0);
  }

  info(`Generating Java wrappers via web3j (package: ${args.package})...`);
  for (const item of exported) {
    generateWrapper(web3jCmd, item.abiPath, item.binPath, wrappersOut, args.package);
  }

  info("Done.");
  console.log(`\nOutputs:\n- ABI/BIN: ${path.relative(cwd, outputRoot)}\n- Java wrappers: ${path.relative(cwd, wrappersOut)}\n\nNext in Spring Boot:\n- Copy generated Java sources into your backend (or point your IDE/build to that folder).\n- Load addresses from deployments/deployments.json and use the generated wrappers with your Web3j instance.\n`);
}

main();
