package project.server.controller.duel.GamePackage;

import java.util.List;

import com.google.gson.JsonObject;
import project.model.User;
import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.Deck;

public class ChangeCardsBetweenTwoRounds {

    private Deck allyPlayerDeck;
    private Deck opponentPlayerDeck;
    private List<String> mainDeckCards;
    private List<String> sideDeckCards;
    private String allyPlayerToken;
    private String opponentPlayerToken;
    private boolean isAllyPlayerConfirmChanges = false;
    private boolean isOpponentPlayerConfirmChanges = false;
    private boolean isAllyPlayerChangingHisDeck;
    private boolean isOpponentPlayerChangingHisDeck;
    private boolean isPlayingWithComputer;

    public ChangeCardsBetweenTwoRounds(String allyPlayerToken, Deck allyPlayerDeck, String opponentPlayerToken, Deck opponentPlayerDeck,boolean isPlayingWithComputer) {
        this.allyPlayerToken = allyPlayerToken;
        this.allyPlayerDeck = allyPlayerDeck;
        this.opponentPlayerToken = opponentPlayerToken;
        this.opponentPlayerDeck = opponentPlayerDeck;
        this.isPlayingWithComputer = isPlayingWithComputer;
        isOpponentPlayerConfirmChanges = isPlayingWithComputer;
    }

    public String changeCardsBetweenTwoRounds(String input, int index) {

        return "null";
        // String allyPlayerName =
        // GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(0);
        // String opponentPlayerName =
        // GameManager.getDuelControllerByIndex(index).getPlayingUsers().get(1);

        // if (turn == 1) {
        // mainDeckCards = allyPlayerDeck.getMainDeck();
        // sideDeckCards = allyPlayerDeck.getSideDeck();
        // } else if (turn == 2) {
        // mainDeckCards = opponentPlayerDeck.getMainDeck();
        // sideDeckCards = opponentPlayerDeck.getSideDeck();
        // }

        // if (input.equals("end")) {
        // if (turn == 1) {
        // if (opponentPlayerName.equals("AI")) {
        // GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
        // GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
        // resetCardsAfterChangingDeck();
        // return "next round of duel started\n" + allyPlayerName + " must
        // choose\n1.stone\n2.hand\n3.snips";
        // }
        // turn = 2;
        // return "now " + opponentPlayerName + " can change his deck";
        // }
        // if (turn == 2) {
        // GameManager.getDuelControllerByIndex(index).setPlayersChangedDecks(true);
        // GameManager.getDuelControllerByIndex(index).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
        // resetCardsAfterChangingDeck();
        // turn = 1;
        // return "next round of duel started\n" + allyPlayerName + " must
        // choose\n1.stone\n2.hand\n3.snips";
        // }
    }

    // Matcher matcher;
    // matcher = Utility.getCommandMatcher(input,
    // "move from side deck (?<sideDeckCard>.+) to main deck (?<mainDeckCard>.+)");
    // if (matcher.find()) {
    // String cardToBeMovedToMainDeckStringFormat = matcher.group("sideDeckCard");
    // String cardToBeMovedToSideDeckStringFormat = matcher.group("mainDeckCard");
    // return moveCards(cardToBeMovedToMainDeckStringFormat,
    // cardToBeMovedToSideDeckStringFormat);
    // }

    // return "invalid command!";
    // }

    // private void resetCardsAfterChangingDeck() {
    // GameManager.getDuelBoardByIndex(0).resetCards(1);
    // GameManager.getDuelBoardByIndex(0).resetCards(2);
    // }

    // private String moveCards(String cardToBeMovedToMainDeckStringFormat, String
    // cardToBeMovedToSideDeckStringFormat) {

    // if (!sideDeckCards.contains(cardToBeMovedToMainDeckStringFormat))
    // return cardToBeMovedToMainDeckStringFormat + " does not exist in side deck";

    // if (!mainDeckCards.contains(cardToBeMovedToSideDeckStringFormat))
    // return cardToBeMovedToSideDeckStringFormat + " does not exist in main deck";

    // sideDeckCards.remove(cardToBeMovedToMainDeckStringFormat);
    // mainDeckCards.add((cardToBeMovedToMainDeckStringFormat));

    // mainDeckCards.remove(cardToBeMovedToSideDeckStringFormat);
    // sideDeckCards.add(cardToBeMovedToSideDeckStringFormat);
    // return "cards moved successfully!";
    // }


    public boolean addOrRemoveCardFromMainOrSideDeck(String cardName, boolean isAddCard, boolean isMainDeck, String token) {

        if (token.equals(allyPlayerToken)) {
            mainDeckCards = allyPlayerDeck.getMainDeck();
            sideDeckCards = allyPlayerDeck.getSideDeck();
            isAllyPlayerChangingHisDeck = true;
        } else if (token.equals(opponentPlayerToken)) {
            mainDeckCards = opponentPlayerDeck.getMainDeck();
            sideDeckCards = opponentPlayerDeck.getSideDeck();
            isOpponentPlayerChangingHisDeck = true;
        }

        if (isMainDeck && isAddCard) {
            return mainDeckCards.add((cardName));
        } else if (isMainDeck) {
            return mainDeckCards.remove(cardName);
        } else if (isAddCard) {
            return sideDeckCards.add(cardName);
        } else {
            return sideDeckCards.remove(cardName);
        }
    }

    public static String getInputFromClientAndProcessIt(JsonObject details) {
        String token = "";
        String cardName = "";
        boolean isAddCard = false;
        boolean isMainDeck = false;
        boolean isConfirmedChanges = false;
        try {
            token = details.get("token").getAsString();
            if (details.has("ConfirmChanges")) {
                isConfirmedChanges = true;
            } else {
                cardName = details.get("cardName").getAsString();
                isAddCard = details.get("isAddCard").getAsBoolean();
                isMainDeck = details.get("isMainDeck").getAsBoolean();
            }
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ServerController.getConnectionDisconnected();
        }
        ChangeCardsBetweenTwoRounds changeCardsBetweenTwoRounds = GameManager.getChangeCardsBetweenTwoRoundsByIndex(token);

        if (isConfirmedChanges) {
            return changeCardsBetweenTwoRounds.ConfirmChanges(token);
        }
        if (changeCardsBetweenTwoRounds.addOrRemoveCardFromMainOrSideDeck(cardName, isAddCard, isMainDeck, token)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful", "Card Moved Successfully!");
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "Card Does Not Exit!");
    }

    public String ConfirmChanges(String token) {
        if (token.equals(allyPlayerToken)) {
            isAllyPlayerConfirmChanges = true;
        } else {
            isOpponentPlayerConfirmChanges = true;
        }

        for (int i = 0; i < 30; i++) {
            if (isOpponentPlayerConfirmChanges && isAllyPlayerConfirmChanges) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful", "Next Round Started Successfully!");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            if (isAllyPlayerChangingHisDeck) {
                i = 0;
                isAllyPlayerChangingHisDeck = false;
            }
            if (isOpponentPlayerChangingHisDeck) {
                i = 0;
                isOpponentPlayerChangingHisDeck = false;
            }
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "Game interrupted");
    }

    public Deck getAllyPlayerDeck() {
        return allyPlayerDeck;
    }

    public Deck getOpponentPlayerDeck() {
        return opponentPlayerDeck;
    }

}
