package project.server;

import com.google.gson.JsonObject;

public class ToGsonFormatForSendInformation {

    public static String ToGsonFormatForRegister(String type, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("massage", message);
        return jsonObject.toString();
    }

}
