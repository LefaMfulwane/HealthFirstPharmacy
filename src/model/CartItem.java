package model;

/**
 *
 * @author admin
 */
public class CartItem {
    
    private int medicineId;
    private String name;
    private double unitPrice;
    private int quantity;
    
    public CartItem(int medicineId, String name, double unitPrice, int quantity) { 
        this.medicineId = medicineId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
    
    // Getters &; Setters 
    public int getMedicineId() { return medicineId; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; } 
    public void setQuantity(int quantity) { this.quantity = quantity; } 
    public double getSubtotal() { return unitPrice * quantity; }
    
    
    
    
}
