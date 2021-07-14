package project.client.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import project.client.ServerConnection;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

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
    @FXML
    Button chatRoombtn;

    public static MediaPlayer backgroundMusic;
    public MediaView mediaView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/MainMenu.mp3");
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

    public void goToChatRoomPage() {
        try {
            new MainView().changeView("/project/fxml/chatRoomPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToLoginPage() {
        String logout = "{\"type\":\"logout\",\"token\":\"" + LoginController.getToken() + "\"}";
        ServerConnection.sendDataToServerAndReceiveResult(logout);
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
