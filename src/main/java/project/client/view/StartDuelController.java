package project.client.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.Components.PackageForShowTweet;
import project.model.Deck;
import project.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StartDuelController implements Initializable {

    @FXML
    private Button randomUserbtn;
    @FXML
    private Button computerbtn;
    @FXML
    private Button threeRoundsbtn;
    @FXML
    private Button oneRoundbtn;
    @FXML
    private Button backbtn;
    @FXML
    private Button cancelGamebtn;
    @FXML
    private Button playbtn;
    @FXML
    private Label numberOfRoundsLabel;
    @FXML
    private Label playWithAnotherLabel;
    @FXML
    private Label gameStateLabel;
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
    private SendDuelRequestToServer sendDuelRequestToServer;

    private boolean isRequestForGameSend;
    private boolean isPlayWithComputer;
    private boolean isMatchGame;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/startDuel.mp3");
        setEffectsOfGameButtons();
        gameStateLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
        numberOfRoundsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
        playWithAnotherLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
        onlineUsersLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        onlineUsersLabel.setTextFill(Color.BLUE);
        pane = new Pane();
        messageHolderScrollPane.setContent(pane);
        refreshTweets();
    }

    protected void setEffectsOfGameButtons() {

        if (isRequestForGameSend) {
            playbtn.setDisable(true);
            cancelGamebtn.setDisable(false);
        } else {
            playbtn.setDisable(false);
            cancelGamebtn.setDisable(true);
        }

        if (isMatchGame) {
            numberOfRoundsLabel.setText("Rounds: 3");
            threeRoundsbtn.setDisable(true);
            oneRoundbtn.setDisable(false);
        } else {
            numberOfRoundsLabel.setText("Rounds: 1");
            threeRoundsbtn.setDisable(false);
            oneRoundbtn.setDisable(true);
        }

        if (isPlayWithComputer) {
            playWithAnotherLabel.setText("play with computer");
            randomUserbtn.setDisable(false);
            computerbtn.setDisable(true);
        } else {
            playWithAnotherLabel.setText("play with random User");
            randomUserbtn.setDisable(true);
            computerbtn.setDisable(false);
        }
    }

    private void sendRequestDuelToServer() {
        isRequestForGameSend = true;
        setEffectsOfGameButtons();
        sendDuelRequestToServer = new SendDuelRequestToServer(this);

    }

    protected void startGame() {
        try {
            new MainView().changeView("/project/fxml/rockPaperScissorPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelGame() {
        if (!isRequestForGameSend) {
            showAlert("You Have Not Requested To Play Yet", "Error");
            return;
        }
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("cancelDuel", "", "");
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
        isRequestForGameSend = false;
        setEffectsOfGameButtons();
        sendDuelRequestToServer.stop();
    }

    public void setComputerOpponent() {
        isPlayWithComputer = true;
        setEffectsOfGameButtons();
    }

    public void setRandomOpponent() {
        isPlayWithComputer = false;
        setEffectsOfGameButtons();
    }

    public void setThreeRoundsDuel() {
        isMatchGame = true;
        setEffectsOfGameButtons();
    }

    public void setOneRoundDuel() {
        isMatchGame = false;
        setEffectsOfGameButtons();
    }

    public void playGame() {
        String condition = checkConditionOfPlayer();
        if (!condition.equals("Wait Until Another Player Wants To Play")) {
            showAlert(condition, "Error");
            return;
        }
        sendRequestDuelToServer();
    }


    public void sendTweet() {
        if (textArea.getText().matches("^\\s*$")) {
            return;
        }
        String message = textArea.getText();
        textArea.setText("");

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
    }

    public void refreshTweets() {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToGetTweetsById(lastIdOfTweetReceived + 1);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
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

    private void addRightClickEffect(PackageForShowTweet packageForShowTweet) {
        ContextMenu contextMenu = new ContextMenu();
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
        PackageForShowTweet deletedPackage = getPackageForShowTweetById(idOfTweet);
        if (deletedPackage == null) {
            return;
        }
        for (int i = 0; i < packageForShowTweets.size(); i++) {
            if (packageForShowTweets.get(i).getMessageId() > idOfTweet) {
                packageForShowTweets.get(i).setTranslateY(packageForShowTweets.get(i).getTranslateY() - deletedPackage.getBackGroundRectangle().getHeight() - 10);
            }
        }

        packageForShowTweets.remove(deletedPackage);
        pane.getChildren().remove(deletedPackage);
        System.out.println(deletedPackage.getBackGroundRectangle().getHeight() + "   " + pane.getPrefHeight() + "   " + YMoveOfScrollPane);
        YMoveOfScrollPane -= deletedPackage.getBackGroundRectangle().getHeight() + 10;
        pane.setPrefHeight(YMoveOfScrollPane);
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
        packageForShowTweets.get(packageForShowTweets.size() - 1).getUserImageCircle().setOpacity(1);
        lastIdOfTweetFixItsImage = packageForShowTweets.size() - 1;
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

    private void showTweet(JsonObject jsonObject) {
        lastIdOfTweetReceived = jsonObject.get("id").getAsInt();
        PackageForShowTweet packageForShowTweet = new PackageForShowTweet(jsonObject.get("author").getAsString(), YMoveOfScrollPane);
        pane.getChildren().add(packageForShowTweet);
        addMessageToPackageForShowTweet(packageForShowTweet, jsonObject.get("message").getAsString());
        YMoveOfScrollPane += 20;
        packageForShowTweets.add(packageForShowTweet);
        if (packageForShowTweet.isMessageFromOnlineUser()) {
            addRightClickEffect(packageForShowTweet);
        }
    }

    private void addMessageToPackageForShowTweet(PackageForShowTweet packageForShowTweet, String message) {
        Label label = createLabelForMessage(message, packageForShowTweet);
        packageForShowTweet.getChildren().add(label);
        List<String> splitMessageByEnter = new ArrayList<>();
        splitMessageByEnter = Arrays.asList(message.split("\n"));
        double YMoveBeforeMessage = YMoveOfScrollPane;
        YMoveOfScrollPane += splitMessageByEnter.size() * 15;
        for (int i = 0; i < splitMessageByEnter.size(); i++) {
            YMoveOfScrollPane += (splitMessageByEnter.get(i).length() / 30) * 15;
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
        label.setMaxWidth(250);
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


    private String checkConditionOfPlayer() {
        Deck activeDeck = getActiveDeck(LoginController.getOnlineUser());

        if (activeDeck == null) {
            return "You Have No Active Deck";
        }

        if (!isThisDeckValid(activeDeck)) {
            return "Your Active Deck is Not Valid!";
        }

//        if (!isPlayWithComputer) {
        return "Wait Until Another Player Wants To Play";
//        }
    }

    public void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public void fixLengthOfMessage() {
        List<String> textInEachLine = new ArrayList<>();
        textInEachLine = Arrays.asList(textArea.getText().split("\n"));
        for (int i = 0; i < textInEachLine.size(); i++) {
        }
    }

    private Deck getActiveDeck(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
            if (allDecks.get(entry.getKey()).getIsDeckActive())
                return allDecks.get(entry.getKey());
        }
        return null;
    }

    private boolean isThisDeckValid(Deck deck) {
        if (deck.getSizeOfMainDeck() >= 40 && deck.getSizeOfMainDeck() <= 60)
            return true;
        return false;
    }

    public void backToMainMenu() {
        if (isRequestForGameSend) {
            String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("cancelDuel", "", "");
            String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        }
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMatchGame() {
        return isMatchGame;
    }

    public boolean isPlayWithComputer() {
        return isPlayWithComputer;
    }

    public void setRequestForGameSend(boolean requestForGameSend) {
        isRequestForGameSend = requestForGameSend;
    }
}


class SendDuelRequestToServer implements Runnable {

    private boolean exit = false;
    StartDuelController startDuelController;
    Thread thread;
    int numberOfRounds;

    SendDuelRequestToServer(StartDuelController startDuelController) {
        this.startDuelController = startDuelController;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        numberOfRounds = startDuelController.isMatchGame() ? 3 : 1;
        String dataSendToServer = "";
        if (startDuelController.isPlayWithComputer()) {
            dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToPlayWithComputer(numberOfRounds);
            getMessageOfServerFromComputerGame(dataSendToServer);
            return;
        } else {
            dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("requestDuel",
                "numberOfRounds", numberOfRounds + "");
            getMessageOfServerFromWithRandomUser(dataSendToServer);
            return;
        }
    }

    private void getMessageOfServerFromWithRandomUser(String dataSendToServer) {
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (deserializeResult.get("message").equals("Connection Disconnected")) {
                    CustomDialog customDialog = new CustomDialog("Error", "Connection Disconnected", "mainMenu");
                    customDialog.openDialog();
                    return;
                }
                if (deserializeResult.get("type").equals("Error")) {
                    startDuelController.showAlert(deserializeResult.get("message"), "Error");
                    return;
                }
            }
        });

        while (!exit) {

            dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("getStatusAfterRequestDuel", "numberOfRounds", numberOfRounds + "");
            messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
            HashMap<String, String> deserializeResult2 = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (deserializeResult2.get("message").equals("Duel Started Successfully!")) {
                        startDuelController.startGame();
                        exit = true;
                    }
                    if (deserializeResult2.get("message").equals("Duel Canceled Successfully!")) {
                        startDuelController.showAlert("Duel Canceled Successfully!", "Confirmation");
                        exit = true;
                        return;
                    }
                    if (deserializeResult2.get("message").equals("Game interrupted")) {
                        startDuelController.showAlert("Game interrupted", "Confirmation");
                        startDuelController.setRequestForGameSend(false);
                        startDuelController.setEffectsOfGameButtons();
                        exit = true;
                    }
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMessageOfServerFromComputerGame(String dataSendToServer) {
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (deserializeResult.get("message").equals("Connection Disconnected")) {
                    CustomDialog customDialog = new CustomDialog("Error", "Connection Disconnected", "mainMenu");
                    customDialog.openDialog();
                    return;
                }
                if (deserializeResult.get("type").equals("Error")) {
                    startDuelController.showAlert(deserializeResult.get("message"), "Error");
                    return;
                }
                startDuelController.startGame();
            }
        });

    }


    public void stop() {
        exit = true;
    }
}
