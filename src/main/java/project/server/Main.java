package project.server;
import project.server.controller.non_duel.storage.Storage;
import project.server.controller.non_duel.storage.TweetStorage;

import java.util.Scanner;

public class Main {
    private static Storage storage = new Storage();

    public static void main(String[] args) throws Exception {
        storage.startProgram();
        TweetStorage.startProgram();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input.equals("end")) {
                    try {
                        storage.endProgram();
                        TweetStorage.endProgram();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        ServerController.runServer();
    }
}
