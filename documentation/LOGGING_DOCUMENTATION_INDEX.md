# 📚 Complete Logging Documentation Index

This document is your central reference for all logging-related guides.

## 🚀 Quick Start (Start Here!)

If you just want to see logs immediately:

```bash
# 1. Rebuild and start
docker-compose down && docker-compose up -d --build

# 2. View logs
docker-compose logs -f spring-app

# 3. Make a request in another terminal
curl http://localhost:8080/scores

# Done! You should see detailed logs
```

**Want more?** Go to **Beginner** section below.

## 📖 Documentation by Experience Level

### 🟢 Beginner (First Time?)
Start with these guides in order:

1. **DEBUG_LOGGING_GUIDE.md** ← START HERE
   - What logging is and why you need it
   - How to view logs
   - Common debugging scenarios
   - Troubleshooting

2. **LOGGING_QUICK_REFERENCE.md**
   - Copy-paste commands for common tasks
   - Most useful grep filters
   - Log level adjustment commands

### 🟡 Intermediate (Need More Power?)

3. **OAUTH2_DEBUG_GUIDE.md**
   - OAuth2-specific debugging
   - Keycloak troubleshooting
   - Token validation issues
   - "Realm does not exist" error diagnosis

4. **CONFIG_HOT_RELOAD.md**
   - Change configuration without rebuilding
   - Volume mounting explained
   - Best practices

### 🔴 Advanced (Going Deep?)

5. **SETUP_DEBUG_LOGGING_SUMMARY.md**
   - Complete technical architecture
   - All components explained
   - Performance tuning
   - Log rotation configuration

## 📋 Documentation by Topic

### Setting Up & Configuration
- **DEBUG_LOGGING_GUIDE.md** - Complete setup guide
- **SETUP_DEBUG_LOGGING_SUMMARY.md** - Technical details
- **logback-spring.xml** - Logback configuration file

### Using Logs
- **LOGGING_QUICK_REFERENCE.md** - Command reference
- **DEBUG_LOGGING_GUIDE.md** - Usage patterns and examples

### Debugging Specific Issues
- **OAUTH2_DEBUG_GUIDE.md** - OAuth2 & Keycloak issues
- **DEBUG_LOGGING_GUIDE.md** - Troubleshooting section
- **CONFIG_HOT_RELOAD.md** - Configuration issues

### Development Workflow
- **CONFIG_HOT_RELOAD.md** - Hot reload for faster iteration
- **LOGGING_QUICK_REFERENCE.md** - Performance analysis

## 🔧 Technical Components

### What Was Added

| Component | File | Purpose |
|-----------|------|---------|
| **Logging Configuration** | `application-docker.yml` | Spring Boot logging config |
| **Logback Config** | `logback-spring.xml` | Professional log formatting |
| **HTTP Filter** | `HttpLoggingFilter.java` | Request/response logging |

### What Logs What

| Logger | Coverage |
|--------|----------|
| `org.springframework.*` | All Spring Framework operations |
| `org.springframework.security` | Authentication & authorization |
| `org.springframework.security.oauth2` | OAuth2 token handling |
| `org.springframework.web` | HTTP routing & controllers |
| `org.apache.tomcat` | Servlet container |
| `app.*` | Your application code |
| `HttpLoggingFilter` | All HTTP requests/responses |

## 💡 Common Tasks

### I want to...

| Goal | Read This | Command |
|------|-----------|---------|
| See all logs | QUICK_REFERENCE | `docker-compose logs -f` |
| Debug OAuth2 | OAUTH2_DEBUG_GUIDE | `grep -i oauth2` |
| Fix "Realm exists" error | OAUTH2_DEBUG_GUIDE | `grep -i realm` |
| See only errors | QUICK_REFERENCE | `grep ERROR` |
| Track a specific request | DEBUG_LOGGING_GUIDE | Use RequestID |
| Change log level fast | QUICK_REFERENCE | Actuator endpoint |
| Change config without rebuild | CONFIG_HOT_RELOAD | Volume mounting |
| Find slow requests | QUICK_REFERENCE | Sort Duration |
| See request details | DEBUG_LOGGING_GUIDE | REQUEST/RESPONSE lines |

## 🎯 Recommended Reading Paths

### Path 1: Just Show Me The Logs (5 min)
```
1. LOGGING_QUICK_REFERENCE.md (skip intro, go to commands)
2. Run: docker-compose logs -f spring-app
3. Done!
```

### Path 2: Complete Understanding (20 min)
```
1. DEBUG_LOGGING_GUIDE.md (read full)
2. LOGGING_QUICK_REFERENCE.md (skim commands)
3. OAUTH2_DEBUG_GUIDE.md (read sections relevant to your issue)
```

### Path 3: Master Everything (1 hour)
```
1. DEBUG_LOGGING_GUIDE.md
2. SETUP_DEBUG_LOGGING_SUMMARY.md
3. OAUTH2_DEBUG_GUIDE.md
4. CONFIG_HOT_RELOAD.md
5. LOGGING_QUICK_REFERENCE.md
6. Read the actual logback-spring.xml and HttpLoggingFilter.java
```

