# 📦 Debug Logging Setup - Complete Manifest

## ✅ Installation Complete

All components have been successfully installed and configured for debug logging.

---

## 📋 Files Created

### Documentation Files (8 files)

Located at: `/home/matthieu/dev/repos/clean-architecture-java-spring/`

1. **LOGGING_DOCUMENTATION_INDEX.md** ← Start here!
   - Central navigation hub
   - Learning paths by experience level
   - Topic-based navigation
   - Quick help section

2. **DEBUG_LOGGING_GUIDE.md**
   - Complete guide (read after index)
   - Log output locations
   - What gets logged
   - Viewing logs
   - Changing log levels
   - Troubleshooting

3. **LOGGING_QUICK_REFERENCE.md**
   - Copy-paste ready commands
   - Common log viewing scenarios
   - Filter patterns
   - Performance analysis commands
   - Bookmark this for daily use!

4. **OAUTH2_DEBUG_GUIDE.md**
   - OAuth2-specific debugging
   - Keycloak troubleshooting
   - "Realm does not exist" error diagnosis
   - Network debugging
   - Quick diagnostic script

5. **CONFIG_HOT_RELOAD.md**
   - Change configuration without rebuild
   - Volume mounting explanation
   - Hot reload workflow
   - Best practices

6. **SETUP_DEBUG_LOGGING_SUMMARY.md**
   - Technical architecture
   - Component details
   - Advanced configuration
   - Log rotation settings

7. **SETUP_CHECKLIST.md**
   - Step-by-step setup tasks
   - Verification tasks
   - Common scenarios
   - Integration with IDEs

8. **LOGGING_DOCUMENTATION_INDEX.md** (This file)
   - Complete installation manifest
   - What was done
   - File locations
   - Quick start guide

### Code Files (2 files)

1. **HttpLoggingFilter.java**
   - Location: `clean-archi/app/src/main/java/app/config/HttpLoggingFilter.java`
   - Purpose: HTTP request/response logging filter
   - Logs: Every HTTP request and response
   - Features: 
     - Request ID tracking
     - Duration measurement
     - Header redaction
     - Status code differentiation
     - Health endpoint filtering

2. **logback-spring.xml**
   - Location: `clean-archi/app/src/main/resources/logback-spring.xml`
   - Purpose: Logback configuration
   - Features:
     - Console output
     - File output
     - Rolling file policy
     - Spring profile awareness

### Configuration Files (1 file)

1. **application-docker.yml** (MODIFIED)
   - Location: `clean-archi/app/src/main/resources/application-docker.yml`
   - Changes: Enhanced logging configuration
   - Added:
     - Spring Framework logging (DEBUG)
     - Spring Security logging (DEBUG)
     - Spring OAuth2 logging (DEBUG)
     - Application code logging (DEBUG)
     - Tomcat logging (DEBUG)
     - Log pattern configuration
     - Log file configuration
     - Management endpoints for Actuator

---

## 🎯 What Each Component Does

### HttpLoggingFilter.java
```
Every HTTP Request
        ↓
   [Log Entry Created]
        ↓
   ├─ Method (GET, POST, etc.)
   ├─ URI (/scores, /secure/score, etc.)
   ├─ Headers (with sensitive redaction)
   ├─ Request ID (for tracking)
   └─ Timestamp

        ↓
   [Request Processed by Spring]
        ↓
Every HTTP Response
        ↓
   [Log Entry Created]
        ↓
   ├─ Status Code (200, 401, 500, etc.)
   ├─ Duration (in milliseconds)
   ├─ Response Headers
   ├─ Request ID (for correlation)
   └─ Timestamp
```

### logback-spring.xml
```
Log Message → Configuration Evaluation
           ↓
       ├─ Console Appender → docker-compose logs
       ├─ File Appender → /logs/spring-app.log
       └─ Filter Settings
              ├─ Profile: docker = DEBUG
              └─ Profile: default = INFO
```

### application-docker.yml
```
Spring Boot Startup
        ↓
   [Reads YAML]
        ↓
   Configures Logging:
   ├─ Root Level: INFO
   ├─ org.springframework.*: DEBUG
   ├─ org.springframework.security: DEBUG
   ├─ org.springframework.security.oauth2: DEBUG
   ├─ org.springframework.web: DEBUG
   ├─ org.apache.tomcat: DEBUG
   ├─ app.*: DEBUG
   ├─ com.example: DEBUG
   ├─ Log Pattern
   ├─ Log File Location
   └─ Log Rotation
```

