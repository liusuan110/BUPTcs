# CS自学助手 - 大学生计算机技能自学软件

## 项目简介

这是一个基于LLM大语言模型的大学生计算机技能自学软件，旨在为大学生提供个性化的学习路径规划和智能课程推荐。

## 核心功能

### 1. 学习进度跟踪
- 记录用户的学习历程和经历
- 实时更新学习状态和完成度
- 生成学习报告和数据分析

### 2. 智能推荐系统
- 基于LLM分析用户学习进度
- 推荐适合的课程和练习内容
- 个性化学习路径规划

### 3. 练习与评估
- 提供编程练习和项目实战
- 自动代码评测和反馈
- 技能评估和认证

## 技术架构

### 前端 (Frontend)
- **框架**: React 18 + TypeScript
- **构建工具**: Vite
- **UI库**: Ant Design + Tailwind CSS
- **状态管理**: Redux Toolkit
- **路由**: React Router v6

### 后端 (Backend)
- **语言**: Java 17
- **框架**: Spring Boot 3.1
- **数据库**: PostgreSQL + Spring Data JPA
- **缓存**: Redis + Spring Data Redis
- **认证**: JWT + Spring Security
- **API文档**: SpringDoc OpenAPI 3

### AI服务 (AI Service)
- **语言**: Python 3.11+
- **框架**: FastAPI
- **LLM集成**: OpenAI GPT / 本地大模型
- **机器学习**: scikit-learn, pandas
- **向量数据库**: Chroma/Pinecone

### 数据库设计
- **主数据库**: PostgreSQL (用户数据、课程内容)
- **缓存**: Redis (会话、缓存)
- **文件存储**: MinIO/AWS S3 (资源文件)

## 项目结构

```
cs-learning-assistant/
├── frontend/                 # React前端应用
│   ├── src/
│   │   ├── components/      # 可复用组件
│   │   ├── pages/          # 页面组件
│   │   ├── hooks/          # 自定义Hooks
│   │   ├── store/          # Redux状态管理
│   │   ├── services/       # API服务
│   │   └── utils/          # 工具函数
│   ├── public/             # 静态资源
│   └── package.json
├── backend-java/             # Java Spring Boot后端
│   ├── src/main/java/
│   │   └── com/buptcs/
│   │       ├── controller/ # 控制器
│   │       ├── service/    # 服务层
│   │       ├── repository/ # 数据访问层
│   │       ├── entity/     # JPA实体
│   │       ├── dto/        # 数据传输对象
│   │       ├── config/     # 配置类
│   │       ├── security/   # 安全配置
│   │       └── exception/  # 异常处理
│   ├── src/main/resources/ # 配置文件
│   ├── pom.xml            # Maven配置
│   └── Dockerfile         # Docker配置
├── ai-service/              # Python AI服务
│   ├── app/
│   │   ├── models/         # AI模型
│   │   ├── services/       # AI服务
│   │   ├── api/            # API端点
│   │   └── utils/          # 工具函数
│   ├── requirements.txt
│   └── main.py
├── database/                # 数据库配置
│   ├── migrations/         # 数据迁移
│   ├── seeds/              # 初始数据
│   └── schemas/            # 数据库模式
├── docs/                    # 项目文档
│   ├── api/                # API文档
│   ├── design/             # 设计文档
│   └── deployment/         # 部署文档
├── docker-compose.yml       # Docker编排
├── .env.example            # 环境变量示例
└── README.md               # 项目说明
```

## 快速开始

### 环境要求
- Node.js 18+
- Java 17+
- Python 3.11+
- PostgreSQL 15+
- Redis 7.0+

### 安装依赖

```bash
# 前端依赖
cd frontend
npm install

# Java后端依赖
cd ../backend-java
./mvnw install

# AI服务依赖
cd ../ai-service
pip install -r requirements.txt
```

### 启动开发环境

```bash
# 启动数据库服务
docker-compose up -d postgresql redis

# 启动Java后端服务
cd backend-java
./mvnw spring-boot:run

# 启动AI服务
cd ../ai-service
python main.py

# 启动前端服务
cd ../frontend
npm run dev
```

## 开发路线图

### Phase 1: 基础架构 (1-2个月)
- [ ] 项目初始化和环境搭建
- [ ] 用户认证系统
- [ ] 基础数据库设计
- [ ] 前端基础页面和组件

### Phase 2: 核心功能 (2-3个月)
- [ ] 学习进度跟踪系统
- [ ] 课程内容管理
- [ ] 基础推荐算法
- [ ] 练习题库系统

### Phase 3: AI增强 (2-3个月)
- [ ] LLM集成和API
- [ ] 智能推荐系统
- [ ] 自然语言交互
- [ ] 个性化学习路径

### Phase 4: 优化完善 (1-2个月)
- [ ] 性能优化
- [ ] 用户体验改进
- [ ] 数据分析面板
- [ ] 移动端适配

## 贡献指南

请查看 [CONTRIBUTING.md](docs/CONTRIBUTING.md) 了解如何参与项目开发。

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件。