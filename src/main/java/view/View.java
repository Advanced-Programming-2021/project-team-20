package view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.mainController.MainController;

public class View {

    private MainController mainController = new MainController();
    private String invalidCommand = "invalid command!";
    private String loginMenu = "Login Menu";

    private static String currentMenu;
    static {
        currentMenu = new String("Login Menu");
    }

    public void infiniteLoop() {

        Scanner input = new Scanner(System.in);
        String commands = new String();

        while (true) {

            commands = input.nextLine().replaceAll("\\s+", " ").trim();
        //    System.out.println(commands + "  beging of view");
            if (commands.matches("end program")) {
                break;
            }
        //    System.out.println("123456789");
            if (currentMenu.equals("Main Menu") && commands.matches("user logout")) {
                setCurrentMenu(loginMenu);
                continue;
            }
        //    System.out.println("   dsadasdasasdsada");
            if (commands.startsWith("menu enter")) {
        //        System.out.println("menu enter");
                enterMenu(commands);
                continue;
            } else if (commands.startsWith("menu exit")) {
                exitMenu(commands);
                continue;
            } else if (commands.equals("menu show-current")) {
        //        System.out.println("menu show current          2");
                System.out.println(currentMenu);
                continue;
            }
        //    System.out.println(commands + " " + currentMenu);
            System.out.println(mainController.switchCaseInput(commands));
        }

        input.close();
    }

    public static void setCurrentMenu(String currentMenu) {
        View.currentMenu = currentMenu;
    }

    private void exitMenu(String command) {

        if (!command.equals("menu exit")) {
            System.out.println(invalidCommand);
            return;
        }
        mainController.exitMenu();

    }

    private void enterMenu(String command) {

        Pattern pattern = Pattern.compile("menu enter (\\S+)");
        Matcher matcher = pattern.matcher(command);

        String menuname = new String();
        if (matcher.find()) {
            menuname = matcher.group(1);
        } else {
            System.out.println(invalidCommand);
            return;
        }

        if (currentMenu.equals(loginMenu)) {
            System.out.println("please login first");
            return;
        }

        if (currentMenu.equals("Main Menu")) {
            if (!mainController.enterMenu(menuname)) {
                System.out.println(invalidCommand);
            }
        } else {
            System.out.println("menu navigation is not possible");
        }
    }
}
