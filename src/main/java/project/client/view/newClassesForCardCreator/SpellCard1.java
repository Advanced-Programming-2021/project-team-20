package project.client.view.newClassesForCardCreator;

import javafx.scene.image.Image;
import project.model.MonsterEffectEnums.SentToGraveyardEffect;
import project.model.SpellEffectEnums.*;
import project.server.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SpellCard1 {
    private int highestNumberOfTurnsOfActivation;//
    private int numberOfTurnsForActivation;
    private boolean oncePerTurnCardEffectUsed;
    private SpellCardValue spellCardValue;
    private ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects;
    private ArrayList<FieldSpellEffect> fieldSpellEffects;
    private ArrayList<EquipSpellEffect> equipSpellEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;//
    private ArrayList<NormalSpellCardEffect> normalSpellCardEffects;
    private ArrayList<QuickSpellEffect> quickSpellEffects;
    private ArrayList<RitualSpellEffect> ritualSpellEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;//
    private ArrayList<UserReplyForActivation> userReplyForActivations;

    public SpellCard1(String cardName, String cardDescription, SpellCardValue valueOf, CardPosition notApplicable, int numberOfAllowedUsages
        , int numberOfTurnsForActivationSpell, int i, HashMap<String, List<String>> enumValues, Image cardImage
        , List<String> monsterFamilyTrapEquip, List<String> monsterFamilyTrapField, HashMap<String, Integer> numbersOfEffectsToSend, String imagePath) {
        ArrayList<MonsterCardFamily> monsterCardFamilies = new ArrayList<>();
        if (monsterFamilyTrapEquip != null) {
            for (int j = 1; j < monsterFamilyTrapEquip.size(); j++) {
                monsterCardFamilies.add(MonsterCardFamily.valueOf(monsterFamilyTrapEquip.get(i)));
            }
        }
        if (monsterFamilyTrapField != null) {
            for (int j = 1; j < monsterFamilyTrapField.size(); j++) {
                monsterCardFamilies.add(MonsterCardFamily.valueOf(monsterFamilyTrapField.get(i)));
            }
        }
        this.continuousSpellCardEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.normalSpellCardEffects = new ArrayList<>();
        this.equipSpellEffects = new ArrayList<>();
        this.fieldSpellEffects = new ArrayList<>();
        this.quickSpellEffects = new ArrayList<>();
        this.ritualSpellEffects = new ArrayList<>();
        this.sentToGraveyardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
        setEnumValues(enumValues);
        addLogicalActivationRequirement();
        Collection<Integer> integers = numbersOfEffectsToSend.values();
        ArrayList<Integer> finalIntegers = new ArrayList<>();
        finalIntegers.addAll(integers);
        HashMap<String, List<String>> hashMap = new HashMap<>();
        ArrayList<String> continu = new ArrayList<>();
        for (int j = 0; j < continuousSpellCardEffects.size(); j++) {
            continu.add(continuousSpellCardEffects.get(j).toString());
        }
        hashMap.put("ContinuousSpellCardEffect", continu);
        ArrayList<String> equip = new ArrayList<>();
        for (int j = 0; j < equipSpellEffects.size(); j++) {
            equip.add(equipSpellEffects.get(j).toString());
        }
        hashMap.put("EquipSpellEffect", equip);
        ArrayList<String> field = new ArrayList<>();
        for (int j = 0; j < fieldSpellEffects.size(); j++) {
            field.add(fieldSpellEffects.get(j).toString());
        }
        hashMap.put("FieldSpellEffect", field);
        ArrayList<String> logAc = new ArrayList<>();
        for (int j = 0; j < logicalActivationRequirements.size(); j++) {
            logAc.add(logicalActivationRequirements.get(j).toString());
        }
        hashMap.put("LogicalActivationRequirement", logAc);
        ArrayList<String> norma = new ArrayList<>();
        for (int j = 0; j < normalSpellCardEffects.size(); j++) {
            norma.add(normalSpellCardEffects.get(j).toString());
        }
        hashMap.put("NormalSpellCardEffect", norma);
        ArrayList<String> sentToGY = new ArrayList<>();
        for (int j = 0; j < sentToGraveyardEffects.size(); j++) {
            sentToGY.add(sentToGraveyardEffects.get(j).toString());
        }
        hashMap.put("SentToGraveyardEffect", sentToGY);
        ArrayList<String> quick = new ArrayList<>();
        for (int j = 0; j < quickSpellEffects.size(); j++) {
            quick.add(quickSpellEffects.get(j).toString());
        }
        hashMap.put("QuickSpellEffect", quick);
        ArrayList<String> ritual = new ArrayList<>();
        for (int j = 0; j < ritualSpellEffects.size(); j++) {
            ritual.add(ritualSpellEffects.get(j).toString());
        }
        hashMap.put("RitualSpellEffect", ritual);
        ArrayList<String> userReply = new ArrayList<>();
        for (int j = 0; j < userReplyForActivations.size(); j++) {
            userReply.add(userReplyForActivations.get(j).toString());
        }
        hashMap.put("UserReplyForActivation", userReply);
        SpellCard spellCard = new SpellCard(cardName, cardDescription, valueOf, notApplicable, numberOfAllowedUsages
            , numberOfTurnsForActivationSpell, i, hashMap, cardImage, monsterCardFamilies, finalIntegers);

        Storage.saveNewImagesOfCardsInFile(spellCard, imagePath);
        Storage.addCardToNewCardsCrated(spellCard);
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

    private void addLogicalActivationRequirement() {
        if (equipSpellEffects.contains(EquipSpellEffect.FIEND_OR_SPELLCASTER_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_FIEND_OR_SPELLCASTER_MONSTER);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_FIEND_OR_SPELLCASTER_MONSTER_OWNER_CONTROLS)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_FIEND_OR_SPELLCASTER_MONSTER_OWNER_CONTROLS);
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK) ||
            equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS)) {

            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK) ||
            equipSpellEffects.contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_WARRIOR_MONSTER);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS);
            }
        }
        if (quickSpellEffects.contains(QuickSpellEffect.TRAP_CARD_INFLICTING_DAMAGE_IS_ACTIVATED_SET_DAMAGE_OF_TRAP_CARD_TO_0)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.TRAP_CARD_INFLICTING_DAMAGE_MUST_BE_ACTIVATED);
        } else if (quickSpellEffects.contains(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_SPELL_TRAP_CARD_IN_FIELD)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_TRAP_CARD_IN_FIELD);
            }
        }
        if (quickSpellEffects.contains(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
            if (!userReplyForActivations.contains(UserReplyForActivation.DISCARD_1_CARD)) {
                userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);
            }
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD);
            }
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_ZONE_SLOT_EMPTY);
            logicalActivationRequirements.add(LogicalActivationRequirement.OPPONENT_MUST_CONTROL_AT_LEAST_1_MONSTER);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS);
            }
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.ADD_SPELL_FIELD_CARD_FROM_DECK_TO_HAND)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_ONE_SPELL_FIELD_CARD_IN_DECK);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK);
            }
        }
        if (normalSpellCardEffects.contains(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_1_MONSTER_IN_EITHER_GY);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY);
            }
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
            }
        }
        if (ritualSpellEffects.contains(RitualSpellEffect.SEND_NORMAL_MONSTERS_WITH_SUM_OF_LEVELS_EQUAL_TO_MONSTERS_LEVEL_FROM_DECK_TO_GRAVEYARD)
            || ritualSpellEffects.contains(RitualSpellEffect.RITUAL_SUMMON_CHOSEN_MONSTER_FROM_HAND)) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_RITUAL_MONSTER_IN_HAND);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_CARDS_WITH_SUM_OF_LEVELS_AT_LEAST_RITUAL_MONSTERS_LEVEL_IN_DECK);
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_NORMAL_MONSTERS_FROM_YOUR_DECK_WITH_SUM_OF_LEVELS_EQUAL_TO_A_RITUAL_MONSTER_LEVEL)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_NORMAL_MONSTERS_FROM_YOUR_DECK_WITH_SUM_OF_LEVELS_EQUAL_TO_A_RITUAL_MONSTER_LEVEL);
            }
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_ONE_RITUAL_MONSTER_FROM_YOUR_HAND_WITH_LEVEL_EQUAL_TO_SUM_OF_LEVELS_YOU_CHOSE)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_RITUAL_MONSTER_FROM_YOUR_HAND_WITH_LEVEL_EQUAL_TO_SUM_OF_LEVELS_YOU_CHOSE);
            }
            if (!userReplyForActivations.contains(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER)) {
                userReplyForActivations.add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
            }
        }
    }

}
