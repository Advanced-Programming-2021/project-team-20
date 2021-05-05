package model.cardData.TrapCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.duel.CardEffects.SpellEffectEnums.LogicalActivationRequirement;
import controller.duel.CardEffects.SpellEffectEnums.UserReplyForActivation;
import controller.duel.CardEffects.TrapEffectEnums.ContinuousTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.FlipSummonTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.MonsterAttackingTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.MonsterEffectActivationTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.NormalSummonTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.NormalTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.RitualSummonTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.SpecialSummonTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.SpellCardActivationTrapCardEffect;
import controller.duel.CardEffects.TrapEffectEnums.TrapCardActivationTrapCardEffect;
import model.cardData.General.Card;
import model.cardData.General.CardPosition;
import model.cardData.General.CardType;

public class TrapCard extends Card {
    private boolean isCardActivated;
    private int numberOfTurnsForActivation;
    private TrapCardValue trapCardValue;
    private boolean isAlreadyActivated;
    private ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects = new ArrayList<>();
    private ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects = new ArrayList<>();
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements = new ArrayList<>();
    private ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects = new ArrayList<>();
    private ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects = new ArrayList<>();
    private ArrayList<NormalTrapCardEffect> normalTrapCardEffects = new ArrayList<>();
    private ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects = new ArrayList<>();
    private ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects = new ArrayList<>();
    private ArrayList<MonsterEffectActivationTrapCardEffect> monsterEffectActivationTrapCardEffect = new ArrayList<>();
    private ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects = new ArrayList<>();
    private ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects = new ArrayList<>();
    private ArrayList<UserReplyForActivation> userReplyForActivations = new ArrayList<>();

    public TrapCard(String cardName, String cardDescription, TrapCardValue trapCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
            HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.trapCardValue = trapCardValue;
        setEnumValues(enumValues);
    }

    public TrapCard(TrapCard trapCard) {
        super(trapCard.getCardName(), CardType.TRAP, trapCard.getCardDescription(), trapCard.getCardPosition(),
                trapCard.getNumberOfAllowedUsages(), trapCard.getCardPrice());
        this.isCardActivated = trapCard.isCardActivated;
        this.numberOfTurnsForActivation = trapCard.getNumberOfTurnsForActivation();
        this.trapCardValue = trapCard.getTrapCardValue();
        this.continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
        this.flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
        this.logicalActivationRequirements = trapCard.getLogicalActivationRequirements();
        this.monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        this.normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        this.normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
        this.ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
        this.specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();
        this.monsterEffectActivationTrapCardEffect = trapCard.getMonsterEffectActivationTrapCardEffect();
        this.spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
        this.trapCardActivationTrapCardEffects = trapCard.getTrapCardActivationTrapCardEffects();
        this.userReplyForActivations = trapCard.getUserReplyForActivations();
    }

    public boolean isCardActivated() {
        return isCardActivated;
    }

    public int getNumberOfTurnsForActivation() {
        return numberOfTurnsForActivation;
    }

    public TrapCardValue getTrapCardValue() {
        return trapCardValue;
    }

    public boolean isAlreadyActivated() {
        return isAlreadyActivated;
    }

    public ArrayList<ContinuousTrapCardEffect> getContinuousTrapCardEffects() {
        return continuousTrapCardEffects;
    }

    public ArrayList<FlipSummonTrapCardEffect> getFlipSummonTrapCardEffects() {
        return flipSummonTrapCardEffects;
    }

    public ArrayList<LogicalActivationRequirement> getLogicalActivationRequirements() {
        return logicalActivationRequirements;
    }

    public ArrayList<MonsterAttackingTrapCardEffect> getMonsterAttackingTrapCardEffects() {
        return monsterAttackingTrapCardEffects;
    }

    public ArrayList<NormalSummonTrapCardEffect> getNormalSummonTrapCardEffects() {
        return normalSummonTrapCardEffects;
    }

