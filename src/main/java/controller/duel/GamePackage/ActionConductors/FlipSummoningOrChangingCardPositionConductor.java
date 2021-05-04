package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.CardPosition;
import model.cardData.MonsterCardData.MonsterCard;

public class FlipSummoningOrChangingCardPositionConductor {
    private static boolean isActionCanceled = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        FlipSummoningOrChangingCardPositionConductor.isActionCanceled = isActionCanceled;
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
                uninterruptedActions.remove(numberInListOfActions);
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
        return "";
    }
}
