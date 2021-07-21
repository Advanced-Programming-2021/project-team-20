package project.server.controller.duel.GamePackage.ActionConductors;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import project.model.SpellEffectEnums.*;
import project.server.controller.duel.GamePackage.Action;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;

public class ActivateSpellConductor {

    public static String conductActivatingSpellUninterruptedAction(String token, int numberInListOfActions) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(token).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation mainSpellCardLocation = uninterruptedAction.getMainCardLocation();
        Card mainSpellCard = duelBoard.getCardByCardLocation(mainSpellCardLocation);
        //System.out.println("CARD THAT IS ASSUMED TO BE ACTIVATED HAS NAME " + mainSpellCard.getCardName());
        //System.out.println("LOCATION " + mainSpellCardLocation.getRowOfCardLocation() + " " + mainSpellCardLocation.getIndex());
        RowOfCardLocation rowOfMainSpellCardLocation = mainSpellCardLocation.getRowOfCardLocation();
        CardLocation superFinalCardLocation = null;
        if (rowOfMainSpellCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            if (((SpellCard) mainSpellCard).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                CardLocation finalCardLocationOfFieldSpellCard = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
                uninterruptedAction.setFinalMainCardLocation(finalCardLocationOfFieldSpellCard);
                sendUseLessSpellFieldCardsToGraveyard(token);
            } else {
                superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true).getIndex() + 1);
                uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
            }
            tendToFirstAndSecondCardsInHandWithBadIndex(token, numberInListOfActions, mainSpellCardLocation);
            duelBoard.removeCardByCardLocation(mainSpellCardLocation);
            int actionTurn = uninterruptedAction.getActionTurn();
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + mainSpellCardLocation.getRowOfCardLocation()
                + " " + mainSpellCardLocation.getIndex() + " is being added to spell zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ACTIVATED_POSITION", token);
            duelBoard.addCardToSpellZone(mainSpellCard, actionTurn, token);
        } else if (rowOfMainSpellCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            if (((SpellCard) mainSpellCard).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                CardLocation finalCardLocationOfFieldSpellCard = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
                uninterruptedAction.setFinalMainCardLocation(finalCardLocationOfFieldSpellCard);
                sendUseLessSpellFieldCardsToGraveyard(token);
            } else {
                superFinalCardLocation = new CardLocation(duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false).getRowOfCardLocation(),
                    duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false).getIndex() + 1);
                uninterruptedAction.setFinalMainCardLocation(superFinalCardLocation);
            }
            tendToFirstAndSecondCardsInHandWithBadIndex(token, numberInListOfActions, mainSpellCardLocation);
            duelBoard.removeCardByCardLocation(mainSpellCardLocation);
            int actionTurn = uninterruptedAction.getActionTurn();
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + mainSpellCardLocation.getRowOfCardLocation()
                + " " + mainSpellCardLocation.getIndex() + " is being added to spell zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ACTIVATED_POSITION", token);
            duelBoard.addCardToSpellZone(mainSpellCard, actionTurn, token);
        } else {
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + mainSpellCardLocation.getRowOfCardLocation()
                + " " + mainSpellCardLocation.getIndex() + " is being stayed at spell zone " + uninterruptedAction.getActionTurn() + " and should finally be FACE_UP_ACTIVATED_POSITION", token);
        }
        // GameManager.getDuelControllerByIndex(index).addStringToAvailableCardLocationForUseForClient(superFinalCardLocation);
        //if none of the above two ifs occur, then the card was set and we dont need to change location of spell card
        mainSpellCard.setCardPosition(CardPosition.FACE_UP_ACTIVATED_POSITION);
        return "spell card ready for activation";
    }

    private static void sendUseLessSpellFieldCardsToGraveyard(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation firstPossibleCardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        Card possibleCard = duelBoard.getCardByCardLocation(firstPossibleCardLocation);
        if (Card.isCardASpell(possibleCard)) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(firstPossibleCardLocation, token);
        }
        CardLocation secondPossibleCardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        Card secondPossibleCard = duelBoard.getCardByCardLocation(secondPossibleCardLocation);
        if (Card.isCardASpell(secondPossibleCard)) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(secondPossibleCardLocation, token);
        }
    }

    private static void tendToFirstAndSecondCardsInHandWithBadIndex(String token, int numberInListOfActions, CardLocation mainSpellCardLocation) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(token).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        SpellCard spellCard = (SpellCard) duelBoard.getCardByCardLocation(mainSpellCardLocation);
        if (spellCard.getSpellCardValue().equals(SpellCardValue.RITUAL)) {
            ArrayList<CardLocation> cardsToBeRitualSummoned = uninterruptedAction.getCardsToBeRitualSummoned();
            int indexOfRitualMonsterToBeSummoned = cardsToBeRitualSummoned.get(cardsToBeRitualSummoned.size() - 1).getIndex();
            uninterruptedAction.setSecondCardInHandAfterFirstCardInHand(indexOfRitualMonsterToBeSummoned > mainSpellCardLocation.getIndex());
        }
        if (spellCard.getSpellCardValue().equals(SpellCardValue.QUICK_PLAY)) {
            ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
            if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
                ArrayList<CardLocation> cardLocationsToBeDiscarded = uninterruptedAction.getCardsToBeDiscarded();
                int indexOfCardToBeDiscarded = cardLocationsToBeDiscarded.get(cardLocationsToBeDiscarded.size() - 1).getIndex();
                uninterruptedAction.setSecondCardInHandAfterFirstCardInHand(indexOfCardToBeDiscarded > mainSpellCardLocation.getIndex());
            }
        }
    }

    public static String conductActivatingSpell(String token, int numberInListOfActions) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(token).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        SpellCard spellCard = (SpellCard) duelBoard.getCardByCardLocation(finalMainCardLocation);
        Action action = GameManager.getActionsByIndex(token).get(numberInListOfActions);
        if (action.isActionCanceled()) {
            return "\nactivation of spell was canceled";
        }
        return switchCaseAmongAllSpellEffects(uninterruptedAction, token, spellCard, numberInListOfActions);
    }

    public static String switchCaseAmongAllSpellEffects(Action uninterruptedAction, String token, SpellCard spellCard, int numberInListOfActions) {
        if (spellCard == null) {
            return "\nactivation of spell was canceled";
        }
        ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
        ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
        ArrayList<FieldSpellExtendedEffect> fieldSpellExtendedEffects = spellCard.getFieldSpellExtendedEffects();
        ArrayList<EquipSpellExtendedEffect> equipSpellExtendedEffects = spellCard.getEquipSpellExtendedEffects();
        ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
        ArrayList<RitualSpellEffect> ritualSpellEffects = spellCard.getRitualSpellEffects();
        if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
            ArrayList<CardLocation> targetingCards = uninterruptedAction.getTargetingCards();
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(targetingCards.size() - 1), token);
            //sendCardToGraveyardAfterRemoving(targetingCards.get(targetingCards.size() - 1), index, GameManager.getDuelControllerByIndex(index).getFakeTurn());
        }
        if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
            ArrayList<CardLocation> targetingCards = uninterruptedAction.getTargetingCards();
            for (int i = 0; i < targetingCards.size(); i++) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), token);
            }
            ArrayList<CardLocation> cardsToBeDiscarded = uninterruptedAction.getCardsToBeDiscarded();
            CardLocation cardLocation = cardsToBeDiscarded.get(cardsToBeDiscarded.size() - 1);
            if (uninterruptedAction.isSecondCardInHandAfterFirstCardInHand()) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(new CardLocation(cardLocation.getRowOfCardLocation(), cardLocation.getIndex() - 1), token);
            } else {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, token);
            }
        }
        if (quickSpellEffects.contains(QuickSpellEffect.TRAP_CARD_INFLICTING_DAMAGE_IS_ACTIVATED_SET_DAMAGE_OF_TRAP_CARD_TO_0)) {
            Action previousAction = GameManager.getUninterruptedActionsByIndex(token).get(numberInListOfActions - 1);
            previousAction.setDamageInflictionIsPossible(false);
        }
        if (quickSpellEffects.contains(QuickSpellEffect.TARGET_FACE_UP_MONSTER_WILL_HAVE_HALF_ATTACK_UNTIL_THE_END_OF_THIS_TURN)) {
            ArrayList<CardLocation> targetingCards = uninterruptedAction.getTargetingCards();
            Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(targetingCards.get(0));
            if (Card.isCardAMonster(card)) {
                ((MonsterCard) card).setShouldATKBeHalvedOnlyThisTurn(true);
            }
        }
        if (quickSpellEffects.contains(QuickSpellEffect.TARGET_FACE_UP_MONSTER_WILL_GAIN_700_ATTACK_UNTIL_THE_END_OF_THIS_TURN)) {
            ArrayList<CardLocation> targetingCards = uninterruptedAction.getTargetingCards();
            Card card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(targetingCards.get(0));
            if (Card.isCardAMonster(card)) {
                ((MonsterCard) card).setShouldATKBeIncreasedOnlyThisTurn(true);
            }
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE)) {
            ArrayList<CardLocation> cardsToTakeControlOf = uninterruptedAction.getCardsToTakeControlOf();
            takeControlOfCard(cardsToTakeControlOf.get(cardsToTakeControlOf.size() - 1), token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.FLIP_OPPONENT_MONSTER_CARDS)) {
            flipOpponentMonsterCards(token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
            ArrayList<CardLocation> cardsToBeSpecialSummoned = uninterruptedAction.getCardsToBeSpecialSummoned();
            ArrayList<CardPosition> cardsInFaceUpAttackPositionOrDefensePosition = uninterruptedAction.getCardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition();
            specialSummonCard(cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1), cardsInFaceUpAttackPositionOrDefensePosition.get(cardsInFaceUpAttackPositionOrDefensePosition.size() - 1), token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.ADD_SPELL_FIELD_CARD_FROM_DECK_TO_HAND)) {
            ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand = uninterruptedAction.getCardsToBeChosenFromDeckAndAddedToHand();
            addCardToHand(cardsToBeChosenFromDeckAndAddedToHand.get(cardsToBeChosenFromDeckAndAddedToHand.size() - 1), token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DRAW_2_CARDS)) {
            drawCard(token, GameManager.getDuelControllerByIndex(token).getFakeTurn());
            drawCard(token, GameManager.getDuelControllerByIndex(token).getFakeTurn());
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)) {
            destroyAllOfOpponentsSpellAndTrapCards(token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
            destroyAllOfOpponentsMonsterCards(token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
            destroyAllMonsterCardsInTheField(token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.ALL_SPELL_TRAPS_IN_FIELD_GO_TO_RESPECTIVE_HANDS)) {
            returnAllSpellTrapsToRespectiveHands(token, uninterruptedAction);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.BOTH_PLAYERS_DISCARD_ALL_CARDS_FROM_HAND_AND_DRAW_CARDS_EQUAL_TO_THROWN_AWAY_CARDS)) {
            bothPlayersDiscardCardsAndDrawAsManyThrownAway(token);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_ONE_LEVEL_5_OR_HIGHER_NORMAL_MONSTER_FROM_YOUR_HAND)) {
            ArrayList<CardLocation> cardsToBeSpecialSummoned = uninterruptedAction.getCardsToBeSpecialSummoned();
            ArrayList<CardPosition> cardsInFaceUpAttackPositionOrDefensePosition = uninterruptedAction.getCardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition();
            specialSummonCard(cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1), cardsInFaceUpAttackPositionOrDefensePosition.get(cardsInFaceUpAttackPositionOrDefensePosition.size() - 1), token);
        }
        if (equipSpellExtendedEffects.size() > 0) {
            ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo = uninterruptedAction.getCardsToBeChosenToApplyEquipSpellTo();
            equipSpellEffect(token, cardsToBeChosenToApplyEquipSpellTo, equipSpellExtendedEffects, uninterruptedAction);
        }
        if (ritualSpellEffects.contains(RitualSpellEffect.SEND_NORMAL_MONSTERS_WITH_SUM_OF_LEVELS_EQUAL_TO_MONSTERS_LEVEL_FROM_DECK_TO_GRAVEYARD)) {
            ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard = uninterruptedAction.getCardsToBeChosenFromDeckAndSentToGraveyard();
            sendCardsFromSensitiveArrayListToGraveyard(cardsToBeChosenFromDeckAndSentToGraveyard, token);
        }
        if (ritualSpellEffects.contains(RitualSpellEffect.RITUAL_SUMMON_CHOSEN_MONSTER_FROM_HAND)) {
            ritualSummonMonster(uninterruptedAction, token);
        }
        CardLocation finalMainCardLocation = uninterruptedAction.getFinalMainCardLocation();
        String output = give500LifePointsToPlayerOwningACardOfThisEffect(token, finalMainCardLocation);
        if (fieldSpellExtendedEffects.get(0).getFieldSpellEffects().size() == 0 && equipSpellExtendedEffects.get(0).getEquipSpellEffects().size() == 0
            && continuousSpellCardEffects.size() == 0) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(finalMainCardLocation, token);
        }
        if (!output.equals("")) {
            return "spell activated\n" + output;
        }
        return "spell activated";
    }


    private static void returnAllSpellTrapsToRespectiveHands(String token, Action uninterruptedAction) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation;
        Card card;
        for (int i = 0; i < 5; i++) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1);
            card = duelBoard.getCardByCardLocation(cardLocation);
            if (card != null && uninterruptedAction.getFinalMainCardLocation() != cardLocation) {
                SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
                duelBoard.addCardToHand(card, 1);
                GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                    + " " + cardLocation.getIndex() + " is being added to hand zone " + 1 + " and should finally be NO_CHANGE", token);
            }
        }
        cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        card = duelBoard.getCardByCardLocation(cardLocation);
        if (card != null) {
            SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
            duelBoard.addCardToHand(card, 1);
            GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                + " " + cardLocation.getIndex() + " is being added to hand zone " + 1 + " and should finally be NO_CHANGE", token);
        }
        for (int i = 0; i < 5; i++) {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1);
            card = duelBoard.getCardByCardLocation(cardLocation);
            if (card != null && uninterruptedAction.getFinalMainCardLocation() != cardLocation) {
                SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
                duelBoard.addCardToHand(card, 2);
                GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
                    + " " + cardLocation.getIndex() + " is being added to hand zone " + 2 + " and should finally be NO_CHANGE", token);

            }
        }
        cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        card = duelBoard.getCardByCardLocation(cardLocation);
        if (card != null) {
            SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
            duelBoard.addCardToHand(card, 2);
        }
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
            + " " + cardLocation.getIndex() + " is being added to hand zone " + 2 + " and should finally be NO_CHANGE", token);
    }

    private static void bothPlayersDiscardCardsAndDrawAsManyThrownAway(String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation;
        Card card;
        int allySize = duelBoard.getAllyCardsInHand().size();
        for (int i = 0; i < allySize; i++) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1);
            card = duelBoard.getCardByCardLocation(cardLocation);
            if (card != null) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, token);
            }
        }
        int opponentSize = duelBoard.getOpponentCardsInHand().size();
        for (int i = 0; i < opponentSize; i++) {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, i + 1);
            card = duelBoard.getCardByCardLocation(cardLocation);
            if (card != null) {
                SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(cardLocation, token);
            }
        }
        for (int i = 0; i < allySize; i++) {
            drawCard(token, 1);
        }
        for (int i = 0; i < opponentSize; i++) {
            drawCard(token, 2);
        }
    }

    public static void ritualSummonMonster(Action uninterruptedAction, String token) {
        ArrayList<CardLocation> cardsToBeRitualSummoned = uninterruptedAction.getCardsToBeRitualSummoned();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation initialCardLocationOfRitualMonster = cardsToBeRitualSummoned.get(cardsToBeRitualSummoned.size() - 1);
        CardLocation correctCardLocationOfRitualMonster;
        if (uninterruptedAction.isSecondCardInHandAfterFirstCardInHand()) {
            correctCardLocationOfRitualMonster = new CardLocation(initialCardLocationOfRitualMonster.getRowOfCardLocation(), initialCardLocationOfRitualMonster.getIndex() - 1);
        } else {
            correctCardLocationOfRitualMonster = new CardLocation(initialCardLocationOfRitualMonster.getRowOfCardLocation(), initialCardLocationOfRitualMonster.getIndex());
        }
        Card card = duelBoard.removeCardByCardLocation(correctCardLocationOfRitualMonster);
        duelBoard.addCardToMonsterZone(card, uninterruptedAction.getActionTurn(), token);
        card.setCardPosition(uninterruptedAction.getCardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition().get(0));
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + correctCardLocationOfRitualMonster.getRowOfCardLocation()
            + " " + correctCardLocationOfRitualMonster.getIndex() + " is being added to monster zone " + uninterruptedAction.getActionTurn() + " and should finally be "
            + uninterruptedAction.getCardsToBeSpecialSummonedInFaceUpAttackPositionOrDefensePosition().get(0), token);
        uninterruptedAction.setSecondCardInHandAfterFirstCardInHand(false);
    }

    public static void takeControlOfCard(CardLocation cardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
        CardLocation takenControlledOfCardLocation;
        CardLocation superFinalCardLocation = null;
        if (turn == 1) {
            superFinalCardLocation = new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true).getIndex() + 1);
            takenControlledOfCardLocation = superFinalCardLocation;
        } else {
            superFinalCardLocation = new CardLocation(
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getRowOfCardLocation(),
                duelBoard.giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false).getIndex() + 1);
            takenControlledOfCardLocation = superFinalCardLocation;
        }
        // GameManager.getDuelControllerByIndex(index).addStringToAvailableCardLocationForUseForClient(superFinalCardLocation);
        duelBoard.addCardToMonsterZone(card, turn, token);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
            + " " + cardLocation.getIndex() + " is being added to monster zone " + turn + " and should finally be "
            + "NO_CHANGE", token);
        duelBoard.addCardLocationToCardLocationsToBeTakenBackInEndPhase(takenControlledOfCardLocation);
    }

    public static void flipOpponentMonsterCards(String token) {
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        int turn = duelController.getTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> monsterCards;
        if (turn == 1) {
            monsterCards = duelBoard.getOpponentMonsterCards();
        } else {
            monsterCards = duelBoard.getAllyMonsterCards();
        }
        for (Card card : monsterCards) {
            if (Card.isCardAMonster(card)) {
                MonsterCard monsterCard = (MonsterCard) card;
                if (monsterCard.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                    monsterCard.setCardPosition(CardPosition.FACE_UP_DEFENSE_POSITION);
                }
            }
        }
    }

    public static void sendCardsFromSensitiveArrayListToGraveyard(ArrayList<CardLocation> cardsToBeChosenFromDeckAndSentToGraveyard, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        duelBoard.sendCardsFromSensitiveArrayListToGraveyard(cardsToBeChosenFromDeckAndSentToGraveyard, token);
    }

    public static String give500LifePointsToPlayerOwningACardOfThisEffect(String token, CardLocation spellCardBeingActivated) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        DuelController duelController = GameManager.getDuelControllerByIndex(token);
        ArrayList<Card> allySpellCards = duelBoard.getAllySpellCards();
        ArrayList<Card> opponentSpellCards = duelBoard.getOpponentSpellCards();
        String output = "";
        for (int i = 0; i < allySpellCards.size(); i++) {
            CardLocation cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (Card.isCardASpell(card) && !(spellCardBeingActivated.getRowOfCardLocation() == RowOfCardLocation.ALLY_SPELL_ZONE && spellCardBeingActivated.getIndex() == i + 1)) {
                SpellCard spellCard = (SpellCard) card;
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.IF_A_SPELL_IS_ACTIVATED_OWNER_GAINS_500_LIFE_POINTS) && spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                    duelController.increaseLifePoints(500, 1, token);
                    output += ("500 life points is added to player " + duelController.getPlayingUsernameByTurn(1) + "\n");
                }
            }
        }
        for (int i = 0; i < opponentSpellCards.size(); i++) {
            CardLocation cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1);
            Card card = duelBoard.getCardByCardLocation(cardLocation);
            if (Card.isCardASpell(card) && !(spellCardBeingActivated.getRowOfCardLocation() == RowOfCardLocation.OPPONENT_SPELL_ZONE && spellCardBeingActivated.getIndex() == i + 1)) {
                SpellCard spellCard = (SpellCard) card;
                ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
                if (continuousSpellCardEffects.contains(ContinuousSpellCardEffect.IF_A_SPELL_IS_ACTIVATED_OWNER_GAINS_500_LIFE_POINTS) && spellCard.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                    duelController.increaseLifePoints(500, 2, token);
                    output += ("500 life points is added to player " + duelController.getPlayingUsernameByTurn(2) + "\n");

                }
            }
        }
        return output;
    }

    public static void specialSummonCard(CardLocation monsterCardLocation, CardPosition cardPosition, String token) {
        int fakeTurn = GameManager.getDuelControllerByIndex(token).getFakeTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = SendCardToGraveyardConductor.removeCardAndGetRemovedCard(monsterCardLocation, token);
        duelBoard.addCardToMonsterZone(card, fakeTurn, token);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + monsterCardLocation.getRowOfCardLocation()
            + " " + monsterCardLocation.getIndex() + " is being added to monster zone " + fakeTurn + " and should finally be "
            + cardPosition, token);
        card.setCardPosition(cardPosition);
    }

    public static void addCardToHand(CardLocation cardLocation, String token) {
        int fakeTurn = GameManager.getDuelControllerByIndex(token).getFakeTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card card = SendCardToGraveyardConductor.removeCardAndGetRemovedCard(cardLocation, token);
        duelBoard.addCardToHand(card, fakeTurn);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
            + " " + cardLocation.getIndex() + " is being added to hand zone " + fakeTurn + " and should finally be NO_CHANGE", token);
    }

    public static void drawCard(String token, int fakeTurn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        CardLocation cardLocation;
        if (fakeTurn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
        } else {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 1);
        }
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        duelBoard.removeCardByCardLocation(cardLocation);
        duelBoard.addCardToHand(card, fakeTurn);
        GameManager.getDuelControllerByIndex(token).addStringToSuperAlmightyString("mainCardLocation " + cardLocation.getRowOfCardLocation()
            + " " + cardLocation.getIndex() + " is being added to hand zone " + fakeTurn + " and should finally be NO_CHANGE", token);

    }

    public static void sendAllCardsInThisArrayListToGraveyard(ArrayList<CardLocation> targetingCards, String token) {
        for (int i = 0; i < targetingCards.size(); i++) {
            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(targetingCards.get(i), token);
        }
    }

    public static ArrayList<CardLocation> giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation rowOfCardLocation) {
        ArrayList<CardLocation> cardLocations = new ArrayList<>();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1));
        }
        return cardLocations;
    }

    public static void destroyAllOfOpponentsSpellAndTrapCards(String token) {
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
        ArrayList<CardLocation> targetingCards = null;
        if (turn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE);
        } else if (turn == 2) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE);
        }
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token);
    }

    public static void destroyAllOfOpponentsMonsterCards(String token) {
        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
        ArrayList<CardLocation> targetingCards = null;
        if (turn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        } else if (turn == 2) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        }
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token);
    }

    public static void destroyAllMonsterCardsInTheField(String token) {
        ArrayList<CardLocation> targetingCards;
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token);
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, token);
    }

    public static void equipSpellEffect(String token, ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo, ArrayList<EquipSpellExtendedEffect> equipSpellExtendedEffect, Action uninterruptedAction) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<MonsterCard> monsterCards = new ArrayList<>();
        for (int i = 0; i < cardsToBeChosenToApplyEquipSpellTo.size(); i++) {
            monsterCards.add((MonsterCard) duelBoard.getCardByCardLocation(cardsToBeChosenToApplyEquipSpellTo.get(i)));
        }
        for (int i = 0; i < monsterCards.size(); i++) {
            for (int j = 0; j < equipSpellExtendedEffect.size(); j++) {
                monsterCards.get(i).addEquipSpellExtendedEffects(equipSpellExtendedEffect.get(j));
            }
        }
        CardLocation mainSpellCardLocation = uninterruptedAction.getFinalMainCardLocation();
        SpellCard mainSpellCard = (SpellCard) duelBoard.getCardByCardLocation(mainSpellCardLocation);
        for (int i = 0; i < cardsToBeChosenToApplyEquipSpellTo.size(); i++) {
            mainSpellCard.addCardLocationToEquipSpellAffected(cardsToBeChosenToApplyEquipSpellTo.get(0));
        }
    }

//    public static void checkSpellFieldEffectInTheField(int index, FieldSpellEffect fieldSpellEffect) {
//        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
//        ArrayList<Card> allyMonsterCards = duelBoard.getAllyMonsterCards();
//        ArrayList<Card> opponentMonsterCards = duelBoard.getOpponentMonsterCards();
//        for (int i = 0; i < allyMonsterCards.size(); i++) {
//            if (allyMonsterCards.get(i) != null) {
//                ((MonsterCard) allyMonsterCards.get(i)).addSpellFieldEffectToList(fieldSpellEffect);
//            }
//        }
//        for (int i = 0; i < opponentMonsterCards.size(); i++) {
//            if (opponentMonsterCards.get(i) != null) {
//                ((MonsterCard) opponentMonsterCards.get(i)).addSpellFieldEffectToList(fieldSpellEffect);
//            }
//        }
//    }


}
