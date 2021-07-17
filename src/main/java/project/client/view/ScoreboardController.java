package project.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.Components.Person;
import project.client.view.Components.PersonForOnlineUsers;

import java.io.IOException;
import java.util.Arrays;

public class ScoreboardController {

    @FXML
    private TableView tableViewForOnlineUsers;
    @FXML
    private Button fillButton;
    @FXML
    private Button returnButton;
    @FXML
    private TableView<Person> tableView;
    @FXML
    private TableColumn<Person, Integer> rankingColumn;
    @FXML
    private TableColumn<Person, String> usernameColumn;
    @FXML
    private TableColumn<Person, Integer> scoreColumn;
    private TableColumn<Integer, String> calltypel;

    public void fillLabel() {

//        String allPeople = new Scoreboard().findCommands("scoreboard show");
        String message = ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformation();
        String allPeople = ServerConnection.sendDataToServerAndReceiveResult(message);
        String[] allPeopleSplited = allPeople.split(",");
        Person[] person = new Person[allPeopleSplited.length/3];
        for (int i = 0; i < person.length; i++) {
            int ranking = Integer.parseInt(allPeopleSplited[i*3]);
            String nickname = allPeopleSplited[i*3 + 1];
            int score = Integer.parseInt(allPeopleSplited[i*3 + 2]);
            person[i] = new Person(ranking, nickname, score);
        }
        if (person.length > 20) {
            person = Arrays.copyOfRange(person, 0, 19);
        }

        final ObservableList<Person> data = FXCollections.observableArrayList(
            person
        );
        TableColumn<Person, Integer> rankingColumn = new TableColumn<>("RANKING");
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        rankingColumn.setStyle( "-fx-alignment: CENTER;");
        rankingColumn.setMinWidth(97);


        TableColumn<Person, String> usernameColumn = new TableColumn<>("NICKNAME");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        usernameColumn.setStyle( "-fx-alignment: CENTER;");
        usernameColumn.setMinWidth(400);

        TableColumn<Person, Integer> scoreColumn = new TableColumn<>("SCORE");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setStyle( "-fx-alignment: CENTER;");
        scoreColumn.setMinWidth(100);
        ObservableList<String> list = FXCollections.observableArrayList();

        tableView.setItems(data);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getColumns().addAll(rankingColumn, usernameColumn, scoreColumn);

        String userNickname = LoginController.getOnlineUser().getNickname();
        customiseFactory((TableColumn<Person, String>) tableView.getColumns().get(1), userNickname);


        //Show OnlineUsers
        String messageForOnlineUsers = ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformationOfONlineUsers();
        String allPeoplemessageForOnlineUsers = ServerConnection.sendDataToServerAndReceiveResult(messageForOnlineUsers);
        String[] allPeopleSplitedmessageForOnlineUsers = allPeoplemessageForOnlineUsers.split(",");
        PersonForOnlineUsers[] personmessageForOnlineUsers = new PersonForOnlineUsers[allPeopleSplitedmessageForOnlineUsers.length];
        for (int i = 0; i < personmessageForOnlineUsers.length; i++) {
            String nickname = allPeopleSplitedmessageForOnlineUsers[i];
            personmessageForOnlineUsers[i] = new PersonForOnlineUsers(nickname);
        }

        final ObservableList<PersonForOnlineUsers> datamessageForOnlineUsers = FXCollections.observableArrayList(
            personmessageForOnlineUsers
        );


        TableColumn<Person, String> usernameColumnmessageForOnlineUsers = new TableColumn<>("NICKNAME OF ONLINE USERS");
        usernameColumnmessageForOnlineUsers.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        usernameColumnmessageForOnlineUsers.setStyle( "-fx-alignment: CENTER;");
        usernameColumnmessageForOnlineUsers.setMinWidth(600);

        ObservableList<String> listmessageForOnlineUsers = FXCollections.observableArrayList();

        tableViewForOnlineUsers.setItems(datamessageForOnlineUsers);
        tableViewForOnlineUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewForOnlineUsers.getColumns().addAll(usernameColumnmessageForOnlineUsers);
    }
    public void returnToMainMenu(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customiseFactory(TableColumn<Person, String> calltypel, String nickname) {
        calltypel.setCellFactory(column -> {
            return new TableCell<Person, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Person> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if(item.equals(nickname))
                            currentRow.setStyle("-fx-background-color:lightcoral");
                        else
                            currentRow.setStyle("-fx-background-color:lightgreen");
                    }
                }
            };
        });
    }

    public void goToTelevision(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/television.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
