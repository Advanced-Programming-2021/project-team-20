package controller.non_duel.mainController;

import controller.duel.StartDuel;
import controller.duel.PreliminaryPackage.GameManager;
import controller.non_duel.deckCommands.DeckCommands;
import controller.non_duel.importAndExport.ImportAndExport;
import controller.non_duel.loginMenu.LoginMenu;
import controller.non_duel.profile.Profile;
import controller.non_duel.scoreboard.Scoreboard;
import controller.non_duel.shop.Shop;
import view.View;

public class MainController {

    private static MainController mainController;

    private boolean isInLoginMenu = true;
    private boolean isInMainMenu;
    private boolean isInDeckCommands;
    private boolean isInScoreBoard;
    private boolean isInShop;
    private boolean isInProfile;
    private boolean isInImportAndExport;
    private boolean isInDuelMenu;

    private LoginMenu loginMenu = new LoginMenu();
    private DeckCommands deckCommands = new DeckCommands();
    private Profile profile = new Profile();
    private Scoreboard scoreboard = new Scoreboard();
    private Shop shop = new Shop();
    private ImportAndExport importAndExport = new ImportAndExport();
    private StartDuel duel = new StartDuel();

    public static MainController getInstance() {
        if (mainController == null) {
            mainController = new MainController();
        }

        return mainController;
    }

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
        } else if (isInImportAndExport) {
            return importAndExport.findCommand(command);
        } else if (isInDuelMenu) {
            return duel.findCommand(command);
        }

        return "invalid command!";
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
        } else if (isInImportAndExport) {
            isInImportAndExport = false;
        } else if (isInDuelMenu) {
            isInDuelMenu = false;
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
            isInDuelMenu = true;
            isInMainMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } else if (menuname.equals("ImportAndExport")) {
            isInImportAndExport = true;
            isInLoginMenu = false;
            View.setCurrentMenu(menuname);
            return true;
        } 
        return false;
    }
}
