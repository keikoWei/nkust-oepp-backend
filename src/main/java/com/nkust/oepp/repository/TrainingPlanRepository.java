package com.nkust.oepp.repository;

import com.nkust.oepp.entity.Role;
import com.nkust.oepp.entity.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    
    /**
     * 根據中心角色查詢（按排序和建立時間排序）
     */
    @Query("SELECT t FROM TrainingPlan t WHERE t.centerRole = :centerRole ORDER BY t.sortOrder ASC, t.createdAt DESC")
    List<TrainingPlan> findByCenterRole(@Param("centerRole") Role centerRole);
}

