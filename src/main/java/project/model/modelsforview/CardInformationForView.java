package project.model.modelsforview;

import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;

public class CardInformationForView {
    private String cardName;
    private CardPosition cardPosition;
    private CardType cardType;
    private String cardDescription;

    public CardInformationForView(String cardName, CardPosition cardPosition, CardType cardType, String cardDescription) {
        this.cardName = cardName;
        this.cardPosition = cardPosition;
        this.cardType = cardType;
        this.cardDescription = cardDescription;
    }

    public String getCardName() {
        return cardName;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardPosition(CardPosition cardPosition) {
        this.cardPosition = cardPosition;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }
}
