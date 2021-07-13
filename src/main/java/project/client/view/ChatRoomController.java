package project.client.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.gson.JsonObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;

public class ChatRoomController implements Initializable {

    @FXML
    private ScrollPane messageHolderScrollPane;
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
         Pane pane = new Pane();
         messageHolderScrollPane.setContent(pane);
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTweet(){
        String message = textArea.getText();
        textArea.setText("");

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("sendTweet", "message", message);
        String messageFromServer = ServerConnection.sendDataToServerAndRecieveResult(dataSendToServer);
        HashMap<String, String> deserilizeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);

    }
}
