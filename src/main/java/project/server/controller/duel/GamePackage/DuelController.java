package project.server.controller.duel.GamePackage;

import java.util.ArrayList;
import java.util.HashMap;

import project.server.controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import project.server.controller.duel.GamePackage.ActionConductors.FlipSummoningOrChangingCardPositionConductor;
import project.server.controller.duel.GamePackage.ActionConductors.NormalSummonConductor;
import project.server.controller.duel.GamePackage.ai.AI;
import project.server.controller.duel.GamePhaseControllers.*;
import project.server.controller.duel.PreliminaryPackage.DuelStarter;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Utility.Utility;
import project.server.controller.duel.cheat.Cheat;
import project.server.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.CardLocation;
import project.model.PhaseInGame;

public class DuelController {
    // turn = 1 -> ALLY, turn = 2 -> OPPONENT
    private int totalTurnsUntilNow;
    private int turn;
    private int fakeTurn;
    private int numberOfRounds;
    private int currentRound;
    private boolean isPlayersChangedDecks;
    private boolean isTurnSetedBetweenTwoPlayerWhenRoundBegin;
    private boolean isGameOver;
    private boolean isAIPlaying;
    private int aiTurn;
    private ArrayList<String> playingUsers = new ArrayList<>();
    private ArrayList<Integer> lifePoints = new ArrayList<>();
    private ArrayList<Integer> maxLifePointOfPlayers = new ArrayList<>();
    private ArrayList<Integer> numberOfRoundsPlayersWon = new ArrayList<>();
    private ArrayList<Integer> playersScores = new ArrayList<>();
    private ArrayList<Boolean> canUserSummonOrSetMonsters = new ArrayList<>();
    private ArrayList<String> allInputs = new ArrayList<>();
    private String superAlmightyChangesString = "";
    private String availableCardLocationForUseForClient = "";
    private String changesInLifePointsToBeGivenToClient = "";

    public String getChangesInLifePointsToBeGivenToClient() {
        return changesInLifePointsToBeGivenToClient;
    }

    public void addStringToChangesInLifePointsToBeGivenToClient(String string, String token) {
        DuelStarter.getGameManager().addStringToChangesInLifePointsToBeGivenToClient(string, token);
    }


    public String getSuperAlmightyChangesString() {
        return superAlmightyChangesString;
    }

    public void addStringToSuperAlmightyString(String string, String token) {
        DuelStarter.getGameManager().addStringToSuperAlmightyString(string, token);
    }

    public void clearSuperAlmightyString() {
        superAlmightyChangesString = "";
    }

    public String getAvailableCardLocationForUseForClient(String token) {
        return DuelStarter.getGameManager().getAvailableCardLocationForUseForClient(token);
    }

    public void addStringToAvailableCardLocationForUseForClient(CardLocation string, String token) {
        DuelStarter.getGameManager().addStringToAvailableCardLocationForUseForClient(string, token);
    }

    public void clearAvailableCardLocationForUseForClient(String token) {
        DuelStarter.getGameManager().clearAvailableCardLocationForUseForClient(token);
    }

    private String whatUsersSay;

    public String getWhatUsersSay() {
        return whatUsersSay;
    }

    public void clearWhatUsersSay() {
        whatUsersSay = "";
    }

    public void addStringToWhatUsersSay(String string, String token) {
        DuelStarter.getGameManager().addStringToWhatUsersSay(string, token);
    }

    private String wholeReportToClient = "";


    private HashMap<String, UserAndTurn> tokenToUser = new HashMap<>();

    public int getTurnByToken(String token) {
        return tokenToUser.get(token).getTurn();
    }

