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
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;

public class FlipSummonController extends SummonSetCommonClass {
    private CardLocation mainCard;
    private boolean isGoingToChangeTurnsForChaining;
    private boolean isClassWaitingForFurtherChainInput;

    public FlipSummonController() {
        isGoingToChangeTurnsForChaining = false;
        isClassWaitingForFurtherChainInput = false;
    }

    public String flipSummonInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)flip-summon(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
            ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
            DuelController duelController = GameManager.getDuelControllerByIndex(token);
            int turn = duelController.getTurn();
            String resultOfChecking = Utility.isACardSelected(token, "", false);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
                    return "you can't change this card position";
                } else {
                    resultOfChecking = Utility.areWeInMainPhase(token);
                    if (!resultOfChecking.equals("")) {
                        return resultOfChecking;
                    } else {
                        if (!(card.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION))) {
                            return "you can't flip summon this card";
                        } else {
                            ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
                            mainCard = selectedCardLocations.get(selectedCardLocations.size() - 1);
                            createActionForFlipSummoning(token);
                            String output = Action.conductUninterruptedAction(token);
                            selectCardController.resetSelectedCardLocationList();
                            String canChainingOccur = canChainingOccur(token, duelController.getFakeTurn(), ActionType.ALLY_FLIP_SUMMONING_MONSTER, ActionType.OPPONENT_FLIP_SUMMONING_MONSTER);
                            if (canChainingOccur.equals("")) {
                                return output + Action.conductAllActions(token);
                            }
                            return output + canChainingOccur;
                        }
                    }
                }
            }

        }
        return null;
    }

    public void createActionForFlipSummoning(String token) {
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
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
