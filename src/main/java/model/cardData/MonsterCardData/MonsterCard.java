package model.cardData.MonsterCardData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controller.duel.CardEffects.MonsterEffectEnums.AttackerEffect;
import controller.duel.CardEffects.MonsterEffectEnums.BeingAttackedEffect;
import controller.duel.CardEffects.MonsterEffectEnums.ContinuousMonsterEffect;
import controller.duel.CardEffects.MonsterEffectEnums.FlipEffect;
import controller.duel.CardEffects.MonsterEffectEnums.OptionalMonsterEffect;
import controller.duel.CardEffects.MonsterEffectEnums.SentToGraveyardEffect;
import controller.duel.CardEffects.MonsterEffectEnums.SummoningRequirement;
import controller.duel.CardEffects.MonsterEffectEnums.UponSummoningEffect;
import controller.duel.CardEffects.SpellEffectEnums.EquipSpellEffect;
import controller.duel.CardEffects.SpellEffectEnums.FieldSpellEffect;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.CardPosition;
import model.cardData.General.CardType;
import model.cardData.General.RowOfCardLocation;

public class MonsterCard extends Card {

    private int attackPower;
    private int defensePower;
    private int attackPowerConsideringEffects;
    private int defensePowerConsideringEffects;
    private int level;
    private boolean cardPositionChanged;
    private boolean cardAttacked;
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

