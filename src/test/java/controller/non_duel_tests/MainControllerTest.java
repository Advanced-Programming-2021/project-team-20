package controller.non_duel_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.non_duel.mainController.MainController;
import controller.non_duel.storage.Storage;
import view.View;

class MainControllerTest {
    static  MainController mainController;
    static View view;
    static Storage storage;
    @BeforeAll
    static void startTest() {
        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view = new View();
        mainController = MainController.getInstance();
        
    }

    @Test
    void enterAndExitMenuTest() {
        mainController.enterMenu("ImportAndExport");
        assertEquals("ImportAndExport", View.getCurrentMenu());
        mainController.exitMenu();
        assertEquals("Main Menu", View.getCurrentMenu());
        mainController.enterMenu("Duel Began"); 
        assertEquals("Duel Began", View.getCurrentMenu());
        mainController.exitMenu();
        assertEquals("Main Menu", View.getCurrentMenu());
        mainController.enterMenu("Duel");
        assertEquals("Duel", View.getCurrentMenu());
        mainController.exitMenu(); 
        mainController.enterMenu("Shop");
        assertEquals("Shop", View.getCurrentMenu());
        mainController.exitMenu();
        mainController.enterMenu("Profile");
        assertEquals("Profile", View.getCurrentMenu());
        mainController.exitMenu();
        mainController.enterMenu("Scoreboard");
        assertEquals("Scoreboard", View.getCurrentMenu());
        mainController.exitMenu();
        mainController.enterMenu("Deck");
        assertEquals("Deck", View.getCurrentMenu());
        mainController.exitMenu();
        mainController.enterMenu("123");
        assertEquals("Main Menu", View.getCurrentMenu());
    }

    @Test
    void switchCaseInputTest(){
        String output = "";
        output = mainController.switchCaseInput("user login -p ali -u ali -n ali");
        assertEquals("user logged in successfully!", output); 
        mainController.enterMenu("Deck");
        output = mainController.switchCaseInput("deck create 132");
        assertEquals("deck created successfully!", output);
        mainController.exitMenu();
        mainController.enterMenu("Profile");
        output = mainController.switchCaseInput("profile change -n 12");
        assertEquals("nickname changed successfully!", output);
        mainController.exitMenu();
        mainController.enterMenu("Shop");
        output = mainController.switchCaseInput("shop buy Suijin");
        assertEquals("successful buy", output);
        mainController.exitMenu();
        mainController.enterMenu("Scoreboard");
        output = mainController.switchCaseInput("Scoreboard how");
        assertEquals("invalid command!", output);
        mainController.exitMenu();
        mainController.enterMenu("ImportAndExport");
        output = mainController.switchCaseInput("import card Suijin");
        assertEquals("card imported successfully!", output);
        mainController.exitMenu();
        mainController.enterMenu("Duel");
        assertEquals("Duel", View.getCurrentMenu());
        output = mainController.switchCaseInput("command");
        assertEquals("invalid command", output);
        mainController.exitMenu();
        mainController.enterMenu("Duel Began");
        output = mainController.switchCaseInput("command");
        assertEquals("invalid command", output);
    }
}
