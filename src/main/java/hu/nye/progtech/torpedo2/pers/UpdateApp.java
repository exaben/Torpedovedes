package hu.nye.progtech.torpedo2.pers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UpdateApp {
  private Connection connect() {
    String url = "jdbc:sqlite:/home/bence/java-projects/Torpedo2/highScore.db";
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  public void update(String name) {
    String sql = "UPDATE hstable SET points = ? "
        + "WHERE name = ?";
    SelectApp app = new SelectApp();
    int points = app.getNametoPoints(name);
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, points + 1);
      pstmt.setString(2, name);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
