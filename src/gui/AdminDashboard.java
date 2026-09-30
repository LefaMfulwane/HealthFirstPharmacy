package gui;

import dao.MedicineDAO;
import dao.ReportDAO;
import dao.SupplierDAO;
import dao.USerDAO;
import model.Medicine;
import model.Supplier;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;


/**
 *
 * @author admin
 */
public class AdminDashboard extends JFrame {

    private User currentUser;
    private MedicineDAO medicineDAO;
    private SupplierDAO supplierDAO;
    private USerDAO userDAO;
    private ReportDAO reportDAO;

    // Component references
    private JTabbedPane tabbedPane;

    // Medicine Tab components
    private JTable tblMedicines;
    private DefaultTableModel modelMedicines;
    private JTextField txtMedName, txtCompany, txtPrice, txtStock, txtReorder, txtExpiry, txtMedSupplierId;
    private JComboBox<String> comboMedType;

    // Supplier Tab components
    private JTable tblSuppliers;
    private DefaultTableModel modelSuppliers;
    private JTextField txtSupName, txtContact, txtPhone, txtEmail, txtAddress;

    // User Tab components
    private JTable tblUsers;
    private DefaultTableModel modelUsers;
    private JTextField txtUsername, txtPassword, txtFullName;
    private JComboBox<String> comboRole;

    // Reports Tab components
    private JTable tblSalesReport, tblExpiryReport;
    private DefaultTableModel modelSalesReport, modelExpiryReport;

    public AdminDashboard(User user) {
        this.currentUser = user;
        this.medicineDAO = new MedicineDAO();
        this.supplierDAO = new SupplierDAO();
        this.userDAO = new USerDAO();
        this.reportDAO = new ReportDAO();

        initComponents();
        loadAllData();
    }

