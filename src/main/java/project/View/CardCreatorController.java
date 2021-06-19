package project.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CardCreatorController implements Initializable {
    @FXML
    Button spellButton;
    @FXML
    Button trapButton;
    @FXML
    Button monsterButton;
    @FXML
    AnchorPane anchorPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        spellButton = new Button();
        trapButton = new Button();
        monsterButton = new Button();
        anchorPane.getChildren().add(spellButton);
        anchorPane.getChildren().add(trapButton);
        anchorPane.getChildren().add(monsterButton);
//        spellButton.setDisable(false);
//        spellButton.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(new Color(1, 2, 3, 4))}));
        spellButton.setLayoutY(100);
        spellButton.setLayoutX(200);
        spellButton.setText("Spell");
        spellButton.setOnAction(actionEvent -> spellCard());


        trapButton.setText("Trap");
        trapButton.setLayoutY(100);
        trapButton.setLayoutX(500);
        trapButton.setOnAction(actionEvent -> trapCard());


        monsterButton.setText("Monster");
        monsterButton.setLayoutY(100);
        monsterButton.setLayoutX(800);
        monsterButton.setOnAction(actionEvent -> monsterCard());
    }

    private void monsterCard() {
        removeThreeButtons();
    }

    private void trapCard() {
        removeThreeButtons();
    }

    private void spellCard() {
        removeThreeButtons();
    }

    private void removeThreeButtons(){
        anchorPane.getChildren().remove(spellButton);
        anchorPane.getChildren().remove(trapButton);
        anchorPane.getChildren().remove(monsterButton);
    }
}
