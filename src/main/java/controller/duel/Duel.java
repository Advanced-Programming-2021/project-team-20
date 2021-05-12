package controller.duel;

import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.Deck;
import model.User;
import model.cardData.General.Card;

import java.util.HashMap;
import java.util.Map;

public class Duel {
    public String findCommand(String command) {
    
        if(DuelPatterns.isItCorrectPattern(command)) {
            String secondPlayer = DuelPatterns.getSecondPlayer(command);
            if (!doesThisUserNameExist(secondPlayer)) {
                return "there is no player with this username";
            }
            User secondUser = Storage.getUserByName(secondPlayer);
            User firstUser = Profile.getOnlineUser();
            if (!doesItHaveAnyActiveDeck(firstUser)) {
                return firstUser.getName() + " has no active deck";
            }
            if (!doesItHaveAnyActiveDeck(secondUser)) {
                return secondUser.getName() + " has no active deck";
            }
            if (!isThisDeckValid(firstUser)) {
                return firstUser.getName() + "’s deck is invalid";
            }
            if (!isThisDeckValid(secondUser)) {
                return secondUser.getName() + "’s deck is invalid";
            }
            if (!isItsRoundNumberCorrect(command)) {
                return "number of rounds is not supported";
            }
        }
        return "invalid commend!";
    }

    private boolean isThisDeckValid(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        Deck deck = null;
        for (Map.Entry string : allDecks.entrySet()) {
            if (allDecks.get(string).getIsDeckActive()) deck = allDecks.get(string);
        }
        if (deck.getSizeOfMainDeck() >= 40) return true;
        return false;
    }

    private boolean doesItHaveAnyActiveDeck(User user) {
        HashMap<String, Deck> allDecks = user.getDecks();
        for (Map.Entry string : allDecks.entrySet()) {
            if (allDecks.get(string).getIsDeckActive()) return true;
        }
        return false;
    }

    private boolean doesThisUserNameExist(String player) {
        if (Storage.getUserByName(player) == null) return false;
        return true;
    }

    private boolean isItsRoundNumberCorrect(String command) {
        int number = DuelPatterns.getRoundsNumber(command);
        if (number == 1 || number == 3) return true;
        return false;
    }
}
