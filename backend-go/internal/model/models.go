package model

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

// 用户模型
type User struct {
	ID        uuid.UUID      `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	Username  string         `json:"username" gorm:"uniqueIndex;not null"`
	Email     string         `json:"email" gorm:"uniqueIndex;not null"`
	Password  string         `json:"-" gorm:"not null"`
	FirstName string         `json:"first_name"`
	LastName  string         `json:"last_name"`
	Avatar    string         `json:"avatar"`
	Bio       string         `json:"bio"`
	
	// 学习相关信息
	University   string `json:"university"`
	Major        string `json:"major"`
	Year         int    `json:"year"`
	CurrentLevel string `json:"current_level" gorm:"default:'beginner'"`
	Experience   int    `json:"experience" gorm:"default:0"`
	Streak       int    `json:"streak" gorm:"default:0"`
	
	// 系统字段
	IsActive  bool           `json:"is_active" gorm:"default:true"`
	Role      string         `json:"role" gorm:"default:'student'"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `json:"-" gorm:"index"`
	LastLogin *time.Time     `json:"last_login"`
	
	// 关联
	Enrollments []Enrollment `json:"enrollments,omitempty"`
	Progresses  []Progress   `json:"progresses,omitempty"`
}

// 课程模型
type Course struct {
	ID          uuid.UUID `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	Title       string    `json:"title" gorm:"not null"`
	Description string    `json:"description"`
	Category    string    `json:"category" gorm:"not null"`
	Subcategory string    `json:"subcategory"`
	Level       string    `json:"level" gorm:"not null"` // beginner, intermediate, advanced
	Duration    int       `json:"duration"`              // 分钟
	
	// 内容相关
	ThumbnailURL string `json:"thumbnail_url"`
	VideoURL     string `json:"video_url"`
	MaterialsURL string `json:"materials_url"`
	
	// 统计信息
	Enrollments   int     `json:"enrollments" gorm:"default:0"`
	Completions   int     `json:"completions" gorm:"default:0"`
	Rating        float64 `json:"rating" gorm:"default:0"`
	ReviewCount   int     `json:"review_count" gorm:"default:0"`
	
	// 定价
	IsFree   bool    `json:"is_free" gorm:"default:true"`
	Price    float64 `json:"price" gorm:"default:0"`
	Currency string  `json:"currency" gorm:"default:'CNY'"`
	
	// 系统字段
	Status      string         `json:"status" gorm:"default:'draft'"` // draft, published, archived
	CreatedAt   time.Time      `json:"created_at"`
	UpdatedAt   time.Time      `json:"updated_at"`
	DeletedAt   gorm.DeletedAt `json:"-" gorm:"index"`
	PublishedAt *time.Time     `json:"published_at"`
	
	// 关联
	Lessons     []Lesson     `json:"lessons,omitempty"`
	Enrollments []Enrollment `json:"enrollments,omitempty"`
	Progresses  []Progress   `json:"progresses,omitempty"`
}

// 课程章节模型
type Lesson struct {
	ID       uuid.UUID `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	CourseID uuid.UUID `json:"course_id" gorm:"not null"`
	Title    string    `json:"title" gorm:"not null"`
	Content  string    `json:"content"`
	VideoURL string    `json:"video_url"`
	Duration int       `json:"duration"` // 分钟
	Order    int       `json:"order" gorm:"not null"`
	
	// 系统字段
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `json:"-" gorm:"index"`
	
	// 关联
	Course Course `json:"course,omitempty"`
}

// 用户课程注册模型
type Enrollment struct {
	ID       uuid.UUID `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	UserID   uuid.UUID `json:"user_id" gorm:"not null"`
	CourseID uuid.UUID `json:"course_id" gorm:"not null"`
	
	EnrolledAt  time.Time  `json:"enrolled_at"`
	CompletedAt *time.Time `json:"completed_at"`
	
	// 关联
	User   User   `json:"user,omitempty"`
	Course Course `json:"course,omitempty"`
}

// 学习进度模型
type Progress struct {
	ID       uuid.UUID `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	UserID   uuid.UUID `json:"user_id" gorm:"not null"`
	CourseID uuid.UUID `json:"course_id" gorm:"not null"`
	LessonID uuid.UUID `json:"lesson_id" gorm:"not null"`
	
	// 进度信息
	IsCompleted        bool      `json:"is_completed" gorm:"default:false"`
	CompletionProgress float64   `json:"completion_progress" gorm:"default:0"` // 0-100
	TimeSpent          int       `json:"time_spent" gorm:"default:0"`          // 分钟
	LastAccessedAt     time.Time `json:"last_accessed_at"`
	CompletedAt        *time.Time `json:"completed_at"`
	
	// 系统字段
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `json:"-" gorm:"index"`
	
	// 关联
	User   User   `json:"user,omitempty"`
	Course Course `json:"course,omitempty"`
	Lesson Lesson `json:"lesson,omitempty"`
}

// 用户偏好设置模型
type UserPreference struct {
	ID     uuid.UUID `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()"`
	UserID uuid.UUID `json:"user_id" gorm:"uniqueIndex;not null"`
	
	// 学习偏好
	LearningStyle string `json:"learning_style"` // visual, auditory, kinesthetic
	Difficulty    string `json:"difficulty"`     // beginner, intermediate, advanced
	DailyGoal     int    `json:"daily_goal"`     // 每日学习目标（分钟）
	
	// 通知设置
	EmailNotifications bool `json:"email_notifications" gorm:"default:true"`
	PushNotifications  bool `json:"push_notifications" gorm:"default:true"`
	
	// 隐私设置
	ProfilePublic   bool `json:"profile_public" gorm:"default:true"`
	ProgressPublic  bool `json:"progress_public" gorm:"default:true"`
	
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `json:"-" gorm:"index"`
	
	// 关联
	User User `json:"user,omitempty"`
}