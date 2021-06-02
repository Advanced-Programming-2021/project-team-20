package sample.View;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.non_duel.storage.Storage;

public class MainView extends Application {
    private static Stage stage;
    private static Storage storage;

    @Override
    public void start(Stage stage) throws IOException {
        MainView.stage = stage;

        URL welcomeUrl = getClass().getResource("/sample/fxml/loginPage.fxml");
        Parent root = FXMLLoader.load(welcomeUrl);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/sample/css/abc.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        MainView.storage = new Storage();
        storage.startProgram();
        launch(args);
    }


    public void changeView(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(root);
    }
}
