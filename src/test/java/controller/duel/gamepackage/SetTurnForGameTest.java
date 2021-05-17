package controller.duel.gamepackage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.duel.GamePackage.SetTurnForGame;
import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.storage.Storage;
import model.cardData.General.Card;

class SetTurnForGameTest {

    static GameManager gameManager;
    static SetTurnForGame setTurnForGame;
    static Storage storage;

    @BeforeAll
    static void startTest() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void AITest() {

        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "firstPlayer", "AI", 1);
        setTurnForGame = new SetTurnForGame();
        String output = setTurnForGame.setTurnBetweenTwoPlayer("1", 0);
        assertNotNull(output);
        System.out.println(output);
    }

    @Test
    void normalTest1() {
        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "firstPlayer", "secondPlayer", 1);
        setTurnForGame = new SetTurnForGame();

        String output = setTurnForGame.setTurnBetweenTwoPlayer("dsada", 0);
        assertEquals("please choose number between 1 to 3\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("10", 0);
        assertEquals("please choose number between 1 to 3\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("1", 0);
        assertEquals("secondPlayer must choose\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("input", 0);
        assertEquals("please choose number between 1 to 3\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("1", 0);
        assertEquals("both player select similar choice\ndo this action again\n"
                + "firstPlayer must choose\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("2", 0);
        assertEquals("secondPlayer must choose\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("3", 0);
        assertEquals("secondPlayer must start game", output);
        setTurnForGame.setTurnBetweenTwoPlayer("input", 0);
    }

    @Test
    void normalTest2() {
        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "firstPlayer", "secondPlayer", 1);
        setTurnForGame = new SetTurnForGame();

        String output = setTurnForGame.setTurnBetweenTwoPlayer("1", 0);
        assertEquals("secondPlayer must choose\n1.stone\n2.hand\n3.snips", output);
        output = setTurnForGame.setTurnBetweenTwoPlayer("3", 0);
        assertEquals("firstPlayer must start game", output);
    }

}
