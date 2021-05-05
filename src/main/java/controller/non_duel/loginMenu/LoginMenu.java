package controller.non_duel.loginMenu;

import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;

public class LoginMenu {

    public String findCommand(String command) {

        if (LoginMenuPatterns.isItCreateUserPattern(command)) {

            if (doesUserWithThisUsernameAlreadyExists(command)) {
                
                return "user with username already exists";
            }
            if (doesUserWithThisNicknameAlreadyExists(command)) {

                return "user with nickname <nickname> already exists";
            }
            return createUser(command);
        }
        // if (LoginMenuPatterns.isItLoginUserPattern(command)) {

        //     if (!doesUserWithThisUsernameAlreadyExists(command)) {
        //         return "Username and password didn't match!";
        //     }
        //     if (!doPasswordAndUsernameMatch()) {
        //         return "Username and password didn't match!";
        //     }
            return loginUser(command);
       // }
      //  return "invalid command";
    }

    private String createUser(String command) {

        String username = LoginMenuPatterns.findUsernameCreateUser(command);
        String password = LoginMenuPatterns.findPasswordCreateUser(command);
        String nickname = LoginMenuPatterns.findNicknameCreateUser(command);
        Storage.addUserToAllUsers(new User(username, nickname, password));
        return "user created successfully!";
    }

    private boolean doesUserWithThisUsernameAlreadyExists(String command) {
        return false;
    }

    private boolean doesUserWithThisNicknameAlreadyExists(String command) {
        return false;
    }

    private String loginUser(String command) {
        Profile.setOnlineUser(Storage.getUserByName(LoginMenuPatterns.findUsernameLoginUser(command)));
        return "user logged in successfully!";
    }

    private boolean doPasswordAndUsernameMatch() {
        return false;
    }

}
