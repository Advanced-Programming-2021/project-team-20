package project.server.controller.non_duel.storage;

import java.io.File;
import java.util.HashMap;

public class TweetStorage {

    private static String tweetsFolderPath = "Resourses\\Tweest";
    private static HashMap<String, String> allTweets = new HashMap<>();

    public static void startProgram() {
        File directory = new File(tweetsFolderPath);
        File[] contents = directory.listFiles();
        for (File f : contents) {
            
        }
    }
}
