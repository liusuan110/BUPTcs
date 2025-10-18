package com.buptcs.controller;

import com.buptcs.dto.UserDto;
import com.buptcs.security.CurrentUser;
import com.buptcs.security.UserPrincipal;
import com.buptcs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "用户管理", description = "用户注册、登录、资料管理等API")
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public ResponseEntity<UserDto.ApiResponse<UserDto.UserInfo>> register(
            @Valid @RequestBody UserDto.RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        
        UserDto.UserInfo userInfo = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(UserDto.ApiResponse.success("注册成功，请查收邮箱验证邮件", userInfo));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户账号密码登录")
    public ResponseEntity<UserDto.ApiResponse<UserDto.LoginResponse>> login(
            @Valid @RequestBody UserDto.LoginRequest request) {
        log.info("用户登录请求: {}", request.getUsernameOrEmail());
        
        UserDto.LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("登录成功", response));
    }

    /**
     * 刷新访问令牌
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ResponseEntity<UserDto.ApiResponse<UserDto.LoginResponse>> refreshToken(
            @Valid @RequestBody UserDto.RefreshTokenRequest request) {
        log.info("刷新令牌请求");
        
        UserDto.LoginResponse response = userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(UserDto.ApiResponse.success("令牌刷新成功", response));
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public ResponseEntity<UserDto.ApiResponse<UserDto.UserInfo>> getCurrentUser(
            @CurrentUser UserPrincipal currentUser) {
        log.info("获取用户信息: {}", currentUser.getId());
        
        UserDto.UserInfo userInfo = userService.getUserInfo(currentUser.getId());
        return ResponseEntity.ok(UserDto.ApiResponse.success(userInfo));
    }

    /**
     * 更新用户资料
     */
    @PutMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "更新用户资料", description = "更新当前用户的个人资料")
    public ResponseEntity<UserDto.ApiResponse<UserDto.UserInfo>> updateProfile(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody UserDto.UpdateProfileRequest request) {
        log.info("更新用户资料: {}", currentUser.getId());
        
        UserDto.UserInfo userInfo = userService.updateUserProfile(currentUser.getId(), request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("资料更新成功", userInfo));
    }

    /**
     * 更改密码
     */
    @PutMapping("/me/password")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "更改密码", description = "更改当前用户密码")
    public ResponseEntity<UserDto.ApiResponse<Void>> changePassword(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody UserDto.ChangePasswordRequest request) {
        log.info("更改密码: {}", currentUser.getId());
        
        userService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("密码更改成功", null));
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "忘记密码", description = "发送密码重置邮件")
    public ResponseEntity<UserDto.ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody UserDto.ForgotPasswordRequest request) {
        log.info("忘记密码请求: {}", request.getEmail());
        
        userService.sendPasswordResetEmail(request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("密码重置邮件已发送", null));
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "使用重置令牌重置密码")
    public ResponseEntity<UserDto.ApiResponse<Void>> resetPassword(
            @Valid @RequestBody UserDto.ResetPasswordRequest request) {
        log.info("重置密码请求");
        
        userService.resetPassword(request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("密码重置成功", null));
    }

    /**
     * 邮箱验证
     */
    @PostMapping("/verify-email")
    @Operation(summary = "邮箱验证", description = "验证用户邮箱地址")
    public ResponseEntity<UserDto.ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody UserDto.EmailVerificationRequest request) {
        log.info("邮箱验证请求");
        
        userService.verifyEmail(request);
        return ResponseEntity.ok(UserDto.ApiResponse.success("邮箱验证成功", null));
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "搜索用户", description = "根据关键字搜索用户")
    public ResponseEntity<UserDto.ApiResponse<Page<UserDto.UserSearchResult>>> searchUsers(
            @Parameter(description = "搜索关键字") @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("搜索用户: {}", keyword);
        
        Page<UserDto.UserSearchResult> results = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(UserDto.ApiResponse.success(results));
    }

    /**
     * 获取指定用户信息（公开信息）
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "获取指定用户的公开信息")
    public ResponseEntity<UserDto.ApiResponse<UserDto.UserSearchResult>> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        log.info("获取用户信息: {}", userId);
        
        // 这里可以返回用户的公开信息
        UserDto.UserInfo userInfo = userService.getUserInfo(userId);
        UserDto.UserSearchResult result = UserDto.UserSearchResult.builder()
            .id(userInfo.getId())
            .username(userInfo.getUsername())
            .displayName(userInfo.getDisplayName())
            .avatarUrl(userInfo.getAvatarUrl())
            .bio(userInfo.getBio())
            .role(userInfo.getRole())
            .skillLevel(userInfo.getSkillLevel())
            .createdAt(userInfo.getCreatedAt())
            .build();
        
        return ResponseEntity.ok(UserDto.ApiResponse.success(result));
    }

    /**
     * 删除当前用户账号
     */
    @DeleteMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "删除账号", description = "删除当前用户账号")
    public ResponseEntity<UserDto.ApiResponse<Void>> deleteCurrentUser(
            @CurrentUser UserPrincipal currentUser) {
        log.info("删除用户账号: {}", currentUser.getId());
        
        userService.deleteUser(currentUser.getId());
        return ResponseEntity.ok(UserDto.ApiResponse.success("账号删除成功", null));
    }

    /**
     * 管理员删除用户（需要管理员权限）
     */
    @DeleteMapping("/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除用户", description = "管理员删除指定用户（需要管理员权限）")
    public ResponseEntity<UserDto.ApiResponse<Void>> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        log.info("管理员删除用户: {}", userId);
        
        userService.deleteUser(userId);
        return ResponseEntity.ok(UserDto.ApiResponse.success("用户删除成功", null));
    }
}