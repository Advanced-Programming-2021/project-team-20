package controller.loginMenu;

import java.util.regex.*;

public class LoginMenuPatterns {

    public static boolean isItCreateUserPattern(String command) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find())
                return true;
        }
        return false;
    }

    public static boolean isItLoginUserPattern(String command) {
        Pattern[] loginUserPattern = new Pattern[2];
        loginUserPattern[0] = Pattern.compile("user login (-p|--password) (\\S+) (-u|--username) (\\S+)");
        loginUserPattern[1] = Pattern.compile("user login (-u|--username) (\\S+) (-p|--password) (\\S+)");
        for (int i = 0; i < 2; i++) {
            Matcher matcher = loginUserPattern[i].matcher(command);
            if (matcher.find())
                return true;
        }
        return false;
    }

    public static String findUsernameCreateUser(String command) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                return findUsernameCreateUserWithNumber(command, i);
            }
        }
        return null;
    }

    private static String findUsernameCreateUserWithNumber(String command, int i) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int j = 0; j < 6; j++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                if (i == 2 || i == 3)
                    return matcher.group(2);
                if (i == 0 || i == 5)
                    return matcher.group(4);
                return matcher.group(6);
            }
        }
        return null;
    }

    public static String findNicknameCreateUser(String command) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                return findNicknameCreateUserWithNumber(command, i);
            }
        }
        return null;
    }

    private static String findNicknameCreateUserWithNumber(String command, int i) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int j = 0; j < 6; j++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                if (i == 0 || i == 1)
                    return matcher.group(2);
                if (i == 2 || i == 4)
                    return matcher.group(4);
                return matcher.group(6);
            }
        }
        return null;
    }

    public static String findPasswordCreateUser(String command) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int i = 0; i < 6; i++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                return findPasswordCreateUserWithNumber(command, i);
            }
        }
        return null;
    }

    private static String findPasswordCreateUserWithNumber(String command, int i) {
        Pattern[] createUserPattern = new Pattern[6];
        createUserPattern[0] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-u|--username) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[1] = Pattern
                .compile("user create (-n|--nickname) (\\S+) (-p|--password) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[2] = Pattern
                .compile("user create (-u|--username) (\\S+) (-n|--nickname) (\\S+) (-p|--password) (\\S+)");
        createUserPattern[3] = Pattern
                .compile("user create (-u|--username) (\\S+) (-p|--password) (\\S+) (-n|--nickname) (\\S+)");
        createUserPattern[4] = Pattern
                .compile("user create (-p|--password) (\\S+) (-n|--nickname) (\\S+) (-u|--username) (\\S+)");
        createUserPattern[5] = Pattern
                .compile("user create (-p|--password) (\\S+) (-u|--username) (\\S+) (-n|--nickname) (\\S+)");
        for (int j = 0; j < 6; j++) {
            Matcher matcher = createUserPattern[i].matcher(command);
            if (matcher.find()) {
                if (i == 4 || i == 5)
                    return matcher.group(2);
                if (i == 1 || i == 3)
                    return matcher.group(4);
                return matcher.group(6);
            }
        }
        return null;
    }

    public static String findUsernameLoginUser(String command) {
        Pattern[] loginUserPattern = new Pattern[2];
        loginUserPattern[0] = Pattern.compile("user login (-p|--password) (\\S+) (-u|--username) (\\S+)");
        loginUserPattern[1] = Pattern.compile("user login (-u|--username) (\\S+) (-p|--password) (\\S+)");
        for (int i = 0; i < 2; i++) {
            Matcher matcher = loginUserPattern[i].matcher(command);
            if (matcher.find()) {
                if (i == 0)
                    return matcher.group(4);
                return matcher.group(2);
            }
        }
        return null;
    }

    public static String findPasswordLoginUser(String command) {
        Pattern[] loginUserPattern = new Pattern[2];
        loginUserPattern[0] = Pattern.compile("user login (-p|--password) (\\S+) (-u|--username) (\\S+)");
        loginUserPattern[1] = Pattern.compile("user login (-u|--username) (\\S+) (-p|--password) (\\S+)");
        for (int i = 0; i < 2; i++) {
            Matcher matcher = loginUserPattern[i].matcher(command);
            if (matcher.find()) {
                if (i == 0)
                    return matcher.group(2);
                return matcher.group(4);
            }
        }
        return null;
    }

}
