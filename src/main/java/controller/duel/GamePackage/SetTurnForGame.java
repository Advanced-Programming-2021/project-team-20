package controller.duel.GamePackage;

import controller.duel.PreliminaryPackage.GameManager;

public class SetTurnForGame {

    private boolean canFirstPlayerSelect = true;
    private boolean canSecondPlayerSelect = false;
    private int player1Selection;
    private int player2Selection;

    public String setTurnBetweenTwoPlayer(String input, int index) {

        String allyPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

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
            GameManager.getDuelControllerByIndex(index).setTurn(1);
            GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
            setFieldsOfClassLikeFirstForNextRound();
            GameManager.getDuelControllerByIndex(index).startDuel(index);
            return allyPlayerName + " must start game";
        }

        GameManager.getDuelControllerByIndex(index).setTurn(2);
        GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
        setFieldsOfClassLikeFirstForNextRound();
        GameManager.getDuelControllerByIndex(index).startDuel(index);
        return opponentPlayerName + " must start game";
    }

    private void setFieldsOfClassLikeFirstForNextRound() {
        canFirstPlayerSelect = true;
        canSecondPlayerSelect = false;
        player1Selection = 0;
        player2Selection = 0;
    }
}
