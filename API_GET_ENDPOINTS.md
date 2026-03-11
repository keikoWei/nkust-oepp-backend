# 取得資料 API 文件（前端串接用）

本文檔列出後端**取得資料**相關的 GET API，供前端串接使用。

---

## 基礎資訊

| 項目 | 說明 |
|------|------|
| **Base URL** | `https://edu.nkust.edu.tw` 或 `http://localhost:8080`（開發） |
| **API 前綴** | `/api` |
| **認證** | 需認證的 API 請在 Header 帶入：`Authorization: Bearer <JWT>` |
| **Content-Type** | `application/json` |

### 中心角色代碼 (centerRole)

公開 API 路徑中的 `{centerRole}` 請使用以下**大寫**代碼：

| 代碼 | 名稱 |
|------|------|
| `HEADQUARTERS` | 處本部 |
| `EDUCATION_CENTER` | 教育推廣中心 |
| `PRODUCT_CENTER` | 產品推廣中心 |
| `EXHIBITION_CENTER` | 會展管理中心 |
| `MANAGEMENT_CENTER` | 經營管理中心 |

---

## 一、系統設定

### 1.1 取得維護模式狀態

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/system-config/maintenance-mode` |
| **認證** | 不需要 |

**回應範例 (200)**  
```json
{
  "maintenanceMode": false,
  "message": "維護模式已關閉"
}
```

---

## 二、消息 (News)

### 2.1 依 ID 取得單筆消息

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/news/{id}` |
| **認證** | 不需要 |

**路徑參數**

| 參數 | 類型 | 說明 |
|------|------|------|
| id | number | 消息 ID |

**回應範例 (200)**  
```json
{
  "id": 1,
  "centerRole": "EDUCATION_CENTER",
  "centerRoleName": "教育推廣中心",
  "title": "消息標題",
  "contentHtml": "<p>HTML 內容</p>",
  "imageUrls": ["https://..."],
  "filePaths": ["upload/xxx.pdf"],
  "sortOrder": 0,
  "isEnabled": true,
  "publishTime": "2024-01-01T00:00:00",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

---

### 2.2 取得所有消息（依登入者權限）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/news` |
| **認證** | 需要 |

**回應**  
`200`：`NewsResponse[]` 陣列（結構同 2.1）

---

### 2.3 依中心取得消息（公開）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/news/public/{centerRole}` |
| **認證** | 不需要 |

**路徑參數**

| 參數 | 類型 | 說明 |
|------|------|------|
| centerRole | string | 中心代碼（大寫），見上方「中心角色代碼」 |

**Query 參數**

| 參數 | 類型 | 必填 | 預設 | 說明 |
|------|------|------|------|------|
| onlyEnabled | boolean | 否 | true | 是否只回傳已啟用且已發布 |

**回應**  
`200`：`NewsResponse[]` 陣列

---

### 2.4 取得消息檔案（下載）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/news/file?path={path}`（建議）或 `/api/news/file/{filePath}` |
| **認證** | 不需要 |

**參數怎麼帶（建議：用 query，避免 404）**  
- 使用 **Query 參數** `path`：從單筆消息的 `filePaths` 取一個字串（例如 `news/abc.pdf`），做 **`encodeURIComponent`** 後帶入。
- 下載網址：`GET ${baseUrl}/api/news/file?path=${encodeURIComponent(filePath)}`

**前端範例**  
```javascript
// 從單筆消息取第一個檔案下載（建議寫法，不會 404）
const filePath = newsResponse.filePaths[0];  // 例如 "news/abc.pdf"
const url = `${baseUrl}/api/news/file?path=${encodeURIComponent(filePath)}`;
window.open(url, '_blank');  // 或 <a href={url} download>
```

**備選：路徑寫法**  
- 也可用 `GET /api/news/file/news/abc.pdf`（路徑接在 `/file/` 後），若遇 404 請改以上 query 寫法。

**說明**  
回應為檔案串流，依副檔名決定 Content-Type（PDF 為 `application/pdf`）。

---

## 三、課程 (Courses)

