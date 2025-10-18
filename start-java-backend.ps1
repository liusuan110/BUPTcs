# CS自学助手 - Java后端启动脚本

Write-Host "🚀 正在启动CS自学助手Java后端服务..." -ForegroundColor Green

# 检查Java环境
Write-Host "📋 检查Java环境..."
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✅ Java版本: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ 未检测到Java环境，请安装Java 21或更高版本" -ForegroundColor Red
    exit 1
}

# 检查Maven环境
Write-Host "📋 检查Maven环境..."
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "✅ Maven版本: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "⚠️  未检测到Maven，将使用Maven Wrapper" -ForegroundColor Yellow
}

# 切换到Java后端目录
Set-Location -Path "backend-java"

# 设置开发环境配置
$env:SPRING_PROFILES_ACTIVE = "dev"

Write-Host "📦 安装依赖..." -ForegroundColor Blue
try {
    if (Test-Path "mvnw.cmd") {
        .\mvnw.cmd clean install -DskipTests
    } else {
        mvn clean install -DskipTests
    }
    Write-Host "✅ 依赖安装完成" -ForegroundColor Green
} catch {
    Write-Host "❌ 依赖安装失败" -ForegroundColor Red
    exit 1
}

Write-Host "🔧 启动Java后端服务..." -ForegroundColor Blue
Write-Host "📍 服务地址: http://localhost:8080" -ForegroundColor Cyan
Write-Host "📖 API文档: http://localhost:8080/api/v1/swagger-ui.html" -ForegroundColor Cyan
Write-Host "💡 按 Ctrl+C 停止服务" -ForegroundColor Yellow

try {
    if (Test-Path "mvnw.cmd") {
        .\mvnw.cmd spring-boot:run
    } else {
        mvn spring-boot:run
    }
} catch {
    Write-Host "❌ Java后端服务启动失败" -ForegroundColor Red
    exit 1
}