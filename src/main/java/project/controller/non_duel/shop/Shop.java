package project.controller.non_duel.shop;

import java.util.*;

import project.View.LoginController;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;

public class Shop {

    public String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getCardName(command);
            int userAmount = LoginController.getOnlineUser().getMoney();
            if (!isItInvalidCardName(cardName)) {
                return "there is no card with this name";
            }
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
            if (cardAmount > userAmount) {
                return "not enough money";
            }
            LoginController.getOnlineUser().setMoney(userAmount - cardAmount);
            LoginController.getOnlineUser().addCardToAllUselessCards(cardName);
            return "successful buy";
        }

        else if (ShopPatterns.isItShowAllPattern(command)) {
            return showAllCards();
        }
        return "invalid command!";
    }

    private String showAllCards() {
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
            returnString += "\n";
        }
        return returnString;
    }

    private Card getCardWithName(String cardName) {
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allSpellAndTrapCards.get(cardName) != null)
            return allSpellAndTrapCards.get(cardName);
        if (allMonsterCards.get(cardName) != null)
            return allMonsterCards.get(cardName);
        return null;
    }

    private boolean isItInvalidCardName(String cardName) {
       return(Storage.doesCardExist(cardName));
    }
}
