# 多阶段构建：在 Linux 容器内执行 Maven，避免宿主机中文路径等问题
# 构建：docker build -t hollow:1.0.0 .
# 运行：见 docker-compose.yml

FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
ENV TZ=Asia/Shanghai
COPY --from=build /build/target/hollow-1.0.0.jar /app/app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -fsS http://127.0.0.1:8080/actuator/health >/dev/null || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
