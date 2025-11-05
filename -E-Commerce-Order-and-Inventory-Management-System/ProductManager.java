import java.util.*;

public class ProductManager {
    private List<Product> products;
    private ProductHeap minPriceHeap;
    private ProductHeap maxPriceHeap;
    
    public ProductManager() {
        this.products = new ArrayList<>();
        this.minPriceHeap = new ProductHeap(true);  // Min-heap for ascending sort
        this.maxPriceHeap = new ProductHeap(false); // Max-heap for descending sort
        initializeSampleProducts();
    }
    
    private void initializeSampleProducts() {
        addProduct(new Product("p1", "Laptop", "Electronics", 999.99, 10));
        addProduct(new Product("p2", "Smartphone", "Electronics", 699.99, 25));
        addProduct(new Product("p3", "Book", "Education", 29.99, 100));
        addProduct(new Product("p4", "Headphones", "Electronics", 149.99, 15));
        addProduct(new Product("p5", "Tablet", "Electronics", 499.99, 8));
    }
    
    // Enhanced CRUD operations
    public boolean addProduct(Product product) {
        // Check for duplicate ID
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                return false;
            }
        }
        
        products.add(product);
        minPriceHeap.add(product);
        maxPriceHeap.add(product);
        return true;
    }
    
    public boolean removeProduct(String productId) {
        Product toRemove = null;
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                toRemove = product;
                break;
            }
        }
        
        if (toRemove != null) {
            products.remove(toRemove);
            // Rebuild heaps (simplified approach)
            rebuildHeaps();
            return true;
        }
        return false;
    }
    
    public boolean updateProduct(String productId, String name, String category, double price, int quantity) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(name);
                product.setCategory(category);
                product.setPrice(price);
                product.setQuantity(quantity);
                
                // Rebuild heaps since price might have changed
                rebuildHeaps();
                return true;
            }
        }
        return false;
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
    
    // Advanced sorting using heaps
    public List<Product> getProductsSortedByPriceAsc() {
        return minPriceHeap.getAllSorted();
    }
    
    public List<Product> getProductsSortedByPriceDesc() {
        return maxPriceHeap.getAllSorted();
    }
    
    // Search functionality
    public List<Product> searchProductsByName(String name) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }
    
    public List<Product> getProductsByCategory(String category) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                results.add(product);
            }
        }
        return results;
    }
    
    // Inventory management
    public List<Product> getOutOfStockProducts() {
        List<Product> outOfStock = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() == 0) {
                outOfStock.add(product);
            }
        }
        return outOfStock;
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> lowStock = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() > 0 && product.getQuantity() <= threshold) {
                lowStock.add(product);
            }
        }
        return lowStock;
    }
    
    private void rebuildHeaps() {
        minPriceHeap = new ProductHeap(true);
        maxPriceHeap = new ProductHeap(false);
        
        for (Product product : products) {
            minPriceHeap.add(product);
            maxPriceHeap.add(product);
        }
    }
    
    public List<String> getAllCategories() {
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }
        return new ArrayList<>(categories);
    }
}
