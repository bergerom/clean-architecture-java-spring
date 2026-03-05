### Dockerfile
This simple Dockerfile:
- copies the output of gradle compilation into the docker volumes
- launches the jar file
 
### Useful commands

Building the app (in case of spring)
```sh
docker build --tag 'spring-app' .
```

Launching the app (in case of spring)
```sh
docker run 'spring-app'
```
