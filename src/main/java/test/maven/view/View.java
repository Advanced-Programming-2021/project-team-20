package test.maven.view;

import java.util.Scanner;
import test.maven.controller.MainController;


public class View {

    private static String currentMenu;

    public void infiniteLoop() {
       
        MainController mainController = new MainController();
        Scanner input = new Scanner(System.in);
        String commands = new String();

        while (!commands.matches("user logout")) {
       
            commands = input.nextLine();
            if (isInputForCurrentMenu(commands)) {
                mainController.switchCaseInput(commands);
            } else {
                System.out.println("menu navigation is not possible");
            }
       
        }
        input.close();
    }

    public static void setCurrentMenu(String currentMenu) {
        View.currentMenu = currentMenu;
    }

    private boolean isInputForCurrentMenu(String input) {
        if (input.matches("")) {
            return false;
        }
        return true;
    }
}