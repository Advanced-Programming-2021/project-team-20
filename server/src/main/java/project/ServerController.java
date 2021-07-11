package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.google.gson.*;

import project.controller.loginMenu.LoginMenu;
import project.model.User;

public class ServerController {
    private static String badRequestFormat = "Bad request format";
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
                e.printStackTrace();
            }
        }).start();
    }

    private static void getInputAndProcess(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
            throws IOException {
        while (true) {
            String input = dataInputStream.readUTF();
            String result = process(input);
            if (result.equals(""))
                break;
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
            default:
                return badRequestFormat;
        }
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
}