### 3.1 依 ID 取得單筆課程

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/courses/{id}` |
| **認證** | 需要 |

**路徑參數**  
| id | number | 課程 ID |

**回應範例 (200)**  
```json
{
  "id": 1,
  "centerRole": "EDUCATION_CENTER",
  "centerRoleName": "教育推廣中心",
  "title": "課程標題",
  "contentHtml": "<p>HTML 內容</p>",
  "mainImageUrl": "https://...",
  "imageUrls": ["https://..."],
  "filePaths": ["upload/xxx.pdf"],
  "sortOrder": 0,
  "isEnabled": true,
  "publishTime": "2024-01-01T00:00:00",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

---

### 3.2 取得所有課程（依登入者權限）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/courses` |
| **認證** | 需要 |

**回應**  
`200`：`CourseResponse[]` 陣列

---

### 3.3 依中心取得課程（公開）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/courses/public/{centerRole}` |
| **認證** | 不需要 |

**Query 參數**

| 參數 | 類型 | 必填 | 預設 | 說明 |
|------|------|------|------|------|
| onlyEnabled | boolean | 否 | true | 是否只回傳已啟用且已發布 |

**回應**  
`200`：`CourseResponse[]` 陣列

---

### 3.4 取得課程檔案（下載）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/courses/file?path={path}`（建議）或 `/api/courses/file/{filePath}` |
| **認證** | 不需要 |

**參數怎麼帶**  
同 **2.4 取得消息檔案**：建議用 query，避免 404。  
`GET ${baseUrl}/api/courses/file?path=${encodeURIComponent(filePath)}`（`filePath` 從課程回應的 `filePaths` 取，如 `courses/xxx.pdf`）。

---

## 四、出版品 (Publications)

### 4.1 依 ID 取得單筆出版品

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/publications/{id}` |
| **認證** | 需要 |

**回應範例 (200)**  
```json
{
  "id": 1,
  "centerRole": "EDUCATION_CENTER",
  "centerRoleName": "教育推廣中心",
  "title": "出版品標題",
  "mainImageUrl": "https://...",
  "iframeLink": "https://...",
  "sortOrder": 0,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

### 4.2 取得所有出版品（依登入者權限）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/publications` |
| **認證** | 需要 |

**回應**  
`200`：`PublicationResponse[]` 陣列

---

### 4.3 依中心取得出版品（公開）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/publications/public/{centerRole}` |
| **認證** | 不需要 |

**回應**  
`200`：`PublicationResponse[]` 陣列

---

## 五、委訓計畫 (Training Plans)

### 5.1 依 ID 取得單筆委訓計畫

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/training-plans/{id}` |
| **認證** | 需要 |

**回應範例 (200)**  
```json
{
  "id": 1,
  "centerRole": "EDUCATION_CENTER",
  "centerRoleName": "教育推廣中心",
  "title": "委訓計畫標題",
  "link": "https://...",
  "sortOrder": 0,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

### 5.2 取得所有委訓計畫（依登入者權限）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/training-plans` |
| **認證** | 需要 |

**回應**  
`200`：`TrainingPlanResponse[]` 陣列

---

### 5.3 依中心取得委訓計畫（公開）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/training-plans/public/{centerRole}` |
| **認證** | 不需要 |

**回應**  
`200`：`TrainingPlanResponse[]` 陣列

---

## 六、輪播圖 (Carousel)

### 6.1 依 ID 取得單筆輪播圖

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/carousel/{id}` |
| **認證** | 需要 |

**回應範例 (200)**  
```json
{
  "id": 1,
  "title": "輪播標題",
  "centerRole": "EDUCATION_CENTER",
  "centerRoleName": "教育推廣中心",
  "imageUrl": "https://...",
  "clickUrl": "https://...",
  "sortOrder": 0,
  "isEnabled": true,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

---

### 6.2 取得所有輪播圖（依登入者權限）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/carousel` |
| **認證** | 需要 |

**回應**  
`200`：`CarouselImageResponse[]` 陣列

---

### 6.3 依中心取得輪播圖（公開）

| 項目 | 說明 |
|------|------|
| **方法** | `GET` |
| **路徑** | `/api/carousel/public/{centerRole}` |
| **認證** | 不需要 |

**Query 參數**

| 參數 | 類型 | 必填 | 預設 | 說明 |
|------|------|------|------|------|
| onlyEnabled | boolean | 否 | true | 是否只回傳已啟用 |

**回應**  
`200`：`CarouselImageResponse[]` 陣列

---

## 七、各中心後台（Dashboard）

以下為後台用、需認證的取得資料 API，依中心回傳歡迎訊息與當前使用者資訊。

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/headquarters/dashboard` | 處本部後台 |
| GET | `/api/education/dashboard` | 教育推廣中心後台 |
| GET | `/api/product/dashboard` | 產品推廣中心後台 |
| GET | `/api/exhibition/dashboard` | 會展管理中心後台 |
| GET | `/api/management/dashboard` | 經營管理中心後台 |

**回應範例 (200)**  
```json
{
  "center": "教育推廣中心",
  "message": "歡迎使用教育推廣中心後台管理系統",
  "user": "education",
  "role": "教育推廣中心"
}
```

---

## 依中心／功能對照的 API

以下依「教育推廣中心」「會展中心」常用功能整理對應的取得資料 API，方便前端依頁面串接。

---

### 教育推廣中心：熱門課程、委訓計畫

| 功能 | 方法 | 路徑 | 認證 | 說明 |
|------|------|------|------|------|
| 熱門課程列表 | GET | `/api/courses/public/EDUCATION_CENTER` | 不需要 | 取得教育推廣中心已發布且啟用的課程，可作熱門課程區塊 |
| 單筆課程詳情 | GET | `/api/courses/{id}` | 需要 | 依課程 ID 取得單筆（後台用）；前台可改用單筆消息／課程等公開資料 |
| 課程檔案下載 | GET | `/api/courses/file?path={path}` | 不需要 | 下載課程附檔，`path` 從課程回應的 `filePaths` 取並 `encodeURIComponent` |
| 委訓計畫列表 | GET | `/api/training-plans/public/EDUCATION_CENTER` | 不需要 | 取得教育推廣中心的委訓計畫列表 |
| 單筆委訓計畫 | GET | `/api/training-plans/{id}` | 需要 | 依 ID 取得單筆委訓計畫（後台用） |

**熱門課程列表 Query 參數（選用）**

| 參數 | 類型 | 預設 | 說明 |
|------|------|------|------|
| onlyEnabled | boolean | true | 是否只回傳已啟用且已發布 |

**前端範例**

```javascript
// 熱門課程（教育推廣中心）
const coursesRes = await fetch(`${baseUrl}/api/courses/public/EDUCATION_CENTER?onlyEnabled=true`);
const courses = await coursesRes.json();

// 委訓計畫（教育推廣中心）
const plansRes = await fetch(`${baseUrl}/api/training-plans/public/EDUCATION_CENTER`);
const trainingPlans = await plansRes.json();
```

**課程回應欄位**（見 § 三、課程）：`id`, `centerRole`, `centerRoleName`, `title`, `contentHtml`, `mainImageUrl`, `imageUrls`, `filePaths`, `sortOrder`, `isEnabled`, `publishTime`, `createdAt`, `updatedAt`。

**委訓計畫回應欄位**（見 § 五、委訓計畫）：`id`, `centerRole`, `centerRoleName`, `title`, `link`, `sortOrder`, `createdAt`。

---

### 會展中心：出版品管理

| 功能 | 方法 | 路徑 | 認證 | 說明 |
|------|------|------|------|------|
| 出版品列表 | GET | `/api/publications/public/EXHIBITION_CENTER` | 不需要 | 取得會展管理中心的出版品列表，供出版品管理／展示區塊使用 |
| 單筆出版品 | GET | `/api/publications/{id}` | 需要 | 依 ID 取得單筆出版品（後台用） |

**前端範例**

```javascript
// 會展中心出版品列表
const pubRes = await fetch(`${baseUrl}/api/publications/public/EXHIBITION_CENTER`);
const publications = await pubRes.json();
```

**出版品回應欄位**（見 § 四、出版品）：`id`, `centerRole`, `centerRoleName`, `title`, `mainImageUrl`, `iframeLink`, `sortOrder`, `createdAt`。

---

## 錯誤碼

| 狀態碼 | 說明 |
|--------|------|
| 200 | 成功 |
| 401 | 未登入或 Token 無效 |
| 403 | 無權限 |
| 404 | 資源不存在 |
| 500 | 伺服器錯誤 |

---

## 前端串接注意事項

1. **公開 API**：路徑含 `/public/` 或 `/file/`、`/api/system-config/maintenance-mode` 不需帶 Token。
2. **需認證 API**：請先呼叫 `POST /api/auth/login` 取得 JWT，再在 Header 加上 `Authorization: Bearer <token>`。
3. **centerRole**：呼叫公開 API 時，`centerRole` 請使用大寫，如 `EDUCATION_CENTER`。
4. **日期時間**：回應中的日期時間為 ISO 8601 格式（如 `2024-01-01T00:00:00`）。
