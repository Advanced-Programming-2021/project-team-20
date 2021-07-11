package project.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import project.DeserializeInformationFromServer;
import project.ServerConnection;
import project.ToGsonFormatToSendDataToServer;
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
    private static String token;
    private static Image image;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/opening.mp3");
    }

    public void loginUser(ActionEvent ev) {
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            passwordField.setText("");
            usernameField.setText("");
        }
        String data = ToGsonFormatToSendDataToServer.toGsonFormatLogin(usernameField.getText(),
                passwordField.getText());
        String result = ServerConnection.sendDataToServerAndRecieveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeLogin(result);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        image = createImageAlaki();
        // setOnlineUser(Storage.getUserByName(usernameField.getText()));
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public void signUpUser() {
        if (usernameFieldForRegister.getText().equals("") || nickNameFieldForRegister.getText().equals("")
                || passwordFieldfORegister.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            return;
        }
        String data = ToGsonFormatToSendDataToServer.toGsonFormatRegister(usernameFieldForRegister.getText(),
                nickNameFieldForRegister.getText(), passwordFieldfORegister.getText());
        String result = ServerConnection.sendDataToServerAndRecieveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeRegister(result);
        if (deserializeResult.get(DeserializeInformationFromServer.getType())
                .equals(DeserializeInformationFromServer.getError())) {
            showAlert(deserializeResult.get(DeserializeInformationFromServer.getMessage()),
                    DeserializeInformationFromServer.getError());
        } else {
            token = deserializeResult.get(DeserializeInformationFromServer.getToken());
            CustomDialog customDialog = new CustomDialog(DeserializeInformationFromServer.getSuccess(),
                    deserializeResult.get(DeserializeInformationFromServer.getMessage()), "mainMenu");
            customDialog.openDialog();
            image = createImageAlaki();
            // onlineUser = new User(usernameFieldForRegister.getText(),
            // nickNameFieldForRegister.getText(),
            // passwordFieldfORegister.getText(), "");
            // onlineUser.setImage(createImageAlaki());
        }

        usernameFieldForRegister.setText("");
        passwordFieldfORegister.setText("");
        nickNameFieldForRegister.setText("");
    }

    private Image createImageAlaki() {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\images\\userLabel.jpg");
            return new Image(stream);
        } catch (Exception e) {
            System.out.println("exception in createImageAlaki");
        }
        return null;
    }

    public static void setOnlineUser(User onlineUser) {
        LoginController.onlineUser = onlineUser;
    }

    public static User getOnlineUser() {
        return onlineUser;
    }
}