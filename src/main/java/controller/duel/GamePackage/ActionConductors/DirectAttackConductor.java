package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.MonsterCardData.MonsterCard;

public class DirectAttackConductor {
    //private static boolean isDirectAttackCanceled;
    private static int actionTurn;
    private static ArrayList<Integer> playersLifePointsChange = new ArrayList<>();
    private static CardLocation attackingMonsterCardLocation;
    private static MonsterCard attackingMonsterCard;

    public static String DirectAttackConductor(int index, int numberInListOfActions) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        boolean isDirectAttackCanceled = action.isActionCanceled();
        if (!isDirectAttackCanceled) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
            actionTurn = action.getActionTurn();
            playersLifePointsChange.clear();
            playersLifePointsChange.add(0);
            playersLifePointsChange.add(0);
            attackingMonsterCardLocation = action.getMainCardLocation();
            Card attackingCard = duelBoard.getCardByCardLocation(attackingMonsterCardLocation);
            attackingMonsterCard = (MonsterCard) attackingCard;
            playersLifePointsChange.set(2-actionTurn, MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0));
            duelController.increaseLifePoints(playersLifePointsChange.get(0)*(-1), 1);
            duelController.increaseLifePoints(playersLifePointsChange.get(1)*(-1), 2);
            return "your opponent receives" + playersLifePointsChange.get(2 - actionTurn) + " battle damage";
        }
        return "action was canceled.";
    }

}
