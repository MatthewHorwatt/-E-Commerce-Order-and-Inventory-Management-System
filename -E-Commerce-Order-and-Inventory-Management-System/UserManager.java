import java.util.*;

public class UserManager {
    private List<User> users;
    
    public UserManager() {
        this.users = new ArrayList<>();
        initializeSampleUsers();
    }
    
    private void initializeSampleUsers() {
        // Add sample admin and customer
        users.add(new User("admin1", "admin", "admin123", "Admin User", "admin"));
        users.add(new User("cust1", "john", "password", "John Doe", "customer"));
    }
    
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users); // Return copy
    }
    
    public void addUser(User user) {
        users.add(user);
    }
}