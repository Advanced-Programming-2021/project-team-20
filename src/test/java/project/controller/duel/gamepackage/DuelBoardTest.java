package project.controller.duel.gamepackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.RowOfCardLocation;

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
       // gameManager.addANewGame(cards, cards, cards, cards, "1", "2", 1);
    }

    @Test
    void testWholeClass() {
        CardLocation cardLocation = new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, 1);
        DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
        duelBoard.shuffleMainDecks();
        assertEquals(duelBoard.getAllyMonsterCards().get(0), duelBoard.getCardByCardLocation(cardLocation));
        cardLocation = new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, 1);
        assertEquals(duelBoard.getAllySpellCards().get(0), duelBoard.getCardByCardLocation(cardLocation));
      //  cardLocation = new CardLocation(rowOfCardLocations, index);

    }
}
