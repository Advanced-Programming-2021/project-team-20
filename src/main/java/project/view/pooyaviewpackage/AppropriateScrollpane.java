package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppropriateScrollpane extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        String wholeDescription = "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n\n\n\n\n\n\n\n\n";
//        String wholeDescription = "dfkjsdfjskdfsd fsdjfksdjfs dfskjdf sjd fn sdfsjdfnksdfs dfkjsdfnjskdf sdfjjsd kfsdjf" +
//            "sdklfnsdfjk sdfkjsdflwefd qwfpqwiofjndfnsd vsdv jsdmv skdjvsldjkfs dfjskdfjsbf sdjfskdf sdfsjdfkenfjksbdf sf";
        List<String> shortDescription = new ArrayList<>();
        shortDescription = Arrays.asList(wholeDescription.split(" "));
        StringBuilder sentencesForEachLabel = new StringBuilder();
        int numberOFLabelUsed = 0;
        Label label;
        for (int i = 0; i < shortDescription.size(); i++) {
            label = new Label();
            label.setFont(new Font(22));
            if (sentencesForEachLabel.length() <= 23 - shortDescription.get(i).length()) {
                sentencesForEachLabel.append(shortDescription.get(i) + " ");
                label.setLayoutY(20 * (numberOFLabelUsed + 1));
                numberOFLabelUsed++;
            } else {
                while (sentencesForEachLabel.length() <= 22) {
                    sentencesForEachLabel.append(" ");
                }
                label.setText(sentencesForEachLabel.toString());
                label.setLayoutY(20 * (numberOFLabelUsed + 1));
                vBox.getChildren().add(label);
                sentencesForEachLabel.setLength(0);
                numberOFLabelUsed++;
            }
        }
        while (sentencesForEachLabel.length() <= 30) {
            sentencesForEachLabel.append(" ");
        }
        label = new Label();
        label.setFont(new Font(22));
        label.setText(sentencesForEachLabel.toString());
        ScrollPane scrollPaneForInformation = new ScrollPane();
        scrollPaneForInformation.setPannable(true);
        scrollPaneForInformation.setFitToHeight(true);
        scrollPaneForInformation.setFitToWidth(true);
       // scrollPaneForInformation.setLayoutY(300);
        scrollPaneForInformation.setMinHeight(100);
        scrollPaneForInformation.setMaxHeight(100);
       // scrollPaneForInformation.setLayoutX(300);
        scrollPaneForInformation.setMinWidth(250);
        scrollPaneForInformation.setMaxWidth(250);
        scrollPaneForInformation.setContent(vBox);

        anchorPane.getChildren().add(scrollPaneForInformation);
        Scene scene = new Scene(anchorPane, 250, 100);
        stage.setScene(scene);
        stage.show();

    }
}
