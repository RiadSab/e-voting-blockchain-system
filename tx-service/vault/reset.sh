#!/usr/bin/env bash
set -e

BASE_DIR="/home/riad/e-voting-blockchain-system/tx-service/vault"

echo "[+] Cleaning Vault Agent generated artifacts"

rm -f "$BASE_DIR/token.env"
rm -f "$BASE_DIR/secrets/tx-service.env"

mkdir -p "$BASE_DIR/secrets"

echo "[+] Clean state ready"
