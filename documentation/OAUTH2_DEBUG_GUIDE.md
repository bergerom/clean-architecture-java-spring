# Debugging OAuth2 & Keycloak Issues with Logging

## Using Debug Logs to Diagnose "Realm does not exist" Error

The error message `{"error": "Realm does not exist", ...}` can now be easily diagnosed with the logging setup.

### What to Look For

When you see this error, check the logs for:

```bash
docker-compose logs -f spring-app | grep -i "realm\|issuer\|keycloak"
```

## Common OAuth2 Issues & How to Debug Them

### Issue 1: "Realm does not exist"

**What this means**: 
- Keycloak is not finding the realm configuration
- OR the issuer URI is mismatched
- OR Keycloak is not running

**How to debug with logs**:

```bash
# Look for issuer discovery attempts
docker-compose logs -f spring-app | grep -i "issuer"

# Look for Keycloak connection errors
docker-compose logs -f spring-app | grep -i "keycloak"

# Look for realm validation
docker-compose logs -f spring-app | grep -i "realm"
```

**What you should see**:
```
[nio-8080-exec-1] DEBUG o.s.s.oauth2.client.web.OAuth2AuthenticationRequestRedirectFilter
 - Redirecting to: http://keycloak:8080/auth/realms/my-realm/protocol/openid-connect/auth?...
```

If you DON'T see this, the issuer might not be configured correctly.

### Issue 2: "Unauthorized" or "Invalid token"

**Debug with**:
```bash
docker-compose logs -f spring-app | grep -i "token\|unauthorized"
```

**Look for**:
- Token validation attempts
- Token expiration messages
- Signature validation failures

### Issue 3: Redirect URI mismatch

**Debug with**:
```bash
docker-compose logs -f spring-app | grep -i "redirect"
```

**You should see**:
```
Registered redirect: http://spring-app:8080/login/oauth2/code/keycloak
```

If this doesn't match what's configured in Keycloak, you'll get authorization errors.

### Issue 4: Connection refused to Keycloak

**Debug with**:
```bash
docker-compose logs -f spring-app | grep -i "connection\|refused\|timeout"
```

**Common causes**:
- Keycloak container not running: `docker-compose ps`
- Network issues: Check `docker-compose logs keycloak`
- Hostname resolution: Keycloak service name should be `keycloak`

### Issue 5: Slow requests / Timeouts

**Debug with**:
```bash
docker-compose logs spring-app | grep "Duration:" | awk -F'Duration: ' '{print $2}' | sort -rn | head -10
```

Shows your slowest requests.

## Step-by-Step Debugging Workflow

### 1. Make the request and capture RequestID

```bash
curl -v http://localhost:8080/secure/score
```

### 2. Find the request in logs

```bash
docker-compose logs spring-app | grep "REQUEST.*secure/score"
```

Get the RequestID (e.g., `1234567890`).

### 3. Trace the entire request lifecycle

```bash
docker-compose logs spring-app | grep "1234567890"
```

This shows you the complete journey of that specific request.

### 4. Increase verbosity for deeper debugging

```bash
# Change to TRACE level
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security.oauth2 \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'

# Make the request again
curl http://localhost:8080/secure/score

# View verbose logs
docker-compose logs -f spring-app | grep -i "oauth2"

# Reset
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security.oauth2 \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":null}'
```

## Key Log Patterns to Watch For

### OAuth2 Login Initiation
```
Redirecting to: http://keycloak:8080/auth/realms/my-realm/protocol/openid-connect/auth
```

### Authorization Code Exchange
```
Exchanging code [...] for access token
```

### Token Validation
```
Successfully validated ID Token signature
Successfully retrieved user info
```

### Errors
```
Failed to get access token
Invalid authorization code
Token has expired
```

## Specific Configuration Issues

### If issuer-uri is wrong:

