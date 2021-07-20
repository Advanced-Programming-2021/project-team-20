package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuctionPageController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button activateDeckbtn;
    @FXML
    private Label activatedStatusLabel;
    @FXML
    private Label deckNameLabel;
    private static List<Rectangle> allMainDeckRectangle;
    private static List<Rectangle> allSideDeckRectangle;
    private static List<Rectangle> allScrollBarRectangle;
    private static List<Rectangle> allScrollBarBackGroundRectangles;
    private static AnchorPane anchorPane;
    private static String deckname;
    private Rectangle shownCardRectangle;
    private static List<Label> allCardDiscriptionLabels;
    private static List<Label> allScrollBarLabels;
    private static Label sizeOfMainDeckLabel;
    private static Label sizeOfAllMonsterCardsLabel;
    private static Label sizeOfAllSpellCardsLabel;
    private static Label sizeOfAllTrapCardsLabel;
    private static boolean isAddedNecessaryThingsForTheFirstTime = false;
    private static Label equalActivatedStatusLabel;
    private static Button equalActivateDeckbtn;
    private static Label equalDeckNameLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/DeckMenu.mp3");
        equalActivatedStatusLabel = activatedStatusLabel;
        equalActivateDeckbtn = activateDeckbtn;
        equalDeckNameLabel = deckNameLabel;
    }



    public void auctionFunction(ActionEvent actionEvent) {
    }
}
