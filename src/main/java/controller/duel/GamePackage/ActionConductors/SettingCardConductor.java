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
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;

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
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                        duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, true)
                                .getRowOfCardLocation(),
                        duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, true)
                                .getIndex() + 1));
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
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(
                        duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, false)
                                .getRowOfCardLocation(),
                        duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, false)
                                .getIndex() + 1));
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
        System.out
                .println("main Care Location is row" + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
                        + " index " + uninterruptedAction.getMainCardLocation().getIndex());
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER)
                || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            duelBoard.addCardToMonsterZone(mainCard, turn);
            mainCard.setCardPosition(CardPosition.FACE_DOWN_MONSTER_SET_POSITION);
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

    public static String conductNormalSettingAction(int index, int numberInListOfActions) {
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
            if (uponSummoningEffects.contains(UponSummoningEffect.SET_ATK_1900_IF_SET)){
                monsterCard.setAttackPower(1900);
            }
        }
        return "";
    }
}
