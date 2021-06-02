package sample.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import sample.model.User;

public class ChangePasswordController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private Button changePasswordbtn;
    @FXML
    private Label label;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO
    }

    public void changePassword(ActionEvent actionEvent) {

        if (currentPasswordField.getText().equals("") || newPasswordField.getText().equals("")) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("FILL FIELDS");
            return;
        }
        if (!User.getOnlineUser().getPassword().equals(currentPasswordField.getText())) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("CURRENT PASSWORD IS WRONG");
            return;
        }
        if (newPasswordField.getText().equals(currentPasswordField.getText())) {
            label.setStyle("-fx-text-fill:red;-fx-padding:4 0 8 0;-fx-font-weight:bold");
            label.setText("ENTER NEW PASSWORD");
            return;
        }

        User.getOnlineUser().setPassword(newPasswordField.getText());
        try {
            new MainView().changeView("/sample/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backToProfile() {
        try {
            new MainView().changeView("/sample/fxml/profile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
