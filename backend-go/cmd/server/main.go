package main

import (
	"cs-learning-backend/internal/config"
	"cs-learning-backend/internal/handler"
	"cs-learning-backend/internal/middleware"
	"cs-learning-backend/internal/repository"
	"cs-learning-backend/internal/service"
	"cs-learning-backend/pkg/database"
	"cs-learning-backend/pkg/logger"
	"cs-learning-backend/pkg/redis"
	"log"
	"net/http"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"go.uber.org/zap"
)

// @title CS Learning API
// @version 1.0
// @description 大学生计算机技能自学软件API
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.swagger.io/support
// @contact.email support@swagger.io

// @license.name MIT
// @license.url https://opensource.org/licenses/MIT

// @host localhost:5000
// @BasePath /api/v1

// @securityDefinitions.apikey Bearer
// @in header
// @name Authorization
// @description Type "Bearer" followed by a space and JWT token.

func main() {
	// 初始化配置
	cfg, err := config.Load()
	if err != nil {
		log.Fatal("Failed to load config:", err)
	}

	// 初始化日志
	logger.Init(cfg.LogLevel)
	defer logger.Sync()

	// 初始化数据库
	db, err := database.Init(cfg.Database.DSN)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}

	// 初始化Redis
	rdb, err := redis.Init(cfg.Redis.URL)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}

	// 初始化Repository层
	userRepo := repository.NewUserRepository(db)
	courseRepo := repository.NewCourseRepository(db)
	progressRepo := repository.NewProgressRepository(db)

	// 初始化Service层
	userService := service.NewUserService(userRepo, rdb)
	courseService := service.NewCourseService(courseRepo)
	progressService := service.NewProgressService(progressRepo)
	authService := service.NewAuthService(userRepo, cfg.JWT.Secret)

	// 初始化Handler层
	userHandler := handler.NewUserHandler(userService)
	courseHandler := handler.NewCourseHandler(courseService)
	progressHandler := handler.NewProgressHandler(progressService)
	authHandler := handler.NewAuthHandler(authService)

	// 设置路由
	router := setupRouter(cfg, authHandler, userHandler, courseHandler, progressHandler)

	// 启动服务器
	logger.Info("Starting server", zap.String("port", cfg.Server.Port))
	if err := router.Run(":" + cfg.Server.Port); err != nil {
		logger.Fatal("Failed to start server", zap.Error(err))
	}
}

func setupRouter(
	cfg *config.Config,
	authHandler *handler.AuthHandler,
	userHandler *handler.UserHandler,
	courseHandler *handler.CourseHandler,
	progressHandler *handler.ProgressHandler,
) *gin.Engine {
	// 根据环境设置Gin模式
	if cfg.Env == "production" {
		gin.SetMode(gin.ReleaseMode)
	}

	router := gin.New()

	// 中间件
	router.Use(gin.Logger())
	router.Use(gin.Recovery())
	router.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"http://localhost:3000"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"*"},
		AllowCredentials: true,
	}))

	// 健康检查
	router.GET("/health", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{"status": "healthy"})
	})

	// API路由组
	v1 := router.Group("/api/v1")
	{
		// 认证路由
		auth := v1.Group("/auth")
		{
			auth.POST("/register", authHandler.Register)
			auth.POST("/login", authHandler.Login)
			auth.POST("/refresh", authHandler.RefreshToken)
			auth.POST("/logout", middleware.AuthMiddleware(cfg.JWT.Secret), authHandler.Logout)
		}

		// 用户路由
		users := v1.Group("/users")
		users.Use(middleware.AuthMiddleware(cfg.JWT.Secret))
		{
			users.GET("/profile", userHandler.GetProfile)
			users.PUT("/profile", userHandler.UpdateProfile)
			users.GET("/progress", userHandler.GetUserProgress)
		}

		// 课程路由
		courses := v1.Group("/courses")
		{
			courses.GET("", courseHandler.GetCourses)
			courses.GET("/:id", courseHandler.GetCourse)
			courses.GET("/:id/lessons", courseHandler.GetCourseLessons)
		}

		// 学习进度路由
		progress := v1.Group("/progress")
		progress.Use(middleware.AuthMiddleware(cfg.JWT.Secret))
		{
			progress.POST("/start", progressHandler.StartCourse)
			progress.PUT("/update", progressHandler.UpdateProgress)
			progress.GET("/stats", progressHandler.GetProgressStats)
		}
	}

	// Swagger文档
	router.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	return router
}