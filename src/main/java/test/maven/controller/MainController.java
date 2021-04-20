package test.maven.controller;

import test.maven.controller.deckCommands.DeckCommands;
import test.maven.controller.duel.GameManager;
import test.maven.controller.loginMenu.LoginMenu;
import test.maven.controller.profile.Profile;
import test.maven.controller.scoreboard.Scoreboard;
import test.maven.controller.shop.Shop;


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