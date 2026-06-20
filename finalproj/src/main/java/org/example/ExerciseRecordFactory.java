package org.example;

import java.time.LocalDate;
import java.util.UUID;

public class ExerciseRecordFactory {
  public static ExerciseRecord createRecord(String exerciseName, int durationMinutes) {
    return new ExerciseRecord(
        UUID.randomUUID().toString(),
        LocalDate.now(),
        exerciseName,
        durationMinutes
    );
  }
}