    public MonsterCard(int attackPower, int defensePower, int level, MonsterCardAttribute attribute,
                       MonsterCardFamily monsterCardFamily, MonsterCardValue monsterCardValue, String cardName,
                       String cardDescription, CardPosition cardPosition, int numberOfAllowedUsages, int cardPrice,
                       HashMap<String, List<String>> enumValues) {
        super(cardName, CardType.MONSTER, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.attackPowerConsideringEffects = 0;
        this.defensePowerConsideringEffects = 0;
        this.level = level;
        this.oncePerTurnCardEffectUsed = false;
        this.cardPositionChanged = false;
        this.cardAttacked = false;
        this.monsterCardAttribute = attribute;
        this.monsterCardFamily = monsterCardFamily;
        this.monsterCardValue = monsterCardValue;
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
        if (cardDescription.equals("")){
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            if (level == 5 || level == 6) {
                summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
            } else if (level == 7 || level == 8) {
                summoningRequirements.add(SummoningRequirement.TRIBUTE_2_MONSTERS);
            } else if (level >= 9) {
                summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
            }
        } else if (cardDescription.equals("a")){
            summoningRequirements.add(SummoningRequirement.CAN_BE_SPECIAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
            summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
            summoningRequirements.add(SummoningRequirement.DISCARD_1_CARD);
        } else if (cardDescription.equals("b")){
            summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
            summoningRequirements.add(SummoningRequirement.CAN_BE_TRIBUTE_SUMMONED);
            summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
            summoningRequirements.add(SummoningRequirement.IN_CASE_OF_NORMAL_SUMMON_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            summoningRequirements.add(SummoningRequirement.IN_CASE_OF_SET_THERE_IS_NO_NEED_TO_COUNT_NUMBER_OF_TRIBUTES_NEEDED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_NORMAL_SUMMONED);
            uponSummoningEffects.add(UponSummoningEffect.SET_ATK_1900_IF_SET);
            uponSummoningEffects.add(UponSummoningEffect.DESTROY_ALL_OF_YOUR_OPPONENTS_CARDS);
        }
        if (enumValues != null) {
            setEnumValues(enumValues);
        }
    }

    public MonsterCard(MonsterCard monster) {
        super(monster.getCardName(), CardType.MONSTER, monster.getCardDescription(), monster.getCardPosition(),
            monster.getNumberOfAllowedUsages(), monster.getCardPrice());
        this.attackPower = monster.getAttackPower();
        this.defensePower = monster.getDefensePower();
        this.attackPowerConsideringEffects = 0;
        this.defensePowerConsideringEffects = 0;
        this.level = monster.getLevel();
        this.oncePerTurnCardEffectUsed = monster.isOncePerTurnCardEffectUsed();
        this.cardPositionChanged = false;
        this.cardAttacked = false;
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

    public boolean isCardAttacked() {
        return cardAttacked;
    }

    public void setCardPositionChanged(boolean cardPositionChanged) {
        this.cardPositionChanged = cardPositionChanged;
    }

    public void setOncePerTurnCardEffectUsed(boolean oncePerTurnCardEffectUsed) {
        this.oncePerTurnCardEffectUsed = oncePerTurnCardEffectUsed;
    }

    public void setCardAttacked(boolean cardAttacked) {
        this.cardAttacked = cardAttacked;
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

        for (int i = 1; i < enumValues.get("AttackerEffect").size(); i++) {
            attackerEffects.add(AttackerEffect.valueOf(enumValues.get("AttackerEffects").get(i)));
        }

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

    public static int giveATKDEFConsideringEffects(String string, CardLocation cardLocation, int index) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        int finalAttackPower = monsterCard.getAttackPower();
        int finalDefensePower = monsterCard.getDefensePower();
        ArrayList<FieldSpellEffect> fieldSpellEffects = monsterCard.getFieldSpellEffects();
        ArrayList<EquipSpellEffect> equipSpellEffects = monsterCard.getEquipSpellEffects();
        ArrayList<ContinuousMonsterEffect> continuousMonsterEffects = monsterCard.getContinuousMonsterEffects();
        MonsterCardFamily monsterCardFamily = monsterCard.getMonsterCardFamily();
        MonsterCardAttribute monsterCardAttribute = monsterCard.getMonsterCardAttribute();
        CardPosition cardPosition = monsterCard.getCardPosition();
        if (continuousMonsterEffects.contains(ContinuousMonsterEffect.ATK_IS_SET_300_MULTIPLIED_BY_TOTAL_OF_FACE_UP_MONSTER_LEVELS_YOU_CONTROL)) {
            int sumOfLevels = 0;
            if (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                for (int i = 0; i < 5; i++) {
                    Card card = duelBoard.getCardByCardLocation(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1));
                    if (Card.isCardAMonster(card) && (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) ||
                        card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
                        sumOfLevels += ((MonsterCard) card).getLevel();
                    }
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    Card card = duelBoard.getCardByCardLocation(new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1));
                    if (Card.isCardAMonster(card) && (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) ||
                        card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
                        sumOfLevels += ((MonsterCard) card).getLevel();
                    }
                }
            }
            finalAttackPower += 300 * sumOfLevels;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.FIEND_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.FIEND)) {
            System.out.println("F1");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.FAIRY_LOSE_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.FAIRY)) {
            System.out.println("F2");
            finalAttackPower -= 200;
            finalDefensePower -= 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.SPELLCASTER_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.SPELLCASTER)) {
            System.out.println("F3");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.INSECT_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.INSECT)) {
            System.out.println("F4");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.BEAST_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.BEAST)) {
            System.out.println("F5");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.PLANT_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.PLANT)) {
            System.out.println("F6");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.BEASTWARRIOR_GAIN_200_ATK_DEF) && monsterCardFamily.equals(MonsterCardFamily.BEAST_WARRIOR)) {
            System.out.println("F7");
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.WATER_GAINS_500_ATK) && monsterCardAttribute.equals(MonsterCardAttribute.WATER)) {
            System.out.println("F8");
            finalAttackPower += 500;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.WATER_LOSES_400_DEF) && monsterCardAttribute.equals(MonsterCardAttribute.WATER)) {
            System.out.println("F9");
            finalDefensePower -= 400;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY) && monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.BEAST)) {
            Card card = duelBoard.getCardByCardLocation(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
            if (card == null && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                System.out.println("F10");
                ArrayList<Card> cardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += 100;
                    }
                }
            } else if (card != null && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                System.out.println("F11");
                ArrayList<Card> cardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += 100;
                    }
                }
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.DARK_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF) && monsterCardAttribute.equals(MonsterCardAttribute.DARK)) {
            System.out.println("E1");
            finalAttackPower += 400;
            finalDefensePower -= 200;
        }
        if (equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK)) {
            System.out.println("E2");
            finalAttackPower += 500;
        }
        if (equipSpellEffects.contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK) && monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
            System.out.println("E3");
            if (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                finalDefensePower += monsterCard.getAttackPower();
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF) && monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
            System.out.println("E4");
            if (cardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                finalAttackPower += monsterCard.getDefensePower();
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS)) {
            RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
            if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                System.out.println("E5");
                for (int i = 0; i < 5; i++) {
                    CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1);
                    Card card = duelBoard.getCardByCardLocation(cardLocationOfAllyMonster);
                    if (card != null) {
                        MonsterCard allyMonsterCard = (MonsterCard) card;
                        if (allyMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                            finalAttackPower += 800;
                            finalDefensePower += 800;
                        }
                    }
                }
            } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                System.out.println("E6");
                for (int i = 0; i < 5; i++) {
                    CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1);
                    Card card = duelBoard.getCardByCardLocation(cardLocationOfAllyMonster);
                    if (card != null) {
                        MonsterCard allyMonsterCard = (MonsterCard) card;
                        if (allyMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                            finalAttackPower += 800;
                            finalDefensePower += 800;
                        }
                    }
                }
            }
        }
        if (string.equals("attack")) {
            return finalAttackPower;
        }
        if (string.equals("defense")) {
            return finalDefensePower;
        }
        return 0;
    }

    @Override
    protected Object clone() {
        return new MonsterCard(this);
    }

}
