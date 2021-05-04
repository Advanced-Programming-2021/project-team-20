package model.cardData.TrapCardData;
import java.util.ArrayList;

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
	
    public TrapCard(String cardName, String cardDescription,TrapCardValue trapCardValue, CardPosition cardPosition, int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
       this.isCardActivated = false;
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
        if (cardDescription.equals("a")){
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD);
        } else if (cardDescription.equals("b")){
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS);
        } else if (cardDescription.equals("c")){
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK);
        } else if (cardDescription.equals("d")){
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE);
        } else if (cardDescription.equals("e")){
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
            flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
            logicalActivationRequirements.add(LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
            logicalActivationRequirements.add(LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
        }
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
    @Override 
    protected Object clone(){
        return new TrapCard(cardName, cardDescription, trapCardValue, cardPosition, numberOfAllowedUsages, numberOfTurnsForActivation, cardPrice);
    }
}
