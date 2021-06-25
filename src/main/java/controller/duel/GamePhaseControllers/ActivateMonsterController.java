package controller.duel.GamePhaseControllers;

import controller.duel.CardEffects.MonsterEffectEnums.OptionalMonsterEffect;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;
import model.cardData.MonsterCardData.MonsterCard;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ActivateMonsterController extends ChainController {
    private boolean isClassWaitingForUserToDiscardOneCard;
    private boolean isClassWaitingForUserToChooseMonsterFromGraveyard;
    private boolean isClassWaitingForUserToChooseMonsterFromOpponentGraveyard;
    private int activateMonsterTurn;
    private CardLocation mainMonsterCardLocation;

    public ActivateMonsterController() {
        mainMonsterCardLocation = null;
        activateMonsterTurn = 1;
        isClassWaitingForUserToDiscardOneCard = false;
        isClassWaitingForUserToChooseMonsterFromGraveyard = false;
        isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = false;
    }

    public boolean isClassWaitingForUserToDiscardOneCard() {
        return isClassWaitingForUserToDiscardOneCard;
    }

    public boolean isClassWaitingForUserToChooseMonsterFromGraveyard() {
        return isClassWaitingForUserToChooseMonsterFromGraveyard;
    }

    public boolean isClassWaitingForUserToChooseMonsterFromOpponentGraveyard() {
        return isClassWaitingForUserToChooseMonsterFromOpponentGraveyard;
    }

    public String activateMonsterEffectInputAnalysis(String string) {
        String inputRegex = "(?<=\\n|^)activate[\\s]+effect(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            return startChecking(0);
        }
        return null;
    }

    public String startChecking(int index) {
        String resultOfChecking = Utility.isACardSelected(index, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkTypeOfCardForActivation(index);
        }
    }

    public String checkTypeOfCardForActivation(int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        if (!Card.isCardAMonster(card)) {
            return "activate effect is not possible for this card";
        } else {
            return checkCorrectPhase(index);
        }
    }

    public String checkCorrectPhase(int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
        String resultOfChecking;
        if (turn == fakeTurn) {
            resultOfChecking = Utility.areWeInMainPhase(index);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return isCardInDuelBoard(index);
            }
        } else {
            return isCardInDuelBoard(index);
        }
    }

    public String isCardInDuelBoard(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int fakeTurn = duelController.getFakeTurn();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) && fakeTurn == 1 || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && fakeTurn == 2) {
            return isMonsterCardAlreadyActivated(index);
        }
        return "you can't activate the effect of this card";
    }

    public String isMonsterCardAlreadyActivated(int index) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation mainCardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainCardLocation);
        if (monsterCard.isOncePerTurnCardEffectUsed()) {
            return "you have used your monster's effect once this turn";
        } else {
            return analyzePossibleEffectsOfMonster(mainCardLocation, index);
        }
    }

    public String analyzePossibleEffectsOfMonster(CardLocation cardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        ArrayList<OptionalMonsterEffect> optionalMonsterEffects = monsterCard.getOptionalMonsterEffects();
        if (optionalMonsterEffects.size() == 0) {
            return "there is no effect you can activate";
        } else {
            if (optionalMonsterEffects.contains(OptionalMonsterEffect.ONCE_PER_TURN_DISCARD_1_CARD_SEND_LEVEL_7_OR_MORE_MONSTER_FROM_GY_TO_HAND)) {
                return isPossibleForPlayerToDiscardCardAndChooseLevel7OrHigherFromGraveyard(cardLocation, index);
            }
            if (optionalMonsterEffects.contains(OptionalMonsterEffect.ONCE_PER_TURN_CHOOSE_A_MONSTER_IN_YOUR_OPPONENTS_GRAVEYARD_AND_COPY_ALL_CHARACTERISTICS_UNTIL_THE_END_OF_THAT_TURN)) {
                return isPossibleForPlayerToChooseMonsterFromOpponentGraveyard(cardLocation, index);
            }
            return "there is no effect you can activate";
        }
    }

    public String isPossibleForPlayerToDiscardCardAndChooseLevel7OrHigherFromGraveyard(CardLocation cardLocation, int index) {
        String resultOfChecking = isPossibleToDiscardOneCard(cardLocation, index);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        }
        resultOfChecking = doesMonsterWithLevelAtLeast7ExistInGraveyard(cardLocation, index);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        }
        isClassWaitingForUserToDiscardOneCard = true;
        if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            activateMonsterTurn = 1;
        } else {
            activateMonsterTurn = 2;
        }
        mainMonsterCardLocation = cardLocation;
        return "choose one card from your hand to discard\nSimply enter select command";
    }

    public String isPossibleToDiscardOneCard(CardLocation monsterCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        RowOfCardLocation rowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            ArrayList<Card> cardsInAllyHand = duelBoard.getAllyCardsInHand();
            if (cardsInAllyHand.size() == 0) {
                return "you do not have any cards to discard\nSo you can't activate the effect of this card";
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            ArrayList<Card> cardsInOpponentHand = duelBoard.getOpponentCardsInHand();
            if (cardsInOpponentHand.size() == 0) {
                return "you do not have any cards to discard\nSo you can't activate the effect of this card";
            }
        }
        return "";
    }

    public String doesMonsterWithLevelAtLeast7ExistInGraveyard(CardLocation monsterCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        RowOfCardLocation rowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
        ArrayList<Card> cards = new ArrayList<>();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            cards = duelBoard.getAllyCardsInGraveyard();
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            cards = duelBoard.getOpponentCardsInGraveyard();
        }
        boolean doesMonsterWithLevelAtLeast7Exist = false;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (Card.isCardAMonster(card)) {
                MonsterCard monsterCard = (MonsterCard) card;
                if (monsterCard.getLevel() >= 7) {
                    doesMonsterWithLevelAtLeast7Exist = true;

                }
            }
        }
        if (!doesMonsterWithLevelAtLeast7Exist) {
            return "there is no monster with level at least 7 in your graveyard\nSo you can't activate the effect of this card";
        }
        return "";
    }

    public String isPossibleForPlayerToChooseMonsterFromOpponentGraveyard(CardLocation monsterCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        RowOfCardLocation rowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
        ArrayList<Card> cards = new ArrayList<>();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            cards = duelBoard.getOpponentCardsInGraveyard();
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            cards = duelBoard.getAllyCardsInGraveyard();
        }
        boolean doesMonsterExistInOpponentGraveyard = false;
        for (int i = 0; i < cards.size(); i++) {
            if (Card.isCardAMonster(cards.get(i))) {
                doesMonsterExistInOpponentGraveyard = true;
            }
        }
        if (!doesMonsterExistInOpponentGraveyard) {
            return "there is no monster in your opponent's graveyard\nSo you can't the activate effect of this card";
        }
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            activateMonsterTurn = 1;
        } else {
            activateMonsterTurn = 2;
        }
        isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = true;
        mainMonsterCardLocation = monsterCardLocation;
        return "choose one card from your opponent graveyard\nSimply enter select command";
    }

    public String analyzeInputWhenClassIsWaitingToDiscardCardFromHand() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            sendCardToGraveyardAfterRemoving(cardLocation, 0);
            isClassWaitingForUserToDiscardOneCard = false;
            isClassWaitingForUserToChooseMonsterFromGraveyard = true;
            return "Now choose a monster from your graveyard with level at least 7\nSimply enter select command";
        } else {
            return "you can't select this card for discarding\nSelect another card";
        }
    }

    public String analyzeInputWhenClassIsWaitingToChooseMonsterFromSelfGraveyard() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nselect another card";
            } else {
                if (((MonsterCard) card).getLevel() < 7) {
                    return "chosen monster level is less than 7\nselect another card";
                } else {
                    Card cardToBeAddedToHand = removeCardAndGetRemovedCard(cardLocation, 0);
                    duelBoard.addCardToHand(cardToBeAddedToHand, activateMonsterTurn);
                    isClassWaitingForUserToChooseMonsterFromGraveyard = false;
                    MonsterCard mainMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainMonsterCardLocation);
                    mainMonsterCard.setOncePerTurnCardEffectUsed(true);
                    return "monster effect activated";
                }
            }
        } else {
            return "you can't select this card for bringing to your hand\nselect another card";
        }
    }

    public String analyzeInputWhenClassIsWaitingToChooseMonsterFromOpponentGraveyard() {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nselect another card";
            } else {
                copyPasteCharacteristicsOfCard(mainMonsterCardLocation, cardLocation, 0);
                Card cardToBeAddedToHand = removeCardAndGetRemovedCard(cardLocation, 0);
                duelBoard.addCardToHand(cardToBeAddedToHand, activateMonsterTurn);
                isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = false;
                MonsterCard mainMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainMonsterCardLocation);
                mainMonsterCard.setOncePerTurnCardEffectUsed(true);
                return "monster effect activated";
            }
        } else {
            return "you can't select this card for copying the characteristics\nselect another card";
        }
    }

    public void copyPasteCharacteristicsOfCard(CardLocation copyee, CardLocation copied, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard copyeeMonster = (MonsterCard) duelBoard.getCardByCardLocation(copyee);
        MonsterCard copiedMonster = (MonsterCard) duelBoard.getCardByCardLocation(copied);
        copyeeMonster.setAttackPower(copiedMonster.getAttackPower());
        copyeeMonster.setDefensePower(copiedMonster.getDefensePower());
        copyeeMonster.setLevel(copiedMonster.getLevel());
        copyeeMonster.setMonsterCardFamily(copiedMonster.getMonsterCardFamily());
        copyeeMonster.setMonsterCardValue(copiedMonster.getMonsterCardValue());
        copyeeMonster.setSummoningRequirements(copiedMonster.getSummoningRequirements());
        copyeeMonster.setUponSummoningEffects(copiedMonster.getUponSummoningEffects());
        copyeeMonster.setBeingAttackedEffects(copiedMonster.getBeingAttackedEffects());
        copyeeMonster.setContinuousMonsterEffects(copiedMonster.getContinuousMonsterEffects());
        copyeeMonster.setFlipEffects(copyeeMonster.getFlipEffects());
        copyeeMonster.setOptionalMonsterEffects(copiedMonster.getOptionalMonsterEffects());
        copyeeMonster.setSentToGraveyardEffects(copiedMonster.getSentToGraveyardEffects());
        copyeeMonster.setAttackerEffects(copiedMonster.getAttackerEffects());
        copyeeMonster.setCardName(copiedMonster.getCardName());
        copyeeMonster.setCardType(copiedMonster.getCardType());
        copyeeMonster.setCardDescription(copiedMonster.getCardDescription());
    }

    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card cardGoingToBeSentToGraveyard = duelBoard.getCardByCardLocation(targetingCardLocation);
        if (cardGoingToBeSentToGraveyard != null) {
            int graveyardToSendCardTo;
            RowOfCardLocation rowOfCardLocationOfThrownCard = targetingCardLocation.getRowOfCardLocation();
            if (rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_ZONE) || rowOfCardLocationOfThrownCard.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
                graveyardToSendCardTo = 1;
            } else {
                graveyardToSendCardTo = 2;
            }
            Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
            duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
        }
    }
    public void clearAllVariablesOfThisClass(){
        isClassWaitingForUserToDiscardOneCard  = false;
        isClassWaitingForUserToChooseMonsterFromGraveyard = false;
        isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = false;
        activateMonsterTurn = 0;
        mainMonsterCardLocation = null;
    }
}
