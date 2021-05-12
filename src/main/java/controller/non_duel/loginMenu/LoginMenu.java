package controller.non_duel.loginMenu;

import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;

import java.util.ArrayList;

public class LoginMenu {

    public String findCommand(String command) {
        // find command from login menu patterns
        if (LoginMenuPatterns.isItCreateUserPattern(command)) {
            if (doesUserWithThisUsernameAlreadyExistsCreateMenu(command)) {
                return "user with username already exists";
            }
            if (doesUserWithThisNicknameAlreadyExists(command)) {
                return "user with nickname <nickname> already exists";
            }
            return createUser(command);
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

    private boolean doesUserWithThisUsernameAlreadyExistsCreateMenu(String command) {
        String username = LoginMenuPatterns.findUsernameCreateUser(command);
        if (Storage.getUserByName(username) == null)
            return false;
        return true;
    }

    private String createUser(String command) {
        String username = LoginMenuPatterns.findUsernameCreateUser(command);
        String password = LoginMenuPatterns.findPasswordCreateUser(command);
        String nickname = LoginMenuPatterns.findNicknameCreateUser(command);
        Storage.addUserToAllUsers(new User(username, nickname, password));
        return "user created successfully!";
    }

    private boolean doesUserWithThisUsernameAlreadyExistsLoginMenu(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        if (Storage.getUserByName(username) == null)
            return false;
        return true;
    }

    private boolean doesUserWithThisNicknameAlreadyExists(String command) {
        String nickname = LoginMenuPatterns.findNicknameCreateUser(command);
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickname))
                return true;
        }
        return false;
    }

    private String loginUser(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        Profile.setOnlineUser(Storage.getUserByName(username));
        return "user logged in successfully!";
    }

    private boolean doesPasswordAndUsernameMatch(String command) {
        String username = LoginMenuPatterns.findUsernameLoginUser(command);
        String password = LoginMenuPatterns.findPasswordLoginUser(command);
        User user = Storage.getUserByName(username);
        String correctPassword = user.getPassword();
        return correctPassword.equals(password);
    }

}
