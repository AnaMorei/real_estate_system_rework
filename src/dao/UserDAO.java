package dao;

import dao.DatabaseConnection;
import util.PasswordUtil;
import model.UserType;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

  Connection connection = new DatabaseConnection().getConnection();

  public List<User> getAllUsers() {
    String sql = "SELECT * FROM Users";
    List<User> users = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setCpf(resultSet.getString("cpf"));
        user.setType(UserType.fromTypeName(resultSet.getString("type")));
        users.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public User getUserById(int id) {
    String sql = "SELECT * FROM Users WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setCpf(resultSet.getString("cpf"));
        user.setType(UserType.fromTypeName(resultSet.getString("type")));
        return user;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public User getUserByEmail(String email) {
    final String sql = "SELECT * FROM Users WHERE email = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setCpf(resultSet.getString("cpf"));
        user.setType(UserType.fromTypeName(resultSet.getString("type")));
        return user;
      }
    } catch (SQLException e) {
      e.addSuppressed(e);
    }
    return null;
  }

  public boolean validateUser(String email, char[] password) {
    final String query = "SELECT * FROM Users WHERE email = ?";

    try (PreparedStatement statement = connection.prepareStatement(query);) {
      statement.setString(1, email);
      ResultSet result = statement.executeQuery();

      if (!result.next()) {
        return false;
      }

      String hashedPassword = result.getString("password");

      boolean isValidUser = new PasswordUtil().verifyPassword(password, hashedPassword);

      if (isValidUser) {
        return true;
      };

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean addUser(User user, char[] password) {
    final String query = "INSERT INTO Users (name, email, cpf, password, type) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getCpf());
      statement.setString(4, new PasswordUtil().hashPassword(password));
      statement.setString(5, user.getType().getTypeName());

      final int rowsInserted = statement.executeUpdate();
      return rowsInserted > 0;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean removeUser(String email) {
    final String query = "DELETE FROM Users WHERE email = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);

      int rowsDeleted = statement.executeUpdate();
      return rowsDeleted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}
