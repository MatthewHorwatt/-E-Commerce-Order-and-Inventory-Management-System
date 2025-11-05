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
    
    // Tables
    private JTable productsTable, cartTable, ordersTable;
    private DefaultTableModel productsTableModel, cartTableModel, ordersTableModel;
    
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
        setTitle("E-Commerce Order and Inventory Manager - Module 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
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
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setVisible(false);
        
        logoutButton.addActionListener(e -> {
            currentUser = null;
            currentCart = null;
            updateUIBasedOnRole();
            updateWelcomeMessage();
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
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
                JOptionPane.showMessageDialog(this, 
                    "Login successful! Welcome " + user.getName() + 
                    " (" + ("customer".equals(user.getRole()) ? "Customer" : "Administrator") + ")");
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
        
        // Module 2: Sorting buttons
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
        
        // Module 2: New button actions
        sortAscButton.addActionListener(e -> sortProductsByPrice(true));
        sortDescButton.addActionListener(e -> sortProductsByPrice(false));
        searchButton.addActionListener(e -> searchProducts());
        filterCategoryButton.addActionListener(e -> filterByCategory());
        
        // Hide "Add to Cart" button for admin users
        this.addPropertyChangeListener("currentUser", evt -> {
            if (currentUser != null) {
                addToCartButton.setVisible("customer".equals(currentUser.getRole()));
            } else {
                addToCartButton.setVisible(false);
            }
        });
        
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
        
        // Form panel for Add/Update
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
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
        JButton viewProductsButton = new JButton("View All Products");
        
        buttonPanel.add(addProductButton);
        buttonPanel.add(updateProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(clearFormButton);
        buttonPanel.add(viewProductsButton);
        
        // Products table for admin
        String[] adminColumns = {"ID", "Name", "Category", "Price", "Stock"};
        DefaultTableModel adminTableModel = new DefaultTableModel(adminColumns, 0);
        JTable adminTable = new JTable(adminTableModel);
        
        // Refresh admin table
        JButton refreshAdminButton = new JButton("Refresh Table");
        
        JPanel adminTablePanel = new JPanel(new BorderLayout());
        adminTablePanel.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        adminTablePanel.add(refreshAdminButton, BorderLayout.SOUTH);
        
        // Main admin layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        adminPanel.add(topPanel, BorderLayout.NORTH);
        adminPanel.add(adminTablePanel, BorderLayout.CENTER);
        
        // Button actions
        addProductButton.addActionListener(e -> addProductFromForm(idField, nameField, categoryField, priceField, quantityField, adminTableModel));
        updateProductButton.addActionListener(e -> updateProductFromForm(idField, nameField, categoryField, priceField, quantityField, adminTableModel));
        deleteProductButton.addActionListener(e -> deleteProductFromForm(idField, adminTableModel));
        clearFormButton.addActionListener(e -> clearFields(idField, nameField, categoryField, priceField, quantityField));
        viewProductsButton.addActionListener(e -> showAllProductsDialog());
        refreshAdminButton.addActionListener(e -> refreshAdminTable(adminTableModel));
        
        // Table selection listener to populate form
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
        
        tabbedPane.addTab("Admin", adminPanel);
        
        // Initial load of admin table
        refreshAdminTable(adminTableModel);
    }
    
    // ========== MODULE 2 METHODS ==========
    
    private void sortProductsByPrice(boolean ascending) {
        if (productManager == null) {
            JOptionPane.showMessageDialog(this, "Product manager not initialized!");
            return;
        }
        
        try {
            List<Product> sortedProducts;
            if (ascending) {
                sortedProducts = productManager.getProductsSortedByPriceAsc();
            } else {
                sortedProducts = productManager.getProductsSortedByPriceDesc();
            }
            
            refreshProductsTableWithList(sortedProducts);
            
            String order = ascending ? "lowest to highest" : "highest to lowest";
            JOptionPane.showMessageDialog(this, 
                "Products sorted by price (" + order + ") using Heap sort!\n" +
                "Found " + sortedProducts.size() + " products.",
                "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
                
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
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No products found in category: " + selectedCategory);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Showing " + results.size() + " product(s) in category: " + selectedCategory,
                    "Filter Results", JOptionPane.INFORMATION_MESSAGE);
            }
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
            refreshCartTable(null);
            JOptionPane.showMessageDialog(this, "Item removed from cart!");
        }
    }
    
    private void clearCart() {
        if (!canAccessCart()) {
            return;
        }
        
        if (currentCart != null) {
            currentCart.clear();
            refreshCartTable(null);
            JOptionPane.showMessageDialog(this, "Cart cleared!");
        }
    }
    
    private void checkout() {
        if (!canAccessCart()) {
            return;
        }
        
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
                totalLabel.setText("Cart: Admin Access Not Allowed");
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
            boolean success = productManager.addProduct(product);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearFields(idField, nameField, categoryField, priceField, quantityField);
                refreshAdminTable(adminTableModel);
                refreshProductsTable();
            } else {
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
            
            boolean success = productManager.updateProduct(id, name, category, price, quantity);
            
            if (success) {
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
    
    private void showAllProductsDialog() {
        StringBuilder sb = new StringBuilder("All Products:\n\n");
        for (Product p : productManager.getAllProducts()) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "All Products", JOptionPane.INFORMATION_MESSAGE);
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
            for (int i = 1; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setEnabledAt(i, false);
            }
            tabbedPane.setSelectedIndex(0);
        } else {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                String tabTitle = tabbedPane.getTitleAt(i);
                
                if ("Login".equals(tabTitle)) {
                    tabbedPane.setEnabledAt(i, true);
                } else if ("Admin".equals(tabTitle)) {
                    tabbedPane.setEnabledAt(i, "admin".equals(currentUser.getRole()));
                } else if ("Shopping Cart".equals(tabTitle)) {
                    tabbedPane.setEnabledAt(i, "customer".equals(currentUser.getRole()));
                } else {
                    tabbedPane.setEnabledAt(i, true);
                }
            }
            
            if ("admin".equals(currentUser.getRole())) {
                tabbedPane.setSelectedIndex(4);
            } else {
                tabbedPane.setSelectedIndex(1);
            }
            
            refreshProductsTable();
            refreshCartTable(null);
            refreshOrdersTable();
        }
    }
    
    private void updateWelcomeMessage() {
        if (currentUser != null) {
            String roleDisplay = "customer".equals(currentUser.getRole()) ? "Customer" : "Administrator";
            welcomeLabel.setText("Welcome, " + currentUser.getName() + " (" + roleDisplay + ")");
            
            Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JButton && "Logout".equals(((JButton)comp).getText())) {
                    comp.setVisible(true);
                    break;
                }
            }
        } else {
            welcomeLabel.setText("Welcome! Please login.");
            
            Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
            for (Component comp : components) {
                if (comp instanceof JButton && "Logout".equals(((JButton)comp).getText())) {
                    comp.setVisible(false);
                    break;
                }
            }
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
