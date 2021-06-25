package controller.duel.GamePackage;

import java.util.ArrayList;

import controller.duel.GamePackage.ActionConductors.*;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;

public class Action {
    private ActionType actionType;
    private int actionTurn;
    private boolean isActionCanceled;
    private boolean isSecondCardInHandAfterFirstCardInHand;
    private ArrayList<CardLocation> spendingCards;
    private ArrayList<CardLocation> targetingCards;
    private CardLocation mainCardLocation;
    private CardLocation finalMainCardLocation;
    private ArrayList<CardLocation> cardsToBeDiscarded;
    private ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo;
    private ArrayList<CardLocation> cardsToBeSpecialSummoned;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand;
    private ArrayList<CardLocation> cardsToBeDestroyed;
    private ArrayList<CardLocation> cardsToTakeControlOf;
    private ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard;
    private ArrayList<CardLocation> cardsToBeRitualSummoned;
    private ArrayList<CardPosition> cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition;
    private CardPosition cardPositionOfMainCard;
    private String optionalCardNameInput;
    private boolean damageInflictionIsPossible;
    private static int currentActionConducting = 0;
    private static String outputSentUntilNow = "";

    public Action(ActionType actionType, int actionTurn, CardLocation mainCardLocation, ArrayList<CardLocation> targetingCards, ArrayList<CardLocation> spendingCards
        , ArrayList<CardLocation> cardsToBeDiscarded, ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo
        , ArrayList<CardLocation> cardsToBeSpecialSummoned, ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand
        , ArrayList<CardLocation> cardsToBeDestroyed, ArrayList<CardLocation> cardsToTakeControlOf
        , ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard, ArrayList<CardLocation> cardsToBeRitualSummoned
        , CardPosition cardPosition, String optionalCardNameInput, ArrayList<CardPosition> cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition) {
        this.spendingCards = new ArrayList<>();
        this.targetingCards = new ArrayList<>();
        this.mainCardLocation = mainCardLocation;
        this.cardPositionOfMainCard = cardPosition;
        this.finalMainCardLocation = mainCardLocation;
        this.cardsToBeDiscarded = new ArrayList<>();
        this.cardsToBeChosenToApplyEquipSpellTo = new ArrayList<>();
        this.cardsToBeSpecialSummoned = new ArrayList<>();
        this.cardsToBeChosenFromDeckAndAddedToHand = new ArrayList<>();
        this.cardsToBeDestroyed = new ArrayList<>();
        this.cardsToTakeControlOf = new ArrayList<>();
        this.cardsToBeChosenFromDeckAndSentToGraveyard = new ArrayList<>();
        this.cardsToBeRitualSummoned = new ArrayList<>();
        this.cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition = new ArrayList<>();
        this.actionType = actionType;
        this.actionTurn = actionTurn;
        this.isActionCanceled = false;
        this.isSecondCardInHandAfterFirstCardInHand = false;
        this.damageInflictionIsPossible = true;
        this.optionalCardNameInput = optionalCardNameInput;
        if (spendingCards != null) {
            this.spendingCards.addAll(spendingCards);
        }
        if (targetingCards != null) {
            this.targetingCards.addAll(targetingCards);
        }
        if (cardsToBeDiscarded != null) {
            this.cardsToBeDiscarded.addAll(cardsToBeDiscarded);
        }
        if (cardsToBeChosenToApplyEquipSpellTo != null) {
            this.cardsToBeChosenToApplyEquipSpellTo.addAll(cardsToBeChosenToApplyEquipSpellTo);
        }
        if (cardsToBeSpecialSummoned != null) {
            this.cardsToBeSpecialSummoned.addAll(cardsToBeSpecialSummoned);
        }
        if (cardsToBeChosenFromDeckAndAddedToHand != null) {
            this.cardsToBeChosenFromDeckAndAddedToHand.addAll(cardsToBeChosenFromDeckAndAddedToHand);
        }
        if (cardsToBeDestroyed != null) {
            this.cardsToBeDestroyed.addAll(cardsToBeDestroyed);
        }
        if (cardsToTakeControlOf != null) {
            this.cardsToTakeControlOf.addAll(cardsToTakeControlOf);
        }
        if (cardsToBeChosenFromDeckAndSentToGraveyard != null) {
            this.cardsToBeChosenFromDeckAndSentToGraveyard.addAll(cardsToBeChosenFromDeckAndSentToGraveyard);
        }
        if (cardsToBeRitualSummoned != null) {
            this.cardsToBeRitualSummoned.addAll(cardsToBeRitualSummoned);
        }
        if (cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition != null) {
            this.cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition.addAll(cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition);
        }
    }
/*
    public Action(ArrayList<CardLocation> cardsToBeDiscarded, ActionType actionType, ArrayList<CardLocation> targetingCards, CardLocation mainCardLocation) {
        this.spendingCards = new ArrayList<>();
        this.targetingCards = new ArrayList<>();
        this.actionType = actionType;
        if (targetingCards != null) {
            this.targetingCards.addAll(targetingCards);
        }
        this.mainCardLocation = mainCardLocation;
    }


 */

