package com.nkust.oepp.service;

import com.nkust.oepp.dto.TrainingPlanRequest;
import com.nkust.oepp.dto.TrainingPlanResponse;
import com.nkust.oepp.dto.TrainingPlanUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.entity.TrainingPlan;
import com.nkust.oepp.repository.TrainingPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingPlanService {
    
    @Autowired
    private TrainingPlanRepository trainingPlanRepository;
    
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
     * 創建委訓計畫
     */
    @Transactional
    public TrainingPlanResponse create(TrainingPlanRequest request) {
        // 權限檢查
        if (!hasPermission(request.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的委訓計畫");
        }
        
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setCenterRole(request.getCenterRole());
        trainingPlan.setTitle(request.getTitle());
        trainingPlan.setLink(request.getLink());
        trainingPlan.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        
        trainingPlan = trainingPlanRepository.save(trainingPlan);
        
        return new TrainingPlanResponse(trainingPlan);
    }
    
    /**
     * 更新委訓計畫
     */
    @Transactional
    public TrainingPlanResponse update(Long id, TrainingPlanUpdateRequest request) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的委訓計畫"));
        
        // 權限檢查
        if (!hasPermission(trainingPlan.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的委訓計畫");
        }
        
        // 更新基本資訊
        if (request.getTitle() != null) {
            trainingPlan.setTitle(request.getTitle());
        }
        if (request.getLink() != null) {
            trainingPlan.setLink(request.getLink());
        }
        if (request.getSortOrder() != null) {
            trainingPlan.setSortOrder(request.getSortOrder());
        }
        
        trainingPlan = trainingPlanRepository.save(trainingPlan);
        
        return new TrainingPlanResponse(trainingPlan);
    }
    
    /**
     * 刪除委訓計畫
     */
    @Transactional
    public void delete(Long id) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的委訓計畫"));
        
        // 權限檢查
        if (!hasPermission(trainingPlan.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的委訓計畫");
        }
        
        trainingPlanRepository.delete(trainingPlan);
    }
    
    /**
     * 根據 ID 獲取委訓計畫
     */
    public TrainingPlanResponse getById(Long id) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的委訓計畫"));
        return new TrainingPlanResponse(trainingPlan);
    }
    
    /**
     * 獲取所有委訓計畫（根據用戶權限過濾，按排序排序）
     */
    public List<TrainingPlanResponse> getAll() {
        Role currentRole = getCurrentUserRole();
        List<TrainingPlan> trainingPlanList;
        
        if (currentRole == Role.SUPER_ADMIN) {
            // 超級管理員可以看到所有委訓計畫（按排序和建立時間排序）
            trainingPlanList = trainingPlanRepository.findAll().stream()
                    .sorted((t1, t2) -> {
                        int sortCompare = Integer.compare(
                            t1.getSortOrder() != null ? t1.getSortOrder() : 0,
                            t2.getSortOrder() != null ? t2.getSortOrder() : 0
                        );
                        if (sortCompare != 0) {
                            return sortCompare;
                        }
                        if (t1.getCreatedAt() != null && t2.getCreatedAt() != null) {
                            return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                        }
                        return 0;
                    })
                    .collect(Collectors.toList());
        } else if (currentRole != null) {
            // 其他用戶只能看到自己中心的委訓計畫（已按排序排序）
            trainingPlanList = trainingPlanRepository.findByCenterRole(currentRole);
        } else {
            trainingPlanList = List.of();
        }
        
        return trainingPlanList.stream()
                .map(TrainingPlanResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 根據中心角色獲取委訓計畫（公開 API，用於前端顯示）
     */
    public List<TrainingPlanResponse> getByCenterRole(Role centerRole) {
        List<TrainingPlan> trainingPlanList = trainingPlanRepository.findByCenterRole(centerRole);
        
        return trainingPlanList.stream()
                .map(TrainingPlanResponse::new)
                .collect(Collectors.toList());
    }
}

