package project.client.view.pooyaviewpackage.unused;

import javafx.animation.Transition;
import javafx.util.Duration;
import project.client.modelsforview.CardView;

public class CardMovingTransition extends Transition {
    private CardView cardView;

    public CardMovingTransition(CardView cardView) {
        this.cardView = cardView;
        this.setCycleDuration(Duration.millis(500));
        this.setCycleCount(1);
    }

    @Override
    protected void interpolate(double v) {
        cardView.moveUpperLeftCoordinate(-1, 0);
    }
}
