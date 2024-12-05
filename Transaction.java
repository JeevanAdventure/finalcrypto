import java.util.Date;
import java.math.BigDecimal; 

public class Transaction {
    private String assetName;
    private BigDecimal price;
    private double quantity;
    private Date date;
    private String type; // (Buy/Sell)

    // Constructor
    public Transaction(String assetName, BigDecimal price, double quantity, Date date, String type) {
        this.assetName = assetName;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
        this.type = type;
    }

    // Getters
    public String getAssetName() {
        return assetName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

}