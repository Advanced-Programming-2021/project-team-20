package project.model.modelsforview;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import project.controller.duel.GamePackage.PhaseInGame;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import project.controller.duel.GamePackage.PhaseInGame;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.View.DuelView;

public class CardView extends Rectangle {
    private static double cardWidth = 90;
    private static double cardHeight = 131;
    private String cardName;
    private CardPosition cardPosition;
    private double upperLeftX;
    private double upperLeftY;
    private Card card;
    private boolean canBeSeen;
    private boolean shouldBeSeen180DegreesReversed;
    private boolean shouldBeInvisible;
    private RowOfCardLocation label;
    private ContextMenu contextMenu;
    private MenuItem item1;
    private MenuItem item2;
    private MenuItem item3;
    private MenuItem item4;
    private MenuItem item5;
    private MenuItem item6;
    private MenuItem item7;
    private MenuItem item8;
    private MenuItem item9;
    private MenuItem item10;

    public CardView(Card card, boolean canBeSeen, RowOfCardLocation label) {
        super(DuelView.getXHelperForCardViewConstructor(), DuelView.getYHelperForCardViewConstructor(), cardWidth, cardHeight);
        this.card = card;
        this.label = label;
        this.canBeSeen = canBeSeen;
        this.shouldBeSeen180DegreesReversed = false;
        this.shouldBeInvisible = false;
        this.contextMenu = null;
        this.cardName = card.getCardName();
        this.cardPosition = card.getCardPosition();
        this.upperLeftX = DuelView.getXHelperForCardViewConstructor();
        this.upperLeftY = DuelView.getYHelperForCardViewConstructor();
        showCardImage();
        contextMenu = new ContextMenu();
        item1 = new MenuItem();
        item2 = new MenuItem();
        item3 = new MenuItem();
        item4 = new MenuItem();
        item5 = new MenuItem();
        item6 = new MenuItem();
        item7 = new MenuItem();
        item8 = new MenuItem();
        item9 = new MenuItem();
        item10 = new MenuItem();
        DuelView.getShowOptionsToUser().getNormalSummoningMenuItem(this, item1);
        DuelView.getShowOptionsToUser().getTributeSummoningMenuItem(this, item2);
        DuelView.getShowOptionsToUser().getSpecialSummoningMenuItem(this, item3);
        DuelView.getShowOptionsToUser().getRitualSummoningMenuItem(this, item4);
        DuelView.getShowOptionsToUser().getSettingMenuItem(this, item5);
        DuelView.getShowOptionsToUser().getActivateEffectMenuItem(this, item6);
        DuelView.getShowOptionsToUser().getChangeCardPositionMenuItem(this, item7);
        DuelView.getShowOptionsToUser().getFlipSummonMenuItem(this, item8);
        DuelView.getShowOptionsToUser().getAttackingMonsterMenuItem(this, item9);
        DuelView.getShowOptionsToUser().getDirectAttackingMenuItem(this, item10);
        updateContextMenu();
    }

    public void moveUpperLeftCoordinate(double x, double y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }

    public double getUpperLeftX() {
        return upperLeftX;
    }

    public double getUpperLeftY() {
        return upperLeftY;
    }

    public Card getCard() {
        return card;
    }

    public RowOfCardLocation getLabel() {
        return label;
    }

    public boolean isCanBeSeen() {
        return canBeSeen;
    }

    public boolean isShouldBeSeen180DegreesReversed() {
        return shouldBeSeen180DegreesReversed;
    }

    public void setCanBeSeen(boolean canBeSeen) {
        this.canBeSeen = canBeSeen;
        showCardImage();
    }

