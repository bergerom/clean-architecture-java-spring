# 🔍 Debug Logging Setup Complete

## Summary

You now have comprehensive debug logging configured for your Spring BFF application to see detailed logs for each request.

## What Was Added

### 1. ✅ Enhanced Configuration File
**File**: `application-docker.yml`

Added detailed logging configuration including:
- Spring Framework debugging (DEBUG level)
- Spring Security & OAuth2 debugging (DEBUG level)
- Tomcat servlet debugging (DEBUG level)
- Application logging (DEBUG level)
- Log patterns and file rotation

### 2. ✅ Logback Configuration
**File**: `logback-spring.xml`

Professional logging configuration with:
- Console and file appenders
- Spring profile-aware log levels
- Rolling file policy (10MB per file, 10 files kept)
- Separate HTTP request logging (optional)

### 3. ✅ HTTP Request/Response Logging Filter
**File**: `HttpLoggingFilter.java`

Custom servlet filter that logs:
- **Every incoming request**: Method, URI, headers, parameters
- **Every outgoing response**: Status code, duration, response headers
- **Security**: Automatically redacts sensitive headers (Authorization, Cookie, etc.)
- **Performance**: Measures request duration
- **Smart filtering**: Skips health check endpoints

### 4. ✅ Documentation
Created three guide documents:
- **DEBUG_LOGGING_GUIDE.md** - Comprehensive guide with examples
- **LOGGING_QUICK_REFERENCE.md** - Quick commands for common tasks
- **CONFIG_HOT_RELOAD.md** - Hot reload configuration changes

## How to Use

### Quick Start

1. **Start Docker containers** (rebuild to pick up new filter):
```bash
docker-compose down
docker-compose up -d --build
```

2. **View logs in real-time**:
```bash
docker-compose logs -f spring-app
```

3. **Make a test request**:
```bash
curl http://localhost:8080/scores
```

### View Specific Logs

| What | Command |
|------|---------|
| OAuth2 flow | `docker-compose logs -f spring-app \| grep -i oauth2` |
| Errors only | `docker-compose logs -f spring-app \| grep -E "ERROR\|WARN"` |
| Request/Response | `docker-compose logs -f spring-app \| grep -E "REQUEST\|RESPONSE"` |
| Security events | `docker-compose logs -f spring-app \| grep -i security` |
| Keycloak issues | `docker-compose logs -f spring-app \| grep -i keycloak` |
| Performance | `docker-compose logs spring-app \| grep "Duration:"` |

### Change Log Levels at Runtime (No Restart!)

```bash
# Increase verbosity to TRACE
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'

# Reset to default
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":null}'
```

## What You'll See in Logs

### Example: Successful Request

```
═══════════════════════════════════════════════════════════════════════════════
REQUEST [1234567890] GET /scores
Content-Type: N/A
Headers:
  host: localhost:8080
  user-agent: curl/7.88.1
Remote Address: 172.18.0.1
═══════════════════════════════════════════════════════════════════════════════

2026-03-10 07:34:20.150 [nio-8080-exec-1] DEBUG o.s.security.web.FilterChainProxy - Trying to match using ...

───────────────────────────────────────────────────────────────────────────────
RESPONSE [1234567890] HTTP 200
Content-Type: application/json
Duration: 45ms
───────────────────────────────────────────────────────────────────────────────
```

### Components Logged

| Component | What You See |
|-----------|-------------|
| `HttpLoggingFilter` | All requests/responses with timing |
| `Spring Security` | Authentication, authorization decisions |
| `Spring OAuth2` | Token exchange, Keycloak communication |
| `Spring Web` | Controller routing, dispatcher servlet |
| `Tomcat` | Servlet container operations |
| `Your code` | Application-level debug statements |

## Log Output Locations

### Console (Docker Compose)
```bash
docker-compose logs -f spring-app
```

### Inside Container
```bash
docker exec -it spring-bff tail -f logs/spring-app.log
```

### From Host (if mounted)
```bash
tail -f logs/spring-app.log  # If you add volume mount
```

## Key Features

✅ **Request Tracing**: Track every request from entry to exit
✅ **Performance Monitoring**: See request duration for each call
✅ **Security**: Sensitive headers are automatically redacted
✅ **Filtering**: Easy to filter logs by endpoint, error level, or component
✅ **Hot Reload**: Change log levels without restarting (via Actuator)
✅ **File Rotation**: Logs automatically rotate at 10MB
✅ **OAuth2 Debugging**: See Keycloak interactions in detail
✅ **Spring Security**: Full visibility into authentication/authorization

## Troubleshooting

### No logs appearing?
1. Ensure container is running: `docker-compose ps`
2. Rebuild with new filter: `docker-compose up -d --build`
3. Check container logs: `docker-compose logs spring-app`

### Too much logging?
Edit `application-docker.yml` and reduce log levels:
```yaml
logging:
  level:
    org.springframework.web: INFO  # Less verbose
    org.apache.tomcat: INFO        # Less verbose
```
Then restart: `docker-compose restart spring-app`

### Only seeing INFO level logs?
Check that the filter compiled correctly:
```bash
docker exec spring-bff ls app/config/HttpLoggingFilter.class
```

### Logs not showing request bodies?
The filter logs headers but not request bodies (to avoid logging sensitive data). To see bodies, add custom logging in your controllers.

## Files Modified/Created

| File | Type | Purpose |
|------|------|---------|
| `application-docker.yml` | Modified | Enhanced logging configuration |
| `logback-spring.xml` | New | Logback configuration |
| `HttpLoggingFilter.java` | New | HTTP logging servlet filter |
| `DEBUG_LOGGING_GUIDE.md` | New | Comprehensive documentation |
| `LOGGING_QUICK_REFERENCE.md` | New | Quick command reference |

## Next Steps

1. **Rebuild and test**:
   ```bash
   docker-compose down
   docker-compose up -d --build
   ```

2. **Make a request**:
   ```bash
   curl http://localhost:8080/scores
   ```

3. **View logs**:
   ```bash
   docker-compose logs -f spring-app
   ```

4. **For more details**, see:
   - `DEBUG_LOGGING_GUIDE.md` for comprehensive guide
   - `LOGGING_QUICK_REFERENCE.md` for quick commands

## Security Note

The `HttpLoggingFilter` automatically redacts:
- Authorization headers
- Cookies
- Any header containing "password", "secret", or "token"

This ensures sensitive information is not logged while still providing full request debugging capability.

---

**Ready to debug!** 🚀 Run `docker-compose logs -f spring-app` and watch your requests flow through the system.

