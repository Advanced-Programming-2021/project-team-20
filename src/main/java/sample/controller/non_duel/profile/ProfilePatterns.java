package sample.controller.non_duel.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

public class ProfilePatterns {

        private Pattern changeNickname = Pattern.compile("^profile change (-n|--nickname) (\\S+)$");
        private Pattern changePassword1 = Pattern.compile(
                        "profile change (-p|--password) (-c|--current) (?<current>\\S+) (-n|--new) (?<new>\\S+)");
        private Pattern changePassword2 = Pattern.compile(
                        "profile change (-p|--password) (-n|--new) (?<new>\\S+) (-c|--current) (?<current>\\S+)");
        private Pattern changePassword3 = Pattern.compile(
                        "profile change (-n|--new) (?<new>\\S+) (-p|--password) (-c|--current) (?<current>\\S+)");
        private Pattern changePassword4 = Pattern.compile(
                        "profile change (-c|--current) (?<current>\\S+) (-n|--new) (?<new>\\S+) (-p|--password)");
        private Pattern changePassword5 = Pattern.compile(
                        "profile change (-c|--current) (?<current>\\S+) (-p|--password) (-n|--new) (?<new>\\S+)");
        private Pattern changePassword6 = Pattern.compile(
                        "profile change (-n|--new) (?<new>\\S+) (-c|--current) (?<current>\\S+) (-p|--password)");

        public HashMap<String, String> findCommands(String command) {
                HashMap<String, String> output = new HashMap<>();
                Matcher matcher;
                matcher = changeNickname.matcher(command);
                if (matcher.find()) {
                        output.put("nickname", matcher.group(2));
                        return output;
                }

                ArrayList<Pattern> changePasswordPatterns = new ArrayList<>();
                changePasswordPatterns.add(changePassword1);
                changePasswordPatterns.add(changePassword2);
                changePasswordPatterns.add(changePassword3);
                changePasswordPatterns.add(changePassword4);
                changePasswordPatterns.add(changePassword5);
                changePasswordPatterns.add(changePassword6);

                for (int i = 0; i < 6; i++) {
                        matcher = changePasswordPatterns.get(i).matcher(command);
                        if (matcher.find()) {
                                output.put("current password", matcher.group("current"));
                                output.put("new password", matcher.group("new"));
                                return output;
                        }
                }

                return null;
        }

}
