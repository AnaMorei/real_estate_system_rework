package realestatesystem;

import dao.DatabaseConnection;
import java.sql.SQLException;
import view.LoginScreen;

/**
 *
 * @author Clara
 */
public class RealEstateSystem {

  private DatabaseConnection database = new DatabaseConnection();

  public static void main(String args[]) {
    try {
      new DatabaseConnection().startConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }


    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new LoginScreen().setVisible(true);
      }
    });

  }
}
