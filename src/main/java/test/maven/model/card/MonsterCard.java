package test.maven.model.card;
import java.util.ArrayList;

import test.maven.model.CardPosition;
import test.maven.model.Attribute;


public class MonsterCard {
     
    protected String cardName;
    protected String description;
    protected CardPosition cardPosition;
    protected ArrayList<Effect> effects;
    protected int attackPower;
    protected int defensePower;
    protected int level;
    protected Attribute attribute;

    public int getAttackPower() {
        return attackPower;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getCardName() {
        return cardName;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public int getLevel() {
        return level;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setDefensePower(int defensePower) {
        this.defensePower = defensePower;
    }

    public void increaseDefensePower(int defensePower) {
        this.defensePower += defensePower;
    }

    public void increaseAttackPower(int attackPower) {
        this.defensePower += defensePower;
    }

    // public void addEffect(Effect effect) {

    // }
    
}