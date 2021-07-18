package project.client.view;

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
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.model.User;
import project.model.cardData.General.Card;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.non_duel.shop.Shop;

import java.net.URL;
import java.util.*;

public class ShopController implements Initializable {
//    @FXML
//    private Button auctionBtn;
    @FXML
    private Button buybtn;
//    @FXML
//    private Button sellbtn;
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
    private static Label equalSelectedCardNameLabel;
    private static Label equalNumbserOfShoppingCardsLabel;
    private static Label equalNumberOfUselessCardsLabel;
    private static Label equalUserMoneyLabel;
    private static Button equalBuybtn;
    private Shop shop = new Shop();
    private static String token;
    private static User user;
    private static HashMap<String, String> givenInformationDeserialized;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/Shop.mp3");

        user = LoginController.getOnlineUser();

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
        equalBuybtn = buybtn;
        setEffectsOfLabels();
        setEffectsOfButtons();
    }

    private void setEffectsOfLabels() {

        equalUserMoneyLabel = userMoneyLabel;
        equalUserMoneyLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        equalUserMoneyLabel.setTextFill(Color.WHITE);
//        equalUserMoneyLabel.setText("My Money: " + LoginController.getOnlineUser().getMoney());
        equalUserMoneyLabel.setText("My Money: " + user.getMoney());
        equalSelectedCardNameLabel = selectedCardNameLabel;
        equalSelectedCardNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        equalNumbserOfShoppingCardsLabel = numbserOfShoppingCardsLabel;
        equalNumbserOfShoppingCardsLabel.setTextFill(Color.WHITE);
        equalNumbserOfShoppingCardsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        equalNumberOfUselessCardsLabel = numberOfUselessCardsLabel;
        equalNumberOfUselessCardsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        equalNumberOfUselessCardsLabel.setTextFill(Color.WHITE);
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
            equalBuybtn.setDisable(true);
//            sellbtn.setDisable(true);
//            auctionBtn.setDisable(true);
        } else {
            equalBuybtn.setDisable(false);
//            sellbtn.setDisable(false);
//            auctionBtn.setDisable(false);
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
        System.out.println("show");
        //TODO : get it from shop controller
        HashMap<String, Integer> numberOfCards = shop.getNumberOfCards(cardName);
        equalNumberOfUselessCardsLabel.setText("Useless Cards: " + numberOfCards.get("uselessCards"));
        equalNumbserOfShoppingCardsLabel.setText("Bought Cards: " + numberOfCards.get("numberOfBoughtCards"));
    }

    private void setEffectsOfBuyButtonAndShowLabel() {
        System.out.println(cardNameForBuy);
        String dataToSend = ToGsonFormatForSendInformationToClient.toGsonFormatForGetCardPriceByCardName(cardNameForBuy);

        String answerOfShop = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);

//        givenInformationDeserialized = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfShop);
//        Card card = Storage.getCardByName(cardNameForBuy);
//        int cardPrice = card.getCardPrice();
        int cardPrice = Integer.parseInt(answerOfShop);
        if (cardPrice > user.getMoney()) {
            equalSelectedCardNameLabel.setTextFill(Color.RED);
            equalSelectedCardNameLabel
                    .setText("Selected Card To Buy: " + cardNameForBuy + " , Card price: " + cardPrice);
            equalBuybtn.setDisable(true);
        } else {
            equalBuybtn.setDisable(false);
            equalSelectedCardNameLabel.setTextFill(Color.WHITE);
            equalSelectedCardNameLabel
                    .setText("Selected Card To Buy: " + cardNameForBuy + " , Card price: " + cardPrice);
        }

        String dataToSend2 = ToGsonFormatForSendInformationToClient.toGsonFormatForGetNumberOfBoughtCardsByCardName(token, cardNameForBuy);

        String answerOfShop2 = ServerConnection.sendDataToServerAndReceiveResult(dataToSend2);
        int boughtCards;
        try {
            boughtCards = Integer.parseInt(answerOfShop2);
        } catch (Exception e) {
            boughtCards = 0;
        }

//        if (boughtCards == 0) {
//            sellbtn.setDisable(true);
//            auctionBtn.setDisable(true);
//        }
//        else {
//            sellbtn.setDisable(false);
//            auctionBtn.setDisable(false);
//        }
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

        System.out.println(cardName);
        String dataToSend = ToGsonFormatForSendInformationToClient.toGsonFormatForGetCardDescriptionByCardName(cardName);

        String answerOfShop = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);
//        Card card = Storage.getCardByName(cardName);
        String cardDiscription = answerOfShop;
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
        label.setTextFill(Color.WHITE);
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
        SongPlayer.getInstance().playShortMusic("/project/ingameicons/music/buyCard.mp3");

        token = LoginController.getToken();

        String dataToSend = ToGsonFormatForSendInformationToClient.toGsonFormatForBuyCard(token, cardNameForBuy);

        String answerOfShop = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);

        givenInformationDeserialized = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfShop);

        if (givenInformationDeserialized.get("type").equals("Error")) {
            if (givenInformationDeserialized.get("message").equals("not enough money")) {
                CustomDialog customDialog = new CustomDialog("ERROR", "NOT ENOUGH MONEY");
                customDialog.openDialog();
            }
            else if (givenInformationDeserialized.get("message").equals("there is no card with this name")) {
                CustomDialog customDialog = new CustomDialog("ERROR", "INVALID CARD NAME");
                customDialog.openDialog();
            }
            else {
                CustomDialog customDialog = new CustomDialog("ERROR", "UNKNOWN ERROR!");
                customDialog.openDialog();
            }

        }
        else if (givenInformationDeserialized.get("type").equals("Successful")) {
            CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "SUCCESSFUL BUY!");
            customDialog.openDialog();
            LoginController.getOnlineUser().setMoney(Integer.parseInt(givenInformationDeserialized.get("message")));
        }
        equalUserMoneyLabel.setText("My Money: " + LoginController.getOnlineUser().getMoney());



        String dataToSend2 = ToGsonFormatForSendInformationToClient.toGsonFormatForGetCardPriceByCardName(cardNameForBuy);
        String answerOfShop2 = ServerConnection.sendDataToServerAndReceiveResult(dataToSend2);
        int cardAmount = Integer.parseInt(answerOfShop2);
        int userAmount = LoginController.getOnlineUser().getMoney();
        buybtn.setDisable(cardAmount > userAmount);

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

//    public void sellCard(ActionEvent actionEvent) {
//
//    }
//
//    public void gotoAuctionCard(ActionEvent actionEvent) {
//
//    }
}
