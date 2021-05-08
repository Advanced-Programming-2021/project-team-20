package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;

public class DirectAttackController extends BattlePhaseController {
    private CardLocation mainCard;

    public DirectAttackController() {
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
    }

    public String attackInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)attack[\\s]+direct(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = Utility.isACardSelected(0, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                resultOfChecking = checkAttackWithCard(card, 0);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    return checkPossibilityOfDirectAttack(card, 0);
                }
            }
        }
        return "invalid command";
    }

    public String checkPossibilityOfDirectAttack(Card card, int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        if (turn == 1 && !duelBoard.isZoneEmpty(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return "you can't attack the opponent directly";
        }
        if (turn == 2 && !duelBoard.isZoneEmpty(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return "you can't attack the opponent directly";
        }
        mainCard = selectCardLocations.get(selectCardLocations.size() - 1);
        String output = "";
        output = areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(mainCard, null, index);
        if (!output.equals("")) {
            return output;
        }
        createActionForDirectAttack(card, index, turn);
        output = Action.conductUninterruptedAction(index);
        String canChainingOccur = canChainingOccur(index, turn, ActionType.ALLY_DIRECT_ATTACKING, ActionType.OPPONENT_DIRECT_ATTACKING);
        if (canChainingOccur.equals("")) {
            return output + Action.conductAllActions(index);
        }
        return output + "\n" + canChainingOccur;
    }


    private void createActionForDirectAttack(Card card, int index, int turn) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null));
            uninterruptedActions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null));
        }
    }


}
