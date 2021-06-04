package sample.View;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.controller.non_duel.storage.Storage;
import sample.model.User;

public class RegisterController implements Initializable {

    @FXML
    Button signupbtn;
    @FXML
    Button backbtn;
    @FXML
    TextField usernameField;
    @FXML
    TextField nicknameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label label;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

    }

    public void signupUser() {
        if (nicknameField.getText().isEmpty() || usernameField.getText().isEmpty()
                || passwordField.getText().isEmpty()) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("FILL FIELDS");
            return;
        }
        if (doesUserWithThisUsernameAlreadyExists()) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("USERNAME IS REPEATED");
            return;
        }
        if (doesUserWithThisNicknameAlreadyExists()) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("NICKNAME IS REPEATED");
            return;
        }

        createUser();

        try {
            
            new MainView().changeView("/sample/fxml/loginPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createUser() {
        Storage.addUserToAllUsers(new User(usernameField.getText(), nicknameField.getText(), passwordField.getText(),
                chooseRandomImageForUser()));
    }

    private boolean doesUserWithThisUsernameAlreadyExists() {
        if (Storage.getUserByName(usernameField.getText()) == null)
            return false;
        return true;
    }

    private boolean doesUserWithThisNicknameAlreadyExists() {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nicknameField.getText()))
                return true;
        }
        return false;
    }

    public void backToLoginMenu() {
        try {
            new MainView().changeView("/sample/fxml/loginPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String chooseRandomImageForUser() {
        File dir = new File("src\\main\\resources\\sample\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }

}