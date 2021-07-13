package project.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.model.Deck;
import project.model.User;

public class startDuelController implements Initializable {

    @FXML
    private Button singleDuelWithComputerbtn;
    @FXML
    private Button matchDuelWithComputerbtn;
    @FXML
    private Button matchDuelWithUserbtn;
    @FXML
    private Button singleDuelWithUserbtn;
    @FXML
    private Button backbtn;
    @FXML
    private TextField secondPlayerSingleDuelField;
    @FXML
    private TextField secondPlayerMatchDuelField;

    private String chosenPlayerForPlaying;
    private boolean isPlayWithComputer;
    private boolean isMatchGame;
    private HashMap<String, String> deserializeResult;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/startDuel.mp3");
    }

    public void singleDuelWithComputer() {
        isPlayWithComputer = true;
        isMatchGame = false;
        // if (checkConditionsOfPlayers("AI")) {
        // startGame();
        // }
    }

    public void matchDuleWithComputer() {
        isPlayWithComputer = true;
        isMatchGame = false;
        // if (checkConditionsOfPlayers("AI")) {
        // startGame();
        // }
    }

    public void singleDuelWithUser() {
        String condition = checkConditionOfPlayer();
        if (!condition.equals("Wait Until Another Player Wants To Play")) {
            showAlert(condition, "Error");
            return;
        }
        isMatchGame = false;
        sendRequestToServer();
        // if (userName.equals("")) {
        // showAlert("FILL FIELDS", "ERROR");
        // return;
        // }
        // if (checkConditionsOfPlayers(userName)) {
        // startGame();
        // } else {
        // secondPlayerSingleDuelField.setText("");
        // }
    }

    public void matchDuelWithUser() {
        String condition = checkConditionOfPlayer();
        if (!condition.equals("Wait Until Another Player Wants To Play")) {
            showAlert(condition, "Error");
            return;
        }
        isMatchGame = true;
        sendRequestToServer();
        // if (checkConditionsOfPlayers(userName)) {
        // startGame();
        // } else {
        // secondPlayerMatchDuelField.setText("");
        // }
    }

    private void sendRequestToServer() {
        new Thread(() -> {
            int numberOfRounds = isMatchGame ? 3 : 1;
            String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("requestDuel",
                    "numberOfRounds", numberOfRounds + "");
            String messageFromServer = ServerConnection.sendDataToServerAndRecieveResult(dataSendToServer);
            deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
            if (deserializeResult.get("type").equals("Error")) {  
                // showAlert(deserializeResult.get("message"), "Error");
                return;
            }
            startGame();
        }).start();
    }

    private void startGame() {
        try {
            new MainView().changeView("/project/fxml/rockPaperScissorPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkConditionOfPlayer() {
        Deck activeDeck = getActiveDeck(LoginController.getOnlineUser());

        if (activeDeck == null) {
            return "You Have No Active Deck";
        }

        if (!isThisDeckValid(activeDeck)) {
            return "Your Active Deck is Not Valid!";
        }

        if (!isPlayWithComputer) {
            return "Wait Until Another Player Wants To Play";
        }
        return "null";
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
}
