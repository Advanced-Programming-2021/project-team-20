package project.server;

import project.server.controller.non_duel.storage.Storage;

public class Main {
    public static void main(String[] args) throws Exception {
        Storage storage = new Storage();
        storage.startProgram();
        ServerController.runServer();
    }
}
