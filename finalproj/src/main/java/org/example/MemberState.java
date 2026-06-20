package org.example;

public interface MemberState {
  String getStateName();
  double getPointMultiplier();
  void transitionState(Member member, ExerciseRecord record);
}
