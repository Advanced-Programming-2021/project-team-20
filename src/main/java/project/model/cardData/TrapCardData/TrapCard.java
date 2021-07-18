package project.model.cardData.TrapCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.image.Image;
import project.model.TrapEffectEnums.*;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;

public class TrapCard extends Card {
    private int highestNumberOfTurnsOfActivation;
    private int numberOfTurnsForActivation;
    private int turnCardWasSet;
    private TrapCardValue trapCardValue;
    private ArrayList<ContinuousTrapCardEffect> continuousTrapCardEffects;//
    private ArrayList<FlipSummonTrapCardEffect> flipSummonTrapCardEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;//
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
            HashMap<String, List<String>> enumValues, Image image) {
        super(cardName, CardType.TRAP, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice, image);
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
        this.trapCardActivationTrapCardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
        if (cardName.equals("Torrential Tribute")) {
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.DESTROY_ALL_MONSTERS_ON_FIELD);
        } else if (cardName.equals("Mirror Force")) {
            monsterAttackingTrapCardEffects
                    .add(MonsterAttackingTrapCardEffect.DESTROY_ALL_OPPONENT_ATTACK_POSITION_MONSTERS);
        } else if (cardName.equals("Magic Cylinder")) {
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
            monsterAttackingTrapCardEffects
                    .add(MonsterAttackingTrapCardEffect.INFLICT_DAMAGE_TO_OPPONENT_EQUAL_TO_MONSTERS_ATK);
        } else if (cardName.equals("Negate Attack")) {
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.END_BATTLE_PHASE);
        } else if (cardName.equals("Trap Hole")) {
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
            flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
        } else if (cardName.equals("Time Seal")) {
            normalTrapCardEffects.add(NormalTrapCardEffect.SKIP_OPPONENT_DRAW_PHASE_NEXT_TURN);
        } else if (cardName.equals("Solemn Warning")) {
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.PAY_2000_LP);
            normalSummonTrapCardEffects.add(NormalSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.PAY_2000_LP);
            flipSummonTrapCardEffects.add(FlipSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            specialSummonTrapCardEffects.add(SpecialSummonTrapCardEffect.PAY_2000_LP);
            specialSummonTrapCardEffects
                    .add(SpecialSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            ritualSummonTrapCardEffects.add(RitualSummonTrapCardEffect.PAY_2000_LP);
            ritualSummonTrapCardEffects.add(RitualSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            spellCardActivationTrapCardEffects.add(SpellCardActivationTrapCardEffect.PAY_2000_LP);
            spellCardActivationTrapCardEffects
                    .add(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            monsterEffectActivationTrapCardEffect.add(MonsterEffectActivationTrapCardEffect.PAY_2000_LP);
            monsterEffectActivationTrapCardEffect
                    .add(MonsterEffectActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.PAY_2000_LP);
            monsterAttackingTrapCardEffects
                    .add(MonsterAttackingTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            trapCardActivationTrapCardEffects.add(TrapCardActivationTrapCardEffect.PAY_2000_LP);
            trapCardActivationTrapCardEffects
                    .add(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD);
            logicalActivationRequirements.add(
                    LogicalActivationRequirement.MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING);
        } else if (cardName.equals("Magic Jammer")) {
            spellCardActivationTrapCardEffects
                    .add(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);
        } else if (cardName.equals("Call Of The Haunted")) {
            monsterAttackingTrapCardEffects.add(
                    MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION);
            normalTrapCardEffects.add(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_OWNERS_GRAVEYARD_TO_SPECIAL_SUMMON);
        } else if (cardName.equals("Mind Crush")) {
            normalTrapCardEffects.add(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            logicalActivationRequirements.add(LogicalActivationRequirement.OPPONENT_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            userReplyForActivations.add(UserReplyForActivation.ENTER_NAME_OF_A_CARD);
        } else if (cardName.equals("D.D. Dynamite")){
            normalTrapCardEffects.add(NormalTrapCardEffect.DEAL_300_MULTIPLIED_BY_NUMBER_OF_CARDS_IN_GRAVEYARD_TO_OPPONENT);
        } else if (cardName.equals("Blasting Zone")){
            normalTrapCardEffects.add(NormalTrapCardEffect.DESTROY_ALL_CARDS_IN_THE_SAME_COLUMN_AS_THIS_CARD);
            logicalActivationRequirements.add(LogicalActivationRequirement.ALL_CARDS_IN_SAME_COLUMN_AS_THIS_CARD_MUST_BE_OCCUPIED);
        } else if (cardName.equals("Draining Shield")){
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.NEGATE_OPPONENT_ATTACK);
            monsterAttackingTrapCardEffects.add(MonsterAttackingTrapCardEffect.GAIN_HP_EQUAL_TO_MONSTERS_ATK);
        }
        if (enumValues != null) {
            setEnumValues(enumValues);
        }
    }

    public TrapCard(TrapCard trapCard) {
        super(trapCard.getCardName(), CardType.TRAP, trapCard.getCardDescription(), trapCard.getCardPosition(),
                trapCard.getNumberOfAllowedUsages(), trapCard.getCardPrice(), trapCard.getImage());
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

    public String[] toCSVFormatString() {
        List<String> csvArray = new ArrayList<>();
        csvArray.add(cardName);
        csvArray.add("Spell");
        csvArray.add(trapCardValue + "");
        csvArray.add(cardDescription);
        csvArray.add(numberOfAllowedUsages == 3 ? "Unlimited" : "Limited");
        csvArray.add(cardPrice + "");
        csvArray.add(toCSVformatEffectsOfCards(continuousTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(flipSummonTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(logicalActivationRequirements));
        csvArray.add(toCSVformatEffectsOfCards(monsterAttackingTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(monsterEffectActivationTrapCardEffect));
        csvArray.add(toCSVformatEffectsOfCards(normalSummonTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(normalTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(ritualSummonTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(specialSummonTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(spellCardActivationTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(trapCardActivationTrapCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(userReplyForActivations));
        String[] res = new String[csvArray.size()];
        res = csvArray.toArray(res);
        return res;
    }

    private String toCSVformatEffectsOfCards(ArrayList effects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        for (int i = 0; i < effects.size(); i++) {
            stringBuilder.append(effects.get(i));
            if (i + 1 < effects.size()) {
                stringBuilder.append("#");
            }
        }
        return stringBuilder.toString();
    }
}
