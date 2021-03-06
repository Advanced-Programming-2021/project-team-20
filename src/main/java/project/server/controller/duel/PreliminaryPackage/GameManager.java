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
        DoubleToken doubleToken = new DoubleToken(firstUserToken, secondUserToken);

//        new DuelController(firstPlayerUsername, secondPlayerUsername, numberOfRounds, firstUserToken, secondUserToken);

        duelControllerHashMap.put(doubleToken, new DuelController(firstPlayerUsername, secondPlayerUsername, numberOfRounds, firstUserToken, secondUserToken));
        // duelControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(firstUserToken)).startDuel(firstUserToken);
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
        changeCardsBetweenTwoRoundsHashMap.put(doubleToken, new ChangeCardsBetweenTwoRounds(firstUserToken, firstPlayerActiveDeck, secondUserToken, secondPlayerActiveDeck, secondPlayerUsername.equals("AI")));
        // setTurnForGameHashMap.put(doubleToken, new SetTurnForGame());
        aiHashMap.put(doubleToken, new AI(secondUserToken));

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

    public static boolean shouldEndGameBeCalled = false;
    public static int timesInput = 0;

    public static void clearNecessaryObjects(String token) {
        timesInput++;
        if (timesInput >= 2) {
            timesInput = 0;
            if (shouldEndGameBeCalled) {
                if (DoubleToken.getDoubleTokenByOneToken(token) != null) {
                    if (DuelStarter.getGameManager().superAlmightyChangesStringHashMap.containsKey(DoubleToken.getDoubleTokenByOneToken(token))) {
                        DuelStarter.getGameManager().superAlmightyChangesStringHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
                        DuelStarter.getGameManager().availableCardLocationForUseForClientHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
                        DuelStarter.getGameManager().changesInLifePointsToBeGivenToClientHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
                        DuelStarter.getGameManager().whatUsersSayHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
                        DuelStarter.getGameManager().wholeReportToClientHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
                        DoubleToken.deleteSuchExistence(DoubleToken.getDoubleTokenByOneToken(token));
                    }
                }
            } else {
                System.out.println("END OF GAME CALLED");
                if (DoubleToken.getDoubleTokenByOneToken(token) != null) {
                    System.out.println("UN NULL");
                    if (DuelStarter.getGameManager().superAlmightyChangesStringHashMap.containsKey(DoubleToken.getDoubleTokenByOneToken(token))) {
                        System.out.println("UNNULL");
                        DuelStarter.getGameManager().superAlmightyChangesStringHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
                        DuelStarter.getGameManager().availableCardLocationForUseForClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
                        DuelStarter.getGameManager().changesInLifePointsToBeGivenToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
                        DuelStarter.getGameManager().whatUsersSayHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
                        DuelStarter.getGameManager().wholeReportToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
                        DoubleToken.getDoubleTokenByOneToken(token).clearYourselfUp();
                    }
                }
            }
            shouldEndGameBeCalled = false;
        }
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
        return continuousMonsterEffectControllerHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
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
        getChangeCardsBetweenTwoRoundsByIndex(token).resetFieldsAfterOneRoundOfDuel();
        //setTurnForGames.add(new SetTurnForGame());

    }


    public HashMap<DoubleToken, String> superAlmightyChangesStringHashMap = new HashMap<>();
    public HashMap<DoubleToken, String> availableCardLocationForUseForClientHashMap = new HashMap<>();
    public HashMap<DoubleToken, String> changesInLifePointsToBeGivenToClientHashMap = new HashMap<>();
    // private String superAlmightyChangesString = "";
    // private String availableCardLocationForUseForClient = "";
    // private String changesInLifePointsToBeGivenToClient = "";
    public String whatUsersSay = "";
    public HashMap<DoubleToken, String> whatUsersSayHashMap = new HashMap<>();
    public HashMap<DoubleToken, String> wholeReportToClientHashMap = new HashMap<>();


    public void addStringToChangesInLifePointsToBeGivenToClient(String string, String token) {
        String currentString = changesInLifePointsToBeGivenToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        currentString += string;
        currentString += "\n";
        changesInLifePointsToBeGivenToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
        System.out.println("If you will allow me changesInLifePoints is adding\n" + string + "\nto wholeReport");
        currentString = wholeReportToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        currentString += "&";
        currentString += string;
        currentString += "\n";
        wholeReportToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
    }


    public void addStringToSuperAlmightyString(String string, String token) {
        String superAlmightyChangesString = superAlmightyChangesStringHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        superAlmightyChangesString += string;
        superAlmightyChangesString += "\n";
        superAlmightyChangesStringHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), superAlmightyChangesString);
        System.out.println("If you will allow me superAlmightyString is adding\n" + string + "\nto wholeReport");
        String wholeReportToClient = wholeReportToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        wholeReportToClient += string;
        wholeReportToClient += "\n";
        wholeReportToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), wholeReportToClient);
    }


    // private HashMap<DoubleToken, String> availableCardLocationForUseForClientHashMap = new HashMap<>();

    public String getAvailableCardLocationForUseForClient(String token) {
        String[] lines = availableCardLocationForUseForClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token)).split("\n");
        System.out.println("giveAvailableCardLocationForUseToClient: ");
        System.out.println("String[] lines");
        for (int i = 0; i < lines.length; i++) {
            System.out.println("lines[" + i + "]= " + lines[i]);
        }
        int linesRead = DoubleToken.getDoubleTokenByOneToken(token).getLinesReadByToken(token);
        System.out.println("linesRead = " + linesRead);
        String output = "";
        output += lines[linesRead];
        System.out.println("output is now= " + output);
        output += "\n";
        output += lines[linesRead + 1];
        output += "\n";
        System.out.println("output is finally= " + output);
        return output;
    }

    public void addStringToAvailableCardLocationForUseForClient(CardLocation string, String token) {
        if (string != null) {
            System.out.println("@@@@@@@@@@@@@@Available Card Location is " + string.getRowOfCardLocation() + " "
                + string.getIndex());
            String currentString = availableCardLocationForUseForClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
            currentString += string.getRowOfCardLocation();
            currentString += "\n";
            currentString += string.getIndex();
            currentString += "\n";
            availableCardLocationForUseForClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
        }
    }

    public void clearAvailableCardLocationForUseForClient(String token) {
        DoubleToken doubleToken = DoubleToken.getDoubleTokenByOneToken(token);
        doubleToken.setLinesReadByToken(token, doubleToken.getLinesReadByToken(token) + 2);
    }


    public void addStringToWhatUsersSay(String string, String token) {
        String currentString = whatUsersSayHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        currentString += string;
        currentString += "\n";
        //whatUsersSayHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
        whatUsersSayHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
        System.out.println("If you will allow me whatUsersSay is adding\n" + string + "\nto wholeReport");
        currentString = wholeReportToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        currentString += string;
        currentString += "\n";
        //wholeReportToClientHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
        wholeReportToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
        //wholeReportToClient += string;
        //wholeReportToClient += "\n";
    }


    public String getWholeReportToClient(String token) {
        String[] lines = wholeReportToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token)).split("\n");
        System.out.println("getWholeReportToClient: ");
        System.out.println("String[] lines");
        for (int i = 0; i < lines.length; i++) {
            System.out.println("lines[" + i + "]= " + lines[i]);
        }
        int linesRead = DoubleToken.getDoubleTokenByOneToken(token).getLinesReadFromWholeReportToClientByToken(token);
        System.out.println("linesRead = " + linesRead);
        String output = "";
        // int newLinesReading = 0;
        for (int i = 0; i < lines.length - linesRead; i++) {
            System.out.println("Adding Line!: " + lines[linesRead + i]);
            output += lines[linesRead + i];
            output += "\n";
        }
        return output;
    }

    public void addStringToWholeReportToClient(String string, String token) {
        String currentString = wholeReportToClientHashMap.get(DoubleToken.getDoubleTokenByOneToken(token));
        currentString += string;
        currentString += "\n";
        //wholeReportToClientHashMap.remove(DoubleToken.getDoubleTokenByOneToken(token));
        wholeReportToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), currentString);
    }

    public void clearChangesInLifePointsToBeGivenToClient(String token) {
        changesInLifePointsToBeGivenToClientHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
        //changesInLifePointsToBeGivenToClient = "";
    }

    public void clearSuperAlmightyString(String token) {
        superAlmightyChangesStringHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
    }

    public void clearWhatUsersSay(String token) {
        whatUsersSayHashMap.replace(DoubleToken.getDoubleTokenByOneToken(token), "");
    }

    public void clearWholeReportToClient(String token, boolean isBeingCalledAtTheRunningTime) {
        // maybe first method should be cleared sooner, in its original function
        clearChangesInLifePointsToBeGivenToClient(token);
        clearSuperAlmightyString(token);
        DoubleToken doubleToken = DoubleToken.getDoubleTokenByOneToken(token);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nclearing whole report from client " +
            (wholeReportToClientHashMap.get(doubleToken).split("\n").length));
        if (!isBeingCalledAtTheRunningTime) {
            doubleToken.setLinesReadFromWholeReportToClientByToken(token, wholeReportToClientHashMap.get(doubleToken).split("\n").length);
        }
    }

    public void addDoubleTokenToSuperAlmightyChangesStringHashMap(DoubleToken doubleToken) {
        superAlmightyChangesStringHashMap.put(doubleToken, "");
    }

    public void addDoubleTokenToAvailableCardLocationForUseForClientHashMap(DoubleToken doubleToken) {
        availableCardLocationForUseForClientHashMap.put(doubleToken, "");
    }

    public void addDoubleTokenToChangesInLifePointsToBeGivenToClientHashMap(DoubleToken doubleToken) {
        changesInLifePointsToBeGivenToClientHashMap.put(doubleToken, "");
    }

    public void addDoubleTokenToWhatUsersSayHashMap(DoubleToken doubleToken) {
        whatUsersSayHashMap.put(doubleToken, "");
    }

    public void addDoubleTokenToWholeReportToClientHashMap(DoubleToken doubleToken) {
        wholeReportToClientHashMap.put(doubleToken, "");
    }
}


