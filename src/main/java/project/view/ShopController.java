package project.view;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
import project.controller.non_duel.shop.Shop;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;

import java.net.URL;
import java.util.*;

public class ShopController implements Initializable {
    @FXML
    private Button buybtn;
    @FXML
    private Button backbtn;
    @FXML
    private Button previousCardsbtn;
    @FXML
    private Button nextCardsbtn;
    @FXML
    private Rectangle showCardRectangle;
    @FXML
    private Label selectedCardNameLabel;
    @FXML
    private AnchorPane showCardsAnchorPane;
    @FXML
    private Label numbserOfShoppingCardsLabel;
    @FXML
    private Label numberOfUselessCardsLabel;
    @FXML
    private Label userMoneyLabel;
    private static List<List<Card>> allCardsInDifferentPages;
    private List<Label> allCardDiscriptionLabels;
    private int whichPageIsShowing = 0;
    private static String cardNameForBuy = "";
    private static List<Rectangle> rectanglesToShowCards;
    private static AnchorPane anchorPane;
    private static Rectangle equalShowCardRectangle;
    private Shop shop = new Shop();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/Shop.mp3");

        if (rectanglesToShowCards == null) {
            rectanglesToShowCards = UIStorage.getAllShopRectangles();
            addEffectsToRectanglesThatShowCards();
        }
        allCardDiscriptionLabels = UIStorage.getAllCardDiscriptionLabels1();
        equalShowCardRectangle = showCardRectangle;
        if (allCardsInDifferentPages == null) {
            createPacksOfCardsForEachPage();
        }
        int sizeOfCardsInDifferentPages = 0;
        for (int i = 0; i < allCardsInDifferentPages.size(); i++) {
            sizeOfCardsInDifferentPages += allCardsInDifferentPages.get(i).size();
        }

