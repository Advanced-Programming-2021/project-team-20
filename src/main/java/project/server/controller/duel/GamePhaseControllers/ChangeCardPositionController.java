package project.server.controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import project.server.controller.duel.GamePackage.Action;
import project.model.ActionType;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;

public class ChangeCardPositionController extends SummonSetCommonClass {

    public String changeCardPositionInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)set[\\s]+--position[\\s]+(attack|defense)(?=\\n|$)";
        String secondInputRegex = "(?<=\\n|^)set[\\s]+-p[\\s]+(attack|defense)(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        Matcher secondMatcher = Utility.getCommandMatcher(string, secondInputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher) || Utility.isMatcherCorrectWithoutErrorPrinting(secondMatcher)) {
            return startChecking(token, string);
        }
        return "invalid command";
    }

    private String startChecking(String token, String string) {
        String resultOfChecking = Utility.isACardSelected(token, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return isSelectedCardInMonsterZone(token, string);
        }
    }

    private String isSelectedCardInMonsterZone(String token, String string) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
        if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
            return "you can't change this card position";
        } else {
            return checkCorrectPhase(token, string);
        }
    }

    private String checkCorrectPhase(String token, String string) {
        String output = Utility.areWeInMainPhase(token);
        if (!output.equals("")) {
            return output;
        } else {
            return isCardAlreadyInWantedPosition(token, string);
        }
    }

    private CardLocation giveSelectedCardLocation(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        return (selectedCardLocations.get(selectedCardLocations.size() - 1));
    }

    private Card giveSelectedCard(String token) {
        CardLocation cardLocation = giveSelectedCardLocation(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        return duelBoard.getCardByCardLocation(cardLocation);
    }

    private String isCardAlreadyInWantedPosition(String token, String string) {
        Card card = giveSelectedCard(token);
        if (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && string.equals("attack")) {
            return "this card is already in the wanted position";
        } else if (card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) && string.equals("defense")) {
            return "this card is already in the wanted position";
        } else if (card.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
            return "you can't change this card position";
        } else {
            return didCardPositionChangeInThisTurn(token, string);
        }
    }

    private String didCardPositionChangeInThisTurn(String token, String string) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        Card card = giveSelectedCard(token);
        MonsterCard monsterCard = (MonsterCard) card;
        if (monsterCard.isCardPositionChanged()) {
            return "you already changed this card position in this turn";
        } else {
            createActionForChangingCardPosition(token);
            selectCardController.resetSelectedCardLocationList();
            String output = Action.conductUninterruptedAction(token);
            return output + Action.conductAllActions(token);
        }
    }

    private void createActionForChangingCardPosition(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        CardLocation mainCard = giveSelectedCardLocation(token);
        ArrayList<Action> actions = GameManager.getActionsByIndex(token);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(token);
        if (GameManager.getDuelControllerByIndex(token).getTurn() == 1) {
            actions.add(new Action(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION, 1, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION, 1, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else {
            actions.add(new Action(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION, 2, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION, 2, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }
}
