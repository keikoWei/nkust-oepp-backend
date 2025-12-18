package com.nkust.oepp.repository;

import com.nkust.oepp.entity.Course;
import com.nkust.oepp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * 根據中心角色查詢（按排序和建立時間排序）
     */
    @Query("SELECT c FROM Course c WHERE c.centerRole = :centerRole ORDER BY c.sortOrder ASC, c.createdAt DESC")
    List<Course> findByCenterRole(@Param("centerRole") Role centerRole);
    
    /**
     * 根據中心角色和啟用狀態查詢（按排序和建立時間排序）
     */
    @Query("SELECT c FROM Course c WHERE c.centerRole = :centerRole AND c.isEnabled = :isEnabled ORDER BY c.sortOrder ASC, c.createdAt DESC")
    List<Course> findByCenterRoleAndIsEnabled(@Param("centerRole") Role centerRole, @Param("isEnabled") Boolean isEnabled);
    
    /**
     * 查詢已發布的課程（公開時間已到且啟用，按排序和公開時間排序）
     */
    @Query("SELECT c FROM Course c WHERE c.centerRole = :centerRole " +
           "AND c.isEnabled = true " +
           "AND (c.publishTime IS NULL OR c.publishTime <= :now) " +
           "ORDER BY c.sortOrder ASC, c.publishTime DESC, c.createdAt DESC")
    List<Course> findPublishedByCenterRole(@Param("centerRole") Role centerRole, @Param("now") LocalDateTime now);
    
    /**
     * 根據啟用狀態查詢
     */
    List<Course> findByIsEnabled(Boolean isEnabled);
}