    private void initComponents() {
        setTitle("HealthFirst Pharmacy - Administrator Dashboard (Logged in: " + currentUser.getFullName() + ")");
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 153));
        JLabel lblTitle = new JLabel(" HealthFirst Administrator Panel", JLabel.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane Setup
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));

        tabbedPane.addTab("Medicine Management", createMedicineTab());
        tabbedPane.addTab("Supplier Management", createSupplierTab());
        tabbedPane.addTab("User Management", createUserTab());
        tabbedPane.addTab("Business Reports", createReportsTab());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    // -------------------------------------------------------------------------
    // 1. MEDICINE MANAGEMENT TAB
    // -------------------------------------------------------------------------
    private JPanel createMedicineTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Table
        String[] cols = {"ID", "Name", "Company", "Type", "Price (R)", "Stock", "Reorder", "Expiry", "Supplier ID"};
        modelMedicines = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblMedicines = new JTable(modelMedicines);
        panel.add(new JScrollPane(tblMedicines), BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Medicine Form (Add / Update / Delete)"));

        txtMedName = new JTextField();
        txtCompany = new JTextField();
        comboMedType = new JComboBox<>(new String[]{"Tablet", "Capsule", "Syrup", "Injection", "Cream", "Other"});
        txtPrice = new JTextField();
        txtStock = new JTextField();
        txtReorder = new JTextField();
        txtExpiry = new JTextField("YYYY-MM-DD");
        txtMedSupplierId = new JTextField();

        formPanel.add(new JLabel("Name:")); formPanel.add(txtMedName);
        formPanel.add(new JLabel("Company:")); formPanel.add(txtCompany);
        formPanel.add(new JLabel("Type:")); formPanel.add(comboMedType);
        formPanel.add(new JLabel("Price (R):")); formPanel.add(txtPrice);
        formPanel.add(new JLabel("Quantity:")); formPanel.add(txtStock);
        formPanel.add(new JLabel("Reorder Level:")); formPanel.add(txtReorder);
        formPanel.add(new JLabel("Expiry Date:")); formPanel.add(txtExpiry);
        formPanel.add(new JLabel("Supplier ID:")); formPanel.add(txtMedSupplierId);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Medicine");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnClear = new JButton("Clear Form");

        btnAdd.addActionListener(e -> addMedicine());
        btnUpdate.addActionListener(e -> updateMedicine());
        btnDelete.addActionListener(e -> deleteMedicine());
        btnClear.addActionListener(e -> clearMedicineForm());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // 2. SUPPLIER MANAGEMENT TAB
    // -------------------------------------------------------------------------
    private JPanel createSupplierTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Supplier ID", "Name", "Contact Person", "Phone", "Email", "Address"};
        modelSuppliers = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblSuppliers = new JTable(modelSuppliers);
        panel.add(new JScrollPane(tblSuppliers), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Form (Add / Update / Delete)"));

        txtSupName = new JTextField();
        txtContact = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        formPanel.add(new JLabel("Name:")); formPanel.add(txtSupName);
        formPanel.add(new JLabel("Contact Person:")); formPanel.add(txtContact);
        formPanel.add(new JLabel("Phone:")); formPanel.add(txtPhone);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);
        formPanel.add(new JLabel("Address:")); formPanel.add(txtAddress);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Supplier");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");

        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);
        return panel;
    }

    // -------------------------------------------------------------------------
    // 3. USER MANAGEMENT TAB
    // -------------------------------------------------------------------------
    private JPanel createUserTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"User ID", "Username", "Role", "Full Name"};
        modelUsers = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblUsers = new JTable(modelUsers);
        panel.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Form (Add / Delete Accounts)"));

        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtFullName = new JTextField();
        comboRole = new JComboBox<>(new String[]{"Cashier", "Admin"});

        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtFullName);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(comboRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Create User");
        JButton btnDelete = new JButton("Delete Selected User");
        btnAdd.addActionListener(e -> addUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    // -------------------------------------------------------------------------
    // 4. REPORTS TAB
    // -------------------------------------------------------------------------
    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Sales Performance Panel
        JPanel salesPanel = new JPanel(new BorderLayout(5, 5));
        salesPanel.setBorder(BorderFactory.createTitledBorder("Sales Performance Summary"));
        modelSalesReport = new DefaultTableModel(new String[]{"Sale ID", "Date", "Cashier", "Total (R)"}, 0);
        tblSalesReport = new JTable(modelSalesReport);
        salesPanel.add(new JScrollPane(tblSalesReport), BorderLayout.CENTER);

        // Expiry Report Panel
        JPanel expiryPanel = new JPanel(new BorderLayout(5, 5));
        expiryPanel.setBorder(BorderFactory.createTitledBorder("Medicines Expiring Within 1 Month"));
        modelExpiryReport = new DefaultTableModel(new String[]{"ID", "Medicine Name", "Stock", "Expiry Date"}, 0);
        tblExpiryReport = new JTable(modelExpiryReport);
        expiryPanel.add(new JScrollPane(tblExpiryReport), BorderLayout.CENTER);

        panel.add(salesPanel);
        panel.add(expiryPanel);

        return panel;
    }

    // -------------------------------------------------------------------------
    // DATA LOADING HELPERS
    // -------------------------------------------------------------------------
    private void loadAllData() {
        loadMedicines();
        loadSuppliers();
        loadUsers();
        loadReports();
    }

    private void loadMedicines() {
        modelMedicines.setRowCount(0);
        List<Medicine> list = medicineDAO.getAllMedicines();
        for (Medicine m : list) {
            modelMedicines.addRow(new Object[]{
                m.getMedicineId(), m.getName(), m.getCompany(), m.getMedicineType(),
                String.format("%.2f", m.getPrice()), m.getQuantityInStock(), m.getReorderLevel(),
                m.getExpiryDate(), m.getSupplierId() > 0 ? m.getSupplierId() : "N/A"
            });
        }
    }

    private void loadSuppliers() {
        modelSuppliers.setRowCount(0);
        List<Supplier> list = supplierDAO.getAllSuppliers();
        for (Supplier s : list) {
            modelSuppliers.addRow(new Object[]{
                s.getSupplierId(), s.getName(), s.getContactPerson(), s.getPhone(), s.getEmail(), s.getAddress()
            });
        }
    }

    private void loadUsers() {
        modelUsers.setRowCount(0);
        List<User> list = userDAO.getAllUsers();
        for (User u : list) {
            modelUsers.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getRole(), u.getFullName()});
        }
    }

    private void loadReports() {
        modelSalesReport.setRowCount(0);
        List<Object[]> sales = reportDAO.getSalesReport();
        for (Object[] row : sales) {
            modelSalesReport.addRow(row);
        }

        modelExpiryReport.setRowCount(0);
        List<Medicine> expiring = reportDAO.getExpiringMedicines();
        for (Medicine m : expiring) {
            modelExpiryReport.addRow(new Object[]{m.getMedicineId(), m.getName(), m.getQuantityInStock(), m.getExpiryDate()});
        }
    }

    // -------------------------------------------------------------------------
    // ACTION HANDLERS
    // -------------------------------------------------------------------------
    private void addMedicine() {
        try {
            Medicine m = new Medicine(0,
                    txtMedName.getText().trim(),
                    txtCompany.getText().trim(),
                    comboMedType.getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    Integer.parseInt(txtReorder.getText().trim()),
                    Date.valueOf(txtExpiry.getText().trim()),
                    txtMedSupplierId.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtMedSupplierId.getText().trim()));

            if (medicineDAO.addMedicine(m)) {
                JOptionPane.showMessageDialog(this, "Medicine added successfully!");
                loadMedicines();
                clearMedicineForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid form input! Format date as YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMedicine() {
        int selectedRow = tblMedicines.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine from the table first.");
            return;
        }

        int id = (int) modelMedicines.getValueAt(selectedRow, 0);
        try {
            Medicine m = new Medicine(id,
                    txtMedName.getText().trim(),
                    txtCompany.getText().trim(),
                    comboMedType.getSelectedItem().toString(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    Integer.parseInt(txtReorder.getText().trim()),
                    Date.valueOf(txtExpiry.getText().trim()),
                    txtMedSupplierId.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtMedSupplierId.getText().trim()));

            if (medicineDAO.updateMedicine(m)) {
                JOptionPane.showMessageDialog(this, "Medicine updated successfully!");
                loadMedicines();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid form input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int selectedRow = tblMedicines.getSelectedRow();
        if (selectedRow != -1 && medicineDAO.deleteMedicine((int) modelMedicines.getValueAt(selectedRow, 0))) {
            JOptionPane.showMessageDialog(this, "Medicine deleted!");
            loadMedicines();
        }
    }

    private void clearMedicineForm() {
        txtMedName.setText("");
        txtCompany.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        txtReorder.setText("");
        txtExpiry.setText("YYYY-MM-DD");
        txtMedSupplierId.setText("");
    }

    private void addSupplier() {
        Supplier s = new Supplier(0,
                txtSupName.getText().trim(),
                txtContact.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim());

        if (supplierDAO.addSupplier(s)) {
            JOptionPane.showMessageDialog(this, "Supplier added!");
            loadSuppliers();
        }
    }

    private void updateSupplier() {
        int r = tblSuppliers.getSelectedRow();
        if (r != -1) {
            Supplier s = new Supplier((int) modelSuppliers.getValueAt(r, 0),
                    txtSupName.getText().trim(),
                    txtContact.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtAddress.getText().trim());

            if (supplierDAO.updateSupplier(s)) {
                JOptionPane.showMessageDialog(this, "Supplier updated!");
                loadSuppliers();
            }
        }
    }

    private void deleteSupplier() {
        int r = tblSuppliers.getSelectedRow();
        if (r != -1 && supplierDAO.deleteSupplier((int) modelSuppliers.getValueAt(r, 0))) {
            JOptionPane.showMessageDialog(this, "Supplier deleted!");
            loadSuppliers();
        }
    }

    private void addUser() {
        if (userDAO.addUser(txtUsername.getText().trim(), txtPassword.getText().trim(), comboRole.getSelectedItem().toString(), txtFullName.getText().trim())) {
            JOptionPane.showMessageDialog(this, "User created successfully!");
            loadUsers();
        }
    }

    private void deleteUser() {
        int r = tblUsers.getSelectedRow();
        if (r != -1 && userDAO.deleteUser((int) modelUsers.getValueAt(r, 0))) {
            JOptionPane.showMessageDialog(this, "User deleted!");
            loadUsers();
        }
    }
}