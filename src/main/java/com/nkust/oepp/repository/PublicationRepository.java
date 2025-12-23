package com.nkust.oepp.repository;

import com.nkust.oepp.entity.Publication;
import com.nkust.oepp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    
    /**
     * 根據中心角色查詢（按排序和建立時間排序）
     */
    @Query("SELECT p FROM Publication p WHERE p.centerRole = :centerRole ORDER BY p.sortOrder ASC, p.createdAt DESC")
    List<Publication> findByCenterRole(@Param("centerRole") Role centerRole);
}





