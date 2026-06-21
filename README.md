# 🏆 健康追蹤遊戲化系統 (Health Tracker Gamified System)
> **軟體框架設計期末專題 — 極簡黑夜雙欄儀表板版**

這是一個基於 **Java 23** 與 **Spring Boot** 開發的家庭健康追蹤遊戲化系統。系統旨在透過豐富的遊戲化機制（積分系統、身心狀態加成、連續運動連擊獎勵、久坐扣分、成就解鎖與家庭旅遊基金目標），鼓勵家庭成員共同紀錄日常運動，促進全家人的身心健康。

---

## 🚀 快速啟動說明

### 系統環境要求
- **Java JDK 23** 或以上
- **瀏覽器**（建議使用 Chrome / Edge / Safari）

### 執行步驟
1. 進入專案目錄 `finalproj`。
2. 啟動後端伺服器：
   執行主程式 `HealthTrackerApplication.java`。
3. 啟動成功後，打開瀏覽器前往：[http://localhost:8080](http://localhost:8080)。

---

## 🎨 系統核心功能與版面配置

本系統採用**左右雙欄式自適應儀表板**排版：

1. **📊 左欄：數據與目標看板**
   * **🎯 全家旅遊基金解鎖進度**：顯示 500 點家庭總積分目標的進度條，當全家運動積分達到 500 點即可成功解鎖。
   * **👥 成員狀態與個人指標小卡片**：
     * 展示成員當前的身心狀態（正常、疲勞、活力充沛、怠惰中）與點數加成倍率。
     * **🏃 個人總運動時間**（分鐘）及 **🔥 當前連擊天數**。
     * **🎯 每週目標進度條**：每週運動目標為 150 分鐘，以綠色漸層進度條呈現達標率。
     * **🏆 解鎖成就徽章**：當成員達成特定條件時，顯示精美的橘金色立體成就徽章。
   * **📊 全家運動項目累計**：以膠囊標籤動態統計全家各類運動的累計次數（例如：`跑步: 3次`）。

2. **📝 右欄：運動紀錄登錄表單**
   * 選擇成員、運動種類與時間並送出。送出後會實時計算該次獲得的積分，並自動更新成員狀態，且回傳包含雙人同行或連擊加成的動態訊息。

3. **⚙️ 設定與規則說明（右上方按鈕）**
   * **📜 遊戲化規則說明 (Modal)**：點擊開啟規則彈出視窗，詳細說明身心狀態轉移條件、成就解鎖標準、積分公式、連擊加分、久坐扣分以及雙人同行與補登規則。
   * **⚙️ 系統設定 (Modal)**：動態管理（新增/刪除）家庭成員及自訂運動項目與權重（例如：跑步權重 x3，快走 x1）。

---

## 🏗️ 實作的設計模式 (Design Patterns)

本系統深度結合了物件導向設計原則，實作了以下 5 種經典設計模式：

### 1. 策略模式 (Strategy Pattern)
*   **類別對象**：[`PointStrategy`](finalproj/src/main/java/org/example/PointStrategy.java) 介面
*   **應用場景**：用於動態計算運動積分。不同的情境適用不同的積分策略（如 `StandardPointStrategy`、`HolidayDoublePointStrategy`、`LateLoggingPenaltyStrategy`）。

### 2. 裝飾者模式 (Decorator Pattern)
*   **類別對象**：[`CooperativePointStrategy`](finalproj/src/main/java/org/example/CooperativePointStrategy.java) 與各類 Penalty/Double Strategy。
*   **應用場景**：這兩個策略各自包裹了另一個基礎策略，在其計算結果之上疊加特殊規則：
  *   `HolidayDoublePointStrategy`：使點數雙倍。
  *   `LateLoggingPenaltyStrategy`：補登錄延遲折數（1-3天打8折，超過3天0分）。
  *   `CooperativePointStrategy`：同日若有 2 人（含）以上運動，當天所有人的運動紀錄皆享有 **1.2 倍** 加成。
  *   *雙人同行溯及既往機制*：後登錄的成員在觸發雙人同行時，系統會自動搜尋今日稍早運動的其他家人，並為他們補發 20% 的協力積分。

### 3. 狀態模式 (State Pattern)
*   **類別對象**：[`MemberState`](finalproj/src/main/java/org/example/MemberState.java) 介面與其四個實作類別：
  *   `NormalState` (正常狀態，積分倍率 x1.0)
  *   `FatiguedState` (疲勞狀態，因單次運動超過 60 分鐘觸發，積分倍率 x0.5)
  *   `EnergeticState` (活力充沛狀態，因累計運動次數達到 3 次以上觸發，積分倍率 x1.5)
  *   `SlackingState` (怠惰中狀態，因連續 3 天未在系統登錄運動觸發，積分倍率 x0.5；重新登錄運動後立刻恢復至 `NormalState`)
*   **應用場景**：成員（`Member`）的狀態會隨運動紀錄動態改變，且狀態會影響後續運動紀錄的點數加成倍率。

### 4. 觀察者模式 (Observer Pattern)
*   **類別對象**：[`ExerciseObserver`](finalproj/src/main/java/org/example/ExerciseObserver.java) 介面與其訂閱者：
  *   `EmailNotificationObserver`：模擬發送郵件通知。
  *   `AchievementObserver`：檢查並動態解鎖「初試身手」、「鋼鐵超人」、「健康達人」等成就，並將解鎖成就回寫入 `Member` 領域物件中以在前端顯示。
*   **應用場景**：當 [`ExerciseScoringService`](finalproj/src/main/java/org/example/ExerciseScoringService.java) 完成運動登錄與結算後，會自動廣播給所有註冊的觀察者，執行對應的副效應。

### 5. 工廠模式 (Factory Pattern)
*   **類別對象**：[`PointStrategyFactory`](finalproj/src/main/java/org/example/PointStrategyFactory.java) 與 [`ExerciseRecordFactory`](finalproj/src/main/java/org/example/ExerciseRecordFactory.java)
*   **應用場景**：封裝運動紀錄與計分策略的建立邏輯，簡化控制器（Controller）的實例化複雜度。

---

## 📊 遊戲化核心規則摘要

1.  **連擊獎勵 (Streak Bonus)**：若連續第 2 天（含）以上運動，當次運動額外獲得連擊獎勵分：`(連擊天數 - 1) * 10` 點，上限為 **+50 點**。連擊中斷則天數歸零。
2.  **怠惰流失扣分 (Decay Penalty)**：若連續 4 天（含）以上未在系統登錄運動，每天扣除個人積分 5 點（扣至 0 為止）。
3.  **雙人同行協力**：當天有 2 位（含）以上成員運動，全體皆享有 **1.2 倍** 加成。先運動者由後登錄者溯及既往補發點數。
