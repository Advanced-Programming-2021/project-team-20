package project.server.controller.non_duel.loginMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonObject;

import project.model.User;
import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.non_duel.storage.Storage;

public class LoginMenu {

    private static boolean doesUserWithThisUsernameAlreadyExists(String userName) {
        if (Storage.getUserByName(userName) == null)
            return false;
        return true;
    }

    private static User createUser(String userName, String nickName, String password) {
        String filePath = chooseRandomImageForUser();
        User user = new User(userName, nickName, password, filePath);
        Storage.addUserToAllUsers(user);
        return user;
    }

    private static boolean doesUserWithThisNicknameAlreadyExists(String nickName) {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickName))
                return true;
        }
        return false;
    }

    private static boolean doesPasswordAndUsernameMatch(User user, String password) {
        String correctPassword = user.getPassword();
        return correctPassword.equals(password);
    }

    public static synchronized String registerUser(JsonObject details) {
        String userName = "";
        String password = "";
        String nickName = "";
        try {
            userName = details.get("userName").getAsString();
            password = details.get("password").getAsString();
            nickName = details.get("nickName").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        if (doesUserWithThisUsernameAlreadyExists(userName)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForRegister(ServerController.getError(),
                    "USERNAME IS REPEATED", null);
        }
        if (doesUserWithThisNicknameAlreadyExists(nickName)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForRegister(ServerController.getError(),
                    "NICKNAME IS REPEATED", null);
        }
        User user = createUser(userName, nickName, password);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForRegister(ServerController.getSuccessful(),
                "USER CREATED SUCCESSFULY!", user);
    }

    public static synchronized String loginUser(JsonObject details) {
        String userName = "";
        String password = "";
        try {
            userName = details.get("userName").getAsString();
            password = details.get("password").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        if (!doesUserWithThisUsernameAlreadyExists(userName)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForLogin(ServerController.getError(),
                    "Username And Password Didn't Match!", null);
        }
        User user = Storage.getUserByName(userName);
        if (!doesPasswordAndUsernameMatch(user, password)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForLogin(ServerController.getError(),
                    "Username And Password Didn't Match!", null);
        }
        HashMap<String, User> loginedUsers = ServerController.getLoginedUsers();

        for (Map.Entry<String, User> entry : loginedUsers.entrySet()) {
            if (user.getName().equals(entry.getValue().getName())) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForLogin(ServerController.getError(), "User Allready logined", null);
            }
        }

        return ToGsonFormatForSendInformationToClient.toGsonFormatForLogin(ServerController.getSuccessful(),
                "User Logined Successfully!", user);
    }

    private static String chooseRandomImageForUser() {
        File dir = new File("src\\main\\resources\\project\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }
}
