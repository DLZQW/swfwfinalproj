package org.example;

import java.time.LocalDate;

public class ExerciseRecord {
  private String recordId;
  private LocalDate exerciseDate;
  private String exerciseName; // ❗把 Enum 改成字串
  private int durationMinutes;

  public ExerciseRecord(String recordId, LocalDate exerciseDate, String exerciseName, int durationMinutes) {
    this.recordId = recordId;
    this.exerciseDate = exerciseDate;
    this.exerciseName = exerciseName;
    this.durationMinutes = durationMinutes;
  }

  // Getters
  public String getRecordId() { return recordId; }
  public LocalDate getExerciseDate() { return exerciseDate; }
  public String getExerciseName() { return exerciseName; }
  public int getDurationMinutes() { return durationMinutes; }
}