package project.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CardCreatorController implements Initializable {
    private String cardType;
    private String cardName;
    private int numberOfAllowedUsages;


    @FXML
    Button spellButton;
    @FXML
    Button trapButton;
    @FXML
    Button monsterButton;
    @FXML
    AnchorPane anchorPane;

    Label labelForGettingCardNameFromUser;
    TextField textFieldForGettingCardNameFromUser;
    Button buttonForGettingCardNameFromUser;
    Label labelForGettingNumberOfAllowedUsagesFromUser;
    Button buttonOneForNumberOfAllowedUsages;
    Button buttonThreeForNumberOfAllowedUsages;
    Label labelForGettingAttackPowerMonsterCard;
    TextField textFieldForGettingAttackPowerMonsterCard;
    Button buttonForGettingAttackPowerMonsterCard;
    Label labelForGettingDefencePowerMonsterCard;
    Button buttonForGettingDefencePowerMonsterCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        spellButton = new Button();
        trapButton = new Button();
        monsterButton = new Button();

        anchorPane.getChildren().add(spellButton);
        anchorPane.getChildren().add(trapButton);
        anchorPane.getChildren().add(monsterButton);

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
        cardType = "monster";
        removeThreeButtons();
        getCardNameFromUser();

    }

    private void trapCard() {
        cardType = "trap";
        removeThreeButtons();
        getCardNameFromUser();
    }

    private void spellCard() {
        cardType = "spell";
        removeThreeButtons();
        getCardNameFromUser();
    }

    private void removeThreeButtons(){
        anchorPane.getChildren().remove(spellButton);
        anchorPane.getChildren().remove(trapButton);
        anchorPane.getChildren().remove(monsterButton);
    }

    private void getCardNameFromUser() {
        labelForGettingCardNameFromUser = new Label("Please enter card name");
        labelForGettingCardNameFromUser.setLayoutY(100);
        labelForGettingCardNameFromUser.setLayoutX(450);
        anchorPane.getChildren().add(labelForGettingCardNameFromUser);

        textFieldForGettingCardNameFromUser = new TextField();
        textFieldForGettingCardNameFromUser.setLayoutY(130);
        textFieldForGettingCardNameFromUser.setLayoutX(450);
        anchorPane.getChildren().add(textFieldForGettingCardNameFromUser);

        buttonForGettingCardNameFromUser = new Button("OK");
        buttonForGettingCardNameFromUser.setOnAction(actionEvent -> setCardName());
        buttonForGettingCardNameFromUser.setLayoutY(160);
        buttonForGettingCardNameFromUser.setLayoutX(500);
        anchorPane.getChildren().add(buttonForGettingCardNameFromUser);
    }

    private void setCardName() {
        if (!textFieldForGettingCardNameFromUser.getText().isEmpty()) {
            cardName = textFieldForGettingCardNameFromUser.getText();
            removeThingsInTheGetCardNameScene();
            getNumberOfAllowedUsages();
        }
    }

    private void removeThingsInTheGetCardNameScene() {
        anchorPane.getChildren().remove(labelForGettingCardNameFromUser);
        anchorPane.getChildren().remove(textFieldForGettingCardNameFromUser);
        anchorPane.getChildren().remove(buttonForGettingCardNameFromUser);
    }

    private void getNumberOfAllowedUsages() {
        labelForGettingNumberOfAllowedUsagesFromUser = new Label("Choose one of them for number of allowed usages");
        labelForGettingNumberOfAllowedUsagesFromUser.setLayoutY(100);
        labelForGettingNumberOfAllowedUsagesFromUser.setLayoutX(360);
        anchorPane.getChildren().add(labelForGettingNumberOfAllowedUsagesFromUser);

        buttonOneForNumberOfAllowedUsages = new Button("One");
        buttonOneForNumberOfAllowedUsages.setOnAction(actionEvent -> getCardInformationBasedOnTheCardType(1));
        buttonOneForNumberOfAllowedUsages.setLayoutY(130);
        buttonOneForNumberOfAllowedUsages.setLayoutX(450);
        anchorPane.getChildren().add(buttonOneForNumberOfAllowedUsages);

        buttonThreeForNumberOfAllowedUsages = new Button("Three");
        buttonThreeForNumberOfAllowedUsages.setOnAction(actionEvent -> getCardInformationBasedOnTheCardType(3));
        buttonThreeForNumberOfAllowedUsages.setLayoutY(130);
        buttonThreeForNumberOfAllowedUsages.setLayoutX(500);
        anchorPane.getChildren().add(buttonThreeForNumberOfAllowedUsages);
    }

    private void getCardInformationBasedOnTheCardType(int numberOfAllowedUsages) {
        this.numberOfAllowedUsages = numberOfAllowedUsages;
        removeThingsInTheGetNumberOfAllowedUsages();
        if (cardType.equals("monster")) continueGettingMonsterInformation();
        else if (cardType.equals("spell")) continueGettingSpellInformation();
        else continueGettingTrapInformation();
    }

    private void continueGettingMonsterInformation() {
        labelForGettingAttackPowerMonsterCard = new Label("Please enter the card's attack power");
        labelForGettingAttackPowerMonsterCard.setLayoutY(100);
        labelForGettingAttackPowerMonsterCard.setLayoutX(420);
        anchorPane.getChildren().add(labelForGettingAttackPowerMonsterCard);

        textFieldForGettingAttackPowerMonsterCard = new TextField();
        textFieldForGettingAttackPowerMonsterCard.setLayoutY(130);
        textFieldForGettingAttackPowerMonsterCard.setLayoutX(440);
        anchorPane.getChildren().add(textFieldForGettingAttackPowerMonsterCard);

        buttonForGettingAttackPowerMonsterCard = new Button("OK");
        buttonForGettingAttackPowerMonsterCard.setLayoutY(160);
        buttonForGettingAttackPowerMonsterCard.setLayoutX(500);
        buttonForGettingAttackPowerMonsterCard.setOnAction(actionEvent -> getDefencePowerMonsterCard());
        anchorPane.getChildren().add(buttonForGettingAttackPowerMonsterCard);
    }

    private void getDefencePowerMonsterCard() {
        String attackPower = textFieldForGettingAttackPowerMonsterCard.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!textFieldForGettingAttackPowerMonsterCard.getText().isEmpty() &&
        pattern.matcher(attackPower).matches()) {
            removeThingsInContinueGettingMonsterInformation();
        }
    }

    private void removeThingsInContinueGettingMonsterInformation() {
        anchorPane.getChildren().remove(labelForGettingAttackPowerMonsterCard);
        anchorPane.getChildren().remove(textFieldForGettingAttackPowerMonsterCard);
        anchorPane.getChildren().remove(buttonForGettingAttackPowerMonsterCard);
    }

    private void continueGettingTrapInformation() {
    }

    private void continueGettingSpellInformation() {
    }

    private void removeThingsInTheGetNumberOfAllowedUsages() {
        anchorPane.getChildren().remove(labelForGettingNumberOfAllowedUsagesFromUser);
        anchorPane.getChildren().remove(buttonOneForNumberOfAllowedUsages);
        anchorPane.getChildren().remove(buttonThreeForNumberOfAllowedUsages);
    }

}
