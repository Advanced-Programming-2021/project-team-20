package project.View;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import javafx.geometry.Pos;

public class UIUtility {

    private static List<Rectangle> allMainDeckRectangle;
    private static List<Rectangle> allSideDeckRectangle;
    private static List<Rectangle> allScrollBarRectangle;
    private static List<Rectangle> allScrollBarBackGroundRectangles;
    private static List<Rectangle> fourRectangleToShowDecks;
    private static List<Rectangle> rectanglesToShowCardsInImportAndExportClass;
    private static HashMap<String, Image> decksImage;
    private static List<Label> allScrollBarLabels = new ArrayList<>();
    private static List<Label> numberOfCardslabels = new ArrayList<>();
    private static HashMap<String, List<Card>> allTypeOfCards;
    private static HashMap<String, Label> labelsToShowInformationOfDeck;
    private static HashMap<String, Image> imagesForRockPaperScissorController;

    private static List<Label> allCardDiscriptionLabels1;
    private static List<Label> allCardDiscriptionLabels2;

    public static void createPreliminaryToStartProgram() {
        createAllCardDiscriptionLabels();
        createAllScrooBarLabels();
        createFourRectangleToShowDecks();
        createLabelToShowSizeOfCardsInDeck();
        createOneRectangleForEachCard();
        createAllTypeOfCards();
        initializeLabelsToShowInfornationOfDeck();
        createRectanglesToShowCardsInImportAndExportClass();
        createDeckImages();
        createImagesForRockPaperScissorController();
    }

    private static void createDeckImages() {
        decksImage = new HashMap<>();
        decksImage.put("validDeck", createImages("deckpage\\", "validDeck"));
        decksImage.put("invalidDeck", createImages("deckpage\\", "invalidDeck"));
    }

    private static void createImagesForRockPaperScissorController() {
        imagesForRockPaperScissorController = new HashMap<>();
        imagesForRockPaperScissorController.put("mouseEnterdScissorBaz",
                createImages("stonePaperscissor\\", "mouseEnterdScissorBaz"));
        imagesForRockPaperScissorController.put("stone", createImages("stonePaperscissor\\", "stone"));
        imagesForRockPaperScissorController.put("mouseEnterdStone",
                createImages("stonePaperscissor\\", "mouseEnterdStone"));
        imagesForRockPaperScissorController.put("scissor", createImages("stonePaperscissor\\", "scissor"));
        imagesForRockPaperScissorController.put("mouseEnterePaper",
                createImages("stonePaperscissor\\", "mouseEnterePaper"));
        imagesForRockPaperScissorController.put("paper", createImages("stonePaperscissor\\", "paper"));
    }

