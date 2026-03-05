## Docker Compose

## Commands
Build
```shell
docker-compose build --no-cache
```

Start all services
```shell
docker-compose up -d
```

Start only one service
```shell
docker-compose up keycloak
```

Restart one service 
```shell
docker-compose restart keycloak
```

Run commands on shell for a given container
```shell
docker exec -it keycloak bash
```

Inspect a running container (and filter by the Health properties)
```shell
docker inspect keycloak --format='{{json .State.Health}}' | jq
```

Run healthcheck command inside the container (on the management port)
```shell
bash-5.1$ exec 3<>/dev/tcp/localhost/9000
bash-5.1$ printf 'GET /health/ready HTTP/1.1\r\nHost: localhost\r\n\r\n' >&3
bash-5.1$ cat <&3
```

Show logs
```shell
docker-compose logs
```

Stop everything
```shell
docker-compose down
```
