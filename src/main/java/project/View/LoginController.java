package project.View;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import project.controller.non_duel.storage.Storage;
import project.model.User;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField usernameFieldForRegister;
    @FXML
    private TextField nickNameFieldForRegister;
    @FXML
    private PasswordField passwordFieldfORegister;

    private static User onlineUser;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

    public void loginUser(ActionEvent ev) {
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            showAlert("FILL FIELDS");
            passwordField.setText("");
            usernameField.setText("");
        } else if (Storage.getUserByName(usernameField.getText()) == null) {
            showAlert("THIS USER DOES NOT EXIST");
            passwordField.setText("");
            usernameField.setText("");
        } else if (Storage.getUserByName(usernameField.getText()).getPassword().equals(passwordField.getText())) {
            showAlert("INCORRECT PASSWORD");
            passwordField.setText("");
            usernameField.setText("");
        } else {
            
            setOnlineUser(Storage.getUserByName(usernameField.getText()));
            try {
                new MainView().changeView("/project/fxml/mainMenu.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(null, message, ButtonType.OK);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
    }

    public void signUpUser() {
        if (usernameFieldForRegister.getText().equals("") || nickNameFieldForRegister.getText().equals("")
                || passwordFieldfORegister.getText().equals("")) {
            showAlert("FILL FIELDS");
        } else if (doesUserWithThisUsernameAlreadyExists()) {
            showAlert("USERNAME IS REPEATED");
        } else if (doesUserWithThisNicknameAlreadyExists()) {
            showAlert("NICKNAME IS REPEATED");
        } else {
            showAlert("USER CREATED SUCCESSFULLY!");
            createUser();
        }
        usernameFieldForRegister.setText("");
        passwordFieldfORegister.setText("");
        nickNameFieldForRegister.setText("");
    }

    private void createUser() {
        Storage.addUserToAllUsers(new User(usernameFieldForRegister.getText(), nickNameFieldForRegister.getText(),
                passwordFieldfORegister.getText(), chooseRandomImageForUser()));
    }

    private boolean doesUserWithThisUsernameAlreadyExists() {
        if (Storage.getUserByName(usernameFieldForRegister.getText()) == null)
            return false;
        return true;
    }

    private boolean doesUserWithThisNicknameAlreadyExists() {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(nickNameFieldForRegister.getText()))
                return true;
        }
        return false;
    }

    private String chooseRandomImageForUser() {
        File dir = new File("src\\main\\resources\\project\\images\\Characters\\radomCharacters");
        File[] images = dir.listFiles();
        return images[new Random().nextInt(images.length)].getPath();
    }

    public static void setOnlineUser(User onlineUser) {
        LoginController.onlineUser = onlineUser;
    }

    public static User getOnlineUser() {
        return onlineUser;
    }

}
