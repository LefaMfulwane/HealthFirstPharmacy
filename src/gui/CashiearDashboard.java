package gui;

import dao.MedicineDAO;
import dao.SaleDAO;
import model.CartItem;
import model.Medicine;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class CashiearDashboard extends JFrame {

    private User currentUser;
    private MedicineDAO medicineDAO;
    private SaleDAO saleDAO;

    private JTextField txtSearch;
    private JTable tblMedicines;
    private DefaultTableModel modelMedicines;

    private JTable tblCart;
    private DefaultTableModel modelCart;
    private JSpinner spinnerQty;
    private JLabel lblTotal;

    private List<Medicine> currentMedicineList;
    private List<CartItem> cartList;
    private double grandTotal = 0.0;

    public CashiearDashboard(User user) {
        this.currentUser = user;
        this.medicineDAO = new MedicineDAO();
        this.saleDAO = new SaleDAO();
        this.cartList = new ArrayList<>();

        initComponents();
        loadMedicineData("");
    }

    private void initComponents() {
        setTitle("HealthFirst Pharmacy - Point of Sale (Cashier: " + currentUser.getFullName() + ")");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 153));
        JLabel lblTitle = new JLabel(" HealthFirst Point of Sale", JLabel.LEFT);
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

        // Center Split View (Left: Stock Table, Right: Cart Table)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createStockPanel(), createCartPanel());
        splitPane.setDividerLocation(550);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Stock Check & Selection"));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Reset");

        btnSearch.addActionListener(e -> loadMedicineData(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadMedicineData("");
        });

        searchPanel.add(new JLabel("Find Medicine:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Stock Table
        String[] cols = {"ID", "Name", "Type", "Price (R)", "In Stock", "Expiry Date"};
        modelMedicines = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMedicines = new JTable(modelMedicines);
        panel.add(new JScrollPane(tblMedicines), BorderLayout.CENTER);

        // Add to Cart Controls
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        spinnerQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddToCart.setBackground(new Color(40, 167, 69));
        btnAddToCart.setForeground(Color.WHITE);

        btnAddToCart.addActionListener(e -> addToCart());

        addPanel.add(new JLabel("Quantity:"));
        addPanel.add(spinnerQty);
        addPanel.add(btnAddToCart);
        panel.add(addPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Cart"));

        // Cart Table
        String[] cols = {"Item Name", "Price", "Qty", "Subtotal (R)"};
        modelCart = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCart = new JTable(modelCart);
        panel.add(new JScrollPane(tblCart), BorderLayout.CENTER);

        // Total & Checkout Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        lblTotal = new JLabel("Total: R 0.00 ", JLabel.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotal.setForeground(new Color(180, 0, 0));
        bottomPanel.add(lblTotal, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRemove = new JButton("Remove Item");
        JButton btnClear = new JButton("Clear Cart");
        JButton btnCheckout = new JButton("Process Sale & Bill");
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(0, 102, 153));
        btnCheckout.setForeground(Color.WHITE);

        btnRemove.addActionListener(e -> removeFromCart());
        btnClear.addActionListener(e -> clearCart());
        btnCheckout.addActionListener(e -> processCheckout());

        btnPanel.add(btnRemove);
        btnPanel.add(btnClear);
        btnPanel.add(btnCheckout);

        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void loadMedicineData(String keyword) {
        modelMedicines.setRowCount(0);
        currentMedicineList = keyword.isEmpty() ? medicineDAO.getAllMedicines() : medicineDAO.searchMedicines(keyword);

        for (Medicine m : currentMedicineList) {
            modelMedicines.addRow(new Object[]{
                m.getMedicineId(),
                m.getName(),
                m.getMedicineType(),
                String.format("%.2f", m.getPrice()),
                m.getQuantityInStock(),
                m.getExpiryDate()
            });
        }
    }

    private void addToCart() {
        int selectedRow = tblMedicines.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine from the stock table first.", "Selection Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicine med = currentMedicineList.get(selectedRow);
        int qty = (int) spinnerQty.getValue();

        if (qty > med.getQuantityInStock()) {
            JOptionPane.showMessageDialog(this, "Requested quantity exceeds available stock (" + med.getQuantityInStock() + ").", "Stock Limit", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (CartItem item : cartList) {
            if (item.getMedicineId() == med.getMedicineId()) {
                if (item.getQuantity() + qty > med.getQuantityInStock()) {
                    JOptionPane.showMessageDialog(this, "Cannot add more items than available in stock.", "Stock Limit", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                item.setQuantity(item.getQuantity() + qty);
                refreshCartTable();
                return;
            }
        }

        cartList.add(new CartItem(med.getMedicineId(), med.getName(), med.getPrice(), qty));
        refreshCartTable();
    }

    private void removeFromCart() {
        int selectedRow = tblCart.getSelectedRow();
        if (selectedRow != -1) {
            cartList.remove(selectedRow);
            refreshCartTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item from the cart to remove.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearCart() {
        cartList.clear();
        refreshCartTable();
    }

    private void refreshCartTable() {
        modelCart.setRowCount(0);
        grandTotal = 0.0;

        for (CartItem item : cartList) {
            grandTotal += item.getSubtotal();
            modelCart.addRow(new Object[]{
                item.getName(),
                String.format("%.2f", item.getUnitPrice()),
                item.getQuantity(),
                String.format("%.2f", item.getSubtotal())
            });
        }
        lblTotal.setText(String.format("Total: R %.2f ", grandTotal));
    }

    private void processCheckout() {
        if (cartList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Process sale for Total: R %.2f?", grandTotal),
                "Confirm Transaction",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int saleId = saleDAO.processSale(currentUser.getUserId(), cartList, grandTotal);

            if (saleId != -1) {
                showBillDialog(saleId);
                clearCart();
                loadMedicineData(""); // Refresh stock levels after sale
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process sale in database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBillDialog(int saleId) {
        StringBuilder bill = new StringBuilder();
        bill.append("====================================\n");
        bill.append(" HEALTHFIRST PHARMACY RECEIPT \n");
        bill.append("====================================\n");
        bill.append("Sale ID: ").append(saleId).append("\n");
        bill.append("Processed By: ").append(currentUser.getFullName()).append("\n");
        bill.append("------------------------------------\n");
        bill.append(String.format("%-18s %-5s %-8s\n", "Item", "Qty", "Subtotal"));
        bill.append("------------------------------------\n");

        for (CartItem item : cartList) {
            bill.append(String.format("%-18s %-5d R%-8.2f\n", item.getName(), item.getQuantity(), item.getSubtotal()));
        }

        bill.append("------------------------------------\n");
        bill.append(String.format("TOTAL AMOUNT: R %.2f\n", grandTotal));
        bill.append("====================================\n");
        bill.append(" Thank you for your visit! \n");

        JTextArea txtBill = new JTextArea(bill.toString());
        txtBill.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtBill.setEditable(false);

        JScrollPane scroll = new JScrollPane(txtBill);
        scroll.setPreferredSize(new Dimension(380, 400));

        JOptionPane.showMessageDialog(this, scroll, "Customer Bill / Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
}
    



//replaced code (OLD)
/**
     private User currentUser;
    
    public CashiearDashboard(User user){
        this.currentUser = user;
        setTitle("Healthfirst Pharmacy - Point of sale (Cashier)");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel lblWelcome = new JLabel("Cashier POS Module - Logged in as: " + currentUser.getFullName(), JLabel.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblWelcome);
    } 
 * 
 */
