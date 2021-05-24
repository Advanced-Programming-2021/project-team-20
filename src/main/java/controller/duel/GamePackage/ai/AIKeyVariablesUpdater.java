package controller.duel.GamePackage.ai;

import model.AIBoardAnalysis;

public class AIKeyVariablesUpdater {
    //private boolean doesOpponentHaveMonstersWIthHighAttackInHisGraveyard;
    private boolean doesOpponentHaveSpecialSummoningSpellTrapCards;
    private boolean canOpponentInterruptAISpellWithQuickSpells;
    private boolean doesOpponentHaveMonsterDestroyingTrapCardsInSummoning;
    private boolean doesOpponentHaveMonsterDestroyingTrapCardsInAttacking;

    private boolean doesOpponentHaveMonsterWithHighAttackPowerInBoard;
    private boolean doesOpponentHaveMonsterWithSelfDestructionEffect;
    private boolean doesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation;
    private boolean doesOpponentHaveUndieableMonstersInBoard;
    private boolean doesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard;

    private boolean doesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand;

    private boolean doesOpponentHaveDefensiveTrapCardsInBoard;

    private boolean doesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard;
    private boolean doesOpponentHaveMonstersWithHighAttackInOpponentGraveyard;
    private boolean doesOpponentHaveUndieableMonstersInOpponentGraveyard;
    private boolean doesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard;

    private boolean doesAIHaveSelfDestructionMonstersInAIGraveyard;
    private boolean doesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard;
    private boolean doesAIHaveMonstersWithHighAttackInAIGraveyard;

    private boolean doesAIHaveUndieableMonstersInBoard;
    //private boolean doesAIHaveMonstersWithSelfDestructionEffectsInBoard;
    private boolean doesAIHaveMonstersWithNegatingEffectsInBoard;
    private boolean doesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters;
    private boolean doesAIHaveSpecialSummoningTrapCardsInBoard;
    private boolean doesAIHaveMonstersWithDecentAttackButDefensePositionInBoard;
    private boolean doesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand;

    private boolean doesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard;
    private boolean doesAIHaveMonstersNeeding2TributesInHand;
    private boolean doesAIHaveMonstersNeeding3TributesInHand;
    private boolean doesAIHaveMonsterSwappingSpellCardsInBoard;
    private boolean doesAIHaveSpellDestroyingQuickSpellCardsInBoard;
    private boolean doesAIHaveCardDiscardingTrapCardsInBoard;
    private boolean doesAIHaveDefensiveTrapCardsInBoard;
    private boolean doesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard;
    private boolean doesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard;
    private boolean doesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard;
    private boolean doesAIHaveCardDrawingSpellCardsInHand;
    private boolean doesAIHaveMonsterSwappingSpellCardsInHand;
    private boolean doesAIHaveSpecialSummoningSpellCardsInHand;
    private boolean doesAIHaveSpellDestroyingAllSpellCardsInHand;
    private boolean doesAIHaveSpellDestroyingQuickSpellCardsInHand;

    private boolean doesAIHaveCardDiscardingTrapCardsInHand;
    private boolean doesAIHaveDefensiveTrapCardsInHand;
    private boolean doesAIHaveStoppingMonstersOfAttackingSpellCardsInHand;
    private boolean doesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand;
    private boolean doesAIHaveMonstersWithGoodAttackInHand;
    private boolean doesAIHaveMonstersWithHighAttackInHand;
    private boolean doesAIHaveDefensiveMonstersInHand;
    private boolean doesAIHaveUndieableMonstersInHand;

    public AIKeyVariablesUpdater() {
        clearVariables();
    }

