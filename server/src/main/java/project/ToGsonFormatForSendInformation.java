package project;

import java.util.UUID;

import com.google.gson.JsonObject;

import project.model.User;

public class ToGsonFormatForSendInformation {
    private static String badRequestFormat = "Bad request format";
    private static String error = "Error";
    private static String token = "toekn";
    private static String successful = "Successful";
    private static String type = "type";
    private static String message = "message";

    public static String toGsonFormatForRegister(String type, String message, User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ToGsonFormatForSendInformation.type, type);
        jsonObject.addProperty(ToGsonFormatForSendInformation.message, message);
        if (type.equals(ServerController.getSuccessful())) {
            String token = UUID.randomUUID().toString();
            ServerController.getLoginedUsers().put(token, user);
            jsonObject.addProperty(token, token);
        }
        return jsonObject.toString();
    }

    public static String toGsonFormatForLogin(String type, String message, User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ToGsonFormatForSendInformation.type, type);
        jsonObject.addProperty(ToGsonFormatForSendInformation.message, message);
        if (type.equals(ToGsonFormatForSendInformation.successful)) {
            String token = UUID.randomUUID().toString();
            ServerController.getLoginedUsers().put(token, user);
            jsonObject.addProperty("token", token);
        }
        return jsonObject.toString();
    }

}
