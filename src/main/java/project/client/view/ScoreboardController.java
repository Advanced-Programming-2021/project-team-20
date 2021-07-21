package project.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import project.client.ServerConnection;
import project.client.view.Components.Person;
import project.client.view.Components.PersonForOnlineUsers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ScoreboardController implements Initializable {

    public AnchorPane anchor1;
    public TableView tableViewForOnlineUsers2;
    public TableView tableView2;
    public static TableView staticTable;
    public static TableColumn<Person, Integer> rankingColumn;
    public static TableColumn<Person, String> usernameColumn;
    public static TableColumn<Person, Integer> scoreColumn;
    public static TableColumn<Person, String> usernameColumnmessageForOnlineUsers;
    static {
        rankingColumn = new TableColumn<>("RANKING");
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        rankingColumn.setStyle("-fx-alignment: CENTER;");
        rankingColumn.setMinWidth(97);


        usernameColumn = new TableColumn<>("NICKNAME");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        usernameColumn.setStyle("-fx-alignment: CENTER;");
        usernameColumn.setMinWidth(400);

        scoreColumn = new TableColumn<>("SCORE");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setStyle("-fx-alignment: CENTER;");
        scoreColumn.setMinWidth(100);

        usernameColumnmessageForOnlineUsers = new TableColumn<>("NICKNAME OF ONLINE USERS");
        usernameColumnmessageForOnlineUsers.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        usernameColumnmessageForOnlineUsers.setStyle("-fx-alignment: CENTER;");
        usernameColumnmessageForOnlineUsers.setMinWidth(600);
    }
//    private TableView tableView;
//    private TableView tableViewForOnlineUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticTable = tableView2;
        fillLabel();
    }

    public void fillLabel() {
        ServerConnection.scoreboardAutoRefresh(this);
    }

    public void fillLabelAutomatically(String answer1, String answer2) {
//        String message = ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformation();
//        anchor1.getChildren().removeAll(tableView2, tableViewForOnlineUsers2);

        staticTable.getItems().clear();
        tableViewForOnlineUsers2.getItems().clear();
//        tableView2.getItems().clear();
//        tableViewForOnlineUsers2.getItems().clear();

//        tableViewForOnlineUsers2 = new TableView();
//        tableView2 = new TableView<>();

        String allPeople = answer1;
        System.out.println(answer1);
        String[] allPeopleSplited = allPeople.split(",");
        Person[] person = new Person[allPeopleSplited.length / 3];
        for (int i = 0; i < person.length; i++) {
            int ranking = Integer.parseInt(allPeopleSplited[i * 3]);
            String nickname = allPeopleSplited[i * 3 + 1];
            int score = Integer.parseInt(allPeopleSplited[i * 3 + 2]);
            person[i] = new Person(ranking, nickname, score);
        }
        if (person.length > 20) {
            person = Arrays.copyOfRange(person, 0, 19);
        }

        final ObservableList<Person> data = FXCollections.observableArrayList(
            person
        );

        ObservableList<String> list = FXCollections.observableArrayList();

        staticTable.setItems(data);
        staticTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (!staticTable.getColumns().contains(rankingColumn)) {
            staticTable.getColumns().addAll(rankingColumn, usernameColumn, scoreColumn);
        }


        String userNickname = LoginController.getOnlineUser().getNickname();
        customiseFactory((TableColumn<Person, String>) staticTable.getColumns().get(1), userNickname);


        //Show OnlineUsers
//        String messageForOnlineUsers = ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformationOfONlineUsers();
        String allPeoplemessageForOnlineUsers = answer2;
        System.out.println(answer2);
        String[] allPeopleSplitedmessageForOnlineUsers = allPeoplemessageForOnlineUsers.split(",");
        PersonForOnlineUsers[] personmessageForOnlineUsers = new PersonForOnlineUsers[allPeopleSplitedmessageForOnlineUsers.length];
        for (int i = 0; i < personmessageForOnlineUsers.length; i++) {
            String nickname = allPeopleSplitedmessageForOnlineUsers[i];
            personmessageForOnlineUsers[i] = new PersonForOnlineUsers(nickname);
        }

        final ObservableList<PersonForOnlineUsers> datamessageForOnlineUsers = FXCollections.observableArrayList(
            personmessageForOnlineUsers
        );

        ObservableList<String> listmessageForOnlineUsers = FXCollections.observableArrayList();

        tableViewForOnlineUsers2.setItems(datamessageForOnlineUsers);
        tableViewForOnlineUsers2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (!tableViewForOnlineUsers2.getColumns().contains(usernameColumnmessageForOnlineUsers)) {
            tableViewForOnlineUsers2.getColumns().addAll(usernameColumnmessageForOnlineUsers);
        }

//        anchor1.getChildren().add(tableView2);
//        anchor1.getChildren().add(tableViewForOnlineUsers2);
    }

    public void returnToMainMenu(ActionEvent actionEvent) {
        ServerConnection.stopScoreboard();
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void customiseFactory(TableColumn<Person, String> calltypel, String nickname) {
        calltypel.setCellFactory(column -> {
            return new TableCell<Person, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Person> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if (item.equals(nickname))
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
