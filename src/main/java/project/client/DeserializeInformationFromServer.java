package project.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

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
                information.put("token", jsonObject.get(token).getAsString());
                information.put("userInformation", jsonObject.get("userInformation").getAsString());
                information.put("wholeDeck", jsonObject.get("wholeDeck").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeLogin(String informationOfServer) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(informationOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put(type, jsonObject.get(type).getAsString());
            information.put(message, jsonObject.get(message).getAsString());
            if (information.get(type).equals(success)) {
                information.put(token, jsonObject.get(token).getAsString());
                information.put("userInformation", jsonObject.get("userInformation").getAsString());
                information.put("wholeDeck", jsonObject.get("wholeDeck").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeProgileInformation(String informationOfServer) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(informationOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put(type, jsonObject.get(type).getAsString());
            information.put("userName", jsonObject.get("userName").getAsString());
            information.put("nickName", jsonObject.get("nickName").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeForOnlyTypeAndMessage(String informationOfServer) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(informationOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put(type, jsonObject.get(type).getAsString());
            information.put("message", jsonObject.get("message").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
            information.put("message", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeShowNumberShop(String answerOfServer) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(answerOfServer);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put("uselessCards", jsonObject.get("uselessCards").getAsString());
            information.put("numberOfBoughtCards", jsonObject.get("numberOfBoughtCards").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
        }
        return information;
    }

    public static HashMap<String, String> deserializeInformationOfAdmin(String answerOfServer2) {
        HashMap<String, String> information = new HashMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(answerOfServer2);
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            information.put("isAllowed", jsonObject.get("isAllowed").getAsString());
            information.put("numberOfCardsInShop", jsonObject.get("numberOfCardsInShop").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            information.put("type", "Error");
        }
        return information;
    }

//    public static HashMap<String, String> deserializeForBuyCard(String informationOfServer) {
//        HashMap<String, String> information = new HashMap<>();
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonElement = jsonParser.parse(informationOfServer);
//        try {
//            JsonObject jsonObject = jsonElement.getAsJsonObject();
//            information.put(type, jsonObject.get(type).getAsString());
//            information.put("message", jsonObject.get("message").getAsString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            information.put("type", "Error");
//            information.put("message", "Error");
//        }
//        return information;
//    }


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
