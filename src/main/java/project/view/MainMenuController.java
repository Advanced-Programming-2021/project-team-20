package project.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.print.attribute.standard.Media;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MainMenuController implements Initializable {

    @FXML
    Button backToLoginPagebtn;
    @FXML
    Button importAndExportbtn;
    @FXML
    Button scoreboardBtn;
    @FXML
    Button profilebtn;
    @FXML
    Button shopbtn;
    @FXML
    Button deckbtn;
    @FXML
    Button duelbtn;
    public static MediaPlayer backgroundMusic;
    public MediaView mediaView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        prepareBackgroundMusic();
    }

    public void prepareBackgroundMusic() {
        URL resource = getClass().getResource("/project/music/my-music.mp3");
        backgroundMusic = new MediaPlayer(new Media(resource.toString()));
        mediaView.setMediaPlayer(backgroundMusic);
        backgroundMusic.setAutoPlay(true);
        backgroundMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusic.seek(Duration.ZERO);
            }
        });
        backgroundMusic.play();
    }

    public void duelMenu() {
        try {
            new MainView().changeView("/project/fxml/startDuelPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToDeckMenu() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/wholeDecksPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new WholeDeckPageMenuController().showPage(pane);
    }

    public void shopMenu() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/shopPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ShopController().createSceneAndCardPictures(pane);
    }

    public void goToImportAndExportMenu() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/importAndExport.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ImportAndExportController().createSceneAndCardPictures(pane);
    }

    public void backToLoginPage() {
        try {
            new MainView().changeView("/project/fxml/loginPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToProfile() {
        try {
            new MainView().changeView("/project/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scoreBoardPage() {
        try {
            new MainView().changeView("/project/fxml/scoreboardPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void example(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/cardCreatorPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
