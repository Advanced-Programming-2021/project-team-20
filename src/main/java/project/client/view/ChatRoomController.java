package project.client.view;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.Components.PackageForShowTweet;

public class ChatRoomController implements Initializable {

    @FXML
    private ScrollPane messageHolderScrollPane;
    @FXML
    private TextArea textArea;
    @FXML
    private Label onlineUsersLabel;
    private int lastIdOfTweetReceived = 0;
    private int lastIdOfTweetFixItsImage = 0;
    private double YMoveOfScrollPane = 0;
    private List<PackageForShowTweet> packageForShowTweets = new ArrayList<>();
    private Pane pane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        pane = new Pane();
        onlineUsersLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        onlineUsersLabel.setTextFill(Color.BLUE);
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
        checkConnectionStatus(details);
        JsonArray newTweets = details.getAsJsonArray("newTweets");
        for (int i = 0; i < newTweets.size(); i++) {
            JsonObject jsonObject = newTweets.get(i).getAsJsonObject();
            showTweet(jsonObject);
        }
        JsonArray deletedTweets = details.get("deletedTweetIds").getAsJsonArray();
        for (int i = 0; i < deletedTweets.size(); i++) {
            int tweetId = deletedTweets.get(i).getAsInt();
            deleteTweet(tweetId);
        }
        onlineUsersLabel.setText("Online Users: " + details.get("onlineUsers").getAsString());
        pane.setPrefHeight(YMoveOfScrollPane);
        fixImageOfRepeatedTweetsWithTheSameAuthor();
    }

    private void checkConnectionStatus(JsonObject details) {
        try {
            if (details.get("message").getAsString().equals("Connection Disconnected")) {
                showAlert(details.get("message").getAsString(), "Error");
                new MainMenuController().backToLoginPage();
            }
        } catch (Exception e) {

        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    private void showTweet(JsonObject jsonObject) {

        lastIdOfTweetReceived = jsonObject.get("id").getAsInt();
        PackageForShowTweet packageForShowTweet = new PackageForShowTweet(jsonObject.get("message").getAsString(), jsonObject.get("author").getAsString(), YMoveOfScrollPane, lastIdOfTweetReceived);
        pane.getChildren().add(packageForShowTweet);
        addMessageToPackageForShowTweet(packageForShowTweet, jsonObject.get("message").getAsString());
        YMoveOfScrollPane += 20;
        packageForShowTweets.add(packageForShowTweet);
        if (packageForShowTweet.isMessageFromOnlineUser()) {
            addRightClickEffect(packageForShowTweet);
        }
    }

    private void addRightClickEffect(PackageForShowTweet packageForShowTweet) {
        ContextMenu contextMenu = new ContextMenu();
//        MenuItem edit = new MenuItem("Edit");
//        edit.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//
//            }
//        });
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteMessage(packageForShowTweet);
            }
        });
        contextMenu.getItems().addAll(delete);
        packageForShowTweet.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                contextMenu.show(packageForShowTweet.getBackGroundRectangle(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
        });
    }

    private void deleteMessage(PackageForShowTweet packageForShowTweet) {

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("deleteTweet", "tweetId", packageForShowTweet.getMessageId() + "");
        String result = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(result);
        if (deserializeResult.get("message").equals("Connection Disconnected")) {
            showAlert(deserializeResult.get("message"), "Error");
            new MainMenuController().backToLoginPage();
        } else if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }

        deleteTweet(packageForShowTweet.getMessageId());
        showAlert(deserializeResult.get("message"), "Successful");

    }

    private void deleteTweet(int idOfTweet) {
        PackageForShowTweet packageForShowTweet = getPackageForShowTweetById(idOfTweet);
        if (packageForShowTweet == null) {
            return;
        }
        for (int i = 0; i < packageForShowTweets.size(); i++) {
            if (packageForShowTweets.get(i).getMessageId() > idOfTweet)
                packageForShowTweets.get(i).setTranslateY(packageForShowTweets.get(i).getTranslateY() - packageForShowTweet.getBackGroundRectangle().getHeight() - 15);
        }

        packageForShowTweets.remove(packageForShowTweet);
        pane.getChildren().remove(packageForShowTweet);
        YMoveOfScrollPane -= packageForShowTweet.getBackGroundRectangle().getHeight() + 15;
        pane.setPrefHeight(YMoveOfScrollPane - 20);
        fixImageOfRepeatedTweetsWithTheSameAuthor();
    }

    private PackageForShowTweet getPackageForShowTweetById(int tweetId) {
        for (int i = 0; i < packageForShowTweets.size(); i++) {
            if (packageForShowTweets.get(i).getMessageId() == tweetId) {
                return packageForShowTweets.get(i);
            }
        }
        return null;
    }

    private void fixImageOfRepeatedTweetsWithTheSameAuthor() {
        for (int i = 0; i < packageForShowTweets.size() - 1; i++) {
            if (packageForShowTweets.get(i).getShowUserNameLabel().getText().equals(packageForShowTweets.get(i + 1).getShowUserNameLabel().getText())) {
                packageForShowTweets.get(i).getUserImageCircle().setOpacity(0);
            }
            if (!packageForShowTweets.get(i).getShowUserNameLabel().getText().equals(packageForShowTweets.get(i + 1).getShowUserNameLabel().getText())) {
                packageForShowTweets.get(i).getUserImageCircle().setOpacity(1);
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
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived);
        String messageFromServer = (String) ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
    }

    public void refreshTweets() {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToGetTweetsById(lastIdOfTweetReceived + 1);
        String messageFromServer = (String) ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
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
