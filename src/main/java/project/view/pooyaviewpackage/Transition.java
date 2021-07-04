package project.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
        ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardIncreasingInHand(turn == 1);
        for (int i = 0; i < translateTransitions.size(); i++) {
            parallelTransition.getChildren().add(translateTransitions.get(i));
        }
        return parallelTransition;
    }

//    public void applyTransitionForActivatingSpellTrap(CardView cardView) {
//        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
//        if (initialCardLocation != null) {
//            if (initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ||
//                initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
//                applyTransitionForActivatingSpellTrapInSpellZone(cardView);
//            } else {
//                applyTransitionForActivatingSpellTrapFromHand(cardView);
//            }
//        }
//    }
//
//
//    public void applyTransitionForActivatingSpellTrapInSpellZone(CardView cardView) {
//        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
//        if (initialCardLocation != null) {
//            flipCardBackAndForthConsideringCardImage(cardView, false, 500);
//        }
//    }
//
//    public void applyTransitionForActivatingSpellTrapFromHand(CardView cardView) {
//        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
//        if (initialCardLocation != null) {
//            ParallelTransition parallelTransition = new ParallelTransition();
//            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView, 0);
//            parallelTransition.getChildren().add(translateTransition);
//            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
//            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
//            for (int i = 0; i < translateTransitions.size(); i++) {
//                parallelTransition.getChildren().add(translateTransitions.get(i));
//            }
//            parallelTransition.play();
//
//        }
//    }

    public Object applyTransitionForActivatingSpellTrapSuper(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            if (initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ||
                initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE) || initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
               return flipCardBackAndForthConsideringCardImage(cardView, true, 250);
                // return applyTransitionForActivatingSpellTrapInSpellZoneSuper(cardView);
            } else {
                return applyTransitionForActivatingSpellTrapFromHandSuper(cardView);
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

    public ParallelTransition applyTransitionForActivatingSpellTrapFromHandSuper(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ParallelTransition parallelTransition = new ParallelTransition();
            cardView.setViewOrder(3);
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView, 0);
            parallelTransition.getChildren().add(translateTransition);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //  DuelView.getAdvancedCardMovingController().refreshWholeBattleField();
                }
            });
            return parallelTransition;
        }
        return null;
    }

    public void applyTransitionForSettingCard(CardView cardView) {
        if (cardView.getCard().getCardType().equals(CardType.SPELL) || cardView.getCard().getCardType().equals(CardType.TRAP)) {
            applyTransitionForSettingSpellTrapCard(cardView);
        } else if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
            applyTransitionForSettingMonsterCard(cardView);
        }
    }


    public ParallelTransition applyTransitionForSettingSpellTrapCard(CardView cardView) {
        //correct
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, false, 250);
            ParallelTransition parallelTransition = new ParallelTransition();
            DuelView.getControllerForView().changeLabelOfCardForSendingSpellToSpellZone(cardView);
            cardView.setViewOrder(3);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToSpellZone(cardView, 0);
            parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
            parallelTransition.getChildren().add(translateTransition);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            return parallelTransition;
        }
        return null;
    }

    public ParallelTransition applyTransitionForSettingMonsterCard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, false, 250);
            RotateTransition rotateTransition = rotateCardNintyDegrees(cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
            parallelTransition.getChildren().add(rotateTransition);
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView, 0);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
            System.out.println("conducting setting monster");
            return parallelTransition;
        }
        return null;
    }


    public ParallelTransition applyTransitionForSummoningMonsterCard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            //System.out.println("1");
            ParallelTransition parallelTransition = new ParallelTransition();
            TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToMonsterZone(cardView, 0);
            parallelTransition.getChildren().add(translateTransition);
            DuelView.getControllerForView().changeLabelOfCardForSendingMonsterToMonsterZone(cardView);
            ArrayList<TranslateTransition> translateTransitions = giveTranslateTransitionForCardDecreasingInHand(GameManager.getDuelControllerByIndex(0).getTurn() == 1);
            for (int i = 0; i < translateTransitions.size(); i++) {
                parallelTransition.getChildren().add(translateTransitions.get(i));
            }
