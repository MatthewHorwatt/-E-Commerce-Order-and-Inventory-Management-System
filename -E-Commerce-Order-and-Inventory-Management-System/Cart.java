import java.util.*;

public class Cart {
    private String customerId;
    private List<CartItem> items;
    
    public Cart(String customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }
    
    // Inner class for cart items
    public static class CartItem {
        private String productId;
        private String productName;
        private int quantity;
        private double price;
        
        public CartItem(String productId, String productName, int quantity, double price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }
        
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotalPrice() { return quantity * price; }
        
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        @Override
        public String toString() {
            return String.format("%s (Qty: %d, $%.2f each)", productName, quantity, price);
        }
    }
    
    public void addItem(Product product, int quantity) {
        // Check if item already exists
        for (CartItem item : items) {
            if (item.getProductId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Add new item
        items.add(new CartItem(product.getId(), product.getName(), quantity, product.getPrice()));
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }
    
    public void updateQuantity(String productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }
    
    public double calculateSubtotal() {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }
    
    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public String getCustomerId() { return customerId; }
    public boolean isEmpty() { return items.isEmpty(); }
    public void clear() { items.clear(); }
    
    @Override
    public String toString() {
        return String.format("Cart for %s: %d items, Total: $%.2f", customerId, items.size(), calculateSubtotal());
    }
}
