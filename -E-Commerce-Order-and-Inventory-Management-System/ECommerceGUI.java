import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ECommerceGUI extends JFrame {
    // Data storage
    private java.util.List<User> users = new ArrayList<>();
    private java.util.List<Product> products = new ArrayList<>();
    private java.util.List<Order> orders = new ArrayList<>();
    private Map<String, Cart> customerCarts = new HashMap<>();
    
    // Managers
    private ProductManager productManager;
    
    // Current user
    private User currentUser;
    private Cart currentCart;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private JPanel loginPanel, productsPanel, cartPanel, ordersPanel, adminPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    
    // Tables
    private JTable productsTable, cartTable, ordersTable, adminOrdersTable;
    private DefaultTableModel productsTableModel, cartTableModel, ordersTableModel, adminOrdersTableModel;
    
    // User management
    private int userCounter = 2;
    
    public ECommerceGUI() {
        this.productManager = new ProductManager();
        initializeSampleData();
        setupGUI();
        showLoginScreen();
    }
    
    private void initializeSampleData() {
        // Create sample users
        users.add(new User("admin1", "admin", "admin123", "Admin User", "admin"));
        users.add(new User("cust1", "john", "password", "John Customer", "customer"));
        
        // Start user counter after sample users
        userCounter = 2;
    }
    
    private void setupGUI() {
        setTitle("E-Commerce Order and Inventory Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        createHeaderPanel();
        
        tabbedPane = new JTabbedPane();
        createLoginPanel();
        createProductsPanel();
        createCartPanel();
        createOrdersPanel();
        createAdminPanel();
        
        add(tabbedPane, BorderLayout.CENTER);
        updateUIBasedOnRole();
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        welcomeLabel = new JLabel("Welcome! Please login.");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        logoutButton = new JButton("Logout");
        logoutButton.setVisible(false);
        
        logoutButton.addActionListener(e -> {
            currentUser = null;
            currentCart = null;
            updateUIBasedOnRole();
            updateWelcomeMessage();
            tabbedPane.setSelectedIndex(0);
            //JOptionPane.showMessageDialog(this, "Logged out successfully!");
        });
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("E-Commerce System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Login section
        JLabel loginTitle = new JLabel("Login", JLabel.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        
        JButton loginButton = new JButton("Login");
        
        // Registration section
        JLabel regTitle = new JLabel("New User Registration", JLabel.CENTER);
        regTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel regUserLabel = new JLabel("New Username:");
        JTextField regUserField = new JTextField(15);
        
        JLabel regPassLabel = new JLabel("New Password:");
        JPasswordField regPassField = new JPasswordField(15);
        
        JLabel regNameLabel = new JLabel("Full Name:");
        JTextField regNameField = new JTextField(15);
        
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");
        
        // Layout
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        loginPanel.add(loginTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(userField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        loginPanel.add(passField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        loginPanel.add(loginButton, gbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(separator, gbc);
        
        // Registration Section
        gbc.gridy = 6;
        loginPanel.add(regTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 7;
        loginPanel.add(regUserLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        loginPanel.add(regUserField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        loginPanel.add(regPassLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 8;
        loginPanel.add(regPassField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9;
        loginPanel.add(regNameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 9;
        loginPanel.add(regNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 10;
        loginPanel.add(registerButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 10;
        loginPanel.add(exitButton, gbc);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            User user = authenticate(username, password);
            if (user != null) {
                currentUser = user;
                /*JOptionPane.showMessageDialog(this, 
                    "Login successful! Welcome " + user.getName() + 
                    " (" + ("customer".equals(user.getRole()) ? "Customer" : "Administrator") + ")");*/
                updateUIBasedOnRole();
                updateWelcomeMessage();
                userField.setText("");
                passField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Registration button action
        registerButton.addActionListener(e -> {
            String username = regUserField.getText().trim();
            String password = new String(regPassField.getPassword());
            String name = regNameField.getText().trim();
            
            if (registerNewUser(username, password, name)) {
                regUserField.setText("");
                regPassField.setText("");
                regNameField.setText("");
            }
        });
        
        exitButton.addActionListener(e -> System.exit(0));
        
        tabbedPane.addTab("Login", loginPanel);
    }
    
    private void createProductsPanel() {
        productsPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columns = {"ID", "Name", "Category", "Price", "Stock"};
        productsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable = new JTable(productsTableModel);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Enhanced control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh Products");
        JButton addToCartButton = new JButton("Add to Cart");
        
        // Sorting buttons
        JButton sortAscButton = new JButton("Sort Price ↑");
        JButton sortDescButton = new JButton("Sort Price ↓");
        JButton searchButton = new JButton("Search");
        JButton filterCategoryButton = new JButton("Filter by Category");
        
        controlPanel.add(refreshButton);
        controlPanel.add(addToCartButton);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        controlPanel.add(sortAscButton);
        controlPanel.add(sortDescButton);
        controlPanel.add(searchButton);
        controlPanel.add(filterCategoryButton);
        
        productsPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
        productsPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshButton.addActionListener(e -> refreshProductsTable());
        addToCartButton.addActionListener(e -> addSelectedToCart());
        sortAscButton.addActionListener(e -> sortProductsByPrice(true));
        sortDescButton.addActionListener(e -> sortProductsByPrice(false));
        searchButton.addActionListener(e -> searchProducts());
        filterCategoryButton.addActionListener(e -> filterByCategory());
        
        tabbedPane.addTab("Products", productsPanel);
        
        // Initial load
        refreshProductsTable();
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
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        controlPanel.add(refreshCartButton);
        controlPanel.add(removeItemButton);
        controlPanel.add(clearCartButton);
        controlPanel.add(checkoutButton);
        controlPanel.add(totalLabel);
        
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        cartPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshCartButton.addActionListener(e -> refreshCartTable(totalLabel));
        removeItemButton.addActionListener(e -> removeSelectedFromCart(totalLabel));
        clearCartButton.addActionListener(e -> clearCart(totalLabel));
        checkoutButton.addActionListener(e -> checkout(totalLabel));
        
        tabbedPane.addTab("Shopping Cart", cartPanel);
    }
    
    private void createOrdersPanel() {
        ordersPanel = new JPanel(new BorderLayout());
        
        // Table setup
        String[] columns = {"Order ID", "Items", "Total", "Status", "Date"};
        ordersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        
        JButton refreshOrdersButton = new JButton("Refresh Orders");
        ordersPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        ordersPanel.add(refreshOrdersButton, BorderLayout.SOUTH);
        
        refreshOrdersButton.addActionListener(e -> refreshOrdersTable());
        
        tabbedPane.addTab("My Orders", ordersPanel);
    }
    
    private void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane for admin sections
        JTabbedPane adminTabs = new JTabbedPane();
        
        // 1. Product Management Tab
        JPanel productMgmtPanel = createProductManagementPanel();
        adminTabs.addTab("Product Management", productMgmtPanel);
        
        // 2. Order Management Tab
        JPanel orderMgmtPanel = createOrderManagementPanel();
        adminTabs.addTab("Order Management", orderMgmtPanel);
        
        // 3. Reports Tab
        JPanel reportsPanel = createReportsPanel();
        adminTabs.addTab("Reports", reportsPanel);
        
        adminPanel.add(adminTabs, BorderLayout.CENTER);
        tabbedPane.addTab("Admin", adminPanel);
    }
    
    private JPanel createProductManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel for Add/Update
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Form"));
        
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
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addProductButton = new JButton("Add Product");
        JButton updateProductButton = new JButton("Update Product");
        JButton deleteProductButton = new JButton("Delete Product");
        JButton clearFormButton = new JButton("Clear Form");
        
        buttonPanel.add(addProductButton);
        buttonPanel.add(updateProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(clearFormButton);
        
        // Products table for admin
        String[] adminColumns = {"ID", "Name", "Category", "Price", "Stock"};
        DefaultTableModel adminTableModel = new DefaultTableModel(adminColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable adminTable = new JTable(adminTableModel);
        
        JButton refreshAdminButton = new JButton("Refresh Table");
        
        JPanel adminTablePanel = new JPanel(new BorderLayout());
        adminTablePanel.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        adminTablePanel.add(refreshAdminButton, BorderLayout.SOUTH);
        
        // Main admin layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(adminTablePanel, BorderLayout.CENTER);
        
        // Button actions
        addProductButton.addActionListener(e -> addProductFromForm(idField, nameField, categoryField, priceField, quantityField, adminTableModel));
        updateProductButton.addActionListener(e -> updateProductFromForm(idField, nameField, categoryField, priceField, quantityField, adminTableModel));
        deleteProductButton.addActionListener(e -> deleteProductFromForm(idField, adminTableModel));
        clearFormButton.addActionListener(e -> clearFields(idField, nameField, categoryField, priceField, quantityField));
        refreshAdminButton.addActionListener(e -> refreshAdminTable(adminTableModel));
        
        // Table selection listener
        adminTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && adminTable.getSelectedRow() != -1) {
                int selectedRow = adminTable.getSelectedRow();
                idField.setText(adminTableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(adminTableModel.getValueAt(selectedRow, 1).toString());
                categoryField.setText(adminTableModel.getValueAt(selectedRow, 2).toString());
                priceField.setText(adminTableModel.getValueAt(selectedRow, 3).toString().replace("$", ""));
                quantityField.setText(adminTableModel.getValueAt(selectedRow, 4).toString());
            }
        });
        
        // Initial load
        refreshAdminTable(adminTableModel);
        
        return panel;
    }
    
    private JPanel createOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Orders table
        String[] columns = {"Order ID", "Customer", "Items", "Total", "Status", "Date"};
        adminOrdersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        adminOrdersTable = new JTable(adminOrdersTableModel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh Orders");
        JButton markProcessedButton = new JButton("Mark as Processed");
        JButton markShippedButton = new JButton("Mark as Shipped");
        JButton markDeliveredButton = new JButton("Mark as Delivered");
        JButton viewDetailsButton = new JButton("View Details");
        
        controlPanel.add(refreshButton);
        controlPanel.add(markProcessedButton);
        controlPanel.add(markShippedButton);
        controlPanel.add(markDeliveredButton);
        controlPanel.add(viewDetailsButton);
        
        panel.add(new JScrollPane(adminOrdersTable), BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshButton.addActionListener(e -> refreshAdminOrdersTable());
        markProcessedButton.addActionListener(e -> updateOrderStatus("PROCESSED"));
        markShippedButton.addActionListener(e -> updateOrderStatus("SHIPPED"));
        markDeliveredButton.addActionListener(e -> updateOrderStatus("DELIVERED"));
        viewDetailsButton.addActionListener(e -> viewOrderDetails());
        
        // Initial load
        refreshAdminOrdersTable();
        
        return panel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JButton generateButton = new JButton("Generate Reports");
        generateButton.addActionListener(e -> generateReports(reportArea));
        
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(generateButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ========== ADMIN METHODS ==========
    
    private void refreshAdminOrdersTable() {
        adminOrdersTableModel.setRowCount(0);
        for (Order order : orders) {
            User customer = getUserById(order.getCustomerId());
            String customerName = customer != null ? customer.getName() : "Unknown";
            
            adminOrdersTableModel.addRow(new Object[]{
                order.getOrderId(),
                customerName,
                order.getItems().size() + " items",
                String.format("$%.2f", order.getTotalPrice()),
                order.getStatus(),
                order.getTimestamp()
            });
        }
    }
    
    private void updateOrderStatus(String status) {
        int selectedRow = adminOrdersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first!");
            return;
        }
        
        String orderId = (String) adminOrdersTableModel.getValueAt(selectedRow, 0);
        Order order = getOrderById(orderId);
        
        if (order != null) {
            order.setStatus(status);
            refreshAdminOrdersTable();
            //JOptionPane.showMessageDialog(this, "Order status updated to: " + status);
        }
    }
    
    private void viewOrderDetails() {
        int selectedRow = adminOrdersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first!");
            return;
        }
        
        String orderId = (String) adminOrdersTableModel.getValueAt(selectedRow, 0);
        Order order = getOrderById(orderId);
        
        if (order != null) {
            StringBuilder details = new StringBuilder();
            details.append("Order ID: ").append(order.getOrderId()).append("\n");
            details.append("Customer: ").append(getUserById(order.getCustomerId()).getName()).append("\n");
            details.append("Status: ").append(order.getStatus()).append("\n");
            details.append("Date: ").append(order.getTimestamp()).append("\n");
            details.append("Total: $").append(String.format("%.2f", order.getTotalPrice())).append("\n\n");
            details.append("Items:\n");
            
            for (Cart.CartItem item : order.getItems()) {
                details.append(String.format("  - %s (Qty: %d) @ $%.2f = $%.2f\n",
                    item.getProductName(), item.getQuantity(), item.getPrice(), item.getTotalPrice()));
            }
            
            JOptionPane.showMessageDialog(this, details.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void generateReports(JTextArea reportArea) {
        StringBuilder report = new StringBuilder();
        report.append("========== ADMIN REPORTS ==========\n\n");
        
        // 1. Out of Stock Products
        report.append("1. OUT OF STOCK PRODUCTS:\n");
        List<Product> outOfStock = productManager.getOutOfStockProducts();
        if (outOfStock.isEmpty()) {
            report.append("   No products out of stock.\n");
        } else {
            for (Product p : outOfStock) {
                report.append(String.format("   - %s (ID: %s)\n", p.getName(), p.getId()));
            }
        }
        report.append("\n");
        
        // 2. Total Orders
        report.append("2. TOTAL ORDERS PLACED: ").append(orders.size()).append("\n\n");
        
        // 3. Orders by Status
        report.append("3. ORDERS BY STATUS:\n");
        Map<String, Integer> statusCount = new HashMap<>();
        for (Order order : orders) {
            statusCount.put(order.getStatus(), statusCount.getOrDefault(order.getStatus(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            report.append(String.format("   - %s: %d\n", entry.getKey(), entry.getValue()));
        }
        report.append("\n");
        
        // 4. Most Frequently Ordered Products (using frequency map)
        report.append("4. MOST FREQUENTLY ORDERED PRODUCTS:\n");
        Map<String, Integer> productFrequency = new HashMap<>();
        
        for (Order order : orders) {
            for (Cart.CartItem item : order.getItems()) {
                String productName = item.getProductName();
                productFrequency.put(productName, productFrequency.getOrDefault(productName, 0) + item.getQuantity());
            }
        }
        
        if (productFrequency.isEmpty()) {
            report.append("   No orders placed yet.\n");
        } else {
            // Sort by frequency
            List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productFrequency.entrySet());
            sortedProducts.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedProducts) {
                if (count >= 5) break; // Show top 5
                report.append(String.format("   %d. %s - %d units sold\n", ++count, entry.getKey(), entry.getValue()));
            }
        }
        report.append("\n");
        
        // 5. Total Revenue
        double totalRevenue = 0;
        for (Order order : orders) {
            totalRevenue += order.getTotalPrice();
        }
        report.append(String.format("5. TOTAL REVENUE: $%.2f\n\n", totalRevenue));
        
        // 6. Low Stock Alert
        report.append("6. LOW STOCK ALERT (≤5 items):\n");
        List<Product> lowStock = productManager.getLowStockProducts(5);
        if (lowStock.isEmpty()) {
            report.append("   No low stock products.\n");
        } else {
            for (Product p : lowStock) {
                report.append(String.format("   - %s: %d units remaining\n", p.getName(), p.getQuantity()));
            }
        }
        report.append("\n");
        
        report.append("========== END OF REPORTS ==========\n");
        
        reportArea.setText(report.toString());
        reportArea.setCaretPosition(0);
    }
    
    private Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }
    
    private User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    // ========== PRODUCT SORTING & FILTERING ==========
    
    private void sortProductsByPrice(boolean ascending) {
        try {
            List<Product> sortedProducts;
            if (ascending) {
                sortedProducts = productManager.getProductsSortedByPriceAsc();
            } else {
                sortedProducts = productManager.getProductsSortedByPriceDesc();
            }
            
            refreshProductsTableWithList(sortedProducts);
            
            String order = ascending ? "lowest to highest" : "highest to lowest";
            /*JOptionPane.showMessageDialog(this, 
                "Products sorted by price (" + order + ") using Heap sort!\n" +
                "Found " + sortedProducts.size() + " products.",
                "Sort Complete", JOptionPane.INFORMATION_MESSAGE);*/
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error during sorting: " + e.getMessage(), 
                "Sort Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchProducts() {
        String query = JOptionPane.showInputDialog(this, "Enter product name to search:");
        if (query != null && !query.trim().isEmpty()) {
            List<Product> results = productManager.searchProductsByName(query.trim());
            refreshProductsTableWithList(results);
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No products found matching: '" + query + "'", 
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Found " + results.size() + " product(s) matching: '" + query + "'", 
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void filterByCategory() {
        List<String> categories = productManager.getAllCategories();
        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No categories available!");
            return;
        }
        
        String selectedCategory = (String) JOptionPane.showInputDialog(this,
            "Select category to filter:",
            "Filter by Category",
            JOptionPane.QUESTION_MESSAGE,
            null,
            categories.toArray(),
            categories.get(0));
        
        if (selectedCategory != null) {
            List<Product> results = productManager.getProductsByCategory(selectedCategory);
            refreshProductsTableWithList(results);
            
            JOptionPane.showMessageDialog(this, 
                "Showing " + results.size() + " product(s) in category: " + selectedCategory,
                "Filter Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refreshProductsTableWithList(List<Product> productList) {
        productsTableModel.setRowCount(0);
        for (Product product : productList) {
            productsTableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getQuantity()
            });
        }
    }
    
    // ========== CART METHODS ==========
    
    private void addSelectedToCart() {
        if (!canAddToCart()) {
            return;
        }
        
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first!");
            return;
        }
        
        String productId = (String) productsTableModel.getValueAt(selectedRow, 0);
        Product product = productManager.getProductById(productId);
        
        if (product != null) {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity for " + product.getName() + ":");
            if (quantityStr != null) {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity > 0 && quantity <= product.getQuantity()) {
                        currentCart.addItem(product, quantity);
                        JOptionPane.showMessageDialog(this, "Added to cart successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid quantity or insufficient stock!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number!");
                }
            }
        }
    }
    
    private void removeSelectedFromCart(JLabel totalLabel) {
        if (!canAccessCart()) {
            return;
        }
        
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!");
            return;
        }
        
        String productName = (String) cartTableModel.getValueAt(selectedRow, 0);
        Product product = findProductByName(productName);
        if (product != null) {
            currentCart.removeItem(product.getId());
            refreshCartTable(totalLabel);
            JOptionPane.showMessageDialog(this, "Item removed from cart!");
        }
    }
    
    private void clearCart(JLabel totalLabel) {
        if (!canAccessCart()) {
            return;
        }
        
        if (currentCart != null && !currentCart.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to clear the cart?", 
                "Confirm Clear Cart", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                currentCart.clear();
                refreshCartTable(totalLabel);
                JOptionPane.showMessageDialog(this, "Cart cleared!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cart is already empty!");
        }
    }
    
    private void checkout(JLabel totalLabel) {
        if (!canAccessCart()) {
            return;
        }
        
        if (currentCart == null || currentCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Proceed with checkout? Total: $" + String.format("%.2f", currentCart.calculateSubtotal()), 
            "Confirm Checkout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String orderId = "ORD" + System.currentTimeMillis();
            Order order = new Order(orderId, currentUser.getId(), currentCart.getItems(), currentCart.calculateSubtotal());
            orders.add(order);
            currentCart.clear();
            
            refreshCartTable(totalLabel);
            refreshOrdersTable();
            
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\nOrder ID: " + orderId + "\nTotal: $" + String.format("%.2f", order.getTotalPrice()));
        }
    }
    
    private void refreshCartTable(JLabel totalLabel) {
        cartTableModel.setRowCount(0);
        
        if (currentCart != null && currentUser != null && "customer".equals(currentUser.getRole())) {
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
        } else {
            if (totalLabel != null) {
                totalLabel.setText("Total: $0.00");
            }
        }
    }
    
    // ========== ORDER METHODS ==========
    
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
    
    // ========== ADMIN PRODUCT METHODS ==========
    
    private void addProductFromForm(JTextField idField, JTextField nameField, JTextField categoryField, 
                                   JTextField priceField, JTextField quantityField, DefaultTableModel adminTableModel) {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            
            if (id.isEmpty() || name.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Price and quantity cannot be negative!");
                return;
            }
            
            Product product = new Product(id, name, category, price, quantity);
            try {
                productManager.addProduct(product);
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearFields(idField, nameField, categoryField, priceField, quantityField);
                refreshAdminTable(adminTableModel);
                refreshProductsTable();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Product ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProductFromForm(JTextField idField, JTextField nameField, JTextField categoryField, 
                                      JTextField priceField, JTextField quantityField, DefaultTableModel adminTableModel) {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            
            if (id.isEmpty() || name.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            // Find the existing product and update its properties directly.
            Product product = productManager.getProductById(id);
            if (product != null) {
                // Update fields on the Product object (assumes Product has setters).
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                product.setQuantity(quantity);
                
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                refreshAdminTable(adminTableModel);
                refreshProductsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProductFromForm(JTextField idField, DefaultTableModel adminTableModel) {
        String id = idField.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product ID to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete product " + id + "?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = productManager.removeProduct(id);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearFields(idField);
                refreshAdminTable(adminTableModel);
                refreshProductsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshAdminTable(DefaultTableModel adminTableModel) {
        adminTableModel.setRowCount(0);
        for (Product product : productManager.getAllProducts()) {
            adminTableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getQuantity()
            });
        }
    }
    
    // ========== UTILITY METHODS ==========
    
    private void refreshProductsTable() {
        refreshProductsTableWithList(productManager.getAllProducts());
    }
    
    private Product findProductByName(String name) {
        for (Product product : productManager.getAllProducts()) {
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
    
    private User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                if ("customer".equals(user.getRole())) {
                    currentCart = customerCarts.getOrDefault(user.getId(), new Cart(user.getId()));
                    customerCarts.put(user.getId(), currentCart);
                } else {
                    currentCart = null;
                }
                return user;
            }
        }
        return null;
    }
    
    private boolean registerNewUser(String username, String password, String name) {
        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (username.length() < 3 || !username.matches("^[a-zA-Z0-9_]+$")) {
            JOptionPane.showMessageDialog(this, 
                "Username must be at least 3 characters long and contain only letters, numbers, and underscores!", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 4 characters long!", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, 
                "Username already exists! Please choose a different username.", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String newUserId = "user" + (++userCounter);
        User newUser = new User(newUserId, username, password, name, "customer");
        users.add(newUser);
        
        customerCarts.put(newUserId, new Cart(newUserId));
        
        JOptionPane.showMessageDialog(this, 
            "Registration successful!\nYou can now login with your new account.", 
            "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        
        return true;
    }
    
    private boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    private void updateUIBasedOnRole() {
        if (currentUser == null) {
            // Disable all tabs except login
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setEnabledAt(i, i == 0);
            }
            tabbedPane.setSelectedIndex(0);
        } else {
            // Enable tabs based on role
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                String tabTitle = tabbedPane.getTitleAt(i);
                
                if ("Login".equals(tabTitle)) {
                    tabbedPane.setEnabledAt(i, true);
                } else if ("Admin".equals(tabTitle)) {
                    // Only admins can access admin panel
                    tabbedPane.setEnabledAt(i, "admin".equals(currentUser.getRole()));
                } else if ("Shopping Cart".equals(tabTitle) || "My Orders".equals(tabTitle)) {
                    // Only customers can access cart and orders
                    tabbedPane.setEnabledAt(i, "customer".equals(currentUser.getRole()));
                } else {
                    // Products tab accessible to all logged-in users
                    tabbedPane.setEnabledAt(i, true);
                }
            }
            
            // Navigate to appropriate tab
            if ("admin".equals(currentUser.getRole())) {
                tabbedPane.setSelectedIndex(4); // Admin tab
            } else {
                tabbedPane.setSelectedIndex(1); // Products tab
            }
            
            refreshProductsTable();
            if ("customer".equals(currentUser.getRole())) {
                refreshOrdersTable();
            }
        }
    }
    
    private void updateWelcomeMessage() {
        if (currentUser != null) {
            String roleDisplay = "customer".equals(currentUser.getRole()) ? "Customer" : "Administrator";
            welcomeLabel.setText("Welcome, " + currentUser.getName() + " (" + roleDisplay + ")");
            logoutButton.setVisible(true);
        } else {
            welcomeLabel.setText("Welcome! Please login.");
            logoutButton.setVisible(false);
        }
    }
    
    private boolean canAccessCart() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first!", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!"customer".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, 
                "Admin users cannot access shopping cart functionality!\nPlease login as a customer to shop.", 
                "Access Denied", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean canAddToCart() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login as a customer to add items to cart!", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!"customer".equals(currentUser.getRole())) {
            JOptionPane.showMessageDialog(this, 
                "Admin users cannot add items to cart!\nPlease login as a customer to shop.", 
                "Access Denied", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void showLoginScreen() {
        setVisible(true);
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ECommerceGUI();
        });
    }
}