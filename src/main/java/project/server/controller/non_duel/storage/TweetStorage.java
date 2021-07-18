package project.server.controller.non_duel.storage;

import com.google.gson.*;
import project.model.Tweet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class TweetStorage {

    private static String tweetsFolderPath = "Resourses\\Server\\Tweets";
    private static List<Tweet> allTweets = new ArrayList<>();
    private static long lastTimeTweetsSavedInFile = 0l;
    private static int numberOfTweetsSavedInFile;

    public static void startProgram() {

        File directory = new File(tweetsFolderPath);
        File[] contents = directory.listFiles();
        numberOfTweetsSavedInFile = contents.length;
        for (File f : contents) {
            try {
                Scanner scanner = new Scanner(f);
                StringBuilder fileInformation = new StringBuilder();
                while (scanner.hasNextLine()) {
                    fileInformation.append(scanner.nextLine());
                }
                addTweetToAllTweets(fileInformation.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void addTweetToAllTweets(String gsonInformation) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(gsonInformation);
        JsonObject details = jsonElement.getAsJsonObject();
        Tweet tweet = new Tweet(details.get("id").getAsInt(), details.get("author").getAsString(), details.get("message").getAsString());
        allTweets.add(tweet);
    }

    public static void sendTweet(HashMap<String, String> tweet) {

        tweet.put("id", allTweets.size() + "");
        addTweetToAllTweets(new Gson().toJson(tweet));
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
                fileWriter.append(allTweets.get(i).toGsonString());
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Tweet> getAllTweets() {
        return allTweets;
    }
}
