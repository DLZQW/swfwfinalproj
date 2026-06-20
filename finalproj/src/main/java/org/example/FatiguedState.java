package org.example;

public class FatiguedState implements MemberState {
  @Override
  public String getStateName() {
    return "疲勞";
  }

  @Override
  public double getPointMultiplier() {
    return 0.5;
  }

  @Override
  public void transitionState(Member member, ExerciseRecord record) {
    if (record.getDurationMinutes() <= 30) {
      member.setState(new NormalState());
      System.out.println("🍀 [狀態變更] " + member.getName() + " 進行了輕度運動（" + record.getDurationMinutes() + " 分鐘），成功恢復至 [正常] 狀態！");
    } else {
      System.out.println("⚠️ [狀態警告] " + member.getName() + " 目前處於疲勞狀態，仍進行了強度較高的運動，建議適度休息。");
    }
  }
}
