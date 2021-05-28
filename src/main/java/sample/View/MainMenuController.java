package sample.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainMenuController implements Initializable {

    @FXML
    Button backToLoginPagebtn;
    @FXML
    Button importAndExportbtn;
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

        // TODO
    }

    public void deckMenu() {
        // TODO
    }

    public void shopMenu() {

        // TODO
    }

    public void profileMenu() {

        // TODO
    }

    public void importAndExportMenu() {

        // TODO
    }

    public void backToLoginPage() {
        try {
            new MainView().changeView("/sample/fxml/loginPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
