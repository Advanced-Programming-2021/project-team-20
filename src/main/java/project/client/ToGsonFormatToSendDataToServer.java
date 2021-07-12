package project.client;

import com.google.gson.*;

import project.client.view.LoginController;

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

    public static String toGsonFormatAddOrRemoveCardFromMainOrSideDeck(String type, String cardName,
            boolean isMainDeck) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("cardName", cardName);
        jsonObject.addProperty("isCardAddToMainDeck", isMainDeck);
        jsonObject.addProperty("token", LoginController.getToken());
        return jsonObject.toString();
    }

}
