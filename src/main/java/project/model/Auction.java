package project.model;

public class Auction {
    private String auctionCreatorName;
    private int price;
    private String bestBuyerName;
//    private static HashMap<String, Integer>
    private static String addressOfStorage = "Resourses\\Server\\";

    public Auction(String auctionCreatorName, int initialPrice) {
        this.auctionCreatorName = auctionCreatorName;
        this.price = initialPrice;
        this.bestBuyerName = "";
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
