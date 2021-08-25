package project.model.cardData.MonsterCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.image.Image;
import project.model.MonsterEffectEnums.AttackerEffect;
import project.model.MonsterEffectEnums.BeingAttackedEffect;
import project.model.MonsterEffectEnums.ContinuousMonsterEffect;
import project.model.MonsterEffectEnums.FlipEffect;
import project.model.MonsterEffectEnums.OptionalMonsterEffect;
import project.model.MonsterEffectEnums.SentToGraveyardEffect;
import project.model.MonsterEffectEnums.SummoningRequirement;
import project.model.MonsterEffectEnums.UponSummoningEffect;
import project.model.SpellEffectEnums.EquipSpellEffect;
import project.model.SpellEffectEnums.EquipSpellExtendedEffect;
import project.model.SpellEffectEnums.FieldSpellEffect;
import project.model.SpellEffectEnums.FieldSpellExtendedEffect;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;

public class MonsterCard extends Card {

    private int attackPower;
    private int defensePower;
    private int attackPowerConsideringEffects;
    private int defensePowerConsideringEffects;
    private int level;
    private boolean cardPositionChanged;
    private boolean hasCardAlreadyAttacked;
    private boolean oncePerTurnCardEffectUsed;
    private MonsterCardAttribute monsterCardAttribute;
    private MonsterCardFamily monsterCardFamily;
    private MonsterCardValue monsterCardValue;
    private ArrayList<SummoningRequirement> summoningRequirements;
    private ArrayList<UponSummoningEffect> uponSummoningEffects;
    private ArrayList<AttackerEffect> attackerEffects;
    private ArrayList<BeingAttackedEffect> beingAttackedEffects;
    private ArrayList<ContinuousMonsterEffect> continuousMonsterEffects;
    private ArrayList<FlipEffect> flipEffects;
    private ArrayList<OptionalMonsterEffect> optionalMonsterEffects;
    private ArrayList<SentToGraveyardEffect> sentToGraveyardEffects;
    private ArrayList<EquipSpellEffect> equipSpellEffects;
    private ArrayList<FieldSpellEffect> fieldSpellEffects;
    private ArrayList<EquipSpellExtendedEffect> equipSpellExtendedEffects;
    private ArrayList<FieldSpellExtendedEffect> fieldSpellExtendedEffects;
    private boolean shouldATKBeIncreasedOnlyThisTurn;
    private boolean shouldATKBeHalvedOnlyThisTurn;

    public void addEquipSpellExtendedEffects(EquipSpellExtendedEffect equipSpellExtendedEffect) {
        this.equipSpellExtendedEffects.add(equipSpellExtendedEffect);
    }

    public void removeEquipSpellExtendedEffect(EquipSpellExtendedEffect equipSpellExtendedEffect) {
        this.equipSpellEffects.remove(equipSpellExtendedEffect);
    }

    public void addFieldSpellExtendedEffects(FieldSpellExtendedEffect fieldSpellExtendedEffect) {
        this.fieldSpellExtendedEffects.add(fieldSpellExtendedEffect);
    }

