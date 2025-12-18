package com.nkust.oepp.controller;

import com.nkust.oepp.dto.TrainingPlanRequest;
import com.nkust.oepp.dto.TrainingPlanResponse;
import com.nkust.oepp.dto.TrainingPlanUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.service.TrainingPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {
    
    @Autowired
    private TrainingPlanService trainingPlanService;
    
    /**
     * 創建委訓計畫
     */
    @PostMapping
    public ResponseEntity<TrainingPlanResponse> create(@Valid @RequestBody TrainingPlanRequest request) {
        try {
            TrainingPlanResponse response = trainingPlanService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 更新委訓計畫
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingPlanResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrainingPlanUpdateRequest request) {
        try {
            TrainingPlanResponse response = trainingPlanService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 刪除委訓計畫
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            trainingPlanService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "委訓計畫刪除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 根據 ID 獲取委訓計畫
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingPlanResponse> getById(@PathVariable Long id) {
        try {
            TrainingPlanResponse response = trainingPlanService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 獲取所有委訓計畫（根據用戶權限過濾）
     */
    @GetMapping
    public ResponseEntity<List<TrainingPlanResponse>> getAll() {
        List<TrainingPlanResponse> responses = trainingPlanService.getAll();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 根據中心角色獲取委訓計畫（公開 API，用於前端顯示）
     * 不需要認證
     */
    @GetMapping("/public/{centerRole}")
    public ResponseEntity<List<TrainingPlanResponse>> getByCenterRole(@PathVariable String centerRole) {
        try {
            Role role = Role.fromCode(centerRole.toUpperCase());
            List<TrainingPlanResponse> responses = trainingPlanService.getByCenterRole(role);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("無效的中心角色: " + centerRole, e);
        }
    }
}

