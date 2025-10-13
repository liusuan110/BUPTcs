# 在GitHub上创建仓库指南

## 步骤1: 在GitHub网站创建新仓库

1. 登录您的GitHub账户
2. 点击右上角的 "+" 号，选择 "New repository"
3. 填写仓库信息：
   - **Repository name**: `cs-learning-assistant`
   - **Description**: `🎓 大学生计算机技能自学软件 - 基于LLM的智能学习平台，支持进度跟踪、课程推荐和在线评测`
   - **Visibility**: 选择 Public 或 Private
   - **不要**勾选 "Add a README file"（因为我们已经有了）
   - **不要**勾选 "Add .gitignore"（我们已经创建了）
   - **不要**选择 License（我们已经添加了MIT许可证）

4. 点击 "Create repository"

## 步骤2: 连接本地仓库到GitHub

创建仓库后，GitHub会显示连接指令，请在PowerShell中执行：

```powershell
# 添加远程仓库（将YOUR_USERNAME替换为您的GitHub用户名）
git remote add origin https://github.com/YOUR_USERNAME/cs-learning-assistant.git

# 推送代码到GitHub
git branch -M main
git push -u origin main
```

## 步骤3: 验证上传成功

访问您的GitHub仓库页面，确认所有文件都已成功上传。

## 可选：设置仓库详细信息

在GitHub仓库页面右侧点击 "⚙️ Settings"，然后：

1. 在 "About" 部分添加：
   - Website: 您的演示地址（如果有）
   - Topics: `learning-platform`, `llm`, `ai-education`, `react`, `golang`, `computer-science`

2. 在 "Features" 部分启用：
   - ✅ Issues
   - ✅ Projects  
   - ✅ Wiki
   - ✅ Discussions

## 仓库特性配置建议

### Branch Protection Rules
在 Settings > Branches 中设置：
- Require a pull request before merging
- Require status checks to pass before merging
- Require conversation resolution before merging

### Issue Templates
创建Issue模板以规范问题报告：
- Bug Report
- Feature Request  
- Documentation

### Actions Secrets
如果使用GitHub Actions，在 Settings > Secrets and variables > Actions 中添加：
- `OPENAI_API_KEY`
- `DATABASE_URL`
- 其他敏感环境变量