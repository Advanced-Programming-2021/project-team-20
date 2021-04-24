package test.maven.controller.profile;

import java.util.ArrayList;
import java.util.HashMap;

import test.maven.controller.storage.Storage;
import test.maven.model.User;

public class Profile {

    private ProfilePatterns profilePatterns = new ProfilePatterns();
    private User onlineUser = new User();

    public String findCommands(String command) {
        HashMap<String, String> foundCommadns = profilePatterns.findCommands(command);
        if (foundCommadns == null) {
            return "invalid command!";
        }
        if (foundCommadns.containsKey("nickname")) {
            return changeNickname(foundCommadns.get("nickname"));
        }

        return changePassword(foundCommadns.get("current password"), foundCommadns.get("new password"));
    }

    private String changeNickname(String nickname) {

        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickname)) {
                return "user with nickname " + nickname + " already exists";
            }
        }
        onlineUser.setNickname(nickname);

        return "nickname changed successfully!";
    }

    private String changePassword(String currentPassword, String newPassword) {

        if (!currentPassword.equals(onlineUser.getPassword())) {
            return "current password is invalid";
        }
        if (newPassword.equals(currentPassword)) {
            return "please enter a new password";
        }

        onlineUser.setPassword(newPassword);
        return "password changed successfully!";
    }

}