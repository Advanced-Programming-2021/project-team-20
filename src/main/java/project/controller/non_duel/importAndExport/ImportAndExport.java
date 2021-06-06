package project.controller.non_duel.importAndExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;
import com.opencsv.CSVReader;

import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.MonsterCardData.MonsterCardAttribute;
import project.model.cardData.MonsterCardData.MonsterCardFamily;
import project.model.cardData.MonsterCardData.MonsterCardValue;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.cardData.TrapCardData.TrapCard;
import project.model.cardData.TrapCardData.TrapCardValue;

public class ImportAndExport {

    public String importCard(File chosenFile) {

        if (!Storage.doesCardExist(chosenFile.getName().substring(0, chosenFile.getName().lastIndexOf(".")))) {
            return "this card does not exist";
        }
        String fileType = chosenFile.getName().substring(chosenFile.getName().lastIndexOf("."),
                chosenFile.getName().length());

        if (fileType.equals(".json")) {
            if (checkValidJsonCard(chosenFile)) {
                return "card exists";
            } else {
                System.out.println("in json");
                return "this card does not exist";
            }
        } else if (fileType.equals(".csv")) {
            if (checkValidCSVFormat(chosenFile)) {
                return "card exists";
            } else {
                return "this card does not exist";
            }
        }
        return "this card does not exist";
    }

    private boolean checkValidCSVFormat(File file) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();

        List<List<String>> validInfornationOfCard = new ArrayList<>();
        if (allMonsterCards.containsKey(file.getName())) {
            validInfornationOfCard = toCSVFormatMonsterCard(allMonsterCards.get(file.getName()));
        } else {
            validInfornationOfCard = toCSVFormatSpellTrapCard(allSpellAndTrapCards.get(file.getName()));
        }

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> csvFileInformation = csvReader.readAll();
            csvReader.close();

            for (int i = 0; i < csvFileInformation.size(); i++) {
                for (int j = 0; j < csvFileInformation.get(i).length; j++) {
                    if (!csvFileInformation.get(i)[j].equals(validInfornationOfCard.get(i).get(j))) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean checkValidJsonCard(File file) {
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        StringBuilder fileInputs = new StringBuilder();

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                fileInputs.append(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fileInputs.toString().equals(toJsonFormatMonsterCard(allMonsterCards.get(file.getName())))) {
            return true;
        }

        if (fileInputs.toString().equals(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(file.getName())))) {
            return true;
        }

        return false;
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

            if (Storage.getAllMonsterCards().containsKey(cardName)) {
                addMonsterToAllCards(detailsOfCards, cardName, cardType, cardDesciption, price);
            } else if (Storage.getAllSpellAndTrapCards().containsKey(cardName)) {
                addSpellAndTrapCardToAllCards(detailsOfCards, cardName, cardType, cardDesciption, price);
            }

            try {
                FileWriter fileWriter = new FileWriter("Resourses\\ExportedCards\\" + cardName + ".json");
                fileWriter.write(cardDesciption);
                fileWriter.close();
            } catch (Exception e) {
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
        // new MonsterCard(attackPower, defensePower, level,
        // MonsterCardAttribute.valueOf(attribute),
        // MonsterCardFamily.valueOf(formatterStringToEnum(monsterType)),
        // MonsterCardValue.valueOf(cardType), cardName, cardDescription, null, 3,
        // cardPrice, null, );
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
        // if (cardType.equals("Trap")) {
        // new TrapCard(cardName, cardDescription,
        // TrapCardValue.valueOf(cardProperty.toUpperCase()), null,
        // status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
        // } else {
        // new SpellCard(cardName, cardDescription,
        // SpellCardValue.valueOf(cardProperty), null,
        // status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
        // }
    }

    public String exportCard(String cardname) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        if (allMonsterCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter("Resourses\\ImportedCards\\" + cardname + ".json");
                fileWriter.write(toJsonFormatMonsterCard(allMonsterCards.get(cardname)));
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "card imported successfully!";
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardname)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter("Resourses\\ImportedCards\\" + cardname + ".json");
                fileWriter.write(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(cardname)));
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "card imported successfuly!";
        }
        return "card does not exist!";
    }

    private List<List<String>> toCSVFormatMonsterCard(Card card) {

        List<String> firstRowVariables = new ArrayList<>();
        firstRowVariables.add("Name");
        firstRowVariables.add("Type");
        firstRowVariables.add("Icon (Property)");
        firstRowVariables.add("Description");
        firstRowVariables.add("Status");
        firstRowVariables.add("Price");

        List<String> secondRowVariables = new ArrayList<>();
        secondRowVariables.add(card.getCardName());
        secondRowVariables.add(card.getCardType() + "");
        if (card.getCardType().equals(CardType.TRAP)) {
            TrapCard trapCard = (TrapCard) card;
            secondRowVariables.add(trapCard.getTrapCardValue() + "");
        } else {
            SpellCard spellCard = (SpellCard) card;
            secondRowVariables.add(spellCard.getSpellCardValue() + "");
        }
        secondRowVariables.add(card.getCardDescription());
        secondRowVariables.add(card.getNumberOfAllowedUsages() == 3 ? "Unlimited" : "Limited");
        secondRowVariables.add(card.getCardPrice() + "");

        List<List<String>> wholeCSVFile = new ArrayList<>();
        wholeCSVFile.add(firstRowVariables);
        wholeCSVFile.add(secondRowVariables);

        return wholeCSVFile;
    }

    private List<List<String>> toCSVFormatSpellTrapCard(Card card) {

        MonsterCard monsterCard = (MonsterCard) card;

        List<String> firstRowVariables = new ArrayList<>();
        firstRowVariables.add("Name");
        firstRowVariables.add("Level");
        firstRowVariables.add("Attribute");
        firstRowVariables.add("Monster Type");
        firstRowVariables.add("Card Type");
        firstRowVariables.add("Atk");
        firstRowVariables.add("Def");
        firstRowVariables.add("Description");
        firstRowVariables.add("Price");

        List<String> secondRowVariables = new ArrayList<>();
        secondRowVariables.add(monsterCard.getCardName());
        secondRowVariables.add(monsterCard.getLevel() + "");
        secondRowVariables.add(monsterCard.getMonsterCardAttribute() + "");
        secondRowVariables.add(monsterCard.getMonsterCardFamily() + "");
        secondRowVariables.add(monsterCard.getMonsterCardValue() + "");
        secondRowVariables.add(monsterCard.getAttackPower() + "");
        secondRowVariables.add(monsterCard.getDefensePower() + "");
        secondRowVariables.add(monsterCard.getCardDescription());
        secondRowVariables.add(monsterCard.getCardPrice() + "");

        List<List<String>> wholeCSVFile = new ArrayList<>();
        wholeCSVFile.add(firstRowVariables);
        wholeCSVFile.add(secondRowVariables);

        return wholeCSVFile;
    }

    private String toJsonFormatMonsterCard(Card card) {

        MonsterCard monsterCard = (MonsterCard) card;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", monsterCard.getCardName());
        jsonObject.addProperty("Level", monsterCard.getLevel());
        jsonObject.addProperty("Attribute", monsterCard.getMonsterCardAttribute().toString());
        jsonObject.addProperty("Monster Type", monsterCard.getMonsterCardFamily().toString());
        jsonObject.addProperty("Card Type", monsterCard.getMonsterCardValue().toString());
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
