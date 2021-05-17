package controller.duel.GamePackage;

import java.util.ArrayList;

import controller.duel.GamePackage.ActionConductors.AttackMonsterToMonsterConductor;
import controller.duel.GamePackage.ActionConductors.FlipSummoningOrChangingCardPositionConductor;
import controller.duel.GamePhaseControllers.*;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.cheat.Cheat;
import controller.non_duel.mainController.MainController;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;
import model.cardData.MonsterCardData.MonsterCard;

public class DuelController {
    // turn = 1 -> ALLY, turn = 2 -> OPPONENT
    private int turn;
    private int fakeTurn;
    private int numberOfRounds;
    private int currentRound;
    private boolean isPlayersChangedDecks;
    private boolean isTurnSetedBetweenTwoPlayerWhenRoundBegin;
    private boolean isGameOver;
    private ArrayList<Card> allySideDeckCards = new ArrayList<>();
    private ArrayList<Card> opponentSideDeckCards = new ArrayList<>();
    private ArrayList<String> playingUsers = new ArrayList<>();
    private ArrayList<Integer> lifePoints = new ArrayList<>();
    private ArrayList<Integer> maxLifePointOfPlayers = new ArrayList<>();
    private ArrayList<Integer> numberOfRoundsPlayersWon = new ArrayList<>();
    private ArrayList<Integer> playersScores = new ArrayList<>();
    private ArrayList<Boolean> usersSummoningOrSettingMonsterOneTime = new ArrayList<>();
    private ArrayList<String> allInputs = new ArrayList<>();
    private ChangeCardsBetweenTwoRounds changeCardsBetweenTwoRounds = new ChangeCardsBetweenTwoRounds();
    private SetTurnForGame setTurnForGame = new SetTurnForGame();

    public DuelController(String firstUser, String secondUser, ArrayList<Card> allySideDeckCards,
            ArrayList<Card> opponentSideDeckCards, int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        this.allySideDeckCards = allySideDeckCards;
        this.opponentSideDeckCards = opponentSideDeckCards;
        playingUsers.add(firstUser);
        playingUsers.add(secondUser);
        lifePoints.add(8000);
        lifePoints.add(8000);
        maxLifePointOfPlayers.add(0);
        maxLifePointOfPlayers.add(0);
        numberOfRoundsPlayersWon.add(0);
        numberOfRoundsPlayersWon.add(0);
        playersScores.add(0);
        playersScores.add(0);
        usersSummoningOrSettingMonsterOneTime.add(true);
        usersSummoningOrSettingMonsterOneTime.add(true);
        currentRound = 1;
    }

    public String getInput(String string) {

        allInputs.add(string);
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(0);
        ActivateMonsterController activateMonsterController = GameManager.getActivateMonsterControllerByIndex(0);
        NormalSummonController normalSummonController = GameManager.getNormalSummonControllerByIndex(0);
        FlipSummonController flipSummonController = GameManager.getFlipSummonControllerByIndex(0);
        SpecialSummonController specialSummonController = GameManager.getSpecialSummonControllerByIndex(0);
        TributeSummonController tributeSummonController = GameManager.getTributeSummonControllerByIndex(0);
        SetCardController setCardController = GameManager.getSetCardControllerByIndex(0);
        SelectCardController selectCardController = GameManager.getSelectCardControllerByIndex(0);
        AttackMonsterToMonsterController attackMonsterToMonsterController = GameManager
                .getAttackMonsterToMonsterControllerByIndex(0);
        AttackMonsterToMonsterConductor attackMonsterToMonsterConductor = GameManager
                .getAttackMonsterToMonsterConductorsByIndex(0);
        DirectAttackController directAttackController = GameManager.getDirectAttackControllerByIndex(0);
        ActivateSpellTrapController activateSpellTrapController = GameManager.getActivateSpellTrapControllerByIndex(0);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);

        if (string.startsWith("cheat")) {
            Cheat cheat = new Cheat();
            return cheat.findCheatCommand(string, 0);
        } else if (string.equals("surrender")) {
            return endGame(-turn + 3, 0);
        }

        if (!isPlayersChangedDecks) {
            return changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(turn, string, 0);
        } else if (!isTurnSetedBetweenTwoPlayerWhenRoundBegin) {
            return setTurnForGame.setTurnBetweenTwoPlayer(string, 0);
        }

