package project.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.controller.non_duel.scoreboard.Scoreboard;

import java.io.IOException;

public class ScoreboardController {
    @FXML
    private Label label;
    @FXML
    private Label listView;
    public void fillLabel() {
        label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
        label.setText(new Scoreboard().findCommands("scoreboard show"));
    }

    public void returnToMainMenu(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
