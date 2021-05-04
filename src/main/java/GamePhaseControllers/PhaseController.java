package GamePhaseControllers;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.RowOfCardLocation;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import GamePackage.PhaseInGame;
import PreliminaryPackage.GameManager;
import Utility.Utility;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class PhaseController {
    private PhaseInGame phaseInGame;

    public String phaseControllerInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)next[\\s]+phase(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.ALLY_BATTLE_PHASE;
                return "phase: battle phase";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_2;
                return "phase: main phase 2";
            } else if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.ALLY_END_PHASE;
                return "phase: end phase\nphase: draw phase\n" + addCardToHand(0);
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
                phaseInGame = PhaseInGame.OPPONENT_BATTLE_PHASE;
                return "phase: battle phase";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
                phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_2;
                return "phase: main phase 2";
            } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
                phaseInGame = PhaseInGame.OPPONENT_END_PHASE;
                return "phase: end phase\n" + resetAllVariables(0) + "phase: draw phase\n" + addCardToHand(0);
            }
        }
        return "invalid command";
    }

    public PhaseInGame getPhaseInGame() {
        return phaseInGame;
    }

    public void setPhaseInGame(PhaseInGame phaseInGame) {
        this.phaseInGame = phaseInGame;
    }

    public boolean isPhaseCurrentlyMainPhase(int turn) {
        if (turn == 1 && (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2))) {
            return true;
        } else if (turn == 1) {
            return false;
        } else if (phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhaseCurrentlyBattlePhase(int turn) {
        if (turn == 1 && (phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE))) {
            return true;
        } else if (turn == 1) {
            return false;
        } else if (phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            return true;
        } else {
            return false;
        }
    }

    public String addCardToHand(int index) {
        // must check time seal card in the future here
        // must add standby phase effects too
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation = null;
        boolean isGameFinished = false;
        if (turn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 1);
            ArrayList<Card> opponentDeck = duelBoard.getOpponentCardsInDeck();
            if (opponentDeck.size() == 0) {
                isGameFinished = true;
            }
        } else if (turn == 2) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
            ArrayList<Card> allyDeck = duelBoard.getAllyCardsInDeck();
            if (allyDeck.size() == 0) {
                isGameFinished = true;
            }
        }
        if (isGameFinished) {
            if (turn == 1) {
                int difference = duelController.getLifePoints().get(0) - duelController.getLifePoints().get(1);
                return duelController.getPlayingUsers().get(0) + " won the game and the score is: " + difference + "\n";
            } else {
                int difference = duelController.getLifePoints().get(1) - duelController.getLifePoints().get(0);
                return duelController.getPlayingUsers().get(1) + " won the game and the score is: " + difference + "\n";
            }
        }
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        duelBoard.removeCardByCardLocation(cardLocation);
        duelBoard.addCardToHand(card, 3 - turn);
        duelController.setTurn(3 - turn);
        duelController.setFakeTurn(3 - turn);
        if (turn == 1) {
            phaseInGame = PhaseInGame.OPPONENT_MAIN_PHASE_1;
        } else {
            phaseInGame = PhaseInGame.ALLY_MAIN_PHASE_1;
        }
        return "new card added to hand: " + card.getCardName() + "\nphase: standby phase\nphase: main phase 1\n";
    }

    private String resetAllVariables(int index) {

        return "";
    }
}
