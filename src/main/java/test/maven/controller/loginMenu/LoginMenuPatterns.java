package test.maven.controller.loginMenu;

import java.util.ArrayList;
import java.util.regex.*;

public class LoginMenuPatterns {

    // Pattern createUserPattern1 = Pattern.compile("user create --username \\S+
    // --nickname \\S+ --password \\S+");
    // Pattern createUserPattern2 = Pattern.compile("user create --nickname \\S+
    // --username \\S+ --password \\S+");
    // Pattern createUserPattern3 = Pattern.compile("user create --password \\S+
    // --nickname \\S+ --username \\S+");
    // Pattern createUserPattern4 = Pattern.compile("user create --password \\S+
    // --username \\S+ --nickname \\S+");
    // Pattern createUserPattern5 = Pattern.compile("user create --username \\S+
    // --password \\S+ --nickname \\S+");
    // Pattern createUserPattern6 = Pattern.compile("user create --nickname \\S+
    // --password \\S+ --username \\S+");
    // Pattern createUserPattern7 = Pattern.compile("user create --nickname \\S+
    // --password \\S+ --username \\S+");
    // String x = "asda (\\S+)";
    // Pattern t = Pattern.compile(x);
    ArrayList<String> patterns = new ArrayList<>();

    private void x() {
        patterns.add("--username (\\S+)");
        patterns.add("--nickname (\\S+)");
        patterns.add("--password (\\S+)");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                int index = i + j + 1;
                String finalPattern = "user create" + patterns.get(i) + patterns.get(index % 3)
                        + patterns.get(index + 1 % 3);
            }
            Pattern pattern = Pattern.compile("user create" + patterns.get(i % 3));
        }

    }

}