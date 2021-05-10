package controller.duel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelPatterns {

    public static boolean isItCorrectPattern(String command) {
        //duel --new --second-player <player2 username> --rounds <1/3>
        Pattern[] patterns = new Pattern[6];
        patterns[0] = Pattern.compile("duel --new --second-player (\\S+) --rounds (\\d+)");
        patterns[1] = Pattern.compile("duel --second-player (\\S+) --new --rounds (\\d+)");
        patterns[2] = Pattern.compile("duel --new --rounds (\\d+) --second-player (\\S+)");
        patterns[3] = Pattern.compile("duel --second-player (\\S+) --rounds (\\d+) --new");
        patterns[4] = Pattern.compile("duel --rounds (\\d+) --new --second-player (\\S+)");
        patterns[5] = Pattern.compile("duel --rounds (\\d+) --second-player (\\S+) --new");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = patterns[i].matcher(command);
            if (matcher.find()) return true;
        }
        return false;
    }

    public static String getSecondPlayer(String command) {
        Pattern[] patterns = new Pattern[6];
        patterns[0] = Pattern.compile("duel --new --second-player (\\S+) --rounds (\\d+)");
        patterns[1] = Pattern.compile("duel --second-player (\\S+) --new --rounds (\\d+)");
        patterns[2] = Pattern.compile("duel --new --rounds (\\d+) --second-player (\\S+)");
        patterns[3] = Pattern.compile("duel --second-player (\\S+) --rounds (\\d+) --new");
        patterns[4] = Pattern.compile("duel --rounds (\\d+) --new --second-player (\\S+)");
        patterns[5] = Pattern.compile("duel --rounds (\\d+) --second-player (\\S+) --new");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = patterns[i].matcher(command);
            if (matcher.find()) {
                if (i == 0 || i == 1 || i == 3) return matcher.group(1);
                return matcher.group(2);
            }
        }
        return null;
    }

    public static int getRoundsNumber(String command) {
        Pattern[] patterns = new Pattern[6];
        patterns[0] = Pattern.compile("duel --new --second-player (\\S+) --rounds (\\d+)");
        patterns[1] = Pattern.compile("duel --second-player (\\S+) --new --rounds (\\d+)");
        patterns[2] = Pattern.compile("duel --new --rounds (\\d+) --second-player (\\S+)");
        patterns[3] = Pattern.compile("duel --second-player (\\S+) --rounds (\\d+) --new");
        patterns[4] = Pattern.compile("duel --rounds (\\d+) --new --second-player (\\S+)");
        patterns[5] = Pattern.compile("duel --rounds (\\d+) --second-player (\\S+) --new");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = patterns[i].matcher(command);
            if (matcher.find()) {
                if (i == 0 || i == 1 || i == 3) return Integer.parseInt(matcher.group(2));
                return Integer.parseInt(matcher.group(1));
            }
        }
        return 0;
    }
}
