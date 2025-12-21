# NKUST OEPP Backend

NKUST 線上教育平台後端系統

## 技術棧

- **Java**: 21
- **Spring Boot**: 3.4.2
- **MySQL**: 8.0.30
- **Maven**: 專案管理工具
- **Spring Data JPA**: 資料持久層
- **Spring Security**: 安全認證與授權
- **JWT (JSON Web Token)**: 無狀態身份驗證

## 專案結構

```
nkust-oepp-backend/
├── src/
│   ├── main/
│   │   ├── java/com/nkust/oepp/
│   │   │   ├── config/          # 配置類
│   │   │   ├── controller/      # 控制器
│   │   │   ├── service/          # 服務層
│   │   │   ├── repository/      # 資料存取層
│   │   │   ├── entity/          # 實體類
│   │   │   ├── dto/             # 資料傳輸物件
│   │   │   ├── filter/          # 過濾器
│   │   │   ├── util/            # 工具類
│   │   │   └── OeppApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
└── pom.xml
```

## 環境要求

- JDK 21 或更高版本
- Maven 3.6+ 
- MySQL 8.0.30 或更高版本

## API 文檔

- **前端串接文檔**: 請參考 [FRONTEND_API_DOCUMENTATION.md](./FRONTEND_API_DOCUMENTATION.md)
- **Postman Collection**: 請參考 [NKUST_OEPP_Backend.postman_collection.json](./NKUST_OEPP_Backend.postman_collection.json)
- **Postman 測試指南**: 請參考 [POSTMAN_TEST_GUIDE.md](./POSTMAN_TEST_GUIDE.md)

## 維護模式功能

系統提供維護模式功能，允許管理員透過後台控制系統的維護狀態。

### 功能特點

- ✅ 透過 API 開關維護模式
- ✅ 維護模式下自動攔截非允許的請求
- ✅ 維護模式下仍可登入和管理維護模式
- ✅ 公開 API 端點不受維護模式影響

### 快速開始

1. **取得維護模式狀態**:
   ```bash
   GET /api/system-config/maintenance-mode
   ```

2. **開啟維護模式**:
   ```bash
   PUT /api/system-config/maintenance-mode?enabled=true
   Authorization: Bearer <your-jwt-token>
   ```

3. **切換維護模式**:
   ```bash
   POST /api/system-config/maintenance-mode/toggle
   Authorization: Bearer <your-jwt-token>
   ```

詳細 API 文檔請參考 [FRONTEND_API_DOCUMENTATION.md](./FRONTEND_API_DOCUMENTATION.md)