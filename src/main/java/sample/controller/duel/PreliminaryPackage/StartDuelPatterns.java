package sample.controller.duel.PreliminaryPackage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartDuelPatterns {

    public HashMap<String, String> findCommand(String command) {

        HashMap<String, String> startDuelBetweenTwoPlayer = findCommandsWhenTwoPlayerStartDuel(command);
        if (startDuelBetweenTwoPlayer != null) {
            return startDuelBetweenTwoPlayer;
        }

        HashMap<String, String> startDuelWithAI = findCommandsBetweenOnePlayerAndAI(command);
        if (startDuelWithAI != null) {
            return startDuelWithAI;
        }
        return null;

    }

    private HashMap<String, String> findCommandsWhenTwoPlayerStartDuel(String command) {
        Pattern[] patterns = new Pattern[6];
        patterns[0] = Pattern
                .compile("duel (-n|--new) (-s-p|--second-player) (?<secondPlayer>\\S+) (-r|--rounds) (?<rounds>\\d+)");
        patterns[1] = Pattern
                .compile("duel (-s-p|--second-player) (?<secondPlayer>\\S+) (-n|--new) (-r|--rounds) (?<rounds>\\d+)");
        patterns[2] = Pattern
                .compile("duel (-n|--new) (-r|--rounds) (?<rounds>\\d+) (-s-p|--second-player) (?<secondPlayer>\\S+)");
        patterns[3] = Pattern
                .compile("duel (-s-p|--second-player) (?<secondPlayer>\\S+) (-r|--rounds) (?<rounds>\\d+) (-n|--new)");
        patterns[4] = Pattern
                .compile("duel (-r|--rounds) (?<rounds>\\d+) (-n|--new) (-s-p|--second-player) (?<secondPlayer>\\S+)");
        patterns[5] = Pattern
                .compile("duel (-r|--rounds) (?<rounds>\\d+) (-s-p|--second-player) (?<secondPlayer>\\S+) (-n|--new)");

        HashMap<String, String> foundCommands = new HashMap<>();
        for (int i = 0; i < patterns.length; i++) {
            Matcher matcher = patterns[i].matcher(command);
            if (matcher.find()) {
                foundCommands.put("secondPlayer", matcher.group("secondPlayer"));
                foundCommands.put("rounds", matcher.group("rounds"));
                return foundCommands;
            }
        }
        return null;
    }

    private HashMap<String, String> findCommandsBetweenOnePlayerAndAI(String command) {

        Pattern[] patterns = new Pattern[6];
        patterns[0] = Pattern.compile("duel (-n|--new) (-a|--ai) (-r|--rounds) (<?rounds>\\d+)");
        patterns[1] = Pattern.compile("duel (-n|--new) (-r|--rounds) (<?rounds>\\d+) (-a|--ai)");
        patterns[2] = Pattern.compile("duel (-a|--ai) (-n|--new) (-r|--rounds) (<?rounds>\\d+)");
        patterns[3] = Pattern.compile("duel (-a|--ai) (-r|--rounds) (<?rounds>\\d+) (-n|--new)");
        patterns[4] = Pattern.compile("duel (-r|--rounds) (<?rounds>\\d+) (-n|--new) (-a|--ai)");
        patterns[5] = Pattern.compile("duel (-r|--rounds) (<?rounds>\\d+) (-a|--ai) (-n|--new)");

        HashMap<String, String> foundCommands = new HashMap<>();
        for (int i = 0; i < patterns.length; i++) {
            Matcher matcher = patterns[i].matcher(command);
            if (matcher.find()) {
                foundCommands.put("secondPlayer", "AI");
                foundCommands.put("rounds", matcher.group("rounds"));
                return foundCommands;
            }
        }
        return null;
    }
}
