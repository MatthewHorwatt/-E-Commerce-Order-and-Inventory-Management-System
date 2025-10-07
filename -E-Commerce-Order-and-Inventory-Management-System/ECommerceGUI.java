import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*; // For List, ArrayList, Map, HashMap
import java.util.List; // Explicit import to avoid ambiguity

public class ECommerceGUI extends JFrame {
    // Data storage - using explicit java.util.List
    private java.util.List<User> users = new ArrayList<>();
    private java.util.List<Product> products = new ArrayList<>();
    private java.util.List<Order> orders = new ArrayList<>();
    private Map<String, Cart> customerCarts = new HashMap<>();
    
    // Current user
    private User currentUser;
    private Cart currentCart;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private JPanel loginPanel, productsPanel, cartPanel, ordersPanel, adminPanel;
    
    // Tables
    private JTable productsTable, cartTable, ordersTable;
    private DefaultTableModel productsTableModel, cartTableModel, ordersTableModel;
    
    public ECommerceGUI() {
        initializeSampleData();
        setupGUI();
        showLoginScreen();
    }
    
    private void initializeSampleData() {
        // Create sample users
        users.add(new User("admin1", "admin", "admin123", "Admin User", "admin"));
        users.add(new User("cust1", "john", "password", "John Customer", "customer"));
        
        // Create sample products
        products.add(new Product("p1", "Laptop", "Electronics", 999.99, 10));
        products.add(new Product("p2", "Smartphone", "Electronics", 699.99, 25));
        products.add(new Product("p3", "Book", "Education", 29.99, 100));
        products.add(new Product("p4", "Headphones", "Electronics", 149.99, 15));
    }
    