    protected void updateVariablesOfThisClassAccordingToSituation(AI ai) {
        AIBoardAnalysis opponentMonsterSpellBoardAnalysis = ai.getAiBoardUnderstander().getOpponentMonsterSpellBoardAnalysis();
        AIBoardAnalysis opponentHandBoardAnalysis = ai.getAiBoardUnderstander().getOpponentHandBoardAnalysis();
        AIBoardAnalysis aiMonsterSpellBoardAnalysis = ai.getAiBoardUnderstander().getAIMonsterSpellBoardAnalysis();
        AIBoardAnalysis aiHandBoardAnalysis = ai.getAiBoardUnderstander().getAiHandBoardAnalysis();
        AIBoardAnalysis aiGraveyardBoardAnalysis = ai.getAiBoardUnderstander().getAiGraveyardBoardAnalysis();
        AIBoardAnalysis opponentGraveyardBoardAnalysis = ai.getAiBoardUnderstander().getOpponentGraveyardBoardAnalysis();
        if (aiMonsterSpellBoardAnalysis.getIndexesOfSpecialSummoningTrapCardsToBeActivated().size() > 0) {
            doesAIHaveSpecialSummoningTrapCardsInBoard = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfSpecialSummoningTrapCardsToBeActivated().size() > 0
            || opponentMonsterSpellBoardAnalysis.getIndexesOfSpecialSummoningSpellCardsToBeActivated().size() > 0
            || opponentHandBoardAnalysis.getIndexesOfSpecialSummoningSpellCardsToBeActivated().size() > 0) {
            doesOpponentHaveSpecialSummoningSpellTrapCards = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfQuickSpellTrapDestroyingSpellCards().size() > 0) {
            canOpponentInterruptAISpellWithQuickSpells = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInSummoning().size() > 0) {
            doesOpponentHaveMonsterDestroyingTrapCardsInSummoning = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase().size() > 0) {
            doesOpponentHaveMonsterDestroyingTrapCardsInAttacking = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfMonstersWithHighAttack().size() > 0) {
            doesOpponentHaveMonsterWithHighAttackPowerInBoard = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfSelfDestructionMonsters().size() > 0) {
            doesOpponentHaveMonsterWithSelfDestructionEffect = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfUndieableMonsters().size() > 0) {
            doesAIHaveUndieableMonstersInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersWithHighDefense().size() > 0
            || aiHandBoardAnalysis.getIndexesOfSelfDestructionMonsters().size() > 0
            || aiHandBoardAnalysis.getIndexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters().size() > 0
            || aiHandBoardAnalysis.getIndexesOfMonstersWithNegatingEffects().size() > 0) {
            doesAIHaveDefensiveMonstersInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersWithGoodAttack().size() > 0) {
            doesAIHaveMonstersWithGoodAttackInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersWithHighAttack().size() > 0) {
            doesAIHaveMonstersWithHighAttackInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonsterDestroyingSpellCards().size() > 0
            || aiMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingSpellCards().size() > 0) {
            doesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfSpellsDestroyingAllSpellCards().size() > 0) {
            doesAIHaveSpellDestroyingAllSpellCardsInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfQuickSpellTrapDestroyingSpellCards().size() > 0) {
            doesAIHaveSpellDestroyingQuickSpellCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfQuickSpellTrapDestroyingSpellCards().size() > 0) {
            doesAIHaveSpellDestroyingQuickSpellCardsInBoard = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfUndieableMonsters().size() > 0) {
            doesAIHaveUndieableMonstersInBoard = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonstersWithNegatingEffects().size() > 0) {
            doesAIHaveMonstersWithNegatingEffectsInBoard = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfUndieableMonsters().size() > 0) {
            doesOpponentHaveUndieableMonstersInBoard = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase().size() +
            aiMonsterSpellBoardAnalysis.getIndexesOfDamageDealingTrapCardsInBattlePhase().size() +
            aiMonsterSpellBoardAnalysis.getIndexesOfSpecialSummoningTrapCardsToBeActivated().size() +
            aiMonsterSpellBoardAnalysis.getIndexesOfAttackNegatingTrapCardsInBattlePhase().size() >= 1) {
            doesAIHaveDefensiveTrapCardsInBoard = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase().size() +
            opponentMonsterSpellBoardAnalysis.getIndexesOfDamageDealingTrapCardsInBattlePhase().size() +
            opponentMonsterSpellBoardAnalysis.getIndexesOfSelfDestructionMonsters().size() >= 1) {
            doesOpponentHaveDefensiveTrapCardsInBoard = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase().size() +
            aiHandBoardAnalysis.getIndexesOfDamageDealingTrapCardsInBattlePhase().size() +
            aiHandBoardAnalysis.getIndexesOfAttackNegatingTrapCardsInBattlePhase().size() >= 1) {
            doesAIHaveDefensiveTrapCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonstersWithDecentAttackButDefensePosition().size() > 0) {
            doesAIHaveMonstersWithDecentAttackButDefensePositionInBoard = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfCardDrawingSpellCards().size() > 0) {
            doesAIHaveCardDrawingSpellCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfSelfDestructionMonstersWithNoDamageCalculations().size() > 0) {
            doesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonsterSwappingSpellCards().size() > 0) {
            doesAIHaveMonsterSwappingSpellCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonsterSwappingSpellCards().size() > 0) {
            doesAIHaveMonsterSwappingSpellCardsInBoard = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersWithStoppingTrapCardActivationEffect().size() > 0) {
            doesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonstersWithFlipEffectsIncludingDestroyingMonsters().size() > 0) {
            doesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCards().size() > 0) {
            doesAIHaveStoppingMonstersOfAttackingSpellCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCards().size() > 0) {
            doesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard = true;
        }
        if (opponentHandBoardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCards().size() > 0) {
            doesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand = true;
        }
        if (opponentMonsterSpellBoardAnalysis.getIndexesOfStoppingMonstersFromAttackingSpellCardsCurrentlyActive().size() > 0) {
            doesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfCardDiscardingTrapCards().size() > 0) {
            doesAIHaveCardDiscardingTrapCardsInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfCardDiscardingTrapCards().size() > 0) {
            doesAIHaveCardDiscardingTrapCardsInBoard = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfSpecialSummoningSpellCardsToBeActivated().size() > 0) {
            doesAIHaveSpecialSummoningSpellCardsInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersNeeding2Tributes().size() > 0) {
            doesAIHaveMonstersNeeding2TributesInHand = true;
        }
        if (aiHandBoardAnalysis.getIndexesOfMonstersNeeding3Tributes().size() > 0) {
            doesAIHaveMonstersNeeding3TributesInHand = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfMonsterDestroyingTrapCardsInBattlePhase().size() > 0) {
            doesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfDamageDealingTrapCardsInBattlePhase().size() > 0) {
            doesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard = true;
        }
        if (aiGraveyardBoardAnalysis.getIndexesOfMonstersWithEffectSettingToZero().size() > 0) {
            doesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard = true;
        }
        if (opponentGraveyardBoardAnalysis.getIndexesOfMonstersWithEffectSettingToZero().size() > 0) {
            doesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard = true;
        }
        if (opponentGraveyardBoardAnalysis.getIndexesOfMonstersWithHighAttack().size() > 0) {
            doesOpponentHaveMonstersWithHighAttackInOpponentGraveyard = true;
        }
        if (opponentGraveyardBoardAnalysis.getIndexesOfUndieableMonsters().size() > 0) {
            doesOpponentHaveUndieableMonstersInOpponentGraveyard = true;
        }
        if (opponentGraveyardBoardAnalysis.getIndexesOfSelfDestructionMonsters().size() > 0) {
            doesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard = true;
        }
        if (aiGraveyardBoardAnalysis.getIndexesOfSelfDestructionMonstersWithNoDamageCalculations().size() > 0) {
            doesAIHaveSelfDestructionMonstersInAIGraveyard = true;
        }
        if (aiGraveyardBoardAnalysis.getIndexesOfMonstersWithHighAttack().size() > 0) {
            doesAIHaveMonstersWithHighAttackInAIGraveyard = true;
        }
        if (aiMonsterSpellBoardAnalysis.getIndexesOfAttackNegatingTrapCardsInBattlePhase().size() > 0) {
            doesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard = true;
        }
    }

