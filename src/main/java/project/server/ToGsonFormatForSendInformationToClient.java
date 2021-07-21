package project.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.Deck;
import project.model.User;
import project.server.controller.non_duel.storage.TweetStorage;

import java.util.*;

public class ToGsonFormatForSendInformationToClient {
    private static String successful = "Successful";
    private static String type = "type";
    private static String message = "message";

    public static String toGsonFormatForRegister(String type, String message, User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ToGsonFormatForSendInformationToClient.type, type);
        jsonObject.addProperty(ToGsonFormatForSendInformationToClient.message, message);
        if (type.equals(ServerController.getSuccessful())) {
            String token = UUID.randomUUID().toString();
            ServerController.getLoginedUsers().put(token, user);
            ServerController.refreshLastConnectionTime(token);
            jsonObject.addProperty("token", token);
            jsonObject.addProperty("userInformation", toGsonFormatUserInformation(user));
            jsonObject.addProperty("wholeDeck", toGsonFormatDecksAndCards(user));
        }
        return jsonObject.toString();
    }

    public static String toGsonFormatForLogin(String type, String message, User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ToGsonFormatForSendInformationToClient.type, type);
        jsonObject.addProperty(ToGsonFormatForSendInformationToClient.message, message);
        if (type.equals(ToGsonFormatForSendInformationToClient.successful)) {
            String token = UUID.randomUUID().toString();
            ServerController.getLoginedUsers().put(token, user);
            ServerController.refreshLastConnectionTime(token);
            jsonObject.addProperty("token", token);
            jsonObject.addProperty("userInformation", toGsonFormatUserInformation(user));
            jsonObject.addProperty("wholeDeck", toGsonFormatDecksAndCards(user));
        }
        return jsonObject.toString();
    }

    private static String toGsonFormatUserInformation(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("nickname", user.getNickname());
        jsonObject.addProperty("password", user.getPassword());
        // jsonObject.addProperty("imagePath", user.getImagePath());
        jsonObject.addProperty("score", user.getScore());
        jsonObject.addProperty("money", user.getMoney());
        return jsonObject.toString();
    }

    private static String toGsonFormatDecksAndCards(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        JsonArray allDecksJson = new JsonArray();

        if (allDecks != null) {
            for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
                JsonObject wholeDeckJson = new JsonObject();
                wholeDeckJson.addProperty("deckname", entry.getValue().getDeckname());
                wholeDeckJson.addProperty("isActivated", entry.getValue().getIsDeckActive());
                wholeDeckJson.add("mainDeck", new Gson().toJsonTree(entry.getValue().getMainDeck()).getAsJsonArray());
                wholeDeckJson.add("sideDeck", new Gson().toJsonTree(entry.getValue().getSideDeck()).getAsJsonArray());
                allDecksJson.add(wholeDeckJson);
            }
        }
        JsonObject wholeDecksOfUser = new JsonObject();
        wholeDecksOfUser.add("decks", allDecksJson);
        wholeDecksOfUser.add("uselessCards", new Gson().toJsonTree(user.getAllUselessCards()).getAsJsonArray());
        return wholeDecksOfUser.toString();
    }


    public static String toGsonFormatForGetInformationOfProfile(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Successful");
        jsonObject.addProperty("userName", user.getName());
        jsonObject.addProperty("nickName", user.getNickname());
        return jsonObject.toString();
    }

    public static String toGsonFormatForOnlyTypeAndMessage(String type, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("message", message);
        return jsonObject.toString();
    }

    public static String toGsonFormatForAcceptPlaying(String message) {

        return null;
    }

    public static String toGsonFormatForSendTweetsToClient(ArrayList<String> newTweets) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Successful");
        jsonObject.addProperty("message", "Tweet Sent Successfully!");
        jsonObject.add("newTweets", toJsonArrayTweets(newTweets));
        jsonObject.add("deletedTweetIds", toJsonArrayDeletedTweetIds(TweetStorage.getDeletedTweets()));
        jsonObject.addProperty("onlineUsers", ServerController.getLoginedUsers().size());
        return jsonObject.toString();
    }

    private static JsonArray toJsonArrayDeletedTweetIds(List<Integer> deletedTweetIds) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < deletedTweetIds.size(); i++) {
            jsonArray.add(deletedTweetIds.get(i));
        }
        return jsonArray;
    }

    private static JsonArray toJsonArrayTweets(ArrayList<String> newTweets) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < newTweets.size(); i++) {
            JsonParser jsonParser = new JsonParser();
            jsonArray.add(jsonParser.parse(newTweets.get(i)).getAsJsonObject());
        }
        return jsonArray;
    }

    public static String toGsonFormatForSentLastTweetsToClient(ArrayList<String> newTweets) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("newTweets", toJsonArrayTweets(newTweets));
        jsonObject.add("deletedTweetIds", toJsonArrayDeletedTweetIds(TweetStorage.getDeletedTweets()));
        jsonObject.addProperty("onlineUsers", ServerController.getLoginedUsers().size());
        return jsonObject.toString();
    }

    public static String toGsonFormatForBuyCard(String token, String cardNameForBuy) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", "shopBuy");
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("cardNameForBuy", cardNameForBuy);
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetCardPriceByCardName(String cardNameForBuy) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getCardPriceByCardName");
        jsonObject.addProperty("cardName", cardNameForBuy);
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetCardDescriptionByCardName(String cardNameForBuy) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getCardDescriptionByCardName");
        jsonObject.addProperty("cardName", cardNameForBuy);
        return jsonObject.toString();
    }

    public static String toGsonFormatForGetNumberOfBoughtCardsByCardName(String token, String cardNameForBuy) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "getNumberOfBoughtCardsByCardName");
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("cardName", cardNameForBuy);
        return jsonObject.toString();
    }

    public static String toGsonFormatForNumberOfBoughtCardsAndUselessCards(HashMap<String, Integer> numberOfCards) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uselessCards", numberOfCards.get("uselessCards"));
        jsonObject.addProperty("numberOfBoughtCards", numberOfCards.get("numberOfBoughtCards"));
        return jsonObject.toString();
    }

    public static String showInformationOfAdmin(String isAllowed, String numberOfCardsInShop) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isAllowed", isAllowed);
        jsonObject.addProperty("numberOfCardsInShop", numberOfCardsInShop);
        return jsonObject.toString();
    }
}