---

## 📊 Log Flow Diagram

```
┌─────────────────────────────────────────────────┐
│         Client Request (curl, browser)           │
└────────────────────┬────────────────────────────┘
                     ↓
          ┌──────────────────────┐
          │   Docker Container   │
          │  spring-bff:8080     │
          │                      │
          │ ┌──────────────────┐ │
          │ │ HttpLoggingFilter│ │
          │ │   REQUEST LOG    │ │  ← See all requests
          │ └────────┬─────────┘ │
          │          ↓           │
          │ ┌──────────────────┐ │
          │ │ Spring Security  │ │
          │ │  (OAuth2 Check)  │ │  ← OAuth2 logs
          │ └────────┬─────────┘ │
          │          ↓           │
          │ ┌──────────────────┐ │
          │ │ Spring MVC       │ │
          │ │  Controller      │ │  ← Request processing
          │ └────────┬─────────┘ │
          │          ↓           │
          │ ┌──────────────────┐ │
          │ │ HttpLoggingFilter│ │
          │ │  RESPONSE LOG    │ │  ← See all responses + timing
          │ └────────┬─────────┘ │
          │          ↓           │
          │    [Send Response]   │
          │                      │
          └──────────┬───────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│         Client Receives Response                 │
└─────────────────────────────────────────────────┘

Simultaneously:
┌─────────────────────────────────────────────────┐
│     Logback writes to:                          │
│     • Console (docker-compose logs)             │
│     • File (/logs/spring-app.log)               │
│                                                 │
│  With Configuration from:                       │
│  • application-docker.yml (log levels)          │
│  • logback-spring.xml (format, rotation)        │
└─────────────────────────────────────────────────┘
```

---

## 🚀 Quick Start (3 Steps)

### Step 1: Rebuild
```bash
cd /home/matthieu/dev/repos/clean-architecture-java-spring
docker-compose down
docker-compose up -d --build
```

### Step 2: View Logs
```bash
docker-compose logs -f spring-app
```

### Step 3: Make Request
```bash
# In another terminal
curl http://localhost:8080/scores
```

**Result**: See detailed logs in real-time! 🎉

---

## 📈 What You Can Do

| Task | Command | Guide |
|------|---------|-------|
| See all logs | `docker-compose logs -f spring-app` | QUICK_REFERENCE |
| See requests | `docker-compose logs -f \| grep REQUEST` | QUICK_REFERENCE |
| Debug OAuth2 | `docker-compose logs -f \| grep -i oauth2` | OAUTH2_DEBUG_GUIDE |
| Find errors | `docker-compose logs -f \| grep ERROR` | QUICK_REFERENCE |
| Profile performance | `docker-compose logs \| grep Duration: \| sort -rn` | QUICK_REFERENCE |
| Change log level | `curl -X POST http://localhost:8080/actuator/loggers/...` | QUICK_REFERENCE |

---

## 📚 Reading Guide

### For First Time Users
1. This file (you are here)
2. `LOGGING_DOCUMENTATION_INDEX.md` - Navigation
3. `DEBUG_LOGGING_GUIDE.md` - Complete guide
4. `SETUP_CHECKLIST.md` - Do setup tasks

### For Finding Commands
- `LOGGING_QUICK_REFERENCE.md` - Bookmark this!

### For OAuth2 Issues
- `OAUTH2_DEBUG_GUIDE.md` - Everything about OAuth2 logging

### For Advanced Users
- `SETUP_DEBUG_LOGGING_SUMMARY.md` - Technical deep dive
- Source files: `HttpLoggingFilter.java`, `logback-spring.xml`

---

## 🔒 Security Features

✅ **Automatic Redaction**
- `Authorization` headers → `***REDACTED***`
- `Cookie` headers → `***REDACTED***`
- Headers with "token", "secret", "password" → `***REDACTED***`

✅ **Request Body Not Logged**
- Prevents sensitive data exposure
- Headers are logged (safe information)

✅ **Health Endpoint Filtering**
- `/health` endpoints don't create log spam

✅ **Production Ready**
- Can configure INFO level for production
- Log rotation prevents disk space issues

---

## 📂 File Structure

