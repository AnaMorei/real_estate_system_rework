package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static final String USER = "root";
  private static final String PASSWORD = "";
  private static final String CONNECTION_STR = "jdbc:mysql://localhost:3306/";
  private static final String DATABASE = "real_estate_ana";
  private static Connection connection = null;

  public DatabaseConnection() {
  }

  public void startConnection() throws SQLException {
    try {
      this.connection = DriverManager.getConnection(CONNECTION_STR + DATABASE, USER, PASSWORD);
      if (connection != null) {
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return this.connection;
  }

  public void closeConnection() throws SQLException {
    try {
      if (this.connection != null) {
        this.connection.close();
      } else {
        return;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
