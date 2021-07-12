package project.server.controller.non_duel.loginMenu;

import com.google.gson.JsonObject;
import project.model.User;
import project.server.ServerController;
import project.server.ToGsonFormatForSendInformation;
import project.server.controller.non_duel.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

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

    private static boolean doesUserWithThisUsernameAlreadyExistsCreateMenu(String command) {
        String username = LoginMenuPatterns.findUsernameCreateUser(command);
        if (Storage.getUserByName(username) == null)
            return false;
        return true;
    }

    private static String createUser(String userName, String nickName, String password) {
        String filePath = chooseRandomImageForUser();
        Storage.addUserToAllUsers(new User(userName, nickName, password, filePath));
        return "user created successfully!";
    }

    private static boolean doesUserWithThisUsernameAlreadyExistsLoginMenu(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        if (Storage.getUserByName(username) == null)
            return false;
        return true;
    }

    private static boolean doesUserWithThisNicknameAlreadyExists(String command) {
        String nickname = LoginMenuPatterns.findNicknameCreateUser(command);
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    private static String loginUser(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        //TODO: what string should I send?
        ServerController.setLoginedUser("", Storage.getUserByName(username));
        //   Profile.setOnlineUser(Storage.getUserByName(username));
        return "user logged in successfully!";
    }

    private static boolean doesPasswordAndUsernameMatch(String command) {
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
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Error",
                "user with username " + userName + " already exists");
        }
        if (doesUserWithThisNicknameAlreadyExists(nickName)) {
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Error", "user with nickname " + nickName + " already exists");
        }
        return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Successful", createUser(userName, nickName, password));
    }

    public static synchronized String loginUser(JsonObject details) {
        String username = "";
        String password = "";
        try {
            username = details.get("username").getAsString();
            password = details.get("password").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }

        if (!doesUserWithThisUsernameAlreadyExistsLoginMenu(username)) {
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Error", "Username and password didn't match!");
        }
        String commandForCheckPassword = "user login -u " + username + " -p" + password;
        if (!doesPasswordAndUsernameMatch(commandForCheckPassword)) {
            return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Error", "Username and password didn't match!");
        }
        String commandForLogin = "user login -u " + username + " -p " + password;
        return ToGsonFormatForSendInformation.ToGsonFormatForRegister("Successful", loginUser(commandForLogin));
    }

    private static String chooseRandomImageForUser() {
        File dir = new File("server\\src\\main\\resources\\project\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }

}
