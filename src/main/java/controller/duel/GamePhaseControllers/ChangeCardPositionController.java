package controller.duel.GamePhaseControllers;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ActionType;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.MonsterCardData.MonsterCard;

public class ChangeCardPositionController extends SummonSetCommonClass {

    public String changeCardPositionInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)set[\\s]+--position[\\s]+(attack|defense)(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            return startChecking(0, string);
        }
        return "invalid command";
    }

    private String startChecking(int index, String string) {
        String resultOfChecking = Utility.isACardSelected(0, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return isSelectedCardInMonsterZone(index, string);
        }
    }

    private String isSelectedCardInMonsterZone(int index, String string) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        if (!duelBoard.isCardInMonsterZone((MonsterCard) card, turn)) {
            return "you can't change this card position";
        } else {
            return checkCorrectPhase(index, string);
        }
    }

    private String checkCorrectPhase(int index, String string) {
        String output = Utility.areWeInMainPhase(0);
        if (!output.equals("")) {
            return output;
        } else {
            return isCardAlreadyInWantedPosition(index, string);
        }
    }

    private CardLocation giveSelectedCardLocation(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        return (selectedCardLocations.get(selectedCardLocations.size() - 1));
    }

    private Card giveSelectedCard(int index) {
        CardLocation cardLocation = giveSelectedCardLocation(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        return duelBoard.getCardByCardLocation(cardLocation);
    }

    private String isCardAlreadyInWantedPosition(int index, String string) {
        Card card = giveSelectedCard(index);
        if (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) && string.equals("attack")) {
            return "this card is already in the wanted position";
        } else if (card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION) && string.equals("defense")) {
            return "this card is already in the wanted position";
        } else if (card.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
            return "you can't change this card position";
        } else {
            return didCardPositionChangeInThisTurn(index, string);
        }
    }

    private String didCardPositionChangeInThisTurn(int index, String string) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        Card card = giveSelectedCard(index);
        MonsterCard monsterCard = (MonsterCard) card;
        if (monsterCard.isCardPositionChanged()) {
            return "you already changed this card position in this turn";
        } else {
            createActionForChangingCardPosition(index);
            selectCardController.resetSelectedCardLocationList();
            String output = Action.conductUninterruptedAction(index);
            return output + Action.conductAllActions(index);
        }
    }

    private void createActionForChangingCardPosition(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        CardLocation mainCard = giveSelectedCardLocation(index);
        ArrayList<Action> actions = GameManager.getActionsByIndex(index);
        ArrayList<Action> uninterruptedActions = GameManager.getUninterruptedActionsByIndex(index);
        if (GameManager.getDuelControllerByIndex(index).getTurn() == 1) {
            actions.add(new Action(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION, 1, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.ALLY_CHANGING_MONSTER_CARD_POSITION, 1, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        } else {
            actions.add(new Action(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION, 2, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
            uninterruptedActions.add(new Action(ActionType.OPPONENT_CHANGING_MONSTER_CARD_POSITION, 2, mainCard, null, null, null, null, null, null, null, null, null, null, null, "", null));
        }
    }
}
