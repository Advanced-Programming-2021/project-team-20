package project.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import project.client.CardsStorage;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.model.Deck;
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
    private static String deckname;
    private Rectangle shownCardRectangle;
    private static List<Label> allCardDiscriptionLabels;
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
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/DeckMenu.mp3");
        equalActivatedStatusLabel = activatedStatusLabel;
        equalActivateDeckbtn = activateDeckbtn;
        equalDeckNameLabel = deckNameLabel;
    }

    public void showPage(AnchorPane pane, String chosenDeck) {
        setAnchorPane(pane);
        deckname = chosenDeck;
        allScrollBarLabels = UIStorage.getAllScrollBarLabels();
        allCardDiscriptionLabels = UIStorage.getAllCardDiscriptionLabels1();
        if (!isAddedNecessaryThingsForTheFirstTime) {
            initializeLabesForShowSizeOfDeck();
        }
        // check createAllCardToRectangle
        getRectanglesFromUIStorageForPanes();
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
        shownCardRectangle.setFill(new ImagePattern(CardsStorage.getUnknownCard().getImage()));
        equalDeckNameLabel.setText(chosenDeck);

        MainView.changeScene(pane);
    }

    private void initializeLabesForShowSizeOfDeck() {
        sizeOfMainDeckLabel = UIStorage.getNumberOfCardslabels().get(0);
        sizeOfAllMonsterCardsLabel = UIStorage.getNumberOfCardslabels().get(1);
        sizeOfAllSpellCardsLabel = UIStorage.getNumberOfCardslabels().get(2);
        sizeOfAllTrapCardsLabel = UIStorage.getNumberOfCardslabels().get(3);
        anchorPane.getChildren().add(sizeOfMainDeckLabel);
        anchorPane.getChildren().add(sizeOfAllMonsterCardsLabel);
        anchorPane.getChildren().add(sizeOfAllSpellCardsLabel);
        anchorPane.getChildren().add(sizeOfAllTrapCardsLabel);
    }

    private void showNmberOfCardsInLabels() {
        HashMap<String, Integer> sizeOfEachPart = new HashMap<>();
        sizeOfEachPart = WholeDeckPageMenuController
            .getNumberOfEachTypeOfCardsInDeck(LoginController.getOnlineUser().getDecks().get(deckname));
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
            SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/pullingCard.mp3");
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
            SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/pullingCard.mp3");
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
            SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/pullingCard.mp3");
            showNmberOfCardsInLabels();
        });
    }

    private void transferCardToScrollBar(DragEvent e) {
        Rectangle transfferdRectangle = (Rectangle) e.getGestureSource();

        String dataSentToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("addCardToUselessCards",
            "cardName", transfferdRectangle.getId());
        String resultOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataSentToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(resultOfServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        LoginController.getOnlineUser().getAllUselessCards().add(transfferdRectangle.getId());

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
    }

    private void transferCardToMainOrSideDeck(DragEvent e, Pane pane, boolean isTransferToMainDeck) {
        Rectangle transfferdRectangle = (Rectangle) e.getGestureSource();
        String nameOfAddedCard = transfferdRectangle.getId();
        if (nameOfAddedCard.contains("scrollBar")) {
            nameOfAddedCard = nameOfAddedCard.replace("scrollBar", "");
        }

        String dataSentToServer = ToGsonFormatToSendDataToServer.toGsonFormatAddOrRemoveCardFromMainOrSideDeck(
            "addCardToMainOrSideDeck", nameOfAddedCard, deckname, isTransferToMainDeck);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSentToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }

        Deck deck = LoginController.getOnlineUser().getDecks().get(deckname);
        List<String> mainOrSideDeck = isTransferToMainDeck ? deck.getMainDeck() : deck.getSideDeck();
        mainOrSideDeck.add(nameOfAddedCard);

        int numberOfCardsInMainoRSideDeck = pane.getChildren().size();
        Rectangle addedRectangle = null;
        if (isTransferToMainDeck) {
            addedRectangle = allMainDeckRectangle.get(numberOfCardsInMainoRSideDeck);
        } else {
            addedRectangle = allSideDeckRectangle.get(numberOfCardsInMainoRSideDeck);
        }

        copyPropertyToTransferredCard(transfferdRectangle, addedRectangle, nameOfAddedCard);
        pane.getChildren().add(addedRectangle);
        deleteDraggedCard(transfferdRectangle);
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
            canAddAnotherCardToDeck = canAddCardToDeck(rectangle.getId().replace("scrollBar", ""));
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
            canAddAnotherCardToDeck = canAddCardToDeck(rectangle.getId().replace("scrollBar", ""));
        }
        return isSizeDeckValid && !isCardFromThisPart && canAddAnotherCardToDeck;
    }

    private void getRectanglesFromUIStorageForPanes() {
        allMainDeckRectangle = UIStorage.getAllMainDeckRectangle();
        allScrollBarRectangle = UIStorage.getAllScrollBarRectangle();
        allSideDeckRectangle = UIStorage.getAllSideDeckRectangle();
        allScrollBarBackGroundRectangles = UIStorage.getAllScrollBarBackGroundRectangles();
    }

    private void createMainDeck() {

        List<String> allCardsInMainDeck = LoginController.getOnlineUser().getDecks().get(deckname).getMainDeck();
        List<Card> mainDeckCards = getListOfCards(allCardsInMainDeck);
        Pane mainDeckPane = (Pane) anchorPane.getChildren().get(3);
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
            cards.add(CardsStorage.getCardByName((stringFormatCardInputs.get(i))));
        }
        return cards;
    }

    private void createSideDeck() {
        List<String> allCardsInSideDeck = LoginController.getOnlineUser().getDecks().get(deckname).getSideDeck();
        List<Card> sideDeckCards = getListOfCards(allCardsInSideDeck);
        Pane sideDeckPane = (Pane) anchorPane.getChildren().get(4);

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

    private void createScrollPaneWithAllUselessCards() {
        List<Card> scrollCards = UIStorage.getAllTypeOfCards().get("allCards");
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
                rectangle.setY(5 + 30 * i);
                label.setLayoutY(42 + 30 * i);
            }
            if (i % 2 == 1) {
                rectangle.setY(5 + 30 * (i - 1));
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
                rectangle.setOpacity(1);
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
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest(
            "deleteCardFromUselessCards", "cardName", transfferdRectangle.getId().replace("scrollBar", ""));
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        LoginController.getOnlineUser().getAllUselessCards()
            .remove(transfferdRectangle.getId().replace("scrollBar", ""));

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
        String dataSentToServer = ToGsonFormatToSendDataToServer.toGsonFormatAddOrRemoveCardFromMainOrSideDeck(
            "deleteCardFromMainOrSideDeck", transfferdRectangle.getId(), deckname, isDeleteFromMainDeck);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSentToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        Deck deck = LoginController.getOnlineUser().getDecks().get(deckname);
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

    private void addCardDescription(String cardName) {
        Card card = CardsStorage.getCardByName(cardName);
        String cardDiscription = card.getCardDescription();
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(5);
        Pane pane;
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
        shortCardDescription = Arrays.asList(cardDiscription.split(" "));
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

    private boolean canAddCardToDeck(String cardname) {
        Deck deck = LoginController.getOnlineUser().getDecks().get(deckname);
        int numberOfCardsInDeck = deck.numberOfCardsInDeck(cardname);
        int numberOfAllowedUsages = 0;
        Card card = CardsStorage.getCardByName(cardname);
        numberOfAllowedUsages = card.getNumberOfAllowedUsages();
        if (numberOfCardsInDeck == numberOfAllowedUsages) {
            return false;
        }
        return true;
    }

    public void activeDeck() {
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("activeDeck", "deckName",
            deckname);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
            .deserializeForOnlyTypeAndMessage(messageFromServer);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }

        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            entry.getValue().setDeckActive(false);
        }
        allDecksOfUser.get(deckname).setDeckActive(true);

        equalActivatedStatusLabel.setTextFill(Color.GREEN);
        equalActivatedStatusLabel.setText("activated");

        activateDeckbtn.setDisable(true);
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        DeckMenuController.anchorPane = anchorPane;
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
