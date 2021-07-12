package project.client.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
//import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.modelsforview.CardView;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.WHITE;

public class DeckScene extends Application {
    private int indexOfChosenCardInDeck = 0;
    private boolean isClassWaitingForUserToChooseCardFromDeck;

    public boolean isClassWaitingForUserToChooseCardFromDeck() {
        return isClassWaitingForUserToChooseCardFromDeck;
    }

    public void setClassWaitingForUserToChooseCardFromDeck(boolean classWaitingForUserToChooseCardFromDeck) {
        isClassWaitingForUserToChooseCardFromDeck = classWaitingForUserToChooseCardFromDeck;
    }

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        String wholeDeckString = DuelView.getDeckString();
        boolean isAllySeeing = DuelView.isIsAllySeeingGraveyard();
        ArrayList<String> cardNames = new ArrayList<>();
        ArrayList<Label> hBoxes = new ArrayList<>();
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
                    for (int i = 0; i < group.getChildren().size(); i++) {
                        if (group.getChildren().get(i).equals(rectangle)) {
                            indexOfChosenCardInDeck = i;
                            if (isClassWaitingForUserToChooseCardFromDeck) {
                                String token = DuelView.getToken();
                                String miniString = (GameManager.getDuelControllerByIndex(token).getFakeTurn() == 1 ? "" : "--opponent ");
                                System.out.println("sending to server "+"select " + miniString + "--deck " + indexOfChosenCardInDeck);
                                String output = GameManager.getDuelControllerByIndex(token).getInput("select " + miniString + "--deck " + (indexOfChosenCardInDeck+1), true, token);
                                System.out.println("THIS OUTPUT IF FOR CHOOSING CARD FROM GRAVEYARD " + output);
                                if (output.contains("this card cannot be") || output.contains("this is not a")
                                    || output.contains("this monster is not") || output.contains("invalid")
                                    || output.contains("select another") || output.contains("please try")) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Information Dialog");
                                    alert.setHeaderText("Selecting Card Message");
                                    alert.setContentText(output);
                                    alert.showAndWait();
                                } else if (output.contains("choose if you want")) {
                                    ButtonType attackingButton = new ButtonType("Attacking");
                                    ButtonType defensiveButton = new ButtonType("Defensive");
                                    Alert alert = new Alert(Alert.AlertType.NONE, "Please choose attacking or defensive.", attackingButton, defensiveButton);
                                    alert.setTitle("Information Dialog");
                                    alert.setHeaderText("Choose Attacking Or Defensive Message");
                                    alert.setContentText(output);
                                    ButtonType result = alert.showAndWait().orElse(attackingButton);
                                    if (result.equals(attackingButton)) {
                                        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"attacking\", true, token)");
                                        Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                        newAlert.setTitle("Information Dialog");
                                        newAlert.setHeaderText("Result Message");
                                        newAlert.setContentText(output);
                                        newAlert.showAndWait();
                                        isClassWaitingForUserToChooseCardFromDeck = false;
                                        DuelView.getGraveyardScene().setClassWaitingForUserToChooseCardFromGraveyard(false);
                                        stage.close();
                                        DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                    } else if (result.equals(defensiveButton)) {
                                        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"defensive\", true, token)");
                                        Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                        newAlert.setTitle("Information Dialog");
                                        newAlert.setHeaderText("Result Message");
                                        newAlert.setContentText(output);
                                        newAlert.showAndWait();
                                        isClassWaitingForUserToChooseCardFromDeck = false;
                                        DuelView.getGraveyardScene().setClassWaitingForUserToChooseCardFromGraveyard(false);
                                        stage.close();
                                        DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                    }
                                } else {
                                    Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                    newAlert.setTitle("Information Dialog");
                                    newAlert.setHeaderText("Result Message");
                                    newAlert.setContentText(output);
                                    newAlert.showAndWait();
                                    isClassWaitingForUserToChooseCardFromDeck = false;
                                    stage.close();
                                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                }
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
    }
}
