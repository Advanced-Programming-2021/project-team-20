package project.View;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.controller.duel.CardEffects.MonsterEffectEnums.*;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CardCreatorController implements Initializable {
    private String cardType;
    private String cardName;
    private int numberOfAllowedUsages;
    private int attackPowerMonsterCard;
    private int defencePowerMonsterCard;
    private int monsterAttributeNumber;
    private int monsterFamilyNumber;
    private int monsterValuesNumber;


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
    TextField textFieldForGettingDefencePowerMonsterCard;
    Button buttonForGettingDefencePowerMonsterCard;
    Label labelForGettingLevelMonsterCard;
    TextField textFieldForGettingLevelMonsterCard;
    Button buttonForGettingLevelMonsterCard;

    ArrayList<Button> buttonsForMonsterCardAttribute;
    VBox vboxForMonsterCardAttribute;
    ArrayList<Button> buttonsForMonsterCardFamily;
    ArrayList<Button> buttonsForMonsterCardFamily2;
    VBox vboxForMonsterCardFamily;
    VBox vboxForMonsterCardFamily2;
    ArrayList<Button> buttonsForMonsterCardValues;
    VBox vboxForMonsterCardValues;

    ArrayList<Button> buttonsForGettingSummoningRequirement;
    ArrayList<Integer> selectedSummoningRequirements;
    VBox vBoxForSummoningRequirement;
    Button finishButtonForSummoningRequirement;

    ArrayList<Button> buttonsForUponSummoningEffect;
    ArrayList<Integer> selectedUponSummoningEffect;
    VBox vBoxForUponSummoningEffect;
    Button buttonForFinishUponSummoningEffect;

    ArrayList<Button> buttonsForBeingAttackedEffect;
    ArrayList<Integer> selectedBeingAttackedEffect;
    VBox vBoxForBeingAttackedEffect;
    Button buttonForFinishBeingAttackedEffect;









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
        if (!attackPower.isEmpty() && pattern.matcher(attackPower).matches()) {
            attackPowerMonsterCard = Integer.parseInt(attackPower);
            removeThingsInContinueGettingMonsterInformation();
            labelForGettingDefencePowerMonsterCard = new Label("Please enter the card's defence power");
            labelForGettingDefencePowerMonsterCard.setLayoutY(100);
            labelForGettingDefencePowerMonsterCard.setLayoutX(420);
            anchorPane.getChildren().add(labelForGettingDefencePowerMonsterCard);

            textFieldForGettingDefencePowerMonsterCard = new TextField();
            textFieldForGettingDefencePowerMonsterCard.setLayoutY(130);
            textFieldForGettingDefencePowerMonsterCard.setLayoutX(440);
            anchorPane.getChildren().add(textFieldForGettingDefencePowerMonsterCard);

            buttonForGettingDefencePowerMonsterCard = new Button("OK");
            buttonForGettingDefencePowerMonsterCard.setLayoutY(160);
            buttonForGettingDefencePowerMonsterCard.setLayoutX(500);
            buttonForGettingDefencePowerMonsterCard.setOnAction(actionEvent -> getLevelMonsterCard());
            anchorPane.getChildren().add(buttonForGettingDefencePowerMonsterCard);
        }
    }






    private void getLevelMonsterCard() {
        String defencePower = textFieldForGettingDefencePowerMonsterCard.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!defencePower.isEmpty() && pattern.matcher(defencePower).matches()) {
            defencePowerMonsterCard = Integer.parseInt(defencePower);
            removeThingsInGetDefencePowerMonsterCard();

            labelForGettingLevelMonsterCard = new Label("Please enter the level for your monster card");
            labelForGettingLevelMonsterCard.setLayoutY(100);
            labelForGettingLevelMonsterCard.setLayoutX(360);
            anchorPane.getChildren().add(labelForGettingLevelMonsterCard);

            textFieldForGettingLevelMonsterCard = new TextField();
            textFieldForGettingLevelMonsterCard.setLayoutY(130);
            textFieldForGettingLevelMonsterCard.setLayoutX(440);
            anchorPane.getChildren().add(textFieldForGettingLevelMonsterCard);

            buttonForGettingLevelMonsterCard = new Button("OK");
            buttonForGettingLevelMonsterCard.setLayoutY(160);
            buttonForGettingLevelMonsterCard.setLayoutX(500);
            buttonForGettingLevelMonsterCard.setOnAction(actionEvent -> monsterCardAttributeFunction());
            anchorPane.getChildren().add(buttonForGettingLevelMonsterCard);
        }
    }








    private void monsterCardAttributeFunction() {
        String level = textFieldForGettingLevelMonsterCard.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!level.isEmpty() && pattern.matcher(level).matches()) {
            removeThingsInGetLevelMonsterCard();

            buttonsForMonsterCardAttribute = new ArrayList<>();
            buttonsForMonsterCardAttribute.add(new Button("DARK"));
            buttonsForMonsterCardAttribute.add(new Button("DIVINE"));
            buttonsForMonsterCardAttribute.add(new Button("LIGHT"));
            buttonsForMonsterCardAttribute.add(new Button("EARTH"));
            buttonsForMonsterCardAttribute.add(new Button("FIRE"));
            buttonsForMonsterCardAttribute.add(new Button("FIRE"));
            buttonsForMonsterCardAttribute.add(new Button("WATER"));
            for (int i = 0; i < buttonsForMonsterCardAttribute.size(); i++) {
                int finalI = i;
                buttonsForMonsterCardAttribute.get(i).setOnAction(ActionEvent -> monsterCardFamilyAction(finalI));
            }

            vboxForMonsterCardAttribute = new VBox();
            vboxForMonsterCardAttribute.setLayoutY(100);
            vboxForMonsterCardAttribute.setLayoutX(480);
            for (Button button : buttonsForMonsterCardAttribute) {
                vboxForMonsterCardAttribute.getChildren().add(button);
            }

            anchorPane.getChildren().add(vboxForMonsterCardAttribute);
        }
    }









    private void monsterCardFamilyAction(int finalI) {

        monsterAttributeNumber = finalI;
        anchorPane.getChildren().remove(vboxForMonsterCardAttribute);
        buttonsForMonsterCardFamily = new ArrayList<>();
        MonsterCardFamily[] values = MonsterCardFamily.values();

        for (int i = 0; i < values.length/2; i++) {
            String name = values[i].toString();
            buttonsForMonsterCardFamily.add(new Button(name));
        }

        for (int i = 0; i < values.length/2; i++) {
            int finalI1 = i;
            buttonsForMonsterCardFamily.get(i).setOnAction(ActionEvent -> monsterCardValuesFunction(finalI1));
        }

        vboxForMonsterCardFamily = new VBox();
        vboxForMonsterCardFamily.setLayoutY(100);
        vboxForMonsterCardFamily.setLayoutX(400);

        for (Button button : buttonsForMonsterCardFamily) {
            vboxForMonsterCardFamily.getChildren().add(button);
        }
        anchorPane.getChildren().add(vboxForMonsterCardFamily);


        buttonsForMonsterCardFamily2 = new ArrayList<>();
        for (int i = values.length/2; i < values.length; i++) {
            String name = values[i].toString();
            buttonsForMonsterCardFamily2.add(new Button(name));
        }

        for (int i = 0; i < values.length/2; i++) {
            int finalI1 = i;
            buttonsForMonsterCardFamily2.get(i).setOnAction(ActionEvent -> monsterCardValuesFunction(finalI1 + 12));
        }

        vboxForMonsterCardFamily2 = new VBox();
        vboxForMonsterCardFamily2.setLayoutY(100);
        vboxForMonsterCardFamily2.setLayoutX(600);

        for (Button button : buttonsForMonsterCardFamily2) {
            vboxForMonsterCardFamily2.getChildren().add(button);
        }

        anchorPane.getChildren().add(vboxForMonsterCardFamily2);
    }









    private void monsterCardValuesFunction(int finalI1) {
        monsterFamilyNumber = finalI1;

        anchorPane.getChildren().remove(vboxForMonsterCardFamily);
        anchorPane.getChildren().remove(vboxForMonsterCardFamily2);

        buttonsForMonsterCardValues = new ArrayList<>();
        MonsterCardValue[] values = MonsterCardValue.values();

        for (MonsterCardValue value : values) {
            String name = value.toString();
            buttonsForMonsterCardValues.add(new Button(name));
        }

        for (int i = 0; i < values.length; i++) {
            int finalI2 = i;
            buttonsForMonsterCardValues.get(i).setOnAction(ActionEvent -> endOfCreatingMonsterCardWithoutEffects(finalI2));
        }

        vboxForMonsterCardValues = new VBox();
        vboxForMonsterCardValues.setLayoutY(100);
        vboxForMonsterCardValues.setLayoutX(400);

        for (Button buttonsForMonsterCardValue : buttonsForMonsterCardValues) {
            vboxForMonsterCardValues.getChildren().add(buttonsForMonsterCardValue);
        }
        anchorPane.getChildren().add(vboxForMonsterCardValues);
    }













    private void endOfCreatingMonsterCardWithoutEffects(int finalI2) {
        monsterValuesNumber = finalI2;
        anchorPane.getChildren().remove(vboxForMonsterCardValues);
//        System.out.println("Type : " + cardType);
//        System.out.println("Card name : " + cardName);
//        System.out.println("numberOfAllowedUsages : " + numberOfAllowedUsages);
//        System.out.println("attackPowerMonsterCard : " + attackPowerMonsterCard);
//        System.out.println("defencePowerMonsterCard : " + defencePowerMonsterCard);
//        System.out.println("monsterAttributeNumber : " + monsterAttributeNumber);
//        System.out.println("monsterFamilyNumber : " + monsterFamilyNumber);
//        System.out.println("monsterValuesNumber : " + monsterValuesNumber);


         /* from here we should get effects from user
         ArrayList<SummoningRequirement>
         ArrayList<UponSummoningEffect>
         ArrayList<AttackerEffect> --> there is no need to get this one because it has no usages
         ArrayList<BeingAttackedEffect>
         ArrayList<ContinuousMonsterEffect>
         ArrayList<FlipEffect>
         ArrayList<OptionalMonsterEffect>
         ArrayList<SentToGraveyardEffect> */
        getSummoningRequirementFromUser();
//        getUponSummoningEffectFromUser();
//        getBeingAttackedEffectFromUser();
//        getContinuousMonsterEffectFromUser();
//        getFlipEffectFromUser();
//        getOptionalMonsterEffectFromUser();
//        getSentToGraveyardEffectFromUser();
    }












    private void getSummoningRequirementFromUser() {

        buttonsForGettingSummoningRequirement = new ArrayList<>();
        finishButtonForSummoningRequirement = new Button("OK");
        SummoningRequirement[] summoningRequirements = SummoningRequirement.values();

        for (SummoningRequirement summoningRequirement : summoningRequirements) {
            String summoningRequirementName = summoningRequirement.toString();
            buttonsForGettingSummoningRequirement.add(new Button(summoningRequirementName));
        }

        selectedSummoningRequirements = new ArrayList<>();
        for (int i = 0; i < summoningRequirements.length; i++) {
            int finalI = i;
            buttonsForGettingSummoningRequirement.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedSummoningRequirements, buttonsForGettingSummoningRequirement);
                }
            });
        }

        vBoxForSummoningRequirement = new VBox();
        vBoxForSummoningRequirement.setLayoutX(400);
        vBoxForSummoningRequirement.setLayoutY(100);
        finishButtonForSummoningRequirement.setLayoutX(400);
        finishButtonForSummoningRequirement.setLayoutY(500);
        finishButtonForSummoningRequirement.setOnAction(ActionEvent -> getUponSummoningEffectFromUser());

        for (int i = 0; i < buttonsForGettingSummoningRequirement.size(); i++) {
            if (i == 4 || i == 7 || i == 8) {
                vBoxForSummoningRequirement.getChildren().add(buttonsForGettingSummoningRequirement.get(i));
            }
        }

        anchorPane.getChildren().add(vBoxForSummoningRequirement);
        anchorPane.getChildren().add(finishButtonForSummoningRequirement);

    }









    private void getUponSummoningEffectFromUser() {
        //should be commented
        System.out.println(selectedSummoningRequirements);
        anchorPane.getChildren().remove(vBoxForSummoningRequirement);
        anchorPane.getChildren().remove(finishButtonForSummoningRequirement);


        buttonsForUponSummoningEffect = new ArrayList<>();
        buttonForFinishUponSummoningEffect = new Button("OK");
        UponSummoningEffect[] uponSummoningEffects = UponSummoningEffect.values();
        for (UponSummoningEffect uponSummoningEffect : uponSummoningEffects) {
            String buttonName = uponSummoningEffect.toString();
            buttonsForUponSummoningEffect.add(new Button(buttonName));
        }

        selectedUponSummoningEffect = new ArrayList<>();


        for (int i = 0; i < buttonsForUponSummoningEffect.size(); i++) {
            int finalI = i;
            buttonsForUponSummoningEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedUponSummoningEffect, buttonsForUponSummoningEffect);
                }
            });
        }

        vBoxForUponSummoningEffect = new VBox();
        vBoxForUponSummoningEffect.setLayoutX(400);
        vBoxForUponSummoningEffect.setLayoutY(100);
        buttonForFinishUponSummoningEffect.setLayoutX(400);
        buttonForFinishUponSummoningEffect.setLayoutY(500);
        buttonForFinishUponSummoningEffect.setOnAction(ActionEvent -> getBeingAttackedEffectFromUser());


        for (Button button : buttonsForUponSummoningEffect) {
            vBoxForUponSummoningEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForUponSummoningEffect);
        anchorPane.getChildren().add(buttonForFinishUponSummoningEffect);

    }

    private void getBeingAttackedEffectFromUser() {
        //should be commented
        System.out.println(selectedUponSummoningEffect);
        anchorPane.getChildren().remove(vBoxForUponSummoningEffect);
        anchorPane.getChildren().remove(buttonForFinishUponSummoningEffect);


        buttonsForBeingAttackedEffect = new ArrayList<>();
        buttonForFinishBeingAttackedEffect = new Button("OK");
        BeingAttackedEffect[] beingAttackedEffects = BeingAttackedEffect.values();

        for (BeingAttackedEffect beingAttackedEffect : beingAttackedEffects) {
            String buttonName = beingAttackedEffect.toString();
            buttonsForBeingAttackedEffect.add(new Button(buttonName));
        }

        selectedBeingAttackedEffect = new ArrayList<>();

        for (int i = 0; i < buttonsForBeingAttackedEffect.size(); i++) {
            int finalI = i;
            buttonsForBeingAttackedEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedBeingAttackedEffect, buttonsForBeingAttackedEffect);
                }
            });
        }

        vBoxForBeingAttackedEffect = new VBox();
        vBoxForBeingAttackedEffect.setLayoutY(100);
        vBoxForBeingAttackedEffect.setLayoutX(400);
        buttonForFinishBeingAttackedEffect.setLayoutY(500);
        buttonForFinishBeingAttackedEffect.setLayoutX(500);

        buttonForFinishBeingAttackedEffect.setOnAction(ActionEvent -> getContinuousMonsterEffectFromUser());


        for (Button button : buttonsForBeingAttackedEffect) {
            vBoxForBeingAttackedEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForBeingAttackedEffect);
        anchorPane.getChildren().add(buttonForFinishBeingAttackedEffect);

    }

    private void getContinuousMonsterEffectFromUser() {
        System.out.println(selectedBeingAttackedEffect);
        anchorPane.getChildren().remove(vBoxForBeingAttackedEffect);
        anchorPane.getChildren().remove(buttonForFinishBeingAttackedEffect);

    }

    private void changeAdditionOfThisEffectInTheGivenPlace(int finalI, ArrayList<Integer> integersValues, ArrayList<Button> buttons) {
        if (integersValues.contains(finalI)) {
            integersValues.remove(Integer.valueOf(finalI));
            buttons.get(finalI).setStyle("-fx-background-color: #e6e9ec");
        }
        else {
            integersValues.add(finalI);
            buttons.get(finalI).setStyle("-fx-background-color: #0c7bea;");
        }
    }


    private void removeThingsInGetLevelMonsterCard() {
        anchorPane.getChildren().remove(labelForGettingLevelMonsterCard);
        anchorPane.getChildren().remove(textFieldForGettingLevelMonsterCard);
        anchorPane.getChildren().remove(buttonForGettingLevelMonsterCard);
    }

    private void removeThingsInGetDefencePowerMonsterCard() {
        anchorPane.getChildren().remove(labelForGettingDefencePowerMonsterCard);
        anchorPane.getChildren().remove(textFieldForGettingDefencePowerMonsterCard);
        anchorPane.getChildren().remove(buttonForGettingDefencePowerMonsterCard);
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
