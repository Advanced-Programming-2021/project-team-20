package project.client.view;

import java.io.IOException;
import java.net.URL;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.client.CardsStorage;
import project.client.ServerConnection;
import project.server.controller.non_duel.storage.Storage;

public class MainView extends Application {
    private static Stage stage;
    private AnchorPane root;

    @Override
    public void start(Stage stage) throws IOException {
        URL welcomeUrl = getClass().getResource("/project/fxml/loginPage.fxml");
        root = FXMLLoader.load(welcomeUrl);
        MainView.stage = stage;

        stage.setResizable(false);
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws Exception {
        ServerConnection.initializeNetwork();
        CardsStorage storage = new CardsStorage();
        storage.startProgram();
        UIStorage.createPreliminaryToStartProgram();
        launch(args);
        String logout = "{\"type\":\"logout\",\"token\":\"" + LoginController.getToken() + "\"}";
        ServerConnection.sendDataToServerAndRecieveResult(logout);
        // storage.endProgram();
    }

    public void changeView(String fxml) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(root);
        createFadeTransition(root);
    }

    private static void createFadeTransition(Parent root) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setNode(root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public static void changeScene(Parent parent) {
        stage.getScene().setRoot(parent);
        createFadeTransition(parent);
    }

    public static Stage getStage() {
        return stage;
    }
}
