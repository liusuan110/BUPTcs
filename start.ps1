# 快速启动脚本

Write-Host "正在启动CS自学助手项目..." -ForegroundColor Green

# 检查Docker是否安装
if (!(Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "错误: 未找到Docker，请先安装Docker Desktop" -ForegroundColor Red
    exit 1
}

# 检查Node.js是否安装
if (!(Get-Command node -ErrorAction SilentlyContinue)) {
    Write-Host "错误: 未找到Node.js，请先安装Node.js 18+" -ForegroundColor Red
    exit 1
}

# 检查Python是否安装
if (!(Get-Command python -ErrorAction SilentlyContinue)) {
    Write-Host "错误: 未找到Python，请先安装Python 3.11+" -ForegroundColor Red
    exit 1
}

# 复制环境变量文件
if (!(Test-Path .env)) {
    Write-Host "创建环境变量文件..." -ForegroundColor Yellow
    Copy-Item .env.example .env
    Write-Host "请编辑 .env 文件配置必要的环境变量" -ForegroundColor Yellow
}

# 启动数据库服务
Write-Host "启动数据库服务..." -ForegroundColor Blue
docker-compose up -d mongodb redis minio

# 等待数据库启动
Write-Host "等待数据库启动..." -ForegroundColor Blue
Start-Sleep -Seconds 10

# 安装前端依赖
Write-Host "安装前端依赖..." -ForegroundColor Blue
Set-Location frontend
npm install
Set-Location ..

# 安装后端依赖
Write-Host "安装后端依赖..." -ForegroundColor Blue
Set-Location backend
npm install
Set-Location ..

# 安装AI服务依赖
Write-Host "安装AI服务依赖..." -ForegroundColor Blue
Set-Location ai-service
pip install -r requirements.txt
Set-Location ..

Write-Host "依赖安装完成！" -ForegroundColor Green
Write-Host ""
Write-Host "启动开发服务器:" -ForegroundColor Cyan
Write-Host "1. 后端API: cd backend && npm run dev" -ForegroundColor White
Write-Host "2. AI服务: cd ai-service && python main.py" -ForegroundColor White  
Write-Host "3. 前端应用: cd frontend && npm run dev" -ForegroundColor White
Write-Host ""
Write-Host "服务地址:" -ForegroundColor Cyan
Write-Host "- 前端: http://localhost:3000" -ForegroundColor White
Write-Host "- 后端API: http://localhost:5000" -ForegroundColor White
Write-Host "- AI服务: http://localhost:8000" -ForegroundColor White
Write-Host "- MongoDB: localhost:27017" -ForegroundColor White
Write-Host "- Redis: localhost:6379" -ForegroundColor White
Write-Host "- MinIO: http://localhost:9001" -ForegroundColor White