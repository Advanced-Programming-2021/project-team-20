package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import project.model.cardData.General.Card;
import project.server.controller.non_duel.storage.Storage;

import java.net.URL;
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

    public void dontAllowACard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            //TODO: get card from server
            Card card = Storage.getCardByName(cardName);
            if (card == null) {
                CustomDialog customDialog = new CustomDialog("ERROR", "invalid card name");
                customDialog.openDialog();
            }
            else {
                //TODO: get from server
                boolean isAllowed = card.getIsShopAllowed();
                if (!isAllowed) {
                    CustomDialog customDialog = new CustomDialog("ERROR", "this card had been disallowed before");
                    customDialog.openDialog();
                }
                else {
                    card.setShopAllowed(false);
                    //TODO: send data to server
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "this card had been disallowed successfully");
                    customDialog.openDialog();
                }
            }
        }
    }

    public void allowACard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            //TODO: get card from server
            Card card = Storage.getCardByName(cardName);
            if (card == null) {
                CustomDialog customDialog = new CustomDialog("ERROR", "invalid card name");
                customDialog.openDialog();
            }
            else {
                //TODO: get from server
                boolean isAllowed = card.getIsShopAllowed();
                if (isAllowed) {
                    CustomDialog customDialog = new CustomDialog("ERROR", "this card had been allowed before");
                    customDialog.openDialog();
                }
                else {
                    card.setShopAllowed(true);
                    //TODO: send data to server
                    CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "this card had been allowed successfully");
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
