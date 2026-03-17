# Spring Configuration Hot Reload in Docker

## Overview
You can now modify the Spring application configuration file (`application-docker.yml`) and have the changes reflected immediately in the running Docker container **without rebuilding the image**.

## How It Works

### Volume Mounting
The `docker-compose.yml` file mounts your local configuration file into the container:
```yaml
volumes:
  - ./clean-archi/app/src/main/resources/application-docker.yml:/config/application.yml
```

This creates a bridge between:
- **Host**: `./clean-archi/app/src/main/resources/application-docker.yml`
- **Container**: `/config/application.yml`

### Spring Configuration Location
The environment variable `SPRING_CONFIG_LOCATION` tells Spring where to look for configuration:
```yaml
environment:
  SPRING_CONFIG_LOCATION: file:/config/
```

This takes precedence over the classpath configuration, so Spring will use the mounted file instead of the bundled one.

## Workflow

### Step 1: Start Docker Compose
```bash
docker-compose up -d
```

### Step 2: Modify Configuration on Host
Edit `./clean-archi/app/src/main/resources/application-docker.yml`:
```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://keycloak:8080/realms/my-realm
```

### Step 3: Restart the Spring App (Optional)
Changes in Spring configuration may or may not take effect depending on the property:
```bash
docker-compose restart spring-app
```

Or restart just the Spring service without restarting Keycloak:
```bash
docker-compose down spring-app
docker-compose up -d spring-app
```

## Important Notes

### Properties That Require Restart
Some Spring properties are cached at startup and require a container restart:
- OAuth2 client registration settings
- Security configuration
- Server port settings

### Properties That Hot-Reload
Some properties can be hot-reloaded without restart:
- Logging levels (if using Spring Boot Actuator)
- Custom application properties (if beans support refresh)

### Development vs Production
This approach is **perfect for development** but should NOT be used in production:
- Security risk: mounted files are world-readable
- Performance impact from network volumes
- Difficult to track configuration changes

For production, use environment variables or external configuration management services.

## Troubleshooting

### Configuration Changes Not Applied
1. Ensure the file path in `docker-compose.yml` is correct
2. Check that `SPRING_CONFIG_LOCATION` is set
3. Restart the container: `docker-compose restart spring-app`
4. Check logs: `docker-compose logs spring-app`

### Volume Not Mounted
Verify the mount with:
```bash
docker exec spring-bff ls -la /config/
```

### Spring Not Finding File
Check Spring logs:
```bash
docker-compose logs spring-app | grep -i "config"
```

## Example: Updating Keycloak Issuer URI

1. Start containers:
   ```bash
   docker-compose up -d
   ```

2. Edit configuration:
   ```yaml
   # ./clean-archi/app/src/main/resources/application-docker.yml
   spring:
     security:
       oauth2:
         client:
           provider:
             keycloak:
               issuer-uri: http://keycloak:8080/realms/my-new-realm  # Changed
   ```

3. Restart Spring app:
   ```bash
   docker-compose restart spring-app
   ```

4. The new configuration is now active without rebuilding the Docker image!

## Environment Variables Still Work
The `SPRING_CONFIG_LOCATION` approach doesn't prevent environment variable overrides. Environment variables take precedence:

```yaml
environment:
  SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_CLIENT_ID: bff-client  # This overrides config file
```

