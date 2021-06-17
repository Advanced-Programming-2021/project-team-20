package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.controller.non_duel.profile.Profile;
import project.controller.non_duel.storage.Storage;
import project.model.User;

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
    private Profile profile = new Profile();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        showImage((LoginController.getOnlineUser().getImage()));
        nicknameLabel.setText(LoginController.getOnlineUser().getNickname());
        usernameLabel.setText(LoginController.getOnlineUser().getName());
    }

    private void showImage(Image image) {
        rectangleImage.setFill(new ImagePattern(image));
        rectangleImage.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
    }

    public void changeImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.png"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.jpg"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.PNG"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.JPG"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        profile.changeImage(file.getAbsolutePath());
        rectangleImage.setFill(new ImagePattern(LoginController.getOnlineUser().getImage()));
    }

    public void changePassword(ActionEvent actionEvent) {

        if (currentPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("FILL FIELDS");
            currentPasswordField.setText("");
            newPasswordField.setText("");
            return;
        }
        String result = profile.changePassword(currentPasswordField.getText(), newPasswordField.getText());
        if (result.equals("current password is invalid")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("CURRENT PASSWORD IS WRONG");

        } else if (result.equals("please enter a new password")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("ENTER NEW PASSWORD");

        }

        currentPasswordField.setText("");
        newPasswordField.setText("");
        // else {} show successful change password message

    }

    public void changeNickname(ActionEvent actionEvent) {

        if (newNicknameField.getText().equals("")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("FILL FIELDS");
            return;
        }

        String result = profile.changeNickname(newNicknameField.getText());
        if (result.equals("user with nickname already exists")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("NEW NICKNAME IS REPEATED");
            newNicknameField.setText("");
            return;
        }

        nicknameLabel.setText(newNicknameField.getText());
        newNicknameField.setText("");

        // show successful message
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}