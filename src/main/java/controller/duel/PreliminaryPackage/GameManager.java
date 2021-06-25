package controller.duel.PreliminaryPackage;

import java.util.ArrayList;

import controller.duel.GamePackage.ai.AI;
import controller.duel.GamePackage.Action;
import controller.duel.GamePackage.ChangeCardsBetweenTwoRounds;
import controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import controller.duel.GamePackage.ActionConductors.ContinuousMonsterEffectController;
import controller.duel.GamePackage.DuelBoard;
import controller.duel.GamePackage.DuelController;
import controller.duel.GamePackage.SetTurnForGame;
import controller.duel.GamePhaseControllers.*;
import model.Deck;
import model.cardData.General.Card;

public class GameManager {
    public static ArrayList<DuelController> duelControllerList = new ArrayList<>();
    public static ArrayList<DuelBoard> duelBoardList = new ArrayList<>();
    public static ArrayList<ActivateSpellTrapController> activateSpellTrapControllers = new ArrayList<>();
    public static ArrayList<ActivateMonsterController> activateMonsterControllers = new ArrayList<>();
    public static ArrayList<AttackMonsterToMonsterController> attackMonsterToMonsterControllers = new ArrayList<>();
    public static ArrayList<AttackMonsterToMonsterConductor> attackMonsterToMonsterConductors = new ArrayList<>();
    public static ArrayList<BattlePhaseController> battlePhaseControllers = new ArrayList<>();
    public static ArrayList<ChainController> chainControllers = new ArrayList<>();
    public static ArrayList<ChangeCardPositionController> changeCardPositionControllers = new ArrayList<>();
    public static ArrayList<DirectAttackController> directAttackControllers = new ArrayList<>();
    public static ArrayList<ContinuousMonsterEffectController> continuousMonsterEffectControllers = new ArrayList<>();
    // public static ArrayList<MainPhaseController> mainPhaseControllers = new
    // ArrayList<>();
    public static ArrayList<NormalSummonController> normalSummonControllers = new ArrayList<>();
    public static ArrayList<FlipSummonController> flipSummonControllers = new ArrayList<>();
    public static ArrayList<SpecialSummonController> specialSummonControllers = new ArrayList<>();
    public static ArrayList<TributeSummonController> tributeSummonControllers = new ArrayList<>();
    public static ArrayList<PhaseController> phaseControllers = new ArrayList<>();
    public static ArrayList<SelectCardController> selectCardControllers = new ArrayList<>();
    public static ArrayList<SetCardController> setCardControllers = new ArrayList<>();
    public static ArrayList<SummonSetCommonClass> summonSetCommonClasses = new ArrayList<>();
    public static ArrayList<ArrayList<Action>> actions = new ArrayList<>();
    private static ArrayList<SetTurnForGame> setTurnForGames = new ArrayList<>();
    private static ArrayList<ChangeCardsBetweenTwoRounds> changeCardsBetweenTwoRounds = new ArrayList<>();
    public static ArrayList<ArrayList<Action>> uninterruptedActions = new ArrayList<>();
    public static ArrayList<AI> ais = new ArrayList<>();

    public void addANewGame(Deck firstPlayerActiveDeck, ArrayList<Card> firstPlayerMainDeck,
            ArrayList<Card> firstPlayerSideDeck, Deck secondPlayerActiveDeck, ArrayList<Card> secondPlayerMainDeck,
            ArrayList<Card> secondPlayerSideDeck, String firstPlayerUsername, String secondPlayerUsername,
            int numberOfRounds) {
        duelControllerList.add(new DuelController(firstPlayerUsername, secondPlayerUsername, numberOfRounds));
        duelBoardList.add(new DuelBoard(firstPlayerMainDeck, secondPlayerMainDeck));
        activateSpellTrapControllers.add(new ActivateSpellTrapController());
        activateMonsterControllers.add(new ActivateMonsterController());
        attackMonsterToMonsterControllers.add(new AttackMonsterToMonsterController());
        attackMonsterToMonsterConductors.add(new AttackMonsterToMonsterConductor());
        battlePhaseControllers.add(new BattlePhaseController());
        chainControllers.add(new ChainController());
        changeCardPositionControllers.add(new ChangeCardPositionController());
        directAttackControllers.add(new DirectAttackController());
        continuousMonsterEffectControllers.add(new ContinuousMonsterEffectController());
        // mainPhaseControllers.add(new MainPhaseController());
        normalSummonControllers.add(new NormalSummonController());
        flipSummonControllers.add(new FlipSummonController());
        specialSummonControllers.add(new SpecialSummonController());
        tributeSummonControllers.add(new TributeSummonController());
        selectCardControllers.add(new SelectCardController());
        setCardControllers.add(new SetCardController());
        summonSetCommonClasses.add(new SummonSetCommonClass());
        phaseControllers.add(new PhaseController());
        actions.add(new ArrayList<>());
        uninterruptedActions.add(new ArrayList<>());
        changeCardsBetweenTwoRounds.add(new ChangeCardsBetweenTwoRounds(firstPlayerActiveDeck, secondPlayerActiveDeck));
        setTurnForGames.add(new SetTurnForGame());
        ais.add(new AI());
    }

