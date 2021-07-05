package project.controller.non_duel.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javafx.scene.image.Image;
import project.view.LoginController;
import project.controller.non_duel.storage.Storage;
import project.model.User;


public class Profile {

    private ProfilePatterns profilePatterns;

    // public String findCommands(String command) {
    // profilePatterns = new ProfilePatterns();
    // HashMap<String, String> foundCommadns =
    // profilePatterns.findCommands(command);
    // if (foundCommadns == null) {
    // return "invalid command!";
    // }
    // if (foundCommadns.containsKey("nickname")) {
    // return changeNickname(foundCommadns.get("nickname"));
    // }

    // return changePassword(foundCommadns.get("current password"),
    // foundCommadns.get("new password"));
    // }

    public String changeNickname(String nickname) {

        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickname)) {
                return "user with nickname already exists";
            }
        }
        LoginController.getOnlineUser().setNickname(nickname);

        return "nickname changed successfully!";
    }

    public String changePassword(String currentPassword, String newPassword) {

        if (!currentPassword.equals(LoginController.getOnlineUser().getPassword())) {
            return "current password is invalid";
        }
        if (newPassword.equals(currentPassword)) {
            return "please enter a new password";
        }

        LoginController.getOnlineUser().setPassword(newPassword);
        return "password changed successfully!";
    }

    public void changeImage(String imagePath) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginController.getOnlineUser().setImage(new Image(stream));
        Storage.getNewImagesThatChanges().put(LoginController.getOnlineUser() , imagePath);
    }
}