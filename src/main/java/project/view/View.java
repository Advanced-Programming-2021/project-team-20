package project.view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.controller.non_duel.mainController.MainController;

public class View {
    private MainController mainController;
    private String invalidCommand = "invalid command!";
    private String loginMenu = "Login Menu";

    private static String currentMenu;
    static {
        currentMenu = new String("Login Menu");
    }

    public void infiniteLoop() {
        mainController = MainController.getInstance();
        Scanner input = new Scanner(System.in);
        String commands = new String();

        while (true) {

            commands = input.nextLine().replaceAll("\\s+", " ").trim();
            if (commands.matches("end program")) {
                break;
            }
            if (currentMenu.equals("project.Main Menu") && commands.matches("user logout")) {
                setCurrentMenu(loginMenu);
                continue;
            }
            if (commands.startsWith("menu enter")) {
                enterMenu(commands);
                continue;
            } else if (commands.startsWith("menu exit")) {
                exitMenu(commands);
                continue;
            } else if (commands.equals("menu show-current")) {
                printMethod(currentMenu);
                continue;
            }
            // try {
            //     if (System.getProperty("os.name").contains("Windows"))
            //         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            //     else
            //         Runtime.getRuntime().exec("clear");
            // } catch (IOException | InterruptedException ex) {

            // }
           printMethod(mainController.switchCaseInput(commands));
        }
        input.close();
    }

    private void printMethod(String string){
        System.out.println(string);
    }

    public static void setCurrentMenu(String currentMenu) {
        View.currentMenu = currentMenu;
    }

    public static String getCurrentMenu() {
        return currentMenu;
    }

    private void exitMenu(String command) {

        if (!command.equals("menu exit")) {
            printMethod(invalidCommand);
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
            printMethod(invalidCommand);
            return;
        }

        if (currentMenu.equals(loginMenu)) {
            printMethod("please login first");
            return;
        }

        if (currentMenu.equals("project.Main Menu")) {
            if (!mainController.enterMenu(menuname)) {
                printMethod(invalidCommand);
            }
        } else {
            printMethod("menu navigation is not possible");
        }
    }
}
