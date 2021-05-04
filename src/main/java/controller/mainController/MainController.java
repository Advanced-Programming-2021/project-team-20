package controller.mainController;

import controller.deckCommands.DeckCommands;
import controller.duel.PreliminaryPackage.GameManager;
import controller.loginMenu.LoginMenu;
import controller.profile.Profile;
import controller.scoreboard.Scoreboard;
import controller.shop.Shop;
import view.View;

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
            String resultOfLoginUser = loginMenu.findCommand(command);
            if (resultOfLoginUser.equals("user logged in successfully!")) {
                View.setCurrentMenu("Main Menu");
                isInLoginMenu = false;
            }
            return resultOfLoginUser;
        }
        if (isInDeckCommands) {
            return deckCommands.findCommands(command);
        } else if (isInProfile) {
            return profile.findCommands(command);
        } else if (isInScoreBoard) {
            return scoreboard.findCommands(command);
        } else if (isInShop) {
            return shop.findCommand(command);
        } else if (isInGameManager) {
            gameManager.getClass();
        }

        return "invalid commend!";
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
