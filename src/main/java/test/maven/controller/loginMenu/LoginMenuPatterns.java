package test.maven.controller.loginMenu;

import java.util.ArrayList;
import java.util.regex.*;

public class LoginMenuPatterns {

    private Pattern createUserPattern1 = Pattern.compile("user create --username \\S+ --nickname \\S+ --password \\S+");
    private Pattern createUserPattern2 = Pattern.compile("user create --nickname \\S+ --username \\S+ --password \\S+");
    private Pattern createUserPattern3 = Pattern.compile("user create --password \\S+ --nickname \\S+ --username \\S+");
    private Pattern createUserPattern4 = Pattern.compile("user create --password \\S+ --username \\S+ --nickname \\S+");
    private Pattern createUserPattern5 = Pattern.compile("user create --username \\S+ --password \\S+ --nickname \\S+");
    private Pattern createUserPattern6 = Pattern.compile("user create --nickname \\S+ --password \\S+ --username \\S+");
    private static ArrayList<Pattern> allPatterns = new ArrayList<>();
    

}