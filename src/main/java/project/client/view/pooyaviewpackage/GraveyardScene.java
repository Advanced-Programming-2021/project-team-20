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
import project.client.modelsforview.CardView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.WHITE;

public class GraveyardScene extends Application {
    private int indexOfChosenCardInGraveyard = 0;
    private boolean isClassWaitingForUserToChooseCardFromGraveyard;

    public boolean isClassWaitingForUserToChooseCardFromGraveyard() {
        return isClassWaitingForUserToChooseCardFromGraveyard;
    }

    public void setClassWaitingForUserToChooseCardFromGraveyard(boolean classWaitingForUserToChooseCardFromGraveyard) {
        isClassWaitingForUserToChooseCardFromGraveyard = classWaitingForUserToChooseCardFromGraveyard;
    }

    @Override
    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        String wholeGraveyardString = DuelView.getGraveyardString();
        boolean isAllySeeing = DuelView.isIsAllySeeingGraveyard();
        String graveyardUselessString = "first player graveyard:\n((.+\n)+)second player graveyard:((\n.+)+)";
        Pattern graveyardPattern = Pattern.compile(graveyardUselessString);
        Matcher graveyardMatch = graveyardPattern.matcher(wholeGraveyardString);
        ArrayList<String> cardNames = new ArrayList<>();
        ArrayList<Label> hBoxes = new ArrayList<>();
        if (graveyardMatch.find()) {
            System.out.println(graveyardMatch.group(1) + "\n.\n" + graveyardMatch.group(3));
            int index = 1;
            boolean hope = true;
            while (hope) {
                String indexString = index + ". (.+):(.+)";
                Pattern indexPattern = Pattern.compile(indexString);
                System.out.println("graveyardMatch.group((DuelView.isIsAllySeeingGraveyard() ? 1 : 2)) " + graveyardMatch.group((DuelView.isIsAllySeeingGraveyard() ? 1 : 2)));
                Matcher indexMatch = indexPattern.matcher(graveyardMatch.group((DuelView.isIsAllySeeingGraveyard() ? 1 : 3)));
                if (indexMatch.find()) {
                    cardNames.add(indexMatch.group(1));
                    System.out.println(indexMatch.group(1) + " is in graveyard and second is " + indexMatch.group(2));
                } else {
                hope = false;}
                index++;
            }
            ScrollPane scrollPaneForInformation = new ScrollPane();
            scrollPaneForInformation.setPannable(true);
            scrollPaneForInformation.setFitToHeight(true);
            scrollPaneForInformation.setFitToWidth(true);
            scrollPaneForInformation.setMinHeight(CardView.getCardHeight() * 3);
            scrollPaneForInformation.setMaxHeight(CardView.getCardHeight() * 3);
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
                                indexOfChosenCardInGraveyard = i;
                                if (isClassWaitingForUserToChooseCardFromGraveyard) {
                                    String miniString = (DuelView.isIsAllySeeingGraveyard() ? "" : "--opponent ");
                                    String token =DuelView.getToken();
                                    JsonCreator.setFirstAdditionalString(miniString);
                                    JsonCreator.setIntegerString((indexOfChosenCardInGraveyard+1)+"");
                                    String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + miniString + \"--graveyard \" + (indexOfChosenCardInGraveyard + 1), true, token)");
                                    System.out.println("THIS OUTPUT IF FOR CHOOSING CARD FROM GRAVEYARD " + output);
                                    if (output.contains("this card cannot be") || output.contains("this is not a")
                                        || output.contains("this monster is not") || output.contains("please try")) {
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
                                            isClassWaitingForUserToChooseCardFromGraveyard = false;
                                            stage.close();
                                            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                        } else if (result.equals(defensiveButton)) {
                                            output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"defensive\", true, token)");
                                            Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                            newAlert.setTitle("Information Dialog");
                                            newAlert.setHeaderText("Result Message");
                                            newAlert.setContentText(output);
                                            newAlert.showAndWait();
                                            isClassWaitingForUserToChooseCardFromGraveyard = false;
                                            stage.close();
                                            DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                        }
                                    } else {
                                        Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                                        newAlert.setTitle("Information Dialog");
                                        newAlert.setHeaderText("Result Message");
                                        newAlert.setContentText(output);
                                        newAlert.showAndWait();
                                        isClassWaitingForUserToChooseCardFromGraveyard = false;
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
}
