package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LateLoggingPenaltyStrategy implements PointStrategy {
  private final PointStrategy baseStrategy;
  private final LocalDate currentDate; // 系統當前日期，方便測試注入

  public LateLoggingPenaltyStrategy(PointStrategy baseStrategy, LocalDate currentDate) {
    this.baseStrategy = baseStrategy;
    this.currentDate = currentDate;
  }

  @Override
  public int calculatePoints(Member member, ExerciseRecord record) {
    int basePoints = baseStrategy.calculatePoints(member, record);

    long daysLate = ChronoUnit.DAYS.between(record.getExerciseDate(), currentDate);

    if (daysLate > 0 && daysLate <= 3) {
      // 補登錄且在 3 天內，分數打 8 折
      return (int) (basePoints * 0.8);
    } else if (daysLate > 3) {
      // 超過 3 天的紀錄不給分 (防呆機制)
      return 0;
    }

    // 當天登錄，不打折
    return basePoints;
  }
}