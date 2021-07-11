package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.model.MonsterEffectEnums.BeingAttackedEffect;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

public class AttackMonsterToMonsterController extends BattlePhaseController {
    private CardLocation mainCard;
    private int indexOfAttackedMonster;
    private ArrayList<CardLocation> targetingCards;

    public AttackMonsterToMonsterController() {
        targetingCards = new ArrayList<>();
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
    }


    public String attackInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)attack[\\s]+([\\d])(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            indexOfAttackedMonster = Integer.parseInt(matcher.group(1));
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            String resultOfChecking = Utility.isACardSelected(token, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                resultOfChecking = checkAttackWithCard(card, token);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    resultOfChecking = checkIndexOfAttackedMonster(card, token);
                    return resultOfChecking;
                }
            }
        }
        return null;
    }

    private String checkIndexOfAttackedMonster(Card card, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectCardLocations = selectCardController.getSelectedCardLocations();
        if (indexOfAttackedMonster < 1 || indexOfAttackedMonster > 5) {
            return "there is no card to attack here";
        } else {
            RowOfCardLocation rowOfCardLocation;
            if (turn == 1) {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_MONSTER_ZONE;
            } else {
                rowOfCardLocation = RowOfCardLocation.ALLY_MONSTER_ZONE;
            }
            indexOfAttackedMonster = Utility.changeYuGiOhIndexToArrayIndex(indexOfAttackedMonster, rowOfCardLocation);
            CardLocation opponentCardLocation = new CardLocation(rowOfCardLocation, indexOfAttackedMonster);
            Card attackedMonster = duelBoard.getCardByCardLocation(opponentCardLocation);
            if (!(Card.isCardAMonster(attackedMonster))) {
                return "there is no card to attack here";
            } else {
                String output = "";
                mainCard = selectCardLocations.get(selectCardLocations.size() - 1);
                if (((MonsterCard)duelBoard.getCardByCardLocation(mainCard)).isHasCardAlreadyAttacked()){
                    return "this monster has already attacked this turn\n";
                }
                if (GameManager.getDuelControllerByIndex(token).getTotalTurnsUntilNow() == 1) {
                    return "in the first turn of the game, you can't attack\n";
                }
                output = areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(mainCard, opponentCardLocation, token);
                if (!output.equals("")) {
                    return output;
                }
                createActionForAttackMonsterToMonster(token, opponentCardLocation, turn);
                output = Action.conductUninterruptedAction(token);
                String canChainingOccur = canChainingOccur(token, turn, ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER);
                if (canChainingOccur.equals("")) {
                    return output + Action.conductAllActions(token);
                }
                return output + "\n" + canChainingOccur;
            }
        }
    }

    private void createActionForAttackMonsterToMonster(String token, CardLocation opponentCardLocation, int turn) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectCardLocations = selectCardController.getSelectedCardLocations();
        mainCard = selectCardLocations.get(selectCardLocations.size() - 1);
        targetingCards.add(opponentCardLocation);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, 1, mainCard, targetingCards, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, 1, mainCard, targetingCards, null, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER, 2, mainCard, targetingCards, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER, 2, mainCard, targetingCards, null, null, null, null, null, null, null, null, null, null, "", null));
        }
        targetingCards.clear();
    }

    public void clearAllVariablesOfThisClass(){
        mainCard = null;
        indexOfAttackedMonster = 0;
        targetingCards.clear();
    }
}
