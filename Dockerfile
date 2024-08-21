FROM openjdk:23-ea-17-jdk-bullseye
COPY build/libs/accounts-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]