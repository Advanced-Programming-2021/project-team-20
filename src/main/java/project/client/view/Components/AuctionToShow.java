package project.client.view.Components;

import javafx.beans.property.SimpleStringProperty;

public class AuctionToShow {

    private SimpleStringProperty auctionCode = new SimpleStringProperty();
    private SimpleStringProperty cardName = new SimpleStringProperty();
    private SimpleStringProperty auctionCreatorName = new SimpleStringProperty();
    private SimpleStringProperty bestBuyerName = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();
    private SimpleStringProperty isActivated = new SimpleStringProperty();


    public AuctionToShow(String auctionCode, String cardName, String auctionCreatorName, String bestBuyerName, String price, String isActivated) {
        setAuctionCode(auctionCode);
        setCardName(cardName);
        setAuctionCreatorName(auctionCreatorName);
        setBestBuyerName(bestBuyerName);
        setPrice(price);
        setIsActivated(isActivated);
    }



    public void setIsActivated(String isActivated) {
        this.isActivated.set(isActivated);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setBestBuyerName(String bestBuyerName) {
        this.bestBuyerName.set(bestBuyerName);
    }

    public void setAuctionCreatorName(String auctionCreatorName) {
        this.auctionCreatorName.set(auctionCreatorName);
    }

    public void setCardName(String cardName) {
        this.cardName.set(cardName);
    }

    public void setAuctionCode(String auctionCode) {
        this.auctionCode.set(auctionCode);
    }

    public String getAuctionCreatorName(){
        return this.auctionCreatorName.get();
    }

    public String getAuctionCode(){
        return this.auctionCode.get();
    }

    public String getCardName() {
        return this.cardName.get();
    }

    public String getBestBuyerName(){
        return this.bestBuyerName.get();
    }

    public String getPrice(){
        return this.price.get();
    }

    public String getIsActivated(){
        return this.isActivated.get();
    }


}
