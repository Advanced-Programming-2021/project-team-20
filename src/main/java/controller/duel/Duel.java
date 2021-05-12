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

public class Duel {
    public String findCommand(String command) {
        if(DuelPatterns.isItCorrectPattern(command)) {
            String secondPlayer = DuelPatterns.getSecondPlayer(command);
            if (!doesThisUserNameExist(secondPlayer)) {
                return "there is no player with this username";
            }
            User secondUser = Storage.getUserByName(secondPlayer);
            User firstUser = Profile.getOnlineUser();
            if (!doesItHaveAnyActiveDeck(firstUser)) {
                return firstUser.getName() + " has no active deck";
            }
            if (!doesItHaveAnyActiveDeck(secondUser)) {
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
            startNewGame(firstUser, secondUser, DuelPatterns.getRoundsNumber(command));
            return "duel successfully started!";
        }
        return "invalid commend!";
    }


    private void startNewGame(User firstUser, User secondUser, int roundsNumber) {
        HashMap<String, Deck> allDecks = firstUser.getDecks();
        Deck deckFirstPlayer = null;
        for (Map.Entry string : allDecks.entrySet()) {
            if (allDecks.get(string).getIsDeckActive()) deckFirstPlayer = allDecks.get(string);
        }
        //arrayList first player main deck
        ArrayList<Card> cardsFirstPlayerMainDeck = null;
        List<String> stringFirstPlayerMainDeck;
        stringFirstPlayerMainDeck = deckFirstPlayer.getMainDeck();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (int i = 0; i < stringFirstPlayerMainDeck.size(); i++) {
            if (allSpellAndTrapCards.get(stringFirstPlayerMainDeck.get(i)) != null) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(stringFirstPlayerMainDeck.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(stringFirstPlayerMainDeck.get(i));
                    cardsFirstPlayerMainDeck.add((Card) spellCard.clone());
                }
                else if (Card.isCardATrap(allSpellAndTrapCards.get(stringFirstPlayerMainDeck.get(i)))) {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(stringFirstPlayerMainDeck.get(i));
                    cardsFirstPlayerMainDeck.add((Card) trapCard.clone());
                }
            }
            else if (allMonsterCards.get(stringFirstPlayerMainDeck.get(i)) != null) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(stringFirstPlayerMainDeck.get(i));
                cardsFirstPlayerMainDeck.add((Card) monsterCard.clone());
            }
        }
        //arrayList first player side deck
        ArrayList<Card> cardsFirstPlayerSideDeck = null;
        List<String> stringFirstPlayerSideDeck;
        stringFirstPlayerSideDeck = deckFirstPlayer.getSideDeck();
        for (int i = 0; i < stringFirstPlayerSideDeck.size(); i++) {
            if (allSpellAndTrapCards.get(stringFirstPlayerSideDeck.get(i)) != null) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(stringFirstPlayerSideDeck.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(stringFirstPlayerSideDeck.get(i));
                    cardsFirstPlayerSideDeck.add((Card) spellCard.clone());
                }
                else if (Card.isCardATrap(allSpellAndTrapCards.get(stringFirstPlayerSideDeck.get(i)))) {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(stringFirstPlayerSideDeck.get(i));
                    cardsFirstPlayerSideDeck.add((Card) trapCard.clone());
                }
            }
            else if (allMonsterCards.get(stringFirstPlayerSideDeck.get(i)) != null) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(stringFirstPlayerSideDeck.get(i));
                cardsFirstPlayerSideDeck.add((Card) monsterCard.clone());
            }
        }




        HashMap<String, Deck> allDecksSecondPlayer = secondUser.getDecks();
        Deck deckSecondPlayer = null;
        for (Map.Entry string : allDecksSecondPlayer.entrySet()) {
            if (allDecksSecondPlayer.get(string).getIsDeckActive()) deckFirstPlayer = allDecksSecondPlayer.get(string);
        }
        //arrayList second player main deck
        ArrayList<Card> cardsSecondPlayerMainDeck = null;
        List<String> stringSecondPlayerMainDeck;
        stringSecondPlayerMainDeck = deckSecondPlayer.getMainDeck();
        for (int i = 0; i < stringSecondPlayerMainDeck.size(); i++) {
            if (allSpellAndTrapCards.get(stringSecondPlayerMainDeck.get(i)) != null) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(stringSecondPlayerMainDeck.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(stringSecondPlayerMainDeck.get(i));
                    cardsSecondPlayerMainDeck.add((Card) spellCard.clone());
                }
                else if (Card.isCardATrap(allSpellAndTrapCards.get(stringSecondPlayerMainDeck.get(i)))) {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(stringSecondPlayerMainDeck.get(i));
                    cardsSecondPlayerMainDeck.add((Card) trapCard.clone());
                }
            }
            else if (allMonsterCards.get(stringSecondPlayerMainDeck.get(i)) != null) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(stringSecondPlayerMainDeck.get(i));
                cardsSecondPlayerMainDeck.add((Card) monsterCard.clone());
            }
        }
        //arrayList first player side deck
        ArrayList<Card> cardsSecondPlayerSideDeck = null;
        List<String> stringSecondPlayerSideDeck;
        stringSecondPlayerSideDeck = deckSecondPlayer.getSideDeck();
        for (int i = 0; i < stringSecondPlayerSideDeck.size(); i++) {
            if (allSpellAndTrapCards.get(stringSecondPlayerSideDeck.get(i)) != null) {
                if (Card.isCardASpell(allSpellAndTrapCards.get(stringSecondPlayerSideDeck.get(i)))) {
                    SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(stringSecondPlayerSideDeck.get(i));
                    cardsSecondPlayerSideDeck.add((Card) spellCard.clone());
                }
                else if (Card.isCardATrap(allSpellAndTrapCards.get(stringSecondPlayerSideDeck.get(i)))) {
                    TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(stringSecondPlayerSideDeck.get(i));
                    cardsSecondPlayerSideDeck.add((Card) trapCard.clone());
                }
            }
            else if (allMonsterCards.get(stringSecondPlayerSideDeck.get(i)) != null) {
                MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(stringSecondPlayerSideDeck.get(i));
                cardsSecondPlayerSideDeck.add((Card) monsterCard.clone());
            }
        }
        GameManager gameManager = new GameManager();
        gameManager.addANewGame(cardsFirstPlayerMainDeck, cardsFirstPlayerSideDeck,
            cardsSecondPlayerMainDeck, cardsSecondPlayerSideDeck, firstUser.getName(), secondUser.getName(), roundsNumber);

    }


    private boolean isThisDeckValid(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        Deck deck = null;
        for (Map.Entry string : allDecks.entrySet()) {
            if (allDecks.get(string).getIsDeckActive()) deck = allDecks.get(string);
        }
        if (deck.getSizeOfMainDeck() >= 40 && deck.getSizeOfMainDeck() <= 60) return true;
        return false;
    }

    private boolean doesItHaveAnyActiveDeck(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        for (Map.Entry string : allDecks.entrySet()) {
            if (allDecks.get(string).getIsDeckActive()) return true;
        }
        return false;
    }

    private boolean doesThisUserNameExist(String player) {
        if (Storage.getUserByName(player) == null) return false;
        return true;
    }

    private boolean isItsRoundNumberCorrect(String command) {
        int number = DuelPatterns.getRoundsNumber(command);
        if (number == 1 || number == 3) return true;
        return false;
    }
}
