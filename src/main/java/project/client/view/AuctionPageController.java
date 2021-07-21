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
        AuctionToShow[] auctionsToShows = new AuctionToShow[allPeopleSplited.length / 7];

        for (int i = 0; i < auctionsToShows.length; i++) {
            String auctionCode = allPeopleSplited[i * 7];
            String cardName = allPeopleSplited[i * 7 + 1];
            String auctionCreatorName = allPeopleSplited[i * 7 + 2];
            String bestBuyerName = allPeopleSplited[i * 7 + 3];
            String price = allPeopleSplited[i * 7 + 4];
            String isActivated = allPeopleSplited[i * 7 + 5];
            String timeLeftAsSeconds = allPeopleSplited[i * 7 + 6];
            auctionsToShows[i] = new AuctionToShow(auctionCode, cardName, auctionCreatorName, bestBuyerName, price, isActivated, timeLeftAsSeconds);
//            System.out.println(auctionsToShows[i].getAuctionCode() + "==Code");
//            System.out.println(auctionsToShows[i].getCardName() + "==namecard");
        }


        final ObservableList<AuctionToShow> data = FXCollections.observableArrayList(
            auctionsToShows
        );
        ///1
        TableColumn<AuctionToShow, String> auctionCodeColumn = new TableColumn<>("CODE");
        auctionCodeColumn.setCellValueFactory(new PropertyValueFactory<>("auctionCode"));
        auctionCodeColumn.setStyle("-fx-alignment: CENTER;");
        auctionCodeColumn.setMinWidth(80);


        ///1.5
        TableColumn<AuctionToShow, String> cardNameColumn = new TableColumn<>("CARD NAME");
        cardNameColumn.setCellValueFactory(new PropertyValueFactory<>("cardName"));
        cardNameColumn.setStyle("-fx-alignment: CENTER;");
        cardNameColumn.setMinWidth(80);

        ///2
        TableColumn<AuctionToShow, String> auctionCreatorNameColumn = new TableColumn<>("CREATOR NAME");
        auctionCreatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("auctionCreatorName"));
        auctionCreatorNameColumn.setStyle("-fx-alignment: CENTER;");
        auctionCreatorNameColumn.setMinWidth(80);

        ///3
        TableColumn<AuctionToShow, String> bestBuyerNameColumn = new TableColumn<>("BEST BUYER");
        bestBuyerNameColumn.setCellValueFactory(new PropertyValueFactory<>("bestBuyerName"));
        bestBuyerNameColumn.setStyle("-fx-alignment: CENTER;");
        bestBuyerNameColumn.setMinWidth(80);


        ///4
        TableColumn<AuctionToShow, String> priceColumn = new TableColumn<>("PRICE");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setMinWidth(70);


        ///5
        TableColumn<AuctionToShow, String> isActivatedColumn = new TableColumn<>("IS ACTIVATED");
        isActivatedColumn.setCellValueFactory(new PropertyValueFactory<>("isActivated"));
        isActivatedColumn.setStyle("-fx-alignment: CENTER;");
        isActivatedColumn.setMinWidth(70);

        ///6
        TableColumn<AuctionToShow, String> timeLeftAsSecondsColumn = new TableColumn<>("LEFT TIME");
        timeLeftAsSecondsColumn.setCellValueFactory(new PropertyValueFactory<>("timeLeftAsSeconds"));
        timeLeftAsSecondsColumn.setStyle("-fx-alignment: CENTER;");
        timeLeftAsSecondsColumn.setMinWidth(70);


        ObservableList<String> list = FXCollections.observableArrayList();

        tableView.setItems(data);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getColumns().addAll(auctionCodeColumn, cardNameColumn, auctionCreatorNameColumn, bestBuyerNameColumn, priceColumn, isActivatedColumn, timeLeftAsSecondsColumn);

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
