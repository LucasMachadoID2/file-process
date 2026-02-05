# =========================
# Build stage
# =========================
FROM gradle:8-jdk21 AS build

WORKDIR /app

# Copiar arquivos do Gradle primeiro (cache)
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# Copiar o código
COPY src ./src

# Build do jar
RUN gradle clean build -x test --no-daemon


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Dependências nativas do FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg libglib2.0-0 libsm6 libxext6 && \
    rm -rf /var/lib/apt/lists/*

# Criar usuário não-root
RUN useradd -ms /bin/bash appuser

# Copiar o jar do stage build
COPY --from=build /app/build/libs/*.jar app.jar

# Permissões
RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
