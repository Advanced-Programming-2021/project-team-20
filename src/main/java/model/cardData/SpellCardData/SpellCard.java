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
                     int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
                     HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
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
        if (cardDescription.equals("a")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD);
        } else if (cardDescription.equals("b")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS);
        } else if (cardDescription.equals("c")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS);
        } else if (cardDescription.equals("d")) {
            equipSpellEffects.add(EquipSpellEffect.DARK_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_DARK_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_DARK_MONSTER_OWNER_CONTROLS);
        } else if (cardDescription.equals("e")) {
            equipSpellEffects.add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
        } else if (cardDescription.equals("f")) {
            equipSpellEffects.add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
        }
        if (enumValues != null) {
            setEnumValues(enumValues);
        }
    }

    public SpellCard(SpellCard spellCard) {
        super(spellCard.getCardName(), CardType.SPELL, spellCard.getCardDescription(), spellCard.getCardPosition(),
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
