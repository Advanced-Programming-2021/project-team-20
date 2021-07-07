package project.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import project.controller.duel.CardEffects.MonsterEffectEnums.*;
import project.controller.duel.CardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import project.controller.duel.CardEffects.SpellEffectEnums.*;
import project.controller.duel.CardEffects.SpellEffectEnums.UserReplyForActivation;
import project.controller.duel.CardEffects.TrapEffectEnums.*;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;
import project.view.LoginController;
import project.view.MainView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.regex.Pattern;

public class CardCreatorController implements Initializable {
    private String imagePath;
    private String cardType;
    private String cardName;
    private String cardDescription;
    private Image cardImage;
    private int numberOfAllowedUsages;
    private int levelOfMonsterCard;
    private int attackPowerMonsterCard;
    private int defencePowerMonsterCard;
    private int monsterAttributeNumber;
    private int monsterFamilyNumber;
    private int monsterValuesNumber;


    private int numberOfTurnsForActivationSpell;
    private String spellCardValue;
    private ArrayList<Integer> numberOfSelectedEnumSpell;
    private ArrayList<Integer> selectedUserReplySpell;


    private int numberOfTurnsForActivationForTrapCard;
    private int trapCardValueNumber;
    private ArrayList<Integer> flipSummonTrapCardEffectNumbers;
    private ArrayList<Integer> monsterAttackingTrapCardEffectNumbers;
    private ArrayList<Integer> normalSummonTrapCardEffectNumbers;
    private ArrayList<Integer> tributeSummonTrapCardEffectNumbers;

    private ArrayList<Integer> normalTrapCardEffectNumbers;
    private ArrayList<Integer> ritualSummonTrapCardEffectNumbers;
    private ArrayList<Integer> specialSummonTrapCardEffectNumbers;
    private ArrayList<Integer> monsterEffectActivationTrapCardEffectNumbers;

    private ArrayList<Integer> spellCardActivationTrapCardEffectNumbers;
    private ArrayList<Integer> trapCardActivationTrapCardEffectNumbers;
    private ArrayList<Integer> userReplyForActivationNumbers;


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


    Label labelForGettingCardDescriptionFromUser;
    TextField textFieldForGettingCardDescriptionFromUser;
    Button buttonForGettingCardDescriptionFromUser;

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


    ArrayList<Button> buttonsForContinuousMonsterEffect;
    ArrayList<Integer> selectedContinuousMonsterEffect;
    VBox vBoxForContinuousMonsterEffect;
    Button buttonForFinishContinuousMonsterEffect;


    ArrayList<Button> buttonsForFlipEffect;
    ArrayList<Integer> selectedFlipEffect;
    VBox vBoxForFlipEffect;
    Button buttonForFinishFlipEffect;


    ArrayList<Button> buttonsForOptionalMonsterEffect;
    ArrayList<Integer> selectedOptionalMonsterEffect;
    VBox vBoxForOptionalMonsterEffect;
    Button buttonForFinishOptionalMonsterEffect;


    ArrayList<Button> buttonsForSentToGraveyardEffect;
    ArrayList<Integer> selectedSentToGraveyardEffect;
    VBox vBoxForSentToGraveyardEffect;
    Button buttonForFinishSentToGraveyardEffect;


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


