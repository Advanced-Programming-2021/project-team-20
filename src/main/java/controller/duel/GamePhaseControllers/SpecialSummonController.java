package controller.duel.GamePhaseControllers;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.RowOfCardLocation;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class SpecialSummonController extends SummonSetCommonClass {
    private boolean areWeLookingForMonstersToBeTributed;
    private CardLocation mainCard;
    private ArrayList<CardLocation> cardsToBeTributed;
    private ArrayList<CardLocation> cardsToBeDiscarded;
    private int numberOfCardsToBeTributed;
    private boolean isClassWaitingForCardToBeDiscarded;
    private boolean isClassWaitingForUserToChooseAttackPositionOrDefensePosition;
    private CardPosition cardPositionOfMainCard;

    public SpecialSummonController() {
        areWeLookingForMonstersToBeTributed = false;
        cardsToBeTributed = new ArrayList<>();
        numberOfCardsToBeTributed = 0;
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
    }

    public String specialSummonInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)special[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = startChecking(0, "special summon", true);
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
                        return analyzeMonsterCardToBeSummoned(0, card, "special summon");
                    }
                }
            }
        }
        return null;
    }

    public String analyzeMonsterCardToBeSummoned(int index, Card card, String typeOfAnalysis) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, typeOfAnalysis);
        String output;
        if (cardAnalysis.equals("special summoned successfully")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size()-1);
            createActionForSpecialSummoningMonster(index);
            output = Action.conductUninterruptedAction(index);
            String canChainingOccur = canChainingOccur(index, turn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(index);
        } else if (cardAnalysis.startsWith("please choose")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size()-1);
            selectCardController.resetSelectedCardLocationList();
            if (cardAnalysis.startsWith("please choose one monster")) {
                numberOfCardsToBeTributed = 1;
                areWeLookingForMonstersToBeTributed = true;
            } else if (cardAnalysis.startsWith("please choose two")) {
                numberOfCardsToBeTributed = 2;
                areWeLookingForMonstersToBeTributed = true;
            } else if (cardAnalysis.startsWith("please choose three")) {
                numberOfCardsToBeTributed = 3;
                areWeLookingForMonstersToBeTributed = true;
            } else if (cardAnalysis.startsWith("please choose one card")) {
                isClassWaitingForCardToBeDiscarded = true;
            }
            return cardAnalysis;
        } else {
            return cardAnalysis;
        }
        //return "";
    }

    public void createActionForSpecialSummoningMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        //mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
        System.out.println("mainCard\n" + mainCard.getRowOfCardLocation().toString() + mainCard.getIndex());
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, cardPositionOfMainCard));
            actions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, cardPositionOfMainCard));
            //add action that conducts effects of the card
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, cardPositionOfMainCard));
            actions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, cardPositionOfMainCard));
            //add action that conducts effects of the card
        }
        cardsToBeTributed.clear();
        cardsToBeDiscarded.clear();
    }

    public String isClassWaitingForDiscarding() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        //System.out.println("mainCard\n" + mainCard.getRowOfCardLocation().toString() + mainCard.getIndex());
        //selectCardController.resetSelectedCardLocationList();
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (turn == 1 && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || turn == 2 && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            return "you can't choose this card for discarding\nchoose another card";
        } else if (rowOfCardLocation.equals(mainCard.getRowOfCardLocation()) && cardLocation.getIndex() == mainCard.getIndex()) {
            return "this is the card you want to special summon\nyou can't discard this card\nchoose another card";
        } else {
            isClassWaitingForCardToBeDiscarded = false;
            cardsToBeDiscarded.add(cardLocation);
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = true;
            return "do you want to special summon your card in face up attack position or face up defense position?\nsimply enter either attack or defense";
        }
    }

    public String redirectInputForMonsterTributing() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        if (!Card.isCardAMonster(card)) {
            return "there are no monsters on this address\nplease try again";
        }
        if ((turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) || (turn == 2 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                isClassWaitingForUserToChooseAttackPositionOrDefensePosition = true;
                return "do you want to special summon your card in face up attack position or face up defense position?\nsimply enter either attack or defense";
            }
        } else {
            return "this card cannot be chosen for tribute.\nplease try again.";
        }
        return null;
    }

    public String isClassWaitingForUserToChooseAttackPositionOrDefensePosition(String string) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        String output = "";
        String canChainingOccur = "";
        if (string.equals("attack")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionOfMainCard = CardPosition.FACE_UP_ATTACK_POSITION;
            createActionForSpecialSummoningMonster(0);
            output = Action.conductUninterruptedAction(0);
            canChainingOccur = canChainingOccur(0, 1, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        } else if (string.equals("defense")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionOfMainCard = CardPosition.FACE_UP_DEFENSE_POSITION;
            createActionForSpecialSummoningMonster(0);
            output = Action.conductUninterruptedAction(0);
            canChainingOccur = canChainingOccur(0, 2, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        }
        if (!canChainingOccur.equals("")) {
            output += canChainingOccur;
            return output;
        }
        return output + Action.conductAllActions(0);
    }
}