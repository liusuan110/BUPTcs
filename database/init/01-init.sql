-- CS学习助手数据库初始化脚本

-- 创建数据库（如果使用Docker，这通常已经由环境变量完成）
-- CREATE DATABASE cs_learning;

-- 使用数据库
\c cs_learning;

-- 创建用户表的索引（JPA会自动创建表，这里只是预定义一些索引）
-- 注意：在Spring Boot中，实际的表会由JPA自动创建

-- 创建扩展（如果需要）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- 插入初始数据
-- 这些数据会在应用启动后通过Java代码插入

-- 创建管理员用户（密码应该在应用层进行哈希处理）
-- INSERT INTO users (username, email, password, display_name, role, skill_level, is_active, email_verified, created_at, updated_at)
-- VALUES ('admin', 'admin@buptcs.com', '$2a$12$hashed_password_here', '系统管理员', 'ADMIN', 'EXPERT', true, true, NOW(), NOW());

-- 创建一些示例课程分类
-- INSERT INTO course_categories (name, description, created_at, updated_at)
-- VALUES 
--   ('前端开发', '学习现代前端开发技术栈', NOW(), NOW()),
--   ('后端开发', '掌握服务器端开发技能', NOW(), NOW()),
--   ('数据库', '数据库设计与管理', NOW(), NOW()),
--   ('算法与数据结构', '计算机科学基础', NOW(), NOW()),
--   ('系统设计', '大型系统架构设计', NOW(), NOW());

-- 创建学习路径模板
-- INSERT INTO learning_paths (name, description, difficulty_level, estimated_duration_days, created_at, updated_at)
-- VALUES
--   ('前端开发入门', '从零开始学习前端开发', 'BEGINNER', 90, NOW(), NOW()),
--   ('全栈工程师进阶', '成为全栈开发工程师', 'INTERMEDIATE', 180, NOW(), NOW()),
--   ('架构师之路', '系统架构设计与实践', 'ADVANCED', 365, NOW(), NOW());

COMMIT;