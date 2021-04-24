package test.maven.controller.storage;

import com.google.gson.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import test.maven.model.User;
import test.maven.model.card.Card;

public class Storage {

    private static ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<Card> allCards = new ArrayList<>();
    private String addressOfStorage = "Resourses\\";

    public void startProgram() {

        ArrayList<String> filenames = new ArrayList<>();
        File directory = new File(addressOfStorage);
        File[] contents = directory.listFiles();
        for (File f : contents) {
            filenames.add(f.getName() + "\\");
        }
        try {
            addUsersToArrayList(filenames);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void endProgram() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        for (int i = 0; i < allUsers.size(); i++) {
            File file = new File(addressOfStorage + allUsers.get(i).getName());
            file.mkdir();

            try {
                FileWriter fileWriter = new FileWriter(addressOfStorage + allUsers.get(i).getName() + "\\user.json");
                fileWriter.write(gson.toJson(allUsers.get(i)));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addUsersToArrayList(ArrayList<String> filenames) throws IOException {

        for (int i = 0; i < filenames.size(); i++) {

            FileReader fileReader = new FileReader(addressOfStorage + filenames.get(i) + ".json");
            Scanner myReader = new Scanner(fileReader);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String informationOfUser = gson.toJson(myReader.nextLine());

            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(informationOfUser);
            myReader.close();
            fileReader.close();

            if (rootNode.isJsonObject()) {

                JsonObject details = rootNode.getAsJsonObject();
                User user = new User(details.get("name").getAsString(), details.get("nickname").getAsString(),
                        details.get("password").getAsString());

                user.setScore(details.get("score").getAsInt());
                user.setMoney(details.get("money").getAsInt());

                allUsers.add(user);
            }
        }
    }

    public void addUserToAllUsers(User newUser) {
        allUsers.add(newUser);
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

}
