package project.model.cardData.SpellCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.cell.ProgressBarTreeTableCell;
import javafx.scene.image.Image;
import project.model.MonsterEffectEnums.SentToGraveyardEffect;
import project.model.SpellEffectEnums.*;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCardFamily;

public class SpellCard extends Card {
    private int highestNumberOfTurnsOfActivation;//
    private int numberOfTurnsForActivation;
    private boolean oncePerTurnCardEffectUsed;
    private SpellCardValue spellCardValue;
    private ArrayList<ContinuousSpellCardEffect> continuousSpellCardEffects;
    private ArrayList<FieldSpellExtendedEffect> fieldSpellExtendedEffects;
    private ArrayList<EquipSpellExtendedEffect> equipSpellExtendedEffects;
    private ArrayList<LogicalActivationRequirement> logicalActivationRequirements;//
    private ArrayList<NormalSpellCardEffect> normalSpellCardEffects;
    private ArrayList<QuickSpellEffect> quickSpellEffects;
    private ArrayList<RitualSpellEffect> ritualSpellEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;//
    private ArrayList<UserReplyForActivation> userReplyForActivations;
    private ArrayList<CardLocation> cardLocationsToWhichEquipSpellIsApplied;//

    public SpellCard(String cardName, String cardDescription, SpellCardValue spellCardValue, CardPosition cardPosition,
            int numberOfAllowedUsages, int numberOfTurnsForActivation, int cardPrice,
            HashMap<String, List<String>> enumValues, Image image, ArrayList<MonsterCardFamily> monsterCardFamilies,
            ArrayList<Integer> integers) {
        super(cardName, CardType.SPELL, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice, image);
        this.highestNumberOfTurnsOfActivation = numberOfTurnsForActivation;
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
        this.oncePerTurnCardEffectUsed = false;
        this.spellCardValue = spellCardValue;
        this.continuousSpellCardEffects = new ArrayList<>();
        this.equipSpellExtendedEffects = new ArrayList<>();
        this.fieldSpellExtendedEffects = new ArrayList<>();
        this.logicalActivationRequirements = new ArrayList<>();
        this.normalSpellCardEffects = new ArrayList<>();
        this.quickSpellEffects = new ArrayList<>();
        this.ritualSpellEffects = new ArrayList<>();
        this.sentToGraveyardEffects = new ArrayList<>();
        this.userReplyForActivations = new ArrayList<>();
        this.cardLocationsToWhichEquipSpellIsApplied = new ArrayList<>();
        if (cardName.equals("Dark Hole")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_MONSTERS_ON_THE_FIELD);
        } else if (cardName.equals("Harpie's Feather Duster")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_SPELL_AND_TRAP_CARDS);
        } else if (cardName.equals("Raigeki")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DESTROY_ALL_OF_OPPONENTS_MONSTERS);
        } else if (cardName.equals("Sword Of Dark Destruction")) {
            // equipSpellEffects.add(EquipSpellEffect.FIEND_OR_SPELLCASTER_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.OWNER_MUST_CONTROL_FIEND_OR_SPELLCASTER_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_FIEND_OR_SPELLCASTER_MONSTER_OWNER_CONTROLS);
        } else if (cardName.equals("Black Pendant")) {
            // equipSpellEffects.add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
        } else if (cardName.equals("United We Stand")) {
            // equipSpellEffects
            // .add(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_AT_LEAST_1_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_OWNER_CONTROLS);
        } else if (cardName.equals("Magnum Shield")) {
            // equipSpellEffects.add(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK);
            // equipSpellEffects.add(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_CONTROL_WARRIOR_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_WARRIOR_MONSTER_OWNER_CONTROLS);
        } else if (cardName.equals("Umiiruka")) {
            // fieldSpellEffects.add(FieldSpellEffect.AQUA_GAINS_500_ATK);
            // fieldSpellEffects.add(FieldSpellEffect.AQUA_LOSES_400_DEF);
        } else if (cardName.equals("Closed Forest")) {
            // fieldSpellEffects.add(FieldSpellEffect.BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY);
        } else if (cardName.equals("Forest")) {
            // fieldSpellEffects.add(FieldSpellEffect.INSECT_GAIN_200_ATK_DEF);
            // fieldSpellEffects.add(FieldSpellEffect.BEAST_GAIN_200_ATK_DEF);
            // fieldSpellEffects.add(FieldSpellEffect.BEASTWARRIOR_GAIN_200_ATK_DEF);
        } else if (cardName.equals("Yami")) {
            // fieldSpellEffects.add(FieldSpellEffect.FIEND_GAIN_200_ATK_DEF);
            // fieldSpellEffects.add(FieldSpellEffect.SPELLCASTER_GAIN_200_ATK_DEF);
            // fieldSpellEffects.add(FieldSpellEffect.FAIRY_LOSE_200_ATK_DEF);
        } else if (cardName.equals("Ring Of Defense")) {
            quickSpellEffects
                    .add(QuickSpellEffect.TRAP_CARD_INFLICTING_DAMAGE_IS_ACTIVATED_SET_DAMAGE_OF_TRAP_CARD_TO_0);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.TRAP_CARD_INFLICTING_DAMAGE_MUST_BE_ACTIVATED);
        } else if (cardName.equals("Mystical Space Typhoon")) {
            quickSpellEffects.add(QuickSpellEffect.TARGET_1_SPELL_TRAP_CARD_AND_DESTROY);
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_TRAP_CARD_IN_FIELD);
        } else if (cardName.equals("Twin Twisters")) {
            quickSpellEffects.add(QuickSpellEffect.DISCARD_1_CARD_THEN_TARGET_UP_TO_2_SPELL_CARDS_AND_DESTROY);
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_ONE_SPELL_CARD_IN_FIELD);
            userReplyForActivations.add(UserReplyForActivation.DISCARD_1_CARD);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_UP_TO_TWO_SPELL_TRAP_CARDS_IN_FIELD);
        } else if (cardName.equals("Messenger Of Peace")) {
            continuousSpellCardEffects.add(ContinuousSpellCardEffect.MONSTERS_WITH_1500_OR_MORE_ATK_CANT_ATTACK);
            continuousSpellCardEffects.add(ContinuousSpellCardEffect.STANDBY_PHASE_PAY_100_LP_OR_DESTROY_CARD);
        } else if (cardName.equals("Spell Absorption")) {
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.IF_A_SPELL_IS_ACTIVATED_OWNER_GAINS_500_LIFE_POINTS);
        } else if (cardName.equals("Supply Squad")) {
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.IF_A_MONSTER_OWNER_CONTROLS_IS_DESTROYED_DRAW_1_CARD);
        } else if (cardName.equals("Pot Of Greed")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.DRAW_2_CARDS);
        } else if (cardName.equals("Change Of Heart")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.CONTROL_ONE_OF_OPPONENTS_MONSTERS_UNTIL_END_PHASE);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.OWNER_MUST_HAVE_AT_LEAST_ONE_MONSTER_ZONE_SLOT_EMPTY);
            logicalActivationRequirements.add(LogicalActivationRequirement.OPPONENT_MUST_CONTROL_AT_LEAST_1_MONSTER);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_OF_OPPONENTS_MONSTERS);
        } else if (cardName.equals("Swords Of Revealing Light")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.FLIP_OPPONENT_MONSTER_CARDS);
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.UNTIL_THIS_CARD_IS_FACE_UP_ON_FIELD_OPPONENT_MONSTERS_CANT_ATTACK);
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.NUMBER_OF_TURNS_OF_ACTIVATION_REDUCES_BY_1_IN_EACH_TURN);
        } else if (cardName.equals("Terraforming")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.ADD_SPELL_FIELD_CARD_FROM_DECK_TO_HAND);
            logicalActivationRequirements
                    .add(LogicalActivationRequirement.OWNER_MUST_HAVE_ONE_SPELL_FIELD_CARD_IN_DECK);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_SPELL_FIELD_CARD_FROM_OWNER_DECK);
        } else if (cardName.equals("Monster Reborn")) {
            normalSpellCardEffects.add(NormalSpellCardEffect.SPECIAL_SUMMON_MONSTER_FROM_EITHER_GY);
            logicalActivationRequirements.add(LogicalActivationRequirement.MUST_EXIST_AT_LEAST_1_MONSTER_IN_EITHER_GY);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_MONSTER_FROM_EITHER_GY);
            userReplyForActivations
                    .add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
        } else if (cardName.equals("Advanced Ritual Art")) {
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_RITUAL_MONSTER_IN_HAND);
            logicalActivationRequirements.add(
                    LogicalActivationRequirement.OWNER_MUST_HAVE_CARDS_WITH_SUM_OF_LEVELS_AT_LEAST_RITUAL_MONSTERS_LEVEL_IN_DECK);
            ritualSpellEffects.add(
                    RitualSpellEffect.SEND_NORMAL_MONSTERS_WITH_SUM_OF_LEVELS_EQUAL_TO_MONSTERS_LEVEL_FROM_DECK_TO_GRAVEYARD);
            ritualSpellEffects.add(RitualSpellEffect.RITUAL_SUMMON_CHOSEN_MONSTER_FROM_HAND);
            userReplyForActivations.add(
                    UserReplyForActivation.CHOOSE_NORMAL_MONSTERS_FROM_YOUR_DECK_WITH_SUM_OF_LEVELS_EQUAL_TO_A_RITUAL_MONSTER_LEVEL);
            userReplyForActivations.add(
                    UserReplyForActivation.CHOOSE_ONE_RITUAL_MONSTER_FROM_YOUR_HAND_WITH_LEVEL_EQUAL_TO_SUM_OF_LEVELS_YOU_CHOSE);
            userReplyForActivations
                    .add(UserReplyForActivation.CHOOSE_FACE_UP_ATTACK_POSITION_OR_DEFENSE_POSITION_OF_YOUR_MONSTER);
        } else if (cardName.equals("Giant Trunade")){
            normalSpellCardEffects.add(NormalSpellCardEffect.ALL_SPELL_TRAPS_IN_FIELD_GO_TO_RESPECTIVE_HANDS);
        } else if (cardName.equals("Card Destruction")){
            normalSpellCardEffects.add(NormalSpellCardEffect.BOTH_PLAYERS_DISCARD_ALL_CARDS_FROM_HAND_AND_DRAW_CARDS_EQUAL_TO_THROWN_AWAY_CARDS);
        } else if (cardName.equals("Shrink")){
            quickSpellEffects.add(QuickSpellEffect.TARGET_FACE_UP_MONSTER_WILL_HAVE_HALF_ATTACK_UNTIL_THE_END_OF_THIS_TURN);
        } else if (cardName.equals("Rush Recklessly")){
            quickSpellEffects.add(QuickSpellEffect.TARGET_FACE_UP_MONSTER_WILL_GAIN_700_ATTACK_UNTIL_THE_END_OF_THIS_TURN);
        } else if (cardName.equals("Ancient Rules")){
            normalSpellCardEffects.add(NormalSpellCardEffect.SPECIAL_SUMMON_ONE_LEVEL_5_OR_HIGHER_NORMAL_MONSTER_FROM_YOUR_HAND);
            logicalActivationRequirements.add(LogicalActivationRequirement.OWNER_MUST_HAVE_LEVEL_5_OR_HIGHER_NORMAL_MONSTER_IN_HAND);
            userReplyForActivations.add(UserReplyForActivation.CHOOSE_ONE_LEVEL_5_OR_HIGHER_NORMAL_MONSTER_FROM_HAND);
        }
        if (enumValues != null) {
            setEnumValues(enumValues, monsterCardFamilies, integers);
        }
    }

    public SpellCard(SpellCard spellCard) {
        super(spellCard.getCardName(), CardType.SPELL, spellCard.getCardDescription(), spellCard.getCardPosition(),
                spellCard.getNumberOfAllowedUsages(), spellCard.getCardPrice(), spellCard.getImage());
        this.highestNumberOfTurnsOfActivation = spellCard.getHighestNumberOfTurnsOfActivation();
        this.numberOfTurnsForActivation = spellCard.getNumberOfTurnsForActivation();
        this.oncePerTurnCardEffectUsed = false;
        this.spellCardValue = spellCard.getSpellCardValue();
        this.continuousSpellCardEffects = spellCard.getContinuousSpellCardEffects();
        this.equipSpellExtendedEffects = spellCard.getEquipSpellExtendedEffects();
        this.fieldSpellExtendedEffects = spellCard.getFieldSpellExtendedEffects();
        this.logicalActivationRequirements = spellCard.getLogicalActivationRequirements();
        this.normalSpellCardEffects = spellCard.getNormalSpellCardEffects();
        this.quickSpellEffects = spellCard.getQuickSpellEffects();
        this.ritualSpellEffects = spellCard.getRitualSpellEffects();
        this.sentToGraveyardEffects = spellCard.getSentToGraveyardEffects();
        this.userReplyForActivations = spellCard.getUserReplyForActivations();
        this.cardLocationsToWhichEquipSpellIsApplied = spellCard.getCardLocationsToWhichEquipSpellIsApplied();
    }

    public int getHighestNumberOfTurnsOfActivation() {
        return highestNumberOfTurnsOfActivation;
    }

    public int getNumberOfTurnsForActivation() {
        return numberOfTurnsForActivation;
    }

    public boolean isOncePerTurnCardEffectUsed() {
        return oncePerTurnCardEffectUsed;
    }

    public SpellCardValue getSpellCardValue() {
        return spellCardValue;
    }

    public boolean isCardAlreadyActivated() {
        return cardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION);
    }

    public ArrayList<ContinuousSpellCardEffect> getContinuousSpellCardEffects() {
        return continuousSpellCardEffects;
    }

    public ArrayList<LogicalActivationRequirement> getLogicalActivationRequirements() {
        return logicalActivationRequirements;
    }

    public ArrayList<FieldSpellExtendedEffect> getFieldSpellExtendedEffects() {
        return fieldSpellExtendedEffects;
    }

    public ArrayList<EquipSpellExtendedEffect> getEquipSpellExtendedEffects() {
        return equipSpellExtendedEffects;
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

    public ArrayList<CardLocation> getCardLocationsToWhichEquipSpellIsApplied() {
        return cardLocationsToWhichEquipSpellIsApplied;
    }

    public void addCardLocationToEquipSpellAffected(CardLocation cardLocation) {
        cardLocationsToWhichEquipSpellIsApplied.add(cardLocation);
    }

    public void clearCardsToWhichEquipSpellIsApplied() {
        cardLocationsToWhichEquipSpellIsApplied.clear();
    }

    public void setNumberOfTurnsForActivation(int numberOfTurnsForActivation) {
        this.numberOfTurnsForActivation = numberOfTurnsForActivation;
    }

    public void setOncePerTurnCardEffectUsed(boolean oncePerTurnCardEffectUsed) {
        this.oncePerTurnCardEffectUsed = oncePerTurnCardEffectUsed;
    }

    private void setEnumValues(HashMap<String, List<String>> enumValues,
            ArrayList<MonsterCardFamily> monsterCardFamilies, ArrayList<Integer> integers) {

        for (int i = 1; i < enumValues.get("ContinuousSpellCardEffect").size(); i++) {
            continuousSpellCardEffects
                    .add(ContinuousSpellCardEffect.valueOf(enumValues.get("ContinuousSpellCardEffect").get(i)));
        }
        ArrayList<EquipSpellEffect> equipSpellEffects = new ArrayList<>();
        for (int i = 1; i < enumValues.get("EquipSpellEffect").size(); i++) {
            equipSpellEffects.add(EquipSpellEffect.valueOf(enumValues.get("EquipSpellEffect").get(i)));
        }
        equipSpellExtendedEffects.add(
                new EquipSpellExtendedEffect(equipSpellEffects, monsterCardFamilies, integers, this.getCardName()));
        ArrayList<FieldSpellEffect> fieldSpellEffects = new ArrayList<>();
        for (int i = 1; i < enumValues.get("FieldSpellEffect").size(); i++) {
            fieldSpellEffects.add(FieldSpellEffect.valueOf(enumValues.get("FieldSpellEffect").get(i)));
        }
        fieldSpellExtendedEffects.add(
                new FieldSpellExtendedEffect(fieldSpellEffects, monsterCardFamilies, integers, this.getCardName()));

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
    public Object clone() {
        return new SpellCard(this);
    }

    public String[] toCSVFormatString() {
        List<String> csvArray = new ArrayList<>();
        csvArray.add(cardName);
        csvArray.add("Spell");
        csvArray.add(spellCardValue + "");
        csvArray.add(cardDescription + "");
        csvArray.add(numberOfAllowedUsages == 3 ? "Unlimited" : "Limited");
        csvArray.add(cardPrice + "");
        csvArray.add(toCSVformatEffectsOfCards(continuousSpellCardEffects));
        if (equipSpellExtendedEffects != null) {
            if (equipSpellExtendedEffects.get(0) != null) {
                csvArray.add(toCSVformatEffectsOfCards(equipSpellExtendedEffects.get(0).getEquipSpellEffects()));
            } else {
                csvArray.add("#");
            }
            csvArray.add("#");
        } else {
            csvArray.add("#");
            if (fieldSpellExtendedEffects.get(0) != null) {
                csvArray.add(toCSVformatEffectsOfCards(fieldSpellExtendedEffects.get(0).getFieldSpellEffects()));
            } else {
                csvArray.add("#");
            }
        }
        csvArray.add(toCSVformatEffectsOfCards(logicalActivationRequirements));
        csvArray.add(toCSVformatEffectsOfCards(normalSpellCardEffects));
        csvArray.add(toCSVformatEffectsOfCards(sentToGraveyardEffects));
        csvArray.add(toCSVformatEffectsOfCards(quickSpellEffects));
        csvArray.add(toCSVformatEffectsOfCards(ritualSpellEffects));
        csvArray.add(toCSVformatEffectsOfCards(userReplyForActivations));
        if (equipSpellExtendedEffects != null) {
            if (equipSpellExtendedEffects.get(0) != null) {
                csvArray.add(equipSpellExtendedEffects.get(0).getFirstAttack() + "");
            }
        } else if (fieldSpellExtendedEffects != null) {
            if (fieldSpellExtendedEffects.get(0) != null) {
                csvArray.add(fieldSpellExtendedEffects.get(0).getFirstAttack() + "");
            }
        } else {
            csvArray.add(0 + "");
        }

        if (equipSpellExtendedEffects != null) {
            if (equipSpellExtendedEffects.get(0) != null) {
                csvArray.add(toCSVformatEffectsOfCards(equipSpellExtendedEffects.get(0).getFirstMonsterCardFamilies()));
            }
        } else if (fieldSpellExtendedEffects != null) {
            if (fieldSpellExtendedEffects.get(0) != null) {
                csvArray.add(toCSVformatEffectsOfCards(fieldSpellExtendedEffects.get(0).getFirstMonsterCardFamilies()));
            }
        } else {
            csvArray.add("#");
        }
        String[] res = new String[csvArray.size()];
        res = csvArray.toArray(res);
        return res;
    }

    private String toCSVformatEffectsOfCards(ArrayList effects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        if (effects == null) {
            return stringBuilder.toString();
        }
        for (int i = 0; i < effects.size(); i++) {
            stringBuilder.append(effects.get(i));
            if (i + 1 < effects.size()) {
                stringBuilder.append("#");
            }
        }
        return stringBuilder.toString();
    }
}
