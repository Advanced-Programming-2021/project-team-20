package test.maven.controller.loginMenu;

import java.util.HashMap;

public class LoginMenu {

    public HashMap<String, String> findCommand(String command) {
        //find command from login menu patterns
        HashMap<String ,String> x = new HashMap<>();   
        x.put("logined", "dsa");
        return x;
    }

    private String createUser() {
        // CALL FUNCTION in Storage and add user to arrayListOfUsers
        return null;
    }

    private boolean doesUserWithThisUsernameAlreadyExists() {
        return true;
    }

    private boolean doesUserWithThisNicknameAlreadyExists() {
        return true;
    }

    private String loginUser() {
        return null;
    }

    private boolean doPasswordAndUsernameMatch() {
        return true;
    }

}