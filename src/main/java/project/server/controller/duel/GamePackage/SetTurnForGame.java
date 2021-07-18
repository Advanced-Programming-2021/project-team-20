package project.server.controller.duel.GamePackage;

import project.server.controller.duel.PreliminaryPackage.GameManager;

public class SetTurnForGame {

    private int numberOfRounds;
    private boolean canFirstPlayerSelect = true;
    private boolean canSecondPlayerSelect = false;
    private int player1Selection;
    private int player2Selection;
    private String player1Token;
    private String player2Token;

    public SetTurnForGame(String player1Token, String player2Token, int numberOfRounds) {
        this.player1Token = player1Token;
        this.player2Token = player2Token;
        this.numberOfRounds = numberOfRounds;
    }

    public String setTurnBetweenTwoPlayer(String input, String token) {

        String allyPlayerName = GameManager.getDuelControllerByIndex(token).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(token).getPlayingUsers().get(1);

        if (canFirstPlayerSelect) {
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                player1Selection = Integer.parseInt(input);
                canFirstPlayerSelect = false;
                canSecondPlayerSelect = true;
                if (!opponentPlayerName.equals("AI")) {
                    return opponentPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
                }
            } else {
                return "please choose number between 1 to 3\n1.stone\n2.hand\n3.snips";
            }
        }

        if (opponentPlayerName.equals("AI")) {
            if (player1Selection == 1) {
                player2Selection = 3;
            } else {
                player2Selection = player1Selection - 1;
            }
        } else {
            if (canSecondPlayerSelect) {
                if (input.equals("1") || input.equals("2") || input.equals("3")) {
                    player2Selection = Integer.parseInt(input);
                    canSecondPlayerSelect = false;
                } else {
                    return "please choose number between 1 to 3\n1.stone\n2.hand\n3.snips";
                }
            }
        }

        if (player1Selection == player2Selection) {
            canFirstPlayerSelect = true;
            canSecondPlayerSelect = false;
            return "both player select similar choice\ndo this action again\n" + allyPlayerName
                + " must choose\n1.stone\n2.hand\n3.snips";
        }

        if ((player1Selection == 1 && player2Selection == 3) || (player1Selection == 2 && player2Selection == 1)
            || (player1Selection == 3 && player2Selection == 2)) {
            GameManager.getDuelControllerByIndex(token).setTurn(1);
            if (opponentPlayerName.equals("AI")) {
                GameManager.getDuelControllerByIndex(token).setAiTurn(2);
            }
            GameManager.getDuelControllerByIndex(token).startDuel(token);
            GameManager.getDuelControllerByIndex(token).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
            if (GameManager.getDuelControllerByIndex(token).getCurrentRound() >= 2) {
                DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
                duelBoard.initializeCardsInDuelBoard(duelBoard.getAllyCardsInDeck(),
                    duelBoard.getOpponentCardsInDeck());
            }
            setFieldsOfClassLikeForNextRound();
            return allyPlayerName + " must start game";
        }

        GameManager.getDuelControllerByIndex(token).setTurn(2);
        if (opponentPlayerName.equals("AI")) {
            GameManager.getDuelControllerByIndex(token).setAiTurn(1);
        }
        GameManager.getDuelControllerByIndex(token).startDuel(token);
        GameManager.getDuelControllerByIndex(token).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
        if (GameManager.getDuelControllerByIndex(token).getCurrentRound() >= 2) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(token);
            duelBoard.initializeCardsInDuelBoard(duelBoard.getAllyCardsInDeck(), duelBoard.getOpponentCardsInDeck());
        }
        setFieldsOfClassLikeForNextRound();
        return opponentPlayerName + " must start game";
    }

    private void setFieldsOfClassLikeForNextRound() {
        canFirstPlayerSelect = true;
        canSecondPlayerSelect = false;
        player1Selection = 0;
        player2Selection = 0;

    }

    public String setWinnerUserAndSendItsToken() {
        if (player1Selection == 0 && player2Selection == 0) {
            return "Players Must Repeat Game";
        }

        if (player1Selection == 0 || player2Selection == 0) {
            return null;
        }

        if (player1Selection == player2Selection) {
            return "equal";
        }

        if ((player1Selection == 1 && player2Selection == 3) || (player1Selection == 2 && player2Selection == 1)
            || (player1Selection == 3 && player2Selection == 2)) {
            return player1Token;
        } else {
            return player2Token;
        }
    }

    public String getPlayer1Token() {
        return player1Token;
    }

    public String getPlayer2Token() {
        return player2Token;
    }

    public void setPlayer1Selection(int player1Selection) {
        this.player1Selection = player1Selection;
    }

    public void setPlayer2Selection(int player2Selection) {
        this.player2Selection = player2Selection;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }
}
