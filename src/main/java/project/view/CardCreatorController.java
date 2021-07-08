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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import project.controller.duel.CardEffects.MonsterEffectEnums.*;
import project.controller.duel.CardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import project.controller.duel.CardEffects.SpellEffectEnums.*;
import project.controller.duel.CardEffects.SpellEffectEnums.UserReplyForActivation;
import project.controller.duel.CardEffects.TrapEffectEnums.*;
import project.controller.duel.CardEffects.TrapEffectEnums.LogicalActivationRequirement;
import project.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;
import project.view.newClassesForCardCreator.MonsterCard1;
import project.view.newClassesForCardCreator.SpellCard1;
import project.view.newClassesForCardCreator.TrapCard1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardCreatorController implements Initializable {
    private int currentMoneyOfUser;
    ///
    private ArrayList<String> allSelectedEffectsAsStrings;
    private ArrayList<String> allSelectedEffectsThatHaveNumbers;

    private HashMap<String, List<String>> hashMapEffects;
    private HashMap<String, Integer> numbersOfEffectsToSend;
    private HashMap<String, List<String>> monsterFamilySelectedInSpell;
    ///
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
    private MonsterCardValue valueMonster;
    private MonsterCardFamily familyMonster;
    private MonsterCardAttribute attributeMonster;


    private int numberOfTurnsForActivationSpell;
    private String spellCardValue;
    private ArrayList<Integer> numberOfSelectedEnumSpell;
    private ArrayList<Integer> selectedUserReplySpell;


    private int numberOfTurnsForActivationForTrapCard;
    private int trapCardValueNumber;
    private TrapCardValue trapCardValue;
    private VBox lastVboxTrap;
    private VBox previousVbox;
    private Button previousButton;
    private String enumClassName = "";
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
    private ArrayList<Integer> monsterFamilyEffectsInTrapEquip;
    private ArrayList<Integer> monsterFamilyEffectsInTrapField;


    @FXML
    Button spellButton;
    @FXML
    Button trapButton;
    @FXML
    Button monsterButton;
    @FXML
    AnchorPane anchorPane;

    //    Label labelForGettingCardNameFromUser;
    TextField textFieldForGettingCardNameFromUser;
    Button buttonForGettingCardNameFromUser;


    //    Label labelForGettingCardDescriptionFromUser;
    TextField textFieldForGettingCardDescriptionFromUser;
    Button buttonForGettingCardDescriptionFromUser;

    Label labelForGettingNumberOfAllowedUsagesFromUser;
    Button buttonOneForNumberOfAllowedUsages;
    Button buttonThreeForNumberOfAllowedUsages;

    //    Label labelForGettingAttackPowerMonsterCard;
    TextField textFieldForGettingAttackPowerMonsterCard;
    Button buttonForGettingAttackPowerMonsterCard;

    //    Label labelForGettingDefencePowerMonsterCard;
    TextField textFieldForGettingDefencePowerMonsterCard;
    Button buttonForGettingDefencePowerMonsterCard;

    //    Label labelForGettingLevelMonsterCard;
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

    Label labelForShowingPrice;
    private int currentPrice;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentMoneyOfUser = LoginController.getOnlineUser().getMoney();
        allSelectedEffectsAsStrings = new ArrayList<>();
        allSelectedEffectsThatHaveNumbers = new ArrayList<>();
        hashMapEffects = new HashMap<>();
        numbersOfEffectsToSend = new HashMap<>();
        monsterFamilySelectedInSpell = new HashMap<>();
        currentPrice = 0;
        labelForShowingPrice = new Label();
        labelForShowingPrice.setLayoutX(800);
        labelForShowingPrice.setLayoutY(620);
        labelForShowingPrice.setMinWidth(100);
        labelForShowingPrice.setStyle("-fx-font-size: 20; -fx-alignment: CENTER; -fx-background-color: BLACK; -fx-min-height: 50" +
            "; -fx-min-width: 160; -fx-border-color: WHITE; -fx-border-width: 2");
        labelForShowingPrice.setTextFill(Color.web("#FFFFFFFF"));


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
//        labelForGettingCardNameFromUser = new Label("Please enter card name");
//        labelForGettingCardNameFromUser.setLayoutY(100);
//        labelForGettingCardNameFromUser.setLayoutX(420);
//        labelForGettingCardNameFromUser.setStyle("-fx-text-fill:#03d274;-fx-padding:4 0 8 0;" +
//            "-fx-background-color: #031fd2;-fx-font-weight:bold; -fx-font-size: 20");
//        anchorPane.getChildren().add(labelForGettingCardNameFromUser);

        anchorPane.getChildren().add(labelForShowingPrice);
        showPriceWithLabel();

        textFieldForGettingCardNameFromUser = new TextField();
        textFieldForGettingCardNameFromUser.setLayoutY(160);
        textFieldForGettingCardNameFromUser.setLayoutX(370);
        textFieldForGettingCardNameFromUser.promptTextProperty().setValue("Card Name");
        textFieldForGettingCardNameFromUser.setStyle("-fx-alignment: CENTER; -fx-font-size: 25");

        anchorPane.getChildren().add(textFieldForGettingCardNameFromUser);

        textFieldForGettingCardDescriptionFromUser = new TextField();
        textFieldForGettingCardDescriptionFromUser.setLayoutX(370);
        textFieldForGettingCardDescriptionFromUser.setLayoutY(250);
        textFieldForGettingCardDescriptionFromUser.promptTextProperty().setValue("Card Description");
        textFieldForGettingCardDescriptionFromUser.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-min-height: 200");
        anchorPane.getChildren().add(textFieldForGettingCardDescriptionFromUser);


        buttonForGettingCardNameFromUser = new Button("OK");
        buttonForGettingCardNameFromUser.setOnAction(actionEvent -> setCardName());
        buttonForGettingCardNameFromUser.setLayoutY(500);
        buttonForGettingCardNameFromUser.setLayoutX(480);
        buttonForGettingCardNameFromUser.setStyle("-fx-font-size: 25");
        anchorPane.getChildren().add(buttonForGettingCardNameFromUser);
    }

    private void showPriceWithLabel() {
        labelForShowingPrice.setText("PRICE  :   " + currentPrice);
    }


    private void setCardName() {
        if (!textFieldForGettingCardNameFromUser.getText().isEmpty()) {
            cardName = textFieldForGettingCardNameFromUser.getText();
            removeThingsInTheGetCardNameScene();
            getCardImage();
        }
    }


