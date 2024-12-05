//For percesion as API returns numerous decimal points
import java.math.BigDecimal;
//Allows for specified behavior of decimal points (e.g. always round up or down)
import java.math.RoundingMode;
//Use of different data structures
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
//User input
import java.util.Scanner;
//Error handaling
import java.io.IOException;

public class PortfolioManager {
    //list of all crypto assets
    private ArrayList<CryptoAsset> assets;
    //Map Crytpo Asset IDs to real-time prices
    private HashMap<String, BigDecimal> realTimePrices;
    //Get record of transactions as they were put in
    private Queue<Transaction> transactionHistory;

    public PortfolioManager() {
        //empty list of assets
        assets = new ArrayList<>();
        //empty map of real-time prices
        realTimePrices = new HashMap<>();
        //empty queue of transactions
        transactionHistory = new LinkedList<>();
    }

    public void updatePrices() {
        //empty list of asset IDs
        List<String> assetIds = new ArrayList<>();
        //loops through all assets ands adds to list
        for (CryptoAsset asset : assets) {
            assetIds.add(asset.getId());
        }

        //try used to handle error/exceptions
        try {
            //creates instance of API class to get access to real-time prices program
            ApiCall apiCall = new ApiCall();
            //Calls fetchRealTimePrices object within the ApiCall class to get real-time prices bassed on Asset ID
            realTimePrices = apiCall.fetchRealTimePrices(assetIds);
            //Used to debug the system as previously API call didin't wprk:
            //System.out.println("Updated Prices: " + realTimePrices); 
        } 
        //Error hanndling
        catch (IOException | InterruptedException e) {
            //standard error stream + exception description for a more indepth error understanding (System.err.println + e.getMessage())
            System.err.println("Error fetching real-time prices: " + e.getMessage());
        }
    }

    //Method to add asset to the portfolio
    public void addAsset(CryptoAsset asset) {
        assets.add(asset);
    }
    //Method to add transaction to transaction log
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    //Method to update the price of an asset
    public void updatePrice(String assetName, BigDecimal price) {
        realTimePrices.put(assetName, price);
    }

    //Method to calculate the profit and loss of the portfolio and each asset performance 
    public void calculateProfitLoss() {
        //initalize the total profit to zero
        BigDecimal totalProfitLoss = BigDecimal.ZERO;

        //iterates through all the assets in the protfolio
        for (CryptoAsset asset : assets) {
            //gets the current price of the asset, quantity, and pruchase price
            BigDecimal purchasePrice = asset.getPurchasePrice();
            double quantity = asset.getQuantity();
            BigDecimal currentPrice = realTimePrices.get(asset.getId());

            //checks to see if the real time price of the asset was fetched 
            if (currentPrice != null) {
                //gets the intial investment by multiplying the purhcase price by the quantity
                BigDecimal initialInvestment = purchasePrice.multiply(BigDecimal.valueOf(quantity));
                //gets the current value by multiplying the current price by the quantity
                BigDecimal currentValue = currentPrice.multiply(BigDecimal.valueOf(quantity));
                //gets the profit or loss by subtracting the initial investment from the current value
                BigDecimal profitLoss = currentValue.subtract(initialInvestment);

                //adds the result of the profit and loss and adds it to the total profit and loss of the portfolio
                totalProfitLoss = totalProfitLoss.add(profitLoss);

                //prints the profit and loss for the asset
                System.out.printf("Asset: %s, Profit/Loss: %s USD\n", asset.getId(), roundValue(profitLoss));
            }
        }

        //prints the total profit and loss of the portfolio
        System.out.printf("Total Portfolio Profit/Loss: %s USD\n", roundValue(totalProfitLoss));
    }

    //method to find total value of the portfolio
    public void calculateTotalValue() {
        //initalize to zero
        BigDecimal totalValue = BigDecimal.ZERO;

        //iterates through all assets in portfolio
        for (CryptoAsset asset : assets) {
            //Gets the current price of the asset 
            BigDecimal currentPrice = realTimePrices.get(asset.getId());

            //Checks to see if the current price is not null (was retrieved)
            if (currentPrice != null) {
                //gets the quanity of asset
                double quantity = asset.getQuantity();
                //multiplies the current price to quanity, getting the total current value of that asset
                BigDecimal currentValue = currentPrice.multiply(BigDecimal.valueOf(quantity));
                //that total current value is added to teh total value of the portfolio
                totalValue = totalValue.add(currentValue);
            }
        }
        //displays the total value of the portfolio
        System.out.printf("Total Portfolio Value: %s USD\n", roundValue(totalValue));
    }

    private String roundValue(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }

    //method to display transaction history
    public void displayTransactionHistory() {
        //checks if there any transactions
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        System.out.println("Recent Transactions:");
        //loops through the transactions, getting the date, whethere it was a buy or sell, the asset id, quantity, and price when added
        for (Transaction txn : transactionHistory) {
            System.out.printf("Date: %s, Type: %s, Asset: %s, Quantity: %.4f, Price: %s USD\n",
                txn.getDate(),
                txn.getType(),
                txn.getAssetName(),
                txn.getQuantity(),
                txn.getPrice().setScale(2, RoundingMode.HALF_UP).toString());
        }
    }

    //Method Helping with UI
    public void addAssetInteractive() {
        Scanner scanner = new Scanner(System.in);
        
        //gets the user reponse for the asset id
        System.out.print("Enter the CoinGecko ID of the asset (e.g., bitcoin): ");
        String id = scanner.nextLine().toLowerCase();

        //gets the user response for the purchase price
        System.out.print("Enter purchase price (USD): ");
        BigDecimal purchasePrice = scanner.nextBigDecimal();

        //gets the user response for the quantity of the asset 
        System.out.print("Enter quantity: ");
        double quantity = scanner.nextDouble();

        //adds the asset to the portfolio and the corresponding purchase price and quantity
        CryptoAsset asset = new CryptoAsset(id, purchasePrice, quantity);
        addAsset(asset);

        System.out.println("Asset added successfully.");
    }

    
}