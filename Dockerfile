FROM openjdk:17-alpine
COPY build/libs/accounts-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]