package test.maven.controller.storage;

import com.google.gson.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import test.maven.model.User;
import test.maven.model.card.Card;

public class Storage {

    private ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<Card> allCards = new ArrayList<>();
    private String addressOfStorage = "E:\\programing\\JavaVSCode\\test.maven\\src\\main\\java\\test\\maven\\Resourses\\";

    public void Engine() {

        ArrayList<String> filenames = new ArrayList<>();
        File directory = new File(addressOfStorage);
        File[] contents = directory.listFiles();
        for (File f : contents) {
            filenames.add(f.getName() + "\\");
        }

    }

    private void addUsersToArrayList(ArrayList<String> filenames) throws FileNotFoundException {
        for (int i = 0; i < filenames.size(); i++) {

            FileReader fileReader = new FileReader(addressOfStorage + filenames.get(i) + ".json");
            Scanner myReader = new Scanner(fileReader);
            
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String informationOfUser = gson.toJson(myReader.nextLine());
           
            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(informationOfUser);
            myReader.close();
        }
    }
}