    public ActionType getActionType() {
        return actionType;
    }

    public int getActionTurn() {
        return actionTurn;
    }

    public static int getCurrentActionConducting() {
        return currentActionConducting;
    }

    public CardPosition getCardPositionOfMainCard() {
        return cardPositionOfMainCard;
    }

    public ArrayList<CardLocation> getSpendingCards() {
        return spendingCards;
    }

    public ArrayList<CardLocation> getTargetingCards() {
        return targetingCards;
    }

    public CardLocation getMainCardLocation() {
        return mainCardLocation;
    }

    public CardLocation getFinalMainCardLocation() {
        return finalMainCardLocation;
    }

    public boolean isActionCanceled() {
        return isActionCanceled;
    }

    public boolean isSecondCardInHandAfterFirstCardInHand() {
        return isSecondCardInHandAfterFirstCardInHand;
    }

    public boolean isDamageInflictionIsPossible() {
        return damageInflictionIsPossible;
    }

    public String getOptionalCardNameInput() {
        return optionalCardNameInput;
    }

    public ArrayList<CardLocation> getCardsToBeDiscarded() {
        return cardsToBeDiscarded;
    }

    public ArrayList<CardLocation> getCardsToBeChosenToApplyEquipSpellTo() {
        return cardsToBeChosenToApplyEquipSpellTo;
    }

    public ArrayList<CardLocation> getCardsToBeSpecialSummoned() {
        return cardsToBeSpecialSummoned;
    }

    public ArrayList<CardLocation> getCardsToBeChosenFromDeckAndAddedToHand() {
        return cardsToBeChosenFromDeckAndAddedToHand;
    }

    public ArrayList<CardLocation> getCardsToBeChosenFromDeckAndSentToGraveyard() {
        return cardsToBeChosenFromDeckAndSentToGraveyard;
    }

    public ArrayList<CardLocation> getCardsToBeRitualSummoned() {
        return cardsToBeRitualSummoned;
    }

    public ArrayList<CardPosition> getCardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition() {
        return cardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition;
    }

    public ArrayList<CardLocation> getCardsToBeDestroyed() {
        return cardsToBeDestroyed;
    }

    public ArrayList<CardLocation> getCardsToTakeControlOf() {
        return cardsToTakeControlOf;
    }

    public void setFinalMainCardLocation(CardLocation finalMainCardLocation) {
        this.finalMainCardLocation = finalMainCardLocation;
    }

    public void setActionCanceled(boolean actionCanceled) {
        isActionCanceled = actionCanceled;
    }

    public void setSecondCardInHandAfterFirstCardInHand(boolean secondCardInHandAfterFirstCardInHand) {
        this.isSecondCardInHandAfterFirstCardInHand = secondCardInHandAfterFirstCardInHand;
    }

    public void setDamageInflictionIsPossible(boolean damageInflictionIsPossible) {
        this.damageInflictionIsPossible = damageInflictionIsPossible;
    }

