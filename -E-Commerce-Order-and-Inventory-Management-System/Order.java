import java.util.*;

public class Order {
    private String orderId;
    private String customerId;
    private List<Cart.CartItem> items;
    private double subtotal;
    private double tax;
    private double totalPrice;
    private Date timestamp;
    private String status; // "PENDING", "PROCESSED", "SHIPPED", "DELIVERED"
    private String shippingAddress;
    
    // Tax service for tax calculation
    private static TaxService taxService = new TaxService();
    
    public Order(String orderId, String customerId, List<Cart.CartItem> items, 
                double subtotal, String shippingAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items); // Create copy
        this.subtotal = subtotal;
        this.shippingAddress = shippingAddress;
        this.tax = taxService.calculateTax(shippingAddress, subtotal);
        this.totalPrice = subtotal + tax;
        this.timestamp = new Date();
        this.status = "PENDING";
    }
    
    // Getters and setters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<Cart.CartItem> getItems() { return new ArrayList<>(items); }
    public double getSubtotal() { return subtotal; }
    public double getTax() { return tax; }
    public double getTotalPrice() { return totalPrice; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public String getShippingAddress() { return shippingAddress; }
    
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Order #%s: %d items, Subtotal: $%.2f, Tax: $%.2f, Total: $%.2f, Status: %s", 
                           orderId, items.size(), subtotal, tax, totalPrice, status);
    }
}