```yaml
# WRONG - issuer-uri points to localhost but running in Docker
issuer-uri: http://localhost:8080/realms/my-realm

# CORRECT - uses service name inside Docker network
issuer-uri: http://keycloak:8080/realms/my-realm
```

**How to verify with logs**:
```bash
docker-compose logs -f spring-app | grep -i "issuer.uri"
```

You should see:
```
Issuer: http://keycloak:8080/realms/my-realm
```

### If client-id or client-secret are wrong:

```bash
docker-compose logs -f spring-app | grep -i "client.id\|client.secret"
```

**Check that values match Keycloak realm export**:
```bash
cat keycloak/realm-export.json | grep -A5 "clientId"
```

### If redirect-uri is wrong:

```bash
docker-compose logs -f spring-app | grep -E "redirect|callback"
```

The configured redirect URI in Keycloak must exactly match the Spring application.

## Network Debugging

### Test if services can reach each other

```bash
# From spring-app container, can you reach keycloak?
docker exec spring-bff curl -i http://keycloak:8080/realms/my-realm

# You should see JSON with realm configuration
```

### Check Docker network

```bash
# List networks
docker network ls

# Inspect the app network
docker network inspect clean-architecture-java-spring_app-net

# You should see both keycloak and spring-bff connected
```

## Performance Profiling with Logs

### Find slow OAuth2 operations

```bash
docker-compose logs spring-app | grep -E "Duration:|oauth2" | head -20
```

### Find slowest endpoints

```bash
docker-compose logs spring-app | grep "RESPONSE" | sort -t: -k4 -rn | head -10
```

## Save Logs for Analysis

```bash
# Save a snapshot of logs to file
docker-compose logs spring-app > logs_debug_`date +%Y%m%d_%H%M%S`.txt

# Follow logs and save to file simultaneously
docker-compose logs -f spring-app | tee logs_realtime_`date +%Y%m%d_%H%M%S`.txt
```

Then you can analyze them offline or share them for debugging help.

## Quick Diagnostic Script

```bash
#!/bin/bash
# save as: debug-oauth2.sh

echo "=== Docker Services Status ==="
docker-compose ps

echo ""
echo "=== Keycloak Realm Configuration ==="
docker exec keycloak curl -s http://localhost:8080/realms/my-realm | jq .

echo ""
echo "=== Spring App Current Config ==="
curl -s http://localhost:8080/actuator/env | jq '.propertySources[] | select(.name | contains("oauth2"))'

echo ""
echo "=== Recent Logs ==="
docker-compose logs --tail=30 spring-app | grep -E "REQUEST|RESPONSE|ERROR|WARN"

echo ""
echo "=== Network Connectivity Test ==="
docker exec spring-bff curl -i http://keycloak:8080/realms/my-realm | head -5
```

Usage:
```bash
chmod +x debug-oauth2.sh
./debug-oauth2.sh
```

## Related Log Files

See also:
- `DEBUG_LOGGING_GUIDE.md` - General logging guide
- `LOGGING_QUICK_REFERENCE.md` - All useful commands
- `application-docker.yml` - Where logging is configured
- `logback-spring.xml` - Logback configuration

## Pro Tips

1. **Use grep with context**: `docker-compose logs spring-app | grep -C 5 "ERROR"`
   - Shows 5 lines before and after the error

2. **Pipe to less for scrolling**: `docker-compose logs spring-app | less`
   - Use `/` to search, `q` to quit

3. **Combine multiple filters**: `docker-compose logs spring-app | grep -i oauth2 | grep -i error`
   - Shows only OAuth2 errors

4. **Watch specific class logs**: `docker-compose logs -f spring-app | grep "OAuth2LoginAuthenticationFilter"`
   - Focuses on one component

5. **Export and analyze**: Save logs to file and use `grep`, `awk`, `sed` for analysis

---

Now that you have detailed logging, diagnosing OAuth2 and Keycloak issues is much easier! 🔍

