package project.View;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;

import java.util.ArrayList;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.controller.duel.Utility.Utility;
import project.model.cardData.General.Card;
import project.model.cardData.General.CardLocation;
import project.model.cardData.General.CardType;
import project.model.cardData.General.RowOfCardLocation;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.modelsforview.CardView;
import project.model.modelsforview.TwoDimensionalPoint;
import project.model.modelsforview.ViewLittleInformation;

import java.util.ArrayList;


public class ControllerForView {

    public void getFinalCardLocationOfCurrentCardBeforeServer(CardView cardView) {
        CardLocation initialCardLocation = giveCardLocationByCoordinateInView(null, cardView);
        if (initialCardLocation != null) {
            ViewLittleInformation viewLittleInformation;
            if (cardView.getCard().getCardType().equals(CardType.MONSTER)) {
                viewLittleInformation = provideInformationForOutpacingServerInSendingMonsterFromHandDeckOrGraveyardToMonsterZone(cardView);
            } else {
                viewLittleInformation = provideInformationForOutpacingServerInSendingSpellTrapFromHandDeckOrGraveyardToSpellZone(cardView);
            }
            RowOfCardLocation rowOfCardLocation = viewLittleInformation.getRowOfCardLocation();
            boolean isFirstPlayerChoosing = viewLittleInformation.isFirstPlayerChoosing();
            System.out.println("this is the input im receiving " + rowOfCardLocation + " pp " + isFirstPlayerChoosing);
            DuelView.setCardLocationToSendCardTo(GameManager.getDuelBoardByIndex(0).giveAvailableCardLocationForUse(rowOfCardLocation, isFirstPlayerChoosing));
            System.out.println("final card location given is " + DuelView.getCardLocationToSendCardTo().getRowOfCardLocation() + " and has index " + DuelView.getCardLocationToSendCardTo().getIndex());
        }
    }


