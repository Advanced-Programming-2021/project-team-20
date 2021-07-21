//package project.model.modelsforview;
//
//import javafx.application.Platform;
//import javafx.event.EventHandler;
//import javafx.geometry.Bounds;
//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.MenuItem;
//import javafx.scene.image.Image;
//import javafx.scene.input.*;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Rectangle;
//import project.model.PhaseInGame;
////import project.server.controller.duel.PreliminaryPackage.GameManager;
//import project.model.cardData.General.*;
//import project.client.view.pooyaviewpackage.DuelView;
//
//public class CardView extends Rectangle {
//    private static double cardWidth = 90;
//    private static double cardHeight = 131;
//    private String cardName;
//    private CardPosition cardPosition;
//    private double upperLeftX;
//    private double upperLeftY;
//    private Card card;
//    private boolean canBeSeen;
//    private boolean isAlready90DegreesRotated;
//    private boolean shouldBeSeen180DegreesReversed;
//    private boolean shouldBeInvisible;
//    private RowOfCardLocation label;
//    private ContextMenu contextMenu;
//    private MenuItem item1;
//    private MenuItem item2;
//    private MenuItem item3;
//    private MenuItem item4;
//    private MenuItem item5;
//    private MenuItem item6;
//    private MenuItem item7;
//    private MenuItem item8;
//    private MenuItem item9;
//    private MenuItem item10;
//
//    public CardView(Card card, boolean canBeSeen, RowOfCardLocation label, DuelView duelView) {
//        super(DuelView.getXHelperForCardViewConstructor(), DuelView.getYHelperForCardViewConstructor(), cardWidth, cardHeight);
//        this.card = card;
//        this.label = label;
//        this.canBeSeen = canBeSeen;
//        this.isAlready90DegreesRotated = false;
//        this.shouldBeSeen180DegreesReversed = false;
//        this.shouldBeInvisible = false;
//        this.contextMenu = null;
//        this.cardName = card.getCardName();
//        this.cardPosition = card.getCardPosition();
//        this.upperLeftX = DuelView.getXHelperForCardViewConstructor();
//        this.upperLeftY = DuelView.getYHelperForCardViewConstructor();
//        showCardImage();
//        contextMenu = new ContextMenu();
//        item1 = new MenuItem();
//        item2 = new MenuItem();
//        item3 = new MenuItem();
//        item4 = new MenuItem();
//        item5 = new MenuItem();
//        item6 = new MenuItem();
//        item7 = new MenuItem();
//        item8 = new MenuItem();
//        item9 = new MenuItem();
//        item10 = new MenuItem();
//        DuelView.getShowOptionsToUser().getNormalSummoningMenuItem(this, item1, duelView);
//        DuelView.getShowOptionsToUser().getTributeSummoningMenuItem(this, item2, duelView);
//        DuelView.getShowOptionsToUser().getSpecialSummoningMenuItem(this, item3, duelView);
//        DuelView.getShowOptionsToUser().getShowGraveyardMenuItem(this, item4, duelView);
//        DuelView.getShowOptionsToUser().getSettingMenuItem(this, item5);
//        DuelView.getShowOptionsToUser().getActivateEffectMenuItem(this, item6);
//        DuelView.getShowOptionsToUser().getChangeCardPositionMenuItem(this, item7);
//        DuelView.getShowOptionsToUser().getFlipSummonMenuItem(this, item8);
//        DuelView.getShowOptionsToUser().getAttackingMonsterMenuItem(this, item9);
//        DuelView.getShowOptionsToUser().getDirectAttackingMenuItem(this, item10);
//        contextMenu.getItems().addAll(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10);
//        //updateContextMenu();
//    }
//
//    public void moveUpperLeftCoordinate(double x, double y) {
//        this.setX(this.getX() + x);
//        this.setY(this.getY() + y);
//    }
//
//    public double getUpperLeftX() {
//        return upperLeftX;
//    }
//
//    public double getUpperLeftY() {
//        return upperLeftY;
//    }
//
//    public Card getCard() {
//        return card;
//    }
//
//    public RowOfCardLocation getLabel() {
//        return label;
//    }
//
//    public boolean isCanBeSeen() {
//        return canBeSeen;
//    }
//
//    public boolean isAlready90DegreesRotated() {
//        return isAlready90DegreesRotated;
//    }
//
//    public void setAlready90DegreesRotated(boolean already90DegreesRotated) {
//        isAlready90DegreesRotated = already90DegreesRotated;
//    }
//
//    public boolean isShouldBeSeen180DegreesReversed() {
//        return shouldBeSeen180DegreesReversed;
//    }
//
//    public void setCanBeSeen(boolean canBeSeen) {
//        this.canBeSeen = canBeSeen;
//        showCardImage();
//    }
//
//    public void showCardImage() {
//        this.setFill(giveImageForThisCardView());
//    }
//
//    private ImagePattern giveImageForThisCardView() {
//        Bounds bounds = this.localToScene(this.getBoundsInLocal());
//        if (shouldBeInvisible) {
//            return new ImagePattern(new Image(CardView.class.getResource("/project/ingameicons/breaking/1.png").toExternalForm()));
//        } else if (canBeSeen) {
//            if (card.getCardType().equals(CardType.MONSTER)) {
//                if (shouldBeSeen180DegreesReversed) {
//                    return new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardName + ".jpg").toExternalForm()),
//                        upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false);
//                } else {
//                    return new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardName + ".jpg").toExternalForm()));
//                }
//            } else {
//                if (shouldBeSeen180DegreesReversed) {
//                    return new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardName + ".jpg").toExternalForm()),
//                        upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false);
//                } else {
//                    return new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardName + ".jpg").toExternalForm()));
//                }
//            }
//        } else {
//            if (shouldBeSeen180DegreesReversed) {
//                return new ImagePattern(new Image(CardView.class.getResource("/project/cards/card_back.png").toExternalForm()),
//                    upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false);
//            } else {
//                return new ImagePattern(new Image(CardView.class.getResource("/project/cards/card_back.png").toExternalForm()));
//            }
//        }
//    }
//
//    public ImagePattern giveImageVeryNormally() {
//        if (card.getCardType().equals(CardType.MONSTER)) {
//            return new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardName + ".jpg").toExternalForm()));
//        } else {
//            return new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardName + ".jpg").toExternalForm()));
//        }
//    }
//
//    public void setLabel(RowOfCardLocation label) {
//        this.label = label;
//    }
//
//    public static double getCardWidth() {
//        return cardWidth;
//    }
//
//    public static double getCardHeight() {
//        return cardHeight;
//    }
//
//    public void setShouldBeSeen180DegreesReversed(boolean shouldBeSeen180DegreesReversed) {
//        this.shouldBeSeen180DegreesReversed = shouldBeSeen180DegreesReversed;
//        showCardImage();
//    }
//
//    public boolean isShouldBeInvisible() {
//        return shouldBeInvisible;
//    }
//
//    public void setShouldBeInvisible(boolean shouldBeInvisible) {
//        this.shouldBeInvisible = shouldBeInvisible;
//        showCardImage();
//    }
//
//    public ContextMenu getContextMenu() {
//        return contextMenu;
//    }
//
//    public void setContextMenu(ContextMenu contextMenu) {
//        this.contextMenu = contextMenu;
//    }
//
//    public void updateContextMenu() {
//        Bounds bounds = this.localToScene(this.getBoundsInLocal());
//        CardView cardView = this;
//        String name = this.getCard().getCardName();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                item1.setVisible(false);
//                item2.setVisible(false);
//                item3.setVisible(false);
//                item4.setVisible(false);
//                item5.setVisible(false);
//                item6.setVisible(false);
//                item7.setVisible(false);
//                item8.setVisible(false);
//                item9.setVisible(false);
//                item10.setVisible(false);
//                if (DuelView.isShouldDuelViewClickingAbilitiesWork()) {
//                    int turn = GameManager.getDuelControllerByIndex(0).getTurn();
//                    PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();
//                    int fakeTurn = GameManager.getDuelControllerByIndex(0).getFakeTurn();
//                    int belongingTurn = 2;
//                    double meanY = (bounds.getMinY() + bounds.getMaxY()) / 2;
//                    System.out.println("PREPARE FOR THE ALMIGHTY JIGEN with meanY = " + meanY);
//                    if (meanY >= 500) {
//                        belongingTurn = 1;
//                    }
//                    boolean mainPhases = phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)
//                        || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2);
//                    boolean battlePhases = phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE);
//
//                    System.out.println("belongingTurn = " + belongingTurn + " turn = " + turn + " boundsx = " + bounds.getMinX() + " boundsy = " + meanY + " name = " + name);
//                    CardLocation cardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
//                    if (cardLocation.getRowOfCardLocation().toString().contains("GRAVEYARD")) {
//                        item4.setVisible(true);
//                    } else {
//                        if (belongingTurn == turn) {
//                            System.out.println("correct turn name = " + name);
//                            if (card.getCardType().equals(CardType.MONSTER)) {
//                                if (mainPhases) {
//                                    System.out.println("mainphase name = " + name);
//                                    if (label.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || label.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
//                                        System.out.println("inside monster zone name = " + name);
//                                        item6.setVisible(true);
//                                        item7.setVisible(true);
//                                        item8.setVisible(true);
//                                    } else if (label.equals(RowOfCardLocation.ALLY_HAND_ZONE) || label.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
//                                        System.out.println("outside monster zone name = " + name);
//                                        item1.setVisible(true);
//                                        item2.setVisible(true);
//                                        item3.setVisible(true);
//                                        item5.setVisible(true);
//                                    }
//                                } else if (battlePhases) {
//                                    if (label.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) || label.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
//                                        System.out.println("battle phase monster zone name = " + name);
//                                        item9.setVisible(true);
//                                        item10.setVisible(true);
//                                    }
//                                }
//                            } else {
//                                if (mainPhases) {
//                                    item5.setVisible(true);
//                                    item6.setVisible(true);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//    }
//
//
//    public void applyClickingAbilitiesToCardView(DuelView duelView) {
//        CardView cardView = this;
////        cardView.setOnMouseReleased(new EventHandler<MouseEvent>() {
////            @Override
////            public void handle(MouseEvent mouseEvent) {
////                DuelView.setShouldDuelViewClickingAbilitiesWork(true);
////            }
////        });
////        cardView.addEventHandler(
////            MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
////                @Override
////                public void handle(MouseEvent e) {
////                    if (e.getButton().equals(MouseButton.PRIMARY)) {
////                        if (!duelView.isDragFlag()) {
////                            if (e.getClickCount() == 1) {
////                                duelView.setScheduledFuture(duelView.getExecutor().schedule(() -> duelView.singleClickAction(e, cardView), 300, TimeUnit.MILLISECONDS)) ;
////                            } else if (e.getClickCount() > 1) {
////                                if (duelView.getScheduledFuture() != null && !duelView.getScheduledFuture().isCancelled() && !duelView.getScheduledFuture().isDone()) {
////                                    duelView.getScheduledFuture().cancel(false);
////                                    duelView.doubleClickAction(e);
////                                }
////                            }
////                        }
////                        duelView.setDragFlag(false);
////                    }
////                    e.consume();
////                }
////            }
////        );
//        cardView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
//            @Override
//            public void handle(ContextMenuEvent event) {
//                updateContextMenu();
//                contextMenu.show(cardView, event.getScreenX(), event.getScreenY());
//            }
//        });
//    }
//
//    public void applyDragDetectingAbilityToCardView() {
////        CardView cardView = this;
////        cardView.setOnDragDetected(new EventHandler<MouseEvent>() {
////            @Override
////            public void handle(MouseEvent mouseEvent) {
////                DuelView.setShouldDuelViewClickingAbilitiesWork(false);
////                Dragboard db = cardView.startDragAndDrop(TransferMode.MOVE);
////                db.setDragView(cardView.snapshot(null, null));
////                ClipboardContent cc = new ClipboardContent();
////                cc.putString("card");
////                db.setContent(cc);
////                System.out.println("ALERT");
////                System.out.println("card being drag detected");
////                TwoDimensionalPoint twoDimensionalPoint = new TwoDimensionalPoint(mouseEvent.getSceneX(), mouseEvent.getSceneY());
////                System.out.println("twodim point x is "+twoDimensionalPoint.getX());
////                System.out.println("twodim y is "+twoDimensionalPoint.getY());
////                DuelView.setCardLocationBeingDragged(DuelView.getControllerForView().giveCardLocationByCoordinateInView(twoDimensionalPoint, cardView));
////                DuelView.setDraggingObject(cardView);
////                DuelView.setDraggingObjectX(mouseEvent.getSceneX());
////                DuelView.setDraggingObjectY(mouseEvent.getSceneY());
////                mouseEvent.consume();
////            }
////        });
//
//    }
//
//
//}
