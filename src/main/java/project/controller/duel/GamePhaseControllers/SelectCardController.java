package project.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;

public class SelectCardController {
    ArrayList<CardLocation> selectedCardLocations;
    int indexOfWholeGame = 0;

    public SelectCardController() {
        selectedCardLocations = new ArrayList<>();
    }

    public ArrayList<CardLocation> getSelectedCardLocations() {
        return selectedCardLocations;
    }

    public String selectCardInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)(select[\\s]+(--|-)([\\S]+)(|[\\s]+(--|-)([\\S]+))(|[\\s]+([\\d]+))(?=\\n|$))";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        RowOfCardLocation rowOfCardLocation;
        DuelController duelController = GameManager.getDuelControllerByIndex(indexOfWholeGame);
        int fakeTurn = duelController.getFakeTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            rowOfCardLocation = switchCaseInputToGetRowOfCardLocation(matcher.group(3), matcher.group(6), fakeTurn);
            if (rowOfCardLocation == null) {
                return "invalid selection";
            }
            if (matcher.group(8) != null) {
                int cardIndex = Integer.parseInt(matcher.group(8));
                if (isSelectedCardIndexValid(cardIndex, rowOfCardLocation, indexOfWholeGame)) {
                    cardIndex = Utility.changeYuGiOhIndexToArrayIndex(cardIndex, rowOfCardLocation);
                    CardLocation cardLocation = new CardLocation(rowOfCardLocation, cardIndex);
                    DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
                    Card card = duelBoard.getCardByCardLocation(cardLocation);
                    if (card == null) {
                        return "no card found in the given position";
                    } else {
                        selectedCardLocations.add(cardLocation);
                        return "card selected";
                    }
                } else {
                    return "invalid selection";
                }
            } else if (matcher.group(6) != null || matcher.group(6) == null && rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                CardLocation cardLocation = new CardLocation(rowOfCardLocation, 1);
                DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
                Card card = duelBoard.getCardByCardLocation(cardLocation);
                if (card == null) {
                    return "no card found in the given position";
                } else {
                    selectedCardLocations.add(cardLocation);
                    return "card selected";
                }
            }
        } else {
            String secondInputRegex = "(?<=\\n|^)select[\\s]+-d(?=\\n|$)";
            Matcher secondMatcher = Utility.getCommandMatcher(string, secondInputRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(secondMatcher)) {
                if (selectedCardLocations.size() == 0) {
                    return "no card is selected yet";
                } else {
                    selectedCardLocations.remove(selectedCardLocations.size() - 1);
                    return "card deselected";
                }
            }
            return "invalid selection";
        }
        return "invalid selection";
    }

    private void addCardLocationToList(CardLocation cardLocation) {
        selectedCardLocations.add(cardLocation);
    }

    public void resetSelectedCardLocationList() {
        selectedCardLocations.clear();
    }

    private RowOfCardLocation switchCaseInputToGetRowOfCardLocation(String firstString, String secondString, int turn) {
        if (firstString == null) {
            firstString = "";
        }
        if (secondString == null) {
            secondString = "";
        }
        if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("monster") || secondString.equals("m"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if ((firstString.equals("monster") || firstString.equals("m")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if ((firstString.equals("spell") || firstString.equals("s")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("spell") || secondString.equals("s"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if ((firstString.equals("field") || firstString.equals("f")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("field") || secondString.equals("f"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("hand") || firstString.equals("h")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("hand") || secondString.equals("h"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if ((firstString.equals("deck") || firstString.equals("d")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("deck") || secondString.equals("d"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, turn);
        } else if ((firstString.equals("graveyard") || firstString.equals("g")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("graveyard") || secondString.equals("g"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if ((firstString.equals("monster") || firstString.equals("m")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, turn);
        } else if ((firstString.equals("spell") || firstString.equals("s")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, turn);
        } else if ((firstString.equals("field") || firstString.equals("f")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("hand") || firstString.equals("h")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_HAND_ZONE, turn);
        } else if ((firstString.equals("deck") || firstString.equals("d")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_DECK_ZONE, turn);
        } else if ((firstString.equals("graveyard") || firstString.equals("g")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, turn);
        }
        return null;
    }


    // when this function is called, we are sure thar rowOfCardLocation is adjusted with the program.
    private boolean isSelectedCardIndexValid(int cardIndex, RowOfCardLocation rowOfCardLocation, int indexOfWholeGame) {
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) && cardIndex <= 5 && cardIndex >= 1) {
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE) && cardIndex <= 5 && cardIndex >= 1) {
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInHand().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInDeck().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInGraveyard().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && cardIndex <= 5 && cardIndex >= 1) {
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) && cardIndex <= 5 && cardIndex >= 1) {
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInHand().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInDeck().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInGraveyard().size();
            return cardIndex >= 1 && size >= cardIndex;
        }
        return false;
    }

    public boolean doesSelectedCardLocationsHaveCard() {
        return selectedCardLocations.size() != 0;
    }

    public void clearAllVariablesOfThisClass() {
        selectedCardLocations.clear();
        indexOfWholeGame = 0;
    }
}
