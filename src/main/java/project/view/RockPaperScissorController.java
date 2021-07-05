package project.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.view.pooyaviewpackage.DuelView;
import project.view.transitions.RockPaperScissorTransition;
import project.controller.duel.PreliminaryPackage.GameManager;

public class RockPaperScissorController implements Initializable {

    @FXML
    private Rectangle scissor2Rectangle;
    @FXML
    private Rectangle scissor1Rectangle;
    @FXML
    private Rectangle paper1Rectangle;
    @FXML
    private Rectangle paper2Rectangle;
    @FXML
    private Rectangle stone1Rectangle;
    @FXML
    private Rectangle stone2Rectangle;
    @FXML
    private Rectangle player1SelectionRectangle;
    @FXML
    private Rectangle player2SelectionRectangle;
    private RockPaperScissorTransition transition1;
    private RockPaperScissorTransition transition2;
    private RockPaperScissorTransition transition3;
    private RockPaperScissorTransition transition4;
    private RockPaperScissorTransition transition5;
    private RockPaperScissorTransition transition6;
    private HashMap<Rectangle, List<Double>> initialCoordinates = new HashMap<>();

    private HashMap<String, Image> imagesForRockPaperScissor;
    private boolean canSecondPlayerSelect = false;
    private boolean didPlayer2Select = false;
    private int player1Selection;
    private int player2Selection;
    private static String allyPlayerName;
    private static String opponentPlayerName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        rotateRectangles(stone1Rectangle, 1);
        determineInitialCoordinates(stone1Rectangle);
        rotateRectangles(stone2Rectangle, 2);
        determineInitialCoordinates(stone2Rectangle);
        rotateRectangles(paper1Rectangle, 3);
        determineInitialCoordinates(paper1Rectangle);
        rotateRectangles(paper2Rectangle, 4);
        determineInitialCoordinates(paper2Rectangle);
        rotateRectangles(scissor1Rectangle, 5);
        determineInitialCoordinates(scissor1Rectangle);
        rotateRectangles(scissor2Rectangle, 6);
        determineInitialCoordinates(scissor2Rectangle);
        fillRectangles();
        determineUsers();
    }

    private void determineUsers() {
        allyPlayerName = GameManager.getDuelControllerByIndex(0).getPlayingUsers().get(0);
        opponentPlayerName = GameManager.getDuelControllerByIndex(0).getPlayingUsers().get(1);
    }

    private void determineInitialCoordinates(Rectangle rect) {
        List<Double> XAndYLayouts = new ArrayList<>();
        double X = rect.getX();
        double Y = rect.getY();
        XAndYLayouts.add(X);
        XAndYLayouts.add(Y);
        initialCoordinates.put(rect, XAndYLayouts);
    }

    private void fillRectangles() {
        imagesForRockPaperScissor = UIUtility.getImagesForRockPaperScissorController();
        scissor1Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("scissor")));
        scissor2Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("scissor")));
        stone1Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("stone")));
        stone2Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("stone")));
        paper1Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("paper")));
        paper2Rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("paper")));
    }

    private void rotateRectangles(Rectangle rec, int abc) {
        RockPaperScissorTransition moveRectangles = new RockPaperScissorTransition(rec);
        moveRectangles.play();
        if (abc == 1) {
            transition1 = moveRectangles;
        } else if (abc == 2) {
            transition2 = moveRectangles;
        } else if (abc == 3) {
            transition3 = moveRectangles;
        } else if (abc == 4) {
            transition4 = moveRectangles;
        } else if (abc == 5) {
            transition5 = moveRectangles;
        } else if (abc == 6) {
            transition6 = moveRectangles;
        }
    }

    public void clickedRectangles(MouseEvent mouseEvent) {
        Rectangle rectangle = (Rectangle) mouseEvent.getSource();
        int selection;
        if (rectangle.getId().contains("stone")) {
            selection = 1;
        } else if (rectangle.getId().contains("paper")) {
            selection = 2;
        } else {
            selection = 3;
        }
        if (!canSecondPlayerSelect) {
            player1Selection = selection;
            canSecondPlayerSelect = true;
            if (opponentPlayerName.equals("AI")) {
                handleAIPlayerSelection();
            }
        } else {
            player2Selection = selection;
            didPlayer2Select = true;
            pauseTransition();
            handleResult();
        }
    }

    private void handleAIPlayerSelection() {
        // try {
        // Thread.sleep(500);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        if (player1Selection == 1) {
            player2Selection = 3;
        } else {
            player2Selection = player1Selection - 1;
        }
        didPlayer2Select = true;
        pauseTransition();
        handleResult();
    }

    private void handleResult() {
        // for (Map.Entry<Rectangle, List<Double>> entry :
        // initialCoordinates.entrySet()) {
        // TranslateTransition translate = new TranslateTransition();
        // double moveX = entry.getValue().get(0) - entry.getKey().getX();
        // double moveY = entry.getValue().get(1) - entry.getKey().getY();
        // translate.setToX(moveX);
        // translate.setToY(moveY);
        // translate.setDuration(Duration.millis(1000));
        // translate.setCycleCount(1);
        // translate.setNode(entry.getKey());
        // translate.setOnFinished(new EventHandler<ActionEvent>() {
        // @Override
        // public void handle(ActionEvent arg0) {
        // attackChosenRectangles();
        // Rectangle rectangle = (Rectangle) translate.getNode();
        // for (Map.Entry<Rectangle, List<Double>> entry :
        // initialCoordinates.entrySet()) {
        // if (entry.getKey().equals(rectangle)) {
        // rectangle.setX(-moveX);
        // rectangle.setY(-moveY);
        // }
        // }
        // }
        // });
        // translate.play();
        // }

        backRectanglesToFirstPlace();
        attackChosenRectangles();
        if (player1Selection == player2Selection) {
            showAlert("BOTH PLAYERS ARE EQUAL, REPEAT THIS GAME AGAIN", "CONFIRMATION");
            player1Selection = 0;
            player2Selection = 0;
            didPlayer2Select = false;
            canSecondPlayerSelect = false;
            startTransition();
        } else if ((player1Selection == 1 && player2Selection == 3) || (player1Selection == 2 && player2Selection == 1)
                || (player1Selection == 3 && player2Selection == 2)) {
            GameManager.getDuelControllerByIndex(0).setTurn(1);
            GameManager.getDuelControllerByIndex(0).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
            GameManager.getDuelControllerByIndex(0).startDuel(0);
            showAlert("PLAYER " + allyPlayerName + " WON THE GAME AND MUST START GAME", "CONFIRMATION");
            new DuelView().start(new Stage());
        } else {
            GameManager.getDuelControllerByIndex(0).setTurn(1);
            GameManager.getDuelControllerByIndex(0).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
            GameManager.getDuelControllerByIndex(0).startDuel(0);
            showAlert("PLAYER " + opponentPlayerName + " WON THE GAME AND MUST START GAME", "CONFIRMATION");
            new DuelView().start(new Stage());
        }
    }

    private void backRectanglesToFirstPlace() {
        for (Map.Entry<Rectangle, List<Double>> entry : initialCoordinates.entrySet()) {
            entry.getKey().setX(entry.getValue().get(0));
            entry.getKey().setY(entry.getValue().get(1));
        }
    }

    private void showAlert(String message, String typeOfMessage) {
        CustomDialog customDialog = new CustomDialog(typeOfMessage, message);
        customDialog.openDialog();
    }

    private void attackChosenRectangles() {
        player1SelectionRectangle.setOpacity(1);
        player2SelectionRectangle.setOpacity(1);
        if (player1Selection == 1) {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdStone")));
        } else if (player1Selection == 2) {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterePaper")));
        } else {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdScissorBaz")));
        }
        if (player2Selection == 1) {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdStone")));
        } else if (player2Selection == 2) {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterePaper")));
        } else {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdScissorBaz")));
        }

        TranslateTransition transitionPlayer1Selection = createAttackTransition(player1SelectionRectangle, 324);
        TranslateTransition transitionPlayer2Selection = createAttackTransition(player2SelectionRectangle, -324);

        transitionPlayer1Selection.play();
        transitionPlayer2Selection.play();

    }

    private TranslateTransition createAttackTransition(Rectangle rec, double position) {

        TranslateTransition transitionPlayerSelection = new TranslateTransition();
        transitionPlayerSelection.setDuration(Duration.millis(500));
        transitionPlayerSelection.setFromX(rec.getX());
        transitionPlayerSelection.setToX(position);
        transitionPlayerSelection.setCycleCount(2);
        transitionPlayerSelection.setNode(rec);

        transitionPlayerSelection.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                createFadeTransition(transitionPlayerSelection.getNode());
            }

        });
        transitionPlayerSelection.setAutoReverse(true);
        return transitionPlayerSelection;
    }

    private void createFadeTransition(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

    public void enterdToRectangles(MouseEvent mouseEvent) {
        Rectangle rectangle = (Rectangle) mouseEvent.getSource();
        if (rectangle.getId().contains("stone")) {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdStone")));
        } else if (rectangle.getId().contains("paper")) {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterePaper")));
        } else {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("mouseEnterdScissorBaz")));
        }
        // pauseTransition();
    }

    private void pauseTransition() {
        transition1.pause();
        transition2.pause();
        transition3.pause();
        transition4.pause();
        transition5.pause();
        transition6.pause();
    }

    public void exitFromRectangles(MouseEvent mouseEvent) {
        Rectangle rectangle = (Rectangle) mouseEvent.getSource();
        if (rectangle.getId().contains("stone")) {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("stone")));
        } else if (rectangle.getId().contains("paper")) {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("paper")));
        } else {
            rectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("scissor")));
        }
        startTransition();
    }

    private void startTransition() {
        if (!didPlayer2Select) {
            transition1.play();
            transition2.play();
            transition3.play();
            transition4.play();
            transition5.play();
            transition6.play();
        }
    }
}
