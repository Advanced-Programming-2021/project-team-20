package project.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    public void deckMenu() {
        System.out.println("deck");
        // TODO
    }

    public void shopMenu() {
        System.out.println("shop");
        // TODO
    }

    public void importAndExportMenu() {
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

    }

}
