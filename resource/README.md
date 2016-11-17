# Resource (As registered in Pivotal CF SSO Tile)
Sample application registered as resource for Pivotal SSO tile
## SSO Tile is configured in [Pez environment](https://api.run.pez.pivotal.io)
### Environment variables to configure
```
AUTH_SERVER: https://uaa-only.login.run.pez.pivotal.io
GRANT_TYPE: client_credentials
SKIP_SSL_VALIDATION: true
TRUST_CERTS: api.run.pez.pivotal.io
```
_**ResourceServerConfigurer** bean is referring to sso-resource as it is defined in SSO Tile of App Manager_
### Important properties
application.yml must include following configs
```
security:
  ignored: /favicon.ico, /
  basic:
    enabled: false
  require_ssl: false
  oauth2:
    resource:
      preferTokenInfo: false
      userInfoUri: ${AUTH_SERVER}/userinfo
      jwt:
        keyUri: ${AUTH_SERVER}/token_key
```
manifest.yml could have following entry if applicable
```
  env:
    SKIP_SSL_VALIDATION: "true"
```    
### Use curl to test resource setup.
Obtain app id and secret from Apps Manager --> SSO Application --> Next Steps
```
App ID,App Secret
207fb00e-f916-47b2-8be9-3beba6aa6c43,75b79d9f-0ec7-49ef-9b78-dfc296d52369
```
Get token
```
curl -XPOST -k https://uaa-only.login.run.pez.pivotal.io/oauth/token \
   -d grant_type=client_credentials -d client_id=207fb00e-f916-47b2-8be9-3beba6aa6c43 -d client_secret=75b79d9f-0ec7-49ef-9b78-dfc296d52369 \
   -d redirect_uri=http://sso-resource.cfapps.pez.pivotal.io
```   
Use token to execute resouce endpoint
```
TOKEN=eyJhbGciOiJSUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI1YjZkNjUxZTdhMWY0MDAxYWUwMDliYmFmOTFmYjcwZSIsInN1YiI6IjIwN2ZiMDBlLWY5MTYtNDdiMi04YmU5LTNiZWJhNmFhNmM0MyIsImF1dGhvcml0aWVzIjpbInVhYS5yZXNvdXJjZSIsInNzby1yZXNvdXJjZS53cml0ZSIsInNzby1yZXNvdXJjZS5yZWFkIl0sInNjb3BlIjpbInVhYS5yZXNvdXJjZSIsInNzby1yZXNvdXJjZS53cml0ZSIsInNzby1yZXNvdXJjZS5yZWFkIl0sImNsaWVudF9pZCI6IjIwN2ZiMDBlLWY5MTYtNDdiMi04YmU5LTNiZWJhNmFhNmM0MyIsImNpZCI6IjIwN2ZiMDBlLWY5MTYtNDdiMi04YmU5LTNiZWJhNmFhNmM0MyIsImF6cCI6IjIwN2ZiMDBlLWY5MTYtNDdiMi04YmU5LTNiZWJhNmFhNmM0MyIsImdyYW50X3R5cGUiOiJjbGllbnRfY3JlZGVudGlhbHMiLCJyZXZfc2lnIjoiN2E4YjU3M2IiLCJpYXQiOjE0NzkzNDQ5NjYsImV4cCI6MTQ3OTM4ODE2NiwiaXNzIjoiaHR0cHM6Ly91YWEtb25seS51YWEucnVuLnBlei5waXZvdGFsLmlvL29hdXRoL3Rva2VuIiwiemlkIjoiZjgzNDlhOTctMjU4NC00ZGY0LTgzNTgtYzhhZDE0OTVlNGE1IiwiYXVkIjpbIjIwN2ZiMDBlLWY5MTYtNDdiMi04YmU5LTNiZWJhNmFhNmM0MyIsInVhYSIsInNzby1yZXNvdXJjZSJdfQ.pVicoGar0rPrBUcTnt4Q4HJP5Ofs6x16lD_xCYaU4a9nmQkKoYlGcDfOQ6YfR0lPGE4JAd_sa25bg6bSSaAHAWjz9a9zQPrJyH7A5DHcZb3GQN1eRKHSEy1YTyV3yCeS1Q9Itacca61jAYM3Sn4KJ2qtmg8kLadzMqgd-snOl2QxBl-lF2QchHEYJVdYcc9eQJjoQHa3IceJzdtPTb7_S3hvwbVQq_mBftg_ZrqirDP7nK0GDpFWoVlQDJcb0b7-eFcBdQsdQsn28lweT6xiBrodk5bsMHMWG_25TcA45_7cYNa6Szqe7fO0JBqY-QTKJZu1gx74newoBtCEM7KsXA
curl -H "Authorization: Bearer $TOKEN" http://sso-resource.cfapps.pez.pivotal.io/ping
```
#### Resources
[Curl commands to obtain oAuth2 token blog](http://www.hascode.com/2016/03/setting-up-an-oauth2-authorization-server-and-resource-provider-with-spring-boot/)
[Debug JIT token](https://jwt.io/#debugger)
[CF Token REST API Reference](https://docs.cloudfoundry.org/api/uaa/#check-token)