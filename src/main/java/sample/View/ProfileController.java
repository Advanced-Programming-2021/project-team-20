package sample.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sample.controller.non_duel.storage.Storage;
import sample.model.User;

public class ProfileController implements Initializable {
    @FXML
    Circle circleimg;
    @FXML
    Button backbtn;
    @FXML
    Button changePasswordbtn;
    @FXML
    Button changeImagebtn;
    @FXML
    Button changeNicknamebtn;
    @FXML
    Label nicknameLabel;
    @FXML
    Label usernameLabel;
    private String imageName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        User user = Storage.getAllUsers().get(0);
        showImage(addAnotherBackSlash(user.getImagePath()));
        nicknameLabel.setText("My Nickname : " + user.getNickname());
        usernameLabel.setText("My Username : " + user.getName());    
    }

    private String addAnotherBackSlash(String imagePath) {
        StringBuilder anotherBackSlash = new StringBuilder();
        for (int i = 0; i < imagePath.length(); i++) {
            if (imagePath.charAt(i) == '\\') {
                anotherBackSlash.append(imagePath.charAt(i));
            }
            anotherBackSlash.append(imagePath.charAt(i));
        }
        return anotherBackSlash.toString();
    }

    private void showImage(String path) {
        circleimg.setStroke(Color.DARKRED);
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("123456789");
            e.printStackTrace();
        }
        Image image = new Image(stream);
        circleimg.setFill(new ImagePattern(image));
        circleimg.setEffect(new DropShadow(+25000d,0d, +2d, Color.BLACK));
        
    }

    public void changePassword(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/sample/fxml/changePasswordPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeNickname(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/sample/fxml/changeNicknamePage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        String imagePath = "src\\main\\resources\\sample\\images\\Characters\\chosenCharacters\\" + imageName
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

    public void backToMainMenu() {
        try {
            new MainView().changeView("/sample/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}