package project.client.view;

import java.net.URL;
import java.util.*;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import project.client.CardsStorage;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.model.Deck;
import project.model.cardData.General.Card;
import project.client.view.pooyaviewpackage.DuelView;

public class ChangeCardsBetweenTwoRoundsController implements Initializable {

    @FXML
    private Button confirmbtn;
    @FXML
    private Rectangle shownCardRectangle;
    @FXML
    private Label deckNameLabel;
    private static List<Rectangle> allMainDeckRectangle;
    private static List<Rectangle> allSideDeckRectangle;
    private static AnchorPane anchorPane;

    private static List<Label> allCardDiscriptionLabels;
    private static Label sizeOfMainDeckLabel;
    private static Label sizeOfAllMonsterCardsLabel;
    private static Label sizeOfAllSpellCardsLabel;
    private static Label sizeOfAllTrapCardsLabel;
    private static boolean isAddedNecessaryThingsForTheFirstTime = false;
    private static Rectangle equalShownRectangle;
    private static Button equalConfirmbtn;
    private static Label equalDeckNameLabel;
    private int cardsAddedToMainDeck;
    private static List<String> mainDeckCards;
    private static List<String> sideDeckCards;
    private String deckName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/DeckMenu.mp3");
        equalShownRectangle = shownCardRectangle;
        equalConfirmbtn = confirmbtn;
        equalDeckNameLabel = deckNameLabel;
    }

    public void showPage(AnchorPane pane, String playerName, String deckName, String token) {
        setAnchorPane(pane);
        this.deckName = deckName;
        initializePlayersAndDecks(token);
        allCardDiscriptionLabels = UIStorage.getAllCardDiscriptionLabels1();
        if (!isAddedNecessaryThingsForTheFirstTime) {
            initializeLabelsForShowSizeOfDeck();
        }
        equalDeckNameLabel.setText(deckName);
        getRectanglesFromUIStorageForPanes();
        createMainDeck();
        createSideDeck();
        showNumberOfCardsInLabels();
        addDragAndDropEffect();
        equalShownRectangle.setFill(new ImagePattern(CardsStorage.getUnknownCard().getImage()));
        MainView.changeScene(pane);
    }

    private void initializePlayersAndDecks(String token) {
        mainDeckCards = LoginController.getOnlineUser().getDecks().get(deckName).getMainDeck();
        sideDeckCards = LoginController.getOnlineUser().getDecks().get(deckName).getSideDeck();
    }

    public void confirmChanges(String token) {
        String sendDataToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("changeCardsBetweenTwoRounds", "ConfirmChanges", "ConfirmChanges");
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(sendDataToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            CustomDialog customDialog = new CustomDialog("Error", "Game interrupted", "mainMenu");
            customDialog.openDialog();
        } else {
            SongPlayer.getInstance().pauseMusic();
            DuelView.callStage();
        }
    }

    private void setEffectsOfConfirmButton() {
        if (cardsAddedToMainDeck != 0) {
            equalConfirmbtn.setDisable(true);
        } else {
            equalConfirmbtn.setDisable(false);
        }
    }

    private void addDragAndDropEffect() {

        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(0);
        mainDeckPane.setOnDragOver(e -> {
            if (canTransferToMainDeck(e)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        mainDeckPane.setOnDragDropped(e -> {
            transferCardToMainOrSideDeck(e, mainDeckPane, true);
            SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/pullingCard.mp3");
            showNumberOfCardsInLabels();
            cardsAddedToMainDeck++;
            setEffectsOfConfirmButton();
        });

        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(1);
        sideDeckPane.setOnDragOver(e -> {
            if (canTransferToSideDeck(e)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        sideDeckPane.setOnDragDropped(e -> {
            transferCardToMainOrSideDeck(e, sideDeckPane, false);
            showNumberOfCardsInLabels();
            SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/pullingCard.mp3");
            cardsAddedToMainDeck--;
            setEffectsOfConfirmButton();
        });

    }

    private boolean canTransferToMainDeck(DragEvent e) {
        Pane pane = (Pane) anchorPane.getChildren().get(0);
        Rectangle rectangle = (Rectangle) e.getGestureSource();
        boolean isSizeDeckValid = mainDeckCards.size() <= 60;
        boolean isCardFromThisPart = pane.getChildren().contains(rectangle);
        return isSizeDeckValid && !isCardFromThisPart;
    }

    private boolean canTransferToSideDeck(DragEvent e) {
        Pane pane = (Pane) anchorPane.getChildren().get(1);
        Rectangle rectangle = (Rectangle) e.getGestureSource();
        boolean isSizeDeckValid = sideDeckCards.size() <= 15;
        boolean isCardFromThisPart = pane.getChildren().contains(rectangle);
        return isSizeDeckValid && !isCardFromThisPart;
    }

    private void showNumberOfCardsInLabels() {

        HashMap<String, Integer> sizeOfEachPart = WholeDeckPageMenuController.getNumberOfEachTypeOfCardsInDeck(LoginController.getOnlineUser().getDecks().get(deckName));
        if (mainDeckCards.size() < 40) {
            sizeOfMainDeckLabel.setTextFill(Color.RED);
        } else {
            sizeOfMainDeckLabel.setTextFill(Color.BLACK);
        }
        sizeOfMainDeckLabel.setText(mainDeckCards.size() + "");
        sizeOfAllMonsterCardsLabel.setText("" + sizeOfEachPart.get("monstersSize"));
        sizeOfAllSpellCardsLabel.setText("" + sizeOfEachPart.get("spellsSize"));
        sizeOfAllTrapCardsLabel.setText("" + sizeOfEachPart.get("trapsSize"));
    }

    private void createSideDeck() {
        List<String> allCardsInMainDeck = LoginController.getOnlineUser().getDecks().get(deckName)
            .getSideDeck();
        List<Card> sideDeckCards = getListOfCards(allCardsInMainDeck);
        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(1);
        outer:
        for (int i = 0; i < 2; i++) {
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

    private void createMainDeck() {
        List<String> allCardsInMainDeck = LoginController.getOnlineUser().getDecks().get(deckName)
            .getMainDeck();
        List<Card> mainDeckCards = getListOfCards(allCardsInMainDeck);
        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(0);
        outer:
        for (int i = 0; i < 6; i++) {
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
            cards.add(CardsStorage.getCardByName(stringFormatCardInputs.get(i)));
        }
        return cards;
    }

    private void initializeLabelsForShowSizeOfDeck() {
        sizeOfMainDeckLabel = UIStorage.getNumberOfCardslabels().get(0);
        sizeOfAllMonsterCardsLabel = UIStorage.getNumberOfCardslabels().get(1);
        sizeOfAllSpellCardsLabel = UIStorage.getNumberOfCardslabels().get(2);
        sizeOfAllTrapCardsLabel = UIStorage.getNumberOfCardslabels().get(3);
        anchorPane.getChildren().add(sizeOfMainDeckLabel);
        anchorPane.getChildren().add(sizeOfAllMonsterCardsLabel);
        anchorPane.getChildren().add(sizeOfAllSpellCardsLabel);
        anchorPane.getChildren().add(sizeOfAllTrapCardsLabel);
    }

    private void getRectanglesFromUIStorageForPanes() {
        allMainDeckRectangle = UIStorage.getAllMainDeckRectangle();
        allSideDeckRectangle = UIStorage.getAllSideDeckRectangle();
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
                equalShownRectangle.setFill(rectangle.getFill());
                addCardDescription(rectangle.getId());
                flipRectangle(equalShownRectangle);
            }
        });
    }

    private void addCardDescription(String cardName) {
        Card card = CardsStorage.getCardByName(cardName);
        String cardDescription = card.getCardDescription();
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(4);
        Pane pane = null;
        if (scrollPane.getContent() == null) {
            pane = new Pane();
        } else {
            pane = (Pane) scrollPane.getContent();
        }
        pane.getChildren().clear();
        Label label = allCardDiscriptionLabels.get(0);
        label.setText("  " + cardName);
        label.setTextFill(Color.BLUE);
        pane.getChildren().add(label);
        List<String> shortCardDescription = new ArrayList<>();
        shortCardDescription = Arrays.asList(cardDescription.split(" "));
        StringBuilder sentencesForEachLabel = new StringBuilder();
        int numberOfLabelUsed = 1;
        for (int i = 0; i < shortCardDescription.size(); i++) {
            label = allCardDiscriptionLabels.get(numberOfLabelUsed);
            sentencesForEachLabel.append(shortCardDescription.get(i) + " ");
            if (sentencesForEachLabel.length() >= 15) {
                addEffectToLabel(label, sentencesForEachLabel.toString());
                sentencesForEachLabel.setLength(0);
                label.setLayoutY(20 * (numberOfLabelUsed));
                pane.getChildren().add(label);
                numberOfLabelUsed++;
            } else if (i + 1 == shortCardDescription.size()) {
                addEffectToLabel(label, sentencesForEachLabel.toString());
                label.setLayoutY(20 * (numberOfLabelUsed));
                pane.getChildren().add(label);
            }
        }
        scrollPane.setContent(pane);
    }

    private void addEffectToLabel(Label label, String text) {
        label.setText("  " + text);
        label.setTextFill(Color.BLACK);
    }

    private void deleteCardFromMianOrSideDeck(Rectangle transfferdRectangle, Pane pane, boolean isDeleteFromMainDeck) {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatForChangeCardsBetweenTowRounds(transfferdRectangle.getId(), isDeleteFromMainDeck, false);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("type"), "Error");
            return;
        }

        Deck deck = LoginController.getOnlineUser().getDecks().get(deckName);
        List<String> mainOrSideDeck = isDeleteFromMainDeck ? deck.getMainDeck() : deck.getSideDeck();
        mainOrSideDeck.remove(transfferdRectangle.getId());

        int indexOfRemovedRectanlge = pane.getChildren().indexOf(transfferdRectangle);
        for (int i = indexOfRemovedRectanlge; i < pane.getChildren().size() - 1; i++) {
            Rectangle rectangle = (Rectangle) pane.getChildren().get(i);
            Rectangle rectangle2 = (Rectangle) pane.getChildren().get(i + 1);
            rectangle.setFill(rectangle2.getFill());
            rectangle.setId(rectangle2.getId());
        }
        pane.getChildren().remove(pane.getChildren().size() - 1);
    }

    private void flipRectangle(Rectangle rectangle) {
        RotateTransition rotatorRectangle = new RotateTransition(Duration.millis(999), rectangle);
        rotatorRectangle.setAxis(Rotate.Y_AXIS);
        rotatorRectangle.setFromAngle(180);
        rotatorRectangle.setToAngle(0);
        rotatorRectangle.setInterpolator(Interpolator.LINEAR);
        rotatorRectangle.setCycleCount(1);
        rotatorRectangle.play();
    }

    private void transferCardToMainOrSideDeck(DragEvent e, Pane pane, boolean isTransferToMainDeck) {
        Rectangle transfferdRectangle = (Rectangle) e.getGestureSource();
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatForChangeCardsBetweenTowRounds(transfferdRectangle.getId(), isTransferToMainDeck, true);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        Deck deck = LoginController.getOnlineUser().getDecks().get(deckName);
        List<String> mainOrSideDeck = isTransferToMainDeck ? deck.getMainDeck() : deck.getSideDeck();
        mainOrSideDeck.add(transfferdRectangle.getId());

        int numberOfCardsInMainOrSideDeck = pane.getChildren().size();
        Rectangle addedRectangle = null;
        if (isTransferToMainDeck) {
            addedRectangle = allMainDeckRectangle.get(numberOfCardsInMainOrSideDeck);
        } else {
            addedRectangle = allSideDeckRectangle.get(numberOfCardsInMainOrSideDeck);
        }

        copyPropertyToTransferredCard(transfferdRectangle, addedRectangle);
        pane.getChildren().add(addedRectangle);

        deleteDraggedCard(transfferdRectangle);
    }

    private void deleteDraggedCard(Rectangle transfferdRectangle) {

        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(0);
        if (mainDeckPane.getChildren().contains(transfferdRectangle)) {
            deleteCardFromMianOrSideDeck(transfferdRectangle, mainDeckPane, true);
            return;
        }

        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(1);
        if (sideDeckPane.getChildren().contains(transfferdRectangle)) {
            deleteCardFromMianOrSideDeck(transfferdRectangle, sideDeckPane, false);
        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    private void copyPropertyToTransferredCard(Rectangle teransfferdRectangle, Rectangle addedRectangle) {
        addedRectangle.setFill(teransfferdRectangle.getFill());
        addedRectangle.setId(teransfferdRectangle.getId());
        addOnDragDetectedEffectForCard(addedRectangle);
        addOnMouseEnteredEffectForCard(addedRectangle);
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        ChangeCardsBetweenTwoRoundsController.anchorPane = anchorPane;
    }

    public static void setMainDeckCards(List<String> mainDeckCards) {
        ChangeCardsBetweenTwoRoundsController.mainDeckCards = mainDeckCards;
    }

    public static void setSideDeckCards(List<String> sideDeckCards) {
        ChangeCardsBetweenTwoRoundsController.sideDeckCards = sideDeckCards;
    }
}
