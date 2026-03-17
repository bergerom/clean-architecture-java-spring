# Debug Logging Setup for Spring BFF

## Overview

You now have comprehensive debug logging configured for your Spring BFF application. This includes:

1. ✅ **Application-level logging** - Configured in `application-docker.yml`
2. ✅ **Logback configuration** - Detailed in `logback-spring.xml`
3. ✅ **HTTP request/response logging** - Custom filter in `HttpLoggingFilter.java`
4. ✅ **Spring Security & OAuth2 logging** - Detailed Spring framework debugging

## Log Output Locations

### Console Output
When running Docker containers, logs appear in the console:
```bash
docker-compose logs -f spring-app
```

### File Output
Logs are also written to files inside the container:
- **Main application log**: `/logs/spring-app.log` (mounted or inside container)
- **HTTP requests log**: `/logs/http-requests.log` (optional, separate file)

## Log Levels Configuration

The current configuration logs at these levels:

| Component | Level | Description |
|-----------|-------|-------------|
| Root logger | `INFO` | General application logs |
| Spring Framework | `DEBUG` | All Spring internal operations |
| Spring Web | `DEBUG` | HTTP request/response handling |
| Spring Security | `DEBUG` | Authentication and authorization |
| Spring OAuth2 | `DEBUG` | OAuth2 client operations |
| Tomcat | `DEBUG` | Servlet container operations |
| App package | `DEBUG` | Your custom application code |

## What Gets Logged

### 1. HTTP Requests (via HttpLoggingFilter)
For each incoming HTTP request, you'll see:
```
═══════════════════════════════════════════════════════════════════════════════
REQUEST [1234567890] POST /secure/score
Query String: N/A
Content-Type: application/json
Headers:
  host: spring-app:8080
  content-type: application/json
  authorization: ***REDACTED***
Remote Address: 172.18.0.1
═══════════════════════════════════════════════════════════════════════════════
```

### 2. HTTP Responses
For each outgoing HTTP response, you'll see:
```
───────────────────────────────────────────────────────────────────────────────
RESPONSE [1234567890] HTTP 201
Content-Type: N/A
Duration: 45ms
Headers:
  content-length: 0
───────────────────────────────────────────────────────────────────────────────
```

### 3. Spring Security Operations
- Authentication attempts
- OAuth2 token validation
- Authorization decisions
- User principal creation

### 4. OAuth2 Keycloak Interactions
- Token exchange requests
- User info endpoint calls
- Discovery document loading
- Token validation

## Log Format

Logs follow this pattern:
```
[Timestamp] [Thread] [Level] [Logger Name] - [Message]
```

Example:
```
2026-03-10 07:34:06.830 [nio-8080-exec-1] DEBUG o.s.s.w.FilterChainProxy - Trying to match using RequestMatcher
```

## Viewing Logs

### Real-time Docker Logs
```bash
# Follow all logs
docker-compose logs -f spring-app

# Follow with grep filter (e.g., only OAuth2 logs)
docker-compose logs -f spring-app | grep -i oauth2

# Last 100 lines
docker-compose logs --tail=100 spring-app
```

### From Inside Container
```bash
# Enter the container
docker exec -it spring-bff sh

# View logs
cat logs/spring-app.log
tail -f logs/spring-app.log
```

### Accessing Log Files from Host
If you mount the logs directory as a volume, access them directly:
```bash
# Edit docker-compose.yml to add:
volumes:
  - ./logs:/logs

# Then view from host
tail -f logs/spring-app.log
```

## Security Considerations

The `HttpLoggingFilter` automatically redacts sensitive headers:
- ❌ `Authorization` headers
- ❌ `Cookie` headers
- ❌ `X-Auth-Token` headers
- ❌ Any header containing "password", "secret", or "token"

These are logged as `***REDACTED***` for security.

## Adjusting Log Levels at Runtime

With Spring Boot Actuator enabled, you can change log levels without restarting:

```bash
# Get current log level for a logger
curl http://localhost:8080/actuator/loggers/org.springframework.security

# Change log level to TRACE for more detail
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'

# Reset to current config
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":null}'
```

## Common Debug Scenarios

### Debugging OAuth2 Login Flow
```bash
docker-compose logs -f spring-app | grep -i oauth2
```

This will show:
- Token exchange requests
- Authorization code validation
- Access token creation
- Token refresh operations

### Debugging Request Failures (4xx/5xx)
```bash
docker-compose logs -f spring-app | grep "HTTP 40\|HTTP 50"
```

The filter automatically logs responses with status >= 400 at WARN level.

### Debugging Spring Security
```bash
docker-compose logs -f spring-app | grep -i "security\|filter"
```

This shows authentication and authorization decisions.

### Performance Analysis
Each response log includes `Duration: XXms`, making it easy to identify slow requests:
```bash
docker-compose logs spring-app | grep "Duration: " | sort -t: -k3 -rn | head -10
```

## Disabling Specific Loggers (if too verbose)

Edit `application-docker.yml` to reduce verbosity:

```yaml
logging:
  level:
    org.apache.tomcat: INFO  # Change from DEBUG to INFO
    org.springframework.web: INFO  # Less verbose web logging
```

Then restart the container:
```bash
docker-compose restart spring-app
```

## Log Files Rotation

Logs are automatically rotated:
- **Max file size**: 10MB per file
- **Max history**: 10 files kept
- **Naming**: `spring-app.2026-03-10.0.log`, `spring-app.2026-03-10.1.log`, etc.

## Integration with IDEs

### IntelliJ IDEA
1. Open `Run` → `Edit Configurations`
2. Select your Docker Compose configuration
3. Enable "Show Docker containers" in Services
4. Right-click container → View Logs

### VS Code
1. Install Docker extension
2. In Explorer → Containers
3. Right-click container → View Logs

## Example: Full Request-Response Cycle

Here's what a complete request-response looks like in the logs:

```
2026-03-10 07:34:20.150 [main] DEBUG app.config.HttpLoggingFilter - 
═══════════════════════════════════════════════════════════════════════════════
REQUEST [1234567890] POST /secure/score
Content-Type: application/json
Headers:
  authorization: ***REDACTED***
  content-type: application/json
═══════════════════════════════════════════════════════════════════════════════

2026-03-10 07:34:20.155 [nio-8080-exec-1] DEBUG o.s.security.web.FilterChainProxy - Trying to match using ...

2026-03-10 07:34:20.160 [nio-8080-exec-1] DEBUG o.s.security.oauth2.client.authentication.OAuth2AuthenticationToken - ...

2026-03-10 07:34:20.200 [nio-8080-exec-1] DEBUG app.controllers.SecureAPI - Adding score for user ...

2026-03-10 07:34:20.220 [main] DEBUG app.config.HttpLoggingFilter - 
───────────────────────────────────────────────────────────────────────────────
RESPONSE [1234567890] HTTP 201
Duration: 70ms
───────────────────────────────────────────────────────────────────────────────
```

## Next Steps

1. Rebuild and restart Docker:
   ```bash
   docker-compose up -d --build
   ```

2. Make a request to your API:
   ```bash
   curl -X POST http://localhost:8080/scores
   ```

3. View the logs:
   ```bash
   docker-compose logs -f spring-app
   ```

You should now see detailed debug information for every request!

