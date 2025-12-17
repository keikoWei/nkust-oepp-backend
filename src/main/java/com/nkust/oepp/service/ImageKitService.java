package com.nkust.oepp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ImageKitService {
    
    private static final String IMAGEKIT_UPLOAD_URL = "https://upload.imagekit.io/api/v1/files/upload";
    
    @Value("${imagekit.public.key}")
    private String publicKey;
    
    @Value("${imagekit.private.key}")
    private String privateKey;
    
    @Value("${imagekit.url.endpoint}")
    private String urlEndpoint;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 生成 Basic Authentication Header
     * Authorization: Basic Base64(privateKey:)
     */
    private String generateAuthHeader() {
        // ImageKit 服務器端上傳使用 Basic Auth，格式為 privateKey:
        String auth = privateKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 上傳圖片到 ImageKit
     * 
     * @param file 圖片文件
     * @param fileName 檔案名稱（可選，如果為 null 則使用原始檔名）
     * @param folder 資料夾路徑（可選）
     * @return 圖片 URL
     * @throws IOException 上傳失敗時拋出異常
     */
    public String uploadImage(MultipartFile file, String fileName, String folder) throws IOException {
        try {
            // 建立請求頭（使用 Basic Authentication）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", generateAuthHeader());
            
            // 建立請求體
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            
            // 確定最終的檔案名稱（必須提供）
            String finalFileName = (fileName != null && !fileName.isEmpty()) 
                    ? fileName 
                    : file.getOriginalFilename();
            
            if (finalFileName == null || finalFileName.isEmpty()) {
                throw new IOException("無法確定檔案名稱");
            }
            
            // 添加文件
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return finalFileName;
                }
            };
            body.add("file", fileResource);
            
            // 添加檔案名稱（ImageKit 必需參數）
            body.add("fileName", finalFileName);
            
            // 添加資料夾路徑
            if (folder != null && !folder.isEmpty()) {
                body.add("folder", folder);
            }
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            // 執行請求
            ResponseEntity<String> response = restTemplate.exchange(
                IMAGEKIT_UPLOAD_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 解析回應
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                if (jsonNode.has("url")) {
                    return jsonNode.get("url").asText();
                } else if (jsonNode.has("message")) {
                    throw new IOException("ImageKit 上傳失敗: " + jsonNode.get("message").asText());
                } else {
                    throw new IOException("ImageKit 上傳失敗: 無法解析回應 - " + response.getBody());
                }
            } else {
                throw new IOException("ImageKit 上傳失敗: HTTP " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("ImageKit 上傳失敗: " + e.getMessage(), e);
        }
    }
}
