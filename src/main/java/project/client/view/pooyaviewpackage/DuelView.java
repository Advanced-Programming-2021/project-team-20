package project.client.view.pooyaviewpackage;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.client.CardsStorage;
import project.client.DeserializeInformationFromServer;
import project.client.ServerConnection;
import project.client.ToGsonFormatToSendDataToServer;
import project.client.view.LoginController;
import project.model.PhaseInGame;
//import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.server.controller.non_duel.storage.Storage;
import project.model.cardData.General.*;
import project.model.cardData.General.Card;
import project.client.modelsforview.*;
import project.client.view.CustomDialog;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuelView {
    private static volatile boolean isMyTurn;

    public static boolean isIsMyTurn() {
        return isMyTurn;
    }

    public static void setIsMyTurn(boolean isMyTurn) {
        DuelView.isMyTurn = isMyTurn;
    }

    public static void setToken(String token) {
        DuelView.token = token;
    }

    private static Stage stage;
    private static double stageWidth;
    private static double stageHeight;
    private static AnchorPane anchorPane;
    private static BattleFieldView battleFieldView;
    private static Group informationRectangle;
    private static Card currentCardForView;
    private static double xHelperForCardViewConstructor;
    private static double yHelperForCardViewConstructor;
    private static MouseEvent previousMouseEvent;
    private static project.client.modelsforview.CardView draggingObject;
    private static double draggingObjectX;
    private static double draggingObjectY;
    private static RowOfCardLocation rowOfCardLocationOfFinalDraggedPoint;
    private static RowOfCardLocation rowOfCardLocationOfInitialDraggedPoint;
    private boolean dragFlag = false;
    private int clickCounter = 0;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture;
    private static CardLocation cardLocationLookingRightNow;
    private static CardLocation cardLocationSelecting;
    private static boolean isWaitingForRightClickOptionListHit;
    private static Group allCards;
    // private static CardLocation cardLocationToSendCardTo;
    //private static NextPhaseButton nextPhaseButton;
    private static Transition transition;
    private static ControllerForView controllerForView;
    private static Group moreCardInfoGroup;

    private static MediaPlayer backgroundMusic;
    private static MediaView mediaView;
    private static boolean isGameMute;
    private static boolean isGamePaused;
    private static Rectangle pause;
    private static Rectangle mute;
    private static Rectangle surrender;

    public static boolean isIsGameMute() {
        return isGameMute;
    }

    public static boolean isShouldDuelViewClickingAbilitiesWork() {
        return shouldDuelViewClickingAbilitiesWork;
    }

    public static boolean isIsGamePaused() {
        return isGamePaused;
    }

    public static void setIsGameMute(boolean isGameMute) {
        DuelView.isGameMute = isGameMute;
    }

    public static void setIsGamePaused(boolean isGamePaused) {
        DuelView.isGamePaused = isGamePaused;
    }

    private static Rectangle cardImageForCardMoreInfo;
    private static Rectangle cardAttributeForCardMoreInfo;
    private static Rectangle cardLevelStarForCardMoreInfo;
    private static Label cardNameForCardMoreInfo;
    private static Label cardAttackForCardMoreInfo;
    private static Label cardDefenseForCardMoreInfo;
    private static Label cardLevelForCardMoreInfo;
    private static Label cardFamilyForCardMoreInfo;
    private static ScrollPane scrollPaneForCardMoreInfo;
    private static VBox vBox;

    private static CardLocation cardLocationBeingDragged;
    private static CardLocation cardLocationDragDropped;
    private static SendingRequestsToServer sendingRequestsToServer;
    private static AdvancedCardMovingController advancedCardMovingController;
    private static AlertAndMenuItems showOptionsToUser;
    private static MoreCardInfoSection moreCardInfoSection;
    private static boolean shouldDuelViewClickingAbilitiesWork = true;
    private static GamePhaseButton drawPhaseLabel;
    private static GamePhaseButton standByPhaseLabel;
    private static GamePhaseButton mainPhaseOneLabel;
    private static GamePhaseButton battlePhaseLabel;
    private static GamePhaseButton mainPhaseTwoLabel;
    private static GamePhaseButton endPhaseLabel;
    private static GamePhaseButton nextPhaseLabel;
    private static String graveyardString = "";
    private static String deckString = "";
    private static boolean isAllySeeingGraveyard;
    private static boolean isClassWaitingForUserToChooseCardFromGraveyard = false;
    private static boolean isClassWaitingForUserToChooseCardFromDeck = false;
    private static HealthBarAndHealthPoints allyHealthStatus;
    private static HealthBarAndHealthPoints opponentHealthStatus;
    private static Long lastTimeKeyPressed = 0l;
    private static StringBuilder cheatCodes = new StringBuilder();

    private static boolean areWePlayingWithAI;

    private static volatile String token = "nothing";

    public static String getToken() {
        return token;
    }

    public static boolean isAreWePlayingWithAI() {
        return areWePlayingWithAI;
    }

    private static Label firstPlayerUsernameLabel;
    private static Label secondPlayerUsernameLabel;
    private static Label firstPlayerNicknameLabel;
    private static Label secondPlayerNicknameLabel;
    private static Rectangle firstPlayerAvatar;
    private static Rectangle secondPlayerAvatar;


    private static DeckScene deckScene;
    private static GraveyardScene graveyardScene;

    private boolean shouldICallPayAndDestroy;

    public static HealthBarAndHealthPoints getAllyHealthStatus() {
        return allyHealthStatus;
    }

    public static HealthBarAndHealthPoints getOpponentHealthStatus() {
        return opponentHealthStatus;
    }

    public static void setAllyHealthStatus(HealthBarAndHealthPoints allyHealthStatus) {
        DuelView.allyHealthStatus = allyHealthStatus;
    }

    public static void setOpponentHealthStatus(HealthBarAndHealthPoints opponentHealthStatus) {
        DuelView.opponentHealthStatus = opponentHealthStatus;
    }

    public static boolean isIsClassWaitingForUserToChooseCardFromGraveyard() {
        return isClassWaitingForUserToChooseCardFromGraveyard;
    }

    public static boolean isIsClassWaitingForUserToChooseCardFromDeck() {
        return isClassWaitingForUserToChooseCardFromDeck;
    }

    public static void setIsClassWaitingForUserToChooseCardFromGraveyard(
        boolean isClassWaitingForUserToChooseCardFromGraveyard) {
        DuelView.isClassWaitingForUserToChooseCardFromGraveyard = isClassWaitingForUserToChooseCardFromGraveyard;
    }

    public static void setIsClassWaitingForUserToChooseCardFromDeck(boolean isClassWaitingForUserToChooseCardFromDeck) {
        DuelView.isClassWaitingForUserToChooseCardFromDeck = isClassWaitingForUserToChooseCardFromDeck;
    }

    public static String getDeckString() {
        deckString = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"show deck\", true, token)");
        // System.out.println("deckString" + deckString);
        return deckString;
    }

    public static void setDeckString(String deckString) {
        DuelView.deckString = deckString;
    }

    public static boolean isIsAllySeeingGraveyard() {
        return isAllySeeingGraveyard;
    }

    public static void setIsAllySeeingGraveyard(boolean isAllySeeingGraveyard) {
        DuelView.isAllySeeingGraveyard = isAllySeeingGraveyard;
    }

    public static String getGraveyardString() {
        deckString = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"show graveyard\", true, token)");
        // System.out.println("deckString" + deckString);
        return graveyardString;
    }

    public static void setGraveyardString(String graveyardString) {
        DuelView.graveyardString = graveyardString;
    }

    public DuelView() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    private DuelStage duelStage;

    public DuelStage getDuelStage() {
        return duelStage;
    }

    public AnchorPane getAnchorpaneAtBeginning(Stage stage) {
        duelStage = (DuelStage) stage;
        cheatCodes.setLength(0);
        // FakeMain.call();
        areWePlayingWithAI = Boolean.parseBoolean(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).isAIPlaying()"));
        prepareArrayListsForWorking();
        DuelView.stage = stage;
        DuelView.stage.setTitle("Duel Page");

        AnchorPane anchorPane = new AnchorPane();
        DuelView.anchorPane = anchorPane;
        // System.out.println(battleFieldView == null);
        anchorPane.setOnMouseClicked(e -> {
            if (shouldDuelViewClickingAbilitiesWork) {
                TwoDimensionalPoint finalTwoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
                CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint, null);
                // System.out.println("is cardLocation null " + (cardLocation == null));
                if (cardLocation != null) {
                    CardView cardView = controllerForView.getCardViewByCardLocation(cardLocation);
                    // System.out.println("SHOW ME NO MORE cardLocation clicked is " + cardLocation.getRowOfCardLocation()
                    //     + " " + cardLocation.getIndex());
                    if (cardView == null) {
                        // System.out.println("clicked on null SHOW ME NO MORE");
                    } else {
                        Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
                        // System.out.println("clicked on " + cardView.getCard().getCardName() + " " + bounds.getMinX()
                        //     + " " + bounds.getMinY());
                        if (e.getButton().equals(MouseButton.PRIMARY)) {
                            if (!dragFlag) {
                                if (e.getClickCount() == 1) {
                                    scheduledFuture = executor.schedule(() -> singleClickActionSpecial(finalTwoDimensionalPoint, cardView), 300, TimeUnit.MILLISECONDS);
                                } else if (e.getClickCount() > 1) {
                                    if (scheduledFuture != null && !scheduledFuture.isCancelled()
                                        && !scheduledFuture.isDone()) {
                                        scheduledFuture.cancel(false);
                                        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint, cardView);
                                        // System.out.println("cardLocationSelecting is " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());

                                        if (cardLocationSelecting != null) {
                                            // System.out.println("important selection:" + "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting) + ":");
                                            String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"select \" + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true, token)");
                                            System.out.println("&@&@&@&@& " + output);
                                            if (output == null) {

                                            } else if (output.contains("you can select another spell or trap")) {
                                                ButtonType choose = new ButtonType("I will choose another spell/trap");
                                                ButtonType end = new ButtonType("finish selecting");
                                                Alert alert = new Alert(Alert.AlertType.NONE, "Please decide.", choose, end);
                                                alert.setTitle("Information Dialog");
                                                alert.setHeaderText("Decision Message");
                                                //alert.setContentText(output);
                                                ButtonType result = alert.showAndWait().orElse(end);
                                                if (result.equals(end)) {
                                                    output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"finish selecting\", true, token)");
                                                    System.out.println("&@&@&@&@& " + output);
                                                    advancedCardMovingController.advanceForwardBattleField();
                                                } else {
                                                    Alert newerAlert = new Alert(Alert.AlertType.INFORMATION);
                                                    newerAlert.setTitle("Information Dialog");
                                                    newerAlert.setHeaderText("Result Message");
                                                    newerAlert.setContentText("Select another spell/trap");
                                                    newerAlert.showAndWait();
                                                }
                                            } else if (!output.contains("selected") && !output.contains("no card found")) {
                                                if (output.contains("attacking") || output.contains("defensive") || output.contains("attack") || output.contains("defense")) {


                                                    ButtonType attackingButton = new ButtonType("Attacking");
                                                    ButtonType defensiveButton = new ButtonType("Defensive");
                                                    Alert newAlert = new Alert(Alert.AlertType.NONE, "Please choose attacking or defensive.", attackingButton, defensiveButton);
                                                    newAlert.setTitle("Information Dialog");
                                                    newAlert.setHeaderText("Choose Attacking Or Defensive Message");
                                                    newAlert.setContentText(output);
                                                    ButtonType result = newAlert.showAndWait().orElse(attackingButton);
                                                    if (result.equals(attackingButton)) {
                                                        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"attacking\", true, token)");
                                                        Alert newerAlert = new Alert(Alert.AlertType.INFORMATION);
                                                        newerAlert.setTitle("Information Dialog");
                                                        newerAlert.setHeaderText("Result Message");
                                                        newerAlert.setContentText(output);
                                                        newerAlert.showAndWait();
                                                        isClassWaitingForUserToChooseCardFromGraveyard = false;
                                                        //   stage.close();
                                                        DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                                    } else if (result.equals(defensiveButton)) {
                                                        output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"defensive\", true, token)");
                                                        Alert newerAlert = new Alert(Alert.AlertType.INFORMATION);
                                                        newerAlert.setTitle("Information Dialog");
                                                        newerAlert.setHeaderText("Result Message");
                                                        newerAlert.setContentText(output);
                                                        newerAlert.showAndWait();
                                                        isClassWaitingForUserToChooseCardFromGraveyard = false;
                                                        // stage.close();
                                                        DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                                    }


                                                } else if (output.contains("imply enter select command") || output.contains("there is no need to enter activate command")) {
                                                    output = output.replaceAll("Simply enter select command", "");
                                                    output = output.replaceAll("simply enter select command", "");
                                                    output = output.replaceAll("there is no need to enter activate command after that", "");
                                                    if (output.contains("please choose one monster from") && output.contains("graveyard") || output.contains("show graveyard") || output.contains("Now choose a monster from your graveyard")) {
                                                        graveyardScene.setClassWaitingForUserToChooseCardFromGraveyard(true);
                                                        output = output.replaceAll("show graveyard", "");
                                                    }
                                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                    alert.setTitle("Information Dialog");
                                                    alert.setHeaderText("Select Card Message");
                                                    alert.setContentText(output);
                                                    alert.showAndWait();
                                                } else {
                                                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                                }

                                            } else {
                                                if (!output.contains("selected")) {
                                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                    alert.setTitle("Information Dialog");
                                                    alert.setHeaderText("Select Card Message");
                                                    alert.setContentText(output);
                                                    alert.showAndWait();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            dragFlag = false;
                        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                            // System.out.println("terente x = " + e.getSceneX() + " y = " + e.getSceneY());
                            cardView.updateContextMenu();
                            cardView.getContextMenu().show(cardView, e.getSceneX() + bounds.getMinX() / 2 + 30, e.getSceneY() + 30);
                        }
                    }
                }
            }
        });
//        anchorPane.setOnDragOver(e -> {
//            if (draggingObject != null && e.getDragboard().hasString() && e.getDragboard().getString().equals("card")) {
//                e.acceptTransferModes(TransferMode.MOVE);
//            }
//            e.consume();
//        });
//        anchorPane.setOnDragDropped(e -> {
//            shouldDuelViewClickingAbilitiesWork = true;
//            if (draggingObject != null && e.getDragboard().hasString() && e.getDragboard().getString().equals("card")) {
//                System.out.println("BEING DROPPED BUUUUUURN!");
//                TwoDimensionalPoint finalTwoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
//                System.out.println("BEING DROPPED BUUUUUURN!");
//                cardLocationDragDropped = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint,
//                    null);
//                System.out.println("BEING DROPPED BUUUUUURN!");
//                System.out.println("cardLocationDragDropped is " + cardLocationDragDropped.getRowOfCardLocation() + " "
//                    + cardLocationDragDropped.getIndex());
//                takeCareOfDraggingAction(cardLocationBeingDragged, cardLocationDragDropped, draggingObject,
//                    finalTwoDimensionalPoint);
//                System.out.println("wow");
//                draggingObject = null;
//                cardLocationBeingDragged = null;
//                cardLocationDragDropped = null;
//                previousMouseEvent = null;
//                draggingObjectX = -1;
//                draggingObjectY = -1;
//                e.setDropCompleted(true);
//            }
//            e.consume();
//        });


//        Scene scene = new Scene(anchorPane, 1200, 1000);
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                checkCheatCommands(keyEvent);
//            }
//        });
//        stage.setScene(scene);
//        stage.show();

        stageWidth = 1200;
        stageHeight = 1000;
        prepareObjectsForWorking();
        anchorPane.getChildren().add(battleFieldView);
        anchorPane.getChildren().add(allCards);
        //anchorPane.getChildren().addAll(nextPhaseButton, nextPhaseButton.getLabel());
        moreCardInfoGroup.getChildren().add(cardAttackForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardDefenseForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardLevelForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardLevelStarForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardFamilyForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardImageForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardAttributeForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(cardNameForCardMoreInfo);
        moreCardInfoGroup.getChildren().add(scrollPaneForCardMoreInfo);
        anchorPane.getChildren().add(moreCardInfoGroup);
        anchorPane.getChildren().add(drawPhaseLabel);
        anchorPane.getChildren().add(standByPhaseLabel);
        anchorPane.getChildren().add(mainPhaseOneLabel);
        anchorPane.getChildren().add(battlePhaseLabel);
        anchorPane.getChildren().add(mainPhaseTwoLabel);
        anchorPane.getChildren().add(endPhaseLabel);
        anchorPane.getChildren().add(nextPhaseLabel);
        anchorPane.getChildren().add(allyHealthStatus.getHealthDigits().get(0));
        anchorPane.getChildren().add(allyHealthStatus.getHealthDigits().get(1));
        anchorPane.getChildren().add(allyHealthStatus.getHealthDigits().get(2));
        anchorPane.getChildren().add(allyHealthStatus.getHealthDigits().get(3));
        anchorPane.getChildren().add(allyHealthStatus.getHealthBar());
        // anchorPane.getChildren().add(allyHealthStatus.getHelpfulHealthRectangle());
        anchorPane.getChildren().add(allyHealthStatus.getBackGroundRectangle());
        anchorPane.getChildren().add(allyHealthStatus.getContainer());
        anchorPane.getChildren().add(opponentHealthStatus.getHealthDigits().get(0));
        anchorPane.getChildren().add(opponentHealthStatus.getHealthDigits().get(1));
        anchorPane.getChildren().add(opponentHealthStatus.getHealthDigits().get(2));
        anchorPane.getChildren().add(opponentHealthStatus.getHealthDigits().get(3));
        anchorPane.getChildren().add(opponentHealthStatus.getHealthBar());
        // anchorPane.getChildren().add(opponentHealthStatus.getHelpfulHealthRectangle());
        anchorPane.getChildren().add(opponentHealthStatus.getBackGroundRectangle());
        anchorPane.getChildren().add(opponentHealthStatus.getContainer());


        anchorPane.getChildren().add(mute);
        anchorPane.getChildren().add(pause);
        anchorPane.getChildren().add(surrender);

        anchorPane.getChildren().add(firstPlayerUsernameLabel);
        anchorPane.getChildren().add(firstPlayerNicknameLabel);
        anchorPane.getChildren().add(firstPlayerAvatar);
        anchorPane.getChildren().add(secondPlayerUsernameLabel);
        anchorPane.getChildren().add(secondPlayerNicknameLabel);
        anchorPane.getChildren().add(secondPlayerAvatar);


        controllerForView.giveCardsAtTheBeginningOfGame();
//        System.out.println(battleFieldView.getUpperLeftX() + " wouiiiiiiiiiiiiiiiiiiiiiiiii");
//        System.out.println(stage.getWidth());
//        System.out.println(stage.getHeight());
        return anchorPane;
        // MainView.changeScene(anchorPane);
    }

    public static void callStage() {
        System.out.println("I am newing DuelStage");
        new DuelStage();
    }

    // public static void main(String args[]) {
    //  launch(args);
//        Stage stage = new Stage();
//        DuelView duelView = new DuelView();
//        AnchorPane anchorPane = duelView.getAnchorpaneAtBeginning(stage);
//        Scene scene = new Scene(anchorPane, 1200, 1000);
////        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
////            @Override
////            public void handle(KeyEvent keyEvent) {
////                checkCheatCommands(keyEvent);
////            }
////        });
//        stage.setScene(scene);
//        stage.show();
//        //callStage();
    // }

    public void callPayOrDestroy(String output) {
        String something = "do you want to pay 100 lifepoints or do you want to destroy your spell card?";
        Pattern pattern = Pattern.compile(something);
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            ButtonType pay = new ButtonType("Pay");
            ButtonType destroy = new ButtonType("Destroy");
            Alert alert = new Alert(Alert.AlertType.NONE, "Decide", pay, destroy);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Setting Card Message");
            alert.setContentText(matcher.group(0));
            ButtonType result = alert.showAndWait().orElse(destroy);
            if (result.equals(pay)) {
                JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"pay\", true, token)");
            } else if (result.equals(destroy)) {
                JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"destroy\", true, token)");
            }
            advancedCardMovingController.advanceForwardBattleField();
            PauseTransition pauseTransition = (new PauseTransition(Duration.seconds(0.3)));
            pauseTransition.play();
            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    GamePhaseButton.updateAllGamePhaseButtonsOnce();
                }
            });
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static double getStageWidth() {
        return stageWidth;
    }

    public static double getStageHeight() {
        return stageHeight;
    }

    public static BattleFieldView getBattleFieldView() {
        return battleFieldView;
    }

    public static double getXHelperForCardViewConstructor() {
        return xHelperForCardViewConstructor;
    }

    public static void setXHelperForCardViewConstructor(double xHelperForCardViewConstructor) {
        DuelView.xHelperForCardViewConstructor = xHelperForCardViewConstructor;
    }

    public static void setYHelperForCardViewConstructor(double yHelperForCardViewConstructor) {
        DuelView.yHelperForCardViewConstructor = yHelperForCardViewConstructor;
    }

    public static double getYHelperForCardViewConstructor() {
        return yHelperForCardViewConstructor;
    }

    public static void setDraggingObject(project.client.modelsforview.CardView draggingObject) {
        DuelView.draggingObject = draggingObject;
    }

    public static DeckScene getDeckScene() {
        return deckScene;
    }

    public static GraveyardScene getGraveyardScene() {
        return graveyardScene;
    }

    public static void setDraggingObjectX(double draggingObjectX) {
        DuelView.draggingObjectX = draggingObjectX;
    }

    public static void setDraggingObjectY(double draggingObjectY) {
        DuelView.draggingObjectY = draggingObjectY;
    }

    private void prepareArrayListsForWorking() {
        deckScene = new DeckScene();
        graveyardScene = new GraveyardScene();
        allCards = new Group();
        //nextPhaseButton = new NextPhaseButton();
        transition = new Transition();
        controllerForView = new ControllerForView();
        sendingRequestsToServer = new SendingRequestsToServer();
        advancedCardMovingController = new AdvancedCardMovingController(this);
        showOptionsToUser = new AlertAndMenuItems();
        moreCardInfoSection = new MoreCardInfoSection();
        moreCardInfoGroup = new Group();
        cardImageForCardMoreInfo = new Rectangle();
        cardAttributeForCardMoreInfo = new Rectangle();
        cardLevelStarForCardMoreInfo = new Rectangle();
        cardNameForCardMoreInfo = new Label();
        cardAttackForCardMoreInfo = new Label();
        cardDefenseForCardMoreInfo = new Label();
        cardLevelForCardMoreInfo = new Label();
        cardFamilyForCardMoreInfo = new Label();
        scrollPaneForCardMoreInfo = new ScrollPane();
        vBox = new VBox();

    }

    private static String myOwnUsername;
    private static String myOwnNickname;
    private static String myOpponentUsername;
    private static String myOpponentNickname;

    public void checkCheatCommands(KeyEvent keyEvent) {
        Long currentTimeKeyPressed = System.currentTimeMillis();
        if (currentTimeKeyPressed - lastTimeKeyPressed > 20000) {
            cheatCodes.setLength(0);
        }
        lastTimeKeyPressed = currentTimeKeyPressed;
        if (keyEvent.getCode().getName().equals("Space")) {
            cheatCodes.append(" ");
        } else if (keyEvent.getCode().getName().equalsIgnoreCase("minus")) {
            cheatCodes.append("-");
        } else if (keyEvent.getCode().getName().equalsIgnoreCase("shift")) {
            duelStage.setShiftKeyOn(true);
        } else if (keyEvent.getCode().getName().equals("Enter")) {
            System.out.println(cheatCodes);
            String dataSendToServer = ToGsonFormatToSendDataToServer.toGsonFormatWithOneRequest("cheat", "cheatCommand", cheatCodes.toString());
            String messageFromServer = ServerConnection.sendDataToServerAndReceiveResult(dataSendToServer);
            HashMap<String, String> deserializeResult = DeserializeInformationFromServer.deserializeForOnlyTypeAndMessage(messageFromServer);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Cheat Message");
            alert.setContentText(deserializeResult.get("message"));
            alert.showAndWait();
            advancedCardMovingController.advanceForwardBattleField();
            cheatCodes.setLength(0);
        } else if (keyEvent.getCode().getName().startsWith("Numpad")) {
            cheatCodes.append(keyEvent.getCode().getName().charAt(keyEvent.getCode().getName().length() - 1));
        } else {
            if (duelStage.isShiftKeyOn()) {
                cheatCodes.append(keyEvent.getCode().getName().toUpperCase());
                duelStage.setShiftKeyOn(false);
            } else {
                cheatCodes.append(keyEvent.getCode().getName().toLowerCase());
            }
        }
        // System.out.println(keyEvent.getText());
    }

    public static MediaPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    private static ArrayList<Card> allyCardsInHand;
    private static ArrayList<Card> allyCardsInDeck;
    private static ArrayList<Card> opponentCardsInHand;
    private static ArrayList<Card> opponentCardsInDeck;

    public static ArrayList<Card> getAllyCardsInHand() {
        return allyCardsInHand;
    }

    public static ArrayList<Card> getAllyCardsInDeck() {
        return allyCardsInDeck;
    }

    public static ArrayList<Card> getOpponentCardsInHand() {
        return opponentCardsInHand;
    }

    public static ArrayList<Card> getOpponentCardsInDeck() {
        return opponentCardsInDeck;
    }

    private void prepareObjectsForWorking() {
        battleFieldView = new BattleFieldView();
        URL resource = getClass().getResource("/project/ingameicons/music/song2.mp3");
        backgroundMusic = new MediaPlayer(new Media(resource.toString()));
        mediaView = new MediaView();
        mediaView.setMediaPlayer(backgroundMusic);
        backgroundMusic.setAutoPlay(true);
        backgroundMusic.setMute(false);
        backgroundMusic.setVolume(0.4);
        backgroundMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusic.seek(Duration.ZERO);
            }
        });
        backgroundMusic.play();
        surrender = new Rectangle(DuelView.getStageWidth() * 11 / 12, 0, DuelView.getStageWidth() / 12, DuelView.getStageHeight() / 8);
        surrender.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/surrender.png").toExternalForm())));
        surrender.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean oneRound = true;
                if (Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getNumberOfRounds()")) == 3) {
                    oneRound = false;
                }
                // System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
                String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"surrender\", true, token)");
                // System.out.println("Surrender: " + output);
                backgroundMusic.setMute(true);
                stage.close();
                // if (oneRound) {
                //     endOneRoundOfDuel(output);
                // } else {
                endGame(output);
                // }
            }
        });
        isGameMute = false;
        isGamePaused = false;
        mute = new Rectangle(DuelView.getStageWidth() * 11 / 12, DuelView.getStageHeight() / 8 +
            (DuelView.getStageWidth() / 12 * 7), DuelView.getStageWidth() / 12,
            (DuelView.getStageHeight() * 7 / 8 - 7 * DuelView.getStageWidth() / 12) / 2);
        pause = new Rectangle(DuelView.getStageWidth() * 11 / 12, DuelView.getStageHeight() / 8 +
            (DuelView.getStageWidth() / 12 * 7) + (DuelView.getStageHeight() * 7 / 8 - 7 * DuelView.getStageWidth() / 12) / 2,
            DuelView.getStageWidth() / 12, (DuelView.getStageHeight() * 7 / 8 - 7 * DuelView.getStageWidth() / 12) / 2);
        mute.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/sound_on_button_transparent.png").toExternalForm())));
        pause.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/play_button_transparent.png").toExternalForm())));
        pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (shouldDuelViewClickingAbilitiesWork) {
                    shouldDuelViewClickingAbilitiesWork = false;
                    pause.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/pause_button_transparent.png").toExternalForm())));
                } else {
                    shouldDuelViewClickingAbilitiesWork = true;
                    pause.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/play_button_transparent.png").toExternalForm())));
                }
            }
        });
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (backgroundMusic.isMute()) {
                    backgroundMusic.setMute(false);
                    isGameMute = false;
                    mute.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/sound_on_button_transparent.png").toExternalForm())));
                } else {
                    backgroundMusic.setMute(true);
                    isGameMute = true;
                    mute.setFill(new ImagePattern(new Image(DuelView.class.getResource("/project/ingameicons/furtherButtons/sound_off_button_transparent.png").toExternalForm())));
                }
            }
        });
        GamePhaseButton.removeAllGamePhaseButtons();
        drawPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_DRAW_PHASE);
        standByPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_STANDBY_PHASE);
        mainPhaseOneLabel = new GamePhaseButton(PhaseInGame.ALLY_MAIN_PHASE_1);
        battlePhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_BATTLE_PHASE);
        mainPhaseTwoLabel = new GamePhaseButton(PhaseInGame.ALLY_MAIN_PHASE_2);
        endPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_END_PHASE);
        nextPhaseLabel = new GamePhaseButton(null);
        // nextPhaseLabel.setLayoutX(100);
        // nextPhaseLabel.setLayoutY(500);
        // nextPhaseButton.setX(100);
        // nextPhaseButton.setY(500);
