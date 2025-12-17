package com.nkust.oepp.service;

import com.nkust.oepp.dto.CarouselImageRequest;
import com.nkust.oepp.dto.CarouselImageResponse;
import com.nkust.oepp.dto.CarouselImageUpdateRequest;
import com.nkust.oepp.entity.CarouselImage;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.repository.CarouselImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarouselImageService {
    
    @Autowired
    private CarouselImageRepository carouselImageRepository;
    
    @Autowired
    private ImageKitService imageKitService;
    
    /**
     * 獲取當前用戶的角色
     */
    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String roleCode = userDetails.getAuthorities().iterator().next().getAuthority()
                    .replace("ROLE_", "");
            return Role.fromCode(roleCode);
        }
        return null;
    }
    
    /**
     * 檢查用戶是否有權限操作該中心
     */
    private boolean hasPermission(Role centerRole) {
        Role currentRole = getCurrentUserRole();
        if (currentRole == null) {
            return false;
        }
        // 超級管理員可以操作所有中心
        if (currentRole == Role.SUPER_ADMIN) {
            return true;
        }
        // 其他用戶只能操作自己中心的資源
        return currentRole == centerRole;
    }
    
    /**
     * 創建輪播圖
     */
    @Transactional
    public CarouselImageResponse create(CarouselImageRequest request) throws IOException {
        // 權限檢查
        if (!hasPermission(request.getCenterRole())) {
            throw new RuntimeException("無權限創建該中心的輪播圖");
        }
        
        // 上傳圖片到 ImageKit
        String imageUrl = imageKitService.uploadImage(
            request.getImage(), 
            null, 
            "carousel"
        );
        
        // 創建輪播圖實體
        CarouselImage carouselImage = new CarouselImage();
        carouselImage.setTitle(request.getTitle());
        carouselImage.setCenterRole(request.getCenterRole());
        carouselImage.setImageUrl(imageUrl);
        carouselImage.setClickUrl(request.getClickUrl());
        carouselImage.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        carouselImage.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);
        
        carouselImage = carouselImageRepository.save(carouselImage);
        
        return new CarouselImageResponse(carouselImage);
    }
    
    /**
     * 更新輪播圖
     */
    @Transactional
    public CarouselImageResponse update(Long id, CarouselImageUpdateRequest request) throws IOException {
        CarouselImage carouselImage = carouselImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("輪播圖不存在"));
        
        // 權限檢查
        if (!hasPermission(carouselImage.getCenterRole())) {
            throw new RuntimeException("無權限更新該輪播圖");
        }
        
        // 更新欄位
        if (request.getTitle() != null) {
            carouselImage.setTitle(request.getTitle());
        }
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // 上傳新圖片
            String imageUrl = imageKitService.uploadImage(
                request.getImage(), 
                null, 
                "carousel"
            );
            carouselImage.setImageUrl(imageUrl);
        }
        if (request.getClickUrl() != null) {
            carouselImage.setClickUrl(request.getClickUrl());
        }
        if (request.getSortOrder() != null) {
            carouselImage.setSortOrder(request.getSortOrder());
        }
        if (request.getIsEnabled() != null) {
            carouselImage.setIsEnabled(request.getIsEnabled());
        }
        
        carouselImage = carouselImageRepository.save(carouselImage);
        
        return new CarouselImageResponse(carouselImage);
    }
    
    /**
     * 刪除輪播圖
     */
    @Transactional
    public void delete(Long id) {
        CarouselImage carouselImage = carouselImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("輪播圖不存在"));
        
        // 權限檢查
        if (!hasPermission(carouselImage.getCenterRole())) {
            throw new RuntimeException("無權限刪除該輪播圖");
        }
        
        carouselImageRepository.delete(carouselImage);
    }
    
    /**
     * 根據 ID 獲取輪播圖
     */
    public CarouselImageResponse getById(Long id) {
        CarouselImage carouselImage = carouselImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("輪播圖不存在"));
        
        // 權限檢查
        if (!hasPermission(carouselImage.getCenterRole())) {
            throw new RuntimeException("無權限查看該輪播圖");
        }
        
        return new CarouselImageResponse(carouselImage);
    }
    
    /**
     * 獲取所有輪播圖（根據用戶權限過濾）
     */
    public List<CarouselImageResponse> getAll() {
        Role currentRole = getCurrentUserRole();
        if (currentRole == null) {
            throw new RuntimeException("無法獲取用戶角色");
        }
        
        List<CarouselImage> carouselImages;
        if (currentRole == Role.SUPER_ADMIN) {
            // 超級管理員可以看到所有輪播圖
            carouselImages = carouselImageRepository.findAll();
        } else {
            // 其他用戶只能看到自己中心的輪播圖
            carouselImages = carouselImageRepository.findByCenterRoleOrderBySortOrderAsc(currentRole);
        }
        
        return carouselImages.stream()
                .map(CarouselImageResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 根據中心角色獲取輪播圖（公開 API）
     */
    public List<CarouselImageResponse> getByCenterRole(Role centerRole, Boolean onlyEnabled) {
        List<CarouselImage> carouselImages;
        if (onlyEnabled != null && onlyEnabled) {
            carouselImages = carouselImageRepository
                    .findByCenterRoleAndIsEnabledOrderBySortOrderAsc(centerRole, true);
        } else {
            carouselImages = carouselImageRepository
                    .findByCenterRoleOrderBySortOrderAsc(centerRole);
        }
        
        return carouselImages.stream()
                .map(CarouselImageResponse::new)
                .collect(Collectors.toList());
    }
}

