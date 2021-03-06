package project.server.controller.duel.GamePackage.ai;

import project.model.aidata.AIFurtherActivationInput;
import project.server.controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import project.server.controller.duel.GamePackage.ActionConductors.FlipSummoningOrChangingCardPositionConductor;
import project.server.controller.duel.GamePackage.ActionConductors.NormalSummonConductor;
import project.server.controller.duel.GamePackage.DuelController;
import project.model.PhaseInGame;
import project.server.controller.duel.GamePhaseControllers.*;
import project.server.controller.duel.PreliminaryPackage.GameManager;

public class AIQueryUnderstander {
    protected String understandQuery(AI ai) {
        ai.updateAIInformationAccordingToBoard();
        ai.getAiMainPhaseMind().getAiKeyVariablesUpdater().updateVariablesOfThisClassAccordingToSituation(ai);
        ai.getAiBattlePhaseMind().getAiKeyVariablesUpdater().updateVariablesOfThisClassAccordingToSituation(ai);
        AIBattlePhaseMind aiBattlePhaseMind = ai.getAiBattlePhaseMind();
        AIMainPhaseMind aiMainPhaseMind = ai.getAiMainPhaseMind();
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(ai.getToken());
        DuelController duelController = GameManager.getDuelControllerByIndex(ai.getToken());
        int aiTurn = duelController.getAiTurn();
        PhaseInGame phaseInGame = phaseController.getPhaseInGame();
        String whatAISays;
        if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_DRAW_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_DRAW_PHASE)) {
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": next phase*", ai.getToken());
            return "next phase";
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_STANDBY_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_STANDBY_PHASE)) {
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": next phase*", ai.getToken());
            return "next phase";
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {
            whatAISays = understandQueryInMainPhaseAITurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {
            whatAISays = understandQueryInBattlePhaseAITurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {
            whatAISays = understandQueryInMainPhaseAITurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_END_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_END_PHASE)) {
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": next phase*", ai.getToken());
            return "next phase";
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)) {
            whatAISays = understandQueryInMainPhase1OpponentTurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)) {
            whatAISays = understandQueryInBattlePhaseOpponentTurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
            whatAISays = understandQueryInMainPhase2OpponentTurn(ai);
            GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": " + whatAISays + "*", ai.getToken());
            return whatAISays;
        }
        GameManager.getDuelControllerByIndex(ai.getToken()).addStringToWhatUsersSay("*user" + aiTurn + ": next phase*", ai.getToken());
        return "next phase";
    }

    private String understandQueryInMainPhaseAITurn(AI ai) {
        AIMainPhaseMind aiMainPhaseMind = ai.getAiMainPhaseMind();
        AIBoardUnderstander aiBoardUnderstander = ai.getAiBoardUnderstander();
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(ai.getToken());
        if (normalSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return aiMainPhaseMind.getCommandForChoosingMonstersForTributesInNormalSummoning(ai);
        }
        if (NormalSummonConductor.isPromptingUserToChooseMonsterToDestroy()){
            return "select --opponent --monster 1";
        }
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(ai.getToken());
        if (specialSummonController.isClassWaitingForCardToBeDiscarded()) {
            return aiMainPhaseMind.getCommandForDiscardingCardsInSpecialSummoning(ai);
        }
        if (specialSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return aiMainPhaseMind.getCommandForChoosingMonstersForTributesInSpecialSummoning(ai);
        }
        if (specialSummonController.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()){
            ai.setAiFurtherActivationInput(AIFurtherActivationInput.NOTHING);
            return "attacking";
        }
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(ai.getToken());
        if (tributeSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return aiMainPhaseMind.getCommandForChoosingMonstersForTributesInTributeSummoning(ai);
        }
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(ai.getToken());
        if (activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return aiMainPhaseMind.getCommandForGivingFurtherInputForActivatingSpellOrTrap(ai);
        }
        if (activateSpellTrapController.isAreWeLookingForACardNameToBeInserted()) {
            return aiMainPhaseMind.getCommandForGivingACardName(ai);
        }
        if (activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateSpellTrapForThirdTimeInChain(ai);
        }
        if (activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            return aiMainPhaseMind.getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        if (FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateMonsterFlipEffectInMainPhase(ai);
        }
        if (FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            return aiMainPhaseMind.getCommandForChoosingMonsterToDestroy(ai);
        }
        return aiMainPhaseMind.getCommandWhenAIIsInMainPhaseWithNoNecessaryQuery(ai);
    }


    private String understandQueryInBattlePhaseAITurn(AI ai) {
        AIBattlePhaseMind aiBattlePhaseMind = ai.getAiBattlePhaseMind();
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(ai.getToken());
        if (activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return aiBattlePhaseMind.getCommandForChoosingToActivateSpellTrapForThirdTimeInChain(ai);
        }
        if (activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            return ai.getAiMainPhaseMind().getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        if (activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return ai.getAiMainPhaseMind().getCommandForGivingFurtherInputForActivatingSpellOrTrap(ai);
        }
        return aiBattlePhaseMind.getCommandWhenAIIsInBattlePhaseWithNoNecessaryQuery(ai);
    }

    private String understandQueryInMainPhase1OpponentTurn(AI ai) {
        AIMainPhaseMind aiMainPhaseMind = ai.getAiMainPhaseMind();
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(ai.getToken());
        if (normalSummonController.isGoingToChangeTurnsForChaining()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(ai);
        }
        if (normalSummonController.isClassWaitingForChainCardToBeSelected()) {
            return aiMainPhaseMind.getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(ai.getToken());
        if (specialSummonController.isGoingToChangeTurnsForChaining()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(ai);
        }
        if (specialSummonController.isClassWaitingForChainCardToBeSelected()) {
            return aiMainPhaseMind.getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(ai.getToken());
        if (tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(ai);
        }
        if (tributeSummonController.isClassWaitingForChainCardToBeSelected()) {
            return aiMainPhaseMind.getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(ai.getToken());
        if (activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return aiMainPhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInMainPhase1(ai);
        }
        if (activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            return aiMainPhaseMind.getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        if (activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return aiMainPhaseMind.getCommandForGivingFurtherInputToActivateSpellTrapInChainInMainPhase1(ai);
        }
        return "no";
    }

    private String understandQueryInBattlePhaseOpponentTurn(AI ai) {
        AIBattlePhaseMind aiBattlePhaseMind = ai.getAiBattlePhaseMind();
        AttackMonsterToMonsterController attackMonsterToMonsterController = GameManager.getAttackMonsterToMonsterControllerByIndex(ai.getToken());
        if (attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return aiBattlePhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInBattlePhase(ai);
        }
        if (attackMonsterToMonsterController.isClassWaitingForChainCardToBeSelected()) {
            return aiBattlePhaseMind.getCommandForChoosingSpellTrapToActivateInChainInBattlePhase(ai);
        }
        DirectAttackController directAttackController = GameManager.getDirectAttackControllerByIndex(ai.getToken());
        if (directAttackController.isGoingToChangeTurnsForChaining()) {
            return aiBattlePhaseMind.getCommandForChoosingToActivateTrapInChainOrNotInBattlePhase(ai);
        }
        if (directAttackController.isClassWaitingForChainCardToBeSelected()) {
            return aiBattlePhaseMind.getCommandForChoosingSpellTrapToActivateInChainInBattlePhase(ai);
        }
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(ai.getToken());
        if (activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return aiBattlePhaseMind.getCommandForGivingFurtherInputToActivateSpellTrapInChainInBattlePhase(ai);
        }
        if (activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return "yes";
        }
        if (activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            return ai.getAiMainPhaseMind().getCommandForChoosingSpellTrapToActivateInChainInMainPhase1(ai);
        }
        AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager.getAttackMonsterToMonsterConductorsByIndex(ai.getToken());
        if (attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return aiBattlePhaseMind.getCommandForChoosingToActivateMonsterEffectInChainInBattlePhase(ai);
        }
        if (attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            return aiBattlePhaseMind.getCommandForChoosingMonsterToSpecialSummonInChainInBattlePhase(ai);
        }
        if (attackMonsterToMonsterConductor.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()) {
            return aiBattlePhaseMind.getCommandForChoosingAttackPositionOrDefensePositionInChainInBattlePhase(ai);
        }
        if (attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            return aiBattlePhaseMind.getCommandForChoosingMonsterToDestroyInChainInBattlePhase(ai);
        }
        return "no";
    }

    private String understandQueryInMainPhase2OpponentTurn(AI ai) {
        return understandQueryInMainPhase1OpponentTurn(ai);
    }
}
