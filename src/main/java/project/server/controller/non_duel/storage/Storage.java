package project.server.controller.non_duel.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.*;
import com.opencsv.*;

import javax.imageio.*;

import java.awt.image.*;

import javafx.scene.image.Image;
import project.model.Utility.Utility;
import project.model.Deck;
import project.model.User;
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

public class Storage {

    private static ArrayList<User> allUsers = new ArrayList<>();
    private static HashMap<String, Card> allMonsterCards = new HashMap<>();
    private static HashMap<String, Card> allSpellAndTrapCards = new HashMap<>();
    private static Card unknownCard;
    private String addressOfStorage = "Resourses\\Server\\";
    private static HashMap<String, Card> newCardsCreated = new HashMap<>();
    private static HashMap<User, String> newImagesThatChanges = new HashMap<>();
    private static HashMap<String, Image> newImageOfNewCards = new HashMap<>();

    public void startProgram() throws Exception {

        addUsersToArrayList();
        addMonsterCards();
        addTrapCards();
        addSpellCards();

        unknownCard = new Card("cardName", null, "", null, 10, 125, null);
        unknownCard.setImage(createImageOfCards("Unknown"));

    }

    public void endProgram() throws IOException {
        saveNewCardsInFile();
        /// saveNewImageOfUsers();
        savaUsersInFile();
    }

    private void saveNewCardsInFile() throws IOException {
        CSVWriter csvWriter = null;
        for (Map.Entry<String, Card> entry : newCardsCreated.entrySet()) {
            if (entry.getValue().getCardType().equals(CardType.MONSTER)) {
                csvWriter = new CSVWriter(new FileWriter(addressOfStorage + "CSV\\Monster.csv", true));
                MonsterCard monsterCard = (MonsterCard) entry.getValue();
                // saveNewImagesOfCardsInFile(monsterCard);
                csvWriter.writeNext(monsterCard.toCSVFormatString());
                csvWriter.flush();
                csvWriter.close();
            } else if (entry.getValue().getCardType().equals(CardType.SPELL)) {
                csvWriter = new CSVWriter(new FileWriter(addressOfStorage + "CSV\\Spell.csv", true));
                SpellCard spellCard = (SpellCard) entry.getValue();
                csvWriter.writeNext(spellCard.toCSVFormatString());
                csvWriter.flush();
                csvWriter.close();
            } else {
                csvWriter = new CSVWriter(new FileWriter(addressOfStorage + "CSV\\Trap.csv", true));
                TrapCard trapCard = (TrapCard) entry.getValue();
                csvWriter.writeNext(trapCard.toCSVFormatString());
                csvWriter.flush();
                csvWriter.close();
            }
        }
    }

    public static void saveNewImageOfUsers(User user, String imagePath) {
        BufferedImage bImage = null;
        try {
            File initialImage = new File(imagePath);
            bImage = ImageIO.read(initialImage);
            ImageIO.write(bImage, "png",
                new File("src\\main\\resources\\project\\images\\Characters\\chosenCharacters\\image"
                    + user.getName() + ".png"));
            user.setImagePath("src\\main\\resources\\project\\images\\Characters\\chosenCharacters\\image"
                + user.getName() + ".png");
        } catch (Exception e) {
            System.out.println("Exception occured :" + e.getMessage());
        }
    }

