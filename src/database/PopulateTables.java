package database;

import dao.*;
import model.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class PopulateTables {

  public static void main(String args[]) {
    DatabaseConnection database = DatabaseConnection.getInstance();
    Connection connection = null;

    try {
      database.startConnection();
      connection = database.getConnection();

    } catch (SQLException erro) {
      return;
    }

    UserDAO userDao = new UserDAO(connection);
    HouseDAO houseDao = new HouseDAO(connection);
    TransactionDAO transactionDao = new TransactionDAO(connection);

    User[] users = {
      new User("John Doe", "john.doe@example.com", "40214857247", UserType.REALTOR),
      new User("Jane Smith", "jane.smith@example.com", "33888390850", UserType.REALTOR),
      new User("Alice Johnson", "alice.johnson@example.com", "44099918092", UserType.BUYER),
      new User("Bob Brown", "bob.brown@example.com", "27071613303", UserType.BUYER),
      new User("Charlie Davis", "charlie.davis@example.com", "27173794870", UserType.BUYER)};
    House[] houses = {
      new House("123 Main St, Springfield", "", 250000.00, 150.50, 1),
      new House("456 Elm St, Shelbyville", "", 180000.00, 120.75, 1),
      new House("789 Oak St, Capital City", "", 150000.00, 90.25, 1),
      new House("321 Pine St, Ogdenville", "", 350000.00, 200.00, 1),
      new House("654 Maple St, North Haverbrook", "", 500000.00, 300.50, 1)};
    Transaction[] transactions = {
      new Transaction(Date.valueOf("2023-10-05"), 250000.00, 3, 1, 1),
      new Transaction(Date.valueOf("2023-10-05"), 180000.00, 4, 1, 2),
      new Transaction(Date.valueOf("2023-10-10"), 150000.00, 5, 2, 3),
      new Transaction(Date.valueOf("2023-10-15"), 350000.00, 3, 2, 4),
      new Transaction(Date.valueOf("2023-10-20"), 500000.00, 4, 1, 5)};

    try {
      for (User user : users) {
        userDao.addUser(user, "123".toCharArray());
      }

      for (House house : houses) {
        houseDao.addHouse(house);
      }
      for (Transaction transaction : transactions) {
        transactionDao.addTransaction(transaction);
      }

      System.out.println("Tabelas populadas com sucesso.");

    } catch (SQLException error) {
      System.err.println("Erro ao popular banco de dados: " + error.getMessage());

    } finally {

      try {
        database.closeConnection();
      } catch (SQLException error) {
        System.err.println("Erro ao fechar conexão depois de popular o banco de dados: " + error.getMessage());
      }

    }

  }
}
