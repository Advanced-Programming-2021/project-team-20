package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import project.model.modelsforview.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.WHITE;

public class GraveyardScene extends Application{
    private static int indexOfChosenCardInGraveyard = 0;

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        String wholeGraveyardString = DuelView.getGraveyardString();
        boolean isAllySeeing = DuelView.isIsAllySeeingGraveyard();
        String graveyardUselessString = "first player graveyard:\n(.+)\nsecond player graveyard:\n(.+)";
        Pattern graveyardPattern = Pattern.compile(graveyardUselessString);
        Matcher graveyardMatch = graveyardPattern.matcher(wholeGraveyardString);
        ArrayList<String> cardNames = new ArrayList<>();
        ArrayList<Label> hBoxes = new ArrayList<>();
        if (graveyardMatch.find()) {
            System.out.println(graveyardMatch.group(1) + "\n.\n" + graveyardMatch.group(2));
            int index = 1;
            boolean hope = true;
            while (hope) {
                String indexString = index + ". (.+):(.+)";
                Pattern indexPattern = Pattern.compile(indexString);
                Matcher indexMatch = indexPattern.matcher(graveyardMatch.group((DuelView.isIsAllySeeingGraveyard() ? 1 : 2)));
                if (indexMatch.find()) {
                    cardNames.add(indexMatch.group(1));
                    System.out.println(indexMatch.group(1) + " is in graveyard and second is " + indexMatch.group(2));
                } else {
                    hope = false;
                }
                index++;
            }
            ScrollPane scrollPaneForInformation = new ScrollPane();
            scrollPaneForInformation.setPannable(true);
            scrollPaneForInformation.setFitToHeight(true);
            scrollPaneForInformation.setFitToWidth(true);
            // scrollPaneForInformation.setLayoutY(300);
            scrollPaneForInformation.setMinHeight(CardView.getCardHeight()*3);
            scrollPaneForInformation.setMaxHeight(CardView.getCardHeight()*3);
            // scrollPaneForInformation.setLayoutX(300);
            scrollPaneForInformation.setMinWidth(CardView.getCardWidth()*5);
            scrollPaneForInformation.setMaxWidth(CardView.getCardWidth()*5);
            Group group = new Group();
            scrollPaneForInformation.setContent(group);

            anchorPane.getChildren().add(scrollPaneForInformation);
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.show();
            for (int i = 0; i < cardNames.size(); i++) {
                Rectangle rectangle = new Rectangle(90 * (i % 5), 131 * (Math.floor(i / 5)), 90, 131);
                try {
                    rectangle.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardNames.get(i) + ".jpg").toExternalForm())));
                } catch (Exception e) {
                    rectangle.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardNames.get(i) + ".jpg").toExternalForm())));
                }
                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        for (int i = 0; i < group.getChildren().size(); i++){
                            if (group.getChildren().get(i).equals(rectangle)){
                                indexOfChosenCardInGraveyard = i;
                                if (DuelView.isIsClassWaitingForUserToChooseCardFromGraveyard()){
                                    stage.close();
                                }
                            }
                        }
                    }
                });
                group.getChildren().add(rectangle);
            }
            if (cardNames.size() < 15){
                for (int i = 0; i < 15 - cardNames.size(); i++){
                    Rectangle rectangle = new Rectangle(90 * ((i+cardNames.size()) % 5), 131 * (Math.floor((i+cardNames.size()) / 5)), 90, 131);
                    rectangle.setFill(WHITE);
                    group.getChildren().add(rectangle);
                }
            }


        }

//        String nowItWillBeTurn = "";
//        System.out.println("WHOUUUUUUU");
//        if (firstPlayerMatch.find()) {
//            nowItWillBeTurn = .substring(firstPlayerMatch.start(), firstPlayerMatch.end());
//            // System.out.println("Found love at index "+ match.start() +" - "+ (match.end()-1));
//        }
//        //    String wholeDescription = "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n\n\n\n\n\n\n\n\n";
//        String wholeDescription = "dfkjsdfjskdfsd fsdjfksdjfs dfskjdf sjd fn sdfsjdfnksdfs dfkjsdfnjskdf sdfjjsd kfsdjf" +
//            "sdklfnsdfjk sdfkjsdflwefd qwfpqwiofjndfnsd vsdv jsdmv skdjvsldjkfs dfjskdfjsbf sdjfskdf sdfsjdfkenfjksbdf sf";
//        List<String> shortDescription = new ArrayList<>();
//        shortDescription = Arrays.asList(wholeDescription.split(" "));
//        StringBuilder sentencesForEachLabel = new StringBuilder();
//        int numberOFLabelUsed = 0;
//        Label label;
//        for (int i = 0; i < shortDescription.size(); i++) {
//            label = new Label();
//            label.setFont(new Font(22));
//            if (sentencesForEachLabel.length() <= 23 - shortDescription.get(i).length()) {
//                sentencesForEachLabel.append(shortDescription.get(i) + " ");
//                label.setLayoutY(20 * (numberOFLabelUsed + 1));
//                numberOFLabelUsed++;
//            } else {
//                while (sentencesForEachLabel.length() <= 22) {
//                    sentencesForEachLabel.append(" ");
//                }
//                label.setText(sentencesForEachLabel.toString());
//                label.setLayoutY(20 * (numberOFLabelUsed + 1));
//                vBox.getChildren().add(label);
//                sentencesForEachLabel.setLength(0);
//                numberOFLabelUsed++;
//            }
//        }
//        while (sentencesForEachLabel.length() <= 30) {
//            sentencesForEachLabel.append(" ");
//        }
//        label = new Label();
//        label.setFont(new Font(22));
//        label.setText(sentencesForEachLabel.toString());
//        ScrollPane scrollPaneForInformation = new ScrollPane();
//        scrollPaneForInformation.setPannable(true);
//        scrollPaneForInformation.setFitToHeight(true);
//        scrollPaneForInformation.setFitToWidth(true);
//        // scrollPaneForInformation.setLayoutY(300);
//        scrollPaneForInformation.setMinHeight(100);
//        scrollPaneForInformation.setMaxHeight(100);
//        // scrollPaneForInformation.setLayoutX(300);
//        scrollPaneForInformation.setMinWidth(250);
//        scrollPaneForInformation.setMaxWidth(250);
//        scrollPaneForInformation.setContent(vBox);
//
//        anchorPane.getChildren().add(scrollPaneForInformation);
//        Scene scene = new Scene(anchorPane, 250, 100);
//        stage.setScene(scene);
//        stage.show();

    }
}
