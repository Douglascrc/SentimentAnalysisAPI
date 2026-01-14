FROM eclipse-temurin:17-jdk-alpine
ADD target/sentiment-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]