        // System.out.println("normalSummonController.isAreWeLookingForMonstersToBeTributed()"
        // + normalSummonController.isAreWeLookingForMonstersToBeTributed());
        if (string.startsWith("select") && normalSummonController.isAreWeLookingForMonstersToBeTributed()) {
            // NormalSummonController normalSummonController =
            // GameManager.getNormalSummonController(0);
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B1");
                return output;
            } else {
                System.out.println("A1");
                return normalSummonController.redirectInputForMonsterTributing();
            }
        } else if (string.startsWith("select") && setCardController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B2");
                return output;
            } else {
                System.out.println("A2");
                return setCardController.redirectInput();
            }
        } else if (string.startsWith("select")
                && attackMonsterToMonsterController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B3");
                return output;
            } else {
                System.out.println("A3");
                return attackMonsterToMonsterController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select") && directAttackController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B4");
                return output;
            } else {
                System.out.println("A4");
                return directAttackController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select")
                && FlipSummoningOrChangingCardPositionConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B4.5");
                return output;
            } else {
                System.out.println("A4.5");
                return FlipSummoningOrChangingCardPositionConductor.checkGivenInputForMonsterToDestroy(0);
            }
        } else if (string.startsWith("select")
                && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToDestroy()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B4.5");
                return output;
            } else {
                System.out.println("A4.5");
                return attackMonsterToMonsterConductor.checkGivenInputForMonsterToDestroy(0);
            }
        } else if (string.startsWith("select")
                && attackMonsterToMonsterConductor.isClassWaitingForPlayerToPickMonsterToSpecialSummon()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B5");
                return output;
            } else {
                System.out.println("A5");
                return attackMonsterToMonsterConductor.checkGivenInputForMonsterEffectActivation(0);
            }
        } else if (string.startsWith("select")
                && attackMonsterToMonsterConductor.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B5.5");
                return output;
            } else {
                System.out.println("A5.5");
                return attackMonsterToMonsterConductor.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select") && normalSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B6");
                return output;
            } else {
                System.out.println("A6");
                return normalSummonController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select")
                && activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B7");
                return output;
            } else {
                System.out.println("A7");
                return activateSpellTrapController.redirectInput(0);
            }
        } else if (string.startsWith("select")
                && activateSpellTrapController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B8");
                return output;
            } else {
                System.out.println("A8");
                return activateSpellTrapController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select") && flipSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B9");
                return output;
            } else {
                System.out.println("A9");
                return flipSummonController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select") && specialSummonController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B10");
                return output;
            } else {
                System.out.println("A10");
                return specialSummonController.redirectInputForMonsterTributing();
            }
        } else if (string.startsWith("select") && specialSummonController.isClassWaitingForCardToBeDiscarded()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B11");
                return output;
            } else {
                System.out.println("A11");
                return specialSummonController.redirectInputForCardsToBeDiscarded();
            }
        } else if (string.startsWith("select") && specialSummonController.isClassWaitingForChainCardToBeSelected()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B12");
                return output;
            } else {
                System.out.println("A12");
                return specialSummonController.isSelectedCardCorrectForChainActivation(string, 0);
            }
        } else if (string.startsWith("select") && tributeSummonController.isAreWeLookingForMonstersToBeTributed()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B13");
                return output;
            } else {
                System.out.println("A13");
                return tributeSummonController.redirectInputForMonsterTributing();
            }
        } else if (string.startsWith("select") && activateMonsterController.isClassWaitingForUserToDiscardOneCard()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B14");
                return output;
            } else {
                System.out.println("A14");
                return activateMonsterController.analyzeInputWhenClassIsWaitingToDiscardCardFromHand();
            }
        } else if (string.startsWith("select")
                && activateMonsterController.isClassWaitingForUserToChooseMonsterFromGraveyard()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B15");
                return output;
            } else {
                System.out.println("A15");
                return activateMonsterController.analyzeInputWhenClassIsWaitingToChooseMonsterFromSelfGraveyard();
            }
        } else if (string.startsWith("select")
                && activateMonsterController.isClassWaitingForUserToChooseMonsterFromOpponentGraveyard()) {
            String output = selectCardController.selectCardInputAnalysis(string);
            if (!output.equals("card selected")) {
                System.out.println("B16");
                return output;
            } else {
                System.out.println("A16");
                return activateMonsterController.analyzeInputWhenClassIsWaitingToChooseMonsterFromOpponentGraveyard();
            }
        } else if (activateSpellTrapController.isAreWeLookingForACardNameToBeInserted()) {
            System.out.println("AB17");
            return activateSpellTrapController.redirectInput(0);
        } else if (string.startsWith("select")) {
            return selectCardController.selectCardInputAnalysis(string);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive"))
                && activateSpellTrapController.isAreWeLookingForFurtherInputToActivateSpellTrap()) {
            return activateSpellTrapController.redirectInput(0);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive"))
                && specialSummonController.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()) {
            return specialSummonController.redirectInputForAnalyzingAttackPositionOrDefensePosition(string);
        } else if ((string.startsWith("attacking") || string.startsWith("defensive"))
                && attackMonsterToMonsterConductor.isClassWaitingForUserToChooseAttackPositionOrDefensePosition()) {
            return attackMonsterToMonsterConductor.redirectInputForAnalyzingAttackPositionOrDefensePosition(string);
        } else if (string.startsWith("next")) {
            return phaseController.phaseControllerInputAnalysis(string);
        } else if (string.startsWith("normal")) {
            return normalSummonController.normalSummonInputAnalysis(string, "normal summon");
        } else if (string.startsWith("tribute")) {
            return tributeSummonController.tributeSummonInputAnalysis(string);
        } else if (string.startsWith("special")) {
            return specialSummonController.specialSummonInputAnalysis(string);
        } else if (string.equals("set")) {
            return setCardController.setCardControllerInputAnalysis(string);
        } else if (string.startsWith("set")) {
            ChangeCardPositionController changeCardPositionController = GameManager.getChangeCardPositionController(0);
            return changeCardPositionController.changeCardPositionInputAnalysis(string);
        } else if (string.startsWith("flip")) {
            return flipSummonController.flipSummonInputAnalysis(string);
        } else if (string.startsWith("attack direct")) {
            return directAttackController.attackInputAnalysis(string);
        } else if (string.startsWith("attack")) {
            return attackMonsterToMonsterController.attackInputAnalysis(string);
        } else if (string.startsWith("activate")) {
            return activateSpellTrapController.activateSpellTrapEffectInputAnalysis(string);
        } else if (string.equals("advanced show board")) {
            return duelBoard.showDuelBoard(0);
        } else if (string.equals("show graveyard")) {
            return duelBoard.showGraveyard();
        } else if (string.equals("no") && attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return attackMonsterToMonsterController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && attackMonsterToMonsterController.isGoingToChangeTurnsForChaining()) {
            return attackMonsterToMonsterController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && directAttackController.isGoingToChangeTurnsForChaining()) {
            return directAttackController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && directAttackController.isGoingToChangeTurnsForChaining()) {
            return directAttackController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return activateSpellTrapController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && activateSpellTrapController.isGoingToChangeTurnsForChaining()) {
            return activateSpellTrapController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && normalSummonController.isGoingToChangeTurnsForChaining()) {
            return normalSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && normalSummonController.isGoingToChangeTurnsForChaining()) {
            return normalSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && flipSummonController.isGoingToChangeTurnsForChaining()) {
            return flipSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && flipSummonController.isGoingToChangeTurnsForChaining()) {
            return flipSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && specialSummonController.isGoingToChangeTurnsForChaining()) {
            return specialSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && specialSummonController.isGoingToChangeTurnsForChaining()) {
            return specialSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return tributeSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && tributeSummonController.isGoingToChangeTurnsForChaining()) {
            return tributeSummonController.userReplyYesNoForChain(string);
        } else if (string.equals("no") && attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return attackMonsterToMonsterConductor.defendingMonsterEffectAnalysis(string);
        } else if (string.equals("yes") && attackMonsterToMonsterConductor.isPromptingUserToActivateMonsterEffect()) {
            return attackMonsterToMonsterConductor.defendingMonsterEffectAnalysis(string);
        } else if (string.equals("no")
                && FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return FlipSummoningOrChangingCardPositionConductor.defendingMonsterEffectAnalysis(string);
        } else if (string.equals("yes")
                && FlipSummoningOrChangingCardPositionConductor.isPromptingUserToActivateMonsterEffect()) {
            return FlipSummoningOrChangingCardPositionConductor.defendingMonsterEffectAnalysis(string);
        } else if (string.equals("no") && attackMonsterToMonsterConductor.isGoingToChangeTurnsForChaining()) {
            return attackMonsterToMonsterConductor.userReplyYesNoForChain(string);
        } else if (string.equals("yes") && attackMonsterToMonsterConductor.isGoingToChangeTurnsForChaining()) {
            return attackMonsterToMonsterConductor.userReplyYesNoForChain(string);
        } else if (string.equals("print")) {
            System.out.println(MonsterCard.giveATKDEFConsideringEffects("attack", selectCardController
                    .getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1), 0));
            System.out.println(MonsterCard.giveATKDEFConsideringEffects("defense", selectCardController
                    .getSelectedCardLocations().get(selectCardController.getSelectedCardLocations().size() - 1), 0));
        } else if ((string.startsWith("pay") || string.startsWith("destroy"))
                && phaseController.isClassWaitingForPayingLifePointsOrDestroyingCard()) {
            return phaseController.redirectInputForStandByPhaseSpellCheck(string);
        }
        return "invalid command!";
    }

    public void startDuel(int index) {
        PhaseController phaseController = GameManager.getPhaseControllerByIndex(index);
        if (turn == 1) {
            phaseController.setPhaseInGame(PhaseInGame.ALLY_MAIN_PHASE_1);
        }
        if (turn == 2) {
            phaseController.setPhaseInGame(PhaseInGame.OPPONENT_MAIN_PHASE_1);
        }
        GameManager.getDuelBoardByIndex(index).shuffleMainDecks();
        lifePoints.set(0, 8000);
        lifePoints.set(1, 8000);

    }

    public String endGame(int turn, int index) {

        User winnerUser = Storage.getUserByName(playingUsers.get(turn - 1));
        User loserUser = Storage.getUserByName(playingUsers.get(-turn + 2));
        winnerUser.setMoney(numberOfRounds * (1000 + maxLifePointOfPlayers.get(turn - 1)));
        winnerUser.setScore(numberOfRounds * (1000));
        loserUser.setMoney(numberOfRounds * (100));
        GameManager.removeClassesWhenGameIsOver(index);
        MainController mainController = MainController.getInstance();
        mainController.exitMenu();

        return winnerUser.getName() + " won the whole match with score: "
                + ((turn == 1) ? (numberOfRounds * 1000) + " - " + playersScores.get(-turn + 2)
                        : playersScores.get(-turn + 2) + " - " + (numberOfRounds * 1000));
    }

    public String endOneRoundOfDuel(int turn) {

        User winnerUser = Storage.getUserByName(playingUsers.get(turn - 1));
        playersScores.set(turn - 1, playersScores.get(turn - 1) + 1000);
        isPlayersChangedDecks = false;
        turn = 1;
        fakeTurn = 1;
        return winnerUser.getName() + " won the whole match with score: "
                + ((turn == 1) ? (1000) + " - 0" : "0 - " + (1000)) + "\n" + "now " + playingUsers.get(0)
                + " can change his deck";
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

    public ArrayList<Card> getAllySideDeckCards() {
        return allySideDeckCards;
    }

    public ArrayList<Card> getOpponentSideDeckCards() {
        return opponentSideDeckCards;
    }

    public int getTurn() {
        return turn;
    }

    public int getFakeTurn() {
        return fakeTurn;
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

    public void increaseLifePoints(int lifePoints, int turn) {
        this.lifePoints.set(turn - 1, this.lifePoints.get(turn - 1) + lifePoints);
    }

    public boolean canUserNormalSummon(int turn) {
        return usersSummoningOrSettingMonsterOneTime.get(turn - 1);
    }

    public void setUsersSummoningOneTime(int fakeTurn, boolean bool) {
        usersSummoningOrSettingMonsterOneTime.set(fakeTurn, bool);
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public void setAllySideDeckCards(ArrayList<Card> allySideDeckCards) {
        this.allySideDeckCards = allySideDeckCards;
    }

    public void setOpponentSideDeckCards(ArrayList<Card> opponentSideDeckCards) {
        this.opponentSideDeckCards = opponentSideDeckCards;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public void setPlayersChangedDecks(boolean isPlayersChangedDecks) {
        this.isPlayersChangedDecks = isPlayersChangedDecks;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setMaxLifePointOfPlayers(ArrayList<Integer> maxLifePointOfPlayers) {
        this.maxLifePointOfPlayers = maxLifePointOfPlayers;
    }
}
