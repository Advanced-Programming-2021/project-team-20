package test.maven.controller.shop;

import java.util.HashMap;
import java.util.regex.*;

public class ShopPatterns {
    private Pattern buyCard = Pattern.compile("shop buy (\\S+)");
    private Pattern shopShowAll = Pattern.compile("shop show --all");

    public HashMap<String, String> findCommand(String command) {
        HashMap<String, String> output = new HashMap<>();
        Matcher matcher;
        matcher = buyCard.matcher(command);
        if (matcher.find()) {
            output.put("buy card", matcher.group(1));
            return output;
        }

        matcher = shopShowAll.matcher(command);
        if (matcher.find()) {
            output.put("show", "all");
            return output;
        }

        return null;
    }
}
