package project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.controller.duel.PreliminaryPackage.DuelStarter;

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

    private DuelStarter duelStarter = new DuelStarter();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void singleDuelWithComputer() {
        isPlayWithComputer = true;
        isMatchGame = false;
        if (checkConditionsOfPlayers("AI")) {
            startGame();
        } 
    }

    public void matchDuleWithComputer() {
        isPlayWithComputer = true;
        isMatchGame = false;
        if (checkConditionsOfPlayers("AI")) {
            startGame();
        } 
        System.out.println("dadasdasdas");
    }

    public void singleDuelWithUser() {
        String userName = secondPlayerSingleDuelField.getText();
        isMatchGame = false;
        if (checkConditionsOfPlayers(userName)) {
            startGame();
        } else {
            secondPlayerSingleDuelField.setText("");
        }
    }

    public void matchDuelWithUser() {
        String userName = secondPlayerMatchDuelField.getText();
        isMatchGame = true;
        if (checkConditionsOfPlayers(userName)) {
            startGame();
        } else {
            secondPlayerMatchDuelField.setText("");
        }
    }

    private boolean checkConditionsOfPlayers(String secondPlayer) {
        int numberOfRounds = isMatchGame ? 3 : 1;
        String result = duelStarter.createGame(LoginController.getOnlineUser().getName(), secondPlayer, numberOfRounds);
        if (!result.equals("game started")) {
            showAlert("WARNING", result);
            return false;
        }
        return true;
    }

    private void startGame() {
        try {
            new MainView().changeView("/project/fxml/rockPaperScissorPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