    public CardLocation giveCardLocationByCoordinateInView(Object mouseOrDragEvent, CardView cardView) {
        //this function should be called before label is changed for the cardView
        double x = 0;
        double y = 0;
        if (mouseOrDragEvent instanceof TwoDimensionalPoint) {
            x = ((TwoDimensionalPoint) mouseOrDragEvent).getX();
            y = ((TwoDimensionalPoint) mouseOrDragEvent).getY();
        } else if (cardView == null || cardView.getUpperLeftX() < 0 || cardView.getUpperLeftY() < 0) {
            if (mouseOrDragEvent instanceof MouseEvent) {
                System.out.println("this is a mouse event babe");
                x = ((MouseEvent) mouseOrDragEvent).getSceneX();
                y = ((MouseEvent) mouseOrDragEvent).getSceneY();
            } else if (mouseOrDragEvent instanceof DragEvent) {
                System.out.println("this is a drag event babe");
                x = ((DragEvent) mouseOrDragEvent).getSceneX();
                y = ((DragEvent) mouseOrDragEvent).getSceneY();
            }

        } else {
            Bounds bounds = cardView.localToScene(cardView.getBoundsInLocal());
            x = bounds.getMinX();
            y = bounds.getMinY();
        }
        System.out.println("x is " + x);
        System.out.println("y is " + y);
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
        System.out.println("cardViewAnalyzing is " + cardViewAnalyzing);
        ArrayList<CardView> cardsInMyHand = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        for (int i = 0; i < cardsInMyHand.size(); i++) {
            System.out.println(cardsInMyHand.get(i).getCard().getCardName() + " da I'm here");
        }
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
        System.out.println("I am so sorry, this is not good");
        double upperLeftXOfBattleField = DuelView.getBattleFieldView().getUpperLeftX();
        double upperLeftYOfBattleField = DuelView.getBattleFieldView().getUpperLeftY();
        double upperRightXOfBattleField = upperLeftXOfBattleField + DuelView.getBattleFieldView().getWidth();
        double lowerLeftYOfBattleField = upperLeftYOfBattleField + DuelView.getBattleFieldView().getHeight();
        System.out.println("upperLeftXOfBattleField is " + upperLeftXOfBattleField);
        System.out.println("upperRightXOfBattleField is " + upperRightXOfBattleField);
        System.out.println("upperLeftYOfBattleField" + upperLeftYOfBattleField);
        if (x - upperLeftXOfBattleField >= 146.4 && x - upperRightXOfBattleField <= -116.4) {
            System.out.println("going inside");
            if (y - upperLeftYOfBattleField >= 131 && y - upperLeftYOfBattleField <= 227) {

                // return RowOfCardLocation.OPPONENT_SPELL_ZONE;
                for (int i = 0; i < 5; i++) {
                    if (x - upperLeftXOfBattleField > 140 + 45 + CardView.getCardWidth() * i && x - upperLeftXOfBattleField < 140 + 45 + CardView.getCardWidth() * (i + 1)) {
                        // if (GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1) {
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
        System.out.println("returning null");
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
        ArrayList<Card> allyCardsInHand = GameManager.getDuelBoardByIndex(0).getAllyCardsInHand();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            System.out.println(allyCardsInHand.get(i).getCardName() + " sfdsfd");
        }
        ParallelTransition allyParallelTransition = new ParallelTransition();
        for (int i = 0; i < allyCardsInHand.size(); i++) {
            ArrayList<CardView> cardViews = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_DECK_ZONE);
            CardView cardView = cardViews.get(0);
            cardView.setLabel(RowOfCardLocation.ALLY_HAND_ZONE);
            cardView.setCanBeSeen(GameManager.getDuelControllerByIndex(0).getFakeTurn() == 1);
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
        ArrayList<Card> opponentCardsInHand = GameManager.getDuelBoardByIndex(0).getOpponentCardsInHand();
        ParallelTransition opponentParallelTransition = new ParallelTransition();
        for (int i = 0; i < opponentCardsInHand.size(); i++) {
            ArrayList<CardView> cardViews = giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_DECK_ZONE);
            CardView cardView = (CardView) cardViews.get(0);
            cardView.setLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
            cardView.setCanBeSeen(true);
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


    public TranslateTransition sendCardToMonsterZone(CardView cardView) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), cardView);
        if (GameManager.getDuelControllerByIndex(0).getTurn() == 1) {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getCardLocationToSendCardTo().getIndex()) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - cardView.getUpperLeftY());
        } else {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getCardLocationToSendCardTo().getIndex()) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - 20 - CardView.getCardHeight() - cardView.getUpperLeftY());
            cardView.setShouldBeSeen180DegreesReversed(true);
        }
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        return translateTransition;
    }


    public TranslateTransition sendCardToGraveyardZone(CardView cardView) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), cardView);
        if (GameManager.getDuelControllerByIndex(0).getTurn() == 1) {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() - 7 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - cardView.getUpperLeftY());
        } else {
            translateTransition.setToX(DuelView.getBattleFieldView().getUpperLeftX() + (0) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX());
            translateTransition.setToY(510 - 20 - CardView.getCardHeight() - cardView.getUpperLeftY());
            cardView.setShouldBeSeen180DegreesReversed(true);
        }
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        return translateTransition;
    }

    public TranslateTransition sendCardToSpellZone(CardView cardView) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), cardView);
        if (GameManager.getDuelControllerByIndex(0).getTurn() == 1) {
            double xTranslation = DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getCardLocationToSendCardTo().getIndex()) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX();
            double yTranslation = 510 + CardView.getCardHeight() - cardView.getUpperLeftY();
            if (cardView.getCard().getCardType().equals(CardType.SPELL) && ((SpellCard) cardView.getCard()).getSpellCardValue().equals(SpellCardValue.FIELD)) {
                translateTransition.setToX(xTranslation - CardView.getCardWidth() - 20);
                translateTransition.setToY(yTranslation - CardView.getCardHeight());
            } else {
                translateTransition.setToX(xTranslation);
                translateTransition.setToY(yTranslation);
            }
        } else {
            double xTranslation = DuelView.getBattleFieldView().getUpperLeftX() + (DuelView.getCardLocationToSendCardTo().getIndex()) * (CardView.getCardWidth() + 20.5) + 147.6 - cardView.getUpperLeftX();
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
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        return translateTransition;
    }

    private void changeVisibilityOfCardsWhenTurnsChange() {
        ArrayList<CardView> allyCardsInHand = giveCardViewWithThisLabel(RowOfCardLocation.ALLY_HAND_ZONE);
        ArrayList<CardView> opponentCardsInHand = giveCardViewWithThisLabel(RowOfCardLocation.OPPONENT_HAND_ZONE);
        int turn = GameManager.getDuelControllerByIndex(0).getTurn();
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
