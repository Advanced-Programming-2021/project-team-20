package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;

public class NormalSummonController extends SummonSetCommonClass {
    private boolean areWeLookingForMonstersToBeTributed;
    private CardLocation mainCard;
    private ArrayList<CardLocation> cardsToBeTributed;
    private int numberOfCardsToBeTributed;

    public NormalSummonController() {
        areWeLookingForMonstersToBeTributed = false;
        cardsToBeTributed = new ArrayList<>();
        numberOfCardsToBeTributed = 0;
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
    }


    public String normalSummonInputAnalysis(String string, String typeOfAnalysis) {
        String inputRegex = "(?<=\\n|^)normal[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = startChecking(0, typeOfAnalysis, true);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                resultOfChecking = isMonsterCardZoneFull(0);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    resultOfChecking = hasUserAlreadySummonedSet(0);
                    if (!resultOfChecking.equals("")) {
                        return resultOfChecking;
                    } else {
                        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                        return analyzeMonsterCardToBeSummoned(0, card, typeOfAnalysis);
                    }
                }
            }
        }
        return null;
    }

    public boolean isAreWeLookingForMonstersToBeTributed() {
        return areWeLookingForMonstersToBeTributed;
    }

    public void setAreWeLookingForMonstersToBeTributed(boolean areWeLookingForMonstersToBeTributed) {
        this.areWeLookingForMonstersToBeTributed = areWeLookingForMonstersToBeTributed;
    }

    public String analyzeMonsterCardToBeSummoned(int index, Card card, String typeOfAnalysis) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, typeOfAnalysis);
        String output;
        if (cardAnalysis.equals("normal summoned successfully")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1);
            createActionForNormalSummoningMonster(index);
            output = Action.conductUninterruptedAction(index);
            String canChainingOccur = canChainingOccur(index, turn, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(index);
        } else if (cardAnalysis.startsWith("please choose") && typeOfAnalysis.equals("normal summon")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1);
            selectCardController.resetSelectedCardLocationList();
            areWeLookingForMonstersToBeTributed = true;
            if (cardAnalysis.startsWith("please choose one")) {
                numberOfCardsToBeTributed = 1;
            } else if (cardAnalysis.startsWith("please choose two")) {
                numberOfCardsToBeTributed = 2;
            } else if (cardAnalysis.startsWith("please choose three")) {
                numberOfCardsToBeTributed = 3;
            }
            return cardAnalysis;
        } else {
            return cardAnalysis;
        }
    }

    public void createActionForNormalSummoningMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
        }
        cardsToBeTributed.clear();
    }

    public String redirectInputForMonsterTributing() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        String output;
        String canChainingOccur;
        if (!Card.isCardAMonster(card)) {
            return "there are no monsters on this address\nplease try again";
        }
        if ((turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) || (turn == 2 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                areWeLookingForMonstersToBeTributed = false;
                if (turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                    createActionForNormalSummoningMonster(0);
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 1, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                } else {
                    createActionForNormalSummoningMonster(0);
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 2, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                }
                if (!canChainingOccur.equals("")) {
                    output += canChainingOccur;
                    return output;
                }
                return output + Action.conductAllActions(0);
            }
        } else {
            return "this card cannot be chosen for tribute.\nplease try again.";
        }
        return null;
    }

    public void clearAllVariablesOfThisClass(){
        areWeLookingForMonstersToBeTributed = false;
        mainCard = null;
        cardsToBeTributed.clear();
        numberOfCardsToBeTributed = 0;
    }
}
