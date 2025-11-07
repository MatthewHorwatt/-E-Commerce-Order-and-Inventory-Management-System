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
        // Prevent adding product with duplicate ID
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                throw new IllegalArgumentException("Product with ID already exists: " + product.getId());
            }
        }
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
        Collections.sort(sorted, Comparator.comparingDouble(Product::getPrice));
        return sorted;
    }

    public List<Product> getProductsSortedByPriceAsc() {
        return getProductsSortedByPrice();
    }

    public List<Product> getProductsSortedByPriceDesc() {
        List<Product> sorted = getProductsSortedByPrice();
        Collections.reverse(sorted);
        return sorted;
    }

    public List<Product> getOutOfStockProducts() {
        List<Product> out = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() <= 0) out.add(p);
        }
        return out;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> low = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() <= threshold) low.add(p);
        }
        return low;
    }

    public List<String> getAllCategories() {
        Set<String> cats = new LinkedHashSet<>();
        for (Product p : products) {
            if (p.getCategory() != null && !p.getCategory().isEmpty()) cats.add(p.getCategory());
        }
        return new ArrayList<>(cats);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory() != null && p.getCategory().equals(category)) results.add(p);
        }
        return results;
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