    public static void removeClassesWhenGameIsOver(int index) {
        duelControllerList.remove(index);
        duelBoardList.remove(index);
        activateSpellTrapControllers.remove(index);
        activateMonsterControllers.remove(index);
        attackMonsterToMonsterControllers.remove(index);
        attackMonsterToMonsterConductors.remove(index);
        battlePhaseControllers.remove(index);
        chainControllers.remove(index);
        changeCardPositionControllers.remove(index);
        directAttackControllers.remove(index);
        continuousMonsterEffectControllers.remove(index);
        // mainPhaseControllers.remove(index);
        normalSummonControllers.remove(index);
        flipSummonControllers.remove(index);
        specialSummonControllers.remove(index);
        tributeSummonControllers.remove(index);
        selectCardControllers.remove(index);
        setCardControllers.remove(index);
        summonSetCommonClasses.remove(index);
        phaseControllers.remove(index);
        actions.remove(index);
        uninterruptedActions.remove(index);
        changeCardsBetweenTwoRounds.remove(index);
        setTurnForGames.remove(index);
        ais.remove(index);
    }

    public static DuelController getDuelControllerByIndex(int index) {
        return duelControllerList.get(index);
    }

    public static DuelBoard getDuelBoardByIndex(int index) {
        return duelBoardList.get(index);
    }

    public static ActivateSpellTrapController getActivateSpellTrapControllerByIndex(int index) {
        return activateSpellTrapControllers.get(index);
    }

    public static ActivateMonsterController getActivateMonsterControllerByIndex(int index) {
        return activateMonsterControllers.get(index);
    }

    public static AttackMonsterToMonsterController getAttackMonsterToMonsterControllerByIndex(int index) {
        return attackMonsterToMonsterControllers.get(index);
    }

    public static AttackMonsterToMonsterConductor getAttackMonsterToMonsterConductorsByIndex(int index) {
        return attackMonsterToMonsterConductors.get(index);
    }

    public static BattlePhaseController battlePhaseController(int index) {
        return battlePhaseControllers.get(index);
    }

    public static ChainController getChainControllerByIndex(int index) {
        return chainControllers.get(index);
    }

    public static ChangeCardPositionController getChangeCardPositionController(int index) {
        return changeCardPositionControllers.get(index);
    }

    public static DirectAttackController getDirectAttackControllerByIndex(int index) {
        return directAttackControllers.get(index);
    }

    public static FlipSummonController getFlipSummonControllerByIndex(int index) {
        return flipSummonControllers.get(index);
    }

    public static ContinuousMonsterEffectController getContinuousMonsterEffectControllersByIndex(int index) {
        return continuousMonsterEffectControllers.get(index);
    }

    public static NormalSummonController getNormalSummonControllerByIndex(int index) {
        return normalSummonControllers.get(index);
    }

    public static SpecialSummonController getSpecialSummonControllerByIndex(int index) {
        return specialSummonControllers.get(index);
    }

    public static TributeSummonController getTributeSummonControllerByIndex(int index) {
        return tributeSummonControllers.get(index);
    }

    public static SelectCardController getSelectCardControllerByIndex(int index) {
        return selectCardControllers.get(index);
    }

    public static SetCardController getSetCardControllerByIndex(int index) {
        return setCardControllers.get(index);
    }

    public static ArrayList<Action> getActionsByIndex(int index) {
        return actions.get(index);
    }

    public static ArrayList<Action> getUninterruptedActionsByIndex(int index) {
        return uninterruptedActions.get(index);
    }

    public static AI getAIByIndex(int index) {
        return ais.get(index);
    }

    public static SummonSetCommonClass getSummonSetCommonClassByIndex(int index) {
        return summonSetCommonClasses.get(index);
    }

    public static PhaseController getPhaseControllerByIndex(int index) {
        return phaseControllers.get(index);
    }

    public static SetTurnForGame getSetTurnForGamesByIndex(int index) {
        return setTurnForGames.get(index);
    }

    public static ChangeCardsBetweenTwoRounds getChangeCardsBetweenTwoRoundsByIndex(int index) {
        return changeCardsBetweenTwoRounds.get(index);
    }


    public static void clearAllVariablesOfThisIndex(int index){
        duelControllerList.get(index).clearAllVariablesOfThisClass();
        duelBoardList.get(index).clearAllVariablesOfThisClass();
        activateSpellTrapControllers.get(index).clearAllVariablesOfThisClass();
        activateMonsterControllers.get(index).clearAllVariablesOfThisClass();
        attackMonsterToMonsterControllers.get(index).clearAllVariablesOfThisClass();
        attackMonsterToMonsterConductors.get(index).clearAllVariablesOfThisClass();
        battlePhaseControllers.get(index).clearAllVariablesOfThisClass();
        chainControllers.get(index).clearAllVariablesOfThisClass();
        changeCardPositionControllers.get(index).clearAllVariablesOfThisClass();
        directAttackControllers.get(index).clearAllVariablesOfThisClass();
        // mainPhaseControllers.add(new MainPhaseController());
        normalSummonControllers.get(index).clearAllVariablesOfThisClass();
        flipSummonControllers.get(index).clearAllVariablesOfThisClass();
        specialSummonControllers.get(index).clearAllVariablesOfThisClass();
        tributeSummonControllers.get(index).clearAllVariablesOfThisClass();
        selectCardControllers.get(index).clearAllVariablesOfThisClass();
        setCardControllers.get(index).clearAllVariablesOfThisClass();
        summonSetCommonClasses.get(index).clearAllVariablesOfThisClass();
        phaseControllers.get(index).clearAllVariablesOfThisClass();
        actions.get(index).clear();
        uninterruptedActions.get(index).clear();
        //changeCardsBetweenTwoRounds.add(new ChangeCardsBetweenTwoRounds(firstPlayerActiveDeck, secondPlayerActiveDeck));
        //setTurnForGames.add(new SetTurnForGame());
    }
}