//        nextPhaseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                System.out.println(GameManager.getDuelControllerByIndex(0).getInput("advanced show board", true));
//            }
//        });
        shouldICallPayAndDestroy = false;
        nextPhaseLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (shouldDuelViewClickingAbilitiesWork) {
                    if (!areWePlayingWithAI) {
                        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"next phase\", true, token)");
                        // System.out.println("&" + output + "&");
                        if (output.contains("phase: ")) {
                            conductPhaseChanging(output, false);
                        }
                    } else {
                        boolean shouldPrepareAICard = false;
                        PhaseInGame phaseInGame = PhaseInGame.valueOf(JsonCreator.getResult("GameManager.getPhaseControllerByIndex(token).getPhaseInGame()"));
                        if (phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
                            shouldPrepareAICard = true;
                        }
                        if (shouldPrepareAICard) {
                            updatePrivacyForCardsForAI();
                        }
                        String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"next phase\", true, token)");
                        // System.out.println("we are playing with ai");
                        // System.out.println("&" + output + "&");
                        if (output.contains("phase: ")) {
                            GamePhaseButton.updateAllGamePhaseButtonsOnce();
                            if (output.contains("phase: draw phase") && output.contains("new card added to hand") && output.contains("phase: standby phase")) {
                                GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                //DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                PauseTransition pauseTransition = (new PauseTransition(Duration.seconds(0.2)));
                                pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                        advancedCardMovingController.advanceForwardBattleField();
                                        PauseTransition newPauseTransition = new PauseTransition(Duration.seconds(0.3));
                                        newPauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                                PauseTransition newerPauseTransition = new PauseTransition(Duration.seconds(0.3));
                                                newerPauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                                    @Override
                                                    public void handle(ActionEvent actionEvent) {
                                                        GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                                        PauseTransition newestPauseTransition = new PauseTransition(Duration.seconds(0.3));
                                                        newestPauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                                            @Override
                                                            public void handle(ActionEvent actionEvent) {
                                                                GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                                                updatePrivacyForCards();
                                                                GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                                                advancedCardMovingController.advanceForwardBattleField();
                                                            }
                                                        });
                                                        newestPauseTransition.play();
                                                    }
                                                });
                                                newerPauseTransition.play();
                                            }
                                        });
                                        newPauseTransition.play();
                                    }

                                });
                                pauseTransition.play();
                            }
                        }
                    }
                } else {
                    System.out.println("we are playing with ai");
                }
            }


