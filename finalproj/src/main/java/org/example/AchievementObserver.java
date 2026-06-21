package org.example;

import java.util.HashSet;
import java.util.Set;

public class AchievementObserver implements ExerciseObserver {
  private final Set<String> unlockedAchievements = new HashSet<>();

  @Override
  public void onExerciseRecorded(Member member, ExerciseRecord record, int pointsEarned) {
    String memberId = member.getMemberId();

    // 1. 初試身手
    String a1Key = memberId + "_FIRST_STEP";
    if (!unlockedAchievements.contains(a1Key) && member.getExerciseRecords().size() == 1) {
      unlockedAchievements.add(a1Key);
      member.addAchievement("初試身手"); // 🌟 把成就寫入成員
      printAchievement(member.getName(), "初試身手", "完成人生中第 1 次運動登錄！");
    }

    // 2. 鋼鐵超人
    String a2Key = memberId + "_IRON_MAN";
    if (!unlockedAchievements.contains(a2Key) && record.getDurationMinutes() >= 60) {
      unlockedAchievements.add(a2Key);
      member.addAchievement("鋼鐵超人"); // 🌟 把成就寫入成員
      printAchievement(member.getName(), "鋼鐵超人", "單次持續運動達到 60 分鐘以上！");
    }

    // 3. 健康達人
    String a3Key = memberId + "_FITNESS_EXPERT";
    if (!unlockedAchievements.contains(a3Key) && member.getPersonalPoints() >= 100) {
      unlockedAchievements.add(a3Key);
      member.addAchievement("健康達人"); // 🌟 把成就寫入成員
      printAchievement(member.getName(), "健康達人", "個人總積分累計突破 100 點！");
    }
  }

  private void printAchievement(String memberName, String title, String description) {
    System.out.println("\n🏆 [成就解鎖] ---------------------------------");
    System.out.println("恭喜成員: " + memberName);
    System.out.println("達成成就: ★ " + title + " ★");
    System.out.println("-----------------------------------------------\n");
  }
}
