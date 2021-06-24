package controller.duel.GamePackage;

import java.util.List;
import java.util.regex.Matcher;

import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.Deck;

public class ChangeCardsBetweenTwoRounds {

    private Deck allyPlayerDeck;
    private Deck opponentPlayerDeck;
    private List<String> mainDeckCards;
    private List<String> sideDeckCards;
    private int turn;

    public ChangeCardsBetweenTwoRounds(Deck allyPlayerDeck, Deck opponentPlayerDeck) {
        this.allyPlayerDeck = allyPlayerDeck;
        this.opponentPlayerDeck = opponentPlayerDeck;
        this.turn = 0;
    }

    public String changeCardsBetweenTwoRounds(String input, int index) {
        turn++;
        if (turn == 3) {
            turn = 1;
        }
        String allyPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

        if (turn == 1) {
            mainDeckCards = allyPlayerDeck.getMainDeck();
            sideDeckCards = allyPlayerDeck.getSideDeck();
        } else if (turn == 2) {
            mainDeckCards = opponentPlayerDeck.getMainDeck();
            sideDeckCards = opponentPlayerDeck.getSideDeck();
        }

        if (input.equals("end")) {
            if (turn == 1) {
                if (opponentPlayerName.equals("AI")) {
                    GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
                    GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
                    resetCardsAfterChangingDeck();
                    return "next round of duel started\n" + allyPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
                }
                return "now " + opponentPlayerName + " can change his deck";
            }
            if (turn == 2) {
                GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
                GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
                resetCardsAfterChangingDeck();
                return "next round of duel started\n" + allyPlayerName + " must choose\n1.stone\n2.hand\n3.snips";
            }
        }

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input,
            "move from side deck (?<sideDeckCard>.+) to main deck (?<mainDeckCard>.+)");
        if (matcher.find()) {
            String cardToBeMovedToMainDeckStringFormat = matcher.group("sideDeckCard");
            String cardToBeMovedToSideDeckStringFormat = matcher.group("mainDeckCard");
            return moveCards(cardToBeMovedToMainDeckStringFormat, cardToBeMovedToSideDeckStringFormat);
        }

        return "invalid command!";
    }

    private void resetCardsAfterChangingDeck() {
        GameManager.getDuelBoardByIndex(0).resetCards(1);
        GameManager.getDuelBoardByIndex(0).resetCards(2);
    }

    private String moveCards(String cardToBeMovedToMainDeckStringFormat, String cardToBeMovedToSideDeckStringFormat) {

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

    public Deck getAllyPlayerDeck() {
        return allyPlayerDeck;
    }

    public Deck getOpponentPlayerDeck() {
        return opponentPlayerDeck;
    }
}
