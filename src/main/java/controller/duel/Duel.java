package controller.duel;

import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;

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
            GameManager gameManager;
        }
        return "invalid commend!";
    }
}
