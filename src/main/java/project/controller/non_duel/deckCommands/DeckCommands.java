package project.controller.non_duel.deckCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import project.View.LoginController;
import project.controller.non_duel.profile.Profile;
import project.controller.non_duel.storage.Storage;
import project.model.Deck;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;

public class DeckCommands {

    private HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
    private HashMap<String, Card> allSpellAndTrapCard = Storage.getAllSpellAndTrapCards();

    // public String findCommands(String command) {
    // DeckCommandsPatterns deckCommandsPatterns = new DeckCommandsPatterns();

    // HashMap<String, String> foundCommadns =
    // deckCommandsPatterns.findCommands(command);
    // if (foundCommadns == null) {
    // return "invalid command!";

    // }

    // if (foundCommadns.containsKey("create deck")) {
    // return createDeck(foundCommadns.get("create deck"));
    // }

    // if (foundCommadns.containsKey("delete deck")) {
    // return deleteDeck(foundCommadns.get("delete deck"));
    // }

    // if (foundCommadns.containsKey("activate deck")) {
    // return activateDeck(foundCommadns.get("activate deck"));
    // }

    // if (foundCommadns.containsKey("add card to")) {
    // return addCardToDeck(foundCommadns);
    // }

    // if (foundCommadns.containsKey("delete card from")) {
    // return deleteCardFromDeck(foundCommadns);
    // }

    // if (foundCommadns.containsKey("show all deck")) {
    // return showAllDecks();
    // }

    // if (foundCommadns.containsKey("show deck with name")) {
    // return showOneDeck(foundCommadns);
    // }

    // return showAllCards();

    // }

    private String showAllCards() {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        List<String> allCards = new ArrayList<>();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            allCards.addAll(entry.getValue().getMainDeck());
            allCards.addAll(entry.getValue().getSideDeck());
        }
        allCards.addAll(LoginController.getOnlineUser().getAllUselessCards());

        allCards = allCards.stream().distinct().collect(Collectors.toList());
        Collections.sort(allCards);

