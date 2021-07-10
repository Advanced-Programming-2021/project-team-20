package project.controller.non_duel.importAndExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.google.gson.*;
import com.opencsv.CSVReader;

import project.controller.duel.Utility.Utility;
import project.controller.non_duel.storage.Storage;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardType;
import project.model.cardData.MonsterCardData.MonsterCard;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.TrapCardData.TrapCard;

public class ImportAndExport {

    public String importCard(File chosenFile) {

        String fileType = chosenFile.getName().substring(chosenFile.getName().lastIndexOf("."),
                chosenFile.getName().length());

        if (fileType.equals(".json")) {
            String result = checkValidJsonCard(chosenFile); 
            if (!result.equals("false")) {
                return result;
            } else {
                return "this card does not exist";
            }
        } else if (fileType.equals(".csv")) {
            String result = checkValidCSVFormat(chosenFile); 
            if (!result.equals("false")) {
                return result;
            } else {
                return "this card does not exist";
            }
        }
        return "this card does not exist";
    }

    private String checkValidCSVFormat(File file) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();

        List<String[]> csvFileInformation = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            CSVReader csvReader = new CSVReader(fileReader);
            csvFileInformation = csvReader.readAll();
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        if (csvFileInformation.size() < 2) {
            return "false";
        }
        String cardname = csvFileInformation.get(1)[0];
        List<List<String>> validInfornationOfCard = new ArrayList<>();
        if (allMonsterCards.containsKey(Utility.giveCardNameRemovingRedundancy(cardname))) {
            validInfornationOfCard = toCSVFormatMonsterCard(allMonsterCards.get(Utility.giveCardNameRemovingRedundancy(cardname)));
        } else if (allSpellAndTrapCards.containsKey(Utility.giveCardNameRemovingRedundancy(cardname))) {
            validInfornationOfCard = toCSVFormatSpellTrapCard(allSpellAndTrapCards.get(Utility.giveCardNameRemovingRedundancy(cardname)));
        } else {
            return "false";
        }

