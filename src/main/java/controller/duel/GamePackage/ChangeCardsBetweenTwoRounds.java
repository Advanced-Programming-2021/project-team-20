package controller.duel.GamePackage;

import java.util.ArrayList;
import java.util.regex.Matcher;

import controller.duel.PreliminaryPackage.GameManager;
import controller.duel.Utility.Utility;
import model.cardData.General.Card;
import model.cardData.General.CardType;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.TrapCardData.TrapCard;

public class ChangeCardsBetweenTwoRounds {

    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> sideDeckCards;
    private boolean isFirstPlayerChangedHisDeck = true;

    public String changeCardsBetweenTwoRounds(int turn, String input, int index) {

        if (turn == 1) {
            mainDeckCards = GameManager.getDuelBoardByIndex(index).getAllyCardsInDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(index).getAllySideDeckCards();
        } else if (turn == 2) {
            mainDeckCards = GameManager.getDuelBoardByIndex(index).getOpponentCardsInDeck();
            sideDeckCards = GameManager.getDuelControllerByIndex(index).getOpponentSideDeckCards();
        }

        if (isFirstPlayerChangedHisDeck) {
            isFirstPlayerChangedHisDeck = false;
            return "now player 1 can change his main deck";
        }

        if (input.equals("end")) {
            if (mainDeckCards.size() < 40) {
                return "cards in main deck should be at least 40";
            }
            if (mainDeckCards.size() > 60) {
                return "cards in main deck should be at most 60";
            }
            if (turn == 1) {
                GameManager.getDuelControllerByIndex(index).setTurn(2);
                return "now player 2 can change his main deck";
            }
            if (turn == 2) {
                GameManager.getDuelControllerByIndex(index).startDuel(index);
                return "next round of duel started";
            }

        }

        Matcher matcher;
        matcher = Utility.getCommandMatcher(input,
                "transfer card to (-sd|--side deck) from (-md|--main deck) (?<cardname>\\S+)");
        if (matcher.find()) {
            String cardname = matcher.group("cardname");
            if (addCardToSideDeck(cardname)) {
                transferCardsBetweenSideAndMainDeck(turn, index);
                return cardname + " added to side deck successfully!";
            }
            return cardname + " does not exist in main deck";
        }

        matcher = Utility.getCommandMatcher(input,
                "transfer card to (-md|--main deck) from (-sd|--side deck) (?<cardname>\\S+)");
        if (matcher.find()) {
            String cardname = matcher.group("cardname");
            if (addCardToMainDeck(cardname)) {
                transferCardsBetweenSideAndMainDeck(turn, index);
                return cardname + " added to main deck successfully!";
            }
            return cardname + " does not exist in side deck";
        }

        return "invalid command!";
    }

    private boolean addCardToMainDeck(String cardname) {

        for (int i = 0; i < sideDeckCards.size(); i++) {
            if (sideDeckCards.get(i).getCardName().equals(cardname)) {
                if (sideDeckCards.get(i).getCardType().equals(CardType.MONSTER)) {
                    MonsterCard monsterCard = (MonsterCard) sideDeckCards.get(i);
                    mainDeckCards.add(monsterCard);
                } else if (sideDeckCards.get(i).getCardType().equals(CardType.SPELL)) {
                    SpellCard spellCard = (SpellCard) sideDeckCards.get(i);
                    mainDeckCards.add(spellCard);
                } else if (sideDeckCards.get(i).getCardType().equals(CardType.TRAP)) {
                    TrapCard trapCard = (TrapCard) sideDeckCards.get(i);
                    mainDeckCards.add(trapCard);
                }
                return true;
            }
        }
        return false;
    }

    private boolean addCardToSideDeck(String cardname) {

        for (int i = 0; i < mainDeckCards.size(); i++) {
            if (mainDeckCards.get(i).getCardName().equals(cardname)) {
                if (mainDeckCards.get(i).getCardType().equals(CardType.MONSTER)) {
                    MonsterCard monsterCard = (MonsterCard) mainDeckCards.get(i);
                    sideDeckCards.add(monsterCard);
                } else if (mainDeckCards.get(i).getCardType().equals(CardType.SPELL)) {
                    SpellCard spellCard = (SpellCard) mainDeckCards.get(i);
                    sideDeckCards.add(spellCard);
                } else if (mainDeckCards.get(i).getCardType().equals(CardType.TRAP)) {
                    TrapCard trapCard = (TrapCard) mainDeckCards.get(i);
                    sideDeckCards.add(trapCard);
                }
                return true;
            }
        }
        return false;
    }

    private void transferCardsBetweenSideAndMainDeck(int turn, int index) {
        if (turn == 1) {
            GameManager.getDuelBoardByIndex(index).setAllyCardsInDeck(mainDeckCards);
            GameManager.getDuelControllerByIndex(index).setAllySideDeckCards(sideDeckCards);
        } else if (turn == 2) {
            GameManager.getDuelBoardByIndex(index).setOpponentCardsInDeck(mainDeckCards);
            GameManager.getDuelControllerByIndex(index).setOpponentSideDeckCards(sideDeckCards);
        }
    }
}
