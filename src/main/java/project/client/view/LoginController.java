package project.client.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.gson.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.pooyaviewpackage.DuelView;
import project.model.Deck;
import project.model.User;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField usernameFieldForRegister;
    @FXML
    private TextField nickNameFieldForRegister;
    @FXML
    private PasswordField passwordFieldfORegister;

    private static User onlineUser;
    private static String token;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        SongPlayer.getInstance().pauseMusic();
        SongPlayer.getInstance().prepareBackgroundMusic("/project/ingameicons/music/opening.mp3");
    }

    public void loginUser() {
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            passwordField.setText("");
            usernameField.setText("");
        }
        String data = ToGsonFormatToSendDataToServer.toGsonFormatLogin(usernameField.getText(),
                passwordField.getText());
        String result = (String) ServerConnection.sendDataToServerAndReceiveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeLogin(result);
        if (deserializeResult.get("type").equals("Error")) {
            showAlert(deserializeResult.get("message"), "Error");
            return;
        }
        token = deserializeResult.get("token");
        DuelView.setToken(token);
        createUser(deserializeResult);
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    public void signUpUser() {
        if (usernameFieldForRegister.getText().equals("") || nickNameFieldForRegister.getText().equals("")
                || passwordFieldfORegister.getText().equals("")) {
            showAlert("FILL FIELDS", "ERROR");
            return;
        }
        String data = ToGsonFormatToSendDataToServer.toGsonFormatRegister(usernameFieldForRegister.getText(),
                nickNameFieldForRegister.getText(), passwordFieldfORegister.getText());
        String result = (String) ServerConnection.sendDataToServerAndReceiveResult(data);
        HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeRegister(result);
        if (deserializeResult.get(DeserializeInformationFromServer.getType())
                .equals(DeserializeInformationFromServer.getError())) {
            showAlert(deserializeResult.get(DeserializeInformationFromServer.getMessage()),
                    DeserializeInformationFromServer.getError());
        } else {
            token = deserializeResult.get("token");
            CustomDialog customDialog = new CustomDialog(DeserializeInformationFromServer.getSuccess(),
                    deserializeResult.get(DeserializeInformationFromServer.getMessage()), "mainMenu");
            customDialog.openDialog();
            // onlineUser = new User(usernameFieldForRegister.getText(),
            // nickNameFieldForRegister.getText(),
            // passwordFieldfORegister.getText(), "");
            // onlineUser.setImage(createImageAlaki());
        }

        usernameFieldForRegister.setText("");
        passwordFieldfORegister.setText("");
        nickNameFieldForRegister.setText("");
    }

    private Image createImageAlaki() {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\images\\userLabel.jpg");
            return new Image(stream);
        } catch (Exception e) {
            System.out.println("exception in createImageAlaki");
        }
        return null;
    }

    private void createUser(HashMap<String, String> deserializeResult) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(deserializeResult.get("userInformation"));
        User user = null;
        if (jsonElement.isJsonObject()) {
            JsonObject detailes = jsonElement.getAsJsonObject();
            user = new User(detailes.get("name").getAsString(), detailes.get("nickname").getAsString(),
                    detailes.get("password").getAsString(), "");
            user.setMoney(detailes.get("money").getAsInt());
            user.setScore(detailes.get("score").getAsInt());
            user.setImage(createImageAlaki());
        }

        jsonElement = jsonParser.parse(deserializeResult.get("wholeDeck"));
        if (jsonElement.isJsonObject()) {
            JsonObject details = jsonElement.getAsJsonObject();
            JsonArray allDecks = details.getAsJsonArray("decks");
            Deck deck;

            if (allDecks != null) {
                for (int i = 0; i < allDecks.size(); i++) {
                    JsonObject getDeckFromJsonFromat = allDecks.get(i).getAsJsonObject();
                    deck = addDecksToUser(getDeckFromJsonFromat);
                    user.addDeckToAllDecks(getDeckFromJsonFromat.get("deckname").getAsString(), deck);
                }
            }

            JsonArray allUselessCards = details.getAsJsonArray("uselessCards");
            for (JsonElement jsonElement1 : allUselessCards) {
                user.addCardToAllUselessCards(jsonElement1.getAsString());
            }
        }
        onlineUser = user;
    }

    private Deck addDecksToUser(JsonObject getDeckFromJsonFromat) {

        Deck deck = new Deck(getDeckFromJsonFromat.get("deckname").getAsString());
        deck.setDeckActive(getDeckFromJsonFromat.get("isActivated").getAsBoolean());

        JsonArray mainDeck = getDeckFromJsonFromat.getAsJsonArray("mainDeck");
        for (JsonElement jsonElement : mainDeck) {
            deck.addCardToMainDeck(jsonElement.getAsString());
        }

        JsonArray sideDeck = getDeckFromJsonFromat.getAsJsonArray("sideDeck");
        for (JsonElement jsonElement : sideDeck) {
            deck.addCardToSideDeck(jsonElement.getAsString());
        }
        return deck;
    }

    public static void setOnlineUser(User onlineUser) {
        LoginController.onlineUser = onlineUser;
    }

    public static User getOnlineUser() {
        return onlineUser;
    }

    public static String getToken() {
        return token;
    }
}
