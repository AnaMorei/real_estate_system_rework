package realestatesystem;

import dao.DatabaseConnection;
import java.sql.SQLException;
import utils.MessageDisplayer;
import view.LoginScreen;

public class RealEstateSystem {

  public static void main(String args[]) {
    DatabaseConnection database = DatabaseConnection.getInstance();

    try {
      database.startConnection();

    } catch (SQLException error) {
      MessageDisplayer.showDatabaseErrorDialog(null, "Houve algum erro ao se conectar com o banco de dados.");
      System.exit(0);
    }

    java.awt.EventQueue.invokeLater(() -> {
      new LoginScreen().setVisible(true);
    });

  }
}
