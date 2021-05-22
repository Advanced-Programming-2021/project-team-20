package controller.non_duel_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.non_duel.deckCommands.DeckCommands;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;
import model.cardData.General.Card;

class DeckCommandsTest {

    static Storage storage;
    static DeckCommands deckCommands;

    @BeforeAll
    static void startTest() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        User user = new User("1", "1", "1");
        Storage.addUserToAllUsers(user);
        Profile.setOnlineUser(user);
        deckCommands = new DeckCommands();
        deckCommands.findCommands("deck create 121");
        deckCommands.findCommands("deck create 122");
        deckCommands.findCommands("deck create 123");
        deckCommands.findCommands("deck create 124");
        deckCommands.findCommands("deck create 125");
        deckCommands.findCommands("deck create 126");
        deckCommands.findCommands("deck create 127");
        deckCommands.findCommands("deck create 128");
        deckCommands.findCommands("deck create 129");

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }
        
        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            user.addCardToAllUselessCards(entry.getKey());
        }

        deckCommands.findCommands("deck add-card -c Suijin -d 123");
        deckCommands.findCommands("deck add-card -d 123 -c Suijin");
        deckCommands.findCommands("deck add-card -c Yami -s -d 123");
        deckCommands.findCommands("deck add-card -d 123 -c Yami");
        deckCommands.findCommands("deck add-card -s -d 123 -c Bitron");
        deckCommands.findCommands("deck add-card -s -c Bitron -d 123");
        deckCommands.findCommands("deck add-card -c Suijin -d 123");

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 124 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 125 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 125 -s -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 124 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -s -d 124 -c " + entry.getKey());
        }
    }

    @Test
    void createDeckTest() {
        String output = deckCommands.findCommands("command");
        assertEquals("invalid command!", output);
        output = deckCommands.findCommands("deck create 130");
        assertEquals("deck created successfully!", output);
        output = deckCommands.findCommands("deck create 130");
        assertEquals("deck with name 130 already exists", output);
    }

    @Test
    void deleteDeckTest() {
        String output = deckCommands.findCommands("deck delete 1");
        assertEquals("deck with name 1 does not exist", output);
        output = deckCommands.findCommands("deck delete 129");
        assertEquals("deck deleted successfully!", output);
        output = deckCommands.findCommands("deck delete 129");
        assertEquals("deck with name 129 does not exist", output);
    }

    @Test
    void activeDeckTest() {
        String output = deckCommands.findCommands("deck set-activate 130");
        assertEquals("deck with name 130 does not exist", output);
        output = deckCommands.findCommands("deck set-activate 129");
        assertEquals("deck activated successfully!", output);
        assertTrue(Profile.getOnlineUser().getDecks().get("129").getIsDeckActive());
    }

   // @Test
    // void addCardToDeck() {
    //     String output = deckCommands.findCommands("deck add-card -c Suijiin -d 123");
    //     assertEquals("card with name Suijiin does not exist", output);
    //     output = deckCommands.findCommands("deck add-card -c Suijin -d 1");
    //     assertEquals("deck with name 1 does not exist", output);
    //     output = deckCommands.findCommands("deck add-card -c Raigeki -d 124");
    //     assertEquals("main deck is full", output);
    //     output = deckCommands.findCommands("deck add-card -s -c Raigeki -d 124");
    //     assertEquals("side deck is full", output);
    //     output = deckCommands.findCommands("deck add-card -c Suijin -d 123");
    //     assertEquals("there are already three cards with name Suijin in deck 123", output);
    // }

    // @Test
    // void deleteCardFromDeckTest() {
    //     String output = deckCommands.findCommands("deck rm-card -c Suijin -d 1");
    //     // assertEquals("deck with name 1 does not exist", output);
    //     // output = deckCommands.findCommands("deck rm-card -d 123 -c 123");
    //     // assertEquals("card with name 123 does not exist in main deck", output);
    //     // output = deckCommands.findCommands("deck rm-card -d 123 -s -c 123");
    //     // assertEquals("card with name 123 does not exist in side deck", output);
    //     // output = deckCommands.findCommands("deck rm-card -c Battle warrior -d 125");
    //     // assertEquals("card removed from deck successfully", output);
    //     // output = deckCommands.findCommands("deck rm-card -s -c wattkid -d 125");
    //     // assertEquals("card removed from deck successfully", output);
 
    //     // private Pattern deckDeleteCard5 = Pattern
    //     //         .compile("deck rm-card(?<side> -s| --side|) (-c|--card) (?<card>.+) (-d|--deck) (?<deck>.+)");
    //     // private Pattern deckDeleteCard6 = Pattern
    //     //         .compile("deck rm-card(?<side> -s| --side|) (-d|--deck) (?<deck>.+) (-c|--card) (?<card>.+)");

    // }

}