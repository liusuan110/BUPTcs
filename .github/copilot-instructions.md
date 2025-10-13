# CS自学助手项目 - GitHub Copilot 指南

## 项目概述
这是一个基于LLM的大学生计算机技能自学软件，包含完整的前后端架构和智能推荐系统。

## 项目结构
- frontend/ - React前端应用 (TypeScript + Vite + Ant Design)
- backend/ - Node.js/Express后端API (TypeScript + MongoDB)
- ai-service/ - Python AI服务（FastAPI + LLM集成）
- database/ - 数据库配置和迁移文件
- docs/ - 项目文档和优化路线

## 核心功能
- 📚 学习进度跟踪和记录
- 🤖 基于LLM的智能课程推荐
- 💻 编程练习和在线评测
- 📊 学习数据分析和可视化
- 👥 用户认证和权限管理
- 🎯 个性化学习路径规划

## 技术栈
### 前端
- React 18 + TypeScript
- Vite构建工具
- Ant Design UI库
- Redux Toolkit状态管理
- React Router路由

### 后端
- Node.js + Express.js
- TypeScript
- MongoDB + Mongoose
- JWT认证
- Redis缓存

### AI服务
- Python 3.11+ + FastAPI
- OpenAI GPT集成
- scikit-learn机器学习
- Chroma向量数据库

### 基础设施
- Docker容器化
- MongoDB数据库
- Redis缓存
- MinIO对象存储

## 开发指南
- 使用TypeScript确保类型安全
- 遵循RESTful API设计规范
- 实现响应式设计适配移动端
- 采用微服务架构便于扩展
- 注重性能优化和用户体验

## 快速开始
1. 运行 `./start.ps1` 安装依赖并启动数据库
2. 分别启动三个服务：
   - 后端: `cd backend && npm run dev`
   - AI服务: `cd ai-service && python main.py`  
   - 前端: `cd frontend && npm run dev`

## 服务端口
- 前端应用: http://localhost:3000
- 后端API: http://localhost:5000
- AI服务: http://localhost:8000
- MongoDB: localhost:27017
- Redis: localhost:6379

✅ 完整项目架构创建完成
✅ 开发环境配置就绪
✅ 优化路线规划完成