        StringBuilder showCardsBuilder = new StringBuilder();
        for (int i = 0; i < allCards.size(); i++) {
            if (i > 0) {
                showCardsBuilder.append("\n");
            }
            if (allMonsterCards.containsKey(allCards.get(i))) {
                showCardsBuilder
                        .append(allCards.get(i) + ": " + allMonsterCards.get(allCards.get(i)).getCardDescription());
            } else {
                showCardsBuilder
                        .append(allCards.get(i) + ": " + allSpellAndTrapCard.get(allCards.get(i)).getCardDescription());
            }
        }
        return showCardsBuilder.toString();
    }

    private String showOneDeck(HashMap<String, String> foundCommands) {

        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(foundCommands.get("show deck with name"))) {
            return "deck with name " + foundCommands.get("show deck with name") + " does not exist";
        }

        StringBuilder showDeck = new StringBuilder();
        showDeck.append("Deck: " + foundCommands.get("show deck with name") + "\n");

        if (foundCommands.get("side").contains("-s")) {
            showDeck.append("Side Deck:\nMonsters:");
            showDeck.append(showCardsInMainOrSideDeck(
                    allDecksOfUser.get(foundCommands.get("show deck with name")).getSideDeck()));
        } else {
            showDeck.append("Main Deck:\nMonsters:");
            showDeck.append(showCardsInMainOrSideDeck(
                    allDecksOfUser.get(foundCommands.get("show deck with name")).getMainDeck()));
        }
        return showDeck.toString();
    }

    private String showCardsInMainOrSideDeck(List<String> cardsInDeck) {

        Collections.sort(cardsInDeck);

        StringBuilder monsterCardsBuilder = new StringBuilder();
        StringBuilder spellAndTrapCardsBuilder = new StringBuilder();

        for (int i = 0; i < cardsInDeck.size(); i++) {
            if (allMonsterCards.containsKey(cardsInDeck.get(i))) {
                monsterCardsBuilder.append("\n" + cardsInDeck.get(i) + ": "
                        + allMonsterCards.get(cardsInDeck.get(i)).getCardDescription());
            } else {
                spellAndTrapCardsBuilder.append("\n" + cardsInDeck.get(i) + ": "
                        + allSpellAndTrapCard.get(cardsInDeck.get(i)).getCardDescription());
            }
        }
        return monsterCardsBuilder.toString() + "\nSpell and Traps:" + spellAndTrapCardsBuilder.toString();
    }

    private String showAllDecks() {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        StringBuilder showAllDecks = new StringBuilder();
        showAllDecks.append("Decks:\nActive deck:\n");
        if (allDecksOfUser == null) {
            showAllDecks.append("Other Decks:");
            return showAllCards();
        }

        StringBuilder showOtherDecks = new StringBuilder();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            if (allDecksOfUser.get(entry.getKey()).getIsDeckActive()) {
                showAllDecks
                        .append(entry.getKey() + ": main deck " + allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
                                + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() + ", ");
                showAllDecks
                        .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() >= 40 ? "valid\n" : "invalid\n");
            } else {

                showOtherDecks.append(
                        "\n" + entry.getKey() + ": main deck " + allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
                                + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() + ", ");
                showOtherDecks
                        .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() >= 40 ? "valid" : "invalid");
            }
        }
        showAllDecks.append("Other Decks:" + showOtherDecks.toString());
        return showAllDecks.toString();
    }

    public void deleteCardFromMainOrSideDeck(String deckname, String cardname, boolean isDeleteFromMainDeck) {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (isDeleteFromMainDeck) {
            allDecksOfUser.get(deckname).deleteCardFromMainDeck(cardname);
        } else {
            allDecksOfUser.get(deckname).deleteCardFromSideDeck(cardname);
        }
    }

    public void addCardToMainOrSideDeck(String deckname, String cardname, boolean isCardAddedToMainDeck) {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (isCardAddedToMainDeck) {
            allDecksOfUser.get(deckname).addCardToMainDeck(cardname);
        } else {
            allDecksOfUser.get(deckname).addCardToSideDeck(cardname);
        }
    }

    public void deleteCardFromAllUselessCards(String cardname) {
        LoginController.getOnlineUser().deleteCardFromAllUselessCards(cardname);
    }

    public void addCardToAllUselessCards(String cardname) {
        LoginController.getOnlineUser().addCardToAllUselessCards(cardname);
    }

    public boolean canAddCardToDeck(String deckname, String cardname) {

        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();

        int numberOfCardsInDeck = allDecksOfUser.get(deckname).numberOfCardsInDeck(cardname);
        int numberOfAllowedUsages = 0;
        if (allSpellAndTrapCard.containsKey(cardname)) {
            numberOfAllowedUsages = allSpellAndTrapCard.get(cardname).getNumberOfAllowedUsages();
        } else if (allMonsterCards.containsKey(cardname)) {
            numberOfAllowedUsages = 3;
        }
        if (numberOfCardsInDeck == numberOfAllowedUsages) {
            return false;
        }
        return true;
    }

    public String activateDeck(String deckname) {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            entry.getValue().setDeckActive(false);
        }
        allDecksOfUser.get(deckname).setDeckActive(true);
        return "deck activated successfully!";
    }

    public String deleteDeck(String deckname) {
    
        LoginController.getOnlineUser().deleteDeck(deckname);
        return "deck deleted successfully!";
    }

    public String createDeck(String deckname) {
        HashMap<String, Deck> allDecksOfUser = LoginController.getOnlineUser().getDecks();
        if (allDecksOfUser != null && allDecksOfUser.containsKey(deckname)) {
            return "deck already exists";
        }
        LoginController.getOnlineUser().addDeckToAllDecks(deckname, new Deck(deckname));
        return "deck created successfully!";
    }

    public HashMap<String, Integer> getNumberOfEachTypeOfCardsInDeck(String deckname) {
        int numberOfMonsterCards = 0;
        int numberOfSpellCards = 0;
        int numberOfTrapCards = 0;
        List<String> mainDeckCards = LoginController.getOnlineUser().getDecks().get(deckname).getMainDeck();
        HashMap<String, Integer> sizeOfEachPart = new HashMap<>();
        sizeOfEachPart.put("mainDeckSize", mainDeckCards.size());
        sizeOfEachPart.put("sideDeckSize",
                LoginController.getOnlineUser().getDecks().get(deckname).getSizeOfSideDeck());
        for (int i = 0; i < mainDeckCards.size(); i++) {
            if (allMonsterCards.containsKey(mainDeckCards.get(i))) {
                numberOfMonsterCards++;
            } else {
                if (allSpellAndTrapCard.get(mainDeckCards.get(i)).getCardType().equals(CardType.SPELL)) {
                    numberOfSpellCards++;
                } else {
                    numberOfTrapCards++;
                }
            }
        }
        sizeOfEachPart.put("monstersSize", numberOfMonsterCards);
        sizeOfEachPart.put("spellsSize", numberOfSpellCards);
        sizeOfEachPart.put("trapsSize", numberOfTrapCards);
        return sizeOfEachPart;
    }
}