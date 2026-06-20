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

    // 1. 計算應得點數
    int pointsToAward = currentStrategy.calculatePoints(member, record);

    // 2. 將紀錄與點數更新至成員物件 (此時會觸發成員內部狀態轉移)
    member.addExerciseRecord(record);
    member.addPoints(pointsToAward);

    // 3. 觸發觀察者模式，發送通知與解鎖成就
    for (ExerciseObserver observer : observers) {
      observer.onExerciseRecorded(member, record, pointsToAward);
    }
  }
}