package test.maven.controller;

import test.maven.controller.deckCommands.*;
import test.maven.controller.duel.*;
import test.maven.controller.loginMenu.*;
import test.maven.controller.profile.*;
import test.maven.controller.scoreboard.*;
import test.maven.controller.shop.*;
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
    private LoginMenuPatterns loginPattern = new LoginMenuPatterns();
    private DeckCommands deckCommands = new DeckCommands();
    private Profile profile = new Profile();
    private Scoreboard scoreboard = new Scoreboard();
    private Shop shop = new Shop();
    private GameManager gameManager = new GameManager();

    public String switchCaseInput(String command) {

        if (isInLoginMenu) {
            return loginMenu.findCommand(command);
        }
        if (isInDeckCommands) {
            deckCommands.getClass();
        } else if (isInProfile) {
            return profile.findCommands(command);
        } else if (isInScoreBoard) {
            return scoreboard.findCommands(command);
        } else if (isInShop) {
            return shop.findCommand(command);
        } else if (isInGameManager) {
            gameManager.getClass();
        }

        return null;
    }

    public void exitMenu() {

        if (isInMainMenu) {
            isInMainMenu = false;
            isInLoginMenu = true;
            View.setCurrentMenu("Login Menu");
            return;
        }
        if (isInDeckCommands) {
            isInDeckCommands = false;
        } else if (isInProfile) {
            isInProfile = false;
        } else if (isInShop) {
            isInShop = false;
        } else if (isInScoreBoard) {
            isInScoreBoard = false;
        } else if (isInGameManager) {
            isInGameManager = false;
        }

        isInMainMenu = true;
        View.setCurrentMenu("Main Menu");

    }

    public boolean enterMenu(String menuname) {

        if (menuname.equals("Deck")) {
            isInDeckCommands = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } else if (menuname.equals("Scoreboard")) {
            isInScoreBoard = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } else if (menuname.equals("Profile")) {
            isInProfile = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } else if (menuname.equals("Shop")) {
            isInShop = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } else if (menuname.equals("Duel")) {
            isInGameManager = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        }
        return false;
    }

}
