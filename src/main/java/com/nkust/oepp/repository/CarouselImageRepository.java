package com.nkust.oepp.repository;

import com.nkust.oepp.entity.CarouselImage;
import com.nkust.oepp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselImageRepository extends JpaRepository<CarouselImage, Long> {
    
    /**
     * 根據中心角色查詢輪播圖
     */
    List<CarouselImage> findByCenterRole(Role centerRole);
    
    /**
     * 根據中心角色和啟用狀態查詢輪播圖
     */
    List<CarouselImage> findByCenterRoleAndIsEnabled(Role centerRole, Boolean isEnabled);
    
    /**
     * 根據中心角色查詢，按排序順序排列
     */
    List<CarouselImage> findByCenterRoleOrderBySortOrderAsc(Role centerRole);
    
    /**
     * 根據中心角色和啟用狀態查詢，按排序順序排列
     */
    List<CarouselImage> findByCenterRoleAndIsEnabledOrderBySortOrderAsc(Role centerRole, Boolean isEnabled);
}

