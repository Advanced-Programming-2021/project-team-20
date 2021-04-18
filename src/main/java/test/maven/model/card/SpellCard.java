package test.maven.model.card;

import java.util.ArrayList;

import test.maven.model.CardPosition;

public class SpellCard {
    protected String cardName;
   
    protected String description;
    protected CardPosition cardPosition;
    protected boolean cardActivated;
    protected int numberOfTurnsForActivation;
    protected ArrayList<Effect> effects;

    public String getCardName() {
        return cardName;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public void addEffect() {
       System.out.println();
    }

}