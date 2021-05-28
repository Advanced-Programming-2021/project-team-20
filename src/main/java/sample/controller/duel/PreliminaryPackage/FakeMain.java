package sample.controller.duel.PreliminaryPackage;

import sample.controller.duel.GamePackage.DuelBoard;
import sample.controller.duel.GamePackage.DuelController;
import sample.model.cardData.General.Card;
import sample.model.cardData.MonsterCardData.MonsterCard;
import sample.model.cardData.MonsterCardData.MonsterCardAttribute;
import sample.model.cardData.MonsterCardData.MonsterCardFamily;
import sample.model.cardData.MonsterCardData.MonsterCardValue;
import sample.model.cardData.SpellCardData.SpellCard;
import sample.model.cardData.SpellCardData.SpellCardValue;
import sample.model.cardData.TrapCardData.TrapCard;
import sample.model.cardData.TrapCardData.TrapCardValue;

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
        gameManager.addANewGame(firstPlayerDeck, null,secondPlayerDeck,null, "zenos", "aii",1);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        duelController.startDuel(0);
        //System.out.println(duelController.startDuel(0));
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        System.out.println(duelBoard.showDuelBoard(0));
        boolean continueInput = true;
        String input;
        while (continueInput) {
            input = scanner.nextLine();
            if (input.equals("end")) {
                continueInput = false;
            } else {
                System.out.print(duelController.getInput(input, true));
            }
        }
    }
    public static void addCardsToFirstPlayer(ArrayList<Card> firstPlayerDeck) {
        //firstPlayerDeck.add(new MonsterCard(1000, 1000, 4, MonsterCardAttribute.DARK, MonsterCardFamily.AQUA, MonsterCardValue.NORMAL, "normalAQUA", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "leotron", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(100, 100, 1, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.EFFECT, "texchanger", "", null, 3, 0, null));

        //firstPlayerDeck.add(new SpellCard("dark hole", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new TrapCard("call of the hunted", "", TrapCardValue.NORMAL, null, 3, 60, 0, null));
        //firstPlayerDeck.add(new SpellCard("terraforming", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new TrapCard("torrential tribute", "", null, null, 3, 60, 0, null));
        //firstPlayerDeck.add(new SpellCard("messenger of peace", "", SpellCardValue.CONTINUOUS, null, 3, 6, 0, null));

        firstPlayerDeck.add(new TrapCard("mirror force", "", TrapCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "alexandrite dragon", "", null, 3, 0, null));

        firstPlayerDeck.add(new MonsterCard(2500, 2400, 7, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "suijin", "", null, 3, 0, null));
        firstPlayerDeck.add(new SpellCard("umiruka", "", SpellCardValue.FIELD, null, 3, 60, 0, null));

        firstPlayerDeck.add(new MonsterCard(100, 0, 3, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "exploder dragon", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(1300, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosfive", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenossix", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(10, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosseven", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 4, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenoseight", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 3, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosnine", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosten", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 1, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenoseleven", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenostwelve", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenosthirteen", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos14", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 1, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos15", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "zenos16", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 1500, 2, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "zenos17", "", null, 3, 0, null));

    }

    public static void addCardsToSecondPlayer(ArrayList<Card> secondPlayerDeck) {
        //secondPlayerDeck.add(new TrapCard("torrential tribute", "", null, null, 3, 60, 0, null));
        secondPlayerDeck.add(new SpellCard("twin twisters", "", SpellCardValue.QUICK_PLAY, null, 3, 6, 0, null));

        secondPlayerDeck.add(new MonsterCard(1800, 1200, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.WARRIOR, MonsterCardValue.EFFECT, "terratiger, the empowered warrior", "", null, 3, 0, null));

        secondPlayerDeck.add(new TrapCard("mirror force", "", null, null, 3, 60, 0, null));
        secondPlayerDeck.add(new MonsterCard(200, 2000, 2, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "bitron", "", null, 3, 0, null));

        secondPlayerDeck.add(new MonsterCard(300, 500, 3, MonsterCardAttribute.LIGHT, MonsterCardFamily.FAIRY, MonsterCardValue.EFFECT, "marshmallon", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "alexandrite dragon", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(300, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfour", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(600, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathfive", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(2050, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathsix", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathseven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheight", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathnine", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathten", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheleven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathtwelve", "", null, 3, 0, null));

    }

}
