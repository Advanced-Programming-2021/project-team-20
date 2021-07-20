package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AuctionPageController implements Initializable {

    public Button auctionButton;
    public Button bidPriceButton;
    public TableView tableView;
    public TextField auctionCodeTextField;
    public TextField priceTextField;
    public TextField cardNameTextField;
    public TextField initialPriceTextField;
    public Button refreshButton;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        createTable();
    }

    private void createTable() {

    }

    public void auctionFunction(ActionEvent actionEvent) {
        String initialPriceAsString = initialPriceTextField.getText();
        String cardName = cardNameTextField.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!initialPriceAsString.isEmpty() && !cardName.isEmpty() && pattern.matcher(initialPriceAsString).find()) {
            int initialPrice = Integer.parseInt(initialPriceAsString);
            String token = LoginController.getToken();
            String dataToSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatAuction(token, cardName, initialPrice);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSendToServer);
        }
    }

    public void bidPriceFunction(ActionEvent actionEvent) {

    }

    public void refresh(ActionEvent actionEvent) {
        String dataToSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatRefreshAuction();
        String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSendToServer);
        System.out.println(answerOfServer);
    }


//    private static void continueCreatingTable(){
//
//    }
}
