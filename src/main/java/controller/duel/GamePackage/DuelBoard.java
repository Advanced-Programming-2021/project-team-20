package controller.duel.GamePackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import controller.duel.CardEffects.SpellEffectEnums.EquipSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.FieldSpellEffect;
import controller.duel.GamePackage.ActionConductors.SendCardToGraveyardConductor;
import controller.duel.GamePhaseControllers.SelectCardController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.*;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;

public class DuelBoard {
    private ArrayList<Card> allyCardsInHand;
    private ArrayList<Card> allySpellCards;
    private ArrayList<Card> allySpellFieldCard;
    private ArrayList<Card> allyMonsterCards;
    private ArrayList<Card> allyCardsInDeck;
    private ArrayList<Card> allyCardsInGraveyard;
    private ArrayList<Card> opponentCardsInHand;
    private ArrayList<Card> opponentSpellCards;
    private ArrayList<Card> opponentSpellFieldCard;
    private ArrayList<Card> opponentMonsterCards;
    private ArrayList<Card> opponentCardsInDeck;
    private ArrayList<Card> opponentCardsInGraveyard;
    private ArrayList<CardLocation> cardLocationsToBeTakenBackInEndPhase;

    public DuelBoard(ArrayList<Card> firstPlayerDeck, ArrayList<Card> secondPlayerDeck) {
        allyCardsInHand = new ArrayList<>();
        allySpellCards = new ArrayList<>();
        allySpellFieldCard = new ArrayList<>();
        allyMonsterCards = new ArrayList<>();
        allyCardsInDeck = new ArrayList<>();
        allyCardsInGraveyard = new ArrayList<>();
        opponentCardsInHand = new ArrayList<>();
        opponentSpellCards = new ArrayList<>();
        opponentSpellFieldCard = new ArrayList<>();
        opponentMonsterCards = new ArrayList<>();
        opponentCardsInDeck = new ArrayList<>();
        opponentCardsInGraveyard = new ArrayList<>();
        cardLocationsToBeTakenBackInEndPhase = new ArrayList<>();
        initializeCardsInDuelBoard(firstPlayerDeck, secondPlayerDeck);
    }

    public ArrayList<Card> getAllyCardsInHand() {
        return allyCardsInHand;
    }

    public ArrayList<Card> getAllySpellCards() {
        return allySpellCards;
    }

    public ArrayList<Card> getAllySpellFieldCard() {
        return allySpellFieldCard;
    }

    public ArrayList<Card> getAllyMonsterCards() {
        return allyMonsterCards;
    }

    public ArrayList<Card> getAllyCardsInDeck() {
        return allyCardsInDeck;
    }

    public ArrayList<Card> getAllyCardsInGraveyard() {
        return allyCardsInGraveyard;
    }

    public ArrayList<Card> getOpponentCardsInHand() {
        return opponentCardsInHand;
    }

    public ArrayList<Card> getOpponentSpellCards() {
        return opponentSpellCards;
    }

    public ArrayList<Card> getOpponentSpellFieldCard() {
        return opponentSpellFieldCard;
    }

    public ArrayList<Card> getOpponentMonsterCards() {
        return opponentMonsterCards;
    }

    public ArrayList<Card> getOpponentCardsInDeck() {
        return opponentCardsInDeck;
    }

    public ArrayList<Card> getOpponentCardsInGraveyard() {
        return opponentCardsInGraveyard;
    }

    public void setAllyCardsInDeck(ArrayList<Card> allyCardsInDeck) {
        this.allyCardsInDeck = allyCardsInDeck;
    }

    public void setOpponentCardsInDeck(ArrayList<Card> opponentCardsInDeck) {
        this.opponentCardsInDeck = opponentCardsInDeck;
    }

    public ArrayList<CardLocation> getCardLocationsToBeTakenBackInEndPhase() {
        return cardLocationsToBeTakenBackInEndPhase;
    }

    public void addCardLocationToCardLocationsToBeTakenBackInEndPhase(CardLocation cardLocation) {
        cardLocationsToBeTakenBackInEndPhase.add(cardLocation);
    }

    public void clearCardLocationsToBeTakenBackInEndPhase() {
        cardLocationsToBeTakenBackInEndPhase.clear();
    }