class DoubleToken {
    private String firstPlayerToken;
    private String secondPlayerToken;
    private int firstLinesRead;
    private int secondLinesRead;
    private static ArrayList<DoubleToken> doubleTokens = new ArrayList<>();
    private int firstLinesReadFromWholeReportToClient;
    private int secondLinesReadFromWholeReportToClient;
    //actually minus one

    public DoubleToken(String firstPlayerToken, String secondPlayerToken) {
        this.firstPlayerToken = firstPlayerToken;
        this.secondPlayerToken = secondPlayerToken;
        this.firstLinesRead = 0;
        this.secondLinesRead = 0;
        this.firstLinesReadFromWholeReportToClient = 0;
        this.secondLinesReadFromWholeReportToClient = 0;
        doubleTokens.add(this);
        ClientMessageReceiver.addDoubleTokenToIndirectMessages(this);
        DuelStarter.getGameManager().addDoubleTokenToSuperAlmightyChangesStringHashMap(this);
        DuelStarter.getGameManager().addDoubleTokenToAvailableCardLocationForUseForClientHashMap(this);
        DuelStarter.getGameManager().addDoubleTokenToChangesInLifePointsToBeGivenToClientHashMap(this);
        DuelStarter.getGameManager().addDoubleTokenToWhatUsersSayHashMap(this);
        DuelStarter.getGameManager().addDoubleTokenToWholeReportToClientHashMap(this);
    }

