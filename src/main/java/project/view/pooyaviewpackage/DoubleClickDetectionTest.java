package project.view.pooyaviewpackage;
import java.util.concurrent.ScheduledFuture;
    import java.util.concurrent.ScheduledThreadPoolExecutor;
    import java.util.concurrent.TimeUnit;

    import javafx.application.Application;
    import javafx.event.EventHandler;
    import javafx.scene.Scene;
    import javafx.scene.input.MouseButton;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;

public class DoubleClickDetectionTest extends Application {

    boolean dragFlag = false;

    int clickCounter = 0;

    ScheduledThreadPoolExecutor executor;

    ScheduledFuture<?> scheduledFuture;

    public DoubleClickDetectionTest() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    dragFlag = true;
                }
            }
        });

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (!dragFlag) {
                        System.out.println(++clickCounter + " " + e.getClickCount());
                        if (e.getClickCount() == 1) {
                            scheduledFuture = executor.schedule(() -> singleClickAction(), 500, TimeUnit.MILLISECONDS);
                        } else if (e.getClickCount() > 1) {
                            if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
                                scheduledFuture.cancel(false);
                                doubleClickAction();
                            }
                        }
                    }
                    dragFlag = false;
                }
            }
        });
    }

    @Override
    public void stop() {
        executor.shutdown();
    }

    private void singleClickAction() {
        System.out.println("Single-click action executed.");
    }

    private void doubleClickAction() {
        System.out.println("Double-click action executed.");
    }
}
