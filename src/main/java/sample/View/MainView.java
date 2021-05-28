package sample.View;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {
    private static Stage stage;

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
        launch(args);
    }


    public void changeView(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(root);
    }
}
