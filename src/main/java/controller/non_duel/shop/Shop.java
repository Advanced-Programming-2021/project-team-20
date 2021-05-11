package controller.non_duel.shop;

import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;

public class Shop {
    public String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getCardName(command);
            int userAmount = Profile.getOnlineUser().getMoney();
            if (isItInvalidCardName(cardName)) {
                return "there is no card with this name";
            }
            ///cardAmount?
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
            if (cardAmount > userAmount) {
                return "not enough money";
            }
            Profile.getOnlineUser().setMoney(userAmount - cardAmount);
            Profile.getOnlineUser().addCardToAllUselessCards(cardName);
            //what should it return?
        }

        else if (ShopPatterns.isItShowAllPattern(command)) {
            ///how to get all cards?
            ///how to get card by its name?
        }
        return null;
    }
}
