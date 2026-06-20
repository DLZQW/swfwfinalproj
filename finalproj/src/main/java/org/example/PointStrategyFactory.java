package org.example;

import java.time.LocalDate;

public class PointStrategyFactory {
  public static PointStrategy createStrategy(String type, ExerciseWeightConfig config, LocalDate date) {
    PointStrategy standard = new StandardPointStrategy(config);
    if (type == null) {
      return standard;
    }
    
    switch (type.toUpperCase()) {
      case "HOLIDAY":
        return new HolidayDoublePointStrategy(standard);
      case "LATE_PENALTY":
        return new LateLoggingPenaltyStrategy(standard, date);
      case "STANDARD":
      default:
        return standard;
    }
  }
}
