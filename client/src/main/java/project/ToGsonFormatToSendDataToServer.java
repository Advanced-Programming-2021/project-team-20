package project;

import com.google.gson.JsonObject;

public class ToGsonFormatToSendDataToServer {
    public static String toGsonFormatRegister(String userName, String nickName ,String password){
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("type","register");
         jsonObject.addProperty("userName", userName);
         jsonObject.addProperty("nickName", nickName);
         jsonObject.addProperty("password", password);
         return jsonObject.toString();
    }

    public static String toGsonFormatLogin(String userName, String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "login");
        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("password", password);
        return jsonObject.toString();
    }
}
