package sample.controller.duel.GamePackage.ActionConductors;

import sample.controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import sample.controller.duel.GamePackage.DuelBoard;
import sample.controller.duel.PreliminaryPackage.GameManager;
import sample.model.cardData.General.Card;
import sample.model.cardData.General.CardLocation;
import sample.model.cardData.General.CardPosition;
import sample.model.cardData.General.RowOfCardLocation;
import sample.model.cardData.MonsterCardData.MonsterCard;

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
