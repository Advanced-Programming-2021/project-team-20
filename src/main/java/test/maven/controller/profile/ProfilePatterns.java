package test.maven.controller.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

public class ProfilePatterns {

    ArrayList<Pattern> allPatterns = new ArrayList<>();

    private Pattern changeNickname = Pattern.compile("profile change --(n|nickname) (\\S+)");
    private Pattern changePassword1 = Pattern
            .compile("profile change --password --(c|current) (\\S+) --(n|new) (\\S+)");
    private Pattern changePassword2 = Pattern
            .compile("profile change --password --(n|new) (\\S+) --(c|current) (\\S+)");

    public HashMap<String, String> findCommands(String command) {
        HashMap<String, String> output = new HashMap<>();
        Matcher matcher ;
        matcher = changeNickname.matcher(command);
        if (matcher.find()) {
            output.put("nickname", matcher.group(2));
            return output;
        }

        matcher = changePassword1.matcher(command);
        if (matcher.find()) {
            output.put("current password", matcher.group(2));
            output.put("new password", matcher.group(4));
            return output;
        }

        matcher = changePassword2.matcher(command);
        if (matcher.find()) {
            output.put("current password", matcher.group(4));
            output.put("new password", matcher.group(2));
            return output;
        }

        return null;
    }

}
