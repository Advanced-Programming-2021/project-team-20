package project.controller.duel.PreliminaryPackage;

import project.controller.non_duel.profile.Profile;
import project.controller.non_duel.storage.Storage;
import project.model.Deck;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartDuel {

    private boolean isDuelStarted = false;
    private GameManager gameManager = new GameManager();

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
        int numberOfRounds = Integer.parseInt(foundCommands.get("rounds"));
        Deck firstUserActiveDeck = getActiveDeck(firstUser);
        if (firstUserActiveDeck == null) {
            return firstUser.getName() + " has no active deck";
        }

        Deck secondUserActiveDeck = getActiveDeck(secondUser);
        if (secondUserActiveDeck == null) {
            return secondUser.getName() + " has no active deck";
        }

        if (!isThisDeckValid(firstUser)) {
            return firstUser.getName() + "’s deck is invalid";
        }
        if (!isThisDeckValid(secondUser)) {
            return secondUser.getName() + "’s deck is invalid";
        }
        if (!isItsRoundNumberCorrect(numberOfRounds)) {
            return "number of rounds is not supported";
        }

        startNewGame(firstUser, secondUser, numberOfRounds, firstUserActiveDeck, secondUserActiveDeck);
        isDuelStarted = true;
        return "duel successfully started!\n" + firstUser.getName() + " must choose\n1.stone\n2.hand\n3.snips";
    }

    private void startNewGame(User firstUser, User secondUser, int roundsNumber, Deck firstUserActiveDeck,
            Deck secondUserActiveDeck) {

        ArrayList<Card> firstUserMainDeck = getMainOrSideDeckCards(firstUserActiveDeck, true);
        ArrayList<Card> firstUserSideDeck = getMainOrSideDeckCards(firstUserActiveDeck, false);
        ArrayList<Card> secondUserMainDeck = getMainOrSideDeckCards(secondUserActiveDeck, true);
        ArrayList<Card> secondUserSideDeck = getMainOrSideDeckCards(secondUserActiveDeck, false);
        Collections.shuffle(firstUserMainDeck);
        Collections.shuffle(secondUserMainDeck);
        gameManager.addANewGame(firstUserActiveDeck, firstUserMainDeck, firstUserSideDeck, secondUserActiveDeck,
                secondUserMainDeck, secondUserSideDeck, firstUser.getName(), secondUser.getName(), roundsNumber);
        GameManager.getDuelControllerByIndex(0).setPlayersChangedDecks(true);
        GameManager.getDuelControllerByIndex(0).setTurnSetedBetweenTwoPlayerWhenRoundBegin(false);
    }

    private ArrayList<Card> getMainOrSideDeckCards(Deck activeDeck, boolean isCardsInMainDeck) {

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

    private boolean isItsRoundNumberCorrect(int number) {
        if (number == 1 || number == 3)
            return true;
        return false;
    }
}