    public boolean isDoesOpponentHaveSpecialSummoningSpellTrapCards() {
        return doesOpponentHaveSpecialSummoningSpellTrapCards;
    }

    public boolean isCanOpponentInterruptAISpellWithQuickSpells() {
        return canOpponentInterruptAISpellWithQuickSpells;
    }

    public boolean isDoesOpponentHaveMonsterDestroyingTrapCardsInSummoning() {
        return doesOpponentHaveMonsterDestroyingTrapCardsInSummoning;
    }

    public boolean isDoesOpponentHaveMonsterDestroyingTrapCardsInAttacking() {
        return doesOpponentHaveMonsterDestroyingTrapCardsInAttacking;
    }

    public boolean isDoesOpponentHaveMonsterWithHighAttackPowerInBoard() {
        return doesOpponentHaveMonsterWithHighAttackPowerInBoard;
    }

    public boolean isDoesOpponentHaveMonsterWithSelfDestructionEffect() {
        return doesOpponentHaveMonsterWithSelfDestructionEffect;
    }

    public boolean isDoesOpponentHaveUndieableMonstersInBoard() {
        return doesOpponentHaveUndieableMonstersInBoard;
    }

    public boolean isDoesAIHaveUndieableMonstersInBoard() {
        return doesAIHaveUndieableMonstersInBoard;
    }


    public boolean isDoesAIHaveMonstersWithNegatingEffectsInBoard() {
        return doesAIHaveMonstersWithNegatingEffectsInBoard;
    }

