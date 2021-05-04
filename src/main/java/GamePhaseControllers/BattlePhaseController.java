package GamePhaseControllers;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.CardPosition;
import CardData.General.RowOfCardLocation;
import CardData.MonsterCardData.MonsterCard;
import GamePackage.Action;
import GamePackage.ActionType;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import PreliminaryPackage.GameManager;
import Utility.Utility;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;

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
