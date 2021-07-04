package project.controller.non_duel.shop;

import project.controller.non_duel.profile.Profile;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

import java.util.*;

public class Shop {

    public String findCommand(String command) {

        if (ShopPatterns.isItBuyPattern(command)) {
            String cardName = ShopPatterns.getBoughtCardName(command);
            int userAmount = Profile.getOnlineUser().getMoney();
            if (!isItInvalidCardName(cardName)) {
                return "there is no card with this name";
            }
            Card card = getCardWithName(cardName);
            int cardAmount = card.getCardPrice();
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

        if(command.matches("card show (.+)")){
            return showCard(ShopPatterns.getShownCardName(command));
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
            if (i <= allCards.size() - 2)
                returnString += "\n";
        }
        return returnString;
    }

    private String showCard(String cardName) {
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
        return (Storage.doesCardExist(cardName));
    }
}
