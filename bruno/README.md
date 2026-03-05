# Bruno Collection - OAuth2 / Keycloak Authentication Flow

A complete Bruno API testing collection demonstrating the full OAuth2 Authorization Code Flow with Keycloak authentication.

## 📋 Overview

This collection implements the complete OAuth2 authentication flow as documented in `/keycloak/Authentication-Flow.md`:

```
┌──────────────────────────────────────────────────────────────────┐
│         OAuth2 Authorization Code Flow with Keycloak             │
├──────────────────────────────────────────────────────────────────┤
│ 1. User accesses protected resource                              │
│ 2. BFF redirects to Keycloak authorization endpoint              │
│ 3. User logs in to Keycloak                                      │
│ 4. Keycloak redirects back with authorization code               │
│ 5. BFF exchanges code for tokens (server-to-server)              │
│ 6. User accesses protected resources with token                  │
│ 7. User can refresh token when expired                           │
│ 8. User can access public resources                              │
│ 9. User can logout and invalidate session                        │
└──────────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Option 1: Automated Setup (Recommended)

```bash
cd bruno
./setup.sh
```

This script will:
- ✅ Check Docker and Docker Compose installation
- ✅ Start Keycloak and Spring App containers
- ✅ Wait for services to be healthy
- ✅ Display URLs and next steps

### Option 2: Manual Setup

#### Start Services

```bash
# Start Keycloak and Spring App
docker-compose up -d keycloak spring-app

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

#### Open Bruno Collection

