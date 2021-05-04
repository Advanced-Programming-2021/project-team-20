package GamePhaseControllers;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.RowOfCardLocation;
import GamePackage.Action;
import GamePackage.ActionType;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import PreliminaryPackage.GameManager;
import Utility.Utility;

import java.util.ArrayList;
import java.util.regex.Matcher;

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


    public String normalSummonInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)normal[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = startChecking(0, "normal summon", true);
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
                        return analyzeMonsterCardToBeSummonedOrSet(0, card);
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

    public String analyzeMonsterCardToBeSummonedOrSet(int index, Card card) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, "normal summon");
        String output;
        if (cardAnalysis.equals("normal summoned successfully")) {
            createActionForNormalSummoningMonster(index);
            output = Action.conductUninterruptedAction(index);
            String canChainingOccur = canChainingOccur(index, turn, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output+Action.conductAllActions(index);
        } else if (cardAnalysis.startsWith("please choose")) {
            mainCard = selectCardController.getSelectedCardLocations().get(0);
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
        //return "";
    }

    public void createActionForNormalSummoningMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
        System.out.println("mainCard\n" + mainCard.getRowOfCardLocation().toString() + mainCard.getIndex());
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, null, null, null, null, null, null, null));
            actions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, null, null, null, null, null, null, null));
            //add action that conducts effects of the card
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null));
            actions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null));
            //add action that conducts effects of the card
        }

    }

    public String redirectInputForMonsterTributing() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
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
                ArrayList<Action> actions = GameManager.getActionsByIndex(0);
                ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
                areWeLookingForMonstersToBeTributed = false;
                if (turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                    actions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null));
                    uninterruptedActions.add(new Action(ActionType.ALLY_NORMAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null));
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 1, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                } else {
                    actions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null));
                    uninterruptedActions.add(new Action(ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null));
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 2, ActionType.ALLY_NORMAL_SUMMONING_MONSTER, ActionType.OPPONENT_NORMAL_SUMMONING_MONSTER);
                }
                //apply gate guardian effect
                if (!canChainingOccur.equals("")) {
                    output += canChainingOccur;
                    return output;
                }
                return output+Action.conductAllActions(0);
            }
        } else {
            return "this card cannot be chosen for tribute.\nplease try again.";
        }
        return null;
    }


}
