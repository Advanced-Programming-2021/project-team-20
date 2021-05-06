package controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import controller.duel.CardEffects.MonsterEffectEnums.UponSummoningEffect;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

public class NormalSummonConductor {
    private static boolean isActionCanceled = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        NormalSummonConductor.isActionCanceled = isActionCanceled;
    }
    public static String conductNormalSummoningActionUninterruptedAction(int index, int numberInListOfActions) {
        //if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> cardsToBeTributed = new ArrayList<>();
        int turn = 0;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_NORMAL_SUMMONING_MONSTER)) {
            turn = 1;
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation()+" !!!");
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex()+" !!!");
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex()+1)
            );
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getRowOfCardLocation()+" ***");
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getIndex()+" ***");
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER)) {
            turn = 2;
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation()+" !!!");
            System.out.println(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex()+" !!!");
            uninterruptedAction.setFinalMainCardLocation(
                new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex()+1)
            );
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getRowOfCardLocation()+" ***");
            System.out.println(uninterruptedAction.getFinalMainCardLocation().getIndex()+" ***");
        }
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            cardsToBeTributed.add(duelBoard.getCardByCardLocation(uninterruptedAction.getSpendingCards().get(i)));
            duelBoard.removeCardByCardLocation(uninterruptedAction.getSpendingCards().get(i));
            duelBoard.addCardToGraveyard(cardsToBeTributed.get(i), turn);
        }
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        //System.out.println(duelBoard.getCardByCardLocation(action.getMainCardLocation()).getCardName());

        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.addCardToMonsterZone(mainCard, turn);
        mainCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        return "summoned successfully";
        //}
        //return "normal summoning action was interrupted and therefore, canceled.";
    }
    public static String conductNormalSummoningAction(int index, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (!action.isActionCanceled()){
            CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
            Card mainCard = duelBoard.getCardByCardLocation(mainCardLocation);
            MonsterCard monsterCard = (MonsterCard) mainCard;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED)){
                monsterCard.setAttackPower(1900);
            }
        }
        return "";
    }
}
