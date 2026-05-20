{{/* vault/templates/tx-service.env.tpl */}}
{{ with secret "secret/data/tx-service/config" }}
RELAYER_PRIVATE_KEY={{ .Data.data.relayer_private_key }}
RPC_URL={{ .Data.data.rpc_url }}
SAFE_TX_SERVICE_URL={{ .Data.data.safe_tx_service_url }}
{{ end }}
