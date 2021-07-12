package project.server.controller.duel.PreliminaryPackage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import project.server.controller.duel.GamePackage.ai.AI;
import project.server.controller.duel.GamePackage.Action;
import project.server.controller.duel.GamePackage.ChangeCardsBetweenTwoRounds;
import project.server.controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import project.server.controller.duel.GamePackage.ActionConductors.ContinuousMonsterEffectController;
import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.GamePackage.SetTurnForGame;
import project.server.controller.duel.GamePhaseControllers.*;
import project.model.Deck;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;

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


    private static HashMap<DoubleToken, DuelController> duelControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, DuelBoard> duelBoardHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ActivateSpellTrapController> activateSpellTrapControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ActivateMonsterController> activateMonsterControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, AttackMonsterToMonsterConductor> attackMonsterToMonsterConductorHashMap = new HashMap<>();
    private static HashMap<DoubleToken, AttackMonsterToMonsterController> attackMonsterToMonsterControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, BattlePhaseController> battlePhaseControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ChainController> chainControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ChangeCardPositionController> changeCardPositionControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, DirectAttackController> directAttackControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ContinuousMonsterEffectController> continuousMonsterEffectControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, NormalSummonController> normalSummonControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, FlipSummonController> flipSummonControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, SpecialSummonController> specialSummonControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, TributeSummonController> tributeSummonControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, PhaseController> phaseControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, SelectCardController> selectCardControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, SetCardController> setCardControllerHashMap = new HashMap<>();
    private static HashMap<DoubleToken, SummonSetCommonClass> summonSetCommonClassHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ArrayList<Action>> actionsHashMap = new HashMap<>();
    private static HashMap<DoubleToken, SetTurnForGame> setTurnForGameHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ChangeCardsBetweenTwoRounds> changeCardsBetweenTwoRoundsHashMap = new HashMap<>();
    private static HashMap<DoubleToken, ArrayList<Action>> uninterruptedActionsHashMap = new HashMap<>();
    private static HashMap<DoubleToken, AI> aiHashMap = new HashMap<>();

    public void addANewGame(Deck firstPlayerActiveDeck, ArrayList<Card> firstPlayerMainDeck,
                            ArrayList<Card> firstPlayerSideDeck, Deck secondPlayerActiveDeck, ArrayList<Card> secondPlayerMainDeck,
                            ArrayList<Card> secondPlayerSideDeck, String firstPlayerUsername, String secondPlayerUsername,
                            int numberOfRounds, String firstUserToken, String secondUserToken) {
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

        DoubleToken doubleToken = new DoubleToken(firstUserToken, secondUserToken);

        duelControllerHashMap.put(doubleToken, new DuelController(firstPlayerUsername, secondPlayerUsername, numberOfRounds));
        duelBoardHashMap.put(doubleToken, new DuelBoard(firstPlayerMainDeck, secondPlayerMainDeck));
        activateSpellTrapControllerHashMap.put(doubleToken, new ActivateSpellTrapController());
        activateMonsterControllerHashMap.put(doubleToken, new ActivateMonsterController());
        attackMonsterToMonsterControllerHashMap.put(doubleToken, new AttackMonsterToMonsterController());
        attackMonsterToMonsterConductorHashMap.put(doubleToken, new AttackMonsterToMonsterConductor());
        battlePhaseControllerHashMap.put(doubleToken, new BattlePhaseController());
        chainControllerHashMap.put(doubleToken, new ChainController());
        changeCardPositionControllerHashMap.put(doubleToken, new ChangeCardPositionController());
        directAttackControllerHashMap.put(doubleToken, new DirectAttackController());
        continuousMonsterEffectControllerHashMap.put(doubleToken, new ContinuousMonsterEffectController());
        normalSummonControllerHashMap.put(doubleToken, new NormalSummonController());
        flipSummonControllerHashMap.put(doubleToken, new FlipSummonController());
        specialSummonControllerHashMap.put(doubleToken, new SpecialSummonController());
        tributeSummonControllerHashMap.put(doubleToken, new TributeSummonController());
        selectCardControllerHashMap.put(doubleToken, new SelectCardController());
        setCardControllerHashMap.put(doubleToken, new SetCardController());
        summonSetCommonClassHashMap.put(doubleToken, new SummonSetCommonClass());
        phaseControllerHashMap.put(doubleToken, new PhaseController());
        actionsHashMap.put(doubleToken, new ArrayList<>());
        uninterruptedActionsHashMap.put(doubleToken, new ArrayList<>());
        changeCardsBetweenTwoRoundsHashMap.put(doubleToken, new ChangeCardsBetweenTwoRounds(firstPlayerActiveDeck, secondPlayerActiveDeck));
        setTurnForGameHashMap.put(doubleToken, new SetTurnForGame());
        aiHashMap.put(doubleToken, new AI());

    }

    public static void removeClassesWhenGameIsOver(String token) {
        duelControllerHashMap.remove(token);
        duelBoardHashMap.remove(token);
        activateSpellTrapControllerHashMap.remove(token);
        activateMonsterControllerHashMap.remove(token);
        attackMonsterToMonsterControllerHashMap.remove(token);
        attackMonsterToMonsterConductorHashMap.remove(token);
        battlePhaseControllerHashMap.remove(token);
        chainControllerHashMap.remove(token);
        changeCardPositionControllerHashMap.remove(token);
        directAttackControllerHashMap.remove(token);
        continuousMonsterEffectControllerHashMap.remove(token);
        // mainPhaseControllers.remove(index);
        normalSummonControllerHashMap.remove(token);
        flipSummonControllerHashMap.remove(token);
        specialSummonControllerHashMap.remove(token);
        tributeSummonControllerHashMap.remove(token);
        selectCardControllerHashMap.remove(token);
        setCardControllerHashMap.remove(token);
        summonSetCommonClassHashMap.remove(token);
        phaseControllerHashMap.remove(token);
        actionsHashMap.remove(token);
        uninterruptedActionsHashMap.remove(token);
        changeCardsBetweenTwoRoundsHashMap.remove(token);
        setTurnForGameHashMap.remove(token);
        aiHashMap.remove(token);
    }


    public static DuelController getDuelControllerByIndex(String token) {
        return duelControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));

    }

    public static DuelBoard getDuelBoardByIndex(String token) {
        return duelBoardHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ActivateSpellTrapController getActivateSpellTrapControllerByIndex(String token) {
        return activateSpellTrapControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ActivateMonsterController getActivateMonsterControllerByIndex(String token) {
        return activateMonsterControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static AttackMonsterToMonsterController getAttackMonsterToMonsterControllerByIndex(String token) {
        return attackMonsterToMonsterControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static AttackMonsterToMonsterConductor getAttackMonsterToMonsterConductorsByIndex(String token) {
        return attackMonsterToMonsterConductorHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static BattlePhaseController getBattlePhaseControllerByIndex(String token) {
        return battlePhaseControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ChainController getChainControllerByIndex(String token) {
        return chainControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ChangeCardPositionController getChangeCardPositionController(String token) {
        return changeCardPositionControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static DirectAttackController getDirectAttackControllerByIndex(String token) {
        return directAttackControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static FlipSummonController getFlipSummonControllerByIndex(String token) {
        return flipSummonControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ContinuousMonsterEffectController getContinuousMonsterEffectControllersByIndex(String token) {
        return continuousMonsterEffectControllerHashMap.get(token);
    }

    public static NormalSummonController getNormalSummonControllerByIndex(String token) {
        return normalSummonControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static SpecialSummonController getSpecialSummonControllerByIndex(String token) {
        return specialSummonControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static TributeSummonController getTributeSummonControllerByIndex(String token) {
        return tributeSummonControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static SelectCardController getSelectCardControllerByIndex(String token) {
        return selectCardControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static SetCardController getSetCardControllerByIndex(String token) {
        return setCardControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ArrayList<Action> getActionsByIndex(String token) {
        return actionsHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ArrayList<Action> getUninterruptedActionsByIndex(String token) {
        return uninterruptedActionsHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static AI getAIByIndex(String token) {
        return aiHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static SummonSetCommonClass getSummonSetCommonClassByIndex(String token) {
        return summonSetCommonClassHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static PhaseController getPhaseControllerByIndex(String token) {
        return phaseControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static SetTurnForGame getSetTurnForGamesByIndex(String token) {
        return setTurnForGameHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static ChangeCardsBetweenTwoRounds getChangeCardsBetweenTwoRoundsByIndex(String token) {
        return changeCardsBetweenTwoRoundsHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
    }

    public static void clearAllVariablesOfThisIndex(String token) {
        getDuelControllerByIndex(token).clearAllVariablesOfThisClass();
        getDuelBoardByIndex(token).clearAllVariablesOfThisClass();
        getActivateSpellTrapControllerByIndex(token).clearAllVariablesOfThisClass();
        getActivateMonsterControllerByIndex(token).clearAllVariablesOfThisClass();
        getAttackMonsterToMonsterControllerByIndex(token).clearAllVariablesOfThisClass();
        getAttackMonsterToMonsterConductorsByIndex(token).clearAllVariablesOfThisClass();
        getBattlePhaseControllerByIndex(token).clearAllVariablesOfThisClass();
        getChainControllerByIndex(token).clearAllVariablesOfThisClass();
        getChangeCardPositionController(token).clearAllVariablesOfThisClass();
        getDirectAttackControllerByIndex(token).clearAllVariablesOfThisClass();
        // mainPhaseControllers.add(new MainPhaseController());
        getNormalSummonControllerByIndex(token).clearAllVariablesOfThisClass();
        getFlipSummonControllerByIndex(token).clearAllVariablesOfThisClass();
        getSpecialSummonControllerByIndex(token).clearAllVariablesOfThisClass();
        getTributeSummonControllerByIndex(token).clearAllVariablesOfThisClass();
        getSelectCardControllerByIndex(token).clearAllVariablesOfThisClass();
        getSetCardControllerByIndex(token).clearAllVariablesOfThisClass();
        getSummonSetCommonClassByIndex(token).clearAllVariablesOfThisClass();
        getPhaseControllerByIndex(token).clearAllVariablesOfThisClass();
        getActionsByIndex(token).clear();
        getUninterruptedActionsByIndex(token).clear();
        //changeCardsBetweenTwoRounds.add(new ChangeCardsBetweenTwoRounds(firstPlayerActiveDeck, secondPlayerActiveDeck));
        //setTurnForGames.add(new SetTurnForGame());
    }


    private String superAlmightyChangesString = "";
    private String availableCardLocationForUseForClient = "";
    private String changesInLifePointsToBeGivenToClient = "";


    public String getChangesInLifePointsToBeGivenToClient() {
        return changesInLifePointsToBeGivenToClient;
    }

    public void addStringToChangesInLifePointsToBeGivenToClient(String string) {
        changesInLifePointsToBeGivenToClient += string;
        changesInLifePointsToBeGivenToClient += "\n";
        System.out.println("If you will allow me changesInLifePoints is adding\n" + string + "\nto wholeReport");
        wholeReportToClient += "&";
        wholeReportToClient += string;
        wholeReportToClient += "\n";
    }

    public void clearChangesInLifePointsToBeGivenToClient() {
        changesInLifePointsToBeGivenToClient = "";
    }

    public String getSuperAlmightyChangesString() {
        return superAlmightyChangesString;
    }

    public void addStringToSuperAlmightyString(String string) {
        superAlmightyChangesString += string;
        superAlmightyChangesString += "\n";
        System.out.println("If you will allow me superAlmightyString is adding\n" + string + "\nto wholeReport");
        wholeReportToClient += string;
        wholeReportToClient += "\n";
    }

    public void clearSuperAlmightyString() {
        superAlmightyChangesString = "";
    }

    public String getAvailableCardLocationForUseForClient() {
        return availableCardLocationForUseForClient;
    }

    public void addStringToAvailableCardLocationForUseForClient(CardLocation string) {
        if (string != null) {
            System.out.println("@@@@@@@@@@@@@@Available Card Location is " + string.getRowOfCardLocation() + " "
                + string.getIndex());
            availableCardLocationForUseForClient += string.getRowOfCardLocation();
            availableCardLocationForUseForClient += "\n";
            availableCardLocationForUseForClient += string.getIndex();
            availableCardLocationForUseForClient += "\n";
        }
    }

    public void clearAvailableCardLocationForUseForClient() {
        int timesSeenNextLine = 0;
        while (timesSeenNextLine < 2) {
            if (availableCardLocationForUseForClient.charAt(0) == '\n') {
                timesSeenNextLine++;
            }
            availableCardLocationForUseForClient = availableCardLocationForUseForClient.substring(1);
        }

        // availableCardLocationForUseForClient = "";
    }


    private String whatUsersSay = "";


    public void addStringToWhatUsersSay(String string) {
        whatUsersSay += string;
        whatUsersSay += "\n";
        System.out.println("If you will allow me whatUsersSay is adding\n" + string + "\nto wholeReport");
        wholeReportToClient += string;
        wholeReportToClient += "\n";
    }

    public void clearWhatUsersSay() {
        whatUsersSay = "";
    }

    private String wholeReportToClient = "";

    public String getWholeReportToClient() {
        return wholeReportToClient;
    }

    public void addStringToWholeReportToClient(String string) {
        wholeReportToClient += string;
        wholeReportToClient += "\n";
    }

    public void clearWholeReportToClient() {
        // maybe first method should be cleared sooner, in its original function
        // clearAvailableCardLocationForUseForClient();
        clearChangesInLifePointsToBeGivenToClient();
        clearSuperAlmightyString();
        wholeReportToClient = "";
    }


}


class DoubleToken{
    private String firstPlayerToken;
    private String secondPlayerToken;
    private static ArrayList<DoubleToken> doubleTokens = new ArrayList<>();

    public DoubleToken(String firstPlayerToken, String secondPlayerToken){
        this.firstPlayerToken = firstPlayerToken;
        this.secondPlayerToken = secondPlayerToken;
        doubleTokens.add(this);
    }

    public String getFirstPlayerToken() {
        return firstPlayerToken;
    }

    public String getSecondPlayerToken() {
        return secondPlayerToken;
    }
    public static DoubleToken getDoubleTokenByOneToken(String token){
        for (int i = 0; i < doubleTokens.size(); i++){
            if (doubleTokens.get(i).getFirstPlayerToken().equals(token) || doubleTokens.get(i).getSecondPlayerToken().equals(token)){
                return doubleTokens.get(i);
            }
        }
        return null;
    }
}
