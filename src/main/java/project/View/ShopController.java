package project.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import project.View.Components.CardForShow;
import project.View.Components.CardView;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ShopController implements Initializable {
    @FXML
    GridPane gameBoard;
    private static CardView[][] cards;
    private static CardForShow[][] board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        board = new CardForShow[5][8];
        fillBoard();
        showCards(board);
    }

    private void showCards(CardForShow[][] board) {
        cards = new CardView[board.length][board[0].length];
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[i].length ; j++) {
//                CardView rectangle = getCardRectangle(board[i][j]);
                CardView rectangle = (CardView) getNodeByRowColumnIndex(i, j, gameBoard);
                gameBoard.add(rectangle, j, i);
                //doesn't work, how to add fill with picture?
                rectangle.setFill(board[i][j].getFrontImage());
                cards[i][j] = rectangle;
            }
        }
    }

    public static Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    private CardView getCardRectangle(CardForShow card) {
        CardView rectangle = new CardView();
        rectangle.setHeight(105.6);
        rectangle.setWidth(69.1);
        rectangle.setI(card.getI());
        rectangle.setJ(card.getJ());
        return rectangle;
    }

    private void fillBoard() {
        String[] labels = new String[]{
                ShopController.class.getResource("/project/css/Monsters/AlexandriteDragon.jpg").toExternalForm(),
        };
        List<String> fronts =  Arrays.asList(labels);
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[0].length ; j++)
                board[i][j] = new CardForShow(fronts.get(0),  i, j);
        }
    }
}
