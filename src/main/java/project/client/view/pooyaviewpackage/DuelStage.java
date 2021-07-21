package project.client.view.pooyaviewpackage;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DuelStage extends Stage {
    private DuelView duelView;
    private boolean shiftKeyOn = false;
    private static Thread notMyTurnThread;

    DuelStage() {
        duelView = new DuelView();
        AnchorPane anchorPane = duelView.getAnchorpaneAtBeginning(this);
        Scene scene = new Scene(anchorPane, 1200, 1000);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                duelView.checkCheatCommands(keyEvent);
            }
        });
        this.setScene(scene);
        this.show();
//        notMyTurnThread = new Thread(() -> {
//            while (true) {
//                final String[] output = {JsonCreator.getResult("Is it my turn?")};
//                System.out.println(output[0]);
//                DuelView.setIsMyTurn(Boolean.parseBoolean(output[0]));
//                if (!DuelView.isIsMyTurn()) {
//                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
//                    pauseTransition.play();
//                    pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent actionEvent) {
//                            output[0] = JsonCreator.getResult("it's not my turn");
//                            if (output[0].startsWith("adva")) {
//                                Platform.runLater(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
//                                    }
//                                });
//                            } else if (!output[0].isBlank()) {
//                                Platform.runLater(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        notMyTurnThread.setDaemon(true);
//        notMyTurnThread.start();
    }

    public boolean isShiftKeyOn() {
        return shiftKeyOn;
    }

    public void setShiftKeyOn(boolean shiftKeyOn) {
        this.shiftKeyOn = shiftKeyOn;
    }

    public DuelView getDuelView() {
        return duelView;
    }
}
