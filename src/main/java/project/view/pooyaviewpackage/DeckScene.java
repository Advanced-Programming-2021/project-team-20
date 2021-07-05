package project.view.pooyaviewpackage;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.model.modelsforview.CardView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.WHITE;

public class DeckScene {
    private static int indexOfChosenCardInDeck = 0;

    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        String wholeDeckString = DuelView.getDeckString();
        boolean isAllySeeing = DuelView.isIsAllySeeingGraveyard();
        //    String graveyardUselessString = "first player graveyard:\n(.+)\nsecond player graveyard:\n(.+)";
        //    Pattern graveyardPattern = Pattern.compile(graveyardUselessString);
        //   Matcher graveyardMatch = graveyardPattern.matcher(wholeGraveyardString);
        ArrayList<String> cardNames = new ArrayList<>();
        ArrayList<Label> hBoxes = new ArrayList<>();
        //  if (graveyardMatch.find()) {
        //      System.out.println(graveyardMatch.group(1) + "\n.\n" + graveyardMatch.group(2));
        int index = 1;
        boolean hope = true;
        while (hope) {
            String indexString = index + ": (.+)";
            Pattern indexPattern = Pattern.compile(indexString);
            Matcher indexMatch = indexPattern.matcher(wholeDeckString);
            if (indexMatch.find()) {
                cardNames.add(indexMatch.group(1));
                System.out.println(indexMatch.group(1) + " is in deck");
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
        scrollPaneForInformation.setMinHeight(CardView.getCardHeight() * 3);
        scrollPaneForInformation.setMaxHeight(CardView.getCardHeight() * 3);
        // scrollPaneForInformation.setLayoutX(300);
        scrollPaneForInformation.setMinWidth(CardView.getCardWidth() * 5);
        scrollPaneForInformation.setMaxWidth(CardView.getCardWidth() * 5);
        Group group = new Group();
        scrollPaneForInformation.setContent(group);

        anchorPane.getChildren().add(scrollPaneForInformation);
        Scene scene = new Scene(anchorPane, 250, 100);
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
                    for (int i = 0; i < group.getChildren().size(); i++) {
                        if (group.getChildren().get(i).equals(rectangle)) {
                            indexOfChosenCardInDeck = i;
                            if (DuelView.isIsClassWaitingForUserToChooseCardFromDeck()) {
                                stage.close();
                            }
                        }
                    }
                }
            });
            group.getChildren().add(rectangle);
        }
        if (cardNames.size() < 15) {
            for (int i = 0; i < 15 - cardNames.size(); i++) {
                Rectangle rectangle = new Rectangle(90 * ((i + cardNames.size()) % 5), 131 * (Math.floor((i + cardNames.size()) / 5)), 90, 131);
                rectangle.setFill(WHITE);
                group.getChildren().add(rectangle);
            }
        }


        //  }
    }
}
