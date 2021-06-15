package project.View;

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

public class wholeDeckPageMenuController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button createNewDeckbtn;
    @FXML
    private Button deleteDeckbtn;
    @FXML
    private Button nextPagebtn;
    @FXML
    private Button previousPagebtn;
    private TextField createdDeckNameField;
    private static List<Rectangle> fourRectangleToShowDecks;
    private static HashMap<String, Label> labelsToShowInformationOfDeck;
    private static List<List<Deck>> decksInDifferentPages;
    private static AnchorPane anchorPane;
    private DeckCommands deckCommands = new DeckCommands();
    private String chosenDeck = "";
    private int currentPageToShowDecks = 0;
    private static Button equalToNextPagebtn;
    private static Button equalToPreviousPagebtn;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        equalToNextPagebtn = nextPagebtn;
        equalToPreviousPagebtn = previousPagebtn;
    }

    public void showPage(AnchorPane pane) {
        setAnchorPane(pane);
        if (fourRectangleToShowDecks == null) {
            addEffectsToFourRectangleToShowDeck();
        }
        addDecksToPage();
        addInformationLabelsOfDeckToPane();
        setEffectOfpreviousAndnextCardsbtn();
        MainView.changeScene(pane);
    }

    private void addInformationLabelsOfDeckToPane() {
        labelsToShowInformationOfDeck = UIUtility.getLabelsToShowInformationOfDeck();
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);

        for (Map.Entry<String, Label> entry : labelsToShowInformationOfDeck.entrySet()) {
            pane.getChildren().add(entry.getValue());
        }
    }

    private void addEffectsToFourRectangleToShowDeck() {
        fourRectangleToShowDecks = UIUtility.getFourRectangleToShowDecks();
        for (int i = 0; i < fourRectangleToShowDecks.size(); i++) {
            int index = i;
            fourRectangleToShowDecks.get(i).setOnMouseEntered(MouseEvent -> {
                System.out.println("setOnMouseEntered");
            });

            fourRectangleToShowDecks.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    chosenDeck = fourRectangleToShowDecks.get(index).getId();
                }
            });

            fourRectangleToShowDecks.get(i).setOnMouseExited(MouseEvent -> {
                System.out.println("setOnMouseExited");
            });
        }
    }

    private void addDecksToPage() {
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (decksInDifferentPages == null) {
            createPacksOfDecksForEachPage(allDecksOfUser);
        }

        for (int i = 0; i < 4; i++) {
            if (i >= decksInDifferentPages.get(currentPageToShowDecks).size()) {
                fourRectangleToShowDecks.get(i).setOpacity(0);
                fourRectangleToShowDecks.get(i).setId("");
            } else {
                fourRectangleToShowDecks.get(i)
                        .setId(decksInDifferentPages.get(currentPageToShowDecks).get(i).getDeckname());
                if (decksInDifferentPages.get(currentPageToShowDecks).get(i).getSizeOfMainDeck() < 40) {
                    fourRectangleToShowDecks.get(i).setFill(new ImagePattern(UIUtility.getInvalidDeckImage()));
                } else {
                    fourRectangleToShowDecks.get(i).setFill(new ImagePattern(UIUtility.getValidDeckImage()));
                }
                fourRectangleToShowDecks.get(i).setOpacity(1);
            }
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
        if (!decksInDifferentPages.contains(decksInOnePage)) {
            decksInDifferentPages.add(decksInOnePage);
        }
    }

    public void deleteDeck() {
        // Deck deck =
    }

    public void editDeck() {
        if (chosenDeck.equals("")) {
            //
            return;
        }
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/oneDeckPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new DeckMenuController().showPage(pane, null);
    }

    public void createNewDeck() {
        String createdDeckName = createdDeckNameField.getText();
        String result = deckCommands.createDeck(createdDeckName);
        if (result.equals("deck already exists")) {
            //
            return;
        } else {

        }
        Deck deck = LoginController.getOnlineUser().getDecks().get(createdDeckName);
        if (decksInDifferentPages.get(decksInDifferentPages.size() - 1).size() == 4) {
            List<Deck> anotherDeckPage = new ArrayList<>();
            anotherDeckPage.add(deck);
            decksInDifferentPages.add(anotherDeckPage);
        } else {
            decksInDifferentPages.get(decksInDifferentPages.size() - 1).add(deck);
        }
    }

    private void setEffectOfpreviousAndnextCardsbtn() {
        
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

    public void goToNextPage() {
        currentPageToShowDecks++;
        changePage();
        setEffectOfpreviousAndnextCardsbtn();
    }

    private void changePage() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (2 * i + j >= decksInDifferentPages.get(currentPageToShowDecks).size()) {
                    fourRectangleToShowDecks.get(i * 2 + j).setOpacity(0);
                    fourRectangleToShowDecks.get(i * 2 + j).setId("");
                    labelsToShowInformationOfDeck.get("deckname" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("mainDeck" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("sideDeck" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("monstersSize" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("spellsSize" + i + "" + j).setText("");
                    labelsToShowInformationOfDeck.get("trapsSize" + i + "" + j).setText("");
                } else {
                    fourRectangleToShowDecks.get(i * 2 + j).setOpacity(1);
                    fourRectangleToShowDecks.get(i * 2 + j)
                            .setId(decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getDeckname());
                    HashMap<String, Integer> sizeOfEachPart = deckCommands.getNumberOfEachTypeOfCardsInDeck(
                            decksInDifferentPages.get(currentPageToShowDecks).get(2 * i + j).getDeckname());
                    labelsToShowInformationOfDeck.get("deckname" + i + "" + j)
                            .setText(sizeOfEachPart.get("mainDeckSize") + "");
                    labelsToShowInformationOfDeck.get("mainDeck" + i + "" + j)
                            .setText(sizeOfEachPart.get("sideDeckSize") + "");
                    labelsToShowInformationOfDeck.get("sideDeck" + i + "" + j)
                            .setText(sizeOfEachPart.get("monstersSize") + "");
                    labelsToShowInformationOfDeck.get("monstersSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("monstersSize") + "");
                    labelsToShowInformationOfDeck.get("spellsSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("spellsSize") + "");
                    labelsToShowInformationOfDeck.get("trapsSize" + i + "" + j)
                            .setText(sizeOfEachPart.get("trapsSize") + "");
                }
            }
        }
    }

    public void backToPreviousPage() {
        currentPageToShowDecks--;
        changePage();
        setEffectOfpreviousAndnextCardsbtn();
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        wholeDeckPageMenuController.anchorPane = anchorPane;
    }
}