package view;

import controller.duel.PreliminaryPackage.GameManager;
import model.cardData.General.Card;
import model.cardData.MonsterCardData.*;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.*;
import controller.duel.GamePackage.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FakeMain {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        ArrayList<Card> firstPlayerDeck = new ArrayList<>();
        ArrayList<Card> secondPlayerDeck = new ArrayList<>();
        addCardsToFirstPlayer(firstPlayerDeck);
        addCardsToSecondPlayer(secondPlayerDeck);
        gameManager.addANewGame(firstPlayerDeck, secondPlayerDeck, "zenos", "exdeath");
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        System.out.println(duelController.startDuel(0));
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        System.out.println(duelBoard.showDuelBoard(0));
        boolean continueInput = true;
        String input;
        while (continueInput) {
            input = scanner.nextLine();
            if (input.equals("end")) {
                continueInput = false;
            } else {
                System.out.println(duelController.getInput(input));
            }
        }
    }

    public static void addCardsToFirstPlayer(ArrayList<Card> firstPlayerDeck) {
        firstPlayerDeck.add(new TrapCard("torrential tribute", "a",  TrapCardValue.NORMAL,null, 3, 60,0, null));
        firstPlayerDeck.add(new SpellCard("black pendant", "e", SpellCardValue.EQUIP, null,3, 60,0, null));
        //firstPlayerDeck.add(new TrapCard("trap hole", "e", null, 3, 60));
        //firstPlayerDeck.add(new MonsterCard(2700, 1500, 7, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosone", "", null, 3));
        firstPlayerDeck.add(new MonsterCard(10, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenostwo", "", null, 3,0, null));
        firstPlayerDeck.add(new MonsterCard(1000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosthree", "", null, 3,0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosfour", "", null, 3,0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosfive", "", null, 3,0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenossix", "", null, 3,0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosseven", "", null, 3,0, null));

    }

    public static void addCardsToSecondPlayer(ArrayList<Card> secondPlayerDeck) {
        //secondPlayerDeck.add(new TrapCard("torrential tribute", "a", null, 3, 60));
        secondPlayerDeck.add(new SpellCard("united we stand", "f", SpellCardValue.EQUIP, null,3, 60, 0, null));
        secondPlayerDeck.add(new MonsterCard(100, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathone", "", null, 3,0,  null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathtwo", "", null, 3,0,  null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeaththree", "", null, 3,0,  null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfour", "", null, 3,0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfive", "", null, 3,0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathsix", "", null, 3,0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathseven", "", null, 3,0, null));

    }
}
