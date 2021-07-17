package project.client;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.scene.image.Image;
import project.model.cardData.General.*;
import project.model.cardData.MonsterCardData.*;
import project.model.cardData.SpellCardData.*;
import project.model.cardData.TrapCardData.*;
import project.model.Utility.Utility;

public class CardsStorage {
    private static HashMap<String, Card> allMonsterCards = new HashMap<>();
    private static HashMap<String, Card> allSpellAndTrapCards = new HashMap<>();
    private static Card unknownCard;
    private static HashMap<String, Card> newCardsCreated = new HashMap<>();
    private String addressOfStorage = "Resourses\\";

    public void startProgram() throws Exception {

        addMonsterCards();
        addTrapCards();
        addSpellCards();

        unknownCard = new Card("cardName", null, "", null, 10, 125, null);
        unknownCard.setImage(createImageOfCards("Unknown"));

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

    public static HashMap<String, Card> getAllMonsterCards() {
        return allMonsterCards;
    }

    public static HashMap<String, Card> getAllSpellAndTrapCards() {
        return allSpellAndTrapCards;
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
