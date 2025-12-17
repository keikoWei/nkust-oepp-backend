package com.nkust.oepp.repository;

import com.nkust.oepp.entity.News;
import com.nkust.oepp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
    /**
     * 根據中心角色查詢（按排序和建立時間排序）
     */
    @Query("SELECT n FROM News n WHERE n.centerRole = :centerRole ORDER BY n.sortOrder ASC, n.createdAt DESC")
    List<News> findByCenterRole(@Param("centerRole") Role centerRole);
    
    /**
     * 根據中心角色和啟用狀態查詢（按排序和建立時間排序）
     */
    @Query("SELECT n FROM News n WHERE n.centerRole = :centerRole AND n.isEnabled = :isEnabled ORDER BY n.sortOrder ASC, n.createdAt DESC")
    List<News> findByCenterRoleAndIsEnabled(@Param("centerRole") Role centerRole, @Param("isEnabled") Boolean isEnabled);
    
    /**
     * 查詢已發布的消息（公開時間已到且啟用，按排序和公開時間排序）
     */
    @Query("SELECT n FROM News n WHERE n.centerRole = :centerRole " +
           "AND n.isEnabled = true " +
           "AND (n.publishTime IS NULL OR n.publishTime <= :now) " +
           "ORDER BY n.sortOrder ASC, n.publishTime DESC, n.createdAt DESC")
    List<News> findPublishedByCenterRole(@Param("centerRole") Role centerRole, @Param("now") LocalDateTime now);
    
    /**
     * 根據啟用狀態查詢
     */
    List<News> findByIsEnabled(Boolean isEnabled);
}

