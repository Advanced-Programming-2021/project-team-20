package project.client.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import project.server.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.client.modelsforview.CardView;
import project.client.modelsforview.TwoDimensionalPoint;
import project.client.modelsforview.ViewLittleInformation;

import java.util.ArrayList;


public class ControllerForView {

//    public void getFinalCardLocationOfCurrentCardBeforeServer(CardView cardView) {
//        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
//        if (initialCardLocation != null) {
//            ViewLittleInformation viewLittleInformation;
//            if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
//                viewLittleInformation = provideInformationForOutpacingServerInSendingMonsterFromHandDeckOrGraveyardToMonsterZone(cardView);
//            } else {
//                viewLittleInformation = provideInformationForOutpacingServerInSendingSpellTrapFromHandDeckOrGraveyardToSpellZone(cardView);
//            }
//            RowOfCardLocation rowOfCardLocation = viewLittleInformation.getRowOfCardLocation();
//            boolean isFirstPlayerChoosing = viewLittleInformation.isFirstPlayerChoosing();
//            System.out.println("this is the input im receiving " + rowOfCardLocation + " pp " + isFirstPlayerChoosing);
//            DuelView.setCardLocationToSendCardTo(GameManager.getDuelBoardByIndex(0).giveAvailableCardLocationForUse(rowOfCardLocation, isFirstPlayerChoosing));
//            System.out.println("final card location given is " + DuelView.getCardLocationToSendCardTo().getRowOfCardLocation() + " and has index " + DuelView.getCardLocationToSendCardTo().getIndex());
//        }
//    }


