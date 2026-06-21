package org.example;

import java.time.LocalDate;

public class ExerciseRecord {
  private String recordId;
  private LocalDate exerciseDate;
  private String exerciseName; // ❗把 Enum 改成字串
  private int durationMinutes;
  private int pointsEarned;       // 🌟 記錄該次獲得的分數
  private boolean coopBonusApplied; // 🌟 記錄是否已套用協力加成

  public ExerciseRecord(String recordId, LocalDate exerciseDate, String exerciseName, int durationMinutes) {
    this.recordId = recordId;
    this.exerciseDate = exerciseDate;
    this.exerciseName = exerciseName;
    this.durationMinutes = durationMinutes;
    this.pointsEarned = 0;
    this.coopBonusApplied = false;
  }

  // Getters & Setters
  public String getRecordId() { return recordId; }
  public LocalDate getExerciseDate() { return exerciseDate; }
  public String getExerciseName() { return exerciseName; }
  public int getDurationMinutes() { return durationMinutes; }

  public int getPointsEarned() { return pointsEarned; }
  public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

  public boolean isCoopBonusApplied() { return coopBonusApplied; }
  public void setCoopBonusApplied(boolean coopBonusApplied) { this.coopBonusApplied = coopBonusApplied; }
}