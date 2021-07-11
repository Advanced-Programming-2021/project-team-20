package project.view.transitions;

import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class RockPaperScissorTransition extends Transition {

    private double speed = 0.5;
    private Rectangle rectangle;
    private double dx;
    private double dy;

    public RockPaperScissorTransition(Rectangle rectangle) {
        this.rectangle = rectangle;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double arg0) {

        if (rectangle.getY() == 124) {
            if (rectangle.getX() == 649) {
                dx = 0;
                dy = speed;
            } else {
                dy = 0;
                dx = speed;
            }
        }
        if (rectangle.getY() == 453) {
            if (rectangle.getX() == 256) {
                dx = 0;
                dy = -speed;
            } else {
                dy = 0;
                dx = -speed;
            }
        }

        rectangle.setX(rectangle.getX() + dx);
        rectangle.setY(rectangle.getY() + dy);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

}
