# 📋 Debug Logging Setup Checklist

## Installation & Setup

- [ ] Read this file
- [ ] Review files created:
  - [ ] `logback-spring.xml`
  - [ ] `HttpLoggingFilter.java`
- [ ] Review configuration changes:
  - [ ] `application-docker.yml` (now with enhanced logging)

## Initial Deployment

- [ ] Stop existing containers: `docker-compose down`
- [ ] Rebuild Docker image: `docker-compose up -d --build`
- [ ] Verify containers running: `docker-compose ps`
- [ ] Check Spring app started: `docker-compose logs spring-app | head -20`

## First Test

- [ ] Open terminal 1: `docker-compose logs -f spring-app`
- [ ] Open terminal 2: Make a request
  - [ ] `curl http://localhost:8080/scores`
- [ ] Observe logs in terminal 1:
  - [ ] See "REQUEST" line with method and URI
  - [ ] See Spring processing logs
  - [ ] See "RESPONSE" line with status and duration

## Verify Components

- [ ] Check HttpLoggingFilter is active: Look for "REQUEST" and "RESPONSE" in logs
- [ ] Check Spring logging: Look for org.springframework logs
- [ ] Check OAuth2 logging: Look for oauth2 related logs (if OAuth2 is enabled)
- [ ] Verify security redaction: Check that "authorization" header shows "***REDACTED***"

## Read Documentation

Read in this order:
- [ ] `LOGGING_DOCUMENTATION_INDEX.md` - Navigation hub
- [ ] `DEBUG_LOGGING_GUIDE.md` - Complete guide
- [ ] `LOGGING_QUICK_REFERENCE.md` - Commands reference

## Advanced Features (Optional)

- [ ] Test hot reload of log levels:
  ```bash
  curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
    -H "Content-Type: application/json" \
    -d '{"configuredLevel":"TRACE"}'
  ```
- [ ] Make a request and see more detailed logs
- [ ] Reset log level:
  ```bash
  curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
    -H "Content-Type: application/json" \
    -d '{"configuredLevel":null}'
  ```

## Common Logging Tasks

### View Logs
- [ ] Real-time: `docker-compose logs -f spring-app`
- [ ] Last 50 lines: `docker-compose logs --tail=50 spring-app`
- [ ] Save to file: `docker-compose logs spring-app > debug.txt`

### Filter Logs
- [ ] OAuth2 only: `docker-compose logs -f spring-app | grep -i oauth2`
- [ ] Errors only: `docker-compose logs -f spring-app | grep ERROR`
- [ ] Requests only: `docker-compose logs -f spring-app | grep REQUEST`
- [ ] Responses only: `docker-compose logs -f spring-app | grep RESPONSE`

### Performance Analysis
- [ ] Find slowest requests: `docker-compose logs spring-app | grep Duration: | sort -rn | head -5`
- [ ] Count requests: `docker-compose logs spring-app | grep -c REQUEST`

## Troubleshooting Checklist

If logs aren't showing:
- [ ] Containers running? `docker-compose ps`
- [ ] Spring app healthy? `curl http://localhost:8080/health`
- [ ] Logs exist? `docker exec spring-bff ls -la logs/`
- [ ] Filter compiled? `docker exec spring-bff find . -name "HttpLoggingFilter.class" 2>/dev/null`
- [ ] Rebuild if needed: `docker-compose up -d --build`

If too much logging:
- [ ] Edit `application-docker.yml`
- [ ] Change log level from DEBUG to INFO for specific packages
- [ ] Restart: `docker-compose restart spring-app`

## OAuth2 Specific

If debugging OAuth2/Keycloak:
- [ ] Read `OAUTH2_DEBUG_GUIDE.md`
- [ ] Check Keycloak is running: `docker-compose ps | grep keycloak`
- [ ] Check realm import: `docker-compose logs keycloak | grep import`
- [ ] View OAuth2 logs: `docker-compose logs -f spring-app | grep -i oauth2`
- [ ] View realm/issuer logs: `docker-compose logs -f spring-app | grep -i realm`

