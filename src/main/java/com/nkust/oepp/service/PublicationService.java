package com.nkust.oepp.service;

import com.nkust.oepp.dto.PublicationRequest;
import com.nkust.oepp.dto.PublicationResponse;
import com.nkust.oepp.dto.PublicationUpdateRequest;
import com.nkust.oepp.entity.Publication;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.repository.PublicationRepository;
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
public class PublicationService {
    
    @Autowired
    private PublicationRepository publicationRepository;
    
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
        // 其他用戶只能操作自己的中心
        return currentRole == centerRole;
    }
    
    /**
     * 創建出版品
     */
    @Transactional
    public PublicationResponse create(PublicationRequest request) throws IOException {
        // 權限檢查
        if (!hasPermission(request.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的出版品");
        }
        
        // 上傳主圖片到 ImageKit
        String mainImageUrl = imageKitService.uploadImage(request.getMainImage(), null, "publications");
        
        // 創建出版品實體
        Publication publication = new Publication();
        publication.setCenterRole(request.getCenterRole());
        publication.setTitle(request.getTitle());
        publication.setMainImageUrl(mainImageUrl);
        publication.setIframeLink(request.getIframeLink());
        publication.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        
        publication = publicationRepository.save(publication);
        
        return new PublicationResponse(publication);
    }
    
    /**
     * 更新出版品
     */
    @Transactional
    public PublicationResponse update(Long id, PublicationUpdateRequest request) throws IOException {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的出版品"));
        
        // 權限檢查
        if (!hasPermission(publication.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的出版品");
        }
        
        // 更新基本資訊
        if (request.getTitle() != null) {
            publication.setTitle(request.getTitle());
        }
        if (request.getIframeLink() != null) {
            publication.setIframeLink(request.getIframeLink());
        }
        if (request.getSortOrder() != null) {
            publication.setSortOrder(request.getSortOrder());
        }
        
        // 更新主圖片
        if (request.getMainImage() != null && !request.getMainImage().isEmpty()) {
            String mainImageUrl = imageKitService.uploadImage(request.getMainImage(), null, "publications");
            publication.setMainImageUrl(mainImageUrl);
        }
        
        publication = publicationRepository.save(publication);
        
        return new PublicationResponse(publication);
    }
    
    /**
     * 刪除出版品
     */
    @Transactional
    public void delete(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的出版品"));
        
        // 權限檢查
        if (!hasPermission(publication.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的出版品");
        }
        
        publicationRepository.delete(publication);
    }
    
    /**
     * 根據 ID 獲取出版品
     */
    public PublicationResponse getById(Long id) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的出版品"));
        return new PublicationResponse(publication);
    }
    
    /**
     * 獲取所有出版品（根據用戶權限過濾，按排序排序）
     */
    public List<PublicationResponse> getAll() {
        Role currentRole = getCurrentUserRole();
        List<Publication> publicationList;
        
        if (currentRole == Role.SUPER_ADMIN) {
            // 超級管理員可以看到所有出版品（按排序和建立時間排序）
            publicationList = publicationRepository.findAll().stream()
                    .sorted((p1, p2) -> {
                        int sortCompare = Integer.compare(
                            p1.getSortOrder() != null ? p1.getSortOrder() : 0,
                            p2.getSortOrder() != null ? p2.getSortOrder() : 0
                        );
                        if (sortCompare != 0) {
                            return sortCompare;
                        }
                        if (p1.getCreatedAt() != null && p2.getCreatedAt() != null) {
                            return p2.getCreatedAt().compareTo(p1.getCreatedAt());
                        }
                        return 0;
                    })
                    .collect(Collectors.toList());
        } else if (currentRole != null) {
            // 其他用戶只能看到自己中心的出版品（已按排序排序）
            publicationList = publicationRepository.findByCenterRole(currentRole);
        } else {
            publicationList = List.of();
        }
        
        return publicationList.stream()
                .map(PublicationResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 根據中心角色獲取出版品（公開 API，用於前端顯示）
     */
    public List<PublicationResponse> getByCenterRole(Role centerRole) {
        List<Publication> publicationList = publicationRepository.findByCenterRole(centerRole);
        
        return publicationList.stream()
                .map(PublicationResponse::new)
                .collect(Collectors.toList());
    }
}

