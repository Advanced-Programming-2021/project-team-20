package controller.duel.GamePackage;

import controller.duel.PreliminaryPackage.GameManager;

public class SetTurnForGame {

    private boolean isPrimarilyMessageSent = false;
    private boolean isFirstPlayerCanSelect = true;
    private boolean isSecondPlayerCanSelect = false;
    private int player1Selection;
    private int player2Selecyion;

    public String setTurnBetweenTwoPlayer(String input, int index) {

        if (!isPrimarilyMessageSent) {
            isPrimarilyMessageSent = true;
            return "choose\n1.stone\n2.hand\n3.snips";
        }
        if (isFirstPlayerCanSelect) {
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                player1Selection = Integer.parseInt(input);
                isFirstPlayerCanSelect = false;
                isSecondPlayerCanSelect = true;
                return "player 2 must choose\n1.stone\n2.hand\n3.snips";
            }
            return "please choose number between 1 to 3";
        }

        if (isSecondPlayerCanSelect) {
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                player2Selecyion = Integer.parseInt(input);
                isSecondPlayerCanSelect = false;
            } else {
                return "please choose number between 1 to 3";
            }
        }
        if (player1Selection == player2Selecyion) {
            isFirstPlayerCanSelect = true;
            isSecondPlayerCanSelect = false;
            return "both player select similar choice\ndo this action again";
        }

        if ((player1Selection == 1 && player2Selecyion == 3) || (player1Selection == 2 && player2Selecyion == 1)
                || (player1Selection == 3 && player2Selecyion == 2)) {
            GameManager.getDuelControllerByIndex(index).setTurn(1);
            GameManager.getDuelControllerByIndex(index).setRoundBegin(false);
            return "player 1 must start game";
        }

        GameManager.getDuelControllerByIndex(index).setTurn(2);
        GameManager.getDuelControllerByIndex(index).setRoundBegin(false);
        return "player 2 must start game";

    }
}