    public CardLocation giveCardLocationByCoordinateInView(Object mouseOrDragEvent, CardView cardView) {
        //this function should be called before label is changed for the cardView
        double x = 0;
        double y = 0;
        if (mouseOrDragEvent instanceof TwoDimensionalPoint) {
            x = ((TwoDimensionalPoint) mouseOrDragEvent).getX();
            y = ((TwoDimensionalPoint) mouseOrDragEvent).getY();
        } else if (cardView == null || cardView.getUpperLeftX() < 0 || cardView.getUpperLeftY() < 0) {
            if (mouseOrDragEvent instanceof MouseEvent) {
                //             System.out.println("this is a mouse event babe");
                x = ((MouseEvent) mouseOrDragEvent).getSceneX();
                y = ((MouseEvent) mouseOrDragEvent).getSceneY();
            } else if (mouseOrDragEvent instanceof DragEvent) {
                //            System.out.println("this is a drag event babe");
                x = ((DragEvent) mouseOrDragEvent).getSceneX();
                y = ((DragEvent) mouseOrDragEvent).getSceneY();
            }

        } else {
            Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
            x = (bounds.getMinX() + bounds.getMaxX()) / 2;
            y = (bounds.getMinY() + bounds.getMaxY()) / 2;
        }
        //     System.out.println("x is " + x);
        //     System.out.println("y is " + y);
        //if (x )
        CardView cardViewAnalyzing = null;
        if (cardView == null || cardView.getUpperLeftX() < 0 || cardView.getUpperLeftY() < 0) {
            if (mouseOrDragEvent instanceof MouseEvent) {
                cardViewAnalyzing = (CardView) ((MouseEvent) mouseOrDragEvent).getSource();
            } else if (mouseOrDragEvent instanceof DragEvent) {
                cardViewAnalyzing = (CardView) ((DragEvent) mouseOrDragEvent).getSource();
            }
        } else {
            cardViewAnalyzing = cardView;
        }
        if (cardViewAnalyzing != null) {
            //         System.out.println("cardViewAnalyzing is " + cardViewAnalyzing.getCard().getCardName());
        }

        //     for (int i = 0; i < cardsInMyHand.size(); i++) {
        //         System.out.println(cardsInMyHand.get(i).getCard().getCardName() + " da I'm here");
        //     }
        if (cardViewAnalyzing != null) {
            if (giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                return new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_HAND_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_HAND_ZONE) + 1);
                //}
            }
            if (giveCardViewWithThisLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                return new CardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_GRAVEYARD_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE) + 1);
                //}
            }
            if (giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                //         System.out.println("this card is in graphic deck " + cardViewAnalyzing.getCard().getCardName() + " with index = " + (getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_DECK_ZONE) + 1));
                return new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_DECK_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_DECK_ZONE) + 1);
                //}
            }
            if (giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                return new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_HAND_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_HAND_ZONE) + 1);
                //}
            }
            if (giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                return new CardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_GRAVEYARD_ZONE) + 1);
                //}
            }
            if (giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE).contains(cardViewAnalyzing)) {
                //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                return new CardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.OPPONENT_DECK_ZONE) + 1);
                //} else {
                //    return new CardLocation(RowOfCardLocation.ALLY_DECK_ZONE, getIndexOfANodeInGroup(cardViewAnalyzing, RowOfCardLocation.ALLY_DECK_ZONE) + 1);
                //}
            }
        }

        //     System.out.println("I am so sorry, this is not good");
        double upperLeftXOfBattleField = DuelView.getBattleFieldView().getUpperLeftX();
        double upperLeftYOfBattleField = DuelView.getBattleFieldView().getUpperLeftY();
        double upperRightXOfBattleField = upperLeftXOfBattleField + DuelView.getBattleFieldView().getWidth();
        double lowerLeftYOfBattleField = upperLeftYOfBattleField + DuelView.getBattleFieldView().getHeight();
        //     System.out.println("upperLeftXOfBattleField is " + upperLeftXOfBattleField);
        //     System.out.println("upperRightXOfBattleField is " + upperRightXOfBattleField);
        //     System.out.println("upperLeftYOfBattleField" + upperLeftYOfBattleField);
        ArrayList<CardView> cardsInMyHand = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        if (y - upperLeftYOfBattleField >= 617.1 && y - upperLeftYOfBattleField <= 617.1 + CardView.getCardHeight()) {
            for (int i = 0; i < cardsInMyHand.size(); i++) {
                CardView cardView1 = getCardViewByCardLocation(new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1));
                Bounds bounds = cardView1.localToScene(cardView1.getBoundsInLocal());
                double x1 = (bounds.getMinX() + bounds.getMaxX()) / 2;
                double y1 = (bounds.getMinY() + bounds.getMaxY()) / 2;
                if (x - x1 >= (-1) * CardView.getCardWidth() / 2 && x - x1 <= CardView.getCardWidth() / 2) {
                    return new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, i + 1);
                }
            }
        }
        cardsInMyHand = giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        if (y - upperLeftYOfBattleField >= 0 && y - upperLeftYOfBattleField <= 130.9) {
            for (int i = 0; i < cardsInMyHand.size(); i++) {
                CardView cardView1 = getCardViewByCardLocation(new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, i + 1));
                Bounds bounds = cardView1.localToScene(cardView1.getBoundsInLocal());
                double x1 = (bounds.getMinX() + bounds.getMaxX()) / 2;
                double y1 = (bounds.getMinY() + bounds.getMaxY()) / 2;
                if (x - x1 >= (-1) * CardView.getCardWidth() / 2 && x - x1 <= CardView.getCardWidth() / 2) {
                    return new CardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, i + 1);
                }
            }
        }
        //       translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() - 7 - cardView.getUpperLeftX());
        //       translateTransition.setToY(506 - cardView.getUpperLeftY());
        //   } else {
        //       translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + 40 - cardView.getUpperLeftX());
        //       translateTransition.setToY(494 - CardView.getCardHeight() - cardView.getUpperLeftY());
        if (x - DuelView.getBattleFieldView().getUpperLeftX() - DuelView.getBattleFieldView().getWidth() + CardView.getCardWidth() + 7 >= 0 &&
            x - DuelView.getBattleFieldView().getUpperLeftX() - DuelView.getBattleFieldView().getWidth() + CardView.getCardWidth() + 7 <= CardView.getCardWidth() &&
            y - 506 >= 0 && y - 506 <= CardView.getCardHeight()) {
            System.out.println("\n\ngiving out ally graveyard 1");
            return new CardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, 1);
        }
        if (x - DuelView.getBattleFieldView().getUpperLeftX() - 40 >= 0 && x - DuelView.getBattleFieldView().getUpperLeftX() - 40 <= CardView.getCardWidth() &&
            y - 494 + CardView.getCardHeight() >= 0 && y - 494 + CardView.getCardHeight() <= CardView.getCardHeight()) {
            System.out.println("\n\ngiving out opponent graveyard 1");
            return new CardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, 1);
        }
        if (x - DuelView.getBattleFieldView().getUpperLeftX() - (0) * (CardView.getCardWidth() + 20.5) - 147.6 + CardView.getCardWidth() + 20 >= 0 &&
            x - DuelView.getBattleFieldView().getUpperLeftX() - (0) * (CardView.getCardWidth() + 20.5) - 147.6 + CardView.getCardWidth() + 20 <= CardView.getCardWidth() &&
            y - 510 - CardView.getCardHeight() + CardView.getCardHeight() >= 0 && y - 510 - CardView.getCardHeight() + CardView.getCardHeight() <= CardView.getCardHeight()) {
            return new CardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, 1);
        }
        if (x - DuelView.getBattleFieldView().getUpperLeftX() - (0) * (CardView.getCardWidth() + 20.5) - 147.6 - 6 * CardView.getCardWidth() - 20 >= 0 &&
            x - DuelView.getBattleFieldView().getUpperLeftX() - (0) * (CardView.getCardWidth() + 20.5) - 147.6 - 6 * CardView.getCardWidth() - 20 <= CardView.getCardWidth() &&
            y - 510 + 20 + 2 * CardView.getCardHeight() - CardView.getCardHeight() >= 0 && y - 510 + 20 + 2 * CardView.getCardHeight() - CardView.getCardHeight() <= CardView.getCardHeight()) {
            return new CardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, 1);
        }

