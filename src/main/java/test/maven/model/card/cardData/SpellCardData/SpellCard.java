package test.maven.model.card.cardData.SpellCardData;

import test.maven.model.card.cardData.General.Card;
import test.maven.model.card.cardData.General.CardPosition;
import test.maven.model.card.cardData.General.CardType;
import test.maven.model.card.cardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.EquipSpellEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.FieldSpellEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.LogicalActivationRequirement;
import test.maven.model.card.cardEffects.SpellEffectEnums.NormalSpellCardEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.QuickSpellEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.RitualSpellEffect;
import test.maven.model.card.cardEffects.SpellEffectEnums.UserReplyForActivation;

import java.util.ArrayList;

public class SpellCard extends Card {
    private boolean isCardActivated;
    private int numberOfTurnsForActivation;
    private SpellCardValue spellCardValue;
    private boolean isAlreadyActivated;
    private ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects;
    private ArrayList<EquipSpellEffect> equipSpellEffects;
    private ArrayList<FieldSpellEffect> fieldSpellEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;
    private ArrayList<NormalSpellCardEffect> normalSpellCardEffects;
    private ArrayList<QuickSpellEffect> quickSpellEffects;
    private ArrayList<RitualSpellEffect> ritualSpellEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;
    private ArrayList<UserReplyForActivation> userReplyForActivations;

    public SpellCard(String cardName, String cardDescription, SpellCardValue spellCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.spellCardValue = spellCardValue;
    }

    public boolean isCardActivated() {
        return isCardActivated;
    }

    public int getNumberOfTurnsForActivation() {
        return numberOfTurnsForActivation;
    }

    public SpellCardValue getSpellCardValue() {
        return spellCardValue;
    }

    public boolean isAlreadyActivated() {
        return isAlreadyActivated;
    }

    public ArrayList<ContinuousSpellCardEffect> getContinuousSpellCardEffects() {
        return continuousSpellCardEffects;
    }

    public ArrayList<EquipSpellEffect> getEquipSpellEffects() {
        return equipSpellEffects;
    }

    public ArrayList<FieldSpellEffect> getFieldSpellEffects() {
        return fieldSpellEffects;
    }

    public ArrayList<LogicalActivationRequirement> getLogicalActivationRequirements() {
        return logicalActivationRequirements;
    }

    public ArrayList<NormalSpellCardEffect> getNormalSpellCardEffects() {
        return normalSpellCardEffects;
    }

    public ArrayList<QuickSpellEffect> getQuickSpellEffects() {
        return quickSpellEffects;
    }

    public ArrayList<RitualSpellEffect> getRitualSpellEffects() {
        return ritualSpellEffects;
    }

    public ArrayList<SentToGraveyardEffect> getSentToGraveyardEffects() {
        return sentToGraveyardEffects;
    }

    public ArrayList<UserReplyForActivation> getUserReplyForActivations() {
        return userReplyForActivations;
    }

    public void setAlreadyActivated(boolean alreadyActivated) {
        isAlreadyActivated = alreadyActivated;
    }
}
