# 技术栈选择启动脚本

param(
    [Parameter(Mandatory=$false)]
    [ValidateSet("node", "java")]
    [string]$Backend = "java",
    
    [Parameter(Mandatory=$false)]
    [switch]$Dev = $false,
    
    [Parameter(Mandatory=$false)]
    [switch]$Docker = $false
)

Write-Host "=== CS自学助手项目启动 ===" -ForegroundColor Green
Write-Host "选择的后端技术栈: $Backend" -ForegroundColor Cyan

# 检查必要的工具
function Test-Command {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# 检查Docker
if ($Docker -and !(Test-Command docker)) {
    Write-Host "错误: 未找到Docker，请先安装Docker Desktop" -ForegroundColor Red
    exit 1
}

# 根据选择的技术栈检查环境
switch ($Backend) {
    "node" {
        if (!(Test-Command node)) {
            Write-Host "错误: 未找到Node.js，请先安装Node.js 18+" -ForegroundColor Red
            exit 1
        }
        Write-Host "✅ Node.js环境检查通过" -ForegroundColor Green
    }
    "java" {
        if (!(Test-Command java)) {
            Write-Host "错误: 未找到Java，请先安装Java 17+" -ForegroundColor Red
            exit 1
        }
        Write-Host "✅ Java环境检查通过" -ForegroundColor Green
    }
}

# 检查Python (AI服务必需)
if (!(Test-Command python)) {
    Write-Host "错误: 未找到Python，请先安装Python 3.11+" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Python环境检查通过" -ForegroundColor Green

# 创建环境变量文件
if (!(Test-Path .env)) {
    Write-Host "创建环境变量文件..." -ForegroundColor Yellow
    Copy-Item .env.example .env
    Write-Host "⚠️  请编辑 .env 文件配置必要的环境变量" -ForegroundColor Yellow
}

if ($Docker) {
    # Docker模式启动
    Write-Host "使用Docker启动所有服务..." -ForegroundColor Blue
    
    if ($Backend -eq "node") {
        docker-compose --profile node-backend up -d
    } else {
        docker-compose up -d
    }
    
    Write-Host "✅ 所有服务已启动" -ForegroundColor Green
} else {
    # 开发模式启动
    Write-Host "启动基础设施服务 (PostgreSQL, Redis, MinIO)..." -ForegroundColor Blue
    docker-compose up -d postgres redis minio
    
    Write-Host "等待数据库启动..." -ForegroundColor Blue
    Start-Sleep -Seconds 10
    
    # 根据选择的后端技术栈进行不同的处理
    switch ($Backend) {
        "node" {
            Write-Host "初始化Node.js项目..." -ForegroundColor Blue
            Set-Location backend
            
            if (!(Test-Path node_modules)) {
                Write-Host "安装Node.js依赖..." -ForegroundColor Blue
                npm install
            }
            
            Write-Host "启动Node.js后端服务..." -ForegroundColor Green
            Write-Host "命令: cd backend && npm run dev" -ForegroundColor White
            Set-Location ..
        }
    }
    
    # 安装AI服务依赖
    Write-Host "初始化AI服务..." -ForegroundColor Blue
    Set-Location ai-service
    
    if (!(Test-Path venv)) {
        Write-Host "创建Python虚拟环境..." -ForegroundColor Blue
        python -m venv venv
        
        if ($IsWindows) {
            & "venv\Scripts\Activate.ps1"
        } else {
            & source venv/bin/activate
        }
    }
    
    Write-Host "安装Python依赖..." -ForegroundColor Blue
    pip install -r requirements.txt
    
    Write-Host "启动AI服务..." -ForegroundColor Green
    Write-Host "命令: cd ai-service && python main.py" -ForegroundColor White
    Set-Location ..
    
    # 安装前端依赖
    Write-Host "初始化前端项目..." -ForegroundColor Blue
    Set-Location frontend
    
    if (!(Test-Path node_modules)) {
        Write-Host "安装前端依赖..." -ForegroundColor Blue
        npm install
    }
    
    Write-Host "启动前端服务..." -ForegroundColor Green
    Write-Host "命令: cd frontend && npm run dev" -ForegroundColor White
    Set-Location ..
}

Write-Host ""
Write-Host "=== 服务启动完成 ===" -ForegroundColor Green
Write-Host ""
Write-Host "📋 服务地址:" -ForegroundColor Cyan
Write-Host "   🌐 前端应用: http://localhost:3000" -ForegroundColor White
Write-Host "   🔧 后端API: http://localhost:5000" -ForegroundColor White
Write-Host "   🤖 AI服务: http://localhost:8000" -ForegroundColor White
Write-Host "   📊 API文档: http://localhost:5000/swagger/" -ForegroundColor White
Write-Host ""
Write-Host "📊 数据库服务:" -ForegroundColor Cyan
Write-Host "   🐘 PostgreSQL: localhost:5432" -ForegroundColor White
Write-Host "   📦 Redis: localhost:6379" -ForegroundColor White
Write-Host "   💾 MinIO: http://localhost:9001" -ForegroundColor White
Write-Host ""

if (!$Docker) {
    Write-Host "🚀 手动启动开发服务器:" -ForegroundColor Cyan
    if ($Backend -eq "node") {
        Write-Host "   1. 后端服务: cd backend && npm run dev" -ForegroundColor White
    } elseif ($Backend -eq "java") {
        Write-Host "   1. 后端服务: cd backend-java && mvn spring-boot:run" -ForegroundColor White
    }
    
    Write-Host "   2. AI服务: cd ai-service && python main.py" -ForegroundColor White
    Write-Host "   3. 前端应用: cd frontend && npm run dev" -ForegroundColor White
}

Write-Host ""
Write-Host "💡 技术栈对比和选择建议请查看: docs/TECH_STACK_COMPARISON.md" -ForegroundColor Yellow