# Full login flow - Authorization code flow

### User tries to access resource under /private uri
```declarative
POST  /secure/score
```
### BFF redirects user to Keycloak
```declarative
302 Location:
https://keycloak/authorize?
  client_id=bff-client
  response_type=code
  redirect_uri=https://bff/login/oauth2/code/keycloak
  state=xyz
```


### User logs in to Keycloak
```declarative
Browser → Keycloak login page
```

- Credentials entered

- MFA, password policies, etc.

- Keycloak authenticates the user

### Keycloak redirects user back to the BFF
```declarative
302 Location:
https://bff/login/oauth2/code/keycloak?code=abc&state=xyz
```


### BFF exchanges code for tokens (server side)
```declarative
Authorization Code → access token + refresh token

Happens backend to backend

Browser is not involved
```

### BFF Creates a session
```declarative
BFF:
  - stores tokens in session
  - stores SecurityContext
```

### BFF sends session cookie to front-end
```declarative
Set-Cookie: JSESSIONID=xyz; HttpOnly; Secure; SameSite=Strict
302 Location: /app
```


### Browser load the app (authenticated)
```declarative
Browser → GET /app
Cookie: JSESSIONID=xyz
```