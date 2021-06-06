package project.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

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
            playersLifePointsChange.set(2 - actionTurn, MonsterCard.giveATKDEFConsideringEffects("attack", attackingMonsterCardLocation, 0));
            duelController.increaseLifePoints(playersLifePointsChange.get(0) * (-1), 1);
            duelController.increaseLifePoints(playersLifePointsChange.get(1) * (-1), 2);
            attackingMonsterCard.setHasCardAlreadyAttacked(true);
            return "your opponent receives" + playersLifePointsChange.get(2 - actionTurn) + " battle damage";
        } else {
            if (attackingMonsterCard != null){
                attackingMonsterCard.setHasCardAlreadyAttacked(true);
            }
        }
        return "action was canceled.";
    }

}
