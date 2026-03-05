# OAuth2 / Keycloak Authentication Flow - Quick Reference

## Visual Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    OAuth2 Authorization Code Flow                        │
└─────────────────────────────────────────────────────────────────────────┘

                           Browser                  BFF                Keycloak
                            │                        │                     │
1. Access Secure            │                        │                     │
   Resource                 │─POST /secure/score────>│                     │
                            │                        │                     │
2. Redirect to Keycloak     │<─302 Redirect───────────│                     │
                            │                        │                     │
3. Login Form               │                        │<─Show Login Form─────│
                            │<─────────Login Page─────────────────────────│
                            │                        │                     │
4. User Submits             │─POST /login(creds)────────────────────────>│
   Credentials              │                        │                     │
                            │                        │                     │
5. Keycloak Validates       │                        │   [Validate User]   │
   & Redirects              │<──302 Redirect (code)──────────────────────│
                            │                        │                     │
6. Exchange Code            │─POST /token────────────>│                     │
   for Tokens               │  (code, secret)        │─POST /token────────>│
   (Backend)                │                        │  (code, secret)     │
                            │                        │                     │
7. Return Tokens            │<─────────Tokens (access + refresh)─────────│
   (Backend)                │                        │                     │
                            │                        │                     │
8. Session Created          │<─Set-Cookie (JWT/Sid)──│                     │
                            │  & Redirect /app───────│                     │
                            │                        │                     │
9. Access Protected         │─POST /secure/score────>│                     │
   with Token/Cookie        │  (Bearer token)        │                     │
                            │                        │                     │
10. Token Validated         │<─201 Created───────────│                     │
                            │                        │                     │

```

## HTTP Request/Response Examples

### 1. Initial Request to Secure Endpoint
```http
POST /secure/score HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "gameSessionId": "550e8400-e29b-41d4-a716-446655440000",
  "score": 100,
  "date": "2026-03-05T10:00:00Z"
}
```

**Response:**
```http
HTTP/1.1 302 Found
Location: http://localhost:8081/realms/my-realm/protocol/openid-connect/auth?
          client_id=bff-client&
          response_type=code&
          redirect_uri=http://localhost:8080/login/oauth2/code/keycloak&
          state=xyz123&
          scope=openid profile email offline_access
```

### 2. Authorization Request to Keycloak
```http
GET /realms/my-realm/protocol/openid-connect/auth?
    client_id=bff-client&
    response_type=code&
    redirect_uri=http://localhost:8080/login/oauth2/code/keycloak&
    state=xyz123&
    scope=openid profile email offline_access HTTP/1.1
Host: localhost:8081
```

**Response:**
```http
HTTP/1.1 200 OK
Content-Type: text/html

<html>
  <form method="POST" action="/realms/my-realm/login-actions/authenticate">
    <input type="text" name="username" placeholder="Username or email" />
    <input type="password" name="password" placeholder="Password" />
    <button type="submit">Sign In</button>
  </form>
</html>
```

### 3. User Credentials Submitted (Manual Step)
```http
POST /realms/my-realm/login-actions/authenticate HTTP/1.1
Host: localhost:8081
Content-Type: application/x-www-form-urlencoded

username=admin&password=admin
```

**Response:**
```http
HTTP/1.1 302 Found
Location: http://localhost:8080/login/oauth2/code/keycloak?
          code=abc123def456&
          state=xyz123
```

### 4. Exchange Authorization Code for Tokens (Backend)
```http
POST /realms/my-realm/protocol/openid-connect/token HTTP/1.1
Host: localhost:8081
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&
code=abc123def456&
client_id=bff-client&
client_secret=bff-secret&
redirect_uri=http://localhost:8080/login/oauth2/code/keycloak
```

**Response:**
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "not-before-policy": 0,
  "session_state": "12345678-abcd-ef01-2345",
  "scope": "openid profile email offline_access"
}
```

### 5. Access Protected Resource with Token
```http
POST /secure/score HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "gameSessionId": "550e8400-e29b-41d4-a716-446655440000",
  "score": 100,
  "date": "2026-03-05T10:00:00Z"
}
```

**Response:**
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "message": "Score recorded successfully"
}
```

### 6. Refresh Token (When Access Token Expires)
```http
POST /realms/my-realm/protocol/openid-connect/token HTTP/1.1
Host: localhost:8081
Content-Type: application/x-www-form-urlencoded

grant_type=refresh_token&
refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...&
client_id=bff-client&
client_secret=bff-secret
```

**Response:**
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "token_type": "Bearer",
  ...
}
```

