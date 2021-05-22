package controller.non_duel_tests;

import controller.non_duel.mainController.MainController;
import controller.non_duel.storage.Storage;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static controller.non_duel_tests.MainControllerTest.mainController;

public class LoginMenuTest {
    static Storage storage;
    static MainController mainController;

    @BeforeAll
    static void startTest() {
        storage = new Storage();
        mainController = MainController.getInstance();
        User user = new User("1", "1", "1");
        User user2 = new User("2", "2", "2");
        Storage.addUserToAllUsers(user);
        Storage.addUserToAllUsers(user2);
//        mainController.switchCaseInput("user login -u 1 -p 1 -n 1");
//        mainController.enterMenu("Profile");
    }


    @Test
    void findCommandTest() {

        Assertions.assertEquals("user with username 1 already exists",
            mainController.switchCaseInput("user create -u 1 -p 2 -n d"));

        Assertions.assertEquals("user with nickname 2 already exists",
            mainController.switchCaseInput("user create -u 11 -p 2 -n 2"));
    }

}
