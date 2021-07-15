package project.server.controller.non_duel.tweets;

import com.google.gson.JsonObject;
import project.model.User;
import project.server.ServerController;
import project.server.controller.non_duel.storage.TweetStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class TweetController {
    public synchronized static String sendTweet(JsonObject details) {
        String token = "";
        String message = "";
        int lastIdOfTweetReceived;
        try {
            token = details.get("token").getAsString();
            message = details.get("message").getAsString();
            lastIdOfTweetReceived = details.get("lastIdOfTweetsReceived").getAsInt();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        details.addProperty("author", user.getName());
        HashMap<String, String> hashMapTweet = new HashMap<>();
        hashMapTweet.put("author", user.getName());
        hashMapTweet.put("message", message);
        TweetStorage.sendTweet(hashMapTweet);

//        return ToGsonFormatForSendInformationToClient.toGsonFormatForSendTweetsToClient(getLastTweets(lastIdOfTweetReceived));
        return null;
    }

    private static ArrayList<String> getLastTweets(int lastIdOfTweetReceived) {
        ArrayList<String> newMessages = new ArrayList<>();
        for (int i = lastIdOfTweetReceived; i < TweetStorage.getAllTweets().size(); i++) {
//            newMessages.add(TweetStorage.getAllTweets().get(i));
        }
        return newMessages;
    }

    public static String getLastTweets(JsonObject details) {
        String token = "";
        int lastIdOfTweets;
        try {
            token = details.get("token").getAsString();
            lastIdOfTweets = details.get("lastIdOfTweetsReceived").getAsInt();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
//        return ToGsonFormatForSendInformationToClient.toGsonFormatForSentLastTweetsToClient(getLastTweets(lastIdOfTweets));
        return null;
    }
}
