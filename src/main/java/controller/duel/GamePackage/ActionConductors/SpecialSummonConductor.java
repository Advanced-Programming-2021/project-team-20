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
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;

import java.util.ArrayList;

public class SpecialSummonConductor {
    public static String conductSpecialSummoningActionUninterruptedAction(int index, int numberInListOfActions) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(uninterruptedAction.getSpendingCards().get(i), index);
        }
        int turn = 0;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER)) {
            turn = 1;
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1)
            );
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER)) {
            turn = 2;
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + 1)
            );
        }
        CardLocation initialCardLocationOfMonster = uninterruptedAction.getMainCardLocation();
        tendToFirstAndSecondCardsInHandWithBadIndex(index, numberInListOfActions, initialCardLocationOfMonster);
        for (int i = 0; i < uninterruptedAction.getCardsToBeDiscarded().size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(uninterruptedAction.getCardsToBeDiscarded().get(i), index);
        }
        CardLocation correctCardLocationOfMonster;
        if (uninterruptedAction.isSecondCardInHandAfterFirstCardInHand()){
            correctCardLocationOfMonster = new CardLocation(initialCardLocationOfMonster.getRowOfCardLocation(), initialCardLocationOfMonster.getIndex() - 1);
        } else {
            correctCardLocationOfMonster = new CardLocation(initialCardLocationOfMonster.getRowOfCardLocation(), initialCardLocationOfMonster.getIndex());
        }
        Card mainCard = duelBoard.getCardByCardLocation(correctCardLocationOfMonster);
        duelBoard.removeCardByCardLocation(correctCardLocationOfMonster);
        duelBoard.addCardToMonsterZone(mainCard, turn);
        mainCard.setCardPosition(uninterruptedAction.getCardPositionOfMainCard());
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        return "special summoned successfully";
    }

    public static String conductSpecialSummoningAction(int index, int numberInListOfActions) {
        return "";
    }

    private static void tendToFirstAndSecondCardsInHandWithBadIndex(int index, int numberInListOfActions, CardLocation mainMonsterCardLocation) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(index).get(numberInListOfActions);
        if (uninterruptedAction.getCardsToBeDiscarded().size() > 0) {
            ArrayList<CardLocation> cardsToBeDiscarded = uninterruptedAction.getCardsToBeDiscarded();
            int indexOfCardToBeDiscarded = cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1).getIndex();
            uninterruptedAction.setSecondCardInHandAfterFirstCardInHand(indexOfCardToBeDiscarded < mainMonsterCardLocation.getIndex());
        }
    }
}