1. Download and install [Bruno](https://www.usebruno.com/)
2. Open Bruno
3. Click "Open Collection" 
4. Navigate to `./bruno` directory
5. Select "local" environment from dropdown

## 📁 Collection Structure

```
bruno/
├── bruno.json                          # Collection metadata
├── setup.sh                            # Setup script
├── README.md                           # This file
├── QUICK-REFERENCE.md                  # Quick reference guide with examples
├── environments/
│   ├── local.bru                       # Local development (localhost)
│   └── docker.bru                      # Docker environment (container names)
└── Authentication Flow/
    ├── 1-access-secure-endpoint.bru    # Step 1: Trigger redirect to Keycloak
    ├── 2-get-authorization-code.bru    # Step 2: Show authorization endpoint
    ├── 3-keycloak-login.bru            # Step 3: Manual login step
    ├── 4-exchange-code-for-token.bru   # Step 4: Get tokens from code
    ├── 5-access-with-token.bru         # Step 5: Use token to access API
    ├── 6-refresh-token.bru             # Step 6: Refresh expired token
    ├── 7-access-public-endpoint.bru    # Step 7: Access public API
    ├── 8-logout.bru                    # Step 8: Logout and invalidate
    └── README.md                       # Flow-specific documentation
```

## 🔄 Authentication Flow Steps

### Step 1: Access Secure Endpoint
```bash
POST /secure/score
```
📌 **Result:** Get 302 redirect to Keycloak authorization endpoint

### Step 2: Get Authorization Code
```bash
GET /realms/my-realm/protocol/openid-connect/auth?
    client_id=bff-client&
    response_type=code&
    redirect_uri=...&
    state=xyz123
```
📌 **Result:** Keycloak shows login form

### Step 3: Keycloak Login (Manual)
Open browser and navigate to the URL from Step 2:
- Login with: **admin / admin**
- You'll be redirected with authorization code

### Step 4: Exchange Code for Tokens
```bash
POST /realms/my-realm/protocol/openid-connect/token
{
  "grant_type": "authorization_code",
  "code": "abc123...",
  "client_id": "bff-client",
  "client_secret": "bff-secret"
}
```
📌 **Result:** Get `access_token` and `refresh_token`

### Step 5: Access Protected Resource
```bash
POST /secure/score
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```
📌 **Result:** 201 Created - Score recorded successfully

### Step 6: Refresh Token (When Expired)
```bash
POST /realms/my-realm/protocol/openid-connect/token
{
  "grant_type": "refresh_token",
  "refresh_token": "xyz789...",
  "client_id": "bff-client",
  "client_secret": "bff-secret"
}
```
📌 **Result:** Get new `access_token`

### Step 7: Access Public Endpoint
```bash
GET /scores
```
📌 **Result:** 200 OK - No authentication required

### Step 8: Logout
```bash
POST /logout
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```
📌 **Result:** Session invalidated

## ⚙️ Environment Variables

Two environments are provided:

### Local Environment
```yaml
bff_url: http://localhost:8080          # Spring App
keycloak_url: http://localhost:8081     # Keycloak (docker-compose port mapping)
client_id: bff-client
client_secret: bff-secret
username: admin
password: admin
```

### Docker Environment
```yaml
bff_url: http://spring-app:8080         # Spring App container name
keycloak_url: http://keycloak:8080      # Keycloak container name
client_id: bff-client
client_secret: bff-secret
username: admin
password: admin
```

## 🔑 Key Concepts Demonstrated

### 1. Authorization Code Flow
The most secure OAuth2 flow for web applications
- Client never handles user credentials
- Authorization code is single-use, short-lived
- Token exchange happens server-to-server

### 2. Bearer Tokens
JWT tokens sent in Authorization header:
```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 3. Token Refresh
Refresh tokens allow getting new access tokens without re-authentication:
```
grant_type: refresh_token
```

### 4. State Parameter
CSRF protection during OAuth flow:
```
state=xyz123
```

### 5. Scope-based Authorization
Request specific permissions:
```
scope: openid profile email offline_access
```

## 🧪 Running the Collection

### Complete Flow (Step-by-Step)

1. **Select Environment:** Choose "local" from dropdown
2. **Run Step 1:** Access Secure Endpoint
3. **Run Step 2:** Get Authorization Code
4. **MANUAL Step 3:** Open browser, login to Keycloak
5. **Run Step 4:** Exchange Code for Token
6. **Run Step 5:** Access Protected Resource (uses auto-populated token)
7. **Run Step 6:** Refresh Token (optional)
8. **Run Step 7:** Access Public Endpoint
9. **Run Step 8:** Logout

### Individual Requests

You can also run individual requests:
- Test token refresh without manual login
- Test public endpoints
- Test expired token scenarios

## 📊 Service URLs

| Service | URL | Notes |
|---------|-----|-------|
| Spring App | http://localhost:8080 | Backend for Frontend (BFF) |
| Keycloak | http://localhost:8081 | Keycloak server |
| Keycloak Realm | my-realm | OAuth2 realm configuration |
| Keycloak Admin | http://localhost:8081/admin | Admin console |

## 🔐 Default Credentials

| Component | Username | Password |
|-----------|----------|----------|
| Keycloak | admin | admin |
| (OAuth2 scope is public, no special credentials needed) | - | - |

## 📝 Request/Response Examples

See `QUICK-REFERENCE.md` for detailed HTTP request/response examples

## ⚡ Useful Commands

### Docker Commands
```bash
# Start services
docker-compose up -d keycloak spring-app

# Stop services
docker-compose down

# View logs
docker-compose logs -f keycloak
docker-compose logs -f spring-app

# Restart services
docker-compose restart
```

### Access Keycloak Admin Console
1. Navigate to http://localhost:8081
2. Click "Administration Console"
3. Login with admin / admin
4. Navigate to Realm "my-realm"
5. Check client "bff-client" configuration

## 🐛 Troubleshooting

### Issue: "Cannot GET /secure/score" (404)
**Solution:** Make sure Spring App is running on port 8080

### Issue: "Keycloak not responding" (Connection refused)
**Solution:** 
- Check Docker containers: `docker-compose ps`
- Start services: `docker-compose up -d keycloak`
- Wait 30-60 seconds for startup

### Issue: "Invalid client secret" (401)
**Solution:** Verify credentials in environment variables:
- client_id: bff-client
- client_secret: bff-secret

### Issue: "redirect_uri_mismatch"
**Solution:** Check redirect_uri matches in:
- Environment variable
- Keycloak client configuration
- Default: http://localhost:8080/login/oauth2/code/keycloak

### Issue: "Authorization code has expired"
**Solution:** Authorization codes are valid for ~5 minutes. Restart from Step 1.

### Issue: "Token is invalid or expired"
**Solution:** 
- Use Step 6 to refresh the token
- Or restart from Step 1 to get new tokens

## 🔗 Related Documentation

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OAuth2 RFC 6749](https://tools.ietf.org/html/rfc6749)
- [OpenID Connect](https://openid.net/connect/)
- [Spring Security OAuth2](https://spring.io/projects/spring-security-oauth2-client)
- [Bruno Documentation](https://docs.usebruno.com/)

## 📚 Project Files

| File | Purpose |
|------|---------|
| `/keycloak/Authentication-Flow.md` | Original flow documentation |
| `/docker-compose.yml` | Docker services configuration |
| `/clean-archi/app/src/main/resources/application-keycloak.yml` | Spring OAuth2 config |
| `/keycloak/realm-export.json` | Keycloak realm configuration |

## 💡 Tips & Tricks

### Auto-populate Bearer Token
The collection automatically extracts and stores tokens:
- `access_token` from Step 4
- `refresh_token` from Step 4
- Used automatically in subsequent requests

### View Token Details
Decode JWT tokens at https://jwt.io to inspect:
- Token claims
- Expiration time
- Issued at time
- Token signature

### Monitor Token Expiration
Access tokens expire in ~5 minutes. Watch the test results for:
- Token expiration warnings
- Automatic refresh recommendations

### Test Different Scopes
Edit Step 2 to request different scopes:
```
openid profile email offline_access
```

## 🎯 Learning Outcomes

After using this collection, you'll understand:

✅ OAuth2 Authorization Code Flow  
✅ How Keycloak works as an OAuth2 provider  
✅ Bearer token authentication  
✅ Token refresh mechanism  
✅ State parameter for CSRF protection  
✅ Server-to-server vs client-server communication  
✅ Secure token storage practices  

## 📞 Support

For issues or questions:
1. Check the `QUICK-REFERENCE.md` for examples
2. Review Keycloak logs: `docker-compose logs keycloak`
3. Check Spring App logs: `docker-compose logs spring-app`
4. See `Authentication Flow/README.md` for detailed flow documentation

---

**Happy Testing!** 🚀

