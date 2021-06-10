package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Stack;

import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainView extends Application {
    private static Stage stage;
    private Pane root;

    @Override
    public void start(Stage stage) throws IOException {
        URL welcomeUrl = getClass().getResource("/project/fxml/mainMenu.fxml");
        root = FXMLLoader.load(welcomeUrl);
        MainView.stage = stage;
        stage.setResizable(false);
       // ScrollPane scrollPane = new ScrollPane();
       // scrollPane.setContent(abs());
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    
    public static void main(String[] args) throws IOException {
        UIUtility.createOneRectangleForEachCard();
        UIUtility.createAllCards();
        UIUtility.createAllCardDiscriptionLabels();
        UIUtility.createAllScrooBarLabels();
        UIUtility.createLabelToShowSizeOfCardsInDeck();
        launch(args);
        System.out.println("dasdasd");
    }

    public void changeView(String fxml) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(root);
    }

    public static void changeScene(Parent parent) {
        stage.getScene().setRoot(parent);
    }

    public static Stage getStage() {
        return stage;
    }
}