//                PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
//                standByPhaseLabel.updateImage(phaseInGame);
//                mainPhaseOneLabel.updateImage(phaseInGame);
//                battlePhaseLabel.updateImage(phaseInGame);
//                mainPhaseTwoLabel.updateImage(phaseInGame);
//                endPhaseLabel.updateImage(phaseInGame);
//                if (output.contains("phase: draw phase") && output.contains("new card added to hand:")) {
//                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
//                }
            // PhaseInGame phaseInGame =
            // GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
            // standByPhaseLabel.updateImage(phaseInGame);
            // mainPhaseOneLabel.updateImage(phaseInGame);
            // battlePhaseLabel.updateImage(phaseInGame);
            // mainPhaseTwoLabel.updateImage(phaseInGame);
            // endPhaseLabel.updateImage(phaseInGame);
            // if (output.contains("phase: draw phase") && output.contains("new card added
            // to hand:")) {
            // DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            // }

        });

        allyHealthStatus = new HealthBarAndHealthPoints(true, 8000);
        opponentHealthStatus = new HealthBarAndHealthPoints(false, 8000);

        // System.out.println(" what upper left x  we see is " + battleFieldView.getUpperLeftX());
        // System.out.println("what height we see is " + battleFieldView.getHeight());
        allyCardsInHand = new ArrayList<>();
        allyCardsInDeck = new ArrayList<>();
        String cardsInMyHand = JsonCreator.getResult("give cards in my hand at the beginning of game");
        System.out.println("cardsInMyHand =\n" + cardsInMyHand);
        String cardsInOpponentHand = JsonCreator.getResult("give cards in my opponent hand at the beginning of game");
        System.out.println("cardsInOpponentHand =\n" + cardsInOpponentHand);
        String cardsInMyDeck = JsonCreator.getResult("give cards in my deck at the beginning of game");
        System.out.println("cardsInMyDeck =\n" + cardsInMyDeck);
        String cardsInOpponentDeck = JsonCreator.getResult("give cards in my opponent deck at the beginning of game");
        System.out.println("cardsInMyOpponentDeck =\n" + cardsInOpponentDeck);
        String stringForCardReceiver = "(\\*([^!]+)\\*)";
        Pattern pattern = Pattern.compile(stringForCardReceiver);
        Matcher matcher = pattern.matcher(cardsInMyHand);
        while (matcher.find()) {
            System.out.println("matcher.group(2) = " + matcher.group(2));
            allyCardsInHand.add(CardsStorage.getCardByName(matcher.group(2)));
        }
        matcher = pattern.matcher(cardsInMyDeck);
        while (matcher.find()) {
            System.out.println("matcher.group(2) = " + matcher.group(2));
            allyCardsInDeck.add(CardsStorage.getCardByName(matcher.group(2)));
        }
        //ArrayList<Card> allyCardsInHand = GameManager.getDuelBoardByIndex(token).getAllyCardsInHand();
        //ArrayList<Card> allyCardsInDeck = GameManager.getDuelBoardByIndex(token).getAllyCardsInDeck();
        for (int i = 0; i < allyCardsInDeck.size() + allyCardsInHand.size(); i++) {
            boolean inHand = false;
            if (i < allyCardsInHand.size()) {
                currentCardForView = allyCardsInHand.get(i);
                inHand = true;
            } else {
                currentCardForView = allyCardsInDeck.get(i - allyCardsInHand.size());
            }
            xHelperForCardViewConstructor = battleFieldView.getUpperLeftX() + battleFieldView.getWidth() - CardView.getCardWidth() - 7;
            yHelperForCardViewConstructor = battleFieldView.getUpperLeftY() + battleFieldView.getHeight() - 2 * CardView.getCardHeight() + 20;
            CardView cardView;
            if (inHand) {
                cardView = new CardView(currentCardForView, true, RowOfCardLocation.ALLY_DECK_ZONE, this);
            } else {
                cardView = new CardView(currentCardForView, false, RowOfCardLocation.ALLY_DECK_ZONE, this);
            }
            cardView.applyClickingAbilitiesToCardView(this);
            cardView.applyDragDetectingAbilityToCardView();
            System.out.println("preparing " + cardView.getCard().getCardName());
            allCards.getChildren().add(cardView);
        }


        String firstUsername = LoginController.getOnlineUser().getName();
        String secondUsername = JsonCreator.getResult("give my opponent username");
        String firstNickname = LoginController.getOnlineUser().getNickname();
        String secondNickname = JsonCreator.getResult("give my opponent nickname");
        Image firstPlayerImage = LoginController.getOnlineUser().getImage();
        Image secondPlayerImage = CardsStorage.getCardByName("Mirror Force").getImage();


        firstPlayerUsernameLabel = new Label("Username: " + firstUsername);
        secondPlayerUsernameLabel = new Label("Username: " + secondUsername);
        firstPlayerNicknameLabel = new Label("Nickname: " + firstNickname);
        secondPlayerNicknameLabel = new Label("Nickname: " + secondNickname);

        firstPlayerUsernameLabel.setLayoutX(DuelView.getBattleFieldView().getUpperLeftX() + 10);
        firstPlayerUsernameLabel.setLayoutY(DuelView.getBattleFieldView().getUpperLeftY() * 7 + 10);
        firstPlayerUsernameLabel.setFont(new Font(30));
        firstPlayerNicknameLabel.setLayoutX(DuelView.getBattleFieldView().getUpperLeftX() + 10);
        firstPlayerNicknameLabel.setLayoutY(DuelView.getBattleFieldView().getUpperLeftY() * 7 + 10 + DuelView.getStageHeight() / 16 - 10);
        firstPlayerNicknameLabel.setFont(new Font(30));


        secondPlayerUsernameLabel.setLayoutX(DuelView.getBattleFieldView().getUpperLeftX() + 10);
        secondPlayerUsernameLabel.setLayoutY(10);
        secondPlayerUsernameLabel.setFont(new Font(30));
        secondPlayerNicknameLabel.setLayoutX(DuelView.getBattleFieldView().getUpperLeftX() + 10);
        secondPlayerNicknameLabel.setLayoutY(10 + DuelView.getStageHeight() / 16 - 10);
        secondPlayerNicknameLabel.setFont(new Font(30));

        firstPlayerAvatar = new Rectangle(DuelView.getStageWidth() * 3 / 4, DuelView.getStageHeight() * 7 / 8, DuelView.getStageWidth() / 6, DuelView.getStageHeight() / 8);
        firstPlayerAvatar.setFill(new ImagePattern(firstPlayerImage));
        if (secondUsername.equals("AI")) {
            secondPlayerAvatar = new Rectangle(DuelView.getStageWidth() * 7 / 12, 0, DuelView.getStageWidth() / 3, DuelView.getStageHeight() / 8);
            secondPlayerAvatar.setFill(new ImagePattern(secondPlayerImage));
        } else {
            secondPlayerAvatar = new Rectangle(DuelView.getStageWidth() * 3 / 4, 0, DuelView.getStageWidth() / 6, DuelView.getStageHeight() / 8);
            secondPlayerAvatar.setFill(new ImagePattern(secondPlayerImage));
        }

        opponentCardsInHand = new ArrayList<>();
        opponentCardsInDeck = new ArrayList<>();
        matcher = pattern.matcher(cardsInOpponentHand);
        while (matcher.find()) {
            System.out.println("matcher.group(2) = " + matcher.group(2));
            opponentCardsInHand.add(CardsStorage.getCardByName(matcher.group(2)));
        }
        matcher = pattern.matcher(cardsInOpponentDeck);
        while (matcher.find()) {
            System.out.println("matcher.group(2) = " + matcher.group(2));
            opponentCardsInDeck.add(CardsStorage.getCardByName(matcher.group(2)));
        }
        // ArrayList<Card> opponentCardsInHand = GameManager.getDuelBoardByIndex(token).getOpponentCardsInHand();
        // ArrayList<Card> opponentCardsInDeck = GameManager.getDuelBoardByIndex(token).getOpponentCardsInDeck();
        for (int i = 0; i < opponentCardsInDeck.size() + opponentCardsInHand.size(); i++) {
            if (i < opponentCardsInHand.size()) {
                currentCardForView = opponentCardsInHand.get(i);
            } else {
                currentCardForView = opponentCardsInDeck.get(i - opponentCardsInHand.size());
            }
            xHelperForCardViewConstructor = battleFieldView.getUpperLeftX() + 40;
            yHelperForCardViewConstructor = battleFieldView.getUpperLeftY() + 108;
            CardView cardView = new CardView(currentCardForView, false, RowOfCardLocation.OPPONENT_DECK_ZONE, this);
            cardView.applyClickingAbilitiesToCardView(this);
            cardView.applyDragDetectingAbilityToCardView();
            // System.out.println("preparing " + cardView.getCard().getCardName());
            allCards.getChildren().add(cardView);
        }
        moreCardInfoSection.updateCardMoreInfoSection((CardView) allCards.getChildren().get(0),
            ((CardView) allCards.getChildren().get(0)).getCard().getCardDescription());
    }

    public void updatePrivacyForCards() {
        ArrayList<CardView> allyCardViews = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        ArrayList<CardView> opponentCardViews = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        int turn = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getTurn()"));
        if (turn == 1) {
            for (int i = 0; i < allyCardViews.size(); i++) {
                allyCardViews.get(i).setCanBeSeen(true);
            }
            for (int i = 0; i < opponentCardViews.size(); i++) {
                opponentCardViews.get(i).setCanBeSeen(false);
            }
        } else {
            for (int i = 0; i < allyCardViews.size(); i++) {
                allyCardViews.get(i).setCanBeSeen(false);
            }
            for (int i = 0; i < opponentCardViews.size(); i++) {
                opponentCardViews.get(i).setCanBeSeen(true);
            }
        }
    }

    public void updatePrivacyForCardsForAI() {
        ArrayList<CardView> allyCardViews = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        ArrayList<CardView> opponentCardViewsINHand = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        ArrayList<CardView> opponentCardViewsInDeck = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE);
        for (int i = 0; i < allyCardViews.size(); i++) {
            allyCardViews.get(i).setCanBeSeen(false);
        }
        for (int i = 0; i < opponentCardViewsINHand.size(); i++) {
            opponentCardViewsINHand.get(i).setCanBeSeen(true);
        }
        opponentCardViewsInDeck.get(0).setCanBeSeen(true);
    }

    public void conductPhaseChanging(String output, boolean updateBoardOrNot) {
        if (output.contains("phase: draw phase") && output.contains("new card added to hand") && output.contains("phase: standby phase")) {
            GamePhaseButton.updateAllGamePhaseButtonsOnce();
            updatePrivacyForCards();
            //DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
            PauseTransition pauseTransition = (new PauseTransition(Duration.seconds(0.3)));
            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    GamePhaseButton.updateAllGamePhaseButtonsOnce();
                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                    PauseTransition pauseTransition = (new PauseTransition(Duration.seconds(0.3)));
                    pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            GamePhaseButton.updateAllGamePhaseButtonsOnce();
                            PauseTransition pauseTransition = (new PauseTransition(Duration.seconds(0.3)));
                            if (output.contains("pay") && output.contains("destroy")) {
                                //shouldICallPayAndDestroy = true;
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        callPayOrDestroy(output);
                                    }
                                });
                            } else if (output.contains("phase: main phase")) {
                                shouldICallPayAndDestroy = false;
                                pauseTransition.play();
                                pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        if (updateBoardOrNot) {
                                            advancedCardMovingController.advanceForwardBattleField();
                                        } else {
                                            GamePhaseButton.updateAllGamePhaseButtonsOnce();
                                        }
                                    }
                                });

                            }
                        }
                    });
                    pauseTransition.play();
                }
            });
            pauseTransition.play();
        } else {
            GamePhaseButton.updateAllGamePhaseButtonsOnce();
        }
    }