    public static void saveNewImagesOfCardsInFile(Card card, String imagePath) {
        BufferedImage bImage = null;
        try {
            File file = new File(imagePath);
            bImage = ImageIO.read(file);
            if (card.getCardType().equals(CardType.MONSTER)) {
                ImageIO.write(bImage, "png",
                    new File("src\\main\\resources\\project\\cards\\monsters\\" + card.getCardName() + ".jpg"));
            } else {
                ImageIO.write(bImage, "png",
                    new File("src\\main\\resources\\project\\cards\\spelltraps\\" + card.getCardName() + ".jpg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savaUsersInFile() {
        File file;
        for (int i = 0; i < allUsers.size(); i++) {
            file = new File(addressOfStorage + "Users\\" + allUsers.get(i).getName());
            file.mkdir();
            try {
                FileWriter fileWriter = new FileWriter(
                    addressOfStorage + "Users\\" + allUsers.get(i).getName() + "\\User.json");
                fileWriter.write(toGsonFormat(allUsers.get(i)));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }

            HashMap<String, Deck> allDecks = allUsers.get(i).getDecks();
            JsonArray allDecksJson = new JsonArray();

            if (allDecks != null) {
                for (Map.Entry<String, Deck> entry : allDecks.entrySet()) {
                    JsonObject wholeDeckJson = new JsonObject();
                    wholeDeckJson.addProperty("deckname", entry.getValue().getDeckname());
                    wholeDeckJson.addProperty("isActivated", entry.getValue().getIsDeckActive());
                    wholeDeckJson.add("mainDeck",
                        new Gson().toJsonTree(entry.getValue().getMainDeck()).getAsJsonArray());
                    wholeDeckJson.add("sideDeck",
                        new Gson().toJsonTree(entry.getValue().getSideDeck()).getAsJsonArray());
                    allDecksJson.add(wholeDeckJson);
                }
            }
            JsonObject wholeDecksOfUser = new JsonObject();
            wholeDecksOfUser.add("decks", allDecksJson);
            wholeDecksOfUser.add("uselessCards",
                new Gson().toJsonTree(allUsers.get(i).getAllUselessCards()).getAsJsonArray());

            try {
                FileWriter fileWriter = new FileWriter(
                    addressOfStorage + "Users\\" + allUsers.get(i).getName() + "\\DeckAndCards.json");
                fileWriter.write(wholeDecksOfUser.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private String toGsonFormat(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("nickname", user.getNickname());
        jsonObject.addProperty("password", user.getPassword());
        jsonObject.addProperty("imagePath", user.getImagePath());
        jsonObject.addProperty("score", user.getScore());
        jsonObject.addProperty("money", user.getMoney());
        return jsonObject.toString();
    }

    private void addTrapCards() {

        try {

            FileReader filereader = new FileReader(addressOfStorage + "CSV\\Trap.csv");
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[1].equals("Trap")) {
                    String correctCardName = Utility.giveCardNameRemovingRedundancy(nextRecord[0]);
                    allSpellAndTrapCards.put(correctCardName,
                        new TrapCard(nextRecord[0], nextRecord[3],
                            TrapCardValue.valueOf(nextRecord[2].toUpperCase()), null,
                            nextRecord[4].equals("Unlimited") ? 3 : 1, 120, Integer.parseInt(nextRecord[5]),
                            addEffectsTrapCards(nextRecord), createImageOfCards(nextRecord[0])));
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void addSpellCards() {

        try {
            FileReader filereader = new FileReader(addressOfStorage + "CSV\\Spell.csv");
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            int numberOfTurnsOfActivation = 120;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[1].equals("Spell")) {
                    if (nextRecord[0].startsWith("Swords of R")) {
                        numberOfTurnsOfActivation = 6;
                    }
                    String correctCardName = Utility.giveCardNameRemovingRedundancy(nextRecord[0]);
                    allSpellAndTrapCards.put(correctCardName,
                        new SpellCard(nextRecord[0], nextRecord[3],
                            SpellCardValue.valueOf(formatterStringToEnum(nextRecord[2])), null,
                            nextRecord[4].equals("Unlimited") ? 3 : 1, numberOfTurnsOfActivation,
                            Integer.parseInt(nextRecord[5]), addEffectsSpellCards(nextRecord),
                            createImageOfCards(nextRecord[0]), addMonsterCardFamilyToSpellCards(nextRecord[16]),
                            addArrayListOfIntegerToSpellCard(nextRecord[15])));
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private ArrayList<MonsterCardFamily> addMonsterCardFamilyToSpellCards(String columnValues) {
        ArrayList<MonsterCardFamily> monsterCardFamilies = new ArrayList<>();
        String[] values = new String[columnValues.split("#").length];
        values = columnValues.split("#");
        for (int i = 1; i < values.length; i++) {
            monsterCardFamilies.add(MonsterCardFamily.valueOf(values[i]));
        }
        return monsterCardFamilies;
    }

    private ArrayList<Integer> addArrayListOfIntegerToSpellCard(String columnValues) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(Integer.parseInt(columnValues));
        return integers;
    }

    private HashMap<String, List<String>> addEffectsSpellCards(String[] cardValues) {
        HashMap<String, List<String>> enumValues = new HashMap<>();
        enumValues.put("ContinuousSpellCardEffect", Arrays.asList(cardValues[6].split("#")));
        enumValues.put("EquipSpellEffect", Arrays.asList(cardValues[7].split("#")));
        enumValues.put("FieldSpellEffect", Arrays.asList(cardValues[8].split("#")));
        enumValues.put("LogicalActivationRequirement", Arrays.asList(cardValues[9].split("#")));
        enumValues.put("NormalSpellCardEffect", Arrays.asList(cardValues[10].split("#")));
        enumValues.put("SentToGraveyardEffect", Arrays.asList(cardValues[11].split("#")));
        enumValues.put("QuickSpellEffect", Arrays.asList(cardValues[12].split("#")));
        enumValues.put("RitualSpellEffect", Arrays.asList(cardValues[13].split("#")));
        enumValues.put("UserReplyForActivation", Arrays.asList(cardValues[14].split("#")));
        return enumValues;
    }

    private HashMap<String, List<String>> addEffectsTrapCards(String[] cardValues) {
        HashMap<String, List<String>> enumValues = new HashMap<>();
        enumValues.put("ContinuousTrapCardEffect", Arrays.asList(cardValues[6].split("#")));
        enumValues.put("FlipSummonTrapCardEffect", Arrays.asList(cardValues[7].split("#")));
        enumValues.put("LogicalActivationRequirement", Arrays.asList(cardValues[8].split("#")));
        enumValues.put("MonsterAttackingTrapCardEffect", Arrays.asList(cardValues[9].split("#")));
        enumValues.put("MonsterEffectActivationTrapCardEffect", Arrays.asList(cardValues[10].split("#")));
        enumValues.put("NormalSummonTrapCardEffect", Arrays.asList(cardValues[11].split("#")));
        enumValues.put("NormalTrapCardEffect", Arrays.asList(cardValues[12].split("#")));
        enumValues.put("RitualSummonTrapCardEffect", Arrays.asList(cardValues[13].split("#")));
        enumValues.put("SpecialSummonTrapCardEffect", Arrays.asList(cardValues[14].split("#")));
        enumValues.put("SpellCardActivationTrapCardEffect", Arrays.asList(cardValues[15].split("#")));
        enumValues.put("TrapCardActivationTrapCardEffect", Arrays.asList(cardValues[16].split("#")));
        enumValues.put("UserReplyForActivation", Arrays.asList(cardValues[17].split("#")));
        return enumValues;
    }

    private void addMonsterCards() {
        try {
            FileReader filereader = new FileReader(addressOfStorage + "CSV\\Monster.csv");
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            int firstRow = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (firstRow > 0) {
                    String correctCardName = Utility.giveCardNameRemovingRedundancy(nextRecord[0]);
                    allMonsterCards.put(correctCardName,
                        new MonsterCard(Integer.parseInt(nextRecord[5]), Integer.parseInt(nextRecord[6]),
                            Integer.parseInt(nextRecord[1]), MonsterCardAttribute.valueOf(nextRecord[2]),
                            MonsterCardFamily.valueOf(formatterStringToEnum(nextRecord[3])),
                            MonsterCardValue.valueOf(nextRecord[4].toUpperCase()), nextRecord[0], nextRecord[7],
                            null, 3, Integer.parseInt(nextRecord[8]), addEffectsMonsterCards(nextRecord),
                            createImageOfCards(nextRecord[0])));
                }
                firstRow++;
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private Image createImageOfCards(String cardname) {
        InputStream stream = null;
        try {
            stream = new FileInputStream("src\\main\\resources\\project\\cards\\monsters\\" + cardname + ".jpg");
            return new Image(stream);
        } catch (Exception e) {
            try {
                stream = new FileInputStream("src\\main\\resources\\project\\cards\\spelltraps\\" + cardname + ".jpg");
                return new Image(stream);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return new Image(stream);
    }

    private HashMap<String, List<String>> addEffectsMonsterCards(String[] cardValues) {

        HashMap<String, List<String>> enumValues = new HashMap<>();
        enumValues.put("AttackerEffect", Arrays.asList(cardValues[9].split("#")));
        enumValues.put("BeingAttackedEffect", Arrays.asList(cardValues[10].split("#")));
        enumValues.put("ContinuousMonsterEffect", Arrays.asList(cardValues[11].split("#")));
        enumValues.put("FlipEffect", Arrays.asList(cardValues[12].split("#")));
        enumValues.put("OptionalMonsterEffect", Arrays.asList(cardValues[13].split("#")));
        enumValues.put("SentToGraveyardEffect", Arrays.asList(cardValues[14].split("#")));
        enumValues.put("SummoningRequirement", Arrays.asList(cardValues[15].split("#")));
        enumValues.put("UponSummoningEffect", Arrays.asList(cardValues[16].split("#")));
        return enumValues;
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

    private void addUsersToArrayList() throws Exception {

        ArrayList<String> filenames = new ArrayList<>();
        File directory = new File(addressOfStorage + "Users\\");
        File[] contents = directory.listFiles();
        for (File f : contents) {
            filenames.add(f.getName() + "\\");
        }

        for (int i = 0; i < filenames.size(); i++) {

            FileReader fileReader = new FileReader(addressOfStorage + "Users\\" + filenames.get(i) + "\\User.json");
            Scanner myReader = new Scanner(fileReader);

            String informationOfUser = myReader.nextLine();

            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(informationOfUser);
            myReader.close();
            fileReader.close();

            if (rootNode.isJsonObject()) {
                JsonObject details = rootNode.getAsJsonObject();
                User user = new User(details.get("name").getAsString(), details.get("nickname").getAsString(),
                    details.get("password").getAsString(), details.get("imagePath").getAsString());
                user.setScore(details.get("score").getAsInt());
                user.setMoney(details.get("money").getAsInt());
                user.setImage(createImageOfUsers(details.get("imagePath").getAsString()));
                addDecksAndUselessCardsToUser(user, filenames.get(i));
            }
        }
    }

    private Image createImageOfUsers(String path) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(stream);
    }

    private void addDecksAndUselessCardsToUser(User user, String filename) throws Exception {

        FileReader fileReader = new FileReader(addressOfStorage + "Users\\" + filename + "\\DeckAndCards.json");
        Scanner myReader = new Scanner(fileReader);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootNote = jsonParser.parse(myReader.nextLine());
        myReader.close();

        if (rootNote.isJsonObject()) {
            JsonObject details = rootNote.getAsJsonObject();
            JsonArray allDecks = details.getAsJsonArray("decks");
            Deck deck;

            if (allDecks != null) {
                for (int i = 0; i < allDecks.size(); i++) {
                    JsonObject getDeckFromJsonFromat = allDecks.get(i).getAsJsonObject();
                    deck = addDecksToUser(getDeckFromJsonFromat);
                    user.addDeckToAllDecks(getDeckFromJsonFromat.get("deckname").getAsString(), deck);
                }
            }

            JsonArray allUselessCards = details.getAsJsonArray("uselessCards");
            for (JsonElement jsonElement : allUselessCards) {
                user.addCardToAllUselessCards(jsonElement.getAsString());
            }
        }
        allUsers.add(user);
    }

    private Deck addDecksToUser(JsonObject getDeckFromJsonFromat) {

        Deck deck = new Deck(getDeckFromJsonFromat.get("deckname").getAsString());
        deck.setDeckActive(getDeckFromJsonFromat.get("isActivated").getAsBoolean());

        JsonArray mainDeck = getDeckFromJsonFromat.getAsJsonArray("mainDeck");
        for (JsonElement jsonElement : mainDeck) {
            deck.addCardToMainDeck(jsonElement.getAsString());
        }

        JsonArray sideDeck = getDeckFromJsonFromat.getAsJsonArray("sideDeck");
        for (JsonElement jsonElement : sideDeck) {
            deck.addCardToSideDeck(jsonElement.getAsString());
        }
        return deck;
    }

    public static void addUserToAllUsers(User newUser) {
        allUsers.add(newUser);
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static User getUserByName(String username) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getName().equals(username)) {
                return allUsers.get(i);
            }
        }
        return null;
    }

    public static HashMap<String, Card> getAllMonsterCards() {
        return allMonsterCards;
    }

    public static HashMap<String, Card> getAllSpellAndTrapCards() {
        return allSpellAndTrapCards;
    }

    public static void addNewImageForNewCards(String cardName, Image cardImage) {

    }

    public static boolean doesCardExist(String cardName) {
        String correctCardName = Utility.giveCardNameRemovingRedundancy(cardName);
        return allMonsterCards.containsKey(correctCardName) || allSpellAndTrapCards.containsKey(correctCardName);
    }

    public static Card getCardByName(String cardName) {
        String correctCardName = Utility.giveCardNameRemovingRedundancy(cardName);
        if (allMonsterCards.containsKey(correctCardName)) {
            return allMonsterCards.get(correctCardName);
        }
        if (allSpellAndTrapCards.containsKey(correctCardName)) {
            return allSpellAndTrapCards.get(correctCardName);
        }
        return null;
    }

    public static void addCardToNewCardsCrated(Card newCard) {
        newCardsCreated.put(newCard.getCardName(), newCard);
    }

    public static Card getUnknownCard() {
        return unknownCard;
    }
}
