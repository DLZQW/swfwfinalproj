package org.example;

public class ExerciseScoringService {
  private PointStrategy currentStrategy;

  // 允許在執行期間動態切換計分策略
  public void setStrategy(PointStrategy strategy) {
    this.currentStrategy = strategy;
  }

  // 核心業務流程：新增紀錄並結算分數
  public void recordAndScore(Member member, ExerciseRecord record) {
    if (currentStrategy == null) {
      throw new IllegalStateException("計分策略尚未設定！");
    }

    // 1. 計算應得點數
    int pointsToAward = currentStrategy.calculatePoints(record);

    // 2. 將紀錄與點數更新至成員物件
    member.addExerciseRecord(record);
    member.addPoints(pointsToAward);

    // System.out.println("成員 " + member.getName() + " 獲得了 " + pointsToAward + " 點！");
    // TODO: 這裡未來可以觸發 Observer Pattern，發送 Line 或 Email 通知
  }
}