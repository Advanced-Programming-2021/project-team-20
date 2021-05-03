package test.maven.model.card.cardData.General;

import test.maven.model.card.cardData.MonsterCardData.MonsterCard;
import test.maven.model.card.cardData.SpellCardData.SpellCard;
import test.maven.model.card.cardData.TrapCardData.TrapCard;

public class Card {
    protected String cardName;
    protected CardType cardType;
    protected String cardDescription;
    protected CardPosition cardPosition;
    protected int numberOfAllowedUsages;
    protected int cardPrice;

    public Card(String cardName, CardType cardType, String cardDescription, CardPosition cardPosition,
            int numberOfAllowedUsages, int cardPrice) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardDescription = cardDescription;
        this.cardPosition = cardPosition;
        this.numberOfAllowedUsages = numberOfAllowedUsages;
        this.cardPrice = cardPrice;
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    // public CardLocation getCardLocation() {
    // return cardLocation;
    // }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public int getNumberOfAllowedUsages() {
        return numberOfAllowedUsages;
    }

    public void setCardPosition(CardPosition cardPosition) {
        this.cardPosition = cardPosition;
    }

    public static boolean isCardAMonster(Card card) {
        return card instanceof MonsterCard;
    }

    public static boolean isCardASpell(Card card) {
        return card instanceof SpellCard;
    }

    public static boolean isCardATrap(Card card) {
        return card instanceof TrapCard;
    }
}
