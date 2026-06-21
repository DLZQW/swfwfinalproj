package org.example;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允許前端跨域請求 (開發階段使用)
public class HealthTrackerController {

  // 為了展示方便，我們先將狀態保存在記憶體中（未來這部分會替換成關聯式資料庫 MySQL）
  private Family family;
  private ExerciseScoringService scoringService;
  private ExerciseWeightConfig config;

  public HealthTrackerController() {
    family = new Family("F001", "我的健康家庭");
    Member member1 = new Member("M001", "爸爸");
    family.addMember(member1);

    // 2. 💡 關鍵在這裡！
    // 把原本的 ExerciseWeightConfig config = new ExerciseWeightConfig();
    // 改成下面這樣，去掉前面的型態，讓它直接存入全域變數中
    this.config = new ExerciseWeightConfig();

    // 使用工廠模式建立計分策略 (Factory Pattern)
    PointStrategy penaltyStrategy = PointStrategyFactory.createStrategy("LATE_PENALTY", this.config, LocalDate.now());

    scoringService = new ExerciseScoringService();
    scoringService.setStrategy(penaltyStrategy);

    // 註冊觀察者 (Observer Pattern)
    scoringService.addObserver(new EmailNotificationObserver());
    scoringService.addObserver(new AchievementObserver());
  }

  // [API 1] 取得家庭與成員資訊 (對應原本選單的「查看積分」)
  @GetMapping("/family/info")
  public Map<String, Object> getFamilyInfo() {
    // 檢查所有家庭成員的怠惰扣分 (太久未運動)
    for (Member m : family.getMembers()) {
      int penalty = m.checkAndApplyInactivityPenalty();
      if (penalty > 0) {
        System.out.println("❌ [怠惰扣分] " + m.getName() + " 因太久未運動，扣除 " + penalty + " 點積分！");
      }
    }

    Map<String, Object> response = new HashMap<>();
    response.put("familyName", family.getFamilyName());
    response.put("totalPoints", family.getTotalFamilyPoints());
    response.put("members", family.getMembers());
    response.put("familyGoalPoints", family.getFamilyGoalPoints());
    response.put("familyGoalPercent", family.getFamilyGoalPercent());
    response.put("familyWorkoutStats", family.getFamilyWorkoutStats());
    return response;
  }

  // [API 2] 新增運動紀錄
  @PostMapping("/exercise")
  public String recordExercise(@RequestParam("memberId") String memberId,
      @RequestParam("exerciseType") String exerciseType,
      @RequestParam("duration") int duration) {

    Member targetMember = family.getMembers().stream()
        .filter(m -> m.getMemberId().equals(memberId))
        .findFirst()
        .orElse(null);

    if (targetMember == null) {
      return "找不到該成員！";
    }

    // 紀錄舊的分數
    int oldPoints = targetMember.getPersonalPoints();

    // 檢查是否延續連擊，以顯示在成功訊息中
    int initialStreak = targetMember.calculateCurrentStreak();
    boolean willGainStreak = false;
    int gainStreakDay = 0;
    int streakBonusCalculated = 0;
    if (!targetMember.getExerciseRecords().isEmpty()) {
      ExerciseRecord lastRecord = targetMember.getExerciseRecords().get(targetMember.getExerciseRecords().size() - 1);
      long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastRecord.getExerciseDate(), LocalDate.now());
      if (daysBetween == 1) {
        willGainStreak = true;
        gainStreakDay = initialStreak + 1;
        streakBonusCalculated = Math.min(50, (gainStreakDay - 1) * 10);
      }
    }

    // 取得當前的基礎計分策略 (動態取得以確保系統日期正確)
    PointStrategy baseStrategy = PointStrategyFactory.createStrategy("LATE_PENALTY", this.config, LocalDate.now());

    // 檢查是否有其他家人今天也運動了
    boolean partnerExercised = family.getMembers().stream()
        .filter(m -> !m.getMemberId().equals(memberId))
        .anyMatch(m -> m.getExerciseRecords().stream()
            .anyMatch(r -> r.getExerciseDate().equals(LocalDate.now()))
        );

