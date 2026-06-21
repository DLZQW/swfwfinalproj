package org.example;

public class CooperativePointStrategy implements PointStrategy {
  private final PointStrategy baseStrategy;
  private final boolean partnerExercised;

  public CooperativePointStrategy(PointStrategy baseStrategy, boolean partnerExercised) {
    this.baseStrategy = baseStrategy;
    this.partnerExercised = partnerExercised;
  }

  @Override
  public int calculatePoints(Member member, ExerciseRecord record) {
    int basePoints = baseStrategy.calculatePoints(member, record);
    if (partnerExercised) {
      return (int) (basePoints * 1.2); // 額外增加 20% 的點數
    }
    return basePoints;
  }
}
