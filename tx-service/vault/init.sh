#!/usr/bin/env bash

set -e

export VAULT_ADDR="http://127.0.0.1:8200"
export VAULT_TOKEN="root"

echo "[+] Enabling approle auth"
vault auth enable approle || true

echo "[+] Writing policy"
vault policy write tx-service /home/riad/e-voting-blockchain-system/vault/policies/tx-service-policy.hcl

echo "[+] Creating AppRole"
vault write auth/approle/role/tx-service \
  token_policies="tx-service" \
  token_ttl=1h \
  token_max_ttl=4h

echo "[+] Generating role-id"
vault read -field=role_id \
  auth/approle/role/tx-service/role-id \
  >/home/riad/e-voting-blockchain-system/tx-service/vault/role-id

echo "[+] Generating secret-id"
vault write -f -field=secret_id \
  auth/approle/role/tx-service/secret-id \
  >/home/riad/e-voting-blockchain-system/tx-service/vault/secret-id

echo "[+] Seeding secrets"
vault kv put secret/tx-service/config \
  relayer_private_key="dev-private-key" \
  rpc_url="http://localhost:8545" \
  safe_tx_service_url="http://localhost:8000"

vault agent -config=/home/riad/e-voting-blockchain-system/tx-service/vault/vault-agent.hcl

vault audit enable file file_path=/opt/vault/logs/audit.log
