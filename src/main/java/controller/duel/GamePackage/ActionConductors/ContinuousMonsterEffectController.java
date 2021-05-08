package controller.duel.GamePackage.ActionConductors;

import controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;

public class ContinuousMonsterEffectController {
    public boolean areContinuousMonsterCardEffectsPreventingUserFromActivatingTrap(CardLocation spellTrapCardLocation, int index){
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        RowOfCardLocation rowOfCardLocation = spellTrapCardLocation.getRowOfCardLocation();
        ArrayList<Card> otherPlayerMonsterCards;
        RowOfCardLocation otherPlayerRowOfCardLocation;
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)){
            otherPlayerMonsterCards = duelBoard.getOpponentMonsterCards();
            otherPlayerRowOfCardLocation = RowOfCardLocation.OPPONENT_MONSTER_ZONE;
        } else {
            otherPlayerMonsterCards = duelBoard.getAllyMonsterCards();
            otherPlayerRowOfCardLocation = RowOfCardLocation.ALLY_MONSTER_ZONE;
        }
        for (int i = 0; i < otherPlayerMonsterCards.size();i++){
            Card card = duelBoard.getCardByCardLocation(new CardLocation(otherPlayerRowOfCardLocation, i+1));
            if (Card.isCardAMonster(card)){
                MonsterCard monsterCard = (MonsterCard) card;
                ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = monsterCard.getContinuousMonsterEffects();
                if (continuousMonsterEffects.contains(ContinuousMonsterEffect.OPPONENT_CANNOT_ACTIVATE_TRAP_CARDS_WHILE_THIS_CARD_IS_FACE_UP)){
                    if (monsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) || monsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
