package project.server.controller.non_duel.shop;

import com.google.gson.JsonObject;
import project.client.view.LoginController;
import project.model.Auction;
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
        } else if (ShopPatterns.isItShowAllPattern(command)) {
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


        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
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
        if (!card.getIsShopAllowed()) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "NOT ALLOWED");
        }
        if (cardAmount > userAmount) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "not enough money");
        }
        if (card.getNumberOfCardsInShop() <= 0) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Error", "not enough cards in shop");
        }
        user.setMoney(userAmount - cardAmount);
        user.addCardToAllUselessCards(cardName);
        Storage.changeShopCardInformation(card, card.getIsShopAllowed(), card.getNumberOfCardsInShop() - 1);
        card.decreaseNumberOfCardsInShop();
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("Successful", String.valueOf(user.getMoney()));
    }

    public static String getMoneyOfUserRequestFromServer(JsonObject details) {
        String token = "";
        try {
            token = details.get("token").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
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
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
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

    public static String adminAllowACard(JsonObject details, boolean expectedBoolean) {
        String token = "";
        String cardName = "";
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ServerController.getBadRequestFormat();
        }
        if (!user.getName().equals("admin")) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "NOT ADMIN");
        }
        if (Storage.getCardByName(cardName) == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "invalid card name");
        } else {
            Card card = Storage.getCardByName(cardName);
            assert card != null;
            if (card.getIsShopAllowed() == expectedBoolean) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("SUCCESSFUL", "Before");
            } else {
                card.setShopAllowed(expectedBoolean);
                Storage.changeShopCardInformation(card, expectedBoolean, card.getNumberOfCardsInShop());
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("SUCCESSFUL", "Now");
            }
        }
    }

    public static String changeNumberOfCardsInShop(JsonObject details, int changeInt) {
        String token = "";
        String cardName = "";
        Card card = null;
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        card = Storage.getCardByName(cardName);
        if (card == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID CARD");
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ServerController.getBadRequestFormat();
        }
        if (!user.getName().equals("admin")) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "ADMIN ERROR");
        } else {
            int newNumber = card.getNumberOfCardsInShop() + changeInt;
            if (newNumber < 0) {
                return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "NUMBER OF CARDS IN SHOP IS 0");
            }
            card.setNumberOfCardsInShop(newNumber);
            Storage.changeShopCardInformation(card, card.getIsShopAllowed(), newNumber);
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("SUCCESSFUL", "SUCCESSFUL");
        }
    }

    public static String getShowNumberOfBoughtCardsForClient(JsonObject details) {
        String token = "";
        String cardName = "";
        Card card = null;
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        card = Storage.getCardByName(cardName);
        if (card == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID CARD");
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ServerController.getBadRequestFormat();
        }
        HashMap<String, Integer> numberOfCards = getNumberOfCards(cardName);
        String answer = ToGsonFormatForSendInformationToClient.toGsonFormatForNumberOfBoughtCardsAndUselessCards(numberOfCards);
        return answer;
//        equalNumberOfUselessCardsLabel.setText("Useless Cards: " + numberOfCards.get("uselessCards"));
//        equalNumbserOfShoppingCardsLabel.setText("Bought Cards: " + numberOfCards.get("numberOfBoughtCards"));
    }

    public static String showInformationOfAdmin(JsonObject details) {
        String cardName = "";
        try {
            cardName = details.get("cardName").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        Card card = Storage.getCardByName(cardName);
        if (card == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID CARD");
        }
        boolean isAllowed = card.getIsShopAllowed();
        String isAllowedString = "";
        if (isAllowed) {
            isAllowedString = "Allowed";
        } else {
            isAllowedString = "Not Allowed";
        }

        String numberOfCardsInShop = String.valueOf(card.getNumberOfCardsInShop());
        String answer = ToGsonFormatForSendInformationToClient.showInformationOfAdmin(isAllowedString, numberOfCardsInShop);
        return answer;
    }

    public static String createAuction(JsonObject details) {
        String cardName = "";
        String token = "";
        String username = "";
        int initialPrice = 0;
        try {
            token = details.get("token").getAsString();
            cardName = details.get("cardName").getAsString();
            //?
            initialPrice = details.get("initialPrice").getAsInt();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID TOKEN");
        }
        username = user.getName();
        Card card = Storage.getCardByName(cardName);
        if (card == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID CARD");
        }

        if (!user.getAllUselessCards().contains(cardName)) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "YOU DON'T HAVE THIS CARD IN YOUR USELESS CARDS");
        }

        Auction auction = new Auction(username, initialPrice, cardName);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("SUCCESSFUL", String.valueOf(auction.getAuctionCode()));
    }

    public static String getAuctionInfo(JsonObject details) {
        String auctionCode = "";
        String token = "";
        try {
            token = details.get("token").getAsString();
            auctionCode = details.get("auctionCode").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID TOKEN");
        }
        String cardName = Auction.getCardNameByAuctionCode(Integer.parseInt(auctionCode));
        String buyerName = Auction.getBuyerNameByAuctionCode(auctionCode);
        if (cardName == null) {
            return ServerController.getBadRequestFormat();
        }
        if (buyerName.equals("null")) {
            return "FAILED";
        }
        user.setMoney(user.getMoney() + Storage.getCardByName(cardName).getCardPrice());
        user.deleteCardFromAllUselessCards(cardName);
        User buyerUser = Storage.getUserByName(buyerName);
        buyerUser.addCardToAllUselessCards(cardName);
        return "SUCCESSFUL";
    }

    public static String buyRequestForAuction(JsonObject details) {
        String auctionCode = "";
        String token = "";
        String price = "";
        try {
            token = details.get("token").getAsString();
            auctionCode = details.get("auctionCode").getAsString();
            price = details.get("price").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID TOKEN");
        }
        int requestedAuctionCode = Integer.parseInt(auctionCode);
        int numberOfAuctions = Auction.getNumberOfAllAuctons();
        if (requestedAuctionCode > numberOfAuctions) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID AUCTION CODE");
        }
        String isActive = Auction.getIsActivated(auctionCode);
        boolean isActivated = Boolean.parseBoolean(isActive);
        if (!isActivated) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "EXPIRED");
        }
        int currentPrice = Auction.getPriceByCode(auctionCode);
        int suggestedPrice = Integer.parseInt(price);
        System.out.println(currentPrice + "c");
        System.out.println(suggestedPrice + "s");
        if (currentPrice > suggestedPrice) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID PRICE");
        }
        int userMoney = user.getMoney();
        if (userMoney < suggestedPrice) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "NOT ENOUGH MONEY");
        }
        String cardName = Auction.getCardNameByAuctionCode(Integer.parseInt(auctionCode));
        Card card = Storage.getCardByName(cardName);
        user.setMoney(user.getMoney() - card.getCardPrice());
        Auction.changeAuctionBuyerAndPrice(auctionCode, user.getName(), suggestedPrice);
        return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("SUCCESSFUL", cardName);
    }

    public static String getAuctionInfoForBuyer(JsonObject details) {
        String auctionCode = "";
        String token = "";
        try {
            token = details.get("token").getAsString();
            auctionCode = details.get("auctionCode").getAsString();
        } catch (Exception a) {
            return ServerController.getBadRequestFormat();
        }
        user = ServerController.getUserByTokenAndRefreshLastConnectionTime(token);
        if (user == null) {
            return ToGsonFormatForSendInformationToClient.toGsonFormatForOnlyTypeAndMessage("ERROR", "INVALID TOKEN");
        }
        String cardName = Auction.getCardNameByAuctionCode(Integer.parseInt(auctionCode));
        String buyerName = Auction.getBuyerNameByAuctionCode(auctionCode);
        if (cardName == null) {
            return ServerController.getBadRequestFormat();
        }
        if (!buyerName.equals(user.getName())) {
            return "FAILED";
        }
        return "SUCCESSFUL";
    }
}
