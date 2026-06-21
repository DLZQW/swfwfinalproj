package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

public class Member {
  private String memberId;
  private String name;
  private int personalPoints;
  private List<ExerciseRecord> exerciseRecords;
  private MemberState state;
  private List<String> achievements; // 🌟 新增：存放成就的列表
  private LocalDate lastDeductionDate; // 🌟 記錄上一次執行扣分的日期

  public Member(String memberId, String name) {
    this.memberId = memberId;
    this.name = name;
    this.personalPoints = 0;
    this.exerciseRecords = new ArrayList<>();
    this.state = new NormalState();
    this.achievements = new ArrayList<>(); // 🌟 初始化
    this.lastDeductionDate = null;
  }

  public void addExerciseRecord(ExerciseRecord record) {
    if (record != null) {
      this.exerciseRecords.add(record);
      this.state.transitionState(this, record);
    }
  }

  public List<ExerciseRecord> getExerciseRecords() {
    return Collections.unmodifiableList(exerciseRecords);
  }

  public void addPoints(int points) {
    this.personalPoints += points;
  }

  public void deductPoints(int points) {
    this.personalPoints = Math.max(0, this.personalPoints - points);
  }

  // 🌟 新增：寫入成就的方法
  public void addAchievement(String title) {
    if (!this.achievements.contains(title)) {
      this.achievements.add(title);
    }
  }

  // 🌟 新增：讓 Spring Boot 能轉成 JSON 給網頁
  public List<String> getAchievements() {
    return Collections.unmodifiableList(achievements);
  }

  // 🌟 新增：計算當前連續運動天數 (昨有運動算連續，中斷就歸零)
  public int calculateCurrentStreak() {
    java.util.List<LocalDate> sortedDates = exerciseRecords.stream()
        .map(ExerciseRecord::getExerciseDate)
        .distinct()
        .sorted()
        .toList();
    if (sortedDates.isEmpty()) return 0;
    
    LocalDate today = LocalDate.now();
    LocalDate lastDate = sortedDates.get(sortedDates.size() - 1);
    
    // 如果最後一次運動比昨天還早，代表連擊已中斷
    if (java.time.temporal.ChronoUnit.DAYS.between(lastDate, today) > 1) {
      return 0;
    }
    
    int streak = 1;
    for (int i = sortedDates.size() - 1; i > 0; i--) {
      LocalDate current = sortedDates.get(i);
      LocalDate prev = sortedDates.get(i - 1);
      long gap = java.time.temporal.ChronoUnit.DAYS.between(prev, current);
      if (gap == 1) {
        streak++;
      } else if (gap > 1) {
        break;
      }
    }
    return streak;
  }

  // 🌟 新增：太久未運動扣分判斷 (第 4 天起每天扣 5 點)
  public int checkAndApplyInactivityPenalty() {
    if (exerciseRecords.isEmpty()) return 0;
    LocalDate today = LocalDate.now();
    
    // 避免同一天重複判定與扣分
    if (lastDeductionDate != null && lastDeductionDate.equals(today)) {
      return 0;
    }
    
    ExerciseRecord lastRecord = exerciseRecords.get(exerciseRecords.size() - 1);
    long daysInactive = java.time.temporal.ChronoUnit.DAYS.between(lastRecord.getExerciseDate(), today);
    
    int penalty = 0;
    if (daysInactive >= 4) {
      long daysToPenalize;
      if (lastDeductionDate == null) {
        // 從第 4 天起開始每天扣 5 點 (即 daysInactive - 3)
        daysToPenalize = daysInactive - 3;
      } else {
        // 自上次扣分日起算
        daysToPenalize = java.time.temporal.ChronoUnit.DAYS.between(lastDeductionDate, today);
      }
      
      if (daysToPenalize > 0) {
        penalty = (int) (daysToPenalize * 5);
        deductPoints(penalty);
      }
    }
    lastDeductionDate = today;
    return penalty;
  }

  // 🌟 新增統計指標 (供 JSON 序列化)
  public int getTotalWorkoutDuration() {
    return exerciseRecords.stream().mapToInt(ExerciseRecord::getDurationMinutes).sum();
  }

  public int getTotalWorkoutCount() {
    return exerciseRecords.size();
  }

  public int getCurrentStreak() {
    return calculateCurrentStreak();
  }

  public int getWeeklyGoalMinutes() {
    return 150; // 每週運動目標時間：150 分鐘
  }

  public int getWeeklyGoalPercent() {
    int duration = getTotalWorkoutDuration();
    return Math.min(100, (duration * 100) / 150);
  }

  public java.util.Map<String, Integer> getWorkoutStats() {
    java.util.Map<String, Integer> stats = new java.util.HashMap<>();
    for (ExerciseRecord r : exerciseRecords) {
      stats.put(r.getExerciseName(), stats.getOrDefault(r.getExerciseName(), 0) + 1);
    }
    return stats;
  }

  public String getMemberId() { return memberId; }
  public String getName() { return name; }
  public int getPersonalPoints() { return personalPoints; }

  public MemberState getState() { return state; }
  public void setState(MemberState state) { this.state = state; }

  public String getStateName() {
    return this.state.getStateName();
  }

  public double getPointMultiplier() {
    return this.state.getPointMultiplier();
  }
}
