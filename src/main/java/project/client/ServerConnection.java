package project.client;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import project.client.view.ScoreboardController;
import project.client.view.pooyaviewpackage.DuelView;

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
    private static Socket thirdSocket;
    private static DataInputStream thirdDataInputStream;
    private static DataOutputStream thirdDataOutputStream;
    private static DataInputStream fourthDataInputStream;
    private static DataOutputStream fourthDataOutputStream;
    private static Thread readingThirdThread;
    private static Thread writingThirdThread;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 12345);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            secondSocket = new Socket("localhost", 12346);
            secondDataInputStream = new DataInputStream(secondSocket.getInputStream());
            secondDataOutputStream = new DataOutputStream(secondSocket.getOutputStream());
            thirdSocket = new Socket("localhost", 12356);
            thirdDataInputStream = new DataInputStream(thirdSocket.getInputStream());
            thirdDataOutputStream = new DataOutputStream(thirdSocket.getOutputStream());
            fourthDataInputStream = new DataInputStream(thirdSocket.getInputStream());
            fourthDataOutputStream = new DataOutputStream(thirdSocket.getOutputStream());
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

    private static String whatToWrite = "Is it my turn?";


    private static synchronized void setWhatToWrite(String string) {
        whatToWrite = string;
    }


    public static void scoreboardAutoRefresh(ScoreboardController scoreboardController) {
        writingThirdThread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        thirdDataOutputStream.writeUTF(ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformation());
//                        fourthDataOutputStream.writeUTF(ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformationOfONlineUsers());
                        thirdDataOutputStream.flush();
//                        fourthDataOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readingThirdThread = new Thread(() -> {
            try {
                while (true) {
                    String whatServerGave = thirdDataInputStream.readUTF();
//                    String whatServerGave2 = fourthDataInputStream.readUTF();
                    System.out.println(whatServerGave);
//                    System.out.println(whatServerGave2);
                    scoreboardController.fillLabelAutomatically(whatServerGave);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writingThirdThread.setDaemon(true);
        readingThirdThread.setDaemon(true);
        writingThirdThread.start();
        readingThirdThread.start();
    }

    public static void stopScoreboard(){
        writingThirdThread.setDaemon(false);
        readingThirdThread.setDaemon(false);
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

    public static void receiveDataFromServerAndGiveResults() {
        Thread writingSecondThread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        //      System.out.println("what second thread client is sending to server: "+whatToWrite);
                        secondDataOutputStream.writeUTF(getResultSecondTime(whatToWrite) + "\n");
                        secondDataOutputStream.flush();
                    }
                }
                //whatToWrite = "Is it my turn?";
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread readingSecondThread = new Thread(() -> {
            try {
                while (true) {
                    String whatServerGave = secondDataInputStream.readUTF();
                    // System.out.println("what server gave to second thread: *" + whatServerGave+"*");
                    if (whatServerGave.contains("do you want to ")||whatServerGave.contains("now it will be")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(whatServerGave);
                            }
                        });
                    } else if (whatServerGave.startsWith("adva") || whatServerGave.startsWith("call you")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                            }
                        });
                    } else if (!whatServerGave.isBlank() && !whatServerGave.startsWith("you haven't")) {
                        //   System.out.println("This is parsing "+Boolean.parseBoolean(whatServerGave)+" please don't care");
                        if (!DuelView.isIsMyTurn() && whatServerGave.startsWith("true")) {
                            DuelView.setIsMyTurn(true);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                }
                            });
                        } else if (DuelView.isIsMyTurn() && whatServerGave.startsWith("true")) {
                            DuelView.setIsMyTurn(true);
                        } else if (whatServerGave.startsWith("false")) {
                            DuelView.setIsMyTurn(false);
                        }
                        if (!DuelView.isIsMyTurn()) {
                            setWhatToWrite("it's not my turn");
                        } else {
                            setWhatToWrite("Is it my turn?");
                        }
                    } else {
                        setWhatToWrite("Is it my turn?");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writingSecondThread.setDaemon(true);
        readingSecondThread.setDaemon(true);
        writingSecondThread.start();
        readingSecondThread.start();

    }

}
