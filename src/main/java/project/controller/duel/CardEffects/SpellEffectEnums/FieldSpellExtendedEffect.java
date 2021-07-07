package project.controller.duel.CardEffects.SpellEffectEnums;

import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.SpellCardData.SpellCard;
import project.view.pooyaviewpackage.DuelView;

import java.util.ArrayList;

public class FieldSpellExtendedEffect {
    private ArrayList<MonsterCardFamily> firstMonsterCardFamilies;
    private ArrayList<MonsterCardFamily> secondMonsterCardFamilies;
    private int firstAttack;
    private int firstDefense;
    private int secondAttack;
    private int secondDefense;
    private ArrayList<FieldSpellEffect> fieldSpellEffects;
    public FieldSpellExtendedEffect(ArrayList<MonsterCardFamily> firstMonsterCardFamilies, ArrayList<MonsterCardFamily> secondMonsterCardFamilies
        , int firstAttack, int firstDefense, int secondAttack, int secondDefense, ArrayList<FieldSpellEffect> fieldSpellEffects) {
        this.firstAttack = firstAttack;
        this.firstDefense = firstDefense;
        this.secondAttack = secondAttack;
        this.secondDefense = secondDefense;
        this.firstMonsterCardFamilies = new ArrayList<>();
        this.firstMonsterCardFamilies.addAll(firstMonsterCardFamilies);
        this.secondMonsterCardFamilies = new ArrayList<>();
        this.secondMonsterCardFamilies.addAll(secondMonsterCardFamilies);
        this.fieldSpellEffects = new ArrayList<>();
        this.fieldSpellEffects.addAll(fieldSpellEffects);
    }

    public ArrayList<MonsterCardFamily> getFirstMonsterCardFamilies() {
        return firstMonsterCardFamilies;
    }

    public ArrayList<MonsterCardFamily> getSecondMonsterCardFamilies() {
        return secondMonsterCardFamilies;
    }

    public int getFirstAttack() {
        return firstAttack;
    }

    public int getFirstDefense() {
        return firstDefense;
    }

    public int getSecondAttack() {
        return secondAttack;
    }

    public int getSecondDefense() {
        return secondDefense;
    }

    public ArrayList<FieldSpellEffect> getFieldSpellEffects() {
        return fieldSpellEffects;
    }

    private static int giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(CardLocation monsterCardLocation
        , CardLocation spellFieldCardLocation, int index, String attackOrDefense) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
        MonsterCard monsterCard = (MonsterCard) duelBoard.getCardByCardLocation(monsterCardLocation);
        MonsterCardFamily monsterCardFamily = monsterCard.getMonsterCardFamily();
        SpellCard spellFieldCard = (SpellCard) duelBoard.getCardByCardLocation(spellFieldCardLocation);
        int finalAttackPower = 0;
        int finalDefensePower = 0;
        if (spellFieldCard.getCardPosition().equals(CardPosition.FACE_DOWN_SPELL_SET_POSITION)){
            return 0;
        }
        ArrayList<FieldSpellExtendedEffect> fieldSpellExtendedEffects = spellFieldCard.getFieldSpellExtendedEffects();
        if (fieldSpellExtendedEffects.get(0).getFirstMonsterCardFamilies().contains(monsterCardFamily)){
            finalAttackPower += fieldSpellExtendedEffects.get(0).getFirstAttack();
            finalDefensePower += fieldSpellExtendedEffects.get(0).getFirstDefense();
        }
        if (fieldSpellExtendedEffects.get(1).getFirstMonsterCardFamilies().contains(monsterCardFamily)){
            finalAttackPower += fieldSpellExtendedEffects.get(1).getFirstAttack();
            finalDefensePower += fieldSpellExtendedEffects.get(1).getFirstDefense();
        }
        ArrayList<FieldSpellEffect> fieldSpellEffects = fieldSpellExtendedEffects.get(0).getFieldSpellEffects();
        if (fieldSpellEffects
            .contains(FieldSpellEffect.BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY)
            && fieldSpellExtendedEffects.get(0).getFirstMonsterCardFamilies().contains(monsterCardFamily)
            && isSpellFieldCardOnOurSide(monsterCardLocation, spellFieldCardLocation)) {
            RowOfCardLocation rowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
            if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
                ArrayList<Card> cardsInGraveyard = duelBoard.getOpponentCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += fieldSpellExtendedEffects.get(0).getFirstAttack();
                    }
                }
            } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
                System.out.println("F11");
                ArrayList<Card> cardsInGraveyard = duelBoard.getAllyCardsInGraveyard();
                for (int i = 0; i < cardsInGraveyard.size(); i++) {
                    if (Card.isCardAMonster(cardsInGraveyard.get(i))) {
                        finalAttackPower += 100;
                    }
                }
            }
        }
        if (attackOrDefense.equals("attack")) {
            return finalAttackPower;
        }
        return finalDefensePower;
    }
    private static boolean isSpellFieldCardOnOurSide(CardLocation monsterCardLocation,
                                                     CardLocation spellFieldCardLocation) {
        RowOfCardLocation monsterRowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
        RowOfCardLocation spellFieldCardRowOfCardLocation = spellFieldCardLocation.getRowOfCardLocation();
        if (monsterRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
            && spellFieldCardRowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return true;
        }
        if (monsterRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
            && spellFieldCardRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return true;
        }
        return false;
    }
}
