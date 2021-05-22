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
    // null deck of user must be check in show all decks
    // show all cards must be check

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

        deckCommands.findCommands("deck add-card -c Suijin -d 121");
        deckCommands.findCommands("deck add-card -d 121 -c Suijin");
        deckCommands.findCommands("deck add-card -c Yami -s -d 121");
        deckCommands.findCommands("deck add-card -d 121 -c Yami");
        deckCommands.findCommands("deck add-card -s -d 121 -c Bitron");
        deckCommands.findCommands("deck add-card -s -c Bitron -d 121");
        deckCommands.findCommands("deck add-card -c Suijin -d 121");

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 122 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allSpellAndTrapCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 122 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -s -d 122 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 123 -c " + entry.getKey());
        }

        for (Map.Entry<String, Card> entry : allMonsterCards.entrySet()) {
            deckCommands.findCommands("deck add-card -d 123 -s -c " + entry.getKey());
        }

    }

    @Test
    void createDeckTest() {
        // create deck
        String output = deckCommands.findCommands("command");
        assertEquals("invalid command!", output);
        output = deckCommands.findCommands("deck create abc");
        assertEquals("deck created successfully!", output);
        output = deckCommands.findCommands("deck create abc");
        assertEquals("deck with name abc already exists", output);
        // delete deck
        output = deckCommands.findCommands("deck delete 1");
        assertEquals("deck with name 1 does not exist", output);
        output = deckCommands.findCommands("deck delete abc");
        assertEquals("deck deleted successfully!", output);
        output = deckCommands.findCommands("deck delete abc");
        assertEquals("deck with name abc does not exist", output);
        // activate deck
        output = deckCommands.findCommands("deck set-activate abc");
        assertEquals("deck with name abc does not exist", output);
        output = deckCommands.findCommands("deck set-activate 122");
        assertEquals("deck activated successfully!", output);
        assertTrue(Profile.getOnlineUser().getDecks().get("122").getIsDeckActive());
        // add card to deck
        output = deckCommands.findCommands("deck add-card -c Suijiin -d 123");
        assertEquals("card with name Suijiin does not exist", output);
        output = deckCommands.findCommands("deck add-card -c Suijin -d 1");
        assertEquals("deck with name 1 does not exist", output);
        output = deckCommands.findCommands("deck add-card -c Raigeki -d 122");
        assertEquals("main deck is full", output);
        output = deckCommands.findCommands("deck add-card -s -c Raigeki -d 123");
        assertEquals("side deck is full", output);
        output = deckCommands.findCommands("deck add-card -c Suijin -d 121");
        assertEquals("there are already 3 cards with name Suijin in deck 121", output);
        // dalete card from deck
        output = deckCommands.findCommands("deck rm-card -c Suijin -d 1");
        assertEquals("deck with name 1 does not exist", output);
        output = deckCommands.findCommands("deck rm-card -d 122 -c 123");
        assertEquals("card with name 123 does not exist in main deck", output);
        output = deckCommands.findCommands("deck rm-card -d 122 -s -c 123");
        assertEquals("card with name 123 does not exist in side deck", output);
        output = deckCommands.findCommands("deck rm-card -c Battle warrior -d 122");
        assertEquals("card removed from deck successfully", output);
        output = deckCommands.findCommands("deck rm-card -s -c Wattkid -d 122");
        assertEquals("card removed from deck successfully", output);
        output = deckCommands.findCommands("deck rm-card --side --deck 122 --card Suijin");
        assertEquals("card removed from deck successfully", output);
        // show all decks
        output = deckCommands.findCommands("deck show --all");
        assertEquals(
                "Decks:\nActive deck:\n122: main deck 59, side deck 13, valid\nOther Decks:\n121: main deck 4, side deck 2, invalid\n123: main deck 41, side deck 15, valid\n124: main deck 0, side deck 0, invalid",
                output);
        // null deck of user must be check in show all decks
        // show one deck
        output = deckCommands.findCommands("deck show -d-n 956");
        assertEquals("deck with name 956 does not exist", output);
        output = deckCommands.findCommands("deck show -d-n 121");
        assertEquals(
                "Deck: 121\nMain Deck:\nMonsters:\nSuijin: During damage calculation in your opponent's turn, if this card is being attacked: You can target the attacking monster; make that target's ATK 0 during damage calculation only (this is a Quick Effect). This effect can only be used once while this card is face-up on the field.\nSuijin: During damage calculation in your opponent's turn, if this card is being attacked: You can target the attacking monster; make that target's ATK 0 during damage calculation only (this is a Quick Effect). This effect can only be used once while this card is face-up on the field.\nSuijin: During damage calculation in your opponent's turn, if this card is being attacked: You can target the attacking monster; make that target's ATK 0 during damage calculation only (this is a Quick Effect). This effect can only be used once while this card is face-up on the field.\nSpell and Traps:\nYami: All Fiend and Spellcaster monsters on the field gain 200 ATK/DEF, also all Fairy monsters on the field lose 200 ATK/DEF.",
                output);
        output = deckCommands.findCommands("deck show -s --deck-name 121");
        assertEquals(
                "Deck: 121\nSide Deck:\nMonsters:\nBitron: A new species found in electronic space. There's not much information on it.\nBitron: A new species found in electronic space. There's not much information on it.\nSpell and Traps:",
                output);
        // show all cards must be check
    }
}