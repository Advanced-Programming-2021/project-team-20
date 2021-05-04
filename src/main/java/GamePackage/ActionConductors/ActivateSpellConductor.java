package GamePackage.ActionConductors;

import CardData.General.Card;
import CardData.General.CardLocation;
import CardData.General.CardPosition;
import CardData.General.RowOfCardLocation;
import CardData.MonsterCardData.MonsterCard;
import CardData.MonsterCardData.MonsterCardFamily;
import CardData.SpellCardData.SpellCard;
import CardEffects.SpellEffectEnums.EquipSpellEffect;
import CardEffects.SpellEffectEnums.FieldSpellEffect;
import CardEffects.SpellEffectEnums.NormalSpellCardEffect;
import CardEffects.SpellEffectEnums.QuickSpellEffect;
import GamePackage.Action;
import GamePackage.DuelBoard;
import PreliminaryPackage.GameManager;

import java.util.ArrayList;

public class ActivateSpellConductor {

    public static String conductActivatingSpellUninterruptedAction(int index, int numberInListOfActions) {
        Action uninterruptedAction = GameManager.getUninterruptedActionsByIndex(index).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation mainSpellCardLocation = uninterruptedAction.getMainCardLocation();
        Card mainSpellCard = duelBoard.getCardByCardLocation(mainSpellCardLocation);
        RowOfCardLocation rowOfMainSpellCardLocation = mainSpellCardLocation.getRowOfCardLocation();
        if (rowOfMainSpellCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE) || rowOfMainSpellCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            duelBoard.removeCardByCardLocation(mainSpellCardLocation);
            int actionTurn = uninterruptedAction.getActionTurn();
            duelBoard.addCardToSpellZone(mainSpellCard, actionTurn);
        }
        mainSpellCard.setCardPosition(CardPosition.FACE_UP_ACTIVATED_POSITION);
        return "spell card ready for activation";
    }

    public static String conductActivatingSpell(int index, int numberInListOfActions) {
        Action action = GameManager.getActionsByIndex(index).get(numberInListOfActions);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation mainCardLocation = action.getMainCardLocation();
        SpellCard spellCard = (SpellCard) duelBoard.getCardByCardLocation(mainCardLocation);
        return switchCaseAmongAllSpellEffects(action, index, spellCard);
    }

    public static String switchCaseAmongAllSpellEffects(Action action, int index, SpellCard spellCard) {
        ArrayList<NormalSpellCardEffect> normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
        ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
        ArrayList<FieldSpellEffect> fieldSpellEffects = spellCard.getFieldSpellEffects();
        ArrayList<EquipSpellEffect> equipSpellEffects = spellCard.getEquipSpellEffects();
        if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
            ArrayList<CardLocation> targetingCards = action.getTargetingCards();
            sendCardToGraveyardAfterRemoving(targetingCards.get(targetingCards.size() - 1), index, GameManager.getDuelControllerByIndex(index).getFakeTurn());
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
            ArrayList<CardLocation> cardsToBeSpecialSummoned = action.getCardsToBeSpecialSummoned();
            specialSummonCard(cardsToBeSpecialSummoned.get(cardsToBeSpecialSummoned.size() - 1), index);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.ADD_SPELL_FIELD_CARD_FROM_DECK_TO_HAND)) {
            ArrayList<CardLocation> cardsToBeChosenFromDeckAndAddedToHand = action.getCardsToBeChosenFromDeckAndAddedToHand();
            addCardToHand(cardsToBeChosenFromDeckAndAddedToHand.get(cardsToBeChosenFromDeckAndAddedToHand.size() - 1), index);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DRAW_2_CARDS)) {
            drawCard(index);
            drawCard(index);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS)) {
            destroyAllOfOpponentsSpellAndTrapCards(index);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS)) {
            destroyAllOfOpponentsMonsterCards(index);
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD)) {
            destroyAllMonsterCardsInTheField(index);
        }
        if (equipSpellEffects.size() > 0) {
            ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo = action.getCardsToBeChosenToApplyEquipSpellTo();
            equipSpellEffect(index, cardsToBeChosenToApplyEquipSpellTo, equipSpellEffects);
        }
        if (fieldSpellEffects.size() > 0) {
            for (int i = 0; i < fieldSpellEffects.size(); i++) {
                checkSpellFieldEffectInTheField(index, fieldSpellEffects.get(i));
            }
        }
        CardLocation mainCardLocation = action.getMainCardLocation();
        if (fieldSpellEffects.size() == 0 || equipSpellEffects.size() == 0) {
            //Action thisAction = actions.get(numberInListOfActions);
            int actionTurn = action.getActionTurn();
            sendCardToGraveyardAfterRemoving(mainCardLocation, index, actionTurn);
        }
        return "spell activated";
    }

    public static void sendCardToGraveyardAfterRemoving(CardLocation targetingCardLocation, int index, int graveyardToSendCardTo) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = removeCardAndGetRemovedCard(targetingCardLocation, index);
        duelBoard.addCardToGraveyard(card, graveyardToSendCardTo);
    }

    public static void specialSummonCard(CardLocation monsterCardLocation, int index) {
        int fakeTurn = GameManager.getDuelControllerByIndex(index).getFakeTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = removeCardAndGetRemovedCard(monsterCardLocation, index);
        duelBoard.addCardToMonsterZone(card, fakeTurn);
    }

    public static void addCardToHand(CardLocation cardLocation, int index) {
        int fakeTurn = GameManager.getDuelControllerByIndex(index).getFakeTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = removeCardAndGetRemovedCard(cardLocation, index);
        duelBoard.addCardToHand(card, fakeTurn);
    }

    public static Card removeCardAndGetRemovedCard(CardLocation cardToBeRemoved, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        Card card = duelBoard.getCardByCardLocation(cardToBeRemoved);
        duelBoard.removeCardByCardLocation(cardToBeRemoved);
        return card;
    }

    public static void drawCard(int index) {
        int fakeTurn = GameManager.getDuelControllerByIndex(index).getFakeTurn();
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        CardLocation cardLocation;
        if (fakeTurn == 1) {
            cardLocation = new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, 1);
        } else {
            cardLocation = new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, 1);
        }
        Card card = duelBoard.getCardByCardLocation(cardLocation);
        duelBoard.removeCardByCardLocation(cardLocation);
        duelBoard.addCardToHand(card, fakeTurn);
    }

    public static void sendAllCardsInThisArrayListToGraveyard(ArrayList<CardLocation> targetingCards, int index, int graveyardToSendCardsTo) {
        for (int i = 0; i < targetingCards.size(); i++) {
            sendCardToGraveyardAfterRemoving(targetingCards.get(i), index, graveyardToSendCardsTo);
        }
    }

    public static ArrayList<CardLocation> giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation rowOfCardLocation) {
        ArrayList<CardLocation> cardLocations = new ArrayList<>();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i));
            }
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            for (int i = 0; i < 5; i++) {
                cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i));
            }
            cardLocations.add(new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1));
        }
        return cardLocations;
    }

    public static void destroyAllOfOpponentsSpellAndTrapCards(int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        ArrayList<CardLocation> targetingCards;
        if (turn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE);
            sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 2);
        } else if (turn == 2) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE);
            sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 1);
        }
    }

    public static void destroyAllOfOpponentsMonsterCards(int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        ArrayList<CardLocation> targetingCards;
        if (turn == 1) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
            sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 2);
        } else if (turn == 2) {
            targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
            sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 1);
        }
    }

    public static void destroyAllMonsterCardsInTheField(int index) {
        ArrayList<CardLocation> targetingCards;
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 2);
        targetingCards = giveCardLocationArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
        sendAllCardsInThisArrayListToGraveyard(targetingCards, index, 1);
    }

    public static void equipSpellEffect(int index, ArrayList<CardLocation> cardsToBeChosenToApplyEquipSpellTo, ArrayList<EquipSpellEffect> equipSpellEffects) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<MonsterCard> monsterCards = new ArrayList<>();
        for (int i = 0; i < cardsToBeChosenToApplyEquipSpellTo.size(); i++) {
            monsterCards.add((MonsterCard) duelBoard.getCardByCardLocation(cardsToBeChosenToApplyEquipSpellTo.get(i)));
        }
        for (int i = 0; i < monsterCards.size(); i++) {
            for (int j = 0; j < equipSpellEffects.size(); j++) {
                monsterCards.get(i).addEquipSpellEffectToList(equipSpellEffects.get(j));
            }
        }
    }

    public static void checkSpellFieldEffectInTheField(int index, FieldSpellEffect fieldSpellEffect) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        ArrayList<Card> allyMonsterCards = duelBoard.getAllyMonsterCards();
        ArrayList<Card> opponentMonsterCards = duelBoard.getOpponentMonsterCards();
        for (int i = 0; i < allyMonsterCards.size(); i++) {
            if (allyMonsterCards.get(i) != null) {
                ((MonsterCard) allyMonsterCards.get(i)).addSpellFieldEffectToList(fieldSpellEffect);
            }
        }
        for (int i = 0; i < opponentMonsterCards.size(); i++) {
            if (opponentMonsterCards.get(i) != null) {
                ((MonsterCard) opponentMonsterCards.get(i)).addSpellFieldEffectToList(fieldSpellEffect);
            }
        }
    }


}
