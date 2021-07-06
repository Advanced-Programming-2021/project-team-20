package project.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
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

    private ImportAndExport importAndExport = new ImportAndExport();
    private static List<List<Card>> allCardsInDifferentPages;
    private static List<Rectangle> rectanglesToShowCards;
    private List<Label> allCardDiscriptionLabelsForExport;
    private List<Label> allCardDiscriptionLabelsForImport;
    private int whichPageIsShowing = 0;
    private static AnchorPane anchorPane;
    private String cardNameForExport = "";
    private static Rectangle equalExportRectangle;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        if (rectanglesToShowCards == null) {
            rectanglesToShowCards = UIStorage.getRectanglesToShowCardsInImportAndExportClass();
            addEffectsToRectanglesThatShowCards();
        }
        equalExportRectangle = exportRectangle;
        allCardDiscriptionLabelsForExport = new ArrayList<>();
        allCardDiscriptionLabelsForExport = (UIStorage.getAllCardDiscriptionLabels1());
        allCardDiscriptionLabelsForImport = new ArrayList<>();
        allCardDiscriptionLabelsForImport = (UIStorage.getAllCardDiscriptionLabels2());
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
        setEffectsOfButtons();
    }

    public void createSceneAndCardPictures(AnchorPane pane) {
        setAnchorPane(pane);
        AnchorPane backgroundPane = (AnchorPane) anchorPane.getChildren().get(1);
        List<Card> allCards = UIStorage.getAllTypeOfCards().get("allCards");
        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            Rectangle rectangle = rectanglesToShowCards.get(i);
            rectangle.setFill(new ImagePattern(allCards.get(i).getImage()));
            rectangle.setId(allCards.get(i).getCardName());
            backgroundPane.getChildren().add(rectangle);
        }
        MainView.changeScene(pane);
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

        if (cardNameForExport.equals("")) {
            exportbtn.setDisable(true);
        } else {
            exportbtn.setDisable(false);
        }
    }

    private void createPacksOfCardsForEachPage() {
        allCardsInDifferentPages = new ArrayList<>();
        List<Card> allCards = UIStorage.getAllTypeOfCards().get("allCards");
        List<Card> cardsInOnePage = new ArrayList<>();

        for (int i = 0; i < Math.floorDiv(allCards.size(), 30) + 1; i++) {
            for (int j = 0; j < rectanglesToShowCards.size(); j++) {
                if (i * 30 + j >= allCards.size()) {
                    break;
                }
                cardsInOnePage.add(allCards.get(i * 30 + j));
            }
            allCardsInDifferentPages.add(cardsInOnePage);
            cardsInOnePage = new ArrayList<>();
        }
    }

    private void addEffectsToRectanglesThatShowCards() {

        for (int i = 0; i < rectanglesToShowCards.size(); i++) {
            Rectangle rectangle = rectanglesToShowCards.get(i);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    cardNameForExport = rectangle.getId();
                    setEffectsOfButtons();
                }
            });

            rectangle.setOnMouseEntered(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
                    equalExportRectangle.setFill(rectangle.getFill());
                    equalExportRectangle.setOpacity(1);
                    addCardDescription(rectangle.getId(), true);
                    flipRectangle(equalExportRectangle);
                }
            });
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

    private void addCardDescription(String cardName, boolean isExportCard) {

        Card card = Storage.getCardByName(cardName);
        String cardDiscription = card.getCardDescription();
        ScrollPane scrollPane = null;
        if (isExportCard) {
            scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
        } else {
            scrollPane = (ScrollPane) anchorPane.getChildren().get(2);
        }
        Pane pane = null;
        if (scrollPane.getContent() == null) {
            pane = new Pane();
        } else {
            pane = (Pane) scrollPane.getContent();
        }
        pane.getChildren().clear();
        List<Label> allCardDiscriptionLabels = new ArrayList<>();
        if (isExportCard) {
            allCardDiscriptionLabels = allCardDiscriptionLabelsForExport;
        } else {
            allCardDiscriptionLabels = allCardDiscriptionLabelsForImport;
        }
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

    public void importFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CHOOSE YOUR CARD");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        String result = importAndExport.importCard(file);
        if (result.equals("this card does not exist")) {
            showAlert("THIS FILE NOT A CARD","ERROR");
            return;
        }

        importRectangle.setStroke(Color.DARKRED);
        Image image = Storage.getCardByName(result).getImage();
        importRectangle.setFill(new ImagePattern(image));
        importRectangle.setOpacity(1);
        System.out.println(result);
        addCardDescription(result, false);
        importRectangle.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
        flipRectangle(importRectangle);
    }

    public void exportFiles() {
        if (cardNameForExport.equals("")) {
            showAlert("CHOOSE A CARD", "ERROR");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("EXPORT CARD");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showSaveDialog(MainView.getStage());

        if (importAndExport.exportCard(cardNameForExport, file).equals("ERROR")) {
            showAlert("FILE CANNOT BE EXPORTED", "ERROR");
            return;
        }
        showAlert("FILE EXPORTED SUCCESSFULLY", "SUCCCESSFUL");
        cardNameForExport = "";
        setEffectsOfButtons();
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
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