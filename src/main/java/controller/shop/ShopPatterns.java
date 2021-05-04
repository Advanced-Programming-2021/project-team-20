package controller.shop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopPatterns {

    public static boolean isItBuyPattern(String command) {
        Pattern pattern = Pattern.compile("shop buy \\S+");
        Matcher matcher = pattern.matcher(command);
        return matcher.find();
    }

    public static boolean isItShowAllPattern(String command) {
        Pattern pattern = Pattern.compile("^shop show --all$");
        Matcher matcher = pattern.matcher(command);
        return matcher.find();
    }
}
