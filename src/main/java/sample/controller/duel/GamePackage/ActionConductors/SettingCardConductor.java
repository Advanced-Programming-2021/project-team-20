package sample.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

import sample.controller.duel.CardEffects.MonsterEffectEnums.UponSummoningEffect;
import sample.controller.duel.GamePackage.Action;
import sample.controller.duel.GamePackage.ActionType;
import sample.controller.duel.GamePackage.DuelBoard;
import sample.controller.duel.PreliminaryPackage.GameManager;
import sample.model.cardData.General.Card;
import sample.model.cardData.General.CardLocation;
import sample.model.cardData.General.CardPosition;
import sample.model.cardData.General.RowOfCardLocation;
import sample.model.cardData.MonsterCardData.MonsterCard;
import sample.model.cardData.SpellCardData.SpellCard;
import sample.model.cardData.SpellCardData.SpellCardValue;
import sample.model.cardData.TrapCardData.TrapCard;

public class SettingCardConductor {
    private static boolean isActionCanceled = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        SettingCardConductor.isActionCanceled = isActionCanceled;
    }

    public static String conductNormalSettingActionUninterruptedAction(int index, int numberInListOfActions) {
        // if (!isActionCanceled){
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> cardsToBeTributed = new ArrayList<>();
        int turn = 0;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER)) {
            turn = 1;
            uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true)
                    .getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex()
                    + 1));
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD)) {
            turn = 1;
            Card card = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
                sendUseLessSpellFieldCardToGraveyard(index, 1);
            } else {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true)
                        .getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true).getIndex()
                        + 1));
            }
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            turn = 2;
            uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false)
                    .getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex()
                    + 1));
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            turn = 2;
            Card card = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1));
                sendUseLessSpellFieldCardToGraveyard(index, 2);
            } else {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false)
                        .getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false)
                        .getIndex() + 1));
            }
        }
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            cardsToBeTributed.add(duelBoard.getCardByCardLocation(uninterruptedAction.getSpendingCards().get(i)));
            duelBoard.removeCardByCardLocation(uninterruptedAction.getSpendingCards().get(i));
            duelBoard.addCardToGraveyard(cardsToBeTributed.get(i), turn);
        }
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        //System.out
        //        .println("main Care Location is row" + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
        //                + " index " + uninterruptedAction.getMainCardLocation().getIndex());
        if (Card.isCardATrap(mainCard)){
            ((TrapCard)mainCard).setTurnCardWasSet(GameManager.getDuelControllerByIndex(index).getTotalTurnsUntilNow());
        }
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER)
            || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            duelBoard.addCardToMonsterZone(mainCard, turn);
            mainCard.setCardPosition(CardPosition.FACE_DOWN_MONSTER_SET_POSITION);
            GameManager.getDuelControllerByIndex(index).setCanUserSummonOrSetMonsters(uninterruptedAction.getActionTurn(), false);
            // actions.remove(numberInListOfActions);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD)
            || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            duelBoard.addCardToSpellZone(mainCard, turn);
            mainCard.setCardPosition(CardPosition.FACE_DOWN_SPELL_SET_POSITION);
            // actions.remove(numberInListOfActions);
        }
        return "set successfully";
        // }
        // return "normal setting action was interrupted and therefore, canceled.";
    }

    private static void sendUseLessSpellFieldCardToGraveyard(int index, int turn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation possibleCardLocation;
        if (turn == 1) {
            possibleCardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        } else {
            possibleCardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        }
        Card possibleCard = duelBoard.getCardByCardLocation(possibleCardLocation);
        if (Card.isCardASpell(possibleCard)) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(possibleCardLocation, index);
        }
    }

    public static String conductNormalSettingAction(int index, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (!action.isActionCanceled()) {
            CardLocation mainCardLocation = uninterruptedAction.getFinalMainCardLocation();
            Card mainCard = duelBoard.getCardByCardLocation(mainCardLocation);
            if (Card.isCardAMonster(mainCard)) {
                MonsterCard monsterCard = (MonsterCard) mainCard;
                ArrayList<UponSummoningEffect> uponSummoningEffects = monsterCard.getUponSummoningEffects();
                if (uponSummoningEffects.contains(UponSummoningEffect.SET_ATK_1900_IF_SET)) {
                    monsterCard.setAttackPower(1900);
                }
            }
        }
        return "";
    }
}
