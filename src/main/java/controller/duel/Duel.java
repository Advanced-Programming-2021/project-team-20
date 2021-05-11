package controller.duel;

import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.Deck;
import model.User;
import model.cardData.General.Card;

import java.util.HashMap;

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
            //GameManager gameManager;
        }
        return "invalid commend!";
    }

    private boolean isThisDeckValid(User user) {
        //needs completion
        HashMap<String, Deck> decks = user.getDecks();
        Deck deck;
        //deck.
    }

    private boolean doesItHaveAnyActiveDeck(User firstUser) {
        //doroste?
        if (firstUser.getDecks() == null) return false;
        return true;
    }

    private boolean doesThisUserNameExist(String secondPlayer) {
        if (Storage.getUserByName(secondPlayer) == null) return false;
        return true;
    }

    private boolean isItsRoundNumberCorrect(String command) {
        int number = DuelPatterns.getRoundsNumber(command);
        if (number == 1 || number == 3) return true;
        return false;
    }
}
