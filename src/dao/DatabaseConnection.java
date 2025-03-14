package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static final String USER = "root";
  private static final String PASSWORD = "";
  private static final String CONNECTION_STR = "jdbc:mysql://localhost:3306/";
  private static final String DATABASE = "real_estate_ana";
  private static DatabaseConnection instance;
  private static Connection connection;

  private DatabaseConnection() {
  }

  public static DatabaseConnection getInstance() {
    if (instance == null) {
      instance = new DatabaseConnection();
    }

    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  public void startConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      try {
        DatabaseConnection.connection = DriverManager.getConnection(CONNECTION_STR + DATABASE, USER, PASSWORD);

        if (connection != null) {
          System.out.println("Connection to dabase established.");
        }

      } catch (SQLException error) {
        System.err.println("Error starting database connection:" + error.getMessage());
        throw error;
      }
    }
  }

  public void closeConnection() throws SQLException {

    if (connection != null && !connection.isClosed()) {
      try {
        connection.close();
        System.err.println("Database connection closed.");

      } catch (SQLException error) {
        System.err.println("Error closing database connection: " + error.getMessage());
        throw error;
      }
    }

  }
}
