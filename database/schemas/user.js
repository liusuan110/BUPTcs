// 用户数据模型
const userSchema = {
  _id: "ObjectId",
  username: "String",          // 用户名
  email: "String",             // 邮箱
  password: "String",          // 加密密码
  profile: {
    firstName: "String",       // 姓名
    lastName: "String",
    avatar: "String",          // 头像URL
    bio: "String",             // 个人简介
    university: "String",      // 大学
    major: "String",           // 专业
    year: "Number",            // 年级
    interests: ["String"],     // 兴趣领域
  },
  learningData: {
    totalStudyTime: "Number",  // 总学习时间(分钟)
    currentLevel: "String",    // 当前等级
    experience: "Number",      // 经验值
    streak: "Number",          // 连续学习天数
    achievements: ["ObjectId"], // 成就ID数组
    skillsLearned: ["String"], // 已学技能
  },
  preferences: {
    learningStyle: "String",   // 学习风格
    difficulty: "String",      // 偏好难度
    dailyGoal: "Number",       // 每日目标时间
    notifications: "Boolean",  // 是否接收通知
  },
  createdAt: "Date",
  updatedAt: "Date",
  lastLogin: "Date",
  isActive: "Boolean",
  role: "String"               // 角色: student, teacher, admin
}