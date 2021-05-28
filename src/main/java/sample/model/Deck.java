package sample.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<String> mainDeck = new ArrayList<>();
    private List<String> sideDeck = new ArrayList<>();
    private String deckname = new String();
    private boolean isDeckActive;

    public Deck(String deckname) {
        this.deckname = deckname;
        isDeckActive = false;
    }

    public boolean getIsDeckActive() {
        return isDeckActive;
    }

    public String getDeckname() {
        return deckname;
    }

    public List<String> getMainDeck() {
        return mainDeck;
    }

    public List<String> getSideDeck() {
        return sideDeck;
    }

    public void setDeckActive(boolean isDeckActive) {
        this.isDeckActive = isDeckActive;
    }

    public int numberOfCardsInDeck(String cardname) {
        return Collections.frequency(mainDeck, cardname) + Collections.frequency(sideDeck, cardname);
    }

    public int getSizeOfMainDeck() {
        return mainDeck.size();
    }

    public int getSizeOfSideDeck() {
        return sideDeck.size();
    }

    public void addCardToMainDeck(String cardname) {
        mainDeck.add(cardname);
    }

    public void deleteCardFromMainDeck(String cardname) {
        mainDeck.remove(cardname);
    }

    public void addCardToSideDeck(String cardname) {
        sideDeck.add(cardname);
    }

    public void deleteCardFromSideDeck(String cardname) {
        sideDeck.remove(cardname);
    }
    
}