package project.server.controller.non_duel.storage;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class TweetStorage {

    private static String tweetsFolderPath = "Resourses\\Tweets";
    private static List<HashMap<String, String>> allTweets = new ArrayList<>();
    private static long lastTimeTweetsSavedInFile = 0l;
    private static int numberOfTweetsSavedInFile;

    public static void startProgram() {

//        File directory = new File(tweetsFolderPath);
//        File[] contents = directory.listFiles();
//        numberOfTweetsSavedInFile = contents.length;
//        for (File f : contents) {
//            try {
//                Scanner scanner = new Scanner(f);
////                allTweets.add(scanner.nextLine());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static void sendTweet(HashMap<String, String> tweet) {

        tweet.put("id", allTweets.size() + "");
        System.out.println(tweet.toString());
        allTweets.add(tweet);
        if (System.currentTimeMillis() - lastTimeTweetsSavedInFile > 120000) {
            lastTimeTweetsSavedInFile = System.currentTimeMillis();
            new Thread(() -> {
                saveTweetsInFile();
            }).start();
        }
    }

    public static void endProgram() {
        saveTweetsInFile();
    }

    private static void saveTweetsInFile() {
        for (int i = numberOfTweetsSavedInFile; i < allTweets.size(); i++) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(new File(tweetsFolderPath + "\\" + i + ".json"));
                Gson gson = new Gson();
                fileWriter.append(gson.toJson(allTweets.get(i)));
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<HashMap<String, String>> getAllTweets() {
        return allTweets;
    }
}