//    public void stop() {
//        executor.shutdown();
//    }
//
//    public void doubleClickAction(MouseEvent mouseEvent) {
//        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
//        // System.out.println("cardLocationSelecting is " + cardLocationSelecting.getRowOfCardLocation() + " "
//        //     + cardLocationSelecting.getIndex());
//
//        if (cardLocationSelecting != null) {
//            String output = GameManager.getDuelControllerByIndex(token).getInput(
//                "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting),
//                true, token);
//            // System.out.println("&" + output);
//        }
//    }
//
//    private void takeCareOfDraggingAction(CardLocation initialCardLocation, CardLocation finalCardLocation,
//                                          project.client.modelsforview.CardView cardViewBeingDragged, TwoDimensionalPoint finalTwoDimensionalPoint) {
//        int turn = GameManager.getDuelControllerByIndex(token).getTurn();
//        PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(token).getPhaseInGame();
//        boolean allySummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)
//            || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2);
//        boolean opponentSummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)
//            || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2);
//        if (finalCardLocation != null) {
//            if ((turn == 1 && allySummonSetActivateCardPhase
//                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
//                || (turn == 2 && opponentSummonSetActivateCardPhase
//                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)))
//                && (cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
//                || cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP))) {
//                if ((turn == 1 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
//                    || turn == 2 && finalCardLocation.getRowOfCardLocation()
//                    .equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
//                    && cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP)) {
//                    // System.out.println("take care set trap");
//                    showOptionsToUser.showSetAlertForTrapCard(cardViewBeingDragged, initialCardLocation);
//                } else if ((turn == 1
//                    && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
//                    || turn == 2 && finalCardLocation.getRowOfCardLocation()
//                    .equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
//                    && cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
//                    && !((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue()
//                    .equals(SpellCardValue.FIELD)) {
//                    // System.out.println("take care set activate spell");
//                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);
//
//                } else if ((turn == 1
//                    && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
//                    || turn == 2 && finalCardLocation.getRowOfCardLocation()
//                    .equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE))
//                    && cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
//                    && ((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue()
//                    .equals(SpellCardValue.FIELD)) {
//                    // System.out.println("take care set activate spell field card");
//                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);
//                }
//            } else if (turn == 1 && allySummonSetActivateCardPhase
//                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
//                && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
//                || (turn == 2 && opponentSummonSetActivateCardPhase
//                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)
//                && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))
//                && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
//                // System.out.println("take care show all summon options");
//                showOptionsToUser.showAllSummonOptionsAlertForMonsterCard(cardViewBeingDragged, initialCardLocation,
//                    this);
//
//            } else if ((turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
//                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
//                && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
//                || (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
//                && initialCardLocation.getRowOfCardLocation()
//                .equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
//                && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE))
//                && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
//                // System.out.println("take care attack monster to monster");
//                showOptionsToUser.showAttackMonsterToMonsterAlert(initialCardLocation, finalCardLocation);
//            }
//        }
//        if ((turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
//            && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
//            && finalTwoDimensionalPoint.getY() <= battleFieldView.getUpperLeftY() + 90
//            && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
//            && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX() + battleFieldView.getWidth()
//            || (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
//            && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
//            && finalTwoDimensionalPoint.getY() >= battleFieldView.getUpperLeftY()
//            + battleFieldView.getHeight() - 90
//            && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
//            && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX()
//            + battleFieldView.getWidth())
//            && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
//            // System.out.println("take care attack direct");
//            showOptionsToUser.showDirectAttackAlert(initialCardLocation);
//        }
//
//    }
//
//    private void draggingAction(MouseEvent previousMouseEvent, Object currentDroppedObject) {
//        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(previousMouseEvent, null);
//        if (cardLocationSelecting != null) {
//            String output = GameManager.getDuelControllerByIndex(token).getInput(
//                "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting),
//                true, token);
//            // System.out.println("&" + output);
//            Object card = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(cardLocationSelecting);
//            if (card != null) {
//                CardType cardType = GameManager.getDuelBoardByIndex(token).getCardByCardLocation(cardLocationSelecting)
//                    .getCardType();
//                if (cardType.equals(CardType.MONSTER)) {
//                    if (GameManager.getPhaseControllerByIndex(token).getPhaseInGame().equals(PhaseInGame.ALLY_MAIN_PHASE_1)
//                        || GameManager.getPhaseControllerByIndex(token).getPhaseInGame()
//                        .equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {
//
//                    }
//                } else if (cardType.equals(CardType.SPELL) || cardType.equals(CardType.TRAP)) {
//
//                }
//            }
//        }
//    }

    public void singleClickActionSpecial(TwoDimensionalPoint twoDimensionalPoint, CardView cardView) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(twoDimensionalPoint, cardView);
        if (cardLocationSelecting != null) {
            String output = JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getInput(\"card show --selected\", true, token)");
            // System.out.println("show card: " + output);
            String cardDescriptionUselessString = "Description: (.+)";
            Pattern pattern = Pattern.compile(cardDescriptionUselessString);
            Matcher matcher = pattern.matcher(output);
            String ourDescriptionString = "";
            if (matcher.find()) {
                ourDescriptionString = matcher.group(1);
            }
            CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(twoDimensionalPoint,
                cardView);
            // System.out.println("cardLocation picking is " + cardLocation.getRowOfCardLocation() + " and "
            //     + cardLocation.getIndex());
            moreCardInfoSection.updateCardMoreInfoSection(cardView, ourDescriptionString);
        }
    }

    public static void endOneRoundOfDuel(String result) {
        CustomDialog customDialog = new CustomDialog("CONFIRMATION", result, true);
        customDialog.openDialog();
    }

    public static void endGame(String result) {
        CustomDialog customDialog = new CustomDialog("CONFIRMATION", result, false);
        customDialog.openDialog();
    }


    public static AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public static Group getInformationRectangle() {
        return informationRectangle;
    }

    public static Card getCurrentCardForView() {
        return currentCardForView;
    }

    public static double getxHelperForCardViewConstructor() {
        return xHelperForCardViewConstructor;
    }

    public static double getyHelperForCardViewConstructor() {
        return yHelperForCardViewConstructor;
    }

    public static MouseEvent getPreviousMouseEvent() {
        return previousMouseEvent;
    }

    public static CardView getDraggingObject() {
        return draggingObject;
    }

    public static double getDraggingObjectX() {
        return draggingObjectX;
    }

    public static double getDraggingObjectY() {
        return draggingObjectY;
    }

    public static RowOfCardLocation getRowOfCardLocationOfFinalDraggedPoint() {
        return rowOfCardLocationOfFinalDraggedPoint;
    }

    public static RowOfCardLocation getRowOfCardLocationOfInitialDraggedPoint() {
        return rowOfCardLocationOfInitialDraggedPoint;
    }

    public boolean isDragFlag() {
        return this.dragFlag;
    }

    public int getClickCounter() {
        return clickCounter;
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public static CardLocation getCardLocationLookingRightNow() {
        return cardLocationLookingRightNow;
    }

    public static CardLocation getCardLocationSelecting() {
        return cardLocationSelecting;
    }

    public static boolean isIsWaitingForRightClickOptionListHit() {
        return isWaitingForRightClickOptionListHit;
    }

    public static Group getAllCards() {
        return allCards;
    }

//    public static CardLocation getCardLocationToSendCardTo() {
//        return cardLocationToSendCardTo;
//    }

//    public static NextPhaseButton getNextPhaseButton() {
//        return nextPhaseButton;
//    }

    public static Transition getTransition() {
        return transition;
    }

    public static ControllerForView getControllerForView() {
        return controllerForView;
    }

    public static Group getMoreCardInfoGroup() {
        return moreCardInfoGroup;
    }

    public static Rectangle getCardImageForCardMoreInfo() {
        return cardImageForCardMoreInfo;
    }

    public static Rectangle getCardAttributeForCardMoreInfo() {
        return cardAttributeForCardMoreInfo;
    }

    public static Rectangle getCardLevelStarForCardMoreInfo() {
        return cardLevelStarForCardMoreInfo;
    }

    public static Label getCardNameForCardMoreInfo() {
        return cardNameForCardMoreInfo;
    }

    public static Label getCardAttackForCardMoreInfo() {
        return cardAttackForCardMoreInfo;
    }

    public static Label getCardDefenseForCardMoreInfo() {
        return cardDefenseForCardMoreInfo;
    }

    public static Label getCardLevelForCardMoreInfo() {
        return cardLevelForCardMoreInfo;
    }

    public static Label getCardFamilyForCardMoreInfo() {
        return cardFamilyForCardMoreInfo;
    }


    public static ScrollPane getScrollPaneForCardMoreInfo() {
        return scrollPaneForCardMoreInfo;
    }

    public static VBox getvBox() {
        return vBox;
    }

    public static CardLocation getCardLocationDragDropped() {
        return cardLocationDragDropped;
    }

    public static MoreCardInfoSection getMoreCardInfoSection() {
        return moreCardInfoSection;
    }

    public static AdvancedCardMovingController getAdvancedCardMovingController() {
        return advancedCardMovingController;
    }

    public static void setStage(Stage stage) {
        DuelView.stage = stage;
    }

    public static void setStageWidth(double stageWidth) {
        DuelView.stageWidth = stageWidth;
    }

    public static void setStageHeight(double stageHeight) {
        DuelView.stageHeight = stageHeight;
    }

    public static void setAnchorPane(AnchorPane anchorPane) {
        DuelView.anchorPane = anchorPane;
    }

    public static void setBattleFieldView(BattleFieldView battleFieldView) {
        DuelView.battleFieldView = battleFieldView;
    }

    public static void setInformationRectangle(Group informationRectangle) {
        DuelView.informationRectangle = informationRectangle;
    }

    public static void setCurrentCardForView(Card currentCardForView) {
        DuelView.currentCardForView = currentCardForView;
    }

    public static void setxHelperForCardViewConstructor(double xHelperForCardViewConstructor) {
        DuelView.xHelperForCardViewConstructor = xHelperForCardViewConstructor;
    }

    public static void setyHelperForCardViewConstructor(double yHelperForCardViewConstructor) {
        DuelView.yHelperForCardViewConstructor = yHelperForCardViewConstructor;
    }

    public static void setPreviousMouseEvent(MouseEvent previousMouseEvent) {
        DuelView.previousMouseEvent = previousMouseEvent;
    }

    public static void setRowOfCardLocationOfFinalDraggedPoint(RowOfCardLocation rowOfCardLocationOfFinalDraggedPoint) {
        DuelView.rowOfCardLocationOfFinalDraggedPoint = rowOfCardLocationOfFinalDraggedPoint;
    }

    public static void setRowOfCardLocationOfInitialDraggedPoint(
        RowOfCardLocation rowOfCardLocationOfInitialDraggedPoint) {
        DuelView.rowOfCardLocationOfInitialDraggedPoint = rowOfCardLocationOfInitialDraggedPoint;
    }

    public void setDragFlag(boolean dragFlag) {
        this.dragFlag = dragFlag;
    }

    public static void setShouldDuelViewClickingAbilitiesWork(boolean shouldDuelViewClickingAbilitiesWork) {
        DuelView.shouldDuelViewClickingAbilitiesWork = shouldDuelViewClickingAbilitiesWork;
    }

    public void setClickCounter(int clickCounter) {
        this.clickCounter = clickCounter;
    }

    public void setExecutor(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public static void setCardLocationLookingRightNow(CardLocation cardLocationLookingRightNow) {
        DuelView.cardLocationLookingRightNow = cardLocationLookingRightNow;
    }

    public static void setCardLocationSelecting(CardLocation cardLocationSelecting) {
        DuelView.cardLocationSelecting = cardLocationSelecting;
    }

    public static void setIsWaitingForRightClickOptionListHit(boolean isWaitingForRightClickOptionListHit) {
        DuelView.isWaitingForRightClickOptionListHit = isWaitingForRightClickOptionListHit;
    }

    public static void setAllCards(Group allCards) {
        DuelView.allCards = allCards;
    }

//    public static void setCardLocationToSendCardTo(CardLocation cardLocationToSendCardTo) {
//        DuelView.cardLocationToSendCardTo = cardLocationToSendCardTo;
//    }

    public static CardLocation getCardLocationBeingDragged() {
        return cardLocationBeingDragged;
    }

    public static void setCardLocationBeingDragged(CardLocation cardLocationBeingDragged) {
        DuelView.cardLocationBeingDragged = cardLocationBeingDragged;
        // System.out.println("cardLocationBeingDragged is " + cardLocationBeingDragged.getRowOfCardLocation() + " "
        //     + cardLocationBeingDragged.getIndex());
    }

    public static SendingRequestsToServer getSendingRequestsToServer() {
        return sendingRequestsToServer;
    }

    public static AlertAndMenuItems getShowOptionsToUser() {
        return showOptionsToUser;
    }

    public static void printChildrenInGroups() {
        // System.out.println("ALLY CARDS IN DECK GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("ALLY CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("ALLY CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)
            .size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("OPPONENT CARDS IN DECK GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE)
            .size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            //     .giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("OPPONENT CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE)
            .size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            //     .giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("OPPONENT CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)
            .size(); i++) {
            // System.out.println(((project.client.modelsforview.CardView) controllerForView
            //     .giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE).get(i)).getCard()
            //     .getCardName());
        }
    }

}