    public MonsterCard(int attackPower, int defensePower, int level, MonsterCardAttribute attribute,
                       MonsterCardFamily monsterCardFamily, MonsterCardValue monsterCardValue, String cardName,
                       String cardDescription, CardPosition cardPosition, int numberOfAllowedUsages, int cardPrice,
                       HashMap<String, List<String>> enumValues, Image image) {
        super(cardName, CardType.MONSTER, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice, image);
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.attackPowerConsideringEffects = 0;
        this.defensePowerConsideringEffects = 0;
        this.level = level;
        this.oncePerTurnCardEffectUsed = false;
        this.cardPositionChanged = false;
        this.hasCardAlreadyAttacked = false;
        this.shouldATKBeHalvedOnlyThisTurn = false;
        this.shouldATKBeIncreasedOnlyThisTurn = false;
        this.monsterCardAttribute = attribute;
        this.monsterCardFamily = monsterCardFamily;
        this.monsterCardValue = monsterCardValue;
        // System.out.println(123);
        summoningRequirements = new ArrayList<>();
        uponSummoningEffects = new ArrayList<>();
        attackerEffects = new ArrayList<>();
        beingAttackedEffects = new ArrayList<>();
        continuousMonsterEffects = new ArrayList<>();
        flipEffects = new ArrayList<>();
        optionalMonsterEffects = new ArrayList<>();
        sentToGraveyardEffects = new ArrayList<>();
        equipSpellEffects = new ArrayList<>();
        fieldSpellEffects = new ArrayList<>();
        this.equipSpellExtendedEffects = new ArrayList<>();
        if (cardDescription.equals("")) {
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            if (level == 5 || level == 6) {
                summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
                summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
            } else if (level == 7 || level == 8) {
                summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
                summoningRequirements.add(SummoningRequirement.TRIBUTE_2_MONSTERS);
            } else if (level >= 9) {
                summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
                summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
            }
        } else if (cardDescription.equals("b")) {
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
            summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
            summoningRequirements.add(
                SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            summoningRequirements
                .add(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_SET);
            uponSummoningEffects.add(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS);
        } else if (cardDescription.equals("c")) {

        } else if (cardDescription.equals("r")) {
            summoningRequirements.add(SummoningRequirement.CAN_BE_RITUAL_SUMMONED);
        }
        if (cardName.equals("Beast King Barbaros")) {
            summoningRequirements.add(
                SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            summoningRequirements
                .add(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_SET);
            uponSummoningEffects.add(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS);
        }
        if (cardName.equals("Terratiger, the Empowered Warrior")) {
            uponSummoningEffects.add(
                UponSummoningEffect.IF_NORMAL_SUMMONED_SPECIAL_SUMMON_1_LEVEL_4_OR_LESS_NORMAL_MONSTER_FROM_HAND_IN_DEFENSE_POSITION);
        }
        if (cardName.equals("The Tricky")) {
            summoningRequirements.add(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED);
            // summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
            summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
            summoningRequirements.add(SummoningRequirement.DISCARD_1_CARD);
        } else if (cardName.equals("Gate Guardian")) {
            summoningRequirements.add(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
        } else if (cardName.equals("Exploder Dragon")) {
            beingAttackedEffects.add(BeingAttackedEffect.NEITHER_PLAYER_RECEIVES_BATTLE_DAMAGE_IF_MONSTER_DIES);
            beingAttackedEffects
                .add(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD);
        } else if (cardName.equals("Yomi Ship")) {
            beingAttackedEffects.add(BeingAttackedEffect.IF_DESTROYED_AND_SENT_TO_GRAVEYARD_SEND_ATTACKING_MONSTER_TO_GRAVEYARD);
        } else if (cardName.equals("Marshmallon")) {
            beingAttackedEffects.add(BeingAttackedEffect.CANNOT_BE_DESTROYED_BY_BATTLE);
            beingAttackedEffects.add(BeingAttackedEffect.IF_FACE_DOWN_AT_THE_BEGINNING_THEN_OPPONENT_RECEIVES_1000_DAMAGE);
        } else if (cardName.equals("Texchanger")) {
            beingAttackedEffects.add(BeingAttackedEffect.NEGATE_ATTACK_ONCE_PER_TURN);
            beingAttackedEffects.add(BeingAttackedEffect.SPECIAL_SUMMON_CYBERSE_NORMAL_MONSTER_FROM_HAND_GV_DECK_ONCE_PER_TURN);
        } else if (cardName.equals("Man-Eater Bug")) {
            flipEffects.add(FlipEffect.DESTROY_1_MONSTER_ON_THE_FIELD);
        } else if (cardName.equals("Suijin")) {
            beingAttackedEffects.add(BeingAttackedEffect.SET_ATTACKING_MONSTER_ATK_TO_0_ONCE_PER_TURN);
        } else if (cardName.equals("Herald Of Creation")) {
            optionalMonsterEffects.add(
                OptionalMonsterEffect.ONCE_PER_TURN_DISCARD_1_CARD_SEND_LEVEL_7_OR_MORE_MONSTER_FROM_GY_TO_HAND);
        } else if (cardName.equals("Scanner")) {
            optionalMonsterEffects.add(
                OptionalMonsterEffect.ONCE_PER_TURN_CHOOSE_A_MONSTER_IN_YOUR_OPPONENTS_GRAVEYARD_AND_COPY_ALL_CHARACTERISTICS_UNTIL_THE_END_OF_THAT_TURN);
        } else if (cardName.equals("The Calculator")) {
            continuousMonsterEffects.add(
                ContinuousMonsterEffect.ATK_IS_SET_300_MULTIPLIED_BY_TOTAL_OF_FACE_UP_MONSTER_LEVELS_YOU_CONTROL);
        } else if (cardName.equals("Mirage Dragon")) {
            continuousMonsterEffects
                .add(ContinuousMonsterEffect.OPPONENT_CANNOT_ACTIVATE_TRAP_CARDS_WHILE_THIS_CARD_IS_FACE_UP);
        } else if (cardName.equals("Command Knight")) {
            continuousMonsterEffects.add(ContinuousMonsterEffect.ALL_MONSTERS_OWNER_CONTROLS_GAIN_400_ATK);
            continuousMonsterEffects.add(ContinuousMonsterEffect.CANNOT_BE_ATTACKED_IF_YOU_CONTROL_ANOTHER_MONSTER);
        } else if (cardName.equals("Caius The Shadow Monarch")) {
            summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
            uponSummoningEffects.add(UponSummoningEffect.SELECT_1_CARD_ON_THE_FIELD_AND_DESTROY);
        }
        if (enumValues != null) {
            setEnumValues(enumValues);
        }

    }

    public MonsterCard(MonsterCard monster) {
        super(monster.getCardName(), CardType.MONSTER, monster.getCardDescription(), monster.getCardPosition(),
            monster.getNumberOfAllowedUsages(), monster.getCardPrice(), monster.getImage());
        this.attackPower = monster.getAttackPower();
        this.defensePower = monster.getDefensePower();
        this.attackPowerConsideringEffects = 0;
        this.defensePowerConsideringEffects = 0;
        this.level = monster.getLevel();
        this.oncePerTurnCardEffectUsed = monster.isOncePerTurnCardEffectUsed();
        this.cardPositionChanged = false;
        this.hasCardAlreadyAttacked = false;
        this.monsterCardAttribute = monster.getMonsterCardAttribute();
        this.monsterCardFamily = monster.getMonsterCardFamily();
        this.monsterCardValue = monster.getMonsterCardValue();
        this.summoningRequirements = monster.getSummoningRequirements();
        this.uponSummoningEffects = monster.getUponSummoningEffects();
        this.attackerEffects = monster.getAttackerEffects();
        this.beingAttackedEffects = monster.getBeingAttackedEffects();
        this.continuousMonsterEffects = monster.getContinuousMonsterEffects();
        this.flipEffects = monster.getFlipEffects();
        this.optionalMonsterEffects = monster.getOptionalMonsterEffects();
        this.sentToGraveyardEffects = monster.getSentToGraveyardEffects();
        this.equipSpellEffects = monster.getEquipSpellEffects();
        this.fieldSpellEffects = monster.getFieldSpellEffects();
        this.equipSpellExtendedEffects = monster.getEquipSpellExtendedEffects();
    }

    public MonsterCardValue getMonsterCardValue() {
        return monsterCardValue;
    }

    public MonsterCardFamily getMonsterCardFamily() {
        return monsterCardFamily;
    }

    public MonsterCardAttribute getMonsterCardAttribute() {
        return monsterCardAttribute;
    }

    public int getLevel() {
        return level;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getAttackPowerConsideringEffects() {
        return attackPowerConsideringEffects;
    }

    public int getDefensePowerConsideringEffects() {
        return defensePowerConsideringEffects;
    }

    public boolean isOncePerTurnCardEffectUsed() {
        return oncePerTurnCardEffectUsed;
    }

    public boolean isCardPositionChanged() {
        return cardPositionChanged;
    }

    public boolean isHasCardAlreadyAttacked() {
        return hasCardAlreadyAttacked;
    }

    public boolean isShouldATKBeIncreasedOnlyThisTurn() {
        return shouldATKBeIncreasedOnlyThisTurn;
    }

    public boolean isShouldATKBeHalvedOnlyThisTurn() {
        return shouldATKBeHalvedOnlyThisTurn;
    }

    public void setCardPositionChanged(boolean cardPositionChanged) {
        this.cardPositionChanged = cardPositionChanged;
    }

    public void setOncePerTurnCardEffectUsed(boolean oncePerTurnCardEffectUsed) {
        this.oncePerTurnCardEffectUsed = oncePerTurnCardEffectUsed;
    }

    public void setHasCardAlreadyAttacked(boolean hasCardAlreadyAttacked) {
        this.hasCardAlreadyAttacked = hasCardAlreadyAttacked;
    }

    public void setShouldATKBeIncreasedOnlyThisTurn(boolean shouldATKBeIncreasedOnlyThisTurn) {
        this.shouldATKBeIncreasedOnlyThisTurn = shouldATKBeIncreasedOnlyThisTurn;
    }

    public void setShouldATKBeHalvedOnlyThisTurn(boolean shouldATKBeHalvedOnlyThisTurn) {
        this.shouldATKBeHalvedOnlyThisTurn = shouldATKBeHalvedOnlyThisTurn;
    }

    public ArrayList<SummoningRequirement> getSummoningRequirements() {
        return summoningRequirements;
    }

    public ArrayList<UponSummoningEffect> getUponSummoningEffects() {
        return uponSummoningEffects;
    }

    public ArrayList<AttackerEffect> getAttackerEffects() {
        return attackerEffects;
    }

    public ArrayList<BeingAttackedEffect> getBeingAttackedEffects() {
        return beingAttackedEffects;
    }

    public ArrayList<ContinuousMonsterEffect> getContinuousMonsterEffects() {
        return continuousMonsterEffects;
    }

    public ArrayList<FlipEffect> getFlipEffects() {
        return flipEffects;
    }

    public ArrayList<OptionalMonsterEffect> getOptionalMonsterEffects() {
        return optionalMonsterEffects;
    }

    public ArrayList<SentToGraveyardEffect> getSentToGraveyardEffects() {
        return sentToGraveyardEffects;
    }

    public ArrayList<EquipSpellEffect> getEquipSpellEffects() {
        return equipSpellEffects;
    }

    public ArrayList<EquipSpellExtendedEffect> getEquipSpellExtendedEffects() {
        return equipSpellExtendedEffects;
    }

    public ArrayList<FieldSpellEffect> getFieldSpellEffects() {
        return fieldSpellEffects;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setDefensePower(int defensePower) {
        this.defensePower = defensePower;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMonsterCardFamily(MonsterCardFamily monsterCardFamily) {
        this.monsterCardFamily = monsterCardFamily;
    }

    public void setMonsterCardValue(MonsterCardValue monsterCardValue) {
        this.monsterCardValue = monsterCardValue;
    }

    public void setSummoningRequirements(ArrayList<SummoningRequirement> summoningRequirements) {
        this.summoningRequirements = summoningRequirements;
    }

    public void setUponSummoningEffects(ArrayList<UponSummoningEffect> uponSummoningEffects) {
        this.uponSummoningEffects = uponSummoningEffects;
    }

    public void setBeingAttackedEffects(ArrayList<BeingAttackedEffect> beingAttackedEffects) {
        this.beingAttackedEffects = beingAttackedEffects;
    }

    public void setContinuousMonsterEffects(ArrayList<ContinuousMonsterEffect> continuousMonsterEffects) {
        this.continuousMonsterEffects = continuousMonsterEffects;
    }

    public void setFlipEffects(ArrayList<FlipEffect> flipEffects) {
        this.flipEffects = flipEffects;
    }

    public void setOptionalMonsterEffects(ArrayList<OptionalMonsterEffect> optionalMonsterEffects) {
        this.optionalMonsterEffects = optionalMonsterEffects;
    }

    public void setSentToGraveyardEffects(ArrayList<SentToGraveyardEffect> sentToGraveyardEffects) {
        this.sentToGraveyardEffects = sentToGraveyardEffects;
    }

    public void setAttackerEffects(ArrayList<AttackerEffect> attackerEffects) {
        this.attackerEffects = attackerEffects;
    }

    public void addEquipSpellEffectToList(EquipSpellEffect equipSpellEffect) {
        this.equipSpellEffects.add(equipSpellEffect);
    }

    public void removeEquipSpellEffectFromList(EquipSpellEffect equipSpellEffect) {
        this.equipSpellEffects.remove(equipSpellEffect);
    }

    public void addSpellFieldEffectToList(FieldSpellEffect fieldSpellEffect) {
        this.fieldSpellEffects.add(fieldSpellEffect);
    }

    public void removeSpellFieldEffectFromList(FieldSpellEffect fieldSpellEffect) {
        this.fieldSpellEffects.remove(fieldSpellEffect);
    }

    public void clearEquipSpellEffect() {
        this.equipSpellEffects.clear();
    }

    public void clearFieldSpellEffect() {
        this.fieldSpellEffects.clear();
    }

    private void setEnumValues(HashMap<String, List<String>> enumValues) {

        //I commented this line because it made error during creating new  card in CardCreatorController
//        for (int i = 1; i < enumValues.get("AttackerEffect").size(); i++) {
//            attackerEffects.add(AttackerEffect.valueOf(enumValues.get("AttackerEffects").get(i)));
//        }

        for (int i = 1; i < enumValues.get("BeingAttackedEffect").size(); i++) {
            beingAttackedEffects.add(BeingAttackedEffect.valueOf(enumValues.get("BeingAttackedEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("ContinuousMonsterEffect").size(); i++) {
            continuousMonsterEffects
                .add(ContinuousMonsterEffect.valueOf(enumValues.get("ContinuousMonsterEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("FlipEffect").size(); i++) {
            flipEffects.add(FlipEffect.valueOf(enumValues.get("FlipEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("OptionalMonsterEffect").size(); i++) {
            optionalMonsterEffects.add(OptionalMonsterEffect.valueOf(enumValues.get("OptionalMonsterEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("SentToGraveyardEffect").size(); i++) {
            sentToGraveyardEffects.add(SentToGraveyardEffect.valueOf(enumValues.get("SentToGraveyardEffect").get(i)));
        }

        for (int i = 1; i < enumValues.get("SummoningRequirement").size(); i++) {
            summoningRequirements.add(SummoningRequirement.valueOf(enumValues.get("SummoningRequirement").get(i)));
        }

        for (int i = 1; i < enumValues.get("UponSummoningEffect").size(); i++) {
            uponSummoningEffects.add(UponSummoningEffect.valueOf(enumValues.get("UponSummoningEffect").get(i)));
        }

    }

    public static int giveATKDEFConsideringEffects(String string, CardLocation cardLocation, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        System.out.println("monster card more info: cardLocation is: " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex());
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        int finalAttackPower = monsterCard.getAttackPower();
        int finalDefensePower = monsterCard.getDefensePower();
        finalAttackPower += giveChangesInATKDEFConsideringContinuousMonsterEffect(cardLocation, token, "attack");
        finalDefensePower += giveChangesInATKDEFConsideringContinuousMonsterEffect(cardLocation, token, "defense");
        finalAttackPower += EquipSpellExtendedEffect.giveChangesOnATKDEFConsideringEquipSpellEffects(cardLocation, token, "attack");
        finalDefensePower += EquipSpellExtendedEffect.giveChangesOnATKDEFConsideringEquipSpellEffects(cardLocation, token, "defense");
        CardLocation cardLocationOfFirstSpellFieldCard = giveLocationOfPossibleSpellFieldCard(token, 1);
        CardLocation cardLocationOfSecondSpellFieldCard = giveLocationOfPossibleSpellFieldCard(token, 2);
        if (cardLocationOfFirstSpellFieldCard != null) {
            System.out.println("ally has spell field card %%%");
            finalAttackPower += FieldSpellExtendedEffect.giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
                cardLocationOfFirstSpellFieldCard, token, "attack");
            finalDefensePower += FieldSpellExtendedEffect.giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
                cardLocationOfFirstSpellFieldCard, token, "defense");
        }
        if (cardLocationOfSecondSpellFieldCard != null) {
            System.out.println("opponent has spell field card %%%");
            finalAttackPower += FieldSpellExtendedEffect.giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
                cardLocationOfSecondSpellFieldCard, token, "attack");
            finalDefensePower += FieldSpellExtendedEffect.giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
                cardLocationOfSecondSpellFieldCard, token, "defense");

        }
        finalAttackPower += giveChangesInATKDEFConsideringOtherContinuousMonsterEffects("attack", cardLocation, token);
        finalDefensePower += giveChangesInATKDEFConsideringOtherContinuousMonsterEffects("defense", cardLocation,
            token);
        if (monsterCard.shouldATKBeIncreasedOnlyThisTurn) {
            finalAttackPower += 700;
        }
        if (monsterCard.shouldATKBeHalvedOnlyThisTurn) {
            finalAttackPower = finalAttackPower / 2;
        }
        if (string.equals("attack")) {
            return finalAttackPower;
        }
        if (string.equals("defense")) {
            return finalDefensePower;
        }
        return 0;
    }


    private static int giveChangesInATKDEFConsideringContinuousMonsterEffect(CardLocation cardLocation, String token, String attackOrDefense) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = monsterCard.getContinuousMonsterEffects();
        int finalAttackPower = 0;
        int finalDefensePower = 0;
        if (continuousMonsterEffects.contains(
            ContinuousMonsterEffect.ATK_IS_SET_300_MULTIPLIED_BY_TOTAL_OF_FACE_UP_MONSTER_LEVELS_YOU_CONTROL)) {
            int sumOfLevels = 0;
            if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                for (int i = 0; i < 5; i++) {
                    Card card = duelBoard
                        .getCardByCardLocation(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1));
                    if (Card.isCardAMonster(card)
                        && (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
                        || card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
                        sumOfLevels += ((MonsterCard) card).getLevel();
                    }
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    Card card = duelBoard
                        .getCardByCardLocation(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1));
                    if (Card.isCardAMonster(card)
                        && (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
                        || card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
                        sumOfLevels += ((MonsterCard) card).getLevel();
                    }
                }
            }
            finalAttackPower += 300 * sumOfLevels;
        }
        if (attackOrDefense.equals("attack")) {
            return finalAttackPower;
        }
        return finalDefensePower;
    }

    private static int giveChangesInATKDEFConsideringOtherContinuousMonsterEffects(String string,
                                                                                   CardLocation cardLocation, String token) {
        if (string.equals("defense")) {
            return 0;
        }
        int attackChanges = 0;
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        ArrayList<Card> allyMonsterCards = duelBoard.getAllyMonsterCards();
        ArrayList<Card> opponentMonsterCards = duelBoard.getOpponentMonsterCards();
        if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            attackChanges += giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(allyMonsterCards, token);
        } else if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            attackChanges += giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(opponentMonsterCards, token);
        }
        return attackChanges;
    }

    private static int giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(ArrayList<Card> monsterCards, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        int attackChanges = 0;
        for (int i = 0; i < monsterCards.size(); i++) {
            CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1);
            MonsterCard sampleMonsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocationOfAllyMonster);
            if (sampleMonsterCard != null) {
                ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = sampleMonsterCard
                    .getContinuousMonsterEffects();
                if (continuousMonsterEffects.contains(ContinuousMonsterEffect.ALL_MONSTERS_OWNER_CONTROLS_GAIN_400_ATK)
                    && (sampleMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)
                    || sampleMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
                    attackChanges += 400;
                }
            }
        }
        return attackChanges;
    }

    private static CardLocation giveLocationOfPossibleSpellFieldCard(String token, int turn) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        Card possibleCard = null;
        CardLocation possibleCardLocation = null;
        if (turn == 1) {
            possibleCardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        } else {
            possibleCardLocation = new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        }
        possibleCard = duelBoard.getCardByCardLocation(possibleCardLocation);
        if (!Card.isCardASpell(possibleCard)) {
            return null;
        }
        return possibleCardLocation;
    }

    @Override
    public Object clone() {
        return new MonsterCard(this);
    }

    public String[] toCSVFormatString() {
        List<String> csvArray = new ArrayList<>();
        csvArray.add(cardName);
        csvArray.add(level + "");
        csvArray.add(monsterCardAttribute + "");
        csvArray.add(monsterCardFamily + "");
        csvArray.add(monsterCardValue + "");
        csvArray.add(attackPower + "");
        csvArray.add(defensePower + "");
        csvArray.add(cardDescription);
        csvArray.add(cardPrice + "");
        csvArray.add(toCSVformatEffectsOfCards(attackerEffects));
        csvArray.add(toCSVformatEffectsOfCards(beingAttackedEffects));
        csvArray.add(toCSVformatEffectsOfCards(continuousMonsterEffects));
        csvArray.add(toCSVformatEffectsOfCards(flipEffects));
        csvArray.add(toCSVformatEffectsOfCards(optionalMonsterEffects));
        csvArray.add(toCSVformatEffectsOfCards(sentToGraveyardEffects));
        csvArray.add(toCSVformatEffectsOfCards(summoningRequirements));
        csvArray.add(toCSVformatEffectsOfCards(uponSummoningEffects));
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
