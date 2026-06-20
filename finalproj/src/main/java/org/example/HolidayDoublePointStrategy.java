package org.example;

public class HolidayDoublePointStrategy implements PointStrategy {
  private final PointStrategy baseStrategy;

  // 這裡運用了裝飾者模式 (Decorator) 的概念來包裝基礎策略
  public HolidayDoublePointStrategy(PointStrategy baseStrategy) {
    this.baseStrategy = baseStrategy;
  }

  @Override
  public int calculatePoints(Member member, ExerciseRecord record) {
    // 取得原基礎分數後直接乘以 2
    return baseStrategy.calculatePoints(member, record) * 2;
  }
}