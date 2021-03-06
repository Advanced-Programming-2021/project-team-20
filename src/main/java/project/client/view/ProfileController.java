package project.client.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class ProfileController implements Initializable {
    @FXML
    private Rectangle rectangleImage;
    @FXML
    private Button backbtn;
    @FXML
    private Button changePasswordbtn;
    @FXML
    private Button changeImagebtn;
    @FXML
    private Button changeNicknamebtn;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField newNicknameField;
    @FXML
    private Label myUserNmaeLabel;
    @FXML
    private Label myNicknameLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/Profile.mp3");
        showImage((LoginController.getOnlineUser().getImage()));
        myUserNmaeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        myNicknameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        nicknameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        nicknameLabel.setText(LoginController.getOnlineUser().getNickname());
        usernameLabel.setText(LoginController.getOnlineUser().getName());
    }

    private void showImage(Image image) {
        rectangleImage.setFill(new ImagePattern(image));
        rectangleImage.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
    }

    public void changeImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images",
            "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images",
            "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images",
            "*.PNG"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images",
            "*.JPG"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        if (file != null) {
            String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("changeImage", "imagePath", file.getAbsolutePath());
            String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
            HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
            showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
            if (deserializeResult.get("message").equals("Connection Disconnected")) {
                new MainMenuController().backToLoginPage();
            }
            if (deserializeResult.get("type").equals("Successful")) {
                LoginController.getOnlineUser().setImage(createImage(file.getAbsolutePath()));
                rectangleImage.setFill(new
                    ImagePattern(LoginController.getOnlineUser().getImage()));
            }
        }
    }

    private Image createImage(String path) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
            return new Image(stream);
        } catch (Exception e) {
        }
        return null;
    }

    public void changePassword() {

        if (currentPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            currentPasswordField.setText("");
            newPasswordField.setText("");
            return;
        }
        String data = ToGsonFormatToSendDataToServer.toGsonFormatChangePassword(currentPasswordField.getText(),
            newPasswordField.getText());
        String resultOfServer = (String) ServerConnection.sendDataToServerAndReceiveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(resultOfServer);
        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
        if (deserializeResult.get("message").equals("Connection Disconnected")) {
            new MainMenuController().backToLoginPage();
        }
        currentPasswordField.setText("");
        newPasswordField.setText("");
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public void changeNickname() {

        if (newNicknameField.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            return;
        }

        String data = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("changeNickName", "newNickName", newNicknameField.getText());
        String resultOfServer = (String) ServerConnection.sendDataToServerAndReceiveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(resultOfServer);

        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
        if (deserializeResult.get("type").equals("Successful")) {
            LoginController.getOnlineUser().setNickname(nicknameLabel.getText());
            nicknameLabel.setText(newNicknameField.getText());
        }
        if (deserializeResult.get("message").equals("Connection Disconnected")) {
            new MainMenuController().backToLoginPage();
        }
        newNicknameField.setText("");
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
