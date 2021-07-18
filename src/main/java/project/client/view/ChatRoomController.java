package project.client.view;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.Components.PackageForShowTweet;

public class ChatRoomController implements Initializable {

    @FXML
    private ScrollPane messageHolderScrollPane;
    @FXML
    private TextArea textArea;
    private int lastIdOfTweetReceived = 0;
    private int lastIdOfTweetFixItsImage = 0;
    private double YMoveOfScrollPane = 0;
    private List<PackageForShowTweet> packageForShowTweets = new ArrayList<>();
    private Pane pane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        pane = new Pane();
//        pane.setMaxWidth(messageHolderScrollPane.getMaxWidth());
//        BackgroundFill background_fill = new BackgroundFill(Color.PINK,
//            CornerRadii.EMPTY, Insets.EMPTY);
//        Background background = new Background(background_fill);
//        pane.setBackground(background);

        messageHolderScrollPane.setContent(pane);
        refreshTweets();
    }

    private Image createImage() {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\images\\chatRoom.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(stream);
    }

    private void deserializeMessageAndShowIt(String messageFromServer) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(messageFromServer);
        JsonObject details = jsonElement.getAsJsonObject();
        JsonArray newTweets = details.getAsJsonArray("newTweets");
        for (int i = 0; i < newTweets.size(); i++) {
            JsonObject jsonObject = newTweets.get(i).getAsJsonObject();
            showTweet(jsonObject);
        }
        pane.setPrefHeight(YMoveOfScrollPane);
        fixImageOfRepeatedTweetsWithTheSameAuthor();
    }

    private void showTweet(JsonObject jsonObject) {

        lastIdOfTweetReceived = jsonObject.get("id").getAsInt();
        System.out.println(lastIdOfTweetReceived);
        PackageForShowTweet packageForShowTweet = new PackageForShowTweet(jsonObject.get("message").getAsString(), jsonObject.get("author").getAsString(), YMoveOfScrollPane);
        pane.getChildren().add(packageForShowTweet);
        addMessageToPackageForShowTweet(packageForShowTweet, jsonObject.get("message").getAsString());
        YMoveOfScrollPane += 20;
        packageForShowTweets.add(packageForShowTweet);
    }

    private void fixImageOfRepeatedTweetsWithTheSameAuthor() {
        for (int i = lastIdOfTweetFixItsImage; i < packageForShowTweets.size() - 1; i++) {
            if (packageForShowTweets.get(i).getShowUserNameLabel().getText().equals(packageForShowTweets.get(i + 1).getShowUserNameLabel().getText())) {
                packageForShowTweets.get(i).getUserImageCircle().setFill(null);
            }
        }
        lastIdOfTweetFixItsImage = packageForShowTweets.size() - 1;
    }

    private void addMessageToPackageForShowTweet(PackageForShowTweet packageForShowTweet, String message) {
        Label label = createLabelForMessage(message, packageForShowTweet);
        packageForShowTweet.getChildren().add(label);
        List<String> splitMessageByEnter = new ArrayList<>();
        splitMessageByEnter = Arrays.asList(message.split("\n"));
        double YMoveBeforeMessage = YMoveOfScrollPane;
        YMoveOfScrollPane += splitMessageByEnter.size() * 15;
        for (int i = 0; i < splitMessageByEnter.size(); i++) {
            YMoveOfScrollPane += (splitMessageByEnter.get(i).length() / 37) * 15;
        }
        if (packageForShowTweet.isMessageFromOnlineUser()) {
            YMoveBeforeMessage -= 10;
        } else {
            YMoveBeforeMessage -= 25;
        }
        packageForShowTweet.getBackGroundRectangle().setHeight(YMoveOfScrollPane - YMoveBeforeMessage);
    }

    private void addLabelToPackageForShowTweet(String text, PackageForShowTweet packageForShowTweet, int row) {
        Label label = createLabelForMessage(text, packageForShowTweet);
        label.setLayoutY(packageForShowTweet.getLayoutY() + (row) * 20);
        packageForShowTweet.getChildren().add(label);
        packageForShowTweet.addOneToNumberOfLabelForShowMessages();
        packageForShowTweet.getBackGroundRectangle().setHeight(30 + packageForShowTweet.getNumberOfLabelForShowMessages() * 20);
        YMoveOfScrollPane += 20;
    }

    private Label createLabelForMessage(String text, PackageForShowTweet packageForShowTweet) {
        Label label = new Label();
        if (packageForShowTweet.isMessageFromOnlineUser()) {
            label.setTextFill(Color.BLACK);
        } else {
            label.setTextFill(Color.BLUE);
        }
        label.setMaxWidth(300);
        label.setWrapText(true);
        if (!packageForShowTweet.isMessageFromOnlineUser()) {
            label.setLayoutY(20);
            YMoveOfScrollPane += 20;
        } else {
            label.setLayoutY(5);
        }
        label.setLayoutX(60);
        label.setText(text);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));
        return label;
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTweet() {
        if (textArea.getText().matches("^\\s*$")) {
            return;
        }
        String message = textArea.getText();
        textArea.setText("");

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived + 1);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
    }

    public void refreshTweets() {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToGetTweetsById(lastIdOfTweetReceived + 1);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
    }

    public void fixLengthOfMessage() {
        List<String> textInEachLine = new ArrayList<>();
        textInEachLine = Arrays.asList(textArea.getText().split("\n"));
        for (int i = 0; i < textInEachLine.size(); i++) {

//            if()
        }
    }
}
