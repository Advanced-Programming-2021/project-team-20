package project.client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.Components.AuctionToShow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AuctionPageController implements Initializable {

    public Button auctionButton;
    public Button bidPriceButton;
    public TableView tableView;
    public TextField auctionCodeTextField;
    public TextField priceTextField;
    public TextField cardNameTextField;
    public TextField initialPriceTextField;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        createTable();
    }

    private void createTable() {
        ServerConnection.startAuction();
        ServerConnection.auctionAutoRefresh(this);
    }

    public void auctionFunction(ActionEvent actionEvent) {
        String initialPriceAsString = initialPriceTextField.getText();
        String cardName = cardNameTextField.getText();
        Pattern pattern = Pattern.compile("^\\d+$");
        if (!initialPriceAsString.isEmpty() && !cardName.isEmpty() && pattern.matcher(initialPriceAsString).find()) {
            int initialPrice = Integer.parseInt(initialPriceAsString);
            String token = LoginController.getToken();
            String dataToSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatAuction(token, cardName, initialPrice);
            String answerOfServer = ServerConnection.sendDataToServerAndReceiveResult(dataToSendToServer);
        }
    }

    public void bidPriceFunction(ActionEvent actionEvent) {

    }


    public void refreshTable(String whatServerGave) {
        System.out.println(whatServerGave);


        tableView.getColumns().clear();

        String allPeople = whatServerGave;
        String[] allPeopleSplited = allPeople.split(",");
        AuctionToShow[] auctionsToShows = new AuctionToShow[allPeopleSplited.length / 6];

        for (int i = 0; i < auctionsToShows.length; i++) {
            String auctionCode = allPeopleSplited[i * 6];
            System.out.println("code:" + auctionCode);
            String cardName = allPeopleSplited[i * 6 + 1];
            System.out.println("name:" + cardName);
            String auctionCreatorName = allPeopleSplited[i * 6 + 2];
            String bestBuyerName = allPeopleSplited[i * 6 + 3];
            String price = allPeopleSplited[i * 6 + 4];
            String isActivated = allPeopleSplited[i * 6 + 5];
            auctionsToShows[i] = new AuctionToShow(auctionCode, cardName, auctionCreatorName, bestBuyerName, price, isActivated);
        }


        final ObservableList<AuctionToShow> data = FXCollections.observableArrayList(
            auctionsToShows
        );
        ///1
        TableColumn<AuctionToShow, String> auctionCodeColumn = new TableColumn<>("CODE");
        auctionCodeColumn.setCellValueFactory(new PropertyValueFactory<>("auctionCode"));
        auctionCodeColumn.setStyle("-fx-alignment: CENTER;");
        auctionCodeColumn.setMinWidth(97);


        ///1.5
        TableColumn<AuctionToShow, String> cardNameColumn = new TableColumn<>("CARD NAME");
        auctionCodeColumn.setCellValueFactory(new PropertyValueFactory<>("cardName"));
        auctionCodeColumn.setStyle("-fx-alignment: CENTER;");
        auctionCodeColumn.setMinWidth(97);

        ///2
        TableColumn<AuctionToShow, String> auctionCreatorNameColumn = new TableColumn<>("CREATOR NAME");
        auctionCreatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("auctionCreatorName"));
        auctionCreatorNameColumn.setStyle("-fx-alignment: CENTER;");
        auctionCreatorNameColumn.setMinWidth(100);

        ///3
        TableColumn<AuctionToShow, String> bestBuyerNameColumn = new TableColumn<>("BEST BUYER NAME");
        bestBuyerNameColumn.setCellValueFactory(new PropertyValueFactory<>("bestBuyerName"));
        bestBuyerNameColumn.setStyle("-fx-alignment: CENTER;");
        bestBuyerNameColumn.setMinWidth(100);


        ///4
        TableColumn<AuctionToShow, String> priceColumn = new TableColumn<>("PRICE");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setMinWidth(100);


        ///5
        TableColumn<AuctionToShow, String> isActivatedColumn = new TableColumn<>("IS ACTIVATED");
        isActivatedColumn.setCellValueFactory(new PropertyValueFactory<>("isActivated"));
        isActivatedColumn.setStyle("-fx-alignment: CENTER;");
        isActivatedColumn.setMinWidth(100);

        ObservableList<String> list = FXCollections.observableArrayList();

        tableView.setItems(data);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getColumns().addAll(auctionCodeColumn, cardNameColumn, auctionCreatorNameColumn, bestBuyerNameColumn, priceColumn, isActivatedColumn);

    }

    public void back(ActionEvent actionEvent) {
        ServerConnection.stopAuctionRefresh();
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private static void continueCreatingTable(){
//
//    }
}
