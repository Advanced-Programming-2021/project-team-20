package project.View;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project.controller.non_duel.storage.Storage;

public class MainView extends Application {
    private static Stage stage;
    private Pane root;

    @Override
    public void start(Stage stage) throws IOException {
        URL welcomeUrl = getClass().getResource("/project/fxml/mainMenu.fxml");
        root = FXMLLoader.load(welcomeUrl);
        MainView.stage = stage;
        stage.setResizable(false);
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    
    public static void main(String[] args) throws IOException {
        Storage storage = new Storage();
        storage.startProgram();
        UIUtility.createPreliminaryToStartProgram();
        launch(args);
        storage.endProgram();
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
