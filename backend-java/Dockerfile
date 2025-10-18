# Java后端Dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 下载依赖
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY src src

# 构建应用
RUN ./mvnw package -DskipTests

# 运行时镜像
FROM eclipse-temurin:21-jre-alpine

# 安装必要工具
RUN apk add --no-cache curl

# 创建应用用户
RUN addgroup -g 1001 -S app && \
    adduser -S app -u 1001 -G app

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=build /app/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && \
    chown -R app:app /app

# 切换到应用用户
USER app

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/v1/actuator/health || exit 1

# 暴露端口
EXPOSE 8080

# 设置JVM参数
# JDK 21 默认支持容器内存限制，无需显式 UseContainerSupport
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]