package view;

import dao.DatabaseConnection;
import dao.HouseDAO;
import dao.TransactionDAO;
import dao.UserDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import model.User;
import model.House;
import model.Transaction;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.UserType;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardScreen extends javax.swing.JFrame {

  private final HouseDAO houseDAO;
  private final TransactionDAO transactionDAO;
  private final UserDAO userDAO;

  public DashboardScreen(User user) {

    DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    Connection connection = databaseConnection.getConnection();

    this.houseDAO = new HouseDAO(connection);
    this.transactionDAO = new TransactionDAO(connection);
    this.userDAO = new UserDAO(connection);

    initComponents();
    centerOnScreen();
    setTitle("Imobiliaria online");
    setResizable(false);
    updateAll();

    createHouse.addActionListener(e -> addHouseToList(user));
    createTransaction.addActionListener(e -> addTransactionToList(user));

    if (user.getType() == UserType.BUYER) {
      disableTabIfNotAdmin();
    }
  }

  private void updateAll() {
    setHouseTableData();
    setTransactionTableData();
    setComboBoxInfo();
  }

  private void centerOnScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - getWidth()) / 2;
    int y = (screenSize.height - getHeight()) / 2;
    setLocation(x, y);
  }

  private void addTransactionToList(User user) {
    House selectedHouse = (House) transactionCreateHouseComboBox.getSelectedItem();
    User selectedUser = (User) transactionCreateBuyerComboBox.getSelectedItem();
    String price = transactionCreatePriceInput.getText();
    LocalDate date = LocalDate.now();
    Transaction transaction = new Transaction(
        Date.valueOf(date),
        Double.parseDouble(price),
        selectedUser.getId(),
        user.getId(),
        selectedHouse.getId());
    boolean isTransactionInserted = false;

    try {
      isTransactionInserted = transactionDAO.addTransaction(transaction);
    } catch (SQLException ex) {
      Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
    }

    if (isTransactionInserted) {
      transactionCreateHouseComboBox.setSelectedIndex(-1);
      transactionCreateBuyerComboBox.setSelectedIndex(-1);
      transactionCreatePriceInput.setText("");
      updateAll();
      JOptionPane.showMessageDialog(
          null,
          "A transação foi cadastrada com sucesso.",
          "Sucesso",
          JOptionPane.INFORMATION_MESSAGE
      );

      return;
    }

    JOptionPane.showMessageDialog(
        null,
        "Houve uma falha ao cadastrar a transação.",
        "Erro",
        JOptionPane.ERROR_MESSAGE
    );
  }

  private void addHouseToList(User user) {
    String address = houseCreateAddressInput.getText();
    String description = houseCreateDescriptionInput.getText();
    String size = houseCreateSizeInput.getText();
    String price = houseCreatePriceInput.getText();
    House house = new House(address, description, Double.parseDouble(size), Double.parseDouble(price), user.getId());
    boolean isHouseAdded = false;

    try {
      isHouseAdded = houseDAO.addHouse(house);
    } catch (SQLException ex) {
      Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
    }

    if (isHouseAdded) {
      houseCreateAddressInput.setText("");
      houseCreateDescriptionInput.setText("");
      houseCreateSizeInput.setText("");
      houseCreatePriceInput.setText("");
      updateAll();
      JOptionPane.showMessageDialog(
          null,
          "A casa foi cadastrada com sucesso.",
          "Sucesso",
          JOptionPane.INFORMATION_MESSAGE
      );
      return;
    }
    JOptionPane.showMessageDialog(
        null,
        "Houve uma falha ao cadastrar a casa.",
        "Erro",
        JOptionPane.ERROR_MESSAGE
    );
  }

  private void disableTabIfNotAdmin() {
    houseTab.setEnabledAt(1, false);
    transactionTab.setEnabledAt(1, false);
  }

  private void setComboBoxInfo() {
    transactionCreateHouseComboBox.removeAllItems();
    transactionCreateBuyerComboBox.removeAllItems();
    List<House> houses = null;
    List<User> users = null;

    try {
      houses = houseDAO.getAllHouses();
      users = userDAO.getAllUsers();
    } catch (SQLException ex) {
      Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
    }

    DefaultComboBoxModel<House> houseModel = new DefaultComboBoxModel<>();
    DefaultComboBoxModel<User> userModel = new DefaultComboBoxModel<>();

    if (houses != null) {
      for (House house : houses) {
        houseModel.addElement(house);
      }
      transactionCreateHouseComboBox.setModel(houseModel);

    }

    if (users != null) {
      for (User user : users) {
        userModel.addElement(user);
      }
      transactionCreateBuyerComboBox.setModel(userModel);
    }
  }

  private void setHouseTableData() {
    List<House> houses = null;

    try {
      houses = new HouseDAO().getAllHouses();
    } catch (SQLException ex) {
      Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
    }

    DefaultTableModel tableModel = (DefaultTableModel) houseListTable.getModel();
    tableModel.setRowCount(0);

    if (houses != null) {
      for (House house : houses) {
        Object[] row = {house.getAddress(), house.getDescription(), house.getSize() + " m²", String.format("%.2f", house.getPrice())};
        tableModel.addRow(row);
      }
    }

    houseListTable.setEnabled(false);
  }

  private void setTransactionTableData() {
    List<Transaction> transactions = null;

    try {
      transactions = transactionDAO.getAllTransactions();
    } catch (SQLException ex) {
      Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
    }

    DefaultTableModel tableModel = (DefaultTableModel) TransactionListTable.getModel();
    tableModel.setRowCount(0);

    if (transactions != null) {
      for (Transaction transaction : transactions) {
        House house = null;
        User user = null;

        try {
          house = houseDAO.getHouseById(transaction.getHouseId());
          user = userDAO.getUserById(transaction.getBuyerId());
        } catch (SQLException ex) {
          Logger.getLogger(DashboardScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (house == null || user == null) {
          return;
        }

        Object[] row = {
          house.getAddress(),
          user.getName(),
          transaction.getAmount(),
          transaction.getDate(),};

        tableModel.addRow(row);
      }
    }

    TransactionListTable.setEnabled(false);
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    mainFrame = new javax.swing.JTabbedPane();
    houseTab = new javax.swing.JTabbedPane();
    houseListScrollPane = new javax.swing.JScrollPane();
    houseListTable = new javax.swing.JTable();
    houseCreatePanel = new javax.swing.JPanel();
    houseCreatePanelInnerPanel = new javax.swing.JPanel();
    houseCreateAddress = new javax.swing.JPanel();
    houseCreateAddressLabel = new javax.swing.JLabel();
    houseCreateAddressInput = new javax.swing.JTextField();
    houseCreateDescriptionPanel = new javax.swing.JPanel();
    houseCreateDescriptionLabel = new javax.swing.JLabel();
    houseCreateDescriptionInput = new javax.swing.JTextField();
    houseCreateSizePanel = new javax.swing.JPanel();
    houseCreateSizeLabel = new javax.swing.JLabel();
    houseCreateSizeInput = new javax.swing.JTextField();
    houseCreatePrice = new javax.swing.JPanel();
    houseCreatePriceLabel = new javax.swing.JLabel();
    houseCreatePriceInput = new javax.swing.JTextField();
    createHouse = new javax.swing.JButton();
    transactionTab = new javax.swing.JTabbedPane();
    transactionListScrollPane = new javax.swing.JScrollPane();
    TransactionListTable = new javax.swing.JTable();
    transactionCreatePanel = new javax.swing.JPanel();
    transactionCreateInnerPanel = new javax.swing.JPanel();
    transactionCreateHousePanel = new javax.swing.JPanel();
    transactionCreateHouseLabel = new javax.swing.JLabel();
    transactionCreateHouseComboBox = new javax.swing.JComboBox();
    transactionCreateBuyerPanel = new javax.swing.JPanel();
    transactionCreateBuyerLabel = new javax.swing.JLabel();
    transactionCreateBuyerComboBox = new javax.swing.JComboBox<>();
    transactionCreatePricePanel = new javax.swing.JPanel();
    transactionCreatePriceLabel = new javax.swing.JLabel();
    transactionCreatePriceInput = new javax.swing.JTextField();
    createTransaction = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    houseListTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Endereço", "Descrição", "Tamanho", "Preço"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }
    });
    houseListTable.setShowGrid(true);
    houseListTable.addContainerListener(new java.awt.event.ContainerAdapter() {
      public void componentAdded(java.awt.event.ContainerEvent evt) {
        houseListTableComponentAdded(evt);
      }
    });
    houseListScrollPane.setViewportView(houseListTable);

    houseTab.addTab("Listar", houseListScrollPane);

    houseCreatePanelInnerPanel.setPreferredSize(new java.awt.Dimension(300, 452));
    houseCreatePanelInnerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 25));

    houseCreateAddress.setMinimumSize(new java.awt.Dimension(300, 60));

    houseCreateAddressLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    houseCreateAddressLabel.setText("Endereço");

    houseCreateAddressInput.setPreferredSize(new java.awt.Dimension(300, 40));
    houseCreateAddressInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        houseCreateAddressInputActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout houseCreateAddressLayout = new javax.swing.GroupLayout(houseCreateAddress);
    houseCreateAddress.setLayout(houseCreateAddressLayout);
    houseCreateAddressLayout.setHorizontalGroup(
      houseCreateAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateAddressLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateAddressLabel))
      .addComponent(houseCreateAddressInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    houseCreateAddressLayout.setVerticalGroup(
      houseCreateAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateAddressLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateAddressLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(houseCreateAddressInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    houseCreatePanelInnerPanel.add(houseCreateAddress);

    houseCreateDescriptionPanel.setMinimumSize(new java.awt.Dimension(300, 60));

    houseCreateDescriptionLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    houseCreateDescriptionLabel.setText("Descrição");

    houseCreateDescriptionInput.setPreferredSize(new java.awt.Dimension(300, 40));
    houseCreateDescriptionInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        houseCreateDescriptionInputActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout houseCreateDescriptionPanelLayout = new javax.swing.GroupLayout(houseCreateDescriptionPanel);
    houseCreateDescriptionPanel.setLayout(houseCreateDescriptionPanelLayout);
    houseCreateDescriptionPanelLayout.setHorizontalGroup(
      houseCreateDescriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateDescriptionPanelLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateDescriptionLabel))
      .addComponent(houseCreateDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    houseCreateDescriptionPanelLayout.setVerticalGroup(
      houseCreateDescriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateDescriptionPanelLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateDescriptionLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(houseCreateDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    houseCreatePanelInnerPanel.add(houseCreateDescriptionPanel);

    houseCreateSizePanel.setMinimumSize(new java.awt.Dimension(300, 60));

    houseCreateSizeLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    houseCreateSizeLabel.setText("Tamanho");

    houseCreateSizeInput.setPreferredSize(new java.awt.Dimension(300, 40));
    houseCreateSizeInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        houseCreateSizeInputActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout houseCreateSizePanelLayout = new javax.swing.GroupLayout(houseCreateSizePanel);
    houseCreateSizePanel.setLayout(houseCreateSizePanelLayout);
    houseCreateSizePanelLayout.setHorizontalGroup(
      houseCreateSizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateSizePanelLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateSizeLabel))
      .addComponent(houseCreateSizeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    houseCreateSizePanelLayout.setVerticalGroup(
      houseCreateSizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreateSizePanelLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreateSizeLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(houseCreateSizeInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    houseCreatePanelInnerPanel.add(houseCreateSizePanel);

    houseCreatePrice.setMinimumSize(new java.awt.Dimension(300, 60));

    houseCreatePriceLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    houseCreatePriceLabel.setText("Preço");

    houseCreatePriceInput.setPreferredSize(new java.awt.Dimension(300, 40));
    houseCreatePriceInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        houseCreatePriceInputActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout houseCreatePriceLayout = new javax.swing.GroupLayout(houseCreatePrice);
    houseCreatePrice.setLayout(houseCreatePriceLayout);
    houseCreatePriceLayout.setHorizontalGroup(
      houseCreatePriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreatePriceLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreatePriceLabel))
      .addComponent(houseCreatePriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    houseCreatePriceLayout.setVerticalGroup(
      houseCreatePriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreatePriceLayout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(houseCreatePriceLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(houseCreatePriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    houseCreatePanelInnerPanel.add(houseCreatePrice);

    createHouse.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    createHouse.setText("Criar listagem");
    createHouse.setPreferredSize(new java.awt.Dimension(300, 50));
    houseCreatePanelInnerPanel.add(createHouse);

    javax.swing.GroupLayout houseCreatePanelLayout = new javax.swing.GroupLayout(houseCreatePanel);
    houseCreatePanel.setLayout(houseCreatePanelLayout);
    houseCreatePanelLayout.setHorizontalGroup(
      houseCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(houseCreatePanelLayout.createSequentialGroup()
        .addContainerGap(489, Short.MAX_VALUE)
        .addComponent(houseCreatePanelInnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(491, Short.MAX_VALUE))
    );
    houseCreatePanelLayout.setVerticalGroup(
      houseCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(houseCreatePanelInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
    );

    houseTab.addTab("Criar", houseCreatePanel);

    mainFrame.addTab("Casas", houseTab);

    TransactionListTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Endereço", "Comprador", "Preço", "Data"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }
    });
    TransactionListTable.setColumnSelectionAllowed(true);
    transactionListScrollPane.setViewportView(TransactionListTable);
    TransactionListTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

    transactionTab.addTab("Lista", transactionListScrollPane);

    transactionCreateInnerPanel.setPreferredSize(new java.awt.Dimension(300, 650));
    transactionCreateInnerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 25));

    transactionCreateHousePanel.setMinimumSize(new java.awt.Dimension(300, 100));
    transactionCreateHousePanel.setName(""); // NOI18N
    transactionCreateHousePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    transactionCreateHouseLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    transactionCreateHouseLabel.setText("Casa");
    transactionCreateHousePanel.add(transactionCreateHouseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

    transactionCreateHouseComboBox.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
    transactionCreateHouseComboBox.setPreferredSize(new java.awt.Dimension(300, 45));
    transactionCreateHousePanel.add(transactionCreateHouseComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

    transactionCreateInnerPanel.add(transactionCreateHousePanel);

    transactionCreateBuyerPanel.setMinimumSize(new java.awt.Dimension(300, 100));
    transactionCreateBuyerPanel.setName(""); // NOI18N
    transactionCreateBuyerPanel.setPreferredSize(new java.awt.Dimension(300, 75));
    transactionCreateBuyerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    transactionCreateBuyerLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    transactionCreateBuyerLabel.setText("Comprador");
    transactionCreateBuyerPanel.add(transactionCreateBuyerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

    transactionCreateBuyerComboBox.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
    transactionCreateBuyerComboBox.setToolTipText("");
    transactionCreateBuyerComboBox.setMinimumSize(new java.awt.Dimension(300, 45));
    transactionCreateBuyerComboBox.setPreferredSize(new java.awt.Dimension(300, 45));
    transactionCreateBuyerPanel.add(transactionCreateBuyerComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 300, -1));

    transactionCreateInnerPanel.add(transactionCreateBuyerPanel);

    transactionCreatePricePanel.setMinimumSize(new java.awt.Dimension(300, 100));
    transactionCreatePricePanel.setName(""); // NOI18N
    transactionCreatePricePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    transactionCreatePriceLabel.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
    transactionCreatePriceLabel.setText("Preço");
    transactionCreatePricePanel.add(transactionCreatePriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

    transactionCreatePriceInput.setMinimumSize(new java.awt.Dimension(300, 45));
    transactionCreatePriceInput.setName(""); // NOI18N
    transactionCreatePriceInput.setPreferredSize(new java.awt.Dimension(300, 45));
    transactionCreatePriceInput.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        transactionCreatePriceInputActionPerformed(evt);
      }
    });
    transactionCreatePricePanel.add(transactionCreatePriceInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

    transactionCreateInnerPanel.add(transactionCreatePricePanel);

    createTransaction.setText("Criar");
    createTransaction.setPreferredSize(new java.awt.Dimension(300, 45));
    createTransaction.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        createTransactionActionPerformed(evt);
      }
    });
    transactionCreateInnerPanel.add(createTransaction);

    javax.swing.GroupLayout transactionCreatePanelLayout = new javax.swing.GroupLayout(transactionCreatePanel);
    transactionCreatePanel.setLayout(transactionCreatePanelLayout);
    transactionCreatePanelLayout.setHorizontalGroup(
      transactionCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionCreatePanelLayout.createSequentialGroup()
        .addContainerGap(349, Short.MAX_VALUE)
        .addComponent(transactionCreateInnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(326, 326, 326))
    );
    transactionCreatePanelLayout.setVerticalGroup(
      transactionCreatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(transactionCreateInnerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    transactionTab.addTab("Criar", transactionCreatePanel);

    mainFrame.addTab("Transações", transactionTab);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(mainFrame)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(mainFrame, javax.swing.GroupLayout.Alignment.TRAILING)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void houseCreateAddressInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_houseCreateAddressInputActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_houseCreateAddressInputActionPerformed

  private void houseCreateDescriptionInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_houseCreateDescriptionInputActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_houseCreateDescriptionInputActionPerformed

  private void houseCreatePriceInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_houseCreatePriceInputActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_houseCreatePriceInputActionPerformed

  private void houseCreateSizeInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_houseCreateSizeInputActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_houseCreateSizeInputActionPerformed

  private void houseListTableComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_houseListTableComponentAdded
    // TODO add your handling code here:

  }//GEN-LAST:event_houseListTableComponentAdded

  private void transactionCreatePriceInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionCreatePriceInputActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_transactionCreatePriceInputActionPerformed

  private void createTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createTransactionActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_createTransactionActionPerformed

  // <editor-fold defaultstate="collapsed" desc="Variables of all components">
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTable TransactionListTable;
  private javax.swing.JButton createHouse;
  private javax.swing.JButton createTransaction;
  private javax.swing.JPanel houseCreateAddress;
  private javax.swing.JTextField houseCreateAddressInput;
  private javax.swing.JLabel houseCreateAddressLabel;
  private javax.swing.JTextField houseCreateDescriptionInput;
  private javax.swing.JLabel houseCreateDescriptionLabel;
  private javax.swing.JPanel houseCreateDescriptionPanel;
  private javax.swing.JPanel houseCreatePanel;
  private javax.swing.JPanel houseCreatePanelInnerPanel;
  private javax.swing.JPanel houseCreatePrice;
  private javax.swing.JTextField houseCreatePriceInput;
  private javax.swing.JLabel houseCreatePriceLabel;
  private javax.swing.JTextField houseCreateSizeInput;
  private javax.swing.JLabel houseCreateSizeLabel;
  private javax.swing.JPanel houseCreateSizePanel;
  private javax.swing.JScrollPane houseListScrollPane;
  private javax.swing.JTable houseListTable;
  private javax.swing.JTabbedPane houseTab;
  private javax.swing.JTabbedPane mainFrame;
  private javax.swing.JComboBox<User> transactionCreateBuyerComboBox;
  private javax.swing.JLabel transactionCreateBuyerLabel;
  private javax.swing.JPanel transactionCreateBuyerPanel;
  private javax.swing.JComboBox transactionCreateHouseComboBox;
  private javax.swing.JLabel transactionCreateHouseLabel;
  private javax.swing.JPanel transactionCreateHousePanel;
  private javax.swing.JPanel transactionCreateInnerPanel;
  private javax.swing.JPanel transactionCreatePanel;
  private javax.swing.JTextField transactionCreatePriceInput;
  private javax.swing.JLabel transactionCreatePriceLabel;
  private javax.swing.JPanel transactionCreatePricePanel;
  private javax.swing.JScrollPane transactionListScrollPane;
  private javax.swing.JTabbedPane transactionTab;
  // End of variables declaration//GEN-END:variables
  // </editor-fold>     
}
