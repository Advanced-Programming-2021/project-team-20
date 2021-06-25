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

public class SetCardController extends SummonSetCommonClass {
    private boolean areWeLookingForMonstersToBeTributed;
    protected CardLocation mainCard;
    protected ArrayList<CardLocation> cardsToBeTributed;
    protected int numberOfCardsToBeTributed;

    public SetCardController() {
        areWeLookingForMonstersToBeTributed = false;
        cardsToBeTributed = new ArrayList<>();
        numberOfCardsToBeTributed = 0;
    }

    public void setMainCard(CardLocation mainCard) {
        this.mainCard = mainCard;
    }

    public String setCardControllerInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)set(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            String resultOfChecking = startChecking(0, "set", true);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                if (Card.isCardAMonster(card)) {
                    return monsterCardSetInputCheck(card);
                } else if (Card.isCardASpell(card) || Card.isCardATrap(card)) {
                    return spellOrTrapCardSetInputCheck(card, 0);
                }
            }
        }
        return null;
    }

    public boolean isAreWeLookingForMonstersToBeTributed() {
        return areWeLookingForMonstersToBeTributed;
    }

    public String monsterCardSetInputCheck(Card card) {
        String resultOfChecking = isMonsterCardZoneFull(0);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            resultOfChecking = hasUserAlreadySummonedSet(0);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return analyzeMonsterCardToBeSet(0, card);
            }
        }
    }

    public String spellOrTrapCardSetInputCheck(Card card, int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        String resultOfChecking = isSpellCardZoneFull(index, card);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            createActionForSettingSpellOrTrap(index);
            String output = Action.conductUninterruptedAction(index);
            return output + Action.conductAllActions(index);
        }
    }

    public String analyzeMonsterCardToBeSet(int index, Card card) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, "set");
        if (cardAnalysis.equals("set successfully")) {
            createActionForSettingMonster(index);
            return Action.conductUninterruptedAction(index) + Action.conductAllActions(index);
        } else if (cardAnalysis.startsWith("please choose")) {
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
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

    public void createActionForSettingSpellOrTrap(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD, 1, cardLocation, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_SETTING_SPELL_OR_TRAP_CARD, 1, cardLocation, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD, 2, cardLocation, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SETTING_SPELL_OR_TRAP_CARD, 2, cardLocation, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }

    public void createActionForSettingMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, null, null, null, null, null, null,null,null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, null, null, null, null,null,null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null, null,null,null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null,null,null, null, "", null));
        }
    }

    public String redirectInput() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        ArrayList<Action> actions = GameManager.getActionsByIndex(0);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        String output;
        if (!Card.isCardAMonster(card)) {
            return "there are no monsters on this address\nplease try again";
        }
        if (turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                areWeLookingForMonstersToBeTributed = false;
                actions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null,null,null, null, null, null, null, "", null));
                uninterruptedActions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, cardsToBeTributed, null,null,null, null, null, null, null, null, null, "", null));
                output = Action.conductUninterruptedAction(0);
                return output + Action.conductAllActions(0);
            }
        } else if (turn == 2 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                areWeLookingForMonstersToBeTributed = false;
                actions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null,null,null, "", null));
                uninterruptedActions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null,null,null, null, null, "", null));
                output = Action.conductUninterruptedAction(0);
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
