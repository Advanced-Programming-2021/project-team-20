package project.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private Circle imageCircle;
    @FXML
    private Label label;
    private ImportAndExport importAndExport = new ImportAndExport();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imageCircle.setFill(null);
    }

    public void createSceneAndCardPictures(Pane pane) {
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellTrapCards = Storage.getAllSpellAndTrapCards();
        ArrayList<Rectangle> allRectangles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rectangle = new Rectangle(20, 20);
                rectangle.setX(100 + 22 * i);
                rectangle.setY(100 + 22 * j);
                allRectangles.add(rectangle);
            }
        }
        pane.getChildren().addAll(allRectangles);
        new MainView().changeScene(pane);
    }

    public void importFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Your Card");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.csv"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Card", "*.json"));
        File file = fileChooser.showOpenDialog(MainView.getStage());
        String result = importAndExport.importCard(file);
        if (result.equals("this card does not exist")) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("THIS FILE NOT A CARD");
            return;
        }

        imageCircle.setStroke(Color.DARKRED);
        InputStream stream = null;
        try {
            stream = new FileInputStream(Storage.getAllMonsterCards().get(file.getName()).getImagePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        imageCircle.setFill(new ImagePattern(image));
        imageCircle.setEffect(new DropShadow(+25000d, 0d, +2d, Color.BLACK));
    }

    public void exportFiles() {

    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}