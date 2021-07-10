package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Gradient extends Application {

    public static final double S = 100;

    @Override
    public void start(Stage stage) {
        Stop[] stops = new Stop[] {
            new Stop(0, Color.BLUE),
            new Stop(1, Color.RED)
        };
        LinearGradient gradient = new LinearGradient(
            0, 0,
            1, 0,
            true,
            CycleMethod.NO_CYCLE,
            stops
        );

        Rectangle rectangle = new Rectangle(S, S, gradient);

        stage.setScene(new Scene(new Group(rectangle)));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
