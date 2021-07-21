package project.client;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import project.client.view.AuctionPageController;
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
    private static boolean shouldContinueScoreboard = true;
    private static Socket fourthSocket;
    private static DataInputStream fifthDataInputStream;
    private static DataOutputStream fifthDataOutputStream;
    private static Thread readingFourthThread;
    private static Thread writingFourthThread;
    private static boolean shouldContinueAuctionRefresh = true;

    public static void initializeNetwork() {
        try {
//            socket = new Socket("localhost", 12345);
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
            fourthSocket = new Socket("localhost", 11122);
            fifthDataInputStream = new DataInputStream(fourthSocket.getInputStream());
            fifthDataOutputStream = new DataOutputStream(fourthSocket.getOutputStream());
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
        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }
    }

    public static String sendDataToServerAndReceiveResult2(String data) {
        try {
            secondDataOutputStream.writeUTF(data);
            secondDataOutputStream.flush();
            return secondDataInputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }
    }


    public static void scoreboardAutoRefresh(ScoreboardController scoreboardController) {
        writingThirdThread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (shouldContinueScoreboard) {
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        thirdDataOutputStream.writeUTF(ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformation());
                        thirdDataOutputStream.flush();
                        fourthDataOutputStream.writeUTF(ToGsonFormatToSendDataToServer.toGsonFormatGetScoreboardInformationOfONlineUsers());
                        fourthDataOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readingThirdThread = new Thread(() -> {
            try {
                while (shouldContinueScoreboard) {
                    String whatServerGave = thirdDataInputStream.readUTF();
                    String whatServerGave2 = fourthDataInputStream.readUTF();
                    System.out.println(whatServerGave);
                    System.out.println(whatServerGave2);
                    scoreboardController.fillLabelAutomatically(whatServerGave, whatServerGave2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writingThirdThread.setDaemon(shouldContinueScoreboard);
        readingThirdThread.setDaemon(shouldContinueScoreboard);
        writingThirdThread.start();
        readingThirdThread.start();
    }

    public static void stopScoreboard() {
        shouldContinueScoreboard = false;
    }

    public static void startAuction() {
        shouldContinueAuctionRefresh = true;
    }

    public static void auctionAutoRefresh(AuctionPageController auctionPageController) {
        writingFourthThread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (shouldContinueAuctionRefresh) {
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        fifthDataOutputStream.writeUTF(ToGsonFormatToSendDataToServer.toGsonFormatRefreshAuction());
                        fifthDataOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        readingFourthThread = new Thread(() -> {
            try {
                while (shouldContinueAuctionRefresh) {
                    String whatServerGave = fifthDataInputStream.readUTF();
                    System.out.println(whatServerGave);
                    auctionPageController.refreshTable(whatServerGave);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writingFourthThread.setDaemon(shouldContinueAuctionRefresh);
        readingFourthThread.setDaemon(shouldContinueAuctionRefresh);
        writingFourthThread.start();
        readingFourthThread.start();
    }

    public static void stopAuctionRefresh() {
        shouldContinueAuctionRefresh = false;
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

    private static String whatToWrite = "Is it my turn?";

    private static synchronized void setWhatToWrite(String string) {
        whatToWrite = string;
    }

    public static void receiveDataFromServerAndGiveResults() {
        Thread writingSecondThread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - time > 1000) {
                        time = System.currentTimeMillis();
                        //System.out.println("what second thread client is sending to server: " + whatToWrite);
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
                    //System.out.println("what server gave to second thread: *" + whatServerGave + "*");
                    if (whatServerGave.contains("do you want to ") || whatServerGave.contains("now it will be")) {
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
