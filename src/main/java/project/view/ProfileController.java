package project.view;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.controller.non_duel.profile.Profile;

public class ProfileController implements Initializable {
    private static final AlertType CONFIRMATION = null;
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
    private Profile profile = new Profile();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        showImage((LoginController.getOnlineUser().getImage()));
        myUserNmaeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        myNicknameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        nicknameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        nicknameLabel.setText(LoginController.getOnlineUser().getNickname());
        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        usernameLabel.setText(LoginController.getOnlineUser().getName());
    }

    private void showImage(Image image) {
        rectangleImage.setFill(new ImagePattern(image));
        rectangleImage.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
    }

    public void changeImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("images", "*.png"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("images", "*.PNG"));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("images", "*.JPG"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        if (file != null) {
            profile.changeImage(file.getAbsolutePath());
            rectangleImage.setFill(new ImagePattern(LoginController.getOnlineUser().getImage()));
        }
    }

    public void changePassword(ActionEvent actionEvent) {

        if (currentPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            showAlert("FILL FIELDS");
            currentPasswordField.setText("");
            newPasswordField.setText("");
            return;
        }
        String result = profile.changePassword(currentPasswordField.getText(), newPasswordField.getText());
        if (result.equals("current password is invalid")) {
            showAlert("CURRENT PASSWORD IS WRONG");
        } else if (result.equals("please enter a new password")) {
            showAlert("ENTER NEW PASSWORD");
        } else {
            showAlert("PASSWORD CHANGED SUCCESSFULLY!");
        }
        currentPasswordField.setText("");
        newPasswordField.setText("");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(CONFIRMATION, message, ButtonType.OK);
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
    }

    public void changeNickname(ActionEvent actionEvent) {

        if (newNicknameField.getText().equals("")) {
            showAlert("FILL FIELDS");
            return;
        }

        String result = profile.changeNickname(newNicknameField.getText());
        if (result.equals("user with nickname already exists")) {
            showAlert("NEW NICKNAME IS REPEATED");
            newNicknameField.setText("");
            return;
        }

        nicknameLabel.setText(newNicknameField.getText());
        newNicknameField.setText("");
        showAlert("NICKNAME CHANGED SUCCESSFULLY!");
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
