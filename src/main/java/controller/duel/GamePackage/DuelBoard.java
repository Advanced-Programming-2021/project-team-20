package controller.duel.GamePackage;

import java.util.ArrayList;

import controller.duel.GamePackage.ActionConductors.SendCardToGraveyardConductor;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.*;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;

public class DuelBoard {
    ArrayList<Card> allyCardsInHand;
    ArrayList<Card> allySpellCards;
    ArrayList<Card> allySpellFieldCard;
    ArrayList<Card> allyMonsterCards;
    ArrayList<Card> allyCardsInDeck;
    ArrayList<Card> allyCardsInGraveyard;
    ArrayList<Card> opponentCardsInHand;
    ArrayList<Card> opponentSpellCards;
    ArrayList<Card> opponentSpellFieldCard;
    ArrayList<Card> opponentMonsterCards;
    ArrayList<Card> opponentCardsInDeck;
    ArrayList<Card> opponentCardsInGraveyard;
    ArrayList<CardLocation> changedCardPositions;

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
        changedCardPositions = new ArrayList<>();
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

    public CardType getCardTypeByCardLocation(CardLocation cardLocation) {
        Card card = getCardByCardLocation(cardLocation);
        if (card instanceof MonsterCard) {
            return CardType.MONSTER;
        } else if (card instanceof SpellCard) {
            return CardType.SPELL;
        } else if (card instanceof TrapCard) {
            return CardType.TRAP;
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
            return allySpellFieldCard != null;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return isArrayFull(opponentMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return isArrayFull(opponentSpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard != null;
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
            return allySpellFieldCard == null;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return isArrayEmpty(opponentMonsterCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return isArrayEmpty(opponentSpellCards, 5);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard == null;
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

    public CardLocation giveAvailableCardLocationForUse(RowOfCardLocation rowOfCardLocation, boolean isFirstPlayerChoosing) {
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
                    return new CardLocation(rowOfCardLocation, 2);
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
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            if (allyCardsInHand.get(i) != null) {
                newOpponentCardsInHand.add(allyCardsInHand.get(i));
            }
        }
        allyCardsInHand = newOpponentCardsInHand;
    }

    public Card setCardLocationToNull(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        Card card = arrayList.get(cardLocation.getIndex() - 1);
        arrayList.set(cardLocation.getIndex() - 1, null);
        return card;
    }

    public void destroyEquipSpellsRelatedToThisCard(CardLocation targetingCardLocation, int graveyardToSendCardTo) {
        //if change of heart is used and changes card locations, the corresponding arraylist in spell card should be updated too
        destroyEquipSpellsRelatedToThisCardInThisArrayList(allySpellCards, targetingCardLocation);
        destroyEquipSpellsRelatedToThisCardInThisArrayList(opponentSpellCards, targetingCardLocation);
    }

    private void destroyEquipSpellsRelatedToThisCardInThisArrayList(ArrayList<Card> spellCards, CardLocation targetingCardLocation) {
        for (int i = 0; i < spellCards.size(); i++) {
            SpellCard spellCard = (SpellCard) spellCards.get(i);
            ArrayList<CardLocation> equipSpellCardLocations = spellCard.getCardLocationsToWhichEquipSpellIsApplied();
            for (int j = 0; j < equipSpellCardLocations.size(); j++) {
                if (targetingCardLocation.getRowOfCardLocation().equals(equipSpellCardLocations.get(j).getRowOfCardLocation()) && targetingCardLocation.getIndex() == equipSpellCardLocations.get(j).getIndex()) {
                    SendCardToGraveyardConductor.sendCardToGraveyardAfterRemoving(equipSpellCardLocations.get(j), 0);
                }
            }
        }
    }

    public void refreshCharacteristicsOfACardSentToGraveyard(Card card){
        if (Card.isCardAMonster(card)){
            MonsterCard monsterCard = (MonsterCard) card;
            monsterCard.setOncePerTurnCardEffectUsed(false);
            monsterCard.setCardPositionChanged(false);
            monsterCard.setCardAttacked(false);
            monsterCard.clearEquipSpellEffect();
            monsterCard.clearFieldSpellEffect();
            monsterCard.setCardPosition(null);
        } else if (Card.isCardASpell(card)){
            SpellCard spellCard = (SpellCard) card;
            spellCard.setCardPosition(null);
            spellCard.clearCardsToWhichEquipSpellIsApplied();
            spellCard.setNumberOfTurnsForActivation(spellCard.getHighestNumberOfTurnsOfActivation());
        } else if (Card.isCardATrap(card)){
            TrapCard trapCard = (TrapCard) card;
            trapCard.setCardPosition(null);
            trapCard.setNumberOfTurnsForActivation(trapCard.getHighestNumberOfTurnsOfActivation());
        }
    }

    public Card removeCardByCardLocation(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        ArrayList<Card> arrayList = giveArrayListByRowOfCardLocation(rowOfCardLocation);
        Card card = arrayList.get(cardLocation.getIndex() - 1);
        if (rowOfCardLocation == RowOfCardLocation.ALLY_MONSTER_ZONE || rowOfCardLocation == RowOfCardLocation.OPPONENT_MONSTER_ZONE || rowOfCardLocation == RowOfCardLocation.ALLY_SPELL_ZONE || rowOfCardLocation == RowOfCardLocation.OPPONENT_SPELL_ZONE || rowOfCardLocation == RowOfCardLocation.ALLY_SPELL_FIELD_ZONE || rowOfCardLocation == RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE) {
            arrayList.set(cardLocation.getIndex() - 1, null);
        } else {
            arrayList.remove(cardLocation.getIndex() - 1);
        }
        return card;
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
        System.out.println(card.getCardName());
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
        for (int i = 0; i < 2; i++) {
            allyCardsInDeck.add(firstPlayerDeck.get(i + 5));
            opponentCardsInDeck.add(secondPlayerDeck.get(i + 5));
        }
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
                MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
                output += (monsterCard.getCardName() + " ");
            }
        }
        output += "\n";
        for (int i = 0; i < monsterCards.size(); i++) {
            if (monsterCards.get(i) == null) {
                output += "unoccupied ";
            } else {
                MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
                output += (monsterCard.getCardPosition().toString() + " ");
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
                    SpellCard spellCard = (SpellCard) spellCards.get(i);
                    output += (spellCard.getCardName() + " ");
                } else if (Card.isCardATrap(spellCards.get(i))) {
                    TrapCard trapCard = (TrapCard) spellCards.get(i);
                    output += (trapCard.getCardName() + " ");
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
            graveyard.append("graveyard empty");
        } else {
            for (int i = 0; i < cards.size(); i++) {
                graveyard.append(i).append(". ").append(cards.get(i).getCardName()).append(": ");
                graveyard.append(cards.get(i).getCardDescription()).append("\n");
            }
        }
        return graveyard.toString();
    }
}