    private void setupGUI() {
        setTitle("E-Commerce Order and Inventory Manager - Module 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        createLoginPanel();
        createProductsPanel();
        createCartPanel();
        createOrdersPanel();
        createAdminPanel();
        
        add(tabbedPane);
        updateUIBasedOnRole();
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("E-Commerce System Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");
        
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(userField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(passField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        loginPanel.add(exitButton, gbc);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            User user = authenticate(username, password);
            if (user != null) {
                currentUser = user;
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user.getName());
                updateUIBasedOnRole();
                userField.setText("");
                passField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        exitButton.addActionListener(e -> System.exit(0));
        
        tabbedPane.addTab("Login", loginPanel);
    }
    
    private void createProductsPanel() {
        productsPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columns = {"ID", "Name", "Category", "Price", "Stock"};
        productsTableModel = new DefaultTableModel(columns, 0);
        productsTable = new JTable(productsTableModel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh Products");
        JButton addToCartButton = new JButton("Add to Cart");
        
        controlPanel.add(refreshButton);
        controlPanel.add(addToCartButton);
        
        productsPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
        productsPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshButton.addActionListener(e -> refreshProductsTable());
        addToCartButton.addActionListener(e -> addSelectedToCart());
        
        tabbedPane.addTab("Products", productsPanel);
    }
    
    private void createCartPanel() {
        cartPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columns = {"Product", "Quantity", "Price", "Total"};
        cartTableModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartTableModel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton refreshCartButton = new JButton("Refresh Cart");
        JButton removeItemButton = new JButton("Remove Item");
        JButton clearCartButton = new JButton("Clear Cart");
        JButton checkoutButton = new JButton("Checkout");
        
        JLabel totalLabel = new JLabel("Total: $0.00");
        
        controlPanel.add(refreshCartButton);
        controlPanel.add(removeItemButton);
        controlPanel.add(clearCartButton);
        controlPanel.add(checkoutButton);
        controlPanel.add(totalLabel);
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        cartPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshCartButton.addActionListener(e -> refreshCartTable(totalLabel));
        removeItemButton.addActionListener(e -> removeSelectedFromCart());
        clearCartButton.addActionListener(e -> clearCart());
        checkoutButton.addActionListener(e -> checkout());
        
        tabbedPane.addTab("Shopping Cart", cartPanel);
    }
    
    private void createOrdersPanel() {
        ordersPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columns = {"Order ID", "Items", "Total", "Status", "Date"};
        ordersTableModel = new DefaultTableModel(columns, 0);
        ordersTable = new JTable(ordersTableModel);
        
        JButton refreshOrdersButton = new JButton("Refresh Orders");
        ordersPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        ordersPanel.add(refreshOrdersButton, BorderLayout.SOUTH);
        
        refreshOrdersButton.addActionListener(e -> refreshOrdersTable());
        
        tabbedPane.addTab("My Orders", ordersPanel);
    }
    
    private void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        
        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        
        JButton addProductButton = new JButton("Add Product");
        JButton viewProductsButton = new JButton("View All Products");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(viewProductsButton);
        
        adminPanel.add(formPanel, BorderLayout.NORTH);
        adminPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Button actions
        addProductButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                
                if (id.isEmpty() || name.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!");
                    return;
                }
                
                Product product = new Product(id, name, category, price, quantity);
                products.add(product);
                
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearFields(idField, nameField, categoryField, priceField, quantityField);
                refreshProductsTable();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        viewProductsButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("All Products:\n\n");
            for (Product p : products) {
                sb.append(p.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "All Products", JOptionPane.INFORMATION_MESSAGE);
        });
        
        tabbedPane.addTab("Admin", adminPanel);
    }
    
    // Utility methods
    private User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                // Initialize cart for customer
                if ("customer".equals(user.getRole())) {
                    currentCart = customerCarts.getOrDefault(user.getId(), new Cart(user.getId()));
                    customerCarts.put(user.getId(), currentCart);
                }
                return user;
            }
        }
        return null;
    }
    
    private void updateUIBasedOnRole() {
        if (currentUser == null) {
            // Show only login tab
            for (int i = 1; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setEnabledAt(i, false);
            }
            tabbedPane.setSelectedIndex(0);
        } else {
            // Enable tabs based on role
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setEnabledAt(i, true);
            }
            
            if ("admin".equals(currentUser.getRole())) {
                tabbedPane.setSelectedIndex(4); // Admin tab
            } else {
                tabbedPane.setSelectedIndex(1); // Products tab
            }
            
            refreshProductsTable();
            refreshCartTable(null);
            refreshOrdersTable();
        }
    }
    
    private void refreshProductsTable() {
        productsTableModel.setRowCount(0);
        for (Product product : products) {
            productsTableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getQuantity()
            });
        }
    }
    
    private void refreshCartTable(JLabel totalLabel) {
        cartTableModel.setRowCount(0);
        if (currentCart != null) {
            for (Cart.CartItem item : currentCart.getItems()) {
                cartTableModel.addRow(new Object[]{
                    item.getProductName(),
                    item.getQuantity(),
                    String.format("$%.2f", item.getPrice()),
                    String.format("$%.2f", item.getTotalPrice())
                });
            }
            if (totalLabel != null) {
                totalLabel.setText(String.format("Total: $%.2f", currentCart.calculateSubtotal()));
            }
        }
    }
    
    private void refreshOrdersTable() {
        ordersTableModel.setRowCount(0);
        if (currentUser != null) {
            for (Order order : orders) {
                if (order.getCustomerId().equals(currentUser.getId())) {
                    ordersTableModel.addRow(new Object[]{
                        order.getOrderId(),
                        order.getItems().size() + " items",
                        String.format("$%.2f", order.getTotalPrice()),
                        order.getStatus(),
                        order.getTimestamp()
                    });
                }
            }
        }
    }
    
    private void addSelectedToCart() {
        if (currentUser == null || !"customer".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, "Please login as customer to add items to cart!");
            return;
        }
        
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first!");
            return;
        }
        
        String productId = (String) productsTableModel.getValueAt(selectedRow, 0);
        Product product = findProductById(productId);
        
        if (product != null) {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity for " + product.getName() + ":");
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0 && quantity <= product.getQuantity()) {
                    currentCart.addItem(product, quantity);
                    refreshCartTable(null);
                    JOptionPane.showMessageDialog(this, "Added to cart successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid quantity or insufficient stock!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!");
            }
        }
    }
    
    private void removeSelectedFromCart() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!");
            return;
        }
        
        String productName = (String) cartTableModel.getValueAt(selectedRow, 0);
        currentCart.removeItem(findProductByName(productName).getId());
        refreshCartTable(null);
        JOptionPane.showMessageDialog(this, "Item removed from cart!");
    }
    
    private void clearCart() {
        if (currentCart != null) {
            currentCart.clear();
            refreshCartTable(null);
            JOptionPane.showMessageDialog(this, "Cart cleared!");
        }
    }
    
    private void checkout() {
        if (currentCart == null || currentCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Proceed with checkout? Total: $" + currentCart.calculateSubtotal(), 
            "Confirm Checkout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String orderId = "ORD" + System.currentTimeMillis();
            Order order = new Order(orderId, currentUser.getId(), currentCart.getItems(), currentCart.calculateSubtotal());
            orders.add(order);
            currentCart.clear();
            
            refreshCartTable(null);
            refreshOrdersTable();
            
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\nOrder ID: " + orderId + "\nTotal: $" + order.getTotalPrice());
        }
    }
    
    private Product findProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
    
    private Product findProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
    
    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
    
    private void showLoginScreen() {
        setVisible(true);
    }
}