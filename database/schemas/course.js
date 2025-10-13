// 课程数据模型
const courseSchema = {
  _id: "ObjectId",
  title: "String",             // 课程标题
  description: "String",       // 课程描述
  instructor: "ObjectId",      // 讲师ID
  category: "String",          // 课程分类
  subcategory: "String",       // 子分类
  level: "String",             // 难度等级: beginner, intermediate, advanced
  duration: "Number",          // 课程时长(分钟)
  
  content: {
    videos: [{
      title: "String",
      url: "String",
      duration: "Number",
      order: "Number"
    }],
    materials: [{
      type: "String",          // pdf, doc, code
      title: "String",
      url: "String",
      size: "Number"
    }],
    quizzes: ["ObjectId"],     // 测验ID数组
    assignments: ["ObjectId"]   // 作业ID数组
  },
  
  prerequisites: ["ObjectId"], // 前置课程
  tags: ["String"],           // 标签
  
  stats: {
    enrollments: "Number",     // 注册人数
    completions: "Number",     // 完成人数
    rating: "Number",          // 平均评分
    reviews: "Number"          // 评论数
  },
  
  pricing: {
    isFree: "Boolean",
    price: "Number",
    currency: "String"
  },
  
  status: "String",           // draft, published, archived
  createdAt: "Date",
  updatedAt: "Date",
  publishedAt: "Date"
}

// 学习进度模型
const progressSchema = {
  _id: "ObjectId",
  userId: "ObjectId",
  courseId: "ObjectId",
  
  progress: {
    completedLessons: ["ObjectId"],
    currentLesson: "ObjectId",
    totalLessons: "Number",
    completionPercentage: "Number",
    timeSpent: "Number"        // 已花费时间(分钟)
  },
  
  performance: {
    quizScores: [{
      quizId: "ObjectId",
      score: "Number",
      maxScore: "Number",
      attempts: "Number",
      completedAt: "Date"
    }],
    assignmentScores: [{
      assignmentId: "ObjectId",
      score: "Number",
      feedback: "String",
      submittedAt: "Date"
    }]
  },
  
  enrolledAt: "Date",
  lastAccessedAt: "Date",
  completedAt: "Date",
  certificateIssued: "Boolean"
}