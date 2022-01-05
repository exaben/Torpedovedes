package hu.nye.progtech.torpedo2.pers;

import java.sql.*;

public class SelectApp {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:/home/bence/java-projects/Torpedo2/highScore.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void selectAll(){
        String sql = "SELECT id, name, points FROM hstable";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getInt("points"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int getNametoPoints(String name){
        String sql = "SELECT id, name, points "
                + "FROM hstable WHERE name = ?";
        int points = 0;
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1, name);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                points = rs.getInt("points");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return points;
    }
    public void selectSort(){
        String sql = "SELECT id, name, points FROM hstable ORDER BY points DESC";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(
                        rs.getString("name") + "\t" +
                                rs.getInt("points"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