        for (int i = 0; i < csvFileInformation.size(); i++) {
            if (csvFileInformation.get(i).length - 1 > validInfornationOfCard.get(i).size()) {
                return "false";   
            }
            for (int j = 0; j < csvFileInformation.get(i).length - 1; j++) {
                if (!csvFileInformation.get(i)[j].equals(validInfornationOfCard.get(i).get(j))) {
                    return "false";
                }
            }
        }
        return cardname;
    }

    private String checkValidJsonCard(File file) {
        
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

        String nameOfCardInFile = new String();
        try {
            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(fileInputs.toString());
            System.out.println(rootNode.isJsonObject());
            if (rootNode.isJsonObject()) {
                JsonObject details = rootNode.getAsJsonObject();
                nameOfCardInFile = details.get("Name").getAsString();
            } else {
                return "false";
            }
        } catch (Exception e) {
            return "false";
        }

        if (fileInputs.toString().equals(toJsonFormatMonsterCard(allMonsterCards.get(Utility.giveCardNameRemovingRedundancy(nameOfCardInFile))))) {
            return nameOfCardInFile;
        }
        if (fileInputs.toString().equals(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get(Utility.giveCardNameRemovingRedundancy(nameOfCardInFile))))) {
            return nameOfCardInFile;
        }
        return "false";
    }

    // private void getValuesOfCardFromFile(String cardInformation) {

    // JsonParser parser = new JsonParser();
    // JsonElement rootNode = parser.parse(cardInformation);
    // if (rootNode.isJsonObject()) {
    // JsonObject detailsOfCards = rootNode.getAsJsonObject();
    // String cardName = detailsOfCards.get("Name").getAsString();
    // String cardType = detailsOfCards.get("Card Type").getAsString();
    // String cardDesciption = detailsOfCards.get("Description").getAsString();
    // int price = detailsOfCards.get("Price").getAsInt();

    // if (Storage.getAllMonsterCards().containsKey(cardName)) {
    // addMonsterToAllCards(detailsOfCards, cardName, cardType, cardDesciption,
    // price);
    // } else if (Storage.getAllSpellAndTrapCards().containsKey(cardName)) {
    // addSpellAndTrapCardToAllCards(detailsOfCards, cardName, cardType,
    // cardDesciption, price);
    // }

    // try {
    // FileWriter fileWriter = new FileWriter("Resourses\\ExportedCards\\" +
    // cardName + ".json");
    // fileWriter.write(cardDesciption);
    // fileWriter.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // private void addMonsterToAllCards(JsonObject detailsOfCards, String cardName,
    // String cardType,
    // String cardDescription, int cardPrice) {

    // int level = detailsOfCards.get("Level").getAsInt();
    // String attribute = detailsOfCards.get("Attribute").getAsString();
    // String monsterType = detailsOfCards.get("Monster Type").getAsString();
    // int attackPower = detailsOfCards.get("Attack Power").getAsInt();
    // int defensePower = detailsOfCards.get("Defence Power").getAsInt();
    // // new MonsterCard(attackPower, defensePower, level,
    // // MonsterCardAttribute.valueOf(attribute),
    // // MonsterCardFamily.valueOf(formatterStringToEnum(monsterType)),
    // // MonsterCardValue.valueOf(cardType), cardName, cardDescription, null, 3,
    // // cardPrice, null, );
    // }

    // private String formatterStringToEnum(String string) {

    // if (string.contains("-")) {
    // string = string.replace('-', '_');
    // }
    // if (string.contains(" ")) {
    // string = string.replace(' ', '_');
    // }
    // return string.toUpperCase();
    // }

    // private void addSpellAndTrapCardToAllCards(JsonObject detailsOfCards, String
    // cardName, String cardType,
    // String cardDescription, int cardPrice) {
    // String cardProperty = detailsOfCards.get("Card Property").getAsString();
    // String status = detailsOfCards.get("Status").getAsString();
    // // if (cardType.equals("Trap")) {
    // // new TrapCard(cardName, cardDescription,
    // // TrapCardValue.valueOf(cardProperty.toUpperCase()), null,
    // // status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
    // // } else {
    // // new SpellCard(cardName, cardDescription,
    // // SpellCardValue.valueOf(cardProperty), null,
    // // status.equals("Unlimited") ? 3 : 1, 0, cardPrice, null);
    // // }
    // }

    public String exportCard(String cardname, File file) {

        String fileType = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        if (fileType.equals(".json")) {
            if (writeFileInJsonFormat(cardname, file)) {
                return "card exported successfully";
            } else {
                return "ERROR";
            }
        } else if (fileType.equals(".csv")) {
            if (writeFileInCSVFormat(cardname, file)) {
                return "card exported successfully";
            } else {
                return "ERROR";
            }
        }
        return "ERROR";
    }

    private boolean writeFileInCSVFormat(String cardname, File file) {

        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        String cardName = Utility.giveCardNameRemovingRedundancy(cardname);
        if (allMonsterCards.containsKey(cardName)) {
            FileWriter csvWriter;
            try {
                csvWriter = new FileWriter(file.getAbsolutePath());
                List<List<String>> wholeCSVFile = toCSVFormatMonsterCard(allMonsterCards.get(cardName));
                for (List<String> rowData : wholeCSVFile) {
                    for (int i = 0; i < rowData.size(); i++) {
                        csvWriter.append("\"" + rowData.get(i) + "\"");
                        csvWriter.append(",");
                    }
                    csvWriter.append("\n");
                }
                csvWriter.flush();
                csvWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardName)) {
            FileWriter csvWriter;
            try {
                csvWriter = new FileWriter(file.getAbsolutePath());
                List<List<String>> wholeCSVFile = toCSVFormatSpellTrapCard(allSpellAndTrapCards.get(cardName));
                for (List<String> rowData : wholeCSVFile) {
                    for (int i = 0; i < rowData.size(); i++) {
                        csvWriter.append("\"" + rowData.get(i) + "\"");
                        csvWriter.append(",");
                    }
                    csvWriter.append("\n");
                }
                csvWriter.flush();
                csvWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private boolean writeFileInJsonFormat(String cardname, File file) {
        HashMap<String, Card> allMonsterCards = Storage.getAllMonsterCards();
        String cardName = Utility.giveCardNameRemovingRedundancy(cardname);
        if (allMonsterCards.containsKey(cardName)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file.getAbsolutePath());
                fileWriter.write(toJsonFormatMonsterCard(allMonsterCards.get(cardName)));
                fileWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        HashMap<String, Card> allSpellAndTrapCards = Storage.getAllSpellAndTrapCards();
        if (allSpellAndTrapCards.containsKey(cardName)) {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file.getAbsolutePath());
                fileWriter.write(toJsonFormatSpellAndTrapCard(allSpellAndTrapCards.get((cardName))));
                fileWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private List<List<String>> toCSVFormatMonsterCard(Card card) {

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

    private List<List<String>> toCSVFormatSpellTrapCard(Card card) {

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
