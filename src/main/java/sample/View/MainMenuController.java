package sample.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
      
    }

    public void backToLoginPage() {
        try {
            new MainView().changeView("/sample/fxml/loginPage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToProfile(){
        try {
            new MainView().changeView("/sample/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    public void scoreBoardPage(){

    }

}