    public static String conductAllActions(int index) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        //if (actions.get(0).getActionType().equals())
        StringBuilder output = new StringBuilder();
        int startIndex = currentActionConducting;
     //   System.out.println("actions.size() " + actions.size() + " currently activating");
        for (int j = 0; j < actions.size(); j++) {
     //       System.out.println("action " + j + " " + actions.get(j).getActionType().toString());
        }
        //int maximumSize = actions.size();
        //int mirageSize = maximumSize;
        boolean breakStatement = false;
        while (actions.size() != 0 && !breakStatement) {
            //currentActionConducting = i;
            Action action = actions.get(actions.size() - 1);
            AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager.getAttackMonsterToMonsterConductorsByIndex(index);
            if (action.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
                output.append(NormalSummonConductor.conductNormalSummoningAction(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
                output.append(TributeSummonConductor.conductTributeSummoningAction(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
                output.append(SpecialSummonConductor.conductSpecialSummoningAction(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SETTING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
                output.append(SettingCardConductor.conductNormalSettingAction(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD) || action.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
                output.append(SettingCardConductor.conductNormalSettingAction(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) ||
                action.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER) ||
                action.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) ||
                action.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)) {
                output.append(FlipSummoningOrChangingCardPositionConductor.conductFlipSummoningOrChangingCardPosition(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
                String string = attackMonsterToMonsterConductor.AttackConductor(index, actions.size() - 1);
                output.append(string);
            } else if (action.getActionType().equals(ActionType.ALLY_DIRECT_ATTACKING) || action.getActionType().equals(ActionType.OPPONENT_DIRECT_ATTACKING)) {
                output.append(DirectAttackConductor.DirectAttackConductor(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || action.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
                output.append(ActivateSpellConductor.conductActivatingSpell(index, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || action.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
                output.append(ActivateTrapConductor.conductActivatingTrap(index, actions.size() - 1));
            }
            output.append("\n");
            outputSentUntilNow = output.toString();
            if (attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy() || attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()
                || attackMonsterToMonsterConductor.isClassWaitingForUserToChooseAttackPositionOrDefensePosition() || attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()
                || FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy() || FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()
                || NormalSummonConductor.isPromptingUserToActivateMonsterEffect() || NormalSummonConductor.isIsClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
                breakStatement = true;
            } else {
                String isGameOver = isGameOver(output, index, action);
                if (!isGameOver.equals("")) {
                    return isGameOver;
                }
                if (actions.size() > 1) {
              //      System.out.println("NOW AN ACTION IS BEING PROCESSES WITH ACTION TURN " + action.getActionTurn());
              //      System.out.println("THE ACTION TO BE PROCESSED HAS ACTION TURN " + actions.get(actions.size() - 2).getActionTurn());
                } else {
             //       System.out.println("ACTION SIZE WAS 1");
                }

                if (actions.size() > 1 && action.getActionTurn() != actions.get(actions.size() - 2).getActionTurn()) {
                    GameManager.getDuelControllerByIndex(index).changeFakeTurn();
                }
                uninterruptedActions.remove(uninterruptedActions.size() - 1);
                actions.remove(actions.size() - 1);
            }
        }
        if (output.toString().equals("")) {
            return "nothing is conducted";
        }
        /*
        if (currentActionConducting == actions.size() - 1) {
            GameManager.getDuelControllerByIndex(index).setFakeTurn(GameManager.getDuelControllerByIndex(index).getTurn());
            System.out.println("fakeTurn DuelController is now " + GameManager.getDuelControllerByIndex(0).getFakeTurn());
            currentActionConducting = 0;
            actions.clear();
            System.out.println("!actions.size is now" + GameManager.getActionsByIndex(0).size());
            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
            uninterruptedActions.clear();
        }

         */
        return output.toString();
    }

    public static String isGameOver(StringBuilder output, int index, Action action) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        if (duelController.getLifePoints().get(2 - action.getActionTurn()) <= 0) {
            if (duelController.getNumberOfRounds() == 1 && duelController.getCurrentRound() == 1) {
                return output.toString() + duelController.endGame(action.getActionTurn(), index);
            } else if (duelController.getNumberOfRounds() == 3 && duelController.getCurrentRound() == 3) {
                return output.toString() + duelController.endGame(action.getActionTurn(), index);
            } else {
                return output.toString() + duelController.endOneRoundOfDuel(action.getActionTurn());
            }
        } else if (duelController.getLifePoints().get(action.getActionTurn() - 1) <= 0) {
            if (duelController.getNumberOfRounds() == 1 && duelController.getCurrentRound() == 1) {
                return output.toString() + duelController.endGame(3 - action.getActionTurn(), index);
            } else if (duelController.getNumberOfRounds() == 3 && duelController.getCurrentRound() == 3) {
                return output.toString() + duelController.endGame(3 - action.getActionTurn(), index);
            } else {
                return output.toString() + duelController.endOneRoundOfDuel(3 - action.getActionTurn());
            }
        } else {
            return "";
        }
    }

    public static String conductUninterruptedAction(int index) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        //if (actions.get(0).getActionType().equals())
        String output = "";
        // there shouldn't be a for
      //  System.out.println("uninterruptedActions.size() " + uninterruptedActions.size() + " currently activating");
        for (int j = 0; j < uninterruptedActions.size(); j++) {
      //      System.out.println("uninterruptedAction" + j + uninterruptedActions.get(j).getActionType().toString());
        }
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
            output = NormalSummonConductor.conductNormalSummoningActionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
            output = TributeSummonConductor.conductTributeSummoningActionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
            output = SpecialSummonConductor.conductSpecialSummoningActionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            output = SettingCardConductor.conductNormalSettingActionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            output = SettingCardConductor.conductNormalSettingActionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) ||
            uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER) ||
            uninterruptedAction.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) ||
            uninterruptedAction.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)) {
            output = FlipSummoningOrChangingCardPositionConductor.conductFlipSummoningOrChangingCardPositionUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {

        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_DIRECT_ATTACKING) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_DIRECT_ATTACKING)) {

        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            output = ActivateSpellConductor.conductActivatingSpellUninterruptedAction(index, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            output = ActivateTrapConductor.conductActivatingTrapUninterruptedAction(index, uninterruptedActions.size() - 1);
        }
        if (output.equals("")) {
            return "nothing is conducted";
        }
        return output;
    }

}
