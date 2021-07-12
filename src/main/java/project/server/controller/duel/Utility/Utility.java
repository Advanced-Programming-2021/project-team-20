package project.server.controller.duel.Utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.model.SpellEffectEnums.QuickSpellEffect;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.GamePhaseControllers.PhaseController;
import project.server.controller.duel.GamePhaseControllers.SelectCardController;
import project.server.controller.duel.GamePhaseControllers.SummonSetCommonClass;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.RowOfCardLocation;

public class Utility {
    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern inputCompiler = Pattern.compile(regex);
        return inputCompiler.matcher(input);
    }

    public static boolean isMatcherCorrectWithoutErrorPrinting(Matcher matcherOfCommandInput) {
        return matcherOfCommandInput.find();
    }

    public static RowOfCardLocation considerTurnsForRowOfCardLocation(RowOfCardLocation rowOfCardLocation, int turn) {
        if (turn == 1) {
            return rowOfCardLocation;
        } else if (turn == 2) {
            if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                return RowOfCardLocation.OPPONENT_MONSTER_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
                return RowOfCardLocation.OPPONENT_SPELL_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                return RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
                return RowOfCardLocation.OPPONENT_HAND_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                return RowOfCardLocation.OPPONENT_DECK_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                return RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                return RowOfCardLocation.ALLY_MONSTER_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
                return RowOfCardLocation.ALLY_SPELL_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                return RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
                return RowOfCardLocation.ALLY_HAND_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
                return RowOfCardLocation.ALLY_DECK_ZONE;
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
                return RowOfCardLocation.ALLY_GRAVEYARD_ZONE;
            }
        }
        return null;
    }

    public static int changeYuGiOhIndexToArrayIndex(int cardIndex, RowOfCardLocation rowOfCardLocation) {
        boolean seeminglyChoosingSelf = rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                || rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE);
        boolean seeminglyChoosingOther = rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
                || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE);
        if (seeminglyChoosingSelf) {
            if (cardIndex == 1) {
                return 3;
            } else if (cardIndex == 2) {
                return 4;
            } else if (cardIndex == 3) {
                return 2;
            } else if (cardIndex == 4) {
                return 5;
            } else if (cardIndex == 5) {
                return 1;
            }
        } else if (seeminglyChoosingOther) {
            if (cardIndex == 1) {
                return 3;
            } else if (cardIndex == 2) {
                return 2;
            } else if (cardIndex == 3) {
                return 4;
            } else if (cardIndex == 4) {
                return 1;
            } else if (cardIndex == 5) {
                return 5;
            }
        }
        return cardIndex;
    }

    public static int changeArrayIndexFromOneToFiveToYuGiOhIndex(int cardIndex, boolean choosingSelf, String token) {
        if (!choosingSelf && GameManager.getDuelControllerByIndex(token).getAiTurn() == 2
                || choosingSelf && GameManager.getDuelControllerByIndex(token).getAiTurn() == 1) {
            if (cardIndex == 1) {
                return 5;
            } else if (cardIndex == 2) {
                return 3;
            } else if (cardIndex == 3) {
                return 1;
            } else if (cardIndex == 4) {
                return 2;
            }
            return 5;
        } else {
            if (cardIndex == 1) {
                return 4;
            } else if (cardIndex == 2) {
                return 2;
            } else if (cardIndex == 3) {
                return 1;
            } else if (cardIndex == 4) {
                return 3;
            }
            return 5;
        }
    }

    public static String isACardSelected(String token, String stringUsedInOutput, boolean continueChecking) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        if (!selectCardController.doesSelectedCardLocationsHaveCard()) {
            return "no card is selected yet";
        } else {
            if (continueChecking) {
                return SummonSetCommonClass.isCardInHand(token, stringUsedInOutput, true);
            }
            return "";
        }
    }

    public static String areWeInMainPhase(String token) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        if (!phaseController.isPhaseCurrentlyMainPhase(turn)) {
            return "action not allowed in this phase";
        }
        return "";
    }

    public static String areWeInBattlePhase(String token) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        if (!phaseController.isPhaseCurrentlyBattlePhase(turn)) {
            return "action not allowed in this phase";
        }
        return "";
    }

    public static String giveCardNameRemovingRedundancy(String cardName) {
        String string = cardName.replaceAll(" ", "");
        string = string.toLowerCase();
        return string.replaceAll(",", "");
    }
}
