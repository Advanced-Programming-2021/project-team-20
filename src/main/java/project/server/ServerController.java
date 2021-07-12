package project.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.User;
import project.server.controller.non_duel.loginMenu.LoginMenu;
import project.server.controller.non_duel.scoreboard.Scoreboard;
import project.server.controller.non_duel.profile.Profile;
import project.server.controller.non_duel.deckCommands.DeckCommands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerController {
    private static String badRequestFormat = "{\"type\":\"Error\",\"message\":\"Bad request format\"}";
    private static String userNotLogined = "{\"type\":\"Error\",\"message\":\"User Not Logined\"}";
    private static String error = "Error";
    private static String successful = "Successful";
    private static HashMap<String, User> loginedUsers = new HashMap<>();

    public static void runServer() {
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            while (true) {
                socket = serverSocket.accept();
                startNewThread(serverSocket, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startNewThread(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                getInputAndProcess(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Connection reset");
            }
        }).start();
    }

    private static void getInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
            throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            System.out.println("=============================================");
            System.out.println("message from client: " + input);
            String result = process(input);
            if (result.equals(""))
                break;
            System.out.println("message send to client: " + result);
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    private static String process(String input) {
        JsonParser parser = new JsonParser();
        JsonElement rootNode = parser.parse(input);
        try {
            if (rootNode.isJsonObject()) {
                JsonObject details = rootNode.getAsJsonObject();
                String type = details.get("type").getAsString();
                return findCommand(type, details);
            } else {
                return badRequestFormat;
            }
        } catch (Exception e) {
            return badRequestFormat;
        }

    }

    private static String findCommand(String type, JsonObject details) {
        switch (type) {
            case "register":
                return LoginMenu.registerUser(details);
            case "login":
                return LoginMenu.loginUser(details);
            case "scoreboard":
                return Scoreboard.findCommands("scoreboard show");
            case "deleteDeck":
                return DeckCommands.deleteDeck(details);
            case "createDeck":
                return DeckCommands.createDeck(details);
            case "addCardToUselessCards":
                return DeckCommands.addCardToAllUselessCards(details);
            case "deleteCardFromMainOrSideDeck":
                return DeckCommands.deleteCardFromMainOrSideDeck(details);
            case "deleteCardFromUselessCards":
                return DeckCommands.deleteCardFromAllUselessCards(details);
            case "addCardToMainOrSideDeck":
                return DeckCommands.addCardToMainOrSideDeck(details);
            case "activeDeck":
                return DeckCommands.activateDeck(details);    
            case "changePassword":
                return Profile.changePassword(details);
            case "changeNickName":
                return Profile.changeNickname(details);
            case "logout":
                return logoutUser(details);
            default:
                return badRequestFormat;
        }
    }

    private static String logoutUser(JsonObject details) {
        String token = "";
        try {
            token = details.get("token").getAsString();
        } catch (Exception e) {
            System.out.println("Exception in logoutUser");
        }
        if (loginedUsers.containsKey(token)) {
            loginedUsers.remove(token);
        }
        return "null";
    }

    public static User getUserByToken(String token) {
        if (loginedUsers.containsKey(token)) {
            return loginedUsers.get(token);
        }
        return null;
    }

    public static String getBadRequestFormat() {
        return badRequestFormat;
    }

    public static String getError() {
        return error;
    }

    public static String getSuccessful() {
        return successful;
    }

    public static HashMap<String, User> getLoginedUsers() {
        return loginedUsers;
    }

    public static String getUserNotLogined() {
        return userNotLogined;
    }

    public static void setLoginedUser(String string, User user) {
        loginedUsers.put(string, user);
    }
}
