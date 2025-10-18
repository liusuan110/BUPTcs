package com.buptcs.service;

import com.buptcs.dto.UserDto;
import com.buptcs.entity.User;
import com.buptcs.exception.ResourceNotFoundException;
import com.buptcs.exception.UserAlreadyExistsException;
import com.buptcs.exception.InvalidCredentialsException;
import com.buptcs.repository.UserRepository;
import com.buptcs.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务类
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    /**
     * 用户注册
     */
    public UserDto.UserInfo registerUser(UserDto.RegisterRequest request) {
        log.info("开始注册用户: {}", request.getUsername());

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("用户名已存在: " + request.getUsername());
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("邮箱已存在: " + request.getEmail());
        }

        // 创建新用户
        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .displayName(request.getDisplayName() != null ? request.getDisplayName() : request.getUsername())
            .emailVerificationToken(UUID.randomUUID().toString())
            .role(User.UserRole.STUDENT)
            .skillLevel(User.SkillLevel.BEGINNER)
            .isActive(true)
            .emailVerified(false)
            .loginCount(0)
            .build();

        User savedUser = userRepository.save(user);

        // 发送邮箱验证邮件
        emailService.sendEmailVerification(savedUser.getEmail(), savedUser.getEmailVerificationToken());

        log.info("用户注册成功: {} (ID: {})", savedUser.getUsername(), savedUser.getId());
        return convertToUserInfo(savedUser);
    }

    /**
     * 用户登录
     */
    public UserDto.LoginResponse loginUser(UserDto.LoginRequest request) {
        log.info("用户尝试登录: {}", request.getUsernameOrEmail());

        // 查找用户
        User user = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(), 
                request.getUsernameOrEmail()
            ).orElseThrow(() -> new InvalidCredentialsException("用户名或邮箱不存在"));

        // 检查用户是否活跃
        if (!user.getIsActive()) {
            throw new InvalidCredentialsException("账户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("密码错误");
        }

        // 更新登录信息
        userRepository.updateLastLogin(user.getId(), LocalDateTime.now());

        // 生成JWT令牌
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        log.info("用户登录成功: {} (ID: {})", user.getUsername(), user.getId());

        return UserDto.LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
            .userInfo(convertToUserInfo(user))
            .build();
    }

    /**
     * 刷新访问令牌
     */
    public UserDto.LoginResponse refreshAccessToken(String refreshToken) {
        log.info("刷新访问令牌");

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidCredentialsException("刷新令牌无效或已过期");
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);
        User user = getUserById(userId);

        String newAccessToken = jwtTokenProvider.generateAccessToken(
            user.getId(), user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        return UserDto.LoginResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
            .userInfo(convertToUserInfo(user))
            .build();
    }

    /**
     * 获取用户信息
     */
    @Transactional(readOnly = true)
    public UserDto.UserInfo getUserInfo(Long userId) {
        User user = getUserById(userId);
        return convertToUserInfo(user);
    }

    /**
     * 更新用户资料
     */
    public UserDto.UserInfo updateUserProfile(Long userId, UserDto.UpdateProfileRequest request) {
        log.info("更新用户资料: {}", userId);

        User user = getUserById(userId);

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getSkillLevel() != null) {
            user.setSkillLevel(request.getSkillLevel());
        }
        if (request.getInterests() != null) {
            user.setInterests(request.getInterests());
        }
        if (request.getLearningGoals() != null) {
            user.setLearningGoals(request.getLearningGoals());
        }
        if (request.getPreferredLanguage() != null) {
            user.setPreferredLanguage(request.getPreferredLanguage());
        }

        User updatedUser = userRepository.save(user);
        log.info("用户资料更新成功: {}", userId);

        return convertToUserInfo(updatedUser);
    }

    /**
     * 更改密码
     */
    public void changePassword(Long userId, UserDto.ChangePasswordRequest request) {
        log.info("更改用户密码: {}", userId);

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("新密码和确认密码不一致");
        }

        User user = getUserById(userId);

        // 验证当前密码
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("当前密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("用户密码更改成功: {}", userId);
    }

    /**
     * 发送密码重置邮件
     */
    public void sendPasswordResetEmail(UserDto.ForgotPasswordRequest request) {
        log.info("发送密码重置邮件: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("邮箱不存在: " + request.getEmail()));

        // 生成重置令牌
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1)); // 1小时后过期

        userRepository.save(user);

        // 发送重置邮件
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        log.info("密码重置邮件发送成功: {}", request.getEmail());
    }

    /**
     * 重置密码
     */
    public void resetPassword(UserDto.ResetPasswordRequest request) {
        log.info("重置密码");

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("新密码和确认密码不一致");
        }

        User user = userRepository.findByPasswordResetToken(request.getToken())
            .orElseThrow(() -> new InvalidCredentialsException("密码重置令牌无效"));

        // 检查令牌是否过期
        if (user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("密码重置令牌已过期");
        }

        // 更新密码并清除重置令牌
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);

        userRepository.save(user);

        log.info("密码重置成功: {}", user.getUsername());
    }

    /**
     * 邮箱验证
     */
    public void verifyEmail(UserDto.EmailVerificationRequest request) {
        log.info("验证邮箱");

        User user = userRepository.findByEmailVerificationToken(request.getToken())
            .orElseThrow(() -> new InvalidCredentialsException("邮箱验证令牌无效"));

        userRepository.verifyEmail(user.getId());

        log.info("邮箱验证成功: {}", user.getEmail());
    }

    /**
     * 搜索用户
     */
    @Transactional(readOnly = true)
    public Page<UserDto.UserSearchResult> searchUsers(String keyword, Pageable pageable) {
        Page<User> users = userRepository.searchActiveUsers(keyword, pageable);
        return users.map(this::convertToUserSearchResult);
    }

    /**
     * 根据ID获取用户
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + userId));
    }

    /**
     * 软删除用户
     */
    public void deleteUser(Long userId) {
        log.info("删除用户: {}", userId);

        User user = getUserById(userId);
        userRepository.softDeleteUser(userId);

        log.info("用户删除成功: {}", user.getUsername());
    }

    /**
     * 转换为UserInfo DTO
     */
    private UserDto.UserInfo convertToUserInfo(User user) {
        return UserDto.UserInfo.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .displayName(user.getDisplayName())
            .avatarUrl(user.getAvatarUrl())
            .bio(user.getBio())
            .emailVerified(user.getEmailVerified())
            .role(user.getRole())
            .skillLevel(user.getSkillLevel())
            .interests(user.getInterests())
            .learningGoals(user.getLearningGoals())
            .preferredLanguage(user.getPreferredLanguage())
            .loginCount(user.getLoginCount())
            .lastLoginAt(user.getLastLoginAt())
            .createdAt(user.getCreatedAt())
            .build();
    }

    /**
     * 转换为UserSearchResult DTO
     */
    private UserDto.UserSearchResult convertToUserSearchResult(User user) {
        return UserDto.UserSearchResult.builder()
            .id(user.getId())
            .username(user.getUsername())
            .displayName(user.getDisplayName())
            .avatarUrl(user.getAvatarUrl())
            .bio(user.getBio())
            .role(user.getRole())
            .skillLevel(user.getSkillLevel())
            .createdAt(user.getCreatedAt())
            .build();
    }
}