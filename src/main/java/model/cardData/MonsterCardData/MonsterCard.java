package model.cardData.MonsterCardData;

import java.util.ArrayList;

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
            String cardDescription, CardPosition cardPosition, int numberOfAllowedUsages, int cardPrice) {
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
        summoningRequirements.add(SummoningRequirement.CAN_BE_NORMAL_SUMMONED);
        if (level == 5 || level == 6) {
            summoningRequirements.add(SummoningRequirement.TRIBUTE_1_MONSTER);
        } else if (level == 7 || level == 8) {
            summoningRequirements.add(SummoningRequirement.TRIBUTE_2_MONSTERS);
        } else if (level >= 9) {
            summoningRequirements.add(SummoningRequirement.TRIBUTE_3_MONSTERS);
        }
        uponSummoningEffects = new ArrayList<>();
        attackerEffects = new ArrayList<>();
        beingAttackedEffects = new ArrayList<>();
        continuousMonsterEffects = new ArrayList<>();
        flipEffects = new ArrayList<>();
        optionalMonsterEffects = new ArrayList<>();
        sentToGraveyardEffects = new ArrayList<>();
        equipSpellEffects = new ArrayList<>();
        fieldSpellEffects = new ArrayList<>();
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

    public static int giveATKDEFConsideringEffects(String string, CardLocation cardLocation, int index) {
        
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
        int finalAttackPower = monsterCard.getAttackPower();
        int finalDefensePower = monsterCard.getDefensePower();
        ArrayList<FieldSpellEffect> fieldSpellEffects = monsterCard.getFieldSpellEffects();
        ArrayList<EquipSpellEffect> equipSpellEffects = monsterCard.getEquipSpellEffects();
        MonsterCardFamily monsterCardFamily = monsterCard.getMonsterCardFamily();
        MonsterCardAttribute monsterCardAttribute = monsterCard.getMonsterCardAttribute();
        CardPosition cardPosition = monsterCard.getCardPosition();
        if (fieldSpellEffects.contains(FieldSpellEffect.FIEND_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.FIEND)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.FAIRY_LOSE_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.FAIRY)) {
            finalAttackPower -= 200;
            finalDefensePower -= 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.SPELLCASTER_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.SPELLCASTER)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.INSECT_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.INSECT)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.BEAST_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.BEAST)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.PLANT_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.PLANT)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.BEASTWARRIOR_GAIN_200_ATK_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.BEAST_WARRIOR)) {
            finalAttackPower += 200;
            finalDefensePower += 200;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.WATER_GAINS_500_ATK)
                && monsterCardAttribute.equals(MonsterCardAttribute.WATER)) {
            finalAttackPower += 500;
        }
        if (fieldSpellEffects.contains(FieldSpellEffect.WATER_LOSES_400_DEF)
                && monsterCardAttribute.equals(MonsterCardAttribute.WATER)) {
            finalDefensePower -= 400;
        }
        if (fieldSpellEffects
                .contains(FieldSpellEffect.BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY)
                && monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.BEAST)) {
            Card card = duelBoard.getCardByCardLocation(new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1));
            if (card == null && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                ArrayList<Card> cardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += 100;
                    }
                }
            } else if (card != null
                    && cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                ArrayList<Card> cardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += 100;
                    }
                }
            }
        }
        if (equipSpellEffects.contains(EquipSpellEffect.DARK_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF)
                && monsterCardAttribute.equals(MonsterCardAttribute.DARK)) {
            finalAttackPower += 400;
            finalDefensePower -= 200;
        }
        if (equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK)) {
            finalAttackPower += 500;
        }
        if (equipSpellEffects
                .contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK)
                && monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
            if (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                finalDefensePower += monsterCard.getAttackPower();
            }
        }
        if (equipSpellEffects
                .contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF)
                && monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
            if (cardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                finalAttackPower += monsterCard.getDefensePower();
            }
        }
        if (equipSpellEffects
                .contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS)) {
            RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
            if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                for (int i = 0; i < 5; i++) {
                    CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i);
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
                for (int i = 0; i < 5; i++) {
                    CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE,
                            i);
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
    protected Object clone(){
        return new MonsterCard(attackPower, defensePower, level, monsterCardAttribute, monsterCardFamily, monsterCardValue, cardName, cardDescription, cardPosition, numberOfAllowedUsages, cardPrice);
    }

}