---

## Bruno Collection Usage Guide

### Files in the Collection

```
bruno/
├── bruno.json                          # Collection metadata
├── environments/
│   ├── local.bru                       # Local development environment
│   └── docker.bru                      # Docker environment
└── Authentication Flow/
    ├── 1-access-secure-endpoint.bru    # Step 1
    ├── 2-get-authorization-code.bru    # Step 2
    ├── 3-keycloak-login.bru            # Step 3 (manual)
    ├── 4-exchange-code-for-token.bru   # Step 4
    ├── 5-access-with-token.bru         # Step 5
    ├── 6-refresh-token.bru             # Step 6
    ├── 7-access-public-endpoint.bru    # Step 7
    ├── 8-logout.bru                    # Step 8
    └── README.md                       # Detailed documentation
```

### Running the Collection

#### Prerequisites
1. Start Keycloak: `docker-compose up keycloak`
2. Start Spring App: `./gradlew clean bootRun --args='--spring.profiles.active=keycloak'`
3. Open Bruno and load the collection

#### Step-by-Step Execution

| Step | Request | Environment | Action | Notes |
|------|---------|-------------|--------|-------|
| 1 | Access Secure Endpoint | local | Run | Get redirect to Keycloak |
| 2 | Get Authorization Code | local | Run | Shows auth endpoint |
| 3 | Keycloak Login | local | **Manual** | Open browser, login with admin/admin, copy code |
| 4 | Exchange Code for Token | local | Run | Get access_token automatically |
| 5 | Access with Token | local | Run | Token auto-populated from step 4 |
| 6 | Refresh Token | local | Run | Get new token when expired |
| 7 | Public Endpoint | local | Run | No auth required |
| 8 | Logout | local | Run | Invalidate session |

---

## Key Variables Explained

| Variable | Value (local) | Purpose |
|----------|---|---------|
| `bff_url` | http://localhost:8080 | Our Spring Boot Backend for Frontend |
| `keycloak_url` | http://localhost:8081 | Keycloak server (port 8081 in docker-compose) |
| `client_id` | bff-client | OAuth2 client identifier |
| `client_secret` | bff-secret | OAuth2 client secret (keep secret!) |
| `redirect_uri` | http://localhost:8080/login/oauth2/code/keycloak | Where Keycloak redirects after login |
| `username` | admin | Keycloak admin username |
| `password` | admin | Keycloak admin password |
| `state` | xyz123 | CSRF protection token |
| `authorization_code` | (auto-populated) | Single-use code from Keycloak |
| `access_token` | (auto-populated) | JWT token for API requests |
| `refresh_token` | (auto-populated) | Token to refresh access_token |

---

## Security Best Practices Demonstrated

### ✅ What This Collection Shows

1. **Authorization Code Flow** - Industry standard for OAuth2
2. **Server-to-Server Communication** - Code exchange happens backend-to-backend
3. **Token Usage** - Bearer tokens in Authorization header
4. **Token Refresh** - Refresh tokens for long-lived sessions
5. **State Parameter** - CSRF protection during login flow

### ⚠️ Security Notes

1. **Client Secret** - Should never be exposed in frontend code
2. **Redirect URI Validation** - Must match exactly with configured URI
3. **HTTPS in Production** - Always use HTTPS in production
4. **Token Storage** - Use HTTP-only cookies or secure backend storage
5. **Short-lived Tokens** - Access tokens expire in ~5 minutes
6. **Token Rotation** - Refresh tokens to get new access tokens

---

## Troubleshooting

### Issue: "Invalid redirect_uri"
**Solution:** Ensure redirect_uri matches exactly in:
- Bruno environment variable
- Keycloak client configuration

### Issue: "Unknown client"
**Solution:** Check client_id is correct (should be `bff-client`)

### Issue: "Invalid client credentials"
**Solution:** Verify client_secret (should be `bff-secret`)

### Issue: "Authorization code has expired"
**Solution:** Authorization codes are valid for ~5 minutes. Repeat the flow from step 1.

### Issue: "Token has expired"
**Solution:** Run the "Refresh Token" request to get a new access_token

### Issue: "Invalid scope"
**Solution:** Some Keycloak realms don't support all scopes. Try: `openid profile email`

---

## Related Files

- `/keycloak/Authentication-Flow.md` - Original flow documentation
- `/clean-archi/app/src/main/resources/application-keycloak.yml` - OAuth2 configuration
- `/docker-compose.yml` - Docker setup with Keycloak


