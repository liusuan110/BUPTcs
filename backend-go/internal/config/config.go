package config

import (
	"os"

	"github.com/joho/godotenv"
	"github.com/spf13/viper"
)

type Config struct {
	Env      string         `mapstructure:"ENV"`
	Server   ServerConfig   `mapstructure:"server"`
	Database DatabaseConfig `mapstructure:"database"`
	Redis    RedisConfig    `mapstructure:"redis"`
	JWT      JWTConfig      `mapstructure:"jwt"`
	LogLevel string         `mapstructure:"log_level"`
	AI       AIConfig       `mapstructure:"ai"`
}

type ServerConfig struct {
	Port string `mapstructure:"port"`
	Host string `mapstructure:"host"`
}

type DatabaseConfig struct {
	DSN string `mapstructure:"dsn"`
}

type RedisConfig struct {
	URL string `mapstructure:"url"`
}

type JWTConfig struct {
	Secret string `mapstructure:"secret"`
	Expire string `mapstructure:"expire"`
}

type AIConfig struct {
	ServiceURL string `mapstructure:"service_url"`
	APIKey     string `mapstructure:"api_key"`
}

func Load() (*Config, error) {
	// 加载.env文件
	_ = godotenv.Load()

	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath("./configs")
	viper.AddConfigPath(".")

	// 设置默认值
	setDefaults()

	// 绑定环境变量
	bindEnvs()

	// 读取配置文件
	if err := viper.ReadInConfig(); err != nil {
		// 如果没有配置文件，使用环境变量
		if _, ok := err.(viper.ConfigFileNotFoundError); !ok {
			return nil, err
		}
	}

	var config Config
	if err := viper.Unmarshal(&config); err != nil {
		return nil, err
	}

	return &config, nil
}

func setDefaults() {
	viper.SetDefault("ENV", "development")
	viper.SetDefault("server.port", "5000")
	viper.SetDefault("server.host", "localhost")
	viper.SetDefault("log_level", "info")
	viper.SetDefault("jwt.expire", "24h")
}

func bindEnvs() {
	viper.BindEnv("ENV")
	viper.BindEnv("server.port", "PORT")
	viper.BindEnv("database.dsn", "DATABASE_URL")
	viper.BindEnv("redis.url", "REDIS_URL")
	viper.BindEnv("jwt.secret", "JWT_SECRET")
	viper.BindEnv("ai.service_url", "AI_SERVICE_URL")
	viper.BindEnv("ai.api_key", "OPENAI_API_KEY")

	// 从环境变量设置数据库DSN
	if dbURL := os.Getenv("DATABASE_URL"); dbURL != "" {
		viper.Set("database.dsn", dbURL)
	} else {
		// 默认PostgreSQL连接
		viper.SetDefault("database.dsn", "postgres://postgres:password@localhost:5432/cs_learning?sslmode=disable")
	}
}