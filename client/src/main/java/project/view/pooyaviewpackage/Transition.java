package project.view.pooyaviewpackage;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardPosition;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.modelsforview.CardView;

import java.nio.file.Paths;
import java.util.ArrayList;

public class Transition {

    public ParallelTransition sendCardToHandZone(CardView cardView, int turn) {
        ArrayList<CardView> cardViews;
        ParallelTransition parallelTransition = new ParallelTransition();

        if (turn == 1) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE);
            for (int i = 0; i < cardViews.size(); i++) {
                System.out.println("these are cards in ally deck in order " + cardViews.get(i).getCard().getCardName());
            }
            cardView.setLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            translate.setToX(
                (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                    + 33.6 + CardView.getCardWidth() * (cardViews.size() - 1) - cardViews.get(cardViews.size() - 1).getUpperLeftX()));
            translate.setToY(CardView.getCardHeight() - 19);
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            parallelTransition.getChildren().add(translate);
        } else {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE);
            for (int i = 0; i < cardViews.size(); i++) {
                System.out.println("these are cards in opponent deck in order " + cardViews.get(i).getCard().getCardName());
            }
            cardView.setLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            translate.setToX(
                (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                    + 33.6 + CardView.getCardWidth() * (cardViews.size() - 1) - cardViews.get(cardViews.size() - 1).getUpperLeftX()
                    - CardView.getCardWidth() * (cardViews.size() - 1)));
            translate.setToY(19 - CardView.getCardHeight());
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            parallelTransition.getChildren().add(translate);
        }
        int currentTurn = GameManager.getDuelControllerByIndex(0).getTurn();
        System.out.println("I want to add card to hand and turn = "+turn+" currentTurn = "+currentTurn);
        if (turn == currentTurn || GameManager.getDuelControllerByIndex(0).getPlayingUsers().get(1).equals("AI")){
            cardView.setCanBeSeen(true);
        } else {
            cardView.setCanBeSeen(false);
        }
