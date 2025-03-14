package dao;

import model.Transaction;
import dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

  Connection connection = new DatabaseConnection().getConnection();

  public boolean addTransaction(Transaction transaction) {
    String query = "INSERT INTO Transactions (date, amount, buyer_id, realtor_id, house_id) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(transaction.getDate().getTime()));
      statement.setDouble(2, transaction.getAmount());
      statement.setInt(3, transaction.getBuyerId());
      statement.setInt(4, transaction.getRealtorId());
      statement.setInt(5, transaction.getHouseId());
      int rowInserted = statement.executeUpdate();
      return rowInserted > 0;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public List<Transaction> getAllTransactions() {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT * FROM Transactions";
    try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery();) {
      while (resultSet.next()) {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getInt("id"));
        transaction.setDate(resultSet.getDate("date"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setBuyerId(resultSet.getInt("buyer_id"));
        transaction.setRealtorId(resultSet.getInt("realtor_id"));
        transaction.setHouseId(resultSet.getInt("house_id"));
        transactions.add(transaction);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transactions;
  }

  public Transaction findTransactionById(int transactionId) {
    String query = "SELECT * FROM Transactions WHERE id = ?";
    Transaction transaction = null;
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, transactionId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        transaction = new Transaction();
        transaction.setId(resultSet.getInt("id"));
        transaction.setDate(resultSet.getDate("date"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setBuyerId(resultSet.getInt("buyer_id"));
        transaction.setRealtorId(resultSet.getInt("realtor_id"));
        transaction.setHouseId(resultSet.getInt("house_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return transaction;
  }

  public void deleteTransaction(int transactionId) {
    String query = "DELETE FROM Transactions WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, transactionId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
