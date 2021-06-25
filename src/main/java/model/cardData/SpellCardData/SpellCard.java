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
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.CardType;

public class SpellCard extends Card {
    private int highestNumberOfTurnsOfActivation;
    private int numberOfTurnsForActivation;
    private boolean oncePerTurnCardEffectUsed;
    private SpellCardValue spellCardValue;
    private ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects;
    private ArrayList<EquipSpellEffect> equipSpellEffects;
    private ArrayList<FieldSpellEffect> fieldSpellEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;
    private ArrayList<NormalSpellCardEffect> normalSpellCardEffects;
    private ArrayList<QuickSpellEffect> quickSpellEffects;
    private ArrayList<RitualSpellEffect> ritualSpellEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;
    private ArrayList<UserReplyForActivation> userReplyForActivations;
    private ArrayList<CardLocation> cardLocationsToWhichEquipSpellIsApplied;

    public SpellCard(String cardName, String cardDescription, SpellCardValue spellCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
            HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.highestNumberOfTurnsOfActivation = numberOfTurnsForActivation;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.oncePerTurnCardEffectUsed = false;
        this.spellCardValue = spellCardValue;
        this.continuousSpellCardEffects = new ArrayList<>();
        this.equipSpellEffects = new ArrayList<>();
        this.fieldSpellEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.normalSpellCardEffects = new ArrayList<>();
        this.quickSpellEffects = new ArrayList<>();
        this.ritualSpellEffects = new ArrayList<>();
        this.sentToGraveyardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
        this.cardLocationsToWhichEquipSpellIsApplied = new ArrayList<>();
        if (enumValues != null) {
            setEnumValues(enumValues);
        }
    }

    public SpellCard(SpellCard spellCard) {
        super(spellCard.getCardName(), CardType.SPELL, spellCard.getCardDescription(), spellCard.getCardPosition(),
                spellCard.getNumberOfAllowedUsages(), spellCard.getCardPrice());
        this.highestNumberOfTurnsOfActivation = spellCard.getHighestNumberOfTurnsOfActivation();
        this.numberOfTurnsForActivation = spellCard.getNumberOfTurnsForActivation();
        this.oncePerTurnCardEffectUsed = spellCard.isOncePerTurnCardEffectUsed();
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
        this.cardLocationsToWhichEquipSpellIsApplied = spellCard.getCardLocationsToWhichEquipSpellIsApplied();
    }

    public int getHighestNumberOfTurnsOfActivation() {
        return highestNumberOfTurnsOfActivation;
    }

    public int getNumberOfTurnsForActivation() {
        return numberOfTurnsForActivation;
    }

    public boolean isOncePerTurnCardEffectUsed() {
        return oncePerTurnCardEffectUsed;
    }

    public SpellCardValue getSpellCardValue() {
        return spellCardValue;
    }

    public boolean isCardAlreadyActivated() {
        return cardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION);
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

    public ArrayList<CardLocation> getCardLocationsToWhichEquipSpellIsApplied() {
        return cardLocationsToWhichEquipSpellIsApplied;
    }

    public void addCardLocationToEquipSpellAffected(CardLocation cardLocation) {
        cardLocationsToWhichEquipSpellIsApplied.add(cardLocation);
    }

    public void clearCardsToWhichEquipSpellIsApplied() {
        cardLocationsToWhichEquipSpellIsApplied.clear();
    }

    public void setNumberOfTurnsForActivation(int numberOfTurnsForActivation) {
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
    }

    public void setOncePerTurnCardEffectUsed(boolean oncePerTurnCardEffectUsed) {
        this.oncePerTurnCardEffectUsed = oncePerTurnCardEffectUsed;
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
    public Object clone() {
        return new SpellCard(this);
    }
}
