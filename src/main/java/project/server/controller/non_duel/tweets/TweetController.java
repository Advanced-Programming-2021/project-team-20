package project.server.controller.non_duel.tweets;

import com.google.gson.*;

import project.model.User;
import project.server.ServerController;

public class TweetController {
    public static String getTweet(JsonObject details) {
        String token = "";
        String message = "";
        try {
            token = details.get("token").getAsString();
            message = details.get("message").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        return null;
    }
}
