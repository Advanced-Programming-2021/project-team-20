package project.client.view.pooyaviewpackage.unused;

    import javafx.animation.*;
    import javafx.application.Application;
    import javafx.geometry.Insets;
    import javafx.scene.Scene;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.Circle;
    import javafx.scene.shape.Rectangle;
    import javafx.scene.text.Font;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;
    import javafx.util.Duration;

//public class FxTransitionExamples extends Application
//{
//    public static void main(String[] args)
//    {
//        Application.launch(args);
//    }
//
//    @Override
//    public void start(Stage stage)
//    {
//        // Create a green Rectangle
//        Rectangle rect = new Rectangle(400, 200, Color.GREEN);
//        // Create the HBox
//        HBox root = new HBox(rect);
//        // Set the Style-properties of the HBox
//        root.setStyle("-fx-padding: 10;" +
//            "-fx-border-style: solid inside;" +
//            "-fx-border-width: 2;" +
//            "-fx-border-insets: 5;" +
//            "-fx-border-radius: 5;" +
//            "-fx-border-color: blue;");
//
//        // Create the Scene
//        Scene scene = new Scene(root);
//        // Add the Scene to the Stage
//        stage.setScene(scene);
//        // Set the Title of the Stage
//        stage.setTitle("A Fade-in and Fade-out Transition Example");
//        // Display the Stage
//        stage.show();
//
//        // Set up a fade-in and fade-out animation for the rectangle
//        FadeTransition trans = new FadeTransition(Duration.seconds(2), rect);
//        trans.setFromValue(1.0);
//        trans.setToValue(.20);
//        // Let the animation run forever
//        trans.setCycleCount(FadeTransition.INDEFINITE);
//        // Reverse direction on alternating cycles
//        trans.setAutoReverse(true);
//        // Play the Animation
//        trans.play();
//    }
//}
//
//

//    An instance of the FadeTransition class represents a fade-in or fade-out effect for a Node by gradually increasing or decreasing the opacity of the node over the specified duration. The class defines the following properties to specify the animation:
//
//    duration
//    node
//    fromValue
//    toValue
//    byValue
//    The duration property specifies the duration for one cycle of the animation.
//
//    The node property specifies the node whose opacity property is changed.
//
//    The fromValue property specifies the initial value for the opacity. If it is not specified, the current opacity of the node is used.
//
//    The toValue property specifies the opacity end value. The opacity of the node is updated between the initial value and the toValue for one cycle of the animation.
//
//    The byValue property lets you specify the opacity end value differently using the following formula:
//
//    opacity_end_value = opacity_initial_value + byValue;
//    The byValue lets you set the opacity end value by incrementing or decrementing the initial value by an offset. If both toValue and byValue are specified, the toValue is used.
//
//    Suppose you want to set the initial and end opacity of a node between 1.0 and 0.5 in an animation. You can achieve it by setting the fromValue and toValue to 1.0 and 0.50 or by setting fromValue and byValue to 1.0 and -0.50.
//
//    The valid opacity value for a node is between 0.0 and 1.0. It is possible to set FadeTransition properties to exceed the range. The transition takes care of clamping the actual value in the range.
//
//    The following snippet of code sets up a fade-out animation for a Rectangle by changing its opacity from 1.0 to 0.20 in 2 seconds:
//
//// Create a green Rectangle
//    Rectangle rect = new Rectangle(400, 200, Color.GREEN);
//
//// Set up a fade-in and fade-out animation for the rectangle
//    FadeTransition trans = new FadeTransition(Duration.seconds(2), rect);
//    trans.setFromValue(1.0);
//    trans.setToValue(.20);
//// Let the animation run forever
//    trans.setCycleCount(FadeTransition.INDEFINITE);
//// Reverse direction on alternating cycles
//    trans.setAutoReverse(true);
//// Play the Animation
//    trans.play();
//    1.2 The GUI
//    At the beginning, the color is green:
//
//    A JavaFX Fade Transition Example
//    A JavaFX Fade Transition Example
//
//    After a few seconds, the color has been changed:
//
//    A JavaFX Fade Transition Example
//    A JavaFX Fade Transition Example
//
//    2. The Fill Transition
//    2.1 The Code
//    FxTransitionExample2.java
//
//    import javafx.animation.FillTransition;
//    import javafx.application.Application;
//    import javafx.scene.Scene;
//    import javafx.scene.layout.HBox;
//    import javafx.scene.paint.Color;
//    import javafx.scene.shape.Rectangle;
//    import javafx.stage.Stage;
//    import javafx.util.Duration;

