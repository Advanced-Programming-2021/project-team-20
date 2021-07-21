package project.client;

import com.google.gson.JsonObject;
import project.client.view.LoginController;

import java.util.UUID;

public class ToGsonFormatToSendDataToServer {
    public static String toGsonFormatRegister(String userName, String nickName, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "register");
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("password", password);
        return jsonObject.toString();
    }

    public static String toGsonFormatLogin(String userName, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "login");
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("password", password);
        return jsonObject.toString();
    }

    public static String toGsonFormatChangePassword(String currentPassword, String newPassword) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "changePassword");
        jsonObject.addProperty("currentPassword", currentPassword);
        jsonObject.addProperty("newPassword", newPassword);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatWithOneRequest(String type, String requestName, String request) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty(requestName, request);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatToPlayWithComputer(int numberOfRounds) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "playWithComputer");
        jsonObject.addProperty("numberOfRounds", numberOfRounds + "");
        jsonObject.addProperty("computerToken", UUID.randomUUID().toString());
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatAddOrRemoveCardFromMainOrSideDeck(String type, String cardName, String deckName,
                                                                       boolean isMainDeck) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("deckName", deckName);
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("isMainDeck", isMainDeck);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatForChangeCardsBetweenTowRounds(String cardName, boolean isMainOrSideDeck, boolean isAddCard) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "changeCardsBetweenTwoRounds");
        jsonObject.addProperty("isAddCard", isAddCard);
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("isMainDeck", isMainOrSideDeck);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatGetScoreboardInformation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "scoreboard");
        return jsonObject.toString();
    }

    public static String toGsonFormatSendTweet(String tweet, int lastIdOfTweetReceived) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "sendTweet");
        jsonObject.addProperty("message", tweet);
        jsonObject.addProperty("lastIdOfTweetsReceived", lastIdOfTweetReceived + "");
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatToGetTweetsById(int lastIdOfTweetReceived) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getLastTweets");
        jsonObject.addProperty("lastIdOfTweetsReceived", lastIdOfTweetReceived + "");
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatGetScoreboardInformationOfONlineUsers() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "scoreboardOnline");
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetDataAllowCardAdminPanelShop(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "allowAdminPanelShop");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetDataDisallowCardAdminPanelShop(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "disallowAdminPanelShop");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetDataIncreaseCardAdminPanelShop(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "increaseAdminPanelShop");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetDataDecreaseCardAdminPanelShop(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "decreaseAdminPanelShop");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

    public static String toGsonFormatshowNumberOfBoughtCards(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "showNumberOfBoughtCards");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();

    }

    public static String toGsonFormatShowInformationOfAdmin(String cardName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "showInformationOfAdmin");
        jsonObject.addProperty("cardName", cardName);
        return jsonObject.toString();
    }

    public static String toGsonFormatAuction(String token, String cardName, int initialPrice) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "createAuction");
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("initialPrice", initialPrice);
        return jsonObject.toString();
    }

    public static String toGsonFormatRefreshAuction() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "refreshAuction");
        return jsonObject.toString();
    }

    public static String getInformationOfAuction(String auctionCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getInformationOfAuction");
        jsonObject.addProperty("token", LoginController.getToken());
        jsonObject.addProperty("auctionCode", auctionCode);
        return jsonObject.toString();
    }

    public static String sendBuyRequest(String token, String auctionCode, int price) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "sendBuyRequestForAuction");
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("auctionCode", auctionCode);
        jsonObject.addProperty("price", price);
        return jsonObject.toString();
    }

    public static String getInformationOfAuctionAsBuyer(String auctionCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getInformationOfAuctionAsBuyer");
        jsonObject.addProperty("token", LoginController.getToken());
        jsonObject.addProperty("auctionCode", auctionCode);
        return jsonObject.toString();
    }
}
