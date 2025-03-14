package dao;

import model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

  private final Connection connection;

  public TransactionDAO(Connection connection) {
    this.connection = connection;
  }

  public boolean addTransaction(Transaction transaction) throws SQLException {
    String query = "INSERT INTO Transactions (date, amount, buyer_id, realtor_id, house_id) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(transaction.getDate().getTime()));
      statement.setDouble(2, transaction.getAmount());
      statement.setInt(3, transaction.getBuyerId());
      statement.setInt(4, transaction.getRealtorId());
      statement.setInt(5, transaction.getHouseId());

      int rowInserted = statement.executeUpdate();
      
      return rowInserted > 0;

    } catch (SQLException error) {
      System.err.println("Erro ao inserir transação no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public List<Transaction> getAllTransactions() throws SQLException {
    List<Transaction> transactions = new ArrayList<>();
    String query = "SELECT * FROM Transactions";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();

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

      return transactions;

    } catch (SQLException error) {
      System.err.println("Erro ao buscar transações no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public Transaction findTransactionById(int transactionId) throws SQLException {
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

      return transaction;

    } catch (SQLException error) {
      System.err.println("Erro ao buscar transação no banco de dados: " + error.getMessage());
      throw error;
    }
  }

  public boolean deleteTransaction(int transactionId) throws SQLException {
    String query = "DELETE FROM Transactions WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, transactionId);

      int rowChanged = statement.executeUpdate();
      return rowChanged > 0;

    } catch (SQLException error) {
      System.err.println("Erro ao deletar transação no banco de dados: " + error.getMessage());
      throw error;
    }
  }
}
