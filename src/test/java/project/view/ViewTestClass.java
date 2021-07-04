package project.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewTestClass {

    View view;

    @BeforeAll
    void startView() {
        view = new View();
        View.setCurrentMenu("project.Main Menu");
    }

    @Test
    void currentMenuTest() {
        assertEquals("project.Main Menu", View.getCurrentMenu());
    }
}
