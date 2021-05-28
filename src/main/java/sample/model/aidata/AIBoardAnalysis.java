package sample.model.aidata;

import java.util.ArrayList;

public class AIBoardAnalysis {
    private ArrayList<Integer> indexesOfUndieableMonsters;
    private ArrayList<Integer> indexesOfSelfDestructionMonsters;
    private ArrayList<Integer> indexesOfMonstersWithEffectSettingToZero;
    private ArrayList<Integer> indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters;
    private ArrayList<Integer> indexesOfMonstersWithHighAttack;
    private ArrayList<Integer> indexesOfMonstersWithGoodAttack;
    private ArrayList<Integer> indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells;
    private ArrayList<Integer> indexesOfMonstersWithHighDefense;
    private ArrayList<Integer> indexesOfMonstersWithNegatingEffects;
    private ArrayList<Integer> indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells;
    private ArrayList<Integer> indexesOfMonsterDestroyingSpellCards;
    private ArrayList<Integer> indexesOfAllMonstersDestroyingSpellCards;
    private ArrayList<Integer> indexesOfSpellTrapDestroyingSpellCards;
    private ArrayList<Integer> indexesOfSpellsDestroyingAllSpellCards;
    private ArrayList<Integer> indexesOfQuickSpellTrapDestroyingSpellCards;
    private ArrayList<Integer> indexesOfSpecialSummoningSpellCardsToBeActivated;
    private ArrayList<Integer> indexesOfMonsterDestroyingTrapCardsInSummoning;
    private ArrayList<Integer> indexesOfMonsterDestroyingTrapCardsInBattlePhase;
    private ArrayList<Integer> indexesOfSpecialSummoningTrapCardsToBeActivated;
    private ArrayList<Integer> indexesOfDamageDealingTrapCardsInBattlePhase;
    private ArrayList<Integer> indexesOfAttackNegatingTrapCardsInBattlePhase;
    private ArrayList<Integer> indexesOfMonstersWithDecentAttackButDefensePosition;
    private ArrayList<Integer> indexesOfEquipSpells;
    private ArrayList<Integer> indexesOfSpellFieldCards;
    private ArrayList<Integer> indexesOfStoppingMonstersFromAttackingSpellCards;
    private ArrayList<Integer> indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive;
    private ArrayList<Integer> indexesOfCardDrawingSpellCards;
    private ArrayList<Integer> indexesOfMonstersWorthyOfKilling;
    private ArrayList<Integer> indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition;
    private ArrayList<Integer> indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition;
    private ArrayList<Integer> indexesOfSpellTrapsWorthyOfKilling;
    private ArrayList<Integer> indexesOfSpellTrapsGoodToBeKilled;
    private ArrayList<Integer> indexesOfSelfDestructionMonstersWithNoDamageCalculations;
    private ArrayList<Integer> indexesOfMonsterSwappingSpellCards;
    private ArrayList<Integer> indexesOfMonstersWithStoppingTrapCardActivationEffect;
    private ArrayList<Integer> indexesOfCardDiscardingTrapCards;
    private ArrayList<Integer> indexesOfMonstersWorthyOfBeingTributes;
    private ArrayList<Integer> indexesOfMonstersNeeding2Tributes;
    private ArrayList<Integer> indexesOfMonstersNeeding3Tributes;

