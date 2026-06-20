package org.example;

import java.util.HashSet;
import java.util.Set;

public class AchievementObserver implements ExerciseObserver {
  // 記錄已解鎖的成就，避免重複觸發
  private final Set<String> unlockedAchievements = new HashSet<>();

  @Override
  public void onExerciseRecorded(Member member, ExerciseRecord record, int pointsEarned) {
    String memberId = member.getMemberId();

    // 1. 初試身手 (第 1 次運動)
    String a1Key = memberId + "_FIRST_STEP";
    if (!unlockedAchievements.contains(a1Key) && member.getExerciseRecords().size() == 1) {
      unlockedAchievements.add(a1Key);
      printAchievement(member.getName(), "初試身手", "完成人生中第 1 次運動登錄！踏出健康的第一步！");
    }

    // 2. 鋼鐵超人 (單次運動時間 >= 60 分鐘)
    String a2Key = memberId + "_IRON_MAN";
    if (!unlockedAchievements.contains(a2Key) && record.getDurationMinutes() >= 60) {
      unlockedAchievements.add(a2Key);
      printAchievement(member.getName(), "鋼鐵超人", "單次持續運動達到 " + record.getDurationMinutes() + " 分鐘！意志力驚人！");
    }

    // 3. 健康達人 (個人總點數 >= 100 點)
    String a3Key = memberId + "_FITNESS_EXPERT";
    if (!unlockedAchievements.contains(a3Key) && member.getPersonalPoints() >= 100) {
      unlockedAchievements.add(a3Key);
      printAchievement(member.getName(), "健康達人", "個人總積分累計突破 100 點！健康生活已融入日常！");
    }
  }

  private void printAchievement(String memberName, String title, String description) {
    System.out.println("\n🏆 [成就解鎖] ---------------------------------");
    System.out.println("恭喜成員: " + memberName);
    System.out.println("達成成就: ★ " + title + " ★");
    System.out.println("成就說明: " + description);
    System.out.println("-----------------------------------------------\n");
  }
}