    private void removeThreeButtons() {
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
            getCardDescription();
        }
    }


    private void getCardDescription() {
        labelForGettingCardDescriptionFromUser = new Label("Please enter the description for your card");
        labelForGettingCardDescriptionFromUser.setLayoutX(100);
        labelForGettingCardDescriptionFromUser.setLayoutX(400);

        textFieldForGettingCardDescriptionFromUser = new TextField();
        textFieldForGettingCardDescriptionFromUser.setLayoutX(450);
        textFieldForGettingCardDescriptionFromUser.setLayoutY(200);

        buttonForGettingCardDescriptionFromUser = new Button("OK");
        buttonForGettingCardDescriptionFromUser.setLayoutY(400);
        buttonForGettingCardDescriptionFromUser.setLayoutX(450);
        buttonForGettingCardDescriptionFromUser.setOnAction(ActionEvent -> getCardImage());

        anchorPane.getChildren().add(labelForGettingCardDescriptionFromUser);
        anchorPane.getChildren().add(textFieldForGettingCardDescriptionFromUser);
        anchorPane.getChildren().add(buttonForGettingCardDescriptionFromUser);

    }


    private void getCardImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images", "*.PNG"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images", "*.JPG"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        if (file != null) {
            changeImage(file.getAbsolutePath());
            imagePath = file.getAbsolutePath();
        } else {
            changeImage("src\\main\\resources\\project\\cards\\monsters\\Unknown.jpg");
            imagePath = "src\\main\\resources\\project\\cards\\monsters\\Unknown.jpg"; 
        }
        getNumberOfAllowedUsages();

    }


    private void changeImage(String imagePath) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardImage = new Image(stream);
    }


    private void removeThingsInTheGetCardNameScene() {
        anchorPane.getChildren().remove(labelForGettingCardNameFromUser);
        anchorPane.getChildren().remove(textFieldForGettingCardNameFromUser);
        anchorPane.getChildren().remove(buttonForGettingCardNameFromUser);
    }


    private void getNumberOfAllowedUsages() {
        cardDescription = textFieldForGettingCardDescriptionFromUser.getText();
        removeThingsInGettingCardDescription();
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


    private void removeThingsInGettingCardDescription() {
        anchorPane.getChildren().remove(labelForGettingCardDescriptionFromUser);
        anchorPane.getChildren().remove(textFieldForGettingCardDescriptionFromUser);
        anchorPane.getChildren().remove(buttonForGettingCardDescriptionFromUser);
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
            levelOfMonsterCard = Integer.parseInt(level);
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

        for (int i = 0; i < values.length / 2; i++) {
            String name = values[i].toString();
            buttonsForMonsterCardFamily.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
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
        for (int i = values.length / 2; i < values.length; i++) {
            String name = values[i].toString();
            buttonsForMonsterCardFamily2.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
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


        buttonsForContinuousMonsterEffect = new ArrayList<>();
        buttonForFinishContinuousMonsterEffect = new Button("OK");
        ContinuousMonsterEffect[] continuousMonsterEffects = ContinuousMonsterEffect.values();

        for (ContinuousMonsterEffect continuousMonsterEffect : continuousMonsterEffects) {
            String buttonName = continuousMonsterEffect.toString();
            buttonsForContinuousMonsterEffect.add(new Button(buttonName));
        }

        selectedContinuousMonsterEffect = new ArrayList<>();

        for (int i = 0; i < buttonsForContinuousMonsterEffect.size(); i++) {
            int finalI = i;
            buttonsForContinuousMonsterEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedContinuousMonsterEffect, buttonsForContinuousMonsterEffect);
                }
            });
        }

        vBoxForContinuousMonsterEffect = new VBox();
        vBoxForContinuousMonsterEffect.setLayoutX(400);
        vBoxForContinuousMonsterEffect.setLayoutY(100);
        buttonForFinishContinuousMonsterEffect.setLayoutX(400);
        buttonForFinishContinuousMonsterEffect.setLayoutY(400);
        buttonForFinishContinuousMonsterEffect.setOnAction(ActionEvent -> getFlipEffectFromUser());

        for (Button button : buttonsForContinuousMonsterEffect) {
            vBoxForContinuousMonsterEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForContinuousMonsterEffect);
        anchorPane.getChildren().add(buttonForFinishContinuousMonsterEffect);


    }


    private void getFlipEffectFromUser() {
        System.out.println(selectedContinuousMonsterEffect);
        anchorPane.getChildren().remove(vBoxForContinuousMonsterEffect);
        anchorPane.getChildren().remove(buttonForFinishContinuousMonsterEffect);


        buttonsForFlipEffect = new ArrayList<>();
        buttonForFinishFlipEffect = new Button("OK");

        FlipEffect[] flipEffects = FlipEffect.values();

        for (FlipEffect flipEffect : flipEffects) {
            String buttonName = flipEffect.toString();
            buttonsForFlipEffect.add(new Button(buttonName));
        }


        selectedFlipEffect = new ArrayList<>();

        for (int i = 0; i < buttonsForFlipEffect.size(); i++) {
            int finalI = i;
            buttonsForFlipEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedFlipEffect, buttonsForFlipEffect);
                }
            });
        }


        vBoxForFlipEffect = new VBox();
        vBoxForFlipEffect.setLayoutY(100);
        vBoxForFlipEffect.setLayoutX(400);
        buttonForFinishFlipEffect.setLayoutY(400);
        buttonForFinishFlipEffect.setLayoutX(400);
        buttonForFinishFlipEffect.setOnAction(ActionEvent -> getOptionalMonsterEffectFromUser());

        for (Button button : buttonsForFlipEffect) {
            vBoxForFlipEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForFlipEffect);
        anchorPane.getChildren().add(buttonForFinishFlipEffect);
    }


    private void getOptionalMonsterEffectFromUser() {
        System.out.println(selectedFlipEffect);
        anchorPane.getChildren().remove(vBoxForFlipEffect);
        anchorPane.getChildren().remove(buttonForFinishFlipEffect);


        buttonsForOptionalMonsterEffect = new ArrayList<>();
        buttonForFinishOptionalMonsterEffect = new Button("OK");

        OptionalMonsterEffect[] optionalMonsterEffect = OptionalMonsterEffect.values();

        for (OptionalMonsterEffect e : optionalMonsterEffect) {
            String buttonName = e.toString();
            buttonsForOptionalMonsterEffect.add(new Button(buttonName));
        }


        selectedOptionalMonsterEffect = new ArrayList<>();

        for (int i = 0; i < buttonsForOptionalMonsterEffect.size(); i++) {
            int finalI = i;
            buttonsForOptionalMonsterEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedOptionalMonsterEffect, buttonsForOptionalMonsterEffect);
                }
            });
        }


        vBoxForOptionalMonsterEffect = new VBox();
        vBoxForOptionalMonsterEffect.setLayoutY(100);
        vBoxForOptionalMonsterEffect.setLayoutX(400);
        buttonForFinishOptionalMonsterEffect.setLayoutY(400);
        buttonForFinishOptionalMonsterEffect.setLayoutX(400);
        buttonForFinishOptionalMonsterEffect.setOnAction(ActionEvent -> getSentToGraveyardEffectFromUser());

        for (Button button : buttonsForOptionalMonsterEffect) {
            vBoxForOptionalMonsterEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForOptionalMonsterEffect);
        anchorPane.getChildren().add(buttonForFinishOptionalMonsterEffect);
    }


    private void getSentToGraveyardEffectFromUser() {
        System.out.println(selectedOptionalMonsterEffect);
        anchorPane.getChildren().remove(vBoxForOptionalMonsterEffect);
        anchorPane.getChildren().remove(buttonForFinishOptionalMonsterEffect);


        buttonsForSentToGraveyardEffect = new ArrayList<>();
        buttonForFinishSentToGraveyardEffect = new Button("OK");

        SentToGraveyardEffect[] sentToGraveyardEffect = SentToGraveyardEffect.values();

        for (SentToGraveyardEffect e : sentToGraveyardEffect) {
            String buttonName = e.toString();
            buttonsForSentToGraveyardEffect.add(new Button(buttonName));
        }


        selectedSentToGraveyardEffect = new ArrayList<>();

        for (int i = 0; i < buttonsForSentToGraveyardEffect.size(); i++) {
            int finalI = i;
            buttonsForSentToGraveyardEffect.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedSentToGraveyardEffect, buttonsForSentToGraveyardEffect);
                }
            });
        }


        vBoxForSentToGraveyardEffect = new VBox();
        vBoxForSentToGraveyardEffect.setLayoutY(100);
        vBoxForSentToGraveyardEffect.setLayoutX(400);
        buttonForFinishSentToGraveyardEffect.setLayoutY(400);
        buttonForFinishSentToGraveyardEffect.setLayoutX(400);
        buttonForFinishSentToGraveyardEffect.setOnAction(ActionEvent -> finishMonsterCardCreator());

        for (Button button : buttonsForSentToGraveyardEffect) {
            vBoxForSentToGraveyardEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForSentToGraveyardEffect);
        anchorPane.getChildren().add(buttonForFinishSentToGraveyardEffect);
    }


    private void finishMonsterCardCreator() {
        //Attribute
        MonsterCardAttribute[] allValues = MonsterCardAttribute.values();
        MonsterCardAttribute attribute = null;
        int counter = 0;
        for (MonsterCardAttribute allValue : allValues) {
            if (counter == monsterAttributeNumber) attribute = allValue;
            counter++;
        }
        //Family
        MonsterCardFamily[] allValuesFamily = MonsterCardFamily.values();
        MonsterCardFamily family = null;
        counter = 0;
        for (MonsterCardFamily monsterCardFamily : allValuesFamily) {
            if (counter == monsterFamilyNumber) family = monsterCardFamily;
            counter++;
        }
        //Value
        MonsterCardValue[] allValuesValue = MonsterCardValue.values();
        MonsterCardValue value = null;
        counter = 0;
        for (MonsterCardValue monsterCardValue : allValuesValue) {
            if (counter == monsterValuesNumber) value = monsterCardValue;
            counter++;
        }
        //Start Enums
        HashMap<String, List<String>> monsterHashMap = new HashMap<>();
        //add SummoningRequirement
        SummoningRequirement[] summoningRequirements = SummoningRequirement.values();
        ArrayList<String> selectedArrayList = new ArrayList<>();
        counter = 0;
        for (SummoningRequirement summoningRequirement : summoningRequirements) {
            if (selectedUponSummoningEffect.contains(Integer.valueOf(counter)))
                selectedArrayList.add(String.valueOf(summoningRequirement));
            counter++;
        }
        monsterHashMap.put("SummoningRequirement", selectedArrayList);
        //add UponSummoningEffect
        UponSummoningEffect[] uponSummoningEffects = UponSummoningEffect.values();
        ArrayList<String> selectedUponSummoning = new ArrayList<>();
        counter = 0;
        for (UponSummoningEffect uponSummoningEffect : uponSummoningEffects) {
            if (selectedUponSummoningEffect.contains(Integer.valueOf(counter)))
                selectedUponSummoning.add(String.valueOf(uponSummoningEffect));
            counter++;
        }
        monsterHashMap.put("UponSummoningEffect", selectedUponSummoning);

        //add BeingAttackedEffect
        BeingAttackedEffect[] beingAttackedEffects = BeingAttackedEffect.values();
        ArrayList<String> selectedBeingAttacked = new ArrayList<>();
        counter = 0;
        for (BeingAttackedEffect beingAttackedEffect : beingAttackedEffects) {
            if (selectedBeingAttackedEffect.contains(Integer.valueOf(counter)))
                selectedBeingAttacked.add(String.valueOf(beingAttackedEffect));
            counter++;
        }
        monsterHashMap.put("BeingAttackedEffect", selectedBeingAttacked);

        //add ContinuousMonsterEffect
        ContinuousMonsterEffect[] continuousMonsterEffects = ContinuousMonsterEffect.values();
        ArrayList<String> selectedContinuousMonster = new ArrayList<>();
        counter = 0;
        for (ContinuousMonsterEffect continuousMonsterEffect : continuousMonsterEffects) {
            if (selectedContinuousMonsterEffect.contains(Integer.valueOf(counter)))
                selectedContinuousMonster.add(String.valueOf(continuousMonsterEffect));
            counter++;
        }
        monsterHashMap.put("ContinuousMonsterEffect", selectedContinuousMonster);

        //add FlipEffect
        FlipEffect[] flipEffects = FlipEffect.values();
        ArrayList<String> selectedFlip = new ArrayList<>();
        counter = 0;
        for (FlipEffect flipEffect : flipEffects) {
            if (selectedFlipEffect.contains(Integer.valueOf(counter))) selectedFlip.add(String.valueOf(flipEffect));
            counter++;
        }
        monsterHashMap.put("FlipEffect", selectedFlip);

        //add OptionalMonsterEffect
        OptionalMonsterEffect[] optionalMonsterEffects = OptionalMonsterEffect.values();
        ArrayList<String> selectedOptionalMonster = new ArrayList<>();
        counter = 0;
        for (OptionalMonsterEffect optionalMonsterEffect : optionalMonsterEffects) {
            if (selectedOptionalMonsterEffect.contains(Integer.valueOf(counter)))
                selectedOptionalMonster.add(String.valueOf(optionalMonsterEffect));
            counter++;
        }
        monsterHashMap.put("OptionalMonsterEffect", selectedOptionalMonster);

        //add SentToGraveyardEffect
        SentToGraveyardEffect[] sentToGraveyardEffects = SentToGraveyardEffect.values();
        ArrayList<String> selectedSent = new ArrayList<>();
        counter = 0;
        for (SentToGraveyardEffect sentToGraveyardEffect : sentToGraveyardEffects) {
            if (selectedSentToGraveyardEffect.contains(Integer.valueOf(counter)))
                selectedSent.add(String.valueOf(sentToGraveyardEffect));
            counter++;
        }
        monsterHashMap.put("SentToGraveyardEffect", selectedSent);


        MonsterCard monsterCard = new MonsterCard(attackPowerMonsterCard, defencePowerMonsterCard, levelOfMonsterCard, attribute,
            family, value, cardName, cardDescription, CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, 0, monsterHashMap, cardImage);
        Storage.addCardToNewCardsCrated(monsterCard);
        Storage.saveNewImagesOfCardsInFile(monsterCard, imagePath);


        //TODO -> calculate card price

        System.out.println("Card Created and added to storage successfully");
    }


    private void changeAdditionOfThisEffectInTheGivenPlace(int finalI, ArrayList<Integer> integersValues, ArrayList<Button> buttons) {
        if (integersValues.contains(finalI)) {
            integersValues.remove(Integer.valueOf(finalI));
            buttons.get(finalI).setStyle("-fx-background-color: #e6e9ec");
        } else {
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
        getNumberOfTurnsForActivationForTrapCard();
    }

    private void getNumberOfTurnsForActivationForTrapCard() {
        Label label = new Label("Enter number of turns for activation");
        label.setLayoutX(450);
        label.setLayoutY(100);

        TextField textField = new TextField();
        textField.setLayoutY(200);
        textField.setLayoutX(450);

        Button button = new Button("OK");
        button.setLayoutX(450);
        button.setLayoutY(300);
        button.setOnAction(ActionEvent -> getTrapCardValue(textField, label, button));

        anchorPane.getChildren().add(label);
        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(button);
    }

    private void getTrapCardValue(TextField textField, Label label, Button button) {
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!textField.getText().isEmpty() && pattern.matcher(textField.getText()).find()) {
            numberOfTurnsForActivationForTrapCard = Integer.parseInt(textField.getText());
            flipSummonTrapCardEffectNumbers = new ArrayList<>();
            monsterAttackingTrapCardEffectNumbers = new ArrayList<>();
            normalSummonTrapCardEffectNumbers = new ArrayList<>();
            tributeSummonTrapCardEffectNumbers = new ArrayList<>();
            normalTrapCardEffectNumbers = new ArrayList<>();
            ritualSummonTrapCardEffectNumbers = new ArrayList<>();
            specialSummonTrapCardEffectNumbers = new ArrayList<>();
            monsterEffectActivationTrapCardEffectNumbers = new ArrayList<>();
            spellCardActivationTrapCardEffectNumbers = new ArrayList<>();
            trapCardActivationTrapCardEffectNumbers = new ArrayList<>();
            userReplyForActivationNumbers = new ArrayList<>();
            anchorPane.getChildren().remove(textField);
            anchorPane.getChildren().remove(label);
            anchorPane.getChildren().remove(button);
            //TODO -> get TrapValue

            getFlipSummonTrapCardEffect(textField, label, button);

        }
    }

    public void getFlipSummonTrapCardEffect(TextField textField, Label label, Button button) {
        anchorPane.getChildren().remove(textField);
        anchorPane.getChildren().remove(label);
        anchorPane.getChildren().remove(button);

        VBox vbox = new VBox();
        Button button1 = new Button();
        anchorPane.getChildren().add(vbox);
        anchorPane.getChildren().add(button1);
        gotoTrapFunctionEffect(vbox, button1, "FlipSummonTrapCardEffect", "getMonsterAttackingTrapCardEffect");
    }

    public void getMonsterAttackingTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "MonsterAttackingTrapCardEffect", "getNormalSummonTrapCardEffect");
    }


    public void getNormalSummonTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "NormalSummonTrapCardEffect", "getTributeSummonTrapCardEffect");
    }

    public void getTributeSummonTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "TributeSummonTrapCardEffect", "getNormalTrapCardEffect");
    }

    public void getNormalTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "NormalTrapCardEffect", "getRitualSummonTrapCardEffect");
    }

    public void getRitualSummonTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "RitualSummonTrapCardEffect", "getSpecialSummonTrapCardEffect");
    }

    public void getSpecialSummonTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "SpecialSummonTrapCardEffect", "getMonsterEffectActivationTrapCardEffect");
    }

    public void getMonsterEffectActivationTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "MonsterEffectActivationTrapCardEffect", "getSpellCardActivationTrapCardEffect");
    }

    public void getSpellCardActivationTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "SpellCardActivationTrapCardEffect", "getTrapCardActivationTrapCardEffect");
    }

    public void getTrapCardActivationTrapCardEffect(VBox vBox, Button button) {
        gotoTrapFunctionEffect(vBox, button, "TrapCardActivationTrapCardEffect", "getUserReplyForActivation");
    }

    public void getUserReplyForActivation(VBox vBox, Button button) {
        anchorPane.getChildren().remove(vBox);
        anchorPane.getChildren().remove(button);

        VBox newVbox = new VBox();
        ArrayList<Button> buttons = new ArrayList<>();
        Button finishButton = new Button("OK");
        ArrayList<Integer> selected = selectedUserReplySpell;
        UserReplyForActivation[] userReplyForActivations = UserReplyForActivation.values();
        for (UserReplyForActivation userReplyForActivation : userReplyForActivations) {
            String name = userReplyForActivation.toString();
            buttons.add(new Button(name));
        }


        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selected, buttons);
                }
            });
        }




        newVbox.setLayoutY(100);
        newVbox.setLayoutX(400);
        finishButton.setLayoutY(400);
        finishButton.setLayoutX(400);
        finishButton.setOnAction(ActionEvent -> finishTrapCard());

        for (Button button1 : buttons) {
            vBox.getChildren().add(button1);
        }

        anchorPane.getChildren().add(newVbox);
        anchorPane.getChildren().add(finishButton);
    }

    private void finishTrapCard() {
        System.out.println("Trying to create");
        int counter = 0;
        TrapCardValue trapValue = null;
        TrapCardValue[] trapCardValues = TrapCardValue.values();
        for (TrapCardValue trapCardValue : trapCardValues) {
            if (trapCardValueNumber == counter) trapValue = trapCardValue;
            counter++;
        }

        TrapCard trapCard = new TrapCard(cardName, cardDescription, trapValue, CardPosition.NOT_APPLICABLE,
            numberOfAllowedUsages, numberOfTurnsForActivationForTrapCard, 0, new HashMap<>(), cardImage);
        Storage.addCardToNewCardsCrated(trapCard);
        Storage.saveNewImagesOfCardsInFile(trapCard, imagePath);
        Storage.addCardToNewCardsCrated(trapCard);
        Storage.addNewImageForNewCards(cardName, cardImage);
        System.out.println("card was created");
    }


    private void gotoTrapFunctionEffect(VBox previousVbox, Button previousButton, String enumClassName, String nextMethod) {
        anchorPane.getChildren().remove(previousButton);
        anchorPane.getChildren().remove(previousVbox);


        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        ArrayList<Integer> selectedEffects = null;

        if (enumClassName.equals("FlipSummonTrapCardEffect")) {
            FlipSummonTrapCardEffect[] effects = FlipSummonTrapCardEffect.values();
            for (FlipSummonTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = flipSummonTrapCardEffectNumbers;
        } else if (enumClassName.equals("MonsterAttackingTrapCardEffect")) {
            MonsterAttackingTrapCardEffect[] effects = MonsterAttackingTrapCardEffect.values();
            for (MonsterAttackingTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = monsterAttackingTrapCardEffectNumbers;
        } else if (enumClassName.equals("NormalSummonTrapCardEffect")) {
            NormalSummonTrapCardEffect[] effects = NormalSummonTrapCardEffect.values();
            for (NormalSummonTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = normalSummonTrapCardEffectNumbers;
        } else if (enumClassName.equals("TributeSummonTrapCardEffect")) {
            TributeSummonTrapCardEffect[] effects = TributeSummonTrapCardEffect.values();
            for (TributeSummonTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = tributeSummonTrapCardEffectNumbers;
        } else if (enumClassName.equals("NormalTrapCardEffect")) {
            NormalTrapCardEffect[] effects = NormalTrapCardEffect.values();
            for (NormalTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = normalTrapCardEffectNumbers;
        } else if (enumClassName.equals("RitualSummonTrapCardEffect")) {
            RitualSummonTrapCardEffect[] effects = RitualSummonTrapCardEffect.values();
            for (RitualSummonTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = ritualSummonTrapCardEffectNumbers;
        } else if (enumClassName.equals("SpecialSummonTrapCardEffect")) {
            SpecialSummonTrapCardEffect[] effects = SpecialSummonTrapCardEffect.values();
            for (SpecialSummonTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = specialSummonTrapCardEffectNumbers;
        } else if (enumClassName.equals("MonsterEffectActivationTrapCardEffect")) {
            MonsterEffectActivationTrapCardEffect[] effects = MonsterEffectActivationTrapCardEffect.values();
            for (MonsterEffectActivationTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = monsterEffectActivationTrapCardEffectNumbers;
        } else if (enumClassName.equals("SpellCardActivationTrapCardEffect")) {
            SpellCardActivationTrapCardEffect[] effects = SpellCardActivationTrapCardEffect.values();
            for (SpellCardActivationTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = spellCardActivationTrapCardEffectNumbers;
        } else if (enumClassName.equals("TrapCardActivationTrapCardEffect")) {
            TrapCardActivationTrapCardEffect[] effects = TrapCardActivationTrapCardEffect.values();
            for (TrapCardActivationTrapCardEffect e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = trapCardActivationTrapCardEffectNumbers;
        } else if (enumClassName.equals("UserReplyForActivation")) {
            UserReplyForActivation[] effects = UserReplyForActivation.values();
            for (UserReplyForActivation e : effects) {
                String buttonName = e.toString();
                buttons.add(new Button(buttonName));
            }
            selectedEffects = userReplyForActivationNumbers;
        }


        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            ArrayList<Integer> finalSelectedEffects = selectedEffects;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, finalSelectedEffects, buttons);
                }
            });
        }


        VBox vBox = new VBox();
        vBox.setLayoutY(100);
        vBox.setLayoutX(400);
        buttonForFinish.setLayoutY(400);
        buttonForFinish.setLayoutX(400);
        Method method = null;
        try {
            method = Class.forName("project.view.CardCreatorController").getMethod(nextMethod, VBox.class, Button.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method finalMethod = method;
        buttonForFinish.setOnAction(ActionEvent -> {
            try {
//                finalMethod.invoke(null, vBox, buttonForFinish);
                finalMethod.invoke(vBox, buttonForFinish);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void continueGettingSpellInformation() {
        getNumberOfTurnsForActivation();
    }


    private void getNumberOfTurnsForActivation() {
        Label label = new Label("Enter number of turns for activation");
        label.setLayoutX(450);
        label.setLayoutY(100);

        TextField textField = new TextField();
        textField.setLayoutY(200);
        textField.setLayoutX(450);

        Button button = new Button("OK");
        button.setLayoutX(450);
        button.setLayoutY(300);
        button.setOnAction(ActionEvent -> getSpellCardValue(textField, label, button));

        anchorPane.getChildren().add(label);
        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(button);

    }


    private void getSpellCardValue(TextField textField, Label label, Button button) {
        Pattern pattern = Pattern.compile("^\\d+$");
        String numberOfTurns = textField.getText();
        if (!numberOfTurns.isEmpty() && pattern.matcher(numberOfTurns).matches()) {
            numberOfTurnsForActivationSpell = Integer.parseInt(numberOfTurns);
            anchorPane.getChildren().remove(textField);
            anchorPane.getChildren().remove(label);
            anchorPane.getChildren().remove(button);

            Label newLabel = new Label("Please choose one of these");
            newLabel.setLayoutY(100);
            newLabel.setLayoutX(450);

            VBox vbox = new VBox();
            vbox.setLayoutY(150);
            vbox.setLayoutX(450);

            ArrayList<Button> buttons = new ArrayList<>();
            SpellCardValue[] spellCardValues = SpellCardValue.values();
            for (SpellCardValue cardValue : spellCardValues) {
                String name = cardValue.toString();
                buttons.add(new Button(name));
            }

            for (Button button1 : buttons) {
                vbox.getChildren().add(button1);
            }

            for (int i = 0; i < buttons.size(); i++) {
                switch (i) {
                    case 0:
                        buttons.get(i).setOnAction(ActionEvent -> normalSpell(vbox, label));
                        break;
                    case 1:
                        buttons.get(i).setOnAction(ActionEvent -> equipSpell(vbox, label));
                        break;
                    case 2:
                        buttons.get(i).setOnAction(ActionEvent -> fieldSpell(vbox, label));
                        break;
                    case 3:
                        buttons.get(i).setOnAction(ActionEvent -> ritualSpell(vbox, label));
                        break;
                    case 4:
                        buttons.get(i).setOnAction(ActionEvent -> quickPlaySpell(vbox, label));
                        break;
                    case 5:
                        buttons.get(i).setOnAction(ActionEvent -> continuousSpell(vbox, label));
                        break;

                }
            }

            numberOfSelectedEnumSpell = new ArrayList<>();
            anchorPane.getChildren().add(vbox);
            anchorPane.getChildren().add(label);

        }
    }


    private void continuousSpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.CONTINUOUS.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);


        ContinuousSpellCardEffect[] effects = ContinuousSpellCardEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (ContinuousSpellCardEffect effect : effects) {
            String name = effect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void quickPlaySpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.QUICK_PLAY.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);


        QuickSpellEffect[] effects = QuickSpellEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (QuickSpellEffect effect : effects) {
            String name = effect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void ritualSpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.RITUAL.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);


        RitualSpellEffect[] effects = RitualSpellEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (RitualSpellEffect effect : effects) {
            String name = effect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void fieldSpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.FIELD.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);

        FieldSpellEffect[] effects = FieldSpellEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (FieldSpellEffect effect : effects) {
            String name = effect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void equipSpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.EQUIP.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);

        EquipSpellEffect[] effects = EquipSpellEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (EquipSpellEffect effect : effects) {
            String name = effect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void normalSpell(VBox vbox, Label label) {
        spellCardValue = SpellCardValue.NORMAL.toString();
        anchorPane.getChildren().remove(vbox);
        anchorPane.getChildren().remove(label);

        NormalSpellCardEffect[] normalSpellCardEffects = NormalSpellCardEffect.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (NormalSpellCardEffect normalSpellCardEffect : normalSpellCardEffects) {
            String name = normalSpellCardEffect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, numberOfSelectedEnumSpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> getUserReplyForActivations(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);

    }


    private void getUserReplyForActivations(VBox vBox1, Button buttonForFinish1) {
        System.out.println(numberOfSelectedEnumSpell);

        anchorPane.getChildren().remove(vBox1);
        anchorPane.getChildren().remove(buttonForFinish1);

        selectedUserReplySpell = new ArrayList<>();
        UserReplyForActivation[] effects = UserReplyForActivation.values();
        ArrayList<Button> buttons = new ArrayList<>();
        Button buttonForFinish = new Button("OK");
        VBox vBox = new VBox();

        for (UserReplyForActivation normalSpellCardEffect : effects) {
            String name = normalSpellCardEffect.toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeAdditionOfThisEffectInTheGivenPlace(finalI, selectedUserReplySpell, buttons);
                }
            });
        }

        buttonForFinish.setOnAction(ActionEvent -> createSpellCard());
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutX(450);
        vBox.setLayoutY(100);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(500);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void createSpellCard() {
        System.out.println(selectedUserReplySpell);

        HashMap<String, List<String>> enumValues = new HashMap<>();

        EquipSpellEffect[] equipSpellEffects = EquipSpellEffect.values();
        FieldSpellEffect[] fieldSpellEffects = FieldSpellEffect.values();
        NormalSpellCardEffect[] normalSpellCardEffects = NormalSpellCardEffect.values();
        QuickSpellEffect[] quickSpellEffects = QuickSpellEffect.values();
        RitualSpellEffect[] ritualSpellEffects = RitualSpellEffect.values();
        ContinuousSpellCardEffect[] continuousSpellCardEffects = ContinuousSpellCardEffect.values();


        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> userReplyArrayList = new ArrayList<>();
        int counter = 0;
        switch (spellCardValue) {
            case "NORMAL":
                for (NormalSpellCardEffect normalSpellCardEffect : normalSpellCardEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(normalSpellCardEffect));
                    counter++;
                }

                break;
            case "EQUIP":
                for (EquipSpellEffect effect : equipSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(effect));
                    counter++;
                }

                break;
            case "FIELD":
                for (FieldSpellEffect effect : fieldSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(effect));
                    counter++;
                }

                break;
            case "RITUAL":
                for (RitualSpellEffect effect : ritualSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(effect));
                    counter++;
                }

                break;
            case "QUICK_PLAY":
                for (QuickSpellEffect effect : quickSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(effect));
                    counter++;
                }

                break;
            case "CONTINUOUS":
                for (ContinuousSpellCardEffect effect : continuousSpellCardEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter)))
                        strings.add(String.valueOf(effect));
                    counter++;
                }

                break;
        }
        enumValues.put("NormalSpellCardEffect", strings);
        enumValues.put("EquipSpellEffect", strings);
        enumValues.put("FieldSpellEffect", strings);
        enumValues.put("RitualSpellEffect", strings);
        enumValues.put("QuickSpellEffect", strings);
        enumValues.put("ContinuousSpellCardEffect", strings);
        enumValues.put("LogicalActivationRequirement", new ArrayList<>());
        enumValues.put("SentToGraveyardEffect", new ArrayList<>());

        UserReplyForActivation[] userReplyForActivations = UserReplyForActivation.values();
        counter = 0;
        for (UserReplyForActivation userReplyForActivation : userReplyForActivations) {
            if (selectedUserReplySpell.contains(Integer.valueOf(counter)))
                userReplyArrayList.add(String.valueOf(userReplyForActivation));
            counter++;
        }

        enumValues.put("UserReplyForActivation", userReplyArrayList);
        SpellCard spellCard = new SpellCard(cardName, cardDescription, SpellCardValue.valueOf(spellCardValue),
            CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, numberOfTurnsForActivationSpell, 0, enumValues, cardImage);

        Storage.addCardToNewCardsCrated(spellCard);
        Storage.saveNewImagesOfCardsInFile(spellCard, imagePath);
        //TODO : calculate card price
        //Should I add all of them even if they are empty?

        System.out.println("Card created successfully");
    }


    private void removeThingsInTheGetNumberOfAllowedUsages() {
        anchorPane.getChildren().remove(labelForGettingNumberOfAllowedUsagesFromUser);
        anchorPane.getChildren().remove(buttonOneForNumberOfAllowedUsages);
        anchorPane.getChildren().remove(buttonThreeForNumberOfAllowedUsages);
    }


}
