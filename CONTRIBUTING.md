# 贡献指南

感谢您对CS自学助手项目的关注！我们欢迎任何形式的贡献。

## 如何参与贡献

### 报告问题
- 使用GitHub Issues报告bug或提出功能请求
- 请提供详细的问题描述和复现步骤
- 如果可能，请提供错误日志或截图

### 提交代码
1. Fork本项目到您的GitHub账户
2. 创建功能分支: `git checkout -b feature/amazing-feature`
3. 提交您的更改: `git commit -m 'Add some amazing feature'`
4. 推送到分支: `git push origin feature/amazing-feature`
5. 创建Pull Request

### 开发环境设置
```bash
# 克隆项目
git clone https://github.com/your-username/cs-learning-assistant.git
cd cs-learning-assistant

# 安装依赖并启动服务
<!-- 原 `backend-go/` Go后端服务目录已移除 -->
./start-stack.ps1 -Backend java
```

### 代码规范
- 遵循各语言的标准代码风格
- 编写清晰的提交信息
- 为新功能添加适当的测试
- 更新相关文档

### 提交规范
使用语义化的提交信息格式：
```
type(scope): description

feat(auth): add JWT authentication
fix(api): resolve user registration bug
docs(readme): update installation instructions
```

类型说明：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建或辅助工具的变动

## 开发指南

### 项目结构
 `backend-java/` - Java后端服务

### 技术栈
- 前端: React + TypeScript + Ant Design
- 后端: Go + Gin / Node.js + NestJS
- 数据库: PostgreSQL + Redis
- AI服务: Python + FastAPI

### 测试
```bash
# 前端测试
cd frontend && npm test

# 后端与服务测试
# Java 后端
cd backend-java && mvnw test

# Python测试
cd ai-service && python -m pytest
```

## 社区

- 加入我们的讨论区参与社区交流
- 关注项目动态和发布公告
- 分享您的使用经验和建议

## 行为准则

我们致力于为所有参与者提供友好、安全和包容的环境。请遵循以下准则：

- 尊重不同观点和经验
- 优雅地给出和接受建设性反馈
- 关注对社区最有利的事情
- 对其他社区成员表现出同理心

## 感谢

感谢所有为项目做出贡献的开发者们！

## 联系我们

如有任何问题，请通过以下方式联系：
- GitHub Issues
- 项目讨论区
- 邮箱: [项目邮箱]

再次感谢您的贡献！🎉