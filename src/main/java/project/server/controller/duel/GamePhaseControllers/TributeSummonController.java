package project.server.controller.duel.GamePhaseControllers;

import project.server.controller.duel.GamePackage.Action;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.model.ActionType;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;


import java.util.ArrayList;
import java.util.regex.Matcher;

public class TributeSummonController extends ChainController {
    private boolean areWeLookingForMonstersToBeTributed;
    private CardLocation mainCard;
    private ArrayList<CardLocation> cardsToBeTributed;
    private int numberOfCardsToBeTributed;

    public TributeSummonController() {
        areWeLookingForMonstersToBeTributed = false;
        cardsToBeTributed = new ArrayList<>();
        numberOfCardsToBeTributed = 0;
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForChainCardToBeSelected = false;
    }

    public boolean isAreWeLookingForMonstersToBeTributed() {
        return areWeLookingForMonstersToBeTributed;
    }

    public String tributeSummonInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)tribute[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(token);
            String resultOfChecking = normalSummonController.normalSummonInputAnalysis("normal summon", "tribute summon");
            if (resultOfChecking.startsWith("you can't") || resultOfChecking.startsWith("there are")) {
                return resultOfChecking;
            }
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
            selectCardController.resetSelectedCardLocationList();
            areWeLookingForMonstersToBeTributed = true;
            if (resultOfChecking.startsWith("please choose one")) {
                numberOfCardsToBeTributed = 1;
            } else if (resultOfChecking.startsWith("please choose two")) {
                numberOfCardsToBeTributed = 2;
            } else if (resultOfChecking.startsWith("please choose three")) {
                numberOfCardsToBeTributed = 3;
            }
            return resultOfChecking;
        }
        return null;
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
                    createActionForTributeSummoningMonster(token);
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token, 1, ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER);
                } else {
                    createActionForTributeSummoningMonster(token);
                    output = Action.conductUninterruptedAction(token);
                    canChainingOccur = canChainingOccur(token, 2, ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER);
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
        return null;
    }

    public void createActionForTributeSummoningMonster(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            actions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            actions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
        }
        cardsToBeTributed.clear();
    }

    public void clearAllVariablesOfThisClass() {
        areWeLookingForMonstersToBeTributed = false;
        mainCard = null;
        cardsToBeTributed.clear();
        numberOfCardsToBeTributed = 0;
    }
}
