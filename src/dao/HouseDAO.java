package dao;

import model.House;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HouseDAO {

  private final Connection connection;

  public HouseDAO(Connection connection) {
    this.connection = connection;
  }

  public boolean addHouse(House house) throws SQLException {
    String query = "INSERT INTO Houses (address, description, price, size, realtor_id) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, house.getAddress());
      statement.setString(2, house.getDescription());
      statement.setDouble(3, house.getPrice());
      statement.setDouble(4, house.getSize());
      statement.setInt(5, house.getRealtorId());

      int rowInserted = statement.executeUpdate();
      return rowInserted > 0;

    } catch (SQLException error) {
      System.err.println("Erro ao inserir casa no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public List<House> getAllHouses() throws SQLException {
    List<House> houses = new ArrayList<>();
    String query = "SELECT * FROM Houses";

    try (PreparedStatement statement = connection.prepareStatement(query);) {
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        House house = new House();
        house.setId(resultSet.getInt("id"));
        house.setAddress(resultSet.getString("address"));
        house.setPrice(resultSet.getDouble("price"));
        house.setSize(resultSet.getDouble("size"));
        house.setRealtorId(resultSet.getInt("realtor_id"));
        houses.add(house);
      }

      return houses;

    } catch (SQLException error) {
      System.err.println("Erro ao buscar casa no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public boolean updateHouse(House house) throws SQLException {
    String query = "UPDATE Houses SET address = ?, price = ?, size = ?, realtor_id = ? WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, house.getAddress());
      statement.setDouble(2, house.getPrice());
      statement.setDouble(3, house.getSize());
      statement.setInt(4, house.getRealtorId());
      statement.setInt(5, house.getId());

      int rowChanged = statement.executeUpdate();
      return rowChanged > 0;

    } catch (SQLException error) {
      System.err.println("Erro ao buscar casa no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public boolean deleteHouse(int houseId) throws SQLException {
    String query = "DELETE FROM Houses WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, houseId);

      int rowChanged = statement.executeUpdate();
      return rowChanged > 0;

    } catch (SQLException error) {
      System.err.println("Erro ao deletar casa no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public House getHouseById(int houseId) throws SQLException {
    String query = "SELECT * FROM Houses WHERE id = ?";
    House house = null;

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, houseId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        house = new House();
        house.setId(resultSet.getInt("id"));
        house.setAddress(resultSet.getString("address"));
        house.setPrice(resultSet.getDouble("price"));
        house.setSize(resultSet.getDouble("size"));
        house.setRealtorId(resultSet.getInt("realtor_id"));
      }

      return house;

    } catch (SQLException error) {
      System.err.println("Erro ao buscar casa no banco de dados: " + error.getMessage());
      throw error;
    }
  }
}
