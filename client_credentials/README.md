# Client Credentials
Application demonstrates the oAuth2 client funcionality when securing microservices using Pivotal CF SSO Tile.
## SSO Tile is configured in [Pez environment](https://api.run.pez.pivotal.io)
### Define Identity Service
```
cf set-env oauth-resource TRUST_CERTS api.sys.dev.pcf.[somewhere].com
cf set-env oauth-client TRUST_CERTS api.sys.dev.pcf.[somewhere].com
cf cs p-identity sso-demo aa-identity-server
```
Configure resouce in the SSO Tile, and add scopes to the client application setup.
### Add following environment variables to manifest.yml
```
env:
  SKIP_SSL_VALIDATION: "true"
  GRANT_TYPE: client_credentials
  SSO_IDENTITY_PROVIDERS: uaa
```
### Disable basic auth in application.yml
```
security:
  ignored: /favicon.ico, /
  basic:
    enabled: false
```        
