package org.example;

import java.util.HashMap;
import java.util.Map;

public class ExerciseWeightConfig {
  private final Map<String, Integer> weights;

  public ExerciseWeightConfig() {
    weights = new HashMap<>();
    // 系統預設的運動 (現在變成字串了)
    weights.put("重量訓練", 4);
    weights.put("跑步", 3);
    weights.put("皮拉提斯", 2);
    weights.put("快走", 1);
  }

  // 🌟 新增擴充點：允許動態加入新運動與權重
  public void addExercise(String name, int weight) {
    weights.put(name, weight);
  }

  // 在原本的 addExercise 下方加入這個方法
  public void removeExercise(String name) {
    weights.remove(name);
  }

  public int getWeight(String name) {
    return weights.getOrDefault(name, 1); // 找不到就預設給 1 分
  }

  // 取得目前所有的運動清單 (給前端網頁產生下拉選單用)
  public Map<String, Integer> getAllExercises() {
    return weights;
  }
}