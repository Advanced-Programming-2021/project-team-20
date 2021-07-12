package project.server.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;

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
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;

public class SettingCardConductor {
    private static boolean isActionCanceled = false;

    public static boolean isIsActionCanceled() {
        return isActionCanceled;
    }

    public static void setIsActionCanceled(boolean isActionCanceled) {
        SettingCardConductor.isActionCanceled = isActionCanceled;
    }

    public static String conductNormalSettingActionUninterruptedAction(String token, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> cardsToBeTributed = new ArrayList<>();
        int turn = 0;
        CardLocation superFinalCardLocation = null;
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER)) {
            turn = 1;
            superFinalCardLocation = new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true)
                    .getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex()
                    + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD)) {
            turn = 1;
            Card card = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
                sendUseLessSpellFieldCardToGraveyard(token, 1);
            } else {
                superFinalCardLocation = new CardLocation(
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true)
                        .getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true).getIndex()
                        + 1);
                uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
            }
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            turn = 2;
            superFinalCardLocation = new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false)
                    .getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex()
                    + 1);
            uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
        } else if (uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            turn = 2;
            Card card = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
            if (Card.isCardASpell(card) && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                uninterruptedAction.setFinalMainCardLocation(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1));
                sendUseLessSpellFieldCardToGraveyard(token, 2);
            } else {
                superFinalCardLocation = new CardLocation(
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false)
                        .getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false)
                        .getIndex() + 1);
                uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
            }
        }
        //GameManager.getDuelControllerByIndex(index).addStringToAvailableCardLocationForUseForClient(superFinalCardLocation);
        for (int i = 0; i < uninterruptedAction.getSpendingCards().size(); i++) {
            cardsToBeTributed.add(duelBoard.getCardByCardLocation(uninterruptedAction.getSpendingCards().get(i)));
            duelBoard.removeCardByCardLocation(uninterruptedAction.getSpendingCards().get(i));
            duelBoard.addCardToGraveyard(cardsToBeTributed.get(i), turn);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + uninterruptedAction.getSpendingCards().get(i).getRowOfCardLocation()
                + " " + uninterruptedAction.getSpendingCards().get(i).getIndex() + " is being added to graveyard zone " + turn + " and should finally be FACE_UP_ATTACK_POSITION or FACE_UP_ACTIVATED_POSITION ");

        }
        Card mainCard = duelBoard.getCardByCardLocation(uninterruptedAction.getMainCardLocation());
        if (Card.isCardATrap(mainCard)) {
            ((TrapCard) mainCard).setTurnCardWasSet(GameManager.getDuelControllerByIndex(token).getTotalTurnsUntilNow());
        }
        duelBoard.removeCardByCardLocation(uninterruptedAction.getMainCardLocation());
        if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_MONSTER)
            || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_MONSTER)) {
            duelBoard.addCardToMonsterZone(mainCard, turn, token);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
                + " " + uninterruptedAction.getMainCardLocation().getIndex() + " is being added to monster zone " + turn + " and should finally be FACE_DOWN_MONSTER_SET_POSITION");

            mainCard.setCardPosition(CardPosition.FACE_DOWN_MONSTER_SET_POSITION);
            GameManager.getDuelControllerByIndex(token).setCanUserSummonOrSetMonsters(uninterruptedAction.getActionTurn(), false);
        } else if (uninterruptedAction.getActionType().equals(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD)
            || uninterruptedAction.getActionType().equals(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD)) {
            duelBoard.addCardToSpellZone(mainCard, turn, token);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + uninterruptedAction.getMainCardLocation().getRowOfCardLocation()
                + " " + uninterruptedAction.getMainCardLocation().getIndex() + " is being added to spell zone " + turn + " and should finally be FACE_DOWN_SPELL_SET_POSITION");
            mainCard.setCardPosition(CardPosition.FACE_DOWN_SPELL_SET_POSITION);
        }
        return "set successfully";
    }

    private static void sendUseLessSpellFieldCardToGraveyard(String token, int turn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation possibleCardLocation;
        if (turn == 1) {
            possibleCardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        } else {
            possibleCardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        }
        Card possibleCard = duelBoard.getCardByCardLocation(possibleCardLocation);
        if (Card.isCardASpell(possibleCard)) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(possibleCardLocation, token);
        }
    }

    public static String conductNormalSettingAction(String token, int numberInListOfActions) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        Action uninterruptedAction = uninterruptedActions.get(numberInListOfActions);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        Action action = actions.get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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
