package CardData.SpellCardData;
import CardData.General.*;
import CardEffects.SpellEffectEnums.*;
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

    public SpellCard(String cardName, String cardDescription, CardPosition cardPosition, SpellCardValue spellCardValue, int numberOfAllowedUsages, int numberOfTurnsForActivation) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages);
        this.isCardActivated = false;
        this.spellCardValue = spellCardValue;
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
