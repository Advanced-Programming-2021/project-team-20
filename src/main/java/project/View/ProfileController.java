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

    private String imageName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        User user = Storage.getAllUsers().get(0);
        showImage((user.getImagePath()));
        nicknameLabel.setText(user.getNickname());
        usernameLabel.setText(user.getName());
    }

    private void showImage(String path) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        rectangleImage.setFill(new ImagePattern(image));
        rectangleImage.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
        rectangleImage.setOpacity(1);
        rectangleImage.setArcHeight(25);
        rectangleImage.setArcWidth(25);
        

    }

    public void changeImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.png"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.jpg"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.PNG"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("images", "*.JPG"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        imageName = file.getName();
        User user = Storage.getAllUsers().get(0);
        user.setImagePath(makeImageAndGetItsPath());
    }

    private String makeImageAndGetItsPath() {
        User user = Storage.getAllUsers().get(0);
        String imagePath = "src\\main\\resources\\project\\images\\Characters\\chosenCharacters\\" + imageName
                + user.getName() + ".png";
        try {
            File file = new File(imagePath);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("str");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public void changePassword(ActionEvent actionEvent) {

        if (currentPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("FILL FIELDS");
            // return;
        }
        if (!User.getOnlineUser().getPassword().equals(currentPasswordField.getText())) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("CURRENT PASSWORD IS WRONG");
            // return;
        }
        if (newPasswordField.getText().equals(currentPasswordField.getText())) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("ENTER NEW PASSWORD");
            // return;
        }

        User.getOnlineUser().setPassword(newPasswordField.getText());

    }

    public void changeNickname(ActionEvent actionEvent) {
        if (newNicknameField.getText().equals("")) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("FILL FIELDS");
            // return;
        }
        if (doesUserWithThisNicknameAlreadyExists()) {
            // label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            // label.setText("NEW NICKNAME IS REPEATED");
            // return;
        }

        Storage.getAllUsers().get(0).setNickname(newNicknameField.getText());

    }

    private boolean doesUserWithThisNicknameAlreadyExists() {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(newNicknameField.getText()))
                return true;
        }
        return false;
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}