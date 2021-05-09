package controller.duel.cheat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import controller.duel.GamePackage.DuelController;
import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;
import model.cardData.General.CardType;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

public class Cheat {

    public String findCheatCommand(String input, int index) {

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input, "cheat increase (-L|--LP) (?<amount>\\d+)");
        if (matcher.find()) {
            return increaseLifePoints(Integer.parseInt(matcher.group("amount")), index);

        }

        matcher = Utility.getCommandMatcher(input, "cheat duel set-winner (\\S+)");
        if (matcher.find()) {
            return setWinner(matcher.group(1), index);

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
            MonsterCard monster = (MonsterCard) allMonsterCards.get(cardname);
            GameManager.getDuelBoardByIndex(index).addCardToHand((MonsterCard) monster.clone(), turn);
            return cardname + " added to hand successfully!";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            if (allSpellAndTrapCards.get(cardname).getCardType().equals(CardType.SPELL)) {
                SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardname);
                GameManager.getDuelBoardByIndex(index).addCardToHand((SpellCard) spellCard.clone(), turn);
            } else {
                TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardname);
                GameManager.getDuelBoardByIndex(index).addCardToHand((TrapCard) trapCard.clone(), turn);
            }
            return cardname + " added to hand successfully!";
        }
        return cardname + " does not found!";
    }

    private String increaseMoney(int amount, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        Storage.getUserByName(GameManager.getDuelControllerByIndex(0).getPlayingUsernameByTurn(turn)).setMoney(amount);
        return "money increased " + amount + " successfully!";
    }

    private String setWinner(String nickname, int index) {

        DuelController duelController = GameManager.getDuelControllerByIndex(index);
        User allyUser = Storage.getUserByName(duelController.getPlayingUsers().get(0));
        User opponentUser = Storage.getUserByName(duelController.getPlayingUsers().get(1));
        if (allyUser.getNickname().equals(nickname)) {
            return duelController.endGame(1, index);
        }
        if (opponentUser.getNickname().equals(nickname)) {
            return duelController.endGame(2, index);
        }

        return "player with this nickname is not playing!";

    }

    private String increaseLifePoints(int amount, int index) {
        GameManager.getDuelControllerByIndex(index).increaseLifePoints(amount,
                GameManager.getDuelControllerByIndex(index).getTurn());
        return "lifePoint increased " + amount + " successfully!";
    }
}
