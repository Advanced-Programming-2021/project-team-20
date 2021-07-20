package project.model;

import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

public class Auction {
    private String auctionCreatorName;
    private int price;
    private String bestBuyerName;
    private static int auctionCode = 1;
//    private static HashMap<String, Integer>
    private static String addressOfStorage = "Resourses\\Server\\";

    public Auction(String auctionCreatorName, int initialPrice) {
        this.auctionCreatorName = auctionCreatorName;
        this.price = initialPrice;
        this.bestBuyerName = "";

            try {
                FileWriter fileWriter = new FileWriter(
                    addressOfStorage + "Auctions\\" + auctionCode + ".json");
                fileWriter.write(toGsonFormat(this));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
    }

    private String toGsonFormat(Auction auction) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
        jsonObject.addProperty("price", auction.getPrice());
        return jsonObject.toString();
    }

    public String getAuctionCreatorName() {
        return this.auctionCreatorName;
    }

    public String getBestBuyerName(){
        return this.bestBuyerName;
    }

    public int getPrice(){
        return this.price;
    }

    public void setNewSuggestion(String username, int newPrice) {
        this.bestBuyerName = username;
        this.price = newPrice;
    }
}
