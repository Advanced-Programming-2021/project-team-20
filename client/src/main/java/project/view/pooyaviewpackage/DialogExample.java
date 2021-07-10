package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

public class DialogExample extends Application {
    @Override
    public void start(Stage stage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        VBox vbox = new VBox(new Text("Hi"), new Button("Ok."));

        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
//        AlertExample.stage = stage;
//        //Image backgroundColor = new Image(DuelView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm());
//
//        //BackgroundSize backgroundSize = new BackgroundSize(300, 500, true, true, true, false);
//        //BackgroundImage backgroundImage = new BackgroundImage(backgroundColor, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//
//        stage.setTitle("Duel Page");
//        anchorPane = new AnchorPane();
//        //anchorPane.setOnMouseClicked();
//
//        //anchorPane.setBackground(new Background(backgroundImage));
//        Scene scene = new Scene(anchorPane, 1200, 1000);
//
//        // set the scene
//        stage.setScene(scene);
//        //stage.setFullScreen(true);
//        stage.show();
//        Alert alert = new Alert(CONFIRMATION, "Delete " + "me" + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
//        alert.showAndWait();
//
//        if(alert.getResult()==ButtonType.YES)
//
//        {
//            //do stuff
//            System.out.println("farewell");
//        }
//        System.out.println(stage.getWidth());
//        System.out.println(stage.getHeight());
    }
}
