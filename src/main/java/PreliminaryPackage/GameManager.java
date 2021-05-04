package PreliminaryPackage;

import CardData.General.Card;
import GamePackage.Action;
import GamePackage.DuelBoard;
import GamePackage.DuelController;
import GamePhaseControllers.*;
import com.sun.tools.javac.Main;

import java.util.ArrayList;

public class GameManager {
    public static ArrayList<DuelController> duelControllerList = new ArrayList<>();
    public static ArrayList<DuelBoard> duelBoardList = new ArrayList<>();
    public static ArrayList<ActivateSpellTrapController> activateSpellTrapControllers = new ArrayList<>();
    public static ArrayList<AttackMonsterToMonsterController> attackMonsterToMonsterControllers = new ArrayList<>();
    public static ArrayList<BattlePhaseController> battlePhaseControllers = new ArrayList<>();
    public static ArrayList<ChainController> chainControllers = new ArrayList<>();
    public static ArrayList<ChangeCardPositionController> changeCardPositionControllers = new ArrayList<>();
    public static ArrayList<DirectAttackController> directAttackControllers = new ArrayList<>();
    public static ArrayList<FlipSummonController> flipSummonControllers = new ArrayList<>();
    public static ArrayList<MainPhaseController> mainPhaseControllers = new ArrayList<>();
    public static ArrayList<NormalSummonController> normalSummonControllers = new ArrayList<>();
    public static ArrayList<PhaseController> phaseControllers = new ArrayList<>();
    public static ArrayList<SelectCardController> selectCardControllers = new ArrayList<>();
    public static ArrayList<SetCardController> setCardControllers = new ArrayList<>();
    public static ArrayList<SummonSetCommonClass> summonSetCommonClasses = new ArrayList<>();
    public static ArrayList<ArrayList<Action>> actions = new ArrayList<>();
    public static ArrayList<ArrayList<Action>> uninterruptedActions = new ArrayList<>();

    public void addANewGame(ArrayList<Card> firstPlayerDeck, ArrayList<Card> secondPlayerDeck, String firstPlayerUsername, String secondPlayerUsername) {
        duelControllerList.add(new DuelController(firstPlayerUsername, secondPlayerUsername));
        duelBoardList.add(new DuelBoard(firstPlayerDeck, secondPlayerDeck));
        activateSpellTrapControllers.add(new ActivateSpellTrapController());
        attackMonsterToMonsterControllers.add(new AttackMonsterToMonsterController());
        battlePhaseControllers.add(new BattlePhaseController());
        chainControllers.add(new ChainController());
        changeCardPositionControllers.add(new ChangeCardPositionController());
        directAttackControllers.add(new DirectAttackController());
        flipSummonControllers.add(new FlipSummonController());
        mainPhaseControllers.add(new MainPhaseController());
        normalSummonControllers.add(new NormalSummonController());
        selectCardControllers.add(new SelectCardController());
        setCardControllers.add(new SetCardController());
        summonSetCommonClasses.add(new SummonSetCommonClass());
        phaseControllers.add(new PhaseController());
        actions.add(new ArrayList<>());
        uninterruptedActions.add(new ArrayList<>());
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

    public static AttackMonsterToMonsterController getAttackMonsterToMonsterControllerByIndex(int index) {
        return attackMonsterToMonsterControllers.get(index);
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

    public static MainPhaseController getMainPhaseControllerByIndex(int index) {
        return mainPhaseControllers.get(index);
    }

    public static NormalSummonController getNormalSummonController(int index) {
        return normalSummonControllers.get(index);
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

    public static ArrayList<Action> getUninterruptedActionsByIndex(int index){
        return uninterruptedActions.get(index);
    }
    public static SummonSetCommonClass getSummonSetCommonClassByIndex(int index) {
        return summonSetCommonClasses.get(index);
    }

    public static PhaseController getPhaseControllerByIndex(int index) {
        return phaseControllers.get(index);
    }


}
