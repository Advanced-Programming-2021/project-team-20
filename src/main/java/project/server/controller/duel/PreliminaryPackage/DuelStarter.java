package project.server.controller.duel.PreliminaryPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import project.client.view.LoginController;
import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.duel.Utility.Utility;
import project.server.controller.non_duel.storage.Storage;
import project.model.Deck;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class DuelStarter {

    private boolean isDuelStarted = false;
    private static int numberOfRounds;
    private static String firstPlayer;
    private static String secondPlayer;
    private static HashMap<String, Integer> listOfTypeOfGameThatSuggest = new HashMap<>();
    // public String findCommand(String command, String token) {

    // // if (isDuelStarted) {
    // // return GameManager.getDuelControllerByIndex(token).getInput(command, true,
    // token);
    // // }

    // // StartDuelPatterns startDuelPatterns = new StartDuelPatterns();
    // // HashMap<String, String> foundCommands =
    // startDuelPatterns.findCommand(command);
    // // if (foundCommands == null) {
    // // return "invalid command!";
    // // }

    // // if (!doesThisUserNameExist(foundCommands.get("secondPlayer"))) {
    // // return "there is no player with this username";
    // // }
    // // User secondUser =
    // Storage.getUserByName(foundCommands.get("secondPlayer"));
    // // User firstUser = LoginController.getOnlineUser();
    // // int numberOfRounds = Integer.parseInt(foundCommands.get("rounds"));
    // // if (getActiveDeck(firstUser) == null) {
    // // return firstUser.getName() + " has no active deck";
    // // }
    // // if (getActiveDeck(secondUser) == null) {
    // // return secondUser.getName() + " has no active deck";
    // // }
    // // // if (!isThisDeckValid(firstUser)) {
    // // // return firstUser.getName() + "’s deck is invalid";
    // // // }
    // // // if (!isThisDeckValid(secondUser)) {
    // // // return secondUser.getName() + "’s deck is invalid";
    // // // }
    // // if (!isItsRoundNumberCorrect(numberOfRounds)) {
    // // return "number of rounds is not supported";
    // // }

    // // isDuelStarted = true;
    // // return "duel successfully started!\n" + firstUser.getName() + " must
    // choose\n1.stone\n2.hand\n3.snips";
    // }

    public String checkConditionsOfPlayers(String firstUserName, String secondUserName, int numberOfRounds) {
        if (!doesThisUserNameExist(firstUserName)) {
            return "user " + firstUserName + " not found";
        }

        if (!doesThisUserNameExist(secondUserName)) {
            return "user " + secondUserName + " not found";
        }

        User firstUser = Storage.getUserByName(firstUserName);
        User secondUser = Storage.getUserByName(secondUserName);
        Deck firstUserActiveDeck = getActiveDeck(firstUser);
        if (firstUserActiveDeck == null) {
            return firstUserName + " has no active deck";
        }

        if (!isThisDeckValid(firstUserActiveDeck)) {
            return firstUserName + " has not valid deck";
        }

        Deck secondUserActiveDeck = getActiveDeck(secondUser);
        if (secondUserActiveDeck == null) {
            return secondUserName + " has no active deck";
        }

        if (!isThisDeckValid(secondUserActiveDeck)) {
            return secondUserName + " has not valid deck";
        }
        DuelStarter.numberOfRounds = numberOfRounds;
        DuelStarter.firstPlayer = firstUserName;
        DuelStarter.secondPlayer = secondUserName;
        return "game started";
    }

    public void createNewGame(String firstUserName, String secondUserName, String firstUserToken,
            String secondUserToken) {
        User firstUser = Storage.getUserByName(firstUserName);
        User secondUser = Storage.getUserByName(secondUserName);
        Deck firstUserActiveDeck = getActiveDeck(firstUser);
        Deck secondUserActiveDeck = getActiveDeck(secondUser);
        startNewGame(firstUser, secondUser, numberOfRounds, firstUserActiveDeck, secondUserActiveDeck, firstUserToken,
                secondUserToken);
    }

    private static GameManager gameManager;

    public static GameManager getGameManager() {
        return gameManager;
    }

    private void startNewGame(User firstUser, User secondUser, int roundsNumber, Deck firstUserActiveDeck,
            Deck secondUserActiveDeck, String firstUserToken, String secondUserToken) {

        ArrayList<Card> firstUserMainDeck = getMainOrSideDeckCards(firstUserActiveDeck, true);
        ArrayList<Card> firstUserSideDeck = getMainOrSideDeckCards(firstUserActiveDeck, false);
        ArrayList<Card> secondUserMainDeck = getMainOrSideDeckCards(secondUserActiveDeck, true);
        ArrayList<Card> secondUserSideDeck = getMainOrSideDeckCards(secondUserActiveDeck, false);
        Collections.shuffle(firstUserMainDeck);
        Collections.shuffle(secondUserMainDeck);
        gameManager = new GameManager();
        gameManager.addANewGame(firstUserActiveDeck, firstUserMainDeck, firstUserSideDeck, secondUserActiveDeck,
                secondUserMainDeck, secondUserSideDeck, firstUser.getName(), secondUser.getName(), roundsNumber,
                firstUserToken, secondUserToken);
        GameManager.getDuelControllerByIndex(firstUserToken).setPlayersChangedDecks(true);
        GameManager.getDuelControllerByIndex(firstUserToken).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
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
        outer: while (true) {
            if (listOfTypeOfGameThatSuggest.size() > 1) {
                for (Map.Entry<String, Integer> entry : listOfTypeOfGameThatSuggest.entrySet()) {
                    if (!entry.getKey().equals(token) && entry.getValue() == numberOfRounds) {
                        startGame(token, entry.getKey());
                        break outer;
                    }
                }
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Duel Started Successfully!");
    }

    private static synchronized void startGame(String firstPlayerToken, String secondPlayerToken) {
          System.out.println("salam");
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

    private boolean isThisDeckValid(Deck deck) {
        if (deck.getSizeOfMainDeck() >= 40 && deck.getSizeOfMainDeck() <= 60)
            return true;
        return false;
    }

    public Deck getActiveDeck(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
            if (allDecks.get(entry.getKey()).getIsDeckActive())
                return allDecks.get(entry.getKey());
        }
        return null;
    }

    private boolean doesThisUserNameExist(String player) {
        if (Storage.getUserByName(player) == null)
            return false;
        return true;
    }

    private boolean isItsRoundNumberCorrect(int number) {
        if (number == 1 || number == 3)
            return true;
        return false;
    }

    public static String getFirstPlayer() {
        return firstPlayer;
    }

    public static String getSecondPlayer() {
        return secondPlayer;
    }
}
