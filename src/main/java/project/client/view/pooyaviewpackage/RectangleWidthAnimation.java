package project.client.view.pooyaviewpackage;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

//
//public class RectangleWidthAnimation extends Application {
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Rectangle statusBar = new Rectangle(100, 100, 300, 100);
//        Button animationButton = new Button("Animate width decrease by 25");
//        animationButton.setOnAction(event -> {
//            System.out.println("Animation start: width = " + statusBar.getWidth());
//            KeyValue widthValue = new KeyValue(statusBar.widthProperty(), statusBar.getWidth() + 25);
//
//            KeyFrame frame = new KeyFrame(Duration.seconds(0.4), widthValue);
//            Timeline timeline = new Timeline(frame);
//            timeline.play();
//            timeline.setOnFinished(finishedEvent -> System.out.println("Animation end: width = " + statusBar.getWidth()));
//        });
//
//        VBox container = new VBox(statusBar);
//        container.setLayoutX(100);
//        container.setLayoutY(100);
//        container.setMaxWidth(300);
//        container.setMaxHeight(100);
//        //Experiment with these alignments
//        //Group group = new Group();
//        //group.getChildren().add(container);
//        //container.setAlignment(Pos.CENTER);
//        //container.setAlignment(Pos.CENTER_LEFT);
//        container.setAlignment(Pos.CENTER_RIGHT);
//
//        primaryStage.setScene(new Scene(new Group(container,animationButton), 500, 500));
//        primaryStage.show();
//    }
//}
public class RectangleWidthAnimation extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle statusBar = new Rectangle(0, 200);
        Button animationButton = new Button("Animate width increase by 25");
        animationButton.setOnAction(event -> {
            ParallelTransition parallelTransition = new ParallelTransition();
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.001));
            parallelTransition.getChildren().add(pauseTransition);
            parallelTransition.play();
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Animation start: width = " + statusBar.getWidth());
                    KeyValue widthValue = new KeyValue(statusBar.widthProperty(), statusBar.getWidth() + 25);
                    KeyFrame frame = new KeyFrame(Duration.seconds(0.4), widthValue);
                    Timeline timeline = new Timeline(frame);
                    timeline.play();
                    timeline.setOnFinished(finishedEvent -> System.out.println("Animation end: width = " + statusBar.getWidth()));
                }
            });

        });
        VBox container = new VBox(statusBar);
        //container.setLayoutX(100);
        container.setPrefWidth(425);
        container.setLayoutY(100);
        container.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(container);
        anchorPane.getChildren().add(animationButton);
        primaryStage.setScene(new Scene(anchorPane, 500, 350));
        primaryStage.show();
    }
}
