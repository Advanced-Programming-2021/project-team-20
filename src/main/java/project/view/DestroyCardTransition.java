//package project.view;
//
//import javafx.animation.Transition;
//import javafx.geometry.Bounds;
//import javafx.scene.image.Image;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Rectangle;
//import javafx.util.Duration;
//import project.model.modelsforview.CardView;
//import javafx.geometry.Bounds;
//import javafx.scene.image.Image;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Rectangle;
//import javafx.util.Duration;
//import project.model.modelsforview.CardView;
//
//import javafx.animation.Transition;
//import javafx.util.Duration;
//import project.view.pooyaviewpackage.DuelView;
//
//import java.util.Locale;
//
//public class DestroyCardTransition extends Transition {
//    private Rectangle transitionRectangle;
//    private double upperLeftX;
//    private double upperLeftY;
//    private boolean shouldBeSeen180DegreesReversed;
//    private CardView cardView;
//
//    public DestroyCardTransition(CardView cardView) {
//        Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
//        // System.out.println("being destroyed is x = " + bounds);
//        // System.out.println("upperleft was " + cardView.getUpperLeftX() + " " + cardView.getUpperLeftY());
//        // System.out.println("being destroyed is x = " + cardView.getX() + " and y = " + cardView.getY());
//        // System.out.println("being destroyed is x = " + cardView.getLayoutX() + " and y = " + cardView.getLayoutY());
//        // System.out.println("being destroyed is x = " + cardView.getScaleX() + " and y = " + cardView.getScaleY());
//        // System.out.println("being destroyed is x = " + cardView.getScene().getX() + " and y = " + cardView.getScene().getY());
//        this.transitionRectangle = new Rectangle(bounds.getMinX(), bounds.getMinY(), CardView.getCardWidth(), CardView.getCardHeight());
//        this.upperLeftX = cardView.getUpperLeftX();
//        this.upperLeftY = cardView.getUpperLeftY();
//        this.shouldBeSeen180DegreesReversed = cardView.isShouldBeSeen180DegreesReversed();
//        this.cardView = cardView;
//        setCycleDuration(Duration.millis(500));
//        DuelView.getAnchorPane().getChildren().add(transitionRectangle);
//    }
//
//    @Override
//    protected void interpolate(double v) {
//        int frame = (int) Math.floor(v * 47) + 1;
//        if (frame == 27) {
//            cardView.setShouldBeInvisible(true);
//        }
//     //   System.out.println("/project/ingameicons/breaking/" + frame + ".png");
//        transitionRectangle.setFill(new ImagePattern(new Image(DestroyCardTransition.class.getResource("/project/ingameicons/breaking/" + frame + ".png").toExternalForm())));
//    }
//
//    public CardView getCardView() {
//        return cardView;
//    }
//}
