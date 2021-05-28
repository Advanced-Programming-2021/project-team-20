package sample.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import sample.controller.duel.GamePackage.Action;
import sample.controller.duel.GamePackage.ActionType;
import sample.controller.duel.GamePackage.DuelBoard;
import sample.controller.duel.GamePackage.DuelController;
import sample.controller.duel.PreliminaryPackage.GameManager;
import sample.controller.duel.Utility.Utility;
import sample.model.cardData.General.Card;
import sample.model.cardData.General.CardLocation;
import sample.model.cardData.General.CardPosition;
import sample.model.cardData.MonsterCardData.MonsterCard;

public class FlipSummonController extends SummonSetCommonClass {
    private CardLocation mainCard;
    private boolean isGoingToChangeTurnsForChaining;
    private boolean isClassWaitingForFurtherChainInput;

    public FlipSummonController() {
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForFurtherChainInput = false;
    }

    public String flipSummonInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)flip-summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
            DuelController duelController = GameManager.getDuelControllerByIndex(0);
            int turn = duelController.getTurn();
            String resultOfChecking = Utility.isACardSelected(0, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
                    return "you can't change this card position";
                } else {
                    resultOfChecking = Utility.areWeInMainPhase(0);
                    if (!resultOfChecking.equals("")) {
                        return resultOfChecking;
                    } else {
                        if (!(card.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION))) {
                            return "you can't flip summon this card";
                        } else {
                            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(0);
                            mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
                            createActionForFlipSummoning(0);
                            String output = Action.conductUninterruptedAction(0);
                            selectCardController.resetSelectedCardLocationList();
                            String canChainingOccur = canChainingOccur(0, duelController.getFakeTurn(), ActionType.ALLY_FLIP_SUMMONING_MONSTER, ActionType.OPPONENT_FLIP_SUMMONING_MONSTER);
                            if (canChainingOccur.equals("")) {
                                return output + Action.conductAllActions(0);
                            }
                            return output + canChainingOccur;
                        }
                    }
                }
            }

        }
        return null;
    }

    public void createActionForFlipSummoning(int index) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int fakeTurn = duelController.getFakeTurn();
        //maybe should get turn
        if (fakeTurn == 1) {
            uninterruptedActions.add(new Action(ActionType.ALLY_FLIP_SUMMONING_MONSTER, fakeTurn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.ALLY_FLIP_SUMMONING_MONSTER, fakeTurn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else {
            uninterruptedActions.add(new Action(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER, fakeTurn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            actions.add(new Action(ActionType.OPPONENT_FLIP_SUMMONING_MONSTER, fakeTurn, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }

}
