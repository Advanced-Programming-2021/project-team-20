package project.client;

import com.google.gson.*;

import project.client.view.LoginController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static String toGsonFormatGetInformation(String whichClass) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", whichClass);
        jsonObject.addProperty("token", LoginController.getToken());
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

    public static String toGsonFormatForChangeCardsBetweentTowRounds() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "changeCardsBetweenTwoRounds");
        jsonObject.addProperty("", "");
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

}