//    private void getCardDescription() {
////        labelForGettingCardDescriptionFromUser = new Label("Please enter the description for your card");
////        labelForGettingCardDescriptionFromUser.setLayoutY(100);
////        labelForGettingCardDescriptionFromUser.setLayoutX(400);
////        labelForGettingCardDescriptionFromUser.setStyle("-fx-text-fill:#03d274;-fx-padding:4 0 8 0;" +
////            "-fx-background-color: #031fd2;-fx-font-weight:bold; -fx-font-size: 20");
//
//        textFieldForGettingCardDescriptionFromUser = new TextField();
//        textFieldForGettingCardDescriptionFromUser.setLayoutX(450);
//        textFieldForGettingCardDescriptionFromUser.setLayoutY(200);
//
//        buttonForGettingCardDescriptionFromUser = new Button("OK");
//        buttonForGettingCardDescriptionFromUser.setLayoutY(400);
//        buttonForGettingCardDescriptionFromUser.setLayoutX(450);
//        buttonForGettingCardDescriptionFromUser.setOnAction(ActionEvent -> getCardImage());
//
////        anchorPane.getChildren().add(labelForGettingCardDescriptionFromUser);
//        anchorPane.getChildren().add(textFieldForGettingCardDescriptionFromUser);
//        anchorPane.getChildren().add(buttonForGettingCardDescriptionFromUser);
//
//    }


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
        }
        imagePath = file.getAbsolutePath();
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
        anchorPane.getChildren().remove(textFieldForGettingCardDescriptionFromUser);
        anchorPane.getChildren().remove(textFieldForGettingCardNameFromUser);
        anchorPane.getChildren().remove(buttonForGettingCardNameFromUser);
    }


    private void getNumberOfAllowedUsages() {
        cardDescription = textFieldForGettingCardDescriptionFromUser.getText();
        removeThingsInGettingCardDescription();
        labelForGettingNumberOfAllowedUsagesFromUser = new Label("Choose one of them for number of allowed usages");
        labelForGettingNumberOfAllowedUsagesFromUser.setLayoutY(200);
        labelForGettingNumberOfAllowedUsagesFromUser.setLayoutX(250);
        labelForGettingNumberOfAllowedUsagesFromUser.setStyle("-fx-background-color: #f5eeee; -fx-font-size: 25");
        anchorPane.getChildren().add(labelForGettingNumberOfAllowedUsagesFromUser);

        buttonOneForNumberOfAllowedUsages = new Button("One");
        buttonOneForNumberOfAllowedUsages.setOnAction(actionEvent -> getCardInformationBasedOnTheCardType(1));
        buttonOneForNumberOfAllowedUsages.setLayoutY(240);
        buttonOneForNumberOfAllowedUsages.setLayoutX(400);
        buttonOneForNumberOfAllowedUsages.setStyle("-fx-font-size: 30");
        anchorPane.getChildren().add(buttonOneForNumberOfAllowedUsages);

        buttonThreeForNumberOfAllowedUsages = new Button("Three");
        buttonThreeForNumberOfAllowedUsages.setOnAction(actionEvent -> getCardInformationBasedOnTheCardType(3));
        buttonThreeForNumberOfAllowedUsages.setLayoutY(240);
        buttonThreeForNumberOfAllowedUsages.setLayoutX(550);
        buttonThreeForNumberOfAllowedUsages.setStyle("-fx-font-size: 30");
        anchorPane.getChildren().add(buttonThreeForNumberOfAllowedUsages);
    }


    private void removeThingsInGettingCardDescription() {
//        anchorPane.getChildren().remove(labelForGettingCardDescriptionFromUser);
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
//        labelForGettingAttackPowerMonsterCard = new Label("Please enter the card's attack power");
//        labelForGettingAttackPowerMonsterCard.setLayoutY(100);
//        labelForGettingAttackPowerMonsterCard.setLayoutX(420);
//        anchorPane.getChildren().add(labelForGettingAttackPowerMonsterCard);

        textFieldForGettingAttackPowerMonsterCard = new TextField();
        textFieldForGettingAttackPowerMonsterCard.setLayoutY(200);
        textFieldForGettingAttackPowerMonsterCard.setLayoutX(370);
        textFieldForGettingAttackPowerMonsterCard.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-background-color: #f5eeee");
        textFieldForGettingAttackPowerMonsterCard.setPromptText("ATTACK POWER");
        anchorPane.getChildren().add(textFieldForGettingAttackPowerMonsterCard);

        buttonForGettingAttackPowerMonsterCard = new Button("OK");
        buttonForGettingAttackPowerMonsterCard.setLayoutY(290);
        buttonForGettingAttackPowerMonsterCard.setLayoutX(480);
        buttonForGettingAttackPowerMonsterCard.setStyle("-fx-font-size: 25");
        buttonForGettingAttackPowerMonsterCard.setOnAction(actionEvent -> getDefencePowerMonsterCard());
        anchorPane.getChildren().add(buttonForGettingAttackPowerMonsterCard);
    }


    private void getDefencePowerMonsterCard() {
        String attackPower = textFieldForGettingAttackPowerMonsterCard.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!attackPower.isEmpty() && pattern.matcher(attackPower).matches()) {
            attackPowerMonsterCard = Integer.parseInt(attackPower);
            currentPrice += attackPowerMonsterCard * 2;
            showPriceWithLabel();
            removeThingsInContinueGettingMonsterInformation();
//            labelForGettingDefencePowerMonsterCard = new Label("Please enter the card's defence power");
//            labelForGettingDefencePowerMonsterCard.setLayoutY(100);
//            labelForGettingDefencePowerMonsterCard.setLayoutX(420);
//            anchorPane.getChildren().add(labelForGettingDefencePowerMonsterCard);

            textFieldForGettingDefencePowerMonsterCard = new TextField();
            textFieldForGettingDefencePowerMonsterCard.setLayoutY(200);
            textFieldForGettingDefencePowerMonsterCard.setLayoutX(370);
            textFieldForGettingDefencePowerMonsterCard.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-background-color: #f5eeee");
            textFieldForGettingDefencePowerMonsterCard.setPromptText("DEFENCE POWER");
            anchorPane.getChildren().add(textFieldForGettingDefencePowerMonsterCard);

            buttonForGettingDefencePowerMonsterCard = new Button("OK");
            buttonForGettingDefencePowerMonsterCard.setLayoutY(290);
            buttonForGettingDefencePowerMonsterCard.setLayoutX(480);
            buttonForGettingDefencePowerMonsterCard.setStyle("-fx-font-size: 25");
            buttonForGettingDefencePowerMonsterCard.setOnAction(actionEvent -> getLevelMonsterCard());
            anchorPane.getChildren().add(buttonForGettingDefencePowerMonsterCard);
        }
    }


    private void getLevelMonsterCard() {
        String defencePower = textFieldForGettingDefencePowerMonsterCard.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!defencePower.isEmpty() && pattern.matcher(defencePower).matches()) {
            defencePowerMonsterCard = Integer.parseInt(defencePower);
            currentPrice += defencePowerMonsterCard * 1.5;
            showPriceWithLabel();
            removeThingsInGetDefencePowerMonsterCard();

//            labelForGettingLevelMonsterCard = new Label("Please enter the level for your monster card");
//            labelForGettingLevelMonsterCard.setLayoutY(100);
//            labelForGettingLevelMonsterCard.setLayoutX(360);
//            anchorPane.getChildren().add(labelForGettingLevelMonsterCard);

            textFieldForGettingLevelMonsterCard = new TextField();
            textFieldForGettingLevelMonsterCard.setLayoutY(200);
            textFieldForGettingLevelMonsterCard.setLayoutX(370);
            textFieldForGettingLevelMonsterCard.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-background-color: #f5eeee");
            textFieldForGettingLevelMonsterCard.setPromptText("LEVEL");
            anchorPane.getChildren().add(textFieldForGettingLevelMonsterCard);

            buttonForGettingLevelMonsterCard = new Button("OK");
            buttonForGettingLevelMonsterCard.setLayoutY(290);
            buttonForGettingLevelMonsterCard.setLayoutX(480);
            buttonForGettingLevelMonsterCard.setStyle("-fx-font-size: 25");
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
            vboxForMonsterCardAttribute.setLayoutX(400);
            vboxForMonsterCardAttribute.setMinHeight(300);
            vboxForMonsterCardAttribute.setMinWidth(200);
            vboxForMonsterCardAttribute.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
                " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
            vboxForMonsterCardAttribute.setSpacing(20);
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
        vboxForMonsterCardFamily.setLayoutY(60);
        vboxForMonsterCardFamily.setLayoutX(570);
        vboxForMonsterCardFamily.setMinHeight(600);
        vboxForMonsterCardFamily.setMinWidth(200);
        vboxForMonsterCardFamily.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vboxForMonsterCardFamily.setSpacing(13);

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
        vboxForMonsterCardFamily2.setLayoutY(60);
        vboxForMonsterCardFamily2.setLayoutX(230);
        vboxForMonsterCardFamily2.setMinHeight(600);
        vboxForMonsterCardFamily2.setMinWidth(200);
        vboxForMonsterCardFamily2.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vboxForMonsterCardFamily2.setSpacing(13);

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
        vboxForMonsterCardValues.setLayoutY(200);
        vboxForMonsterCardValues.setLayoutX(450);
        vboxForMonsterCardValues.setMinHeight(200);
        vboxForMonsterCardValues.setMinWidth(100);
        vboxForMonsterCardValues.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vboxForMonsterCardValues.setSpacing(13);

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
        vBoxForSummoningRequirement.setLayoutY(200);
        vBoxForSummoningRequirement.setLayoutX(190);
        vBoxForSummoningRequirement.setMinHeight(200);
        vBoxForSummoningRequirement.setMinWidth(100);
        vBoxForSummoningRequirement.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
//        vBoxForSummoningRequirement.setAlignment(Pos.CENTER);
        vBoxForSummoningRequirement.setSpacing(20);
        finishButtonForSummoningRequirement.setLayoutX(475);
        finishButtonForSummoningRequirement.setLayoutY(450);
        finishButtonForSummoningRequirement.setStyle("-fx-font-size: 25");
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
        vBoxForUponSummoningEffect.setLayoutY(200);
        vBoxForUponSummoningEffect.setLayoutX(90);
        vBoxForUponSummoningEffect.setMinHeight(200);
        vBoxForUponSummoningEffect.setMinWidth(100);
        vBoxForUponSummoningEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
//        vBoxForSummoningRequirement.setAlignment(Pos.CENTER);
        vBoxForUponSummoningEffect.setSpacing(20);
        buttonForFinishUponSummoningEffect.setLayoutX(475);
        buttonForFinishUponSummoningEffect.setLayoutY(450);
        buttonForFinishUponSummoningEffect.setStyle("-fx-font-size: 25");
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


        vBoxForBeingAttackedEffect.setLayoutY(110);
        vBoxForBeingAttackedEffect.setLayoutX(190);
        vBoxForBeingAttackedEffect.setMinHeight(200);
        vBoxForBeingAttackedEffect.setMinWidth(100);
        vBoxForBeingAttackedEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBoxForBeingAttackedEffect.setSpacing(20);
        buttonForFinishBeingAttackedEffect.setLayoutX(475);
        buttonForFinishBeingAttackedEffect.setLayoutY(520);
        buttonForFinishBeingAttackedEffect.setStyle("-fx-font-size: 25");

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


        vBoxForContinuousMonsterEffect.setLayoutY(180);
        vBoxForContinuousMonsterEffect.setLayoutX(190);
        vBoxForContinuousMonsterEffect.setMinHeight(200);
        vBoxForContinuousMonsterEffect.setMinWidth(100);
        vBoxForContinuousMonsterEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBoxForContinuousMonsterEffect.setSpacing(20);
        buttonForFinishContinuousMonsterEffect.setLayoutX(475);
        buttonForFinishContinuousMonsterEffect.setLayoutY(480);
        buttonForFinishContinuousMonsterEffect.setStyle("-fx-font-size: 25");


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


        vBoxForFlipEffect.setLayoutY(310);
        vBoxForFlipEffect.setLayoutX(370);
        vBoxForFlipEffect.setMinHeight(100);
        vBoxForFlipEffect.setMinWidth(90);
        vBoxForFlipEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBoxForFlipEffect.setSpacing(20);
        buttonForFinishFlipEffect.setLayoutX(475);
        buttonForFinishFlipEffect.setLayoutY(420);
        buttonForFinishFlipEffect.setStyle("-fx-font-size: 25");


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


        vBoxForOptionalMonsterEffect.setLayoutY(200);
        vBoxForOptionalMonsterEffect.setLayoutX(20);
        vBoxForOptionalMonsterEffect.setMinHeight(100);
        vBoxForOptionalMonsterEffect.setMinWidth(90);
        vBoxForOptionalMonsterEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBoxForOptionalMonsterEffect.setSpacing(20);
        buttonForFinishOptionalMonsterEffect.setLayoutX(475);
        buttonForFinishOptionalMonsterEffect.setLayoutY(420);
        buttonForFinishOptionalMonsterEffect.setStyle("-fx-font-size: 25");


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

        vBoxForSentToGraveyardEffect.setLayoutY(300);
        vBoxForSentToGraveyardEffect.setLayoutX(380);
        vBoxForSentToGraveyardEffect.setMinHeight(100);
        vBoxForSentToGraveyardEffect.setMinWidth(90);
        vBoxForSentToGraveyardEffect.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBoxForSentToGraveyardEffect.setSpacing(20);
        buttonForFinishSentToGraveyardEffect.setLayoutX(475);
        buttonForFinishSentToGraveyardEffect.setLayoutY(430);
        buttonForFinishSentToGraveyardEffect.setStyle("-fx-font-size: 25");


        buttonForFinishSentToGraveyardEffect.setOnAction(ActionEvent -> finishMonsterCardCreator());

        for (Button button : buttonsForSentToGraveyardEffect) {
            vBoxForSentToGraveyardEffect.getChildren().add(button);
        }

        anchorPane.getChildren().add(vBoxForSentToGraveyardEffect);
        anchorPane.getChildren().add(buttonForFinishSentToGraveyardEffect);
    }


    private void finishMonsterCardCreator() {
        anchorPane.getChildren().remove(vBoxForSentToGraveyardEffect);
        anchorPane.getChildren().remove(buttonForFinishSentToGraveyardEffect);
        //Attribute
        MonsterCardAttribute[] allValues = MonsterCardAttribute.values();
        MonsterCardAttribute attribute = null;
        int counter = 0;
        for (MonsterCardAttribute allValue : allValues) {
            if (counter == monsterAttributeNumber) {
                attributeMonster = allValue;
//                allSelectedEffectsAsStrings.add(String.valueOf(allValue));
            }
            counter++;
        }
        //Family
        MonsterCardFamily[] allValuesFamily = MonsterCardFamily.values();
        MonsterCardFamily family = null;
        counter = 0;
        for (MonsterCardFamily monsterCardFamily : allValuesFamily) {
            if (counter == monsterFamilyNumber) {
                familyMonster = monsterCardFamily;
//                allSelectedEffectsAsStrings.add(String.valueOf(monsterCardFamily));
            }
            counter++;
        }
        //Value
        MonsterCardValue[] allValuesValue = MonsterCardValue.values();
        MonsterCardValue value = null;
        counter = 0;
        for (MonsterCardValue monsterCardValue : allValuesValue) {
            if (counter == monsterValuesNumber) {
                valueMonster = monsterCardValue;
//                allSelectedEffectsAsStrings.add(String.valueOf(monsterCardValue));
            }
            counter++;
        }
        //Start Enums
        HashMap<String, List<String>> monsterHashMap = new HashMap<>();
        //add SummoningRequirement
        SummoningRequirement[] summoningRequirements = SummoningRequirement.values();
        ArrayList<String> selectedArrayList = new ArrayList<>();
        counter = 0;
        for (SummoningRequirement summoningRequirement : summoningRequirements) {
            if (selectedUponSummoningEffect.contains(Integer.valueOf(counter))) {
                selectedArrayList.add(String.valueOf(summoningRequirement));
                allSelectedEffectsAsStrings.add(String.valueOf(summoningRequirement));
            }

            counter++;
        }
        monsterHashMap.put("SummoningRequirement", selectedArrayList);
        //add UponSummoningEffect
        UponSummoningEffect[] uponSummoningEffects = UponSummoningEffect.values();
        ArrayList<String> selectedUponSummoning = new ArrayList<>();
        counter = 0;
        for (UponSummoningEffect uponSummoningEffect : uponSummoningEffects) {
            if (selectedUponSummoningEffect.contains(Integer.valueOf(counter))) {
                selectedUponSummoning.add(String.valueOf(uponSummoningEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(uponSummoningEffect));
            }
            counter++;
        }
        monsterHashMap.put("UponSummoningEffect", selectedUponSummoning);

        //add BeingAttackedEffect
        BeingAttackedEffect[] beingAttackedEffects = BeingAttackedEffect.values();
        ArrayList<String> selectedBeingAttacked = new ArrayList<>();
        counter = 0;
        for (BeingAttackedEffect beingAttackedEffect : beingAttackedEffects) {
            if (selectedBeingAttackedEffect.contains(Integer.valueOf(counter))) {
                selectedBeingAttacked.add(String.valueOf(beingAttackedEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(beingAttackedEffect));
            }
            counter++;
        }
        monsterHashMap.put("BeingAttackedEffect", selectedBeingAttacked);

        //add ContinuousMonsterEffect
        ContinuousMonsterEffect[] continuousMonsterEffects = ContinuousMonsterEffect.values();
        ArrayList<String> selectedContinuousMonster = new ArrayList<>();
        counter = 0;
        for (ContinuousMonsterEffect continuousMonsterEffect : continuousMonsterEffects) {
            if (selectedContinuousMonsterEffect.contains(Integer.valueOf(counter))) {
                selectedContinuousMonster.add(String.valueOf(continuousMonsterEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(continuousMonsterEffect));
            }
            counter++;
        }
        monsterHashMap.put("ContinuousMonsterEffect", selectedContinuousMonster);

        //add FlipEffect
        FlipEffect[] flipEffects = FlipEffect.values();
        ArrayList<String> selectedFlip = new ArrayList<>();
        counter = 0;
        for (FlipEffect flipEffect : flipEffects) {
            if (selectedFlipEffect.contains(Integer.valueOf(counter))) {
                selectedFlip.add(String.valueOf(flipEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(flipEffect));
            }
            counter++;
        }
        monsterHashMap.put("FlipEffect", selectedFlip);

        //add OptionalMonsterEffect
        OptionalMonsterEffect[] optionalMonsterEffects = OptionalMonsterEffect.values();
        ArrayList<String> selectedOptionalMonster = new ArrayList<>();
        counter = 0;
        for (OptionalMonsterEffect optionalMonsterEffect : optionalMonsterEffects) {
            if (selectedOptionalMonsterEffect.contains(Integer.valueOf(counter))) {
                selectedOptionalMonster.add(String.valueOf(optionalMonsterEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(optionalMonsterEffect));
            }
            counter++;
        }
        monsterHashMap.put("OptionalMonsterEffect", selectedOptionalMonster);

        //add SentToGraveyardEffect
        SentToGraveyardEffect[] sentToGraveyardEffects = SentToGraveyardEffect.values();
        ArrayList<String> selectedSent = new ArrayList<>();
        counter = 0;
        for (SentToGraveyardEffect sentToGraveyardEffect : sentToGraveyardEffects) {
            if (selectedSentToGraveyardEffect.contains(Integer.valueOf(counter))) {
                selectedSent.add(String.valueOf(sentToGraveyardEffect));
                allSelectedEffectsAsStrings.add(String.valueOf(sentToGraveyardEffect));
            }
            counter++;
        }
        monsterHashMap.put("SentToGraveyardEffect", selectedSent);

        hashMapEffects = monsterHashMap;
        allSelectedEffectsThatHaveNumbers = getEnumsWithNumbers();
        getNumberOfUserMonsterCard();
//        MonsterCard monsterCard = new MonsterCard(attackPowerMonsterCard, defencePowerMonsterCard, levelOfMonsterCard, attribute,
//            family, value, cardName, cardDescription, CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, 0, monsterHashMap, cardImage);
//        Storage.addCardToNewCardsCrated(monsterCard);
//        Storage.saveNewImagesOfCardsInFile(monsterCard, imagePath);


        //TODO -> calculate card price

//        System.out.println("Card Created and added to storage successfully");
//        backToMainMenu();
    }

    private void getNumberOfUserMonsterCard() {

        if (allSelectedEffectsThatHaveNumbers.size() != 0) {

            CustomDialog customDialog = new CustomDialog("INPUT", "Enter a number for:\n" + allSelectedEffectsThatHaveNumbers.get(0));
            customDialog.openDialog();
            TextField textField = new TextField();
            textField.setLayoutX(340);
            textField.setLayoutY(200);

            anchorPane.getChildren().add(textField);

            Button endButton = new Button("OK");
            endButton.setLayoutX(450);
            endButton.setLayoutY(270);
            endButton.setStyle("-fx-font-size: 25;");
            anchorPane.getChildren().add(endButton);
            endButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Pattern pattern = Pattern.compile("^\\d+$");
                    if (!textField.getText().isEmpty() && pattern.matcher(textField.getText()).find()) {
                        numbersOfEffectsToSend.put(allSelectedEffectsThatHaveNumbers.get(0), Integer.valueOf(textField.getText()));
                        allSelectedEffectsThatHaveNumbers.remove(0);
                        anchorPane.getChildren().remove(endButton);
                        anchorPane.getChildren().remove(textField);
                        if (allSelectedEffectsThatHaveNumbers.size() != 0) {
                            getNumberOfUserMonsterCard();
                        } else {
                            lastStepOfCreatingMonsterCard();
                        }
                    }

                }
            });
        } else {
            lastStepOfCreatingMonsterCard();
        }
    }

    private void lastStepOfCreatingMonsterCard() {
        if (currentMoneyOfUser < currentPrice * 0.1) {
            CustomDialog customDialog = new CustomDialog("ERROR", "NOT ENOUGH MONEY");
            customDialog.openDialog();
        } else {
            LoginController.getOnlineUser().setMoney((int) (currentMoneyOfUser - 0.1 * currentPrice));
            MonsterCard1 monsterCard1 = new MonsterCard1(attackPowerMonsterCard, defencePowerMonsterCard, levelOfMonsterCard, attributeMonster,
                familyMonster, valueMonster, cardName, cardDescription, CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, currentPrice, hashMapEffects, cardImage, numbersOfEffectsToSend);

            System.out.println("Created");
            CustomDialog customDialog = new CustomDialog("MESSAGE", "Card Created Successfully");
            customDialog.openDialog();
            UIStorage.addNodesForViewWhenNewCardCreated();
        }
        backToMainMenu();
    }

    private ArrayList<String> getEnumsWithNumbers() {
        ArrayList<String> answer = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{2,3}");
        for (String allSelectedEffectsAsString : allSelectedEffectsAsStrings) {
            Matcher matcher = pattern.matcher(allSelectedEffectsAsString);
            if (matcher.find()) {
                answer.add(allSelectedEffectsAsString);
            }
        }
        return answer;
    }

    private void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void changeAdditionOfThisEffectInTheGivenPlace(int finalI, ArrayList<Integer> integersValues, ArrayList<Button> buttons) {
        if (integersValues.contains(finalI)) {
            currentPrice -= 2500;
            integersValues.remove(Integer.valueOf(finalI));
            buttons.get(finalI).setStyle("-fx-background-color: #000000");
        } else {
            currentPrice += 2500;
            integersValues.add(finalI);
            buttons.get(finalI).setStyle("-fx-background-color: #0c7bea;");
        }
        showPriceWithLabel();
    }

    private void changeAdditionOfThisEffectInTheGivenPlace2(int finalI, ArrayList<Integer> integersValues, ArrayList<Button> buttons) {
        if (integersValues.contains(finalI)) {
            currentPrice -= 2500;
            integersValues.remove(Integer.valueOf(finalI));
            buttons.get(finalI - 12).setStyle("-fx-background-color: #000000FF");
            showPriceWithLabel();
        } else {
            currentPrice += 2500;
            integersValues.add(finalI);
            buttons.get(finalI - 12).setStyle("-fx-background-color: #0c7bea;");
            showPriceWithLabel();
        }
    }


    private void removeThingsInGetLevelMonsterCard() {
//        anchorPane.getChildren().remove(labelForGettingLevelMonsterCard);
        anchorPane.getChildren().remove(textFieldForGettingLevelMonsterCard);
        anchorPane.getChildren().remove(buttonForGettingLevelMonsterCard);
    }


    private void removeThingsInGetDefencePowerMonsterCard() {
//        anchorPane.getChildren().remove(labelForGettingDefencePowerMonsterCard);
        anchorPane.getChildren().remove(textFieldForGettingDefencePowerMonsterCard);
        anchorPane.getChildren().remove(buttonForGettingDefencePowerMonsterCard);
    }


    private void removeThingsInContinueGettingMonsterInformation() {
//        anchorPane.getChildren().remove(labelForGettingAttackPowerMonsterCard);
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

        Button button = new Button("OK");
        TextField textField = new TextField();
        textField.setLayoutY(200);
        textField.setLayoutX(310);
        textField.setMinWidth(400);
        textField.setStyle("-fx-alignment: CENTER; -fx-font-size: 20; -fx-background-color: #f5eeee");
        textField.setPromptText("NUMBER OF TURNS FOR ACTIVATION");

        button.setLayoutY(290);
        button.setLayoutX(480);
        button.setStyle("-fx-font-size: 25");
        button.setOnAction(ActionEvent -> getTrapCardValue(textField, button));

        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(button);
    }

    private void getTrapCardValue(TextField textField, Button button) {
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
            anchorPane.getChildren().remove(button);

            ArrayList<Button> buttons = new ArrayList<>();
            SpellCardValue[] spellCardValues = SpellCardValue.values();
            previousVbox = new VBox();
            for (SpellCardValue cardValue : spellCardValues) {
                buttons.add(new Button(cardValue.toString()));
            }

            for (int i = 0; i < buttons.size(); i++) {
                int finalI = i;
                buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getFlipSummonTrapCardEffect(finalI);
                    }
                });
            }

            for (Button button1 : buttons) {
                previousVbox.getChildren().add(button1);
            }
            previousVbox.setLayoutY(100);
            previousVbox.setLayoutX(400);
            anchorPane.getChildren().add(previousVbox);


        }
    }

    public void getFlipSummonTrapCardEffect(int finalI1) {
        trapCardValueNumber = finalI1;
        System.out.println(trapCardValueNumber);

        enumClassName = "FlipSummonTrapCardEffect";
        trapController();
//        nextMethod = "getMonsterAttackingTrapCardEffect";
//        gotoTrapFunctionEffect();
    }

