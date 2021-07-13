package project.server;

import java.util.Scanner;

import project.server.controller.non_duel.storage.Storage;

public class Main {
    private static Storage storage = new Storage();

    public static void main(String[] args) throws Exception {
        storage.startProgram();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("end")) {
                try {
                    storage.endProgram();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }).start();
        ServerController.runServer();
    }
}
