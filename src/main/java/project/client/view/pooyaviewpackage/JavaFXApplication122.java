package project.client.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class JavaFXApplication122 extends Application {

    double orgSceneX, orgSceneY;//Used to help keep up with change in mouse position

    @Override
    public void start(Stage primaryStage) {
        double RATIO = .5;//The ration of height to width is 1/2

        Rectangle rectangle = new Rectangle(100, 50);
        rectangle.setX(400 - 50);
        rectangle.setY(250 - 25);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);

        //Circles will be used to do the event handling/movements
        Circle leftAnchor = new Circle(400 - 50, 250, 5);
        Circle topAnchor = new Circle(400, 250 - 25, 5);

        leftAnchor.setOnMouseDragEntered((event) -> {
            ((Circle) event.getSource()).getScene().setCursor(Cursor.MOVE);
        });
        leftAnchor.setOnMousePressed((event) -> {
            orgSceneX = event.getSceneX();//Store current mouse position
        });
        leftAnchor.setOnMouseDragged((event) -> {
            double offSetX = event.getSceneX() - orgSceneX;//Find change in mouse X position

            leftAnchor.setCenterX(event.getSceneX());
            rectangle.setX(event.getSceneX());//move rectangle left side with mouse
            rectangle.setWidth(rectangle.getWidth() - offSetX);//Change rectangle's width with movement of mouse
            topAnchor.setCenterX(topAnchor.getCenterX() + offSetX / 2);//Adjust top circle as rectangle's size change
            rectangle.setHeight(rectangle.getWidth() * RATIO);//Change the height so that it meets the ratio requirements
            leftAnchor.setCenterY((rectangle.getY() + rectangle.getHeight()) - (rectangle.getHeight() / 2));//Adjust the left circle with the growth of the rectangle

            orgSceneX = event.getSceneX();//save last mouse position to recalculate change in mouse postion as the circle moves
        });
        leftAnchor.setOnMouseExited((event) -> {
            leftAnchor.getScene().setCursor(null);
        });

        topAnchor.setOnMouseDragEntered((event) -> {
            topAnchor.getScene().setCursor(Cursor.MOVE);
        });
        topAnchor.setOnMousePressed((event) -> {
            orgSceneY = event.getSceneY();//store current mouse position
        });
        topAnchor.setOnMouseDragged((event) -> {
            double offSetY = event.getSceneY() - orgSceneY;

            topAnchor.setCenterY(event.getSceneY());
            rectangle.setY(event.getSceneY());//move rectangle top side with mouse
            rectangle.setHeight(rectangle.getHeight() - offSetY);//Change rectangle's height with movement of mouse
            leftAnchor.setCenterY(leftAnchor.getCenterY() + offSetY / 2);//Adjust left circle as rectangle's size change
            rectangle.setWidth(rectangle.getHeight() * (1 / RATIO));//Change the width so that it meets the ratio requirements
            topAnchor.setCenterX((rectangle.getX() + rectangle.getWidth()) - (rectangle.getWidth() / 2));//Adjust the top circle with the growth of the rectangle

            orgSceneY = event.getSceneY();//save last mouse position to recalculate change in mouse postion as the circle moves
        });
        topAnchor.setOnMouseExited((event) -> {
            topAnchor.getScene().setCursor(null);
        });

        Pane root = new Pane();
        root.getChildren().addAll(rectangle, leftAnchor, topAnchor);

        Scene scene = new Scene(root, 800, 500);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
