package project.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import project.controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.ActionType;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.GamePhaseControllers.SelectCardController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

public class FlipSummoningOrChangingCardPositionConductor {
    private static CardLocation monsterCardToBeFlippedOrPositionChanged;
    private static boolean isActionCanceled = false;
    private static boolean isClassWaitingForPlayerToPickMonsterToDestroy = false;
    private static boolean shouldRedirectConductorToAvoidRepetition = false;
    private static boolean promptingUserToActivateMonsterEffect = false;

    public static boolean isPromptingUserToActivateMonsterEffect() {
        return promptingUserToActivateMonsterEffect;
    }


    public static boolean isClassWaitingForPlayerToPickMonsterToDestroy() {
        return isClassWaitingForPlayerToPickMonsterToDestroy;
    }

    public static String conductFlipSummoningOrChangingCardPositionUninterruptedAction(int index, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        if (!isActionCanceled) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            monsterCardToBeFlippedOrPositionChanged = uninterruptedAction.getMainCardLocation();
            MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(monsterCardToBeFlippedOrPositionChanged);
            monsterCard.setCardPositionChanged(true);
            if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) {
                monsterCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
                GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + monsterCardToBeFlippedOrPositionChanged.getRowOfCardLocation()
                    + " " + monsterCardToBeFlippedOrPositionChanged.getIndex() + " is being stayed at monster zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ATTACK_POSITION");
                return "flip summoned successfully";
            } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)) {
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                    monsterCard.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
                    GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + monsterCardToBeFlippedOrPositionChanged.getRowOfCardLocation()
                        + " " + monsterCardToBeFlippedOrPositionChanged.getIndex() + " is being stayed at monster zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_DEFENSE_POSITION");
                } else if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                    monsterCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
                    GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + monsterCardToBeFlippedOrPositionChanged.getRowOfCardLocation()
                        + " " + monsterCardToBeFlippedOrPositionChanged.getIndex() + " is being stayed at monster zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ATTACK_POSITION");
                }
                return "monster card position changed successfully";
            }
            return "nothing is conducted";
        }
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)) {
            return "flip summoning action was interrupted and therefore, canceled.";
        } else {
            return "changing card position action was interrupted and therefore, canceled.";
        }
    }

    public static String conductFlipSummoningOrChangingCardPosition(int index, int numberInListOfActions) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        if (!action.isActionCanceled()) {
            if (shouldRedirectConductorToAvoidRepetition) {
                shouldRedirectConductorToAvoidRepetition = false;
                return "";
            }
            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
            Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(uninterruptedAction.getFinalMainCardLocation());
            ArrayList<FlipEffect> flipEffects = monsterCard.getFlipEffects();
            if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                shouldRedirectConductorToAvoidRepetition = true;
                promptingUserToActivateMonsterEffect = true;
                return "do you want to activate your monster card's effect?";
            }
        }
        return "";
    }

    public static String defendingMonsterEffectAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)no(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            promptingUserToActivateMonsterEffect = false;
            duelController.changeFakeTurn();
        } else {
            String anotherRegex = "(?<=\\n|^)yes(?=\\n|$)";
            Matcher newMatcher = Utility.getCommandMatcher(string, anotherRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(newMatcher)) {
                DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
                MonsterCard defendingMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(monsterCardToBeFlippedOrPositionChanged);
                defendingMonsterCard.setOncePerTurnCardEffectUsed(true);
                promptingUserToActivateMonsterEffect = false;
                ArrayList<FlipEffect> flipEffects = defendingMonsterCard.getFlipEffects();
                if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)) {
                    isClassWaitingForPlayerToPickMonsterToDestroy = true;
                    return "select one monster on the field to destroy";
                }
            }
        }
        return Action.conductAllActions(0);
    }

    public static String checkGivenInputForMonsterToDestroy(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        if (Card.isCardATrap(card) || Card.isCardASpell(card)) {
            return "this card cannot be chosen for destroying.\nselect another card";
        } else if (Card.isCardAMonster(card) &&
            !(cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            return "you can't select this monster for destroying.\nselect another card";
        } else {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, index);
            isClassWaitingForPlayerToPickMonsterToDestroy = false;
            return "monster destroyed successfully\n" + Action.conductAllActions(index);
        }
    }

}
