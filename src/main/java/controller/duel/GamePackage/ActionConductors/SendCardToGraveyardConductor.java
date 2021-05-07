package controller.duel.GamePackage.ActionConductors;

import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;

public class SendCardToGraveyardConductor {

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
            duelBoard.destroyEquipSpellsRelatedToThisCard(targetingCardLocation, graveyardToSendCardTo);
            Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
            if (card == null) {

            }
            System.out.println("CARD WITH NAME" + card.getCardName() + "IS BEING SENT TO GRAVEYARD " + graveyardToSendCardTo);
            duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
            duelBoard.refreshCharacteristicsOfACardSentToGraveyard(card);
        }
    }
}
