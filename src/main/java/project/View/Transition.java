package project.View;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;

import java.util.ArrayList;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.modelsforview.CardView;

import java.util.ArrayList;

public class Transition {
    public void applyTransitionForDrawingACard(boolean isForAlly) {
        //this function is called after we receive successful card drawing from server
        ArrayList<CardView> cardViews;
        ParallelTransition parallelTransition = new ParallelTransition();

        if (isForAlly) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE);
            for (int i = 0; i < cardViews.size(); i++) {
                System.out.println("these are cards in ally deck in order " + cardViews.get(i).getCard().getCardName());
            }
            cardViews.get(0).setLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardViews.get(cardViews.size() - 1));
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
            cardViews.get(0).setLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardViews.get(cardViews.size() - 1));
            translate.setToX(
                    (DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getBattleFieldView().getWidth() - 33.6 - CardView.getCardWidth() * (cardViews.size())) / 2
                            + 33.6 + CardView.getCardWidth() * (cardViews.size() - 1) - cardViews.get(cardViews.size() - 1).getUpperLeftX()
                            - CardView.getCardWidth() * (cardViews.size() - 1)));
            translate.setToY(19 - CardView.getCardHeight());
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            parallelTransition.getChildren().add(translate);
        }
        ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardIncreasingInHand(isForAlly);
        for (int i = 0; i < translateTransitions.size(); i++) {
            parallelTransition.getChildren().add(translateTransitions.get(i));
        }
        parallelTransition.play();
    }

    public void applyTransitionForActivatingSpellTrap(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            if (initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ||
                    initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
                applyTransitionForActivatingSpellTrapInSpellZone(cardView);
            } else {
                applyTransitionForActivatingSpellTrapFromHand(cardView);
            }
        }
    }


    public void applyTransitionForActivatingSpellTrapInSpellZone(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, false);
        }
    }

    public void applyTransitionForActivatingSpellTrapFromHand(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ParallelTransition parallelTransition = new ParallelTransition();
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            parallelTransition.play();

        }
    }

    public void applyTransitionForSettingCard(CardView cardView) {
        if (cardView.getCard().getCardType().equals(CardType.SPELL) || cardView.getCard().getCardType().equals(CardType.TRAP)) {
            applyTransitionForSettingSpellTrapCard(cardView);
        } else if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
            applyTransitionForSettingMonsterCard(cardView);
        }
    }


    public void applyTransitionForSettingSpellTrapCard(CardView cardView) {
        //incorrect
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, false);
            ParallelTransition parallelTransition = new ParallelTransition();
            cardView.setViewOrder(3);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            parallelTransition.play();
        }
    }


    public void applyTransitionForSummoningMonsterCard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            //System.out.println("1");
            ParallelTransition parallelTransition = new ParallelTransition();
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            parallelTransition.play();
        }
    }

    public void applyTransitionForSettingMonsterCard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, false);
            RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(rotateTransition);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            System.out.println("conducting setting monster");
            parallelTransition.play();
        }
    }

    public void applyTransitionForChangingMonsterPositionToFaceUpAttackPosition(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            RotateTransition rotateTransition = rotateCardMinusNintyDegrees(cardView);
            rotateTransition.play();
        }
    }

    public void applyTransitionForChangingMonsterPositionToFaceUpDefensePosition(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
            rotateTransition.play();
        }
    }

    public void applyTransitionForFlipSummoning(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, true);
            RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
            rotateTransition.play();
        }
    }

    public void applyTransitionForShowingFrontOfACard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, true);
        }
    }

    private ArrayList<TranslateTransition> giveTranslateTransitionForCardDecreasingInHand(boolean isForAlly) {
        //before calling this method you must delete your card from hand
        ArrayList<TranslateTransition> translateTransitions = new ArrayList<>();
        ArrayList<CardView> cardViews;
        if (isForAlly) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        } else {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        }
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            if (isForAlly) {
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


    private ArrayList<TranslateTransition> giveTranslateTransitionForCardIncreasingInHand(boolean isForAlly) {
        //before calling this method you must add your card to hand
        //therefore this function gives translations for already existing cards in hand
        ArrayList<TranslateTransition> translateTransitions = new ArrayList<>();
        ArrayList<CardView> cardViews;
        if (isForAlly) {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        } else {
            cardViews = DuelView.getControllerForView().giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        }
        for (int i = 0; i < cardViews.size() - 1; i++) {
            CardView cardView = cardViews.get(i);
            System.out.println("just to make sure " + cardView.getCard().getCardName());
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            if (isForAlly) {
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

    private RotateTransition rotateCardNintyDegrees(CardView cardView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), cardView);
        rotateTransition.setFromAngle(cardView.getRotate());
        rotateTransition.setToAngle(90);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);
        return rotateTransition;
    }

    private RotateTransition rotateCardMinusNintyDegrees(CardView cardView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), cardView);
        rotateTransition.setFromAngle(cardView.getRotate());
        rotateTransition.setToAngle(-90);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);
        return rotateTransition;
    }

    private void flipCardBackAndForthConsideringCardImage(CardView cardView, boolean isFinallyFrontSeen) {
        ScaleTransition stHideFront = new ScaleTransition(Duration.millis(500), cardView);
        stHideFront.setFromX(1);
        stHideFront.setToX(0);

        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(500), cardView);
        stShowBack.setFromX(0);
        stShowBack.setToX(1);
        stHideFront.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (isFinallyFrontSeen) {
                    cardView.setCanBeSeen(true);
                } else {
                    cardView.setCanBeSeen(false);
                }
                //parallelTransition.getChildren().add(stShowBack);
                stShowBack.play();
            }
        });
        stHideFront.play();
    }
}
