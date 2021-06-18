package controller.non_duel_tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.non_duel.storage.Storage;
import model.Deck;
import model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

class StorageTest {
    static Storage storage;

    @BeforeAll
    static void runStorage() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = new User("ali", "ali", "ali");
        Storage.addUserToAllUsers(user);
        Deck deck = new Deck("ali");
        user.addDeckToAllDecks("ali", deck);

    }

    @Test
    void doesCardExistTest() {
        assertEquals(true, Storage.doesCardExist("Suijin"));
    }

    @Test
    void getAllSpellAndTrapCardSizeTest() {
        assertEquals(33, Storage.getAllSpellAndTrapCards().size());
    }

    @Test
    void getMonsterCardSizeTest() {
        assertEquals(41, Storage.getAllMonsterCards().size());
    }

    @Test
    void getUserByNameTest() {
        assertNull(Storage.getUserByName("123456789"));
        assertNotNull(Storage.getUserByName("ali"));
    }

    @Test
    void getAllUsersTest() {
        ArrayList<User> allUsers = Storage.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            assertNotNull(allUsers.get(i));
        }
    }

    @AfterAll
    static void endProgram() {
        storage.endProgram();
    }
}
