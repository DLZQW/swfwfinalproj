package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
  private String memberId;
  private String name;
  private int personalPoints;
  private List<ExerciseRecord> exerciseRecords;
  private MemberState state;
  private List<String> achievements; // 🌟 新增：存放成就的列表

  public Member(String memberId, String name) {
    this.memberId = memberId;
    this.name = name;
    this.personalPoints = 0;
    this.exerciseRecords = new ArrayList<>();
    this.state = new NormalState();
    this.achievements = new ArrayList<>(); // 🌟 初始化
  }

  public void addExerciseRecord(ExerciseRecord record) {
    if (record != null) {
      this.exerciseRecords.add(record);
      this.state.transitionState(this, record);
    }
  }

  public List<ExerciseRecord> getExerciseRecords() {
    return Collections.unmodifiableList(exerciseRecords);
  }

  public void addPoints(int points) {
    this.personalPoints += points;
  }

  public void deductPoints(int points) {
    this.personalPoints = Math.max(0, this.personalPoints - points);
  }

  // 🌟 新增：寫入成就的方法
  public void addAchievement(String title) {
    if (!this.achievements.contains(title)) {
      this.achievements.add(title);
    }
  }

  // 🌟 新增：讓 Spring Boot 能轉成 JSON 給網頁
  public List<String> getAchievements() {
    return Collections.unmodifiableList(achievements);
  }

  public String getMemberId() { return memberId; }
  public String getName() { return name; }
  public int getPersonalPoints() { return personalPoints; }

  public MemberState getState() { return state; }
  public void setState(MemberState state) { this.state = state; }

  public String getStateName() {
    return this.state.getStateName();
  }

  public double getPointMultiplier() {
    return this.state.getPointMultiplier();
  }
}
