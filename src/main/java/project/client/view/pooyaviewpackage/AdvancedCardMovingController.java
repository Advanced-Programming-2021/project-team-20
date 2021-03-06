package project.client.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import project.model.PhaseInGame;
import project.model.Utility.Utility;
import project.model.cardData.General.*;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.client.modelsforview.CardView;
import project.client.modelsforview.GamePhaseButton;
//import project.server.controller.duel.GamePackage.DuelBoard;
import project.server.controller.duel.GamePackage.DuelController;
//import project.server.controller.duel.PreliminaryPackage.GameManager;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedCardMovingController {
    ArrayList<ChangeConductor> allChangeConductorsObjects;
    private static String report = "";
    private DuelView duelView;
    private static Card newlyAddedCard;
    private static CardLocation previousSelected = new CardLocation(RowOfCardLocation.ALLY_HAND_ZONE, 1);

    public static CardLocation getPreviousSelected() {
        return previousSelected;
    }

    public static void setPreviousSelected(CardLocation previousSelected) {
        AdvancedCardMovingController.previousSelected = previousSelected;
    }

    public static void setNewlyAddedCard(Card newlyAddedCard) {
        AdvancedCardMovingController.newlyAddedCard = newlyAddedCard;
    }

    public AdvancedCardMovingController(DuelView duelView) {
        this.duelView = duelView;
    }

    public static String getReport() {
        return report;
    }

    public static void setReport(String report) {
        AdvancedCardMovingController.report = report;
    }

    public ArrayList<ChangeConductor> getAllChangeConductorsObjects() {
        return allChangeConductorsObjects;
    }

    public void advanceForwardBattleField() {
        //this function simply splits whole report into pieces for better understanding
        String string = JsonCreator.getResult("DuelStarter.getGameManager().getWholeReportToClient()");
        if (!string.isBlank()) {
            System.out.println("advanceForwardBattleField is called with input =\n" + string);
//            String winloss = "(\\S+) won the game and the score is: (\\S+)";
//            Pattern pattern = Pattern.compile(winloss);
//            Matcher matcher = pattern.matcher(string);
//            String token = DuelView.getToken();
//            if (matcher.find()) {
//                report = matcher.group(0);
//                boolean oneRound = true;
//                int numberOfRounds = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getNumberOfRounds()"));
//                if (numberOfRounds == 3) {
//                    oneRound = false;
//                }
//                int currentRound = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getCurrentRound()"));
//                System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
//                String output = AdvancedCardMovingController.getReport();
//                System.out.println("WinLoss: " + output);
//                DuelView.getStage().close();
//                DuelView.getBackgroundMusic().setMute(true);
//                // if (!oneRound && (currentRound == 1 || currentRound == 2)) {
//                DuelView.endOneRoundOfDuel(matcher.group(0));
//                AdvancedCardMovingController.setReport("");
//            } else {
//                winloss = "(\\S+) won the whole match with score: (\\S+)";
//                pattern = Pattern.compile(winloss);
//                matcher = pattern.matcher(string);
//                if (matcher.find()) {
//                    report = matcher.group(0);
//                    //System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
//                    //   String output = AdvancedCardMovingController.getReport();
//                    //    System.out.println("WinLoss: " + output);
//                    DuelView.getStage().close();
//                    DuelView.endGame(matcher.group(0));
//                    //    AdvancedCardMovingController.setReport("");
//                } else {
            String[] miniStrings = string.split("\n");
            int numberOfUserSayings = 0;
            for (int i = 0; i < miniStrings.length; i++) {
                if (miniStrings[i].startsWith("*")) {
                    numberOfUserSayings++;
                }
            }
            ArrayList<ArrayList<String>> changeConductorsInStringForm = new ArrayList<>();
            ArrayList<String> bulkOfStrings = new ArrayList<>();
            boolean firstTimeOccurringUserSaying = true;
            for (int i = 0; i < miniStrings.length; i++) {
                System.out.println("one of ministrings " + i + " is " + miniStrings[i]);
                if (miniStrings[i].startsWith("*") && firstTimeOccurringUserSaying) {
                    bulkOfStrings.add(miniStrings[i]);
                    firstTimeOccurringUserSaying = false;
                } else if (miniStrings[i].startsWith("*")) {
                    //changeConductorsInStringForm.get(helpIndex).add(new ArrayList<>());
                    changeConductorsInStringForm.add(bulkOfStrings);
                    bulkOfStrings = new ArrayList<>();
                    bulkOfStrings.add(miniStrings[i]);
                } else {
                    bulkOfStrings.add(miniStrings[i]);
                }
            }
            changeConductorsInStringForm.add(bulkOfStrings);
            bulkOfStrings = new ArrayList<>();
            allChangeConductorsObjects = new ArrayList<>();
            int helpIndex = 0;
            for (int i = 0; i < changeConductorsInStringForm.size(); i++) {
                for (int j = 0; j < changeConductorsInStringForm.get(i).size(); j++) {
                    System.out.println("this string is being added " + changeConductorsInStringForm.get(i).get(j));
                    allChangeConductorsObjects.add(new ChangeConductor(changeConductorsInStringForm.get(i).get(j), helpIndex));
                    helpIndex++;
                }
            }
            allChangeConductorsObjects.get(0).conductChange();
            JsonCreator.getResult("DuelStarter.getGameManager().clearWholeReportToClient()");
        }
        //}

        //}
    }

    public static String allyChainingMessage = "";

    private static int numberOfAINextPhaseSayings = 0;
    private static boolean prepareForAIChaining = false;

    public ObjectOfChange giveObjectForThisSingleString(String change) {
        int belongingTurn = Integer.parseInt(JsonCreator.getResult("give my actual turn"));
        if (change.startsWith("*")) {
            if (change.contains("next phase")) {

                if (DuelView.isAreWePlayingWithAI() && change.contains("user2")) {
                    numberOfAINextPhaseSayings++;
                    if (numberOfAINextPhaseSayings % 6 == 0) {
                        return new ObjectOfChange(new Animation(DuelView.getSupremeKingEndingTurnString()), action.NOT_APPLICABLE);
                    }
                }
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.4));
                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().add(pauseTransition);
                PredefinedActionWithParallelTransition predefinedActionWithParallelTransition = new PredefinedActionWithParallelTransition(parallelTransition);
                return new ObjectOfChange(predefinedActionWithParallelTransition, action.NOT_APPLICABLE);
            } else {
                if (DuelView.isAreWePlayingWithAI() && change.contains("user2")) {
                    System.out.println("AI Action: change = " + change);
                    if (change.contains("yes")) {
                        prepareForAIChaining = true;
                    } else if (change.contains("no") && !change.contains("normal")) {
                        prepareForAIChaining = false;
                    } else if (change.contains("select")) {
                        String inputRegex = "\\*user2: (?<=\\s|^)(select[\\s]+(--|-)([\\S]+)(|[\\s]+(--|-)([\\S]+))(|[\\s]+([\\d]+))(?=\\*|$))\\*";
                        Matcher matcher = Utility.getCommandMatcher(change, inputRegex);
                        RowOfCardLocation rowOfCardLocation;
                        //  DuelController duelController = GameManager.getDuelControllerByIndex(token);
                        //  int fakeTurn = duelController.getFakeTurn();
                        if (Utility.isMatcherCorrectWithoutErrorPrinting(matcher)) {
                            System.out.println("Match successful");
                            System.out.println("match3 = " + matcher.group(3) + " match6 = " + matcher.group(6));
                            rowOfCardLocation = switchCaseInputToGetRowOfCardLocation(matcher.group(3), matcher.group(6), 1);
                            if (rowOfCardLocation == null) {
                                // return "invalid selection";
                            } else if (matcher.group(8) != null) {
                                int cardIndex = Integer.parseInt(matcher.group(8));
                                rowOfCardLocation = RowOfCardLocation.valueOf(rowOfCardLocation.toString().replaceAll("ALLY", "OPPONENT"));
                                cardIndex = Utility.changeYuGiOhIndexToArrayIndex(cardIndex, rowOfCardLocation);
                                CardLocation cardLocation;
                                CardView cardView;
                                if (rowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                                    cardLocation = new CardLocation(rowOfCardLocation, cardIndex + 1);
                                    cardView = DuelView.getControllerForView().getCardViewByCardLocation(cardLocation);
                                } else {
                                    cardLocation = new CardLocation(rowOfCardLocation, cardIndex);
                                    cardView = DuelView.getControllerForView().getCardViewByCardLocation(cardLocation);
                                }
                                if (cardView != null) {
                                    previousSelected = cardLocation;
                                    if (prepareForAIChaining) {
                                        prepareForAIChaining = false;
                                        if (Card.isCardASpell(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingSpellAlreadyInFieldString()), action.NOT_APPLICABLE);
                                        } else if (Card.isCardATrap(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingTrapString()), action.NOT_APPLICABLE);
                                        } else if (Card.isCardAMonster(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingMonsterEffectString()), action.NOT_APPLICABLE);
                                        }
                                    }
                                }
                            } else if (matcher.group(6) != null || matcher.group(6) == null &&
                                (rowOfCardLocation.equals(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE) || rowOfCardLocation.equals(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE))) {
                                CardLocation cardLocation = new CardLocation(rowOfCardLocation, 1);
                                CardView cardView = DuelView.getControllerForView().getCardViewByCardLocation(cardLocation);
                                if (cardView != null) {
                                    previousSelected = cardLocation;
                                    if (prepareForAIChaining) {
                                        prepareForAIChaining = false;
                                        if (Card.isCardASpell(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingSpellAlreadyInFieldString()), action.NOT_APPLICABLE);
                                        } else if (Card.isCardATrap(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingTrapString()), action.NOT_APPLICABLE);
                                        } else if (Card.isCardAMonster(cardView.getCard())) {
                                            return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingMonsterEffectString()), action.NOT_APPLICABLE);
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("MAATCH ERROR");
                        }
                    } else if (change.contains("set")) {
                        if (previousSelected != null) {
                            CardView cardView = DuelView.getControllerForView().getCardViewByCardLocation(previousSelected);
                            Card card = cardView.getCard();
                            System.out.println("Selected Card! Selected card! name !!!: " + card.getCardName());
                            if (Card.isCardAMonster(card)) {
                                return new ObjectOfChange(new Animation(DuelView.getSupremeKingSettingAMonsterString()), action.NOT_APPLICABLE);
                            } else {
                                return new ObjectOfChange(new Animation(DuelView.getSupremeKingSettingASpellString()), action.NOT_APPLICABLE);
                            }
                        }
                    } else if (change.contains("summon")) {
                        return new ObjectOfChange(new Animation(DuelView.getSupremeKingSummoningInAttackPositionString()), action.NOT_APPLICABLE);
                    } else if (change.contains("direct") && change.contains("attack")) {
                        return new ObjectOfChange(new Animation(DuelView.getSupremeKingAttackDirectString()), action.NOT_APPLICABLE);
                    } else if (change.contains("attack") && !change.contains("attacking")) {
                        return new ObjectOfChange(new Animation(DuelView.getSupremeKingAttackMonsterString()), action.NOT_APPLICABLE);
                    } else if (change.contains("activate effect")) {
                        if (previousSelected != null) {
                            RowOfCardLocation rowOfCardLocation = previousSelected.getRowOfCardLocation();
                            if (rowOfCardLocation.toString().contains("HAND")) {
                                return new ObjectOfChange(new Animation(DuelView.getSupremeKingActivatingSpellString()), action.NOT_APPLICABLE);
                            }
                        }
                    }
                }
                ParallelTransition parallelTransition = new ParallelTransition();
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.0001));
                parallelTransition.getChildren().add(pauseTransition);
                return new ObjectOfChange(parallelTransition, action.NOT_APPLICABLE);
            }
        } else if (change.startsWith("&")) {
            int turn = (belongingTurn == 1 ? Integer.parseInt(change.split(" ")[3]) : 3 - Integer.parseInt(change.split(" ")[3]));
            int increaseInHealth = Integer.parseInt(change.split(" ")[9]);
            System.out.println("TURN SI " + turn + " AND INCREASEINHEALTH IS " + increaseInHealth);
            int realIncreaseInHealthForAlly = 0;
            int realIncreaseInHealthForOpponent = 0;
            if (turn == 1) {
                realIncreaseInHealthForAlly += increaseInHealth;
            } else {
                realIncreaseInHealthForOpponent += increaseInHealth;
            }
            UpdateHealthPointsTransition allyTransition = DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition();
            allyTransition.setIncreaseInHealth(allyTransition.getIncreaseInHealth() + realIncreaseInHealthForAlly);
            UpdateHealthPointsTransition opponentTransition = DuelView.getOpponentHealthStatus().getUpdateHealthPointsTransition();
            opponentTransition.setIncreaseInHealth(opponentTransition.getIncreaseInHealth() + realIncreaseInHealthForOpponent);
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition());
            parallelTransition.getChildren().add(DuelView.getOpponentHealthStatus().getUpdateHealthPointsTransition());
            parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(true, realIncreaseInHealthForAlly,
                DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
            parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(false, realIncreaseInHealthForOpponent,
                DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
            if (realIncreaseInHealthForAlly != 0 || realIncreaseInHealthForOpponent != 0) {
                boolean bool = Boolean.parseBoolean(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).isAIPlaying()"));
                if (bool && realIncreaseInHealthForOpponent != 0) {
                    return new ObjectOfChange(
                        new AnimationAndParallelTransition(new Animation(DuelView.getSupremeKingReceivingDamageString()), parallelTransition),
                        action.HP_LOSS);
                } else {
                    return new ObjectOfChange(parallelTransition, action.HP_LOSS);
                }
            } else {
                return new ObjectOfChange(parallelTransition, action.NOT_APPLICABLE);
            }
        } else {
            String winloss = "(\\S+) won the game and the score is: (\\S+)";
            Pattern pattern = Pattern.compile(winloss);
            Matcher matcher = pattern.matcher(change);
            String token = DuelView.getToken();
            boolean gameHasEnded = false;
            if (matcher.find()) {
                report = matcher.group(0);
                gameHasEnded = true;
                boolean oneRound = true;
                int numberOfRounds = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getNumberOfRounds()"));
                if (numberOfRounds == 3) {
                    oneRound = false;
                }
                int currentRound = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getCurrentRound()"));
                System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
                String output = AdvancedCardMovingController.getReport();
                System.out.println("WinLoss: " + output);
                Animation animation = new Animation(DuelView.getSupremeKingWinning());
                animation.play();
                MediaPlayer mediaPlayer = animation.getMediaPlayer();
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        animation.adjustSizeToZero();
                        DuelView.getStage().close();
                        DuelView.getBackgroundMusic().setMute(true);
                        // if (!oneRound && (currentRound == 1 || currentRound == 2)) {
                        DuelView.endOneRoundOfDuel(report);
                        AdvancedCardMovingController.setReport("");
                    }
                });

            } else {
                winloss = "(\\S+) won the whole match with score: (\\S+)";
                pattern = Pattern.compile(winloss);
                matcher = pattern.matcher(change);
                if (matcher.find()) {
                    report = matcher.group(0);
                    gameHasEnded = true;
                    //System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
                    //   String output = AdvancedCardMovingController.getReport();
                    //    System.out.println("WinLoss: " + output);
                    Animation animation = new Animation(DuelView.getSupremeKingWinning());
                    animation.play();
                    MediaPlayer mediaPlayer = animation.getMediaPlayer();
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            animation.adjustSizeToZero();
                            DuelView.getStage().close();
                            DuelView.getBackgroundMusic().setMute(true);
                            // if (!oneRound && (currentRound == 1 || currentRound == 2)) {
                            DuelView.endGame(report);
                            //AdvancedCardMovingController.setReport("");
                        }
                    });
                    //DuelView.getStage().close();
                    //DuelView.endGame(matcher.group(0));
                    //    AdvancedCardMovingController.setReport("");
                }
                System.out.println("Look dude my string is " + change);
            }
            if (!gameHasEnded) {
                String[] subCommands = change.split(" ");
                int sideOfFinalDestination = (subCommands[9].equals("zone") ? 0 : Integer.parseInt(subCommands[9]));
                //
                int myActualTurn = Integer.parseInt(JsonCreator.getResult("give my actual turn"));
                if (myActualTurn == 2 && (sideOfFinalDestination == 1 || sideOfFinalDestination == 2)) {
                    sideOfFinalDestination = 3 - sideOfFinalDestination;
                }
                //
                if (subCommands[1].equals("UNKNOWN")) {
                    int turn = Integer.parseInt(JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getTurn()"));
                    if (turn == 1) {
                        DuelView.setXHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftX() +
                            DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() - 7);
                        DuelView.setYHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftY() +
                            DuelView.getBattleFieldView().getHeight() - 2 * CardView.getCardHeight() + 20);
                    } else {
                        DuelView.setXHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftX() + 40);
                        DuelView.setYHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftY() + 108);
                    }
                    System.out.println("cheating card name is " + newlyAddedCard.getCardName() + " going to sideOfFInalDestination = " + sideOfFinalDestination +
                        " right now turn = " + JsonCreator.getResult("GameManager.getDuelControllerByIndex(token).getTurn()"));
                    CardView cardView = new CardView(newlyAddedCard, true,
                        (turn == 1 ? RowOfCardLocation.ALLY_MONSTER_ZONE : RowOfCardLocation.OPPONENT_MONSTER_ZONE), duelView);
                    DuelView.getAllCards().getChildren().add(cardView);
                    return new ObjectOfChange(DuelView.getTransition().sendCardToHandZone(cardView, sideOfFinalDestination), action.NOT_APPLICABLE);
                }
                RowOfCardLocation initialRowOfCardLocation = RowOfCardLocation.valueOf(subCommands[1]);
                int initialSide = (initialRowOfCardLocation.toString().startsWith("ALLY") ? 1 : 2);
                //
                //int belongingTurn = Integer.parseInt(JsonCreator.getResult("give my actual turn"));
                if (myActualTurn == 2) {
                    initialSide = 3 - initialSide;
                    if (initialRowOfCardLocation.toString().startsWith("ALL")) {
                        initialRowOfCardLocation = RowOfCardLocation.valueOf(initialRowOfCardLocation.toString().replaceAll("ALLY", "OPPONENT"));
                    } else if (initialRowOfCardLocation.toString().startsWith("OPP")) {
                        initialRowOfCardLocation = RowOfCardLocation.valueOf(initialRowOfCardLocation.toString().replaceAll("OPPONENT", "ALLY"));
                    }
                }
                //
                int index = Integer.parseInt(subCommands[2]);
                if (initialRowOfCardLocation.equals(RowOfCardLocation.ALLY_GRAVEYARD_ZONE)) {
                    index = index + 1;
                }
                boolean staying = subCommands[5].equals("stayed");
                String finalDestination = subCommands[7];

                CardPosition finalCardPosition = (subCommands[14].equals("NO_CHANGE") ? CardPosition.NOT_APPLICABLE : CardPosition.valueOf(subCommands[14]));
                System.out.println("rowOfCardLocation = " + initialRowOfCardLocation + " index = " + index + " finalDestination = " +
                    finalDestination + " sideOfFinalDestination = " + sideOfFinalDestination + " finalCardPosition = " + finalCardPosition);
                CardView cardView = null;
                if ((initialRowOfCardLocation.toString().contains("MONSTER") || initialRowOfCardLocation.toString().contains("SPELL") &&
                    !initialRowOfCardLocation.toString().contains("SPELL_FIELD")) && belongingTurn == 2) {
                    cardView = DuelView.getControllerForView().getCardViewByCardLocation(new CardLocation(initialRowOfCardLocation, 6 - index));
                } else {
                    cardView = DuelView.getControllerForView().getCardViewByCardLocation(new CardLocation(initialRowOfCardLocation, index));
                }
                if (cardView == null) {
                    System.out.println("\nYOU ARE DOOMED SEVERELY BECAUSE CARDVIEW IS NULL\n");
                }
                if (finalDestination.equals("monster")) {
                    //changing card positions not considered
                    if (finalCardPosition.equals(CardPosition.FACE_UP_ATTACK_POSITION)) {
                        if (initialRowOfCardLocation.toString().contains("MONSTER")) {
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForFlipSummoningOrChangingCardPositionSoThatFinallyIsFaceUpAttackPosition(cardView), (cardView.isCanBeSeen() ? action.NOT_APPLICABLE : action.SUMMON));
                        } else {
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView, CardPosition.FACE_UP_ATTACK_POSITION, sideOfFinalDestination, initialSide), action.SUMMON);
                        }
                    } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForSettingMonsterCard(cardView, sideOfFinalDestination, initialSide), action.SET);
                    } else if (finalCardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                        if (initialRowOfCardLocation.toString().contains("MONSTER")) {
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForFlippingCardByOpponentOrChangingCardPositionSoThatFinallyIsFaceUpDefensePosition(cardView), action.NOT_APPLICABLE);
                        } else {
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView, CardPosition.FACE_UP_DEFENSE_POSITION, sideOfFinalDestination, initialSide), action.SUMMON);
                        }
                    }
                } else if (finalDestination.equals("spell")) {
                    if (staying) {
                        boolean isSpecial = isCardSpecialForActivating(cardView);
                        return new ObjectOfChange(DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION), 250, isSpecial),
                            (isSpecial ? action.FIELD_SPELL_CHANGE : action.ACTIVATE_EFFECT));
                    } else {
                        if (finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                            boolean isSpecial = isCardSpecialForActivating(cardView);
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForActivatingSpellTrapSuper(cardView, sideOfFinalDestination, initialSide, isSpecial), (isSpecial ? action.FIELD_SPELL_CHANGE : action.ACTIVATE_EFFECT));


                        } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_SPELL_SET_POSITION)) {
                            return new ObjectOfChange(DuelView.getTransition().applyTransitionForSettingSpellTrapCard(cardView, sideOfFinalDestination, initialSide), action.SET);
                        }
                    }
                } else if (finalDestination.equals("graveyard")) {
                    boolean isSpecial = isCardSpecialForGoingToGraveyard(cardView);
                    return new ObjectOfChange(DuelView.getTransition().applyTransitionForSendingCardToGraveyard(cardView, sideOfFinalDestination,
                        !(cardView.getCard().getCardType().equals(CardType.MONSTER) &&
                            (initialRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ||
                                initialRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE))), isSpecial),
                        (isSpecial ? action.FIELD_SPELL_CHANGE : action.NOT_APPLICABLE));
                } else if (finalDestination.equals("hand")) {
                    return new ObjectOfChange(DuelView.getTransition().sendCardToHandZone(cardView, sideOfFinalDestination), action.NOT_APPLICABLE);
                }
                return new ObjectOfChange(new ParallelTransition(), action.NOT_APPLICABLE);
            }
        }
        return null;
    }

    private boolean isCardSpecialForActivating(CardView cardView) {
        Card card = cardView.getCard();
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            if (spellCard.getSpellCardValue().equals(SpellCardValue.FIELD)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCardSpecialForGoingToGraveyard(CardView cardView) {
        Card card = cardView.getCard();
        if (Card.isCardASpell(card)) {
            SpellCard spellCard = (SpellCard) card;
            if (spellCard.getSpellCardValue().equals(SpellCardValue.FIELD) && cardView.isCanBeSeen()) {
                return true;
            }
        }
        return false;
    }

    private RowOfCardLocation switchCaseInputToGetRowOfCardLocation(String firstString, String secondString, int turn) {
        if (firstString == null) {
            firstString = "";
        }
        if (secondString == null) {
            secondString = "";
        }
        if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("monster") || secondString.equals("m"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if ((firstString.equals("monster") || firstString.equals("m")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_MONSTER_ZONE, turn);
        } else if ((firstString.equals("spell") || firstString.equals("s")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("spell") || secondString.equals("s"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_ZONE, turn);
        } else if ((firstString.equals("field") || firstString.equals("f")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("field") || secondString.equals("f"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("hand") || firstString.equals("h")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("hand") || secondString.equals("h"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_HAND_ZONE, turn);
        } else if ((firstString.equals("deck") || firstString.equals("d")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("deck") || secondString.equals("d"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_DECK_ZONE, turn);
        } else if ((firstString.equals("graveyard") || firstString.equals("g")) && (secondString.equals("opponent") || secondString.equals("o"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if ((firstString.equals("opponent") || firstString.equals("o")) && (secondString.equals("graveyard") || secondString.equals("g"))) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.OPPONENT_GRAVEYARD_ZONE, turn);
        } else if ((firstString.equals("monster") || firstString.equals("m")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_MONSTER_ZONE, turn);
        } else if ((firstString.equals("spell") || firstString.equals("s")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_ZONE, turn);
        } else if ((firstString.equals("field") || firstString.equals("f")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_SPELL_FIELD_ZONE, turn);
        } else if ((firstString.equals("hand") || firstString.equals("h")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_HAND_ZONE, turn);
        } else if ((firstString.equals("deck") || firstString.equals("d")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_DECK_ZONE, turn);
        } else if ((firstString.equals("graveyard") || firstString.equals("g")) && secondString.equals("")) {
            return Utility.considerTurnsForRowOfCardLocation(RowOfCardLocation.ALLY_GRAVEYARD_ZONE, turn);
        }
        return null;
    }


}

interface conductChange {
    void conductChange();
}

class ChangeConductor implements conductChange {
    ObjectOfChange objectOfChange;
    String information;
    int helpIndex;

    public ChangeConductor(String information, int helpIndex) {
        this.information = information;
        this.helpIndex = helpIndex;
    }

    @Override
    public void conductChange() {
        this.objectOfChange = DuelView.getAdvancedCardMovingController().giveObjectForThisSingleString(information);
        if (helpIndex != DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().size() - 1) {
            chainThisChangeConductorToYourself();
        } else if (!AdvancedCardMovingController.allyChainingMessage.equals("")) {
            if (objectOfChange.getObject() instanceof ParallelTransition) {
                ParallelTransition parallelTransition = ((ParallelTransition) objectOfChange.getObject());
                parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            } else if (objectOfChange.getObject() instanceof TroubleFlipTransition) {
                ScaleTransition troubleFlipTransition = ((TroubleFlipTransition) objectOfChange.getObject()).getStShowBack();
                troubleFlipTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            } else if (objectOfChange.getObject() instanceof PredefinedActionWithParallelTransition) {
                ParallelTransition parallelTransition = ((PredefinedActionWithParallelTransition) objectOfChange.getObject()).getParallelTransition();
                parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            } else if (objectOfChange.getObject() instanceof Animation) {
                MediaPlayer mediaPlayer = ((Animation) objectOfChange.getObject()).getMediaPlayer();
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        ((Animation) objectOfChange.getObject()).adjustSizeToZero();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            } else if (objectOfChange.getObject() instanceof AnimationAndParallelTransition) {
                ParallelTransition parallelTransition = ((AnimationAndParallelTransition) objectOfChange.getObject()).getParallelTransition();
                parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        ((AnimationAndParallelTransition) objectOfChange.getObject()).getAnimation().adjustSizeToZero();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            } else if (objectOfChange.getObject() instanceof TroubleFlipWithTranslateTransition) {
                PauseTransition pauseTransition = ((TroubleFlipWithTranslateTransition) objectOfChange.getObject()).getPauseTransition();
                pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DuelView.getShowOptionsToUser().doYouWantToAlert(AdvancedCardMovingController.allyChainingMessage);
                                AdvancedCardMovingController.allyChainingMessage = "";
                            }
                        });
                    }
                });
            }
        }
        AudioClip audioClip = objectOfChange.giveMediaPlayerBasedOnAction();
        if (objectOfChange.getObject() instanceof ParallelTransition) {
            ((ParallelTransition) objectOfChange.getObject()).play();
            if (audioClip != null) {
                audioClip.play();
            }
        } else if (objectOfChange.getObject() instanceof TroubleFlipTransition) {
            ((TroubleFlipTransition) objectOfChange.getObject()).getStHideFront().play();
            if (audioClip != null) {
                audioClip.play();
            }
        } else if (objectOfChange.getObject() instanceof PredefinedActionWithParallelTransition) {
            ((PredefinedActionWithParallelTransition) objectOfChange.getObject()).play();
            if (audioClip != null) {
                audioClip.play();
            }
        } else if (objectOfChange.getObject() instanceof Animation) {
            ((Animation) objectOfChange.getObject()).play();
        } else if (objectOfChange.getObject() instanceof AnimationAndParallelTransition) {
            ((AnimationAndParallelTransition) objectOfChange.getObject()).play();
            if (audioClip != null) {
                audioClip.play();
            }
        } else if (objectOfChange.getObject() instanceof TroubleFlipWithTranslateTransition) {
            ((TroubleFlipWithTranslateTransition) objectOfChange.getObject()).play();
            if (audioClip != null) {
                audioClip.play();
            }
        }
    }

    public void chainThisChangeConductorToYourself() {
        if (objectOfChange.getObject() instanceof ParallelTransition) {
            ParallelTransition parallelTransition = ((ParallelTransition) objectOfChange.getObject());
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        } else if (objectOfChange.getObject() instanceof TroubleFlipTransition) {
            ScaleTransition troubleFlipTransition = ((TroubleFlipTransition) objectOfChange.getObject()).getStShowBack();
            troubleFlipTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        } else if (objectOfChange.getObject() instanceof PredefinedActionWithParallelTransition) {
            ParallelTransition parallelTransition = ((PredefinedActionWithParallelTransition) objectOfChange.getObject()).getParallelTransition();
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        } else if (objectOfChange.getObject() instanceof Animation) {
            MediaPlayer mediaPlayer = ((Animation) objectOfChange.getObject()).getMediaPlayer();
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    ((Animation) objectOfChange.getObject()).adjustSizeToZero();
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        } else if (objectOfChange.getObject() instanceof AnimationAndParallelTransition) {
            ParallelTransition parallelTransition = ((AnimationAndParallelTransition) objectOfChange.getObject()).getParallelTransition();
            parallelTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ((AnimationAndParallelTransition) objectOfChange.getObject()).getAnimation().adjustSizeToZero();
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        } else if (objectOfChange.getObject() instanceof TroubleFlipWithTranslateTransition) {
            PauseTransition pauseTransition = ((TroubleFlipWithTranslateTransition) objectOfChange.getObject()).getPauseTransition();
            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    DuelView.getAdvancedCardMovingController().getAllChangeConductorsObjects().get(helpIndex + 1).conductChange();
                }
            });
        }
    }
}

