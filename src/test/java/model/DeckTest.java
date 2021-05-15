package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    void deckTest() {
        Deck deck = new Deck("1");
        deck.setDeckActive(true);
        deck.addCardToMainDeck("cardname1");
        deck.addCardToSideDeck("cardname1");
        deck.addCardToMainDeck("cardname2");
        deck.addCardToSideDeck("cardname2");
        assertEquals(2, deck.getSizeOfMainDeck());
        assertEquals(2, deck.getSizeOfSideDeck());
        assertEquals("1", deck.getDeckname());
        assertNotNull(deck.getMainDeck());
        assertNotNull(deck.getSideDeck());
        assertTrue(deck.getIsDeckActive());
        assertEquals(2, deck.numberOfCardsInDeck("cardname1"));
        deck.deleteCardFromMainDeck("cardname2");
        deck.deleteCardFromSideDeck("cardname2");

    }
}
