package sample.controller.non_duel.shop;

import sample.controller.non_duel.profile.Profile;
import sample.controller.non_duel.storage.Storage;
import sample.model.cardData.General.Card;

import java.util.*;

public class Shop {

    public String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getCardName(command);
            int userAmount = Profile.getOnlineUser().getMoney();
            if (!isItInvalidCardName(cardName)) {
                return "there is no card with this name";
            }
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
        //    System.out.println(userAmount + "  " + cardAmount);
            if (cardAmount > userAmount) {
                return "not enough money";
            }
            Profile.getOnlineUser().setMoney(userAmount - cardAmount);
            Profile.getOnlineUser().addCardToAllUselessCards(cardName);
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
     //   System.out.println(cardName);
       return(Storage.doesCardExist(cardName));
    }
}
