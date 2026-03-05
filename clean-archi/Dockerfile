FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=/app/build/libs/app.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]