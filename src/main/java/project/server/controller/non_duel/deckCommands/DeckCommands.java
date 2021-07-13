package project.server.controller.non_duel.deckCommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import project.server.ServerController;
import project.server.ToGsonFormatForSendInformationToClient;
import project.model.Utility.Utility;
import project.server.controller.non_duel.storage.Storage;
import project.model.Deck;
import project.model.User;
import project.model.cardData.General.Card;

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

    // private String showAllCards() {
    // HashMap<String, Deck> allDecksOfUser =
    // LoginController.getOnlineUser().getDecks();
    // List<String> allCards = new ArrayList<>();
    // for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
    // allCards.addAll(entry.getValue().getMainDeck());
    // allCards.addAll(entry.getValue().getSideDeck());
    // }
    // allCards.addAll(LoginController.getOnlineUser().getAllUselessCards());

    // allCards = allCards.stream().distinct().collect(Collectors.toList());
    // Collections.sort(allCards);

    // StringBuilder showCardsBuilder = new StringBuilder();
    // for (int i = 0; i < allCards.size(); i++) {
    // if (i > 0) {
    // showCardsBuilder.append("\n");
    // }
    // if (allMonsterCards.containsKey(allCards.get(i))) {
    // showCardsBuilder
    // .append(allCards.get(i) + ": " +
    // allMonsterCards.get(allCards.get(i)).getCardDescription());
    // } else {
    // showCardsBuilder
    // .append(allCards.get(i) + ": " +
    // allSpellAndTrapCard.get(allCards.get(i)).getCardDescription());
    // }
    // }
    // return showCardsBuilder.toString();
    // }

    // private String showOneDeck(HashMap<String, String> foundCommands) {

    // HashMap<String, Deck> allDecksOfUser =
    // LoginController.getOnlineUser().getDecks();
    // if (allDecksOfUser == null ||
    // !allDecksOfUser.containsKey(foundCommands.get("show deck with name"))) {
    // return "deck with name " + foundCommands.get("show deck with name") + " does
    // not exist";
    // }

    // StringBuilder showDeck = new StringBuilder();
    // showDeck.append("Deck: " + foundCommands.get("show deck with name") + "\n");

    // if (foundCommands.get("side").contains("-s")) {
    // showDeck.append("Side Deck:\nMonsters:");
    // showDeck.append(showCardsInMainOrSideDeck(
    // allDecksOfUser.get(foundCommands.get("show deck with name")).getSideDeck()));
    // } else {
    // showDeck.append("Main Deck:\nMonsters:");
    // showDeck.append(showCardsInMainOrSideDeck(
    // allDecksOfUser.get(foundCommands.get("show deck with name")).getMainDeck()));
    // }
    // return showDeck.toString();
    // }

    // private String showCardsInMainOrSideDeck(List<String> cardsInDeck) {

    // Collections.sort(cardsInDeck);

    // StringBuilder monsterCardsBuilder = new StringBuilder();
    // StringBuilder spellAndTrapCardsBuilder = new StringBuilder();

    // for (int i = 0; i < cardsInDeck.size(); i++) {
    // if (allMonsterCards.containsKey(cardsInDeck.get(i))) {
    // monsterCardsBuilder.append("\n" + cardsInDeck.get(i) + ": "
    // + allMonsterCards.get(cardsInDeck.get(i)).getCardDescription());
    // } else {
    // spellAndTrapCardsBuilder.append("\n" + cardsInDeck.get(i) + ": "
    // + allSpellAndTrapCard.get(cardsInDeck.get(i)).getCardDescription());
    // }
    // }
    // return monsterCardsBuilder.toString() + "\nSpell and Traps:" +
    // spellAndTrapCardsBuilder.toString();
    // }

    // private String showAllDecks() {
    // HashMap<String, Deck> allDecksOfUser =
    // LoginController.getOnlineUser().getDecks();
    // StringBuilder showAllDecks = new StringBuilder();
    // showAllDecks.append("Decks:\nActive deck:\n");
    // if (allDecksOfUser == null) {
    // showAllDecks.append("Other Decks:");
    // return showAllCards();
    // }

    // StringBuilder showOtherDecks = new StringBuilder();
    // for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
    // if (allDecksOfUser.get(entry.getKey()).getIsDeckActive()) {
    // showAllDecks
    // .append(entry.getKey() + ": main deck " +
    // allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
    // + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() +
    // ", ");
    // showAllDecks
    // .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() >= 40 ?
    // "valid\n" : "invalid\n");
    // } else {

    // showOtherDecks.append(
    // "\n" + entry.getKey() + ": main deck " +
    // allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck()
    // + ", side deck " + allDecksOfUser.get(entry.getKey()).getSizeOfSideDeck() +
    // ", ");
    // showOtherDecks
    // .append(allDecksOfUser.get(entry.getKey()).getSizeOfMainDeck() >= 40 ?
    // "valid" : "invalid");
    // }
    // }
    // showAllDecks.append("Other Decks:" + showOtherDecks.toString());
    // return showAllDecks.toString();
    // }

    public static String deleteCardFromMainOrSideDeck(JsonObject details) {
        String token = "";
        String cardName = "";
        String deckName = "";
        boolean isCardDeletedFromMainDeck;
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
            deckName = details.get("deckName").getAsString();
            isCardDeletedFromMainDeck = details.get("isMainDeck").getAsBoolean();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        HashMap<String, Deck> allDecksOfUser = user.getDecks();
        if (isCardDeletedFromMainDeck) {
            allDecksOfUser.get(deckName).deleteCardFromMainDeck((cardName));
        } else {
            allDecksOfUser.get(deckName).deleteCardFromSideDeck((cardName));
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Card Deleted From " + (isCardDeletedFromMainDeck ? "MainDeck " : "SideDeck ") + "Successfully!");
    }

    public static String addCardToMainOrSideDeck(JsonObject details) {
        String token = "";
        String cardName = "";
        String deckName = "";
        boolean isCardAddedToMainDeck;
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
            deckName = details.get("deckName").getAsString();
            isCardAddedToMainDeck = details.get("isMainDeck").getAsBoolean();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        HashMap<String, Deck> allDecksOfUser = user.getDecks();
        if (isCardAddedToMainDeck) {
            allDecksOfUser.get(deckName).addCardToMainDeck((cardName));
        } else {
            allDecksOfUser.get(deckName).addCardToSideDeck((cardName));
        }
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Card Added To " + (isCardAddedToMainDeck ? "MainDeck " : "SideDeck ") + "Successfully!");
    }

    public static String deleteCardFromAllUselessCards(JsonObject details) {
        String cardName = "";
        String token = "";
        try {
            cardName = details.get("cardName").getAsString();
            token = details.get("token").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }

        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        user.deleteCardFromAllUselessCards(cardName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Card Deleted From Useless Cards Successfully!");
    }

    public static String addCardToAllUselessCards(JsonObject details) {
        String cardName = "";
        String token = "";
        try {
            cardName = details.get("cardName").getAsString();
            token = details.get("token").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        user.addCardToAllUselessCards(cardName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Card Added To Useless Cards Successfully!");
    }

    public static String activateDeck(JsonObject details) {
        String token = "";
        String deckName = "";
        try {
            token = details.get("token").getAsString();
            deckName = details.get("deckName").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        HashMap<String, Deck> allDecksOfUser = user.getDecks();
        for (Map.Entry<String, Deck> entry : allDecksOfUser.entrySet()) {
            entry.getValue().setDeckActive(false);
        }
        allDecksOfUser.get(deckName).setDeckActive(true);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Deck Activated Successfully!");
    }

    public static String deleteDeck(JsonObject details) {
        String token = "";
        String deckName = "";
        try {
            token = details.get("token").getAsString();
            deckName = details.get("deckName").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }
        user.deleteDeck(deckName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Deck Deleted Successfully!");
    }

    public static String createDeck(JsonObject detailes) {
        String deckName = "";
        String token = "";
        try {
            deckName = detailes.get("deckName").getAsString();
            token = detailes.get("token").getAsString();
        } catch (Exception e) {
            return ServerController.getBadRequestFormat();
        }
        User user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getUserNotLogined();
        }

        HashMap<String, Deck> allDecksOfUser = user.getDecks();
        if (allDecksOfUser != null && allDecksOfUser.containsKey(deckName)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error",
                    "Deck Already Exists!");
        }
        user.addDeckToAllDecks(deckName, new Deck(deckName));
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful",
                "Deck Created Successfully!");
    }
}
