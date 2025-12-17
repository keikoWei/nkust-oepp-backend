package com.nkust.oepp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    @Value("${file.upload.dir:upload}")
    private String uploadDir;
    
    /**
     * 儲存檔案到本地
     * 
     * @param file 檔案
     * @param subfolder 子資料夾（例如 "news"）
     * @return 檔案相對路徑（相對於 upload 資料夾）
     * @throws IOException 儲存失敗時拋出異常
     */
    public String storeFile(MultipartFile file, String subfolder) throws IOException {
        // 驗證檔案類型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("檔案名稱不能為空");
        }
        
        String extension = getFileExtension(originalFilename);
        if (!isAllowedFileType(extension)) {
            throw new IOException("不支援的檔案類型: " + extension);
        }
        
        // 建立上傳目錄
        Path uploadPath = Paths.get(uploadDir, subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // 生成唯一檔案名稱
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);
        
        // 儲存檔案
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // 返回相對路徑（用於資料庫儲存）
        return subfolder + "/" + fileName;
    }
    
    /**
     * 刪除檔案
     * 
     * @param filePath 檔案相對路徑
     * @throws IOException 刪除失敗時拋出異常
     */
    public void deleteFile(String filePath) throws IOException {
        Path fullPath = Paths.get(uploadDir, filePath);
        if (Files.exists(fullPath)) {
            Files.delete(fullPath);
        }
    }
    
    /**
     * 獲取檔案擴展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    /**
     * 檢查是否為允許的檔案類型
     */
    private boolean isAllowedFileType(String extension) {
        return extension.equals("pdf") || 
               extension.equals("doc") || 
               extension.equals("docx") || 
               extension.equals("xls") || 
               extension.equals("xlsx");
    }
    
    /**
     * 獲取檔案的完整路徑（用於下載）
     */
    public Path getFilePath(String relativePath) {
        return Paths.get(uploadDir, relativePath);
    }
}

