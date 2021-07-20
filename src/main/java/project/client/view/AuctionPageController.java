package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AuctionPageController implements Initializable {

    public Button auctionButton;
    public Button bidPriceButton;
    public TableView tableView;
    public TextField auctionCodeTextField;
    public TextField priceTextField;
    public TextField cardNameTextField;
    public TextField initialPriceTextField;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        createTable();
    }

    private void createTable() {

    }

    public void auctionFunction(ActionEvent actionEvent) {

    }

    public void bidPriceFunction(ActionEvent actionEvent) {

    }


//    private static void continueCreatingTable(){
//
//    }
}
