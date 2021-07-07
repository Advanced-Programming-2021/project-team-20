package project.view.pooyaviewpackage;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DuelStage extends Stage {
    DuelStage(){
        DuelView duelView = new DuelView();
        AnchorPane anchorPane = duelView.getAnchorpaneAtBeginning(this);
        Scene scene = new Scene(anchorPane, 1200, 1000);
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                checkCheatCommands(keyEvent);
//            }
//        });
        this.setScene(scene);
        this.show();
    }
}
