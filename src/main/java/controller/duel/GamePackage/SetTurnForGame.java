package controller.duel.GamePackage;

import controller.duel.PreliminaryPackage.GameManager;

public class SetTurnForGame {

    private boolean isPrimarilyMessageSent = true;
    private boolean isFirstPlayerCanSelect = true;
    private boolean isSecondPlayerCanSelect = false;
    private int player1Selection;
    private int player2Selection;

    public String setTurnBetweenTwoPlayer(String input, int index) {

        String allyPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

        if (!isPrimarilyMessageSent) {
            isPrimarilyMessageSent = true;
            return allyPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
        }
        if (isFirstPlayerCanSelect) {
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                player1Selection = Integer.parseInt(input);
                isFirstPlayerCanSelect = false;
                isSecondPlayerCanSelect = true;
                return opponentPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
            }
            return "please choose number between 1 to 3";
        }

        if (isSecondPlayerCanSelect) {
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                player2Selection = Integer.parseInt(input);
                isSecondPlayerCanSelect = false;
            } else {
                return "please choose number between 1 to 3";
            }
        }
        if (player1Selection == player2Selection) {
            isFirstPlayerCanSelect = true;
            isSecondPlayerCanSelect = false;
            return "both player select similar choice\ndo this action again";
        }

        if ((player1Selection == 1 && player2Selection == 3) || (player1Selection == 2 && player2Selection == 1)
                || (player1Selection == 3 && player2Selection == 2)) {
            GameManager.getDuelControllerByIndex(index).setTurn(1);
            GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
            GameManager.getDuelControllerByIndex(index).startDuel(index);
            setFieldsOfClassLikeForNextRound();
            return allyPlayerName + " must start game";
        }

        GameManager.getDuelControllerByIndex(index).setTurn(2);
        GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(true);
        GameManager.getDuelControllerByIndex(index).startDuel(index);
        setFieldsOfClassLikeForNextRound();
        return opponentPlayerName + " must start game";

    }

    private void setFieldsOfClassLikeForNextRound() {
        isPrimarilyMessageSent = false;
        isFirstPlayerCanSelect = true;
        isSecondPlayerCanSelect = false;
        player1Selection = 0;
        player2Selection = 0;
    }
}
