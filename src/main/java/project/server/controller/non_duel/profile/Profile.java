package project.server.controller.non_duel.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import project.model.User;
import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.non_duel.storage.Storage;

public class Profile {

    public static synchronized String changeNickname(JsonObject details) {

        String newNickName = "";
        String token = "";
        try {
            newNickName = details.get("newNickName").getAsString();
            token = details.get("token").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(newNickName)) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error",
                        "NEW NICKNAME IS REPEATED");
            }
        }
        user.setNickname(newNickName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "NICKNAME CHANGED SUCCESSFULLY!");
    }

    public static String changePassword(JsonObject details) {

        String currentPassword = "";
        String newPassword = "";
        String token = "";
        try {
            currentPassword = details.get("currentPassword").getAsString();
            newPassword = details.get("newPassword").getAsString();
            token = details.get("token").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        if (!currentPassword.equals(user.getPassword())) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error",
                    "CURRENT PASSWORD IS WRONG");
        }

        if (newPassword.equals(currentPassword)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "ENTER NEW PASSWORD");
        }

        user.setPassword(newPassword);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "PASSWORD CHANGED SUCCESSFULLY!");
    }

    public void changeImage(String imagePath) {
        // InputStream stream = null;
        // try {
        // stream = new FileInputStream(imagePath);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // LoginController.getOnlineUser().setImage(new Image(stream));
        // Storage.saveNewImageOfUsers(LoginController.getOnlineUser() ,imagePath);
    }

    public static String getInformationOfUser(JsonObject details) {
        String token = "";
        try {
            token = details.get("token").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return ServerController.getBadRequestFormat();
        }
        HashMap<String, User> loginedUsers = ServerController.getLoginedUsers();
        User user = null;
        for (Map.Entry<String, User> entry : loginedUsers.entrySet()) {
            if (token.equals(entry.getKey())) {
                user = entry.getValue();
                break;
            }
        }
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForGetInformationOfProfile(user);
    }
}
