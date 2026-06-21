package org.example;

import java.util.ArrayList;
import java.util.List;

public class ExerciseScoringService {
  private PointStrategy currentStrategy;
  private final List<ExerciseObserver> observers = new ArrayList<>();

  public void addObserver(ExerciseObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
    }
  }

  public void removeObserver(ExerciseObserver observer) {
    observers.remove(observer);
  }

  // 允許在執行期間動態切換計分策略
  public void setStrategy(PointStrategy strategy) {
    this.currentStrategy = strategy;
  }

  // 核心業務流程：新增紀錄並結算分數
  public void recordAndScore(Member member, ExerciseRecord record) {
    if (currentStrategy == null) {
      throw new IllegalStateException("計分策略尚未設定！");
    }

    // 0. 檢查與更新怠惰狀態 (若大於等於 3 天未運動，則變更為 SlackingState)
    java.util.List<ExerciseRecord> records = member.getExerciseRecords();
    if (!records.isEmpty()) {
      ExerciseRecord lastRecord = records.get(records.size() - 1);
      long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastRecord.getExerciseDate(), record.getExerciseDate());
      if (daysBetween >= 3) {
        member.setState(new SlackingState());
        System.out.println("⚠️ [狀態變更] " + member.getName() + " 因為超過 " + daysBetween + " 天未運動，進入了 [怠惰中] 狀態。");
      }
    }

    // 0.1. 檢查與計算連續運動獎勵 (若相隔剛好 1 天，代表連擊延續)
    int streakBonus = 0;
    if (!records.isEmpty()) {
      ExerciseRecord lastRecord = records.get(records.size() - 1);
      long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastRecord.getExerciseDate(), record.getExerciseDate());
      if (daysBetween == 1) {
        int newStreak = member.calculateCurrentStreak() + 1;
        streakBonus = Math.min(50, (newStreak - 1) * 10);
      }
    }

    // 1. 計算應得點數 (基本點數 + 連擊獎勵)
    int pointsToAward = currentStrategy.calculatePoints(member, record) + streakBonus;

    // 2. 將紀錄與點數更新至成員物件 (此時會觸發成員內部狀態轉移，如 SlackingState -> NormalState)
    record.setPointsEarned(pointsToAward);
    if (currentStrategy instanceof CooperativePointStrategy) {
      record.setCoopBonusApplied(true);
    }
    member.addExerciseRecord(record);
    member.addPoints(pointsToAward);

    // 3. 觸發觀察者模式，發送通知與解鎖成就
    for (ExerciseObserver observer : observers) {
      observer.onExerciseRecorded(member, record, pointsToAward);
    }
  }
}