package project.model.modelsforview;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import project.controller.duel.GamePackage.PhaseInGame;
import project.view.pooyaviewpackage.DuelView;

public class GamePhaseButton extends Rectangle {
    PhaseInGame phaseInGame;

    public GamePhaseButton(PhaseInGame phaseInGame) {
        super(DuelView.getStageWidth() * 11 / 12, DuelView.getStageHeight() / 8 +
            (DuelView.getStageWidth() / 12 * changeButtonToIndexForHeightHelp(phaseInGame)), DuelView.getStageWidth() / 12, DuelView.getStageWidth() / 12);
        this.phaseInGame = phaseInGame;
        updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
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
        } else {
            this.setFill(new ImagePattern(new Image(GamePhaseButton.class.getResource("/project/ingameicons/phaseInGame/" + changePhaseToImageString() + "_image_uncolored.png").toExternalForm())));
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
}
