package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;

import project.server.controller.duel.CardEffects.*;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;

public class SummonSetCommonClass extends ChainController {

    public static String startChecking(String token, String stringUsedInOutput, boolean continueChecking) {
        return Utility.isACardSelected(token, stringUsedInOutput, continueChecking);
    }

    public static String isCardInHand(String token, String stringUsedInOutput, boolean continueChecking) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        int turn = duelController.getTurn();
        if (!duelBoard.isCardInHand(card, turn)) {
            return "you can't " + stringUsedInOutput + " this card";
        }
        if (continueChecking) {
            return decidePathBetweenSetAndSummon(token, stringUsedInOutput, true);
        } else {
            return "";
        }
    }

    public static String decidePathBetweenSetAndSummon(String token, String stringUsedInOutput, boolean continueChecking) {
        if (stringUsedInOutput.equals("set")) {
            return Utility.areWeInMainPhase(token);
        } else {
            return isCardAMonster(token, stringUsedInOutput, true);
        }
    }

    public static String isCardAMonster(String token, String stringUsedInOutput, boolean continueChecking) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.selectedCardLocations;
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        if (!(card instanceof MonsterCard)) {
            return "you can't " + stringUsedInOutput + " this card";
        }
        if (continueChecking) {
            return Utility.areWeInMainPhase(token);
        } else {
            return "";
        }
    }

    public static String isMonsterCardZoneFull(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int turn = duelController.getTurn();
        if (turn == 1 && duelBoard.isZoneFull(RowOfCardLocation.ALLY_MONSTER_ZONE) || turn == 2 && duelBoard.isZoneFull(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return "monster card zone is full";
        }
        return "";
    }

    public static String isSpellCardZoneFull(String token, Card card) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int turn = duelController.getTurn();
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            SpellCardValue spellCardValue = spellCard.getSpellCardValue();
            if (spellCardValue.equals(SpellCardValue.FIELD)) {
                return "";
            }
        }
        if (turn == 1 && duelBoard.isZoneFull(RowOfCardLocation.ALLY_SPELL_ZONE) || turn == 2 && duelBoard.isZoneFull(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return "spell card zone is full";
        }
        return "";
    }

    public static String hasUserAlreadySummonedSet(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        if (!duelController.canUserSummonOrSetMonsters(turn)) {
            return "you already normal summoned on this turn";
        }
        return "";
    }

    public String convertMessageFromEffectToControllerToString(Card card, DuelBoard duelBoard, int turn, String stringUsedInOutput) {
        MessagesFromEffectToControllers messagesFromEffectToControllers = null;
        if (stringUsedInOutput.equals("set")) {
            messagesFromEffectToControllers = Effect.canMonsterBeNormalSummonedOrSet(card, duelBoard, turn, stringUsedInOutput);
        } else if (stringUsedInOutput.equals("normal summon")) {
            messagesFromEffectToControllers = Effect.canMonsterBeNormalSummonedOrSet(card, duelBoard, turn, stringUsedInOutput);
        } else if (stringUsedInOutput.equals("tribute summon")) {
            messagesFromEffectToControllers = Effect.canMonsterBeNormalSummonedOrSet(card, duelBoard, turn, stringUsedInOutput);
        } else if (stringUsedInOutput.equals("special summon")) {
            messagesFromEffectToControllers = Effect.canMonsterBeSpecialSummoned(card, duelBoard, turn, stringUsedInOutput);
        }
        if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.CANT_BE_NORMAL_SUMMONED)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.CANT_BE_TRIBUTE_SUMMONED)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.CANT_BE_SPECIAL_SUMMONED)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.CANT_BE_RITUAL_SUMMONED)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.CANT_BE_SET)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.RITUAL_SUMMON_THIS_MONSTER_BY_USING_ADVANCED_RITUAL_ART)) {
            return "ritual summon this monster by activating advanced ritual art";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.IT_IS_NOT_A_MONSTER_CARD)) {
            return "you can't " + stringUsedInOutput + " this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.THERE_IS_NO_CARD_IN_HAND_TO_DISCARD)) {
            return "there is no card in your hand for discarding\nyou can't special summon this card";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE)) {
            return "there are not enough cards for tribute";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PLEASE_CHOOSE_1_MONSTER_TO_TRIBUTE)) {
            return "please choose one monster to tribute\nyou need to enter select command";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PLEASE_CHOOSE_2_MONSTERS_TO_TRIBUTE)) {
            return "please choose two monsters to tribute\nyou need to enter select command";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PLEASE_CHOOSE_3_MONSTERS_TO_TRIBUTE)) {
            return "please choose three monsters to tribute\nyou need to enter select command";
        } else if (messagesFromEffectToControllers.equals(MessagesFromEffectToControllers.PLEASE_CHOOSE_ONE_CARD_FROM_YOUR_HAND_TO_DISCARD)) {
            return "please choose one card from your hand to discard\nyou need to enter select command";
        }
        if (stringUsedInOutput.equals("set")) {
            return "set successfully";
        }
        return stringUsedInOutput + "ed successfully";
    }

}
