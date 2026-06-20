package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
  private String memberId;
  private String name;
  private int personalPoints;
  private List<ExerciseRecord> exerciseRecords; // 組合關係 (Composition)
  private MemberState state; // 狀態模式 (State Pattern)

  public Member(String memberId, String name) {
    this.memberId = memberId;
    this.name = name;
    this.personalPoints = 0;
    this.exerciseRecords = new ArrayList<>();
    this.state = new NormalState(); // 預設為正常狀態
  }

  // 新增運動紀錄 (由 Member 負責管理自己的紀錄)
  public void addExerciseRecord(ExerciseRecord record) {
    if (record != null) {
      this.exerciseRecords.add(record);
      // 觸發狀態轉換
      this.state.transitionState(this, record);
    }
  }

  // 取得不可變的紀錄列表，保護內部狀態封裝
  public List<ExerciseRecord> getExerciseRecords() {
    return Collections.unmodifiableList(exerciseRecords);
  }

  public void addPoints(int points) {
    this.personalPoints += points;
  }

  public void deductPoints(int points) {
    this.personalPoints = Math.max(0, this.personalPoints - points);
  }

  // Getters & Setters
  public String getMemberId() { return memberId; }
  public String getName() { return name; }
  public int getPersonalPoints() { return personalPoints; }
  
  public MemberState getState() { return state; }
  public void setState(MemberState state) { this.state = state; }

  // 提供給 JSON 序列化（前端可以直接讀取）
  public String getStateName() {
    return this.state.getStateName();
  }

  public double getPointMultiplier() {
    return this.state.getPointMultiplier();
  }
}