package controller.duel.gamepackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.duel.GamePackage.DuelBoard;
import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;
import model.cardData.General.CardLocation;
import model.cardData.General.RowOfCardLocation;

class DuelBoardTest {

    static GameManager gameManager;
    static Storage storage;

    @BeforeAll
    static void startTest() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user1 = new User("1", "1", "1");
        User user2 = new User("2", "2", "2");
        Storage.addUserToAllUsers(user1);
        Storage.addUserToAllUsers(user2);

        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        gameManager = new GameManager();
        gameManager.addANewGame(cards, cards, cards, cards, "1", "2", 1);
    }

    @Test
    void testWholeClass() {

        CardLocation cardLocation = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, 1);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }

        duelBoard.shuffleMainDecks();
        assertEquals(duelBoard.getAllyMonsterCards().get(0), duelBoard.getCardByCardLocation(cardLocation));
        cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, 1);
        assertEquals(duelBoard.getAllySpellCards().get(0), duelBoard.getCardByCardLocation(cardLocation));
        // turn == 1
        GameManager.getDuelControllerByIndex(0).setTurn(1);
        duelBoard.addCardToHand(cards.get(5), 1);
        duelBoard.addCardToHand(cards.get(5), 2);
        duelBoard.addCardToGraveyard(cards.get(2), 1);
        duelBoard.addCardToGraveyard(cards.get(2), 1);
        duelBoard.addCardToSpellZone(cards.get(5), 1);

        duelBoard.addCardToSpellZone(cards.get(5), 1);
        duelBoard.addCardToSpellZone(cards.get(5), 2);
        duelBoard.addCardToSpellZone(cards.get(5), 2);
        duelBoard.addCardToMonsterZone(cards.get(1), 2);
        duelBoard.addCardToMonsterZone(cards.get(1), 1);
        // String x = "\tE\tE\tfgk\tfgk\tE\t";
        // System.out.println(x);
        // System.out.println(duelBoard.reverseWordsWhenTurnIs2(x));
        System.out.println(duelBoard.showMainDuelBoard(0));
        // turn == 2
        GameManager.getDuelControllerByIndex(0).setTurn(2);

        System.out.println("\n\n" + duelBoard.showMainDuelBoard(0));
    }
}
