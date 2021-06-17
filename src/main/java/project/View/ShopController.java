package project.View;

import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import project.View.Components.CardForShow;
import project.View.Components.CardView;
import project.controller.non_duel.importAndExport.ImportAndExport;
import project.controller.non_duel.shop.Shop;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ShopController implements Initializable {
    @FXML
    private Button importbtn;
    @FXML
    private Button exportbtn;
    @FXML
    private Button backbtn;
    @FXML
    private Button previousCardsbtn;
    @FXML
    private Button nextCardsbtn;
    @FXML
    private Rectangle importRectangle;
    @FXML
    private Rectangle exportRectangle;
    @FXML
    private Label importLabel;
    @FXML
    private Label exportLabel;
//    private ImportAndExport importAndExport = new ImportAndExport();
    private static List<List<Rectangle>> allCardsInDifferentPages;
    private int whichPageIsShowing = 0;
    private static int upToWhichCardAreShown;
    private static String chosenCardName;
    private String[] names;
    static {
        upToWhichCardAreShown = 0;
    }
    private static AnchorPane anchorPane;
    private Rectangle chosenRectangleForExport;
//    private static ArrayList<String> allCardNamesFirstPage;
//    private static ArrayList<String> allCardNamesSecondPage;
//    private static ArrayList<String> allCardNamesThirdPage;
//    static {



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        HashMap<String, Card> allCards = new HashMap<>();
        allCards.putAll(Storage.getAllMonsterCards());
        allCards.putAll(Storage.getAllSpellAndTrapCards());
        System.out.println(allCards.size());
//        ArrayList<String> names = new ArrayList<>();
        names = allCards.keySet().toArray(new String[0]);
//        Collections.sort(names);
//        for (int i = 0; i < allCards.size(); i++) {
//            System.out.println(names[i].replaceAll(" ", ""));
//        }
        int sizeOfWholeCards = allCards.size();
        if (allCardsInDifferentPages == null) {
            allCardsInDifferentPages = new ArrayList<>();
            for (int i = 0; i < Math.floorDiv(sizeOfWholeCards, 64) + 1; i++) {
                allCardsInDifferentPages.add(generateRectangleCardsInOnPage(generateOnePackOfCards(allCards, (i + 1))));
            }
        }

        if (sizeOfWholeCards > allCardsInDifferentPages.size() * 64) {
            allCardsInDifferentPages = new ArrayList<>();
            for (int i = 0; i < Math.floorDiv(sizeOfWholeCards, 64) + 1; i++) {
                allCardsInDifferentPages.add(generateRectangleCardsInOnPage(generateOnePackOfCards(allCards, (i + 1))));
            }
        }
        setEffectOfpreviousAndnextCardsbtn();
    }

    private void setEffectOfpreviousAndnextCardsbtn() {
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
    }

    private List<Card> generateOnePackOfCards(HashMap<String, Card> allCards, int pageNumber) {
        List<Card> onePackOfCard = new ArrayList<>();
        int counterCard = 0;
        for (Map.Entry<String, Card> e : allCards.entrySet()) {
            if (counterCard > (pageNumber - 1) * 64) {
                onePackOfCard.add(e.getValue());
            }
            counterCard++;
            if (counterCard >= 64 * pageNumber) {
                break;
            }
        }
        return onePackOfCard;
    }

    private List<Rectangle> generateRectangleCardsInOnPage(List<Card> onePackOfCard) {
        List<Rectangle> allCardsInOnePage = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            for (int k = 0; k < 6; k++) {
                Rectangle rectangle = new Rectangle(55, 65);
                rectangle.setX(290 + 65 * j);
                rectangle.setY(110 + 65 * k);
                InputStream stream = null;
//                System.out.println(allCards.get(cardNumber).getCardName());
//                cardNumber++;
                String id = names[upToWhichCardAreShown].replaceAll(" ", "");

                try {
                    stream = new FileInputStream("src\\main\\resources\\project\\images\\Cards\\"
                    + id + ".jpg");
//                    stream = new FileInputStream("src\\main\\resources\\project\\images\\Cards\\"
//                            + allCardsInOnePage.get(cardNumber).get + ".jpg");
                } catch (Exception e) {
                    System.out.println(id);
//                    e.printStackTrace();
                }
//                System.out.println(allCards.size());
//                cardNumber++;
                //?
                assert stream != null;
                Image image = new Image(stream);
                rectangle.setFill(new ImagePattern(image));
                rectangle.setArcHeight(20);
                rectangle.setArcWidth(20);

                rectangle.setId(names[upToWhichCardAreShown]);
                if (upToWhichCardAreShown < 73) upToWhichCardAreShown++;
                //rectangle.setId("arg0");
                // rectangle.setText
                DropShadow e = new DropShadow();
                e.setWidth(6);
                e.setHeight(6);
                e.setOffsetX(4);
                e.setOffsetY(4);
                rectangle.setEffect(e);
                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        exportRectangle.setFill(rectangle.getFill());
                        exportRectangle.setOpacity(1);
                        chosenRectangleForExport = rectangle;
                        System.out.println(rectangle.getId());
                        chosenCardName = rectangle.getId();
                        System.out.println("Chosen is : " + chosenCardName);
//                        System.out.println(rectangle.toString());
//                        System.out.println("qqq");
                    }
                });
                allCardsInOnePage.add(rectangle);
            }
        }
        return allCardsInOnePage;
    }

    public void createSceneAndCardPictures(AnchorPane pane) {
        setAnchorPane(pane);
        MainView.changeScene(pane);
        for (int i = 0; i < allCardsInDifferentPages.get(whichPageIsShowing).size(); i++) {
            pane.getChildren().add(allCardsInDifferentPages.get(whichPageIsShowing).get(i));
            MainView.changeScene(pane);
            // try {
            //     Thread.sleep(500);
            // } catch (Exception e) {
            //     System.out.println("TODO: handle exception");
            // }
        }
    }

    public void nextPage() {
        anchorPane.getChildren().removeAll(allCardsInDifferentPages.get(whichPageIsShowing));
        whichPageIsShowing++;
        setEffectOfpreviousAndnextCardsbtn();
        anchorPane.getChildren().addAll(allCardsInDifferentPages.get(whichPageIsShowing));
    }

    public void previousPage() {
        anchorPane.getChildren().removeAll(allCardsInDifferentPages.get(whichPageIsShowing));
        whichPageIsShowing--;
        setEffectOfpreviousAndnextCardsbtn();
        anchorPane.getChildren().addAll(allCardsInDifferentPages.get(whichPageIsShowing));
    }


    public void buyCard() {
        System.out.println("Trying to buy");
        if (chosenCardName == null) {
            System.out.println("Please choose a card first!");
        }
        else {
            exportLabel.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            String answerOfShop = new Shop().findCommand("shop buy " + chosenCardName);
            exportLabel.setText(answerOfShop);
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