## 🔗 File Locations

### Guides (Read These)
- `/DEBUG_LOGGING_GUIDE.md` - Complete guide
- `/LOGGING_QUICK_REFERENCE.md` - Quick reference
- `/OAUTH2_DEBUG_GUIDE.md` - OAuth2 debugging
- `/CONFIG_HOT_RELOAD.md` - Config hot reload
- `/SETUP_DEBUG_LOGGING_SUMMARY.md` - Technical summary
- `/CONFIG_HOT_RELOAD.md` - Hot reload info

### Configuration Files (In Project)
- `clean-archi/app/src/main/resources/application-docker.yml` - Spring config
- `clean-archi/app/src/main/resources/logback-spring.xml` - Logback config

### Code Files (In Project)
- `clean-archi/app/src/main/java/app/config/HttpLoggingFilter.java` - HTTP logging

## 📊 Log Output Locations

### Real-time Console
```bash
docker-compose logs -f spring-app
```

### Inside Container
```bash
docker exec -it spring-bff bash
tail -f logs/spring-app.log
```

### From Host (if volume mounted)
```bash
tail -f logs/spring-app.log
```

### Saved for Analysis
```bash
# Save current logs
docker-compose logs spring-app > debug_session.txt

# Follow and save simultaneously
docker-compose logs -f spring-app | tee debug_session.txt
```

## ⚙️ Customization

### Adjust Log Levels
Edit `application-docker.yml`:
```yaml
logging:
  level:
    org.springframework.web: INFO  # Reduce from DEBUG
    org.springframework.security: DEBUG  # Keep as-is
```

Then restart: `docker-compose restart spring-app`

### Change Log Format
Edit `logback-spring.xml`:
```xml
<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
```

Then rebuild: `docker-compose up -d --build`

### Add Custom Logging
In your Java code:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(YourClass.class);

// Then use:
logger.debug("Debug message: {}", variable);
logger.info("Info message");
logger.warn("Warning message");
logger.error("Error message", exception);
```

## 🚨 Troubleshooting

### Logs not showing up?
→ See **DEBUG_LOGGING_GUIDE.md** "Troubleshooting" section

### Too much logging?
→ See **SETUP_DEBUG_LOGGING_SUMMARY.md** "Disabling Specific Loggers"

### OAuth2 issues?
→ Go straight to **OAUTH2_DEBUG_GUIDE.md**

### Configuration not updating?
→ See **CONFIG_HOT_RELOAD.md**

## 📝 Key Features

✅ **Request Tracing** - Follow a single request through the system
✅ **Performance Monitoring** - See request duration for each call
✅ **Security** - Sensitive headers automatically redacted
✅ **Filtering** - Easy to filter by component, level, or keyword
✅ **Hot Reload** - Change log levels without restarting
✅ **File Rotation** - Logs automatically rotate at 10MB
✅ **Spring Integration** - Full visibility into Spring internals
✅ **OAuth2 Visibility** - See Keycloak interactions

## 🎓 Learning Resources

### External Links
- [Spring Boot Logging Documentation](https://spring.io/guides/gs/logging-log4j2/)
- [Logback Documentation](https://logback.qos.ch/)
- [Spring Security Debugging](https://spring.io/projects/spring-security)
- [Keycloak Documentation](https://www.keycloak.org/documentation)

### In This Project
- `Authentication-Flow.md` - OAuth2 flow explanation
- `Readme.md` - Project overview
- `Docker-Compose-Commands.md` - Docker commands

## 📞 Quick Help

**Q: How do I see logs?**
A: `docker-compose logs -f spring-app`

**Q: How do I find errors?**
A: `docker-compose logs spring-app | grep ERROR`

**Q: How do I debug OAuth2?**
A: `docker-compose logs spring-app | grep -i oauth2` (then read OAUTH2_DEBUG_GUIDE.md)

**Q: How do I see just requests?**
A: `docker-compose logs -f spring-app | grep REQUEST`

**Q: How do I change log level without restart?**
A: Use Actuator endpoint (see LOGGING_QUICK_REFERENCE.md)

## ✅ Checklist for First Use

- [ ] Read DEBUG_LOGGING_GUIDE.md intro
- [ ] Run `docker-compose up -d --build`
- [ ] Run `docker-compose logs -f spring-app`
- [ ] Make a request: `curl http://localhost:8080/scores`
- [ ] See the request/response in logs
- [ ] Try a grep command from LOGGING_QUICK_REFERENCE.md
- [ ] Adjust a log level using Actuator
- [ ] Read OAUTH2_DEBUG_GUIDE.md if needed
- [ ] Bookmark LOGGING_QUICK_REFERENCE.md for future use

---

**Start here:** `DEBUG_LOGGING_GUIDE.md`

**Quick commands:** `LOGGING_QUICK_REFERENCE.md`

**OAuth2 issues:** `OAUTH2_DEBUG_GUIDE.md`

Happy debugging! 🔍

