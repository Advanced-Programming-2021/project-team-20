package sample.View;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controller.non_duel.storage.Storage;

public class MainView extends Application {
    private static Stage stage;
    private static Storage storage;

    @Override
    public void start(Stage stage) throws IOException {
        URL welcomeUrl = getClass().getResource("/sample/fxml/loginPage.fxml");
        Parent root = FXMLLoader.load(welcomeUrl);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        MainView.stage = stage;
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        // stage.setWidth(primaryScreenBounds.getWidth());
        // stage.setHeight(primaryScreenBounds.getHeight());
        stage.setResizable(true);
        Scene scene = new Scene(root);
        // File file = new File("src\\main\\resources\\sample\\images\\passwordLabel.png");
        // System.out.println(file.exists());
        // Image imageCursor = new Image("src\\main\\resources\\sample\\images\\passwordLabel.png");
        // System.out.println(imageCursor.getUrl());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        // File dir = new File("src\\main\\resources\\sample\\images\\Characters");
        // File[] images = dir.listFiles();
        // String imagePath = images[new Random().nextInt(images.length)].getPath();
        // StringBuilder anotherBackSlash = new StringBuilder();
        // for (int i = 0; i < imagePath.length(); i++) {
        // if (imagePath.charAt(i) == '\\') {
        // anotherBackSlash.append(imagePath.charAt(i));
        // }
        // anotherBackSlash.append(imagePath.charAt(i));
        // }
        // System.out.println(anotherBackSlash);
        launch(args);
        System.out.println("dasdasd");
    }

    public void changeView(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(root);
    }

    public static Stage getStage() {
        return stage;
    }
}
