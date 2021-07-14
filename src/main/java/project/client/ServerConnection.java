package project.client;

import com.google.gson.JsonObject;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import project.client.view.pooyaviewpackage.DuelView;
import project.client.view.pooyaviewpackage.JsonCreator;

import javax.swing.text.PlainDocument;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection {
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static Socket secondSocket;
    private static DataInputStream secondDataInputStream;
    private static DataOutputStream secondDataOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 12345);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            secondSocket = new Socket("localhost", 12346);
            secondDataInputStream = new DataInputStream(secondSocket.getInputStream());
            secondDataOutputStream = new DataOutputStream(secondSocket.getOutputStream());
            receiveDataFromServerAndGiveResults();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public static String sendDataToServerAndReceiveResult(String data) {
        try {
            dataOutputStream.writeUTF(data);
            dataOutputStream.flush();
            return dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }
    }


    public static void receiveDataFromServerAndGiveResults() {
        Thread secondThread = new Thread(() -> {
            long time = System.currentTimeMillis();
            while (true) {
                if (System.currentTimeMillis() - time > 2000) {
                    time = System.currentTimeMillis();
                    String output = "Is it my turn?";
                    System.out.println(output);
                    String whatServerGave = "";
                    try {
                        secondDataOutputStream.writeUTF(getResultSecondTime(output));
                        secondDataOutputStream.flush();
                        whatServerGave = secondDataInputStream.readUTF();
                        System.out.println("whatServerGave = "+whatServerGave);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!whatServerGave.isBlank() && !whatServerGave.startsWith("you haven't")) {
                        DuelView.setIsMyTurn(Boolean.parseBoolean(whatServerGave));
                        if (!DuelView.isIsMyTurn()) {
                            try {
                                secondDataOutputStream.writeUTF(getResultSecondTime("it's not my turn"));
                                secondDataOutputStream.flush();
                                String secondSocketInput = secondDataInputStream.readUTF();
                                System.out.println("secondSocketInput = " + secondSocketInput);
                                if (secondSocketInput.startsWith("adva")) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        secondThread.setDaemon(true);
        secondThread.start();
    }

    public static String getResultSecondTime(String string) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "duel");
        jsonObject.addProperty("token", DuelView.getToken());
        jsonObject.addProperty("firstAdditionalString", "");
        jsonObject.addProperty("integerString", "");
        jsonObject.addProperty("request", string);
        return jsonObject.toString();
    }

}