    public AIBoardAnalysis() {
        indexesOfUndieableMonsters = new ArrayList<>();
        indexesOfSelfDestructionMonsters = new ArrayList<>();
        indexesOfMonstersWithEffectSettingToZero = new ArrayList<>();
        indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters = new ArrayList<>();
        indexesOfMonstersWithHighAttack = new ArrayList<>();
        indexesOfMonstersWithGoodAttack = new ArrayList<>();
        indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells = new ArrayList<>();
        indexesOfMonstersWithHighDefense = new ArrayList<>();
        indexesOfMonstersWithNegatingEffects = new ArrayList<>();
        indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells = new ArrayList<>();
        indexesOfMonsterDestroyingSpellCards = new ArrayList<>();
        indexesOfAllMonstersDestroyingSpellCards = new ArrayList<>();
        indexesOfSpellTrapDestroyingSpellCards = new ArrayList<>();
        indexesOfSpellsDestroyingAllSpellCards = new ArrayList<>();
        indexesOfSpecialSummoningSpellCardsToBeActivated = new ArrayList<>();
        indexesOfQuickSpellTrapDestroyingSpellCards = new ArrayList<>();
        indexesOfMonsterDestroyingTrapCardsInSummoning = new ArrayList<>();
        indexesOfMonsterDestroyingTrapCardsInBattlePhase = new ArrayList<>();
        indexesOfSpecialSummoningTrapCardsToBeActivated = new ArrayList<>();
        indexesOfDamageDealingTrapCardsInBattlePhase = new ArrayList<>();
        indexesOfAttackNegatingTrapCardsInBattlePhase = new ArrayList<>();
        indexesOfMonstersWithDecentAttackButDefensePosition = new ArrayList<>();
        indexesOfEquipSpells = new ArrayList<>();
        indexesOfSpellFieldCards = new ArrayList<>();
        indexesOfStoppingMonstersFromAttackingSpellCards = new ArrayList<>();
        indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive = new ArrayList<>();
        indexesOfCardDrawingSpellCards = new ArrayList<>();
        indexesOfMonstersWorthyOfKilling = new ArrayList<>();
        indexesOfSpellTrapsWorthyOfKilling = new ArrayList<>();
        indexesOfSpellTrapsGoodToBeKilled = new ArrayList<>();
        indexesOfSelfDestructionMonstersWithNoDamageCalculations = new ArrayList<>();
        indexesOfMonsterSwappingSpellCards = new ArrayList<>();
        indexesOfMonstersWithStoppingTrapCardActivationEffect = new ArrayList<>();
        indexesOfCardDiscardingTrapCards = new ArrayList<>();
        indexesOfMonstersWorthyOfBeingTributes = new ArrayList<>();
        indexesOfMonstersNeeding2Tributes = new ArrayList<>();
        indexesOfMonstersNeeding3Tributes = new ArrayList<>();
        indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition = new ArrayList<>();
        indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition = new ArrayList<>();
    }

    public ArrayList<Integer> getIndexesOfUndieableMonsters() {
        return indexesOfUndieableMonsters;
    }

