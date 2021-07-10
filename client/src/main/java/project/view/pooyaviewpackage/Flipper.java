package project.view.pooyaviewpackage;


import javafx.application.Application;
    import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Flipper extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Scene scene = new Scene(1000, 1000);
        Canvas canvas = new Canvas(300, 300);
        Image image = new Image("https://i.stack.imgur.com/ySbuj.png");
        double xoff = 15;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        //g.drawImage(RESOURCE, 0, 0, RESOURCE.getWidth(), RESOURCE.getHeight(), RESOURCE.getWidth(),0,-RESOURCE.getWidth(),RESOURCE.getHeight());
        //gc.drawImage(image, 0.0 + image.getWidth(), 0.0, -image.getWidth(), image.getHeight());
        gc.drawImage(image, 0+image.getWidth(), 0 + image.getHeight(), -image.getWidth(), -image.getHeight());
        //Rectangle rectangle = new Rectangle(0,0);
        //rectangle.getGr
//        gc.save();
//        gc.translate(image.getWidth() + xoff * 2, 0);
//        gc.scale(-1, 1);
//        gc.drawImage(image, xoff, 0);
//        gc.restore();
//        gc.drawImage(image, xoff, 0);
        Rectangle rectangle = new Rectangle(100, 100);
        rectangle.setFill(new ImagePattern(new Image(Flipper.class.getResource("/project/cards/spelltraps/MirrorForce.jpg").toExternalForm()), 100, 100, -100, -100, false));
        stage.setScene(new Scene(new Group(rectangle)));
        stage.show();
    }
}
