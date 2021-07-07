package project.controller.duel.CardEffects.SpellEffectEnums;

import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;

import java.util.ArrayList;

public class EquipSpellExtendedEffect {
    private ArrayList<MonsterCardFamily> firstMonsterCardFamilies;
    private int firstAttack;
    private int firstDefense;
    private ArrayList<EquipSpellEffect> equipSpellEffects;

    public EquipSpellExtendedEffect(ArrayList<MonsterCardFamily> firstMonsterCardFamilies, int firstAttack, int firstDefense) {
        this.firstAttack = firstAttack;
        this.firstDefense = firstDefense;
        this.firstMonsterCardFamilies = firstMonsterCardFamilies;
    }

    public ArrayList<MonsterCardFamily> getFirstMonsterCardFamilies() {
        return firstMonsterCardFamilies;
    }

    public int getFirstAttack() {
        return firstAttack;
    }

    public int getFirstDefense() {
        return firstDefense;
    }

    public ArrayList<EquipSpellEffect> getEquipSpellEffects() {
        return equipSpellEffects;
    }

    private static int giveChangesOnATKDEFConsideringEquipSpellEffects(CardLocation monsterCardLocation, int index, String attackOrDefense) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(monsterCardLocation);
        MonsterCardFamily monsterCardFamily = monsterCard.getMonsterCardFamily();
        ArrayList<EquipSpellExtendedEffect> equipSpellExtendedEffects = monsterCard.getEquipSpellExtendedEffects();
        CardPosition cardPosition = monsterCard.getCardPosition();
        int finalAttackPower = 0;
        int finalDefensePower = 0;
        for (int i = 0; i < equipSpellExtendedEffects.size(); i++) {
            // if has every monster ...
            if (equipSpellExtendedEffects.get(i).getFirstMonsterCardFamilies().contains(monsterCardFamily)) {
                if (equipSpellExtendedEffects.get(i).getEquipSpellEffects().contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF)) {
                    finalAttackPower += monsterCard.getDefensePower();
                }
                if (equipSpellExtendedEffects.get(i).getEquipSpellEffects().contains(EquipSpellEffect.WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK)) {
                    finalDefensePower += monsterCard.getAttackPower();
                }
                if (equipSpellExtendedEffects.get(i).getEquipSpellEffects().contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS)) {
                    RowOfCardLocation rowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
                    if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                        for (int j = 0; j < 5; j++) {
                            CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE,
                                j + 1);
                            Card card = duelBoard.getCardByCardLocation(cardLocationOfAllyMonster);
                            if (card != null) {
                                MonsterCard allyMonsterCard = (MonsterCard) card;
                                if (allyMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                                    finalAttackPower += equipSpellExtendedEffects.get(i).getFirstAttack();
                                    finalDefensePower += equipSpellExtendedEffects.get(i).getFirstDefense();
                                }
                            }
                        }
                    } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                        System.out.println("E6");
                        for (int j = 0; j < 5; j++) {
                            CardLocation cardLocationOfAllyMonster = new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE,
                                j + 1);
                            Card card = duelBoard.getCardByCardLocation(cardLocationOfAllyMonster);
                            if (card != null) {
                                MonsterCard allyMonsterCard = (MonsterCard) card;
                                if (allyMonsterCard.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                                    finalAttackPower += equipSpellExtendedEffects.get(i).getFirstAttack();
                                    finalDefensePower += equipSpellExtendedEffects.get(i).getFirstDefense();
                                }
                            }
                        }
                    }
                }
                finalAttackPower += equipSpellExtendedEffects.get(i).getFirstAttack();
                finalDefensePower += equipSpellExtendedEffects.get(i).getFirstDefense();
            }
        }
        if (attackOrDefense.equals("attack")) {
            return finalAttackPower;
        }
        return finalDefensePower;
    }

}
