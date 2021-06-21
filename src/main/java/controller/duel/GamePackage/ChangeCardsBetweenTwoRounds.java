package controller.duel.GamePackage;

import java.util.List;
import java.util.regex.Matcher;

import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;

public class ChangeCardsBetweenTwoRounds {

    private List<String> mainDeckCards;
    private List<String> sideDeckCards;

    public String changeCardsBetweenTwoRounds(int turn, String input, int index) {

        String allyPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

        if (turn == 1) {
            mainDeckCards = GameManager.getDuelControllerByIndex(0).getDecksOfPlayers().get(0).getMainDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(0).getDecksOfPlayers().get(0).getSideDeck();
        } else if (turn == 2) {
            mainDeckCards = GameManager.getDuelControllerByIndex(0).getDecksOfPlayers().get(1).getMainDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(0).getDecksOfPlayers().get(1).getSideDeck();
        }

        if (input.equals("end")) {
            if (turn == 1) {
                if (opponentPlayerName.equals("AI")) {
                    GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
                    GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
                    return "next round of duel started\n" + allyPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
                }
                GameManager.getDuelControllerByIndex(index).setTurn(2);
                return "now " + opponentPlayerName + " can change his deck";
            }
            if (turn == 2) {
                GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
                GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
                return "next round of duel started\n" + allyPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
            }
        }

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input,
                "move from side deck (?<sideDeckCard>.+) to main deck (?<mainDeckCard>.+)");
        if (matcher.find()) {
            String cardToBeMovedToMainDeckStringFormat = matcher.group("sideDeckCard");
            String cardToBeMovedToSideDeckStringFormat = matcher.group("mainDeckCard");
            return moveCards(cardToBeMovedToMainDeckStringFormat, cardToBeMovedToSideDeckStringFormat, index);
        }

        return "invalid command!";
    }

    private String moveCards(String cardToBeMovedToMainDeckStringFormat, String cardToBeMovedToSideDeckStringFormat,
            int index) {

        if (!sideDeckCards.contains(cardToBeMovedToMainDeckStringFormat))
            return cardToBeMovedToMainDeckStringFormat + " does not exist in side deck";

        if (!mainDeckCards.contains(cardToBeMovedToSideDeckStringFormat))
            return cardToBeMovedToSideDeckStringFormat + " does not exist in main deck";

        sideDeckCards.remove(cardToBeMovedToMainDeckStringFormat);
        mainDeckCards.add((cardToBeMovedToMainDeckStringFormat));

        mainDeckCards.remove(cardToBeMovedToSideDeckStringFormat);
        sideDeckCards.add(cardToBeMovedToSideDeckStringFormat);
        return "cards moved successfully!";
    }
}
