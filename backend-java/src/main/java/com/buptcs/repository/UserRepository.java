package com.buptcs.repository;

import com.buptcs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据用户名或邮箱查找用户
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据邮箱验证令牌查找用户
     */
    Optional<User> findByEmailVerificationToken(String token);

    /**
     * 根据密码重置令牌查找用户
     */
    Optional<User> findByPasswordResetToken(String token);

    /**
     * 查找活跃用户
     */
    List<User> findByIsActiveTrue();

    /**
     * 根据角色查找用户
     */
    List<User> findByRole(User.UserRole role);

    /**
     * 根据技能水平查找用户
     */
    List<User> findBySkillLevel(User.SkillLevel skillLevel);

    /**
     * 分页查询活跃用户
     */
    Page<User> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 根据用户名模糊搜索
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.displayName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchActiveUsers(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 统计用户数量按角色
     */
    @Query("SELECT u.role, COUNT(u) FROM User u WHERE u.isActive = true GROUP BY u.role")
    List<Object[]> countUsersByRole();

    /**
     * 统计用户数量按技能水平
     */
    @Query("SELECT u.skillLevel, COUNT(u) FROM User u WHERE u.isActive = true GROUP BY u.skillLevel")
    List<Object[]> countUsersBySkillLevel();

    /**
     * 查找最近注册的用户
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.createdAt >= :since ORDER BY u.createdAt DESC")
    List<User> findRecentlyRegisteredUsers(@Param("since") LocalDateTime since);

    /**
     * 查找最近登录的用户
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.lastLoginAt >= :since ORDER BY u.lastLoginAt DESC")
    List<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since);

    /**
     * 更新用户最后登录时间
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime, u.loginCount = u.loginCount + 1 WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 软删除用户（设置为非活跃状态）
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") Long userId);

    /**
     * 批量软删除用户
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id IN :userIds")
    void softDeleteUsers(@Param("userIds") List<Long> userIds);

    /**
     * 清除密码重置令牌
     */
    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = null, u.passwordResetExpiresAt = null WHERE u.id = :userId")
    void clearPasswordResetToken(@Param("userId") Long userId);

    /**
     * 验证邮箱
     */
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true, u.emailVerificationToken = null WHERE u.id = :userId")
    void verifyEmail(@Param("userId") Long userId);

    /**
     * 查找需要清理的过期密码重置令牌
     */
    @Query("SELECT u FROM User u WHERE u.passwordResetToken IS NOT NULL AND u.passwordResetExpiresAt < :now")
    List<User> findUsersWithExpiredPasswordResetTokens(@Param("now") LocalDateTime now);

    /**
     * 统计今日新增用户数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    long countTodayRegistrations();

    /**
     * 统计本周活跃用户数
     */
    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE u.lastLoginAt >= :weekAgo")
    long countWeeklyActiveUsers(@Param("weekAgo") LocalDateTime weekAgo);

    /**
     * 查找有特定兴趣的用户
     */
    @Query("SELECT u FROM User u JOIN u.interests i WHERE i = :interest AND u.isActive = true")
    List<User> findUsersByInterest(@Param("interest") String interest);
}