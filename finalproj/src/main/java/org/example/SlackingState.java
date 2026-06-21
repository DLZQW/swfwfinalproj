package org.example;

public class SlackingState implements MemberState {
  @Override
  public String getStateName() {
    return "怠惰中";
  }

  @Override
  public double getPointMultiplier() {
    return 0.5; // 點數折半
  }

  @Override
  public void transitionState(Member member, ExerciseRecord record) {
    // 重新運動後，直接回復為正常狀態
    member.setState(new NormalState());
    System.out.println("🍀 [狀態變更] " + member.getName() + " 重新開始運動，脫離了 [怠惰中] 狀態，恢復為 [正常] 狀態！");
  }
}
