package project.client.view.pooyaviewpackage.unused;

import javafx.animation.FillTransition;
import javafx.animation.Timeline;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.scene.shape.Circle;

import javafx.stage.Stage;

import javafx.util.Duration;

public class FillTDemo extends Application {
    final static int SCENE_WIDTH = 300;
    final static int SCENE_HEIGHT = 300;

    @Override
    public void start(Stage primaryStage) {
        Stop[] stops = new Stop[]
            {
                new Stop(0, Color.BLUE),
                new Stop(1, Color.DARKBLUE)
            };
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true,
            CycleMethod.NO_CYCLE,
            stops);

        Group root = new Group();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, lg);

        Circle circle = new Circle();
        circle.centerXProperty().bind(scene.widthProperty().divide(2));
        circle.centerYProperty().bind(scene.heightProperty().divide(2));
        circle.setRadius(100);
        circle.setFill(Color.YELLOW);

        root.getChildren().add(circle);

        FillTransition ft = new FillTransition();
        ft.setShape(circle);
        ft.setDuration(new Duration(8000));
        ft.setToValue(Color.GOLD);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        primaryStage.setTitle("FillTransition Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
