# /etc/vault/vault.hcl
ui            = true
disable_mlock = false

storage "file" {
  path = "/opt/vault/data"
}

listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = true   # put TLS in front via nginx/load balancer
}

api_addr = "http://127.0.0.1:8200"
