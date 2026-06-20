package org.example;

public class NormalState implements MemberState {
  @Override
  public String getStateName() {
    return "正常";
  }

  @Override
  public double getPointMultiplier() {
    return 1.0;
  }

  @Override
  public void transitionState(Member member, ExerciseRecord record) {
    if (record.getDurationMinutes() >= 60) {
      member.setState(new FatiguedState());
      System.out.println("⚠️ [狀態變更] " + member.getName() + " 因為運動時間過長（" + record.getDurationMinutes() + " 分鐘），進入了 [疲勞] 狀態。");
    } else if (member.getExerciseRecords().size() >= 3) {
      member.setState(new EnergeticState());
      System.out.println("🔥 [狀態變更] " + member.getName() + " 運動次數達到 " + member.getExerciseRecords().size() + " 次，進入了 [活力充沛] 狀態！");
    }
  }
}
