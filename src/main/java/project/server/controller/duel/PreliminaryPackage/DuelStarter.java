package project.server.controller.duel.PreliminaryPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.duel.GamePackage.SetTurnForGame;
import project.model.Utility.Utility;
import project.server.controller.non_duel.storage.Storage;
import project.model.Deck;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class DuelStarter {

    private static HashMap<String, Integer> listOfTypeOfGameThatSuggest = new HashMap<>();
    //    private static HashMap<String, Integer> listOfGamesThatMustSetItsTurn = new HashMap<>();
    private static List<SetTurnForGame> newGamesThatShouldSetItsTurn = new ArrayList<>();

    private static GameManager gameManager = new GameManager();

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static String requestGame(JsonObject details) {
        String token = "";
        int numberOfRounds;
        try {
            token = details.get("token").getAsString();
            numberOfRounds = details.get("numberOfRounds").getAsInt();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        listOfTypeOfGameThatSuggest.put(token, numberOfRounds);
        for (int i = 0; i < 150; i++) {
            for (Map.Entry<String, Integer> entry : listOfTypeOfGameThatSuggest.entrySet()) {
                if (!entry.getKey().equals(token) && entry.getValue() == numberOfRounds) {
                    addANewSetTurnForGame(token, entry.getKey(), entry.getValue());
                    listOfTypeOfGameThatSuggest.remove(token);
                    listOfTypeOfGameThatSuggest.remove(entry.getKey());
                    return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                        "Duel Started Successfully!");
                }
            }

            for (int j = 0; j < newGamesThatShouldSetItsTurn.size(); j++) {
                if (newGamesThatShouldSetItsTurn.get(j).getPlayer1Token().equals(token)) {
                    return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                        "Duel Started Successfully!");
                }
                if (newGamesThatShouldSetItsTurn.get(j).getPlayer2Token().equals(token)) {
                    return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                        "Duel Started Successfully!");
                }
            }

            if (!listOfTypeOfGameThatSuggest.containsKey(token)) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Confirmation", "Duel Canceled Successfully!");
            }

            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listOfTypeOfGameThatSuggest.remove(token);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "Game interrupted");

    }

    private static synchronized void addANewSetTurnForGame(String player1Token, String player2Token, int numberOfRounds) {
        for (int i = 0; i < newGamesThatShouldSetItsTurn.size(); i++) {
            if (newGamesThatShouldSetItsTurn.get(i).getPlayer1Token().equals(player1Token)) {
                return;
            }
            if (newGamesThatShouldSetItsTurn.get(i).getPlayer2Token().equals(player1Token)) {
                return;
            }
        }
        SetTurnForGame setTurnForGame = new SetTurnForGame(player1Token, player2Token, numberOfRounds);
        newGamesThatShouldSetItsTurn.add(setTurnForGame);
    }


    public static String setTurnOfGame(JsonObject details) {
        String token = "";
        int userSelection;
        try {
            token = details.get("token").getAsString();
            userSelection = Integer.parseInt(details.get("userSelection").getAsString());
        } catch (Exception e) {
            System.out.println("token is null " + (details.get("token") == null));
            System.out.println("userSelection is null " + (details.get("userSelection") == null));
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        SetTurnForGame setTurnForGame = null;
        for (int i = 0; i < newGamesThatShouldSetItsTurn.size(); i++) {
            if (newGamesThatShouldSetItsTurn.get(i).getPlayer1Token().equals(token)) {
                newGamesThatShouldSetItsTurn.get(i).setPlayer1Selection(userSelection);
                setTurnForGame = newGamesThatShouldSetItsTurn.get(i);
                break;
            } else if (newGamesThatShouldSetItsTurn.get(i).getPlayer2Token().equals(token)) {
                newGamesThatShouldSetItsTurn.get(i).setPlayer2Selection(userSelection);
                setTurnForGame = newGamesThatShouldSetItsTurn.get(i);
                break;
            }
        }

        if (setTurnForGame == null) {
            return ServerController.getUserNotLogined();
        }

        for (int i = 0; i < 30; i++) {
            String result = setTurnForGame.setWinnerUserAndSendItsToken();
            if (result == null) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (result.equals("equal")) {
                i = 0;
                setTurnForGame.setPlayer1Selection(0);
                setTurnForGame.setPlayer2Selection(0);
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error",
                    "Players Must Repeat Game");
            } else if (result.equals("Players Must Repeat Game")) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error",
                    "Players Must Repeat Game");
            } else {
                String secondPlayerToken = "";
                if (result.equals(setTurnForGame.getPlayer1Token())) {
                    secondPlayerToken = setTurnForGame.getPlayer2Token();
                } else {
                    secondPlayerToken = setTurnForGame.getPlayer1Token();
                }
                startGame(result, secondPlayerToken);
                user = ServerController.getUserByToken(result);
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("CONFIRMATION",
                    "Player " + user.getName() + " Must Start Game");
            }
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "Game interrupted");
    }

    public static String cancelDuel(JsonObject details) {
        String token = "";
        try {
            token = details.get("token").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        for (Map.Entry<String, Integer> entry : listOfTypeOfGameThatSuggest.entrySet()) {
            if (entry.getKey().equals(token)) {
                listOfTypeOfGameThatSuggest.remove(token);
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful", "Duel Canceled Successfully!");
            }
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "You Have not Requested For Duel");
    }

    private static synchronized void startGame(String firstPlayerToken, String secondPlayerToken) {
        User firstUser = ServerController.getUserByToken(firstPlayerToken);
        Deck firstUserActiveDeck = getActiveDeck(firstUser);
        User secondUser = ServerController.getUserByToken(secondPlayerToken);
        Deck secondUserActiveDeck = getActiveDeck(secondUser);
        for (int i = 0; i < newGamesThatShouldSetItsTurn.size(); i++) {
            if (newGamesThatShouldSetItsTurn.get(i).getPlayer1Token().equals(firstPlayerToken) || newGamesThatShouldSetItsTurn.get(i).getPlayer2Token().equals(secondPlayerToken)) {
                startNewGame(firstUser, secondUser, newGamesThatShouldSetItsTurn.get(i).getNumberOfRounds(), firstUserActiveDeck, secondUserActiveDeck,
                    firstPlayerToken, secondPlayerToken);
                newGamesThatShouldSetItsTurn.remove(i);
//                listOfGamesThatMustSetItsTurn.remove(firstPlayerToken);
//                listOfGamesThatMustSetItsTurn.remove(secondPlayerToken);
                break;
            }
        }
    }

    private static void startNewGame(User firstUser, User secondUser, int roundsNumber, Deck firstUserActiveDeck,
                                     Deck secondUserActiveDeck, String firstUserToken, String secondUserToken) {

        ArrayList<Card> firstUserMainDeck = getMainOrSideDeckCards(firstUserActiveDeck, true);
        ArrayList<Card> firstUserSideDeck = getMainOrSideDeckCards(firstUserActiveDeck, false);
        ArrayList<Card> secondUserMainDeck = getMainOrSideDeckCards(secondUserActiveDeck, true);
        ArrayList<Card> secondUserSideDeck = getMainOrSideDeckCards(secondUserActiveDeck, false);
        Collections.shuffle(firstUserMainDeck);
        Collections.shuffle(secondUserMainDeck);
        gameManager.addANewGame(firstUserActiveDeck, firstUserMainDeck, firstUserSideDeck, secondUserActiveDeck,
            secondUserMainDeck, secondUserSideDeck, firstUser.getName(), secondUser.getName(), roundsNumber,
            firstUserToken, secondUserToken);
        GameManager.getDuelControllerByIndex(firstUserToken).setPlayersChangedDecks(true);
        GameManager.getDuelControllerByIndex(firstUserToken).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
        GameManager.getDuelControllerByIndex(firstUserToken).startDuel(firstUserToken);
    }

    public static ArrayList<Card> getMainOrSideDeckCards(Deck activeDeck, boolean isCardsInMainDeck) {

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();

        ArrayList<Card> cardsInMainOrSideDeck = new ArrayList<>();
        List<String> cardsToBeCloned = new ArrayList<>();
        if (isCardsInMainDeck) {
            cardsToBeCloned = activeDeck.getMainDeck();
        } else {
            cardsToBeCloned = activeDeck.getSideDeck();
        }

        for (int i = 0; i < cardsToBeCloned.size(); i++) {
            String cardName = Utility.giveCardNameRemovingRedundancy(cardsToBeCloned.get(i));
            if (allMonsterCards.containsKey(cardName)) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(cardName);
                cardsInMainOrSideDeck.add((MonsterCard) monsterCard.clone());
            } else if (allSpellAndTrapCards.containsKey(cardName)) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(cardName))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardName);
                    cardsInMainOrSideDeck.add((SpellCard) spellCard.clone());
                } else {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardName);
                    cardsInMainOrSideDeck.add((TrapCard) trapCard.clone());
                }
            }
        }
        return cardsInMainOrSideDeck;
    }

    public static Deck getActiveDeck(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
            if (allDecks.get(entry.getKey()).getIsDeckActive())
                return allDecks.get(entry.getKey());
        }
        return null;
    }

}
