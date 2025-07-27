FROM eclipse-temurin:17-jdk-alpine

COPY build/libs/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
