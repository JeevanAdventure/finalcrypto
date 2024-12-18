//Packages imported by ChatGPT
/***************************************************************************************

//Title: Code generated by ChatGPT
//Author: OpenAI
//Date: 12/4/2024
//Code versi an: ChatGPT (Model 4)
//Availability: https://chat.openai.com/
***************************************************************************************/
//HTTP Requests
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//Decimal Precision
import java.math.BigDecimal;
//Managing varaying Data collections
import java.util.List;
import java.util.HashMap;
//Hamdling Input/Output exceptioins
import java.io.IOException;
//Parsing JSON responses from the API
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//End of ChatGPT Use section

public class ApiCall {
    private static final String API_URL = "https://api.coingecko.com/api/v3/simple/price";
    private String apiKey;

    public ApiCall() {
        //Holds the api key for authenticating when using the Coingecko API (not secure as it should be behind authentication or on the evm, but for this project it is fine))
        this.apiKey = "CG-TUeusUWZNGG88dT2ngYwXT78";

        //Checks to see if the API is null or empty as then the API Call wouldn't work
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new RuntimeException("CoinGecko API key not found.");
        }
    }
    //The method return type is a hashmap that contains the real-time prices of the assets (via beig decimal) and the asset ID 
    public HashMap<String, BigDecimal> fetchRealTimePrices(List<String> assetIds) throws IOException, InterruptedException {
        HashMap<String, BigDecimal> realTimePrices = new HashMap<>();

        //checks if Asset ID is null or empty as then the API Call would have no asset to fetch the price for
        if (assetIds == null || assetIds.isEmpty()) {
            System.out.println("No asset IDs provided.");
            return realTimePrices;
        }
//For section below to end of ApiCall Method:
        /***************************************************************************************

        //Title: Code generated by ChatGPT
        //Author: OpenAI
        //Date: 12/4/2024
        //Code version: ChatGPT (Model 4)
        //Availability: https://chat.openai.com/
        ***************************************************************************************/

    //https://docs.coingecko.com/reference/simple-price
    //https://chatgpt.com/
    //https://www.geeksforgeeks.org/bigdecimal-class-java/

        //Converts the list of Asset IDs seperated by commas 
        String idsParam = String.join(",", assetIds);
        //Creates the whole url that will aquire the real-time prices of the assets by appending the api url and the asset ids
        String url = API_URL + "?ids=" + idsParam + "&vs_currencies=usd";

        //initalizes the HTTP client for sending HTTP
        HttpClient client = HttpClient.newHttpClient();
        //creates an Http request with the headers + API key
        /***************************************************************************************

        Title: Coin Price by Ids
        Author: Unkown - CoinGecko API Documentation
        Date: 12/4/2024
        Code version: Not specified
        Availability: https://docs.coingecko.com/reference/simple-price
        ***************************************************************************************/
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("accept", "application/json")
            .header("x-cg-pro-api-key", this.apiKey)
            .GET()
            .build();
        //End of Coingecko API Use 
        
        //Sends the HTTP request to the API and gets the response as a String
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Checks to see if the reponse from the API call is a sucess (200), if so then it will parse the JSON response 
        if (response.statusCode() == 200) {
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        //Iterates through the Asset IDs and stores the price into the hasmap
            for (String assetId : assetIds) {
                JsonObject assetData = jsonObject.getAsJsonObject(assetId);
                if (assetData != null && assetData.has("usd")) 
                {
                    //gets the orice as a bigdecimal for precision
                    BigDecimal price = assetData.get("usd").getAsBigDecimal();
                    realTimePrices.put(assetId, price);
                    //Notifies if the price data for an asset is not found
                } else {
                    System.out.printf("Price data for %s not found.\n", assetId);
                }
            }
            //Erorr handling, notifies that the API call was not sucessful and the reasoning why (gets the response from the API fail)
        } else {
            System.err.println("Failed to fetch prices. HTTP Status Code: " + response.statusCode());
            System.err.println("Response Body: " + response.body());
        }
        // Returns the hashmap of the Asset IDs and their real-time prices
        return realTimePrices;
    }
}
