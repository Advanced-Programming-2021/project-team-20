package CardData.TrapCardData;
import CardData.General.*;
import CardData.SpellCardData.SpellCard;
import CardEffects.EffectImplementations.Effect;
import CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import CardEffects.TrapEffectEnums.*;

import java.util.ArrayList;

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
    private ArrayList<MonsterEffectActivationTrapCardEffect> monsterEffectActivationTrapCardEffect;
    private ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects;
    private ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects;
    private ArrayList<UserReplyForActivation> userReplyForActivations;

    public TrapCard(String cardName, String cardDescription, CardPosition cardPosition, TrapCardValue trapCardValue, int numberOfAllowedUsages, int numberOfTurnsForActivation) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages);
        this.isCardActivated = false;
        this.trapCardValue = trapCardValue;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.continuousTrapCardEffects = new ArrayList<>();
        this.flipSummonTrapCardEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.monsterAttackingTrapCardEffects = new ArrayList<>();
        this.normalSummonTrapCardEffects = new ArrayList<>();
        this.normalTrapCardEffects = new ArrayList<>();
        this.ritualSummonTrapCardEffects = new ArrayList<>();
        this.specialSummonTrapCardEffects = new ArrayList<>();
        this.monsterEffectActivationTrapCardEffect = new ArrayList<>();
        this.spellCardActivationTrapCardEffects = new ArrayList<>();
        this.trapCardActivationTrapCardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
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
}
