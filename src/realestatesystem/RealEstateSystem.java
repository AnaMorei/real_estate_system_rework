package realestatesystem;

import dao.DatabaseConnection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import view.LoginScreen;

public class RealEstateSystem {

  public static void main(String args[]) {
    DatabaseConnection database = DatabaseConnection.getInstance();

    try {
      database.startConnection();
      
    } catch (SQLException error) {
      JOptionPane.showMessageDialog(
          null,
          "Houve algum erro ao se conectar com o banco de dados.",
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    
      System.exit(0);
    }

    java.awt.EventQueue.invokeLater(() -> {
      new LoginScreen().setVisible(true);
    });

  }
}
