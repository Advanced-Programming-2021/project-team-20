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

    public String setCardControllerInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)set(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            String resultOfChecking = startChecking(token, "set", true);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
                if (Card.isCardAMonster(card)) {
                    return monsterCardSetInputCheck(card, token);
                } else if (Card.isCardASpell(card) || Card.isCardATrap(card)) {
                    return spellOrTrapCardSetInputCheck(card, token);
                }
            }
        }
        return null;
    }

    public boolean isAreWeLookingForMonstersToBeTributed() {
        return areWeLookingForMonstersToBeTributed;
    }

    public String monsterCardSetInputCheck(Card card, String token) {
        String resultOfChecking = isMonsterCardZoneFull(token);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            resultOfChecking = hasUserAlreadySummonedSet(token);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return analyzeMonsterCardToBeSet(token, card);
            }
        }
    }

    public String spellOrTrapCardSetInputCheck(Card card, String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        String resultOfChecking = isSpellCardZoneFull(token, card);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            createActionForSettingSpellOrTrap(token);
            String output = Action.conductUninterruptedAction(token);
            return output + Action.conductAllActions(token);
        }
    }

    public String analyzeMonsterCardToBeSet(String token, Card card) {
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        String cardAnalysis = convertMessageFromEffectToControllerToString(card, duelBoard, turn, "set");
        if (cardAnalysis.equals("set successfully")) {
            createActionForSettingMonster(token);
            return Action.conductUninterruptedAction(token) + Action.conductAllActions(token);
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

    public void createActionForSettingSpellOrTrap(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
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

    public void createActionForSettingMonster(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        if (turn == 1) {
            actions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, null, null, null, null, null, null,null,null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_SETTING_MONSTER, 1, mainCard, null, null, null, null, null,null,null, null, null, null, null, "", null));
        } else if (turn == 2) {
            actions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null, null,null,null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null,null,null, null, "", null));
        }
    }

    public String redirectInput(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(cardLocation);
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
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
                output = Action.conductUninterruptedAction(token);
                return output + Action.conductAllActions(token);
            }
        } else if (turn == 2 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            cardsToBeTributed.add(cardLocation);
            selectCardController.resetSelectedCardLocationList();
            numberOfCardsToBeTributed -= 1;
            if (numberOfCardsToBeTributed == 0) {
                areWeLookingForMonstersToBeTributed = false;
                actions.add(new Action(ActionType.OPPONENT_SETTING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null,null,null, "", null));
                uninterruptedActions.add(new Action(ActionType.OPPONENT_DIRECT_ATTACKING, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null,null,null, null, null, "", null));
                output = Action.conductUninterruptedAction(token);
                return output + Action.conductAllActions(token);
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
