package controller.duel.cheat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import controller.non_duel.storage.Storage;
import model.cardData.General.Card;
import model.cardData.MonsterCardData.MonsterCard;

public class Cheat {

    public String findCheatCommand(String input, int index) {

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input, "cheat increase (-L|--LP) (?<amount>\\d+)");
        if (matcher.find()) {
            return increaseLifePoints(Integer.parseInt(matcher.group("amount")), 0);

        }

        matcher = Utility.getCommandMatcher(input, "cheat duel set-winner (\\S+)");
        if (matcher.find()) {
            return setWinner(matcher.group(1));

        }

        matcher = Utility.getCommandMatcher(input, "cheat increase (-m|--money) (?<amount>\\d+)");
        if (matcher.find()) {
            return increaseMoney(Integer.parseInt(matcher.group("amount")), index);

        }

        matcher = Utility.getCommandMatcher(input, "cheat select (-h|--hand) (?<cardname>\\S+) (-f|--force)");
        if (matcher.find()) {
            return addAdditionCardToHand(matcher.group("cardname"), index);

        }

        matcher = Utility.getCommandMatcher(input, "cheat select (-f|--force) (-h|--hand) (?<cardname>\\S+)");
        if (matcher.find()) {
            return addAdditionCardToHand(matcher.group("cardname"), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat increase attack power (\\d+)");
        if (matcher.find()) {
            return increaseAttackPower(Integer.parseInt(matcher.group(1)), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat increase defense power (\\d+)");
        if (matcher.find()) {
            return increaseDefensePower(Integer.parseInt(matcher.group(1)), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat get card from graveyard (\\S+)");
        if (matcher.find()) {
            return getCardFromGraveyard(matcher.group(1), index);
        }

        return "invalid command!";
    }

    private String getCardFromGraveyard(String cardname, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        ArrayList<Card> cardsInGraveYard;
        if (turn == 1) {
            cardsInGraveYard = GameManager.getDuelBoardByIndex(index).getAllyCardsInGraveyard();
        } else {
            cardsInGraveYard = GameManager.getDuelBoardByIndex(index).getOpponentCardsInGraveyard();
        }

        for (int i = 0; i < cardsInGraveYard.size(); i++) {
            if (cardsInGraveYard.get(i).getCardName().equals(cardname)) {
                GameManager.getDuelBoardByIndex(index).addCardToHand(cardsInGraveYard.get(i), turn);
                return cardname + " added to hand successfully!";
            }
        }
        return cardname + " does not found!";
    }

    private String increaseDefensePower(int amount, int index) {

        ArrayList<Card> monsterCards;
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();

        if (turn == 1) {
            monsterCards = GameManager.getDuelBoardByIndex(index).getAllyMonsterCards();
        } else {
            monsterCards = GameManager.getDuelBoardByIndex(index).getOpponentMonsterCards();
        }

        for (int i = 0; i < monsterCards.size(); i++) {
            MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
            monsterCard.setDefensePower(amount + monsterCard.getDefensePower());
        }
        return "defensePower of Monsters increased " + amount + " successfully!";
    }

    private String increaseAttackPower(int amount, int index) {
        ArrayList<Card> monsterCards;
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();

        if (turn == 1) {
            monsterCards = GameManager.getDuelBoardByIndex(index).getAllyMonsterCards();
        } else {
            monsterCards = GameManager.getDuelBoardByIndex(index).getOpponentMonsterCards();
        }

        for (int i = 0; i < monsterCards.size(); i++) {
            MonsterCard monsterCard = (MonsterCard) monsterCards.get(i);
            monsterCard.setAttackPower(amount + monsterCard.getAttackPower());
        }
        return "attackPower of Monsters increased " + amount + " successfully!";
    }

    private String addAdditionCardToHand(String cardname, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardname)) {
            GameManager.getDuelBoardByIndex(index).addCardToHand(allMonsterCards.get(cardname), turn);
            return cardname + " added to hand successfully!";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            GameManager.getDuelBoardByIndex(index).addCardToHand(allSpellAndTrapCards.get(cardname), turn);
            return cardname + " added to hand successfully!";
        }
        return cardname + " does not found!";
    }

    private String increaseMoney(int amount, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        Storage.getUserByName(GameManager.getDuelControllerByIndex(0).getPlayingUsernameByTurn(turn)).setMoney(amount);
        return "money increased " + amount + " successfully!";
    }

    private String setWinner(String nickname) {
        return "null";
        // do
    }

    private String increaseLifePoints(int amount, int index) {
        GameManager.getDuelControllerByIndex(index).increaseLifePoints(amount,
                GameManager.getDuelControllerByIndex(index).getTurn());
        return "lifePoint increased " + amount + " successfully!";
    }
}
