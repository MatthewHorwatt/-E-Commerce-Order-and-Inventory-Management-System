import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel with safe approach
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Just continue with default look and feel if there's an error
            System.out.println("Note: Using default look and feel");
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            new ECommerceGUI();
        });
    }
}