//            double xTranslation = DuelView.getBattleFieldView().getUpperLeftX() + (Integer.parseInt((cardLocation.split("\n"))[1])) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX();
//        double yTranslation = 510 - 20 - 2 * CardView.getCardHeight() - cardView.getUpperLeftY();
//
//
//        translateTransition.setToX(xTranslation + 6 * CardView.getCardWidth() + 20);
//        translateTransition.setToY(yTranslation + CardView.getCardHeight());

        if (x - upperLeftXOfBattleField >= 146.4 && x - upperRightXOfBattleField <= -116.4) {
            //         System.out.println("going inside");
            if (y - upperLeftYOfBattleField >= 131 && y - upperLeftYOfBattleField <= 227) {

                // return RowOfCardLocation.OPPONENT_SPELL_ZONE;
                for (int i = 0; i < 5; i++) {
                    if (x - upperLeftXOfBattleField > 140 + 45 + CardView.getCardWidth() * i && x - upperLeftXOfBattleField < 140 + 45 + CardView.getCardWidth() * (i + 1)) {
                        // if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                        //                    System.out.println("HELL YEAH " + RowOfCardLocation.OPPONENT_SPELL_ZONE + " " + (i + 1));
                        return new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, i + 1);
                        //return new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false));
                        // } else {
                        //     return new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false));
                        // }
                    }
                }
            }
            if (y - upperLeftYOfBattleField >= 245 && y - upperLeftYOfBattleField <= 351) {
                // return RowOfCardLocation.OPPONENT_MONSTER_ZONE;
                for (int i = 0; i < 5; i++) {
                    if (x - upperLeftXOfBattleField > 140 + 45 + CardView.getCardWidth() * i && x - upperLeftXOfBattleField < 140 + 45 + CardView.getCardWidth() * (i + 1)) {
                        //   if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                        //                    System.out.println("HELL YEAH " + RowOfCardLocation.OPPONENT_MONSTER_ZONE + " " + (i + 1));
                        return new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, i + 1);
                        //return new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false));
                        //   } else {
                        //      return new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, false));
                        // }
                    }
                }
            }
            if (y - upperLeftYOfBattleField >= 380 && y - upperLeftYOfBattleField <= 502) {
                // return RowOfCardLocation.ALLY_MONSTER_ZONE;
                for (int i = 0; i < 5; i++) {
                    if (x - upperLeftXOfBattleField > 140 + 45 + CardView.getCardWidth() * i && x - upperLeftXOfBattleField < 140 + 45 + CardView.getCardWidth() * (i + 1)) {
                        // if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                        //                    System.out.println("HELL YEAH " + RowOfCardLocation.ALLY_MONSTER_ZONE + " " + (i + 1));
                        return new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, i + 1);
                        //return new CardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true));
                        // } else {
                        //     return new CardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true));
                        //}
                    }
                }
            }
            if (y - upperLeftYOfBattleField >= 521 && y - upperLeftYOfBattleField <= 617) {
                for (int i = 0; i < 5; i++) {
                    if (x - upperLeftXOfBattleField > 140 + 45 + CardView.getCardWidth() * i && x - upperLeftXOfBattleField < 140 + 45 + CardView.getCardWidth() * (i + 1)) {
                        //if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
                        //                     System.out.println("HELL YEAH " + RowOfCardLocation.ALLY_SPELL_ZONE + " " + (i + 1));
                        return new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, i + 1);
                        //return new CardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true));
                        //} else {
                        //    return new CardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, Utility.changeArrayIndexFromOneToFiveToYuGiOhIndex(i + 1, true));
                        //}
                    }
                }
                //return RowOfCardLocation.ALLY_SPELL_ZONE;
            }
        }
        //     System.out.println("returning null");
        return null;
    }

    public CardView getCardViewByCardLocation(CardLocation cardLocation) {
        System.out.println("The requested cardLocation is "+cardLocation.getRowOfCardLocation()+" "+cardLocation.getIndex());
        ArrayList<CardView> cardViews = giveCardViewWithThisLabel(cardLocation.getRowOfCardLocation());
        for (int i = 0; i < cardViews.size(); i++) {
            if (cardViews.get(i) == null) {
                System.out.println("\nTHIS IS AN INTERESTNG REPORT " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex() + " is null");
            }
            CardLocation thisCardLocation = giveCardLocationByCoordinateInView(null, cardViews.get(i));
          //  System.out.println("\nTHIS IS AN wonderful REPORT " + thisCardLocation.getRowOfCardLocation() + " " + thisCardLocation.getIndex() + " is null");
            if (thisCardLocation != null && thisCardLocation.getRowOfCardLocation().equals(cardLocation.getRowOfCardLocation()) && thisCardLocation.getIndex() == cardLocation.getIndex()) {
                return cardViews.get(i);
            }
        }
        System.out.println("card o locasion " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex());
        cardViews = giveCardViewWithThisLabel(cardLocation.getRowOfCardLocation());
        for (int i = 0; i < cardViews.size(); i++) {
            System.out.println("qwerty here! "+cardViews.get(i).getCard().getCardName()+" at index = "+i);
            //CardLocation thisCardLocation = giveCardLocationByCoordinateInView(null, cardViews.get(i));
            //if (thisCardLocation != null && thisCardLocation.getRowOfCardLocation().equals(cardLocation.getRowOfCardLocation()) && thisCardLocation.getIndex() == cardLocation.getIndex()) {
            //    return cardViews.get(i);
            // }
        }
        return null;
    }

    public ArrayList<CardView> giveCardViewWithThisLabel(RowOfCardLocation label) {
        ArrayList<CardView> cardViews = new ArrayList<>();
        for (int i = 0; i < DuelView.getAllCards().getChildren().size(); i++) {
            if (((CardView) DuelView.getAllCards().getChildren().get(i)).getLabel().equals(label)) {
                cardViews.add((CardView) DuelView.getAllCards().getChildren().get(i));
            }
        }
        return cardViews;
    }

    public int getIndexOfANodeInGroup(CardView cardView, RowOfCardLocation label) {
        ArrayList<CardView> cardViews = giveCardViewWithThisLabel(label);
        for (int i = 0; i < cardViews.size(); i++) {
            if (cardView.equals(cardViews.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void giveCardsAtTheBeginningOfGame() {
        DuelView.printChildrenInGroups();
        ArrayList<Card> allyCardsInHand = GameManager.getDuelBoardByIndex(DuelView.getToken()).getAllyCardsInHand();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            System.out.println(allyCardsInHand.get(i).getCardName() + " sfdsfd");
        }
        ParallelTransition allyParallelTransition = new ParallelTransition();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            ArrayList<CardView> cardViews = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE);
            CardView cardView = cardViews.get(0);
            cardView.setLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            cardView.setCanBeSeen(true);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            translate.setToX(10 + (DuelView.getBattleFieldView().getUpperLeftX()
                + (DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() * 5) / 2
                + CardView.getCardWidth() * (i) - cardView.getUpperLeftX()));
            translate.setToY(CardView.getCardHeight() - 19);
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            allyParallelTransition.getChildren().add(translate);
        }
        allyParallelTransition.play();
        ArrayList<Card> opponentCardsInHand = GameManager.getDuelBoardByIndex(DuelView.getToken()).getOpponentCardsInHand();
        ParallelTransition opponentParallelTransition = new ParallelTransition();
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            ArrayList<CardView> cardViews = giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE);
            CardView cardView = (CardView) cardViews.get(0);
            cardView.setLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            cardView.setCanBeSeen(false);
            //cardView.setCanBeSeen(GameManager.getDuelControllerByIndex(0).getFakeTurn() == 2);
            TranslateTransition translate = new TranslateTransition(Duration.millis(500), cardView);
            translate.setToX(10 + (DuelView.getBattleFieldView().getUpperLeftX()
                + (DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() * 5) / 2
                + CardView.getCardWidth() * (opponentCardsInHand.size() - i - 1) - cardView.getUpperLeftX()));
            translate.setToY(19 - CardView.getCardHeight());
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            opponentParallelTransition.getChildren().add(translate);
        }
        opponentParallelTransition.play();
    }


    private ViewLittleInformation provideInformationForOutpacingServerInSendingMonsterFromHandDeckOrGraveyardToMonsterZone(CardView cardView) {
        ViewLittleInformation viewLittleInformation = new ViewLittleInformation(null, false);
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            RowOfCardLocation rowOfCardLocation = null;
            boolean isFirstPlayerChoosing = false;
            boolean firstCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE);
            boolean secondCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
            boolean thirdCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE);
            boolean fourthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE);
            boolean fifthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
            boolean sixthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE);
            if (firstCase || secondCase || thirdCase) {
                rowOfCardLocation = RowOfCardLocation.ALLY_MONSTER_ZONE;
                isFirstPlayerChoosing = true;
            } else if (fourthCase || fifthCase || sixthCase) {
                rowOfCardLocation = RowOfCardLocation.OPPONENT_MONSTER_ZONE;
            }
            viewLittleInformation.setRowOfCardLocation(rowOfCardLocation);
            viewLittleInformation.setFirstPlayerChoosing(isFirstPlayerChoosing);
        }
        return viewLittleInformation;
    }

    public void changeLabelOfCardForSendingMonsterToMonsterZone(CardView cardView) {
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            boolean firstCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE);
            boolean secondCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
            boolean thirdCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE);
            boolean fourthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE);
            boolean fifthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
            boolean sixthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE);
            if (firstCase || secondCase || thirdCase) {
                cardView.setLabel(RowOfCardLocation.ALLY_MONSTER_ZONE);
            } else if (fourthCase || fifthCase || sixthCase) {
                cardView.setLabel(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
            }
        }
    }


    private ViewLittleInformation provideInformationForOutpacingServerInSendingSpellTrapFromHandDeckOrGraveyardToSpellZone(CardView cardView) {
        ViewLittleInformation viewLittleInformation = new ViewLittleInformation(null, false);
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            RowOfCardLocation rowOfCardLocation = null;
            boolean isFirstPlayerChoosing = false;
            boolean firstCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE);
            boolean secondCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
            boolean thirdCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE);
            boolean fourthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE);
            boolean fifthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
            boolean sixthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE);
            if (firstCase || secondCase || thirdCase) {
                //cardView.setViewOrder(3);
                isFirstPlayerChoosing = true;
                if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_FIELD_ZONE;
                } else {
                    rowOfCardLocation = RowOfCardLocation.ALLY_SPELL_ZONE;
                }
            } else if (fourthCase || fifthCase || sixthCase) {
                //cardView.setViewOrder(3);
                if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
                } else {
                    rowOfCardLocation = RowOfCardLocation.OPPONENT_SPELL_ZONE;
                }
            }
            viewLittleInformation.setRowOfCardLocation(rowOfCardLocation);
            viewLittleInformation.setFirstPlayerChoosing(isFirstPlayerChoosing);
        }
        return viewLittleInformation;
    }

    public void changeLabelOfCardForSendingSpellToSpellZone(CardView cardView) {
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            boolean firstCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE);
            boolean secondCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
            boolean thirdCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE);
            boolean fourthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE);
            boolean fifthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
            boolean sixthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE);
            if (firstCase || secondCase || thirdCase) {
                if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    cardView.setLabel(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE);
                } else {
                    cardView.setLabel(RowOfCardLocation.ALLY_SPELL_ZONE);
                }
            } else if (fourthCase || fifthCase || sixthCase) {
                if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                    cardView.setLabel(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE);
                } else {
                    cardView.setLabel(RowOfCardLocation.OPPONENT_SPELL_ZONE);
                }
            }
        }
    }

    public TranslateTransition sendCardToMonsterZone(CardView cardView, int turn, String token) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), cardView);
        if (turn == 1) {
            if (DuelView.getCardLocationToSendCardTo() == null) {
                DuelView.setCardLocationToSendCardTo(GameManager.getDuelBoardByIndex(token).giveAvailableCardLocationForUse(RowOfCardLocation.ALLY_MONSTER_ZONE, true));
                System.out.println("YOU ARE EXCLUSIVELY DOOMED CAUSE CARD LOCATION TO SEND CARD TO IS NULL");
            }
            String cardLocation = GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token);
            // System.out.println(cardLocation+" POUPOLl");
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + Integer.parseInt((cardLocation.split("\n"))[1]) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - cardView.getUpperLeftY());
        } else {
            String cardLocation = GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(token);
            //System.out.println(cardLocation+" POUPOLL");
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + Integer.parseInt((cardLocation.split("\n"))[1]) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - 20 - CardView.getCardHeight() - cardView.getUpperLeftY());
            cardView.setShouldBeSeen180DegreesReversed(true);
        }
        GameManager.getDuelControllerByIndex(DuelView.getToken()).clearAvailableCardLocationForUseForClient(token);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        return translateTransition;
    }


    public void changeLabelOfCardForSendingCardToGraveyardZone(CardView cardView) {
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            boolean firstCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE);
            boolean secondCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE);
            boolean thirdCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE);
            boolean fourthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE);
            boolean fifthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.ALLY_DECK_ZONE);
            boolean sixthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_HAND_ZONE);
            boolean seventhCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE);
            boolean eighthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_ZONE);
            boolean ninthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE);
            boolean tenthCase = initialCardLocation.getRowOfCardLocation().equals(RowOfCardLocation.OPPONENT_DECK_ZONE);
            if (firstCase || secondCase || thirdCase || fourthCase || fifthCase) {
                System.out.println("HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                cardView.setLabel(RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
            } else if (sixthCase || seventhCase || eighthCase || ninthCase || tenthCase) {
                System.out.println("HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                cardView.setLabel(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
            }
        }
    }

    public TranslateTransition sendCardToGraveyardZone(CardView cardView, int turn, double time, boolean isSpecial) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(time), cardView);
        if (turn == 0 && GameManager.getDuelControllerByIndex(DuelView.getToken()).getTurn() == 1 || turn == 1) {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() - 7 - cardView.getUpperLeftX());
            translateTransition.setToY(506 - cardView.getUpperLeftY());
        } else {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + 40 - cardView.getUpperLeftX());
            translateTransition.setToY(494 - CardView.getCardHeight() - cardView.getUpperLeftY());
            cardView.setShouldBeSeen180DegreesReversed(true);
        }
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        if (isSpecial){
            translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DuelView.getBattleFieldView().updateImageByThisName("");
                }
            });
        }
        return translateTransition;
    }

    public TranslateTransition sendCardToSpellZone(CardView cardView, int turn) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), cardView);
        String token = DuelView.getToken();
        if (turn == 1) {
            String cardLocation = GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(DuelView.getToken());
            double xTranslation = DuelView.getBattleFieldView().getUpperLeftX() + (Integer.parseInt((cardLocation.split("\n"))[1])) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX();
            double yTranslation = 510 + CardView.getCardHeight() - cardView.getUpperLeftY();
            if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                translateTransition.setToX(xTranslation - CardView.getCardWidth() - 20);
                translateTransition.setToY(yTranslation - CardView.getCardHeight());
            } else {
                translateTransition.setToX(xTranslation);
                translateTransition.setToY(yTranslation);
            }
        } else {
            String cardLocation = GameManager.getDuelControllerByIndex(token).getAvailableCardLocationForUseForClient(DuelView.getToken());
            double xTranslation = DuelView.getBattleFieldView().getUpperLeftX() + (Integer.parseInt((cardLocation.split("\n"))[1])) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX();
            double yTranslation = 510 - 20 - 2 * CardView.getCardHeight() - cardView.getUpperLeftY();
            if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                translateTransition.setToX(xTranslation + 6 * CardView.getCardWidth() + 20);
                translateTransition.setToY(yTranslation + CardView.getCardHeight());
            } else {
                translateTransition.setToX(xTranslation);
                translateTransition.setToY(yTranslation);
            }
            cardView.setShouldBeSeen180DegreesReversed(true);
        }
        GameManager.getDuelControllerByIndex(DuelView.getToken()).clearAvailableCardLocationForUseForClient(DuelView.getToken());
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        return translateTransition;
    }

    private void changeVisibilityOfCardsWhenTurnsChange() {
        ArrayList<CardView> allyCardsInHand = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        ArrayList<CardView> opponentCardsInHand = giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        int turn = GameManager.getDuelControllerByIndex(DuelView.getToken()).getTurn();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            if (turn == 1) {
                allyCardsInHand.get(i).setCanBeSeen(true);
            } else {
                allyCardsInHand.get(i).setCanBeSeen(false);
            }
        }
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            if (turn == 1) {
                opponentCardsInHand.get(i).setCanBeSeen(false);
            } else {
                opponentCardsInHand.get(i).setCanBeSeen(true);
            }
        }
    }

}