//            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent actionEvent) {
//                    //cardView.applyDragDetectingAbilityToCardView();
//                    //cardView.applyClickingAbilitiesToCardView(duelView);
//                }
//            });
            return parallelTransition;
        }
        return null;
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

    public ParallelTransition applyTransitionForFlipSummoning(CardView cardView) {
        //somehow should add this one too
        ParallelTransition parallelTransition = new ParallelTransition();
        TroubleFlipTransition troubleFlipTransition = flipCardBackAndForthConsideringCardImage(cardView, true, 250);
        RotateTransition rotateTransition = rotateCardMinusNintyDegrees(cardView);
        parallelTransition.getChildren().add(troubleFlipTransition.getStHideFront());
        parallelTransition.getChildren().add(rotateTransition);
        return parallelTransition;
    }

    public void applyTransitionForShowingFrontOfACard(CardView cardView) {
        CardLocation initialCardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            flipCardBackAndForthConsideringCardImage(cardView, true, 500);
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

    public TroubleFlipTransition flipCardBackAndForthConsideringCardImage(CardView cardView, boolean isFinallyFrontSeen, double time) {
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
                } else {
                    cardView.setCanBeSeen(false);
                }
                //parallelTransition.getChildren().add(stShowBack);
                stShowBack.play();
            }
        });
        return new TroubleFlipTransition(stHideFront, stShowBack);
    }





    public Object applyTransitionForSendingCardToGraveyard(CardView cardView, int turn, boolean doYouWantTranslateTransition) {
        DestroyCardTransition destroyCardTransition;
        TranslateTransition translateTransition;
        TroubleFlipTransition troubleFlipTransition = null;
        boolean shouldBeRotatedNintyDegrees = cardView.isAlready90DegreesRotated();
        if (!cardView.isCanBeSeen() && shouldBeRotatedNintyDegrees) {
            //monster face down set position
            troubleFlipTransition = DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, true, 250);
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
        } else if (cardView.isCanBeSeen() && (!doYouWantTranslateTransition||shouldBeRotatedNintyDegrees)) {
            //monster face up attack / face up defense position
            destroyCardTransition = new DestroyCardTransition(cardView, shouldBeRotatedNintyDegrees);
            setOnFinishedForDestroyCardTransition(destroyCardTransition, turn, shouldBeRotatedNintyDegrees, cardView);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(destroyCardTransition);
            return parallelTransition;
        } else {
            //assuming you want translate transition which is for spells and traps
            ParallelTransition parallelTransition = new ParallelTransition();
            translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 100);
            parallelTransition.getChildren().add(translateTransition);
            return parallelTransition;
        }
//        if (!cardView.isCanBeSeen()) {
//        }
//
//        if (shouldBeRotatedNintyDegrees) {
//            destroyCardTransition = new DestroyCardTransition(cardView, true);
//            setOnFinishedForDestroyCardTransition(destroyCardTransition, turn, true, cardView);
//            treatSendingMonsterToGraveyard(cardView, troubleFlipTransition, destroyCardTransition);
//            if (!cardView.isCanBeSeen()) {
//                return troubleFlipTransition;
//            } else {
//                ParallelTransition parallelTransition = new ParallelTransition();
//                parallelTransition.getChildren().add(destroyCardTransition);
//                return parallelTransition;
//            }
//        } else {
//            if (doYouWantTranslateTransition) {
//
//            } else {
//                destroyCardTransition = new DestroyCardTransition(cardView, false);
//                setOnFinishedForDestroyCardTransition(destroyCardTransition, turn, false, cardView);
//                treatSendingMonsterToGraveyard(cardView, troubleFlipTransition, destroyCardTransition);
//                if (!cardView.isCanBeSeen()) {
//                    return troubleFlipTransition;
//                } else {
//                    ParallelTransition parallelTransition = new ParallelTransition();
//                    parallelTransition.getChildren().add(destroyCardTransition);
//                    return parallelTransition;
//                }
//            }
//        }
    }

    public void treatSendingMonsterToGraveyard(CardView cardView, TroubleFlipTransition troubleFlipTransition, DestroyCardTransition destroyCardTransition) {
        if (!cardView.isCanBeSeen()) {

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
                    translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 50);
                    translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            destroyCardTransition.getCardView().setShouldBeInvisible(false);
                        }
                    });
                    rotateTransition.play();
                    translateTransition.play();
                } else {
                    TranslateTransition translateTransition = DuelView.getControllerForView().sendCardToGraveyardZone(cardView, turn, 50);
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
