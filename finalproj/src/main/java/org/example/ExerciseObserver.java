package org.example;

public interface ExerciseObserver {
  void onExerciseRecorded(Member member, ExerciseRecord record, int pointsEarned);
}
