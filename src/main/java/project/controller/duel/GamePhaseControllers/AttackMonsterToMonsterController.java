package project.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.ActionType;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class AttackMonsterToMonsterController extends BattlePhaseController {
    private CardLocation mainCard;
    private int indexOfAttackedMonster;
    private ArrayList<CardLocation> targetingCards;

    public AttackMonsterToMonsterController() {
        targetingCards = new ArrayList<>();
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
    }


    public void setGoingToChangeTurnsForChaining(boolean goingToChangeTurnsForChaining) {
        isGoingToChangeTurnsForChaining = goingToChangeTurnsForChaining;
    }

    public void setClassWaitingForFurtherChainInput(boolean classWaitingForFurtherChainInput) {
        isClassWaitingForChainCardToBeSelected = classWaitingForFurtherChainInput;
    }

    public String attackInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)attack[\\s]+([\\d])(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            indexOfAttackedMonster = Integer.parseInt(matcher.group(1));
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = Utility.isACardSelected(0, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                resultOfChecking = checkAttackWithCard(card, 0);
                //System.out.println("result of checking was " + resultOfChecking);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    resultOfChecking = checkIndexOfAttackedMonster(card, 0);
                    return resultOfChecking;
                    /*
                    if (resultOfChecking.equals("")) {
                        Action.conductAllActions(0);
                    } else {
                        return resultOfChecking;
                    }

                     */
                }
            }
        }
        return null;
    }

    private String checkIndexOfAttackedMonster(Card card, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectCardLocations = selectCardController.getSelectedCardLocations();
        //System.out.println("turn is "+turn);
        //System.out.println("card name is "+card.getCardName());
        //System.out.println("indexOfAttackedMonster is " + indexOfAttackedMonster);
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
            //System.out.println("rowOfCardLocation is " + rowOfCardLocation);
            //System.out.println("indexOfAttackedMonster is " + indexOfAttackedMonster);
            CardLocation opponentCardLocation = new CardLocation(rowOfCardLocation, indexOfAttackedMonster);
            Card attackedMonster = duelBoard.getCardByCardLocation(opponentCardLocation);
            if (!(Card.isCardAMonster(attackedMonster))) {
                return "there is no card to attack here";
            } else {
                //System.out.println("attackedMonster name is " + attackedMonster.getCardName());
                String output = "";
                mainCard = selectCardLocations.get(selectCardLocations.size() - 1);
                if (((MonsterCard)duelBoard.getCardByCardLocation(mainCard)).isHasCardAlreadyAttacked()){
                    return "this monster has already attacked this turn\n";
                }
                if (GameManager.getDuelControllerByIndex(index).getTotalTurnsUntilNow() == 1) {
                    return "in the first turn of the game, you can't attack\n";
                }
                output = areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(mainCard, opponentCardLocation, index);
                if (!output.equals("")) {
                    return output;
                }
                createActionForAttackMonsterToMonster(index, opponentCardLocation, turn);
                output = Action.conductUninterruptedAction(index);
                String canChainingOccur = canChainingOccur(index, turn, ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER);
                if (canChainingOccur.equals("")) {
                    return output + Action.conductAllActions(index);
                }
                return output + "\n" + canChainingOccur;
            }
        }
    }

    private void createActionForAttackMonsterToMonster(int index, CardLocation opponentCardLocation, int turn) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
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


}
