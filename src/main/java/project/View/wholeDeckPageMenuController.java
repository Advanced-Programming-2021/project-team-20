package project.View;

import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }

    public void showPage(AnchorPane pane) {
        setAnchorPane(pane);
        if (fourRectangleToShowDecks == null) {
            addEffectsToFourRectangleToShowDeck();
        }
        addDecksToPage();
        if (labelsToShowInformationOfDeck == null) {
            initializeLabelsToShowInfornationOfDeck();
        }
        setEffectOfpreviousAndnextCardsbtn();
        MainView.changeScene(pane);
    }

    private void initializeLabelsToShowInfornationOfDeck() {
        labelsToShowInformationOfDeck = new HashMap<>();
        Label label = new Label();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForDeckname(label, i, j);
                labelsToShowInformationOfDeck.put("deckname" + i + "" + j, label);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForSizeOfOtherParts(label, i, j, 0, 0);
                labelsToShowInformationOfDeck.put("mainDeck" + i + "" + j, label);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForSizeOfOtherParts(label, i, j, 90, 0);
                labelsToShowInformationOfDeck.put("sideDeck" + i + "" + j, label);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForSizeOfOtherParts(label, i, j, -50, 60);
                labelsToShowInformationOfDeck.put("monstersSize" + i + "" + j, label);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForSizeOfOtherParts(label, i, j, 50, 60);
                labelsToShowInformationOfDeck.put("spellsSize" + i + "" + j, label);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                label = new Label();
                createLabelForSizeOfOtherParts(label, i, j, 150, 60);
                labelsToShowInformationOfDeck.put("trapsSize" + i + "" + j, label);
            }
        }
    }

    private void createLabelForDeckname(Label label, int row, int column) {
        label.setPrefSize(270, 50);
        label.setFont(new Font(20));
        label.setLayoutX(10 + 310 * column);
        label.setLayoutY(210 * row);
        label.setAlignment(Pos.CENTER);
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);
        label.setText("arg0 my best deck");
        pane.getChildren().add(label);
    }

    private void createLabelForSizeOfOtherParts(Label label, int row, int column, int translateX, int translateY) {
        label.setPrefSize(100, 50);
        label.setFont(new Font(20));
        label.setLayoutY(51 + 210 * row + translateY);
        label.setLayoutX(52 + 310 * column + translateX);
        label.setAlignment(Pos.CENTER);
        AnchorPane pane = (AnchorPane) anchorPane.getChildren().get(4);
        label.setText("49");
        pane.getChildren().add(label);
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
        // HashMap<String, Deck> allDecksOfUser =
        // LoginController.getOnlineUser().getDecks();
        // if (decksInDifferentPages == null) {
        // createPacksOfDecksForEachPage(allDecksOfUser);
        // }

        for (int i = 0; i < 4; i++) {
            fourRectangleToShowDecks.get(i).setX(7 + 310 * (i % 2));
            fourRectangleToShowDecks.get(i).setY(5 + 210 * (i / 2));
            // if (i >= decksInDifferentPages.get(currentPageToShowDecks).size()) {
            // fourRectangleToShowDecks.get(i).setOpacity(0);
            // fourRectangleToShowDecks.get(i).setId("");
            // } else {
            // fourRectangleToShowDecks.get(i)
            // .setId(decksInDifferentPages.get(currentPageToShowDecks).get(i).getDeckname());
            // if
            // (decksInDifferentPages.get(currentPageToShowDecks).get(i).getSizeOfMainDeck()
            // < 40) {
            // fourRectangleToShowDecks.get(i).setFill(new
            // ImagePattern(UIUtility.getInvalidDeckImage()));
            // } else {
            // fourRectangleToShowDecks.get(i).setFill(new
            // ImagePattern(UIUtility.getValidDeckImage()));
            // }
            // fourRectangleToShowDecks.get(i).setOpacity(1);
            // }
            pane.getChildren().add(fourRectangleToShowDecks.get(i));
        }
    }

    private void createPacksOfDecksForEachPage(HashMap<String, Deck> allDecksOfUser) {
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
            nextPagebtn.setDisable(true);
        } else {
            nextPagebtn.setDisable(false);
        }

        if (currentPageToShowDecks == 0) {
            previousPagebtn.setDisable(true);
        } else {
            previousPagebtn.setDisable(false);
        }
    }

    public void goToNextPage() {
        currentPageToShowDecks++;
    //    changePage();
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
      //  changePage();
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