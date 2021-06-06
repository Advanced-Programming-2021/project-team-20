package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.controller.non_duel.storage.Storage;
import project.model.User;

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
        showImage((user.getImagePath()));
        nicknameLabel.setText("My Nickname : " + user.getNickname());
        usernameLabel.setText("My Username : " + user.getName());    
    }

    private void showImage(String path) {
        circleimg.setStroke(Color.DARKRED);
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        circleimg.setFill(new ImagePattern(image));
        circleimg.setEffect(new DropShadow(+25000d,0d, +2d, Color.BLACK));
        
    }

    public void changePassword(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/changePasswordPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeNickname(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/changeNicknamePage.fxml");
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

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}