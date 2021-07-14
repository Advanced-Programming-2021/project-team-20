package project.client.view.pooyaviewpackage;

import com.google.gson.JsonObject;
import project.client.ServerConnection;

public class JsonCreator {
    private static String firstAdditionalString = "";
    private static String integerString = "";

    public static String getResult(String string){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "duel");
        jsonObject.addProperty("token", DuelView.getToken());
        jsonObject.addProperty("firstAdditionalString", firstAdditionalString);
        jsonObject.addProperty("integerString", integerString);
        jsonObject.addProperty("request", string);
        return ServerConnection.sendDataToServerAndReceiveResult(jsonObject.toString());
    }

    public static String getFirstAdditionalString() {
        return firstAdditionalString;
    }

    public static void setFirstAdditionalString(String firstAdditionalString) {
        JsonCreator.firstAdditionalString = firstAdditionalString;
    }

    public static String getIntegerString() {
        return integerString;
    }

    public static void setIntegerString(String integerString) {
        JsonCreator.integerString = integerString;
    }
}
