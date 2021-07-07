package project.model.modelsforview;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import project.controller.duel.GamePackage.PhaseInGame;
import project.view.pooyaviewpackage.DuelView;

import java.sql.Time;
import java.util.ArrayList;

public class GamePhaseButton extends Rectangle {
    PhaseInGame phaseInGame;
    boolean isColoredImageOn;
    private static ArrayList<GamePhaseButton> allGamePhaseButtons = new ArrayList<>();
    private static  Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
        }),
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
        }),
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
        }),
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
        }),
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
        }),
        new KeyFrame(Duration.seconds(0.4), e -> {
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_colored.png").toExternalForm())));
        })
    );
    public GamePhaseButton(PhaseInGame phaseInGame) {
        super(DuelView.getStageWidth() * 11 / 12, DuelView.getStageHeight() / 8 +
            (DuelView.getStageWidth() / 12 * changeButtonToIndexForHeightHelp(phaseInGame)), DuelView.getStageWidth() / 12, DuelView.getStageWidth() / 12);
        this.phaseInGame = phaseInGame;
        updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
        allGamePhaseButtons.add(this);
    }

    private static int changeButtonToIndexForHeightHelp(PhaseInGame phaseInGame) {
        if (phaseInGame == null) {
            return 6;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_DRAW_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_DRAW_PHASE)) {
            return 0;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_STANDBY_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_STANDBY_PHASE)) {
            return 1;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
            return 2;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            return 3;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
            return 4;
        } else if (phaseInGame.equals(PhaseInGame.ALLY_END_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_END_PHASE)) {
            return 5;
        }
        return 0;
    }

    public void updateImage(PhaseInGame phaseInGame) {
        if (this.phaseInGame == null || areThesePhasesEquivalent(phaseInGame)) {
            this.setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + changePhaseToImageString() + "_image_colored.png").toExternalForm())));
            this.isColoredImageOn = true;
        } else {
            this.setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + changePhaseToImageString() + "_image_uncolored.png").toExternalForm())));
            this.isColoredImageOn = false;
        }
    }

    private boolean areThesePhasesEquivalent(PhaseInGame phaseInGame) {
        if (phaseInGame.toString().startsWith("ALLY") && this.phaseInGame.toString().startsWith("ALLY") ||
            phaseInGame.toString().startsWith("OPPONENT") && this.phaseInGame.toString().startsWith("OPPONENT")) {
            return this.phaseInGame.equals(phaseInGame);
        } else {
            String toString = phaseInGame.toString();
            String newString = "";
            if (toString.startsWith("A")) {
                newString = "OPPONENT" + toString.substring(4);
            } else {
                newString = "ALLY" + toString.substring(8);
            }
            PhaseInGame newValue = PhaseInGame.valueOf(newString);
            return newValue.equals(this.phaseInGame);
        }
    }

    private String changePhaseToImageString() {
        if (phaseInGame == null) {
            return "next_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_DRAW_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_DRAW_PHASE)) {
            return "draw_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_STANDBY_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_STANDBY_PHASE)) {
            return "standby_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
            return "main_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            return "battle_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
            return "main_phase";
        } else if (phaseInGame.equals(PhaseInGame.ALLY_END_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_END_PHASE)) {
            return "end_phase";
        }
        return "next_phase_button";
    }

    public boolean isColoredImageOn() {
        return isColoredImageOn;
    }

    public PhaseInGame getPhaseInGame() {
        return phaseInGame;
    }

//    public static Timeline getTimeline() {
//        return timeline;
//    }

    public static void updateAllGamePhaseButtonsOnce(){
        int currentCourseOfAccounts = -1;
        if (allGamePhaseButtons.get(0).isColoredImageOn){
            currentCourseOfAccounts = 0;
        } else if (allGamePhaseButtons.get(1).isColoredImageOn){
            currentCourseOfAccounts = 1;
        } else if (allGamePhaseButtons.get(2).isColoredImageOn){
            currentCourseOfAccounts = 2;
        } else if (allGamePhaseButtons.get(3).isColoredImageOn){
            currentCourseOfAccounts = 3;
        } else if (allGamePhaseButtons.get(4).isColoredImageOn){
            currentCourseOfAccounts = 4;
        } else if (allGamePhaseButtons.get(5).isColoredImageOn){
            currentCourseOfAccounts = 5;
        }
        if (currentCourseOfAccounts == 0){
            //currently in draw phase
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(0).isColoredImageOn = false;
            allGamePhaseButtons.get(1).isColoredImageOn = true;
        } else if (currentCourseOfAccounts == 1){
            //currently in standby phase
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).isColoredImageOn = false;
            allGamePhaseButtons.get(2).isColoredImageOn = true;
        } else if (currentCourseOfAccounts == 2){
            //currently in main phase one
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).isColoredImageOn = false;
            allGamePhaseButtons.get(3).isColoredImageOn = true;
        } else if (currentCourseOfAccounts == 3){
            //currently in battle phase
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).isColoredImageOn = false;
            allGamePhaseButtons.get(4).isColoredImageOn = true;
        } else if (currentCourseOfAccounts == 4){
            //currently in main phase two
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(4).isColoredImageOn = false;
            allGamePhaseButtons.get(5).isColoredImageOn = true;
        } else if (currentCourseOfAccounts == 5){
            //currently in end phase
            allGamePhaseButtons.get(0).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "draw_phase" + "_image_colored.png").toExternalForm())));
            allGamePhaseButtons.get(1).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "standby_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(2).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(3).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "battle_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(4).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "main_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + "end_phase" + "_image_uncolored.png").toExternalForm())));
            allGamePhaseButtons.get(5).isColoredImageOn = false;
            allGamePhaseButtons.get(0).isColoredImageOn = true;
        }
    }
}
