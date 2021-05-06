package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;

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
        String inputRegex = "(?<=\\n|^)(select[\\s]+--([\\S]+)(|[\\s]+--([\\S]+))(|[\\s]+([\\d]+))(?=\\n|$))";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        RowOfCardLocation rowOfCardLocation;
        DuelController duelController = GameManager.getDuelControllerByIndex(indexOfWholeGame);
        int fakeTurn = duelController.getFakeTurn();
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            rowOfCardLocation = switchCaseInputToGetRowOfCardLocation(matcher.group(2), matcher.group(4), fakeTurn);
            if (rowOfCardLocation == null){
                return "invalid selection";
            }
            if (matcher.group(6) != null){
                int cardIndex = Integer.parseInt(matcher.group(6));
                //System.out.println("card Index is "+cardIndex);
                if (isSelectedCardIndexValid(cardIndex, rowOfCardLocation, indexOfWholeGame)){
                    cardIndex = Utility.changeYuGiOhIndexToArrayIndex(cardIndex, rowOfCardLocation);
                    System.out.println("cardIndex is "+cardIndex);
                    CardLocation cardLocation = new CardLocation(rowOfCardLocation, cardIndex);
                    DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
                    Card card = duelBoard.getCardByCardLocation(cardLocation);
                    if (card == null){
                        return "no card found in the given position";
                    } else {
                        //System.out.println(card.getCardName()+" is what is chosen");
                        selectedCardLocations.add(cardLocation);
                        for (int i = 0; i < selectedCardLocations.size(); i++){
                            System.out.println(selectedCardLocations.get(i).getRowOfCardLocation().toString()+selectedCardLocations.get(i).getIndex());
                        }
                        System.out.println(duelBoard.getCardByCardLocation(cardLocation).getCardName()+"is selected");
                        return "card selected";
                    }
                } else {
                    return "invalid selection";
                }
            } else if (matcher.group(4) != null || matcher.group(4) == null && rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                CardLocation cardLocation = new CardLocation(rowOfCardLocation, 1);
                DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
                Card card = duelBoard.getCardByCardLocation(cardLocation);
                if (card == null){
                    return "no card found in the given position";
                } else {
                    selectedCardLocations.add(cardLocation);
                    return "card selected";
                }
            }
        } else {
            String secondInputRegex = "(?<=\\n|^)select[\\s]+-d(?=\\n|$)";
            Matcher secondMatcher = Utility.getCommandMatcher(string, secondInputRegex);
            if (Utility.isMatcherCorrectWithoutErrorPrinting(secondMatcher)){
                if (selectedCardLocations.size() == 0){
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

    private RowOfCardLocation switchCaseInputToGetRowOfCardLocation(String firstString, String secondString, int turn){
        if (firstString == null){
            firstString = "";
        }
        if (secondString == null){
            secondString = "";
        }
        if (firstString.equals("opponent") && secondString.equals("monster")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if (firstString.equals("monster") && secondString.equals("opponent")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if (firstString.equals("spell") && secondString.equals("opponent")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if (firstString.equals("opponent") && secondString.equals("spell")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if (firstString.equals("field") && secondString.equals("opponent")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if (firstString.equals("opponent") && secondString.equals("field")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if (firstString.equals("hand") && secondString.equals("opponent")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if (firstString.equals("opponent") && secondString.equals("hand")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if (firstString.equals("graveyard") && secondString.equals("opponent")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if (firstString.equals("opponent") && secondString.equals("graveyard")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if (firstString.equals("monster") && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, turn);
        } else if (firstString.equals("spell") && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, turn);
        } else if (firstString.equals("field") && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, turn);
        } else if (firstString.equals("hand") && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_HAND_ZONE, turn);
        } else if (firstString.equals("graveyard") && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, turn);
        }
        return null;
    }


    // when this function is called, we are sure thar rowOfCardLocation is adjusted with the program.
    private boolean isSelectedCardIndexValid(int cardIndex, RowOfCardLocation rowOfCardLocation, int indexOfWholeGame){
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) && cardIndex <= 5 && cardIndex >= 1){
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE) && cardIndex <= 5 && cardIndex >= 1){
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInHand().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInDeck().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getAllyCardsInGraveyard().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && cardIndex <= 5 && cardIndex >= 1){
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) && cardIndex <= 5 && cardIndex >= 1){
            return true;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInHand().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInDeck().size();
            return cardIndex >= 1 && size >= cardIndex;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)){
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(indexOfWholeGame);
            int size = duelBoard.getOpponentCardsInGraveyard().size();
            return cardIndex >= 1 && size >= cardIndex;
        }
        return false;
    }

    public boolean doesSelectedCardLocationsHaveCard(){
        return selectedCardLocations.size() != 0;
    }
}
