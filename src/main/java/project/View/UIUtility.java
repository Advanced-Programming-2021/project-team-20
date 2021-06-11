package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;

public class UIUtility {

    private static List<Rectangle> allMainDeckRectangle;
    private static List<Rectangle> allSideDeckRectangle;
    private static List<Rectangle> allScrollBarRectangle;
    private static List<Rectangle> allScrollBarBackGroundRectangles;
    private static List<Rectangle> fourRectangleToShowDecks;
    private static Image validDeckImage;
    private static Image invalidDeckImage;
    private static ArrayList<Card> allCards = new ArrayList<>();
    private static List<Label> allScrollBarLabels = new ArrayList<>();
    private static Card unknownCard;
    private static List<Label> numberOfCardslabels = new ArrayList<>();

    private static List<Label> allCardDiscriptionLabels;

    public static void createPreliminaryToStartProgram() {
        createAllCardDiscriptionLabels();
        createAllCards();
        createAllScrooBarLabels();
        createFourRectangleToShowDecks();
        // createImage(cardName);
        createLabelToShowSizeOfCardsInDeck();
        createNmaeOfCards();
        createOneRectangleForEachCard();
        validDeckImage = createValidAndInvalidDeckImage("validDeck");
        invalidDeckImage = createValidAndInvalidDeckImage("invalidDeck");
    }

    private static Image createValidAndInvalidDeckImage(String imagename) {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\images\\deckpage\\" + imagename + ".PNG");
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
        // HashMap<String, Card> allMonsterCard = Storage.getAllMonsterCards();
        // HashMap<String, Card> allSpellTrapCard = Storage.getAllSpellAndTrapCards();

        // for (Entry<String, Card> e : allMonsterCard.entrySet()) {
        // Rectangle rectangle = new Rectangle(35, 45);
        // allMonsterRectangle.add(rectangle);
        // }

        // for (Entry<String, Card> e : allSpellTrapCard.entrySet()) {
        // Rectangle rectangle = new Rectangle(35, 45);
        // if (e.getValue().getCardType().equals(CardType.SPELL)) {
        // allSpellRectangle.add(rectangle);
        // } else {
        // allTrapRectangle.add(rectangle);
        // }
        // }

        for (int i = 0; i < 60; i++) {
            Rectangle rectangle = new Rectangle(60, 85);
            // rectangle.setArcHeight(10);
            // rectangle.setArcWidth(10);
            allMainDeckRectangle.add(rectangle);
            // rectangle.setStroke(Color.WHITE);
            // rectangle.setStrokeWidth(3);
        }

        for (int i = 0; i < 15; i++) {
            Rectangle rectangle = new Rectangle(60, 85);
            // rectangle.setArcHeight(10);
            // rectangle.setArcWidth(10);
            allSideDeckRectangle.add(rectangle);
        }

        for (int i = 0; i < 100; i++) {
            Rectangle rectangle = new Rectangle(45, 55);
            // rectangle.setArcHeight(10);
            // rectangle.setArcWidth(10);
            allScrollBarRectangle.add(rectangle);
        }

        for (int i = 0; i < 100; i++) {
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
            fourRectangleToShowDecks.add(rectangle);
        }
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

    public static void createAllCards() {
        ArrayList<String> sList = createNmaeOfCards();
        for (int i = 0; i < sList.size(); i++) {
            Image image = createImage(sList.get(i));
            Card card = new Card(sList.get(i), CardType.MONSTER,
                    "sadasldlas dsadalsdasld; lkdakdsadkasd saldkasldkla;d ksaldjkasldjkslda kladjaklsjdakls lasjdklasjdka lsjdkajdlaksd",
                    null, 3, 2000, "");
            card.setImage(image);
            allCards.add(card);
        }

        unknownCard = new Card("cardName", null, "", null, 10, 125, "imagePath");
        unknownCard.setImage(createImage("Unknown"));
    }

    public static void createAllCardDiscriptionLabels() {
        allCardDiscriptionLabels = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            allCardDiscriptionLabels.add(new Label());
        }
    }

