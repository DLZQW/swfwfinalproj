package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Family {
  private String familyId;
  private String familyName;
  private double travelFundBalance; // 旅遊基金
  private List<Member> members;     // 聚合關係 (Aggregation)

  public Family(String familyId, String familyName) {
    this.familyId = familyId;
    this.familyName = familyName;
    this.travelFundBalance = 0.0;
    this.members = new ArrayList<>();
  }

  public void addMember(Member member) {
    if (member != null && !members.contains(member)) {
      members.add(member);
    }
  }

  public void removeMember(Member member) {
    members.remove(member);
  }

  public void addTravelFund(double amount) {
    this.travelFundBalance += amount;
  }

  // 取得總分 (向所有 Member 請求當前分數並加總)
  public int getTotalFamilyPoints() {
    return members.stream()
            .mapToInt(Member::getPersonalPoints)
            .sum();
  }

  // Getters & 統計方法
  public String getFamilyId() { return familyId; }
  public String getFamilyName() { return familyName; }
  public double getTravelFundBalance() { return travelFundBalance; }
  public List<Member> getMembers() { return Collections.unmodifiableList(members); }

  public int getFamilyGoalPoints() {
    return 500; // 家庭旅遊基金解鎖目標為 500 點
  }

  public int getFamilyGoalPercent() {
    int totalPoints = getTotalFamilyPoints();
    return Math.min(100, (totalPoints * 100) / 500);
  }

  public java.util.Map<String, Integer> getFamilyWorkoutStats() {
    java.util.Map<String, Integer> stats = new java.util.HashMap<>();
    for (Member m : members) {
      for (ExerciseRecord r : m.getExerciseRecords()) {
        stats.put(r.getExerciseName(), stats.getOrDefault(r.getExerciseName(), 0) + 1);
      }
    }
    return stats;
  }
}