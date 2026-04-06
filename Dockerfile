# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew \
    && ./gradlew dependencies --no-daemon -q

COPY src/ src/

RUN ./gradlew bootJar --no-daemon -x test \
    && java -Djarmode=layertools -jar build/libs/*.jar extract \
       --destination build/layers

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=builder --chown=appuser:appgroup /app/build/layers/dependencies/          ./
COPY --from=builder --chown=appuser:appgroup /app/build/layers/spring-boot-loader/    ./
COPY --from=builder --chown=appuser:appgroup /app/build/layers/snapshot-dependencies/ ./
COPY --from=builder --chown=appuser:appgroup /app/build/layers/application/           ./

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]