    public void showCardImage() {
        if (shouldBeInvisible) {
            this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/ingameicons/breaking/1.png").toExternalForm())));
        } else if (canBeSeen) {
            if (card.getCardType().equals(CardType.MONSTER)) {
                if (shouldBeSeen180DegreesReversed) {
                    this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardName.replaceAll("\\s+", "") + ".jpg").toExternalForm()),
                            upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false));
                } else {
                    System.out.println(cardName);
                    this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/monsters/" + cardName.replaceAll("\\s+", "") + ".jpg").toExternalForm())));
                }
            } else {
                if (shouldBeSeen180DegreesReversed) {
                    this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardName.replaceAll("\\s+", "") + ".jpg").toExternalForm()),
                            upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false));
                } else {
                    System.out.println(cardName + " 148");
                    this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/spelltraps/" + cardName.replaceAll("\\s+", "") + ".jpg").toExternalForm())));
                }
            }
        } else {
            if (shouldBeSeen180DegreesReversed) {
                this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/card_back.png").toExternalForm()),
                        upperLeftX + CardView.getCardWidth(), upperLeftY + CardView.getCardHeight(), -CardView.getCardWidth(), -CardView.getCardHeight(), false));
            } else {
                this.setFill(new ImagePattern(new Image(CardView.class.getResource("/project/cards/card_back.png").toExternalForm())));
            }
        }
    }

    public void setLabel(RowOfCardLocation label) {
        this.label = label;
    }

    public static double getCardWidth() {
        return cardWidth;
    }

    public static double getCardHeight() {
        return cardHeight;
    }

    public void setShouldBeSeen180DegreesReversed(boolean shouldBeSeen180DegreesReversed) {
        this.shouldBeSeen180DegreesReversed = shouldBeSeen180DegreesReversed;
        showCardImage();
    }

    public boolean isShouldBeInvisible() {
        return shouldBeInvisible;
    }

    public void setShouldBeInvisible(boolean shouldBeInvisible) {
        this.shouldBeInvisible = shouldBeInvisible;
        showCardImage();
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public void updateContextMenu() {
        Bounds bounds = this.localToScene(this.getBoundsInLocal());
        String name = this.getCard().getCardName();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contextMenu.getItems().clear();
                // Add MenuItem to ContextMenu
                int turn = GameManager.getDuelControllerByIndex(0).getTurn();
                PhaseInGame phaseInGame = GameManager.getPhaseControllerByIndex(0).getPhaseInGame();

                int belongingTurn = 2;
                if (bounds.getMinY() >= 500) {
                    belongingTurn = 1;
                }
                boolean mainPhases = phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.ALLY_MAIN_PHASE_2)
                        || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_1) || phaseInGame.equals(PhaseInGame.OPPONENT_MAIN_PHASE_2);
                boolean battlePhases = phaseInGame.equals(PhaseInGame.ALLY_BATTLE_PHASE) || phaseInGame.equals(PhaseInGame.OPPONENT_BATTLE_PHASE);
                System.out.println("PREPARE FOR THE ALMIGHTY JIGEN");
                System.out.println("belongingTurn = " + belongingTurn + " turn = " + turn + " boundsx = " + bounds.getMinX() + " boundsy = " + bounds.getMinY() + " name = " + name);
                if (belongingTurn == turn) {
                    System.out.println("correct turn name = " + name);
                    if (card.getCardType().equals(CardType.MONSTER)) {
                        if (mainPhases) {
                            System.out.println("mainphase name = " + name);
                            if (bounds.getMinY() >= 245 + DuelView.getBattleFieldView().getUpperLeftY() && bounds.getMinY() <= 502 + DuelView.getBattleFieldView().getUpperLeftY()) {
                                System.out.println("inside monster zone name = " + name);
                                contextMenu.getItems().addAll(item6, item7, item8);
                            } else {
                                System.out.println("outside monster zone name = " + name);
                                contextMenu.getItems().addAll(item1, item2, item3, item4, item5);
                            }
                        } else if (battlePhases) {
                            if (bounds.getMinY() >= 245 + DuelView.getBattleFieldView().getUpperLeftY() && bounds.getMinY() <= 502 + DuelView.getBattleFieldView().getUpperLeftY()) {
                                System.out.println("battle phase monster zone name = " + name);
                                contextMenu.getItems().addAll(item9, item10);
                            }
                        }
                    } else {
                        if (mainPhases) {
                            contextMenu.getItems().addAll(item5, item6);
                        }
                    }
                }
            }
        });

    }
}
