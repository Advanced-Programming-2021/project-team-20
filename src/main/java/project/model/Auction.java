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
//    private static HashMap<String, Integer>
    private static String addressOfStorage = "Resourses\\Server\\";

    public Auction(String auctionCreatorName, int initialPrice) {
        this.auctionCreatorName = auctionCreatorName;
        this.price = initialPrice;
        this.bestBuyerName = "";
        this.auctionCode = calculateAuctionCode();
        this.isActivated = true;
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
    }

    private int calculateAuctionCode() {
        List<String> results = new ArrayList<String>();

        File[] files = new File("Resourses\\Server\\Auctions\\").listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

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
        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
        jsonObject.addProperty("price", auction.getPrice());
        jsonObject.addProperty("auctionCode", auction.getAuctionCode());
        jsonObject.addProperty("isActivated", auction.getIsActivated());
        return jsonObject.toString();
    }

    public String getAuctionCreatorName() {
        return this.auctionCreatorName;
    }

    public String getBestBuyerName(){
        return this.bestBuyerName;
    }

    public int getPrice(){
        return this.price;
    }

    public void setNewSuggestion(String username, int newPrice) {
        this.bestBuyerName = username;
        this.price = newPrice;
        //Change database
    }

    public int getAuctionCode(){
        return this.auctionCode;
    }

    private boolean getIsActivated(){
        return this.isActivated;
    }

    public String getAllAuctions() throws IOException {
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
                answer += details.get("auctionCreatorName").getAsString();
                answer += ",";
                answer += details.get("bestBuyerName").getAsString();
                answer += ",";
                answer += details.get("price").getAsString();
                answer += ",";
                answer += details.get("auctionCode").getAsString();
                answer += ",";
                answer += details.get("isActivated").getAsString();
                answer += ",";
            }
        }
        return answer;

    }
}
