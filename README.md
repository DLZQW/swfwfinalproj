# 🏆 健康追蹤遊戲化系統 (Health Tracker Gamified System)
> **軟體框架設計期末專體 — 極簡黑夜版**

這是一個基於 **Java 23** 與 **Spring Boot** 開發的家庭健康追蹤遊戲化系統。系統旨在透過遊戲化的機制（積分系統、狀態加成、成就系統），鼓勵家庭成員共同紀錄日常運動，促進全家人的身心健康。

---

## 🚀 快速啟動說明

### 系統環境要求
- **Java JDK 23** 或以上
- **Maven 3.8+**
- 瀏覽器（建議使用 Chrome / Edge / Safari）

### 執行步驟
1. 進入專案目錄 `finalproj`。
2. 在終端機執行 Maven 啟動指令：
   ```bash
   mvn spring-boot:run
   ```
   或者直接在 IDE (如 IntelliJ IDEA / VS Code) 中執行主程式 `HealthTrackerApplication.java`。
3. 啟動成功後，打開瀏覽器前往：[http://localhost:8080](http://localhost:8080)。

---

## 🎨 系統核心功能

1. **🏆 家庭積分儀表板**
   - 顯示家庭總積分。
   - 顯示家庭成員列表，包含個人積分、當前身心狀態（正常、疲勞、活力充沛）與點數倍率。

2. **📝 運動紀錄登錄**
   - 選擇成員、運動項目並輸入運動時間，送出後系統會動態計算該次獲得的積分，並依據運動情況自動轉移成員狀態。

3. **⚙️ 系統設定（右上角齒輪圖示）**
   - **👥 家庭成員管理**：可動態新增或刪除家庭成員。
   - **🏋️ 運動項目管理**：可自訂運動項目名稱與計分權重（例如：跑步權重 x3，散步權重 x1），並支援動態新增與刪除。

---

## 🏗️ 實作的設計模式 (Design Patterns)

本系統深度結合了物件導向設計原則，實作了以下 5 種經典設計模式：

### 1. 策略模式 (Strategy Pattern)
- **類別對象**：[`PointStrategy`](finalproj/src/main/java/org/example/PointStrategy.java) 介面
- **應用場景**：用於動態計算運動積分。不同的情境適用不同的積分策略：
  - `StandardPointStrategy`：基礎計分策略（時間 × 運動項目權重）。
  - `HolidayDoublePointStrategy`：節假日點數加倍策略。
  - `LateLoggingPenaltyStrategy`：補登錄延遲懲罰策略。

### 2. 裝飾者模式 (Decorator Pattern)
- **類別對象**：[`HolidayDoublePointStrategy`](finalproj/src/main/java/org/example/HolidayDoublePointStrategy.java) 與 [`LateLoggingPenaltyStrategy`](finalproj/src/main/java/org/example/LateLoggingPenaltyStrategy.java)
- **應用場景**：這兩個策略各自包裹了另一個基礎策略（例如 `StandardPointStrategy`），在其計算結果之上疊加特殊規則（例如：乘二倍或打折），體現了對修改關閉、對擴充開放的 OCP 原則。

### 3. 狀態模式 (State Pattern)
- **類別對象**：[`MemberState`](finalproj/src/main/java/org/example/MemberState.java) 介面與其三個實作類別：
  - `NormalState` (正常狀態，積分倍率 x1.0)
  - `FatiguedState` (疲勞狀態，因單次運動超過 60 分鐘觸發，積分倍率 x0.8)
  - `EnergeticState` (活力充沛狀態，因累計運動次數達到 3 次以上觸發，積分倍率 x1.5)
- **應用場景**：成員（`Member`）的狀態會隨運動紀錄動態改變，且狀態會影響後續運動紀錄的點數加成倍率。

### 4. 觀察者模式 (Observer Pattern)
- **類別對象**：[`ExerciseObserver`](finalproj/src/main/java/org/example/ExerciseObserver.java) 介面與其訂閱者：
  - `EmailNotificationObserver`：模擬發送郵件通知。
  - `AchievementObserver`：檢查並動態解鎖「初試身手」、「鋼鐵超人」、「健康達人」等遊戲成就。
- **應用場景**：當 [`ExerciseScoringService`](finalproj/src/main/java/org/example/ExerciseScoringService.java) 完成運動登錄與結算後，會自動廣播給所有註冊的觀察者，執行對應的副效應。

### 5. 工廠模式 (Factory Pattern)
- **類別對象**：[`PointStrategyFactory`](finalproj/src/main/java/org/example/PointStrategyFactory.java) 與 [`ExerciseRecordFactory`](finalproj/src/main/java/org/example/ExerciseRecordFactory.java)
- **應用場景**：封裝運動紀錄與計分策略的建立邏輯，簡化控制器（Controller）的實例化複雜度。

---

## 🛠️ 技術棧 (Tech Stack)

- **後端 (Backend)**: Java 23 / Spring Boot 3.2.4 (Starter Web)
- **前端 (Frontend)**: Vanilla HTML5 / CSS3 (CSS 變數、極簡暗黑質感、動畫與 Glassmorphism) / Vanilla JavaScript (Fetch API 異步通訊)
- **版本控制**: Git
