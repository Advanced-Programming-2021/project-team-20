package project.view.pooyaviewpackage;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DuelStage extends Stage {
    private DuelView duelView;
    private boolean shiftKeyOn = false;

    DuelStage(){
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