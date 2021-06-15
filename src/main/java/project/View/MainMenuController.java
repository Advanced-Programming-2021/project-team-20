package project.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO
    }

    public void duelMenu() {
        System.out.println("duel");
        // TODO
    }

    public void goToDeckMenu() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/wholeDecksPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new wholeDeckPageMenuController().showPage(pane);
    }

    public void shopMenu() {
        try {
            new MainView().changeView("/project/fxml/shopPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
