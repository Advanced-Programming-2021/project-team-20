package test.maven.controller.storage;

import com.google.gson.*;
import com.opencsv.CSVReader;
import test.maven.model.Deck;
import test.maven.model.User;
import test.maven.model.card.cardData.General.Card;
import test.maven.model.card.cardData.MonsterCardData.MonsterCard;
import test.maven.model.card.cardData.MonsterCardData.MonsterCardAttribute;
import test.maven.model.card.cardData.MonsterCardData.MonsterCardFamily;
import test.maven.model.card.cardData.MonsterCardData.MonsterCardValue;
import test.maven.model.card.cardData.SpellCardData.SpellCard;
import test.maven.model.card.cardData.SpellCardData.SpellCardValue;
import test.maven.model.card.cardData.TrapCardData.TrapCard;
import test.maven.model.card.cardData.TrapCardData.TrapCardValue;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Storage {

    private static ArrayList<User> allUsers = new ArrayList<>();
    private static HashMap<String, Card> allMonsterCards = new HashMap<>();
    private static HashMap<String, Card> allSpellAndTrapCards = new HashMap<>();
    private String addressOfStorage = "Resourses\\";

    public void startProgram() {
        try {
            addUsersToArrayList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println(allUsers.size());
        addMonsterCards();
        addTrapAndSpellCards();
    }

    public void endProgram() {

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
            }

        }
    }

    private String toGsonFormat(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("nickname", user.getNickname());
        jsonObject.addProperty("password", user.getPassword());
        jsonObject.addProperty("score", user.getScore());
        jsonObject.addProperty("money", user.getMoney());
        return jsonObject.toString();
    }

    private void addTrapAndSpellCards() {

        try {

            FileReader filereader = new FileReader(addressOfStorage + "CSV\\SpellTrap.csv");
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[1].equals("Trap")) {
                    allSpellAndTrapCards.put(nextRecord[0],
                        new TrapCard(nextRecord[0], nextRecord[3],
                            TrapCardValue.valueOf(nextRecord[2].toUpperCase()), null,
                            nextRecord[4].equals("Unlimited") ? 3 : 1, 0, Integer.parseInt(nextRecord[5])));
                } else if (nextRecord[1].equals("Spell")) {
                    allSpellAndTrapCards.put(nextRecord[0],
                        new SpellCard(nextRecord[0], nextRecord[3],
                            SpellCardValue.valueOf(formatterStringToEnum(nextRecord[2])), null,
                            nextRecord[4].equals("Unlimited") ? 3 : 1, 0, Integer.parseInt(nextRecord[5])));
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMonsterCards() {

        try {

            FileReader filereader = new FileReader(addressOfStorage + "CSV\\Monster.csv");
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            int firstRow = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (firstRow > 1) {

                    allMonsterCards.put(nextRecord[0],
                        new MonsterCard(Integer.parseInt(nextRecord[5]), Integer.parseInt(nextRecord[6]),
                            Integer.parseInt(nextRecord[1]), MonsterCardAttribute.valueOf(nextRecord[2]),
                            MonsterCardFamily.valueOf(formatterStringToEnum(nextRecord[3])),
                            MonsterCardValue.valueOf(nextRecord[4].toUpperCase()), nextRecord[0], nextRecord[7],
                            null, 3, Integer.parseInt(nextRecord[8])));
                }
                firstRow++;
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void addUsersToArrayList() throws IOException {

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
                    details.get("password").getAsString());

                user.setScore(details.get("score").getAsInt());
                user.setMoney(details.get("money").getAsInt());
                addDecksAndUselessCardsToUser(user, filenames.get(i));
                allUsers.add(user);
            }
        }
    }

    private void addDecksAndUselessCardsToUser(User user, String filename) throws FileNotFoundException {

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

//    public static boolean doesUserWithThisNicknameExists(String nickname) {
//        for (int i = 0; i < allUsers.size(); i++) {
//            if (allUsers.get(i).getNickname().equals(nickname)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean doesUsernameAndPasswordMatches(String username, String password) {
//        for (int i = 0; i < allUsers.size(); i++) {
//            if (allUsers.get(i).getName().equals(username) && allUsers.get(i).getPassword().equals(password)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static HashMap<String, Card> getAllMonsterCards() {
        return allMonsterCards;
    }

    public static HashMap<String, Card> getAllSpellAndTrapCards() {
        return allSpellAndTrapCards;
    }

    public static boolean doesCardExist(String cardname) {
        return allMonsterCards.containsKey(cardname) || allSpellAndTrapCards.containsKey(cardname);
    }

}
