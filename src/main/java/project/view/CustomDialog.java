package project.view;

import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.Deck;

public class CustomDialog extends Stage {
    private RockPaperScissorController rockPaperScissorController;
    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };
    private ScaleTransition scale1 = new ScaleTransition();
    private ScaleTransition scale2 = new ScaleTransition();

    private SequentialTransition anim = new SequentialTransition(scale1, scale2);

    public CustomDialog(String header, String content) {
        Pane root = new Pane();

        setScale1(root);
        setScale2(root);

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        Rectangle bg = createRectanle(content);
        VBox box = createVBox(header, content);
        Button btn = createButton(bg, false);
        root.getChildren().addAll(bg, box, btn);
        setScene(new Scene(root, null));
    }

    public CustomDialog(String header, String content, RockPaperScissorController rockPaperScissorController) {
        Pane root = new Pane();
        this.rockPaperScissorController = rockPaperScissorController;
        setScale1(root);
        setScale2(root);

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        Rectangle bg = createRectanle(content);
        VBox box = createVBox(header, content);
        Button btn = createButton(bg, true);
        root.getChildren().addAll(bg, box, btn);
        setScene(new Scene(root, null));
    }

    public CustomDialog(String header, String content, String toWhichClass) {
        Pane root = new Pane();
        setScale1(root);
        setScale2(root);

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        Rectangle bg = createRectanle(content);
        VBox box = createVBox(header, content);
        Button btn = createButtonWhenUserLogined(bg);
        root.getChildren().addAll(bg, box, btn);
        setScene(new Scene(root, null));
    }

    private Button createButtonWhenUserLogined(Rectangle bg){
        Button btn = new Button("OK");
        btn.setTranslateX(bg.getWidth() - 75);
        btn.setTranslateY(bg.getHeight() - 50);
        btn.setOnAction(e-> callMainMenu());
        btn.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        return btn;
    }

    public CustomDialog(String header, String content, boolean isOneRoundOfDuelEnded) {
        Pane root = new Pane();
        setScale1(root);
        setScale2(root);

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        Rectangle bg = createRectanle(content);
        VBox box = createVBox(header, content);
        Button btn = createButtonWhenOneRoundOfDuelEnded(bg, isOneRoundOfDuelEnded);
        root.getChildren().addAll(bg, box, btn);
        setScene(new Scene(root, null));
    }

    private Button createButtonWhenOneRoundOfDuelEnded(Rectangle bg, boolean isOneRoundOfDuelEnded) {
        Button btn = new Button("OK");
        btn.setTranslateX(bg.getWidth() - 75);
        btn.setTranslateY(bg.getHeight() - 50);
        if (isOneRoundOfDuelEnded) {
            btn.setOnAction(e -> callChangeCardsBetweenTwoRounds());
        } else {
            btn.setOnAction(e -> callMainMenu());
        }
        btn.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        return btn;
    }

    private void callMainMenu() {
        closeDialog();
        try {
            new MainView().changeView("/project/fxml/mainMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void callChangeCardsBetweenTwoRounds() {
        closeDialog();
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/project/fxml/changeCardsBetweenTwoRoundsPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String currentPlayerWhoChangesDeck = GameManager.getDuelControllerByIndex(0).getPlayingUsers().get(0);
        Deck activeDeck = GameManager.getChangeCardsBetweenTwoRoundsByIndex(0).getAllyPlayerDeck();
        new ChangeCardsBetweenTwoRoundsController().showPage(pane, currentPlayerWhoChangesDeck,
                activeDeck.getDeckname());
    }

    private Button createButton(Rectangle bg, boolean isRockPaperScissorController) {
        Button btn = new Button("OK");
        btn.setTranslateX(bg.getWidth() - 75);
        btn.setTranslateY(bg.getHeight() - 50);
        if (isRockPaperScissorController) {
            btn.setOnAction(e -> callMethodesInRockPaperScissor());
        } else {
            btn.setOnAction(e -> closeDialog());
        }
        btn.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        return btn;
    }

    private VBox createVBox(String header, String content) {
        Text headerText = new Text(header);
        headerText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));

        Text contentText = new Text(content);
        contentText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));

        VBox box = new VBox(10, headerText, new Separator(Orientation.HORIZONTAL), contentText);
        box.setPadding(new Insets(15));
        return box;
    }

    private Rectangle createRectanle(String content) {
        int length = 400;
        if (content.length() > 30) {
            length = content.length() * 13;
        }
        Rectangle bg = new Rectangle(length, 150, Color.GRAY);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(1.5);
        return bg;
    }

    private void setScale2(Pane root) {
        scale2.setFromX(0.01);
        scale2.setToX(1.0);
        scale2.setDuration(Duration.seconds(0.33));
        scale2.setInterpolator(EXP_OUT);
        scale2.setNode(root);
    }

    private void setScale1(Pane root) {
        scale1.setFromX(0.01);
        scale1.setFromY(0.01);
        scale1.setToY(1.0);
        scale1.setDuration(Duration.seconds(0.33));
        scale1.setInterpolator(EXP_IN);
        scale1.setNode(root);
    }

    public void openDialog() {
        show();
        anim.play();
    }

    private void closeDialog() {
        anim.setOnFinished(e -> close());
        anim.setAutoReverse(true);
        anim.setCycleCount(2);
        anim.playFrom(Duration.seconds(0.66));
    }

    // private void closeDialogInRockPaperScissorControllerClass() {
    //     anim.setAutoReverse(true);
    //     anim.setCycleCount(2);
    //     anim.playFrom(Duration.seconds(0.66));
    //     anim.setOnFinished(e -> callMethodesInRockPaperScissor());
    // }

    private void callMethodesInRockPaperScissor() {
        closeDialog();
        if (rockPaperScissorController.didAnyOneWin()) {
            rockPaperScissorController.startDuel();
        } else {
            rockPaperScissorController.startTransition();
        }
    }

}
