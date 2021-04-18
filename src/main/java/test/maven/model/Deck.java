package test.maven.model;

import java.util.ArrayList;

public class Deck {

    private ArrayList<String> mainDeck = new ArrayList<>();
    private ArrayList<String> sideDeck = new ArrayList<>();
    private String owner = new String();
    private String nameOfDeck = new String();
    private boolean isDeckActive;

    public String getOwner() {
        return owner;
    }

    public boolean getIsDeckActive() {
        return isDeckActive;
    }

    public void setDeckActive(boolean isDeckActive) {
        this.isDeckActive = isDeckActive;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    // public boolean addCardToMainDeck(Card card){

    // }
}