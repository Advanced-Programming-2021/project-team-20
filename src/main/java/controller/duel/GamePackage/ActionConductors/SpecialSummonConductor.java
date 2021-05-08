package controller.duel.GamePackage.ActionConductors;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;

public class SpecialSummonConductor extends SendCardToGraveyardConductor{
    public static String conductSpecialSummoningActionUninterruptedAction(int index, int numberInListOfActions) {
        //if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            sendCardToGraveyardAfterRemoving(uninterruptedAction.getSpendingCards().get(i), index);
        }
        int turn = 0;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER)) {
            turn = 1;
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation() + " !!!");
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + " !!!");
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1)
            );
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getRowOfCardLocation() + " ***");
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getIndex() + " ***");
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
            turn = 2;
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation() + " !!!");
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + " !!!");
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + 1)
            );
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getRowOfCardLocation() + " ***");
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getIndex() + " ***");
        }
        for (int i = 0; i < uninterruptedAction.getCardsToBeDiscarded().size(); i++) {
            sendCardToGraveyardAfterRemoving(uninterruptedAction.getCardsToBeDiscarded().get(i), index);
        }
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.addCardToMonsterZone(mainCard, turn);
        mainCard.setCardPosition(uninterruptedAction.getCardPositionOfMainCard());
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        duelBoard.removeNullCardsFromHands();
        return "special summoned successfully";
    }

    public static String conductSpecialSummoningAction(int index, int numberInListOfActions) {
        return "";
    }
}
