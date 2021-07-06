package project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import project.controller.non_duel.deckCommands.DeckCommands;
import project.model.Deck;

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
    private DeckCommands deckCommands = new DeckCommands();
    private static String chosenDeck = new String();
    private int currentPageToShowDecks = 0;
    private static Button equalToNextPagebtn;
    private static Button equalToPreviousPagebtn;
    private static Button equalToDeleteDeckbtn;
    private static Button equalToEditDeckbtn;
    private static Label equalDeckNameLabel;
    private boolean isEnteredMouse = false;
    private Long firstTimeMouseEnteredRectangle = 0l;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
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
        // showCardsInDeck();
        for (int i = 0; i < fourRectangleToShowDecks.size(); i++) {
            int index = i;
            fourRectangleToShowDecks.get(i).setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent arg0) {
                    firstTimeMouseEnteredRectangle = System.currentTimeMillis();
                }
            });

            fourRectangleToShowDecks.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    chosenDeck = fourRectangleToShowDecks.get(index).getId();
                    setEffectsOfEditAndDeleteButtons();
                    equalDeckNameLabel.setText(chosenDeck);
                }
            });
            fourRectangleToShowDecks.get(i).setOnMouseExited(MouseEvent -> {
            });
        }
    }

    private void showCardsInDeck() {
        System.out.println("x");
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
        }
    }

    public void deleteDeck() {
        deckCommands.deleteDeck(chosenDeck, LoginController.getOnlineUser().getName());
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
        chosenDeck = "";
        equalDeckNameLabel.setText("");
        createNewPage();
        setEffectOfpreviousAndnextDecksbtn();
        setEffectsOfEditAndDeleteButtons();
        showAlert("DECK DELETED SUCCESSFULLY!", "SUCCESSFUL");
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
        String result = deckCommands.createDeck(createdDeckName, LoginController.getOnlineUser().getName());
        if (createdDeckName.equals("")) {
            showAlert("ENTER DECK NAME", "ERROR");
            return;
        }
        if (result.equals("deck already exists")) {
            showAlert("DECK ALREADY EXISTS", "ERROR");
            createdDeckNameField.setText("");
            return;
        }
        showAlert("DECK CREATED SUCCESSFULLY!", "SUCCESSFUL");
        createdDeckNameField.setText("");
        Deck deck = LoginController.getOnlineUser().getDecks().get(createdDeckName);
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
                    fourRectangleToShowDecks.get(i * 2 + j).setOpacity(1);
                    fourRectangleToShowDecks.get(i * 2 + j)
                            .setId(decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getDeckname());
                    // System.out.println(fourRectangleToShowDecks.get(2 * i + j).getId() + " " + 2
                    // * i + j);
                    HashMap<String, Integer> sizeOfEachPart = deckCommands.getNumberOfEachTypeOfCardsInDeck(
                            decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getDeckname(),
                            LoginController.getOnlineUser().getName());
                    if (fourRectangleToShowDecks.get(2 * i + j).getId().equals("JustMonster")) {
                        System.out.println("mainDeck" + i + "" + j);
                    }
                    labelsToShowInformationOfDeck.get("mainDeck" + i + "" + j)
                            .setText(sizeOfEachPart.get("mainDeckSize") + "");
                    if (sizeOfEachPart.get("mainDeckSize") < 40) {
                        fourRectangleToShowDecks.get(i * 2 + j)
                                .setFill(new ImagePattern(UIStorage.getDecksImage().get("invalidDeck")));
                    } else {
                        fourRectangleToShowDecks.get(i * 2 + j)
                                .setFill(new ImagePattern(UIStorage.getDecksImage().get("validDeck")));
                    }
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

    public static void setAnchorPane(AnchorPane anchorPane) {
        WholeDeckPageMenuController.anchorPane = anchorPane;
    }
}