    public boolean isDoesAIHaveDefensiveMonstersInHand() {
        return doesAIHaveDefensiveMonstersInHand;
    }

    public boolean isDoesAIHaveUndieableMonstersInHand() {
        return doesAIHaveUndieableMonstersInHand;
    }

    public boolean isDoesAIHaveMonstersWithGoodAttackInHand() {
        return doesAIHaveMonstersWithGoodAttackInHand;
    }

    public boolean isDoesAIHaveMonstersWithHighAttackInHand() {
        return doesAIHaveMonstersWithHighAttackInHand;
    }

    public boolean isDoesAIHaveMonstersWithDecentAttackButDefensePositionInBoard() {
        return doesAIHaveMonstersWithDecentAttackButDefensePositionInBoard;
    }

    public boolean isDoesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand() {
        return doesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand;
    }

    public boolean isDoesAIHaveSpellDestroyingAllSpellCardsInHand() {
        return doesAIHaveSpellDestroyingAllSpellCardsInHand;
    }

    public boolean isDoesAIHaveSpellDestroyingQuickSpellCardsInHand() {
        return doesAIHaveSpellDestroyingQuickSpellCardsInHand;
    }

    public boolean isDoesAIHaveSpellDestroyingQuickSpellCardsInBoard() {
        return doesAIHaveSpellDestroyingQuickSpellCardsInBoard;
    }

    public boolean isDoesAIHaveDefensiveTrapCardsInBoard() {
        return doesAIHaveDefensiveTrapCardsInBoard;
    }

    public boolean isDoesAIHaveDefensiveTrapCardsInHand() {
        return doesAIHaveDefensiveTrapCardsInHand;
    }

    public boolean isDoesAIHaveCardDrawingSpellCardsInHand() {
        return doesAIHaveCardDrawingSpellCardsInHand;
    }

    public boolean isDoesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation() {
        return doesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation;
    }

    public boolean isDoesAIHaveMonsterSwappingSpellCardsInBoard() {
        return doesAIHaveMonsterSwappingSpellCardsInBoard;
    }

    public boolean isDoesAIHaveMonsterSwappingSpellCardsInHand() {
        return doesAIHaveMonsterSwappingSpellCardsInHand;
    }

    public boolean isDoesOpponentHaveDefensiveTrapCardsInBoard() {
        return doesOpponentHaveDefensiveTrapCardsInBoard;
    }

    public boolean isDoesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand() {
        return doesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand;
    }

    public boolean isDoesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters() {
        return doesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters;
    }

    public boolean isDoesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand() {
        return doesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand;
    }

    public boolean isDoesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard() {
        return doesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard;
    }

    public boolean isDoesAIHaveStoppingMonstersOfAttackingSpellCardsInHand() {
        return doesAIHaveStoppingMonstersOfAttackingSpellCardsInHand;
    }

    public boolean isDoesAIHaveCardDiscardingTrapCardsInBoard() {
        return doesAIHaveCardDiscardingTrapCardsInBoard;
    }

    public boolean isDoesAIHaveCardDiscardingTrapCardsInHand() {
        return doesAIHaveCardDiscardingTrapCardsInHand;
    }

    public boolean isDoesAIHaveSpecialSummoningSpellCardsInHand() {
        return doesAIHaveSpecialSummoningSpellCardsInHand;
    }

    public boolean isDoesAIHaveMonstersNeeding2TributesInHand() {
        return doesAIHaveMonstersNeeding2TributesInHand;
    }

    public boolean isDoesAIHaveMonstersNeeding3TributesInHand() {
        return doesAIHaveMonstersNeeding3TributesInHand;
    }

    public boolean isDoesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard() {
        return doesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard;
    }

    public boolean isDoesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard() {
        return doesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard;
    }

    public boolean isDoesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard() {
        return doesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard;
    }

    public boolean isDoesAIHaveSelfDestructionMonstersInAIGraveyard() {
        return doesAIHaveSelfDestructionMonstersInAIGraveyard;
    }

    public boolean isDoesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard() {
        return doesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard;
    }

    public boolean isDoesAIHaveMonstersWithHighAttackInAIGraveyard() {
        return doesAIHaveMonstersWithHighAttackInAIGraveyard;
    }

    public boolean isDoesAIHaveSpecialSummoningTrapCardsInBoard() {
        return doesAIHaveSpecialSummoningTrapCardsInBoard;
    }

    public boolean isDoesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard() {
        return doesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard;
    }

