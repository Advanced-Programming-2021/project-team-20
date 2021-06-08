package project.View;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.stage.Stage;
import project.controller.non_duel.scoreboard.Scoreboard;

import java.io.IOException;

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

    public void fillLabel() {
        System.out.println("hi");
        final ObservableList<Person> data = FXCollections.observableArrayList(
                new Person(1, "ALI", 12),
                new Person(2, "Mohammad", 10)
        );
        TableColumn<Person, Integer> rankingColumn = new TableColumn<>("RANKING");
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        rankingColumn.setStyle( "-fx-alignment: CENTER;");
        rankingColumn.setMinWidth(100);

        TableColumn<Person, String> usernameColumn = new TableColumn<>("USERNAME");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
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

//        tableView.getColumns().add(rankingColumn);
//        tableView.getColumns().add(usernameColumn);
//        tableView.getColumns().add(scoreboardColumn);
//
//
//        tableView.getItems().add(new Person(1, "John", 12));
//        tableView.getItems().add(new Person(2, "Jane", 9));

    }

    public void returnToMainMenu(ActionEvent actionEvent) {
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
