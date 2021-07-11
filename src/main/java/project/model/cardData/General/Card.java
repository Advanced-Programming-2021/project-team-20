package project.model.cardData.General;

import javafx.scene.image.Image;
import project.model.SpellEffectEnums.QuickSpellEffect;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;

import java.util.ArrayList;

public class Card {

    protected String cardName;
    protected String realName;
    protected CardType cardType;
    protected String cardDescription;
    protected CardPosition cardPosition;
    protected int numberOfAllowedUsages;
    protected int cardPrice;
    protected Image image;

    public Card(String cardName, CardType cardType, String cardDescription, CardPosition cardPosition,
                int numberOfAllowedUsages, int cardPrice, Image image) {
        this.cardName = cardName;
        this.realName = cardName;
        this.cardType = cardType;
        this.cardDescription = cardDescription;
        if (cardPosition == null) {
            this.cardPosition = CardPosition.NOT_APPLICABLE;
        } else {
            this.cardPosition = cardPosition;
        }
        this.numberOfAllowedUsages = numberOfAllowedUsages;
        this.cardPrice = cardPrice;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }


    public String getCardName() {
        return cardName;
    }

    public String getRealName() {
        return realName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public int getNumberOfAllowedUsages() {
        return numberOfAllowedUsages;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setNumberOfAllowedUsages(int numberOfAllowedUsages) {
        this.numberOfAllowedUsages = numberOfAllowedUsages;
    }

    public void setCardPrice(int cardPrice) {
        this.cardPrice = cardPrice;
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

    public int getSpeedOfCard() {
        if (Card.isCardAMonster(this)) {
            return 0;
        } else if (Card.isCardASpell(this)) {
            SpellCard spellCard = (SpellCard) this;
            ArrayList<QuickSpellEffect> quickSpellEffects = spellCard.getQuickSpellEffects();
            if (quickSpellEffects.size() >= 1) {
                return 2;
            }
            return 1;
        } else if (Card.isCardATrap(this)) {
            TrapCard trapCard = (TrapCard) this;
            if (trapCard.getTrapCardValue().equals(TrapCardValue.COUNTER)) {
                return 3;
            }
            return 2;
        }
        return 0;
    }
}
