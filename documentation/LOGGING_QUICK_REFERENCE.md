# Debug Logging Quick Reference

## Fastest Way to See Logs

```bash
# Real-time logs from running container
docker-compose logs -f spring-app
```

## Common Log Commands

### View Last 50 Lines
```bash
docker-compose logs --tail=50 spring-app
```

### View OAuth2 Debug Logs Only
```bash
docker-compose logs -f spring-app | grep -i oauth2
```

### View Error Logs Only
```bash
docker-compose logs spring-app | grep -E "ERROR|WARN"
```

### View HTTP Request/Response Logs Only
```bash
docker-compose logs -f spring-app | grep -E "REQUEST|RESPONSE"
```

### View Logs for Specific Endpoint
```bash
# For /secure/score endpoint
docker-compose logs -f spring-app | grep "secure/score"
```

### View Spring Security Logs
```bash
docker-compose logs -f spring-app | grep -i "security"
```

### View Keycloak/Issuer Related Logs
```bash
docker-compose logs -f spring-app | grep -i "keycloak\|issuer"
```

## Change Log Level at Runtime (No Restart!)

### Increase Logging (TRACE = Most Detailed)
```bash
# For Spring Security
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'

# For Spring Web
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.web \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'

# For OAuth2
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security.oauth2 \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'
```

### Reset to Default
```bash
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":null}'
```

### View Current Log Level
```bash
curl http://localhost:8080/actuator/loggers/org.springframework.security
```

## What Each Log Component Shows

| Logger | Shows |
|--------|-------|
| `org.springframework.security` | Authentication, authorization, security filters |
| `org.springframework.security.oauth2` | OAuth2 token exchange, Keycloak interaction |
| `org.springframework.web` | HTTP request/response, routing, controller invocation |
| `app.config.HttpLoggingFilter` | Incoming requests and outgoing responses with timing |
| `org.apache.tomcat` | Servlet container operations |

## Practical Examples

### Debugging "Realm does not exist" Error
```bash
docker-compose logs -f spring-app | grep -i "issuer\|keycloak\|realm"
```
Look for:
- Realm discovery attempt
- Issuer URI validation
- Connection attempts to Keycloak

### Debugging Unauthorized (401) Errors
```bash
docker-compose logs -f spring-app | grep -E "401|UNAUTHORIZED|authentication"
```

### Debugging Request Body Issues
```bash
docker-compose logs -f spring-app | grep "REQUEST\|Content-Type"
```

### Performance Profiling
```bash
# Find slowest requests
docker-compose logs spring-app | grep "Duration:" | sort -t: -k3 -rn | head -5
```

### Finding All Requests to Specific Endpoint
```bash
docker-compose logs spring-app | grep "/secure/score"
```

## Files Generated

- **Main log**: `/logs/spring-app.log` inside container
- **HTTP log**: `/logs/http-requests.log` inside container (if enabled)

## Docker Log Levels

- `--tail` : Show last N lines
- `-f` : Follow logs (live update)
- `--since` : Show logs since timestamp (e.g., `--since 1h`)
- `--until` : Show logs until timestamp

Example with timestamp:
```bash
docker-compose logs --since 2026-03-10T07:00:00 spring-app
```

## Save Logs to File

```bash
# Save current logs to file
docker-compose logs spring-app > logs_backup.txt

# Follow logs and save in real-time
docker-compose logs -f spring-app | tee logs_realtime.txt
```

## Clean Up Old Logs

```bash
# If logs are mounted from host
rm logs/*.log

# Or truncate the Docker logs
docker exec spring-bff sh -c 'echo "" > logs/spring-app.log'
```

