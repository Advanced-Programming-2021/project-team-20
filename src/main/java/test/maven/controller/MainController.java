package test.maven.controller;

import test.maven.controller.duel.GameManager;
import test.maven.controller.loginMenu.LoginMenu;


public class MainController {
    private boolean x;

    public String getCommandFromUser(String input) {

        return null;
    }

    private String switchCaseInput(String input) {
        if (x) {
            LoginMenu loginMenu = new LoginMenu();
        } else if (x) {
            DeckCommands deckCommands = new DeckCommands();
        } else if (x) {
            Profile profile = new Profile();
        } else if (x) {
            Scoreboard scoreboard = new Scoreboard();
        } else if (x) {
            Shop shop = new Shop();
        } else if (x) {
            GameManager gameManager = new GameManager();
        }
        return null;
    }
}