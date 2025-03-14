package utils;

import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class MessageDisplayer {

  private static final Logger LOGGER = Logger.getLogger(MessageDisplayer.class.getName());
  private static final String LOG_FILE = "application.log";
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static void logMessage(Level level, String message) {
    String formattedMessage = "[" + DATE_FORMAT.format(new Date()) + "] " + level.getName() + ": " + message;

    if (level == Level.SEVERE) {
      System.err.println(formattedMessage);
    } else {
      System.out.println(formattedMessage);
    }

    logToFile(formattedMessage);
  }

  private static void logToFile(String message) {
    try (FileWriter fileWriter = new FileWriter(LOG_FILE, true); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
      bufferedWriter.write(message);
      bufferedWriter.newLine();

    } catch (IOException e) {
      System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
      LOGGER.log(Level.SEVERE, "Erro ao escrever no arquivo de log", e);
    }
  }

  public static void showErrorMessage(Component parentComponent, String message, String title) {
    JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.ERROR_MESSAGE);
    logMessage(Level.SEVERE, "ERRO: " + title + " - " + message);
  }

  public static void showSuccessMessage(Component parentComponent, String message, String title) {
    JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.INFORMATION_MESSAGE);
    logMessage(Level.INFO, "SUCESSO: " + title + " - " + message);
  }

  public static void showDatabaseErrorDialog(Component parentComponent, String message) {
    JOptionPane.showMessageDialog(parentComponent, message, "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
    logMessage(Level.SEVERE, "ERRO DE BANCO DE DADOS: " + message);
  }
}
