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
    private static List<Integer> deletedTweets = new ArrayList<>();
    private static long lastTimeTweetsSavedInFile = 0l;
    private static long lastTimeTweetsDeleted = 0l;

    public static void startProgram() {

        File directory = new File(tweetsFolderPath);
        File[] contents = directory.listFiles();
        for (File f : contents) {
            try {
                Scanner scanner = new Scanner(f);
                StringBuilder fileInformation = new StringBuilder();
                while (scanner.hasNextLine()) {
                    fileInformation.append(scanner.nextLine());
                }
                addTweetToAllTweets(fileInformation.toString());
                scanner.close();
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

        tweet.put("id", getLastIdOfTweets() + "");
        addTweetToAllTweets(new Gson().toJson(tweet));
        if (System.currentTimeMillis() - lastTimeTweetsSavedInFile > 120000) {
            lastTimeTweetsSavedInFile = System.currentTimeMillis();
//            new Thread(() -> {
            saveTweetsInFile();
//            }).start();
        }
    }

    private static int getLastIdOfTweets() {
        Collections.sort(getAllTweets(), (o1, o2) -> o1.getId() - o2.getId());
        return allTweets.get(allTweets.size() - 1).getId() + 1;
    }

    public static void endProgram() {
        saveTweetsInFile();
        deleteTweetsFromStorage();
        System.exit(0);
    }

    private static void saveTweetsInFile() {
        for (int i = 0; i < allTweets.size(); i++) {
            File file = new File(tweetsFolderPath + "\\" + allTweets.get(i).getId() + ".json");
            if (!file.exists()) {
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(new File(tweetsFolderPath + "\\" + allTweets.get(i).getId() + ".json"));
                    fileWriter.append(allTweets.get(i).toGsonString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void deleteTweetsFromStorage() {
        for (int i = 0; i < deletedTweets.size(); i++) {
            File file = new File(tweetsFolderPath + "\\" + deletedTweets.get(i) + ".json");
            file.delete();
        }
    }

    public static void deleteTweetById(int id) {
        for (int i = 0; i < allTweets.size(); i++) {
            if (allTweets.get(i).getId() == id) {
                deletedTweets.add(allTweets.get(i).getId());
                allTweets.remove(i);
            }
        }
//        if (System.currentTimeMillis() - lastTimeTweetsDeleted > 120000) {
//            deleteTweetsFromStorage();
//        }
    }

    public static List<Integer> getDeletedTweets() {
        return deletedTweets;
    }

    public static List<Tweet> getAllTweets() {
        return allTweets;
    }
}
