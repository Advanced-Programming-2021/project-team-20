package test.maven.model.card.cardData.TrapCardData;

import java.util.ArrayList;

import test.maven.model.card.cardData.General.Card;
import test.maven.model.card.cardData.General.CardPosition;
import test.maven.model.card.cardData.General.CardType;
import test.maven.model.card.cardEffects.SpellEffectEnums.LogicalActivationRequirement;
import test.maven.model.card.cardEffects.SpellEffectEnums.UserReplyForActivation;
import test.maven.model.card.cardEffects.TrapEffectEnums.ContinuousTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.FlipSummonTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.MonsterAttackingTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.NormalSummonTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.NormalTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.RitualSummonTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.SpecialSummonTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.SpellCardActivationTrapCardEffect;
import test.maven.model.card.cardEffects.TrapEffectEnums.TrapCardActivationTrapCardEffect;

public class TrapCard extends Card {
    private boolean isCardActivated;
    private int numberOfTurnsForActivation;
    private TrapCardValue trapCardValue;
    private boolean isAlreadyActivated;
    private ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects;
    private ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;
    private ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects;
    private ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects;
    private ArrayList<NormalTrapCardEffect> normalTrapCardEffects;
    private ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects;
    private ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects;
    private ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects;
    private ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects;
    private ArrayList<UserReplyForActivation> userReplyForActivations;

    public TrapCard(String cardName, String cardDescription,TrapCardValue trapCardValue, CardPosition cardPosition, int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.isCardActivated = false;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.trapCardValue = trapCardValue;
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
}