//    public void getMonsterAttackingTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "MonsterAttackingTrapCardEffect";
////        nextMethod = "getNormalSummonTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//
//    public void getNormalSummonTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "NormalSummonTrapCardEffect";
////        nextMethod = "getTributeSummonTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getTributeSummonTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "TributeSummonTrapCardEffect";
////        nextMethod = "getNormalTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getNormalTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "NormalTrapCardEffect";
////        nextMethod = "getRitualSummonTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getRitualSummonTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "RitualSummonTrapCardEffect";
////        nextMethod = "getSpecialSummonTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getSpecialSummonTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "SpecialSummonTrapCardEffect";
////        nextMethod = "getMonsterEffectActivationTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getMonsterEffectActivationTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "MonsterEffectActivationTrapCardEffect";
////        nextMethod = "getSpellCardActivationTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getSpellCardActivationTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "SpellCardActivationTrapCardEffect";
////        nextMethod = "getTrapCardActivationTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }
//
//    public void getTrapCardActivationTrapCardEffect() {
//        anchorPane.getChildren().remove(previousButton);
//        anchorPane.getChildren().remove(previousVbox);
//        enumClassName = "TrapCardActivationTrapCardEffect";
//        gotoTrapFunctionEffect();
//    }

    public void getUserReplyForActivation() {
        anchorPane.getChildren().remove(previousButton);
        anchorPane.getChildren().remove(previousVbox);

        previousVbox = null;
        previousVbox = new VBox();
        previousButton = null;
        previousButton = new Button("OK");
        ArrayList<Button> buttons = new ArrayList<>();
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


        previousVbox.setLayoutY(100);
        previousVbox.setLayoutX(400);
        previousButton.setLayoutY(400);
        previousButton.setLayoutX(400);
        previousButton.setOnAction(ActionEvent -> finishTrapCard());

        for (Button button1 : buttons) {
            previousVbox.getChildren().add(button1);
        }

        anchorPane.getChildren().add(previousButton);
        anchorPane.getChildren().add(previousVbox);
    }

    private void finishTrapCard() {
        System.out.println("Trying to create");
        int counter = 0;
        TrapCardValue[] trapCardValues = TrapCardValue.values();
        for (TrapCardValue trapCardValue1 : trapCardValues) {
            if (trapCardValueNumber == counter) trapCardValue = trapCardValue1;
            counter++;
        }


        ContinuousTrapCardEffect[] continuousTrapCardEffects1 = ContinuousTrapCardEffect.values();//empty
        FlipSummonTrapCardEffect[] flipSummonTrapCardEffects1 = FlipSummonTrapCardEffect.values();
        LogicalActivationRequirement[] logicalActivationRequirements1 = LogicalActivationRequirement.values();//empty
        MonsterAttackingTrapCardEffect[] monsterAttackingTrapCardEffects1 = MonsterAttackingTrapCardEffect.values();
        NormalSummonTrapCardEffect[] normalSummonTrapCardEffects1 = NormalSummonTrapCardEffect.values();
        TributeSummonTrapCardEffect[] tributeSummonTrapCardEffects1 = TributeSummonTrapCardEffect.values();
        NormalTrapCardEffect[] normalTrapCardEffects1 = NormalTrapCardEffect.values();
        RitualSummonTrapCardEffect[] ritualSummonTrapCardEffects1 = RitualSummonTrapCardEffect.values();
        SpecialSummonTrapCardEffect[] specialSummonTrapCardEffects1 = SpecialSummonTrapCardEffect.values();
        MonsterEffectActivationTrapCardEffect[] monsterEffectActivationTrapCardEffects1 = MonsterEffectActivationTrapCardEffect.values();
        SpellCardActivationTrapCardEffect[] spellCardActivationTrapCardEffects1 = SpellCardActivationTrapCardEffect.values();
        TrapCardActivationTrapCardEffect[] trapCardActivationTrapCardEffects1 = TrapCardActivationTrapCardEffect.values();
        UserReplyForActivation[] userReplyForActivations1 = UserReplyForActivation.values();


        ArrayList<String> flip = new ArrayList<>();
        ArrayList<String> monsterAttack = new ArrayList<>();
        ArrayList<String> normalSummon = new ArrayList<>();
        ArrayList<String> tribute = new ArrayList<>();
        ArrayList<String> normalTrap = new ArrayList<>();
        ArrayList<String> ritual = new ArrayList<>();
        ArrayList<String> special = new ArrayList<>();
        ArrayList<String> monsterActivation = new ArrayList<>();
        ArrayList<String> spell = new ArrayList<>();
        ArrayList<String> trap = new ArrayList<>();
        ArrayList<String> user = new ArrayList<>();


        counter = 0;

        for (FlipSummonTrapCardEffect effect : flipSummonTrapCardEffects1) {
            if (flipSummonTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                flip.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (MonsterAttackingTrapCardEffect effect : monsterAttackingTrapCardEffects1) {
            if (monsterAttackingTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                monsterAttack.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (NormalSummonTrapCardEffect effect : normalSummonTrapCardEffects1) {
            if (normalSummonTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                normalSummon.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (TributeSummonTrapCardEffect effect : tributeSummonTrapCardEffects1) {
            if (tributeSummonTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                tribute.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (NormalTrapCardEffect effect : normalTrapCardEffects1) {
            if (normalTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                normalTrap.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (RitualSummonTrapCardEffect effect : ritualSummonTrapCardEffects1) {
            if (ritualSummonTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                ritual.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (SpecialSummonTrapCardEffect effect : specialSummonTrapCardEffects1) {
            if (specialSummonTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                special.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (MonsterEffectActivationTrapCardEffect effect : monsterEffectActivationTrapCardEffects1) {
            if (monsterEffectActivationTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                monsterActivation.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (SpellCardActivationTrapCardEffect effect : spellCardActivationTrapCardEffects1) {
            if (spellCardActivationTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                spell.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }

        counter = 0;
        for (SpellCardActivationTrapCardEffect effect : spellCardActivationTrapCardEffects1) {
            if (spellCardActivationTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                spell.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (TrapCardActivationTrapCardEffect effect : trapCardActivationTrapCardEffects1) {
            if (trapCardActivationTrapCardEffectNumbers.contains(Integer.valueOf(counter))) {
                trap.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }


        counter = 0;
        for (UserReplyForActivation effect : userReplyForActivations1) {
            if (userReplyForActivationNumbers.contains(Integer.valueOf(counter))) {
                user.add(String.valueOf(effect));
                allSelectedEffectsAsStrings.add(String.valueOf(effect));
            }
            counter++;
        }

        HashMap<String, List<String>> enums = new HashMap<>();

        enums.put("ContinuousTrapCardEffects", new ArrayList<>());
        enums.put("LogicalActivationRequirements", new ArrayList<>());
        enums.put("FlipSummonTrapCardEffect", flip);
        enums.put("MonsterAttackingTrapCardEffect", monsterAttack);
        enums.put("NormalSummonTrapCardEffect", normalSummon);
        enums.put("TributeSummonTrapCardEffect", tribute);
        enums.put("NormalTrapCardEffect", normalTrap);
        enums.put("RitualSummonTrapCardEffect", ritual);
        enums.put("SpecialSummonTrapCardEffect", special);
        enums.put("MonsterEffectActivationTrapCardEffect", monsterActivation);
        enums.put("SpellCardActivationTrapCardEffect", spell);
        enums.put("TrapCardActivationTrapCardEffect", trap);
        enums.put("UserReplyForActivation", user);

        hashMapEffects = enums;
        allSelectedEffectsThatHaveNumbers = getEnumsWithNumbers();

        getNumberOfUserTrapCard();
//        TrapCard trapCard = new TrapCard(cardName, cardDescription, trapValue, CardPosition.NOT_APPLICABLE,
//            numberOfAllowedUsages, numberOfTurnsForActivationForTrapCard, 0, enums, cardImage);
//        Storage.addCardToNewCardsCrated(trapCard);
//        Storage.saveNewImagesOfCardsInFile(trapCard, imagePath);
//        Storage.addCardToNewCardsCrated(trapCard);
//        Storage.addNewImageForNewCards(cardName, cardImage);
//        System.out.println("card was created");
//        try {
//            new MainView().changeView("/project/fxml/mainMenu.fxml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void getNumberOfUserTrapCard() {
        System.out.println("start");
        if (allSelectedEffectsThatHaveNumbers.size() != 0) {
            System.out.println("hi");
            CustomDialog customDialog = new CustomDialog("Input", "Enter a number for:\n" + allSelectedEffectsThatHaveNumbers.get(0));
            customDialog.openDialog();
            TextField textField = new TextField();
            textField.setLayoutX(340);
            textField.setLayoutY(200);
            textField.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-background-color: #f5eeee");
            anchorPane.getChildren().add(textField);

            Button endButton = new Button("OK");
            endButton.setLayoutX(450);
            endButton.setLayoutY(270);
            endButton.setStyle("-fx-font-size: 25;");
            anchorPane.getChildren().add(endButton);
            endButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Pattern pattern = Pattern.compile("^\\d+$");
                    if (!textField.getText().isEmpty() && pattern.matcher(textField.getText()).find()) {
                        numbersOfEffectsToSend.put(allSelectedEffectsThatHaveNumbers.get(0), Integer.valueOf(textField.getText()));
                        allSelectedEffectsThatHaveNumbers.remove(0);
                        anchorPane.getChildren().remove(endButton);
                        anchorPane.getChildren().remove(textField);
                        if (allSelectedEffectsThatHaveNumbers.size() != 0) {
                            getNumberOfUserTrapCard();
                        } else {
                            lastStepOfCreatingTrapCard();
                        }
                    }

                }
            });
        } else {
            lastStepOfCreatingTrapCard();
        }
    }

    private void lastStepOfCreatingTrapCard() {
        if (currentMoneyOfUser < currentPrice * 0.1) {
            CustomDialog customDialog = new CustomDialog("ERROR", "NOT ENOUGH MONEY");
            customDialog.openDialog();
        } else {
            LoginController.getOnlineUser().setMoney((int) (currentMoneyOfUser - 0.1 * currentPrice));
            TrapCard1 trapCard = new TrapCard1(cardName, cardDescription, trapCardValue, CardPosition.NOT_APPLICABLE,
                numberOfAllowedUsages, numberOfTurnsForActivationForTrapCard, currentPrice, hashMapEffects, cardImage, numbersOfEffectsToSend);
            System.out.println("Card Created");
            CustomDialog customDialog = new CustomDialog("MESSAGE", "Card Created Successfully");
            customDialog.openDialog();
            UIStorage.addNodesForViewWhenNewCardCreated();
        }

        backToMainMenu();
    }


    private void gotoTrapFunctionEffect() {


        System.out.println("we are in:" + enumClassName);

        previousVbox = null;
        previousVbox = new VBox();

        previousButton = null;
        previousButton = new Button("OK");
        ArrayList<Button> buttons = new ArrayList<>();
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


        previousVbox.setLayoutY(100);
        previousVbox.setLayoutX(400);
        previousButton.setLayoutY(400);
        previousButton.setLayoutX(400);
        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                trapController();
            }
        });
//        buttonForFinish.setOnAction(ActionEvent -> {
//            try {
////                finalMethod.invoke(null, vBox, buttonForFinish);
//                finalMethod.invoke(vBox, buttonForFinish);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });

        for (Button button : buttons) {
            previousVbox.getChildren().add(button);
        }

        anchorPane.getChildren().add(previousVbox);
        anchorPane.getChildren().add(previousButton);
    }

    private void trapController() {
        if (anchorPane.getChildren().contains(previousButton)) {
            anchorPane.getChildren().remove(previousButton);
        }
        if (anchorPane.getChildren().contains(previousVbox)) {
            anchorPane.getChildren().remove(previousVbox);
        }

        switch (enumClassName) {
            case "FlipSummonTrapCardEffect":
                enumClassName = "MonsterAttackingTrapCardEffect";
                break;
            case "MonsterAttackingTrapCardEffect":
                enumClassName = "NormalSummonTrapCardEffect";
                break;
            case "NormalSummonTrapCardEffect":
                enumClassName = "TributeSummonTrapCardEffect";
                break;
            case "TributeSummonTrapCardEffect":
                enumClassName = "NormalTrapCardEffect";
                break;
            case "NormalTrapCardEffect":
                enumClassName = "RitualSummonTrapCardEffect";
                break;
            case "RitualSummonTrapCardEffect":
                enumClassName = "SpecialSummonTrapCardEffect";
                break;
            case "SpecialSummonTrapCardEffect":
                enumClassName = "MonsterEffectActivationTrapCardEffect";
                break;
            case "MonsterEffectActivationTrapCardEffect":
                enumClassName = "SpellCardActivationTrapCardEffect";
                break;
            case "SpellCardActivationTrapCardEffect":
                enumClassName = "TrapCardActivationTrapCardEffect";
                break;
            case "TrapCardActivationTrapCardEffect":
                enumClassName = "UserReplyForActivation";
                break;
            case "UserReplyForActivation":
                enumClassName = "a";
                break;
        }
        if (!enumClassName.equals("a")) gotoTrapFunctionEffect();
        else {
            finishTrapCard();
        }
    }


    private void continueGettingSpellInformation() {
        getNumberOfTurnsForActivation();
    }


    private void getNumberOfTurnsForActivation() {

        TextField textField = new TextField();

        Button button = new Button("OK");
        button.setOnAction(ActionEvent -> getSpellCardValue(textField, button));
        textField.setLayoutY(200);
        textField.setLayoutX(310);
        textField.setMinWidth(400);
        textField.setStyle("-fx-alignment: CENTER; -fx-font-size: 20; -fx-background-color: #f5eeee");
        textField.setPromptText("NUMBER OF TURNS FOR ACTIVATION");

        button.setLayoutY(290);
        button.setLayoutX(480);
        button.setStyle("-fx-font-size: 25");


        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(button);

    }


    private void getSpellCardValue(TextField textField, Button button) {
        Pattern pattern = Pattern.compile("^\\d+$");
        String numberOfTurns = textField.getText();
        if (!numberOfTurns.isEmpty() && pattern.matcher(numberOfTurns).matches()) {
            numberOfTurnsForActivationSpell = Integer.parseInt(numberOfTurns);
            anchorPane.getChildren().remove(textField);
            anchorPane.getChildren().remove(button);

            Label newLabel = new Label("Please choose one of these");
            newLabel.setLayoutY(100);
            newLabel.setLayoutX(450);

            VBox vbox = new VBox();


            vbox.setLayoutY(200);
            vbox.setLayoutX(430);
            vbox.setMinHeight(200);
            vbox.setMinWidth(100);


            vbox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
            vbox.setSpacing(20);

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
                        buttons.get(i).setOnAction(ActionEvent -> normalSpell(vbox));
                        break;
                    case 1:
                        buttons.get(i).setOnAction(ActionEvent -> beforeEquipSpell(vbox));
                        break;
                    case 2:
                        buttons.get(i).setOnAction(ActionEvent -> beforeFieldSpell(vbox));
                        break;
                    case 3:
                        buttons.get(i).setOnAction(ActionEvent -> ritualSpell(vbox));
                        break;
                    case 4:
                        buttons.get(i).setOnAction(ActionEvent -> quickPlaySpell(vbox));
                        break;
                    case 5:
                        buttons.get(i).setOnAction(ActionEvent -> continuousSpell(vbox));
                        break;

                }
            }

            numberOfSelectedEnumSpell = new ArrayList<>();
            anchorPane.getChildren().add(vbox);

        }
    }

    private void beforeFieldSpell(VBox vbox) {
        anchorPane.getChildren().remove(vbox);

        ArrayList<Button> buttons = new ArrayList<>();
        MonsterCardFamily[] values = MonsterCardFamily.values();
        monsterFamilyEffectsInTrapField = new ArrayList<>();

        for (int i = 0; i < values.length / 2; i++) {
            String name = values[i].toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
            int finalI1 = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (monsterFamilyEffectsInTrapField.contains(Integer.valueOf(finalI1))) {
                        currentPrice += 2500;
                    }
                    else {
                        currentPrice -= 2500;
                    }
                    changeAdditionOfThisEffectInTheGivenPlace(finalI1, monsterFamilyEffectsInTrapField, buttons);
                }
            });
        }

        VBox vbox1 = new VBox();
        vbox1.setLayoutY(60);
        vbox1.setLayoutX(570);
        vbox1.setMinHeight(600);
        vbox1.setMinWidth(200);
        vbox1.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vbox1.setSpacing(13);

        for (Button button : buttons) {
            vbox1.getChildren().add(button);
        }
        anchorPane.getChildren().add(vbox1);


        ArrayList<Button> buttons2 = new ArrayList<>();
        for (int i = values.length / 2; i < values.length; i++) {
            String name = values[i].toString();
            buttons2.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
            int finalI1 = i;
            buttons2.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (monsterFamilyEffectsInTrapField.contains(Integer.valueOf(finalI1 + 12))) {
                        currentPrice += 2500;
                    }
                    else {
                        currentPrice -= 2500;
                    }
                    changeAdditionOfThisEffectInTheGivenPlace2(finalI1 + 12, monsterFamilyEffectsInTrapField, buttons2);
                }
            });
        }

        VBox vBox2 = new VBox();
        vBox2.setLayoutY(60);
        vBox2.setLayoutX(230);
        vBox2.setMinHeight(600);
        vBox2.setMinWidth(200);
        vBox2.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox2.setSpacing(13);

        for (Button button : buttons2) {
            vBox2.getChildren().add(button);
        }

        Button endButton = new Button("OK");
        endButton.setLayoutY(330);
        endButton.setLayoutX(465);
        endButton.setStyle("-fx-font-size: 25");
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fieldSpell(vbox1, vBox2, endButton);
            }
        });

        anchorPane.getChildren().add(endButton);

        anchorPane.getChildren().add(vBox2);
    }

    private void beforeEquipSpell(VBox vbox) {

        anchorPane.getChildren().remove(vbox);

        ArrayList<Button> buttons = new ArrayList<>();
        MonsterCardFamily[] values = MonsterCardFamily.values();
        monsterFamilyEffectsInTrapEquip = new ArrayList<>();

        for (int i = 0; i < values.length / 2; i++) {
            String name = values[i].toString();
            buttons.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
            int finalI1 = i;
            buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (monsterFamilyEffectsInTrapEquip.contains(Integer.valueOf(finalI1))) {
                        currentPrice += 2500;
                    }
                    else {
                        currentPrice -= 2500;
                    }
                    changeAdditionOfThisEffectInTheGivenPlace(finalI1, monsterFamilyEffectsInTrapEquip, buttons);
                }
            });
        }

        VBox vbox1 = new VBox();
        vbox1.setLayoutY(60);
        vbox1.setLayoutX(570);
        vbox1.setMinHeight(600);
        vbox1.setMinWidth(200);
        vbox1.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vbox1.setSpacing(13);

        for (Button button : buttons) {
            vbox1.getChildren().add(button);
        }
        anchorPane.getChildren().add(vbox1);


        ArrayList<Button> buttons2 = new ArrayList<>();
        for (int i = values.length / 2; i < values.length; i++) {
            String name = values[i].toString();
            buttons2.add(new Button(name));
        }

        for (int i = 0; i < values.length / 2; i++) {
            int finalI1 = i;
            buttons2.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (monsterFamilyEffectsInTrapEquip.contains(Integer.valueOf(finalI1 + 12))) {
                        currentPrice += 2500;
                    }
                    else {
                        currentPrice -= 2500;
                    }
                    changeAdditionOfThisEffectInTheGivenPlace2(finalI1 + 12, monsterFamilyEffectsInTrapEquip, buttons2);
                }
            });
        }

        VBox vBox2 = new VBox();
        vBox2.setLayoutY(60);
        vBox2.setLayoutX(230);
        vBox2.setMinHeight(600);
        vBox2.setMinWidth(200);
        vBox2.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0;" +
            " -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox2.setSpacing(13);

        for (Button button : buttons2) {
            vBox2.getChildren().add(button);
        }

        Button endButton = new Button("OK");
        endButton.setLayoutY(330);
        endButton.setLayoutX(465);
        endButton.setStyle("-fx-font-size: 25");
        endButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                equipSpell(vbox1, vBox2, endButton);
            }
        });

        anchorPane.getChildren().add(endButton);

        anchorPane.getChildren().add(vBox2);
    }


    private void continuousSpell(VBox vbox) {
        spellCardValue = SpellCardValue.CONTINUOUS.toString();
        anchorPane.getChildren().remove(vbox);


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

        vBox.setLayoutY(170);
        vBox.setLayoutX(220);
        vBox.setMinHeight(150);
        vBox.setMinWidth(150);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(460);
        buttonForFinish.setLayoutY(510);
        buttonForFinish.setStyle("-fx-font-size: 25");

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void quickPlaySpell(VBox vbox) {
        spellCardValue = SpellCardValue.QUICK_PLAY.toString();
        anchorPane.getChildren().remove(vbox);


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

        vBox.setLayoutY(230);
        vBox.setLayoutX(202);
        vBox.setMinHeight(150);
        vBox.setMinWidth(150);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(460);
        buttonForFinish.setLayoutY(420);
        buttonForFinish.setStyle("-fx-font-size: 25");


        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void ritualSpell(VBox vbox) {
        spellCardValue = SpellCardValue.RITUAL.toString();
        anchorPane.getChildren().remove(vbox);


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

        vBox.setLayoutY(230);
        vBox.setLayoutX(120);
        vBox.setMinHeight(150);
        vBox.setMinWidth(150);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(460);
        buttonForFinish.setLayoutY(420);
        buttonForFinish.setStyle("-fx-font-size: 25");

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void fieldSpell(VBox vbox111, VBox vbox1111, Button endButton) {
        spellCardValue = SpellCardValue.FIELD.toString();
        anchorPane.getChildren().remove(vbox1111);
        anchorPane.getChildren().remove(vbox111);
        anchorPane.getChildren().remove(endButton);

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

        vBox.setLayoutY(80);
        vBox.setLayoutX(220);
        vBox.setMinHeight(300);
        vBox.setMinWidth(200);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(465);
        buttonForFinish.setLayoutY(580);
        buttonForFinish.setStyle("-fx-font-size: 25");

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void equipSpell(VBox vbox1, VBox vBox2, Button button11) {
        spellCardValue = SpellCardValue.EQUIP.toString();
        anchorPane.getChildren().remove(vbox1);
        anchorPane.getChildren().remove(button11);
        anchorPane.getChildren().remove(vBox2);

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

        vBox.setLayoutY(150);
        vBox.setLayoutX(170);
        vBox.setMinHeight(300);
        vBox.setMinWidth(200);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(450);
        buttonForFinish.setLayoutY(470);
        buttonForFinish.setStyle("-fx-font-size: 25");

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void normalSpell(VBox vbox) {
        spellCardValue = SpellCardValue.NORMAL.toString();
        anchorPane.getChildren().remove(vbox);

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

        vBox.setLayoutY(100);
        vBox.setLayoutX(250);
        vBox.setMinHeight(300);
        vBox.setMinWidth(200);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(20);

        buttonForFinish.setLayoutX(430);
        buttonForFinish.setLayoutY(590);
        buttonForFinish.setStyle("-fx-font-size: 25");

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

        buttonForFinish.setOnAction(ActionEvent -> createSpellCard(vBox, buttonForFinish));
        for (Button button : buttons) {
            vBox.getChildren().add(button);
        }

        vBox.setLayoutY(50);
        vBox.setLayoutX(110);
        vBox.setMinHeight(300);
        vBox.setMinWidth(200);
        vBox.setStyle("-fx-padding:10; -fx-border-radius:8; -fx-border-color: #a7a0a0; -fx-font-size: 25; -fx-background-color: #003e79; -fx-stroke: black; -fx-alignment: CENTER");
        vBox.setSpacing(10);

        buttonForFinish.setLayoutX(460);
        buttonForFinish.setLayoutY(600);

        anchorPane.getChildren().add(vBox);
        anchorPane.getChildren().add(buttonForFinish);
    }


    private void createSpellCard(VBox vBox, Button buttonForFinish) {
        anchorPane.getChildren().remove(vBox);
        anchorPane.getChildren().remove(buttonForFinish);

        System.out.println(selectedUserReplySpell);

        HashMap<String, List<String>> enumValues = new HashMap<>();

        HashMap<String, List<String>> monsterFamilyTrap = new HashMap<>();
        EquipSpellEffect[] equipSpellEffects = EquipSpellEffect.values();
        FieldSpellEffect[] fieldSpellEffects = FieldSpellEffect.values();
        NormalSpellCardEffect[] normalSpellCardEffects = NormalSpellCardEffect.values();
        QuickSpellEffect[] quickSpellEffects = QuickSpellEffect.values();
        RitualSpellEffect[] ritualSpellEffects = RitualSpellEffect.values();
        ContinuousSpellCardEffect[] continuousSpellCardEffects = ContinuousSpellCardEffect.values();
        MonsterCardFamily[] monsterCardFamilies = MonsterCardFamily.values();


        ArrayList<String> stringsNormal = new ArrayList<>();
        ArrayList<String> stringsEquip = new ArrayList<>();
        ArrayList<String> stringsField = new ArrayList<>();
        ArrayList<String> stringsRitual = new ArrayList<>();
        ArrayList<String> stringsQuick = new ArrayList<>();
        ArrayList<String> stringsContinuous = new ArrayList<>();
        ArrayList<String> userReplyArrayList = new ArrayList<>();
        ArrayList<String> monsterFamilyTrapEquip = new ArrayList<>();
        ArrayList<String> monsterFamilyTrapField = new ArrayList<>();
        int counter = 0;
        switch (spellCardValue) {
            case "NORMAL":
                for (NormalSpellCardEffect normalSpellCardEffect : normalSpellCardEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsNormal.add(String.valueOf(normalSpellCardEffect));
                        allSelectedEffectsAsStrings.add(String.valueOf(normalSpellCardEffect));
                    }
                    counter++;
                }

                break;
            case "EQUIP":
                for (EquipSpellEffect effect : equipSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsEquip.add(String.valueOf(effect));
                        allSelectedEffectsAsStrings.add(String.valueOf(effect));
                    }
                    counter++;
                }
                counter = 0;
                for (MonsterCardFamily monsterCardFamily : monsterCardFamilies) {
                    if (monsterFamilyEffectsInTrapEquip.contains(Integer.valueOf(counter))) {
                        monsterFamilyTrapEquip.add(String.valueOf(monsterCardFamily));
                        allSelectedEffectsAsStrings.add(String.valueOf(monsterCardFamily));
                    }
                }

                break;
            case "FIELD":
                for (FieldSpellEffect effect : fieldSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsField.add(String.valueOf(effect));
                        allSelectedEffectsAsStrings.add(String.valueOf(effect));
                    }
                    counter++;
                }
                counter = 0;
                for (MonsterCardFamily monsterCardFamily : monsterCardFamilies) {
                    if (monsterFamilyEffectsInTrapField.contains(Integer.valueOf(counter))) {
                        monsterFamilyTrapField.add(String.valueOf(monsterCardFamily));
                        allSelectedEffectsAsStrings.add(String.valueOf(monsterCardFamily));
                    }
                }
                break;
            case "RITUAL":
                for (RitualSpellEffect effect : ritualSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsRitual.add(String.valueOf(effect));
                        allSelectedEffectsAsStrings.add(String.valueOf(effect));
                    }
                    counter++;
                }

                break;
            case "QUICK_PLAY":
                for (QuickSpellEffect effect : quickSpellEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsQuick.add(String.valueOf(effect));
                        allSelectedEffectsAsStrings.add(String.valueOf(effect));
                    }
                    counter++;
                }

                break;
            case "CONTINUOUS":
                for (ContinuousSpellCardEffect effect : continuousSpellCardEffects) {
                    if (numberOfSelectedEnumSpell.contains(Integer.valueOf(counter))) {
                        stringsContinuous.add(String.valueOf(effect));
                        allSelectedEffectsAsStrings.add(String.valueOf(effect));
                    }

                    counter++;
                }

                break;
        }
        enumValues.put("NormalSpellCardEffect", stringsNormal);
        enumValues.put("EquipSpellEffect", stringsEquip);
        enumValues.put("FieldSpellEffect", stringsField);
        enumValues.put("RitualSpellEffect", stringsRitual);
        enumValues.put("QuickSpellEffect", stringsQuick);
        enumValues.put("ContinuousSpellCardEffect", stringsContinuous);
        enumValues.put("LogicalActivationRequirement", new ArrayList<>());
        enumValues.put("SentToGraveyardEffect", new ArrayList<>());

        monsterFamilyTrap.put("field", monsterFamilyTrapField);
        monsterFamilyTrap.put("equip", monsterFamilyTrapEquip);

        UserReplyForActivation[] userReplyForActivations = UserReplyForActivation.values();
        counter = 0;
        for (UserReplyForActivation userReplyForActivation : userReplyForActivations) {
            if (selectedUserReplySpell.contains(Integer.valueOf(counter))) {
                userReplyArrayList.add(String.valueOf(userReplyForActivation));
                allSelectedEffectsAsStrings.add(String.valueOf(userReplyForActivation));
            }
            counter++;
        }
        enumValues.put("UserReplyForActivation", userReplyArrayList);

        allSelectedEffectsThatHaveNumbers = getEnumsWithNumbers();
        monsterFamilySelectedInSpell = monsterFamilyTrap;
        getNumberOfUserSpellCard();
//        SpellCard1 spellCard = new SpellCard1(cardName, cardDescription, SpellCardValue.valueOf(spellCardValue),
//            CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, numberOfTurnsForActivationSpell, 0, enumValues, cardImage, monsterFamilyTrap);

//        Storage.addCardToNewCardsCrated(spellCard);
//        Storage.saveNewImagesOfCardsInFile(spellCard, imagePath);
        //TODO : calculate card price
        //Should I add all of them even if they are empty?

    }

    private void getNumberOfUserSpellCard() {
        System.out.println("start");
        if (allSelectedEffectsThatHaveNumbers.size() != 0) {
            System.out.println("hi");
            CustomDialog customDialog = new CustomDialog("ERROR", "Enter a number for:\n" + allSelectedEffectsThatHaveNumbers.get(0));
            customDialog.openDialog();
            TextField textField = new TextField();
            textField.setLayoutX(340);
            textField.setLayoutY(200);
            textField.setStyle("-fx-alignment: CENTER; -fx-font-size: 25; -fx-background-color: #f5eeee");
            anchorPane.getChildren().add(textField);

            Button endButton = new Button("OK");
            endButton.setLayoutX(450);
            endButton.setLayoutY(270);
            endButton.setStyle("-fx-font-size: 25;");
            anchorPane.getChildren().add(endButton);
            endButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Pattern pattern = Pattern.compile("^\\d+$");
                    if (!textField.getText().isEmpty() && pattern.matcher(textField.getText()).find()) {
                        numbersOfEffectsToSend.put(allSelectedEffectsThatHaveNumbers.get(0), Integer.valueOf(textField.getText()));
                        allSelectedEffectsThatHaveNumbers.remove(0);
                        anchorPane.getChildren().remove(endButton);
                        anchorPane.getChildren().remove(textField);
                        if (allSelectedEffectsThatHaveNumbers.size() != 0) {
                            getNumberOfUserSpellCard();
                        } else {
                            lastStepOfCreatingSpellCard();
                        }
                    }

                }
            });
        } else {
            lastStepOfCreatingSpellCard();
        }
    }

    private void lastStepOfCreatingSpellCard() {
        if (currentMoneyOfUser < currentPrice * 0.1) {
            CustomDialog customDialog = new CustomDialog("ERROR", "NOT ENOUGH MONEY");
            customDialog.openDialog();
        } else {
            LoginController.getOnlineUser().setMoney((int) (currentMoneyOfUser - 0.1 * currentPrice));
            SpellCard1 spellCard = new SpellCard1(cardName, cardDescription, SpellCardValue.valueOf(spellCardValue),
                CardPosition.NOT_APPLICABLE, numberOfAllowedUsages, numberOfTurnsForActivationSpell,
                currentPrice, hashMapEffects, cardImage, monsterFamilySelectedInSpell, numbersOfEffectsToSend);

            System.out.println("finished");
            CustomDialog customDialog = new CustomDialog("MESSAGE", "Card Created Successfully");
            customDialog.openDialog();
            UIStorage.addNodesForViewWhenNewCardCreated();
        }

        backToMainMenu();

    }


    private void removeThingsInTheGetNumberOfAllowedUsages() {
        anchorPane.getChildren().remove(labelForGettingNumberOfAllowedUsagesFromUser);
        anchorPane.getChildren().remove(buttonOneForNumberOfAllowedUsages);
        anchorPane.getChildren().remove(buttonThreeForNumberOfAllowedUsages);
    }


    public void back(ActionEvent actionEvent) {
        backToMainMenu();
    }
}
