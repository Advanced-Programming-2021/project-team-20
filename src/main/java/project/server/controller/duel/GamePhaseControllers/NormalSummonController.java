package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;

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


    public String normalSummonInputAnalysis(String string, String typeOfAnalysis, String token) {
        String inputRegex = "(?<=\\n|^)normal[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            String resultOfChecking = startChecking(token, typeOfAnalysis, true);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                resultOfChecking = isMonsterCardZoneFull(token);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    resultOfChecking = hasUserAlreadySummonedSet(token);
                    if (!resultOfChecking.equals("")) {
                        return resultOfChecking;
                    } else {
                        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                        return analyzeMonsterCardToBeSummoned(token, card, typeOfAnalysis);
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

    public String analyzeMonsterCardToBeSummoned(String token, Card card, String typeOfAnalysis) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, typeOfAnalysis);
        String output;
        if (cardAnalysis.equals("normal summoned successfully")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1);
            createActionForNormalSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            String canChainingOccur = canChainingOccur(token, turn, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(token);
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

    public void createActionForNormalSummoningMonster(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, null, "", null));
        }
        cardsToBeTributed.clear();
    }

    public String redirectInputForMonsterTributing(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
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
                    createActionForNormalSummoningMonster(token);
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token, 1, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                } else {
                    createActionForNormalSummoningMonster(token);
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token, 2, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                }
                if (!canChainingOccur.equals("")) {
                    output += canChainingOccur;
                    return output;
                }
                return output + Action.conductAllActions(token);
            }
        } else {
            return "this card cannot be chosen for tribute.\nplease try again.";
        }
        return "now select another monster";
    }

    public void clearAllVariablesOfThisClass(){
        areWeLookingForMonstersToBeTributed = false;
        mainCard = null;
        cardsToBeTributed.clear();
        numberOfCardsToBeTributed = 0;
    }
}
