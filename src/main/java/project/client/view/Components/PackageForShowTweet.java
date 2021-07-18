package project.client.view.Components;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.client.view.LoginController;

import java.util.ArrayList;
import java.util.List;


public class PackageForShowTweet extends Group {
    private Rectangle backGroundRectangle;
    private Circle userImageCircle = new Circle(25);
    private Label showUserNameLabel;
    private int numberOfLabelForShowMessages = 0;
    boolean isMessageFromOnlineUser;

    public PackageForShowTweet(String message, String userName, double translateY) {
        isMessageFromOnlineUser = userName.equals(LoginController.getOnlineUser().getName());
        double translateX = 0;
        if (isMessageFromOnlineUser) {
            translateX = 380;
        }
        setTranslateY(translateY + 5);
        setTranslateX(translateX);
        this.backGroundRectangle = createRectangle();
        this.showUserNameLabel = createLabel(userName);
        this.getChildren().add(backGroundRectangle);
        this.getChildren().add(showUserNameLabel);
        this.getChildren().add(createImageCircle());
    }

    public PackageForShowTweet(String userName, double translateY){
        isMessageFromOnlineUser = userName.equals(LoginController.getOnlineUser().getName());
        double translateX = 0;
        if (isMessageFromOnlineUser) {
            translateX = 300;
        }
        setTranslateY(translateY + 5);
        setTranslateX(translateX);
        this.backGroundRectangle = createRectangle();
        this.backGroundRectangle.setWidth(270);
        this.showUserNameLabel = createLabel(userName);
        this.getChildren().add(backGroundRectangle);
        this.getChildren().add(showUserNameLabel);
        this.getChildren().add(createImageCircle());
        userImageCircle.setCenterX(userImageCircle.getCenterX() * 1.02);
    }

    private Circle createImageCircle() {
        userImageCircle.setFill(new ImagePattern(LoginController.getOnlineUser().getImage()));
        userImageCircle.setCenterX(this.getTranslateX() * 1.07 + 22);
        return userImageCircle;
    }

    private List<Label> createMessageLabels(String message) {
        List<Label> labels = new ArrayList<>();
        return labels;
    }

    private Label createLabel(String userName) {
        Label label = new Label();
        if (!isMessageFromOnlineUser) {
            label.setText(userName);
        }
        label.setTranslateX(55);
        label.setTextFill(Color.BLUE);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
        return label;
    }

    private Rectangle createRectangle() {
        Rectangle rec = new Rectangle(350, 30);
        rec.setX(50);
        if (isMessageFromOnlineUser) {
            rec.setFill(Color.BLUE);
        } else {
            rec.setFill(Color.BLACK);
        }
        rec.setOpacity(0.5);
        rec.setArcHeight(25);
        rec.setArcWidth(25);
        return rec;
    }

    public Label getShowUserNameLabel() {
        return showUserNameLabel;
    }

    public Rectangle getBackGroundRectangle() {
        return backGroundRectangle;
    }

    public int getNumberOfLabelForShowMessages() {
        return numberOfLabelForShowMessages;
    }

    public void addOneToNumberOfLabelForShowMessages() {
        this.numberOfLabelForShowMessages += 1;
    }

    public Circle getUserImageCircle() {
        return userImageCircle;
    }

    public boolean isMessageFromOnlineUser() {
        return isMessageFromOnlineUser;
    }
}
