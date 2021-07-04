package project.controller.non_duel_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import project.controller.non_duel.mainController.MainController;
import project.controller.non_duel.storage.Storage;
import project.model.User;

class ScoreboardTest {

    static Storage storage;
    static MainController mainController;

    @BeforeAll
    static void startProgram() {
        storage = new Storage();

        User user = new User("1", "1", "1");
        user.setScore(2);
        User user2 = new User("2", "2", "1");
        user2.setScore(1);
        User user3 = new User("3", "3", "1");
        user3.setScore(1);
        User user4 = new User("4", "4", "1");
        User user5 = new User("5", "5", "5");

        Storage.addUserToAllUsers(user);
        Storage.addUserToAllUsers(user2);
        Storage.addUserToAllUsers(user3);
        Storage.addUserToAllUsers(user4);
        Storage.addUserToAllUsers(user5);

        mainController = MainController.getInstance();
        mainController.switchCaseInput("user login -p 1 -u 1");
        mainController.enterMenu("Scoreboard");
    }

    @Test
    void scoreboardTest() {
        String output = "";
        output = mainController.switchCaseInput("scoreboard");
        assertEquals("invalid command!", output);
        output = mainController.switchCaseInput("scoreboard show");
        assertTrue(output.contains("1-1: 2\n2-2: 1\n2-3: 1\n4-4: 0\n4-5: 0"));

    }
}
