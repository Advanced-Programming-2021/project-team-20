package project.view;

// import javafx.application.Application;
// import javafx.geometry.*;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.stage.Stage;

// public class Hoverboard extends Application {

//     public class TextChooser extends StackPane {
//         private Label label = new Label();
//         private ComboBox<String> combo = new ComboBox<>();

//         public TextChooser(String... options) {
//             StackPane.setAlignment(label, Pos.CENTER_LEFT);
//             StackPane.setAlignment(combo, Pos.CENTER_LEFT);

//             label.textProperty().bind(
//             combo.getSelectionModel().selectedItemProperty()
//             );
//             label.visibleProperty().bind(
//                 combo.visibleProperty().not()
//             );
//             label.setPadding(new Insets(0, 0, 0, 9));

//             combo.getItems().setAll(options);
//             combo.getSelectionModel().select(0);
//             combo.setVisible(false);

//             label.setOnMouseEntered(event -> combo.setVisible(true));
//             combo.showingProperty().addListener(observable -> {
//                 System.out.println("ssssss");
//                 if (!combo.isShowing()) {
//                     combo.setVisible(false);
//                 }
//             });
//             combo.setOnMouseExited(event -> {
//                 if (!combo.isShowing()) {
//                     combo.setVisible(false);
//                 }
//             });

//             getChildren().setAll(label, combo);
//         }
//     }

//     @Override
//     public void start(Stage stage) throws Exception {
//         TextChooser textChooser = new TextChooser(
//             "xyzzy", "frobozz", "foobar"
//         );

//         VBox layout = new VBox(textChooser);
//         layout.setPadding(new Insets(10));

//         stage.setScene(new Scene(layout));
//         stage.show();
//     }

//     public static void main(String[] args) {
//         launch(Hoverboard.class);
//     }

// }

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class DialogsApp extends Application {

    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };

    private Parent createContent() {
        VBox box = new VBox();
        box.setPrefSize(800, 600);
        Button btn1 = new Button("Open JavaFX dialog");
        btn1.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Header");
            alert.setContentText("Content");
            alert.show();
        });

        Button btn2 = new Button("Open Custom dialog");
        btn2.setOnAction(e -> {
            CustomDialog dialog = new CustomDialog("Header", "Content");
            dialog.openDialog();
        });

        box.getChildren().addAll(btn1, btn2);

        return box;
    }

    private static class CustomDialog extends Stage {

        private ScaleTransition scale1 = new ScaleTransition();
        private ScaleTransition scale2 = new ScaleTransition();

        private SequentialTransition anim = new SequentialTransition(scale1, scale2);

        CustomDialog(String header, String content) {
            Pane root = new Pane();
            // BackgroundFill background_fill = new BackgroundFill(Color.PINK, 
            //                               CornerRadii.EMPTY, Insets.EMPTY);
  
            // // create Background
            // Background background = new Background(background_fill);
            // root.setBackground(background);
            scale1.setFromX(0.01);
            scale1.setFromY(0.01);
            scale1.setToY(1.0);
            scale1.setDuration(Duration.seconds(0.33));
            scale1.setInterpolator(EXP_IN);
            scale1.setNode(root);

            scale2.setFromX(0.01);
            scale2.setToX(1.0);
            scale2.setDuration(Duration.seconds(0.33));
            scale2.setInterpolator(EXP_OUT);
            scale2.setNode(root);

            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);

            Rectangle bg = new Rectangle(350, 150, Color.GRAY);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1.5);

            Text headerText = new Text(header);
            headerText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));

            Text contentText = new Text(content);
            contentText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));

            VBox box = new VBox(10,
                    headerText,
                    new Separator(Orientation.HORIZONTAL),
                    contentText
            );
            box.setPadding(new Insets(15));

            Button btn = new Button("OK");
            btn.getStyleClass().add("C");
            btn.setTranslateX(bg.getWidth() - 75);
            btn.setTranslateY(bg.getHeight() - 75);
            btn.setOnAction(e -> closeDialog());
            btn.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));

            root.getChildren().addAll(bg, box, btn);

            setScene(new Scene(root, null));
        }

        void openDialog() {
            show();
            anim.play();
        }

        void closeDialog() {
            anim.setOnFinished(e -> close());
            anim.setAutoReverse(true);
            anim.setCycleCount(2);
            anim.playFrom(Duration.seconds(0.66));
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
stage.setScene(new Scene(createContent()));
                stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}