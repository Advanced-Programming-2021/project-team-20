package project.client.view.pooyaviewpackage.unused;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

//import java.util.Duration;

import static javafx.scene.paint.Color.*;

public class UpdatingSceneWithDelays extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
        Rectangle rectangle2 = new Rectangle(100, 250, 100, 100);
        Rectangle rectangle3 = new Rectangle(100, 400, 100, 100);
        Button button1 = new Button("cancel");
        Button button2 = new Button("update");
        button2.setLayoutY(100);
        Timeline timeline =
            new Timeline(
                // first rectangle to red (assumes all start as black)
                new KeyFrame(
                    Duration.ZERO,
                    e -> rectangle1.setFill(RED)),
                // first rectangle to black, second to green
                new KeyFrame(
                    Duration.seconds(0.5),
                    e -> {
                        rectangle1.setFill(BLACK);
                        rectangle2.setFill(GREEN);
                    }),
                // second rectangle to black, third to blue
                new KeyFrame(
                    Duration.seconds(1.0), // times don't stack
                    e -> {
                        rectangle2.setFill(BLACK);
                        rectangle3.setFill(BLUE);
                    }));
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                timeline.playFromStart();
            }
        });
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rectangle1.setFill(BLACK);
                rectangle2.setFill(BLACK);
                rectangle3.setFill(BLACK);
            }
        });
//        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                rectangle1.setFill(RED);
//                rectangle2.setFill(BLACK);
//                rectangle3.setFill(BLACK);
//                try {
//                    Thread.sleep(500);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                rectangle1.setFill(BLACK);
//                rectangle2.setFill(GREEN);
//                rectangle3.setFill(BLACK);
//                try {
//                    Thread.sleep(500);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                rectangle1.setFill(BLACK);
//                rectangle2.setFill(BLACK);
//                rectangle3.setFill(BLUE);
//            }
//        });
        anchorPane.getChildren().add(rectangle1);
        anchorPane.getChildren().add(rectangle2);
        anchorPane.getChildren().add(rectangle3);
        anchorPane.getChildren().add(button1);
        anchorPane.getChildren().add(button2);
        Scene scene = new Scene(anchorPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}