## Configuration Changes

For changing configuration without rebuild:
- [ ] Read `CONFIG_HOT_RELOAD.md`
- [ ] Edit `application-docker.yml` on host
- [ ] Restart Spring app: `docker-compose restart spring-app`
- [ ] Changes should be reflected in logs

## Bookmarks & References

Create bookmarks for quick access:
- [ ] `LOGGING_DOCUMENTATION_INDEX.md` - Always use this to navigate
- [ ] `LOGGING_QUICK_REFERENCE.md` - Most useful commands
- [ ] `OAUTH2_DEBUG_GUIDE.md` - For OAuth2 issues
- [ ] `DEBUG_LOGGING_GUIDE.md` - Complete reference

## Daily Usage

After setup, for daily work:
- [ ] Command: `docker-compose logs -f spring-app` (view logs)
- [ ] Command: Filter with grep as needed
- [ ] Reference: Use QUICK_REFERENCE.md for commands
- [ ] Adjust: Use Actuator to change log levels if needed

## Integration with IDE

### IntelliJ IDEA
- [ ] Open Run → Edit Configurations
- [ ] Add Docker Compose configuration
- [ ] Enable Services tab
- [ ] Right-click container → View Logs

### VS Code
- [ ] Install Docker extension
- [ ] Use explorer to view container logs
- [ ] Right-click container → View Logs

## Additional Learning

For deeper understanding:
- [ ] Review `logback-spring.xml` to understand configuration
- [ ] Review `HttpLoggingFilter.java` to see how HTTP logging works
- [ ] Read `SETUP_DEBUG_LOGGING_SUMMARY.md` for technical details
- [ ] Explore Spring Security logging in real requests

## Success Criteria

Check these to confirm everything is working:

- [ ] Can see HTTP requests in logs
- [ ] Can see HTTP responses with status and duration
- [ ] Can see Spring Security logs
- [ ] Can filter logs by various patterns
- [ ] Can change log levels without restart
- [ ] Sensitive headers are redacted
- [ ] Health check endpoints don't spam logs

## Performance Considerations

- [ ] Logging is at DEBUG level (impacts performance slightly)
- [ ] For production, reduce to INFO level (in CONFIG file)
- [ ] Log files rotate at 10MB (automatic cleanup)
- [ ] Actuator is enabled (only for dev, disable in production)

## Security Checklist

- [ ] Verify sensitive headers are redacted in logs
- [ ] Don't share logs that contain tokens (wait for redaction)
- [ ] Remember logs might contain PII (Personally Identifiable Information)
- [ ] For production, configure proper log retention and access control

## Final Steps

- [ ] Everything working? 🎉
- [ ] Bookmark LOGGING_QUICK_REFERENCE.md
- [ ] Share LOGGING_DOCUMENTATION_INDEX.md with team
- [ ] Test your specific scenario (OAuth2, endpoints, etc.)
- [ ] Start debugging! 🔍

---

## Quick Command Reference

Keep these handy:

```bash
# View logs
docker-compose logs -f spring-app

# OAuth2 debugging
docker-compose logs -f spring-app | grep -i oauth2

# Error tracking
docker-compose logs -f spring-app | grep ERROR

# Performance analysis
docker-compose logs spring-app | grep Duration: | sort -rn | head -5

# Change log level (no restart)
curl -X POST http://localhost:8080/actuator/loggers/org.springframework.security \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"TRACE"}'
```

## Contact/Support

If something doesn't work:
1. Read the relevant guide
2. Check troubleshooting section
3. Review the exact error message
4. Search logs for the error
5. Increase log level to TRACE
6. Check Docker logs: `docker-compose logs`

---

**You're all set!** 🚀

Next step: `docker-compose up -d --build && docker-compose logs -f spring-app`

