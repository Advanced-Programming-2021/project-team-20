package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

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
                System.out.println("result of checking was " + resultOfChecking);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    resultOfChecking = checkIndexOfAttackedMonster(card, 0);
                    if (resultOfChecking.equals("")) {
                        Action.conductAllActions(0);
                    } else {
                        return resultOfChecking;
                    }
                }
            }
        }
        return null;
    }

    private String checkIndexOfAttackedMonster(Card card, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        //System.out.println("turn is "+turn);
        //System.out.println("card name is "+card.getCardName());
        System.out.println("indexOfAttackedMonster is " + indexOfAttackedMonster);
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
            System.out.println("rowOfCardLocation is " + rowOfCardLocation);
            System.out.println("indexOfAttackedMonster is " + indexOfAttackedMonster);
            CardLocation opponentCardLocation = new CardLocation(rowOfCardLocation, indexOfAttackedMonster);
            Card attackedMonster = duelBoard.getCardByCardLocation(opponentCardLocation);
            System.out.println("attackedMonster name is " + attackedMonster.getCardName());
            if (!(Card.isCardAMonster(attackedMonster))) {
                return "there is no card to attack here";
            } else {
                String output = "";
                output = areContinuousSpellTrapOrMonsterEffectsPreventingMonsterFromAttacking(mainCard, opponentCardLocation, index);
                if (!output.equals("")){
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
            actions.add(new Action(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, 1, mainCard, targetingCards, null, null, null, null, null, null, null, null));
            uninterruptedActions.add(new Action(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER, 1, mainCard, targetingCards, null, null, null, null, null, null, null, null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER, 2, mainCard, targetingCards, null, null, null, null, null, null, null, null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER, 2, mainCard, targetingCards, null, null, null, null, null, null, null, null));
        }
        targetingCards.clear();
    }



    public String isSelectedCardCorrectForChainActivation(String string, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        boolean isAlreadyActivated = false;
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            isAlreadyActivated = spellCard.isCardAlreadyActivated();
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            isAlreadyActivated = trapCard.isCardAlreadyActivated();
        } else if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            ArrayList<BeingAttackedEffect> beingAttackedEffects = monsterCard.getBeingAttackedEffects();
            if (beingAttackedEffects.contains(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN) && beingAttackedEffects.contains(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN)) {
                return "select a card from your graveyard, hand, or deck to special summon";
            }
            // add suijin effect here too
            else {
                return "you can't choose this card for chain activation.\nselect another card";
            }
        } else {
            return "you can't choose this card for chain activation.\nselect another card";
        }
        if (isAlreadyActivated) {
            return "you have already activated this card\nselect another card";
        } else {
            String output = activateSpellTrapController.arePreparationsCompleteForSpellTrapActivation(index);
            System.out.println("output is "+output);
            if (output.startsWith("preparations for this")) {
                return output + "\nselect another card";
            } else if (!output.equals("nothing needed")) {
                activateSpellTrapController.setAreWeLookingForFurtherInputToActivateSpellTrap(true);
                activateSpellTrapController.setMainCardLocation(cardLocation);
                //activateSpellTrapController.setRedirectInputBeingProcessesInChain(true);
                selectCardController.resetSelectedCardLocationList();
                isClassWaitingForChainCardToBeSelected = false;
                return output;
                //This is when user has chosen the spell/trap he wants to activate but we have to prompt him to enter more input to activate his card
            } else {
                activateSpellTrapController.setMainCardLocation(cardLocation);
                System.out.println(duelBoard.getCardByCardLocation(cardLocation).getCardName()+" is being activated");
                isClassWaitingForChainCardToBeSelected = false;
                activateSpellTrapController.createActionForActivatingSpellTrap(index);
                selectCardController.resetSelectedCardLocationList();
                String canChainingOccur = activateSpellTrapController.canChainingOccur(index);
                //duelController.changeFakeTurn();
                //used to give fakeTurn as input
                if (canChainingOccur.equals("")) {
                    return Action.conductUninterruptedAction(index) + Action.conductAllActions(index);
                }
                //activateSpellTrapController.setGoingToChangeTurnsForChaining(true);
                return Action.conductUninterruptedAction(index) + canChainingOccur;
                //create Action For Spell Trap Activation
                //reset selections
                // check if chaining can occur again
            }

        }
    }
}
