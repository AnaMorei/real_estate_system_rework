/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.House;
import dao.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HouseDAO {

  Connection connection = new DatabaseConnection().getConnection();

  public boolean addHouse(House house) {
    String query = "INSERT INTO Houses (address, description, price, size, realtor_id) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, house.getAddress());
      statement.setString(2, house.getDescription());
      statement.setDouble(3, house.getPrice());
      statement.setDouble(4, house.getSize());
      statement.setInt(5, house.getRealtorId());
      int rowInserted = statement.executeUpdate();
      return rowInserted > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public List<House> getAllHouses() {
    List<House> houses = new ArrayList<House>();
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
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return houses;
  }

  public void updateHouse(House house) {
    String query = "UPDATE Houses SET address = ?, price = ?, size = ?, realtor_id = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, house.getAddress());
      statement.setDouble(2, house.getPrice());
      statement.setDouble(3, house.getSize());
      statement.setInt(4, house.getRealtorId());
      statement.setInt(5, house.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteHouse(int houseId) {
    String query = "DELETE FROM Houses WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, houseId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public House getHouseById(int houseId) {
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return house;
  }
}
