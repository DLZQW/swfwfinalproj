package org.example;

public interface PointStrategy {
  /**
   * 計算單筆運動紀錄應得的分數
   * @param member 成員
   * @param record 運動紀錄
   * @return 計算後的點數
   */
  int calculatePoints(Member member, ExerciseRecord record);
}