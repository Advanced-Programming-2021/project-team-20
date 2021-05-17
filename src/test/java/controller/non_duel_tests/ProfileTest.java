package controller.non_duel_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.non_duel.mainController.MainController;
import controller.non_duel.profile.Profile;
import controller.non_duel.storage.Storage;
import model.User;

class ProfileTest {
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
        mainController.switchCaseInput("user login -u 1 -p 1 -n 1");
        mainController.enterMenu("Profile");
    }

    @Test
    void changeNicknameTest() {
        String output = "";
        output = mainController.switchCaseInput("123");
        assertEquals("invalid command!", output);
        output = mainController.switchCaseInput("profile change -n 2");
        assertEquals("user with nickname 2 already exists", output);
        output = mainController.switchCaseInput("profile change -n 3");
        assertEquals("nickname changed successfully!", output);
        assertEquals("3", Storage.getUserByName("1").getNickname());
    }

    @Test
    void changePasswordTest() {
        String output = "";
        output = mainController.switchCaseInput("profile change -p -c 2 -n 1");
        assertEquals("current password is invalid", output);
        output = mainController.switchCaseInput("profile change -p -n 1 -c 1");
        assertEquals("please enter a new password", output);
        output = mainController.switchCaseInput("profile change -n 2 -p -c 1");
        assertEquals("password changed successfully!", output);
        output = mainController.switchCaseInput("profile change -c 2 -n 1 -p");
        assertEquals("password changed successfully!", output);
        output = mainController.switchCaseInput("profile change -c 1 -p -n 2");
        assertEquals("password changed successfully!", output);
        output = mainController.switchCaseInput("profile change -n 1 -c 2 -p");
        assertEquals("password changed successfully!", output);
        assertEquals("1", Storage.getUserByName("1").getPassword());

    }

    @Test
    void getOnlineUserTest(){
        assertEquals("1" ,Profile.getOnlineUser().getName());
    }
}
