package project.controller.duel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.cheat.Cheat;
import project.controller.non_duel.storage.Storage;
import project.model.User;
import project.model.cardData.General.Card;
import project.model.cardData.MonsterCardData.MonsterCard;

class CheatTest {

    static GameManager gameManager;
    static Storage storage;
    static Cheat cheat;

    @BeforeAll
    static void startTest() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user1 = new User("1", "1", "1");
        Storage.addUserToAllUsers(user1);
        cheat = new Cheat();

        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
        gameManager = new GameManager();
       // gameManager.addANewGame(cards, cards, cards, cards, "1", "AI", 1);

    }

    @Test
    void testWholeClass() {

        GameManager.getDuelControllerByIndex(0).setTurn(1);
        User user = Storage.getUserByName("1");
        String actual = cheat.findCheatCommand("132123", 0);
        assertEquals("invalid command!", actual);
        // increase life point
        actual = cheat.findCheatCommand("cheat increase -L 1000", 0);
        assertEquals("lifePoint increased 1000 successfully!", actual);
        assertEquals(9000, GameManager.getDuelControllerByIndex(0).getLifePoints().get(0));
        assertEquals(8000, GameManager.getDuelControllerByIndex(0).getLifePoints().get(1));
        // increase money
        actual = cheat.findCheatCommand("cheat increase --money 50000", 0);
        assertEquals("money increased 50000 successfully!", actual);
        assertEquals(150000, user.getMoney());
        // add addition card to hand
        actual = cheat.findCheatCommand("cheat select -h suijin -f", 0);
        assertEquals("suijin does not found!", actual);
        actual = cheat.findCheatCommand("cheat select -f -h Suijin", 0);
        assertEquals("Suijin added to hand successfully!", actual);
        assertEquals(6, GameManager.getDuelBoardByIndex(0).getAllyCardsInHand().size());
        actual = cheat.findCheatCommand("cheat select -f -h Dark Hole", 0);
        assertEquals("Dark Hole added to hand successfully!", actual);
        assertEquals(7, GameManager.getDuelBoardByIndex(0).getAllyCardsInHand().size());
        actual = cheat.findCheatCommand("cheat select -h Mystical space typhoon -f", 0);
        assertEquals("Mystical space typhoon added to hand successfully!", actual);
        assertEquals(8, GameManager.getDuelBoardByIndex(0).getAllyCardsInHand().size());
        // increase attack power
        actual = cheat.findCheatCommand("cheat increase attack power 1000", 0);
        assertEquals("attackPower of Monsters increased 1000 successfully!", actual);
        MonsterCard monsterCard = (MonsterCard) GameManager.getDuelBoardByIndex(0).getAllyCardsInHand().get(5);
        assertEquals(3500, monsterCard.getAttackPower());
        // increase defense power
        actual = cheat.findCheatCommand("cheat increase defense power 1000", 0);
        assertEquals("defensePower of Monsters increased 1000 successfully!", actual);
        assertEquals(3400, monsterCard.getDefensePower());
        // get card from graveyard
        GameManager.getDuelBoardByIndex(0).addCardToGraveyard(monsterCard, 1);
        actual = cheat.findCheatCommand("cheat get card from graveyard suijin", 0);
        assertEquals("suijin does not found!", actual);
        actual = cheat.findCheatCommand("cheat get card from graveyard Suijin", 0);
        assertEquals("Suijin added to hand successfully!", actual);
        assertEquals(0, GameManager.getDuelBoardByIndex(0).getAllyCardsInGraveyard().size());

        // if turn == 2
        // increase attack power
        GameManager.getDuelControllerByIndex(0).setTurn(2);
        actual = cheat.findCheatCommand("cheat increase attack power 1000", 0);
        assertEquals("attackPower of Monsters increased 1000 successfully!", actual);
        // increase defense power
        actual = cheat.findCheatCommand("cheat increase defense power 1000", 0);
        assertEquals("defensePower of Monsters increased 1000 successfully!", actual);
        assertEquals(3400, monsterCard.getDefensePower());
        // get card from graveyard
        actual = cheat.findCheatCommand("cheat get card from graveyard suijin", 0);
        assertEquals("suijin does not found!", actual);
        // set winner
        actual = cheat.findCheatCommand("cheat duel set-winner 5", 0);
        assertEquals("player with this nickname is not playing!", actual);
        actual = cheat.findCheatCommand("cheat duel set-winner 1", 0);
        assertEquals("1 won the whole match with score: 1000 - 0", actual);
       // another winner
        ArrayList<Card> cards = new ArrayList<>();
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            cards.add(entry.getValue());
        }
      //  gameManager.addANewGame(cards, cards, cards, cards, "1", "AI", 1);
        actual = cheat.findCheatCommand("cheat duel set-winner AI", 0);
        assertEquals("AI won the whole match with score: 0 - 1000", actual);

    }
}
