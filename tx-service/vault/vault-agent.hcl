# vault/vault-agent.hcl

vault {
  address = "http://127.0.0.1:8200"  # your Vault address
}

auto_auth {
  method "approle" {
    config = {
      role_id_file_path   = "/home/riad/e-voting-blockchain-system/tx-service/vault/role-id"
      secret_id_file_path = "/home/riad/e-voting-blockchain-system/tx-service/vault/secret-id"
    }
  }

  # Agent writes the renewed token here automatically
  sink "file" {
    config = {
      path = "/home/riad/e-voting-blockchain-system/tx-service/vault/token.env"
    }
  }
}

template {
  source      = "/home/riad/e-voting-blockchain-system/tx-service/vault/templates/tx-service.env.tpl"
  destination = "/home/riad/e-voting-blockchain-system/tx-service/vault/secrets/tx-service.env"   # rendered output
  perms       = "0600"

  # Restart TX Service when secrets change (optional)
  # command = "kill -HUP <pid>"
}

#cache {
# use_auto_auth_token = true
#}