    public ArrayList<NormalTrapCardEffect> getNormalTrapCardEffects() {
        return normalTrapCardEffects;
    }

    public ArrayList<RitualSummonTrapCardEffect> getRitualSummonTrapCardEffects() {
        return ritualSummonTrapCardEffects;
    }

    public ArrayList<SpecialSummonTrapCardEffect> getSpecialSummonTrapCardEffects() {
        return specialSummonTrapCardEffects;
    }

    public ArrayList<MonsterEffectActivationTrapCardEffect> getMonsterEffectActivationTrapCardEffect() {
        return monsterEffectActivationTrapCardEffect;
    }

    public ArrayList<SpellCardActivationTrapCardEffect> getSpellCardActivationTrapCardEffects() {
        return spellCardActivationTrapCardEffects;
    }

    public ArrayList<TrapCardActivationTrapCardEffect> getTrapCardActivationTrapCardEffects() {
        return trapCardActivationTrapCardEffects;
    }

    public ArrayList<UserReplyForActivation> getUserReplyForActivations() {
        return userReplyForActivations;
    }

    public void setAlreadyActivated(boolean alreadyActivated) {
        isAlreadyActivated = alreadyActivated;
    }

    private void setEnumValues(HashMap<String, List<String>> enumValues) {

        for (int i = 1; i < enumValues.get("ContinuousTrapCardEffect").size(); i++) {
            continuousTrapCardEffects
                    .add(ContinuousTrapCardEffect.valueOf(enumValues.get("ContinuousTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("FlipSummonTrapCardEffect").size(); i++) {
            flipSummonTrapCardEffects
                    .add(FlipSummonTrapCardEffect.valueOf(enumValues.get("FlipSummonTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("LogicalActivationRequirement").size(); i++) {
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.valueOf(enumValues.get("LogicalActivationRequirement").get(i)));
        }

        for (int i = 1; i < enumValues.get("MonsterAttackingTrapCardEffect").size(); i++) {
            monsterAttackingTrapCardEffects.add(
                    MonsterAttackingTrapCardEffect.valueOf(enumValues.get("MonsterAttackingTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("MonsterEffectActivationTrapCardEffect").size(); i++) {
            monsterEffectActivationTrapCardEffect.add(MonsterEffectActivationTrapCardEffect
                    .valueOf(enumValues.get("MonsterEffectActivationTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("NormalSummonTrapCardEffect").size(); i++) {
            normalSummonTrapCardEffects
                    .add(NormalSummonTrapCardEffect.valueOf(enumValues.get("NormalSummonTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("NormalTrapCardEffect").size(); i++) {
            normalTrapCardEffects.add(NormalTrapCardEffect.valueOf(enumValues.get("NormalTrapCardEffect").get(i)));
        }
        for (int i = 1; i < enumValues.get("RitualSummonTrapCardEffect").size(); i++) {
            ritualSummonTrapCardEffects
                    .add(RitualSummonTrapCardEffect.valueOf(enumValues.get("RitualSummonTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("SpecialSummonTrapCardEffect").size(); i++) {
            specialSummonTrapCardEffects
                    .add(SpecialSummonTrapCardEffect.valueOf(enumValues.get("SpecialSummonTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("SpellCardActivationTrapCardEffect").size(); i++) {
            spellCardActivationTrapCardEffects.add(SpellCardActivationTrapCardEffect
                    .valueOf(enumValues.get("SpellCardActivationTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("TrapCardActivationTrapCardEffect").size(); i++) {
            trapCardActivationTrapCardEffects.add(TrapCardActivationTrapCardEffect
                    .valueOf(enumValues.get("TrapCardActivationTrapCardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("UserReplyForActivation").size(); i++) {
            userReplyForActivations
                    .add(UserReplyForActivation.valueOf(enumValues.get("UserReplyForActivation").get(i)));
        }

    }

    @Override
    protected Object clone() {
        return new TrapCard(this);
    }
}