//        parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                int currentTurn = GameManager.getDuelControllerByIndex(0).getTurn();
//                System.out.println("I want to add card to hand and turn = "+turn+" currentTurn = "+currentTurn);
//                if (turn == currentTurn){
//                    cardView.setCanBeSeen(true);
//                } else {
//                    cardView.setCanBeSeen(false);
//                }
//            }
//        });
        ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardIncreasingInHand(turn);
        for (int i = 0; i < translateTransitions.size(); i++) {
            parallelTransition.getChildren().add(translateTransitions.get(i));
        }
        return parallelTransition;
    }

    public Object applyTransitionForActivatingSpellTrapSuper(CardView cardView, int sideOfFinalDestination, int initialSide, boolean isSpecial) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            if (initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ||
                initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                return flipCardBackAndForthConsideringCardImage(cardView, true, 250, isSpecial);
                // return applyTransitionForActivatingSpellTrapInSpellZoneSuper(cardView);
            } else {
                return applyTransitionForActivatingSpellTrapFromHandSuper(cardView, sideOfFinalDestination, initialSide, isSpecial);
            }
        }
        return null;
    }


    public TroubleFlipTransition applyTransitionForActivatingSpellTrapInSpellZoneSuper(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ScaleTransition stHideFront = new ScaleTransition(Duration.millis(500), cardView);
            stHideFront.setFromX(1);
            stHideFront.setToX(0);

            ScaleTransition stShowBack = new ScaleTransition(Duration.millis(500), cardView);
            stShowBack.setFromX(0);
            stShowBack.setToX(1);
            TroubleFlipTransition troubleFlipTransition = new TroubleFlipTransition(stHideFront, stShowBack);
            stHideFront.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    if (false) {
                        cardView.setCanBeSeen(true);
                    } else {
                        cardView.setCanBeSeen(false);
                    }
                    //parallelTransition.getChildren().add(stShowBack);
                    stShowBack.play();
                }
            });
            return troubleFlipTransition;
            //stHideFront.play();
        }
        return null;
    }

    public ParallelTransition applyTransitionForActivatingSpellTrapFromHandSuper(CardView cardView, int sideOfFinalDestination, int initialSide, boolean isSpecial) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ParallelTransition parallelTransition = new ParallelTransition();
            cardView.setViewOrder(3);
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView, sideOfFinalDestination);
            parallelTransition.getChildren().add(translateTransition);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(initialSide);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (isSpecial) {
                        DuelView.getBattleFieldView().updateImageByThisName(cardView.getCard().getCardName());
                    }
                }
            });
            return parallelTransition;
        }
        return null;
    }


    public ParallelTransition applyTransitionForSettingSpellTrapCard(CardView cardView, int sideOfFinalDestination, int initialSide) {
        //correct
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, false, 250, false);
            ParallelTransition parallelTransition = new ParallelTransition();
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            cardView.setViewOrder(3);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView, sideOfFinalDestination);
            parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
            parallelTransition.getChildren().add(translateTransition);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(initialSide);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            return parallelTransition;
        }
        return null;
    }

    public ParallelTransition applyTransitionForSettingMonsterCard(CardView cardView, int sideOfFinalDestination, int initialSide) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, false, 250, false);
            RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
            parallelTransition.getChildren().add(rotateTransition);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView, sideOfFinalDestination);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(initialSide);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            System.out.println("conducting setting monster");
            return parallelTransition;
        }
        return null;
    }


    public ParallelTransition applyTransitionForSummoningMonsterCard(CardView cardView, CardPosition cardPosition, int sideOfFinalDestination, int initialSide) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ParallelTransition parallelTransition = new ParallelTransition();
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView, sideOfFinalDestination);
            parallelTransition.getChildren().add(translateTransition);
            if (cardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
                parallelTransition.getChildren().add(rotateTransition);
            }
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(initialSide);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            return parallelTransition;
        }
        return null;
    }


    public ParallelTransition applyTransitionForFlipSummoningOrChangingCardPositionSoThatFinallyIsFaceUpAttackPosition(CardView cardView) {
        if (cardView.isCanBeSeen()) {
            return applyTransitionForChangingMonsterPositionToFaceUpAttackPosition(cardView);
        } else {
            return applyTransitionForFlipSummoning(cardView);
        }
    }


    public Object applyTransitionForFlippingCardByOpponentOrChangingCardPositionSoThatFinallyIsFaceUpDefensePosition(CardView cardView) {
        if (cardView.isCanBeSeen()) {
            return applyTransitionForChangingMonsterPositionToFaceUpDefensePosition(cardView);
        } else {
            return flipCardBackAndForthConsideringCardImage(cardView, true, 250, false);
        }
    }


    public ParallelTransition applyTransitionForChangingMonsterPositionToFaceUpAttackPosition(CardView cardView) {
        ParallelTransition parallelTransition = new ParallelTransition();
        RotateTransition rotateTransition = rotateCardMinusNintyDegrees(cardView);
        parallelTransition.getChildren().add(rotateTransition);
        return parallelTransition;
    }

    public ParallelTransition applyTransitionForChangingMonsterPositionToFaceUpDefensePosition(CardView cardView) {
        ParallelTransition parallelTransition = new ParallelTransition();
        RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
        parallelTransition.getChildren().add(rotateTransition);
        return parallelTransition;
    }

    public ParallelTransition applyTransitionForFlipSummoning(CardView cardView) {
        //somehow should add this one too
        ParallelTransition parallelTransition = new ParallelTransition();
        TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, true, 250, false);
        RotateTransition rotateTransition = rotateCardMinusNintyDegrees(cardView);
        parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
        parallelTransition.getChildren().add(rotateTransition);
        return parallelTransition;
    }

    private ArrayList<TranslateTransition> giveTranslateTransitionForCardDecreasingInHand(int isForAlly) {
        //before calling this method you must delete your card from hand this meand you must change label sooner than this
        ArrayList<TranslateTransition> translateTransitions = new ArrayList<>();
        ArrayList<CardView> cardViews;
        if (isForAlly == 1) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        } else {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        }
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            if (isForAlly == 1) {
                translate.setToX(
                    (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                        + 33.6 + CardView.getCardWidth() * (i) - cardView.getUpperLeftX()));
            } else {
                translate.setToX(
                    (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                        + 33.6 + CardView.getCardWidth() * (cardViews.size() - 1 - i) - cardView.getUpperLeftX()));
            }
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            translateTransitions.add(translate);
        }
        return translateTransitions;
    }


    private ArrayList<TranslateTransition> giveTranslateTransitionForCardIncreasingInHand(int isForAlly) {
        //before calling this method you must add your card to hand this means you must change label sooner than this
        //therefore this function gives translations for already existing cards in hand
        ArrayList<TranslateTransition> translateTransitions = new ArrayList<>();
        ArrayList<CardView> cardViews;
        if (isForAlly == 1) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        } else {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        }
        for (int i = 0; i < cardViews.size() - 1; i++) {
            CardView cardView = cardViews.get(i);
            System.out.println("just to make sure " + cardView.getCard().getCardName());
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            if (isForAlly == 1) {
                double xTranslation = (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                    + 33.6 + CardView.getCardWidth() * (i) - cardView.getUpperLeftX());
                translate.setToX(xTranslation);
            } else {
                double xTranslation = (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                    + 33.6 + CardView.getCardWidth() * (cardViews.size() - 1 - i) - cardView.getUpperLeftX());
                translate.setToX(xTranslation);
            }
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            translateTransitions.add(translate);
        }
        return translateTransitions;
    }

    public RotateTransition rotateCardNintyDegrees(CardView cardView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), cardView);
        rotateTransition.setFromAngle(cardView.getRotate());
        rotateTransition.setToAngle(90);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);
        cardView.setAlready90DegreesRotated(!cardView.isAlready90DegreesRotated());
        return rotateTransition;
    }

    public RotateTransition rotateCardMinusNintyDegrees(CardView cardView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), cardView);
        rotateTransition.setFromAngle(cardView.getRotate());
        rotateTransition.setToAngle(0);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);
        cardView.setAlready90DegreesRotated(!cardView.isAlready90DegreesRotated());
        return rotateTransition;
    }

    public RotateTransition rotateCardMinusNintyDegreesVeryQuickly(CardView cardView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(50), cardView);
        rotateTransition.setFromAngle(cardView.getRotate());
        rotateTransition.setToAngle(0);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);
        cardView.setAlready90DegreesRotated(!cardView.isAlready90DegreesRotated());
        return rotateTransition;
    }

    public TroubleFlipTransition flipCardBackAndForthConsideringCardImage(CardView cardView, boolean isFinallyFrontSeen, double time, boolean isSpecial) {
        ScaleTransition stHideFront = new ScaleTransition(Duration.millis(time), cardView);
        stHideFront.setFromX(1);
        stHideFront.setToX(0);

        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(time), cardView);
        stShowBack.setFromX(0);
        stShowBack.setToX(1);
        stHideFront.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (isFinallyFrontSeen) {
                    cardView.setCanBeSeen(true);
                    if (isSpecial) {
                        DuelView.getBattleFieldView().updateImageByThisName(cardView.getCard().getCardName());
                    }
                } else {
                    cardView.setCanBeSeen(false);
                }
                //parallelTransition.getChildren().add(stShowBack);
                stShowBack.play();
            }
        });
        return new TroubleFlipTransition(stHideFront, stShowBack);
    }


    public Object applyTransitionForSendingCardToGraveyard(CardView cardView, int turn, boolean doYouWantTranslateTransition, boolean isSpecial) {
        DestroyCardTransition destroyCardTransition;
        TranslateTransition translateTransition;

        TroubleFlipTransition troubleFlipTransition = null;
        boolean shouldBeRotatedNintyDegrees = cardView.isAlready90DegreesRotated();
        if (!cardView.isCanBeSeen() && shouldBeRotatedNintyDegrees) {
            //monster face down set position
            troubleFlipTransition = DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, true, 250, false);
            destroyCardTransition = new DestroyCardTransition(cardView, true);
            DestroyCardTransition finalDestroyCardTransition = destroyCardTransition;
            troubleFlipTransition.getStShowBack().setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    finalDestroyCardTransition.play();
                }
            });
            setOnFinishedForDestroyCardTransition(destroyCardTransition, turn, true, cardView);
            return troubleFlipTransition;
        } else if (cardView.isCanBeSeen() && (!doYouWantTranslateTransition || shouldBeRotatedNintyDegrees)) {
            //monster face up attack / face up defense position
            destroyCardTransition = new DestroyCardTransition(cardView, shouldBeRotatedNintyDegrees);
            setOnFinishedForDestroyCardTransition(destroyCardTransition, turn, shouldBeRotatedNintyDegrees, cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(destroyCardTransition);
            return parallelTransition;
        } else {
            //assuming you want translate transition which is for spells and traps
            DuelView.getControllerForView().changeLabelOfCardForSendingCardToGraveyardZone(cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 400, isSpecial);
            parallelTransition.getChildren().addAll(giveTranslateTransitionForCardDecreasingInHand(turn));
            parallelTransition.getChildren().add(translateTransition);
            if (!cardView.isCanBeSeen()) {
                troubleFlipTransition = DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, true, 200, false);
                parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
            }
            return parallelTransition;
        }
    }

    public void setOnFinishedForDestroyCardTransition(DestroyCardTransition destroyCardTransition, int turn, boolean shouldBeRotatedNintyDegrees, CardView cardView) {
        destroyCardTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DuelView.getControllerForView().changeLabelOfCardForSendingCardToGraveyardZone(destroyCardTransition.getCardView());

                if (shouldBeRotatedNintyDegrees) {
                    RotateTransition rotateTransition = DuelView.getTransition().rotateCardMinusNintyDegreesVeryQuickly(destroyCardTransition.getCardView());
                    TranslateTransition translateTransition;
                    translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 50, false);
                    translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            destroyCardTransition.getCardView().setShouldBeInvisible(false);
                        }
                    });
                    rotateTransition.play();
                    translateTransition.play();
                } else {
                    TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 50, false);
                    translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            destroyCardTransition.getCardView().setShouldBeInvisible(false);
                        }
                    });
                    translateTransition.play();
                }
                //controllerForView.sendCardToGraveyardZone(destroyCardTransition.getCardView()).play();
                Bounds bounds = destroyCardTransition.getCardView().localToScene(destroyCardTransition.getCardView().getBoundsInLocal());
                System.out.println("whrere is our card x = " + bounds.getMinX() + " y = " + bounds.getMinY());

            }
        });
    }


    public Timeline applyTransitionForHealthBar(boolean isForAlly, int increaseInHealth, int previousHealth) {
        HealthBarAndHealthPoints healthBarAndHealthPoints;
        if (isForAlly) {
            healthBarAndHealthPoints = DuelView.getAllyHealthStatus();
        } else {
            healthBarAndHealthPoints = DuelView.getOpponentHealthStatus();
        }
        Rectangle helpfulRectangle = healthBarAndHealthPoints.getHelpfulHealthRectangle();
        double realFinalWidth = helpfulRectangle.getWidth() - healthBarAndHealthPoints.getHealthBar().getWidth() * increaseInHealth / previousHealth;
        System.out.println("realFinalWidth is " + realFinalWidth);
        if (realFinalWidth < 0) {
            realFinalWidth = 0;
        }
        if (realFinalWidth > 290) {
            realFinalWidth = 290;
        }
        System.out.println("realFinalWidth is " + realFinalWidth + " after adjustments");

        KeyValue widthValue = new KeyValue(helpfulRectangle.widthProperty(), realFinalWidth);
        KeyFrame frame = new KeyFrame(Duration.seconds(0.4), widthValue);
        return new Timeline(frame);
    }
}

class TroubleFlipTransition {
    private ScaleTransition stHideFront;
    private ScaleTransition stShowBack;

    public TroubleFlipTransition(ScaleTransition stHideFront, ScaleTransition stShowBack) {
        this.stHideFront = stHideFront;
        this.stShowBack = stShowBack;
    }

    public ScaleTransition getStHideFront() {
        return stHideFront;
    }

    public ScaleTransition getStShowBack() {
        return stShowBack;
    }

    public void setStHideFront(ScaleTransition stHideFront) {
        this.stHideFront = stHideFront;
    }

    public void setStShowBack(ScaleTransition stShowBack) {
        this.stShowBack = stShowBack;
    }
}
