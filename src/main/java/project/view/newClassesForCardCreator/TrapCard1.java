package project.view.newClassesForCardCreator;

import javafx.scene.image.Image;
import project.controller.duel.CardEffects.TrapEffectEnums.*;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.CardPosition;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrapCard1 {
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

    public TrapCard1(String cardName, String cardDescription, TrapCardValue trapCardValue, CardPosition notApplicable
        , int numberOfAllowedUsages, int numberOfTurnsForActivationForTrapCard, int i, HashMap<String, List<String>> hashMapEffects
        , Image cardImage, HashMap<String, Integer> numbersOfEffectsToSend) {

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
        TrapCard trapCard = new TrapCard(cardName, cardDescription, trapCardValue, notApplicable, numberOfAllowedUsages, numberOfTurnsForActivationForTrapCard,
        i, hashMapEffects, cardImage);
        Storage.addCardToNewCardsCrated(trapCard);
        setEnumValues(hashMapEffects);
        addLogicalActivationRequirements();
//        this.turnCardWasSet = 0;
//        this.highestNumberOfTurnsOfActivation = numberOfTurnsForActivation;
//        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
//        this.trapCardValue = trapCardValue;
//        this.continuousTrapCardEffects = new ArrayList<>();
//        this.flipSummonTrapCardEffects = new ArrayList<>();
//        this.logicalActivationRequirements = new ArrayList<>();
//        this.monsterAttackingTrapCardEffects = new ArrayList<>();
//        this.normalSummonTrapCardEffects = new ArrayList<>();
//        this.tributeSummonTrapCardEffects = new ArrayList<>();
//        this.trapCardActivationTrapCardEffects = new ArrayList<>();
//        this.normalTrapCardEffects = new ArrayList<>();
//        this.ritualSummonTrapCardEffects = new ArrayList<>();
//        this.specialSummonTrapCardEffects = new ArrayList<>();
//        this.monsterEffectActivationTrapCardEffect = new ArrayList<>();
//        this.spellCardActivationTrapCardEffects = new ArrayList<>();
//        this.trapCardActivationTrapCardEffects = new ArrayList<>();
//        this.userReplyForActivations = new ArrayList<>();
//        setEnumValues(hashMapEffects);
//        addLogicalActivationRequirements();
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

    private void addLogicalActivationRequirements() {
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.NORMAL_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
        }
        if (flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.IF_ATK_IS_AT_LEAST_1000_ATK_DESTROY_IT)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.FLIP_SUMMONED_MONSTER_MUST_HAVE_AT_LEAST_1000_ATK);
        }
        if (normalSummonTrapCardEffects.contains(NormalSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            flipSummonTrapCardEffects.contains(FlipSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            specialSummonTrapCardEffects.contains(SpecialSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            ritualSummonTrapCardEffects.contains(RitualSummonTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            monsterEffectActivationTrapCardEffect.contains(MonsterEffectActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD) ||
            trapCardActivationTrapCardEffects.contains(TrapCardActivationTrapCardEffect.NEGATE_ACTIVATION_OR_SUMMONING_AND_DESTROY_CARD)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.MONSTER_IS_SUMMONED_OR_SPELL_TRAP_MONSTER_EFFECT_INCLUDING_SPECIAL_SUMMONING);

        }
        if (normalTrapCardEffects.contains(NormalTrapCardEffect.OPPONENT_DISCARDS_ALL_CARDS_WITH_A_GIVEN_NAME_OTHERWISE_OWNER_LOSES_ONE_RANDOM_CARD)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            logicalActivationRequirements.add(LogicalActivationRequirement.OPPONENT_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            if (!userReplyForActivations.contains(UserReplyForActivation.ENTER_NAME_OF_A_CARD)) {
                userReplyForActivations.add(UserReplyForActivation.ENTER_NAME_OF_A_CARD);
            }
        }
        if (monsterAttackingTrapCardEffects.contains(MonsterAttackingTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION) ||
            normalTrapCardEffects.contains(NormalTrapCardEffect.SPECIAL_SUMMON_ONE_MONSTER_IN_YOUR_GRAVEYARD_IN_FACE_UP_ATTACK_POSITION)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_IN_THEIR_GY);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_OWNERS_GRAVEYARD_TO_SPECIAL_SUMMON)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_OWNERS_GRAVEYARD_TO_SPECIAL_SUMMON);
            }

        }
        if (spellCardActivationTrapCardEffects.contains(SpellCardActivationTrapCardEffect.DISCARD_1_CARD_NEGATE_DESTROY_CARD)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_CARD_IN_HAND);
            if (!userReplyForActivations.contains(UserReplyForActivation.DISCARD_1_CARD)){
            userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);}
        }

    }
}
