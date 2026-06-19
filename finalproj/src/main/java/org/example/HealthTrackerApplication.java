package org.example; // 這裡請替換成你專案頂端的 package 名稱

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthTrackerApplication {
  public static void main(String[] args) {
    // 這行程式碼會啟動內建的伺服器，並載入我們寫的 API
    SpringApplication.run(HealthTrackerApplication.class, args);
    System.out.println("\n🚀 系統已成功啟動！請打開瀏覽器前往 http://localhost:8080\n");
  }
}