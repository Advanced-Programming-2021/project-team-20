package project.client.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ContextMenuDemo extends Application {

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();

        Label label = new Label();

        Circle circle = new Circle();
        circle.setRadius(80);
        circle.setFill(Color.AQUA);

        VBox root = new VBox();
        root.setPadding(new Insets(5));
        root.setSpacing(5);

        root.getChildren().addAll(label, circle);

        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Menu Item 1");
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                label.setText("Select Menu Item 1");
            }
        });
        MenuItem item2 = new MenuItem("Menu Item 2");
        item2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                label.setText("Select Menu Item 2");
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2);

        // When user right-click on Circle
        circle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {

                contextMenu.show(circle, event.getScreenX(), event.getScreenY());
            }
        });
        anchorPane.getChildren().addAll(root);
        Scene scene = new Scene(anchorPane, 400, 200);

        stage.setTitle("JavaFX ContextMenu (o7planning.org)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
