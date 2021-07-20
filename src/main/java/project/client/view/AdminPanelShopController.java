package project.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;

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
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void increaseCard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            String dataToSend = ToGsonFormatToSendDataToServer.toGsonFormatForGetDataIncreaseCardAdminPanelShop(cardName);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);
            HashMap<String, String> deserializaedInformation = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfServer);

            String type = deserializaedInformation.get("type");
            String message = deserializaedInformation.get("message");

            if (type.equals("ERROR")) {
                CustomDialog customDialog;
                if (message.equals("INVALID CARD")) {
                    customDialog = new CustomDialog("ERROR", "invalid card name");
                }
                else if (message.equals("ADMIN ERROR")) {
                    customDialog = new CustomDialog("ERROR", "you are not admin");
                }
                else if (message.equals("NUMBER OF CARDS IN SHOP IS 0")) {
                    customDialog = new CustomDialog("ERROR", "NUMBER OF CARDS IN SHOP IS 0");
                }
                else {
                    customDialog = new CustomDialog("ERROR", "UNKNOWN ERROR");
                }
                customDialog.openDialog();

            }
            else if (type.equals("SUCCESSFUL")){
                CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "successfully increased");
                customDialog.openDialog();
            }
            else {
                CustomDialog customDialog = new CustomDialog("UNKNOWN", "UNKNOWN");
                customDialog.openDialog();
            }
        }
    }

    public void decreaseCard(ActionEvent actionEvent) {
        if (!textField.getText().isEmpty()) {
            String cardName = textField.getText();
            String dataToSend = ToGsonFormatToSendDataToServer.toGsonFormatForGetDataDecreaseCardAdminPanelShop(cardName);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSend);
            HashMap<String, String> deserializaedInformation = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(answerOfServer);

            String type = deserializaedInformation.get("type");
            String message = deserializaedInformation.get("message");

            if (type.equals("ERROR")) {
                CustomDialog customDialog;
                if (message.equals("INVALID CARD")) {
                    customDialog = new CustomDialog("ERROR", "invalid card name");
                }
                else if (message.equals("NUMBER OF CARDS IN SHOP IS 0")) {
                    customDialog = new CustomDialog("ERROR", "NUMBER OF CARDS IN SHOP IS 0");
                }
                else {
                    customDialog = new CustomDialog("ERROR", "UNKNOWN ERROR");
                }
                customDialog.openDialog();

            } else if (type.equals("SUCCESSFUL")) {
                CustomDialog customDialog = new CustomDialog("SUCCESSFUL", "successfully decreased");
                customDialog.openDialog();
            } else {
                CustomDialog customDialog = new CustomDialog("UNKNOWN", "UNKNOWN");
                customDialog.openDialog();
            }
        }
    }

}
