package sample.controller.non_duel.profile;

import java.util.ArrayList;
import java.util.HashMap;

import sample.controller.non_duel.storage.Storage;
import sample.model.User;

public class Profile {

    private ProfilePatterns profilePatterns;
    private static User onlineUser = new User();

    public String findCommands(String command) {
        profilePatterns = new ProfilePatterns();
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

    public static void setOnlineUser(User user){
        onlineUser = user;
    }
    public static User getOnlineUser(){
        return onlineUser;
    }

}