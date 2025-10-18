package com.buptcs.dto;

import com.buptcs.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户相关DTO类
 */
public class UserDto {

    /**
     * 用户注册请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        @Size(max = 100, message = "邮箱长度不能超过100个字符")
        private String email;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 128, message = "密码长度必须在6-128个字符之间")
        private String password;

        @Size(max = 100, message = "昵称长度不能超过100个字符")
        private String displayName;
    }

    /**
     * 用户登录请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "用户名或邮箱不能为空")
        private String usernameOrEmail;

        @NotBlank(message = "密码不能为空")
        private String password;

        private Boolean rememberMe = false;
    }

    /**
     * 用户登录响应DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserInfo userInfo;
    }

    /**
     * 用户信息DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String displayName;
        private String avatarUrl;
        private String bio;
        private Boolean emailVerified;
        private User.UserRole role;
        private User.SkillLevel skillLevel;
        private Set<String> interests;
        private String learningGoals;
        private String preferredLanguage;
        private Integer loginCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastLoginAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    /**
     * 用户资料更新请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        @Size(max = 100, message = "昵称长度不能超过100个字符")
        private String displayName;

        @Size(max = 200, message = "头像URL长度不能超过200个字符")
        private String avatarUrl;

        @Size(max = 500, message = "个人简介长度不能超过500个字符")
        private String bio;

        private User.SkillLevel skillLevel;

        private Set<String> interests;

        @Size(max = 1000, message = "学习目标长度不能超过1000个字符")
        private String learningGoals;

        @Size(max = 10, message = "首选语言代码长度不能超过10个字符")
        private String preferredLanguage;
    }

    /**
     * 密码更改请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordRequest {
        @NotBlank(message = "当前密码不能为空")
        private String currentPassword;

        @NotBlank(message = "新密码不能为空")
        @Size(min = 6, max = 128, message = "新密码长度必须在6-128个字符之间")
        private String newPassword;

        @NotBlank(message = "确认密码不能为空")
        private String confirmPassword;
    }

    /**
     * 忘记密码请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequest {
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;
    }

    /**
     * 重置密码请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordRequest {
        @NotBlank(message = "重置令牌不能为空")
        private String token;

        @NotBlank(message = "新密码不能为空")
        @Size(min = 6, max = 128, message = "新密码长度必须在6-128个字符之间")
        private String newPassword;

        @NotBlank(message = "确认密码不能为空")
        private String confirmPassword;
    }

    /**
     * 邮箱验证请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailVerificationRequest {
        @NotBlank(message = "验证令牌不能为空")
        private String token;
    }

    /**
     * 用户搜索响应DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSearchResult {
        private Long id;
        private String username;
        private String displayName;
        private String avatarUrl;
        private String bio;
        private User.UserRole role;
        private User.SkillLevel skillLevel;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    /**
     * 刷新令牌请求DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        @NotBlank(message = "刷新令牌不能为空")
        private String refreshToken;
    }

    /**
     * 通用响应DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private Boolean success;
        private String message;
        private T data;
        private String timestamp;

        public static <T> ApiResponse<T> success(T data) {
            return ApiResponse.<T>builder()
                .success(true)
                .message("操作成功")
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
        }
    }
}