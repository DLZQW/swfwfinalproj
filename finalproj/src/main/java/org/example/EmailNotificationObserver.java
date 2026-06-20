package org.example;

public class EmailNotificationObserver implements ExerciseObserver {
  @Override
  public void onExerciseRecorded(Member member, ExerciseRecord record, int pointsEarned) {
    System.out.println("\n📬 [Email 模擬傳送]");
    System.out.println("收件人: " + member.getName() + " (member_" + member.getMemberId() + "@healthtracker.org)");
    System.out.println("主旨: 您已成功登錄運動紀錄並獲得積分！");
    System.out.println("內容: 您好，" + member.getName() + "！您已於 " + record.getExerciseDate() 
        + " 完成了 " + record.getDurationMinutes() + " 分鐘的「" + record.getExerciseName() 
        + "」運動。根據您的活力狀態 [" + member.getStateName() + "] (倍率: x" + member.getPointMultiplier() 
        + ")，此次運動共獲得了 " + pointsEarned + " 點積分！目前您的總個人積分為 " + member.getPersonalPoints() + " 點。繼續保持！");
    System.out.println("-----------------------------------------------\n");
  }
}
