public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    
    public Product(String id, String name, String category, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    
    public boolean isInStock() {
        return quantity > 0;
    }
    
    @Override
    public String toString() {
        return String.format("Product{id=%s, name=%s, category=%s, price=$%.2f, stock=%d}", 
                           id, name, category, price, quantity);
    }
}