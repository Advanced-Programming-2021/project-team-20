package project.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.controller.duel.GamePackage.Action;
import project.controller.duel.GamePackage.ActionType;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

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

    public String tributeSummonInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)tribute[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(0);
            String resultOfChecking = normalSummonController.normalSummonInputAnalysis("normal summon", "tribute summon");
            if (resultOfChecking.startsWith("you can't") || resultOfChecking.startsWith("there are")) {
                return resultOfChecking;
            }
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
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
                ArrayList<Action> actions = GameManager.getActionsByIndex(0);
                ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
                areWeLookingForMonstersToBeTributed = false;
                if (turn == 1 && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                    createActionForTributeSummoningMonster(0);
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 1, ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER);
                } else {
                    createActionForTributeSummoningMonster(0);
                    output = Action.conductUninterruptedAction(0);
                    canChainingOccur = canChainingOccur(0, 2, ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER);
                }
                //apply gate guardian effect
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

    public void createActionForTributeSummoningMonster(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        //System.out.println("mainCard\n" + mainCard.getRowOfCardLocation().toString() + mainCard.getIndex());
        selectCardController.resetSelectedCardLocationList();
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        if (turn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            actions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            //add action that conducts effects of the card
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed,  null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            actions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, cardsToBeTributed, null, null, null, null, null, null, null, null, CardPosition.FACE_UP_ATTACK_POSITION, "", null));
            //add action that conducts effects of the card
        }
        cardsToBeTributed.clear();
    }
}