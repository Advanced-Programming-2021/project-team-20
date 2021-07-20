package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.model.cardData.General.Card;
import project.server.controller.non_duel.storage.Storage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminPanelShopController implements Initializable {

    public TextField textField;
    public Button disAllowButton;
    public Button allowButton;
    public Button backButton;
    public Button increaseButton;
    public Button decreaseButton;
    public Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setStyle("-fx-background-color: #fffefe");
    }

    public void allowACard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            String dataToSend = ToGsonFormatToSendDataToServer.toGsonFormatForGetDataAllowCardAdminPanelShop(cardName);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);
            HashMap<String, String> deserializedAnswer = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfServer);
            String type = deserializedAnswer.get("type");
            String message = deserializedAnswer.get("message");
            if (type.equals("ERROR")) {
                CustomDialog customDialog;
                if (message.equals("invalid card name")) {
                    customDialog = new CustomDialog("ERROR", "invalid card name");
                }
                else if (message.equals("NOT ADMIN")) {
                    customDialog = new CustomDialog("ERROR", "YOU ARE NOT ADMIN");
                }
                else {
                    customDialog = new CustomDialog("ERROR", "UNKNOWN ERROR");
                }
                customDialog.openDialog();
            }
            else {
                if (message.equals("Before")) {
                    CustomDialog customDialog = new CustomDialog("ERROR", "this card had been allowed before");
                    customDialog.openDialog();
                }
                else if (message.equals("Now")){
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "this card had been allowed successfully");
                    customDialog.openDialog();
                }
            }

        }
    }

    public void disallowACard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            String dataToSend = ToGsonFormatToSendDataToServer.toGsonFormatForGetDataDisallowCardAdminPanelShop(cardName);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);
            HashMap<String, String> deserializedAnswer = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfServer);
            String type = deserializedAnswer.get("type");
            String message = deserializedAnswer.get("message");
            if (type.equals("ERROR")) {
                CustomDialog customDialog;
                if (message.equals("invalid card name")) {
                    customDialog = new CustomDialog("ERROR", "invalid card name");
                }
                else if (message.equals("NOT ADMIN")) {
                    customDialog = new CustomDialog("ERROR", "YOU ARE NOT ADMIN");
                }
                else {
                    customDialog = new CustomDialog("ERROR", "UNKNOWN ERROR");
                }
                customDialog.openDialog();
            }
            else {
                if (message.equals("Before")) {
                    CustomDialog customDialog = new CustomDialog("ERROR", "this card had been disallowed before");
                    customDialog.openDialog();
                }
                else if (message.equals("Now")){
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "this card had been disallowed successfully");
                    customDialog.openDialog();
                }
            }
        }
    }

    public void back(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/shopPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void increaseCard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            //TODO: get card from server
            Card card = Storage.getCardByName(cardName);
            if (card == null) {
                CustomDialog customDialog = new CustomDialog("ERROR", "invalid card name");
                customDialog.openDialog();
            }
            else {
                //TODO: send data from server
                card.increaseNumberOfCardsInShop();
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "number increased successfully");
                customDialog.openDialog();
            }
        }
    }

    public void decreaseCard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            //TODO: get card from server
            Card card = Storage.getCardByName(cardName);
            if (card == null) {
                CustomDialog customDialog = new CustomDialog("ERROR", "invalid card name");
                customDialog.openDialog();
            }
            else {
                //TODO: send data from server
                if (card.getNumberOfCardsInShop() == 0) {
                    CustomDialog customDialog = new CustomDialog("ERROR", "number of cards in shop is 0");
                    customDialog.openDialog();
                }
                else {
                    //TODO: server
                    card.decreaseNumberOfCardsInShop();
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "number decreased successfully");
                    customDialog.openDialog();
                }
            }
        }
    }
}
