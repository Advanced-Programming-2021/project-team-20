package model.cardData.SpellCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.duel.CardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import controller.duel.CardEffects.SpellEffectEnums.ContinuousSpellCardEffect;
import controller.duel.CardEffects.SpellEffectEnums.EquipSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.FieldSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.LogicalActivationRequirement;
import controller.duel.CardEffects.SpellEffectEnums.NormalSpellCardEffect;
import controller.duel.CardEffects.SpellEffectEnums.QuickSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.RitualSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.UserReplyForActivation;
import model.cardData.General.Card;
import model.cardData.General.CardPosition;
import model.cardData.General.CardType;

public class SpellCard extends Card {
    private boolean isCardActivated;
    private int numberOfTurnsForActivation;
    private SpellCardValue spellCardValue;
    private boolean isAlreadyActivated;
    private ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects = new ArrayList<>();
    private ArrayList<EquipSpellEffect> equipSpellEffects = new ArrayList<>();
    private ArrayList<FieldSpellEffect> fieldSpellEffects = new ArrayList<>();
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements = new ArrayList<>();
    private ArrayList<NormalSpellCardEffect> normalSpellCardEffects = new ArrayList<>();
    private ArrayList<QuickSpellEffect> quickSpellEffects = new ArrayList<>();
    private ArrayList<RitualSpellEffect> ritualSpellEffects = new ArrayList<>();
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects = new ArrayList<>();
    private ArrayList<UserReplyForActivation> userReplyForActivations = new ArrayList<>();

    public SpellCard(String cardName, String cardDescription, SpellCardValue spellCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
            HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.spellCardValue = spellCardValue;
        setEnumValues(enumValues);
    }

    public SpellCard(SpellCard spellCard) {
        super(spellCard.getCardName(), CardType.SPELL, spellCard.getCardDescription(), null,
                spellCard.getNumberOfAllowedUsages(), spellCard.getCardPrice());
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = spellCard.getNumberOfTurnsForActivation();
        this.spellCardValue = spellCard.getSpellCardValue();
        this.continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
        this.equipSpellEffects = spellCard.getEquipSpellEffects();
        this.fieldSpellEffects = spellCard.getFieldSpellEffects();
        this.logicalActivationRequirements = spellCard.getLogicalActivationRequirements();
        this.normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
        this.quickSpellEffects = spellCard.getQuickSpellEffects();
        this.ritualSpellEffects = spellCard.getRitualSpellEffects();
        this.sentToGraveyardEffects = spellCard.getSentToGraveyardEffects();
        this.userReplyForActivations = spellCard.getUserReplyForActivations();
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

    private void setEnumValues(HashMap<String, List<String>> enumValues) {

        for (int i = 1; i < enumValues.get("ContinuousSpellCardEffect").size(); i++) {
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.valueOf(enumValues.get("ContinuousSpellCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("EquipSpellEffect").size(); i++) {
            equipSpellEffects.add(EquipSpellEffect.valueOf(enumValues.get("EquipSpellEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("FieldSpellEffect").size(); i++) {
            fieldSpellEffects.add(FieldSpellEffect.valueOf(enumValues.get("FieldSpellEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("LogicalActivationRequirement").size(); i++) {
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.valueOf(enumValues.get("LogicalActivationRequirement").get(i)));
        }

        for (int i = 1; i < enumValues.get("NormalSpellCardEffect").size(); i++) {
            normalSpellCardEffects.add(NormalSpellCardEffect.valueOf(enumValues.get("NormalSpellCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("SentToGraveyardEffect").size(); i++) {
            sentToGraveyardEffects.add(SentToGraveyardEffect.valueOf(enumValues.get("SentToGraveyardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("QuickSpellEffect").size(); i++) {
            quickSpellEffects.add(QuickSpellEffect.valueOf(enumValues.get("QuickSpellEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("RitualSpellEffect").size(); i++) {
            ritualSpellEffects.add(RitualSpellEffect.valueOf(enumValues.get("RitualSpellEffect").get(i)));
        }
        for (int i = 1; i < enumValues.get("UserReplyForActivation").size(); i++) {
            userReplyForActivations
                    .add(UserReplyForActivation.valueOf(enumValues.get("UserReplyForActivation").get(i)));
        }

    }

    @Override
    protected Object clone() {
        return new SpellCard(this);
    }
}
