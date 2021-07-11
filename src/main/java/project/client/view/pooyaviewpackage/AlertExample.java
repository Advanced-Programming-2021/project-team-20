package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class AlertExample extends Application {
    private static Stage stage;
    private static AnchorPane anchorPane;
    @Override
    public void start(Stage stage) {

        AlertExample.stage = stage;
        //Image backgroundColor = new Image(DuelView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm());

        //BackgroundSize backgroundSize = new BackgroundSize(300, 500, true, true, true, false);
        //BackgroundImage backgroundImage = new BackgroundImage(backgroundColor, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        stage.setTitle("Duel Page");
        anchorPane = new AnchorPane();
        //anchorPane.setOnMouseClicked();

        //anchorPane.setBackground(new Background(backgroundImage));
        Scene scene = new Scene(anchorPane, 1200, 1000);

        // set the scene
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
        Alert alert = new Alert(INFORMATION, "Delete " + "me" + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult()==ButtonType.YES)

        {
            //do stuff
            System.out.println("farewell");
        }
        System.out.println(stage.getWidth());
        System.out.println(stage.getHeight());
    }

}
