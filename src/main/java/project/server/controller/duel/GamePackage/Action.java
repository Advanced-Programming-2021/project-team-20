package project.server.controller.duel.GamePackage;

import project.server.controller.duel.GamePackage.ActionConductors.*;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.ActionType;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;

import java.util.ArrayList;

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

    public static String conductAllActions(String token) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        StringBuilder output = new StringBuilder();
        boolean breakStatement = false;
        while (actions.size() != 0 && !breakStatement) {
            Action action = actions.get(actions.size() - 1);
            AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager.getAttackMonsterToMonsterConductorsByIndex(token);
            if (action.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
                output.append(NormalSummonConductor.conductNormalSummoningAction(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
                output.append(TributeSummonConductor.conductTributeSummoningAction(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
                output.append(SpecialSummonConductor.conductSpecialSummoningAction(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SETTING_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
                output.append(SettingCardConductor.conductNormalSettingAction(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD) || action.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
                output.append(SettingCardConductor.conductNormalSettingAction(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) ||
                action.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER) ||
                action.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) ||
                action.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)) {
                output.append(FlipSummoningOrChangingCardPositionConductor.conductFlipSummoningOrChangingCardPosition(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || action.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {
                String string = attackMonsterToMonsterConductor.AttackConductor(token, actions.size() - 1);
                output.append(string);
            } else if (action.getActionType().equals(ActionType.ALLY_DIRECT_ATTACKING) || action.getActionType().equals(ActionType.OPPONENT_DIRECT_ATTACKING)) {
                output.append(DirectAttackConductor.DirectAttackConductor(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || action.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
                output.append(ActivateSpellConductor.conductActivatingSpell(token, actions.size() - 1));
            } else if (action.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || action.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
                output.append(ActivateTrapConductor.conductActivatingTrap(token, actions.size() - 1));
            }
            output.append("\n");
            outputSentUntilNow = output.toString();
            if (attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy() || attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()
                || attackMonsterToMonsterConductor.isClassWaitingForUserToChooseAttackPositionOrDefensePosition() || attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()
                || FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy() || FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()
                || NormalSummonConductor.isPromptingUserToActivateMonsterEffect() || NormalSummonConductor.isIsClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
                breakStatement = true;
            } else {
                String isGameOver = isGameOver(output, token, action);
                if (!isGameOver.equals("")) {
                    return isGameOver;
                }

                if (actions.size() > 1 && action.getActionTurn() != actions.get(actions.size() - 2).getActionTurn()) {
                    GameManager.getDuelControllerByIndex(token).changeFakeTurn();
                }
                uninterruptedActions.remove(uninterruptedActions.size() - 1);
                actions.remove(actions.size() - 1);
            }
        }
        if (output.toString().equals("")) {
            return "nothing is conducted";
        }
        return output.toString();
    }

    public static String isGameOver(StringBuilder output, String token, Action action) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        if (duelController.getLifePoints().get(2 - action.getActionTurn()) <= 0) {
            if (duelController.getNumberOfRounds() == 1 && duelController.getCurrentRound() == 1) {
                return output.toString() + duelController.endGame(action.getActionTurn(), token);
            } else if (duelController.getNumberOfRounds() == 3 && duelController.getCurrentRound() == 3) {
                return output.toString() + duelController.endGame(action.getActionTurn(), token);
            } else {
                return output.toString() + duelController.endOneRoundOfDuel(action.getActionTurn(), token);
            }
        } else if (duelController.getLifePoints().get(action.getActionTurn() - 1) <= 0) {
            if (duelController.getNumberOfRounds() == 1 && duelController.getCurrentRound() == 1) {
                return output.toString() + duelController.endGame(3 - action.getActionTurn(), token);
            } else if (duelController.getNumberOfRounds() == 3 && duelController.getCurrentRound() == 3) {
                return output.toString() + duelController.endGame(3 - action.getActionTurn(), token);
            } else {
                return output.toString() + duelController.endOneRoundOfDuel(3 - action.getActionTurn(), token);
            }
        } else {
            return "";
        }
    }

    public static String conductUninterruptedAction(String token) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        String output = "";
        Action uninterruptedAction = uninterruptedActions.get(uninterruptedActions.size() - 1);
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
            output = NormalSummonConductor.conductNormalSummoningActionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
            output = TributeSummonConductor.conductTributeSummoningActionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
            output = SpecialSummonConductor.conductSpecialSummoningActionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            output = SettingCardConductor.conductNormalSettingActionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            output = SettingCardConductor.conductNormalSettingActionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) ||
            uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER) ||
            uninterruptedAction.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) ||
            uninterruptedAction.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)) {
            output = FlipSummoningOrChangingCardPositionConductor.conductFlipSummoningOrChangingCardPositionUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_MONSTER_ATTACKING_OPPONENT_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_MONSTER_ATTACKING_ALLY_MONSTER)) {

        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_DIRECT_ATTACKING) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_DIRECT_ATTACKING)) {

        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_SPELL) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_SPELL)) {
            output = ActivateSpellConductor.conductActivatingSpellUninterruptedAction(token, uninterruptedActions.size() - 1);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_ACTIVATING_TRAP) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_ACTIVATING_TRAP)) {
            output = ActivateTrapConductor.conductActivatingTrapUninterruptedAction(token, uninterruptedActions.size() - 1);
        }
        if (output.equals("")) {
            return "nothing is conducted";
        }
        return output;
    }

}