    public Card getCardByCardLocation(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        int index = cardLocation.getIndex();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return allyMonsterCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            return allySpellCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return allySpellFieldCard.get(0);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            return allyCardsInHand.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            return allyCardsInDeck.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            return allyCardsInGraveyard.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return opponentMonsterCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return opponentSpellCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard.get(0);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            return opponentCardsInHand.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
            return opponentCardsInDeck.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            return opponentCardsInGraveyard.get(index - 1);
        } else {
            return null;
        }
    }

    public boolean isArrayFull(ArrayList<Card> array, int size) {
        boolean isFull = true;
        for (int i = 0; i < size; i++) {
            if (array.get(i) == null) {
                isFull = false;
                break;
            }
        }
        return isFull;
    }

    public boolean isZoneFull(RowOfCardLocation rowOfCardLocation) {
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return isArrayFull(allyMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            return isArrayFull(allySpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            // System.out.println("this is spell field zone full ");
            // System.out.println(allySpellFieldCard!=null);
            return allySpellFieldCard.get(0) != null;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return isArrayFull(opponentMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return isArrayFull(opponentSpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard.get(0) != null;
        } else {
            return true;
        }
    }

    public boolean isArrayEmpty(ArrayList<Card> array, int size) {
        boolean isEmpty = true;
        for (int i = 0; i < size; i++) {
            if (array.get(i) != null) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public boolean isZoneEmpty(RowOfCardLocation rowOfCardLocation) {
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return isArrayEmpty(allyMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            return isArrayEmpty(allySpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return allySpellFieldCard.get(0) == null;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return isArrayEmpty(opponentMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return isArrayEmpty(opponentSpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard.get(0) == null;
        } else {
            return true;
        }
    }

    public boolean isDeckEmpty(int turn) {
        if (turn == 1) {
            return allyCardsInDeck.size() == 0;
        } else if (turn == 2) {
            return opponentCardsInDeck.size() == 0;
        }
        return false;
    }

    public ArrayList<Card> giveArrayListByRowOfCardLocation(RowOfCardLocation rowOfCardLocation) {
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return allyMonsterCards;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            return allySpellCards;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return allySpellFieldCard;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            return allyCardsInHand;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            return allyCardsInDeck;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            return allyCardsInGraveyard;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return opponentMonsterCards;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return opponentSpellCards;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            return opponentCardsInHand;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
            return opponentCardsInDeck;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            return opponentCardsInGraveyard;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard;
        } else {
            return null;
        }
    }

    public CardLocation giveAvailableCardLocationForUse(RowOfCardLocation rowOfCardLocation,
                                                        boolean isFirstPlayerChoosing) {
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        if (arrayList.size() == 5) {
            if (isFirstPlayerChoosing) {
                if (arrayList.get(2) == null) {
                    return new CardLocation(rowOfCardLocation, 2);
                } else if (arrayList.get(3) == null) {
                    return new CardLocation(rowOfCardLocation, 3);
                } else if (arrayList.get(1) == null) {
                    return new CardLocation(rowOfCardLocation, 1);
                } else if (arrayList.get(4) == null) {
                    return new CardLocation(rowOfCardLocation, 4);
                } else if (arrayList.get(0) == null) {
                    return new CardLocation(rowOfCardLocation, 0);
                }
            } else {
                if (arrayList.get(2) == null) {
                    return new CardLocation(rowOfCardLocation, 2);
                } else if (arrayList.get(1) == null) {
                    return new CardLocation(rowOfCardLocation, 1);
                } else if (arrayList.get(3) == null) {
                    return new CardLocation(rowOfCardLocation, 3);
                } else if (arrayList.get(0) == null) {
                    return new CardLocation(rowOfCardLocation, 0);
                } else if (arrayList.get(4) == null) {
                    return new CardLocation(rowOfCardLocation, 4);
                }
            }
        } else if (arrayList.size() == 1) {
            if (arrayList.get(0) == null) {
                return new CardLocation(rowOfCardLocation, 0);
            }
        }
        return null;
    }

    public void removeNullCardsFromHands() {
        ArrayList<Card> newAllyCardsInHand = new ArrayList<>();
        ArrayList<Card> newOpponentCardsInHand = new ArrayList<>();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            if (allyCardsInHand.get(i) != null) {
                newAllyCardsInHand.add(allyCardsInHand.get(i));
            }
        }
        allyCardsInHand = newAllyCardsInHand;
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            if (opponentCardsInHand.get(i) != null) {
                newOpponentCardsInHand.add(opponentCardsInHand.get(i));
            }
        }
        opponentCardsInHand = newOpponentCardsInHand;
    }

    public Card setCardLocationToNull(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        Card card = arrayList.get(cardLocation.getIndex() - 1);
        arrayList.set(cardLocation.getIndex() - 1, null);
        return card;
    }

    public void removeFieldSpellEffectsOnCardsWhenSpellFieldIsDestroyed(CardLocation spellCardLocation) {
        SpellCard spellCard = (SpellCard) getCardByCardLocation(spellCardLocation);
        ArrayList<FieldSpellEffect> fieldSpellEffects = spellCard.getFieldSpellEffects();
        Card card;
        for (int i = 0; i < 5; i++) {
            card = getAllyMonsterCards().get(i);
            removeFieldSpellEffectFromCard(fieldSpellEffects, card);
            card = getOpponentMonsterCards().get(i);
            removeFieldSpellEffectFromCard(fieldSpellEffects, card);
        }
    }

    private void removeFieldSpellEffectFromCard(ArrayList<FieldSpellEffect> fieldSpellEffects, Card card) {
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            for (int j = 0; j < fieldSpellEffects.size(); j++) {
                monsterCard.removeSpellFieldEffectFromList(fieldSpellEffects.get(j));
            }
        }
    }

    public void removeEquipSpellEffectsOnCardsWhenEquipSpellIsDestroyed(CardLocation spellCardLocation) {
        SpellCard spellCard = (SpellCard) getCardByCardLocation(spellCardLocation);
        ArrayList<CardLocation> cardsToWhichEquipSpellEffectIsApplied = spellCard
            .getCardLocationsToWhichEquipSpellIsApplied();
        ArrayList<EquipSpellEffect> equipSpellEffects = spellCard.getEquipSpellEffects();
        if (cardsToWhichEquipSpellEffectIsApplied != null) {
            for (int i = 0; i < cardsToWhichEquipSpellEffectIsApplied.size(); i++) {
                Card card = getCardByCardLocation(cardsToWhichEquipSpellEffectIsApplied.get(i));
                if (Card.isCardAMonster(card)) {
                    MonsterCard monsterCard = (MonsterCard) card;
                    for (int j = 0; j < equipSpellEffects.size(); j++) {
                        monsterCard.removeEquipSpellEffectFromList(equipSpellEffects.get(j));
                    }
                }
            }
        }
    }

    public void destroyEquipSpellsRelatedToThisCard(CardLocation targetingCardLocation, int graveyardToSendCardTo) {
        // if change of heart is used and changes card locations, the corresponding
        // arraylist in spell card should be updated too
        destroyEquipSpellsRelatedToThisCardInThisArrayList(allySpellCards, targetingCardLocation);
        destroyEquipSpellsRelatedToThisCardInThisArrayList(opponentSpellCards, targetingCardLocation);
    }

    private void destroyEquipSpellsRelatedToThisCardInThisArrayList(ArrayList<Card> spellCards, CardLocation targetingCardLocation) {
        for (int i = 0; i < spellCards.size(); i++) {
            if (Card.isCardASpell(spellCards.get(i))) {
                SpellCard spellCard = (SpellCard) spellCards.get(i);
                ArrayList<CardLocation> equipSpellCardLocations = spellCard
                    .getCardLocationsToWhichEquipSpellIsApplied();
                if (equipSpellCardLocations != null) {
                    for (int j = 0; j < equipSpellCardLocations.size(); j++) {
                        if (targetingCardLocation.getRowOfCardLocation()
                            .equals(equipSpellCardLocations.get(j).getRowOfCardLocation())
                            && targetingCardLocation.getIndex() == equipSpellCardLocations.get(j).getIndex()) {
                            SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(equipSpellCardLocations.get(j),
                                0);
                        }
                    }
                }
            }
        }
    }

    public void refreshCharacteristicsOfACardSentToGraveyard(Card card) {
        if (Card.isCardAMonster(card)) {
            MonsterCard monsterCard = (MonsterCard) card;
            monsterCard.setOncePerTurnCardEffectUsed(false);
            monsterCard.setCardPositionChanged(false);
            monsterCard.setHasCardAlreadyAttacked(false);
            monsterCard.clearEquipSpellEffect();
            monsterCard.clearFieldSpellEffect();
            monsterCard.setCardPosition(CardPosition.NOT_APPLICABLE);
        } else if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            spellCard.setCardPosition(CardPosition.NOT_APPLICABLE);
            spellCard.clearCardsToWhichEquipSpellIsApplied();
            spellCard.setNumberOfTurnsForActivation(spellCard.getHighestNumberOfTurnsOfActivation());
        } else if (Card.isCardATrap(card)) {
            TrapCard trapCard = (TrapCard) card;
            trapCard.setCardPosition(CardPosition.NOT_APPLICABLE);
            trapCard.setNumberOfTurnsForActivation(trapCard.getHighestNumberOfTurnsOfActivation());
        }
    }

    public Card removeCardByCardLocation(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        Card card = arrayList.get(cardLocation.getIndex() - 1);
        if (rowOfCardLocation == RowOfCardLocation.ALLY_MONSTER_ZONE
            || rowOfCardLocation == RowOfCardLocation.OPPONENT_MONSTER_ZONE
            || rowOfCardLocation == RowOfCardLocation.ALLY_SPELL_ZONE
            || rowOfCardLocation == RowOfCardLocation.OPPONENT_SPELL_ZONE
            || rowOfCardLocation == RowOfCardLocation.ALLY_SPELL_FIELD_ZONE
            || rowOfCardLocation == RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE) {
            arrayList.set(cardLocation.getIndex() - 1, null);
        } else {
            arrayList.remove(cardLocation.getIndex() - 1);
        }
        return card;
    }

    public void sendCardsFromSensitiveArrayListToGraveyard(ArrayList<CardLocation> cardLocations) {
        // System.out.println("LLL " + cardLocations);
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardLocations.size(); i++) {
            cards.add(getCardByCardLocation(cardLocations.get(i)));
        }
        RowOfCardLocation rowOfCardLocation = cardLocations.get(0).getRowOfCardLocation();
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        for (int i = 0; i < cardLocations.size(); i++) {
            arrayList.set(cardLocations.get(i).getIndex() - 1, null);
        }
        for (int i = 0; i < cards.size(); i++) {
            if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)
                || rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
                // System.out.println("BEFORE "+allyCardsInGraveyard);
                allyCardsInGraveyard.add(cards.get(i));
                // System.out.println("AFTER "+allyCardsInGraveyard);
            } else {
                opponentCardsInGraveyard.add(cards.get(i));
            }
        }
        ArrayList<Card> newArrayList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null) {
                newArrayList.add(arrayList.get(i));
            }
        }
        arrayList = newArrayList;
    }

    public void addCardToHand(Card card, int turn) {
        ArrayList<Card> arrayList;
        if (turn == 1) {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_HAND_ZONE);
        } else {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE);
        }
        arrayList.add(card);
    }

    public void addCardToGraveyard(Card card, int turn) {
        ArrayList<Card> arrayList;
        if (turn == 1) {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
        } else {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
        }
        arrayList.add(card);
    }

    public void addCardToMonsterZone(Card card, int turn) {
        ArrayList<Card> arrayList;
        CardLocation cardLocation;
        // System.out.println(card.getCardName());
        if (turn == 1) {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE);
            cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true);
        } else {
            arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
            cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_MONSTER_ZONE, false);
        }
        if (cardLocation != null) {
            arrayList.set(cardLocation.getIndex(), card);
        }
    }

    public void addCardToSpellZone(Card card, int turn) {
        ArrayList<Card> arrayList;
        CardLocation cardLocation;
        if (turn == 1) {
            if (card instanceof SpellCard && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE);
                cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, true);
            } else {
                arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE);
                cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_SPELL_ZONE, true);
            }
        } else {
            if (card instanceof SpellCard && ((SpellCard) card).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE);
                cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, false);
            } else {
                arrayList = giveArrayListByRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE);
                cardLocation = giveAvailableCardLocationForUse(RowOfCardLocation.OPPONENT_SPELL_ZONE, false);
            }
        }
        if (cardLocation != null) {
            arrayList.set(cardLocation.getIndex(), card);
        }
    }

    public boolean isCardInHand(Card card, int turn) {
        if (turn == 1) {
            for (int i = 0; i < allyCardsInHand.size(); i++) {
                if (card.equals(allyCardsInHand.get(i))) {
                    return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < opponentCardsInHand.size(); i++) {
                if (card.equals(opponentCardsInHand.get(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isCardInMonsterZone(MonsterCard monsterCard, int turn) {
        if (turn == 1) {
            for (int i = 0; i < allyMonsterCards.size(); i++) {
                if (monsterCard.equals(allyMonsterCards.get(i))) {
                    return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < opponentMonsterCards.size(); i++) {
                if (monsterCard.equals(opponentMonsterCards.get(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public void initializeCardsInDuelBoard(ArrayList<Card> firstPlayerDeck, ArrayList<Card> secondPlayerDeck) {
        for (int i = 0; i < 5; i++) {
            allyMonsterCards.add(null);
            allySpellCards.add(null);
            opponentMonsterCards.add(null);
            opponentSpellCards.add(null);
            allyCardsInHand.add(firstPlayerDeck.get(i));
            opponentCardsInHand.add(secondPlayerDeck.get(i));
        }
        allySpellFieldCard.add(null);
        opponentSpellFieldCard.add(null);
        ArrayList<Card> freshFirstCards = new ArrayList<>();
        freshFirstCards.addAll(firstPlayerDeck);

        ArrayList<Card> freshSecondCards = new ArrayList<>();
        freshSecondCards.addAll(secondPlayerDeck);
        allyCardsInDeck.clear();
        opponentCardsInDeck.clear();
        for (int i = 0; i < freshFirstCards.size() - 5; i++) {
            allyCardsInDeck.add(freshFirstCards.get(i + 5));
        }
        for (int i = 0; i < freshSecondCards.size() - 5; i++) {
            opponentCardsInDeck.add(freshSecondCards.get(i + 5));
        }
    }

    public void shuffleMainDecks() {
        Collections.shuffle(GameManager.getChangeCardsBetweenTwoRoundsByIndex(0).getAllyPlayerDeck().getMainDeck());
        Collections.shuffle(GameManager.getChangeCardsBetweenTwoRoundsByIndex(0).getOpponentPlayerDeck().getMainDeck());
    }

    public void resetCards(int player1Or2) {

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();

        ArrayList<Card> allCardsInDeck = null;
        List<String> allCardsInMainDeck = null;
        if (player1Or2 == 1) {
            allCardsInDeck = allyCardsInDeck;
            allCardsInMainDeck = GameManager.getChangeCardsBetweenTwoRoundsByIndex(0).getAllyPlayerDeck().getMainDeck();
        }
        if (player1Or2 == 2) {
            allCardsInDeck = opponentCardsInDeck;
            allCardsInMainDeck = GameManager.getChangeCardsBetweenTwoRoundsByIndex(0).getOpponentPlayerDeck().getMainDeck();
        }

        allCardsInDeck.clear();

        for (int i = 0; i < allCardsInMainDeck.size(); i++) {
            if (allMonsterCards.containsKey(allCardsInMainDeck.get(i))) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(allCardsInMainDeck.get(i));
                allCardsInDeck.add((MonsterCard) monsterCard.clone());
            } else if (allSpellAndTrapCards.containsKey(allCardsInMainDeck.get(i))) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(allCardsInMainDeck.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(allCardsInMainDeck.get(i));
                    allCardsInDeck.add((SpellCard) spellCard.clone());
                } else {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(allCardsInMainDeck.get(i));
                    allCardsInDeck.add((TrapCard) trapCard.clone());
                }
            }
        }
    }

    public String showMainDuelBoard(int index) {

        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        User myTurnUser = Storage.getUserByName(duelController.getPlayingUsernameByTurn(turn));
        User otherTurnUser = Storage.getUserByName(duelController.getPlayingUsernameByTurn(-turn + 3));
        StringBuilder builderDuelBoard = new StringBuilder();

        builderDuelBoard.append(
            "\t\t" + otherTurnUser.getNickname() + " : " + duelController.getLifePoints().get(-turn + 2) + "\n");
        builderDuelBoard.append(toShowInDuelBoardFormatCardsInHand(-turn + 3).reverse().toString() + "\n");
        builderDuelBoard.append(reverseWords(toShowInDuelBoardFormatCardsInDeck(-turn + 3).toString()) + "\n");
        builderDuelBoard.append((toShowInDuelBoardFormatSpellAndTrapCards(-turn + 3)) + "\n");
        builderDuelBoard.append((toShowInDuelBoardFormatMonsterCards(-turn + 3) + "\n"));
        builderDuelBoard
            .append(reverseWords(toShowInDuelBoardFormatGraveyardAndFieldZone(-turn + 3).toString()) + "\n");

        builderDuelBoard.append("---------------------------------------------------\n");

        builderDuelBoard.append(toShowInDuelBoardFormatGraveyardAndFieldZone(turn).toString() + "\n");
        builderDuelBoard.append(toShowInDuelBoardFormatMonsterCards(turn) + "\n");
        builderDuelBoard.append(toShowInDuelBoardFormatSpellAndTrapCards(turn) + "\n");
        builderDuelBoard.append(toShowInDuelBoardFormatCardsInDeck(turn).toString() + "\n");
        builderDuelBoard.append(toShowInDuelBoardFormatCardsInHand(turn).toString() + "\n");
        builderDuelBoard
            .append("\t\t" + myTurnUser.getNickname() + " : " + duelController.getLifePoints().get(turn - 1));

        return builderDuelBoard.toString();
    }

    private String reverseWords(String input) {
        // System.out.println(input + " input");
        String[] temp = input.split(" ");
        String result = "";

        for (int i = 0; i < temp.length; i++) {
            if (i == temp.length - 1)
                result = temp[i] + result;
            else
                result = " " + temp[i] + result;
        }
        // System.out.println(result + " result");
        return result;
    }

    public String reverseWordsWhenTurnIs2(String input) {
        // System.out.println(input + " input");
        String words[] = input.split("\\s");
        String reverseWord = "";
        for (String w : words) {
            StringBuilder sb = new StringBuilder(w);
            sb.reverse();
            reverseWord += sb.toString() + "\t";
        }
        return reverseWord;
    }

    private StringBuilder toShowInDuelBoardFormatGraveyardAndFieldZone(int whichPlayer) {
        StringBuilder showGraveyardAndFieldZoneSize = new StringBuilder();
        if (whichPlayer == 2) {
            showGraveyardAndFieldZoneSize.append(opponentCardsInGraveyard.size() + "  \t  \t  \t  \t  \t  \t  "
                + ((opponentSpellFieldCard.isEmpty()) ? "O" : "E"));
        } else {
            showGraveyardAndFieldZoneSize.append(allyCardsInGraveyard.size() + "  \t  \t  \t  \t  \t  \t  "
                + ((allySpellFieldCard.isEmpty()) ? "O" : "E"));
        }

        return showGraveyardAndFieldZoneSize;
    }

    private String toShowInDuelBoardFormatMonsterCards(int whichPlayer) {
        StringBuilder showMonsterCards = new StringBuilder();
        showMonsterCards.append("\t");
        if (whichPlayer == 2) {
            for (int i = 0; i < 5; i++) {
                showMonsterCards.append(howPlacedInZone(opponentMonsterCards.get(i)) + "\t");
            }
        } else if (whichPlayer == 1) {
            for (int i = 0; i < 5; i++) {
                showMonsterCards.append(howPlacedInZone(allyMonsterCards.get(i)) + "\t");
            }
        }
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        if (turn == 2)
            return reverseWordsWhenTurnIs2(showMonsterCards.reverse().toString());
        return showMonsterCards.toString();
    }

    private String toShowInDuelBoardFormatSpellAndTrapCards(int whichPlayer) {
        StringBuilder showSpellAndTrapCards = new StringBuilder();
        showSpellAndTrapCards.append("\t");
        if (whichPlayer == 2) {
            for (int i = 0; i < 5; i++) {
                showSpellAndTrapCards.append(howPlacedInZone(opponentSpellCards.get(i)) + "\t");
            }
        } else if (whichPlayer == 1) {
            for (int i = 0; i < 5; i++) {
                showSpellAndTrapCards.append(howPlacedInZone(allySpellCards.get(i)) + "\t");
            }
        }

        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        if (turn == 2)
            return reverseWordsWhenTurnIs2(showSpellAndTrapCards.reverse().toString());
        return showSpellAndTrapCards.toString();
    }

    private String howPlacedInZone(Card card) {

        if (card == null) {
            return "E";
        }
        if (card.getCardPosition().equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
            return "O";
        }
        if (card.getCardPosition().equals(CardPosition.FACE_DOWN_SPELL_SET_POSITION)) {
            return "H";
        }
        if (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
            return "OO";
        }
        if (card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
            return "DO";
        }
        if (card.getCardPosition().equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
            return "DH";
        }
        // NOT_APPLICABLE
        return "E";

    }

    private StringBuilder toShowInDuelBoardFormatCardsInDeck(int whichPlayer) {
        StringBuilder showCardsInDeckBuilder = new StringBuilder();
        if (whichPlayer == 2) {
            showCardsInDeckBuilder.append("  \t  \t  \t  \t  \t  \t  " + opponentCardsInDeck.size());
        } else if (whichPlayer == 1) {
            showCardsInDeckBuilder.append("  \t  \t  \t  \t  \t  \t  " + allyCardsInDeck.size());
        }
        return showCardsInDeckBuilder;
    }

    private StringBuilder toShowInDuelBoardFormatCardsInHand(int whichPlayer) {
        StringBuilder showCardsInHandBuilder = new StringBuilder();
        if (whichPlayer == 2) {
            for (int i = 0; i < opponentCardsInHand.size(); i++) {
                showCardsInHandBuilder.append("c\t");
            }
        } else if (whichPlayer == 1) {
            for (int i = 0; i < allyCardsInHand.size(); i++) {
                showCardsInHandBuilder.append("c\t");
            }
        }
        return showCardsInHandBuilder;
    }

    public String showSelectedCard(int index, int turn) {

        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(index);
        ArrayList<CardLocation> selectedCardLocations = selectCardController.getSelectedCardLocations();
        CardLocation cardLocation = selectedCardLocations.get(selectedCardLocations.size() - 1);

        if (cardLocation == null) {
            return "no card is selected yet";
        }
        Card selectedCard = getCardByCardLocation(cardLocation);
        if (selectedCard == null) {
            return "there is no card here";
        }
        if (turn == 1) {
            if (cardLocation.getRowOfCardLocation().toString().startsWith("OPPONENT") && isCardHidden(selectedCard)) {
                return "card is not visible";
            }
        } else if (turn == 2) {
            if (cardLocation.getRowOfCardLocation().toString().startsWith("ALLY") && isCardHidden(selectedCard)) {
                return "card is not visible";
            }
        }
        return showCard(selectedCard.getCardName());
    }

    private String showCard(String cardName) {

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();

        StringBuilder shownCardStringBuilder = new StringBuilder();
        shownCardStringBuilder.append("Name: " + cardName + "\n");
        if (allMonsterCards.containsKey(cardName)) {
            MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(cardName);
            shownCardStringBuilder.append("Level: " + monsterCard.getLevel() + "\n");
            shownCardStringBuilder.append("Type: " + monsterCard.getMonsterCardFamily() + "\n");
            shownCardStringBuilder.append("ATK: " + monsterCard.getAttackPower() + "\n");
            shownCardStringBuilder.append("DEF: " + monsterCard.getDefensePower() + "\n");
            shownCardStringBuilder.append("Description: " + monsterCard.getCardDescription());
        } else {
            if (allSpellAndTrapCards.get(cardName).getCardType().equals(CardType.SPELL)) {
                SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardName);
                shownCardStringBuilder.append("Spell\n");
                shownCardStringBuilder.append("Type: " + spellCard.getSpellCardValue() + "\n");
                shownCardStringBuilder.append("Description: " + spellCard.getCardDescription());
            } else {
                TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardName);
                shownCardStringBuilder.append("Trap\n");
                shownCardStringBuilder.append("Type: " + trapCard.getTrapCardValue() + "\n");
                shownCardStringBuilder.append("Description: " + trapCard.getCardDescription());
            }
        }

        return shownCardStringBuilder.toString();
    }


    private boolean isCardHidden(Card card) {
        String cardPositionToString = card.getCardPosition().toString();
        if (cardPositionToString.equals("FACE_DOWN_SPELL_SET_POSITION")
            || cardPositionToString.equals("FACE_DOWN_MONSTER_SET_POSITION")) {
            return true;
        }
        return false;
    }

    public String showDuelBoard(int index) {
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        int turn = duelController.getTurn();
        String output = "";
        if (turn == 1) {
            output += printFirstPlayerData(0);
            output += printSecondPlayerData(0);
        } else {
            output += printSecondPlayerData(0);
            output += printFirstPlayerData(0);
        }
        return output;
    }

    private String printFirstPlayerData(int index) {
        String output = "";
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        output += duelController.getPlayingUsers().get(0) + ":" + duelController.getLifePoints().get(0) + "\n";
        output += "ally cards in hand\n";
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            output += allyCardsInHand.get(i).getCardName() + "   ";
        }
        output += "\nally cards in spell zone\n";
        output = printSpellCards(output, allySpellCards);
        if (allySpellFieldCard.get(0) == null) {
            output += "       unoccupied";
        } else {
            output += ("      " + allySpellFieldCard.get(0).getCardName());
        }
        output += "\nally cards in monster zone\n";
        output = printMonsterCards(output, allyMonsterCards);
        output += "\n\n";
        return output;
    }

    private String printSecondPlayerData(int index) {
        String output = "";
        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        output += duelController.getPlayingUsers().get(1) + ":" + duelController.getLifePoints().get(1) + "\n";
        output += "opponent cards in hand\n";
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            output += opponentCardsInHand.get(i).getCardName() + "   ";
        }
        output += "\nopponent cards in spell zone\n";
        output = printSpellCards(output, opponentSpellCards);
        if (opponentSpellFieldCard.get(0) == null) {
            output += "       unoccupied";
        } else {
            output += ("      " + opponentSpellFieldCard.get(0).getCardName());
        }
        output += "\nopponent cards in monster zone\n";
        output = printMonsterCards(output, opponentMonsterCards);
        output += "\n\n";
        return output;
    }

    private String printMonsterCards(String output, ArrayList<Card> monsterCards) {
        for (int i = 0; i < monsterCards.size(); i++) {
            if (monsterCards.get(i) == null) {
                output += "unoccupied ";
            } else {
                // MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
                output += (monsterCards.get(i).getCardName() + " ");
            }
        }
        output += "\n";
        for (int i = 0; i < monsterCards.size(); i++) {
            if (monsterCards.get(i) == null) {
                output += "unoccupied ";
            } else {
                // MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
                output += (monsterCards.get(i).getCardPosition().toString() + " ");
            }
        }
        return output;
    }

    private String printSpellCards(String output, ArrayList<Card> spellCards) {
        for (int i = 0; i < spellCards.size(); i++) {
            if (spellCards.get(i) == null) {
                output += "unoccupied ";
            } else {
                if (Card.isCardASpell(spellCards.get(i))) {
                    // SpellCard spellCard = (SpellCard) spellCards.get(i);
                    output += (spellCards.get(i).getCardName() + " ");
                } else if (Card.isCardATrap(spellCards.get(i))) {
                    // TrapCard trapCard = (TrapCard) spellCards.get(i);
                    output += (spellCards.get(i).getCardName() + " ");
                }
            }
        }
        return output;
    }

    public String showGraveyard() {
        StringBuilder output = new StringBuilder();
        output.append("first player graveyard:\n");
        output.append(showGraveyardInList(allyCardsInGraveyard));
        output.append("second player graveyard:\n");
        output.append(showGraveyardInList(opponentCardsInGraveyard));
        return output.toString();
    }

    public String showGraveyardInList(ArrayList<Card> cards) {
        StringBuilder graveyard = new StringBuilder();
        if (cards.size() == 0) {
            graveyard.append("graveyard empty\n");
        } else {
            for (int i = 0; i < cards.size(); i++) {
                graveyard.append(i).append(". ").append(cards.get(i).getCardName()).append(": ");
                graveyard.append(cards.get(i).getCardDescription()).append("\n");
            }
        }
        return graveyard.toString();
    }


    public void clearAllVariablesOfThisClass() {
        allyCardsInHand.clear();
        allySpellCards.clear();
        allySpellFieldCard.clear();
        allyMonsterCards.clear();
        allyCardsInDeck.clear();
        allyCardsInGraveyard.clear();
        opponentCardsInHand.clear();
        opponentSpellCards.clear();
        opponentSpellFieldCard.clear();
        opponentMonsterCards.clear();
        opponentCardsInDeck.clear();
        opponentCardsInGraveyard.clear();
        cardLocationsToBeTakenBackInEndPhase.clear();
    }

}
