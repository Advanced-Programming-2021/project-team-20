package project.controller.duel.PreliminaryPackage;

import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.GamePackage.DuelController;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;

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
       // gameManager.addANewGame(firstPlayerDeck, null, secondPlayerDeck, null, "zenos", "ai", 1);
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

    public static void call() {
        GameManager gameManager = new GameManager();
        Storage storage = (new Storage());
        try {
            storage.startProgram();
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<Card> firstPlayerDeck = new ArrayList<>();
        ArrayList<Card> secondPlayerDeck = new ArrayList<>();
        addCardsToFirstPlayer(firstPlayerDeck);
        addCardsToSecondPlayer(secondPlayerDeck);
        gameManager.addANewGame(null,firstPlayerDeck,  null,null,secondPlayerDeck, null, "zenos", "aii", 1);
        DuelController duelController = GameManager.getDuelControllerByIndex(0);
        duelController.startDuel(0);
        //System.out.println(duelController.startDuel(0));
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        System.out.println(duelBoard.showDuelBoard(0));
        boolean continueInput = true;
        String input;
    }

    public static void addCardsToFirstPlayer(ArrayList<Card> firstPlayerDeck) {
        //firstPlayerDeck.add(new MonsterCard(1000, 1000, 4, MonsterCardAttribute.DARK, MonsterCardFamily.AQUA, MonsterCardValue.NORMAL, "normalAQUA", "", null, 3, 0, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.CYBERSE, MonsterCardValue.NORMAL, "Leotron", "", null, 3, 0, null, null));
        firstPlayerDeck.add(new MonsterCard(100, 100, 1, MonsterCardAttribute.DARK, MonsterCardFamily.CYBERSE, MonsterCardValue.EFFECT, "Texchanger", "", null, 3, 0, null, null));
        firstPlayerDeck.add(new SpellCard("PotOfGreed", "", SpellCardValue.NORMAL, null, 3, 60, 0, null, null));
        firstPlayerDeck.add(new TrapCard("Call Of The Hunted", "", TrapCardValue.NORMAL, null, 3, 60, 0, null, null));


        //firstPlayerDeck.add(new SpellCard("dark hole", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        //firstPlayerDeck.add(new SpellCard("terraforming", "", SpellCardValue.NORMAL, null, 3, 60, 0, null));
        firstPlayerDeck.add(new TrapCard("TorrentialTribute", "", TrapCardValue.NORMAL, null, 3, 60, 0, null, null));
        //firstPlayerDeck.add(new SpellCard("messenger of peace", "", SpellCardValue.CONTINUOUS, null, 3, 6, 0, null));
        firstPlayerDeck.add(new SpellCard("Umiiruka", "", SpellCardValue.FIELD, null, 3, 60, 0, null, null));

        firstPlayerDeck.add(new TrapCard("MirrorForce", "", TrapCardValue.NORMAL, null, 3, 60, 0, null, null));
        firstPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "AlexandriteDragon", "", null, 3, 0, null, null));
        firstPlayerDeck.add(new MonsterCard(2500, 2400, 7, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "Suijin", "", null, 3, 0, null, null));


        firstPlayerDeck.add(new MonsterCard(1000, 0, 3, MonsterCardAttribute.DARK, MonsterCardFamily.FIEND, MonsterCardValue.NORMAL, "ExploderDragon", "", null, 3, 0, null, null));
        /*
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


         */
    }

    public static void addCardsToSecondPlayer(ArrayList<Card> secondPlayerDeck) {
        //secondPlayerDeck.add(new TrapCard("torrential tribute", "", null, null, 3, 60, 0, null));
        secondPlayerDeck.add(new SpellCard("TwinTwisters", "", SpellCardValue.QUICK_PLAY, null, 3, 6, 0, null, null));
        //secondPlayerDeck.add(new TrapCard("Call Of The Hunted", "", TrapCardValue.NORMAL, null, 3, 60, 0, null));
        secondPlayerDeck.add(new SpellCard("Umiiruka", "", SpellCardValue.FIELD, null, 3, 60, 0, null, null));
        secondPlayerDeck.add(new SpellCard("Raigeki", "", SpellCardValue.NORMAL, null, 3, 60, 0, null, null));


        secondPlayerDeck.add(new MonsterCard(1800, 1200, 4, MonsterCardAttribute.EARTH, MonsterCardFamily.WARRIOR, MonsterCardValue.EFFECT, "Terratiger", "", null, 3, 0, null, null));
        //terratiger effect won't work because of invalid string name in controller
        secondPlayerDeck.add(new TrapCard("MirrorForce", "", TrapCardValue.NORMAL, null, 3, 60, 0, null, null));
        secondPlayerDeck.add(new MonsterCard(200, 1800, 2, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "Bitron", "", null, 3, 0, null, null));

        secondPlayerDeck.add(new MonsterCard(300, 500, 3, MonsterCardAttribute.LIGHT, MonsterCardFamily.FAIRY, MonsterCardValue.EFFECT, "Marshmallon", "", null, 3, 0, null, null));
        secondPlayerDeck.add(new MonsterCard(2000, 0, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "AlexandriteDragon", "", null, 3, 0, null, null));
        secondPlayerDeck.add(new MonsterCard(300, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "ManEaterBug", "", null, 3, 0, null, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1200, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "HeraldOfCreation", "", null, 3, 0, null, null));
        /*
        secondPlayerDeck.add(new MonsterCard(2050, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathsix", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathseven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheight", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathnine", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathten", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeatheleven", "", null, 3, 0, null));
        secondPlayerDeck.add(new MonsterCard(1800, 1900, 4, MonsterCardAttribute.LIGHT, MonsterCardFamily.SPELLCASTER, MonsterCardValue.NORMAL, "exdeathtwelve", "", null, 3, 0, null));


         */
    }
}
