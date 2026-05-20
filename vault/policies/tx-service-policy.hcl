# vault/policies/tx-service-policy.hcl

path "secret/data/tx-service/config" {
  capabilities = ["read"]
}

path "secret/metadata/tx-service/config" {
  capabilities = ["read"]
}
