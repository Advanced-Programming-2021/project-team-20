package controller.non_duel.deckCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.Deck;
import model.cardData.General.Card;

public class DeckCommands {

    private HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
    private HashMap<String, Card> allSpellAndTrapCard = Storage.getAllSpellAndTrapCards();

    public String findCommands(String command) {
        DeckCommandsPatterns deckCommandsPatterns = new DeckCommandsPatterns();

        HashMap<String, String> foundCommadns = deckCommandsPatterns.findCommands(command);
        if (foundCommadns == null) {
            return "invalid command!";

        }

        if (foundCommadns.containsKey("create deck")) {
            return createDeck(foundCommadns.get("create deck"));
        }

        if (foundCommadns.containsKey("delete deck")) {
            return deleteDeck(foundCommadns.get("delete deck"));
        }

        if (foundCommadns.containsKey("activate deck")) {
            return activateDeck(foundCommadns.get("activate deck"));
        }

        if (foundCommadns.containsKey("add card to")) {
            return addCardToDeck(foundCommadns);
        }

        if (foundCommadns.containsKey("delete card from")) {
            return deleteCardFromDeck(foundCommadns);
        }

        if (foundCommadns.containsKey("show all deck")) {
            return showAllDecks();
        }

        if (foundCommadns.containsKey("show deck with name")) {
            return showOneDeck(foundCommadns);
        }

        return showAllCards();

    }

    private String showAllCards() {
        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        List<String> allCards = new ArrayList<>();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            allCards.addAll(entry.getValue().getMainDeck());
            allCards.addAll(entry.getValue().getSideDeck());
        }
        allCards.addAll(Profile.getOnlineUser().getAllUselessCards());

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

        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(foundCommands.get("show deck with name"))) {
            return "deck with name " + foundCommands.get("show deck with name") + " does not exist";
        }

        StringBuilder showDeck = new StringBuilder();
        showDeck.append("Deck: " + foundCommands.get("show deck with name\n"));

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

        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        StringBuilder showAllDecks = new StringBuilder();
        showAllDecks.append("Decks:\nActive deck:\n");
        if (allDecksOfUser == null) {
            showAllDecks.append("Other Decks:\n");
            return showAllCards();
        }

        StringBuilder showOtherDecks = new StringBuilder();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            if (allDecksOfUser.get(entry.getKey()).getIsDeckActive()) {
                showAllDecks
                        .append(entry.getKey() + ": main deck " + allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
                                + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() + ", ");
                showAllDecks
                        .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() > 40 ? "valid\n" : "invalid\n");
            } else {

                showOtherDecks.append(
                        "\n" + entry.getKey() + ": main deck " + allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
                                + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() + ", ");
                showOtherDecks
                        .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() > 40 ? "valid" : "invalid");
            }
        }

        showAllDecks.append("Other Decks:" + showOtherDecks.toString());

        return showAllDecks.toString();
    }

    private String deleteCardFromDeck(HashMap<String, String> foundCommands) {
        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(foundCommands.get("deck"))) {
            return "deck with name " + foundCommands.get("deck") + " does not exist";
        }

        if (foundCommands.get("delete card from").contains("-s")) {
            if (!allDecksOfUser.get(foundCommands.get("deck")).getSideDeck().contains(foundCommands.get("card"))) {
                return "card with name " + foundCommands.get("card") + " does not exist in side deck";
            } 
            allDecksOfUser.get(foundCommands.get("deck")).deleteCardFromSideDeck(foundCommands.get("card"));
        } else{
            if(!allDecksOfUser.get(foundCommands.get("deck")).getMainDeck().contains(foundCommands.get("card"))){
                return "card with name " + foundCommands.get("card") + " does not exist in main deck";     
            }
            allDecksOfUser.get(foundCommands.get("deck")).deleteCardFromMainDeck(foundCommands.get("card"));
        }
        return "card removed from deck successfully";
    }

    private String addCardToDeck(HashMap<String, String> foundCommands) {

        if (!Profile.getOnlineUser().doesCardExistsInUselessCards(foundCommands.get("card"))) {
            return "card with name " + foundCommands.get("card") + " does not exist";
        }
        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(foundCommands.get("deck"))) {
            return "deck with name " + foundCommands.get("deck") + " does not exist";
        }

        if (foundCommands.get("add card to").contains("-s")) {
            if (allDecksOfUser.get(foundCommands.get("deck")).getSizeOfSideDeck() == 15) {
                return "side deck is full";
            }
        } else {
            if (allDecksOfUser.get(foundCommands.get("deck")).getSizeOfMainDeck() == 60) {
                return "main deck is full";
            }
        }

        int numberOfCardsInDeck = allDecksOfUser.get(foundCommands.get("deck"))
                .numberOfCardsInDeck(foundCommands.get("card"));
        if (numberOfCardsInDeck == 3) {
            return "there are already three cards with name " + foundCommands.get("card") + " in deck "
                    + foundCommands.get("deck");
        }

        Profile.getOnlineUser().deleteCardFromAllUselessCards(foundCommands.get("card"));

        if (foundCommands.get("add card to").contains("-s")) {
            allDecksOfUser.get(foundCommands.get("deck")).addCardToSideDeck(foundCommands.get("card"));
        } else {
            allDecksOfUser.get(foundCommands.get("deck")).addCardToMainDeck(foundCommands.get("card"));
        }
        return "card added to deck successfully";

    }

    private String activateDeck(String deckname) {
        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(deckname)) {
            return "deck with name " + deckname + " does not exist";
        }
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            allDecksOfUser.get(entry.getKey()).setDeckActive(false);
        }
        allDecksOfUser.get(deckname).setDeckActive(true);
        return "deck activated successfully!";
    }

    private String deleteDeck(String deckname) {
        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser == null || !allDecksOfUser.containsKey(deckname)) {
            return "deck with name " + deckname + " does not exist";
        }
        Profile.getOnlineUser().deleteDeck(deckname);
        return "deck deleted successfully!";

    }

    private String createDeck(String deckname) {

        HashMap<String, Deck> allDecksOfUser = Profile.getOnlineUser().getDecks();
        if (allDecksOfUser != null && allDecksOfUser.containsKey(deckname)) {
            return "deck with name " + deckname + " already exists";
        }
        Profile.getOnlineUser().addDeckToAllDecks(deckname, new Deck(deckname));
        return "deck created successfully!";
    }

}