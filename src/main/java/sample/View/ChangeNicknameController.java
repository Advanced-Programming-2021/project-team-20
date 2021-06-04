package sample.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.controller.non_duel.storage.Storage;
import sample.model.User;

public class ChangeNicknameController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button changeNicknamebtn;
    @FXML
    private Label label;
    @FXML
    private TextField newNicknameField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //
    }

    public void changeNickname(ActionEvent actionEvent) {
        if (newNicknameField.getText().equals("")) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("FILL FIELDS");
            return;
        }
        if (doesUserWithThisNicknameAlreadyExists()) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("NEW NICKNAME IS REPEATED");
            return;
        }

        Storage.getAllUsers().get(0).setNickname(newNicknameField.getText());
        try {
            new MainView().changeView("/sample/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doesUserWithThisNicknameAlreadyExists() {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getNickname().equals(newNicknameField.getText()))
                return true;
        }
        return false;
    }

    public void backToProfile(){
        try {
            new MainView().changeView("/sample/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
