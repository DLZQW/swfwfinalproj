package org.example;

public class StandardPointStrategy implements PointStrategy {
  private final ExerciseWeightConfig config;

  public StandardPointStrategy(ExerciseWeightConfig config) {
    this.config = config;
  }

  @Override
  public int calculatePoints(Member member, ExerciseRecord record) {
    if (record == null || record.getDurationMinutes() <= 0 || member == null) {
      return 0;
    }
    int weight = config.getWeight(record.getExerciseName());
    // 乘上成員狀態的點數加成倍率
    double multiplier = member.getPointMultiplier();
    return (int) (record.getDurationMinutes() * weight * multiplier);
  }
}