package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import project.controller.non_duel.importAndExport.ImportAndExport;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;

public class ImportAndExportController implements Initializable {
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
    private ImportAndExport importAndExport = new ImportAndExport();
    private static List<List<Rectangle>> allCardsInDifferentPages;
    private int whichPageIsShowing = 0;
    private static AnchorPane anchorPane;
    private Rectangle chosenRectangleForExport;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        HashMap<String, Card> allCards = new HashMap<>();
        allCards.putAll(Storage.getAllMonsterCards());
        allCards.putAll(Storage.getAllSpellAndTrapCards());
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
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                Rectangle rectangle = new Rectangle(35, 45);
                rectangle.setX(290 + 50 * j);
                rectangle.setY(110 + 50 * k);
                InputStream stream = null;
                try {
                    stream = new FileInputStream("src\\main\\resources\\project\\images\\Cards\\Unknown.jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Image image = new Image(stream);
                rectangle.setFill(new ImagePattern(image));
                rectangle.setArcHeight(20);
                rectangle.setArcWidth(20);
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

    public void importFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CHOOSE YOUR CARD");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        String result = importAndExport.importCard(file);
        if (result.equals("this card does not exist")) {
            importLabel.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            importLabel.setFont(new Font(24));
            importLabel.setText("THIS FILE NOT A CARD");
            return;
        }
        String filename = file.getName().substring(0, file.getName().lastIndexOf("."));

        importRectangle.setStroke(Color.DARKRED);
        InputStream stream = null;
        try {
            if (Storage.getAllMonsterCards().containsKey(filename)) {
//stream = new FileInputStream(Storage.getAllMonsterCards().get(filename).getImagePath());
            } else {
                // stream = new FileInputStream(Storage.getAllSpellAndTrapCards().get(filename).getImagePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        importRectangle.setFill(new ImagePattern(image));
        importRectangle.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
    }

    public void exportFiles() {

        exportLabel.setFont(new Font(24.0));
        if (chosenRectangleForExport == null) {
            exportLabel.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            exportLabel.setText("CHOOSE A CARD");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("EXPORT CARD");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showSaveDialog(MainView.getStage());
        String cardRectangle = chosenRectangleForExport.getId();
        if (importAndExport.exportCard(cardRectangle, file).equals("ERROR")) {
            exportLabel.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            exportLabel.setText("FILE CANNOT BE EXPORTED");
            return;
        }
        exportLabel.setStyle("-fx-text-fill:green;-fx-padding:4 0 8 0;-fx-font-weight:bold");
        exportLabel.setText("FILE EXPORTED SUCCESSFULLY");
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        ImportAndExportController.anchorPane = anchorPane;
    }
}