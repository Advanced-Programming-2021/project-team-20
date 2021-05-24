package model;

import org.junit.jupiter.api.Test;

import controller.non_duel.storage.Storage;
import model.cardData.MonsterCardData.MonsterCard;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;

class MonsterCardTest {

    static Storage storage;

    @BeforeAll
    static void startProgram() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void CardTest() {
        MonsterCard monsterCard = new MonsterCard((MonsterCard) Storage.getAllMonsterCards().get("Suijin"));
        monsterCard.getMonsterCardValue();
        monsterCard.getMonsterCardAttribute();
        monsterCard.getMonsterCardFamily();
        monsterCard.getLevel();
        monsterCard.getDefensePower();
        monsterCard.getAttackPower();
        monsterCard.getAttackPowerConsideringEffects();
        monsterCard.getDefensePowerConsideringEffects();
        monsterCard.isOncePerTurnCardEffectUsed();
        monsterCard.isCardPositionChanged();
        monsterCard.isHasCardAlreadyAttacked();
        monsterCard.setCardPositionChanged(true);
        monsterCard.setOncePerTurnCardEffectUsed(true);
        monsterCard.setHasCardAlreadyAttacked(true);
        monsterCard.getSummoningRequirements();
        monsterCard.getUponSummoningEffects();
        monsterCard.getAttackerEffects();
        monsterCard.getBeingAttackedEffects();
        monsterCard.getContinuousMonsterEffects();
        monsterCard.getFlipEffects();
        monsterCard.getOptionalMonsterEffects();
        monsterCard.getSentToGraveyardEffects();
        monsterCard.getEquipSpellEffects();
        monsterCard.getFieldSpellEffects();
        monsterCard.setAttackPower(1000);
        monsterCard.setDefensePower(1000);
        monsterCard.setLevel(5);
        monsterCard.setMonsterCardFamily(null);
        monsterCard.setMonsterCardValue(null);
        monsterCard.setSummoningRequirements(null);
        monsterCard.setUponSummoningEffects(null);
        monsterCard.setAttackerEffects(null);
        monsterCard.setContinuousMonsterEffects(null);
        monsterCard.setFlipEffects(null);
        monsterCard.setOptionalMonsterEffects(null);
        monsterCard.setSentToGraveyardEffects(null);
        monsterCard.setAttackerEffects(null);
        monsterCard.addEquipSpellEffectToList(null);
        monsterCard.removeEquipSpellEffectFromList(null);
        monsterCard.addSpellFieldEffectToList(null);
        monsterCard.removeSpellFieldEffectFromList(null);
        monsterCard.clearEquipSpellEffect();
        monsterCard.clearFieldSpellEffect();
        monsterCard.clone();
        assertEquals(1000, monsterCard.getAttackPower());
    }
    /*
     * public static int giveATKDEFConsideringEffects(String string, CardLocation
     * cardLocation, int index) { DuelBoard duelBoard =
     * GameManager.getDuelBoardByIndex(index); MonsterCard monsterCard =
     * (MonsterCard) duelBoard.getCardByCardLocation(cardLocation); int
     * finalAttackPower = monsterCard.getAttackPower(); int finalDefensePower =
     * monsterCard.getDefensePower(); finalAttackPower +=
     * giveChangesInATKDEFConsideringContinuousMonsterEffect(cardLocation, index,
     * "attack"); finalDefensePower +=
     * giveChangesInATKDEFConsideringContinuousMonsterEffect(cardLocation, index,
     * "defense"); finalAttackPower +=
     * giveChangesOnATKDEFConsideringEquipSpellEffects(cardLocation, index,
     * "attack"); finalDefensePower +=
     * giveChangesOnATKDEFConsideringEquipSpellEffects(cardLocation, index,
     * "defense"); CardLocation cardLocationOfFirstSpellFieldCard =
     * giveLocationOfPossibleSpellFieldCard(0, 1); CardLocation
     * cardLocationOfSecondSpellFieldCard = giveLocationOfPossibleSpellFieldCard(0,
     * 2); if (cardLocationOfFirstSpellFieldCard != null) {
     * System.out.println("ally has spell field card %%%"); finalAttackPower +=
     * giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
     * cardLocationOfFirstSpellFieldCard, index, "attack"); finalDefensePower +=
     * giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
     * cardLocationOfFirstSpellFieldCard, index, "defense"); } if
     * (cardLocationOfSecondSpellFieldCard != null) {
     * System.out.println("opponent has spell field card %%%"); finalAttackPower +=
     * giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
     * cardLocationOfSecondSpellFieldCard, index, "attack"); finalDefensePower +=
     * giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(cardLocation,
     * cardLocationOfSecondSpellFieldCard, index, "defense");
     *
     * } finalAttackPower +=
     * giveChangesInATKDEFConsideringOtherContinuousMonsterEffects("attack",
     * cardLocation, index); finalDefensePower +=
     * giveChangesInATKDEFConsideringOtherContinuousMonsterEffects("defense",
     * cardLocation, index); if (string.equals("attack")) { return finalAttackPower;
     * } if (string.equals("defense")) { return finalDefensePower; } return 0; }
     *
     * private static int
     * giveChangesOnATKDEFConsideringEquipSpellEffects(CardLocation
     * monsterCardLocation, int index, String attackOrDefense) { DuelBoard duelBoard
     * = GameManager.getDuelBoardByIndex(index); MonsterCard monsterCard =
     * (MonsterCard) duelBoard.getCardByCardLocation(monsterCardLocation);
     * MonsterCardFamily monsterCardFamily = monsterCard.getMonsterCardFamily();
     * MonsterCardAttribute monsterCardAttribute =
     * monsterCard.getMonsterCardAttribute(); ArrayList<EquipSpellEffect>
     * equipSpellEffects = monsterCard.getEquipSpellEffects(); CardPosition
     * cardPosition = monsterCard.getCardPosition(); int finalAttackPower = 0; int
     * finalDefensePower = 0; if (equipSpellEffects .contains(EquipSpellEffect.
     * FIEND_OR_SPELLCASTER_EQUIPPED_MONSTER_GAINS_400_ATK_LOSE_200_DEF) &&
     * (monsterCardFamily.equals(MonsterCardFamily.FIEND) ||
     * monsterCardFamily.equals(MonsterCardFamily.SPELLCASTER))) {
     * System.out.println("E1"); finalAttackPower += 400; finalDefensePower -= 200;
     * } if
     * (equipSpellEffects.contains(EquipSpellEffect.EQUIPPED_MONSTER_GAIN_500_ATK))
     * { System.out.println("E2"); finalAttackPower += 500; } if (equipSpellEffects
     * .contains(EquipSpellEffect.
     * WARRIOR_EQUIPPED_MONSTER_IF_DEFENSE_POSITION_GAIN_DEF_EQUAL_TO_ATK) &&
     * monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
     * System.out.println("E3"); if
     * (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
     * finalDefensePower += monsterCard.getAttackPower(); } } if (equipSpellEffects
     * .contains(EquipSpellEffect.
     * WARRIOR_EQUIPPED_MONSTER_IF_ATTACK_POSITION_GAIN_ATK_EQUAL_TO_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.WARRIOR)) {
     * System.out.println("E4"); if
     * (cardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
     * finalAttackPower += monsterCard.getDefensePower(); } } if (equipSpellEffects
     * .contains(EquipSpellEffect.
     * EQUIPPED_MONSTER_GAIN_800_ATK_DEF_FOR_EACH_FACE_UP_MONSTER_OWNER_CONTROLS)) {
     * RowOfCardLocation rowOfCardLocation =
     * monsterCardLocation.getRowOfCardLocation(); if
     * (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
     * System.out.println("E5"); for (int i = 0; i < 5; i++) { CardLocation
     * cardLocationOfAllyMonster = new
     * CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1); Card card =
     * duelBoard.getCardByCardLocation(cardLocationOfAllyMonster); if (card != null)
     * { MonsterCard allyMonsterCard = (MonsterCard) card; if
     * (allyMonsterCard.getCardPosition().equals(CardPosition.
     * FACE_UP_ATTACK_POSITION)) { finalAttackPower += 800; finalDefensePower +=
     * 800; } } } } else if
     * (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
     * System.out.println("E6"); for (int i = 0; i < 5; i++) { CardLocation
     * cardLocationOfAllyMonster = new
     * CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1); Card card =
     * duelBoard.getCardByCardLocation(cardLocationOfAllyMonster); if (card != null)
     * { MonsterCard allyMonsterCard = (MonsterCard) card; if
     * (allyMonsterCard.getCardPosition().equals(CardPosition.
     * FACE_UP_ATTACK_POSITION)) { finalAttackPower += 800; finalDefensePower +=
     * 800; } } } } } if (attackOrDefense.equals("attack")) { return
     * finalAttackPower; } return finalDefensePower; }
     *
     * private static int
     * giveChangesInATKDEFConsideringContinuousMonsterEffect(CardLocation
     * cardLocation, int index, String attackOrDefense) { DuelBoard duelBoard =
     * GameManager.getDuelBoardByIndex(index); MonsterCard monsterCard =
     * (MonsterCard) duelBoard.getCardByCardLocation(cardLocation);
     * ArrayList<ContinuousMonsterEffect> continuousMonsterEffects =
     * monsterCard.getContinuousMonsterEffects(); int finalAttackPower = 0; int
     * finalDefensePower = 0; if (continuousMonsterEffects.contains(
     * ContinuousMonsterEffect.
     * ATK_IS_SET_300_MULTIPLIED_BY_TOTAL_OF_FACE_UP_MONSTER_LEVELS_YOU_CONTROL)) {
     * int sumOfLevels = 0; if
     * (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.
     * ALLY_MONSTER_ZONE)) { for (int i = 0; i < 5; i++) { Card card = duelBoard
     * .getCardByCardLocation(new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE,
     * i + 1)); if (Card.isCardAMonster(card) &&
     * (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) ||
     * card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
     * sumOfLevels += ((MonsterCard) card).getLevel(); } } } else { for (int i = 0;
     * i < 5; i++) { Card card = duelBoard .getCardByCardLocation(new
     * CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1)); if
     * (Card.isCardAMonster(card) &&
     * (card.getCardPosition().equals(CardPosition.FACE_UP_ATTACK_POSITION) ||
     * card.getCardPosition().equals(CardPosition.FACE_UP_DEFENSE_POSITION))) {
     * sumOfLevels += ((MonsterCard) card).getLevel(); } } } finalAttackPower += 300
     * * sumOfLevels; } if (attackOrDefense.equals("attack")) { return
     * finalAttackPower; } return finalDefensePower; }
     *
     * private static int
     * giveChangesInATKDEFConsideringOtherContinuousMonsterEffects(String string,
     * CardLocation cardLocation, int index) { if (string.equals("defense")) {
     * return 0; } int attackChanges = 0; DuelBoard duelBoard =
     * GameManager.getDuelBoardByIndex(index); ArrayList<Card> allyMonsterCards =
     * duelBoard.getAllyMonsterCards(); ArrayList<Card> opponentMonsterCards =
     * duelBoard.getOpponentMonsterCards(); if
     * (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.
     * ALLY_MONSTER_ZONE)) { attackChanges +=
     * giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(
     * allyMonsterCards, index); } else if
     * (cardLocation.getRowOfCardLocation().equals(RowOfCardLocation.
     * OPPONENT_MONSTER_ZONE)) { attackChanges +=
     * giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(
     * opponentMonsterCards, index); } return attackChanges; }
     *
     * private static int
     * giveIncreasesInATKForContinuousMonsterEffectsInThisArrayList(ArrayList<Card>
     * monsterCards, int index) { DuelBoard duelBoard =
     * GameManager.getDuelBoardByIndex(index); int attackChanges = 0; for (int i =
     * 0; i < monsterCards.size(); i++) { CardLocation cardLocationOfAllyMonster =
     * new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1); MonsterCard
     * sampleMonsterCard = (MonsterCard)
     * duelBoard.getCardByCardLocation(cardLocationOfAllyMonster); if
     * (sampleMonsterCard != null) { ArrayList<ContinuousMonsterEffect>
     * continuousMonsterEffects = sampleMonsterCard .getContinuousMonsterEffects();
     * if (continuousMonsterEffects.contains(ContinuousMonsterEffect.
     * ALL_MONSTERS_OWNER_CONTROLS_GAIN_400_ATK) &&
     * (sampleMonsterCard.getCardPosition().equals(CardPosition.
     * FACE_UP_ATTACK_POSITION) ||
     * sampleMonsterCard.getCardPosition().equals(CardPosition.
     * FACE_UP_DEFENSE_POSITION))) { attackChanges += 400; } } } return
     * attackChanges; }
     *
     * private static int
     * giveIncreasesInATKOrDEFGivenSpellFieldCardEffectsAndMonsterCard(CardLocation
     * monsterCardLocation, CardLocation spellFieldCardLocation, int index, String
     * attackOrDefense) { DuelBoard duelBoard =
     * GameManager.getDuelBoardByIndex(index); int finalAttackPower = 0; int
     * finalDefensePower = 0; MonsterCard monsterCard = (MonsterCard)
     * duelBoard.getCardByCardLocation(monsterCardLocation); MonsterCardFamily
     * monsterCardFamily = monsterCard.getMonsterCardFamily(); SpellCard
     * spellFieldCard = (SpellCard)
     * duelBoard.getCardByCardLocation(spellFieldCardLocation);
     * ArrayList<FieldSpellEffect> fieldSpellEffects =
     * spellFieldCard.getFieldSpellEffects(); if
     * (spellFieldCard.getCardPosition().equals(CardPosition.
     * FACE_DOWN_SPELL_SET_POSITION)) { return 0; } if
     * (fieldSpellEffects.contains(FieldSpellEffect.FIEND_GAIN_200_ATK_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.FIEND)) {
     * System.out.println("F1"); finalAttackPower += 200; finalDefensePower += 200;
     * } if (fieldSpellEffects.contains(FieldSpellEffect.FAIRY_LOSE_200_ATK_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.FAIRY)) {
     * System.out.println("F2"); finalAttackPower -= 200; finalDefensePower -= 200;
     * } if
     * (fieldSpellEffects.contains(FieldSpellEffect.SPELLCASTER_GAIN_200_ATK_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.SPELLCASTER)) {
     * System.out.println("F3"); finalAttackPower += 200; finalDefensePower += 200;
     * } if (fieldSpellEffects.contains(FieldSpellEffect.INSECT_GAIN_200_ATK_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.INSECT)) {
     * System.out.println("F4"); finalAttackPower += 200; finalDefensePower += 200;
     * } if (fieldSpellEffects.contains(FieldSpellEffect.BEAST_GAIN_200_ATK_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.BEAST)) {
     * System.out.println("F5"); finalAttackPower += 200; finalDefensePower += 200;
     * } if
     * (fieldSpellEffects.contains(FieldSpellEffect.BEASTWARRIOR_GAIN_200_ATK_DEF)
     * && monsterCardFamily.equals(MonsterCardFamily.BEAST_WARRIOR)) {
     * System.out.println("F7"); finalAttackPower += 200; finalDefensePower += 200;
     * } if (fieldSpellEffects.contains(FieldSpellEffect.AQUA_GAINS_500_ATK) &&
     * monsterCardFamily.equals(MonsterCardFamily.AQUA)) { System.out.println("F8");
     * finalAttackPower += 500; } if
     * (fieldSpellEffects.contains(FieldSpellEffect.AQUA_LOSES_400_DEF) &&
     * monsterCardFamily.equals(MonsterCardFamily.AQUA)) { System.out.println("F9");
     * finalDefensePower -= 400; } if (fieldSpellEffects .contains(FieldSpellEffect.
     * BEAST_MONSTERS_OWNER_CONTROLS_GAIN_100_ATK_FOR_EACH_MONSTER_IN_GY) &&
     * monsterCard.getMonsterCardFamily().equals(MonsterCardFamily.BEAST) &&
     * isSpellFieldCardOnOurSide(monsterCardLocation, spellFieldCardLocation)) {
     * RowOfCardLocation rowOfCardLocation =
     * monsterCardLocation.getRowOfCardLocation(); if
     * (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
     * System.out.println("F10"); ArrayList<Card> cardsInGraveyard =
     * duelBoard.getOpponentCardsInGraveyard(); for (int i = 0; i <
     * cardsInGraveyard.size(); i++) { if
     * (Card.isCardAMonster(cardsInGraveyard.get(i))) { finalAttackPower += 100; } }
     * } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
     * System.out.println("F11"); ArrayList<Card> cardsInGraveyard =
     * duelBoard.getAllyCardsInGraveyard(); for (int i = 0; i <
     * cardsInGraveyard.size(); i++) { if
     * (Card.isCardAMonster(cardsInGraveyard.get(i))) { finalAttackPower += 100; } }
     * } } if (attackOrDefense.equals("attack")) { return finalAttackPower; } return
     * finalDefensePower; }
     *
     * private static boolean isSpellFieldCardOnOurSide(CardLocation
     * monsterCardLocation, CardLocation spellFieldCardLocation) { RowOfCardLocation
     * monsterRowOfCardLocation = monsterCardLocation.getRowOfCardLocation();
     * RowOfCardLocation spellFieldCardRowOfCardLocation =
     * spellFieldCardLocation.getRowOfCardLocation(); if
     * (monsterRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) &&
     * spellFieldCardRowOfCardLocation.equals(RowOfCardLocation.
     * ALLY_SPELL_FIELD_ZONE)) { return true; } if
     * (monsterRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) &&
     * spellFieldCardRowOfCardLocation.equals(RowOfCardLocation.
     * OPPONENT_SPELL_FIELD_ZONE)) { return true; } return false; }
     *
     * private static CardLocation giveLocationOfPossibleSpellFieldCard(int index,
     * int turn) { DuelBoard duelBoard = GameManager.getDuelBoardByIndex(index);
     * Card possibleCard = null; CardLocation possibleCardLocation = null; if (turn
     * == 1) { possibleCardLocation = new
     * CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1); } else {
     * possibleCardLocation = new
     * CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1); } possibleCard
     * = duelBoard.getCardByCardLocation(possibleCardLocation); if
     * (!Card.isCardASpell(possibleCard)) { return null; } return
     * possibleCardLocation; }
     *
     */
}
