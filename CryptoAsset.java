import java.math.BigDecimal; 
public class CryptoAsset {
    private String id; // CoinGecko ID (https://docs.coingecko.com/reference/coins-list)
    private BigDecimal purchasePrice;
    private double quantity;

    //Comstructors
    public CryptoAsset(String id, BigDecimal purchasePrice, double quantity) {
        this.id = id;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
    }

    //Getters
    public String getId() {
        return id;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public double getQuantity() {
        return quantity;
    }
}
