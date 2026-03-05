# OAuth2 / Keycloak Authentication Flow - Bruno Collection

This collection demonstrates the complete OAuth2 authentication flow with Keycloak as documented in `/keycloak/Authentication-Flow.md`.

## Flow Overview

The authentication flow consists of 8 main steps:

### 1. **User Accesses Protected Resource**
   - User attempts to POST to `/secure/score`
   - BFF returns 302 redirect to Keycloak authorization endpoint

### 2. **Authorization Request**
   - Browser redirected to Keycloak with client_id, response_type=code, redirect_uri, state
   - Keycloak shows login form

### 3. **User Login** (Manual Step)
   - User enters credentials (admin/admin)
   - Keycloak authenticates the user
   - Keycloak redirects back to redirect_uri with authorization code

### 4. **Exchange Authorization Code for Tokens**
   - BFF exchanges code for access_token and refresh_token (server-side)
   - Tokens are stored securely in the session

### 5. **Access Protected Resource with Token**
   - User sends request with Authorization header containing access token
   - BFF validates token and grants access

### 6. **Refresh Token** (When Expired)
   - When access token expires, use refresh_token to get a new one
   - Transparent refresh in real scenarios

### 7. **Access Public Resources**
   - Some endpoints don't require authentication
   - Accessible without tokens

### 8. **Logout**
   - User logs out and session is invalidated
   - Cannot access protected resources anymore

## Setup Instructions

### Prerequisites

1. **Keycloak Running**
   ```bash
   docker-compose up keycloak
   # or just: docker-compose up
   ```

2. **Spring App Running**
   ```bash
   # In another terminal
   ./gradlew clean bootRun --args='--spring.profiles.active=keycloak'
   ```

3. **Bruno Installed**
   - Download from https://www.usebruno.com/
   - Open this collection

### Environment Variables

Two environments are provided:

- **local**: For running Keycloak and App on localhost
  - BFF URL: http://localhost:8080
  - Keycloak URL: http://localhost:8081
  - Credentials: admin/admin

- **docker**: For running in Docker containers
  - BFF URL: http://spring-app:8080
  - Keycloak URL: http://keycloak:8080

## Running the Flow

### Option A: Manual Step-by-Step

1. Open Bruno and select **local** environment
2. Run **1. Access Secure Endpoint** - Get redirect URL
3. Run **2. Get Authorization Code** - Shows the authorization endpoint
4. **MANUALLY** (Important):
   - Open browser and navigate to the URL from step 2
   - Login with admin/admin
   - Copy the authorization code from the redirect URL
   - Paste it in the `authorization_code` variable (or the request body)
5. Run **4. Exchange Code for Token** - Get access token
6. Run **5. Access Secure Endpoint with Token** - Send score with authentication
7. Run **6. Refresh Token** - Refresh the access token
8. Run **7. Access Public Endpoint** - No auth required
9. Run **8. Logout** - Logout and invalidate session

### Option B: Automated (If Available)

If you have a script/automation layer:
- Some tooling can automate the browser login in step 3
- Then steps 4-8 can run automatically

## Understanding the Variables

```yaml
bff_url: http://localhost:8080          # Our Spring App
keycloak_url: http://localhost:8081     # Keycloak Server
client_id: bff-client                   # OAuth2 Client ID
client_secret: bff-secret               # OAuth2 Client Secret
redirect_uri: http://localhost:8080/login/oauth2/code/keycloak

username: admin                         # Keycloak Admin
password: admin                         # Keycloak Admin Password
state: xyz123                           # CSRF Protection
authorization_code: (empty)             # Populated after login
access_token: (empty)                   # Populated after code exchange
refresh_token: (empty)                  # Populated after code exchange
```

## Security Notes

⚠️ **Important Security Considerations**:

1. **Authorization Code**
   - Short-lived (typically 5-10 minutes)
   - Single-use only
   - Should only be sent from browser to backend

2. **Access Token**
   - Short-lived (typically 5 minutes)
   - Sent with each request
   - Should be refreshed when expired

3. **Refresh Token**
   - Long-lived (typically days/weeks)
   - Should be stored securely
   - In real scenarios: HTTP-only cookies or secure backend storage

4. **Client Secret**
   - Must be kept secret!
   - Never expose in frontend code
   - Only used in server-to-server communication

5. **HTTPS in Production**
   - Use HTTPS only in production
   - Set Secure and HttpOnly flags on cookies

## Troubleshooting

### 401 Unauthorized on Secure Endpoint
- Token may have expired
- Run "Refresh Token" to get a new access token
- Or repeat the flow from the beginning

### Invalid Client Secret
- Check the client credentials in Keycloak
- Default in dev: client_id=bff-client, client_secret=bff-secret

### Keycloak Not Responding
- Ensure Docker containers are running: `docker-compose ps`
- Check Keycloak health: http://localhost:8081 (with port forwarding)

### Redirect URI Mismatch
- Ensure redirect_uri matches the one configured in Keycloak
- Default: http://localhost:8080/login/oauth2/code/keycloak

## Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OAuth2 Authorization Code Flow](https://tools.ietf.org/html/rfc6749#section-1.3.1)
- [Spring Security OAuth2 Login](https://spring.io/projects/spring-security-oauth2-client)
- [Bruno Documentation](https://docs.usebruno.com/)

