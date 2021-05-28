package sample.controller.duel.GamePackage;

import java.util.ArrayList;
import java.util.regex.Matcher;

import sample.controller.duel.PreliminaryPackage.GameManager;
import sample.controller.duel.Utility.Utility;
import sample.model.cardData.General.Card;

public class ChangeCardsBetweenTwoRounds {

    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> sideDeckCards;

    public String changeCardsBetweenTwoRounds(int turn, String input, int index) {

        String allyPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        String opponentPlayerName = GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

        if (turn == 1) {
            mainDeckCards = GameManager.getDuelBoardByIndex(index).getAllyCardsInDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(index).getAllySideDeckCards();
        } else if (turn == 2) {
            mainDeckCards = GameManager.getDuelBoardByIndex(index).getOpponentCardsInDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(index).getOpponentSideDeckCards();
        }

        if (input.equals("end")) {
            
            if (turn == 1) {
            //    System.out.println(opponentPlayerName);
                if(opponentPlayerName.equals("AI")){
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
            return moveCards(cardToBeMovedToMainDeckStringFormat, cardToBeMovedToSideDeckStringFormat, turn, index);
        }

        return "invalid command!";
    }

    private String moveCards(String cardToBeMovedToMainDeckStringFormat, String cardToBeMovedToSideDeckStringFormat,
            int turn, int index) {

        Card cardToBeMovedToSideDeck = null;
        Card cardToBeMovedToMainDeck = null;

        for (int i = 0; i < sideDeckCards.size(); i++) {
            if (sideDeckCards.get(i).getCardName().equals(cardToBeMovedToMainDeckStringFormat)) {
                cardToBeMovedToMainDeck = sideDeckCards.get(i);
                break;
            }
        }

        for (int i = 0; i < mainDeckCards.size(); i++) {
            if (mainDeckCards.get(i).getCardName().equals(cardToBeMovedToSideDeckStringFormat)) {
                cardToBeMovedToSideDeck = mainDeckCards.get(i);
                break;
            }
        }

        if (cardToBeMovedToMainDeck == null)
            return cardToBeMovedToMainDeckStringFormat + " does not exist in side deck";

        if (cardToBeMovedToSideDeck == null)
            return cardToBeMovedToSideDeckStringFormat + " does not exist in main deck";

        mainDeckCards.add(cardToBeMovedToMainDeck);
        sideDeckCards.add(cardToBeMovedToSideDeck);
        transferCardsBetweenSideAndMainDeck(turn, index);
        return "cards moved successfully!";

    }

    private void transferCardsBetweenSideAndMainDeck(int turn, int index) {
        if (turn == 1) {
            GameManager.getDuelBoardByIndex(index).setAllyCardsInDeck(mainDeckCards);
            GameManager.getDuelControllerByIndex(index).setAllySideDeckCards(sideDeckCards);
        } else if (turn == 2) {
            GameManager.getDuelBoardByIndex(index).setOpponentCardsInDeck(mainDeckCards);
            GameManager.getDuelControllerByIndex(index).setOpponentSideDeckCards(sideDeckCards);
        }
    }
}
