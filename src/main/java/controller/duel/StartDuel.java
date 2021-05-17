package controller.duel;

import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.Deck;
import model.User;
import model.cardData.General.Card;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartDuel {

    private boolean isDuelStarted = false;

    public String findCommand(String command) {

        if (isDuelStarted) {
            return GameManager.getDuelControllerByIndex(0).getInput(command, true);
        }

        StartDuelPatterns startDuelPatterns = new StartDuelPatterns();
        HashMap<String, String> foundCommands = startDuelPatterns.findCommand(command);
        if (foundCommands == null) {
            return "invalid command!";
        }

        if (!doesThisUserNameExist(foundCommands.get("secondPlayer"))) {
            return "there is no player with this username";
        }
        User secondUser = Storage.getUserByName(foundCommands.get("secondPlayer"));
        User firstUser = Profile.getOnlineUser();

        if (getActiveDeck(firstUser) == null) {
            return firstUser.getName() + " has no active deck";
        }
        if (getActiveDeck(secondUser) == null) {
            return secondUser.getName() + " has no active deck";
        }
        if (!isThisDeckValid(firstUser)) {
            return firstUser.getName() + "’s deck is invalid";
        }
        if (!isThisDeckValid(secondUser)) {
            return secondUser.getName() + "’s deck is invalid";
        }
        if (!isItsRoundNumberCorrect(command)) {
            return "number of rounds is not supported";
        }

        startNewGame(firstUser, secondUser, StartDuelPatterns.getRoundsNumber(command));
        isDuelStarted = true;
        return "duel successfully started!\n\n" + firstUser.getName() + " must choose\n1.stone\n2.hand\n3.snips";
    }

    private void startNewGame(User firstUser, User secondUser, int roundsNumber) {

        ArrayList<Card> firstUserMainDeck = getMainOrSideDeckCards(firstUser, true);
        ArrayList<Card> firstUserSideDeck = getMainOrSideDeckCards(firstUser, false);
        ArrayList<Card> secondUserMainDeck = getMainOrSideDeckCards(secondUser, true);
        ArrayList<Card> secondUserSideDeck = getMainOrSideDeckCards(secondUser, false);

        GameManager gameManager = new GameManager();
        gameManager.addANewGame(firstUserMainDeck, firstUserSideDeck, secondUserMainDeck, secondUserSideDeck,
                firstUser.getName(), secondUser.getName(), roundsNumber);
        GameManager.getDuelControllerByIndex(0).startDuel(0);
        GameManager.getDuelControllerByIndex(0).setPlayersChangedDecks(true);
        GameManager.getDuelControllerByIndex(0).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);

    }

    private ArrayList<Card> getMainOrSideDeckCards(User user, boolean isCardsInMainDeck) {

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();

        ArrayList<Card> cardsInMainOrSideDeck = new ArrayList<>();
        Deck activeDeck = getActiveDeck(user);
        List<String> cardsToBeCloned = new ArrayList<>();
        if (isCardsInMainDeck) {
            cardsToBeCloned = activeDeck.getMainDeck();
        } else {
            cardsToBeCloned = activeDeck.getSideDeck();
        }

        for (int i = 0; i < cardsToBeCloned.size(); i++) {
            if (allMonsterCards.containsKey(cardsToBeCloned.get(i))) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(cardsToBeCloned.get(i));
                cardsInMainOrSideDeck.add((MonsterCard) monsterCard.clone());
            } else if (allSpellAndTrapCards.containsKey(cardsToBeCloned.get(i))) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(cardsToBeCloned.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardsToBeCloned.get(i));
                    cardsInMainOrSideDeck.add((SpellCard) spellCard.clone());
                } else {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardsToBeCloned.get(i));
                    cardsInMainOrSideDeck.add((TrapCard) trapCard.clone());
                }
            }
        }
        return cardsInMainOrSideDeck;
    }

    private boolean isThisDeckValid(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        Deck deck = null;
        for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
            if (allDecks.get(entry.getKey()).getIsDeckActive())
                deck = allDecks.get(entry.getKey());
        }
        if (deck.getSizeOfMainDeck() >= 40 && deck.getSizeOfMainDeck() <= 60)
            return true;
        return false;
    }

    private Deck getActiveDeck(User user) {
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

    private boolean isItsRoundNumberCorrect(String command) {
        int number = StartDuelPatterns.getRoundsNumber(command);
        if (number == 1 || number == 3)
            return true;
        return false;
    }
}
