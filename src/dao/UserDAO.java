package dao;

import utils.PasswordUtil;
import model.UserType;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

  private final Connection connection;

  public UserDAO(Connection connection) {
    this.connection = connection;
  }

  public List<User> getAllUsers() throws SQLException {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM Users";

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

      return users;

    } catch (SQLException error) {
      throw error;
    }
  }

  public User getUserById(int id) throws SQLException {
    String sql = "SELECT * FROM Users WHERE id = ?";
    User user = null;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setCpf(resultSet.getString("cpf"));
        user.setType(UserType.fromTypeName(resultSet.getString("type")));
      }

      return user;

    } catch (SQLException error) {
      throw error;
    }
  }

  public User getUserByEmail(String email) throws SQLException {
    final String sql = "SELECT * FROM Users WHERE email = ?";
    User user = null;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setCpf(resultSet.getString("cpf"));
        user.setType(UserType.fromTypeName(resultSet.getString("type")));
      }

      return user;

    } catch (SQLException error) {
      throw error;
    }
  }

  public boolean validateUser(String email, char[] password) throws SQLException {
    final String query = "SELECT * FROM Users WHERE email = ?";

    try (PreparedStatement statement = connection.prepareStatement(query);) {
      statement.setString(1, email);
      ResultSet result = statement.executeQuery();

      if (!result.next()) {
        return false;
      }

      String hashedPassword = result.getString("password");

      return PasswordUtil.verifyPassword(password, hashedPassword);

    } catch (SQLException error) {
      throw error;
    }
  }

  public boolean addUser(User user, char[] password) throws SQLException {
    final String query = "INSERT INTO Users (name, email, cpf, password, type) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getCpf());
      statement.setString(4, PasswordUtil.hashPassword(password));
      statement.setString(5, user.getType().getTypeName());

      final int rowsInserted = statement.executeUpdate();
      return rowsInserted > 0;

    } catch (SQLException error) {
      throw error;
    }
  }

  public boolean removeUser(String email) throws SQLException {
    final String query = "DELETE FROM Users WHERE email = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);

      int rowsDeleted = statement.executeUpdate();
      return rowsDeleted > 0;

    } catch (SQLException error) {
      throw error;
    }
  }

}
