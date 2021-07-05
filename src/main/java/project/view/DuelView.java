package project.view;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.controller.duel.GamePackage.PhaseInGame;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.cheat.Cheat;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import javafx.scene.control.*;
import javafx.scene.layout.*;
import project.model.cardData.General.*;
import project.model.modelsforview.BattleFieldView;
import project.model.modelsforview.CardView;
import project.model.modelsforview.NextPhaseButton;
import project.model.modelsforview.TwoDimensionalPoint;


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
    private static CardView draggingObject;
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
    private static AlertAndMenuItems showOptionsToUser;
    private static MoreCardInfoSection moreCardInfoSection;
    private static Long lastTimeKeyPressed = 0l;
    private static Cheat cheat = new Cheat();
    private static StringBuilder cheatCodes = new StringBuilder();

    public DuelView() {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    @Override
    public void start(Stage stage) {
        // FakeMain.call();
        prepareArrayListsForWorking();
        //
        // DuelView.stage = stage;
        Image backgroundColor = new Image(
                DuelView.class.getResource("/project/battlefield/fie_normal.bmp").toExternalForm());

        BackgroundSize backgroundSize = new BackgroundSize(300, 500, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(backgroundColor, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        // stage.setTitle("Duel Page");
        anchorPane = new AnchorPane();
       
        // System.out.println(battleFieldView == null);
        // anchorPane.setOnMouseClicked();
        /*
         * if i really want to put these options i must not call
         * anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
         * 
         * @Override public void handle(MouseEvent e) { if
         * (e.getButton().equals(MouseButton.PRIMARY)) { dragFlag = true; } } });
         * 
         * anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
         * 
         * @Override public void handle(MouseEvent e) { if
         * (e.getButton().equals(MouseButton.PRIMARY)) { if (!dragFlag) {
         * System.out.println(++clickCounter + " " + e.getClickCount()); if
         * (e.getClickCount() == 1) { //scheduledFuture = executor.schedule(() ->
         * singleClickAction(e.getX(), e.getY()), 10000, TimeUnit.MILLISECONDS); } else
         * if (e.getClickCount() > 1) { if (scheduledFuture != null &&
         * !scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
         * scheduledFuture.cancel(false); doubleClickAction(e); } } } dragFlag = false;
         * } else if (e.getButton().equals(MouseButton.SECONDARY)) {
         * rightClickAction(e); } } });
         * 
         * anchorPane.setOnDragOver(e -> { if (draggingObject != null &&
         * e.getDragboard().hasString() && e.getDragboard().getString().equals("card"))
         * { e.acceptTransferModes(TransferMode.COPY); }
         * //coordinateLabel.setText(String.format("[%.1f, %.1f]", e.getX(), e.getY()));
         * });
         */

        anchorPane.setOnDragOver(e -> {
            if (draggingObject != null && e.getDragboard().hasString() && e.getDragboard().getString().equals("card")) {
                e.acceptTransferModes(TransferMode.MOVE);
                // e.consume();
            }
        });
        anchorPane.setOnDragDropped(e -> {
            if (draggingObject != null && e.getDragboard().hasString() && e.getDragboard().getString().equals("card")) {
                // System.out.println("BEING DROPPED BUUUUUURN!");
                // System.out.println("initial x is " + draggingObjectX + " and initial y is " +
                // draggingObjectY);
                // draggingAction(previousMouseEvent, e.getSource());
                // System.out.println("final x is " + e.getX() + " and final y is " + e.getY());
                TwoDimensionalPoint finalTwoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
                cardLocationDragDropped = controllerForView.giveCardLocationByCoordinateInView(finalTwoDimensionalPoint,
                        null);
                // System.out.println("cardLocationDragDropped is " +
                // cardLocationDragDropped.getRowOfCardLocation() + " "
                // + cardLocationDragDropped.getIndex());
                takeCareOfDraggingAction(cardLocationBeingDragged, cardLocationDragDropped, draggingObject,
                        finalTwoDimensionalPoint);
                // System.out.println("wow");
                draggingObject = null;
                cardLocationBeingDragged = null;
                cardLocationDragDropped = null;
                previousMouseEvent = null;
                draggingObjectX = -1;
                draggingObjectY = -1;
                e.setDropCompleted(true);
                // e.consume();
            }
        });

        anchorPane.setBackground(new Background(backgroundImage));
        Scene scene = new Scene(anchorPane, 1200, 1000);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                checkCheatCommands(keyEvent);
            }
        });

        // MainView.changeScene(anchorPane);
        // set the scene
        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.show();
        stageWidth = scene.getWidth();
        stageHeight = scene.getHeight();
        prepareObjectsForWorking();
        anchorPane.getChildren().add(battleFieldView);
        // anchorPane.getChildren().add(informationRectangle);
        // anchorPane.getChildren().addAll(cardViewsInAllyHand);
        // anchorPane.getChildren().addAll(cardViewsInOpponentHand);
        anchorPane.getChildren().add(allCards);
        anchorPane.getChildren().addAll(nextPhaseButton, nextPhaseButton.getLabel());
        // anchorPane.getChildren().add(moreCardInfoPane);
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
        controllerForView.giveCardsAtTheBeginningOfGame();
        // System.out.println(stage.getWidth());
        // System.out.println(stage.getHeight());

        //
        // Image image = new
        // Image(DuelView.class.getResource("/project/cards/monsters/AlexandriteDragon.jpg").toExternalForm());
        //
        // scene.setCursor(new ImageCursor(image,
        // image.getWidth() / 2,
        // image.getHeight() /2));

    }

    // public static void main(String args[]) {
    // launch(args);
    // }

    protected void checkCheatCommands(KeyEvent keyEvent) {
        Long currentTimeKeyPressed = System.currentTimeMillis();
        if (currentTimeKeyPressed - lastTimeKeyPressed > 5000) {
            cheatCodes.delete(0, cheatCodes.length());
        }
        lastTimeKeyPressed = currentTimeKeyPressed;
        if (keyEvent.getCode().getName().equals("Space")) {
            cheatCodes.append(" ");
        } else if (keyEvent.getCode().getName().equals("Enter")) {
            cheat.findCheatCommand(cheatCodes.toString(), 0);
            cheatCodes.delete(0, cheatCodes.length());
        } else if(keyEvent.getCode().getName().startsWith("Numpad")){
            cheatCodes.append(keyEvent.getCode().getName().charAt(keyEvent.getCode().getName().length() - 1));
        } else {
            cheatCodes.append(keyEvent.getCode().getName().toLowerCase());
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

    public static double getYHelperForCardViewConstructor() {
        return yHelperForCardViewConstructor;
    }

    public static void setDraggingObject(CardView draggingObject) {
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
        nextPhaseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String output = GameManager.getDuelControllerByIndex(0).getInput("next phase", true);
                // System.out.println("&" + output);
                if (output.contains("phase: draw phase") && output.contains("new card added to hand:")) {
                    transition.applyTransitionForDrawingACard(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
                }
                for (Node cardView : allCards.getChildren()) {
                    ((CardView) cardView).updateContextMenu();
                }
            }
        });
    }

    private void prepareObjectsForWorking() {
        battleFieldView = new BattleFieldView();
        // System.out.println(" what upper left x we see is " +
        // battleFieldView.getUpperLeftX());
        // System.out.println("what height we see is " + battleFieldView.getHeight());
        ArrayList<Card> allyCardsInHand = GameManager.getDuelBoardByIndex(0).getAllyCardsInHand();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            // System.out.println("its done " + allyCardsInHand.get(i).getCardName());
        }
        ArrayList<Card> allyCardsInDeck = GameManager.getDuelBoardByIndex(0).getAllyCardsInDeck();
        for (int i = 0; i < allyCardsInDeck.size(); i++) {
            // System.out.println("its done " + allyCardsInDeck.get(i).getCardName());
        }
        for (int i = 0; i < allyCardsInDeck.size() + allyCardsInHand.size(); i++) {
            if (i < allyCardsInHand.size()) {
                currentCardForView = allyCardsInHand.get(i);
            } else {
                currentCardForView = allyCardsInDeck.get(i - allyCardsInHand.size());
            }
            // System.out.println("dont make me mad " + currentCardForView.getCardName());
            xHelperForCardViewConstructor = battleFieldView.getUpperLeftX() + battleFieldView.getWidth()
                    - CardView.getCardWidth() - 7;
            yHelperForCardViewConstructor = battleFieldView.getUpperLeftY() + battleFieldView.getHeight()
                    - 2 * CardView.getCardHeight() + 20;
            CardView cardView = new CardView(currentCardForView, true, RowOfCardLocation.ALLY_DECK_ZONE);
            applyDragDetectingAbilityToCardView(cardView);
            applyClickingAbilitiesToCardView(cardView);
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
            CardView cardView = new CardView(currentCardForView, true, RowOfCardLocation.OPPONENT_DECK_ZONE);
            applyDragDetectingAbilityToCardView(cardView);
            applyClickingAbilitiesToCardView(cardView);
            allCards.getChildren().add(cardView);
            // , new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1)
        }
        moreCardInfoSection.updateCardMoreInfoSection((CardView) allCards.getChildren().get(0));
    }

    private void applyDragDetectingAbilityToCardView(CardView cardView) {
        cardView.setOnDragDetected(e -> {
            Dragboard db = cardView.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(cardView.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.putString("card");
            db.setContent(cc);
            // System.out.println("ALERT");
            // System.out.println("card being drag detected");
            TwoDimensionalPoint twoDimensionalPoint = new TwoDimensionalPoint(e.getSceneX(), e.getSceneY());
            // System.out.println("twodim point x is " + twoDimensionalPoint.getX());
            // System.out.println("twodim y is " + twoDimensionalPoint.getY());
            DuelView.setCardLocationBeingDragged(
                    DuelView.getControllerForView().giveCardLocationByCoordinateInView(twoDimensionalPoint, cardView));
            //
            // Image image = new
            // Image(CardView.class.getResource("/project/cards/monsters/AlexandriteDragon.jpg").toExternalForm());
            //
            // getScene().setCursor(new ImageCursor(image,
            // image.getWidth() / 2,
            // image.getHeight() /2));
            //
            DuelView.setDraggingObject(cardView);
            DuelView.setDraggingObjectX(e.getSceneX());
            DuelView.setDraggingObjectY(e.getSceneY());
            // e.consume();
        });
    }

    private void applyClickingAbilitiesToCardView(CardView cardView) {

        cardView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (!dragFlag) {
                        // System.out.println(++clickCounter + " " + e.getClickCount());
                        if (e.getClickCount() == 1) {
                            scheduledFuture = executor.schedule(() -> singleClickAction(e, cardView), 300,
                                    TimeUnit.MILLISECONDS);
                        } else if (e.getClickCount() > 1) {
                            if (scheduledFuture != null && !scheduledFuture.isCancelled()
                                    && !scheduledFuture.isDone()) {
                                scheduledFuture.cancel(false);
                                doubleClickAction(e);
                            }
                        }
                    }
                    dragFlag = false;
                } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                    // rightClickAction(e);
                }
                // e.consume();
            }
        });

        cardView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    dragFlag = true;
                }
                // e.consume();
            }
        });
        // cardView.setOnDragDropped(e -> {
        // if (draggingObject!= null && e.getDragboard().hasString()
        // && e.getDragboard().getString().equals("card")) {
        // draggingObject = null;
        // cardLocationDragDropped =
        // controllerForView.giveCardLocationByCoordinateInView(e,null);
        // System.out.println("cardLocationDragDropped is
        // "+cardLocationDragDropped.getRowOfCardLocation()+"
        // "+cardLocationDragDropped.getIndex());
        // System.out.println("wow");
        // e.setDropCompleted(true);
        // }
        // });

        // When user right-click on Circle
        cardView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                cardView.getContextMenu().show(cardView, event.getScreenX(), event.getScreenY());
            }
        });
    }

    @Override
    public void stop() {
        executor.shutdown();
    }

    private void doubleClickAction(MouseEvent mouseEvent) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput(
                    "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting),
                    true);
            // String output = GameManager.getDuelControllerByIndex(0).getInput("card show
            // --selected", true);
            // System.out.println("&" + output);
        }
        // System.out.println("Double-click action executed.");
    }

    private void takeCareOfDraggingAction(CardLocation initialCardLocation, CardLocation finalCardLocation,
            CardView cardViewBeingDragged, TwoDimensionalPoint finalTwoDimensionalPoint) {
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
        PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
        boolean allySummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1)
                || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2);
        boolean opponentSummonSetActivateCardPhase = phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1)
                || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2);
        if (finalCardLocation != null) {
            if ((turn == 1 && allySummonSetActivateCardPhase
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
                    || (turn == 2 && opponentSummonSetActivateCardPhase
                            && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)))
                    && (cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
                            || cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP))) {
                if ((turn == 1 && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                        || turn == 2 && finalCardLocation.getRowOfCardLocation()
                                .equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
                        && cardViewBeingDragged.getCard().getCardType().equals(CardType.TRAP)) {
                    // System.out.println("take care set trap");
                    showOptionsToUser.showSetAlertForTrapCard(cardViewBeingDragged, initialCardLocation);
                } else if ((turn == 1
                        && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE)
                        || turn == 2 && finalCardLocation.getRowOfCardLocation()
                                .equals(RowOfCardLocation.OPPONENT_SPELL_ZONE))
                        && cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
                        && !((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue()
                                .equals(SpellCardValue.FIELD)) {
                    // System.out.println("take care set activate spell");
                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);

                } else if ((turn == 1
                        && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)
                        || turn == 2 && finalCardLocation.getRowOfCardLocation()
                                .equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE))
                        && cardViewBeingDragged.getCard().getCardType().equals(CardType.SPELL)
                        && ((SpellCard) cardViewBeingDragged.getCard()).getSpellCardValue()
                                .equals(SpellCardValue.FIELD)) {
                    // System.out.println("take care set activate spell field card");
                    showOptionsToUser.showSetOrActivateForSpellCard(cardViewBeingDragged, initialCardLocation);
                }
            } else if (turn == 1 && allySummonSetActivateCardPhase
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE)
                    && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                    || (turn == 2 && opponentSummonSetActivateCardPhase
                            && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE)
                            && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))
                            && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
                // System.out.println("take care show all summon options");
                showOptionsToUser.showAllSummonOptionsAlertForMonsterCard(cardViewBeingDragged, initialCardLocation);
            } else if ((turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
                    && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                    && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
                    || (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
                            && initialCardLocation.getRowOfCardLocation()
                                    .equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
                            && finalCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE))
                    && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
                // System.out.println("take care attack monster to monster");
                showOptionsToUser.showAttackMonsterToMonsterAlert(initialCardLocation, finalCardLocation);
            }
        }
        if ((turn == 1 && phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE)
                && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE)
                && finalTwoDimensionalPoint.getY() <= battleFieldView.getUpperLeftY() + 90
                && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
                && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX() + battleFieldView.getWidth()
                || (turn == 2 && phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE))
                        && initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)
                        && finalTwoDimensionalPoint.getY() >= battleFieldView.getUpperLeftY()
                                + battleFieldView.getHeight() - 90
                        && finalTwoDimensionalPoint.getX() >= battleFieldView.getUpperLeftX()
                        && finalTwoDimensionalPoint.getX() <= battleFieldView.getUpperLeftX()
                                + battleFieldView.getWidth())
                && (cardViewBeingDragged.getCard().getCardType().equals(CardType.MONSTER))) {
            // System.out.println("take care attack direct");
            showOptionsToUser.showDirectAttackAlert(initialCardLocation);
        }

    }

    private void draggingAction(MouseEvent previousMouseEvent, Object currentDroppedObject) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(previousMouseEvent, null);
        // giveCardLocationByCoordinateInView(currentDroppedObject);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput(
                    "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting),
                    true);
            // String output = GameManager.getDuelControllerByIndex(0).getInput("card show
            // --selected", true);
            System.out.println("&" + output);
            Object card = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocationSelecting);
            if (card != null) {
                CardType cardType = GameManager.getDuelBoardByIndex(0).getCardByCardLocation(cardLocationSelecting)
                        .getCardType();
                if (cardType.equals(CardType.MONSTER)) {
                    if (GameManager.getPhaseControllerByIndex(0).getPhaseInGame().equals(PhaseInGame.ALLY_MAIN_PHASE_1)
                            || GameManager.getPhaseControllerByIndex(0).getPhaseInGame()
                                    .equals(PhaseInGame.ALLY_MAIN_PHASE_2)) {

                    }
                } else if (cardType.equals(CardType.SPELL) || cardType.equals(CardType.TRAP)) {

                }
            }
        }
    }

    private void singleClickAction(MouseEvent mouseEvent, CardView cardView) {
        cardLocationSelecting = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
        if (cardLocationSelecting != null) {
            String output = GameManager.getDuelControllerByIndex(0).getInput(
                    "select " + SendingRequestsToServer.giveStringToGiveToServerByCardLocation(cardLocationSelecting),
                    true);
            // String output = GameManager.getDuelControllerByIndex(0).getInput("card show
            // --selected", true);
            // System.out.println("single click: " + output);
            output = GameManager.getDuelControllerByIndex(0).getInput("card show --selected", true);
            // System.out.println("show card: " + output);
            CardLocation cardLocation = controllerForView.giveCardLocationByCoordinateInView(mouseEvent, null);
            // System.out.println("cardLocation picking is " +
            // cardLocation.getRowOfCardLocation() + " and "
            // + cardLocation.getIndex());
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
        return dragFlag;
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

    public static void setNextPhaseButton(NextPhaseButton nextPhaseButton) {
        DuelView.nextPhaseButton = nextPhaseButton;
    }

    public static void setTransition(Transition transition) {
        DuelView.transition = transition;
    }

    public static void setControllerForView(ControllerForView controllerForView) {
        DuelView.controllerForView = controllerForView;
    }

    public static CardLocation getCardLocationBeingDragged() {
        return cardLocationBeingDragged;
    }

    public static void setCardLocationBeingDragged(CardLocation cardLocationBeingDragged) {
        DuelView.cardLocationBeingDragged = cardLocationBeingDragged;
        // System.out.println("cardLocationBeingDragged is " +
        // cardLocationBeingDragged.getRowOfCardLocation() + " "
        // + cardLocationBeingDragged.getIndex());
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
            // System.out.println(
            // ((CardView)
            // controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).get(i))
            // .getCard().getCardName());
        }

        // System.out.println("ALLY CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).size(); i++) {
            // System.out.println(
            // ((CardView)
            // controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).get(i))
            // .getCard().getCardName());
        }

        // System.out.println("ALLY CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)
                .size(); i++) {
            // System.out.println(((CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("OPPONENT CARDS IN DECK GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE)
                .size(); i++) {
            // System.out.println(((CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("OPPONENT CARDS IN HAND GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE)
                .size(); i++) {
            // System.out.println(((CardView) controllerForView
            // .giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE).get(i)).getCard().getCardName());
        }

        // System.out.println("OPPONENT CARDS IN GRAVEYARD GROUP:");
        for (int i = 0; i < controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)
                .size(); i++) {
            // System.out.println(
            // ((CardView)
            // controllerForView.giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)
            // .get(i)).getCard().getCardName());
        }
    }

}
