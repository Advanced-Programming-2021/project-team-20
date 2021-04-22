package test.maven.controller;

import java.util.HashMap;

import test.maven.controller.deckCommands.DeckCommands;
import test.maven.controller.duel.GameManager;
import test.maven.controller.loginMenu.LoginMenu;
import test.maven.controller.profile.Profile;
import test.maven.controller.scoreboard.Scoreboard;
import test.maven.controller.shop.Shop;
import test.maven.view.View;

public class MainController {
    private boolean isInLoginMenu = true;
    private boolean isInMainMenu;
    private boolean isInDeckCommands;
    private boolean isInScoreBoard;
    private boolean isInShop;
    private boolean isInProfile;
    private boolean isInGameManager;

    private LoginMenu loginMenu = new LoginMenu();
    private DeckCommands deckCommands = new DeckCommands();
    private Profile profile = new Profile();
    private Scoreboard scoreboard = new Scoreboard();
    private Shop shop = new Shop();
    private GameManager gameManager = new GameManager();

    public String switchCaseInput(String command) {
        if (isInLoginMenu) {
            HashMap<String, String> output = loginMenu.findCommand(command);
            if (output.containsKey("logined")) {
                isInLoginMenu = false;
                isInMainMenu = true;
                View.setCurrentMenu("Main Menu");
            }
        } else if (isInDeckCommands) {
        } else if (isInProfile) {

        } else if (isInScoreBoard) {

        } else if (isInShop) {
        } else if (isInGameManager) {
        }
        return null;
    }
}