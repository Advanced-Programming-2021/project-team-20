package controller.duel.GamePackage.ActionConductors;

import controller.duel.CardEffects.MonsterEffectEnums.UponSummoningEffect;
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
import java.util.concurrent.Callable;

public class TributeSummonConductor{
    public static String conductTributeSummoningActionUninterruptedAction(int index, int numberInListOfActions) {
        //if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(uninterruptedAction.getSpendingCards().get(i), index);
        }
        int turn = 0;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER)) {
            turn = 1;
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation() + " !!!");
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + " !!!");
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1)
            );
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getRowOfCardLocation() + " ***");
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getIndex() + " ***");
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
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
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.addCardToMonsterZone(mainCard, turn);
        mainCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        return "tribute summoned successfully";
        //}
        //return "normal summoning action was interrupted and therefore, canceled.";
    }

    public static String conductTributeSummoningAction(int index, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (!action.isActionCanceled()) {
            CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
            Card mainCard = duelBoard.getCardByCardLocation(mainCardLocation);
            MonsterCard monsterCard = (MonsterCard) mainCard;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS)) {
                int actionTurn = action.getActionTurn();
                destroyAllOfOpponentsCards(actionTurn, index);
            }
        }
        return "";
    }
/*
    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index) {
        //System.out.println("sendCardToGraveyardAfterRemoving rowOfCardLocation" + targetingCardLocation.getRowOfCardLocation());
        //System.out.println("sendCardToGraveyardAfterRemoving card index" + targetingCardLocation.getIndex());
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card cardGoingToBeSentToGraveyard = duelBoard.getCardByCardLocation(targetingCardLocation);
        if (cardGoingToBeSentToGraveyard != null) {
            int graveyardToSendCardTo;
            RowOfCardLocation rowOfCardLocationOfThrownCard = targetingCardLocation.getRowOfCardLocation();
            if (rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                graveyardToSendCardTo = 1;
            } else {
                graveyardToSendCardTo = 2;
            }
            Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
            if (card == null) {

            }
            System.out.println("CARD WITH NAME" + card.getCardName() + "IS BEING SENT TO GRAVEYARD " + graveyardToSendCardTo);
            duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
        }
    }


 */
    public static void destroyAllOfOpponentsCards(int actionTurn, int index) {
        if (actionTurn == 1) {
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1), index);
            }
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1), index);
            }
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1), index);
        } else {
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1), index);
            }
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1), index);
            }
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1), index);
        }


    }
}
