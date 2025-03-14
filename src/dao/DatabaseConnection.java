package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import utils.MessageDisplayer;

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
          MessageDisplayer.logMessage(Level.INFO, "Connection to dabase established.");
        }
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  public void closeConnection() throws SQLException {
    if (connection != null && !connection.isClosed()) {
      try {
        connection.close();
        MessageDisplayer.logMessage(Level.INFO, "Database connection closed.");
      } catch (SQLException error) {
        MessageDisplayer.logMessage(Level.SEVERE, "Error closing database connection: " + error.getMessage());
        throw error;
      }
    }
  }
}
