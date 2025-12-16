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