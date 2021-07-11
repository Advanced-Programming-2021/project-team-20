package project.model.modelsforview;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.client.view.pooyaviewpackage.DuelView;

public class InformationRectangle extends Group {
    private double upperLeftX;
    private double upperLeftY;
    private Label label;
    private Rectangle layoutRectangle;

    public InformationRectangle() {
        super();
        layoutRectangle = new Rectangle(DuelView.getStageWidth() / 3, DuelView.getStageHeight() * 4 / 9, DuelView.getStageWidth() / 2, DuelView.getStageHeight() / 9);
        layoutRectangle.setFill(Color.AQUA);
        label = new Label();
        label.setText("Hallo!");
        label.setLayoutX(DuelView.getStageWidth() / 3 + DuelView.getStageWidth()/4);
        label.setLayoutY(DuelView.getStageHeight() * 4 / 9);
        this.upperLeftX = DuelView.getStageWidth() / 3;
        this.upperLeftY = DuelView.getStageHeight() * 4 / 9;
        this.getChildren().add(layoutRectangle);
        this.getChildren().add(label);
    }

    public double getUpperLeftX() {
        return upperLeftX;
    }

    public double getUpperLeftY() {
        return upperLeftY;
    }

    public void move(double x, double y) {
        this.upperLeftX += y;
        this.upperLeftX += x;
        this.label.setLayoutX(this.label.getLayoutX() + x);
        this.label.setLayoutY(this.label.getLayoutY() + y);
    }
}
