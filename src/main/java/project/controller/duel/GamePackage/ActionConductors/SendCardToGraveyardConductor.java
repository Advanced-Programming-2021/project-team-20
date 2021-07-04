package project.controller.duel.GamePackage.ActionConductors;

import project.controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.GamePhaseControllers.PhaseController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;

import java.util.ArrayList;

public class SendCardToGraveyardConductor {

    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card cardGoingToBeSentToGraveyard = duelBoard.getCardByCardLocation(targetingCardLocation);
        if (cardGoingToBeSentToGraveyard != null) {
            int graveyardToSendCardTo;
            RowOfCardLocation rowOfCardLocationOfThrownCard = targetingCardLocation.getRowOfCardLocation();
            if (rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ||
                rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                graveyardToSendCardTo = 1;
            } else {
                graveyardToSendCardTo = 2;
            }
            duelBoard.destroyEquipSpellsRelatedToThisCard(targetingCardLocation, graveyardToSendCardTo);
            Card card = duelBoard.getCardByCardLocation(targetingCardLocation);
            if (Card.isCardAMonster(card)) {
                checkIfPlayerShouldDrawACard(graveyardToSendCardTo, index);
            }
            if (Card.isCardASpell(card)) {
                duelBoard.removeFieldSpellEffectsOnCardsWhenSpellFieldIsDestroyed(targetingCardLocation);
                duelBoard.removeEquipSpellEffectsOnCardsWhenEquipSpellIsDestroyed(targetingCardLocation);
            }
            Card removedCard = removeCardAndGetRemovedCard(targetingCardLocation, index);
            duelBoard.addCardToGraveyard(removedCard, graveyardToSendCardTo);
            GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + targetingCardLocation.getRowOfCardLocation()
                + " " + targetingCardLocation.getIndex() + " is being added to graveyard zone " + graveyardToSendCardTo + " and should finally be FACE_UP_ATTACK_POSITION or FACE_UP_ACTIVATED_POSITION ");
            duelBoard.refreshCharacteristicsOfACardSentToGraveyard(removedCard);
        }
    }

    public static String checkIfPlayerShouldDrawACard(int graveyardToSendCardTo, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> spellCards;
        if (graveyardToSendCardTo == 1) {
            spellCards = duelBoard.getAllySpellCards();
        } else {
            spellCards = duelBoard.getOpponentSpellCards();
        }
        for (int i = 0; i < spellCards.size(); i++) {
            if (Card.isCardASpell(spellCards.get(i))) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.IF_A_MONSTER_OWNER_CONTROLS_IS_DESTROYED_DRAW_1_CARD)
                    && !spellCard.isOncePerTurnCardEffectUsed()){
                    spellCard.setOncePerTurnCardEffectUsed(true);
                    PhaseController phaseController = GameManager.getPhaseControllerByIndex(index);
                    String output = phaseController.ifPossibleDrawACard(index, graveyardToSendCardTo);
                    if (output.equals("")) {
                        return output;
                    }
                    DuelController duelController = GameManager.getDuelControllerByIndex(index);
                    String playerUsername = duelController.getPlayingUsernameByTurn(graveyardToSendCardTo);
                    return output + " of player " + playerUsername;
                }
            }
        }
        return "";
    }
}
