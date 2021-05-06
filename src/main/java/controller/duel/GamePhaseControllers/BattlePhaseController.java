package controller.duel.GamePhaseControllers;

import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardPosition;
import model.cardData.MonsterCardData.MonsterCard;

public class BattlePhaseController extends ChainController{

    protected String checkAttackWithCard(Card card, int index){
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
            return "you can't attack with this card";
        } else {
            return checkPhase(card, index);
        }
    }

    protected String checkPhase(Card card, int index) {
        String resultOfChecking = Utility.areWeInBattlePhase(0);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkPositionForAttack(card, index);
        }
    }

    protected String checkPositionForAttack(Card card, int index) {
        if (!(card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION))) {
            return "you can't attack with this card";
        } else {
            return checkAlreadyAttacked(card ,index);
        }
    }

    protected String checkAlreadyAttacked(Card card, int index){
        MonsterCard monsterCard = (MonsterCard)  card;
        if (monsterCard.isCardAttacked()){
            return "this card already attacked";
        } else {
            return "";
            // return checkIndexOfAttackedMonster(card, index);
        }
    }

}
