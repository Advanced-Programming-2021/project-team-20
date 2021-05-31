package sample.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.controller.non_duel.scoreboard.Scoreboard;

import java.io.IOException;

public class ScoreboardController {
    @FXML
    private Label label;
    public void fillLabel() {
        label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
        label.setText(new Scoreboard().findCommands("scoreboard show"));
    }

    public void returnToMainMenu(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/sample/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
