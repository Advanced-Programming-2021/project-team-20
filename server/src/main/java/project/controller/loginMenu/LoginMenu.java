package project.controller.loginMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.image.*;

import com.google.gson.JsonObject;

import project.ServerController;
import project.ToGsonFormatForSendInformation;
import project.controller.storage.Storage;
import project.model.User;

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

    private String loginUser(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        // Profile.setOnlineUser(Storage.getUserByName(username));
        return "user logged in successfully!";
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
            return ToGsonFormatForSendInformation.toGsonFormatForRegister(ServerController.getError(),
                    "USERNAME IS REPEATED", null);
        }
        if (doesUserWithThisNicknameAlreadyExists(nickName)) {
            return ToGsonFormatForSendInformation.toGsonFormatForRegister(ServerController.getError(),
                    "NICKNAME IS REPEATED", null);
        }
        User user = createUser(userName, nickName, password);
        return ToGsonFormatForSendInformation.toGsonFormatForRegister(ServerController.getSuccessful(),
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
            return ToGsonFormatForSendInformation.toGsonFormatForLogin(ServerController.getError(),
                    "Username And Password Didn't Match!", null);
        }
        User user = Storage.getUserByName(userName);
        if (!doesPasswordAndUsernameMatch(user, password)) {
            return ToGsonFormatForSendInformation.toGsonFormatForLogin(ServerController.getError(),
                    "Username And Password Didn't Match!", null);
        }
        HashMap<String, User> loginedUsers = ServerController.getLoginedUsers();

        for (Map.Entry<String, User> entry : loginedUsers.entrySet()) {
            if (user.getName().equals(entry.getValue().getName())) {
                return ToGsonFormatForSendInformation.toGsonFormatForLogin(ServerController.getError(), "User Allready logined", null);
            }
        }

        // Image iamge ;

        return ToGsonFormatForSendInformation.toGsonFormatForLogin(ServerController.getSuccessful(),
                "User Logined Successfully!", user);
    }

    private static String chooseRandomImageForUser() {
        File dir = new File("src\\main\\resources\\project\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }
}
