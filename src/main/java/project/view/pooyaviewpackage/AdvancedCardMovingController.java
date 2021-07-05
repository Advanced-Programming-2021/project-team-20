package project.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.model.modelsforview.CardView;

import java.util.ArrayList;

public class AdvancedCardMovingController {
    private ArrayList<Card> allyCardsInHand = new ArrayList<>();
    private ArrayList<Card> allySpellCards = new ArrayList<>();
    private ArrayList<Card> allySpellFieldCard = new ArrayList<>();
    private ArrayList<Card> allyMonsterCards = new ArrayList<>();
    private ArrayList<Card> allyCardsInDeck = new ArrayList<>();
    private ArrayList<Card> allyCardsInGraveyard = new ArrayList<>();
    private ArrayList<Card> opponentCardsInHand = new ArrayList<>();
    private ArrayList<Card> opponentSpellCards = new ArrayList<>();
    private ArrayList<Card> opponentSpellFieldCard = new ArrayList<>();
    private ArrayList<Card> opponentMonsterCards = new ArrayList<>();
    private ArrayList<Card> opponentCardsInDeck = new ArrayList<>();
    private ArrayList<Card> opponentCardsInGraveyard = new ArrayList<>();
    private ArrayList<Card> allyCardsInHandNew = new ArrayList<>();
    private ArrayList<Card> allySpellCardsNew = new ArrayList<>();
    private ArrayList<Card> allySpellFieldCardNew = new ArrayList<>();
    private ArrayList<Card> allyMonsterCardsNew = new ArrayList<>();
    private ArrayList<Card> allyCardsInDeckNew = new ArrayList<>();
    private ArrayList<Card> allyCardsInGraveyardNew = new ArrayList<>();
    private ArrayList<Card> opponentCardsInHandNew = new ArrayList<>();
    private ArrayList<Card> opponentSpellCardsNew = new ArrayList<>();
    private ArrayList<Card> opponentSpellFieldCardNew = new ArrayList<>();
    private ArrayList<Card> opponentMonsterCardsNew = new ArrayList<>();
    private ArrayList<Card> opponentCardsInDeckNew = new ArrayList<>();
    private ArrayList<Card> opponentCardsInGraveyardNew = new ArrayList<>();
    private ArrayList<CardLocation> allyCardsInHandDeletions = new ArrayList<>();
    private ArrayList<CardLocation> allySpellCardsDeletions = new ArrayList<>();
    private ArrayList<CardLocation> allySpellFieldCardDeletions = new ArrayList<>();
    private ArrayList<CardLocation> allyMonsterCardsDeletions = new ArrayList<>();
    private ArrayList<CardLocation> allyCardsInDeckDeletions = new ArrayList<>();
    private ArrayList<CardLocation> allyCardsInGraveyardDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentCardsInHandDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentSpellCardsDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentSpellFieldCardDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentMonsterCardsDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentCardsInDeckDeletions = new ArrayList<>();
    private ArrayList<CardLocation> opponentCardsInGraveyardDeletions = new ArrayList<>();
    private ArrayList<CardLocation> pureDeletionArraylist = new ArrayList<>();
    private boolean firstTime = true;
    private boolean secondTime = false;


