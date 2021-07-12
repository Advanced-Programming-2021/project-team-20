package project.client.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GroupAddingRemovingProblem extends Application {
    private static Stage stage;
    private static AnchorPane anchorPane;
    private static Group group1;
    private static Group group2;

    @Override
    public void start(Stage stage) {

        GroupAddingRemovingProblem.stage = stage;
        //Image backgroundColor = new Image(DuelView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm());

        //BackgroundSize backgroundSize = new BackgroundSize(300, 500, true, true, true, false);
        //BackgroundImage backgroundImage = new BackgroundImage(backgroundColor, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        stage.setTitle("Duel Page");
        anchorPane = new AnchorPane();
        //anchorPane.setOnMouseClicked();
        anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                singleClickAction();

            }
        });
        //anchorPane.setBackground(new Background(backgroundImage));
        Scene scene = new Scene(anchorPane, 1000, 1000);

        // set the scene
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.show();
        prepareObjectsForWorking();
        anchorPane.getChildren().add(group1);
        anchorPane.getChildren().add(group2);
        System.out.println(stage.getWidth());
        System.out.println(stage.getHeight());
    }


    private void prepareObjectsForWorking() {
        group1 = new Group();
        group2 = new Group();
        group1.getChildren().add(new Rectangle(10, 10));
        group1.getChildren().add(new Rectangle(20, 40));
        group1.getChildren().add(new Rectangle(30, 60));
    }

    private void singleClickAction() {
        Rectangle rectangle = (Rectangle) group1.getChildren().get(group1.getChildren().size() - 1);
        System.out.println("1");
        //group1.getChildren().remove(rectangle);
        System.out.println("2");
        group2.getChildren().add(rectangle);
        System.out.println("3");
    }





}

