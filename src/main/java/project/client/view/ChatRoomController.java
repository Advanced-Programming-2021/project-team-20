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
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteMessage(packageForShowTweet);
            }
        });
        contextMenu.getItems().addAll(edit, delete);
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

        showAlert(deserializeResult.get("message"), "Successful");
        for (int i = packageForShowTweet.getMessageId(); i < packageForShowTweets.size(); i++) {
            packageForShowTweets.get(i).setTranslateY(packageForShowTweets.get(i).getTranslateY() - packageForShowTweet.getBackGroundRectangle().getHeight());
        }

        packageForShowTweets.remove(packageForShowTweet);
        pane.getChildren().remove(packageForShowTweet);
        YMoveOfScrollPane -= packageForShowTweet.getBackGroundRectangle().getHeight();
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

//    private void addLabelToPackageForShowTweet(String text, PackageForShowTweet packageForShowTweet, int row) {
//        Label label = createLabelForMessage(text, packageForShowTweet);
//        label.setLayoutY(packageForShowTweet.getLayoutY() + (row) * 20);
//        packageForShowTweet.getChildren().add(label);
//        packageForShowTweet.addOneToNumberOfLabelForShowMessages();
//        packageForShowTweet.getBackGroundRectangle().setHeight(30 + packageForShowTweet.getNumberOfLabelForShowMessages() * 20);
//        YMoveOfScrollPane += 20;
//    }

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
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived );
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
