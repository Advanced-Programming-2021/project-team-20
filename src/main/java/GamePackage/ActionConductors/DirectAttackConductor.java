package GamePackage.ActionConductors;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.CardPosition;
import CardData.MonsterCardData.MonsterCard;
import GamePackage.Action;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import PreliminaryPackage.GameManager;

import java.util.ArrayList;

public class DirectAttackConductor {
    private static boolean isDirectAttackCanceled;
    private static int actionTurn;
    private static ArrayList<Integer> playersLifePointsChange = new ArrayList<>();
    private static CardLocation attackingMonsterCardLocation;
    private static MonsterCard attackingMonsterCard;

    public static String DirectAttackConductor(int index, int numberInListOfActions) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        isDirectAttackCanceled = action.isActionCanceled();
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
