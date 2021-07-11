package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ApplicationExample extends Application {
    private static Stage stage;
    private static AnchorPane anchorPane;
    private static NodeInApplication draggingObject;
    private boolean dragFlag = false;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture;

    public ApplicationExample() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    @Override
    public void start(Stage stage) {
        ApplicationExample.stage = stage;
        stage.setTitle("Application Page");
        anchorPane = new AnchorPane();

        anchorPane.setOnDragOver(e -> {
            if (draggingObject != null &&
                e.getDragboard().hasString()
                && e.getDragboard().getString().equals("node")) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        anchorPane.setOnDragDropped(e -> {
            if (draggingObject != null &&
                e.getDragboard().hasString()
                && e.getDragboard().getString().equals("node")) {
                System.out.println("drag dropped");
                draggingObject = null;
                e.setDropCompleted(true);
            }
        });

        Scene scene = new Scene(anchorPane, 1200, 1000);

        stage.setScene(scene);
        stage.show();

        for (int i = 0; i < 10; i++) {
            NodeInApplication node = new NodeInApplication(i);
            node.applyClickingAbilitiesToCardView(this);
            node.applyDragDetectingAbilityToCardView();
            anchorPane.getChildren().add(node);
        }
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public boolean isDragFlag() {
        return dragFlag;
    }

    public void setDragFlag(boolean dragFlag) {
        this.dragFlag = dragFlag;
    }

    public static void setDraggingObject(NodeInApplication draggingObject) {
        ApplicationExample.draggingObject = draggingObject;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public void singleClickAction(MouseEvent mouseEvent, NodeInApplication card) {
        System.out.println("single click action");
    }

    public void doubleClickAction(MouseEvent mouseEvent) {
        System.out.println("double click action");
    }
}

class NodeInApplication extends Rectangle {

    public NodeInApplication(int i) {
        super(10, 10 + 50 * i, 10, 10);
    }

    public void applyClickingAbilitiesToCardView(ApplicationExample applicationExample) {
        NodeInApplication node = this;
        node.addEventHandler(
            MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        if (!applicationExample.isDragFlag()) {
                            if (e.getClickCount() == 1) {
                                applicationExample.setScheduledFuture(applicationExample.getExecutor().schedule(() -> applicationExample.singleClickAction(e, node), 300, TimeUnit.MILLISECONDS));
                            } else if (e.getClickCount() > 1) {
                                if (applicationExample.getScheduledFuture() != null && !applicationExample.getScheduledFuture().isCancelled() && !applicationExample.getScheduledFuture().isDone()) {
                                    applicationExample.getScheduledFuture().cancel(false);
                                    applicationExample.doubleClickAction(e);
                                }
                            }
                        }
                        applicationExample.setDragFlag(false);
                    }
                }
            }
        );

    }

    public void applyDragDetectingAbilityToCardView() {
        NodeInApplication node = this;
        node.setOnDragDetected(e -> {
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(node.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.putString("node");
            db.setContent(cc);
            System.out.println("node being drag detected");
            ApplicationExample.setDraggingObject(node);
        });
    }

}
