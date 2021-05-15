package controller.non_duel_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.non_duel.importAndExport.ImportAndExport;
import controller.non_duel.storage.Storage;

class ImportAndExportTest {

    static Storage storage;
    static ImportAndExport importAndExport;

    @BeforeAll
    static void startProgram() {

        storage = new Storage();
        try {
            storage.startProgram();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        importAndExport = new ImportAndExport();

    }

    @Test
    void importAndExportTest() {
        String output = "";
        output = importAndExport.findCommand("exportt card Suijin");
        assertEquals("invalid command!", output);
        output = importAndExport.findCommand("import card Suiijin");
        assertEquals("card does not exist!", output);
        output = importAndExport.findCommand("import card Suijin");
        assertEquals("card imported successfully!", output);
        output = importAndExport.findCommand("export card Suijiin");
        assertEquals("card does not exist!", output);
        output = importAndExport.findCommand("export card Suijin");
        assertEquals("card exported successfully!", output);
        importAndExport.findCommand("import card Monster Reborn");
        output = importAndExport.findCommand("export card Monster Reborn");
        assertEquals("card exported successfully!", output);
        importAndExport.findCommand("import card Trap Hole");
        output = importAndExport.findCommand("export card Trap Hole");
        assertEquals("card exported successfully!", output);

    }
}
