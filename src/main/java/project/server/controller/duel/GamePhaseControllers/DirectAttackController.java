package project.server.controller.duel.GamePhaseControllers;

import project.server.controller.duel.GamePackage.Action;
import project.server.controller.duel.Utility.Utility;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class DirectAttackController extends BattlePhaseController {
    private CardLocation mainCard;

    public DirectAttackController() {
        isClassWaitingForChainCardToBeSelected = false;
        isGoingToChangeTurnsForChaining = false;
    }

    public String attackInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)attack[\\s]+direct(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            String resultOfChecking = Utility.isACardSelected(0, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                resultOfChecking = checkAttackWithCard(card, token);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    return checkPossibilityOfDirectAttack(card, token);
                }
            }
        }
        return "invalid command";
    }

    public String checkPossibilityOfDirectAttack(Card card, String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
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
        if (GameManager.getDuelControllerByIndex(token).getTotalTurnsUntilNow() == 1) {
            return "in the first turn of the game, you can't attack\n";
        }
        String output = "";
        output = areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(mainCard, null, token);
        if (!output.equals("")) {
            return output;
        }
        createActionForDirectAttack(card, token, turn);
        output = Action.conductUninterruptedAction(token);
        String canChainingOccur = canChainingOccur(token, turn, ActionType.ALLY_DIRECT_ATTACKING, ActionType.OPPONENT_DIRECT_ATTACKING);
        if (canChainingOccur.equals("")) {
            return output + Action.conductAllActions(token);
        }
        return output + "\n" + canChainingOccur;
    }


    private void createActionForDirectAttack(Card card, String token, int turn) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, turn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }


}
