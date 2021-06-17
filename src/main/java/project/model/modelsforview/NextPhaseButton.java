package project.model.modelsforview;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.View.DuelView;

public class NextPhaseButton extends Rectangle {
    private double upperLeftX;
    private double upperLeftY;
    private double width;
    private double height;
    private Label label;

    public NextPhaseButton() {
        super(0, 0, 50, 50);
        //this.upperLeftX = upperLeftX;
        //this.upperLeftY = upperLeftY;
        this.setFill(Color.WHITE);
        this.label = new Label("next phase");
        this.width = 50;
        this.height = 50;
    }

    public double getUpperLeftX() {
        return upperLeftX;
    }

    public double getUpperLeftY() {
        return upperLeftY;
    }

    public Label getLabel() {
        return label;
    }
}
