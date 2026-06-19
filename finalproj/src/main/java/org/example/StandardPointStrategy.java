package org.example;

public class StandardPointStrategy implements PointStrategy {
  private final ExerciseWeightConfig config;

  public StandardPointStrategy(ExerciseWeightConfig config) {
    this.config = config;
  }

  @Override
  public int calculatePoints(ExerciseRecord record) {
    if (record == null || record.getDurationMinutes() <= 0) {
      return 0;
    }
    int weight = config.getWeight(record.getExerciseName());
    return record.getDurationMinutes() * weight;
  }
}