    private static void createRectanglesToShowCardsInImportAndExportClass() {
        rectanglesToShowCardsInImportAndExportClass = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                Rectangle rectangle = new Rectangle(45, 65);
                rectangle.setX(4 + 52.5 * j);
                rectangle.setY(4 + 71 * i);
                rectanglesToShowCardsInImportAndExportClass.add(rectangle);
            }
        }
    }

    private static void createAllTypeOfCards() {
        allTypeOfCards = new HashMap<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        List<Card> allCardsInList = new ArrayList<>();
        List<Card> allMonsterCardsInList = new ArrayList<>();

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            allCardsInList.add(entry.getValue());
            allMonsterCardsInList.add(entry.getValue());
        }

        List<Card> allSpellCardsInList = new ArrayList<>();
        List<Card> allTrapCradsInList = new ArrayList<>();
        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            allCardsInList.add(entry.getValue());
            if (entry.getValue().getCardType().equals(CardType.SPELL)) {
                allSpellCardsInList.add(entry.getValue());
            } else {
                allTrapCradsInList.add(entry.getValue());
            }
        }

        allTypeOfCards.put("allCards", allCardsInList);
        allTypeOfCards.put("allMonsterCards", allMonsterCardsInList);
        allTypeOfCards.put("allSpellCards", allSpellCardsInList);
        allTypeOfCards.put("allTrapCardsInList", allTrapCradsInList);
    }

    private static Image createImages(String path, String imagename) {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\images\\" + path + imagename + ".PNG");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(stream);
    }

    protected static void createOneRectangleForEachCard() {

        allMainDeckRectangle = new ArrayList<>();
        allSideDeckRectangle = new ArrayList<>();
        allScrollBarRectangle = new ArrayList<>();
        allScrollBarBackGroundRectangles = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            Rectangle rectangle = new Rectangle(45, 65);
            rectangle.setX(4 + 52.5 * (i % 10));
            rectangle.setY(4 + 71 * (i / 10));
            allMainDeckRectangle.add(rectangle);
        }

        for (int i = 0; i < 15; i++) {
            Rectangle rectangle = new Rectangle(45, 65);
            rectangle.setX(4 + 52.5 * (i % 10));
            rectangle.setY(4 + 71 * (i / 10));
            allSideDeckRectangle.add(rectangle);
        }

        int sizeOfWholeCards = Storage.getAllMonsterCards().size() + Storage.getAllSpellAndTrapCards().size();

        for (int i = 0; i < sizeOfWholeCards; i++) {
            Rectangle rectangle = new Rectangle(45, 55);
            allScrollBarRectangle.add(rectangle);
        }

        for (int i = 0; i < sizeOfWholeCards; i++) {
            Rectangle rectangle = new Rectangle(50, 60);
            rectangle.setStroke(Color.RED);
            rectangle.setStrokeWidth(5);
            rectangle.setFill(Color.CORAL);
            allScrollBarBackGroundRectangles.add(rectangle);
        }
    }

    public static void createFourRectangleToShowDecks() {
        fourRectangleToShowDecks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Rectangle rectangle = new Rectangle(290, 190);
            rectangle.setX(7 + 306 * (i % 2));
            rectangle.setY(7 + 204 * (i / 2));
            fourRectangleToShowDecks.add(rectangle);
        }
    }

    public static void initializeLabelsToShowInfornationOfDeck() {
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

    private static void createLabelForDeckname(Label label, int row, int column) {
        label.setPrefSize(270, 50);
        label.setFont(new Font(20));
        label.setLayoutX(10 + 310 * column);
        label.setLayoutY(210 * row);
        label.setAlignment(Pos.CENTER);
    }

    private static void createLabelForSizeOfOtherParts(Label label, int row, int column, int translateX,
            int translateY) {
        label.setPrefSize(100, 50);
        label.setFont(new Font(20));
        label.setLayoutY(51 + 210 * row + translateY);
        label.setLayoutX(52 + 310 * column + translateX);
        label.setAlignment(Pos.CENTER);
    }

    public static void createLabelToShowSizeOfCardsInDeck() {
        Label allDecksSizeLabel = new Label();
        allDecksSizeLabel.setLayoutX(268);
        allDecksSizeLabel.setLayoutY(57);
        allDecksSizeLabel.setPrefSize(51, 57);
        allDecksSizeLabel.setFont(new Font(30));
        allDecksSizeLabel.setTextFill(Color.BLACK);
        numberOfCardslabels.add(allDecksSizeLabel);
        for (int i = 0; i < 3; i++) {
            Label label = new Label();
            label.setLayoutX(268);
            label.setLayoutY(181 + 90 * i);
            label.setPrefSize(51, 57);
            label.setTextFill(Color.BLACK);
            label.setFont(new Font(30));
            numberOfCardslabels.add(label);
        }
    }

    public static void createAllScrooBarLabels() {
        for (int i = 0; i < 100; i++) {
            Label label = new Label();
            label.setTextFill(Color.BLACK);
            label.setFont(new Font(14));
            label.setStyle("-fx-font-weight: bold");
            allScrollBarLabels.add(label);
        }
    }

    public static void createAllCardDiscriptionLabels() {
        allCardDiscriptionLabels1 = new ArrayList<>();
        allCardDiscriptionLabels2 = new ArrayList<>();
        Label label;
        for (int i = 0; i < 100; i++) {
            label = new Label();
            label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 14));
            allCardDiscriptionLabels1.add(label);
            label = new Label();
            label.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 14));
            allCardDiscriptionLabels2.add(new Label());
        }
    }

    public static HashMap<String, Image> getImagesForRockPaperScissorController() {
        return imagesForRockPaperScissorController;
    }

    public static List<Rectangle> getAllMainDeckRectangle() {
        return allMainDeckRectangle;
    }

    public static List<Rectangle> getAllScrollBarRectangle() {
        return allScrollBarRectangle;
    }

    public static List<Rectangle> getAllSideDeckRectangle() {
        return allSideDeckRectangle;
    }

    public static List<Label> getAllCardDiscriptionLabels1() {
        return allCardDiscriptionLabels1;
    }

    public static List<Label> getAllCardDiscriptionLabels2() {
        return allCardDiscriptionLabels2;
    }

    public static List<Label> getAllScrollBarLabels() {
        return allScrollBarLabels;
    }

    public static List<Label> getNumberOfCardslabels() {
        return numberOfCardslabels;
    }

    public static List<Rectangle> getAllScrollBarBackGroundRectangles() {
        return allScrollBarBackGroundRectangles;
    }

    public static HashMap<String, Image> getDecksImage() {
        return decksImage;
    }

    public static List<Rectangle> getFourRectangleToShowDecks() {
        return fourRectangleToShowDecks;
    }

    public static HashMap<String, List<Card>> getAllTypeOfCards() {
        return allTypeOfCards;
    }

    public static HashMap<String, Label> getLabelsToShowInformationOfDeck() {
        return labelsToShowInformationOfDeck;
    }

    public static List<Rectangle> getRectanglesToShowCardsInImportAndExportClass() {
        return rectanglesToShowCardsInImportAndExportClass;
    }
}