    // 如果今天有其他家人運動，則用裝飾者模式疊加 CooperativePointStrategy
    PointStrategy activeStrategy = partnerExercised ? new CooperativePointStrategy(baseStrategy, true) : baseStrategy;
    scoringService.setStrategy(activeStrategy);

    // 使用工廠模式建立運動紀錄 (Factory Pattern)
    ExerciseRecord record = ExerciseRecordFactory.createRecord(exerciseType, duration);
    scoringService.recordAndScore(targetMember, record);

    // 溯及既往：如果觸發了雙人同行，把今日稍早運動但尚未享有加成的其他成員紀錄也補上 20%
    int retroPointsAdded = 0;
    if (partnerExercised) {
      for (Member otherMember : family.getMembers()) {
        if (!otherMember.getMemberId().equals(memberId)) {
          for (ExerciseRecord otherRecord : otherMember.getExerciseRecords()) {
            if (otherRecord.getExerciseDate().equals(LocalDate.now()) && !otherRecord.isCoopBonusApplied()) {
              int bonusPoints = (int) (otherRecord.getPointsEarned() * 0.2);
              if (bonusPoints > 0) {
                otherMember.addPoints(bonusPoints);
                otherRecord.setPointsEarned(otherRecord.getPointsEarned() + bonusPoints);
                otherRecord.setCoopBonusApplied(true);
                retroPointsAdded += bonusPoints;
                System.out.println("👥 [協力補發] 幫 " + otherMember.getName() + " 的運動紀錄 [" + otherRecord.getExerciseName() + "] 補發 " + bonusPoints + " 點協力積分！");
              } else {
                otherRecord.setCoopBonusApplied(true);
              }
            }
          }
        }
      }
    }

    // 計算剛剛獲得的分數差額
    int earnedPoints = targetMember.getPersonalPoints() - oldPoints;

    // 回傳更詳細的遊戲化資訊
    String responseMessage = String.format("✅ 成功！%s 完成了 %d 分鐘的 %s，獲得了 %d 點積分！",
        targetMember.getName(), duration, exerciseType, earnedPoints);
    if (willGainStreak && streakBonusCalculated > 0) {
      responseMessage += String.format(" 🔥 連擊第 %d 天！獲得額外 +%d 點連擊獎勵！", gainStreakDay, streakBonusCalculated);
    }
    if (partnerExercised) {
      responseMessage += " 👥 觸發雙人同行協力加成，點數獲得額外 1.2 倍加成！";
      if (retroPointsAdded > 0) {
        responseMessage += " 已為今日稍早運動的其他家人自動補發了 " + retroPointsAdded + " 點協力積分！";
      }
    }
    return responseMessage;
  }

  // [新增 API] 取得所有可用的運動項目與權重
  @GetMapping("/exercises")
  public Map<String, Integer> getExercises() {
    return config.getAllExercises();
  }

  // [新增 API] 動態新增家庭成員
  @PostMapping("/member")
  public String addMember(@RequestParam("name") String name) {
    String newId = "M-" + java.util.UUID.randomUUID().toString().substring(0, 4);
    Member newMember = new Member(newId, name);
    family.addMember(newMember);
    return "✅ 成功新增成員：" + name;
  }

  // [新增 API] 動態新增運動與權重
  @PostMapping("/exercise-type")
  public String addExerciseType(@RequestParam("name") String name,
      @RequestParam("weight") int weight) {
    config.addExercise(name, weight);
    return "✅ 成功新增運動項目：" + name + " (權重: " + weight + ")";
  }

  // [新增 API] 刪除家庭成員
  @DeleteMapping("/member/{id}")
  public String deleteMember(@PathVariable("id") String id) {
    Member target = family.getMembers().stream()
        .filter(m -> m.getMemberId().equals(id))
        .findFirst()
        .orElse(null);
    if (target != null) {
      family.removeMember(target);
    }
    return "✅ 已移除成員";
  }

  // [新增 API] 刪除運動項目
  @DeleteMapping("/exercise-type/{name}")
  public String deleteExerciseType(@PathVariable("name") String name) {
    config.removeExercise(name);
    return "✅ 已移除運動項目";
  }
}