        if (sizeOfCardsInDifferentPages != UIStorage.getAllTypeOfCards().get("allCards").size()) {
            createPacksOfCardsForEachPage();
        }
        userMoneyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        userMoneyLabel.setTextFill(Color.BLUE);
        userMoneyLabel.setText("My Money: " + LoginController.getOnlineUser().getMoney());
        selectedCardNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        numberOfUselessCardsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        numberOfUselessCardsLabel.setTextFill(Color.BLUE);
        numbserOfShoppingCardsLabel.setTextFill(Color.BLUE);
        numbserOfShoppingCardsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        setEffectsOfButtons();
    }

    private void setEffectsOfButtons() {
        if (whichPageIsShowing + 1 == allCardsInDifferentPages.size()) {
            nextCardsbtn.setDisable(true);
        } else {
            nextCardsbtn.setDisable(false);
        }

        if (whichPageIsShowing == 0) {
            previousCardsbtn.setDisable(true);
        } else {
            previousCardsbtn.setDisable(false);
        }

        if (cardNameForBuy.equals("")) {
            buybtn.setDisable(true);
        } else {
            buybtn.setDisable(false);
        }
    }

    private void addEffectsToRectanglesThatShowCards() {

        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            Rectangle rectangle = rectanglesToShowCards.get(i);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    cardNameForBuy = rectangle.getId();
                    setEffectsOfButtons();
                    setEffectsOfBuyButtonAndShowLabel();
                }
            });

            rectangle.setOnMouseEntered(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
                    equalShowCardRectangle.setFill(rectangle.getFill());
                    equalShowCardRectangle.setOpacity(1);
                    addCardDescription(rectangle.getId());
                    showNumberOfBoughtCards(rectangle.getId());
                    flipRectangle(equalShowCardRectangle);
                }
            });
        }
    }

    private void showNumberOfBoughtCards(String cardName) {
        HashMap<String, Integer> numberOfCards = shop.getNumberOfCards(cardName);
        numberOfUselessCardsLabel.setText("Useless Cards: " + numberOfCards.get("uselessCards"));
        numbserOfShoppingCardsLabel.setText("Bought Cards: " + numberOfCards.get("numberOfBoughtCards"));
    }

    private void setEffectsOfBuyButtonAndShowLabel() {
        Card card = Storage.getCardByName(cardNameForBuy);
        int cardPrice = card.getCardPrice();
        if (cardPrice > LoginController.getOnlineUser().getMoney()) {
            selectedCardNameLabel.setTextFill(Color.RED);
            selectedCardNameLabel.setText("Selected Card To Buy: " + cardNameForBuy + " , Card price: " + cardPrice);
            buybtn.setDisable(true);
        } else {
            buybtn.setDisable(false);
            selectedCardNameLabel.setTextFill(Color.BLUE);
            selectedCardNameLabel.setText("Selected Card To Buy: " + cardNameForBuy + " , Card price: " + cardPrice);
        }
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

    private void addCardDescription(String cardName) {
        Card card = Storage.getCardByName(cardName);
        String cardDiscription = card.getCardDescription();
        ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
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

    private void createPacksOfCardsForEachPage() {
        allCardsInDifferentPages = new ArrayList<>();
        List<Card> allCards = UIStorage.getAllTypeOfCards().get("allCards");
        List<Card> cardsInOnePage = new ArrayList<>();

        for (int i = 0; i < Math.floorDiv(allCards.size(), 24) + 1; i++) {
            for (int j = 0; j < rectanglesToShowCards.size(); j++) {
                if (i * 24 + j >= allCards.size()) {
                    break;
                }
                cardsInOnePage.add(allCards.get(i * 24 + j));
            }
            allCardsInDifferentPages.add(cardsInOnePage);
            cardsInOnePage = new ArrayList<>();
        }
    }

    public void createSceneAndCardPictures(AnchorPane pane) {
        setAnchorPane(pane);
        List<Card> allCards = UIStorage.getAllTypeOfCards().get("allCards");
        AnchorPane backgroundPane = (AnchorPane) anchorPane.getChildren().get(1);
        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            Rectangle rectangle = rectanglesToShowCards.get(i);
            rectangle.setFill(new ImagePattern(allCards.get(i).getImage()));
            rectangle.setId(allCards.get(i).getCardName());
            backgroundPane.getChildren().add(rectangle);
        }
        MainView.changeScene(anchorPane);
    }

    public void nextPage() {
        AnchorPane backgroundPane = (AnchorPane) anchorPane.getChildren().get(1);
        whichPageIsShowing++;
        setEffectsOfButtons();
        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            if (i >= allCardsInDifferentPages.get(whichPageIsShowing).size()) {
                backgroundPane.getChildren().remove(rectanglesToShowCards.get(i));
                continue;
            }
            rectanglesToShowCards.get(i).setId(allCardsInDifferentPages.get(whichPageIsShowing).get(i).getCardName());
            rectanglesToShowCards.get(i)
                    .setFill(new ImagePattern(allCardsInDifferentPages.get(whichPageIsShowing).get(i).getImage()));
        }
    }

    public void previousPage() {
        AnchorPane backgroundPane = (AnchorPane) anchorPane.getChildren().get(1);
        whichPageIsShowing--;
        setEffectsOfButtons();
        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            if (i >= backgroundPane.getChildren().size()) {
                backgroundPane.getChildren().add(rectanglesToShowCards.get(i));
            }
            rectanglesToShowCards.get(i).setId(allCardsInDifferentPages.get(whichPageIsShowing).get(i).getCardName());
            rectanglesToShowCards.get(i)
                    .setFill(new ImagePattern(allCardsInDifferentPages.get(whichPageIsShowing).get(i).getImage()));
        }
    }

    public void buyCard() {
        if (cardNameForBuy == null) {
            System.out.println("Please choose a card first!");
        } else {
            String answerOfShop = shop.findCommand("shop buy " + cardNameForBuy);
            userMoneyLabel.setText("My Money: " + LoginController.getOnlineUser().getMoney());
        }

    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        ShopController.anchorPane = anchorPane;
    }

}
