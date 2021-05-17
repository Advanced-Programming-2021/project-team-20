package controller.duel.GamePackage;

import controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import controller.duel.GamePhaseControllers.*;
import controller.duel.PreliminaryPackage.GameManager;

import java.util.ArrayList;

public class AI {
    private boolean doesOpponentHaveMonstersThatDontDie;
    private boolean doesOpponentHaveTrapThatKillsAIMonsterCardsInBattlePhase;
    private boolean doesOpponentHaveTrapThatKillsAIMonsterCardsInSummoning;
    private int numberOfTrapCardsThanCanKillAIMonsters;
    private int numberOfOpponentCardsInHand;
    private boolean doesOpponentHaveTheOverallEdgeInTheGame;
    private boolean isSafeToSummonOrSetDefensiveMonsters;
    private boolean isSafeToSummonOrSetNormalMonsters;
    private boolean isSafeToSummonOrSetStrongMonsters;
    private boolean isSafeToSetOrActivateSpells;
    private boolean doesAIHaveDefensiveTrapsInSpellZoneForBattlePhase;
    private boolean doesAIHaveDefensiveTrapsInHandForBattlePhase;
    private boolean doesAIHaveDefensiveMonstersInHand;
    private boolean doesAIHaveDefensiveMonstersInMonsterZone;
    private ArrayList<Integer> defensiveMonstersInHand;
    private ArrayList<Integer> normalMonstersInHand;
    private ArrayList<Integer> strongMonstersInHand;

    public AI() {
        doesOpponentHaveMonstersThatDontDie = false;
        doesOpponentHaveTheOverallEdgeInTheGame = false;
        doesOpponentHaveTrapThatKillsAIMonsterCardsInBattlePhase = false;
        doesOpponentHaveTrapThatKillsAIMonsterCardsInSummoning = false;
        isSafeToSetOrActivateSpells = true;
    }

    public String getCommand() {

        updateAIInformationAccordingToBoard();
        return "next phase\n";
    }

    private String getCommandWhenProgramIsWaitingToChooseIfAIWantsToActivateMonsterEffect() {
        return "";
    }

    private String getCommandWhenProgramIsWaitingToChooseIfAIWantsToActivateSpellTrap() {
        return "";
    }


    private String understandQuery() {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(0);
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(0);
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(0);
        FlipSummonController flipSummonController = GameManager.getFlipSummonControllerByIndex(0);
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(0);
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(0);
        SetCardController setCardController = GameManager.getSetCardControllerByIndex(0);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        AttackMonsterToMonsterController attackMonsterToMonsterController = GameManager.getAttackMonsterToMonsterControllerByIndex(0);
        AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager.getAttackMonsterToMonsterConductorsByIndex(0);
        DirectAttackController directAttackController = GameManager.getDirectAttackControllerByIndex(0);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(0);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        int aiTurn = duelController.getAiTurn();
        PhaseInGame phaseInGame = phaseController.getPhaseInGame();
        if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_DRAW_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_DRAW_PHASE)) {

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_STANDBY_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_STANDBY_PHASE)) {

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)) {

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE)) {

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2)) {

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.ALLY_END_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_END_PHASE)){

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)){

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)){

        } else if (aiTurn == 1 && phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2) || aiTurn == 2 && phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)){

        }
        return "next phase";
    }
    private String understandQueryInMainPhase1AITurn(){
        return "";
    }

    private void updateAIInformationAccordingToBoard() {

    }
}
