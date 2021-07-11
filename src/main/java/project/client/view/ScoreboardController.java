package project.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import project.server.controller.non_duel.scoreboard.Scoreboard;
import project.client.view.Components.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoreboardController {

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

        String allPeople = new Scoreboard().findCommands("scoreboard show");
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
}
