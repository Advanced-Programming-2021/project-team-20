package project.server.controller.duel.cheat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;

import project.server.controller.duel.GamePackage.DuelController;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.duel.Utility.Utility;
import project.server.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;
import project.client.view.pooyaviewpackage.AdvancedCardMovingController;

public class Cheat {

    public String findCheatCommand(String input, int index) {

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input, "cheat increase (-l|--lp) (?<amount>\\d+)");
        if (matcher.find()) {
            return increaseLifePoints(Integer.parseInt(matcher.group("amount")), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat duel set-winner (.+)");
        if (matcher.find()) {
            return setWinner(matcher.group(1), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat increase (-m|--money) (?<amount>\\d+)");
        if (matcher.find()) {
            return increaseMoney(Integer.parseInt(matcher.group("amount")), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat select (-h|--hand) (?<cardname>.+) (-f|--force)");
        if (matcher.find()) {
            return addAdditionCardToHand(matcher.group("cardname"), index);
        }

        matcher = Utility.getCommandMatcher(input, "cheat select (-f|--force) (-h|--hand) (?<cardname>.+)");
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

        matcher = Utility.getCommandMatcher(input, "cheat get card from graveyard (.+)");
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
                GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " +
                    (turn == 1 ? "ALLY_GRAVEYARD_ZONE" : "OPPONENT_GRAVEYARD_ZONE")
                    + " " + (i + 1) + " is being added to hand zone " + turn + " and should finally be NO_CHANGE");

                cardsInGraveYard.remove(i);
                return cardname + " added to hand successfully!";
            }
        }
        return cardname + " does not found!";
    }

    private String increaseDefensePower(int amount, int index) {

        ArrayList<Card> cardsInHand;
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();

        if (turn == 1) {
            cardsInHand = GameManager.getDuelBoardByIndex(index).getAllyCardsInHand();
        } else {
            cardsInHand = GameManager.getDuelBoardByIndex(index).getOpponentCardsInHand();
        }

        for (int i = 0; i < cardsInHand.size(); i++) {
            if (cardsInHand.get(i) != null && cardsInHand.get(i).getCardType().equals(CardType.MONSTER)) {
                MonsterCard monsterCard = (MonsterCard) cardsInHand.get(i);
                monsterCard.setDefensePower(amount + monsterCard.getDefensePower());
            }
        }
        return "defensePower of Monsters increased " + amount + " successfully!";
    }

    private String increaseAttackPower(int amount, int index) {
        ArrayList<Card> cardsInHand;
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();

        if (turn == 1) {
            cardsInHand = GameManager.getDuelBoardByIndex(index).getAllyCardsInHand();
        } else {
            cardsInHand = GameManager.getDuelBoardByIndex(index).getOpponentCardsInHand();
        }

        for (int i = 0; i < cardsInHand.size(); i++) {
            if (cardsInHand.get(i) != null && cardsInHand.get(i).getCardType().equals(CardType.MONSTER)) {
                MonsterCard monsterCard = (MonsterCard) cardsInHand.get(i);
                monsterCard.setAttackPower(amount + monsterCard.getAttackPower());
            }
        }
        return "attackPower of Monsters increased " + amount + " successfully!";
    }

    private String addAdditionCardToHand(String cardname, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        String cardName = Utility.giveCardNameRemovingRedundancy(cardname);
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardName)) {
            MonsterCard monster = (MonsterCard) allMonsterCards.get(cardName);
            GameManager.getDuelBoardByIndex(index).addCardToHand((MonsterCard) monster.clone(), turn);
            AdvancedCardMovingController.setNewlyAddedCard((MonsterCard) monster.clone());
            GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + "UNKNOWN"
                + " " + "UNKNOWN" + " is being added to hand zone " + turn + " and should finally be NO_CHANGE " + "cardName is " + cardname);

            return cardname + " added to hand successfully!";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardName)) {
            if (allSpellAndTrapCards.get(cardName).getCardType().equals(CardType.SPELL)) {
                SpellCard spellCard = (SpellCard) allSpellAndTrapCards.get(cardName);
                GameManager.getDuelBoardByIndex(index).addCardToHand((SpellCard) spellCard.clone(), turn);
                AdvancedCardMovingController.setNewlyAddedCard((SpellCard) spellCard.clone());
                GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + "UNKNOWN"
                    + " " + "UNKNOWN" + " is being added to hand zone " + turn + " and should finally be NO_CHANGE " + "cardName is " + cardname);
            } else {
                TrapCard trapCard = (TrapCard) allSpellAndTrapCards.get(cardName);
                GameManager.getDuelBoardByIndex(index).addCardToHand((TrapCard) trapCard.clone(), turn);
                AdvancedCardMovingController.setNewlyAddedCard((TrapCard) trapCard.clone());
                GameManager.getDuelControllerByIndex(0).addStringToSuperAlmightyString("mainCardLocation " + "UNKNOWN"
                    + " " + "UNKNOWN" + " is being added to hand zone " + turn + " and should finally be NO_CHANGE " + "cardName is " + cardname);
            }
            return cardname + " added to hand successfully!";
        }
        return cardname + " does not found!";
    }

    private String increaseMoney(int amount, int index) {
        int turn = GameManager.getDuelControllerByIndex(index).getTurn();
        User user = Storage.getUserByName(GameManager.getDuelControllerByIndex(0).getPlayingUsernameByTurn(turn));
        user.setMoney(user.getMoney() + amount);
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
