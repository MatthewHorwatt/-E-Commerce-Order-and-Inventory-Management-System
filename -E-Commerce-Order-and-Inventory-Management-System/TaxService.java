import java.util.HashMap;
import java.util.Map;

public class TaxService {
    private Map<String, Double> stateTaxRates;
    
    public TaxService() {
        stateTaxRates = new HashMap<>();
        initializeTaxRates();
    }
    
    private void initializeTaxRates() {
        // Realistic state tax rates (simplified)
        stateTaxRates.put("california", 0.0725);
        stateTaxRates.put("new york", 0.08875);
        stateTaxRates.put("texas", 0.0625);
        stateTaxRates.put("florida", 0.06);
        stateTaxRates.put("washington", 0.065);
        stateTaxRates.put("colorado", 0.029);
        stateTaxRates.put("illinois", 0.0625);
        stateTaxRates.put("pennsylvania", 0.06);
        stateTaxRates.put("arizona", 0.056);
        stateTaxRates.put("georgia", 0.04);
        // Add more states as needed
    }
    
    public double calculateTax(String address, double subtotal) {
        if (address == null || address.trim().isEmpty()) {
            return subtotal * 0.06; // Default tax
        }
        
        String addressLower = address.toLowerCase();
        
        // Try to detect state from address
        for (String state : stateTaxRates.keySet()) {
            if (addressLower.contains(state)) {
                return subtotal * stateTaxRates.get(state);
            }
        }
        
        // Check for state abbreviations
        Map<String, String> stateAbbreviations = new HashMap<>();
        stateAbbreviations.put("ca", "california");
        stateAbbreviations.put("ny", "new york");
        stateAbbreviations.put("tx", "texas");
        stateAbbreviations.put("fl", "florida");
        stateAbbreviations.put("wa", "washington");
        stateAbbreviations.put("co", "colorado");
        stateAbbreviations.put("il", "illinois");
        stateAbbreviations.put("pa", "pennsylvania");
        stateAbbreviations.put("az", "arizona");
        stateAbbreviations.put("ga", "georgia");
        
        for (String abbrev : stateAbbreviations.keySet()) {
            if (addressLower.matches(".*\\b" + abbrev + "\\b.*")) {
                String fullState = stateAbbreviations.get(abbrev);
                return subtotal * stateTaxRates.get(fullState);
            }
        }
        
        return subtotal * 0.06; // Default tax
    }
    
    public String getTaxRateDescription(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "Default tax rate: 6%";
        }
        
        String addressLower = address.toLowerCase();
        
        for (String state : stateTaxRates.keySet()) {
            if (addressLower.contains(state)) {
                double rate = stateTaxRates.get(state) * 100;
                return String.format("%s tax rate: %.2f%%", capitalize(state), rate);
            }
        }
        
        // Check abbreviations
        Map<String, String> stateAbbreviations = new HashMap<>();
        stateAbbreviations.put("ca", "california");
        stateAbbreviations.put("ny", "new york");
        stateAbbreviations.put("tx", "texas");
        stateAbbreviations.put("fl", "florida");
        stateAbbreviations.put("wa", "washington");
        stateAbbreviations.put("co", "colorado");
        stateAbbreviations.put("il", "illinois");
        stateAbbreviations.put("pa", "pennsylvania");
        stateAbbreviations.put("az", "arizona");
        stateAbbreviations.put("ga", "georgia");
        
        for (String abbrev : stateAbbreviations.keySet()) {
            if (addressLower.matches(".*\\b" + abbrev + "\\b.*")) {
                String fullState = stateAbbreviations.get(abbrev);
                double rate = stateTaxRates.get(fullState) * 100;
                return String.format("%s tax rate: %.2f%%", capitalize(fullState), rate);
            }
        }
        
        return "Default tax rate: 6%";
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
