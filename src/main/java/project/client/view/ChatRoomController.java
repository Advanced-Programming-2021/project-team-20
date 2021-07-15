package project.client.view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private int lastIdOfTweetReceived = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Pane pane = new Pane();
        messageHolderScrollPane.setContent(pane);
        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatToGetTweetsById(lastIdOfTweetReceived);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        showMessages(messageFromServer);
    }

    private void showMessages(String messageFromServer) {
//        JsonParser jsonParser = new JsonParser();
//        JsonElement jsonElement = jsonParser.parse(messageFromServer);
//        JsonObject details = jsonElement.getAsJsonObject();
//        JsonArray newTweets = details.getAsJsonArray("newTweets");
//        for (int i = 0; i < newTweets.size(); i++) {
//            JsonObject jsonObject = newTweets.get(i).getAsJsonObject();
//            System.out.println(jsonObject.get("message") + " " + jsonObject.get("id").getAsInt());
//        }
        System.out.println(messageFromServer);
    }

    public void backToMainMenu() {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTweet() {
        String message = textArea.getText();
        textArea.setText("");

        String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatSendTweet(message, lastIdOfTweetReceived);
        String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
        HashMap<String, String> deserilizeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);

    }
}
