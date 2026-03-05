#!/bin/bash

# Bruno Collection Setup Script
# This script helps you get started with the OAuth2/Keycloak flow collection

set -e

echo "================================"
echo "OAuth2 Keycloak Flow Setup"
echo "================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
echo "Checking prerequisites..."
echo ""

# Check Docker
if command -v docker &> /dev/null; then
    echo -e "${GREEN}✓${NC} Docker is installed"
else
    echo -e "${RED}✗${NC} Docker is not installed"
    echo "Please install Docker: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Check Docker Compose
if command -v docker-compose &> /dev/null; then
    echo -e "${GREEN}✓${NC} Docker Compose is installed"
else
    echo -e "${RED}✗${NC} Docker Compose is not installed"
    echo "Please install Docker Compose: https://docs.docker.com/compose/install/"
    exit 1
fi

# Check Gradle
if command -v ./gradlew &> /dev/null || command -v gradle &> /dev/null; then
    echo -e "${GREEN}✓${NC} Gradle is available"
else
    echo -e "${RED}✗${NC} Gradle is not available"
    exit 1
fi

# Check Bruno
if command -v bruno &> /dev/null; then
    echo -e "${GREEN}✓${NC} Bruno is installed"
else
    echo -e "${YELLOW}⚠${NC} Bruno is not installed (optional)"
    echo "Download from: https://www.usebruno.com/"
fi

echo ""
echo "================================"
echo "Starting Services"
echo "================================"
echo ""

# Start Keycloak and Spring App
echo "Starting Keycloak and Spring App with Docker Compose..."
echo ""

# Check if containers are already running
KEYCLOAK_RUNNING=$(docker-compose ps keycloak 2>/dev/null | grep -c "running" || true)
APP_RUNNING=$(docker-compose ps spring-app 2>/dev/null | grep -c "running" || true)

if [ "$KEYCLOAK_RUNNING" -eq 0 ] || [ "$APP_RUNNING" -eq 0 ]; then
    echo "Starting containers..."
    docker-compose up -d keycloak spring-app

    echo ""
    echo "Waiting for services to be healthy..."
    sleep 5

    # Wait for Keycloak to be ready
    echo "Waiting for Keycloak..."
    RETRY_COUNT=0
    MAX_RETRIES=30
    while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
        if docker-compose exec keycloak curl -s http://keycloak:8081/health/ready > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Keycloak is ready"
            break
        fi
        RETRY_COUNT=$((RETRY_COUNT + 1))
        sleep 2
    done

    if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
        echo -e "${RED}✗${NC} Keycloak failed to start after 60 seconds"
        exit 1
    fi

    # Wait for Spring App to be ready
    echo "Waiting for Spring App..."
    RETRY_COUNT=0
    MAX_RETRIES=30
    while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
        if curl -s http://localhost:8080/health > /dev/null 2>&1; then
            echo -e "${GREEN}✓${NC} Spring App is ready"
            break
        fi
        RETRY_COUNT=$((RETRY_COUNT + 1))
        sleep 2
    done

    if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
        echo -e "${YELLOW}⚠${NC} Spring App startup timeout - it may still be initializing"
    fi
else
    echo -e "${GREEN}✓${NC} Services are already running"
fi

echo ""
echo "================================"
echo "Service URLs"
echo "================================"
echo ""
echo -e "Spring App (BFF):     ${GREEN}http://localhost:8080${NC}"
echo -e "Keycloak Admin:       ${GREEN}http://localhost:8081${NC}"
echo -e "Keycloak Realm:       ${GREEN}my-realm${NC}"
echo -e "Admin Credentials:    ${GREEN}admin / admin${NC}"
echo ""

echo "================================"
echo "Next Steps"
echo "================================"
echo ""
echo "1. Open Bruno application"
echo "2. Load the collection from: ./bruno"
echo "3. Select 'local' environment"
echo "4. Run the requests in order:"
echo ""
echo "   1️⃣  Access Secure Endpoint"
echo "   2️⃣  Get Authorization Code"
echo "   3️⃣  Keycloak Login (Manual - open browser)"
echo "   4️⃣  Exchange Code for Token"
echo "   5️⃣  Access with Token"
echo "   6️⃣  Refresh Token"
echo "   7️⃣  Access Public Endpoint"
echo "   8️⃣  Logout"
echo ""

echo "================================"
echo "Useful Commands"
echo "================================"
echo ""
echo "View logs:"
echo "  docker-compose logs -f keycloak"
echo "  docker-compose logs -f spring-app"
echo ""
echo "Stop services:"
echo "  docker-compose down"
echo ""
echo "Restart services:"
echo "  docker-compose restart"
echo ""
echo "Access Keycloak Admin Console:"
echo "  http://localhost:8081"
echo "  Username: admin"
echo "  Password: admin"
echo ""

echo -e "${GREEN}Setup complete!${NC}"
echo ""

