package project.server.controller.non_duel.shop;

import com.google.gson.JsonObject;
import project.client.view.LoginController;
import project.model.Deck;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;
import project.server.ServerController;
import project.model.Utility.Utility;
import project.server.ToGsonFormatForSendInformationToClient;
import project.server.controller.non_duel.storage.Storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Shop {

    private static User user;
    public static String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getBoughtCardName(command);
            int userAmount = LoginController.getOnlineUser().getMoney();
            if (!isItValidCardName(cardName)) {
                return "there is no card with this name";
            }
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
            if (cardAmount > userAmount) {
                return "not enough money";
            }
            LoginController.getOnlineUser().setMoney(userAmount - cardAmount);
            LoginController.getOnlineUser().addCardToAllUselessCards(card.getCardName());
            return "successful buy";
        }

        else if (ShopPatterns.isItShowAllPattern(command)) {
            return showAllCards();
        }

        if (command.matches("card show (.+)")) {
            return showCard(ShopPatterns.getShownCardName(command));
        }
        return "invalid command!";
    }

    private static String showAllCards() {
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        ArrayList<String> allCards = new ArrayList<>();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet())
            allCards.add(entry.getKey());
        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet())
            allCards.add(entry.getKey());
        Collections.sort(allCards);
        String returnString = "";
        for (int i = 0; i < allCards.size(); i++) {
            returnString += allCards.get(i);
            returnString += ":";
            returnString += getCardWithName(allCards.get(i)).getCardPrice();
            if (i <= allCards.size() - 2)
                returnString += "\n";
        }
        return returnString;
    }

    private static String showCard(String cardName) {
        if (!Storage.doesCardExist(cardName)) {
            return "card with name " + cardName + " does not exist";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();

        StringBuilder shownCardStringBuilder = new StringBuilder();
        shownCardStringBuilder.append("Name: " + cardName + "\n");
        if (allMonsterCards.containsKey(cardName)) {
            MonsterCard monsterCard = (MonsterCard) allMonsterCards.get(cardName);
            shownCardStringBuilder.append("Level: " + monsterCard.getLevel() + "\n");
            shownCardStringBuilder.append("Type: " + monsterCard.getMonsterCardFamily() + "\n");
            shownCardStringBuilder.append("ATK: " + monsterCard.getAttackPower() + "\n");
            shownCardStringBuilder.append("DEF: " + monsterCard.getDefensePower() + "\n");
            shownCardStringBuilder.append("Description: " + monsterCard.getCardDescription());
        } else {
            if (allSpellAndTrapCards.get(cardName).getCardType().equals(CardType.SPELL)) {
                SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardName);
                shownCardStringBuilder.append("Spell\n");
                shownCardStringBuilder.append("Type: " + spellCard.getSpellCardValue() + "\n");
                shownCardStringBuilder.append("Description: " + spellCard.getCardDescription());
            } else {
                TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardName);
                shownCardStringBuilder.append("Trap\n");
                shownCardStringBuilder.append("Type: " + trapCard.getTrapCardValue() + "\n");
                shownCardStringBuilder.append("Description: " + trapCard.getCardDescription());
            }
        }

        return shownCardStringBuilder.toString();
    }

    private static Card getCardWithName(String cardName) {
        String cardname = Utility.giveCardNameRemovingRedundancy(cardName);
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allSpellAndTrapCards.get(cardname) != null)
            return allSpellAndTrapCards.get(cardname);
        if (allMonsterCards.get(cardname) != null)
            return allMonsterCards.get(cardname);
        return null;
    }

    private static boolean isItValidCardName(String cardName) {
        return (Storage.doesCardExist(cardName));
    }


    public static String buyRequestFromClient(JsonObject details) {
        String token = "";
        String cardName = "";
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardNameForBuy").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }


        user = ServerController.getUserByToken(token);
        if (user == null) {
            return "{\"type\":\"Error\",\"message\":\"invalid token!\"}";
        }
        return buyCard(cardName);
    }

    private static String buyCard(String cardName) {
        int userAmount = user.getMoney();
        if (!isItValidCardName(cardName)) {
             return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "there is no card with this name");
        }
        Card card = getCardWithName(cardName);
        int cardAmount = card.getCardPrice();
        if (cardAmount > userAmount) {
             return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "not enough money");
        }
        user.setMoney(userAmount - cardAmount);
        user.addCardToAllUselessCards(cardName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful", String.valueOf(user.getMoney()));
    }

    public static String getMoneyOfUserRequestFromServer(JsonObject details) {
        String token = "";
        try {
            token = details.get("token").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByToken(token);
        if (user == null) {
            return "{\"type\":\"Error\",\"message\":\"invalid token!\"}";
        }
        return String.valueOf(user.getMoney());
    }

    public static String getNumberOfBoughtCardsByCardName(JsonObject details) {
        String token = "";
        String cardName = "";
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByToken(token);
        if (user == null) {
            return ServerController.getBadRequestFormat();
        }

        HashMap<String, Integer> numberOfCards = getNumberOfCards(cardName);
        return String.valueOf(numberOfCards.get("numberOfBoughtCards"));
    }


    public static HashMap<String, Integer> getNumberOfCards(String cardName) {
        HashMap<String, Integer> numberOfCards = new HashMap<>();
        HashMap<String, Deck> allDecks = user.getDecks();
        int numberOfCardsInDeck = 0;
        for (Map.Entry<String, Deck> entrySet : allDecks.entrySet()) {
            numberOfCardsInDeck += entrySet.getValue().numberOfCardsInDeck(cardName);
        }
        int numberOfUselessCards = Collections.frequency(user.getAllUselessCards(), cardName);
        numberOfCards.put("numberOfBoughtCards", numberOfCardsInDeck + numberOfUselessCards);
        numberOfCards.put("uselessCards", numberOfUselessCards);
        return numberOfCards;
    }
}