    public void getWholeBoardInformationFromServer() {
        if (firstTime) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            allyCardsInDeck.clear();
            allyCardsInDeck.addAll(duelBoard.getAllyCardsInDeck());
            allyCardsInHand.clear();
            allyCardsInHand.addAll(duelBoard.getAllyCardsInHand());
            allyCardsInGraveyard.clear();
            allyCardsInGraveyard.addAll(duelBoard.getAllyCardsInGraveyard());
            allyMonsterCards.clear();
            allyMonsterCards.addAll(duelBoard.getAllyMonsterCards());
            allySpellCards.clear();
            allySpellCards.addAll(duelBoard.getAllySpellCards());
            allySpellFieldCard.clear();
            allySpellFieldCard.addAll(duelBoard.getAllySpellFieldCard());
            opponentCardsInDeck.clear();
            opponentCardsInDeck.addAll(duelBoard.getOpponentCardsInDeck());
            opponentCardsInHand.clear();
            opponentCardsInHand.addAll(duelBoard.getOpponentCardsInHand());
            opponentCardsInGraveyard.clear();
            opponentCardsInGraveyard.addAll(duelBoard.getOpponentCardsInGraveyard());
            opponentMonsterCards.clear();
            opponentMonsterCards.addAll(duelBoard.getOpponentMonsterCards());
            opponentSpellCards.clear();
            opponentSpellCards.addAll(duelBoard.getOpponentSpellCards());
            opponentSpellFieldCard.clear();
            opponentSpellFieldCard.addAll(duelBoard.getAllySpellFieldCard());
            for (int i = 0; i < allyCardsInDeck.size(); i++) {
                System.out.println("taken from server duelboard allyCardsInDeck " + i + " card = " + allyCardsInDeck.get(i).getCardName());
            }
            firstTime = false;
            secondTime = true;
        } else if (secondTime) {
            DuelBoard duelBoard = GameManager.getDuelBoardByIndex(0);
            allyCardsInDeckNew.clear();
            allyCardsInDeckNew.addAll(duelBoard.getAllyCardsInDeck());
            allyCardsInHandNew.clear();
            allyCardsInHandNew.addAll(duelBoard.getAllyCardsInHand());
            allyCardsInGraveyardNew.clear();
            allyCardsInGraveyardNew.addAll(duelBoard.getAllyCardsInGraveyard());
            allyMonsterCardsNew.clear();
            allyMonsterCardsNew.addAll(duelBoard.getAllyMonsterCards());
            allySpellCardsNew.clear();
            allySpellCardsNew.addAll(duelBoard.getAllySpellCards());
            allySpellFieldCardNew.clear();
            allySpellFieldCardNew.addAll(duelBoard.getAllySpellFieldCard());
            opponentCardsInDeckNew.clear();
            opponentCardsInDeckNew.addAll(duelBoard.getOpponentCardsInDeck());
            opponentCardsInHandNew.clear();
            opponentCardsInHandNew.addAll(duelBoard.getOpponentCardsInHand());
            opponentCardsInGraveyardNew.clear();
            opponentCardsInGraveyardNew.addAll(duelBoard.getOpponentCardsInGraveyard());
            opponentMonsterCardsNew.clear();
            opponentMonsterCardsNew.addAll(duelBoard.getOpponentMonsterCards());
            opponentSpellCardsNew.clear();
            opponentSpellCardsNew.addAll(duelBoard.getOpponentSpellCards());
            opponentSpellFieldCardNew.clear();
            opponentSpellFieldCardNew.addAll(duelBoard.getAllySpellFieldCard());
            for (int i = 0; i < allyCardsInDeckNew.size(); i++) {
                System.out.println("taken from server duelboard allyCardsInDeck " + i + " card = " + allyCardsInDeckNew.get(i).getCardName());
            }
            firstTime = true;
            secondTime = false;
        }

    }


    public RowOfCardLocation giveOpponentRowFromAllyRow(RowOfCardLocation rowOfCardLocation) {
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            return RowOfCardLocation.OPPONENT_HAND_ZONE;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return RowOfCardLocation.OPPONENT_MONSTER_ZONE;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            return RowOfCardLocation.OPPONENT_DECK_ZONE;
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            return RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE;
        }
        return null;
    }


    private Card getCardByCardLocation(CardLocation cardLocation) {
        RowOfCardLocation rowOfCardLocation = cardLocation.getRowOfCardLocation();
        int index = cardLocation.getIndex();
        if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE)) {
            return allyMonsterCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_ZONE)) {
            return allySpellCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE)) {
            return allySpellFieldCard.get(0);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_HAND_ZONE)) {
            return allyCardsInHand.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_DECK_ZONE)) {
            System.out.println("wuiii index = " + (index - 1));
            return allyCardsInDeck.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
            return allyCardsInGraveyard.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)) {
            return opponentMonsterCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_ZONE)) {
            return opponentSpellCards.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE)) {
            return opponentSpellFieldCard.get(0);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_HAND_ZONE)) {
            return opponentCardsInHand.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_DECK_ZONE)) {
            return opponentCardsInDeck.get(index - 1);
        } else if (rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE)) {
            return opponentCardsInGraveyard.get(index - 1);
        } else {
            return null;
        }
    }


    public void advanceForwardBattleField() {
        String string = GameManager.getDuelControllerByIndex(0).getSuperAlmightyChangesString();
        String anotherString = GameManager.getDuelControllerByIndex(0).getChangesInLifePointsToBeGivenToClient();
        System.out.println("all in all\n" + string + "\n\n");
        if (!string.equals("")) {
            String[] commands = string.split("\n");
            Object[] allActions = new Object[commands.length + 1];
            prepareOneElementOfAllActions(allActions, 0, commands);
            GameManager.getDuelControllerByIndex(0).clearSuperAlmightyString();
        } else if (!anotherString.equals("")) {
            Object[] allActions = new Object[1];
            prepareOneElementOfAllActions(allActions, 0, null);
            GameManager.getDuelControllerByIndex(0).clearSuperAlmightyString();
        }
    }


    public void prepareOneElementOfAllActions(Object[] allActions, int i, String[] commands) {
        if (i == allActions.length - 1) {
            String string = GameManager.getDuelControllerByIndex(0).getChangesInLifePointsToBeGivenToClient();
            System.out.println("WITNESS CHANGES IN LIFE POINTS I HAVE RECEIVED\n" + string);
            if (!string.equals("")) {
                String[] substrings = string.split("\n");
                int totalAllyIncreaseInHealth = 0;
                int totalOpponentIncreaseInHealth = 0;
                for (int j = 0; j < substrings.length; j++) {
                    int turn = Integer.parseInt(substrings[j].split(" ")[3]);
                    int increaseInHealth = Integer.parseInt(substrings[j].split(" ")[9]);
                    System.out.println("TURN SI " + turn + " AND INCREASEINHEALTH IS " + increaseInHealth);
                    if (turn == 1) {
                        totalAllyIncreaseInHealth += increaseInHealth;
                    } else {
                        totalOpponentIncreaseInHealth += increaseInHealth;
                    }
                }
                UpdateHealthPointsTransition allyTransition = DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition();
                allyTransition.setIncreaseInHealth(allyTransition.getIncreaseInHealth() + totalAllyIncreaseInHealth);
                UpdateHealthPointsTransition opponentTransition = DuelView.getOpponentHealthStatus().getUpdateHealthPointsTransition();
                opponentTransition.setIncreaseInHealth(opponentTransition.getIncreaseInHealth() + totalOpponentIncreaseInHealth);
                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().add(DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition());
                parallelTransition.getChildren().add(DuelView.getOpponentHealthStatus().getUpdateHealthPointsTransition());
                //parallelTransition.getChildren().add();
                parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(true, totalAllyIncreaseInHealth,
                    DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
                parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(false, totalOpponentIncreaseInHealth,
                    DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
                allActions[i] = parallelTransition;
                GameManager.getDuelControllerByIndex(0).clearChangesInLifePointsToBeGivenToClient();
                parallelTransition.play();
            }
            return;
        }
        System.out.println(commands[i]);
        String[] subCommands = commands[i].split(" ");
        RowOfCardLocation initialRowOfCardLocation = RowOfCardLocation.valueOf(subCommands[1]);
        int index = Integer.parseInt(subCommands[2]);
        boolean staying = subCommands[5].equals("stayed");
        String finalDestination = subCommands[7];
        int sideOfFinalDestination = (subCommands[9].equals("zone") ? 0 : Integer.parseInt(subCommands[9]));
        CardPosition finalCardPosition = (subCommands[14].equals("NO_CHANGE") ? CardPosition.NOT_APPLICABLE : CardPosition.valueOf(subCommands[14]));
        System.out.println("rowOfCardLocation = " + initialRowOfCardLocation + " index = " + index + " finalDestination = " +
            finalDestination + " sideOfFinalDestination = " + sideOfFinalDestination + " finalCardPosition = " + finalCardPosition);
        CardView cardView = DuelView.getControllerForView().getCardViewByCardLocation(new CardLocation(initialRowOfCardLocation, index));
        if (cardView == null) {
            System.out.println("\nYOU ARE DOOMED SEVERELY BECAUSE CARDVIEW IS NULL\n");
        }
        if (finalDestination.equals("monster")) {
            //changing card positions not considered
            if (finalCardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                if (initialRowOfCardLocation.toString().contains("MONSTER")) {
                    allActions[i] = DuelView.getTransition().applyTransitionForFlipSummoning(cardView);
                } else {
                    allActions[i] = DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView);
                }
            } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                allActions[i] = DuelView.getTransition().applyTransitionForSettingMonsterCard(cardView);
            } else if (finalCardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                if (initialRowOfCardLocation.toString().contains("MONSTER")) {
                    allActions[i] = DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, true, 250);
                } else {
                    allActions[i] = DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView);
                }
            }
        } else if (finalDestination.equals("spell")) {
            if (staying) {
                allActions[i] = DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION), 250);
            } else {
                if (finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                    allActions[i] = DuelView.getTransition().applyTransitionForActivatingSpellTrapSuper(cardView);
                } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_SPELL_SET_POSITION)) {
                    allActions[i] = DuelView.getTransition().applyTransitionForSettingSpellTrapCard(cardView);
                }
            }
        } else if (finalDestination.equals("graveyard")) {
            allActions[i] = DuelView.getTransition().applyTransitionForSendingCardToGraveyard(cardView, sideOfFinalDestination,
                !(cardView.getCard().getCardType().equals(CardType.MONSTER) &&
                    (initialRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ||
                        initialRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))));
        } else if (finalDestination.equals("hand")) {
            allActions[i] = DuelView.getTransition().sendCardToHandZone(cardView, sideOfFinalDestination);
        }

        Object object = allActions[i];
        if (i <= allActions.length - 1) {
            if (object instanceof ParallelTransition) {
                ParallelTransition parallelTransition = (ParallelTransition) object;
                if (i != allActions.length - 1) {
                    int finalI = i;
                    parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            prepareOneElementOfAllActions(allActions, i + 1, commands);
                        }
                    });
                }
            } else if (object instanceof TroubleFlipTransition) {
                TroubleFlipTransition troubleFlipTransition = (TroubleFlipTransition) object;
                int finalI = i;
                troubleFlipTransition.getStShowBack().setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        prepareOneElementOfAllActions(allActions, i + 1, commands);
                    }
                });
            }
        }
        if (object instanceof ParallelTransition) {
            ((ParallelTransition) object).play();
        } else if (object instanceof TroubleFlipTransition) {
            ((TroubleFlipTransition) object).getStHideFront().play();
        }
    }


}



