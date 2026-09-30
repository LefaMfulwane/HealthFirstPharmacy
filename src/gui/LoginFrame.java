package gui;

import dao.USerDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author admin
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private USerDAO userDAO;

    public LoginFrame() {
        userDAO = new USerDAO();
        initComponents();
    }

    private void initComponents() {

        setTitle("HealthFirst Pharmacy - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("HealthFirst Pharmacy System", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(new Color(0, 102, 153));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnLogin.addActionListener(e -> handleLogin());
        btnCancel.addActionListener(e -> System.exit(0));
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password.", "Input Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.authenticate(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getFullName() + " (" + user.getRole() + ")!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 

            if ("Admin".equalsIgnoreCase(user.getRole())) {
                new AdminDashboard(user).setVisible(true);
            } else if ("Cashier".equalsIgnoreCase(user.getRole())) {
                new CashiearDashboard(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}
    
    
    
    
    
    
    

