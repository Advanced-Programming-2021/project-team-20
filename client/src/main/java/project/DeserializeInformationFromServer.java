package project;

import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DeserializeInformationFromServer {
    private static String error = "Error";
    private static String success = "Successful";
    private static String type = "type";
    private static String message = "message";
    private static String token = "token";

    public static HashMap<String, String> deserializeRegister(String informationOfServer) {

        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(informationOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put(type, jsonObject.get(type).getAsString());
            information.put(message, jsonObject.get(message).getAsString());
            if (information.get(type).equals(success)) {
                information.put(token, jsonObject.get(token).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeLogin(String indormationOfServer) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(indormationOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put(type, jsonObject.get(type).getAsString());
            information.put(message, jsonObject.get(message).getAsString());
            if (information.get(type).equals(success)) {
                information.put(token, jsonObject.get(token).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static String getError() {
        return error;
    }

    public static String getMessage() {
        return message;
    }

    public static String getSuccess() {
        return success;
    }

    public static String getToken() {
        return token;
    }

    public static String getType() {
        return type;
    }
}
