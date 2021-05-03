package test.maven.model.card.cardData.MonsterCardData;


import java.util.ArrayList;

import test.maven.model.card.cardData.General.Card;
import test.maven.model.card.cardData.General.CardPosition;
import test.maven.model.card.cardData.General.CardType;
import test.maven.model.card.cardEffects.MonsterEffectEnums.AttackerEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.BeingAttackedEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.FlipEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.OptionalMonsterEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import test.maven.model.card.cardEffects.MonsterEffectEnums.SummoningRequirement;
import test.maven.model.card.cardEffects.MonsterEffectEnums.UponSummoningEffect;

public class MonsterCard extends Card {
    private int attackPower;
    private int defensePower;
    private int level;
    private boolean cardPositionChanged;
    private boolean cardAttacked;
    private MonsterCardAttribute monsterCardAttribute;
    private MonsterCardFamily monsterCardFamily;
    private MonsterCardValue monsterCardValue;
    private ArrayList<SummoningRequirement> summoningRequirements;
    private ArrayList<UponSummoningEffect> uponSummoningEffects;
    private ArrayList<AttackerEffect> attackerEffects;
    private ArrayList<BeingAttackedEffect> beingAttackedEffects;
    private ArrayList<ContinuousMonsterEffect> continuousMonsterEffects;
    private ArrayList<FlipEffect> flipEffects;
    private ArrayList<OptionalMonsterEffect> optionalMonsterEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;

    public MonsterCard(int attackPower, int defensePower, int level, MonsterCardAttribute attribute, MonsterCardFamily monsterCardFamily, MonsterCardValue monsterCardValue, String cardName, String cardDescription, CardPosition cardPosition, int numberOfAllowedUsages, int cardPrice) {
        super(cardName, CardType.MONSTER, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.level = level;
        this.cardPositionChanged = false;
        this.cardAttacked = false;
        this.monsterCardAttribute = attribute;
        this.monsterCardFamily = monsterCardFamily;
        this.monsterCardValue = monsterCardValue;
        summoningRequirements = new ArrayList<>();
        uponSummoningEffects = new ArrayList<>();
        attackerEffects = new ArrayList<>();
        beingAttackedEffects = new ArrayList<>();
        continuousMonsterEffects = new ArrayList<>();
        flipEffects = new ArrayList<>();
        optionalMonsterEffects = new ArrayList<>();
        sentToGraveyardEffects = new ArrayList<>();
    }

    public MonsterCardValue getMonsterCardValue() {
        return monsterCardValue;
    }

    public MonsterCardFamily getMonsterCardFamily() {
        return monsterCardFamily;
    }

    public MonsterCardAttribute getMonsterCardAttribute() {
        return monsterCardAttribute;
    }

    public int getLevel() {
        return level;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public boolean isCardPositionChanged() {
        return cardPositionChanged;
    }

    public boolean isCardAttacked() {
        return cardAttacked;
    }

    public void setCardPositionChanged(boolean cardPositionChanged) {
        this.cardPositionChanged = cardPositionChanged;
    }

    public void setCardAttacked(boolean cardAttacked) {
        this.cardAttacked = cardAttacked;
    }

    public ArrayList<SummoningRequirement> getSummoningRequirements() {
        return summoningRequirements;
    }

    public ArrayList<UponSummoningEffect> getUponSummoningEffects() {
        return uponSummoningEffects;
    }

    public ArrayList<AttackerEffect> getAttackerEffects() {
        return attackerEffects;
    }

    public ArrayList<BeingAttackedEffect> getBeingAttackedEffects() {
        return beingAttackedEffects;
    }

    public ArrayList<ContinuousMonsterEffect> getContinuousMonsterEffects() {
        return continuousMonsterEffects;
    }

    public ArrayList<FlipEffect> getFlipEffects() {
        return flipEffects;
    }

    public ArrayList<OptionalMonsterEffect> getOptionalMonsterEffects() {
        return optionalMonsterEffects;
    }

    public ArrayList<SentToGraveyardEffect> getSentToGraveyardEffects() {
        return sentToGraveyardEffects;
    }

}
