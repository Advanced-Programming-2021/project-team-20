package model.cardData.TrapCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import controller.duel.CardEffects.TrapEffectEnums.*;
import model.cardData.General.Card;
import model.cardData.General.CardPosition;
import model.cardData.General.CardType;

public class TrapCard extends Card {
    private int highestNumberOfTurnsOfActivation;
    private int numberOfTurnsForActivation;
    private int turnCardWasSet;
    private TrapCardValue trapCardValue;
    private ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects;
    private ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;
    private ArrayList<MonsterAttackingTrapCardEffect> monsterAttackingTrapCardEffects;
    private ArrayList<NormalSummonTrapCardEffect> normalSummonTrapCardEffects;
    private ArrayList<TributeSummonTrapCardEffect> tributeSummonTrapCardEffects;
    private ArrayList<NormalTrapCardEffect> normalTrapCardEffects;
    private ArrayList<RitualSummonTrapCardEffect> ritualSummonTrapCardEffects;
    private ArrayList<SpecialSummonTrapCardEffect> specialSummonTrapCardEffects;
    private ArrayList<MonsterEffectActivationTrapCardEffect> monsterEffectActivationTrapCardEffect;
    private ArrayList<SpellCardActivationTrapCardEffect> spellCardActivationTrapCardEffects;
    private ArrayList<TrapCardActivationTrapCardEffect> trapCardActivationTrapCardEffects;
    private ArrayList<UserReplyForActivation> userReplyForActivations;

    public TrapCard(String cardName, String cardDescription, TrapCardValue trapCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
            HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.turnCardWasSet = 0;
        this.highestNumberOfTurnsOfActivation = numberOfTurnsForActivation;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.trapCardValue = trapCardValue;
        this.continuousTrapCardEffects = new ArrayList<>();
        this.flipSummonTrapCardEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.monsterAttackingTrapCardEffects = new ArrayList<>();
        this.normalSummonTrapCardEffects = new ArrayList<>();
        this.tributeSummonTrapCardEffects = new ArrayList<>();
        this.trapCardActivationTrapCardEffects = new ArrayList<>();
        this.normalTrapCardEffects = new ArrayList<>();
        this.ritualSummonTrapCardEffects = new ArrayList<>();
        this.specialSummonTrapCardEffects = new ArrayList<>();
        this.monsterEffectActivationTrapCardEffect = new ArrayList<>();
        this.spellCardActivationTrapCardEffects = new ArrayList<>();
        //this.trapCardActivationTrapCardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();

        if (enumValues != null) {
            setEnumValues(enumValues);
        }
    }

    public TrapCard(TrapCard trapCard) {
        super(trapCard.getCardName(), CardType.TRAP, trapCard.getCardDescription(), trapCard.getCardPosition(),
                trapCard.getNumberOfAllowedUsages(), trapCard.getCardPrice());
        this.turnCardWasSet = 0;
        this.highestNumberOfTurnsOfActivation = trapCard.getHighestNumberOfTurnsOfActivation();
        this.numberOfTurnsForActivation = trapCard.getNumberOfTurnsForActivation();
        this.trapCardValue = trapCard.getTrapCardValue();
        this.continuousTrapCardEffects = trapCard.getContinuousTrapCardEffects();
        this.flipSummonTrapCardEffects = trapCard.getFlipSummonTrapCardEffects();
        this.logicalActivationRequirements = trapCard.getLogicalActivationRequirements();
        this.monsterAttackingTrapCardEffects = trapCard.getMonsterAttackingTrapCardEffects();
        this.normalSummonTrapCardEffects = trapCard.getNormalSummonTrapCardEffects();
        this.tributeSummonTrapCardEffects = trapCard.getTributeSummonTrapCardEffects();
        this.normalTrapCardEffects = trapCard.getNormalTrapCardEffects();
        this.ritualSummonTrapCardEffects = trapCard.getRitualSummonTrapCardEffects();
        this.specialSummonTrapCardEffects = trapCard.getSpecialSummonTrapCardEffects();
        this.monsterEffectActivationTrapCardEffect = trapCard.getMonsterEffectActivationTrapCardEffect();
        this.spellCardActivationTrapCardEffects = trapCard.getSpellCardActivationTrapCardEffects();
        this.trapCardActivationTrapCardEffects = trapCard.getTrapCardActivationTrapCardEffects();
        this.userReplyForActivations = trapCard.getUserReplyForActivations();
    }

    public int getHighestNumberOfTurnsOfActivation() {
        return highestNumberOfTurnsOfActivation;
    }

    public int getNumberOfTurnsForActivation() {
        return numberOfTurnsForActivation;
    }

    public TrapCardValue getTrapCardValue() {
        return trapCardValue;
    }

    public boolean isCardAlreadyActivated() {
        return cardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION);
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

    public ArrayList<TributeSummonTrapCardEffect> getTributeSummonTrapCardEffects() {
        return tributeSummonTrapCardEffects;
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

    public void setNumberOfTurnsForActivation(int numberOfTurnsForActivation) {
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
    }

    public int getTurnCardWasSet() {
        return turnCardWasSet;
    }

    public void setTurnCardWasSet(int turnCardWasSet) {
        this.turnCardWasSet = turnCardWasSet;
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
    public Object clone() {
        return new TrapCard(this);
    }
}
