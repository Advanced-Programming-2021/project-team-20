package project.client.view.Components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.LoginController;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PackageForShowTweet extends Group {
    private Rectangle backGroundRectangle;
    private Circle userImageCircle = new Circle(20);
    private Label showUserNameLabel;
    private String author;
    private int messageId;
    private int numberOfLabelForShowMessages = 0;
    boolean isMessageFromOnlineUser;

    public PackageForShowTweet(String message, String userName, double translateY, int messageId) {
        isMessageFromOnlineUser = userName.equals(LoginController.getOnlineUser().getName());
        double translateX = 0;
        if (isMessageFromOnlineUser) {
            translateX = 380;
        }
        this.author = userName;
        this.messageId = messageId;
        setTranslateY(translateY + 5);
        setTranslateX(translateX);
        this.backGroundRectangle = createRectangle();
        this.showUserNameLabel = createLabel(userName);
        this.getChildren().add(backGroundRectangle);
        this.getChildren().add(showUserNameLabel);
        this.getChildren().add(createImageCircle());

    }


    public PackageForShowTweet(String userName, double translateY) {
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
        userImageCircle.setCenterX(this.getTranslateX() * 1.07 + 22);
        userImageCircle.setCenterY(15);
        if (isMessageFromOnlineUser) {
            userImageCircle.setFill(new ImagePattern(LoginController.getOnlineUser().getImage()));
        } else {
            String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("getImage", "userName", author);
            String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(messageFromServer);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            userImageCircle.setFill(new ImagePattern(createImage(jsonObject.get("imagePath").getAsString())));
        }
        return userImageCircle;
    }

    private Image createImage(String imagePath) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(imagePath);
            return new Image(stream);
        } catch (Exception e) {

        }
        return null;
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

    public int getMessageId() {
        return messageId;
    }

}