    public int getLinesReadByToken(String token) {
        if (firstPlayerToken.equals(token)) {
            return firstLinesRead;
        } else {
            return secondLinesRead;
        }
    }

    public void setLinesReadByToken(String token, int linesRead) {
        if (firstPlayerToken.equals(token)) {
            this.firstLinesRead = linesRead;
        } else {
            this.secondLinesRead = linesRead;
        }
    }

    public int getLinesReadFromWholeReportToClientByToken(String token) {
        if (firstPlayerToken.equals(token)) {
            return firstLinesReadFromWholeReportToClient;
        } else {
            return secondLinesReadFromWholeReportToClient;
        }
    }

    public void setLinesReadFromWholeReportToClientByToken(String token, int linesRead) {
        if (firstPlayerToken.equals(token)) {
            this.firstLinesReadFromWholeReportToClient = linesRead;
        } else {
            this.secondLinesReadFromWholeReportToClient = linesRead;
        }
    }

    public String getFirstPlayerToken() {
        return firstPlayerToken;
    }

    public String getSecondPlayerToken() {
        return secondPlayerToken;
    }

    public static DoubleToken getDoubleTokenByOneToken(String token) {
        for (int i = 0; i < doubleTokens.size(); i++) {
            if (doubleTokens.get(i).getFirstPlayerToken().equals(token) || doubleTokens.get(i).getSecondPlayerToken().equals(token)) {
                return doubleTokens.get(i);
            }
        }
        return null;
    }

    public void clearYourselfUp() {
        this.firstLinesRead = 0;
        this.secondLinesRead = 0;
        this.firstLinesReadFromWholeReportToClient = 0;
        this.secondLinesReadFromWholeReportToClient = 0;
    }

    public static void deleteSuchExistence(DoubleToken doubleToken) {
        doubleTokens.remove(doubleToken);
    }
}