    public ArrayList<Integer> getIndexesOfSelfDestructionMonsters() {
        return indexesOfSelfDestructionMonsters;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithEffectSettingToZero() {
        return indexesOfMonstersWithEffectSettingToZero;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters() {
        return indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithHighAttack() {
        return indexesOfMonstersWithHighAttack;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithGoodAttack() {
        return indexesOfMonstersWithGoodAttack;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells() {
        return indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithHighDefense() {
        return indexesOfMonstersWithHighDefense;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithNegatingEffects() {
        return indexesOfMonstersWithNegatingEffects;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells() {
        return indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells;
    }

    public ArrayList<Integer> getIndexesOfMonsterDestroyingSpellCards() {
        return indexesOfMonsterDestroyingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfAllMonstersDestroyingSpellCards() {
        return indexesOfAllMonstersDestroyingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfSpellTrapDestroyingSpellCards() {
        return indexesOfSpellTrapDestroyingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfSpellsDestroyingAllSpellCards() {
        return indexesOfSpellsDestroyingAllSpellCards;
    }

    public ArrayList<Integer> getIndexesOfQuickSpellTrapDestroyingSpellCards() {
        return indexesOfQuickSpellTrapDestroyingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfMonsterDestroyingTrapCardsInSummoning() {
        return indexesOfMonsterDestroyingTrapCardsInSummoning;
    }

    public ArrayList<Integer> getIndexesOfSpecialSummoningSpellCardsToBeActivated() {
        return indexesOfSpecialSummoningSpellCardsToBeActivated;
    }

    public ArrayList<Integer> getIndexesOfMonsterDestroyingTrapCardsInBattlePhase() {
        return indexesOfMonsterDestroyingTrapCardsInBattlePhase;
    }

    public ArrayList<Integer> getIndexesOfSpecialSummoningTrapCardsToBeActivated() {
        return indexesOfSpecialSummoningTrapCardsToBeActivated;
    }

    public ArrayList<Integer> getIndexesOfDamageDealingTrapCardsInBattlePhase() {
        return indexesOfDamageDealingTrapCardsInBattlePhase;
    }

    public ArrayList<Integer> getIndexesOfAttackNegatingTrapCardsInBattlePhase() {
        return indexesOfAttackNegatingTrapCardsInBattlePhase;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithDecentAttackButDefensePosition() {
        return indexesOfMonstersWithDecentAttackButDefensePosition;
    }

    public ArrayList<Integer> getIndexesOfEquipSpells() {
        return indexesOfEquipSpells;
    }

    public ArrayList<Integer> getIndexesOfSpellFieldCards() {
        return indexesOfSpellFieldCards;
    }

    public ArrayList<Integer> getIndexesOfStoppingMonstersFromAttackingSpellCards() {
        return indexesOfStoppingMonstersFromAttackingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive() {
        return indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive;
    }

    public ArrayList<Integer> getIndexesOfCardDrawingSpellCards() {
        return indexesOfCardDrawingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfSpellTrapsWorthyOfKilling() {
        return indexesOfSpellTrapsWorthyOfKilling;
    }

    public ArrayList<Integer> getIndexesOfSpellTrapsGoodToBeKilled() {
        return indexesOfSpellTrapsGoodToBeKilled;
    }

    public ArrayList<Integer> getIndexesOfMonstersWorthyOfKilling() {
        return indexesOfMonstersWorthyOfKilling;
    }

    public ArrayList<Integer> getIndexesOfSelfDestructionMonstersWithNoDamageCalculations() {
        return indexesOfSelfDestructionMonstersWithNoDamageCalculations;
    }

    public ArrayList<Integer> getIndexesOfMonsterSwappingSpellCards() {
        return indexesOfMonsterSwappingSpellCards;
    }

    public ArrayList<Integer> getIndexesOfMonstersWithStoppingTrapCardActivationEffect() {
        return indexesOfMonstersWithStoppingTrapCardActivationEffect;
    }

    public ArrayList<Integer> getIndexesOfCardDiscardingTrapCards() {
        return indexesOfCardDiscardingTrapCards;
    }

    public ArrayList<Integer> getIndexesOfMonstersWorthyOfBeingTributes() {
        return indexesOfMonstersWorthyOfBeingTributes;
    }

    public ArrayList<Integer> getIndexesOfMonstersNeeding2Tributes() {
        return indexesOfMonstersNeeding2Tributes;
    }

    public ArrayList<Integer> getIndexesOfMonstersNeeding3Tributes() {
        return indexesOfMonstersNeeding3Tributes;
    }

    public ArrayList<Integer> getIndexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition() {
        return indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition;
    }

    public ArrayList<Integer> getIndexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition() {
        return indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition;
    }


    public void clearVariables() {
        indexesOfUndieableMonsters.clear();
        indexesOfSelfDestructionMonsters.clear();
        indexesOfMonstersWithEffectSettingToZero.clear();
        indexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters.clear();
        indexesOfMonstersWithHighAttack.clear();
        indexesOfMonstersWithGoodAttack.clear();
        indexesOfMonstersWithHighAttackPowerBecauseOfEquipOrFieldSpells.clear();
        indexesOfMonstersWithHighDefense.clear();
        indexesOfMonstersWithNegatingEffects.clear();
        indexesOfMonstersWithHighDefensePowerBecauseOfEquipOrFieldSpells.clear();
        indexesOfMonsterDestroyingSpellCards.clear();
        indexesOfAllMonstersDestroyingSpellCards.clear();
        indexesOfSpellTrapDestroyingSpellCards.clear();
        indexesOfSpellsDestroyingAllSpellCards.clear();
        indexesOfQuickSpellTrapDestroyingSpellCards.clear();
        indexesOfSpecialSummoningSpellCardsToBeActivated.clear();
        indexesOfMonsterDestroyingTrapCardsInSummoning.clear();
        indexesOfMonsterDestroyingTrapCardsInBattlePhase.clear();
        indexesOfSpecialSummoningTrapCardsToBeActivated.clear();
        indexesOfDamageDealingTrapCardsInBattlePhase.clear();
        indexesOfAttackNegatingTrapCardsInBattlePhase.clear();
        indexesOfMonstersWithDecentAttackButDefensePosition.clear();
        indexesOfEquipSpells.clear();
        indexesOfSpellFieldCards.clear();
        indexesOfStoppingMonstersFromAttackingSpellCards.clear();
        indexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive.clear();
        indexesOfCardDrawingSpellCards.clear();
        indexesOfMonstersWorthyOfKilling.clear();
        indexesOfMonstersWorthyOfKillingAndFaceUpAttackPosition.clear();
        indexesOfMonstersGoodToBeKilledAndFaceUpAttackPosition.clear();
        indexesOfSpellTrapsWorthyOfKilling.clear();
        indexesOfSpellTrapsGoodToBeKilled.clear();
        indexesOfSelfDestructionMonstersWithNoDamageCalculations.clear();
        indexesOfMonsterSwappingSpellCards.clear();
        indexesOfMonstersWithStoppingTrapCardActivationEffect.clear();
        indexesOfCardDiscardingTrapCards.clear();
        indexesOfMonstersWorthyOfBeingTributes.clear();
        indexesOfMonstersNeeding2Tributes.clear();
        indexesOfMonstersNeeding3Tributes.clear();
    }
}
