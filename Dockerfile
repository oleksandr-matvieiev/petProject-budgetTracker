FROM openjdk:18-jdk-slim
WORKDIR /app
COPY target/petProject-budgetTracker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
