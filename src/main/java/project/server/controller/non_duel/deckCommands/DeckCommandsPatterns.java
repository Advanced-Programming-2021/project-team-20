package project.server.controller.non_duel.deckCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

public class DeckCommandsPatterns {

    private Pattern deckCreate = Pattern.compile("deck create (\\S+)");
    private Pattern deckDelete = Pattern.compile("deck delete (\\S+)");
    private Pattern deckActivate = Pattern.compile("deck set-activate (\\S+)");
    private Pattern deckAddCard1 = Pattern
            .compile("deck add-card (-c|--card) (?<card>.+) (-d|--deck) (?<deck>\\S+)(?<side> -s| --side|)");
    private Pattern deckAddCard2 = Pattern
            .compile("deck add-card (-d|--deck) (?<deck>\\S+) (-c|--card) (?<card>.+)(?<side> -s| --side|)");
    private Pattern deckAddCard3 = Pattern
            .compile("deck add-card (-d|--deck) (?<deck>\\S+)(?<side> -s| --side|) (-c|--card) (?<card>.+)");
    private Pattern deckAddCard4 = Pattern
            .compile("deck add-card (-c|--card) (?<card>.+)(?<side> -s| --side|) (-d|--deck) (?<deck>\\S+)");
    private Pattern deckAddCard5 = Pattern
            .compile("deck add-card(?<side> -s| --side|) (-c|--card) (?<card>.+) (-d|--deck) (?<deck>\\S+)");
    private Pattern deckAddCard6 = Pattern
            .compile("deck add-card(?<side> -s| --side|) (-d|--deck) (?<deck>\\S+) (-c|--card) (?<card>.+)");
    private Pattern deckDeleteCard1 = Pattern
            .compile("deck rm-card (-c|--card) (?<card>.+) (-d|--deck) (?<deck>\\S+)(?<side> -s| --side|)");
    private Pattern deckDeleteCard2 = Pattern
            .compile("deck rm-card (-d|--deck) (?<deck>\\S+) (-c|--card) (?<card>.+)(?<side> -s| --side|)");
    private Pattern deckDeleteCard3 = Pattern
            .compile("deck rm-card (-d|--deck) (?<deck>\\S+)(?<side> -s| --side|) (-c|--card) (?<card>.+)");
    private Pattern deckDeleteCard4 = Pattern
            .compile("deck rm-card (-c|--card) (?<card>.+)(?<side> -s| --side|) (-d|--deck) (?<deck>\\S+)");
    private Pattern deckDeleteCard5 = Pattern
            .compile("deck rm-card(?<side> -s| --side|) (-c|--card) (?<card>.+) (-d|--deck) (?<deck>\\S+)");
    private Pattern deckDeleteCard6 = Pattern
            .compile("deck rm-card(?<side> -s| --side|) (-d|--deck) (?<deck>\\S+) (-c|--card) (?<card>.+)");

    private Pattern deckShowAll = Pattern.compile("deck show (-a|--all)");
    private Pattern deckShowOneDeck1 = Pattern
            .compile("deck show (-d-n|--deck-name) (?<deckName>\\S+)(?<side> -s| --side|)");
    private Pattern deckShowOneDeck2 = Pattern
            .compile("deck show(?<side> -s| --side|) (-d-n|--deck-name) (?<deckName>\\S+)");
    private Pattern deckShowCards = Pattern.compile("deck show (-c|--cards)");

    private Matcher matcher;

    public HashMap<String, String> findCommands(String command) {
        HashMap<String, String> output = new HashMap<>();

        matcher = deckCreate.matcher(command);
        if (matcher.find()) {
            output.put("create deck", matcher.group(1));
            return output;
        }

        matcher = deckDelete.matcher(command);
        if (matcher.find()) {
            output.put("delete deck", matcher.group(1));
            return output;
        }

        matcher = deckActivate.matcher(command);
        if (matcher.find()) {
            output.put("activate deck", matcher.group(1));
            return output;
        }

        if (command.startsWith("deck add-card")) {
            return addCardToDeck(command);
        }

        if (command.startsWith("deck rm-card")) {
            return deleteCardFromDeck(command);
        }

        matcher = deckShowAll.matcher(command);
        if (matcher.find()) {
            output.put("show all deck", " ");
            return output;
        }

        matcher = deckShowCards.matcher(command);
        if (matcher.find()) {
            output.put("show all cards", " ");
            return output;
        }

        if (command.startsWith("deck show")) {
            return showOneDeck(command);
        }

        return null;
    }

    private HashMap<String, String> showOneDeck(String command) {
        HashMap<String, String> output = new HashMap<>();
        matcher = deckShowOneDeck1.matcher(command);
        if (matcher.find()) {
            output.put("show deck with name", matcher.group("deckName"));
            output.put("side", matcher.group("side"));
            return output;
        }

        matcher = deckShowOneDeck2.matcher(command);
        if (matcher.find()) {
            output.put("show deck with name", matcher.group("deckName"));
            output.put("side", matcher.group("side"));
            return output;
        }
        return null;
    }

    private HashMap<String, String> addCardToDeck(String command) {
        HashMap<String, String> output = new HashMap<>();

        ArrayList<Pattern> addCardsPattern = new ArrayList<>();
        addCardsPattern.add(deckAddCard1);
        addCardsPattern.add(deckAddCard2);
        addCardsPattern.add(deckAddCard3);
        addCardsPattern.add(deckAddCard4);
        addCardsPattern.add(deckAddCard5);
        addCardsPattern.add(deckAddCard6);
        for (int i = 0; i < addCardsPattern.size(); i++) {
            matcher = addCardsPattern.get(i).matcher(command);
            if (matcher.find()) {
                output.put("add card to", matcher.group("side"));
                output.put("deck", matcher.group("deck"));
                output.put("card", matcher.group("card"));
                return output;
            }
        }
        return null;
    }

    private HashMap<String, String> deleteCardFromDeck(String command) {
        HashMap<String, String> output = new HashMap<>();

        ArrayList<Pattern> deleteCardPattern = new ArrayList<>();
        deleteCardPattern.add(deckDeleteCard1);
        deleteCardPattern.add(deckDeleteCard2);
        deleteCardPattern.add(deckDeleteCard3);
        deleteCardPattern.add(deckDeleteCard4);
        deleteCardPattern.add(deckDeleteCard5);
        deleteCardPattern.add(deckDeleteCard6);
        for (int i = 0; i < deleteCardPattern.size(); i++) {
            matcher = deleteCardPattern.get(i).matcher(command);
            if (matcher.find()) {
                output.put("delete card from", matcher.group("side"));
                output.put("deck", matcher.group("deck"));
                output.put("card", matcher.group("card"));
                return output;
            }
        }
        return null;
    }
}
