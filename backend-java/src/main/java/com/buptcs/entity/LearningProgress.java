package com.buptcs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 学习进度实体类
 */
@Entity
@Table(name = "learning_progress", indexes = {
    @Index(name = "idx_progress_user_course", columnList = "user_id, course_id"),
    @Index(name = "idx_progress_status", columnList = "status"),
    @Index(name = "idx_progress_updated", columnList = "updated_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "课程ID不能为空")
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @NotBlank(message = "课程名称不能为空")
    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    @NotBlank(message = "课程类型不能为空")
    @Column(name = "course_type", nullable = false, length = 50)
    private String courseType;

    @Min(value = 0, message = "进度百分比不能小于0")
    @Max(value = 100, message = "进度百分比不能大于100")
    @Column(name = "progress_percentage", nullable = false)
    @Builder.Default
    private Integer progressPercentage = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    @Column(name = "current_chapter", length = 200)
    private String currentChapter;

    @Column(name = "current_lesson", length = 200)
    private String currentLesson;

    @Min(value = 0, message = "学习时长不能为负数")
    @Column(name = "time_spent_minutes", nullable = false)
    @Builder.Default
    private Integer timeSpentMinutes = 0;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "target_completion_date")
    private LocalDateTime targetCompletionDate;

    @Min(value = 1, message = "评分不能小于1")
    @Max(value = 5, message = "评分不能大于5")
    @Column(name = "user_rating")
    private Integer userRating;

    @Column(name = "user_review", length = 1000)
    private String userReview;

    @Column(name = "difficulty_rating")
    private Integer difficultyRating;

    @Column(name = "is_favorite", nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 进度状态枚举
     */
    public enum ProgressStatus {
        NOT_STARTED("未开始"),
        IN_PROGRESS("进行中"),
        COMPLETED("已完成"),
        PAUSED("已暂停"),
        DROPPED("已放弃");

        private final String description;

        ProgressStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 更新进度百分比
     */
    public void updateProgress(int newProgress) {
        this.progressPercentage = Math.max(0, Math.min(100, newProgress));
        this.lastAccessedAt = LocalDateTime.now();
        
        if (this.progressPercentage == 100 && this.status != ProgressStatus.COMPLETED) {
            this.status = ProgressStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        } else if (this.progressPercentage > 0 && this.status == ProgressStatus.NOT_STARTED) {
            this.status = ProgressStatus.IN_PROGRESS;
        }
    }

    /**
     * 添加学习时长
     */
    public void addTimeSpent(int minutes) {
        this.timeSpentMinutes += minutes;
        this.lastAccessedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}