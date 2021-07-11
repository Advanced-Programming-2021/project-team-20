package project.server.controller.duel.GamePhaseControllers;

import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;

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
        cardsToBeDiscarded = new ArrayList<>();
        numberOfCardsToBeTributed = 0;
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
    }

    public boolean isAreWeLookingForMonstersToBeTributed() {
        return areWeLookingForMonstersToBeTributed;
    }

    public boolean isClassWaitingForCardToBeDiscarded() {
        return isClassWaitingForCardToBeDiscarded;
    }

    public boolean isClassWaitingForUserToChooseAttackPositionOrDefensePosition() {
        return isClassWaitingForUserToChooseAttackPositionOrDefensePosition;
    }

    public String specialSummonInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)special[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            String resultOfChecking = startChecking(token, "special summon", true);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                resultOfChecking = isMonsterCardZoneFull(token);
                if (!resultOfChecking.equals("")) {
                    return resultOfChecking;
                } else {
                    Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                    return analyzeMonsterCardToBeSummoned(token, card, "special summon");
                }
            }
        }
        return null;
    }

    public String analyzeMonsterCardToBeSummoned(String token, Card card, String typeOfAnalysis) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, typeOfAnalysis);
        String output;
        if (cardAnalysis.equals("special summoned successfully")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1);
            createActionForSpecialSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            String canChainingOccur = canChainingOccur(token, turn, ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(token);
        } else if (cardAnalysis.startsWith("please choose")) {
            mainCard = selectCardController.getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1);
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
    }

    public void createActionForSpecialSummoningMonster(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, null, null, cardPositionOfMainCard, "", null));
            actions.add(new Action(ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, null, null, cardPositionOfMainCard, "", null));
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, null, null, cardPositionOfMainCard, "", null));
            actions.add(new Action(ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, cardsToBeDiscarded, null, null, null, null, null, null, null, cardPositionOfMainCard, "", null));
        }
        cardsToBeTributed.clear();
        cardsToBeDiscarded.clear();
        cardPositionOfMainCard = CardPosition.NOT_APPLICABLE;
    }

    public String redirectInputForCardsToBeDiscarded(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (turn == 1 && !rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || turn == 2 && !rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            return "you can't choose this card for discarding\nchoose another card";
        } else if (rowOfCardLocation.equals(mainCard.getRowOfCardLocation()) && cardLocation.getIndex() == mainCard.getIndex()) {
            return "this is the card you want to special summon\nyou can't discard this card\nchoose another card";
        } else {
            isClassWaitingForCardToBeDiscarded = false;
            cardsToBeDiscarded.add(cardLocation);
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = true;
            return "do you want to special summon your card in face up attack position or face up defense position?\nsimply enter either attacking or defensive";
        }
    }

    public String redirectInputForMonsterTributing(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
        if (!Card.isCardAMonster(card)) {
            return "there are no monsters on this address\nplease try again";
        }
        if ((turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) || (turn == 2 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                areWeLookingForMonstersToBeTributed = false;
                isClassWaitingForUserToChooseAttackPositionOrDefensePosition = true;
                return "do you want to special summon your card in face up attack position or face up defense position?\nsimply enter either attacking or defensive";
            }
        } else {
            return "this card cannot be chosen for tribute.\nplease try again.";
        }
        return null;
    }

    public String redirectInputForAnalyzingAttackPositionOrDefensePosition(String string, String token) {
        String output = "";
        String canChainingOccur = "";
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        if (string.equals("attacking")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionOfMainCard = CardPosition.FACE_UP_ATTACK_POSITION;
            createActionForSpecialSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            canChainingOccur = canChainingOccur(token, duelController.getTurn(), ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        } else if (string.equals("defensive")) {
            isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
            cardPositionOfMainCard = CardPosition.FACE_UP_DEFENSE_POSITION;
            createActionForSpecialSummoningMonster(token);
            output = Action.conductUninterruptedAction(token);
            canChainingOccur = canChainingOccur(token, duelController.getTurn(), ActionType.ALLY_SPECIAL_SUMMONING_MONSTER, ActionType.OPPONENT_SPECIAL_SUMMONING_MONSTER);
        }
        if (!isClassWaitingForUserToChooseAttackPositionOrDefensePosition) {
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(0);
        }
        return "invalid input\nplease enter attacking or defensive";
    }

    public void clearAllVariablesOfThisClass() {
        areWeLookingForMonstersToBeTributed = false;
        mainCard = null;
        cardsToBeTributed.clear();
        cardsToBeDiscarded.clear();
        numberOfCardsToBeTributed = 0;
        isClassWaitingForCardToBeDiscarded = false;
        isClassWaitingForUserToChooseAttackPositionOrDefensePosition = false;
        cardPositionOfMainCard = null;
    }
}
