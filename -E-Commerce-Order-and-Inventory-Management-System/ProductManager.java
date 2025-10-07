import java.util.*;

public class ProductManager {
    private List<Product> products;
    
    public ProductManager() {
        this.products = new ArrayList<>();
        initializeSampleProducts();
    }
    
    private void initializeSampleProducts() {
        products.add(new Product("p1", "Laptop", "Electronics", 999.99, 10));
        products.add(new Product("p2", "Smartphone", "Electronics", 699.99, 25));
        products.add(new Product("p3", "Book", "Education", 29.99, 100));
    }
    
    // Basic CRUD operations
    public void addProduct(Product product) {
        products.add(product);
    }
    
    public boolean removeProduct(String productId) {
        return products.removeIf(p -> p.getId().equals(productId));
    }
    
    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    // Simple sorting by price
    public List<Product> getProductsSortedByPrice() {
        List<Product> sorted = new ArrayList<>(products);
        Collections.sort(sorted, new Comparator<Product>() {
            public int compare(Product p1, Product p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });
        return sorted;
    }
    
    // Basic search
    public List<Product> searchProductsByName(String name) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }
}