//public  class FxTransitionExamples extends Application
//{
//    public static void main(String[] args)
//    {
//        Application.launch(args);
//    }
//
//    @Override
//    public void start(Stage stage)
//    {
//        // Create the Rectangle
//        Rectangle rect = new Rectangle(400, 200, Color.CYAN);
//        // Create the HBox
//        HBox root = new HBox(rect);
//        // Set the Style-properties of the HBox
//        root.setStyle("-fx-padding: 10;" +
//            "-fx-border-style: solid inside;" +
//            "-fx-border-width: 2;" +
//            "-fx-border-insets: 5;" +
//            "-fx-border-radius: 5;" +
//            "-fx-border-color: blue;");
//
//        // Create the Scene
//        Scene scene = new Scene(root);
//        // Add the Scene to the Stage
//        stage.setScene(scene);
//        // Set the Title of the Stage
//        stage.setTitle("A Fill Transition Example");
//        // Display the Stage
//        stage.show();
//
//        // Set up a fill transition for the rectangle
//        FillTransition fillTransition = new FillTransition(Duration.seconds(2), rect);
//        fillTransition.setFromValue(Color.BLUEVIOLET);
//        fillTransition.setToValue(Color.AZURE);
//        fillTransition.setCycleCount(FillTransition.INDEFINITE);
//        fillTransition.setAutoReverse(true);
//        fillTransition.play();
//    }
//}
//    An instance of the FillTransition class represents a fill transition for a Shape by gradually transitioning the fill property of the Shape between the specified range and duration. The class defines the following properties to specify the Animation:
//
//    duration
//    shape
//    fromValue
//    toValue
//    The duration property specifies the duration for one cycle of the animation.
//
//    The shape property specifies the Shape whose fill property is changed.
//
//    The fromValue property specifies the initial fill color. If it is not specified, the current fill of the shape is used.
//
//    The toValue property specifies the fill end value. The fill of the shape is updated between the initial value and the toValue for one cycle of the animation. The fill property in the Shape class is defined as a Paint. However, the fromValue and toValue are of the type Color. That is, the fill transition works for two Colors, not two Paints.
//
//    The following snippet of code sets up a fill transition for a Rectangle by changing its fill from blue violet to azure in 2 seconds:
//
//// Create the Rectangle
//    Rectangle rect = new Rectangle(400, 200, Color.CYAN);
//
//// Set up a fill transition for the rectangle
//    FillTransition fillTransition = new FillTransition(Duration.seconds(2), rect);
//    fillTransition.setFromValue(Color.BLUEVIOLET);
//    fillTransition.setToValue(Color.AZURE);
//    fillTransition.setCycleCount(FillTransition.INDEFINITE);
//    fillTransition.setAutoReverse(true);
//    fillTransition.play();
//    2.2 The GUI
//    At the beginning the color of the rectangle is blue violet:
//
//    A JavaFX Fill Transition Example
//    A JavaFX Fill Transition Example
//
//    After a little time, the color has been changed:
//
//    A JavaFX Fill Transition Example
//    A JavaFX Fill Transition Example
//
//    3. The Translate Transition
//    3.1 The Code
//    FxTransitionExample3.java
//
//    import javafx.animation.TranslateTransition;
//    import javafx.application.Application;
//    import javafx.scene.Scene;
//    import javafx.scene.layout.VBox;
//    import javafx.scene.text.Font;
//    import javafx.scene.text.Text;
//    import javafx.stage.Stage;
//    import javafx.util.Duration;

public class FxTransitionExamples extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create the Text
        Text text = new Text("A Translate Transition Example");
        text.setFont(Font.font(36));

        // Create the VBox
        VBox root = new VBox(text);
        // Set the Size of the VBox
        root.setPrefSize(500, 100);
        // Set the Style-properties of the VBox
        root.setStyle("-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title
        stage.setTitle("Scrolling Text using a Translate Transition");
        // Display the Stage
        stage.show();

        // Set up a Translate Transition for the Text object
        TranslateTransition trans = new TranslateTransition(Duration.seconds(2), text);
        trans.setFromX(scene.getWidth());
        trans.setToX(-1.0 * text.getLayoutBounds().getWidth());
        // Let the animation run forever
        trans.setCycleCount(TranslateTransition.INDEFINITE);
        // Reverse direction on alternating cycles
        trans.setAutoReverse(true);
        // Play the Animation
        trans.play();
    }
}
//    An instance of the TranslateTransition class represents a translate transition for a Node by gradually changing the translateX, translateY, and translateZ properties of the node over the specified duration.
//
//    The class defines the following properties to specify the animation:
//
//    duration
//    node
//    fromX
//    fromY
//    fromZ
//    toX
//    toY
//    toZ
//    byX
//    byY
//    byZ
//    The duration property specifies the duration for one cycle of the animation.
//
//    The node property specifies the Node whose translateX, translateY, and translateZ properties are changed.
//
//    The initial location of the Node is defined by the (fromX, fromY, fromZ) value. If it is not specified, the current (translateX, translateY, translateZ) value of the Node is used as the initial location.
//
//    The (toX, toY, toZ) value specifies the end location.
//
//    The (byX, byY, byZ) value lets you specify the end location using the following formula:
//
//    translateX_end_value = translateX_initial_value + byX;
//    translateY_end_value = translateY_initial_value + byY;
//    translateZ_end_value = translateZ_initial_value + byZ;
//    If both (toX, toY, toZ) and (byX, byY, byZ) values are specified, the former is used.
//
//    The following snippet of code creates a translate transition in an infinite loop for a Text object by scrolling it across the width of the Scene.
//
//    3.2 The GUI
//    A JavaFX Translate Transition Example
//    A JavaFX Translate Transition Example
//
//    4. The Rotate Transition
//    4.1 The Code
//    FxTransitionExample4.java
//
//    import javafx.animation.RotateTransition;
//    import javafx.application.Application;
//    import javafx.geometry.Insets;
//    import javafx.scene.Scene;
//    import javafx.scene.layout.HBox;
//    import javafx.scene.paint.Color;
//    import javafx.scene.shape.Rectangle;
//    import javafx.stage.Stage;
//    import javafx.util.Duration;

