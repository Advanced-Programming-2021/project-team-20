package project.server.controller.duel.GamePhaseControllers;

import project.model.MonsterEffectEnums.OptionalMonsterEffect;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;

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

    public String activateMonsterEffectInputAnalysis(String string, String token) {
        String inputRegex = "(?<=\\n|^)activate[\\s]+effect(?=\\n|$)";
        Matcher matcher = Utility.getCommandMatcher(string, inputRegex);
        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
            return startChecking(token);
        }
        return null;
    }

    public String startChecking(String token) {
        String resultOfChecking = Utility.isACardSelected(token, "", false);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        } else {
            return checkTypeOfCardForActivation(token);
        }
    }

    public String checkTypeOfCardForActivation(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        Card card = duelBoard.getCardByCardLocation(selectedCardLocations.get(selectedCardLocations.size() - 1));
        if (!Card.isCardAMonster(card)) {
            return "activate effect is not possible for this card";
        } else {
            return checkCorrectPhase(token);
        }
    }

    public String checkCorrectPhase(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        int fakeTurn = duelController.getFakeTurn();
        String resultOfChecking;
        if (turn == fakeTurn) {
            resultOfChecking = Utility.areWeInMainPhase(token);
            if (!resultOfChecking.equals("")) {
                return resultOfChecking;
            } else {
                return isCardInDuelBoard(token);
            }
        } else {
            return isCardInDuelBoard(token);
        }
    }

    public String isCardInDuelBoard(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int fakeTurn = duelController.getFakeTurn();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) && fakeTurn == 1 || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && fakeTurn == 2) {
            return isMonsterCardAlreadyActivated(token);
        }
        return "you can't activate the effect of this card";
    }

    public String isMonsterCardAlreadyActivated(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation mainCardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainCardLocation);
        if (monsterCard.isOncePerTurnCardEffectUsed()) {
            return "you have used your monster's effect once this turn";
        } else {
            return analyzePossibleEffectsOfMonster(mainCardLocation, token);
        }
    }

    public String analyzePossibleEffectsOfMonster(CardLocation cardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        ArrayList<OptionalMonsterEffect> optionalMonsterEffects = monsterCard.getOptionalMonsterEffects();
        if (optionalMonsterEffects.size() == 0) {
            return "there is no effect you can activate";
        } else {
            if (optionalMonsterEffects.contains(OptionalMonsterEffect.ONCE_PER_TURN_DISCARD_1_CARD_SEND_LEVEL_7_OR_MORE_MONSTER_FROM_GY_TO_HAND)) {
                return isPossibleForPlayerToDiscardCardAndChooseLevel7OrHigherFromGraveyard(cardLocation, token);
            }
            if (optionalMonsterEffects.contains(OptionalMonsterEffect.ONCE_PER_TURN_CHOOSE_A_MONSTER_IN_YOUR_OPPONENTS_GRAVEYARD_AND_COPY_ALL_CHARACTERISTICS_UNTIL_THE_END_OF_THAT_TURN)) {
                return isPossibleForPlayerToChooseMonsterFromOpponentGraveyard(cardLocation, token);
            }
            return "there is no effect you can activate";
        }
    }

    public String isPossibleForPlayerToDiscardCardAndChooseLevel7OrHigherFromGraveyard(CardLocation cardLocation, String token) {
        String resultOfChecking = isPossibleToDiscardOneCard(cardLocation, token);
        if (!resultOfChecking.equals("")) {
            return resultOfChecking;
        }
        resultOfChecking = doesMonsterWithLevelAtLeast7ExistInGraveyard(cardLocation, token);
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

    public String isPossibleToDiscardOneCard(CardLocation monsterCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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

    public String doesMonsterWithLevelAtLeast7ExistInGraveyard(CardLocation monsterCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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

    public String isPossibleForPlayerToChooseMonsterFromOpponentGraveyard(CardLocation monsterCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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

    public String analyzeInputWhenClassIsWaitingToDiscardCardFromHand(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            sendCardToGraveyardAfterRemoving(cardLocation, token);
            isClassWaitingForUserToDiscardOneCard = false;
            isClassWaitingForUserToChooseMonsterFromGraveyard = true;
            return "Now choose a monster from your graveyard with level at least 7\nSimply enter select command";
        } else {
            return "you can't select this card for discarding\nSelect another card";
        }
    }

    public String analyzeInputWhenClassIsWaitingToChooseMonsterFromSelfGraveyard(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nselect another card";
            } else {
                if (((MonsterCard) card).getLevel() < 7) {
                    return "chosen monster level is less than 7\nselect another card";
                } else {
                    Card cardToBeAddedToHand = removeCardAndGetRemovedCard(cardLocation, token);
                    duelBoard.addCardToHand(cardToBeAddedToHand, activateMonsterTurn);
                    GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                        + " " + cardLocation.getIndex() + " is being added to hand zone " + activateMonsterTurn + " and should finally be NO_CHANGE", token);

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

    public String analyzeInputWhenClassIsWaitingToChooseMonsterFromOpponentGraveyard(String token) {
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        if (activateMonsterTurn == 1 && rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE) || activateMonsterTurn == 2 && rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (!Card.isCardAMonster(card)) {
                return "chosen card is not a monster\nselect another card";
            } else {
                copyPasteCharacteristicsOfCard(mainMonsterCardLocation, cardLocation, token);
                Card cardToBeAddedToHand = removeCardAndGetRemovedCard(cardLocation, token);
                duelBoard.addCardToHand(cardToBeAddedToHand, activateMonsterTurn);
                GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                    + " " + cardLocation.getIndex() + " is being added to hand zone " + activateMonsterTurn + " and should finally be NO_CHANGE", token);

                isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = false;
                MonsterCard mainMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(mainMonsterCardLocation);
                mainMonsterCard.setOncePerTurnCardEffectUsed(true);
                return "monster effect activated";
            }
        } else {
            return "you can't select this card for copying the characteristics\nselect another card";
        }
    }

    public void copyPasteCharacteristicsOfCard(CardLocation copyee, CardLocation copied, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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

    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
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
            Card card = removeCardAndGetRemovedCard(targetingCardLocation, token);
            duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);

            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + targetingCardLocation.getRowOfCardLocation()
                + " " + targetingCardLocation.getIndex() + " is being added to graveyard zone " + graveyardToSendCardTo + " and should finally be FACE_UP_ATTACK_POSITION or FACE_UP_ACTIVATED_POSITION ", token);



        }
    }

    public void clearAllVariablesOfThisClass() {
        isClassWaitingForUserToDiscardOneCard = false;
        isClassWaitingForUserToChooseMonsterFromGraveyard = false;
        isClassWaitingForUserToChooseMonsterFromOpponentGraveyard = false;
        activateMonsterTurn = 0;
        mainMonsterCardLocation = null;
    }
}
