package project.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import project.controller.duel.GamePackage.DuelBoard;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.model.modelsforview.CardView;
import project.view.pooyaviewpackage.TroubleFlipTransition;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList<CardAndCardLocation> allyCardsInHandAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> allySpellCardsAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> allySpellFieldCardAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> allyMonsterCardsAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> allyCardsInDeckAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> allyCardsInGraveyardAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentCardsInHandAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentSpellCardsAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentSpellFieldCardAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentMonsterCardsAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentCardsInDeckAdditions = new ArrayList<>();
    private ArrayList<CardAndCardLocation> opponentCardsInGraveyardAdditions = new ArrayList<>();
    private boolean firstTime = true;
    private boolean secondTime = false;
    private Handler allyHandZoneHandler;
    private Handler allyDeckZoneHandler;
    private Handler allyGraveyardZoneHandler;
    private Handler allyMonsterZoneHandler;
    private Handler allySpellZoneHandler;
    private Handler allySpellFieldZoneHandler;
    private Handler opponentHandZoneHandler;
    private Handler opponentDeckZoneHandler;
    private Handler opponentGraveyardZoneHandler;
    private Handler opponentMonsterZoneHandler;
    private Handler opponentSpellZoneHandler;
    private Handler opponentSpellFieldZoneHandler;


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

    public void deletion(ArrayList<Card> previous, ArrayList<Card> now, ArrayList<CardLocation> deletionArray, RowOfCardLocation rowOfCardLocation) {
        //considers ally monster and spell zones as index arrays not yugioh indexes
        int helpIndex = 0;
        for (int i = 0; i < previous.size(); i++) {
            if (previous.get(i) != null && (now.size() == 0 || now.get(helpIndex) != null)) {
                if (now.size() == 0 || !previous.get(i).equals(now.get(helpIndex))) {
                    deletionArray.add(new CardLocation(rowOfCardLocation, i + 1));
                    pureDeletionArraylist.add(new CardLocation(rowOfCardLocation, i + 1));
                } else {
                    helpIndex++;
                }
            }
        }
    }

    public void doubleDeletion(ArrayList<Card> previousFirst, ArrayList<Card> nowFirst, ArrayList<CardLocation> deletionArrayFirst,
                               ArrayList<Card> previousSecond, ArrayList<Card> nowSecond, ArrayList<CardLocation> deletionArraySecond, RowOfCardLocation rowOfCardLocation) {
        RowOfCardLocation rowOfCardLocationFirst = rowOfCardLocation;
        RowOfCardLocation rowOfCardLocationSecond = giveOpponentRowFromAllyRow(rowOfCardLocationFirst);
        deletion(previousFirst, nowFirst, deletionArrayFirst, rowOfCardLocationFirst);
        deletion(previousSecond, nowSecond, deletionArraySecond, rowOfCardLocationSecond);
    }

    public void addition(ArrayList<Card> previous, ArrayList<Card> now, ArrayList<CardAndCardLocation> additionArray, RowOfCardLocation rowOfCardLocation) {
        int helpIndex = 0;
        for (int i = 0; i < now.size(); i++) {
            if (now.get(i) != null && (previous.size() == 0 || previous.get(helpIndex) != null)) {
                if (previous.size() == 0 || !now.get(i).equals(previous.get(helpIndex))) {
                    System.out.println("ADDDITION ARRAY CARD = " + now.get(i).getCardName() + " cardLocation = " + rowOfCardLocation + " " + (i + 1));
                    additionArray.add(new CardAndCardLocation(now.get(i), new CardLocation(rowOfCardLocation, i + 1)));
                    if (pureDeletionArraylist.contains(new CardLocation(rowOfCardLocation, i + 1))) {
                        helpIndex++;
                    }
                } else {
                    helpIndex++;
                }
            }
        }
    }

    public void doubleAddition(ArrayList<Card> previousFirst, ArrayList<Card> nowFirst, ArrayList<CardAndCardLocation> additionArrayFirst,
                               ArrayList<Card> previousSecond, ArrayList<Card> nowSecond, ArrayList<CardAndCardLocation> additionArraySecond, RowOfCardLocation rowOfCardLocation) {
        RowOfCardLocation rowOfCardLocationFirst = rowOfCardLocation;
        RowOfCardLocation rowOfCardLocationSecond = giveOpponentRowFromAllyRow(rowOfCardLocationFirst);
        addition(previousFirst, nowFirst, additionArrayFirst, rowOfCardLocationFirst);
        addition(previousSecond, nowSecond, additionArraySecond, rowOfCardLocationSecond);
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

    public void prepareDeletionsAndAdditions() {
        doubleDeletion(allyCardsInDeck, allyCardsInDeckNew, allyCardsInDeckDeletions, opponentCardsInDeck, opponentCardsInDeckNew, opponentCardsInDeckDeletions, RowOfCardLocation.ALLY_DECK_ZONE);
        doubleDeletion(allyCardsInGraveyard, allyCardsInGraveyardNew, allyCardsInGraveyardDeletions, opponentCardsInGraveyard, opponentCardsInGraveyardNew, opponentCardsInGraveyardDeletions, RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
        doubleDeletion(allyCardsInHand, allyCardsInHandNew, allyCardsInHandDeletions, opponentCardsInHand, opponentCardsInHandNew, opponentCardsInHandDeletions, RowOfCardLocation.ALLY_HAND_ZONE);
        doubleDeletion(allyMonsterCards, allyMonsterCardsNew, allyMonsterCardsDeletions, opponentMonsterCards, opponentMonsterCardsNew, opponentMonsterCardsDeletions, RowOfCardLocation.ALLY_MONSTER_ZONE);
        doubleDeletion(allySpellCards, allySpellCardsNew, allySpellCardsDeletions, opponentSpellCards, opponentSpellCardsNew, opponentSpellCardsDeletions, RowOfCardLocation.ALLY_SPELL_ZONE);
        doubleDeletion(allySpellFieldCard, allySpellFieldCardNew, allySpellFieldCardDeletions, opponentSpellFieldCard, opponentSpellFieldCardNew, opponentSpellFieldCardDeletions, RowOfCardLocation.ALLY_SPELL_FIELD_ZONE);
        doubleAddition(allyCardsInDeck, allyCardsInDeckNew, allyCardsInDeckAdditions, opponentCardsInDeck, opponentCardsInDeckNew, opponentCardsInDeckAdditions, RowOfCardLocation.ALLY_DECK_ZONE);
        doubleAddition(allyCardsInGraveyard, allyCardsInGraveyardNew, allyCardsInGraveyardAdditions, opponentCardsInGraveyard, opponentCardsInGraveyardNew, opponentCardsInGraveyardAdditions, RowOfCardLocation.ALLY_GRAVEYARD_ZONE);
        doubleAddition(allyCardsInHand, allyCardsInHandNew, allyCardsInHandAdditions, opponentCardsInHand, opponentCardsInHandNew, opponentCardsInHandAdditions, RowOfCardLocation.ALLY_HAND_ZONE);
        doubleAddition(allyMonsterCards, allyMonsterCardsNew, allyMonsterCardsAdditions, opponentMonsterCards, opponentMonsterCardsNew, opponentMonsterCardsAdditions, RowOfCardLocation.ALLY_MONSTER_ZONE);
        doubleAddition(allySpellCards, allySpellCardsNew, allySpellCardsAdditions, opponentSpellCards, opponentSpellCardsNew, opponentSpellCardsAdditions, RowOfCardLocation.ALLY_SPELL_ZONE);
        doubleAddition(allySpellFieldCard, allySpellFieldCardNew, allySpellFieldCardAdditions, opponentSpellFieldCard, opponentSpellFieldCardNew, opponentSpellFieldCardAdditions, RowOfCardLocation.ALLY_SPELL_FIELD_ZONE);
        giveAbstractClassHandlerArrayLists();
    }

    HashMap<CardLocation, CardAndCardViewPair> cardAndCardViewPairHashMap = new HashMap<>();

    public void putValuesInCardAndCardViewHashMap() {
        ArrayList<CardView> cardViews = new ArrayList<>();
        for (int i = 0; i < DuelView.getAllCards().getChildren().size(); i++) {
            cardViews.add((CardView) DuelView.getAllCards().getChildren().get(i));
        }
        for (int i = 0; i < cardViews.size(); i++) {
            CardView cardView = cardViews.get(i);
            CardLocation cardLocation = DuelView.getControllerForView().giveCardLocationByCoordinateInView(null, cardView);
            System.out.println("card = " + cardView.getCard().getCardName() + " at cardlocation = " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex());
            Card card = getCardByCardLocation(cardLocation);
            if (card != null) {
                if (card.getCardName().equals(cardView.getCard().getCardName())) {
                    System.out.println("being newed cardAndCardViewPairHashMap is cardLocation = " + cardLocation.getRowOfCardLocation() + " " + cardLocation.getIndex() + " card = " + card.getCardName());
                    cardAndCardViewPairHashMap.put(cardLocation, new CardAndCardViewPair(cardView, card, cardLocation));
                }
            }
        }
    }

    public void refreshWholeBattleField() {
        allyHandZoneHandler = new AllyHandZoneHandler();
        allyDeckZoneHandler = new AllyDeckZoneHandler();
        allyGraveyardZoneHandler = new AllyGraveyardZoneHandler();
        allyMonsterZoneHandler = new AllyMonsterZoneHandler();
        allySpellZoneHandler = new AllySpellZoneHandler();
        allySpellFieldZoneHandler = new AllySpellFieldZoneHandler();
        opponentDeckZoneHandler = new OpponentDeckZoneHandler();
        opponentGraveyardZoneHandler = new OpponentGraveyardZoneHandler();
        opponentHandZoneHandler = new OpponentGraveyardZoneHandler();
        opponentMonsterZoneHandler = new OpponentMonsterZoneHandler();
        opponentSpellZoneHandler = new OpponentSpellZoneHandler();
        opponentSpellFieldZoneHandler = new OpponentSpellFieldZoneHandler();
        allyHandZoneHandler.setNextHandler(allyDeckZoneHandler);
        allyGraveyardZoneHandler.setNextHandler(opponentGraveyardZoneHandler);
        opponentGraveyardZoneHandler.setNextHandler(allySpellZoneHandler);
        allySpellZoneHandler.setNextHandler(opponentSpellZoneHandler);
        opponentSpellZoneHandler.setNextHandler(allySpellFieldZoneHandler);
        allySpellFieldZoneHandler.setNextHandler(opponentSpellFieldZoneHandler);
        opponentSpellFieldZoneHandler.setNextHandler(allyMonsterZoneHandler);
        allyMonsterZoneHandler.setNextHandler(opponentMonsterZoneHandler);
        opponentMonsterZoneHandler.setNextHandler(allyHandZoneHandler);
        allyHandZoneHandler.setNextHandler(opponentHandZoneHandler);
        opponentHandZoneHandler.setNextHandler(allyDeckZoneHandler);
        allyDeckZoneHandler.setNextHandler(opponentDeckZoneHandler);
        for (int i = 0; i < pureDeletionArraylist.size(); i++) {
            System.out.println("pureDeletionArraylist " + pureDeletionArraylist.get(i).getRowOfCardLocation() + " " + pureDeletionArraylist.get(i).getIndex());
            CardAndCardViewPair cardAndCardViewPair = cardAndCardViewPairHashMap.get(pureDeletionArraylist.get(i));

            allyHandZoneHandler.handle(cardAndCardViewPair);
        }
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

    public void giveAbstractClassHandlerArrayLists() {
        Handler.setAllyCardsInDeckAdditions(allyCardsInDeckAdditions);
        Handler.setAllyCardsInHandAdditions(allyCardsInHandAdditions);
        Handler.setAllyCardsInGraveyardAdditions(allyCardsInGraveyardAdditions);
        Handler.setAllyMonsterCardsAdditions(allyMonsterCardsAdditions);
        Handler.setAllySpellCardsAdditions(allySpellCardsAdditions);
        Handler.setAllySpellFieldCardAdditions(allySpellFieldCardAdditions);
        Handler.setOpponentCardsInDeckAdditions(opponentCardsInDeckAdditions);
        Handler.setOpponentCardsInHandAdditions(opponentCardsInHandAdditions);
        Handler.setOpponentCardsInGraveyardAdditions(opponentCardsInGraveyardAdditions);
        Handler.setOpponentMonsterCardsAdditions(opponentMonsterCardsAdditions);
        Handler.setOpponentSpellCardsAdditions(opponentSpellCardsAdditions);
        Handler.setOpponentSpellFieldCardAdditions(opponentSpellFieldCardAdditions);
    }


    public void advanceForwardBattleField() {
        String string = GameManager.getDuelControllerByIndex(0).getSuperAlmightyChangesString();
        System.out.println("all in all\n" + string + "\n\n");
        if (!string.equals("")) {
            String[] commands = string.split("\n");
            Object[] allActions = new Object[commands.length];
            prepareOneElementOfAllActions(allActions, 0, commands);
            GameManager.getDuelControllerByIndex(0).clearSuperAlmightyString();
        }
    }


    public void prepareOneElementOfAllActions(Object[] allActions, int i, String[] commands) {
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
        if (i != allActions.length - 1) {
            if (object instanceof ParallelTransition) {
                ParallelTransition parallelTransition = (ParallelTransition) object;
                if (i != allActions.length - 1) {
                    int finalI = i;
                    parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
//                        Object object = allActions[finalI + 1];
//                        if (object instanceof ParallelTransition) {
//                            ((ParallelTransition) object).play();
//                        } else if (object instanceof TroubleFlipTransition) {
//                            ((TroubleFlipTransition) object).getStHideFront().play();
//                        }
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
//                    Object object = allActions[finalI + 1];
//                    if (object instanceof ParallelTransition) {
//                        ((ParallelTransition) object).play();
//                    } else if (object instanceof TroubleFlipTransition) {
//                        ((TroubleFlipTransition) object).getStHideFront().play();
//                    }
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

abstract class Handler {
    protected Handler nextHandler;
    protected static ArrayList<CardAndCardLocation> allyCardsInHandAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> allySpellCardsAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> allySpellFieldCardAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> allyMonsterCardsAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> allyCardsInDeckAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> allyCardsInGraveyardAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentCardsInHandAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentSpellCardsAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentSpellFieldCardAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentMonsterCardsAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentCardsInDeckAdditions = new ArrayList<>();
    protected static ArrayList<CardAndCardLocation> opponentCardsInGraveyardAdditions = new ArrayList<>();

    public static void setAllyCardsInHandAdditions(ArrayList<CardAndCardLocation> allyCardsInHandAdditions) {
        Handler.allyCardsInHandAdditions = allyCardsInHandAdditions;
    }

    public static void setAllySpellCardsAdditions(ArrayList<CardAndCardLocation> allySpellCardsAdditions) {
        Handler.allySpellCardsAdditions = allySpellCardsAdditions;
    }

    public static void setAllySpellFieldCardAdditions(ArrayList<CardAndCardLocation> allySpellFieldCardAdditions) {
        Handler.allySpellFieldCardAdditions = allySpellFieldCardAdditions;
    }

    public static void setAllyMonsterCardsAdditions(ArrayList<CardAndCardLocation> allyMonsterCardsAdditions) {
        Handler.allyMonsterCardsAdditions = allyMonsterCardsAdditions;
    }

    public static void setAllyCardsInDeckAdditions(ArrayList<CardAndCardLocation> allyCardsInDeckAdditions) {
        Handler.allyCardsInDeckAdditions = allyCardsInDeckAdditions;
    }

    public static void setAllyCardsInGraveyardAdditions(ArrayList<CardAndCardLocation> allyCardsInGraveyardAdditions) {
        Handler.allyCardsInGraveyardAdditions = allyCardsInGraveyardAdditions;
    }

    public static void setOpponentCardsInHandAdditions(ArrayList<CardAndCardLocation> opponentCardsInHandAdditions) {
        Handler.opponentCardsInHandAdditions = opponentCardsInHandAdditions;
    }

    public static void setOpponentSpellCardsAdditions(ArrayList<CardAndCardLocation> opponentSpellCardsAdditions) {
        Handler.opponentSpellCardsAdditions = opponentSpellCardsAdditions;
    }

    public static void setOpponentSpellFieldCardAdditions(ArrayList<CardAndCardLocation> opponentSpellFieldCardAdditions) {
        Handler.opponentSpellFieldCardAdditions = opponentSpellFieldCardAdditions;
    }

    public static void setOpponentMonsterCardsAdditions(ArrayList<CardAndCardLocation> opponentMonsterCardsAdditions) {
        Handler.opponentMonsterCardsAdditions = opponentMonsterCardsAdditions;
    }

    public static void setOpponentCardsInDeckAdditions(ArrayList<CardAndCardLocation> opponentCardsInDeckAdditions) {
        Handler.opponentCardsInDeckAdditions = opponentCardsInDeckAdditions;
    }

    public static void setOpponentCardsInGraveyardAdditions(ArrayList<CardAndCardLocation> opponentCardsInGraveyardAdditions) {
        Handler.opponentCardsInGraveyardAdditions = opponentCardsInGraveyardAdditions;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handle(CardAndCardViewPair cardAndCardViewPair);
}

class AllyHandZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allyCardsInHandAdditions;
        boolean waiting = true;

        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                //   DuelView.getTransition().sendCardToHandZone(cardAndCardViewPair.getCardView(),
                //       allyCardsInHandAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) ? 1 : 2);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class AllyDeckZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allyCardsInDeckAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class AllyGraveyardZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allyCardsInGraveyardAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToGraveyardZone(cardAndCardViewPair.getCardView(),
                    allyCardsInGraveyardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) ? 1 : 2, 100).play();
                cardAndCardViewPair.getCardView().setLabel(
                    allyCardsInGraveyardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) ? RowOfCardLocation.ALLY_GRAVEYARD_ZONE : RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class AllyMonsterZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allyMonsterCardsAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToMonsterZone(cardAndCardViewPair.getCardView(),
                    allyMonsterCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    allyMonsterCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ? RowOfCardLocation.ALLY_MONSTER_ZONE : RowOfCardLocation.OPPONENT_MONSTER_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class AllySpellZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allySpellCardsAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToSpellZone(cardAndCardViewPair.getCardView(),
                    allySpellCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    allySpellCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) ? RowOfCardLocation.ALLY_SPELL_ZONE : RowOfCardLocation.OPPONENT_SPELL_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class AllySpellFieldZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allySpellFieldCardAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToSpellZone(cardAndCardViewPair.getCardView(),
                    allySpellFieldCardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    allySpellFieldCardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ? RowOfCardLocation.ALLY_SPELL_FIELD_ZONE : RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentHandZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = opponentCardsInHandAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToSpellZone(cardAndCardViewPair.getCardView(),
                    opponentCardsInHandAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    opponentCardsInHandAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_HAND_ZONE) ? RowOfCardLocation.ALLY_HAND_ZONE : RowOfCardLocation.OPPONENT_HAND_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentDeckZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = allyCardsInHandAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentGraveyardZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = opponentCardsInGraveyardAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToGraveyardZone(cardAndCardViewPair.getCardView(),
                    opponentCardsInGraveyardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) ? 1 : 2, 100).play();
                cardAndCardViewPair.getCardView().setLabel(
                    opponentCardsInGraveyardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE) ? RowOfCardLocation.ALLY_GRAVEYARD_ZONE : RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentMonsterZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = opponentMonsterCardsAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToMonsterZone(cardAndCardViewPair.getCardView(),
                    opponentMonsterCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    opponentMonsterCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ? RowOfCardLocation.ALLY_MONSTER_ZONE : RowOfCardLocation.OPPONENT_MONSTER_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentSpellZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = opponentSpellCardsAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToSpellZone(cardAndCardViewPair.getCardView(),
                    opponentSpellCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    opponentSpellCardsAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_ZONE) ? RowOfCardLocation.ALLY_SPELL_ZONE : RowOfCardLocation.OPPONENT_SPELL_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class OpponentSpellFieldZoneHandler extends Handler {

    @Override
    public void handle(CardAndCardViewPair cardAndCardViewPair) {
        ArrayList<CardAndCardLocation> finalPossibleDestination = opponentSpellFieldCardAdditions;
        boolean waiting = true;
        int index = 0;
        while (waiting && index < finalPossibleDestination.size()) {
            if (cardAndCardViewPair.getCard().getCardName().equals(finalPossibleDestination.get(index).getCard().getCardName())) {
                //action for adding card to ally hand zone place
                DuelView.getControllerForView().sendCardToSpellZone(cardAndCardViewPair.getCardView(),
                    opponentSpellFieldCardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ? 1 : 2).play();
                cardAndCardViewPair.getCardView().setLabel(
                    opponentSpellFieldCardAdditions.get(index).getCardLocation().getRowOfCardLocation().equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) ? RowOfCardLocation.ALLY_SPELL_FIELD_ZONE : RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE);
                finalPossibleDestination.remove(index);
                waiting = false;
            } else {
                index++;
            }
        }
        if (index == finalPossibleDestination.size()) {
            //nextHandler.handle(cardAndCardViewPair);
        }
    }
}

class CardAndCardViewPair {
    private Card card;
    private CardView cardView;
    private CardLocation cardLocation;

    public CardAndCardViewPair(CardView cardView, Card card, CardLocation cardLocation) {
        this.cardView = cardView;
        this.card = card;
        this.cardLocation = cardLocation;
    }

    public Card getCard() {
        return card;
    }

    public CardView getCardView() {
        return cardView;
    }

    public CardLocation getCardLocation() {
        return cardLocation;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public void setCardLocation(CardLocation cardLocation) {
        this.cardLocation = cardLocation;
    }
}

class CardAndCardLocation {
    private Card card;
    private CardLocation cardLocation;

    public CardAndCardLocation(Card card, CardLocation cardLocation) {
        this.card = card;
        this.cardLocation = cardLocation;
    }

    public Card getCard() {
        return card;
    }

    public CardLocation getCardLocation() {
        return cardLocation;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setCardLocation(CardLocation cardLocation) {
        this.cardLocation = cardLocation;
    }
}
