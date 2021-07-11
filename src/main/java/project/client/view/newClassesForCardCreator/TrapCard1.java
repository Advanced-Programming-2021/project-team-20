package project.client.view.newClassesForCardCreator;

import javafx.scene.image.Image;
import project.model.TrapEffectEnums.*;
import project.server.controller.non_duel.storage.Storage;
import project.model.cardData.General.CardPosition;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;

import java.util.*;

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
        , Image cardImage, HashMap<String, Integer> numbersOfEffectsToSend, String imagePath) {

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

        setEnumValues(hashMapEffects);
        addLogicalActivationRequirements();
        HashMap<String, List<String>> hashMap = new HashMap<>();
        ArrayList<String> cont = new ArrayList<>();
        for (int j = 0; j < continuousTrapCardEffects.size(); j++){
            cont.add(continuousTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("ContinuousTrapCardEffect", cont);
        ArrayList<String> flip = new ArrayList<>();
        for (int j = 0; j < flipSummonTrapCardEffects.size(); j++){
            flip.add(flipSummonTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("FlipSummonTrapCardEffect", flip);
        ArrayList<String> logAct = new ArrayList<>();
        for (int j = 0; j < logicalActivationRequirements.size(); j++){
            logAct.add(logicalActivationRequirements.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("LogicalActivationRequirement", logAct);
        ArrayList<String> monsterAttacking = new ArrayList<>();
        for (int j = 0; j < monsterAttackingTrapCardEffects.size(); j++){
            monsterAttacking.add(monsterAttackingTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("MonsterAttackingTrapCardEffect", monsterAttacking);
        ArrayList<String> monsterEffect = new ArrayList<>();
        for (int j = 0; j < monsterEffectActivationTrapCardEffect.size(); j++){
            monsterEffect.add(monsterEffectActivationTrapCardEffect.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("MonsterEffectActivationTrapCardEffect", monsterEffect);
        ArrayList<String> normalSummonTrap = new ArrayList<>();
        for (int j = 0; j < normalSummonTrapCardEffects.size(); j++){
            normalSummonTrap.add(normalSummonTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("NormalSummonTrapCardEffect", normalSummonTrap);
        ArrayList<String> normalTrap = new ArrayList<>();
        for (int j = 0; j < normalTrapCardEffects.size(); j++){
            normalTrap.add(normalSummonTrap.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("NormalTrapCardEffect", normalTrap);
        ArrayList<String> ritual = new ArrayList<>();
        for (int j = 0; j < ritualSummonTrapCardEffects.size(); j++){
            ritual.add(ritualSummonTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("RitualSummonTrapCardEffect", ritual);
        ArrayList<String> specialSummon = new ArrayList<>();
        for (int j = 0; j < specialSummonTrapCardEffects.size(); j++){
            specialSummon.add(specialSummonTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("SpecialSummonTrapCardEffect", specialSummon);
        ArrayList<String> spellCard = new ArrayList<>();
        for (int j = 0; j < spellCardActivationTrapCardEffects.size(); j++){
            spellCard.add(specialSummonTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("SpellCardActivationTrapCardEffect", spellCard);
        ArrayList<String> trapCardAc = new ArrayList<>();
        for (int j = 0; j < trapCardActivationTrapCardEffects.size(); j++){
            trapCardAc.add(trapCardActivationTrapCardEffects.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("TrapCardActivationTrapCardEffect", trapCardAc);
        ArrayList<String> userReply = new ArrayList<>();
        for (int j = 0; j < userReplyForActivations.size(); j++){
            userReply.add(userReplyForActivations.get(j).toString());
            //hashMap.put("ContinuousTrapCardEffect", continuousTrapCardEffects.get(i).toString());
        }
        hashMap.put("UserReplyForActivation", userReply);
        TrapCard trapCard = new TrapCard(cardName, cardDescription, trapCardValue, notApplicable, numberOfAllowedUsages, numberOfTurnsForActivationForTrapCard,
            i, hashMap, cardImage);
        Storage.addCardToNewCardsCrated(trapCard);
        Storage.saveNewImagesOfCardsInFile(trapCard, imagePath);
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