    public static ArrayList<String> createNmaeOfCards() {
        ArrayList<String> a = new ArrayList<>();
        a.add("Terratiger, the Empowered Warrior");
        a.add("Wattkid");
        a.add("Scanner");
        a.add("Warrior Dai Grepher");
        a.add("Axe Raider");
        a.add("Beast King Barbaros");
        a.add("The Tricky");
        a.add("Suijin");
        a.add("Blue-Eyes white dragon");
        a.add("Flame manipulator");
        a.add("Dark Blade");
        a.add("Command Knight");
        a.add("Marshmallon");
        a.add("Silver Fang");
        a.add("Dark magician");
        a.add("Slot Machine");
        a.add("Alexandrite Dragon");
        a.add("Hero of the east");
        a.add("Bitron");
        a.add("Leotron");
        a.add("Yomi Ship");
        a.add("Horn Imp");
        a.add("Exploder Dragon");
        a.add("Herald of Creation");
        a.add("Battle warrior");
        a.add("Curtain of the dark ones");
        a.add("Man-Eater Bug");
        a.add("Texchanger");
        a.add("Spiral Serpent");
        a.add("Wattaildragon");
        a.add("Feral Imp");
        a.add("Gate Guardian");
        a.add("Fireyarou");
        a.add("The Calculator");
        a.add("Baby dragon");
        a.add("Crab Turtle");
        a.add("Haniwa");
        a.add("Crawling dragon");
        a.add("Skull Guardian");
        a.add("Mirage Dragon");
        a.add("Sword of dark destruction");
        a.add("Pot of Greed");
        a.add("Umiiruka");
        a.add("Magic Jamamer");
        a.add("Change of Heart");
        a.add("Solemn Warning");
        a.add("Raigeki");
        a.add("Twin Twisters");
        a.add("Terraforming");
        a.add("Magnum Shield");
        a.add("Spell Absorption");
        a.add("Monster Reborn");
        a.add("Negate Attack");
        a.add("Mystical space typhoon");
        a.add("United We Stand");
        a.add("Messenger of peace");
        a.add("Call of The Haunted");
        a.add("Trap Hole");
        a.add("Time Seal");
        a.add("Supply Squad");
        a.add("Dark Hole");
        a.add("Black Pendant");
        a.add("Yami");
        a.add("Advanced Ritual Art");

        a.add("Magic Cylinder");
        a.add("Mind Crush");
        a.add("Harpie's Feather Duster");
        a.add("Ring of defense");
        a.add("Closed Forest");
        a.add("Mirror Force");
        a.add("Torrential Tribute");

        a.add("Forest");
        a.add("Swords of Revealing Light");
        return a;
    }

    private static Image createImage(String cardName) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(
                    "src\\main\\resources\\project\\images\\Cards\\" + cardName.replaceAll("\\s", "") + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(stream);
    }

    public static Card getCardByName(String cardName) {
        for (int i = 0; i < allCards.size(); i++) {
            if (allCards.get(i).getCardName().equals(cardName)) {
                return allCards.get(i);
            }
        }
        return unknownCard;
    }

    public static ArrayList<Card> getMainDeck() {
        ArrayList<Card> mList = new ArrayList<>();
        for (int i = 0; i < allCards.size(); i++) {
            if (i % 2 == 0)
                mList.add(allCards.get(i));
        }
        return mList;
    }

    public static ArrayList<Card> getSideDeck() {
        ArrayList<Card> mList = new ArrayList<>();
        for (int i = 0; i < allCards.size(); i++) {
            if (i % 6 == 0)
                mList.add(allCards.get(i));
        }
        return mList;
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

    public static ArrayList<Card> getAllCards() {
        return allCards;
    }

    public static Card getUnknownCard() {
        return unknownCard;
    }

    public static List<Label> getAllCardDiscriptionLabels() {
        return allCardDiscriptionLabels;
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

    public static Image getValidDeckImage() {
        return validDeckImage;
    }

    public static Image getInvalidDeckImage() {
        return invalidDeckImage;
    }

    public static List<Rectangle> getFourRectangleToShowDecks() {
        return fourRectangleToShowDecks;
    }
}
