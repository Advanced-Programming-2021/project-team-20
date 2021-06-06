package project.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import project.controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

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
