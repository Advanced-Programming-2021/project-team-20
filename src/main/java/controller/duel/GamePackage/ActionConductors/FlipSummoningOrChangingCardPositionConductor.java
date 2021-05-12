package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePhaseControllers.SelectCardController;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

public class FlipSummoningOrChangingCardPositionConductor {
    private static boolean isActionCanceled = false;
    private static boolean isClassWaitingWaitingForPlayerToPickMonsterToDestroy = false;
    private static boolean shouldRedirectConductorToAvoidRepetition = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        FlipSummoningOrChangingCardPositionConductor.isActionCanceled = isActionCanceled;
    }

    public static boolean isIsClassWaitingWaitingForPlayerToPickMonsterToDestroy() {
        return isClassWaitingWaitingForPlayerToPickMonsterToDestroy;
    }

    public static String conductFlipSummoningOrChangingCardPositionUninterruptedAction(int index, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        if (!isActionCanceled){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            monsterCard.setCardPositionChanged(true);
            if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)){
                monsterCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
                //uninterruptedActions.remove(numberInListOfActions);
                return "flip summoned successfully";
            } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION)){
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)){
                    monsterCard.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
                } else if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)){
                    monsterCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
                }
                //actions.remove(numberInListOfActions);
                return "monster card position changed successfully";
            }
            // check effectts
            return "nothing is conducted";
        }
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_FLIP_SUMMONING_MONSTER) || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER)){
            return "flip summoning action was interrupted and therefore, canceled.";
        } else {
            return "changing card position action was interrupted and therefore, canceled.";
        }
    }

    public static String conductFlipSummoningOrChangingCardPosition(int index, int numberInListOfActions) {
        if (shouldRedirectConductorToAvoidRepetition){
            shouldRedirectConductorToAvoidRepetition = false;
            return "";
        }
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(uninterruptedAction.getFinalMainCardLocation());
        ArrayList<FlipEffect> flipEffects = monsterCard.getFlipEffects();
        if (flipEffects.contains(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD)){
            isClassWaitingWaitingForPlayerToPickMonsterToDestroy = true;
            return "flip summoned successfully\ndo you want to activate your monster card's effect?";
        }
        return "";
    }

    public String checkGivenInputForMonsterToDestroy(int index){
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
            isClassWaitingWaitingForPlayerToPickMonsterToDestroy = false;
            return "monster destroyed successfully\n" +Action.conductAllActions(index);
        }
    }

}