class ObjectOfChange {
    Object object;
    action action;

    public ObjectOfChange(Object object, action action) {
        this.object = object;
        this.action = action;
    }

    public Object getObject() {
        return object;
    }

    public project.client.view.pooyaviewpackage.action getAction() {
        return action;
    }

    public AudioClip giveMediaPlayerBasedOnAction() {
        if (this.action.equals(project.client.view.pooyaviewpackage.action.SUMMON)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/summoning_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.client.view.pooyaviewpackage.action.ACTIVATE_EFFECT)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/activate_effect_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.7);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.client.view.pooyaviewpackage.action.SET)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/setting_card_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.client.view.pooyaviewpackage.action.HP_LOSS)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/hp_decrease_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.client.view.pooyaviewpackage.action.FIELD_SPELL_CHANGE)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/field_spell_change_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        }
        return null;
    }
}

enum action {
    SUMMON,
    SET,
    ACTIVATE_EFFECT,
    HP_LOSS,
    FIELD_SPELL_CHANGE,
    NOT_APPLICABLE,
}


class PredefinedActionWithParallelTransition {
    ParallelTransition parallelTransition;

    public PredefinedActionWithParallelTransition(ParallelTransition parallelTransition) {
        this.parallelTransition = parallelTransition;
    }

    public void play() {
        //predefined
        GamePhaseButton.updateAllGamePhaseButtonsOnce();
        // i think i should put if here that goes through end phase draw phase ... as well
        parallelTransition.play();
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }
}

class AnimationAndParallelTransition {
    ParallelTransition parallelTransition;
    Animation animation;

    public AnimationAndParallelTransition(Animation animation, ParallelTransition parallelTransition) {
        this.animation = animation;
        this.parallelTransition = parallelTransition;
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void play() {
        animation.play();
        animation.getMediaPlayer().setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                parallelTransition.play();
            }
        });
    }
}
