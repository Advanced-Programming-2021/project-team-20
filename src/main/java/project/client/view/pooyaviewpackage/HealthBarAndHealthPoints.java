package project.client.view.pooyaviewpackage;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class HealthBarAndHealthPoints {
    private ArrayList<Rectangle> healthDigits;
    private Rectangle healthBar;
    private boolean isForAlly;
    private Rectangle helpfulHealthRectangle;
    private Rectangle backGroundRectangle;
    private UpdateHealthPointsTransition updateHealthPointsTransition;
    private static int widthOfHealthBar = 290;
    private static int heightOfHealthBar = 50;
    private static int widthDistanceOfBackgroundHealthBar = 5;
    private static int heightDistanceOfBackgroundHealthBar = 5;
    private static int maximumAllowedHeightForHealthBar = 75;
    private static int digitHeight = 65;
    private static int upperLeftXOfHelpfulHealthBar = widthOfHealthBar + widthDistanceOfBackgroundHealthBar;
    private double upperLeftYOfHelpfulHealthBar;
    private VBox container;

    public HealthBarAndHealthPoints(boolean isForAlly, int previousHealth) {
        Stop[] stops = new Stop[]{
            new Stop(0, Color.RED),
            new Stop(1, Color.GREEN)
        };
        LinearGradient gradient = new LinearGradient(
            0, 0,
            1, 0,
            true,
            CycleMethod.NO_CYCLE,
            stops
        );
        //double correctStageHeight = 2 * DuelView.getBattleFieldView().getUpperLeftY() + DuelView.getBattleFieldView().getHeight();
        double correctStageHeight = DuelView.getBattleFieldView().getUpperLeftY() + DuelView.getBattleFieldView().getHeight();
        double correctStageWidth = DuelView.getBattleFieldView().getUpperLeftX();
        this.isForAlly = isForAlly;
        updateHealthPointsTransition = new UpdateHealthPointsTransition(this, previousHealth);
        healthBar = new Rectangle(widthOfHealthBar, heightOfHealthBar, gradient);
        int widthOfBackground = widthDistanceOfBackgroundHealthBar * 2 + widthOfHealthBar;
        int heightOfBackground = heightDistanceOfBackgroundHealthBar * 2 + heightOfHealthBar;
        backGroundRectangle = new Rectangle(widthOfBackground, heightOfBackground);
        backGroundRectangle.setX(0);
        backGroundRectangle.setY(isForAlly ? correctStageHeight + digitHeight : digitHeight);
        backGroundRectangle.setArcHeight(3);
        backGroundRectangle.setArcWidth(3);
        healthBar.setX(5);
        healthBar.setY(isForAlly ? correctStageHeight + digitHeight + 5 : digitHeight + 5);
        upperLeftYOfHelpfulHealthBar = (isForAlly ? correctStageHeight + digitHeight + heightDistanceOfBackgroundHealthBar : digitHeight + heightDistanceOfBackgroundHealthBar);
        helpfulHealthRectangle = new Rectangle(0, heightOfHealthBar);
        backGroundRectangle.setViewOrder(4);
        helpfulHealthRectangle.setViewOrder(2);
        healthBar.setViewOrder(3);
        healthDigits = new ArrayList<>();
        healthDigits.add(new Rectangle(100, (isForAlly ? correctStageHeight : 0), 50, digitHeight));
        healthDigits.add(new Rectangle(150, (isForAlly ? correctStageHeight : 0), 50, digitHeight));
        healthDigits.add(new Rectangle(200, (isForAlly ? correctStageHeight : 0), 50, digitHeight));
        healthDigits.add(new Rectangle(250, (isForAlly ? correctStageHeight : 0), 50, digitHeight));
        healthDigits.get(0).setFill(new ImagePattern(new Image(HealthBarAndHealthPoints.class.getResource("/project/ingameicons/numbers/8.png").toExternalForm())));
        healthDigits.get(1).setFill(new ImagePattern(new Image(HealthBarAndHealthPoints.class.getResource("/project/ingameicons/numbers/0.png").toExternalForm())));
        healthDigits.get(2).setFill(new ImagePattern(new Image(HealthBarAndHealthPoints.class.getResource("/project/ingameicons/numbers/0.png").toExternalForm())));
        healthDigits.get(3).setFill(new ImagePattern(new Image(HealthBarAndHealthPoints.class.getResource("/project/ingameicons/numbers/0.png").toExternalForm())));
        container = new VBox(helpfulHealthRectangle);
        container.setPrefWidth(upperLeftXOfHelpfulHealthBar);
        container.setLayoutY(upperLeftYOfHelpfulHealthBar);
        container.setAlignment(Pos.CENTER_RIGHT);
        //container.setMaxWidth(HealthBarAndHealthPoints.getWidthOfHealthBar());
        //container.setMaxHeight(HealthBarAndHealthPoints.getHeightOfHealthBar());

    }

    public ArrayList<Rectangle> getHealthDigits() {
        return healthDigits;
    }

    public Rectangle getHelpfulHealthRectangle() {
        return helpfulHealthRectangle;
    }

    public Rectangle getHealthBar() {
        return healthBar;
    }

    public static int getWidthOfHealthBar() {
        return widthOfHealthBar;
    }

    public static int getHeightOfHealthBar() {
        return heightOfHealthBar;
    }

    public static int getUpperLeftXOfHelpfulHealthBar() {
        return upperLeftXOfHelpfulHealthBar;
    }

    public double getUpperLeftYOfHelpfulHealthBar() {
        return upperLeftYOfHelpfulHealthBar;
    }

    public VBox getContainer() {
        return container;
    }

    public UpdateHealthPointsTransition getUpdateHealthPointsTransition() {
        return updateHealthPointsTransition;
    }

    public Rectangle getBackGroundRectangle() {
        return backGroundRectangle;
    }

    public void updateDigit(int valueOfDigit, int currentDigit) {
        healthDigits.get(valueOfDigit - 1).setFill(new ImagePattern(new Image(HealthBarAndHealthPoints.class.getResource("/project/ingameicons/numbers/" + currentDigit + ".png").toExternalForm())));
    }
}