class FxTransitionExample4 extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create a Square
        Rectangle rect = new Rectangle(150, 150, Color.RED);

        // Create the HBox
        HBox root = new HBox(rect);
        // Set the Margin for the HBox
        HBox.setMargin(rect, new Insets(80));

        // Set the Style-properties of the HBox
        root.setStyle("-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("A Rotate Transition Example");
        // Display the Stage
        stage.show();

        // Set up a Rotate Transition the Rectangle
        RotateTransition trans = new RotateTransition(Duration.seconds(2), rect);
        trans.setFromAngle(0.0);
        trans.setToAngle(360.0);
        // Let the animation run forever
        trans.setCycleCount(RotateTransition.INDEFINITE);
        // Reverse direction on alternating cycles
        trans.setAutoReverse(true);
        // Play the Animation
        trans.play();
    }
}
//    An instance of the RotateTransition class represents a rotation transition for a node by gradually changing its rotate property over the specified duration. The rotation is performed around the center of the node along the specified axis. The class defines the following properties to specify the animation:
//
//    duration
//    node
//    axis
//    fromAngle
//    toAngle
//    byAngle
//    The duration property specifies the duration for one cycle of the animation.
//
//    The node property specifies the node whose rotate property is changed.
//
//    The axis property specifies the axis of rotation. If it is unspecified, the value for the rotationAxis property, which defaults to Rotate.Z_AXIS, for the node is used. The possible values are Rotate.X_AXIS, Rotate.Y_AXIS, and Rotate.Z_AXIS.
//
//    The initial angle for the rotation is specified by fromAngle property. If it is unspecified, the value for the rotate property of the node is used as the initial angle.
//
//    The toAngle specifies the end rotation angle.
//
//    The byAngle lets you specify the end rotation angle using the following formula:
//
//    rotation_end_value = rotation_initial_value + byAngle;
//    If both toAngle and byAngle values are specified, the former is used. All angles are specified in degrees. Zero degrees correspond to the 3 o’clock position. Positive values for angles are measured clockwise.
//
//    The following snippet of code creates a rotate transition in an infinite loop for a Rectangle. It rotates the Rectangle in clockwise and anticlockwise directions in alternate cycles.
//
//// Create a Square
//    Rectangle rect = new Rectangle(150, 150, Color.RED);
//
//// Set up a Rotate Transition the Rectangle
//    RotateTransition trans = new RotateTransition(Duration.seconds(2), rect);
//    trans.setFromAngle(0.0);
//    trans.setToAngle(360.0);
//// Let the animation run forever
//    trans.setCycleCount(RotateTransition.INDEFINITE);
//// Reverse direction on alternating cycles
//    trans.setAutoReverse(true);
//// Play the Animation
//    trans.play();
//    4.2 The GUI
//    A JavaFX Rotate Transition Example
//    A JavaFX Rotate Transition Example
//
//    5. The Scale Transition
//    5.1 The Code
//    FxTransitionExample5.java
//
//    import javafx.animation.ScaleTransition;
//    import javafx.application.Application;
//    import javafx.scene.Scene;
//    import javafx.scene.layout.HBox;
//    import javafx.scene.paint.Color;
//    import javafx.scene.shape.Circle;
//    import javafx.stage.Stage;
//    import javafx.util.Duration;

class FxTransitionExample5 extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create the Circle
        Circle circle = new Circle(150, Color.GRAY);
        // Create the HBox
        HBox root = new HBox(circle);
        // Set the Style-properties of the HBox
        root.setStyle("-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("A Scale Transition Example");
        // Display the Stage
        stage.show();

        // Set up a scale transition for the circle
        ScaleTransition trans = new ScaleTransition(Duration.seconds(2), circle);
        trans.setFromX(1.0);
        trans.setToX(0.40);
        trans.setFromY(1.0);
        trans.setToY(0.20);
        // Let the animation run forever
        trans.setCycleCount(ScaleTransition.INDEFINITE);
        // Reverse direction on alternating cycles
        trans.setAutoReverse(true);
        // Play the Animation
        trans.play();
    }
}
