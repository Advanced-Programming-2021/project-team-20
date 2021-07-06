package project.view.pooyaviewpackage;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import project.controller.duel.PreliminaryPackage.GameManager;
import project.model.cardData.General.*;
import project.model.modelsforview.CardView;

import java.nio.file.Paths;
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

    ArrayList<ChangeConductor> allChangeConductorsObjects;

    public ArrayList<ChangeConductor> getAllChangeConductorsObjects() {
        return allChangeConductorsObjects;
    }

    public void advanceForwardBattleField() {
        String string = GameManager.getDuelControllerByIndex(0).getWholeReportToClient();
        System.out.println("advanceForwardBattleField is called with input =\n" + string);
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
                firstTimeOccurringUserSaying = false;
            } else if (miniStrings[i].startsWith("*")) {
                //changeConductorsInStringForm.get(helpIndex).add(new ArrayList<>());
                changeConductorsInStringForm.add(bulkOfStrings);
                bulkOfStrings = new ArrayList<>();
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
        GameManager.getDuelControllerByIndex(0).clearWholeReportToClient();
    }


    public ArrayList<Object> improveForwardBattleFieldItShouldGiveArrayListOfAllElements(ArrayList<String> changes) {
        System.out.println("uououpss changes is size = 0 " + (changes.size() == 0));
        if (changes.size() != 0) {
            ArrayList<String> commandsForChangesInBattleFiend = new ArrayList<>();
            ArrayList<String> commandsForChangesInHealth = new ArrayList<>();
            for (int i = 0; i < changes.size(); i++) {
                if (changes.get(i).startsWith("&")) {
                    commandsForChangesInHealth.add(changes.get(i));
                } else {
                    commandsForChangesInBattleFiend.add(changes.get(i));
                }
            }
            ArrayList<Object> output = new ArrayList<>();
            for (int i = 0; i < commandsForChangesInBattleFiend.size(); i++) {
                output.add(giveObjectForThisSingleString(commandsForChangesInBattleFiend.get(i)));
            }
            for (int i = 0; i < commandsForChangesInHealth.size(); i++) {
                output.add(giveObjectForThisSingleString(commandsForChangesInHealth.get(i)));
            }
            return output;
        }
        return null;
//        String string = GameManager.getDuelControllerByIndex(0).getSuperAlmightyChangesString();
//        String anotherString = GameManager.getDuelControllerByIndex(0).getChangesInLifePointsToBeGivenToClient();
//        System.out.println("all in all\n" + string + "\n\n");
//        if (!string.equals("")) {
//            String[] commands = string.split("\n");
//            Object[] allActions = new Object[commands.length + 1];
//            prepareOneElementOfAllActions(allActions, 0, commands);
//            GameManager.getDuelControllerByIndex(0).clearSuperAlmightyString();
//        } else if (!anotherString.equals("")) {
//            Object[] allActions = new Object[1];
//            prepareOneElementOfAllActions(allActions, 0, null);
//            GameManager.getDuelControllerByIndex(0).clearSuperAlmightyString();
//        }
    }

    public ObjectOfChange giveObjectForThisSingleString(String change) {
        if (change.startsWith("&")) {
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
            //parallelTransition.getChildren().add();
            parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(true, realIncreaseInHealthForAlly,
                DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
            parallelTransition.getChildren().add(DuelView.getTransition().applyTransitionForHealthBar(false, realIncreaseInHealthForOpponent,
                DuelView.getAllyHealthStatus().getUpdateHealthPointsTransition().getPreviousHealth()));
            return new ObjectOfChange(parallelTransition, action.HP_LOSS);
        } else {
            System.out.println("Look dude my string is " + change);
            String[] subCommands = change.split(" ");
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
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForFlipSummoning(cardView), action.SUMMON);
                    } else {
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView), action.SUMMON);
                    }
                } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_MONSTER_SET_POSITION)) {
                    return new ObjectOfChange(DuelView.getTransition().applyTransitionForSettingMonsterCard(cardView), action.SET);
                } else if (finalCardPosition.equals(CardPosition.FACE_UP_DEFENSE_POSITION)) {
                    if (initialRowOfCardLocation.toString().contains("MONSTER")) {
                        return new ObjectOfChange(DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, true, 250), action.NOT_APPLICABLE);
                    } else {
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForSummoningMonsterCard(cardView), action.SUMMON);
                    }
                }
            } else if (finalDestination.equals("spell")) {
                if (staying) {
                    return new ObjectOfChange(DuelView.getTransition().flipCardBackAndForthConsideringCardImage(cardView, finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION), 250), action.ACTIVATE_EFFECT);
                } else {
                    if (finalCardPosition.equals(CardPosition.FACE_UP_ACTIVATED_POSITION)) {
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForActivatingSpellTrapSuper(cardView), action.ACTIVATE_EFFECT);
                    } else if (finalCardPosition.equals(CardPosition.FACE_DOWN_SPELL_SET_POSITION)) {
                        return new ObjectOfChange(DuelView.getTransition().applyTransitionForSettingSpellTrapCard(cardView), action.SET);
                    }
                }
            } else if (finalDestination.equals("graveyard")) {
                return new ObjectOfChange(DuelView.getTransition().applyTransitionForSendingCardToGraveyard(cardView, sideOfFinalDestination,
                    !(cardView.getCard().getCardType().equals(CardType.MONSTER) &&
                        (initialRowOfCardLocation.equals(RowOfCardLocation.ALLY_MONSTER_ZONE) ||
                            initialRowOfCardLocation.equals(RowOfCardLocation.OPPONENT_MONSTER_ZONE)))), action.NOT_APPLICABLE);
            } else if (finalDestination.equals("hand")) {
                return new ObjectOfChange(DuelView.getTransition().sendCardToHandZone(cardView, sideOfFinalDestination), action.NOT_APPLICABLE);
            }
            return new ObjectOfChange(new ParallelTransition(), action.NOT_APPLICABLE);
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
            ScaleTransition troubleFlipTransition = ((TroubleFlipTransition) objectOfChange.getObject()).getStHideFront();
            troubleFlipTransition.setOnFinished(new EventHandler<ActionEvent>() {
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
