package project.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import project.model.cardData.General.Card;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import project.client.CardsStorage;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.model.Deck;
import project.model.cardData.General.CardType;

public class WholeDeckPageMenuController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button createNewDeckbtn;
    @FXML
    private Button deleteDeckbtn;
    @FXML
    private Button editDeckbtn;
    @FXML
    private Button nextPagebtn;
    @FXML
    private Button previousPagebtn;
    @FXML
    private TextField createdDeckNameField;
    @FXML
    private Label deckNameLabel;

    private static List<Rectangle> fourRectangleToShowDecks;
    private static HashMap<String, Label> labelsToShowInformationOfDeck;
    private static List<List<Deck>> decksInDifferentPages;
    private static AnchorPane anchorPane;
    private static String chosenDeck = new String();
    private int currentPageToShowDecks = 0;
    private static Button equalToNextPagebtn;
    private static Button equalToPreviousPagebtn;
    private static Button equalToDeleteDeckbtn;
    private static Button equalToEditDeckbtn;
    private static Label equalDeckNameLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/WholeDeck.mp3");
        equalToNextPagebtn = nextPagebtn;
        equalToPreviousPagebtn = previousPagebtn;
        equalToDeleteDeckbtn = deleteDeckbtn;
        equalToEditDeckbtn = editDeckbtn;
        equalDeckNameLabel = deckNameLabel;
    }

    public void showPage(AnchorPane pane) {
        setAnchorPane(pane);
        if (fourRectangleToShowDecks == null) {
            addEffectsToFourRectangleToShowDeck();
        }
        addRectangleOfDecksToPage();
        addInformationLabelOfDeckToPane();
        createNewPage();
        setEffectsOfEditAndDeleteButtons();
        setEffectOfpreviousAndnextDecksbtn();
        MainView.changeScene(pane);
    }

    private void addInformationLabelOfDeckToPane() {
        labelsToShowInformationOfDeck = UIStorage.getLabelsToShowInformationOfDeck();
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);

        for (Map.Entry<String, Label> entry : labelsToShowInformationOfDeck.entrySet()) {
            pane.getChildren().add(entry.getValue());
        }
    }

    private void addEffectsToFourRectangleToShowDeck() {
        fourRectangleToShowDecks = UIStorage.getFourRectangleToShowDecks();
        for (int i = 0; i < fourRectangleToShowDecks.size(); i++) {
            int index = i;
            fourRectangleToShowDecks.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    chosenDeck = fourRectangleToShowDecks.get(index).getId();
                    setEffectsOfEditAndDeleteButtons();
                    equalDeckNameLabel.setText(chosenDeck);
                }
            });
        }
    }

    private void addRectangleOfDecksToPage() {
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);
        for (int i = 0; i < 4; i++) {
            pane.getChildren().add(fourRectangleToShowDecks.get(i));
        }
    }

    private void createPacksOfDecksForEachPage(HashMap<String, Deck> allDecksOfUser) {
        decksInDifferentPages = new ArrayList<>();
        int deckCounter = 0;
        List<Deck> decksInOnePage = new ArrayList<>();
        for (Map.Entry<String, Deck> e : allDecksOfUser.entrySet()) {
            decksInOnePage.add(e.getValue());
            deckCounter++;
            if (deckCounter % 4 == 0) {
                decksInDifferentPages.add(decksInOnePage);
                decksInOnePage = new ArrayList<>();
            }
        }
        if (!decksInDifferentPages.contains(decksInOnePage) && decksInOnePage.size() > 0) {
            decksInDifferentPages.add(decksInOnePage);
        } else if (decksInDifferentPages.size() == 0) {
            decksInDifferentPages.add(decksInOnePage);
        }
    }

    public void deleteDeck() {
        String sendDataToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("deleteDeck", "deckName",
                chosenDeck);
        String resultOfServer = (String) ServerConnection.sendDataToServerAndReceiveResult(sendDataToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
                .deserializeForOnlyTypeAndMessage(resultOfServer);
        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
        if (!deserializeResult.get("type").equals("Successful")) {
            return;
        }
        if (deserializeResult.get("message").equals("Connection Disconnected")) {
            new MainMenuController().backToLoginPage();
        }
        if (decksInDifferentPages.get(currentPageToShowDecks).size() == 1) {
            if (currentPageToShowDecks != 0) {
                decksInDifferentPages.remove(currentPageToShowDecks);
                currentPageToShowDecks--;
            } else {
                decksInDifferentPages.get(0).remove(0);
            }
        } else {
            for (int j = 0; j < decksInDifferentPages.get(currentPageToShowDecks).size(); j++) {
                if (decksInDifferentPages.get(currentPageToShowDecks).get(j).getDeckname().equals(chosenDeck)) {
                    decksInDifferentPages.get(currentPageToShowDecks).remove(j);
                    break;
                }
            }
            editDecksInDifferentPageWhenDeckDeleted();
        }
        LoginController.getOnlineUser().deleteDeck(chosenDeck);
        chosenDeck = "";
        equalDeckNameLabel.setText("");
        createNewPage();
        setEffectOfpreviousAndnextDecksbtn();
        setEffectsOfEditAndDeleteButtons();
    }

    private void editDecksInDifferentPageWhenDeckDeleted() {
        for (int i = 0; i < decksInDifferentPages.size(); i++) {
            if (decksInDifferentPages.get(i).size() < 4 && decksInDifferentPages.size() > i + 1) {
                decksInDifferentPages.get(i).add(decksInDifferentPages.get(i + 1).get(0));
                decksInDifferentPages.get(i + 1).remove(0);
                if (decksInDifferentPages.get(i + 1).size() == 0) {
                    decksInDifferentPages.remove(i + 1);
                }
            }
        }
    }

    public void editDeck() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/oneDeckPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new DeckMenuController().showPage(pane, chosenDeck);
        chosenDeck = "";
        equalDeckNameLabel.setText("");
    }

    public void createNewDeck() {
        String createdDeckName = createdDeckNameField.getText();
        String sendDataToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("createDeck", "deckName",
                createdDeckName);
        String resultOfServer = (String) ServerConnection.sendDataToServerAndReceiveResult(sendDataToServer);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer
                .deserializeForOnlyTypeAndMessage(resultOfServer);
        showAlert(deserializeResult.get("message"), deserializeResult.get("type"));
        if (deserializeResult.get("type").equals("Error")) {
            createdDeckNameField.setText("");
            return;
        }
        if (deserializeResult.get("message").equals("Connection Disconnected")) {
            new MainMenuController().backToLoginPage();
        }

        Deck deck = new Deck(createdDeckName);
        LoginController.getOnlineUser().addDeckToAllDecks(createdDeckName, deck);

        LoginController.getOnlineUser().addDeckToAllDecks(createdDeckName, deck);

        createdDeckNameField.setText("");
        if (decksInDifferentPages.get(decksInDifferentPages.size() - 1).size() == 4) {
            List<Deck> anotherDeckPage = new ArrayList<>();
            anotherDeckPage.add(deck);
            decksInDifferentPages.add(anotherDeckPage);
        } else {
            decksInDifferentPages.get(decksInDifferentPages.size() - 1).add(deck);
            if (decksInDifferentPages.size() == currentPageToShowDecks + 1) {
                createNewPage();
            }
        }
        setEffectOfpreviousAndnextDecksbtn();
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    private void setEffectOfpreviousAndnextDecksbtn() {

        if (currentPageToShowDecks + 1 == decksInDifferentPages.size()) {
            equalToNextPagebtn.setDisable(true);
        } else {
            equalToNextPagebtn.setDisable(false);
        }

        if (currentPageToShowDecks == 0) {
            equalToPreviousPagebtn.setDisable(true);
        } else {
            equalToPreviousPagebtn.setDisable(false);
        }
    }

    private void setEffectsOfEditAndDeleteButtons() {
        if (chosenDeck.equals("")) {
            equalToEditDeckbtn.setDisable(true);
            equalToDeleteDeckbtn.setDisable(true);
        } else {
            equalToDeleteDeckbtn.setDisable(false);
            equalToEditDeckbtn.setDisable(false);
        }
    }

    public void goToNextPage() {
        currentPageToShowDecks++;
        createNewPage();
        chosenDeck = "";
        equalDeckNameLabel.setText("");
        setEffectsOfEditAndDeleteButtons();
    }

    private void createNewPage() {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (decksInDifferentPages == null) {
            createPacksOfDecksForEachPage(allDecksOfUser);
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (2 * i + j >= decksInDifferentPages.get(currentPageToShowDecks).size()) {
                    fourRectangleToShowDecks.get(i * 2 + j).setOpacity(0);
                    fourRectangleToShowDecks.get(i * 2 + j).setId("");
                    labelsToShowInformationOfDeck.get("mainDeck" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("sideDeck" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("monstersSize" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("spellsSize" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("trapsSize" + i + "" + j).setText("");
                } else {
                    fourRectangleToShowDecks.get(i * 2 + j)
                            .setId(decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getDeckname());
                    HashMap<String, Integer> sizeOfEachPart = getNumberOfEachTypeOfCardsInDeck(decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j));
                    labelsToShowInformationOfDeck.get("mainDeck" + i + "" + j)
                            .setText(sizeOfEachPart.get("mainDeckSize") + "");
                    setImageOfDeck(decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getIsDeckActive(),
                            sizeOfEachPart.get("mainDeckSize"), fourRectangleToShowDecks.get(2 * i + j));
                    labelsToShowInformationOfDeck.get("sideDeck" + i + "" + j)
                            .setText(sizeOfEachPart.get("sideDeckSize") + "");
                    labelsToShowInformationOfDeck.get("monstersSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("monstersSize") + "");
                    labelsToShowInformationOfDeck.get("spellsSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("spellsSize") + "");
                    labelsToShowInformationOfDeck.get("trapsSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("trapsSize") + "");
                }
            }
        }
        setEffectOfpreviousAndnextDecksbtn();
    }

    private void setImageOfDeck(boolean isDeckActive, int sizeOfMainDeck, Rectangle rectangle) {
        if (isDeckActive) {
            if (sizeOfMainDeck < 40) {
                rectangle.setFill(new ImagePattern(UIStorage.getDecksImage().get("invalidActivateDeck")));
                rectangle.setOpacity(0.95);
            } else {
                rectangle.setFill(new ImagePattern(UIStorage.getDecksImage().get("validActivateDeck")));
                rectangle.setOpacity(1);
            }
        } else {
            if (sizeOfMainDeck < 40) {
                rectangle.setOpacity(0.8);
                rectangle.setFill(new ImagePattern(UIStorage.getDecksImage().get("invalidDeck")));
            } else {
                rectangle.setOpacity(0.9);
                rectangle.setFill(new ImagePattern(UIStorage.getDecksImage().get("validDeck")));
            }
        }
    }

    public void backToPreviousPage() {
        currentPageToShowDecks--;
        createNewPage();
        chosenDeck = "";
        equalDeckNameLabel.setText("");
        setEffectsOfEditAndDeleteButtons();
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Integer> getNumberOfEachTypeOfCardsInDeck(Deck deck) {
        int numberOfMonsterCards = 0;
        int numberOfSpellCards = 0;
        int numberOfTrapCards = 0;
        List<String> mainDeckCards = deck.getMainDeck();

        HashMap<String, Integer> sizeOfEachPart = new HashMap<>();
        sizeOfEachPart.put("mainDeckSize", mainDeckCards.size());
        sizeOfEachPart.put("sideDeckSize", deck.getSizeOfSideDeck());
        for (int i = 0; i < mainDeckCards.size(); i++) {
            Card card = CardsStorage.getCardByName(mainDeckCards.get(i));
            if (card.getCardType().equals(CardType.MONSTER)) {
                numberOfMonsterCards++;
            } else {
                if (card.getCardType().equals(CardType.SPELL)) {
                    numberOfSpellCards++;
                } else {
                    numberOfTrapCards++;
                }
            }
        }
        sizeOfEachPart.put("monstersSize", numberOfMonsterCards);
        sizeOfEachPart.put("spellsSize", numberOfSpellCards);
        sizeOfEachPart.put("trapsSize", numberOfTrapCards);
        return sizeOfEachPart;
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        WholeDeckPageMenuController.anchorPane = anchorPane;
    }
}
