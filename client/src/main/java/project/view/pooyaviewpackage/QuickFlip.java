package project.view.pooyaviewpackage;

import javafx.animation.*;
    import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
    import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
    import javafx.stage.Stage;
    import javafx.util.Duration;

public class QuickFlip extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Rectangle front = new Rectangle(50, 50);

        ScaleTransition stHideFront = new ScaleTransition(Duration.millis(1500), front);
        stHideFront.setFromX(1);
        stHideFront.setToX(0);

        Rectangle back = new Rectangle(50, 50, Color.RED);
        back.setScaleX(0);

        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(1500), front);
        stShowBack.setFromX(0);
        stShowBack.setToX(1);

        stHideFront.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stShowBack.play();
            }
        });

        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(front, back);
        Scene scene = new Scene(root, 300, 250);
        stage.setScene(scene);

        stage.show();
        stHideFront.play();
    }

    private Scene createScene(Node card) {
        StackPane root = new StackPane();
        root.getChildren().addAll(card);

        Scene scene = new Scene(root, 600, 700, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());

        return scene;
    }

    private Node createCard() {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(new ImagePattern(new Image(QuickFlip.class.getResource("/project/cards/card_back.png").toExternalForm())));
        return rectangle;
    }

    private RotateTransition createRotator(Node card) {
        RotateTransition rotator = new RotateTransition(Duration.millis(10000), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(10);

        return rotator;
    }

    public static void main(String[] args) {
        launch();
    }
}
