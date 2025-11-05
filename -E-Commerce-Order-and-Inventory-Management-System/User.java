public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private String role;
    
    public User(String id, String username, String password, String name, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }
    
    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getRole() { return role; }
    
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return String.format("User{id=%s, name=%s, role=%s}", id, name, role);
    }
}
