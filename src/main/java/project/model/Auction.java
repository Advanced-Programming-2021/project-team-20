package project.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Auction {
    private String auctionCreatorName;
    private int price;
    private String bestBuyerName;
    private int auctionCode;
    private boolean isActivated;
    private String cardName;
    private int timeLeftAsSeconds;
    //    private static HashMap<String, Integer>
    private static String addressOfStorage = "Resourses\\Server\\";

    public Auction(String auctionCreatorName, int initialPrice, String cardName) {
        this.auctionCreatorName = auctionCreatorName;
        this.price = initialPrice;
        this.bestBuyerName = "null";
        this.auctionCode = calculateAuctionCode();
        this.isActivated = true;
        this.cardName = cardName;
        this.timeLeftAsSeconds = 60;
        calculateAuctionCode();

        try {
            FileWriter fileWriter = new FileWriter(
                addressOfStorage + "Auctions\\" + getAuctionCode() + ".json");
            fileWriter.write(toGsonFormat(this));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }


        startTimeDecreasing(this);


    }

    public static String getCardNameByAuctionCode(int parseInt) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(addressOfStorage + "Auctions\\" + parseInt + ".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner myReader = new Scanner(fileReader);
        String informationOfUser = myReader.nextLine();
        JsonParser parser = new JsonParser();
        JsonElement rootNode = parser.parse(informationOfUser);
        myReader.close();
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (rootNode.isJsonObject()) {
            JsonObject details = rootNode.getAsJsonObject();
            return details.get("cardName").getAsString();
        }
        return null;
    }

    public static String getBuyerNameByAuctionCode(String auctionCode) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(addressOfStorage + "Auctions\\" + auctionCode + ".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner myReader = new Scanner(fileReader);
        String informationOfUser = myReader.nextLine();
        JsonParser parser = new JsonParser();
        JsonElement rootNode = parser.parse(informationOfUser);
        myReader.close();
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (rootNode.isJsonObject()) {
            JsonObject details = rootNode.getAsJsonObject();
            return details.get("bestBuyerName").getAsString();
        }
        return null;
    }

    private void startTimeDecreasing(Auction auction) {
        new Thread(() -> {
            for (int i = 0; i < 59; i++) {
                try {
                    Thread.sleep(1000);
                    decreaseTimeByOne(auction);
                    auction.timeLeftAsSeconds = auction.getTimeLeftAsSeconds() - 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            auction.isActivated = false;
            decreaseTimeByOne(auction);
        }).start();
    }

    private static void decreaseTimeByOne(Auction auction) {
        try {
            FileWriter fileWriter = new FileWriter(
                addressOfStorage + "Auctions\\" + auction.getAuctionCode() + ".json");
            fileWriter.write(toGsonFormatForSecond(auction));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static String toGsonFormatForSecond(Auction auction) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auctionCode", auction.getAuctionCode());
        jsonObject.addProperty("cardName", auction.getCardName());
        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
        jsonObject.addProperty("price", auction.getPrice());
        jsonObject.addProperty("isActivated", auction.getIsActivated());
        jsonObject.addProperty("timeLeftAsSeconds", auction.getTimeLeftAsSeconds() - 1);
        return jsonObject.toString();
    }

    private int calculateAuctionCode() {
        List<String> results = new ArrayList<String>();
        File[] files = new File("Resourses\\Server\\Auctions\\").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        int answer = 1;
        for (String result : results) {
            answer++;
        }
        return answer;
    }

    private String toGsonFormat(Auction auction) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auctionCode", auction.getAuctionCode());
        jsonObject.addProperty("cardName", auction.getCardName());
        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
        jsonObject.addProperty("price", auction.getPrice());
        jsonObject.addProperty("isActivated", auction.getIsActivated());
        jsonObject.addProperty("timeLeftAsSeconds", auction.getTimeLeftAsSeconds());
        return jsonObject.toString();
    }

    private String getCardName() {
        return this.cardName;
    }

    public String getAuctionCreatorName() {
        return this.auctionCreatorName;
    }

    public String getBestBuyerName() {
        return this.bestBuyerName;
    }

    public int getPrice() {
        return this.price;
    }

    public int getTimeLeftAsSeconds() {
        return this.timeLeftAsSeconds;
    }

    public void setNewSuggestion(String username, int newPrice) {
        this.bestBuyerName = username;
        this.price = newPrice;
        //Change database
    }

    public int getAuctionCode() {
        return this.auctionCode;
    }

    private boolean getIsActivated() {
        return this.isActivated;
    }

    public static String getAllAuctions() throws IOException {
        String answer = "";

        List<String> allFileNames = new ArrayList<String>();

        File[] files = new File("Resourses\\Server\\Auctions\\").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                allFileNames.add(file.getName());
            }
        }

        for (int i = 0; i < allFileNames.size(); i++) {

            FileReader fileReader = new FileReader(addressOfStorage + "Auctions\\" + allFileNames.get(i));

            Scanner myReader = new Scanner(fileReader);
            String informationOfUser = myReader.nextLine();
            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(informationOfUser);
            myReader.close();
            fileReader.close();

            if (rootNode.isJsonObject()) {
                JsonObject details = rootNode.getAsJsonObject();
                answer += details.get("auctionCode").getAsString();
                answer += ",";
                answer += details.get("cardName").getAsString();
                answer += ",";
                answer += details.get("auctionCreatorName").getAsString();
                answer += ",";
                answer += details.get("bestBuyerName").getAsString();
                answer += ",";
                answer += details.get("price").getAsString();
                answer += ",";
                answer += details.get("isActivated").getAsString();
                answer += ",";
                answer += details.get("timeLeftAsSeconds").getAsString();
                answer += ",";
            }
        }
        return answer;

    }
}
