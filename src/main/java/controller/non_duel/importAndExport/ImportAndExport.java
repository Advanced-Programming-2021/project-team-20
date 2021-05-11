package controller.non_duel.importAndExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import controller.non_duel.storage.Storage;
import model.cardData.General.Card;
import model.cardData.General.CardType;
import model.cardData.MonsterCardData.MonsterCard;
import model.cardData.MonsterCardData.MonsterCardAttribute;
import model.cardData.MonsterCardData.MonsterCardFamily;
import model.cardData.MonsterCardData.MonsterCardValue;
import model.cardData.SpellCardData.SpellCard;
import model.cardData.SpellCardData.SpellCardValue;
import model.cardData.TrapCardData.TrapCard;
import model.cardData.TrapCardData.TrapCardValue;

public class ImportAndExport {

    public String findCommand(String command) {

        Pattern importPattern = Pattern.compile("import card (\\S+)");
        Pattern exportPattern = Pattern.compile("export card (\\S+)");
        Matcher matcher;
        matcher = importPattern.matcher(command);
        if (matcher.find()) {
            return importCard(matcher.group(1));
        }

        matcher = exportPattern.matcher(command);
        if (matcher.find()) {
           return exportCard(matcher.group(1));
        }

        return "invalid command!";
    }

    private String exportCard(String cardname) {

        File file = new File("Resourses\\ExportedCards\\" + cardname + ".json");
        
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader("Resourses\\ExportedCards\\" + cardname + ".json");
                Scanner myReader = new Scanner(fileReader);
                getValuesOfCardFromFile(myReader.nextLine());
                myReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "card exported successfully!";
        }

        return "card does not exist!";
    }

    private void getValuesOfCardFromFile(String cardInformation) {

        JsonParser parser = new JsonParser();
        JsonElement rootNode = parser.parse(cardInformation);
        if (rootNode.isJsonObject()) {
            JsonObject detailsOfCards = rootNode.getAsJsonObject();
            String cardName = detailsOfCards.get("Name").getAsString();
            String cardType = detailsOfCards.get("Card Type").getAsString();
            String cardDesciption = detailsOfCards.get("Description").getAsString();
            int price = detailsOfCards.get("Price").getAsInt();

            if (cardType.equals("Trap")) {
                addSpellAndTrapCardToAllCards(detailsOfCards, cardName, cardType, cardDesciption, price);
            } else if (cardType.equals("Spell")) {
                addSpellAndTrapCardToAllCards(detailsOfCards, cardName, cardType, cardDesciption, price);
            } else {
                addMonsterToAllCards(detailsOfCards, cardName, cardType, cardDesciption, price);
            }

            try {
                FileWriter fileWriter = new FileWriter("Resourses\\ExportedCards\\" + cardName+ ".json");
                fileWriter.write(cardDesciption);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMonsterToAllCards(JsonObject detailsOfCards, String cardName, String cardType,
            String cardDescription, int cardPrice) {

        int level = detailsOfCards.get("Level").getAsInt();
        String attribute = detailsOfCards.get("Attribute").getAsString();
        String monsterType = detailsOfCards.get("Monster Type").getAsString();
        int attackPower = detailsOfCards.get("Attack Power").getAsInt();
        int defensePower = detailsOfCards.get("Defence Power").getAsInt();
        new MonsterCard(attackPower, defensePower, level, MonsterCardAttribute.valueOf(attribute),
                MonsterCardFamily.valueOf(formatterStringToEnum(monsterType)),
                MonsterCardValue.valueOf(cardType.toUpperCase()), cardName, cardDescription, null, 3, cardPrice, null);
    }

    private String formatterStringToEnum(String string) {

        if (string.contains("-")) {
            string = string.replace('-', '_');
        }
        if (string.contains(" ")) {
            string = string.replace(' ', '_');
        }
        return string.toUpperCase();
    }

    private void addSpellAndTrapCardToAllCards(JsonObject detailsOfCards, String cardName, String cardType,
            String cardDescription, int cardPrice) {
        String cardProperty = detailsOfCards.get("Card Property").getAsString();
        String status = detailsOfCards.get("Status").getAsString();
        if (cardType.equals("Trap")) {
            new TrapCard(cardName, cardDescription, TrapCardValue.valueOf(cardProperty.toUpperCase()), null,
                    status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
        } else {
            new SpellCard(cardName, cardDescription, SpellCardValue.valueOf(cardProperty), null,
                    status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
        }
    }

    private String importCard(String cardname) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter("Resourses\\ImportedCards\\" + cardname + ".json");
                fileWriter.write(toJsonFormatMonsterCard(allMonsterCards.get(cardname)));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "card imported successfuly!";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter("Resourses\\ImportedCards\\" + cardname + ".json");
                fileWriter.write(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(cardname)));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "card imported successfuly!";
        }
        return "card does not exist!";
    }

    private String toJsonFormatMonsterCard(Card card) {

        MonsterCard monsterCard = (MonsterCard) card;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", monsterCard.getCardName());
        jsonObject.addProperty("Level", monsterCard.getLevel());
        jsonObject.addProperty("Attribute", monsterCard.getMonsterCardAttribute().toString());
        jsonObject.addProperty("Monster Type", monsterCard.getMonsterCardFamily().toString());
        jsonObject.addProperty("Card Type", monsterCard.getCardType().toString());
        jsonObject.addProperty("Attack Power", monsterCard.getAttackPower());
        jsonObject.addProperty("Defence Power", monsterCard.getDefensePower());
        jsonObject.addProperty("Description", monsterCard.getCardDescription());
        jsonObject.addProperty("Price", monsterCard.getCardPrice());
        return jsonObject.toString();

    }

    private String toJsonFormatSpellAndTrapCard(Card card) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", card.getCardName());
        jsonObject.addProperty("Card Type", card.getCardType().toString());
        if (card.getCardType().equals(CardType.TRAP)) {
            TrapCard trapCard = (TrapCard) card;
            jsonObject.addProperty("Card Property", trapCard.getTrapCardValue().toString());
        } else {
            SpellCard spellCard = (SpellCard) card;
            jsonObject.addProperty("Card Property", spellCard.getSpellCardValue().toString());
        }
        jsonObject.addProperty("Description", card.getCardDescription());
        jsonObject.addProperty("Status", card.getNumberOfAllowedUsages() == 3 ? "Unlimited" : "Limited");
        jsonObject.addProperty("Price", card.getCardPrice());
        return jsonObject.toString();
    }
}
