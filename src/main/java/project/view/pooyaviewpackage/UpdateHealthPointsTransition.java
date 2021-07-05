package project.view.pooyaviewpackage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javafx.animation.Transition;
import javafx.util.Duration;

public class UpdateHealthPointsTransition extends Transition {
    private HealthBarAndHealthPoints healthBarAndHealthPoints;
    private int previousHealth;
    private int increaseInHealth;

    public UpdateHealthPointsTransition(HealthBarAndHealthPoints healthBarAndHealthPoints, int previousHealth) {
        this.healthBarAndHealthPoints = healthBarAndHealthPoints;
        this.previousHealth = previousHealth;
        this.increaseInHealth = 0;
        setCycleDuration(Duration.millis(400));
    }

    @Override
    protected void interpolate(double v) {
        int health = (int) Math.floor(previousHealth + v * (increaseInHealth));
        int fourthDigit = (int) Math.floor(health / 1000);
        int thirdDigit = (int) Math.floor((health - fourthDigit * 1000) / 100);
        int secondDigit = (int) Math.floor((health - fourthDigit * 1000 - thirdDigit * 100) / 10);
        int firstDigit = (int) Math.floor(health - fourthDigit * 1000 - thirdDigit * 100 - secondDigit * 10);
        healthBarAndHealthPoints.updateDigit(1, fourthDigit);
        healthBarAndHealthPoints.updateDigit(2, thirdDigit);
        healthBarAndHealthPoints.updateDigit(3, secondDigit);
        healthBarAndHealthPoints.updateDigit(4, firstDigit);
        this.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                previousHealth += increaseInHealth;
                increaseInHealth = 0;
            }
        });
    }

    public int getPreviousHealth() {
        return previousHealth;
    }

    public int getIncreaseInHealth() {
        return increaseInHealth;
    }

    public void setIncreaseInHealth(int increaseInHealth) {
        this.increaseInHealth = increaseInHealth;
    }
}
