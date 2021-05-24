package controller.duel.gamepackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.duel.GamePackage.ChangeCardsBetweenTwoRounds;
import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.storage.Storage;
import model.cardData.General.Card;

class ChangeCardsBetweenTwoRoundsTest {
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
        GameManager gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "firstPlayer", "AI", 1);
        ChangeCardsBetweenTwoRounds changeCardsBetweenTwoRounds = new ChangeCardsBetweenTwoRounds();

        String output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1,
                "move from side deck Suijin to main deck Suijin", 0);
        assertEquals("cards moved successfully!", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1, "end", 0);
        assertEquals("next round of duel started\nfirstPlayer must choose\n1.stone\n2.hand\n3.snips", output);

    }

    @Test
    void normalTest() {

        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        GameManager gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "firstPlayer", "secondPlayer", 1);
        ChangeCardsBetweenTwoRounds changeCardsBetweenTwoRounds = new ChangeCardsBetweenTwoRounds();

        String output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1,
                "move from side deck Suijin to main deck Suijin", 0);
        assertEquals("cards moved successfully!", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1,
                "move from side deck Suijiin to main deck Suijin", 0);
        assertEquals("Suijiin does not exist in side deck", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1,
                "move from side deck Suijin to main deck Susijin", 0);
        assertEquals("Susijin does not exist in main deck", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1, "input", 0);
        assertEquals("invalid command!", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(1, "end", 0);
        assertEquals("now secondPlayer can change his deck", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(2,
                "move from side deck Suijin to main deck Suijin", 0);
        assertEquals("cards moved successfully!", output);
        output = changeCardsBetweenTwoRounds.changeCardsBetweenTwoRounds(2, "end", 0);
        assertEquals("next round of duel started\nfirstPlayer must choose\n1.stone\n2.hand\n3.snips", output);

    }

}
