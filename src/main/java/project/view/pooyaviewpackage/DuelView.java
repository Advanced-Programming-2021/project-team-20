package project.view.pooyaviewpackage;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.controller.duel.GamePackage.PhaseInGame;
import project.controller.duel.PreliminaryPackage.FakeMain;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.model.cardData.General.Card;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.modelsforview.*;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DuelView extends Application {
    // launch the application
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
    private static project.model.modelsforview.CardView draggingObject;
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
    private static CardLocation cardLocationToSendCardTo;
    private static NextPhaseButton nextPhaseButton;
    private static Transition transition;
    private static ControllerForView controllerForView;
    private static Group moreCardInfoGroup;
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

    public DuelView() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    @Override
    public void start(Stage stage) {
        FakeMain.call();
        prepareArrayListsForWorking();
        DuelView.stage = stage;
        stage.setTitle("Duel Page");
        anchorPane = new AnchorPane();
        System.out.println(battleFieldView == null);
        anchorPane.setOnMouseClicked(e -> {
            if (shouldDuelViewClickingAbilitiesWork) {
                TwoDimensionalPoint finalTwoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
                CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint, null);
                System.out.println("is cardLocation null " + (cardLocation == null));
                if (cardLocation != null) {
                    CardView cardView = controllerForView.getCardViewByCardLocation(cardLocation);
                    System.out.println("SHOW ME NO MORE cardLocation clicked is " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex());
                    if (cardView == null) {
                        System.out.println("clicked on null SHOW ME NO MORE");
                    } else {
                        Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
                        System.out.println("clicked on " + cardView.getCard().getCardName() + " " + bounds.getMinX() + " " + bounds.getMinY());
                        if (e.getButton().equals(MouseButton.PRIMARY)) {
                            if (!dragFlag) {
                                if (e.getClickCount() == 1) {
                                    scheduledFuture = executor.schedule(() -> singleClickActionSpecial(finalTwoDimensionalPoint, cardView), 300, TimeUnit.MILLISECONDS);
                                } else if (e.getClickCount() > 1) {
                                    if (scheduledFuture != null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
                                        scheduledFuture.cancel(false);
                                        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint, cardView);
                                        System.out.println("cardLocationSelecting is " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());

                                        if (cardLocationSelecting != null) {
                                            String output = GameManager.getDuelControllerByIndex(0).getInput("select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
                                            System.out.println("&@&@&@&@& " + output);

                                            if (!output.contains("selected")) {
                                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                alert.setTitle("Information Dialog");
                                                alert.setHeaderText("Select Card Message");
                                                alert.setContentText(output);
                                                alert.showAndWait();
                                                if (!output.contains("no card found")) {
                                                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            dragFlag = false;
                        } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                            System.out.println("terente x = " + e.getSceneX() + " y = " + e.getSceneY());
                            cardView.updateContextMenu();
                            cardView.getContextMenu().show(cardView, e.getSceneX() + bounds.getMinX() / 2 + 30, e.getSceneY() + 30);
                        }
                    }
                }
            }
        });
        anchorPane.setOnDragOver(e -> {
            if (draggingObject != null &&
                e.getDragboard().hasString()
                && e.getDragboard().getString().equals("card")) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        anchorPane.setOnDragDropped(e -> {
            shouldDuelViewClickingAbilitiesWork = true;
            if (draggingObject != null &&
                e.getDragboard().hasString()
                && e.getDragboard().getString().equals("card")) {
                System.out.println("BEING DROPPED BUUUUUURN!");
                TwoDimensionalPoint finalTwoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
                System.out.println("BEING DROPPED BUUUUUURN!");
                cardLocationDragDropped = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint, null);
                System.out.println("BEING DROPPED BUUUUUURN!");
                System.out.println("cardLocationDragDropped is " + cardLocationDragDropped.getRowOfCardLocation() + " " + cardLocationDragDropped.getIndex());
                takeCareOfDraggingAction(cardLocationBeingDragged, cardLocationDragDropped, draggingObject, finalTwoDimensionalPoint);
                System.out.println("wow");
                draggingObject = null;
                cardLocationBeingDragged = null;
                cardLocationDragDropped = null;
                previousMouseEvent = null;
                draggingObjectX = -1;
                draggingObjectY = -1;
                e.setDropCompleted(true);
            }
            e.consume();
        });

        Scene scene = new Scene(anchorPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
        stageWidth = scene.getWidth();
        stageHeight = scene.getHeight();
        prepareObjectsForWorking();
        anchorPane.getChildren().add(battleFieldView);
        anchorPane.getChildren().add(allCards);
        anchorPane.getChildren().addAll(nextPhaseButton, nextPhaseButton.getLabel());
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
        controllerForView.giveCardsAtTheBeginningOfGame();
        System.out.println(stage.getWidth());
        System.out.println(stage.getHeight());
    }


    public static void main(String args[]) {
        launch(args);
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

    public static double getYHelperForCardViewConstructor() {
        return yHelperForCardViewConstructor;
    }

    public static void setDraggingObject(project.model.modelsforview.CardView draggingObject) {
        DuelView.draggingObject = draggingObject;
    }

    public static void setDraggingObjectX(double draggingObjectX) {
        DuelView.draggingObjectX = draggingObjectX;
    }

    public static void setDraggingObjectY(double draggingObjectY) {
        DuelView.draggingObjectY = draggingObjectY;
    }

    private void prepareArrayListsForWorking() {
        allCards = new Group();
        nextPhaseButton = new NextPhaseButton();
        transition = new Transition();
        controllerForView = new ControllerForView();
        sendingRequestsToServer = new SendingRequestsToServer();
        advancedCardMovingController = new AdvancedCardMovingController();
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

    private void prepareObjectsForWorking() {
        battleFieldView = new BattleFieldView();

        drawPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_DRAW_PHASE);
        standByPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_STANDBY_PHASE);
        mainPhaseOneLabel = new GamePhaseButton(PhaseInGame.ALLY_MAIN_PHASE_1);
        battlePhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_BATTLE_PHASE);
        mainPhaseTwoLabel = new GamePhaseButton(PhaseInGame.ALLY_MAIN_PHASE_2);
        endPhaseLabel = new GamePhaseButton(PhaseInGame.ALLY_END_PHASE);
        nextPhaseLabel = new GamePhaseButton(null);
        nextPhaseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(GameManager.getDuelControllerByIndex(0).getInput("advanced show board", true));
            }
        });
        nextPhaseLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String output = GameManager.getDuelControllerByIndex(0).getInput("next phase", true);
                System.out.println("&" + output + "&");
                PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
//                if (output.contains("end phase")) {
//                    try {
//                        Thread.sleep(300);
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                drawPhaseLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                                standByPhaseLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                                mainPhaseOneLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                                battlePhaseLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                                mainPhaseTwoLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                                endPhaseLabel.updateImage(PhaseInGame.ALLY_END_PHASE);
//                            }
//                        });
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (output.contains("draw phase")) {
//                    try {
//                        Thread.sleep(300);
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                drawPhaseLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                                standByPhaseLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                                mainPhaseOneLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                                battlePhaseLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                                mainPhaseTwoLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                                endPhaseLabel.updateImage(PhaseInGame.ALLY_DRAW_PHASE);
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (output.contains("standby phase")) {
//                    try {
//                        Thread.sleep(300);
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                drawPhaseLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                                standByPhaseLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                                mainPhaseOneLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                                battlePhaseLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                                mainPhaseTwoLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                                endPhaseLabel.updateImage(PhaseInGame.ALLY_STANDBY_PHASE);
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (output.contains("main phase 1")) {
//                    try {
//                        Thread.sleep(300);
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                drawPhaseLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                                standByPhaseLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                                mainPhaseOneLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                                battlePhaseLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                                mainPhaseTwoLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                                endPhaseLabel.updateImage(PhaseInGame.ALLY_MAIN_PHASE_1);
//                            }
//                        });
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!(output.contains("draw phase") || output.contains("standby phase") || output.contains("end phase") || output.contains("main phase 1"))) {
//                    drawPhaseLabel.updateImage(phaseInGame);
                    standByPhaseLabel.updateImage(phaseInGame);
                    mainPhaseOneLabel.updateImage(phaseInGame);
                    battlePhaseLabel.updateImage(phaseInGame);
                    mainPhaseTwoLabel.updateImage(phaseInGame);
                    endPhaseLabel.updateImage(phaseInGame);
     //           }
                if (output.contains("phase: draw phase") && output.contains("new card added to hand:")) {
                    DuelView.getAdvancedCardMovingController().advanceForwardBattleField();
                    //transition.applyTransitionForDrawingACard(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
                }


//                ArrayList<project.model.modelsforview.CardView> allymonsters = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_MONSTER_ZONE);
//                for (project.model.modelsforview.CardView cardView : allymonsters) {
//                    System.out.println("gua monster " + cardView.getCard().getCardName());
//                }
//
//                ArrayList<project.model.modelsforview.CardView> allyhands = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
//                for (project.model.modelsforview.CardView cardView : allyhands) {
//                    System.out.println("gua hand " + cardView.getCard().getCardName());
//                }
//
//                ArrayList<project.model.modelsforview.CardView> allygraveyards = controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
//                for (project.model.modelsforview.CardView cardView : allygraveyards) {
//                    System.out.println("gua graveyard " + cardView.getCard().getCardName());
//                }
            }
        });

        System.out.println(" what upper left x  we see is " + battleFieldView.getUpperLeftX());
        System.out.println("what height we see is " + battleFieldView.getHeight());
        ArrayList<Card> allyCardsInHand = GameManager.getDuelBoardByIndex(0).getAllyCardsInHand();
        ArrayList<Card> allyCardsInDeck = GameManager.getDuelBoardByIndex(0).getAllyCardsInDeck();
        for (int i = 0; i < allyCardsInDeck.size() + allyCardsInHand.size(); i++) {
            if (i < allyCardsInHand.size()) {
                currentCardForView = allyCardsInHand.get(i);
            } else {
                currentCardForView = allyCardsInDeck.get(i - allyCardsInHand.size());
            }
            xHelperForCardViewConstructor = battleFieldView.getUpperLeftX() + battleFieldView.getWidth() - CardView.getCardWidth() - 7;
            yHelperForCardViewConstructor = battleFieldView.getUpperLeftY() + battleFieldView.getHeight() - 2 * CardView.getCardHeight() + 20;
            CardView cardView = new CardView(currentCardForView, true, RowOfCardLocation.ALLY_DECK_ZONE, this);
            cardView.applyClickingAbilitiesToCardView(this);
            cardView.applyDragDetectingAbilityToCardView();
            System.out.println("preparing " + cardView.getCard().getCardName());
            allCards.getChildren().add(cardView);
        }

        ArrayList<Card> opponentCardsInHand = GameManager.getDuelBoardByIndex(0).getOpponentCardsInHand();
        ArrayList<Card> opponentCardsInDeck = GameManager.getDuelBoardByIndex(0).getOpponentCardsInDeck();
        for (int i = 0; i < opponentCardsInDeck.size() + opponentCardsInHand.size(); i++) {
            if (i < opponentCardsInHand.size()) {
                currentCardForView = opponentCardsInHand.get(i);
            } else {
                currentCardForView = opponentCardsInDeck.get(i - opponentCardsInHand.size());
            }
            xHelperForCardViewConstructor = battleFieldView.getUpperLeftX() + 40;
            yHelperForCardViewConstructor = battleFieldView.getUpperLeftY() + 108;
            CardView cardView = new CardView(currentCardForView, true, RowOfCardLocation.OPPONENT_DECK_ZONE, this);
            cardView.applyClickingAbilitiesToCardView(this);
            cardView.applyDragDetectingAbilityToCardView();
            System.out.println("preparing " + cardView.getCard().getCardName());
            allCards.getChildren().add(cardView);
        }
        moreCardInfoSection.updateCardMoreInfoSection((project.model.modelsforview.CardView) allCards.getChildren().get(0));
    }


    @Override
    public void stop() {
        executor.shutdown();
    }

    public void doubleClickAction(MouseEvent mouseEvent) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
        System.out.println("cardLocationSelecting is " + cardLocationSelecting.getRowOfCardLocation() + " " + cardLocationSelecting.getIndex());

        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput("select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
            System.out.println("&" + output);
        }
    }

    private void takeCareOfDraggingAction(CardLocation initialCardLocation, CardLocation finalCardLocation, project.model.modelsforview.CardView cardViewBeingDragged, TwoDimensionalPoint finalTwoDimensionalPoint) {
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
        boolean allySummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2);
        boolean opponentSummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2);
        if (finalCardLocation != null) {
            if ((turn == 1 && allySummonSetActivateCardPhase
                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
                ||
                (turn == 2 && opponentSummonSetActivateCardPhase
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)))
                &&
                (cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL) || cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP))) {
                if ((turn == 1 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                    || turn == 2 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
                    && cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP)) {
                    System.out.println("take care set trap");
                    showOptionsToUser.showSetAlertForTrapCard(cardViewBeingDragged, initialCardLocation);
                } else if ((turn == 1 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                    || turn == 2 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) &&
                    cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
                    && !((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    System.out.println("take care set activate spell");
                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);

                } else if ((turn == 1 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
                    || turn == 2 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) &&
                    cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL) &&
                    ((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    System.out.println("take care set activate spell field card");
                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);
                }
            } else if (turn == 1 && allySummonSetActivateCardPhase
                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                ||
                (turn == 2 && opponentSummonSetActivateCardPhase
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE) && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) &&
                    (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
                System.out.println("take care show all summon options");
                showOptionsToUser.showAllSummonOptionsAlertForMonsterCard(cardViewBeingDragged, initialCardLocation, this);

            } else if (
                (turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
                    ||
                    (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
                        && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) &&
                    (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
                System.out.println("take care attack monster to monster");
                showOptionsToUser.showAttackMonsterToMonsterAlert(initialCardLocation, finalCardLocation);
            }
        }
        if ((turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
            && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) &&
            finalTwoDimensionalPoint.getY() <= battleFieldView.getUpperLeftY() + 90 && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
            && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX() + battleFieldView.getWidth() ||
            (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE) &&
                finalTwoDimensionalPoint.getY() >= battleFieldView.getUpperLeftY() + battleFieldView.getHeight() - 90 && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
                && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX() + battleFieldView.getWidth()) &&
            (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
            System.out.println("take care attack direct");
            showOptionsToUser.showDirectAttackAlert(initialCardLocation);
        }

    }

    private void draggingAction(MouseEvent previousMouseEvent, Object currentDroppedObject) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(previousMouseEvent, null);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput("select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
            System.out.println("&" + output);
            Object card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocationSelecting);
            if (card != null) {
                CardType cardType = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocationSelecting).getCardType();
                if (cardType.equals(CardType.MONSTER)) {
                    if (GameManager.getPhaseControllerByIndex(0).getPhaseInGame().equals(PhaseInGame.ALLY_MAIN_PHASE_1) || GameManager.getPhaseControllerByIndex(0).getPhaseInGame().equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {

                    }
                } else if (cardType.equals(CardType.SPELL) || cardType.equals(CardType.TRAP)) {

                }
            }
        }
    }


    public void singleClickAction(MouseEvent mouseEvent, CardView cardView) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput("select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
            //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
            System.out.println("single click: " + output);
            output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
            System.out.println("show card: " + output);
            CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
            System.out.println("cardLocation picking is " + cardLocation.getRowOfCardLocation() + " and " + cardLocation.getIndex());
            moreCardInfoSection.updateCardMoreInfoSection(cardView);
        }
    }

    public void singleClickActionSpecial(TwoDimensionalPoint twoDimensionalPoint, CardView cardView) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(twoDimensionalPoint, cardView);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput("select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting), true);
            //String output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
            System.out.println("single click: " + output);
            output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
            System.out.println("show card: " + output);
            CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(twoDimensionalPoint, cardView);
            System.out.println("cardLocation picking is " + cardLocation.getRowOfCardLocation() + " and " + cardLocation.getIndex());
            moreCardInfoSection.updateCardMoreInfoSection(cardView);
        }
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

    public static project.model.modelsforview.CardView getDraggingObject() {
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

    public static CardLocation getCardLocationToSendCardTo() {
        return cardLocationToSendCardTo;
    }

    public static NextPhaseButton getNextPhaseButton() {
        return nextPhaseButton;
    }

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

    public static boolean isShouldDuelViewClickingAbilitiesWork() {
        return shouldDuelViewClickingAbilitiesWork;
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

    public static void setRowOfCardLocationOfInitialDraggedPoint(RowOfCardLocation rowOfCardLocationOfInitialDraggedPoint) {
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

    public static void setCardLocationToSendCardTo(CardLocation cardLocationToSendCardTo) {
        DuelView.cardLocationToSendCardTo = cardLocationToSendCardTo;
    }

    public static CardLocation getCardLocationBeingDragged() {
        return cardLocationBeingDragged;
    }

    public static void setCardLocationBeingDragged(CardLocation cardLocationBeingDragged) {
        DuelView.cardLocationBeingDragged = cardLocationBeingDragged;
        System.out.println("cardLocationBeingDragged is " + cardLocationBeingDragged.getRowOfCardLocation() + " " + cardLocationBeingDragged.getIndex());
    }

    public static SendingRequestsToServer getSendingRequestsToServer() {
        return sendingRequestsToServer;
    }

    public static AlertAndMenuItems getShowOptionsToUser() {
        return showOptionsToUser;
    }

    public static void printChildrenInGroups() {
        System.out.println("ALLY CARDS IN DECK GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("ALLY CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("ALLY CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("OPPONENT CARDS IN DECK GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("OPPONENT CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE).get(i)).getCard().getCardName());
        }

        System.out.println("OPPONENT CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE).size(); i++) {
            System.out.println(((project.model.modelsforview.CardView) controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE).get(i)).getCard().getCardName());
        }
    }

}