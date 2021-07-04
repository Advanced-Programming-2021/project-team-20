package project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import project.controller.non_duel.deckCommands.DeckCommands;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;

public class DeckMenuController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button activateDeckbtn;
    @FXML
    private Label activatedStatusLabel;
    @FXML
    private Label deckNameLabel;
    private static List<Rectangle> allMainDeckRectangle;
    private static List<Rectangle> allSideDeckRectangle;
    private static List<Rectangle> allScrollBarRectangle;
    private static List<Rectangle> allScrollBarBackGroundRectangles;
    private static AnchorPane anchorPane;
    private static String deckname ;
    private Rectangle shownCardRectangle;
    private static List<Label> allCardDiscriptionLabels;
    private DeckCommands deckCommands = new DeckCommands();
    private static List<Label> allScrollBarLabels;
    private static Label sizeOfMainDeckLabel;
    private static Label sizeOfAllMonsterCardsLabel;
    private static Label sizeOfAllSpellCardsLabel;
    private static Label sizeOfAllTrapCardsLabel;
    private static boolean isAddedNecessaryThingsForTheFirstTime = false;
    private static Label equalActivatedStatusLabel;
    private static Button equalActivateDeckbtn;
    private static Label equalDeckNameLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        equalActivatedStatusLabel = activatedStatusLabel;
        equalActivateDeckbtn = activateDeckbtn;
        equalDeckNameLabel = deckNameLabel;
    }

    public void showPage(AnchorPane pane, String chosenDeck) {
        setAnchorPane(pane);
        deckname = chosenDeck;
        allScrollBarLabels = UIUtility.getAllScrollBarLabels();
        allCardDiscriptionLabels = UIUtility.getAllCardDiscriptionLabels1();
        if (!isAddedNecessaryThingsForTheFirstTime) {
            initializeLabesForShowSizeOfDeck();
        }
        // check createAllCardToRectangle
        getRectanglesFromUIUtilityForPanes();
        createScrollPaneWithAllUselessCards();
        createMainDeck();
        createSideDeck();
        showNmberOfCardsInLabels();
        addDragAndDropEffect();

        if (LoginController.getOnlineUser().getDecks().get(deckname).getIsDeckActive()) {
            equalActivatedStatusLabel.setTextFill(Color.GREEN);
            equalActivatedStatusLabel.setText("activated");
            equalActivateDeckbtn.setDisable(true);
        } else {
            equalActivatedStatusLabel.setTextFill(Color.RED);
            equalActivatedStatusLabel.setText("inActivated");
        }
        shownCardRectangle = (Rectangle) pane.getChildren().get(0);
        shownCardRectangle.setFill(new ImagePattern(Storage.getUnknownCard().getImage()));
        equalDeckNameLabel.setText(chosenDeck);
        MainView.changeScene(pane);
    }

    private void initializeLabesForShowSizeOfDeck() {
        sizeOfMainDeckLabel = UIUtility.getNumberOfCardslabels().get(0);
        sizeOfAllMonsterCardsLabel = UIUtility.getNumberOfCardslabels().get(1);
        sizeOfAllSpellCardsLabel = UIUtility.getNumberOfCardslabels().get(2);
        sizeOfAllTrapCardsLabel = UIUtility.getNumberOfCardslabels().get(3);
        anchorPane.getChildren().add(sizeOfMainDeckLabel);
        anchorPane.getChildren().add(sizeOfAllMonsterCardsLabel);
        anchorPane.getChildren().add(sizeOfAllSpellCardsLabel);
        anchorPane.getChildren().add(sizeOfAllTrapCardsLabel);
    }

    private void showNmberOfCardsInLabels() {
        HashMap<String, Integer> sizeOfEachPart = new HashMap<>();
        sizeOfEachPart = deckCommands.getNumberOfEachTypeOfCardsInDeck(deckname,
                LoginController.getOnlineUser().getName());
        if (sizeOfEachPart.get("mainDeckSize") < 40) {
            sizeOfMainDeckLabel.setTextFill(Color.RED);
        } else {
            sizeOfMainDeckLabel.setTextFill(Color.BLACK);
        }
        sizeOfMainDeckLabel.setText(sizeOfEachPart.get("mainDeckSize") + "");
        sizeOfAllMonsterCardsLabel.setText("" + sizeOfEachPart.get("monstersSize"));
        sizeOfAllSpellCardsLabel.setText("" + sizeOfEachPart.get("spellsSize"));
        sizeOfAllTrapCardsLabel.setText("" + sizeOfEachPart.get("trapsSize"));
    }

    private void addDragAndDropEffect() {
        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(3);
        mainDeckPane.setOnDragOver(e -> {
            if (canTransfateToMainDeck(e)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        mainDeckPane.setOnDragDropped(e -> {
            transferCardToMainOrSideDeck(e, mainDeckPane, true);
            showNmberOfCardsInLabels();
        });

        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(4);
        sideDeckPane.setOnDragOver(e -> {
            if (canTransfateToSideDeck(e)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        sideDeckPane.setOnDragDropped(e -> {
            transferCardToMainOrSideDeck(e, sideDeckPane, false);
            showNmberOfCardsInLabels();
        });

        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(2);
        scrollPane.setOnDragOver(e -> {
            Pane pane = (Pane) scrollPane.getContent();
            if (!pane.getChildren().contains(e.getGestureSource())) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        scrollPane.setOnDragDropped(e -> {
            transferCardToScrollBar(e);
            showNmberOfCardsInLabels();
        });
    }

    private void transferCardToScrollBar(DragEvent e) {
        Rectangle transfferdRectangle = (Rectangle) e.getGestureSource();
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(2);
        Pane pane = (Pane) scrollPane.getContent();
        Rectangle equalRectangleInScrollBar = null;
        for (int i = 0; i < pane.getChildren().size(); i++) {
            String IdOfchildOfPane = pane.getChildren().get(i).getId();
            if (IdOfchildOfPane == null) {
                continue;
            }
            if (IdOfchildOfPane.equals(transfferdRectangle.getId() + "scrollBar")) {
                equalRectangleInScrollBar = (Rectangle) pane.getChildren().get(i);
                equalRectangleInScrollBar.setOpacity(1);
                addOnDragDetectedEffectForCard(equalRectangleInScrollBar);
            }
            if (IdOfchildOfPane.replace("scrollBar", "").equals(transfferdRectangle.getId() + "label")) {
                Label label = (Label) pane.getChildren().get(i);
                label.setText((Integer.parseInt(label.getText()) + 1) + "");
            }
        }

        deleteDraggedCard(transfferdRectangle);
        deckCommands.addCardToAllUselessCards(transfferdRectangle.getId(), LoginController.getOnlineUser().getName());
    }

    private void transferCardToMainOrSideDeck(DragEvent e, Pane pane, boolean isTransferToMainDeck) {
        int numberOfCardsInMainoRSideDeck = pane.getChildren().size();
        Rectangle addedRectangle = null;
        if (isTransferToMainDeck) {
            addedRectangle = allMainDeckRectangle.get(numberOfCardsInMainoRSideDeck);
        } else {
            addedRectangle = allSideDeckRectangle.get(numberOfCardsInMainoRSideDeck);
        }

        Rectangle transfferdRectangle = (Rectangle) e.getGestureSource();
        deleteDraggedCard(transfferdRectangle);

        String nameOfAddedCard = transfferdRectangle.getId();
        if (nameOfAddedCard.contains("scrollBar")) {
            nameOfAddedCard = nameOfAddedCard.replace("scrollBar", "");
        }
        copyPropertyToTransferredCard(transfferdRectangle, addedRectangle, nameOfAddedCard);
        pane.getChildren().add(addedRectangle);
        deckCommands.addCardToMainOrSideDeck(deckname, nameOfAddedCard, isTransferToMainDeck,
                LoginController.getOnlineUser().getName());
    }

    private void deleteDraggedCard(Rectangle transfferdRectangle) {

        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(3);
        if (mainDeckPane.getChildren().contains(transfferdRectangle)) {
            deleteCardFromMianOrSideDeck(transfferdRectangle, mainDeckPane, true);
            return;
        }

        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(4);
        if (sideDeckPane.getChildren().contains(transfferdRectangle)) {
            deleteCardFromMianOrSideDeck(transfferdRectangle, sideDeckPane, false);
            return;
        }
        deleteCardFromScrollBar(transfferdRectangle);
    }

    private void copyPropertyToTransferredCard(Rectangle teransfferdRectangle, Rectangle addedRectangle,
            String cardName) {
        addedRectangle.setFill(teransfferdRectangle.getFill());
        addedRectangle.setId(cardName);
        addOnDragDetectedEffectForCard(addedRectangle);
        addOnMouseEnteredEffectForCard(addedRectangle);
    }

    private boolean canTransfateToMainDeck(DragEvent e) {
        Pane pane = (Pane) anchorPane.getChildren().get(3);
        Rectangle rectangle = (Rectangle) e.getGestureSource();
        boolean isSizeDeckValid = LoginController.getOnlineUser().getDecks().get(deckname).getSizeOfMainDeck() < 60;
        boolean isCardFromThisPart = pane.getChildren().contains(rectangle);
        boolean canAddAnotherCardToDeck = true;
        if (rectangle.getId().contains("scrollBar")) {
            canAddAnotherCardToDeck = deckCommands.canAddCardToDeck(deckname,
                    rectangle.getId().replace("scrollBar", ""), LoginController.getOnlineUser().getName());
        }
        return isSizeDeckValid && !isCardFromThisPart && canAddAnotherCardToDeck;
    }

    private boolean canTransfateToSideDeck(DragEvent e) {
        Pane pane = (Pane) anchorPane.getChildren().get(4);
        Rectangle rectangle = (Rectangle) e.getGestureSource();
        boolean isSizeDeckValid = LoginController.getOnlineUser().getDecks().get(deckname).getSizeOfSideDeck() < 15;
        boolean isCardFromThisPart = pane.getChildren().contains(rectangle);
        boolean canAddAnotherCardToDeck = true;
        if (rectangle.getId().contains("scrollBar")) {
            canAddAnotherCardToDeck = deckCommands.canAddCardToDeck(deckname,
                    rectangle.getId().replace("scrollBar", ""), LoginController.getOnlineUser().getName());
        }
        return isSizeDeckValid && !isCardFromThisPart && canAddAnotherCardToDeck;
    }

    private void getRectanglesFromUIUtilityForPanes() {
        allMainDeckRectangle = UIUtility.getAllMainDeckRectangle();
        allScrollBarRectangle = UIUtility.getAllScrollBarRectangle();
        allSideDeckRectangle = UIUtility.getAllSideDeckRectangle();
        allScrollBarBackGroundRectangles = UIUtility.getAllScrollBarBackGroundRectangles();
    }

    private void createMainDeck() {

        List<String> allCardsInMainDeck = LoginController.getOnlineUser().getDecks().get(deckname).getMainDeck();
        List<Card> mainDeckCards = getListOfCards(allCardsInMainDeck);
        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(3);
        outer: for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                if (i * 10 + j == mainDeckCards.size()) {
                    break outer;
                }
                Rectangle rectangle = allMainDeckRectangle.get(i * 10 + j);
                rectangle.setFill(new ImagePattern(mainDeckCards.get(i * 10 + j).getImage()));
                rectangle.setId(mainDeckCards.get(i * 10 + j).getCardName());
                addOnDragDetectedEffectForCard(rectangle);
                addOnMouseEnteredEffectForCard(rectangle);
                mainDeckPane.getChildren().add(rectangle);
            }
        }
    }

    private List<Card> getListOfCards(List<String> stringFormatCardInputs) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < stringFormatCardInputs.size(); i++) {
            cards.add(Storage.getCardByName(stringFormatCardInputs.get(i)));
        }
        return cards;
    }

    private void createSideDeck() {
        List<String> allCardsInSideDeck = LoginController.getOnlineUser().getDecks().get(deckname).getSideDeck();
        List<Card> sideDeckCards = getListOfCards(allCardsInSideDeck);
        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(4);

        outer: for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (i * 10 + j == sideDeckCards.size()) {
                    break outer;
                }
                Rectangle rectangle = allSideDeckRectangle.get(i * 10 + j);
                rectangle.setFill(new ImagePattern(sideDeckCards.get(i * 10 + j).getImage()));
                rectangle.setId(sideDeckCards.get(i * 10 + j).getCardName());
                addOnDragDetectedEffectForCard(rectangle);
                addOnMouseEnteredEffectForCard(rectangle);
                sideDeckPane.getChildren().add(rectangle);
            }
        }
    }

    private void createScrollPaneWithAllUselessCards() {
        List<Card> scrollCards = UIUtility.getAllTypeOfCards().get("allCards");
        Pane pane = new Pane();
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(2);
        for (int i = 0; i < scrollCards.size(); i++) {
            Rectangle rectangle = allScrollBarRectangle.get(i);
            Rectangle backGrouRectangle = allScrollBarBackGroundRectangles.get(i);
            rectangle.setFill(new ImagePattern(scrollCards.get(i).getImage()));
            rectangle.setId(scrollCards.get(i).getCardName() + "scrollBar");
            addOnDragDetectedEffectForCard(rectangle);
            addOnMouseEnteredEffectForCard(rectangle);
            rectangle.setX(5);
            backGrouRectangle.setX(5);
            Label label = allScrollBarLabels.get(i);
            label.setLayoutX(30);
            label.setId(scrollCards.get(i).getCardName() + "label");
            if (i % 2 == 0) {
                backGrouRectangle.setY(5 + 30 * i);
                rectangle.setY(5+30 * i);
                label.setLayoutY(42 + 30 * i);
            }
            if (i % 2 == 1) {
                rectangle.setY(5+ 30 * (i - 1));
                rectangle.setX(rectangle.getX() + 50);
                backGrouRectangle.setY(5 + 30 * (i - 1));
                backGrouRectangle.setX(55);
                label.setLayoutX(label.getLayoutX() + 50);
                label.setLayoutY(42 + 30 * (i - 1));
            }
            if (!doesCardExistInUseLessCards(scrollCards.get(i).getCardName())) {
                rectangle.setOpacity(0.5);
                rectangle.setOnDragDetected(null);
                label.setText(0 + "");
            } else {
                int numberOfCardsInUselessCards = countNumberOfCardsInUselessCards(rectangle.getId());
                label.setText("" + numberOfCardsInUselessCards);
            }
            pane.getChildren().add(backGrouRectangle);
            pane.getChildren().add(rectangle);
            pane.getChildren().add(label);
        }
        scrollPane.setContent(pane);
    }

    private void deleteCardFromScrollBar(Rectangle transfferdRectangle) {
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(2);
        Pane pane = (Pane) scrollPane.getContent();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            String childOfPaneId = pane.getChildren().get(i).getId();
            if (childOfPaneId == null) {
                continue;
            }
            if (childOfPaneId.equals(transfferdRectangle.getId().replace("scrollBar", "") + "label")) {
                Label label = (Label) pane.getChildren().get(i);
                label.setText((Integer.parseInt(label.getText()) - 1) + "");
                if (label.getText().equals("0")) {
                    transfferdRectangle.setOpacity(0.5);
                    transfferdRectangle.setOnDragDetected(null);
                }
            }
        }
        deckCommands.deleteCardFromAllUselessCards(transfferdRectangle.getId().replace("scrollBar", ""),
                LoginController.getOnlineUser().getName());
    }

    private int countNumberOfCardsInUselessCards(String cardname) {
        List<String> allUseLessCards = LoginController.getOnlineUser().getAllUselessCards();
        return Collections.frequency(allUseLessCards, cardname.replace("scrollBar", ""));
    }

    private boolean doesCardExistInUseLessCards(String cardname) {
        return LoginController.getOnlineUser().getAllUselessCards().contains(cardname);
    }

    private void addOnDragDetectedEffectForCard(Rectangle rectangle) {
        rectangle.setOnDragDetected(e -> {
            Dragboard db = rectangle.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(rectangle.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.putString("rectangle");
            db.setContent(cc);
        });
    }

    private void addOnMouseEnteredEffectForCard(Rectangle rectangle) {
        rectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                shownCardRectangle.setFill(rectangle.getFill());
                String cardName = "";
                if (rectangle.getId().contains("scrollBar")) {
                    cardName = rectangle.getId().replace("scrollBar", "");
                    addCardDescription(cardName);
                } else {
                    addCardDescription(rectangle.getId());
                }
                flipRectangle(shownCardRectangle);
            }
        });
    }

    private void flipRectangle(Rectangle rectangle) {
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), rectangle);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(180);
        rotator.setToAngle(0);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        rotator.play();
    }

    private void deleteCardFromMianOrSideDeck(Rectangle transfferdRectangle, Pane pane, boolean isDeleteFromMainDeck) {
        deckCommands.deleteCardFromMainOrSideDeck(deckname, transfferdRectangle.getId(), isDeleteFromMainDeck,
                LoginController.getOnlineUser().getName());
        int indexOfRemovedRectanlge = pane.getChildren().indexOf(transfferdRectangle);
        for (int i = indexOfRemovedRectanlge; i < pane.getChildren().size() - 1; i++) {
            Rectangle rectangle = (Rectangle) pane.getChildren().get(i);
            Rectangle rectangle2 = (Rectangle) pane.getChildren().get(i + 1);
            rectangle.setFill(rectangle2.getFill());
            rectangle.setId(rectangle2.getId());
        }
        pane.getChildren().remove(pane.getChildren().size() - 1);
    }

    private void addCardDescription(String cardName) {
        Card card = Storage.getCardByName(cardName);
        String cardDiscription = card.getCardDescription();
        Pane pane = new Pane();
        Label label = allCardDiscriptionLabels.get(0);
        label.setText("  " + cardName);
        label.setTextFill(Color.YELLOW);
        pane.getChildren().add(label);
        List<String> shortCardDescription = new ArrayList<>();
        shortCardDescription = Arrays.asList(cardDiscription.split(" "));
        StringBuilder sentencesForEachLabel = new StringBuilder();
        int numberOfLabelUsed = 0;
        for (int i = 0; i < shortCardDescription.size(); i++) {
            label = allCardDiscriptionLabels.get(i + 1);
            if (sentencesForEachLabel.length() >= 15) {
                addEffectToLabel(label, sentencesForEachLabel.toString());
                sentencesForEachLabel.setLength(0);
                label.setLayoutY(20 * (numberOfLabelUsed + 1));
                pane.getChildren().add(label);
                numberOfLabelUsed++;
            }
            if (i == shortCardDescription.size()) {
                addEffectToLabel(label, sentencesForEachLabel.toString());
                label.setLayoutY(20 * (numberOfLabelUsed + 1));
                pane.getChildren().add(label);
            }
            sentencesForEachLabel.append(shortCardDescription.get(i) + " ");
        }

        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(5);
        scrollPane.setContent(pane);
    }

    private void addEffectToLabel(Label label, String text) {
        label.setText("  " + text);
        label.setTextFill(Color.BLACK);
    }

    public void activeDeck() {
        equalActivatedStatusLabel.setTextFill(Color.GREEN);
        equalActivatedStatusLabel.setText("activated");
        deckCommands.activateDeck(deckname, LoginController.getOnlineUser().getName());
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        DeckMenuController.anchorPane = anchorPane;
    }

    public static AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static void setAllMainDeckRectangle(List<Rectangle> allMainDeckRectangle) {
        DeckMenuController.allMainDeckRectangle = allMainDeckRectangle;
    }

    public Button getBackbtn() {
        return this.backbtn;
    }

    public void setBackbtn(Button backbtn) {
        this.backbtn = backbtn;
    }

    public Button getActivateDeckbtn() {
        return this.activateDeckbtn;
    }

    public void setActivateDeckbtn(Button activateDeckbtn) {
        this.activateDeckbtn = activateDeckbtn;
    }

    public Label getActivatedStatusLabel() {
        return this.activatedStatusLabel;
    }

    public void setActivatedStatusLabel(Label activatedStatusLabel) {
        this.activatedStatusLabel = activatedStatusLabel;
    }

    public Rectangle getShownCardRectangle() {
        return this.shownCardRectangle;
    }

    public void setShownCardRectangle(Rectangle shownCardRectangle) {
        this.shownCardRectangle = shownCardRectangle;
    }

    public DeckCommands getDeckCommands() {
        return this.deckCommands;
    }

    public void setDeckCommands(DeckCommands deckCommands) {
        this.deckCommands = deckCommands;
    }

    public DeckMenuController backbtn(Button backbtn) {
        setBackbtn(backbtn);
        return this;
    }

    public DeckMenuController activateDeckbtn(Button activateDeckbtn) {
        setActivateDeckbtn(activateDeckbtn);
        return this;
    }

    public DeckMenuController activatedStatusLabel(Label activatedStatusLabel) {
        setActivatedStatusLabel(activatedStatusLabel);
        return this;
    }

    public DeckMenuController shownCardRectangle(Rectangle shownCardRectangle) {
        setShownCardRectangle(shownCardRectangle);
        return this;
    }

    public DeckMenuController deckCommands(DeckCommands deckCommands) {
        setDeckCommands(deckCommands);
        return this;
    }

    public static void setAllSideDeckRectangle(List<Rectangle> allSideDeckRectangle) {
        DeckMenuController.allSideDeckRectangle = allSideDeckRectangle;
    }

    public static void setAllScrollBarLabels(List<Label> allScrollBarLabels) {
        DeckMenuController.allScrollBarLabels = allScrollBarLabels;
    }

    public static void setAllCardDiscriptionLabels(List<Label> allCardDiscriptionLabels) {
        DeckMenuController.allCardDiscriptionLabels = allCardDiscriptionLabels;
    }

    public void backToMainMenu() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/wholeDecksPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new WholeDeckPageMenuController().showPage(pane);
    }
}