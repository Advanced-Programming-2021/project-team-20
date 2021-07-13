package project.client.view.pooyaviewpackage;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import project.client.modelsforview.CardView;

import javafx.animation.Transition;

public class DestroyCardTransition extends Transition {
    private Rectangle transitionRectangle;
    private boolean shouldBeSeen180DegreesReversed;
    private CardView cardView;
    private boolean firstTime;

    public DestroyCardTransition(CardView cardView, boolean defensePosition) {
        Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
        System.out.println("being destroyed is x = " + bounds);
        System.out.println("upperleft was " + cardView.getUpperLeftX() + " " + cardView.getUpperLeftY());
        System.out.println("being destroyed is x = " + cardView.getX() + " and y = " + cardView.getY());
        System.out.println("being destroyed is x = " + cardView.getLayoutX() + " and y = " + cardView.getLayoutY());
        System.out.println("being destroyed is x = " + cardView.getScaleX() + " and y = " + cardView.getScaleY());
        System.out.println("being destroyed is x = " + cardView.getScene().getX() + " and y = " + cardView.getScene().getY());
        if (defensePosition) {
            this.transitionRectangle = new Rectangle(bounds.getMinX()+20, bounds.getMinY()-20, CardView.getCardWidth(), CardView.getCardHeight());
            transitionRectangle.setRotate(90);
        } else {
            this.transitionRectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(), CardView.getCardWidth(), CardView.getCardHeight());
        }
       this.transitionRectangle.setFill(new ImagePattern(new Image(DestroyCardTransition.class.getResource("/project/ingameicons/breaking/" + 1 + ".png").toExternalForm())));

        this.shouldBeSeen180DegreesReversed = cardView.isShouldBeSeen180DegreesReversed();
        this.cardView = cardView;
        this.firstTime = true;

        setCycleDuration(Duration.millis(500));
        DuelView.getAnchorPane().getChildren().add(transitionRectangle);
    }

    @Override
    protected void interpolate(double v) {
        int frame = (int) Math.floor(v * 47) + 1;
        if (firstTime && frame >= 27) {
            firstTime = false;
            cardView.setShouldBeInvisible(true);
        }
  //      System.out.println("/project/ingameicons/breaking/" + frame + ".png");
        transitionRectangle.setFill(new ImagePattern(new Image(DestroyCardTransition.class.getResource("/project/ingameicons/breaking/" + frame + ".png").toExternalForm())));
    }

    public CardView getCardView() {
        return cardView;
    }
}
