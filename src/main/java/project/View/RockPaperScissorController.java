package project.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import project.View.transitions.RockPaperScissorTransition;

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
    private static String palyer1name;
    private static String player2Name;

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
        // allTransitions = new HashMap<>();
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
        // System.out.println(rec.getId());
        // allTransitions.put(rec.getId(), moveRectangles);
    }

    public void clickedRectangles(MouseEvent mouseEvent) {
        Rectangle rectangle = (Rectangle) mouseEvent.getSource();
        // System.out.println(rectangle.getX() + " " + rectangle.getY() + "               ddddddddddddddddddddddddddddddd");
        // System.out.println(rectangle.getLayoutX() + " " + rectangle.getLayoutY() + "               sssssssssssssssssssssssssssss");
        
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
        } else if (canSecondPlayerSelect) {
            player2Selection = selection;
            didPlayer2Select = true;
            pauseTransition();
            handleResult();
        }
    }

    private void handleResult() {
        // for (Map.Entry<Rectangle, List<Double>> entry : initialCoordinates.entrySet()) {
        //     TranslateTransition translate = new TranslateTransition();
        //     translate.setFromX(-100);
        //     translate.setFromY(0);
        //     translate.setToX(entry.getValue().get(0) -100);
        //     translate.setToY(entry.getValue().get(1) - 200);
        //     translate.setDuration(Duration.millis(1000));
        //     translate.setCycleCount(20);
        //     translate.setNode(entry.getKey());
        //     translate.play();
        // }
        backRectanglesToFirstPlace();
        attackChosenRectangles();
        if (player1Selection == player2Selection) {
            // show message
        } else if ((player1Selection == 1 && player2Selection == 3) || (player1Selection == 2 && player2Selection == 1)
                || (player1Selection == 3 && player2Selection == 2)) {

        } else {

        }
    }

    private void attackChosenRectangles() {
        player1SelectionRectangle.setOpacity(1);
        player2SelectionRectangle.setOpacity(1);
        if (player1Selection == 1) {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("stone")));
        } else if (player1Selection == 2) {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("paper")));
        } else {
            player1SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("scissor")));
        }
        if (player2Selection == 1) {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("stone")));
        } else if (player1Selection == 2) {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("paper")));
        } else {
            player2SelectionRectangle.setFill(new ImagePattern(imagesForRockPaperScissor.get("scissor")));
        }

    }

    private void backRectanglesToFirstPlace() {
        for (Map.Entry<Rectangle, List<Double>> entry : initialCoordinates.entrySet()) {
            entry.getKey().setX(entry.getValue().get(0));
            entry.getKey().setY(entry.getValue().get(1));
        }
        // didPlayer2Select = false;
        // canSecondPlayerSelect = false;
        // startTransition();
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
        System.out.println(didPlayer2Select);
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
