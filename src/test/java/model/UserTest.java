package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userTest() {
        User user = new User("1", "1", "1");
        user.setMoney(1000);
        user.setNickname("nickname");
        user.setPassword("password");
        user.setScore(1000);
        assertEquals("1", user.getName());
        assertEquals(1000, user.getMoney());
        assertEquals(1000, user.getScore());
        assertEquals("nickname", user.getNickname());
        assertEquals("password", user.getPassword());
        Deck deck = new Deck("null");
        user.addDeckToAllDecks("null", deck);
        user.addCardToAllUselessCards("null");
        assertEquals(1, user.getAllUselessCards().size());
        assertEquals(1, user.getDecks().size());
        assertTrue(user.doesCardExistsInUselessCards("null"));
        user.deleteCardFromAllUselessCards("null");
        assertFalse(user.doesCardExistsInUselessCards("null"));
        user.deleteDeck("null");
        assertEquals(0, user.getDecks().size());
        User user2 = new User();
        assertEquals(0, user2.getDecks().size());
    }
}
