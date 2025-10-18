# 🚀 CS自学助手项目 - GitHub仓库创建完整指南

## 📋 准备工作检查清单

✅ 项目代码已完成  
✅ 本地Git仓库已初始化  
✅ 所有文件已提交到本地仓库  
✅ .gitignore文件已创建  
✅ README.md文档已完善  
✅ LICENSE许可证已添加  

## 🌐 方法一：GitHub网站创建（推荐）

### 第一步：创建GitHub仓库
1. 访问 [GitHub.com](https://github.com) 并登录
2. 点击右上角 **"+"** → **"New repository"**
3. 填写仓库信息：

```
Repository name: BUPTcs
Description: 🎓 大学生计算机技能自学软件 - 基于LLM的智能学习平台
Visibility: ✅ Public（推荐开源）
Initialize: ❌ 不要勾选任何选项（我们已经有完整项目）
```

4. 点击 **"Create repository"**

### 第二步：连接并推送代码

在PowerShell中执行以下命令（记得替换YOUR_USERNAME）：

```powershell
# 1. 添加远程仓库
git remote add origin https://github.com/liusuan110/BUPTcs.git

# 2. 验证远程仓库
git remote -v

# 3. 推送代码到GitHub
git push -u origin main
```

如果遇到认证问题，使用Personal Access Token：
1. GitHub Settings > Developer settings > Personal access tokens > Tokens (classic)
2. 生成新token，权限选择 "repo"
3. 使用token作为密码进行认证

## 🔧 方法二：GitHub CLI（可选）

如果想使用命令行，可以安装GitHub CLI：

```powershell
# 使用winget安装（Windows 10/11）
winget install --id GitHub.cli

# 或使用Chocolatey
choco install gh

# 安装后登录并创建仓库
gh auth login
gh repo create BUPTcs --public --source=. --remote=origin --push
```

## 📊 仓库配置建议

### 仓库设置优化
在GitHub仓库页面进行以下配置：

#### 1. About部分（右侧边栏）
```
📝 Description: 大学生计算机技能自学软件 - 基于LLM的智能学习平台
🌐 Website: https://your-demo-site.com（如果有）
🏷️ Topics: learning-platform, llm, ai-education, react, golang, computer-science, typescript, postgresql
```

#### 2. 功能启用（Settings > General）
- ✅ Issues（问题追踪）
- ✅ Projects（项目管理）
- ✅ Wiki（文档）  
- ✅ Discussions（社区讨论）
- ✅ Sponsorships（赞助）

#### 3. 分支保护（Settings > Branches）
```
Branch name pattern: main
☑️ Require a pull request before merging
☑️ Require status checks to pass before merging  
☑️ Require conversation resolution before merging
☑️ Include administrators
```

### GitHub Actions工作流

创建 `.github/workflows/ci.yml`：

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  frontend:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - uses: actions/setup-node@v3
      with:
        node-version: '18'
        cache: 'npm'
        cache-dependency-path: frontend/package-lock.json
    - run: cd frontend && npm ci
    - run: cd frontend && npm run build
    - run: cd frontend && npm test

  # backend-go: （已移除）
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - uses: actions/setup-go@v3
      with:
        go-version: '1.21'
  # 已移除 backend-go 相关步骤

  ai-service:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - uses: actions/setup-python@v3
      with:
        python-version: '3.11'
    - run: cd ai-service && pip install -r requirements.txt
    - run: cd ai-service && python -m pytest
```

## 🏷️ 推荐的GitHub标签

在Issues中使用以下标签：

```
🐛 bug - 程序错误
✨ enhancement - 功能增强  
📚 documentation - 文档相关
❓ question - 疑问
🚀 feature - 新功能
🔧 maintenance - 维护
👥 help wanted - 需要帮助
💡 good first issue - 适合新手
🎯 priority:high - 高优先级
⚠️ priority:low - 低优先级
```

## 📈 项目推广建议

### README徽章
在README.md顶部添加：

```markdown
![GitHub stars](https://img.shields.io/github/stars/liusuan110/BUPTcs)
![GitHub forks](https://img.shields.io/github/forks/liusuan110/BUPTcs)
![GitHub issues](https://img.shields.io/github/issues/liusuan110/BUPTcs)
![GitHub license](https://img.shields.io/github/license/liusuan110/BUPTcs)
![GitHub workflow status](https://img.shields.io/github/workflow/status/liusuan110/BUPTcs/CI)
```

### 社区分享
- 发布到 [r/programming](https://reddit.com/r/programming)
- 分享到技术社区（掘金、CSDN、知乎）
- 提交到 [Awesome Lists](https://github.com/sindresorhus/awesome)
- 发布到Product Hunt

## 🔒 安全配置

### Secrets配置（Settings > Secrets and variables > Actions）
```
OPENAI_API_KEY: your_openai_key
DATABASE_URL: your_database_connection_string
JWT_SECRET: your_jwt_secret
DOCKER_USERNAME: your_docker_username
DOCKER_PASSWORD: your_docker_password
```

### Dependabot配置
创建 `.github/dependabot.yml`：

```yaml
version: 2
updates:
  - package-ecosystem: "npm"
    directory: "/frontend"
    schedule:
      interval: "weekly"
  
  - package-ecosystem: "gomod"  
  # directory: "/backend-go" （已移除）
    schedule:
      interval: "weekly"
      
  - package-ecosystem: "pip"
    directory: "/ai-service"  
    schedule:
      interval: "weekly"
```

## ✅ 验证清单

推送完成后，请验证：

- [ ] 所有文件都已上传到GitHub
- [ ] README.md正确显示项目信息
- [ ] .gitignore正确忽略了不需要的文件
- [ ] LICENSE文件存在且格式正确
- [ ] 仓库描述和标签已设置
- [ ] 分支保护规则已配置
- [ ] GitHub Actions工作流正常运行

## 🎉 完成！

恭喜！您的CS自学助手项目现在已经在GitHub上了！

**仓库地址**: `https://github.com/liusuan110/BUPTcs`

接下来可以：
1. 邀请协作者参与开发
2. 设置项目看板管理任务
3. 编写详细的API文档
4. 部署演示环境
5. 推广项目获得关注