package CardData.General;

import CardData.General.CardLocation;
import CardData.General.CardPosition;
import CardData.General.CardType;
import CardData.MonsterCardData.MonsterCard;
import CardData.SpellCardData.SpellCard;
import CardData.TrapCardData.TrapCard;
import CardEffects.EffectImplementations.Effect;

import java.util.ArrayList;

public class Card {
    protected String cardName;
    protected CardType cardType;
    protected String cardDescription;
    protected CardPosition cardPosition;
    protected int numberOfAllowedUsages;

    public Card(String cardName, CardType cardType, String cardDescription, CardPosition cardPosition, int numberOfAllowedUsages) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.cardDescription = cardDescription;
        this.cardPosition = cardPosition;
        this.numberOfAllowedUsages = numberOfAllowedUsages;
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

    //public CardLocation getCardLocation() {
    //    return cardLocation;
    //}

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
