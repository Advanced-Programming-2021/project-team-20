package sample.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import sample.controller.duel.GamePackage.Action;
import sample.controller.duel.GamePackage.ActionType;
import sample.controller.duel.GamePackage.DuelBoard;
import sample.controller.duel.PreliminaryPackage.GameManager;
import sample.controller.duel.Utility.Utility;
import sample.model.cardData.General.Card;
import sample.model.cardData.General.CardLocation;
import sample.model.cardData.General.RowOfCardLocation;
import sample.model.cardData.MonsterCardData.MonsterCard;

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
            return "you can't attack the opponent directly\n";
        }
        if (turn == 2 && !duelBoard.isZoneEmpty(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return "you can't attack the opponent directly\n";
        }
        mainCard = selectCardLocations.get(selectCardLocations.size() - 1);
        if (((MonsterCard) duelBoard.getCardByCardLocation(mainCard)).isHasCardAlreadyAttacked()) {
            return "this monster has already attacked this turn\n";
        }
        if (GameManager.getDuelControllerByIndex(index).getTotalTurnsUntilNow() == 1) {
            return "in the first turn of the game, you can't attack\n";
        }
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
            actions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }


}
