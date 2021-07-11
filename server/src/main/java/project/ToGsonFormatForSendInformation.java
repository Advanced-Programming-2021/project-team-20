package project;

import java.util.UUID;

import com.google.gson.JsonObject;

public class ToGsonFormatForSendInformation {

    public static String ToGsonFormatForRegister(String type, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("massage", message);
        if(type.equals(ServerController.getSuccessful())){
            jsonObject.addProperty("token", UUID.randomUUID().toString());
        }
        return jsonObject.toString();
    }

}
