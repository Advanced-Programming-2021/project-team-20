package project.server.controller.duel.GamePackage.ActionConductors;

import project.model.MonsterEffectEnums.UponSummoningEffect;
import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TributeSummonConductor {
    public static String conductTributeSummoningActionUninterruptedAction(String token, int numberInListOfActions) {
        //if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(uninterruptedAction.getSpendingCards().get(i), token);
        }
        int turn = 0;
        CardLocation superFinalCardLocation = null;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER)) {
            turn = 1;
            superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER)) {
            turn = 2;
            superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        }
        //GameManager.getDuelControllerByIndex(index).addStringToAvailableCardLocationForUseForClient(superFinalCardLocation);
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        duelBoard.addCardToMonsterZone(mainCard, turn, token);

        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
            + " " + uninterruptedAction.getMainCardLocation().getIndex() + " is being added to monster zone " + turn + " and should finally be FACE_UP_ATTACK_POSITION");

        mainCard.setCardPosition(CardPosition.FACE_UP_ATTACK_POSITION);
        MonsterCard monsterCard = (MonsterCard) mainCard;
        monsterCard.setCardPositionChanged(true);
        GameManager.getDuelControllerByIndex(token).setCanUserSummonOrSetMonsters(uninterruptedAction.getActionTurn(), false);
        return "tribute summoned successfully";
    }

    public static String conductTributeSummoningAction(String token, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (!action.isActionCanceled()) {
            CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
            Card mainCard = duelBoard.getCardByCardLocation(mainCardLocation);
            MonsterCard monsterCard = (MonsterCard) mainCard;
            ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
            if (uponSummoningEffects.contains(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS)) {
                int actionTurn = action.getActionTurn();
                destroyAllOfOpponentsCards(actionTurn, token);
            }
        }
        return "";
    }

    public static void destroyAllOfOpponentsCards(int actionTurn, String token) {
        if (actionTurn == 1) {
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1), token);
            }
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1), token);
            }
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1), token);
        } else {
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1), token);
            }
            for (int i = 0; i < 5; i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1), token);
            }
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1), token);
        }


    }
}
