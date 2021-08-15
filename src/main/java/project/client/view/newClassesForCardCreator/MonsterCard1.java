package project.client.view.newClassesForCardCreator;

import javafx.scene.image.Image;
import project.server.controller.non_duel.storage.Storage;
import project.model.cardData.General.CardPosition;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;

import java.util.HashMap;
import java.util.List;

public class MonsterCard1 {
    public MonsterCard1(int attackPowerMonsterCard, int defencePowerMonsterCard, int levelOfMonsterCard, MonsterCardAttribute attributeMonster
        , MonsterCardFamily familyMonster, MonsterCardValue valueMonster, String cardName, String cardDescription
        , CardPosition notApplicable, int numberOfAllowedUsages, int i, HashMap<String, List<String>> hashMapEffects
        , Image cardImage, HashMap<String, Integer> numbersOfEffectsToSend, String imagePath) {
        MonsterCard monsterCard = new MonsterCard(attackPowerMonsterCard, defencePowerMonsterCard, levelOfMonsterCard, attributeMonster,
            familyMonster,valueMonster, cardName, cardDescription, notApplicable, numberOfAllowedUsages, i, hashMapEffects, cardImage);
        Storage.addCardToNewCardsCrated(monsterCard);
        Storage.saveNewImagesOfCardsInFile(monsterCard, imagePath);
    }
}
