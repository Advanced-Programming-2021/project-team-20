package project.client.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.text.Text.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class contextMenu_1 extends Application {
    // labels
    Label l;

    // launch the application
    public void start(Stage stage) {
        // set title for the stage
        stage.setTitle("creating contextMenu ");

        // create a label
        Label label1 = new Label("This is a ContextMenu example ");

        // create a menu
        ContextMenu contextMenu = new ContextMenu();

        // create menuitems
        MenuItem menuItem1 = new MenuItem("menu item 1");
        MenuItem menuItem2 = new MenuItem("menu item 2");
        MenuItem menuItem3 = new MenuItem("menu item 3");

        // add menu items to menu
        contextMenu.getItems().add(menuItem1);
        contextMenu.getItems().add(menuItem2);
        contextMenu.getItems().add(menuItem3);

        // create a tilepane
        TilePane tilePane = new TilePane(label1);

        // setContextMenu to label
        label1.setContextMenu(contextMenu);

        // create a scene
        Scene sc = new Scene(tilePane, 200, 200);

        // set the scene
        stage.setScene(sc);

        stage.show();
    }

    public static void main(String args[]) {
        // launch the application
        launch(args);
    }
}
