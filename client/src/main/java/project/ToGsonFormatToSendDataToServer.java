package project;

import com.google.gson.JsonObject;

public class ToGsonFormatToSendDataToServer {
    public static String ToGsonFormatRegister(String userName, String nickName ,String password){
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("type","register");
         jsonObject.addProperty("userName", userName);
         jsonObject.addProperty("nickName", nickName);
         jsonObject.addProperty("password", password);
         return jsonObject.toString();
    }
}
