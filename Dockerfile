FROM openjdk:17-slim
COPY build/libs/accounts-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]