import java.util.*;

public class Order {
    private String orderId;
    private String customerId;
    private List<Cart.CartItem> items;
    private double totalPrice;
    private Date timestamp;
    private String status; // "PENDING", "PROCESSED", "SHIPPED", "DELIVERED"
    
    public Order(String orderId, String customerId, List<Cart.CartItem> items, double totalPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items); // Create copy
        this.totalPrice = totalPrice;
        this.timestamp = new Date();
        this.status = "PENDING";
    }
    
    // Getters and setters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<Cart.CartItem> getItems() { return new ArrayList<>(items); }
    public double getTotalPrice() { return totalPrice; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Order #%s: %d items, Total: $%.2f, Status: %s", 
                           orderId, items.size(), totalPrice, status);
    }
}