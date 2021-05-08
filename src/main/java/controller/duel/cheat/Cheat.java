package controller.duel.cheat;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import controller.duel.Utility.Utility;

public class Cheat {
    public void findCheatCommand(String input) {

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input, "cheap increase (-L|--LP) (?<amount>\\d+)");
        if (matcher.find()) {
            increaseLifePoints(Integer.parseInt(matcher.group("amount")));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap duel set-winner (\\S+)");
        if (matcher.find()) {
            setWinner(matcher.group(1));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap increase (-m|--money) (?<amount>\\d+)");
        if (matcher.find()) {
            increaseMoney(Integer.parseInt(matcher.group("amount")));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap select (-h|--hand) (?<cardname>\\S+) (-f|--force)");
        if (matcher.find()) {
            addAdditionCardToHand(matcher.group("cardname"));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap select (-f|--force) (-h|--hand) (?<cardname>\\S+)");
        if (matcher.find()) {
            addAdditionCardToHand(matcher.group("cardname"));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap increase attack power (\\d+)");
        if (matcher.find()) {
            increaseAttackPower(Integer.parseInt(matcher.group(1)));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap increase defense power (\\d+)");
        if (matcher.find()) {
            increaseDefensePower(Integer.parseInt(matcher.group(1)));
            return;
        }

        matcher = Utility.getCommandMatcher(input, "cheap get card from graveyard (\\S+)");
        if(matcher.find()){
            getCardFromGraveyard(matcher.group(1));
            return;
        }

    }

    

    private void getCardFromGraveyard(String cardname) {
        //
    }



    private void increaseDefensePower(int amount){
        //
    }

    private void increaseAttackPower(int amount) {
        //
    }

    private void addAdditionCardToHand(String cardName) {
        // do
    }

    private void increaseMoney(int amount) {

        // do
    }

    private void setWinner(String nickname) {

        // do
    }

    private void increaseLifePoints(int amount) {
       //
       
    }
}
