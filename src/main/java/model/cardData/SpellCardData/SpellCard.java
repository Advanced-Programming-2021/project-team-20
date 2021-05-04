package model.cardData.SpellCardData;
import java.util.ArrayList;

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
        this.continuousSpellCardEffects = new ArrayList<>();
        this.equipSpellEffects = new ArrayList<>();
        this.fieldSpellEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.normalSpellCardEffects = new ArrayList<>();
        this.quickSpellEffects = new ArrayList<>();
        this.ritualSpellEffects = new ArrayList<>();
        this.sentToGraveyardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
        if (cardDescription.equals("a")){
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD);
        } else if (cardDescription.equals("b")){
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS);
        } else if (cardDescription.equals("c")){
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS);
        }
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
    @Override
    protected Object clone(){
        return new SpellCard(cardName, cardDescription, spellCardValue, cardPosition, numberOfAllowedUsages, numberOfTurnsForActivation, cardPrice);
    }
}
