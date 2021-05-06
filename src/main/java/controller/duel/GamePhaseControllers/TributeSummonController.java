package controller.duel.GamePhaseControllers;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import jdk.internal.org.objectweb.asm.tree.ModuleNode;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class TributeSummonController extends ChainController{
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

    public String tributeSummonInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)tribute[\\s]+summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            DuelController duelController = GameManager.getDuelControllerByIndex(0);
            int turn = duelController.getTurn();
            NormalSummonController normalSummonController = GameManager.getNormalSummonController(0);
            String resultOfChecking = normalSummonController.normalSummonInputAnalysis("normal summon", "tribute summon");
            if (!resultOfChecking.equals("tribute summoned successfully")) {
                return resultOfChecking;
            }
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
            Card mainCard = duelBoard.getCardByCardLocation(cardLocation);
            if (((MonsterCard) mainCard).getLevel() < 5) {
                return "you can't tribute summon a monster with level less than 5.";
            }
            createActionForTributeSummoningMonster(0);
            String output = Action.conductUninterruptedAction(0);
            String canChainingOccur = canChainingOccur(0, turn, ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER);
            if (!canChainingOccur.equals("")) {
                output += canChainingOccur;
                return output;
            }
            return output + Action.conductAllActions(0);
        }
        return null;
    }

    public void createActionForTributeSummoningMonster(int index) {
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
            uninterruptedActions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, null, null, null, null, null, null, null));
            actions.add(new Action(ActionType.ALLY_TRIBUTE_SUMMONING_MONSTER, 1, mainCard, null, null, null, null, null, null, null, null));
            //add action that conducts effects of the card
        } else if (turn == 2) {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null));
            actions.add(new Action(ActionType.OPPONENT_TRIBUTE_SUMMONING_MONSTER, 2, mainCard, null, null, null, null, null, null, null, null));
            //add action that conducts effects of the card
        }
        cardsToBeTributed.clear();
    }
}
