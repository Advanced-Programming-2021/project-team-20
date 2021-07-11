package project.controller.loginMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.JsonObject;

import project.ServerController;
import project.ToGsonFormatForSendInformation;
import project.controller.storage.Storage;
import project.model.User;

public class LoginMenu {

    public String findCommand(String command) {

        if (LoginMenuPatterns.isItCreateUserPattern(command)) {
            if (doesUserWithThisUsernameAlreadyExistsCreateMenu(command)) {
                String username = LoginMenuPatterns.findUsernameCreateUser(command);
                return "user with username " + username + " already exists";
            }
            if (doesUserWithThisNicknameAlreadyExists(command)) {
                String nickname = LoginMenuPatterns.findNicknameCreateUser(command);
                return "user with nickname " + nickname + " already exists";
            }
            // return createUser(command);
        }
        if (LoginMenuPatterns.isItLoginUserPattern(command)) {
            if (!doesUserWithThisUsernameAlreadyExistsLoginMenu(command)) {
                return "Username and password didn't match!";
            }
            if (!doesPasswordAndUsernameMatch(command)) {
                return "Username and password didn't match!";
            }
            return loginUser(command);
        }
        return "invalid command";
    }

    private static boolean doesUserWithThisUsernameAlreadyExistsCreateMenu(String userName) {
        if (Storage.getUserByName(userName) == null)
            return false;
        return true;
    }

    private static String createUser(String userName, String nickName, String password) {
        String filePath = chooseRandomImageForUser();
        Storage.addUserToAllUsers(new User(userName, nickName, password, filePath));
        return "user created successfully!";
    }

    private boolean doesUserWithThisUsernameAlreadyExistsLoginMenu(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        if (Storage.getUserByName(username) == null)
            return false;
        return true;
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

    private boolean doesPasswordAndUsernameMatch(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        String password = LoginMenuPatterns.findPasswordLoginUser(command);
        User user = Storage.getUserByName(username);
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

        if (doesUserWithThisUsernameAlreadyExistsCreateMenu(userName)) {
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister(ServerController.getError(),
                    "user with username " + userName + " already exists");
        }
        if (doesUserWithThisNicknameAlreadyExists(nickName)) {
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister(ServerController.getError(),
                    "user with nickname " + nickName + " already exists");
        }
        return ToGsonFormatForSendInformation.ToGsonFormatForRegister(ServerController.getSuccessful(),
                createUser(userName, nickName, password));
    }

    public static synchronized String loginUser(JsonObject details) {
        return null;
    }

    private static String chooseRandomImageForUser() {
        File dir = new File("server\\src\\main\\resources\\project\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }
}
