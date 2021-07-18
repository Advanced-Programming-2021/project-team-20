package project.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import project.model.User;
import project.server.controller.duel.GamePackage.ChangeCardsBetweenTwoRounds;
import project.server.controller.duel.PreliminaryPackage.ClientMessageReceiver;
import project.server.controller.duel.PreliminaryPackage.DuelStarter;
import project.server.controller.non_duel.deckCommands.DeckCommands;
import project.server.controller.non_duel.loginMenu.LoginMenu;
import project.server.controller.non_duel.profile.Profile;
import project.server.controller.non_duel.scoreboard.Scoreboard;
import project.server.controller.non_duel.shop.Shop;
import project.server.controller.non_duel.tweets.TweetController;

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
        Socket secondSocket = null;
        Socket thirdSocket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            ServerSocket secondServerSocket = new ServerSocket(12346);
            ServerSocket thirdServerSocket = new ServerSocket(12356);
            while (true) {
                socket = serverSocket.accept();
                startNewThread(serverSocket, socket);
                secondSocket = secondServerSocket.accept();
                startNewDuelThread(secondServerSocket, secondSocket);
                thirdSocket = thirdServerSocket.accept();
                startNewDuelThread1(thirdServerSocket, thirdSocket);
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

    private static void startNewDuelThread(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                getDuelInputAndProcess(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Connection reset");
            }
        }).start();
    }

    private static void getDuelInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
        throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            System.out.println("=============================================");
            System.out.println("message from client: " + input);
            String result = processDuel(input);
            //  if (result.equals(""))
            //      break;
            System.out.println("message send to client: " + result);
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }

    private static String processDuel(String input) {
        JsonParser parser = new JsonParser();
        String[] inputs = input.split("\n");
        String output = "";
        for (int i = 0; i < inputs.length; i++) {
            JsonElement rootNode = parser.parse(inputs[i]);
            try {
                if (rootNode.isJsonObject()) {
                    JsonObject details = rootNode.getAsJsonObject();
                    String type = details.get("type").getAsString();
                    output += findCommand(type, details);
                } else {
                    output += badRequestFormat;
                }
                output += "\n";
            } catch (Exception e) {
                output += badRequestFormat;
                output += "\n";
            }
        }
        return output;
    }


    private static synchronized void startNewDuelThread1(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                getDuelInputAndProcess1(dataInputStream, dataOutputStream);
                dataInputStream.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Connection reset");
            }
        }).start();
    }

    private static synchronized void getDuelInputAndProcess1(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
        throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            System.out.println("=============================================");
            System.out.println("message from client: " + input);

            String result = null;

            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(input);
            try {
                if (rootNode.isJsonObject()) {
                    JsonObject details = rootNode.getAsJsonObject();
                    String type = details.get("type").getAsString();
                    if (type.equals("scoreboard")) {
                        result = Scoreboard.findCommands("scoreboard show");
                    }
                    else if (type.equals("scoreboardOnline")){
                        result = Scoreboard.findCommands("scoreboardOnline");
                    }
                } else {
                    result = "ERROR";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "ERROR";
            }

            System.out.println("message send to client: " + result);
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        }
    }
//
//    private static void secondStartNewThread(ServerSocket serverSocket, Socket socket) {
//        new Thread(() -> {
//            try {
//                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                giveInputAndProcess(dataInputStream, dataOutputStream);
//                dataInputStream.close();
//                socket.close();
//                serverSocket.close();
//            } catch (IOException e) {
//                System.out.println("Connection reset");
//            }
//        }).start();
//    }
//
//    private static void giveInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
//        throws IOException {
//        while (true) {
//            String input = dataInputStream.readUTF();
//            System.out.println("=============================================");
//            System.out.println("message from client: " + input);
//            String result = process(input);
//            if (result.equals(""))
//                break;
//            System.out.println("message send to client: " + result);
//            dataOutputStream.writeUTF(result);
//            dataOutputStream.flush();
//        }
//    }

    private static void getInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
        throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            System.out.println("=============================================");
            System.out.println("message from client: " + input);
            String result = process(input);
//            if (result.equals(""))
//                break;
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
            e.printStackTrace();
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
            case "scoreboardOnline":
                return Scoreboard.findCommands("scoreboardOnline");
            case "shopBuy":
                return Shop.buyRequestFromClient(details);
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
            case "requestDuel":
                return DuelStarter.requestGame(details);
            case "setTurnOfDuel":
                return DuelStarter.setTurnOfGame(details);
            case "sendTweet":
                return TweetController.sendTweet(details);
            case "getLastTweets":
                return TweetController.getLastTweets(details);
            case "changeCardsBetweenTwoRounds":
                return ChangeCardsBetweenTwoRounds.getInputFromClientAndProcessIt(details);
            case "cheat":
            case "logout":
                return logoutUser(details);
            case "duel":
                return ClientMessageReceiver.findCommands(details);
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

}
