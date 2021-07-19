package project.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
        pane = new Pane();
        messageHolderScrollPane.setContent(pane);
        refreshTweets();
    }

    private void setEffectsOfGameButtons() {

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

//        new Thread(() -> {
//            int numberOfRounds = isMatchGame ? 3 : 1;
//            String dataSendToServer = "";
//            if (isPlayWithComputer) {
//                dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToPlayWithComputer(numberOfRounds);
//            } else {
//                dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("requestDuel",
//                    "numberOfRounds", numberOfRounds + "");
//            }
//            String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
//            HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
//            System.out.println(messageFromServer);
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("before deserializeResult.get(type)");
//                    if (deserializeResult.get("type").equals("Error")) {
//                        showAlert(deserializeResult.get("message"), "Error");
//                        return;
//                    }
//                    if (deserializeResult.get("type").equals("Confirmation")) {
//                        System.out.println("here");
//                        showAlert("Duel Canceled!", "CONFIRMATION");
//                        return;
//                    }
//                    startGame();
//                }
//            });
//        }).start();
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
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult2(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
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

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived + 1);
        String messageFromServer =  ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        deserializeMessageAndShowIt(messageFromServer);
    }

    public void refreshTweets() {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToGetTweetsById(lastIdOfTweetReceived + 1);
        String messageFromServer =  ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
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

    private void showTweet(JsonObject jsonObject) {
        lastIdOfTweetReceived = jsonObject.get("id").getAsInt();
        PackageForShowTweet packageForShowTweet = new PackageForShowTweet(jsonObject.get("author").getAsString(), YMoveOfScrollPane);
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
}


class SendDuelRequestToServer implements Runnable {

    private boolean exit = false;
    StartDuelController startDuelController;
    Thread thread;

    SendDuelRequestToServer(StartDuelController startDuelController) {
        this.startDuelController = startDuelController;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("started");
//        while (!exit) {
        int numberOfRounds = startDuelController.isMatchGame() ? 3 : 1;
        String dataSendToServer = "";
        if (startDuelController.isPlayWithComputer()) {
            dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToPlayWithComputer(numberOfRounds);
        } else {
            dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("requestDuel",
                "numberOfRounds", numberOfRounds + "");
        }
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        System.out.println(messageFromServer);
        if (deserializeResult.get("type").equals("Confirmation")) {
            System.out.println("return");
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("before deserializeResult.get(type)");
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
