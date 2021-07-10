package project.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import project.controller.duel.PreliminaryPackage.DuelStarter;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.model.cardData.SpellCardData.SpellCard;
import project.model.cardData.SpellCardData.SpellCardValue;
import project.model.modelsforview.CardView;
import project.model.modelsforview.GamePhaseButton;

import java.awt.*;
import java.awt.image.PixelInterleavedSampleModel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedCardMovingController {
    ArrayList<ChangeConductor> allChangeConductorsObjects;
    private static String report = "";
    private DuelView duelView;
    private static Card newlyAddedCard;

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
        String string = DuelStarter.getGameManager().getWholeReportToClient();
        if (!string.isBlank()) {
            System.out.println("advanceForwardBattleField is called with input =\n" + string);
            String winloss = "(\\S+) won the game and the score is: (\\S+) - (\\S+)";
            Pattern pattern = Pattern.compile(winloss);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                report = matcher.group(0);
                boolean oneRound = true;
                if (GameManager.getDuelControllerByIndex(0).getNumberOfRounds() == 3) {
                    oneRound = false;
                }
                int currentRound = GameManager.getDuelControllerByIndex(0).getCurrentRound();
                System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
                String output = AdvancedCardMovingController.getReport();
                System.out.println("WinLoss: " + output);
                DuelView.getStage().close();
                // if (!oneRound && (currentRound == 1 || currentRound == 2)) {
                DuelView.endOneRoundOfDuel(matcher.group(0));
                AdvancedCardMovingController.setReport("");
            } else {
                winloss = "(\\S+) won the whole match with score: (\\S+) - (\\S+)";
                pattern = Pattern.compile(winloss);
                matcher = pattern.matcher(string);
                if (matcher.find()) {
                    report = matcher.group(0);
                    //System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
                    //   String output = AdvancedCardMovingController.getReport();
                    //    System.out.println("WinLoss: " + output);
                    DuelView.getStage().close();
                    DuelView.endGame(matcher.group(0));
                    //    AdvancedCardMovingController.setReport("");
                } else {
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
//            ArrayList<Object> giveMeObjects = improveForwardBattleFieldItShouldGiveArrayListOfAllElements(changeConductorsInStringForm.get(i));
//            for (int j = 0; j < giveMeObjects.size(); j++) {
//                allChangeConductorsObjects.add(new ChangeConductor(giveMeObjects.get(j)));
//            }
                    }
//        for (int i = 0; i < allChangeConductorsObjects.size() - 1; i++) {
//            allChangeConductorsObjects.get(i).chainThisChangeConductorToYourself(allChangeConductorsObjects.get(i + 1));
//        }
                    allChangeConductorsObjects.get(0).conductChange();
                    DuelStarter.getGameManager().clearWholeReportToClient();
                }
            }

        }
    }

    public ObjectOfChange giveObjectForThisSingleString(String change) {
        if (change.startsWith("*")) {
            if (change.contains("next phase")) {
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.4));
                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().add(pauseTransition);
                PredefinedActionWithParallelTransition predefinedActionWithParallelTransition = new PredefinedActionWithParallelTransition(parallelTransition);
                return new ObjectOfChange(predefinedActionWithParallelTransition, action.NOT_APPLICABLE);
            } else {
                ParallelTransition parallelTransition = new ParallelTransition();
                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.0001));
                parallelTransition.getChildren().add(pauseTransition);
                return new ObjectOfChange(parallelTransition, action.NOT_APPLICABLE);
            }
        } else if (change.startsWith("&")) {
            int turn = Integer.parseInt(change.split(" ")[3]);
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
                return new ObjectOfChange(parallelTransition, action.HP_LOSS);
            } else {
                return new ObjectOfChange(parallelTransition, action.NOT_APPLICABLE);
            }
        } else {
            System.out.println("Look dude my string is " + change);
            String[] subCommands = change.split(" ");
            int sideOfFinalDestination = (subCommands[9].equals("zone") ? 0 : Integer.parseInt(subCommands[9]));
            if (subCommands[1].equals("UNKNOWN")) {
                int turn = GameManager.getDuelControllerByIndex(0).getTurn();
                if (turn == 1){
                    DuelView.setXHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftX() +
                        DuelView.getBattleFieldView().getWidth() - CardView.getCardWidth() - 7);
                    DuelView.setYHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftY() +
                        DuelView.getBattleFieldView().getHeight() - 2 * CardView.getCardHeight() + 20);
                } else {
                    DuelView.setXHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftX()+40);
                    DuelView.setYHelperForCardViewConstructor(DuelView.getBattleFieldView().getUpperLeftY()+108);
                }
                System.out.println("cheating card name is "+newlyAddedCard.getCardName()+" going to sideOfFInalDestination = "+sideOfFinalDestination+
                    " right now turn = "+GameManager.getDuelControllerByIndex(0).getTurn());
                CardView cardView = new CardView(newlyAddedCard, true,
                    (turn == 1 ? RowOfCardLocation.ALLY_MONSTER_ZONE : RowOfCardLocation.OPPONENT_MONSTER_ZONE), duelView);
                DuelView.getAllCards().getChildren().add(cardView);
                return new ObjectOfChange(DuelView.getTransition().sendCardToHandZone(cardView, sideOfFinalDestination), action.NOT_APPLICABLE);
            }
            RowOfCardLocation initialRowOfCardLocation = RowOfCardLocation.valueOf(subCommands[1]);
            int initialSide = (initialRowOfCardLocation.toString().startsWith("ALLY") ? 1 : 2);
            int index = Integer.parseInt(subCommands[2]);
            boolean staying = subCommands[5].equals("stayed");
            String finalDestination = subCommands[7];

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
        }
//        else {
//            if (!AdvancedCardMovingController.getReport().equals("")) {
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        boolean oneRound = true;
//                        if (GameManager.getDuelControllerByIndex(0).getNumberOfRounds() == 3) {
//                            oneRound = false;
//                        }
//                        int currentRound = GameManager.getDuelControllerByIndex(0).getCurrentRound();
//                        System.out.println("Was this one round " + oneRound + " currentRound = " + currentRound);
//                        String output = AdvancedCardMovingController.getReport();
//                        System.out.println("WinLoss: " + output);
//                        DuelView.getStage().close();
//                        if (!oneRound && (currentRound == 1 || currentRound == 2)) {
//                            DuelView.endOneRoundOfDuel(output);
//                        } else {
//                            DuelView.endGame(output);
//                        }
//                        AdvancedCardMovingController.setReport("");
//                    }
//                });
//            }
//        }
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

    public project.view.pooyaviewpackage.action getAction() {
        return action;
    }

    public AudioClip giveMediaPlayerBasedOnAction() {
        if (this.action.equals(project.view.pooyaviewpackage.action.SUMMON)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/summoning_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.view.pooyaviewpackage.action.ACTIVATE_EFFECT)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/activate_effect_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.7);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.view.pooyaviewpackage.action.SET)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/setting_card_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.view.pooyaviewpackage.action.HP_LOSS)) {
            if (!DuelView.isIsGameMute()) {
                String bip = "src/main/resources/project/ingameicons/music/hp_decrease_music.mp3";
                Media hit = new Media(Paths.get(bip).toUri().toString());
                AudioClip mediaPlayer = new AudioClip(hit.getSource());
                mediaPlayer.setVolume(0.6);
                return mediaPlayer;
            }
        } else if (this.action.equals(project.view.pooyaviewpackage.action.FIELD_SPELL_CHANGE)) {
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
        parallelTransition.play();
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }
}
