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
    private static AnchorPane anchorPane1;
    private Rectangle chosenRectangleForExport;
    private static HashMap<String, Card> allCards;
    private static int cardNumber;
    static {
        allCards = new HashMap<>();
        cardNumber = 0;
    }
//    private static ArrayList<String> allCardNamesFirstPage;
//    private static ArrayList<String> allCardNamesSecondPage;
//    private static ArrayList<String> allCardNamesThirdPage;
//    static {
//        String staticString = "src\\main\\resources\\project\\images\\Cards\\";
//        allCardNamesFirstPage = new ArrayList<>();
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//        allCardNamesFirstPage.add(staticString + "");
//    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
//        HashMap<String, Card> allCards = new HashMap<>();
        allCards.putAll(Storage.getAllMonsterCards());
        allCards.putAll(Storage.getAllSpellAndTrapCards());
        System.out.println(allCards.size());
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
//                System.out.println(allCards.get(cardNumber).getCardName());
//                cardNumber++;
                try {
                    stream = new FileInputStream("src\\main\\resources\\project\\images\\Cards\\Unknown.jpg");
//                    stream = new FileInputStream("src\\main\\resources\\project\\images\\Cards\\"
//                            + allCardsInOnePage.get(cardNumber).get + ".jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(allCards.size());
//                cardNumber++;
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
        anchorPane1.getChildren().removeAll(allCardsInDifferentPages.get(whichPageIsShowing));
        whichPageIsShowing++;
        setEffectOfpreviousAndnextCardsbtn();
        anchorPane1.getChildren().addAll(allCardsInDifferentPages.get(whichPageIsShowing));
    }

    public void previousPage() {
        anchorPane1.getChildren().removeAll(allCardsInDifferentPages.get(whichPageIsShowing));
        whichPageIsShowing--;
        setEffectOfpreviousAndnextCardsbtn();
        anchorPane1.getChildren().addAll(allCardsInDifferentPages.get(whichPageIsShowing));
    }

    public void importFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("CHOOSE YOUR CARD");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        String result = importCard(file);
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
                stream = new FileInputStream(Storage.getAllMonsterCards().get(filename).getImagePath());
            } else {
                stream = new FileInputStream(Storage.getAllSpellAndTrapCards().get(filename).getImagePath());
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
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showSaveDialog(MainView.getStage());
        String cardRectangle = chosenRectangleForExport.getId();
        if (exportCard(cardRectangle, file).equals("ERROR")) {
            exportLabel.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            exportLabel.setText("FILE CANNOT BE EXPORTED");
            return;
        }
        exportLabel.setStyle("-fx-text-fill:green;-fx-padding:4 0 8 0;-fx-font-weight:bold");
        exportLabel.setText("FILE EXPORTED SUCCESSFULLY");
    }

    public String importCard(File chosenFile) {

        if (!Storage.doesCardExist(chosenFile.getName().substring(0, chosenFile.getName().lastIndexOf(".")))) {
            return "this card does not exist";
        }
        String fileType = chosenFile.getName().substring(chosenFile.getName().lastIndexOf("."),
                chosenFile.getName().length());

        if (fileType.equals(".json")) {
            if (checkValidJsonCard(chosenFile)) {
                return "card exists";
            } else {
                return "this card does not exist";
            }
        } else if (fileType.equals(".csv")) {
            if (checkValidCSVFormat(chosenFile)) {
                return "card exists";
            } else {
                return "this card does not exist";
            }
        }
        return "this card does not exist";
    }

    private boolean checkValidCSVFormat(File file) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();

        List<List<String>> validInfornationOfCard = new ArrayList<>();
        if (allMonsterCards.containsKey(file.getName())) {
            validInfornationOfCard = toCSVFormatMonsterCard(allMonsterCards.get(file.getName()));
        } else {
            validInfornationOfCard = toCSVFormatSpellTrapCard(allSpellAndTrapCards.get(file.getName()));
        }

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvFileInformation = csvReader.readAll();
            csvReader.close();

            for (int i = 0; i < csvFileInformation.size(); i++) {
                for (int j = 0; j < csvFileInformation.get(i).length; j++) {
                    if (!csvFileInformation.get(i)[j].equals(validInfornationOfCard.get(i).get(j))) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean checkValidJsonCard(File file) {
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        StringBuilder fileInputs = new StringBuilder();

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                fileInputs.append(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fileInputs.toString().equals(toJsonFormatMonsterCard(allMonsterCards.get(file.getName())))) {
            return true;
        }

        if (fileInputs.toString().equals(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(file.getName())))) {
            return true;
        }

        return false;
    }


    public String exportCard(String cardname, File file) {

        String fileType = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        System.out.println(file.getAbsolutePath() + " " + cardname);
        if (fileType.equals(".json")) {
            if (writeFileInJsonFormat(cardname, file)) {
                return "card exported successfully";
            } else {
                return "ERROR";
            }
        } else if (fileType.equals(".csv")) {
            if (writeFileInCSVFormat(cardname, file)) {
                return "card exported successfully";
            } else {
                return "ERROR";
            }
        }
        return "ERROR";
    }

    private boolean writeFileInCSVFormat(String cardname, File file) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardname)) {
            FileWriter csvWriter;
            try {
                csvWriter = new FileWriter(file.getAbsolutePath());
                List<List<String>> wholeCSVFile = toCSVFormatMonsterCard(allMonsterCards.get(cardname));
                for (List<String> rowData : wholeCSVFile) {
                    csvWriter.append(String.join(",", rowData));
                    csvWriter.append("\n");
                }
                csvWriter.flush();
                csvWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            FileWriter csvWriter;
            try {
                csvWriter = new FileWriter(file.getAbsolutePath());
                List<List<String>> wholeCSVFile = toCSVFormatSpellTrapCard(allSpellAndTrapCards.get(cardname));
                for (List<String> rowData : wholeCSVFile) {
                    csvWriter.append(String.join(",", rowData));
                    csvWriter.append("\n");
                }
                csvWriter.flush();
                csvWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private boolean writeFileInJsonFormat(String cardname, File file) {
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file.getAbsolutePath());
                fileWriter.write(toJsonFormatMonsterCard(allMonsterCards.get(cardname)));
                fileWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file.getAbsolutePath());
                fileWriter.write(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(cardname)));
                fileWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private List<List<String>> toCSVFormatMonsterCard(Card card) {

        List<String> firstRowVariables = new ArrayList<>();
        firstRowVariables.add("Name");
        firstRowVariables.add("Type");
        firstRowVariables.add("Icon (Property)");
        firstRowVariables.add("Description");
        firstRowVariables.add("Status");
        firstRowVariables.add("Price");

        List<String> secondRowVariables = new ArrayList<>();
        secondRowVariables.add(card.getCardName());
        secondRowVariables.add(card.getCardType() + "");
        if (card.getCardType().equals(CardType.TRAP)) {
            TrapCard trapCard = (TrapCard) card;
            secondRowVariables.add(trapCard.getTrapCardValue() + "");
        } else {
            SpellCard spellCard = (SpellCard) card;
            secondRowVariables.add(spellCard.getSpellCardValue() + "");
        }
        secondRowVariables.add(card.getCardDescription());
        secondRowVariables.add(card.getNumberOfAllowedUsages() == 3 ? "Unlimited" : "Limited");
        secondRowVariables.add(card.getCardPrice() + "");

        List<List<String>> wholeCSVFile = new ArrayList<>();
        wholeCSVFile.add(firstRowVariables);
        wholeCSVFile.add(secondRowVariables);

        return wholeCSVFile;
    }

    private List<List<String>> toCSVFormatSpellTrapCard(Card card) {

        MonsterCard monsterCard = (MonsterCard) card;

        List<String> firstRowVariables = new ArrayList<>();
        firstRowVariables.add("Name");
        firstRowVariables.add("Level");
        firstRowVariables.add("Attribute");
        firstRowVariables.add("Monster Type");
        firstRowVariables.add("Card Type");
        firstRowVariables.add("Atk");
        firstRowVariables.add("Def");
        firstRowVariables.add("Description");
        firstRowVariables.add("Price");

        List<String> secondRowVariables = new ArrayList<>();
        secondRowVariables.add(monsterCard.getCardName());
        secondRowVariables.add(monsterCard.getLevel() + "");
        secondRowVariables.add(monsterCard.getMonsterCardAttribute() + "");
        secondRowVariables.add(monsterCard.getMonsterCardFamily() + "");
        secondRowVariables.add(monsterCard.getMonsterCardValue() + "");
        secondRowVariables.add(monsterCard.getAttackPower() + "");
        secondRowVariables.add(monsterCard.getDefensePower() + "");
        secondRowVariables.add(monsterCard.getCardDescription());
        secondRowVariables.add(monsterCard.getCardPrice() + "");

        List<List<String>> wholeCSVFile = new ArrayList<>();
        wholeCSVFile.add(firstRowVariables);
        wholeCSVFile.add(secondRowVariables);

        return wholeCSVFile;
    }

    private String toJsonFormatMonsterCard(Card card) {

        MonsterCard monsterCard = (MonsterCard) card;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", monsterCard.getCardName());
        jsonObject.addProperty("Level", monsterCard.getLevel());
        jsonObject.addProperty("Attribute", monsterCard.getMonsterCardAttribute().toString());
        jsonObject.addProperty("Monster Type", monsterCard.getMonsterCardFamily().toString());
        jsonObject.addProperty("Card Type", monsterCard.getMonsterCardValue().toString());
        jsonObject.addProperty("Attack Power", monsterCard.getAttackPower());
        jsonObject.addProperty("Defence Power", monsterCard.getDefensePower());
        jsonObject.addProperty("Description", monsterCard.getCardDescription());
        jsonObject.addProperty("Price", monsterCard.getCardPrice());
        return jsonObject.toString();

    }

    private String toJsonFormatSpellAndTrapCard(Card card) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", card.getCardName());
        jsonObject.addProperty("Card Type", card.getCardType().toString());
        if (card.getCardType().equals(CardType.TRAP)) {
            TrapCard trapCard = (TrapCard) card;
            jsonObject.addProperty("Card Property", trapCard.getTrapCardValue().toString());
        } else {
            SpellCard spellCard = (SpellCard) card;
            jsonObject.addProperty("Card Property", spellCard.getSpellCardValue().toString());
        }
        jsonObject.addProperty("Description", card.getCardDescription());
        jsonObject.addProperty("Status", card.getNumberOfAllowedUsages() == 3 ? "Unlimited" : "Limited");
        jsonObject.addProperty("Price", card.getCardPrice());
        return jsonObject.toString();
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAnchorPane(AnchorPane anchorPane1) {
        ShopController.anchorPane1 = anchorPane1;
    }

}