```
/home/matthieu/dev/repos/clean-architecture-java-spring/
│
├─ Documentation (8 files)
│  ├─ LOGGING_DOCUMENTATION_INDEX.md (START HERE!)
│  ├─ DEBUG_LOGGING_GUIDE.md
│  ├─ LOGGING_QUICK_REFERENCE.md (bookmark!)
│  ├─ OAUTH2_DEBUG_GUIDE.md
│  ├─ CONFIG_HOT_RELOAD.md
│  ├─ SETUP_DEBUG_LOGGING_SUMMARY.md
│  ├─ SETUP_CHECKLIST.md
│  └─ (this file)
│
├─ clean-archi/
│  ├─ app/
│  │  ├─ src/main/java/app/config/
│  │  │  └─ HttpLoggingFilter.java (NEW)
│  │  └─ src/main/resources/
│  │     ├─ application-docker.yml (MODIFIED)
│  │     └─ logback-spring.xml (NEW)
│  │
│  └─ [other files...]
│
└─ docker-compose.yml
```

---

## ⚙️ Configuration

### Logging Levels

```yaml
logging:
  level:
    root: INFO                          # Default
    org.springframework: DEBUG           # All Spring
    org.springframework.web: DEBUG       # HTTP routing
    org.springframework.security: DEBUG  # Authentication
    org.springframework.security.oauth2: DEBUG  # OAuth2
    org.apache.tomcat: DEBUG           # Servlet container
    app: DEBUG                         # Your code
    com.example: DEBUG                 # Your code
```

### Log Output

**Console**: 
```bash
docker-compose logs -f spring-app
```

**File** (in container):
```bash
/logs/spring-app.log (10MB max, 10 files kept)
```

**Docker Log Pattern**:
```
%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 🧪 Test It

### Verify Installation
```bash
# 1. Check filter exists
docker exec spring-bff find . -name "HttpLoggingFilter.class" 2>/dev/null

# 2. Check logs show up
docker-compose logs spring-app | tail -20

# 3. Make a request
curl http://localhost:8080/scores

# 4. See REQUEST/RESPONSE in logs
docker-compose logs spring-app | grep -E "REQUEST|RESPONSE"
```

### Verify Components
```bash
# Check Logback initialized
docker-compose logs spring-app | grep -i logback

# Check HTTP filter active
docker-compose logs spring-app | grep -i "HttpLoggingFilter"

# Check logging configuration loaded
docker-compose logs spring-app | grep -i "logging"
```

---

## 🛠️ Troubleshooting Quick Links

| Issue | Read |
|-------|------|
| No logs showing | DEBUG_LOGGING_GUIDE.md → Troubleshooting |
| Too verbose | SETUP_DEBUG_LOGGING_SUMMARY.md → Disabling Loggers |
| OAuth2 issues | OAUTH2_DEBUG_GUIDE.md |
| Config not updating | CONFIG_HOT_RELOAD.md |
| Need command reference | LOGGING_QUICK_REFERENCE.md |

---

## ✅ Verification Checklist

- [ ] All 8 documentation files created
- [ ] HttpLoggingFilter.java created
- [ ] logback-spring.xml created
- [ ] application-docker.yml modified
- [ ] Docker image rebuilt
- [ ] Containers running
- [ ] Logs visible with `docker-compose logs`
- [ ] Request creates log entries
- [ ] Sensitive headers redacted

---

## 🎓 Key Takeaways

1. **HttpLoggingFilter** logs every request and response
2. **logback-spring.xml** controls log format and output
3. **application-docker.yml** sets log levels
4. **Logs visible** via `docker-compose logs -f`
5. **Filtering** via grep: `| grep pattern`
6. **Hot reload** via Actuator endpoint (no restart)
7. **Security** built-in (headers redacted)

---

## 📞 Support

For questions on:
- **General logging**: See DEBUG_LOGGING_GUIDE.md
- **Quick commands**: See LOGGING_QUICK_REFERENCE.md
- **OAuth2 issues**: See OAUTH2_DEBUG_GUIDE.md
- **Setup**: See SETUP_CHECKLIST.md
- **Navigation**: See LOGGING_DOCUMENTATION_INDEX.md

---

## 🎉 You're Ready!

```bash
# Run this now:
docker-compose down && docker-compose up -d --build && docker-compose logs -f spring-app

# In another terminal:
curl http://localhost:8080/scores

# Watch the logs! 👀
```

---

**Next Step**: Read `LOGGING_DOCUMENTATION_INDEX.md`

Happy debugging! 🔍

