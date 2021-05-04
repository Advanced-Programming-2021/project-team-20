package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

public class NormalSummonConductor {
    private static boolean isActionCanceled = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        NormalSummonConductor.isActionCanceled = isActionCanceled;
    }
    public static String conductNormalSummoningActionUninterruptedAction(int index, int numberInListOfActions) {
        //if (!isActionCanceled){
            ArrayList<Action> uninterruptedActions = GameManager.getActionsByIndex(index);
            Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            ArrayList<Card> cardsToBeTributed = new ArrayList<>();
            int turn = 0;
            if (uninterruptedAction.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER)) {
                turn = 1;
                uninterruptedAction.setFinalMainCardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true));
            } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
                turn = 2;
                uninterruptedAction.setFinalMainCardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false));
            }
            for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
                cardsToBeTributed.add(duelBoard.getCardByCardLocation(uninterruptedAction.getSpendingCards().get(i)));
                duelBoard.removeCardByCardLocation(uninterruptedAction.getSpendingCards().get(i));
                duelBoard.addCardToGraveyard(cardsToBeTributed.get(i), turn);
            }
            Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            //System.out.println(duelBoard.getCardByCardLocation(action.getMainCardLocation()).getCardName());

            duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
            duelBoard.addCardToMonsterZone(mainCard, turn);
            mainCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
            MonsterCard monsterCard = (MonsterCard) mainCard;
            monsterCard.setCardPositionChanged(true);
            return "summoned successfully";
        //}
        //return "normal summoning action was interrupted and therefore, canceled.";
    }
    public static String conductNormalSummoningAction(int index, int numberInListOfActions) {
        return "";
    }
}