    public DuelController(String firstUser, String secondUser, int numberOfRounds, String firstUserToken, String secondUsedToken) {
        fakeTurn = turn = 1;
        GameManager.getPhaseControllerByIndex(firstUserToken).setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_1);
        tokenToUser.put(firstUserToken, new UserAndTurn(firstUser, 1));
        tokenToUser.put(secondUsedToken, new UserAndTurn(secondUser, 2));
        this.numberOfRounds = numberOfRounds;
        currentRound = 1;
        clearAllVariablesOfThisClass();
        playingUsers.add(firstUser);
        playingUsers.add(secondUser);
        playersScores.clear();
        playersScores.add(0);
        playersScores.add(0);
        isPlayersChangedDecks = true;
        isTurnSetedBetweenTwoPlayerWhenRoundBegin = true;
        isGameOver = false;
        // currentRound = 1;
        // lifePoints.add(8000);
        // lifePoints.add(8000);
        // maxLifePointOfPlayers.add(0);
        // maxLifePointOfPlayers.add(0);
        // numberOfRoundsPlayersWon.add(0);
        // numberOfRoundsPlayersWon.add(0);
        // playersScores.add(0);
        // playersScores.add(0);
        // canUserSummonOrSetMonsters.add(true);
        // canUserSummonOrSetMonsters.add(true);
        // isPlayersChangedDecks = true;
        // isTurnSetedBetweenTwoPlayerWhenRoundBegin = true;
        // currentRound = 1;
        // isAIPlaying = false;
        // aiTurn = 0;
        if (firstUser.equals("AI")) {
            aiTurn = 1;
            isAIPlaying = true;
        } else if (secondUser.equals("AI")) {
            aiTurn = 2;
            isAIPlaying = true;
        }
        // totalTurnsUntilNow = 1;
    }

    //private boolean firstTimeStarting;
    public String getInput(String string, boolean needToMediate, String token) {
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
        allInputs.add(string);
        if (string.equals("show deck")) {
            return duelBoard.showDeck(fakeTurn);
        } else if (string.equals("show graveyard")) {
            return duelBoard.showGraveyard();
        }
        String appropriateInput = isThisInputAppropriateAccordingToPhase(string, token);
        if (!appropriateInput.equals("yes")) {
            return appropriateInput;
        }
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(token);
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(token);
        FlipSummonController flipSummonController = GameManager.getFlipSummonControllerByIndex(token);
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(token);
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(token);
        SetCardController setCardController = GameManager.getSetCardControllerByIndex(token);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(token);
        AttackMonsterToMonsterController attackMonsterToMonsterController = GameManager.getAttackMonsterToMonsterControllerByIndex(token);
        AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager.getAttackMonsterToMonsterConductorsByIndex(token);
        DirectAttackController directAttackController = GameManager.getDirectAttackControllerByIndex(token);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(token);

        ChangeCardsBetweenTwoRounds changeCardsBetweenTwoRounds = GameManager.getChangeCardsBetweenTwoRoundsByIndex(token);
        SetTurnForGame setTurnForGame = GameManager.getSetTurnForGamesByIndex(token);
//        if (firstTimeStarting){
//            lifePoints.set(0, 8000);
//            lifePoints.set(1,8000);
//            firstTimeStarting = false;
//        }
        if (string.startsWith("cheat")) {
            Cheat cheat = new Cheat();
            return cheat.findCheatCommand(string, token);
        } else if (string.equals("surrender")) {
            return endGame(-turn + 3, token);
        }

        if (!isPlayersChangedDecks) {
            // return changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(string, 0);
        } else if (!isTurnSetedBetweenTwoPlayerWhenRoundBegin) {
            return setTurnForGame.setTurnBetweenTwoPlayer(string, token);
        }

        // System.out.println("normalSummonController.isAreWeLookingForMonstersToBeTributed()"
        // + normalSummonController.isAreWeLookingForMonstersToBeTributed());
        if (string.startsWith("select") && normalSummonController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                System.out.println("B1");
                return output;
            } else {
                System.out.println("A1");
                return mediateOutputBeforeSendingToGameManager(
                    normalSummonController.redirectInputForMonsterTributing(token), needToMediate, token);
            }
        } else if (string.startsWith("select") && setCardController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B2");
                return output;
            } else {
                // System.out.println("A2");
                return mediateOutputBeforeSendingToGameManager(setCardController.redirectInput(token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && attackMonsterToMonsterController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B3");
                return output;
            } else {
                // System.out.println("A3");
                return mediateOutputBeforeSendingToGameManager(
                    attackMonsterToMonsterController.isSelectedCardCorrectForChainActivation(string, token),
                    needToMediate, token);
            }
        } else if (string.startsWith("select") && directAttackController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B4");
                return output;
            } else {
                // System.out.println("A4");
                return mediateOutputBeforeSendingToGameManager(
                    directAttackController.isSelectedCardCorrectForChainActivation(string, token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B4.5");
                return output;
            } else {
                // System.out.println("A4.5");
                return mediateOutputBeforeSendingToGameManager(
                    FlipSummoningOrChangingCardPositionConductor.checkGivenInputForMonsterToDestroy(token),
                    needToMediate, token);
            }
        } else if (string.startsWith("select")
            && NormalSummonConductor.isIsClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B4.8");
                return output;
            } else {
                // System.out.println("A4.8");
                return mediateOutputBeforeSendingToGameManager(
                    NormalSummonConductor.checkGivenInputForMonsterToSpecialSummon(token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B4.5");
                return output;
            } else {
                // System.out.println("A4.5");
                return mediateOutputBeforeSendingToGameManager(
                    attackMonsterToMonsterConductor.checkGivenInputForMonsterToDestroy(token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B5");
                return output;
            } else {
                // System.out.println("A5");
                return mediateOutputBeforeSendingToGameManager(
                    attackMonsterToMonsterConductor.checkGivenInputForMonsterEffectActivation(token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B5.5");
                return output;
            } else {
                // System.out.println("A5.5");
                return mediateOutputBeforeSendingToGameManager(
                    attackMonsterToMonsterConductor.isSelectedCardCorrectForChainActivation(string, token),
                    needToMediate, token);
            }
        } else if (string.startsWith("select") && normalSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B6");
                return output;
            } else {
                // System.out.println("A6");
                return mediateOutputBeforeSendingToGameManager(
                    normalSummonController.isSelectedCardCorrectForChainActivation(string, token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B7");
                return output;
            } else {
                // System.out.println("A7");
                return mediateOutputBeforeSendingToGameManager(activateSpellTrapController.redirectInput(token),
                    needToMediate, token);
            }
        } else if (string.startsWith("select")
            && activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B8");
                return output;
            } else {
                // System.out.println("A8");
                return mediateOutputBeforeSendingToGameManager(
                    activateSpellTrapController.isSelectedCardCorrectForChainActivation(string, token), needToMediate, token);
            }
        } else if (string.startsWith("select") && flipSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B9");
                return output;
            } else {
                // System.out.println("A9");
                return mediateOutputBeforeSendingToGameManager(
                    flipSummonController.isSelectedCardCorrectForChainActivation(string, token), needToMediate, token);
            }
        } else if (string.startsWith("select") && specialSummonController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B10");
                return output;
            } else {
                // System.out.println("A10");
                return mediateOutputBeforeSendingToGameManager(
                    specialSummonController.redirectInputForMonsterTributing(token), needToMediate, token);
            }
        } else if (string.startsWith("select") && specialSummonController.isClassWaitingForCardToBeDiscarded()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B11");
                return output;
            } else {
                // System.out.println("A11");
                return mediateOutputBeforeSendingToGameManager(
                    specialSummonController.redirectInputForCardsToBeDiscarded(token), needToMediate, token);
            }
        } else if (string.startsWith("select") && specialSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B12");
                return output;
            } else {
                // System.out.println("A12");
                return mediateOutputBeforeSendingToGameManager(
                    specialSummonController.isSelectedCardCorrectForChainActivation(string, token), needToMediate, token);
            }
        } else if (string.startsWith("select") && tributeSummonController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B13");
                return output;
            } else {
                // System.out.println("A13");
                return mediateOutputBeforeSendingToGameManager(
                    tributeSummonController.redirectInputForMonsterTributing(token), needToMediate, token);
            }
        } else if (string.startsWith("select") && activateMonsterController.isClassWaitingForUserToDiscardOneCard()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B14");
                return output;
            } else {
                // System.out.println("A14");
                return mediateOutputBeforeSendingToGameManager(
                    activateMonsterController.analyzeInputWhenClassIsWaitingToDiscardCardFromHand(token), needToMediate, token);
            }
        } else if (string.startsWith("select")
            && activateMonsterController.isClassWaitingForUserToChooseMonsterFromGraveyard()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B15");
                return output;
            } else {
                // System.out.println("A15");
                return mediateOutputBeforeSendingToGameManager(
                    activateMonsterController.analyzeInputWhenClassIsWaitingToChooseMonsterFromSelfGraveyard(token),
                    needToMediate, token);
            }
        } else if (string.startsWith("select")
            && activateMonsterController.isClassWaitingForUserToChooseMonsterFromOpponentGraveyard()) {
            String output = selectCardController.selectCardInputAnalysis(string, token);
            if (!output.equals("card selected")) {
                // System.out.println("B16");
                return output;
            } else {
                // System.out.println("A16");
                return mediateOutputBeforeSendingToGameManager(
                    activateMonsterController.analyzeInputWhenClassIsWaitingToChooseMonsterFromOpponentGraveyard(token),
                    needToMediate, token);
            }
        } else if (activateSpellTrapController.isAreWeLookingForACardNameToBeInserted()) {
            // System.out.println("AB17");
            return mediateOutputBeforeSendingToGameManager(activateSpellTrapController.redirectInput(token), needToMediate, token);
        } else if (string.startsWith("select")) {
            return mediateOutputBeforeSendingToGameManager(selectCardController.selectCardInputAnalysis(string, token),
                needToMediate, token);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive") || string.startsWith("finish sel"))
            && activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return mediateOutputBeforeSendingToGameManager(activateSpellTrapController.redirectInput(token), needToMediate, token);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive"))
            && specialSummonController.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()) {
            return mediateOutputBeforeSendingToGameManager(
                specialSummonController.redirectInputForAnalyzingAttackPositionOrDefensePosition(string, token),
                needToMediate, token);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive"))
            && attackMonsterToMonsterConductor.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterConductor.redirectInputForAnalyzingAttackPositionOrDefensePosition(string, token),
                needToMediate, token);
        } else if (string.startsWith("next")) {
            return mediateOutputBeforeSendingToGameManager(phaseController.phaseControllerInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("normal")) {
            return mediateOutputBeforeSendingToGameManager(
                normalSummonController.normalSummonInputAnalysis(string, "normal summon", token), needToMediate, token);
        } else if (string.startsWith("tribute")) {
            return mediateOutputBeforeSendingToGameManager(tributeSummonController.tributeSummonInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("special")) {
            return mediateOutputBeforeSendingToGameManager(specialSummonController.specialSummonInputAnalysis(string, token),
                needToMediate, token);
        } else if (Utility
            .isMatcherCorrectWithoutErrorPrinting(Utility.getCommandMatcher(string, "(?<=\\n|^)set(?=\\n|$)"))) {
            return mediateOutputBeforeSendingToGameManager(setCardController.setCardControllerInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("set")) {
            ChangeCardPositionController changeCardPositionController = GameManager.getChangeCardPositionController(token);
            return mediateOutputBeforeSendingToGameManager(
                changeCardPositionController.changeCardPositionInputAnalysis(string, token), needToMediate, token);
        } else if (string.startsWith("flip")) {
            return mediateOutputBeforeSendingToGameManager(flipSummonController.flipSummonInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("attack direct")) {
            return mediateOutputBeforeSendingToGameManager(directAttackController.attackInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("attack")) {
            return mediateOutputBeforeSendingToGameManager(attackMonsterToMonsterController.attackInputAnalysis(string, token),
                needToMediate, token);
        } else if (string.startsWith("activate")) {
            return mediateOutputBeforeSendingToGameManager(
                activateSpellTrapController.activateSpellTrapEffectInputAnalysis(string, token), needToMediate, token);
        } else if (string.equals("advanced show board")) {
            return duelBoard.showDuelBoard(token);
        } else if (string.equals("no") && attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterController.userReplyYesNoForChain(string, token), needToMediate, token);
        } else if (string.equals("yes") && attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterController.userReplyYesNoForChain(string, token), needToMediate, token);
        } else if (string.equals("no") && directAttackController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(directAttackController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && directAttackController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(directAttackController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(activateSpellTrapController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(activateSpellTrapController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && normalSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(normalSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && normalSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(normalSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && flipSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(flipSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && flipSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(flipSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && specialSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(specialSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && specialSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(specialSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(tributeSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("yes") && tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(tributeSummonController.userReplyYesNoForChain(string, token),
                needToMediate, token);
        } else if (string.equals("no") && attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterConductor.defendingMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("yes") && attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterConductor.defendingMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("no")
            && FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                FlipSummoningOrChangingCardPositionConductor.defendingMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("yes")
            && FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                FlipSummoningOrChangingCardPositionConductor.defendingMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("no") && NormalSummonConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                NormalSummonConductor.normalSummonedMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("yes") && NormalSummonConductor.isPromptingUserToActivateMonsterEffect()) {
            return mediateOutputBeforeSendingToGameManager(
                NormalSummonConductor.normalSummonedMonsterEffectAnalysis(string, token), needToMediate, token);
        } else if (string.equals("no") && attackMonsterToMonsterConductor.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterConductor.userReplyYesNoForChain(string, token), needToMediate, token);
        } else if (string.equals("yes") && attackMonsterToMonsterConductor.isGoingToChangeTurnsForChaining()) {
            return mediateOutputBeforeSendingToGameManager(
                attackMonsterToMonsterConductor.userReplyYesNoForChain(string, token), needToMediate, token);
        } else if (string.equals("print")) {
            // System.out.println(MonsterCard.giveATKDEFConsideringEffects("attack",
            // selectCardController
            // .getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size()
            // - 1), 0));
            // System.out.println(MonsterCard.giveATKDEFConsideringEffects("defense",
            // selectCardController
            // .getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size()
            // - 1), 0));
        } else if ((string.startsWith("pay") || string.startsWith("destroy"))
            && phaseController.isClassWaitingForPayingLifePointsOrDestroyingCard()) {
            return mediateOutputBeforeSendingToGameManager(
                phaseController.redirectInputForStandByPhaseSpellCheck(string, token), needToMediate, token);
        } else if (Utility.isMatcherCorrectWithoutErrorPrinting(
            Utility.getCommandMatcher(string, "(?<=\\n|^)card[\\s]+show[\\s]+--selected(?=\\n|$)"))
            || Utility.isMatcherCorrectWithoutErrorPrinting(
            Utility.getCommandMatcher(string, "(?<=\\n|^)card[\\s]+show[\\s]+-s(?=\\n|$)"))) {
            return duelBoard.showSelectedCard(token, fakeTurn);
        } else if (Utility.isMatcherCorrectWithoutErrorPrinting(
            Utility.getCommandMatcher(string, "(?<=\\n|^)show[\\s]+board(?=\\n|$)"))) {
            return duelBoard.showMainDuelBoard(token);
        }
        return "invalid command!";
    }

    public void startDuel(String token) {
        fakeTurn = turn = 1;
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        if (turn == 1) {
            phaseController.setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_1);
        }
        if (turn == 2) {
            phaseController.setPhaseInGame(PhaseInGame.OPPONENT_MAIN_PHASE_1);
        }
        GameManager.getDuelBoardByIndex(token).shuffleMainDecks(token);
        lifePoints.set(0, 8000);
        lifePoints.set(1, 8000);
        DuelStarter.getGameManager().clearWholeReportToClient(token);
        DuelStarter.getGameManager().clearWhatUsersSay(token);
        //firstTimeStarting = true;
    }

    public String endGame(int turn, String token) {

        User winnerUser = Storage.getUserByName(playingUsers.get(turn - 1));
        User loserUser = Storage.getUserByName(playingUsers.get(-turn + 2));
        int maxLifePoint = 0;
        if (numberOfRounds == 1) {
            maxLifePoint = playersScores.get(turn - 1);
        } else if (currentRound == 1) {
            maxLifePoint = playersScores.get(turn - 1);
        } else if (currentRound == 2) {
            if (playersScores.get(turn - 1) > maxLifePointOfPlayers.get(turn - 1)) {
                maxLifePoint = playersScores.get(turn - 1);
            }
        } else if (currentRound == 3) {
            if (playersScores.get(turn - 1) > maxLifePointOfPlayers.get(turn - 1)) {
                maxLifePoint = playersScores.get(turn - 1);
            }
        }

        winnerUser.setMoney(numberOfRounds * (1000 + maxLifePoint) + winnerUser.getMoney());
        winnerUser.setScore(numberOfRounds * (1000) + winnerUser.getScore());
        loserUser.setMoney(numberOfRounds * (100) + loserUser.getMoney());
        GameManager.removeClassesWhenGameIsOver(token);
        isGameOver = true;
        String output = winnerUser.getName() + " won the whole match with score: " + numberOfRounds * 1000;
        DuelStarter.getGameManager().addStringToWholeReportToClient(output, token);
        return output;
    }

    public String endOneRoundOfDuel(int turn, String token) {
        User winnerUser = Storage.getUserByName(playingUsers.get(turn - 1));
        if (lifePoints.get(turn - 1) > maxLifePointOfPlayers.get(turn - 1)) {
            maxLifePointOfPlayers.set(turn - 1, lifePoints.get(turn - 1));
        }
        GameManager.getDuelBoardByIndex(token).resetCards(1, token);
        GameManager.getDuelBoardByIndex(token).resetCards(2, token);
        playersScores.set(turn - 1, playersScores.get(turn - 1) + 1000);
        isPlayersChangedDecks = false;
        this.turn = 1;
        fakeTurn = 1;
        currentRound += 1;
        GameManager.clearAllVariablesOfThisIndex(token);
        String output = winnerUser.getName() + " won the game and the score is: 1000";
        DuelStarter.getGameManager().addStringToWholeReportToClient(output, token);
        return output;
    }

    public String mediateOutputBeforeSendingToGameManager(String string, boolean needToMediate, String token) {
        if (!needToMediate || isGameOver) {
            return string;
        }
        AI ai = GameManager.getAIByIndex(token);
        String aiString = "";
        String nothing = "";
        while (isAIPlaying && fakeTurn == aiTurn && !isGameOver) {
            // System.out.println("aiTurn is "+fakeTurn);
            aiString = ai.getCommand();
            // System.out.println("AI COMMAND! AI COMMAND! is" + aiString + ".");
            nothing = getInput(aiString, false, token);
            // System.out.println("whoops aiString: " + aiString + " getResult: " +
            // nothing);
            // System.out.println("AI RESULT OF SAYING " + nothing);
            string += (aiString + nothing);
        }
        /*
         * if (GameManager.getDuelControllerByIndex(0).getTurn() != aiTurn){
         * AIBattlePhaseMind.numberOfActions = 0; }
         */
        return string;
    }

    private String isThisInputAppropriateAccordingToPhase(String string, String token) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(token);
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(token);
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(token);
        FlipSummonController flipSummonController = GameManager.getFlipSummonControllerByIndex(token);
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(token);
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(token);
        SetCardController setCardController = GameManager.getSetCardControllerByIndex(token);
        AttackMonsterToMonsterController attackMonsterToMonsterController = GameManager
            .getAttackMonsterToMonsterControllerByIndex(token);
        AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager
            .getAttackMonsterToMonsterConductorsByIndex(token);
        DirectAttackController directAttackController = GameManager.getDirectAttackControllerByIndex(token);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(token);
        if (!string.startsWith("select") && normalSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return "you should choose tributes for your summon right now";
        } else if (!string.startsWith("select") && setCardController.isAreWeLookingForMonstersToBeTributed()) {
            return "you should choose tributes in order to set right now";
        } else if (!string.startsWith("select")
            && attackMonsterToMonsterController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select") && directAttackController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select")
            && FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            return "you should choose a monster to destroy right now";
        } else if (!string.startsWith("select")
            && NormalSummonConductor.isIsClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            return "you should a monster to special summon right now";
        } else if (!string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select")
            && attackMonsterToMonsterConductor.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select") && normalSummonController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select")
            && activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select") && flipSummonController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select") && specialSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return "you should choose tributes for your special summon right now";
        } else if (!string.startsWith("select") && specialSummonController.isClassWaitingForCardToBeDiscarded()) {
            return "you should choose card to discard right now";
        } else if (!string.startsWith("select") && specialSummonController.isClassWaitingForChainCardToBeSelected()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.startsWith("select") && tributeSummonController.isAreWeLookingForMonstersToBeTributed()) {
            return "you should choose tributes for your tribute summon right now";
        } else if (!string.startsWith("select") && activateMonsterController.isClassWaitingForUserToDiscardOneCard()) {
            return "you should choose a card to discard right now";
        } else if (!string.startsWith("select")
            && activateMonsterController.isClassWaitingForUserToChooseMonsterFromGraveyard()) {
            return "you should choose a monster from your graveyard right now";
        } else if (!string.startsWith("select")
            && activateMonsterController.isClassWaitingForUserToChooseMonsterFromOpponentGraveyard()) {
            return "you should choose a monster from your graveyard right now";
        } else if (!string.equals("no") && !string.equals("yes")
            && attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && directAttackController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && normalSummonController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && flipSummonController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && specialSummonController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && NormalSummonConductor.isPromptingUserToActivateMonsterEffect()) {
            return "it's not your turn to play this kind of moves";
        } else if (!string.equals("no") && !string.equals("yes")
            && attackMonsterToMonsterConductor.isGoingToChangeTurnsForChaining()) {
            return "it's not your turn to play this kind of moves";
        } else if (!(string.startsWith("pay") || string.startsWith("destroy"))
            && phaseController.isClassWaitingForPayingLifePointsOrDestroyingCard()) {
            return "you must enter pay or destroy right now";
        }
        return "yes";
    }

    public ArrayList<Integer> getMaxLifePointOfPlayers() {
        return maxLifePointOfPlayers;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPlayersChangedDecks() {
        return isPlayersChangedDecks;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getTotalTurnsUntilNow() {
        return totalTurnsUntilNow;
    }

    public void setTotalTurnsUntilNow(int totalTurnsUntilNow) {
        this.totalTurnsUntilNow = totalTurnsUntilNow;
    }

    public int getTurn() {
        return turn;
    }

    public int getFakeTurn() {
        return fakeTurn;
    }

    public boolean isAIPlaying() {
        return isAIPlaying;
    }

    public int getAiTurn() {
        return aiTurn;
    }

    public ArrayList<String> getPlayingUsers() {
        return playingUsers;
    }

    public ArrayList<Integer> getLifePoints() {
        return lifePoints;
    }

    public String getPlayingUsernameByTurn(int turn) {
        return playingUsers.get(turn - 1);
    }

    public boolean isTurnSetedBetweenTwoPlayerWhenRoundBegin() {
        return isTurnSetedBetweenTwoPlayerWhenRoundBegin;
    }

    public void setTurnSetedBetweenTwoPlayerWhenRoundBegin(boolean isTurnSetedBetweenTwoPlayerWhenRoundBegin) {
        this.isTurnSetedBetweenTwoPlayerWhenRoundBegin = isTurnSetedBetweenTwoPlayerWhenRoundBegin;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getLatestInput() {
        return allInputs.get(allInputs.size() - 1);
    }

    public void changeFakeTurn() {
        this.fakeTurn = 3 - this.fakeTurn;
    }

    public void setFakeTurn(int fakeTurn) {
        this.fakeTurn = fakeTurn;
    }

    public void setAiTurn(int aiTurn) {
        this.aiTurn = aiTurn;
    }

    public void increaseLifePoints(int lifePoints, int turn, String token) {
        addStringToChangesInLifePointsToBeGivenToClient(
            "user with turn " + turn + " is increasing in health by " + lifePoints, token);
        this.lifePoints.set(turn - 1, this.lifePoints.get(turn - 1) + lifePoints);
    }

    public boolean canUserSummonOrSetMonsters(int turn) {
        return canUserSummonOrSetMonsters.get(turn - 1);
    }

    public void setCanUserSummonOrSetMonsters(int turn, boolean bool) {
        canUserSummonOrSetMonsters.set(turn - 1, bool);
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    // public void setGameOver(boolean isGameOver) {
    // this.isGameOver = isGameOver;
    // }

    public void setPlayersChangedDecks(boolean isPlayersChangedDecks) {
        this.isPlayersChangedDecks = isPlayersChangedDecks;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setMaxLifePointOfPlayers(ArrayList<Integer> maxLifePointOfPlayers) {
        this.maxLifePointOfPlayers = maxLifePointOfPlayers;
    }

    public void clearAllVariablesOfThisClass() {
        allInputs.clear();
        // playingUsers.clear();
        lifePoints.clear();
        lifePoints.add(8000);
        lifePoints.add(8000);
        maxLifePointOfPlayers.clear();
        maxLifePointOfPlayers.add(0);
        maxLifePointOfPlayers.add(0);
        numberOfRoundsPlayersWon.clear();
        numberOfRoundsPlayersWon.add(0);
        numberOfRoundsPlayersWon.add(0);
        // playersScores.clear();
        // playersScores.add(0);
        // playersScores.add(0);
        canUserSummonOrSetMonsters.clear();
        canUserSummonOrSetMonsters.add(true);
        canUserSummonOrSetMonsters.add(true);
        // isPlayersChangedDecks = true;
        // isTurnSetedBetweenTwoPlayerWhenRoundBegin = true;
        // currentRound = 1;
        // isAIPlaying = false;
        // aiTurn = 0;
        //
        // if (firstUser.equals("ai")) {
        // aiTurn = 1;
        // isAIPlaying = true;
        // } else if (secondUser.equals("ai")) {
        // aiTurn = 2;
        // isAIPlaying = true;
        // }
        totalTurnsUntilNow = 1;
    }
}

class UserAndTurn {
    String username;
    int turn;
    public UserAndTurn(String user, int turn){
        this.username = user;
        this.turn = turn;
    }

    public String getUsername() {
        return username;
    }

    public int getTurn() {
        return turn;
    }
}
