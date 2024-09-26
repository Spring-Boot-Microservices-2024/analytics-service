FROM openjdk:24

WORKDIR /app

COPY build/libs/analytics-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