    public boolean isDoesOpponentHaveMonstersWithHighAttackInOpponentGraveyard() {
        return doesOpponentHaveMonstersWithHighAttackInOpponentGraveyard;
    }

    public boolean isDoesOpponentHaveUndieableMonstersInOpponentGraveyard() {
        return doesOpponentHaveUndieableMonstersInOpponentGraveyard;
    }

    public boolean isDoesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard() {
        return doesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard;
    }

    public boolean isDoesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard() {
        return doesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard;
    }

    public void clearVariables() {

        //doesOpponentHaveMonstersWIthHighAttackInHisGraveyard = false;
        doesOpponentHaveSpecialSummoningSpellTrapCards = false;
        canOpponentInterruptAISpellWithQuickSpells = false;
        doesOpponentHaveMonsterDestroyingTrapCardsInSummoning = false;
        doesOpponentHaveMonsterDestroyingTrapCardsInAttacking = false;

        doesOpponentHaveMonsterWithHighAttackPowerInBoard = false;
        doesOpponentHaveMonsterWithSelfDestructionEffect = false;
        doesOpponentHaveMonsterWithSelfDestructionEffectAndNoDamageCalculation = false;
        doesOpponentHaveUndieableMonstersInBoard = false;
        doesOpponentHaveStoppingMonstersOfAttackingSpellCardsCurrentlyActiveInBoard = false;

        doesOpponentHaveStoppingMonstersOfAttackingSpellCardsInHand = false;

        doesOpponentHaveDefensiveTrapCardsInBoard = false;

        doesOpponentHaveMonstersWithEffectSettingToZeroInOpponentGraveyard = false;
        doesOpponentHaveMonstersWithHighAttackInOpponentGraveyard = false;
        doesOpponentHaveUndieableMonstersInOpponentGraveyard = false;
        doesOpponentHaveMonstersWithSelfDestructionEffectInOpponentGraveyard = false;

        doesAIHaveSelfDestructionMonstersInAIGraveyard = false;
        doesAIHaveMonstersWithEffectSettingToZeroInAIGraveyard = false;
        doesAIHaveMonstersWithHighAttackInAIGraveyard = false;

        doesAIHaveUndieableMonstersInBoard = false;
        //doesAIHaveMonstersWithSelfDestructionEffectsInBoard = false;
        doesAIHaveMonstersWithNegatingEffectsInBoard = false;
        doesAIHaveMonstersWithFlipEffectsIncludingDestroyingMonsters = false;
        doesAIHaveSpecialSummoningTrapCardsInBoard = false;
        doesAIHaveMonstersWithDecentAttackButDefensePositionInBoard = false;
        doesAIHaveMonsterDestroyingSpellCardsEitherInBoardOrInHand = false;

        doesAIHaveStoppingMonstersOfAttackingSpellCardsInBoard = false;
        doesAIHaveMonstersNeeding2TributesInHand = false;
        doesAIHaveMonstersNeeding3TributesInHand = false;
        doesAIHaveMonsterSwappingSpellCardsInBoard = false;
        doesAIHaveSpellDestroyingQuickSpellCardsInBoard = false;
        doesAIHaveCardDiscardingTrapCardsInBoard = false;
        doesAIHaveDefensiveTrapCardsInBoard = false;
        doesAIHaveMonsterDestroyingTrapCardsInBattlePhaseInBoard = false;
        //doesAIHaveMonsterAttackNegatingTrapCardsInBattlePhaseInBoard = false;
        doesAIHaveDamageInflictingTrapCardsInBattlePhaseInBoard = false;
        doesAIHaveCardDrawingSpellCardsInHand = false;
        doesAIHaveMonsterSwappingSpellCardsInHand = false;
        doesAIHaveSpecialSummoningSpellCardsInHand = false;
        doesAIHaveSpellDestroyingAllSpellCardsInHand = false;
        doesAIHaveSpellDestroyingQuickSpellCardsInHand = false;

        doesAIHaveCardDiscardingTrapCardsInHand = false;
        doesAIHaveDefensiveTrapCardsInHand = false;
        doesAIHaveStoppingMonstersOfAttackingSpellCardsInHand = false;
        doesAIHaveMonstersWithStoppingTrapCardActivationEffectInHand = false;
        doesAIHaveMonstersWithGoodAttackInHand = false;
        doesAIHaveMonstersWithHighAttackInHand = false;
        doesAIHaveDefensiveMonstersInHand = false;
        doesAIHaveUndieableMonstersInHand = false;


    }
}
