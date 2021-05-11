package controller.non_duel.shop;

import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;

import java.util.*;

public class Shop {

    public String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getCardName(command);
            int userAmount = Profile.getOnlineUser().getMoney();
            if (isItInvalidCardName(cardName)) {
                return "there is no card with this name";
            }
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
            if (cardAmount > userAmount) {
                return "not enough money";
            }
            Profile.getOnlineUser().setMoney(userAmount - cardAmount);
            Profile.getOnlineUser().addCardToAllUselessCards(cardName);
            //what should it return?
            return "successful buy";
        }

        else if (ShopPatterns.isItShowAllPattern(command)) {
            return showAllCards();
        }
        return null;
    }

    private String showAllCards() {
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        ArrayList<String> allCards = new ArrayList<>();
        for (Map.Entry string : allMonsterCards.entrySet()) allCards.add((String) string.getKey());
        for (Map.Entry string : allSpellAndTrapCards.entrySet()) allCards.add((String) string.getKey());
        Collections.sort(allCards);
        String returnString = "";
        for (int i = 0; i < allCards.size(); i++) {
            returnString += allCards.get(i);
            returnString += ":";
            returnString += getCardWithName(allCards.get(i)).getCardPrice();
            returnString += "\n";
        }
        return returnString;
    }

    private Card getCardWithName(String cardName) {
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allSpellAndTrapCards.get(cardName) != null) return allSpellAndTrapCards.remove(cardName);
        if (allMonsterCards.get(cardName) != null) return allMonsterCards.get(cardName);
        return null;
    }

    private boolean isItInvalidCardName(String cardName) {
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allSpellAndTrapCards.get(cardName) != null) return true;
        if (allMonsterCards.get(cardName) != null) return true;
        return false;
    }
}
