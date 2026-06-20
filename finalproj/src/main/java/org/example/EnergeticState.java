package org.example;

public class EnergeticState implements MemberState {
  @Override
  public String getStateName() {
    return "活力充沛";
  }

  @Override
  public double getPointMultiplier() {
    return 1.5;
  }

  @Override
  public void transitionState(Member member, ExerciseRecord record) {
    if (record.getDurationMinutes() >= 60) {
      member.setState(new FatiguedState());
      System.out.println("⚠️ [狀態變更] " + member.getName() + " 因為運動時間過長（" + record.getDurationMinutes() + " 分鐘），轉變為 [疲勞] 狀態。");
    } else if (record.getDurationMinutes() < 15) {
      member.setState(new NormalState());
      System.out.println("💤 [狀態變更] " + member.getName() + " 的運動時間小於 15 分鐘，回復為 [正常] 狀態。");
    }
  }
}
